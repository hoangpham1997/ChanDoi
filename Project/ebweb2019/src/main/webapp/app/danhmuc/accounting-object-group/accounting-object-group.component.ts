import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IAccountingObjectGroup } from 'app/shared/model/accounting-object-group.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { AccountingObjectGroupService } from './accounting-object-group.service';
import { IUnit } from 'app/shared/model/unit.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { ITreeMaterialGoodsCategory } from 'app/shared/model/material-goods-category-tree.model';
import { ITreeAccountingObjectGroup } from 'app/shared/model/organization-unit-tree/accounting-object-group-tree.model';
import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { TreeGridComponent } from 'app/shared/tree-grid/tree-grid.component';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-accounting-object-group',
    styleUrls: ['./accounting-object-group.component.css'],
    templateUrl: './accounting-object-group.component.html'
})
export class AccountingObjectGroupComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('child') child: TreeGridComponent;
    currentAccount: any;
    accountingObjectGroups: IAccountingObjectGroup[];
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
    accountingobjectgroupcode: string;
    selectedAOG: IAccountingObjectGroup;
    selectedRow: ITreeAccountingObjectGroup;
    treeAccountingObjectGroup: ITreeAccountingObjectGroup[];
    listTreeAccountingObjectGroup: ITreeAccountingObjectGroup[];
    flatTreeAccountingObjectGroup: ITreeAccountingObjectGroup[];
    navigateForm: string;
    listTHead: string[];
    listKey: any[];
    ROLE_DanhMucKhachHangVaNhaCungCap_Them = ROLE.DanhMucKhachHangVaNhaCungCap_Them;
    ROLE_DanhMucKhachHangVaNhaCungCap_Sua = ROLE.DanhMucKhachHangVaNhaCungCap_Sua;
    ROLE_DanhMucKhachHangVaNhaCungCap_Xoa = ROLE.DanhMucKhachHangVaNhaCungCap_Xoa;

    constructor(
        private accountingObjectGroupService: AccountingObjectGroupService,
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

    loadAllForSearch() {
        this.accountingObjectGroupService
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                accountingobjectgroupcode: this.accountingobjectgroupcode ? this.accountingobjectgroupcode : ''
            })
            .subscribe(
                (res: HttpResponse<IAccountingObjectGroup[]>) => this.paginateAccountingObjectGroups(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }
    loadAllForSearchResult() {
        this.page = 1;
        this.loadAllForSearch();
    }
    loadAll() {
        this.accountingObjectGroupService.getLoadAllAccountingObjectGroup().subscribe((res: HttpResponse<IAccountingObjectGroup[]>) => {
            this.listTreeAccountingObjectGroup = [];
            this.flatTreeAccountingObjectGroup = [];
            this.accountingObjectGroups = res.body;
            const listAccountingObjectGroup = this.accountingObjectGroups.filter(s => s.parentId == null || s.parentId === undefined);
            for (let i = 0; i < listAccountingObjectGroup.length; i++) {
                this.listTreeAccountingObjectGroup.push(Object.assign({}));
                this.listTreeAccountingObjectGroup[i].parent = listAccountingObjectGroup[i];
            }
            this.tree(this.listTreeAccountingObjectGroup);
            this.cutTree(this.listTreeAccountingObjectGroup);
            this.objects = this.flatTreeAccountingObjectGroup;
            this.selectedRow = this.flatTreeAccountingObjectGroup[0];
        });
    }

    cutTree(tree: ITreeAccountingObjectGroup[]) {
        tree.forEach(branch => {
            this.flatTreeAccountingObjectGroup.push(branch);
            if (branch.children && branch.children.length > 0) {
                this.cutTree(branch.children);
            }
        });
    }

    tree(accountingObjectGroup: ITreeAccountingObjectGroup[]) {
        for (let i = 0; i < accountingObjectGroup.length; i++) {
            const newList = this.accountingObjectGroups.filter(s => s.parentId === accountingObjectGroup[i].parent.id);
            accountingObjectGroup[i].children = [];
            for (let j = 0; j < newList.length; j++) {
                accountingObjectGroup[i].children.push(Object.assign({}));
                accountingObjectGroup[i].children[j].parent = newList[j];
            }
            if (accountingObjectGroup[i].children && accountingObjectGroup[i].children.length > 0) {
                this.tree(accountingObjectGroup[i].children);
            }
        }
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/accounting-object-group'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAllForSearch();
        // this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/accounting-object-group',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.treeAccountingObjectGroup = [];
        this.listTHead = [];
        this.listKey = [];
        this.navigateForm = './accounting-object-group';
        this.listTHead.push('ebwebApp.accountingObjectGroup.accountingObjectGroupCode');
        this.listTHead.push('ebwebApp.accountingObjectGroup.accountingObjectGroupName');
        this.listTHead.push('ebwebApp.accountingObjectGroup.description');
        this.listTHead.push('ebwebApp.accountingObjectGroup.isActive');
        this.listKey.push({ key: 'accountingObjectGroupCode', type: 1 });
        this.listKey.push({ key: 'accountingObjectGroupName', type: 1 });
        this.listKey.push({ key: 'description', type: 1 });
        this.listKey.push({ key: 'isActive', type: 1 });
        this.accountingObjectGroups = [];
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.onSelect(this.selectedRow);
        this.registerChangeInAccountingObjectGroups();
        this.registerSelectedRow();
    }

    onSelect(select: ITreeAccountingObjectGroup) {
        this.child.onSelect(select);
    }

    registerSelectedRow() {
        this.eventSubscriber = this.eventManager.subscribe('selectRow', response => {
            this.selectedRow = response.data;
        });
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IAccountingObjectGroup) {
        return item.id;
    }

    registerChangeInAccountingObjectGroups() {
        this.eventSubscriber = this.eventManager.subscribe('accountingObjectGroupListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }
    RowSelected(u: any) {
        this.selectedAOG = u; // declare variable in component.
        console.log(this.selectedAOG);
    }

    private paginateAccountingObjectGroups(data: IAccountingObjectGroup[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.accountingObjectGroups = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
    edit() {
        event.preventDefault();
        if (this.selectedRow.parent.id) {
            this.router.navigate(['./accounting-object-group', this.selectedRow.parent.id, 'edit']);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucKhachHangVaNhaCungCap_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRow.parent.id) {
            this.router.navigate(['/accounting-object-group', { outlets: { popup: this.selectedRow.parent.id + '/delete' } }]);
        }
    }
    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }
    addNew($event) {
        event.preventDefault();
        this.router.navigate(['/accounting-object-group/new']);
    }
}
