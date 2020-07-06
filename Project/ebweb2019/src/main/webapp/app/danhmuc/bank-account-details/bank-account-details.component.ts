import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { BankAccountDetailsService } from './bank-account-details.service';
import { IBank } from 'app/shared/model/bank.model';
import { IUnit } from 'app/shared/model/unit.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { SU_DUNG_DM_TK_NGan_Hang } from 'app/app.constants';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { TranslateService } from '@ngx-translate/core';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';
import { DeleteMultipleLinesUnitComponent } from 'app/shared/modal/delete-multiple-line-unit/delete-multiple-lines-unit.component';
import { DeleteMultipleLinesBankAccountDetailsComponent } from 'app/shared/modal/delete-multiple-line-bank-account-details/delete-multiple-lines-bank-account-details.component';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-bank-account-details',
    templateUrl: './bank-account-details.component.html',
    styleUrls: ['./bank-account-details.component.css']
})
export class BankAccountDetailsComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('deletePopup') deletePopup;
    currentAccount: any;
    bankAccountDetails: IBankAccountDetails[];
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
    selectedRow: IBankAccountDetails;
    isGetAllCompany: boolean;
    modalRef: NgbModalRef;
    modalRefMess: NgbModalRef;
    ROLE_DanhMucTaiKhoanNganHang_Them = ROLE.DanhMucTaiKhoanNganHang_Them;
    ROLE_DanhMucTaiKhoanNganHang_Sua = ROLE.DanhMucTaiKhoanNganHang_Sua;
    ROLE_DanhMucTaiKhoanNganHang_Xoa = ROLE.DanhMucTaiKhoanNganHang_Xoa;

    constructor(
        private bankAccountDetailsService: BankAccountDetailsService,
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
        this.bankAccountDetailsService
            .pageableBankAccountDetails({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IBudgetItem[]>) => this.paginateBankAccountDetails(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    doubleClickRow(any?, any2?) {
        super.doubleClickRow(any, any2);
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/bank-account-details'], {
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
            '/bank-account-details',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.isGetAllCompany = false;
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.isGetAllCompany = this.currentAccount.systemOption.find(x => x.code === SU_DUNG_DM_TK_NGan_Hang).data === '0';
            this.loadAll();
        });
        this.registerChangeInRepositories();
    }
    registerChangeInRepositories() {
        this.eventSubscriber = this.eventManager.subscribe('repositoryListModification', response => this.loadAll());
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IBankAccountDetails) {
        return item.id;
    }

    registerChangeInBankAccountDetails() {
        this.eventSubscriber = this.eventManager.subscribe('bankAccountDetailsListModification', response => this.loadAll());
    }
    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }
    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
    onSelect(select: IBankAccountDetails) {
        event.preventDefault();
        this.selectedRow = select;
    }
    edit() {
        event.preventDefault();
        if (this.selectedRow.id) {
            this.router.navigate(['./bank-account-details', this.selectedRow.id, 'edit']);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucTaiKhoanNganHang_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.modalRef = this.modalService.open(this.deletePopup, { size: 'lg', backdrop: 'static' });
        } else {
            this.router.navigate(['/bank-account-details', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
        }
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

    private paginateBankAccountDetails(data: IBankAccountDetails[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.bankAccountDetails = data;
        this.objects = data;
        this.selectedRow = this.bankAccountDetails[0];
    }
    addNew($event) {
        event.preventDefault();
        this.router.navigate(['/bank-account-details/new']);
    }

    confirmDeleteList() {
        this.bankAccountDetailsService
            .deleteByListIDUnit(this.selectedRows.map(n => n.id))
            .subscribe((res: HttpResponse<HandlingResult>) => {
                if (res.body.countSuccessVouchers > 0) {
                    this.loadAll();
                }
                if (res.body.countFailVouchers === 0) {
                    this.toastr.success(this.translate.instant('ebwebApp.bank.deleteSuccess'));
                } else {
                    const lstFailView = [];
                    res.body.listIDFail.forEach(n => {
                        const sa = this.bankAccountDetails.find(m => m.id === n);
                        if (sa) {
                            const viewNo: ViewVoucherNo = {};
                            viewNo.bankAccount = sa.bankAccount;
                            viewNo.reasonFail = this.translate.instant('ebwebApp.sAOrder.delete.deleteFail');
                            lstFailView.push(viewNo);
                        }
                    });
                    res.body.listFail = lstFailView;
                    const lstHideColumn = ['bankAccount'];
                    this.modalRefMess = this.refModalService.open(
                        res.body,
                        DeleteMultipleLinesBankAccountDetailsComponent,
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
