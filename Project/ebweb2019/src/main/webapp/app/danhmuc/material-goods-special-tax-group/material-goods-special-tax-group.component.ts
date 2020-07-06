import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IMaterialGoodsSpecialTaxGroup } from 'app/shared/model/material-goods-special-tax-group.model';
import { Principal } from 'app/core';
import { MaterialGoodsSpecialTaxGroupService } from './material-goods-special-tax-group.service';
import { ITreeMaterialGoodsSpecialTaxGroup } from 'app/shared/model/material-goods-special-tax-group-tree.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { IUnit } from 'app/shared/model/unit.model';
import { ROLE } from 'app/role.constants';
import { TranslateService } from '@ngx-translate/core';
import { IMaterialGoodsConvertUnit } from 'app/shared/model/material-goods-convert-unit.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { UnitService } from 'app/danhmuc/unit';
import { ITreeAccountList } from 'app/shared/model/account-list-tree.model';
import { IAccountList } from 'app/shared/model/account-list.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { TreeGridComponent } from 'app/shared/tree-grid/tree-grid.component';
import { ITreeExpenseList } from 'app/shared/model/expense-list-tree.model';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-material-goods-special-tax-group',
    templateUrl: './material-goods-special-tax-group.component.html',
    styleUrls: ['./material-goods-special-tax-group.component.css']
})
export class MaterialGoodsSpecialTaxGroupComponent extends BaseComponent implements OnInit, OnDestroy {
    private _materialGoodsSpecialTaxGroup: IMaterialGoodsSpecialTaxGroup;
    currentAccount: any;
    materialGoodsSpecialTaxGroups: IMaterialGoodsSpecialTaxGroup[];
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
    navigateForm: string;
    listTHead: string[];
    listKey: any[];
    treeMaterialGoodsSpecialTaxGroup: ITreeMaterialGoodsSpecialTaxGroup[];
    flatTreeMaterialGoodsSpecialTaxGroup: ITreeMaterialGoodsSpecialTaxGroup[];
    selectedRow: ITreeMaterialGoodsSpecialTaxGroup;
    listTreeMaterialGoodsSpecialTaxGroup: ITreeMaterialGoodsSpecialTaxGroup[];
    units: IUnit[];
    ROLE_HHDVChiuThue_Them = ROLE.DanhMucHHDVChiuThueTTDB_Them;
    ROLE_HHDVChiuThue_Sua = ROLE.DanhMucHHDVChiuThueTTDB_Sua;
    ROLE_HHDVChiuThue_Xoa = ROLE.DanhMucHHDVChiuThueTTDB_Xoa;
    @ViewChild('child') child: TreeGridComponent;

    constructor(
        private materialGoodsSpecialTaxGroupService: MaterialGoodsSpecialTaxGroupService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private translate: TranslateService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private unitService: UnitService
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
        // let index = 0;
        this.materialGoodsSpecialTaxGroupService
            .getMaterialGoodsSpecialTaxGroup()
            .subscribe((res: HttpResponse<ITreeMaterialGoodsSpecialTaxGroup[]>) => {
                this.listTreeMaterialGoodsSpecialTaxGroup = [];
                this.flatTreeMaterialGoodsSpecialTaxGroup = [];
                this.materialGoodsSpecialTaxGroups = res.body;
                this.selectedRow = this.materialGoodsSpecialTaxGroups[0];
                const listAccount = this.materialGoodsSpecialTaxGroups.filter(a => a.grade === 1);
                for (let i = 0; i < listAccount.length; i++) {
                    this.listTreeMaterialGoodsSpecialTaxGroup.push(Object.assign({}));
                    this.listTreeMaterialGoodsSpecialTaxGroup[i].parent = listAccount[i];
                    // index++;
                }
                this.tree(this.listTreeMaterialGoodsSpecialTaxGroup, 1);
                this.cutTree(this.listTreeMaterialGoodsSpecialTaxGroup);
                this.objects = this.flatTreeMaterialGoodsSpecialTaxGroup;
                this.selectedRow = this.flatTreeMaterialGoodsSpecialTaxGroup[0];
            });
    }

    tree(materialGoodsSpecialTaxGroup: ITreeMaterialGoodsSpecialTaxGroup[], grade) {
        for (let i = 0; i < materialGoodsSpecialTaxGroup.length; i++) {
            const newList = this.materialGoodsSpecialTaxGroups.filter(a => a.parentID === materialGoodsSpecialTaxGroup[i].parent.id);
            materialGoodsSpecialTaxGroup[i].children = [];
            for (let j = 0; j < newList.length; j++) {
                materialGoodsSpecialTaxGroup[i].children.push(Object.assign({}));
                materialGoodsSpecialTaxGroup[i].children[j].parent = newList[j];
            }
            if (materialGoodsSpecialTaxGroup[i].children && materialGoodsSpecialTaxGroup[i].children.length > 0) {
                this.tree(materialGoodsSpecialTaxGroup[i].children, grade + 1);
            }
        }
        for (let i = 0; i < materialGoodsSpecialTaxGroup.length; i++) {
            if (materialGoodsSpecialTaxGroup[i].parent.unitID !== null || materialGoodsSpecialTaxGroup[i].parent.unitID !== undefined) {
                materialGoodsSpecialTaxGroup[i].parent.unitName = this.getUnitName(materialGoodsSpecialTaxGroup[i].parent.unitID);
            }
        }
    }

    cutTree(tree: ITreeMaterialGoodsSpecialTaxGroup[]) {
        tree.forEach(branch => {
            this.flatTreeMaterialGoodsSpecialTaxGroup.push(branch);
            if (branch.children && branch.children.length > 0) {
                this.cutTree(branch.children);
            }
        });
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
        this.router.navigate(['/material-goods-special-tax-group'], {
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
            '/material-goods-special-tax-group',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.listTHead = [];
        this.listKey = [];
        this.navigateForm = './material-goods-special-tax-group';
        this.listTHead.push('ebwebApp.materialGoodsSpecialTaxGroup.materialGoodsSpecialTaxGroupCodeNew');
        this.listTHead.push('ebwebApp.materialGoodsSpecialTaxGroup.materialGoodsSpecialTaxGroupNameNew');
        this.listTHead.push('ebwebApp.materialGoodsSpecialTaxGroup.unitID');
        this.listTHead.push('ebwebApp.materialGoodsSpecialTaxGroup.taxRate');
        this.listTHead.push('ebwebApp.materialGoodsSpecialTaxGroup.isActive');
        this.listKey.push({ key: 'materialGoodsSpecialTaxGroupCode', type: 1 });
        this.listKey.push({ key: 'materialGoodsSpecialTaxGroupName', type: 1 });
        this.listKey.push({ key: 'unitName', type: 1 });
        this.listKey.push({ key: 'taxRate', type: 1 });
        this.listKey.push({ key: 'isActive', type: 1 });
        this.materialGoodsSpecialTaxGroups = [];
        this.treeMaterialGoodsSpecialTaxGroup = [];
        this.units = [];
        this.unitService.getUnits().subscribe((res: HttpResponse<IUnit[]>) => {
            this.units = res.body;
        });
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInMaterialGoodsSpecialTaxGroups();
        this.registerSelectedRow();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IMaterialGoodsSpecialTaxGroup) {
        return item.id;
    }

    registerChangeInMaterialGoodsSpecialTaxGroups() {
        this.eventSubscriber = this.eventManager.subscribe('materialGoodsSpecialTaxGroupListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    doubleClickRow(any?) {
        this.edit();
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucHHDVChiuThueTTDB_Xem])
    edit() {
        event.preventDefault();
        if (this.selectedRow.parent.id) {
            this.router.navigate(['./material-goods-special-tax-group', this.selectedRow.parent.id, 'edit']);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucHHDVChiuThueTTDB_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRow.parent.id) {
            this.router.navigate(['/material-goods-special-tax-group', { outlets: { popup: this.selectedRow.parent.id + '/delete' } }]);
        }
    }

    // onSelect(select: IMaterialGoodsSpecialTaxGroup, event) {
    //     this.selectedRow = select;
    //     console.log(`selectedTest1 = ${JSON.stringify(this.selectedRow)}`);
    // }

    onSelect(select: ITreeMaterialGoodsSpecialTaxGroup) {
        this.child.onSelect(select);
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    getUnitName(unitID: string) {
        if (this.units && unitID) {
            const unit = this.units.find(u => u.id.toLowerCase() === unitID.toLowerCase());
            if (unit.unitName) {
                return unit.unitName;
            } else {
                return '';
            }
        }
    }

    selectedItemPerPage() {
        this.loadAll();
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucHHDVChiuThueTTDB_Them])
    addNew($event?) {
        event.preventDefault();
        this.router.navigate(['/material-goods-special-tax-group/new']);
    }

    get materialGoodsSpecialTaxGroup() {
        return this._materialGoodsSpecialTaxGroup;
    }

    set materialGoodsSpecialTaxGroup(materialGoodsSpecialTaxGroup: IMaterialGoodsSpecialTaxGroup) {
        this._materialGoodsSpecialTaxGroup = materialGoodsSpecialTaxGroup;
    }
}
