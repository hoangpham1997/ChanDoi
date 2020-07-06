import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { Principal } from 'app/core';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { BudgetItem } from 'app/shared/model/budget-item.model';
import { BudgetItemService } from './budget-item.service';
import { ITreeBudgetItem, TreeBudgetItem } from 'app/shared/model/budget-item-model-tree';
import { ToastrService } from 'ngx-toastr';
import { IAccountList } from 'app/shared/model/account-list.model';
import { TranslateService } from '@ngx-translate/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { HandlingResult } from 'app/shared/modal/bank/bank-result.model';
import { TreeGridComponent } from 'app/shared/tree-grid/tree-grid.component';
import { ITreeExpenseList } from 'app/shared/model/expense-list-tree.model';

@Component({
    selector: 'eb-budget-item',
    templateUrl: './budget-item.component.html',
    styleUrls: ['./budget-item.component.css']
})
export class BudgetItemComponent extends BaseComponent implements OnInit, OnDestroy {
    budgetItems: IBudgetItem[];
    selectedRow: ITreeBudgetItem;
    eventSubscriber: Subscription;
    handlingResult: HandlingResult;
    predicate: any;
    reverse: any;
    navigateForm: string;
    listParentBudgetItem: ITreeBudgetItem[];
    listTHead: string[];
    listKey: any[];
    modalRef: NgbModalRef;
    ROLE_BudgetItem_Them = ROLE.DanhMucThuChi_Them;
    ROLE_BudgetItem_Sua = ROLE.DanhMucThuChi_Sua;
    ROLE_BudgetItem_Xoa = ROLE.DanhMucThuChi_Xoa;
    @ViewChild('deleteModal') public deleteModal: NgbModalRef;
    @ViewChild('deleteDetailModal') public deleteDetailModal: NgbModalRef;
    @ViewChild('child') child: TreeGridComponent;

    constructor(
        private budgetItemService: BudgetItemService,
        private router: Router,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private translate: TranslateService,
        private toastr: ToastrService,
        private modalService: NgbModal
    ) {
        super();
    }
    arrayTree(tree: TreeBudgetItem[]) {
        if (tree) {
            for (let i = 0; i < tree.length; i++) {
                this.objects.push(tree[i]);
                this.arrayTree(tree[i].children);
            }
        }
    }
    registerChangeBudgetItem() {
        this.eventSubscriber = this.eventManager.subscribe('BudgetItem', response => this.loadAll());
    }
    registerSelectedRow() {
        this.eventSubscriber = this.eventManager.subscribe('selectRow', response => {
            this.selectedRow = response.data;
            console.log(this.selectedRow);
        });
        this.eventSubscriber = this.eventManager.subscribe('selectRows', response => {
            this.selectedRows = response.data;
            console.log(this.selectedRows);
        });
    }
    ngOnInit(): void {
        this.navigateForm = './budget-item';
        this.listTHead = [];
        this.listKey = [];
        this.budgetItems = [];
        this.listParentBudgetItem = [];
        this.listTHead.push('ebwebApp.budgetItem.code');
        this.listTHead.push('ebwebApp.budgetItem.name');
        this.listTHead.push('ebwebApp.budgetItem.type');
        this.listTHead.push('ebwebApp.budgetItem.isActive');
        this.listKey.push({ key: 'budgetItemCode', type: 1 });
        this.listKey.push({ key: 'budgetItemName', type: 1 });
        this.listKey.push({ key: 'budgetItemTypeName', type: 1 });
        this.listKey.push({ key: 'isActive', type: 2 });
        this.objects = [];
        this.loadAll();
        this.registerSelectedRow();
        this.registerChangeBudgetItem();
    }
    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }
    loadAll() {
        this.budgetItemService.getAll().subscribe(res => {
            this.listParentBudgetItem = [];
            this.budgetItems = res.body.map(budget => {
                this.translate
                    .get(budget.budgetItemType === 0 ? 'ebwebApp.budgetItem.revenue' : 'ebwebApp.budgetItem.expenditure')
                    .subscribe(trans => {
                        budget.budgetItemTypeName = trans;
                    });
                return budget;
            });
            const listBudgetItem = this.budgetItems.filter(budget => budget.grade === 1);
            for (let i = 0; i < listBudgetItem.length; i++) {
                this.listParentBudgetItem.push(Object.assign({}));
                this.listParentBudgetItem[i].parent = listBudgetItem[i];
            }
            this.tree(this.listParentBudgetItem, 1);
            this.arrayTree(this.listParentBudgetItem);
            this.selectedRow = this.objects[0];
        });
    }
    tree(treeBudgetItem: ITreeBudgetItem[], grade: number) {
        for (let i = 0; i < treeBudgetItem.length; i++) {
            const childrenNode = this.budgetItems.filter(budget => budget.parentID === treeBudgetItem[i].parent.id);
            for (let j = 0; j < childrenNode.length; j++) {
                if (j === 0) {
                    treeBudgetItem[i].children = [];
                }
                treeBudgetItem[i].children.push(Object.assign({}));
                treeBudgetItem[i].children[j].parent = childrenNode[j];
            }
            if (treeBudgetItem[i].children && treeBudgetItem[i].children.length > 0) {
                this.tree(treeBudgetItem[i].children, grade + 1);
            }
        }
    }
    addNew($event?) {
        event.preventDefault();
        this.router.navigate(['budget-item/new']);
    }
    delete() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.modalRef = this.modalService.open(this.deleteModal, { size: 'lg', backdrop: 'static' });
        } else if (this.selectedRow.parent.isParentNode) {
            this.toastr.error(this.translate.instant('ebwebApp.budgetItem.notDeleteParentNode'));
        } else if (this.selectedRow.parent.id) {
            this.router.navigate(['/budget-item', { outlets: { popup: this.selectedRow.parent.id + '/delete' } }]);
        }
    }

    edit() {
        event.preventDefault();
        if (this.selectedRow.parent.id) {
            this.router.navigate(['budget-item', this.selectedRow.parent.id, 'edit']);
        }
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    deleteAfter() {
        this.modalRef.close();
        const budgetItemsDelete = this.selectedRows.map(budget => budget.parent);
        this.budgetItemService.multiDelete(budgetItemsDelete).subscribe(res => {
            this.handlingResult = res.body;
            this.modalRef = this.modalService.open(this.deleteDetailModal, {
                size: 'lg',
                windowClass: 'width-80',
                backdrop: 'static'
            });
        });
    }
    closeDeleteMultiDetail() {
        this.modalRef.close();
        this.ngOnInit();
    }
    onSelect(select: ITreeExpenseList) {
        this.child.onSelect(select);
    }
}
