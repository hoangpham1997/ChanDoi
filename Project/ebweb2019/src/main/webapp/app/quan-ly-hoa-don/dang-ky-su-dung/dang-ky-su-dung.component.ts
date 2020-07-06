import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE, ITEMS_PER_PAGE_QLHD } from 'app/shared';

import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { IARegisterInvoice } from 'app/shared/model/ia-register-invoice.model';
import { IARegisterInvoiceService } from 'app/quan-ly-hoa-don/dang-ky-su-dung/ia-register-invoice.service';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { DataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { ROLE } from 'app/role.constants';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IPporder } from 'app/shared/model/pporder.model';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';

@Component({
    selector: 'eb-don-mua-hang',
    templateUrl: './dang-ky-su-dung.component.html',
    styleUrls: ['./dang-ky-su-dung.component.css']
})
export class DangKySuDungComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('popUpMultiDelete') popUpMultiDelete: TemplateRef<any>;
    currentAccount: any;
    iaRegisterInvoices: IARegisterInvoice[];
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
    selectedRow: IARegisterInvoice;
    pageCount: any;
    index: number;
    dataSession: any;
    account: any;
    selectedID: string;
    DKSD = 'DKSD';
    modalRef: NgbModalRef;

    ROLE_XEM = ROLE.DKSD_XEM;
    ROLE_THEM = ROLE.DKSD_THEM;
    ROLE_SUA = ROLE.DKSD_SUA;
    ROLE_XOA = ROLE.DKSD_XOA;
    ROLE_KETXUAT = ROLE.DKSD_KET_XUAT;

    constructor(
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private refModalService: RefModalService,
        private iaRegisterInvoiceService: IARegisterInvoiceService,
        private eventManager: JhiEventManager,
        public utilsService: UtilsService,
        private toastr: ToastrService,
        private translate: TranslateService,
        public activeModal: NgbActiveModal,
        private modalService: NgbModal
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
        this.registerChangeInIARegisterInvoice();
        this.registerExport();
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.getSessionData();
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('DKSD_DataSession'));
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
        sessionStorage.removeItem('DKSD_DataSession');
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    search() {
        this.iaRegisterInvoiceService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage
            })
            .subscribe(
                (res: HttpResponse<IARegisterInvoice[]>) => this.paginateiaReport(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    transition() {
        this.router.navigate(['/dang-ky-su-dung-hoa-don'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage
                // sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.search();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/dang-ky-su-dung-hoa-don',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.search();
    }

    doubleClickRow(iaRegisterInvoice: IARegisterInvoice) {
        this.router.navigate(['/dang-ky-su-dung-hoa-don', iaRegisterInvoice.id, 'edit']);
    }

    saveSearchData(id: string, index: number) {
        this.dataSession = new DataSessionStorage();
        this.dataSession.page = this.page;
        this.dataSession.itemsPerPage = this.itemsPerPage;
        this.dataSession.pageCount = this.pageCount;
        this.dataSession.totalItems = this.totalItems;
        this.dataSession.predicate = this.predicate;
        this.dataSession.reverse = this.reverse;
        this.dataSession.iaRegisterInvoiceID = id;
        this.dataSession.index = index;
        sessionStorage.setItem('DKSD_DataSession', JSON.stringify(this.dataSession));
    }

    addNew($event = null) {
        this.router.navigate(['/dang-ky-su-dung-hoa-don/new']);
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInIARegisterInvoice() {
        this.eventSubscriber = this.eventManager.subscribe('IARegisterInvoiceListModification', response => this.search());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateiaReport(data: IARegisterInvoice[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.iaRegisterInvoices = data;
        // Mũi tên lên xuống màn ds dữ liệu
        this.objects = data;
        // end
        this.index = this.index || 0;
        this.selectedRow = this.iaRegisterInvoices[0];
        if (this.iaRegisterInvoices && this.iaRegisterInvoices.length) {
            if (this.index < 0 || this.index > this.iaRegisterInvoices.length - 1) {
                this.selectedID = this.iaRegisterInvoices[0].id;
            } else {
                this.selectedID = this.iaRegisterInvoices[this.index].id;
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
        this.iaRegisterInvoiceService.export('excel').subscribe(res => {
            const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
            const fileURL = URL.createObjectURL(blob);
            const link = document.createElement('a');
            document.body.appendChild(link);
            link.download = fileURL;
            link.setAttribute('style', 'display: none');
            const name = 'DS_DKSD.xls';
            link.setAttribute('download', name);
            link.href = fileURL;
            link.click();
        });
    }

    export() {
        this.iaRegisterInvoiceService.export('pdf').subscribe(res => {
            this.refModalService.open(null, EbReportPdfPopupComponent, res, false, this.DKSD);
        });
    }

    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${this.DKSD}`, () => {
            this.exportExcel();
        });
    }

    clickRow(id: string, index: number) {
        this.selectedID = id;
        this.index = index;
    }

    delete() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            this.router.navigate(['/dang-ky-su-dung-hoa-don', { outlets: { popup: this.selectedID + '/delete' } }]);
        }
    }

    closePopUpDelete() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    continueDelete() {
        this.iaRegisterInvoiceService.multiDelete(this.selectedRows).subscribe(
            (res: HttpResponse<IPporder[]>) => {
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.selectedRows = [];
                this.loadAllForSearch();
                this.modalRef = this.refModalService.open(
                    res.body,
                    HandlingResultComponent,
                    null,
                    false,
                    null,
                    null,
                    null,
                    null,
                    null,
                    true
                );
                this.activeModal.close();
                // }
            },
            (res: HttpErrorResponse) => {
                if (res.error.errorKey === 'errorDeleteList') {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.mBDeposit.errorDeleteVoucherNo'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                }
                if (this.modalRef) {
                    this.modalRef.close();
                }
            }
        );
    }
}
