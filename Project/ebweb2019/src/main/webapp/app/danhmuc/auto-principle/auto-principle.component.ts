import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { AutoPrincipleService } from './auto-principle.service';
import { IAccountList } from 'app/shared/model/account-list.model';
import { IType } from 'app/shared/model/type.model';
import { TypeService } from 'app/entities/type';
import { IBank } from 'app/shared/model/bank.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-auto-principle',
    templateUrl: './auto-principle.component.html',
    styleUrls: ['./auto-principle.component.css']
})
export class AutoPrincipleComponent extends BaseComponent implements OnInit, OnDestroy {
    currentAccount: any;
    autoPrinciples: IAutoPrinciple[];
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
    selectedRow: IAutoPrinciple;
    types: IType[];
    ROLE_DinhKhoanTuDong_Them = ROLE.DanhMucDinhKhoanTuDong_Them;
    ROLE_DanhMucDinhKhoanTuDong_Sua = ROLE.DanhMucDinhKhoanTuDong_Sua;
    ROLE_DanhMucDinhKhoanTuDong_Xoa = ROLE.DanhMucDinhKhoanTuDong_Xoa;
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
        private autoPrincipleService: AutoPrincipleService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private typeService: TypeService
    ) {
        super();
        this.itemsPerPage = 20;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.typeService.getAllTypes().subscribe((res: HttpResponse<IType[]>) => {
            this.types = res.body.sort((a, b) => a.typeName.localeCompare(b.typeName));
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
        this.autoPrincipleService.getAutoPrinciplesByCompanyID().subscribe((res: HttpResponse<IAutoPrinciple[]>) => {
            this.autoPrinciples = res.body;
            this.objects = res.body;
            if (this.dataSession && this.dataSession.rowNum) {
                this.selectedRows.push(this.autoPrinciples[this.dataSession.rowNum]);
                this.selectedRow = this.autoPrinciples[this.dataSession.rowNum];
            } else {
                this.selectedRows.push(this.autoPrinciples[0]);
                this.selectedRow = this.autoPrinciples[0];
            }
        });
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/auto-principle'], {
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
            '/auto-principle',
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
        this.registerChangeInAutoPrinciples();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IAutoPrinciple) {
        return item.id;
    }

    registerChangeInAutoPrinciples() {
        this.eventSubscriber = this.eventManager.subscribe('autoPrincipleListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateAutoPrinciples(data: IAutoPrinciple[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.autoPrinciples = data;
        this.selectedRow = this.autoPrinciples[0];
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    doubleClickRow(any?) {
        this.edit();
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucDinhKhoanTuDong_Xem])
    edit() {
        event.preventDefault();
        this.dataSession.page = this.page;
        this.dataSession.itemsPerPage = this.itemsPerPage;
        this.dataSession.totalItems = this.totalItems;
        this.dataSession.rowNum = this.autoPrinciples.indexOf(this.selectedRow);
        // sort
        this.dataSession.predicate = this.predicate;
        this.dataSession.reverse = this.reverse;
        sessionStorage.setItem('dataSession', JSON.stringify(this.dataSession));
        if (this.selectedRow.id) {
            this.router.navigate(['./auto-principle', this.selectedRow.id, 'edit']);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucDinhKhoanTuDong_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRow.id) {
            this.router.navigate(['/auto-principle', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
        }
    }

    getTypeByTypeID(typeID: number) {
        if (this.types) {
            const type = this.types.find(n => n.id === typeID);
            if (type !== undefined && type !== null) {
                return type.typeName;
            } else {
                return '';
            }
        }
    }

    onSelect(select: IAutoPrinciple) {
        this.selectedRow = select;
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucDinhKhoanTuDong_Them])
    addNew($event) {
        event.preventDefault();
        this.router.navigate(['/auto-principle/new']);
    }
}
