import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IFixedAssetCategory } from 'app/shared/model/fixed-asset-category.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { FixedAssetCategoryService } from './fixed-asset-category.service';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IContractState } from 'app/shared/model/contract-state.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IMBDepositDetails } from 'app/shared/model/mb-deposit-details.model';

@Component({
    selector: 'eb-fixed-asset-category',
    templateUrl: './fixed-asset-category.component.html',
    styleUrls: ['./fixed-asset-category.component.css']
})
export class FixedAssetCategoryComponent implements OnInit, OnDestroy {
    currentAccount: any;
    fixedAssetCategories: IFixedAssetCategory[];
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
    selectedRow: IFixedAssetCategory;

    constructor(
        private fixedAssetCategoryService: FixedAssetCategoryService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {
        this.fixedAssetCategoryService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IFixedAssetCategory[]>) => this.paginateFixedAssetCategories(res.body, res.headers),
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
        this.router.navigate(['/fixed-asset-category'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    onSelect(select: IFixedAssetCategory) {
        this.selectedRow = select;
        console.log(`selectedTest1 = ${JSON.stringify(this.selectedRow)}`);
        // alert(`selectedTest1 = ${JSON.stringify(this.selectedTest1)}`);
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/fixed-asset-category',
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
        this.registerChangeInFixedAssetCategories();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IFixedAssetCategory) {
        return item.id;
    }

    registerChangeInFixedAssetCategories() {
        this.eventSubscriber = this.eventManager.subscribe('fixedAssetCategoryListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateFixedAssetCategories(data: IFixedAssetCategory[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.fixedAssetCategories = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
