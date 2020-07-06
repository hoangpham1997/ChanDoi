import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { Principal } from 'app/core';

import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { DataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { IaPublishInvoice } from 'app/shared/model/ia-publish-invoice.model';
import { IaPublishInvoiceService } from 'app/quan-ly-hoa-don/thong-bao-phat-hanh-hoa-don/ia-publish-invoice.service';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { ITEMS_PER_PAGE_QLHD } from 'app/shared';
import { ROLE } from 'app/role.constants';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'eb-don-mua-hang',
    templateUrl: './thong-bao-phat-hanh.component.html',
    styleUrls: ['./thong-bao-phat-hanh.component.css']
})
export class ThongBaoPhatHanhComponent extends BaseComponent implements OnInit, OnDestroy {
    currentAccount: any;
    iaReports: IaPublishInvoice[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    status: number;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    selectedRow: IaPublishInvoice;
    pageCount: any;
    index: number;
    dataSession: any;
    account: any;
    selectedID: string;
    TBPH = 'TBPH';
    CHUA_CO_HIEU_LUC = 0;
    DA_CO_HIEU_LUC = 1;

    ROLE_XEM = ROLE.TBPH_XEM;
    ROLE_THEM = ROLE.TBPH_THEM;
    ROLE_SUA = ROLE.TBPH_SUA;
    ROLE_XOA = ROLE.TBPH_XOA;
    ROLE_KETXUAT = ROLE.TBPH_KET_XUAT;
    ROLE_IN = ROLE.TBPH_IN;
    TYPE_THONG_BAO_PHAT_HANH_HDDT = 820;

    constructor(
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private refModalService: RefModalService,
        private eventManager: JhiEventManager,
        private iaPublishInvoiceService: IaPublishInvoiceService,
        public utilsService: UtilsService,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {
        super();
    }

    ngOnInit() {
        this.itemsPerPage = ITEMS_PER_PAGE_QLHD;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
        });
        this.registerChangeIniaPublish();
        this.registerExport();
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.getSessionData();
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('TBPH_DataSession'));
        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.pageCount = this.dataSession.pageCount;
            this.totalItems = this.dataSession.totalItems;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
            this.index = this.dataSession.index;
            this.transition();
        } else {
            this.search();
        }
        sessionStorage.removeItem('TBPH_DataSession');
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    exportPDF(isDownload, typeReports: number) {
        this.iaPublishInvoiceService
            .getCustomerReport({
                id: this.selectedRow.id,
                typeID: this.TYPE_THONG_BAO_PHAT_HANH_HDDT,
                typeReport: typeReports
            })
            .subscribe(response => {
                // this.showReport(response);
                const file = new Blob([response.body], { type: 'application/pdf' });
                const fileURL = window.URL.createObjectURL(file);
                if (isDownload) {
                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    const name = 'Thong_bao_phat_hanh_hddt.pdf';
                    link.setAttribute('download', name);
                    link.href = fileURL;
                    link.click();
                } else {
                    const contentDispositionHeader = response.headers.get('Content-Disposition');
                    const result = contentDispositionHeader
                        .split(';')[1]
                        .trim()
                        .split('=')[1];
                    const newWin = window.open(fileURL, '_blank');

                    // add a load listener to the window so that the title gets changed on page load
                    newWin.addEventListener('load', function() {
                        newWin.document.title = result.replace(/"/g, '');
                        // this.router.navigate(['/report/buy']);
                    });
                }
            });
        if (typeReports === 1) {
            this.toastr.success(
                this.translate.instant('ebwebApp.mBDeposit.printing') + this.translate.instant('ebwebApp.iaPublishInvoice.temp1') + '...',
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === 2) {
            this.toastr.success(
                this.translate.instant('ebwebApp.mBDeposit.printing') + this.translate.instant('ebwebApp.iaPublishInvoice.temp2') + '...',
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        }
    }

    search() {
        this.iaPublishInvoiceService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage
            })
            .subscribe(
                (res: HttpResponse<IaPublishInvoice[]>) => this.paginateiaReport(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    transition() {
        this.router.navigate(['/thong-bao-phat-hanh-hoa-don'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage
            }
        });
        this.search();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/thong-bao-phat-hanh-hoa-don',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.search();
    }

    doubleClickRow(iaPublishInvoice: IaPublishInvoice) {
        this.router.navigate(['/thong-bao-phat-hanh-hoa-don', iaPublishInvoice.id, 'edit']);
    }

    saveSearchData(id: string, index: number) {
        this.dataSession = new DataSessionStorage();
        this.dataSession.page = this.page;
        this.dataSession.itemsPerPage = this.itemsPerPage;
        this.dataSession.pageCount = this.pageCount;
        this.dataSession.totalItems = this.totalItems;
        this.dataSession.predicate = this.predicate;
        this.dataSession.reverse = this.reverse;
        this.dataSession.iaReportID = id;
        this.dataSession.index = index;
        sessionStorage.setItem('TBPH_DataSession', JSON.stringify(this.dataSession));
    }

    addNew($event = null) {
        this.router.navigate(['/thong-bao-phat-hanh-hoa-don/new']);
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeIniaPublish() {
        this.eventSubscriber = this.eventManager.subscribe('iaPublishInvoiceListModification', response => this.search());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateiaReport(data: IaPublishInvoice[], headers: HttpHeaders) {
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.iaReports = data;
        this.objects = data;
        this.index = this.index || 0;
        this.selectedRow = this.iaReports[0];
        if (this.iaReports && this.iaReports.length) {
            if (this.index < 0 || this.index > this.iaReports.length - 1) {
                this.selectedID = this.iaReports[0].id;
            } else {
                this.selectedID = this.iaReports[this.index].id;
            }
        }
        this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    loadAllForSearch() {
        this.page = 1;
        this.search();
    }

    exportExcel() {
        this.iaPublishInvoiceService.export('excel').subscribe(res => {
            const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
            const fileURL = URL.createObjectURL(blob);

            const link = document.createElement('a');
            document.body.appendChild(link);
            link.download = fileURL;
            link.setAttribute('style', 'display: none');
            const name = 'DS_TBPHHD.xls';
            link.setAttribute('download', name);
            link.href = fileURL;
            link.click();
        });
    }

    export() {
        this.iaPublishInvoiceService.export('pdf').subscribe(res => {
            this.refModalService.open(null, EbReportPdfPopupComponent, res, false, this.TBPH);
        });
    }

    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${this.TBPH}`, () => {
            this.exportExcel();
        });
    }

    clickRow(id: string, index: number) {
        this.selectedID = id;
        this.index = index;
    }

    delete() {
        event.preventDefault();
        this.router.navigate(['/thong-bao-phat-hanh-hoa-don', { outlets: { popup: this.selectedID + '/delete' } }]);
    }
}
