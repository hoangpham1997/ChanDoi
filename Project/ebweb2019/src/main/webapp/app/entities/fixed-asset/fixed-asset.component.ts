import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IFixedAsset } from 'app/shared/model/fixed-asset.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { FixedAssetService } from './fixed-asset.service';
import { ICreditCard } from 'app/shared/model/credit-card.model';
import { IAccountList } from 'app/shared/model/account-list.model';
import { IFixedAssetAllocation } from 'app/shared/model/fixed-asset-allocation.model';
import { FixedAssetAllocationService } from 'app/entities/fixed-asset-allocation';

@Component({
    selector: 'eb-fixed-asset',
    templateUrl: './fixed-asset.component.html',
    styleUrls: ['./fixed-asset.component.css']
})
export class FixedAssetComponent implements OnInit, OnDestroy {
    private _fixedAsset: IFixedAsset;
    currentAccount: any;
    fixedAssets: IFixedAsset[];
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
    fixedAssetAllocations: IFixedAssetAllocation[];
    selectedRow?: IFixedAsset;

    constructor(
        private fixedAssetService: FixedAssetService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private fixedAssetAllocationService: FixedAssetAllocationService
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
        this.fixedAssetService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IFixedAsset[]>) => this.paginateFixedAssets(res.body, res.headers),
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
        this.router.navigate(['/fixed-asset'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    get fixedAsset() {
        return this._fixedAsset;
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/fixed-asset',
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
        this.registerChangeInFixedAssets();
        // if(this.selectedRow.id!==undefined)
        // { this.fixedAssetAllocationService.query().subscribe(
        //     (res: HttpResponse<IFixedAssetAllocation[]>) => {
        //         this.fixedAssetAllocations = res.body.filter(n=>n.fixedassetID===this.selectedRow.id);
        //     },
        //     (res: HttpErrorResponse) => this.onError(res.message)
        // );
        // }
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IFixedAsset) {
        return item.id;
    }

    trackByFixedAssetAllocation(index: number, item: IFixedAssetAllocation) {
        return item.id;
    }

    trackByFixedAsset(index: number, item: IFixedAsset) {
        return item.id;
    }

    registerChangeInFixedAssets() {
        this.eventSubscriber = this.eventManager.subscribe('fixedAssetListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateFixedAssets(data: IFixedAsset[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.fixedAssets = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    onSelect(select: IFixedAsset) {
        this.selectedRow = select;
        this.fixedAssetAllocations = select.fixedAssetAllocation;
        console.log(`selectedTest1 = ${JSON.stringify(this.selectedRow)}`);
        // alert(`selectedTest1 = ${JSON.stringify(this.selectedTest1)}`);
    }
}
