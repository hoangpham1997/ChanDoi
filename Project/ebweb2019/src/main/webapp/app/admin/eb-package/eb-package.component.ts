import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { EbPackage, IEbPackage } from 'app/shared/model/eb-package.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE_ADMIN } from 'app/shared';
import { EbPackageService } from './eb-package.service';
import { EbPackageDeleteDialogComponent, UserMgmtDeleteDialogAdminComponent } from 'app/admin';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BaseComponent } from 'app/shared/base-component/base.component';

@Component({
    selector: 'eb-eb-package',
    templateUrl: './eb-package.component.html',
    styleUrls: ['./eb-package.component.css']
})
export class EbPackageComponent extends BaseComponent implements OnInit, OnDestroy {
    currentAccount: any;
    ebPackages: IEbPackage[];
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
    selectedRow: IEbPackage;

    constructor(
        private ebPackageService: EbPackageService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private modalService: NgbModal
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE_ADMIN;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {
        this.ebPackageService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IEbPackage[]>) => this.paginateEbPackages(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    transition() {
        this.router.navigate(['/eb-package'], {
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
            '/eb-package',
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
        this.registerChangeInEbPackages();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IEbPackage) {
        return item.id;
    }

    registerChangeInEbPackages() {
        this.eventSubscriber = this.eventManager.subscribe('ebPackageListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateEbPackages(data: IEbPackage[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.ebPackages = data;
        this.selectedRow = data[0];
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    addNew($event) {
        this.router.navigate(['/admin/eb-package/new']);
    }

    edit() {
        event.preventDefault();
        if (this.selectedRow.id) {
            this.router.navigate(['/admin/eb-package', this.selectedRow.id, 'edit']);
        }
    }

    delete() {
        // if(this.selectedRow.id) {
        //     this.router.navigate(['/admin/eb-package', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
        // }
        event.preventDefault();
        const modalRef = this.modalService.open(EbPackageDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.ebPackage = this.selectedRow;
        modalRef.result.then(
            result => {
                // Left blank intentionally, nothing to do here
            },
            reason => {
                // Left blank intentionally, nothing to do here
            }
        );
    }

    selectedItemPerPage() {
        this.loadAll();
    }

    onSelect(ebPackage: IEbPackage) {
        this.selectedRow = ebPackage;
    }
}
