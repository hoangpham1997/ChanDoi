import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Currency, ICurrency } from 'app/shared/model/currency.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { CurrencyService } from './currency.service';
import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { IBank } from 'app/shared/model/bank.model';
import { ICreditCard } from 'app/shared/model/credit-card.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { ISearchVoucherBank } from 'app/shared/model/SearchVoucherBank';
import { ISearchVoucherCurrency } from 'app/shared/model/SearchVoucherCurrency';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-currency',
    templateUrl: './currency.component.html',
    styleUrls: ['./currency.component.css']
})
export class CurrencyComponent extends BaseComponent implements OnInit, OnDestroy {
    currentAccount: any;
    currencies: ICurrency[];
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
    index: any;
    previousPage: any;
    reverse: any;
    pageCount: any;
    isShowSearch: boolean;
    selectedRow: any;
    ROLE_LoaiTien_Them = ROLE.DanhMucLoaiTien_Them;
    ROLE_LoaiTien_Sua = ROLE.DanhMucLoaiTien_Sua;
    ROLE_LoaiTien_Xoa = ROLE.DanhMucLoaiTien_Xoa;
    search: ISearchVoucherCurrency;
    rowNum: number;

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
        private currencyService: CurrencyService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
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
        this.currencyService
            .pageableCurrency({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<ICurrency[]>) => {
                    this.paginateCurrency(res.body, res.headers);
                    if (this.currencies.length > 0) {
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
        this.router.navigate(['/currency'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    selectedItemPerPage() {
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/currency',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    toggleSearch($event) {
        $event.preventDefault();
        this.isShowSearch = !this.isShowSearch;
    }

    ngOnInit() {
        this.search = new class implements ISearchVoucherCurrency {
            currencyCode: string;
            currencyName: string;
            exchangeRate: string;
            keySearch: string;
        }();
        this.dataSession = new class implements IDataSessionStorage {
            creditCardType: string;
            itemsPerPage: number;
            ownerCard: string;
            page: number;
            pageCount: number;
            predicate: number;
            reverse: number;
            rowNum: number;
            searchVoucher: string;
            totalItems: number;
        }();
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInCurrency();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ICurrency) {
        return item.id;
    }

    registerChangeInCurrency() {
        this.eventSubscriber = this.eventManager.subscribe('currencyListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateCurrency(data: ICurrency[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.currencies = data;
        this.objects = data;
        if (this.dataSession && this.dataSession.rowNum) {
            this.selectedRows.push(this.currencies[this.dataSession.rowNum]);
            this.selectedRow = this.currencies[this.dataSession.rowNum];
        } else {
            this.selectedRows.push(this.currencies[0]);
            this.selectedRow = this.currencies[0];
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    doubleClickRow(any?) {
        this.edit();
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucLoaiTien_Xem])
    edit() {
        event.preventDefault();
        this.dataSession.page = this.page;
        this.dataSession.itemsPerPage = this.itemsPerPage;
        this.dataSession.totalItems = this.totalItems;
        this.dataSession.rowNum = this.currencies.indexOf(this.selectedRow);
        // sort
        this.dataSession.predicate = this.predicate;
        this.dataSession.reverse = this.reverse;
        sessionStorage.setItem('dataSession', JSON.stringify(this.dataSession));
        if (this.selectedRow.id) {
            this.router.navigate(['./currency', this.selectedRow.id, 'edit']);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucLoaiTien_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRow.id) {
            this.router.navigate(['/currency', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
        }
    }

    onSelect(select: ICurrency) {
        this.selectedRow = select;
        this.index = this.currencies.indexOf(this.selectedRow);
        this.rowNum = this.getRowNumberOfRecord(this.page, this.index);
    }
    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    getRowNumberOfRecord(page: number, index: number): number {
        if (page > 0 && index !== -1) {
            return (page - 1) * this.itemsPerPage + index + 1;
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucLoaiTien_Them])
    addNew($event) {
        event.preventDefault();
        this.router.navigate(['/currency/new']);
    }

    searchAll() {
        this.currencyService
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                searchVoucherCurrency: JSON.stringify(this.search)
            })
            .subscribe(
                (res: HttpResponse<ICurrency[]>) => this.paginateCurrency(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    resetSearch() {
        this.search = {};
        this.search.currencyCode = null;
        this.search.currencyName = null;
        this.search.exchangeRate = null;
        this.search.keySearch = null;
        sessionStorage.removeItem('sessionSearchVoucher');
        this.loadAll();
    }

    loadAllForSearch() {
        this.page = 1;
        this.searchAll();
    }
}
