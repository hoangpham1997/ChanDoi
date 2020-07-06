import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IAccountDefault } from 'app/shared/model/account-default.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { AccountDefaultService } from './account-default.service';
import { IAccountDefaultCategory } from 'app/shared/model/account-default-category.model';
import { IAccountList } from 'app/shared/model/account-list.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-account-default',
    templateUrl: './account-default.component.html',
    styleUrls: ['./account-default.component.css']
})
export class AccountDefaultComponent extends BaseComponent implements OnInit, OnDestroy {
    currentAccount: any;
    accountDefaults: IAccountDefault[];
    accountDefaultCategories: IAccountDefaultCategory[];
    allAccountDefaultCategories: IAccountDefaultCategory[];
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
    selectedRow: IAccountDefaultCategory;
    searchTypeName = '';
    searchDebitAccount = '';
    searchCreditAccount = '';
    searchDefaultDebitAccount = '';
    searchDefaultCreditAccount = '';
    ROLE_TaiKhoanNgamDinh_Xem = ROLE.TaiKhoanNgamDinh_Xem;
    ROLE_TaiKhoanNgamDinh_Sua = ROLE.TaiKhoanNgamDinh_Sua;

    buttonDeleteTranslate = 'ebwebApp.mBDeposit.toolTip.delete';
    buttonAddTranslate = 'ebwebApp.mBDeposit.toolTip.add';
    buttonEditTranslate = 'ebwebApp.mBDeposit.toolTip.edit';
    buttonRecordTranslate = 'ebwebApp.mBDeposit.toolTip.record';
    buttonUnRecordTranslate = 'ebwebApp.mBDeposit.toolTip.unrecord';
    buttonExportTranslate = 'ebwebApp.mBDeposit.toolTip.export';
    buttonSearchTranslate = 'ebwebApp.mBDeposit.toolTip.search';

    constructor(
        private accountDefaultService: AccountDefaultService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {
        this.accountDefaultService.getAccountDefaultsByCompanyID().subscribe(
            (res: HttpResponse<IAccountDefault[]>) => {
                this.accountDefaultCategories = res.body;
                this.allAccountDefaultCategories = res.body;
                for (let i = 0; i < this.allAccountDefaultCategories.length; i++) {
                    if (!this.allAccountDefaultCategories[i].typeName) {
                        this.allAccountDefaultCategories[i].typeName = '';
                    }
                    if (!this.allAccountDefaultCategories[i].debitAccount) {
                        this.allAccountDefaultCategories[i].debitAccount = '';
                    }
                    if (!this.allAccountDefaultCategories[i].creditAccount) {
                        this.allAccountDefaultCategories[i].creditAccount = '';
                    }
                    if (!this.allAccountDefaultCategories[i].defaultDebitAccount) {
                        this.allAccountDefaultCategories[i].defaultDebitAccount = '';
                    }
                    if (!this.allAccountDefaultCategories[i].defaultCreditAccount) {
                        this.allAccountDefaultCategories[i].defaultCreditAccount = '';
                    }
                }
                this.objects = res.body;
                if (sessionStorage.getItem('currentRow')) {
                    const currentRow: number = JSON.parse(sessionStorage.getItem('currentRow'));
                    this.selectedRows.push(this.accountDefaultCategories[currentRow]);
                    this.selectedRow = this.accountDefaultCategories[currentRow];
                } else {
                    this.selectedRows.push(this.accountDefaultCategories[0]);
                    this.selectedRow = this.accountDefaultCategories[0];
                }
                sessionStorage.removeItem('currentRow');
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
        this.router.navigate(['/account-default'], {
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
            '/account-default',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.accountDefaultCategories = [];
        this.allAccountDefaultCategories = [];
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInAccountDefaults();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IAccountDefault) {
        return item.id;
    }

    registerChangeInAccountDefaults() {
        this.eventSubscriber = this.eventManager.subscribe('accountDefaultListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        result.push('typeID');
        return result;
    }

    private paginateAccountDefaults(data: IAccountDefault[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.accountDefaults = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    onSelect(select: IAccountDefaultCategory) {
        this.selectedRow = select;
    }

    doubleClickRow(any?) {
        this.edit();
    }

    @ebAuth(['ROLE_ADMIN', ROLE.TaiKhoanKetChuyen_Xem])
    edit() {
        event.preventDefault();
        sessionStorage.setItem('currentRow', JSON.stringify(this.accountDefaultCategories.indexOf(this.selectedRow)));
        if (this.selectedRow.typeID) {
            sessionStorage.setItem('typeID', JSON.stringify(this.selectedRow.typeID));
            this.router.navigate(['./account-default', this.selectedRow.typeID, 'edit']);
        }
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    changeSearch() {
        this.accountDefaultCategories = this.allAccountDefaultCategories.filter(
            a =>
                a.typeName.toUpperCase().includes(this.searchTypeName.toUpperCase()) &&
                a.debitAccount.toUpperCase().includes(this.searchDebitAccount.toUpperCase()) &&
                a.creditAccount.toUpperCase().includes(this.searchCreditAccount.toUpperCase()) &&
                a.defaultDebitAccount.toUpperCase().includes(this.searchDefaultDebitAccount.toUpperCase()) &&
                a.defaultCreditAccount.toUpperCase().includes(this.searchDefaultCreditAccount.toUpperCase())
        );
    }
}
