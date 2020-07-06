import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ICreditCard } from 'app/shared/model/credit-card.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { CreditCardService } from './credit-card.service';
import { IBank } from 'app/shared/model/bank.model';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { IType } from 'app/shared/model/type.model';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { BankService } from 'app/danhmuc/bank';
import { SU_DUNG_DM_THE_TIN_DUNG } from 'app/app.constants';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';
import { BankResultComponent } from 'app/shared/modal/bank/bank-result.component';
import { MaterialGoodsResultComponent } from 'app/shared/modal/material-goods-1/bank-result.component';
import { ToastrService } from 'ngx-toastr';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { TranslateService } from '@ngx-translate/core';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-credit-card',
    templateUrl: './credit-card.component.html',
    styleUrls: ['./credit-card.component.css']
})
export class CreditCardComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('deletePopup') deletePopup;
    currentAccount: any;
    creditCards: ICreditCard[];
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
    creditform: FormGroup;
    selectedRow: ICreditCard;
    banks: IBank[];
    modalRef: NgbModalRef;
    isGetAllCompany: boolean;
    modalRefMess: NgbModalRef;
    ROLE_TheTinDung_Them = ROLE.DanhMucTheTinDung_Them;
    ROLE_TheTinDung_Sua = ROLE.DanhMucTheTinDung_Sua;
    ROLE_TheTinDung_Xoa = ROLE.DanhMucTheTinDung_Xoa;
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
        private creditCardService: CreditCardService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private cr: FormBuilder,
        private bankService: BankService,
        private toastr: ToastrService,
        private modalService: NgbModal,
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
        this.creditCardService
            .pageableCreditCard1({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                isGetAllCompany: this.isGetAllCompany
            })
            .subscribe(
                (res: HttpResponse<ICreditCard[]>) => {
                    this.paginateCreditCard(res.body, res.headers);
                    if (this.creditCards.length > 0) {
                        this.onSelect(this.selectedRow);
                    }
                },
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
        this.router.navigate(['/credit-card'], {
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
        this.banks = [];
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.bankService.getBanks().subscribe((res: HttpResponse<IBank[]>) => {
            this.banks = res.body;
        });
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.isGetAllCompany = this.currentAccount.systemOption.find(x => x.code === SU_DUNG_DM_THE_TIN_DUNG).data === '0';
            this.loadAll();
        });
        this.registerChangeIncreditCard();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IBank) {
        return item.id;
    }

    registerChangeIncreditCard() {
        this.eventSubscriber = this.eventManager.subscribe('creditCardListModification', response => this.loadAll());
    }

    doubleClickRow(any?) {
        this.edit();
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucTheTinDung_Xem])
    edit() {
        event.preventDefault();
        this.dataSession.page = this.page;
        this.dataSession.itemsPerPage = this.itemsPerPage;
        this.dataSession.totalItems = this.totalItems;
        this.dataSession.rowNum = this.creditCards.indexOf(this.selectedRow);
        // sort
        this.dataSession.predicate = this.predicate;
        this.dataSession.reverse = this.reverse;
        sessionStorage.setItem('dataSession', JSON.stringify(this.dataSession));
        if (this.selectedRow.id) {
            this.router.navigate(['./credit-card', this.selectedRow.id, 'edit']);
        }
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateCreditCard(data: ICreditCard[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.creditCards = data;
        this.objects = data;
        if (this.dataSession && this.dataSession.rowNum) {
            this.selectedRows.push(this.creditCards[this.dataSession.rowNum]);
            this.selectedRow = this.creditCards[this.dataSession.rowNum];
        } else {
            this.selectedRows.push(this.creditCards[0]);
            this.selectedRow = this.creditCards[0];
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    onSelect(select: IBank) {
        this.selectedRow = select;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucTheTinDung_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.modalRef = this.modalService.open(this.deletePopup, { size: 'lg', backdrop: 'static' });
        } else {
            this.router.navigate(['/credit-card', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
        }
    }

    getBankCode(bankIDIssueCard) {
        if (this.banks && bankIDIssueCard) {
            const bank = this.banks.find(n => n.id === bankIDIssueCard);
            if (bank && bank.bankCode) {
                return bank.bankCode;
            }
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

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucTheTinDung_Them])
    addNew($event) {
        event.preventDefault();
        this.router.navigate(['/credit-card/new']);
    }

    confirmDeleteList() {
        this.creditCardService.deleteCreditCardID(this.selectedRows.map(n => n.id)).subscribe((res: HttpResponse<HandlingResult>) => {
            if (res.body.countSuccessVouchers > 0) {
                this.loadAll();
            }
            if (res.body.countFailVouchers === 0) {
                this.toastr.success(this.translate.instant('ebwebApp.bank.deleteSuccess'));
            } else {
                const lstFailView = [];
                res.body.listIDFail.forEach(n => {
                    const sa = this.creditCards.find(m => m.id === n);
                    if (sa) {
                        const viewNo: ViewVoucherNo = {};
                        viewNo.creditCardNumber = sa.creditCardNumber;
                        viewNo.reasonFail = this.translate.instant('ebwebApp.sAOrder.delete.hasRef');
                        lstFailView.push(viewNo);
                    }
                });
                res.body.listFail = lstFailView;
                const lstHideColumn = ['creditCardNumber'];
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
}
