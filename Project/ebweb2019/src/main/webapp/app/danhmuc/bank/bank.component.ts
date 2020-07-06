import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IBank } from 'app/shared/model/bank.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { BankService } from './bank.service';
import { IUnit } from 'app/shared/model/unit.model';
import { IRepository } from 'app/shared/model/repository.model';
import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { IAccountList } from 'app/shared/model/account-list.model';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { ISearchVoucherMaterialGoods } from 'app/shared/model/SearchVoucherMaterialGoods';
import { ISearchVoucherBank } from 'app/shared/model/SearchVoucherBank';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import * as moment from 'moment';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { BankResultComponent } from 'app/shared/modal/bank/bank-result.component';
import { MaterialGoodsResultComponent } from 'app/shared/modal/material-goods-1/bank-result.component';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-bank',
    templateUrl: './bank.component.html',
    styleUrls: ['./bank.component.css']
})
export class BankComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('deletePopup') deletePopup;
    currentAccount: any;
    banks: IBank[];
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
    bankCode: string;
    bankName: any;
    isShowSearch: boolean;
    selectedRow: IBank;
    ROLE_NganHang_Them = ROLE.DanhMucNganHang_Them;
    ROLE_NganHang_Sua = ROLE.DanhMucNganHang_Sua;
    ROLE_NganHang_Xoa = ROLE.DanhMucNganHang_Xoa;
    description: any;
    keySearch: any;
    search: ISearchVoucherBank;
    modalRef: NgbModalRef;
    modalRefMess: NgbModalRef;
    dataSession: IDataSessionStorage = new class implements IDataSessionStorage {
        accountingObjectName: string;
        itemsPerPage: number;
        page: number;
        pageCount: number;
        predicate: number;
        reverse: number;
        rowNum: number;
        totalItems: number;
    }();

    constructor(
        private bankService: BankService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private modalService: NgbModal,
        private toastr: ToastrService,
        private refModalService: RefModalService,
        private translate: TranslateService
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
        if (sessionStorage.getItem('dataSession')) {
            this.dataSession = JSON.parse(sessionStorage.getItem('dataSession'));
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
            this.previousPage = this.dataSession.page;
            sessionStorage.removeItem('dataSession');
        }
        this.bankService
            .pageableBank({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IBank[]>) => {
                    this.paginateBank(res.body, res.headers);
                    if (this.banks.length > 0) {
                        this.onSelect(this.selectedRow);
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    searchAll() {
        this.bankService
            .searchAllBank({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                searchVoucherBank: JSON.stringify(this.search)
            })
            .subscribe(
                (res: HttpResponse<IBank[]>) => this.paginateBank(res.body, res.headers),
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
        this.dataSession.rowNum = 0;
        this.router.navigate(['/bank'], {
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
            '/bank',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.search = new class implements ISearchVoucherBank {
            bankName: string;
            bankCode: string;
            description: string;
            keySearch: string;
        }();
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInBanks();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IBank) {
        return item.id;
    }

    registerChangeInBanks() {
        this.eventSubscriber = this.eventManager.subscribe('bankListModification', response => this.loadAll());
    }

    doubleClickRow(any?) {
        this.edit();
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucNganHang_Xem])
    edit() {
        event.preventDefault();
        this.dataSession.page = this.page;
        this.dataSession.itemsPerPage = this.itemsPerPage;
        this.dataSession.totalItems = this.totalItems;
        this.dataSession.rowNum = this.banks.indexOf(this.selectedRow);
        // sort
        this.dataSession.predicate = this.predicate;
        this.dataSession.reverse = this.reverse;
        sessionStorage.setItem('dataSession', JSON.stringify(this.dataSession));
        if (this.selectedRow.id) {
            this.router.navigate(['./bank', this.selectedRow.id, 'edit']);
        }
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateBank(data: IBank[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.banks = data;
        this.objects = data;
        if (this.dataSession && this.dataSession.rowNum) {
            this.selectedRows.push(this.banks[this.dataSession.rowNum]);
            this.selectedRow = this.banks[this.dataSession.rowNum];
        } else {
            this.selectedRows.push(this.banks[0]);
            this.selectedRow = this.banks[0];
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    onSelect(select: IBank) {
        this.selectedRow = select;
        console.log(`selectedTest1 = ${JSON.stringify(this.selectedRow)}`);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucNganHang_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.modalRef = this.modalService.open(this.deletePopup, { size: 'lg', backdrop: 'static' });
        } else {
            this.router.navigate(['/bank', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
        }
    }

    confirmDeleteList() {
        this.bankService.deleteByListID(this.selectedRows.map(n => n.id)).subscribe((res: HttpResponse<HandlingResult>) => {
            if (res.body.countSuccessVouchers > 0) {
                this.loadAll();
            }
            if (res.body.countFailVouchers === 0) {
                this.toastr.success(this.translate.instant('ebwebApp.bank.deleteSuccess'));
            } else {
                const lstFailView = [];
                res.body.listIDFail.forEach(n => {
                    const sa = this.banks.find(m => m.id === n);
                    if (sa) {
                        const viewNo: ViewVoucherNo = {};
                        viewNo.bankCode = sa.bankCode;
                        viewNo.bankName = sa.bankName;
                        viewNo.reasonFail = this.translate.instant('ebwebApp.sAOrder.delete.hasRef');
                        lstFailView.push(viewNo);
                    }
                });
                res.body.listFail = lstFailView;
                const lstHideColumn = ['bankCode', 'bankName'];
                this.modalRefMess = this.refModalService.open(
                    res.body,
                    BankResultComponent,
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

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucNganHang_Them])
    addNew($event) {
        event.preventDefault();
        this.router.navigate(['/bank/new']);
    }

    toggleSearch($event) {
        $event.preventDefault();
        this.isShowSearch = !this.isShowSearch;
    }

    resetSearch() {
        this.search = {};
        this.search.bankCode = null;
        this.search.bankName = null;
        this.search.description = null;
        this.search.keySearch = null;
        sessionStorage.removeItem('sessionSearchVoucher');
        this.loadAll();
    }

    loadAllForSearch() {
        this.page = 1;
        this.searchAll();
    }
}
