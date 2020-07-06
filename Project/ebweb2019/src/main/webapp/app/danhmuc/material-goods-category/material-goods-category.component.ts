import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { MaterialGoodsCategoryService } from './material-goods-category.service';
import { IBank } from 'app/shared/model/bank.model';
import { IUnit } from 'app/shared/model/unit.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { ITreeMaterialGoodsCategory } from 'app/shared/model/material-goods-category-tree.model';
import { IAccountList } from 'app/shared/model/account-list.model';
import { ITreeAccountList } from 'app/shared/model/account-list-tree.model';
import { ITreeStatisticsCode } from 'app/shared/model/statistics-code-tree.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { ITreeBudgetItem } from 'app/shared/model/budget-item-model-tree';
import { TranslateService } from '@ngx-translate/core';
import { TreeGridComponent } from 'app/shared/tree-grid/tree-grid.component';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-material-goods-category',
    templateUrl: './material-goods-category.component.html',
    styleUrls: ['./material-goods-category.component.css']
})
export class MaterialGoodsCategoryComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('child') child: TreeGridComponent;
    currentAccount: any;
    materialGoodsCategories: IMaterialGoodsCategory[];
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
    listTHead: string[];
    listKey: any[];
    navigateForm: string;
    listHead: string[];
    treeMaterialGoodsCategory: ITreeMaterialGoodsCategory[];
    listTreeMaterialGoodsCategory: ITreeMaterialGoodsCategory[];
    flatTreeMaterialGoodsCategory: ITreeMaterialGoodsCategory[];
    selectedRow: ITreeMaterialGoodsCategory;
    ROLE_DanhMucLoaiVatTuHangHoa_Them = ROLE.DanhMucLoaiVatTuHangHoa_Them;
    ROLE_DanhMucLoaiVatTuHangHoa_Sua = ROLE.DanhMucLoaiVatTuHangHoa_Sua;
    ROLE_DanhMucLoaiVatTuHangHoa_Xoa = ROLE.DanhMucLoaiVatTuHangHoa_Xoa;

    constructor(
        private materialGoodsCategoryService: MaterialGoodsCategoryService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
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
        this.materialGoodsCategoryService.getMaterialGoodsCategory().subscribe((res: HttpResponse<IMaterialGoodsCategory[]>) => {
            this.listTreeMaterialGoodsCategory = [];
            this.flatTreeMaterialGoodsCategory = [];
            this.materialGoodsCategories = res.body;
            const listMaterialGoodsCategory = this.materialGoodsCategories.filter(s => s.parentID == null || s.parentID === undefined);
            for (let i = 0; i < listMaterialGoodsCategory.length; i++) {
                this.listTreeMaterialGoodsCategory.push(Object.assign({}));
                this.listTreeMaterialGoodsCategory[i].parent = listMaterialGoodsCategory[i];
            }
            this.tree(this.listTreeMaterialGoodsCategory);
            this.cutTree(this.listTreeMaterialGoodsCategory);
            this.objects = this.flatTreeMaterialGoodsCategory;
            this.selectedRow = this.flatTreeMaterialGoodsCategory[0];
        });
    }

    cutTree(tree: ITreeMaterialGoodsCategory[]) {
        tree.forEach(branch => {
            this.flatTreeMaterialGoodsCategory.push(branch);
            if (branch.children && branch.children.length > 0) {
                this.cutTree(branch.children);
            }
        });
    }

    tree(materialGoodsCategory: ITreeMaterialGoodsCategory[]) {
        for (let i = 0; i < materialGoodsCategory.length; i++) {
            const newList = this.materialGoodsCategories.filter(s => s.parentID === materialGoodsCategory[i].parent.id);
            materialGoodsCategory[i].children = [];
            for (let j = 0; j < newList.length; j++) {
                materialGoodsCategory[i].children.push(Object.assign({}));
                materialGoodsCategory[i].children[j].parent = newList[j];
            }
            if (materialGoodsCategory[i].children && materialGoodsCategory[i].children.length > 0) {
                this.tree(materialGoodsCategory[i].children);
            }
        }
    }

    registerSelectedRow() {
        this.eventSubscriber = this.eventManager.subscribe('selectRow', response => {
            this.selectedRow = response.data;
        });
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/material-goods-category'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        // this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/material-goods-category',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        // this.loadAll();
    }

    ngOnInit() {
        this.treeMaterialGoodsCategory = [];
        this.listTHead = [];
        this.listKey = [];
        this.navigateForm = './material-goods-category';
        this.listTHead.push('ebwebApp.materialGoodsCategory.materialGoodsCategoryCode');
        this.listTHead.push('ebwebApp.materialGoodsCategory.materialGoodsCategoryName');
        this.listTHead.push('ebwebApp.materialGoodsCategory.isActive');
        this.listKey.push({ key: 'materialGoodsCategoryCode', type: 1 });
        this.listKey.push({ key: 'materialGoodsCategoryName', type: 1 });
        this.listKey.push({ key: 'isActive', type: 1 });
        this.materialGoodsCategories = [];
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.onSelect(this.selectedRow);
        this.registerChangeInMaterialGoodsCategories();
        this.registerSelectedRow();
    }

    onSelect(select: ITreeMaterialGoodsCategory) {
        this.child.onSelect(select);
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IMaterialGoodsCategory) {
        return item.id;
    }

    registerChangeInMaterialGoodsCategories() {
        this.eventSubscriber = this.eventManager.subscribe('materialGoodsCategoryListModification', response => this.loadAll());
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

    edit() {
        event.preventDefault();
        if (this.selectedRow.parent.id) {
            this.router.navigate(['./material-goods-category', this.selectedRow.parent.id, 'edit']);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucLoaiVatTuHangHoa_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRow.parent.id) {
            this.router.navigate(['/material-goods-category', { outlets: { popup: this.selectedRow.parent.id + '/delete' } }]);
        }
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    selectedItemPerPage() {
        // this.loadAll();
    }

    private paginateMaterialGoodsCategories(data: IMaterialGoodsCategory[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.materialGoodsCategories = data;
    }
    addNew($event) {
        event.preventDefault();
        this.router.navigate(['/material-goods-category/new']);
    }
}
