import { Component, OnInit, OnDestroy, AfterViewInit, ViewChild } from '@angular/core';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { KhoanMucChiPhiListService } from 'app/danhmuc/khoan-muc-chi-phi-list/khoan-muc-chi-phi-list.service';
import { ActivatedRoute, Router } from '@angular/router';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ITreeExpenseList, TreeExpenseList } from 'app/shared/model/expense-list-tree.model';
import { ITreeAccountList } from 'app/shared/model/account-list-tree.model';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { IAccountList } from 'app/shared/model/account-list.model';
import { Subscription } from 'rxjs';
import { ROLE } from 'app/role.constants';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { IPaymentClause } from 'app/shared/model/payment-clause.model';
import { switchAll, switchMap } from 'rxjs/operators';
import { TreeGridComponent } from 'app/shared/tree-grid/tree-grid.component';
import { Principal } from 'app/core';
import { ITreeStatisticsCode } from 'app/shared/model/statistics-code-tree.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { HandlingResult } from 'app/shared/modal/bank/bank-result.model';

@Component({
    selector: 'eb-expense-list',
    templateUrl: './khoan-muc-chi-phi-list.component.html',
    styleUrls: ['./khoan-muc-chi-phi-list.component.css']
})
export class KhoanMucChiPhiListComponent extends BaseComponent implements OnInit, OnDestroy {
    currentAccount: any;
    page: any;
    itemsPerPage: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    selectedRow: ITreeExpenseList;
    expenseItem: IExpenseItem;
    treeExpenseLists: ITreeExpenseList[];
    exPenLists: IExpenseItem[];
    listParenExpense: ITreeExpenseList[];
    flatTreeAccountList: ITreeExpenseList[];
    // listExpense: IExpenseItem[];
    eventSubscriber: Subscription;
    navigateForm: string;
    listTHead: string[];
    listKey: any[];
    listKeys: string[];
    @ViewChild('child') child: TreeGridComponent;
    @ViewChild('deleteModal') public deleteModal: NgbModalRef;
    @ViewChild('deleteDetailModal') public deleteDetailModal: NgbModalRef;
    ROLE_IExpenseItem_Them = ROLE.DanhMucKhoanMucChiPhi_Them;
    ROLE_IExpenseItem_Sua = ROLE.DanhMucKhoanMucChiPhi_Sua;
    ROLE_IExpenseItem_Xoa = ROLE.DanhMucKhoanMucChiPhi_Xoa;
    ROLE_IExpenseItem_Xem = ROLE.DanhMucKhoanMucChiPhi_Xem;
    links: any;
    totalItems: number;
    queryCount: number;
    count: number;
    case: string;
    modalRef: NgbModalRef;

    buttonDeleteTranslate = 'ebwebApp.mBDeposit.toolTip.delete';
    buttonAddTranslate = 'ebwebApp.mBDeposit.toolTip.add';
    buttonEditTranslate = 'ebwebApp.mBDeposit.toolTip.edit';
    buttonRecordTranslate = 'ebwebApp.mBDeposit.toolTip.record';
    buttonUnRecordTranslate = 'ebwebApp.mBDeposit.toolTip.unrecord';
    buttonExportTranslate = 'ebwebApp.mBDeposit.toolTip.export';
    buttonSearchTranslate = 'ebwebApp.mBDeposit.toolTip.search';
    handlingResult: HandlingResult;

    constructor(
        private jhiAlertService: JhiAlertService,
        private parseLinks: JhiParseLinks,
        private expenseItemService: KhoanMucChiPhiListService,
        private activatedRoute: ActivatedRoute,
        public utilsService: UtilsService,
        private principal: Principal,
        private router: Router,
        private toastr: ToastrService,
        public translate: TranslateService,
        private eventManager: JhiEventManager,
        private modalService: NgbModal
    ) {
        super();
    }

    ngOnInit(): void {
        this.navigateForm = './khoan-muc-chi-phi-list';
        this.treeExpenseLists = [];
        this.listParenExpense = [];
        this.exPenLists = [];
        this.listKey = [];
        this.listKeys = [];
        this.listTHead = [];
        this.listTHead.push('ebwebApp.expenseItem.expenseItemCode');
        this.listTHead.push('ebwebApp.expenseItem.expenseItemName');
        this.listTHead.push('ebwebApp.expenseItem.expenseType');
        this.listTHead.push('ebwebApp.expenseItem.isActive');
        this.listKey.push({ key: 'expenseItemCode', type: 1 });
        this.listKey.push({ key: 'expenseItemName', type: 1 });
        this.listKey.push({ key: 'expenseTypeName', type: 1 });
        this.listKey.push({ key: 'isActive', type: 2 });
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeITransportMethod();
        this.registerSelectedRow();
    }

    onSelect(select: ITreeExpenseList) {
        this.child.onSelect(select);
    }

    ngOnDestroy(): void {}

    registerChangeITransportMethod() {
        this.eventSubscriber = this.eventManager.subscribe('IExpenseItemListModification', even => this.loadAll());
    }

    registerSelectedRow() {
        this.eventSubscriber = this.eventManager.subscribe('selectRow', response => {
            this.selectedRow = response.data;
            console.log(this.selectedRow);
            console.log(this.selectedRows);
        });
        this.eventSubscriber = this.eventManager.subscribe('selectRows', response => {
            this.selectedRows = response.data;
            console.log(this.selectedRows);
        });
    }

    tree(exPenseList: ITreeExpenseList[], grade) {
        for (let i = 0; i < exPenseList.length; i++) {
            const newList = this.exPenLists.filter(a => a.parentID === exPenseList[i].parent.id);
            for (let j = 0; j < newList.length; j++) {
                if (j === 0) {
                    exPenseList[i].children = [];
                }
                exPenseList[i].children.push(Object.assign({}));
                exPenseList[i].children[j].parent = newList[j];
            }
            if (exPenseList[i].children && exPenseList[i].children.length > 0) {
                this.tree(exPenseList[i].children, grade + 1);
            }
        }
    }

    cutTree(tree: ITreeExpenseList[]) {
        tree.forEach(branch => {
            this.flatTreeAccountList.push(branch);
            if (branch.children && branch.children.length > 0) {
                this.cutTree(branch.children);
            }
        });
    }

    trackId(index: number, item: IExpenseItem) {
        return item.id;
    }

    transition() {
        this.router.navigate(['/khoan-muc-chi-phi-list'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/khoan-muc-chi-phi-list',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    loadAll() {
        this.expenseItemService.findAllByAndCompanyID().subscribe((res: HttpResponse<IExpenseItem[]>) => {
            this.listParenExpense = [];
            this.flatTreeAccountList = [];
            this.exPenLists = res.body;
            this.switchCase();
            const listExpense = this.exPenLists.filter(e => e.grade === 1);
            for (let i = 0; i < listExpense.length; i++) {
                this.listParenExpense.push(Object.assign({}));
                this.listParenExpense[i].parent = listExpense[i];
            }
            this.tree(this.listParenExpense, 1);
            this.cutTree(this.listParenExpense);
            this.objects = this.flatTreeAccountList;
            this.selectedRow = this.flatTreeAccountList[0];
            this.onSelect(this.selectedRow);
        });
    }

    switchCase() {
        for (let i = 0; i < this.exPenLists.length; i++) {
            switch (this.exPenLists[i].expenseType) {
                case 0:
                    this.translate.get('ebwebApp.expenseItem.rawMaterialsDirectly').subscribe(trans => {
                        this.exPenLists[i].expenseTypeName = trans;
                    });
                    break;
                case 1:
                    this.translate.get('ebwebApp.expenseItem.directLabor').subscribe(trans => {
                        this.exPenLists[i].expenseTypeName = trans;
                    });
                    break;
                case 2:
                    this.translate.get('ebwebApp.expenseItem.laborCosts').subscribe(trans => {
                        this.exPenLists[i].expenseTypeName = trans;
                    });
                    break;
                case 3:
                    this.translate.get('ebwebApp.expenseItem.costOfRawMaterials').subscribe(trans => {
                        this.exPenLists[i].expenseTypeName = trans;
                    });
                    break;
                case 4:
                    this.translate.get('ebwebApp.expenseItem.costOfProductionTools').subscribe(trans => {
                        this.exPenLists[i].expenseTypeName = trans;
                    });
                    break;
                case 5:
                    this.translate.get('ebwebApp.expenseItem.machineDepreciationExpense').subscribe(trans => {
                        this.exPenLists[i].expenseTypeName = trans;
                    });
                    break;
                case 6:
                    this.translate.get('ebwebApp.expenseItem.costOfHiredServices').subscribe(trans => {
                        this.exPenLists[i].expenseTypeName = trans;
                    });
                    break;
                case 7:
                    this.translate.get('ebwebApp.expenseItem.costsInOtherCurrencies').subscribe(trans => {
                        this.exPenLists[i].expenseTypeName = trans;
                    });
                    break;
                case 8:
                    this.translate.get('ebwebApp.expenseItem.generalFactoryStaffCosts').subscribe(trans => {
                        this.exPenLists[i].expenseTypeName = trans;
                    });
                    break;
                case 9:
                    this.translate.get('ebwebApp.expenseItem.generalMaterialCosts').subscribe(trans => {
                        this.exPenLists[i].expenseTypeName = trans;
                    });
                    break;
                case 10:
                    this.translate.get('ebwebApp.expenseItem.generalProductionEquipmentCosts').subscribe(trans => {
                        this.exPenLists[i].expenseTypeName = trans;
                    });
                    break;
                case 11:
                    this.translate.get('ebwebApp.expenseItem.generalDepreciationExpense').subscribe(trans => {
                        this.exPenLists[i].expenseTypeName = trans;
                    });
                    break;
                case 12:
                    this.translate.get('ebwebApp.expenseItem.expensesForServicesPurchasedFromOutside').subscribe(trans => {
                        this.exPenLists[i].expenseTypeName = trans;
                    });
                    break;
                case 13:
                    this.translate.get('ebwebApp.expenseItem.costsInOtherGeneralCurrency').subscribe(trans => {
                        this.exPenLists[i].expenseTypeName = trans;
                    });
                    break;
            }
        }
    }

    addNew($event) {
        event.preventDefault();
        this.router.navigate(['/khoan-muc-chi-phi-list/new']);
    }

    delete() {
        event.preventDefault();
        // if(this.selectedRow.parent.isParentNode){
        if (this.selectedRows.length > 1) {
            this.modalRef = this.modalService.open(this.deleteModal, { backdrop: 'static' });
        } else if (this.selectedRow.parent.isParentNode) {
            this.toastr.error(this.translate.instant('ebwebApp.expenseItem.errorParent'));
        } else if (this.selectedRow.parent.id) {
            this.router.navigate(['/khoan-muc-chi-phi-list', { outlets: { popup: this.selectedRow.parent.id + '/delete' } }]);
        }
    }

    edit() {
        event.preventDefault();
        if (this.selectedRow.parent.id) {
            this.router.navigate(['./khoan-muc-chi-phi-list', this.selectedRow.parent.id, 'edit']);
        }
    }

    deleteAfter() {
        const expenseItemsDelete = this.selectedRows.map(expense => expense.parent);
        this.expenseItemService.multiDelete(expenseItemsDelete).subscribe(res => {
            this.handlingResult = res.body;
            this.modalRef = this.modalService.open(this.deleteDetailModal, {
                size: 'lg',
                windowClass: 'width-80',
                backdrop: false
            });
        });
    }

    closeDeleteMultiDetail() {
        this.modalRef.close();
        this.ngOnInit();
    }
}
