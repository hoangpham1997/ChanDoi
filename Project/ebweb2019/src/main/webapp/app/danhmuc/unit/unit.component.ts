import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IUnit } from 'app/shared/model/unit.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { UnitService } from './unit.service';
import { IAccountList } from 'app/shared/model/account-list.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { ROLE } from 'app/role.constants';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';
import { DeleteMultipleLinesComponent } from 'app/shared/modal/delete-multiple-lines/delete-multiple-lines.component';
import { ToastrService } from 'ngx-toastr';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { TranslateService } from '@ngx-translate/core';
import { DeleteMultipleLinesUnitComponent } from 'app/shared/modal/delete-multiple-line-unit/delete-multiple-lines-unit.component';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-unit',
    templateUrl: './unit.component.html',
    styleUrls: ['./unit.component.css']
})
export class UnitComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('deletePopup') deletePopup;
    currentAccount: any;
    units: IUnit[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    unitName: string;
    selectedRow: IUnit;
    modalRef: NgbModalRef;
    modalRefMess: NgbModalRef;
    ROLE_DanhMucDonViTinh_Them = ROLE.DanhMucDonViTinh_Them;
    ROLE_DanhMucDonViTinh_Sua = ROLE.DanhMucDonViTinh_Sua;
    ROLE_DanhMucDonViTinh_Xoa = ROLE.DanhMucDonViTinh_Xoa;

    constructor(
        private unitService: UnitService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private toastr: ToastrService,
        private refModalService: RefModalService,
        private translate: TranslateService,
        private modalService: NgbModal
    ) {
        super();
        this.itemsPerPage = 20;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {
        this.unitService
            .pageableUnit({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IBudgetItem[]>) => this.paginateUnit(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/unit'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/unit',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInUnits();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IUnit) {
        return item.id;
    }

    registerChangeInUnits() {
        this.eventSubscriber = this.eventManager.subscribe('unitListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateUnits(data: IUnit[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.units = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    exportPdf(isDownload) {
        this.unitService
            .getCustomerReport({
                id: this.units[0].id,
                typeID: 0
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
                    const name = 'ten_bao_cao.pdf';
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
    }

    exportExcel() {
        this.unitService.exportExcel({}).subscribe(response => {
            const file = new Blob([response.body], {
                type: 'application/vnd.ms-excel'
            });
            const fileURL = window.URL.createObjectURL(file);
            const contentDispositionHeader = response.headers.get('Content-Disposition');
            const fileName = contentDispositionHeader
                .split(';')[1]
                .trim()
                .split('=')[1];
            const a = document.createElement('a');
            document.body.appendChild(a);
            a.href = fileURL;
            a.download = fileName.replace(/"/g, '');
            a.click();
        });
    }

    edit() {
        event.preventDefault();
        if (this.selectedRow.id) {
            this.router.navigate(['./unit', this.selectedRow.id, 'edit']);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucDonViTinh_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.modalRef = this.modalService.open(this.deletePopup, { size: 'lg', backdrop: 'static' });
        } else {
            this.router.navigate(['/unit', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
        }
    }

    onSelect(select: IUnit) {
        event.preventDefault();
        this.selectedRow = select;
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    selectedItemPerPage() {
        this.loadAll();
    }

    doubleClickRow(any?, any2?) {
        super.doubleClickRow(any, any2);
    }

    private paginateUnit(data: IUnit[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.units = data;
        this.objects = data;
        this.selectedRow = this.units[0];
    }

    addNew($event) {
        event.preventDefault();
        this.router.navigate(['/unit/new']);
    }

    confirmDeleteList() {
        this.unitService.deleteByListIDUnit(this.selectedRows.map(n => n.id)).subscribe((res: HttpResponse<HandlingResult>) => {
            if (res.body.countSuccessVouchers > 0) {
                this.loadAll();
            }
            if (res.body.countFailVouchers === 0) {
                this.toastr.success(this.translate.instant('ebwebApp.bank.deleteSuccess'));
            } else {
                const lstFailView = [];
                res.body.listIDFail.forEach(n => {
                    const sa = this.units.find(m => m.id === n);
                    if (sa) {
                        const viewNo: ViewVoucherNo = {};
                        viewNo.unitName = sa.unitName;
                        viewNo.reasonFail = this.translate.instant('ebwebApp.sAOrder.delete.deleteFail');
                        lstFailView.push(viewNo);
                    }
                });
                res.body.listFail = lstFailView;
                const lstHideColumn = ['unitName'];
                this.modalRefMess = this.refModalService.open(
                    res.body,
                    DeleteMultipleLinesUnitComponent,
                    lstHideColumn,
                    false,
                    null,
                    null,
                    null,
                    null,
                    null,
                    true
                );
            }
        });
        this.modalRef.close();
    }
}
