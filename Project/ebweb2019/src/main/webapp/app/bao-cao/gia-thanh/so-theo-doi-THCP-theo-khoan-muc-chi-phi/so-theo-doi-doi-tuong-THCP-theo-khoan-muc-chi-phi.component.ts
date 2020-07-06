import { Component, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { JhiEventManager } from 'ng-jhipster';
import { IRequestReport, RequestReport } from 'app/bao-cao/reqest-report.model';
import * as moment from 'moment';
import { Moment } from 'moment';
import { BaoCao, MATERIAL_GOODS_TYPE } from 'app/app.constants';
import { DATE_FORMAT, DATE_FORMAT_SEARCH } from 'app/shared';
import { BaoCaoService } from 'app/bao-cao/bao-cao.service';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { HttpResponse } from '@angular/common/http';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { IAccountList } from 'app/shared/model/account-list.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { CostSetService } from 'app/entities/cost-set';
import { AccountListService } from 'app/danhmuc/account-list';
import { ROLE } from 'app/role.constants';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { ExpenseItemService } from 'app/entities/expense-item';
import { ITreeExpenseList } from 'app/shared/model/expense-list-tree.model';
import { Subscription } from 'rxjs';
import { TreeOrganizationUnit } from 'app/shared/model/organization-unit-tree.model';

@Component({
    selector: 'eb-soTHCPTheoKhoanMucChiPhi',
    templateUrl: './so-theo-doi-doi-tuong-THCP-theo-khoan-muc-chi-phi.component.html',
    styleUrls: ['so-theo-doi-doi-tuong-THCP-theo-khoan-muc-chi-phi.component.css']
})
export class SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiComponent implements OnInit {
    createDate: Moment;
    fromDate: Moment;
    toDate: Moment;
    isKetXuat: boolean;
    role_Ketxuat = ROLE.SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhi_KetXuat;
    dependent: boolean;
    requestReport: RequestReport;
    dataSession: IRequestReport;
    timeLineVoucher: any;
    listTimeLine: any[];
    account: any;
    modalRef: NgbModalRef;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    organizationUnit: any;
    treeOrganizationUnits: any[];
    costSet: ICostSet[];
    expenseItems: IExpenseItem[];
    costSetCode: string;
    costSetName: string;
    expenseItemCode: string;
    expenseItemName: string;
    costSetFilter: ICostSet[];
    expenseItemsFilter: IExpenseItem[];
    listTHead: string[];
    listKey: any[];
    listSearch: any;
    navigateForm: string;
    keySearch: any[];
    treeExpenseLists: ITreeExpenseList[];
    listParentAccount: ITreeExpenseList[];
    flatTreeAccountList: ITreeExpenseList[];
    index: number;
    min: number;
    listExpenseItemsChecked: any[];
    eventSubscriber: Subscription;

    constructor(
        public activeModal: NgbActiveModal,
        private principal: Principal,
        private toastr: ToastrService,
        public translate: TranslateService,
        public eventManager: JhiEventManager,
        private baoCaoService: BaoCaoService,
        public utilsService: UtilsService,
        private organizationUnitService: OrganizationUnitService,
        private costSetService: CostSetService,
        private expenseItemService: ExpenseItemService
    ) {}

    ngOnInit(): void {
        this.dependent = false;
        this.min = 0;
        this.treeExpenseLists = [];
        this.listTHead = [];
        this.listKey = [];
        this.keySearch = [];
        this.listSearch = {};
        this.listExpenseItemsChecked = [];
        this.listTHead.push('checked');
        this.listTHead.push('ebwebApp.expenseItem.expenseItemCode');
        this.listTHead.push('ebwebApp.expenseItem.expenseItemName');
        this.listKey.push({ key: 'checked', type: 1 });
        this.listKey.push({ key: 'expenseItemCode', type: 1 });
        this.listKey.push({ key: 'expenseItemName', type: 1 });
        this.listSearch = {
            expenseItemCode: '',
            expenseItemName: ''
        };
        this.keySearch.push('checked');
        this.keySearch.push('expenseItemCode');
        this.keySearch.push('expenseItemName');
        this.principal.identity().then(account => {
            this.account = account;
            this.isKetXuat = !this.utilsService.isPackageDemo(account);
            this.listTimeLine = this.utilsService.getCbbTimeLine();
            this.timeLineVoucher = this.listTimeLine[4].value;
            this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
            this.getSessionData();
            // this.organizationUnitService.getChildCompanyByID().subscribe((res: HttpResponse<any>) => {
            //     this.treeOrganizationUnits = res.body.orgTrees;
            //     this.organizationUnit = res.body.currentOrgLogin;
            //     this.changeOptionData();
            // });
            this.changeOptionData();
        });
        // this.loadAll();
        this.registerChangeListSearchExpenseItem();
        this.registerChangeCheckBox();
    }

    addAllExpenseItems(listParentAccount: ITreeExpenseList[], grade) {
        for (let i = 0; i < listParentAccount.length; i++) {
            if (listParentAccount[i].parent.checked) {
                this.listExpenseItemsChecked.push(listParentAccount[i].parent.id);
            }
            if (listParentAccount[i].children && listParentAccount[i].children.length > 0) {
                this.addAllExpenseItems(listParentAccount[i].children, grade + 1);
            }
        }
    }

    loadAll() {
        this.expenseItemService.getExpenseItems().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.index = 0;
            this.listParentAccount = [];
            this.flatTreeAccountList = [];
            this.expenseItemsFilter = res.body;
            for (let i = 0; i < this.expenseItemsFilter.length; i++) {
                if (!this.expenseItemsFilter[i].expenseItemCode) {
                    this.expenseItemsFilter[i].expenseItemCode = '';
                }
                if (!this.expenseItemsFilter[i].expenseItemName) {
                    this.expenseItemsFilter[i].expenseItemName = '';
                }
            }
            this.expenseItems = res.body;
            if (this.expenseItems && this.expenseItems.length > 0) {
                this.min = this.expenseItems[0].grade;
                for (let i = 0; i < this.expenseItems.length; i++) {
                    if (this.min > this.expenseItems[i].grade) {
                        this.min = this.expenseItems[i].grade;
                    }
                }
                const expenseItem = this.expenseItems.filter(a => a.grade === 1);
                for (let i = 0; i < expenseItem.length; i++) {
                    this.listParentAccount.push(Object.assign({}));
                    this.listParentAccount[i].parent = expenseItem[i];
                }
                this.tree(this.listParentAccount, this.min);
                this.setIndexTreeExpenseItem(this.listParentAccount, this.min);
            }
        });
    }

    tree(expenseItem: ITreeExpenseList[], grade) {
        for (let i = 0; i < expenseItem.length; i++) {
            const newList = this.expenseItems.filter(a => a.parentID === expenseItem[i].parent.id);
            expenseItem[i].children = [];
            for (let j = 0; j < newList.length; j++) {
                expenseItem[i].children.push(Object.assign({}));
                expenseItem[i].children[j].parent = newList[j];
            }
            if (expenseItem[i].children && expenseItem[i].children.length > 0) {
                this.tree(expenseItem[i].children, grade + 1);
            }
        }
    }

    registerChangeListSearchExpenseItem() {
        this.eventSubscriber = this.eventManager.subscribe('listSearch', response => {
            this.listSearch = response.data;
            this.listParentAccount = [];
            this.flatTreeAccountList = [];
            this.expenseItems = this.expenseItemsFilter.filter(
                a =>
                    a.expenseItemCode.toUpperCase().includes(this.listSearch.expenseItemCode.toUpperCase()) &&
                    a.expenseItemName.toUpperCase().includes(this.listSearch.expenseItemName.toUpperCase())
            );
            this.treeforSearchExpenseItem();
            let grade;
            if (this.expenseItems.length) {
                grade = this.expenseItems[0].grade;
            }
            for (let i = 0; i < this.expenseItems.length; i++) {
                if (this.expenseItems[i].grade < grade) {
                    grade = this.expenseItems[i].grade;
                }
            }
            const listAccount = this.expenseItems.filter(a => a.grade === grade);
            for (let i = 0; i < listAccount.length; i++) {
                this.listParentAccount.push(Object.assign({}));
                this.listParentAccount[i].parent = listAccount[i];
            }
            this.tree(this.listParentAccount, grade);
            this.setIndexTreeExpenseItem(this.listParentAccount, grade);
        });
    }

    treeforSearchExpenseItem() {
        for (let i = 0; i < this.expenseItems.length; i++) {
            if (this.expenseItems[i].parentID) {
                this.getAllParentExpenseItem(this.expenseItems[i]);
            }
        }
    }

    getAllParentExpenseItem(expenseItem: IExpenseItem) {
        const addAccount = this.expenseItemsFilter.find(a => a.id === expenseItem.parentID);
        const checkExistAccount = this.expenseItems.find(a => a.id === addAccount.id);
        if (addAccount && !checkExistAccount) {
            this.expenseItems.push(addAccount);
            if (addAccount.parentID) {
                this.getAllParentExpenseItem(addAccount);
            }
        }
    }

    setIndexTreeExpenseItem(expenseLists: ITreeExpenseList[], grade) {
        for (let i = 0; i < expenseLists.length; i++) {
            expenseLists[i].index = this.index;
            this.index++;
            if (expenseLists[i].children && expenseLists[i].children.length > 0) {
                this.setIndexTreeExpenseItem(expenseLists[i].children, grade + 1);
            }
        }
    }

    checkErr() {
        if (!this.organizationUnit) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.error.nullOrg'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (!this.fromDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.nullFromDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (!this.toDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.nullToDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (this.toDate && this.fromDate) {
            if (moment(this.toDate, DATE_FORMAT_SEARCH) < moment(this.fromDate, DATE_FORMAT_SEARCH)) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mCReceipt.error.fromDateGreaterToDate'),
                    this.translate.instant('ebwebApp.mCReceipt.error.error')
                );
                return false;
            }
        }
        if (this.expenseItemsFilter.filter(x => x.checked).length === 0) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.error.nullExpenseItem'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (this.costSetFilter.filter(x => x.checked).length === 0) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.error.nullCostSet'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        return true;
    }

    selectChangeBeginDateAndEndDate(intTimeLine: String) {
        if (intTimeLine) {
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
            this.fromDate = moment(this.objTimeLine.dtBeginDate);
            this.toDate = moment(this.objTimeLine.dtEndDate);
            this.createDate = this.fromDate;
        }
    }

    getCurrentDate(): { year; month; day } {
        const _date = moment();
        return { year: _date.year(), month: _date.month() + 1, day: _date.date() };
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    isCheckAll2() {
        if (this.costSetFilter) {
            return this.costSetFilter.every(item => item.checked) && this.costSetFilter.length;
        } else {
            return false;
        }
    }

    checkAll2() {
        const isCheck = this.costSetFilter.every(item => item.checked) && this.costSetFilter.length;
        this.costSetFilter.forEach(item => (item.checked = !isCheck));
    }

    check2(costSetFilter: ICostSet) {
        costSetFilter.checked = !costSetFilter.checked;
    }

    changeMGFilter2() {
        if (this.costSetCode && this.costSetName) {
            this.costSetFilter = this.costSet.filter(
                x =>
                    x.costSetCode.toLowerCase().includes(this.costSetCode.toLowerCase()) &&
                    x.costSetName.toLowerCase().includes(this.costSetName.toLowerCase())
            );
        } else if (this.costSetName) {
            this.costSetFilter = this.costSet.filter(x => x.costSetName.toLowerCase().includes(this.costSetName));
        } else if (this.costSetCode) {
            this.costSetFilter = this.costSet.filter(x => x.costSetCode.toLowerCase().includes(this.costSetCode));
        } else this.costSetFilter = this.costSet;
    }

    changeMGFilter() {
        if (this.expenseItemCode && this.expenseItemName) {
            this.expenseItemsFilter = this.expenseItems.filter(
                x =>
                    x.expenseItemCode.toLowerCase().includes(this.expenseItemCode.toLowerCase()) &&
                    x.expenseItemName.toLowerCase().includes(this.expenseItemName.toLowerCase())
            );
        } else if (this.expenseItemCode) {
            this.expenseItemsFilter = this.expenseItems.filter(x => x.expenseItemCode.toLowerCase().includes(this.expenseItemCode));
        } else if (this.expenseItemName) {
            this.expenseItemsFilter = this.expenseItems.filter(x => x.expenseItemName.toLowerCase().includes(this.expenseItemName));
        } else this.expenseItemsFilter = this.expenseItems;
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('searchSoTheoDoiTHCPTheoKhoanChiPhi'));
        if (this.dataSession) {
            this.toDate = this.dataSession.toDate;
            this.fromDate = this.dataSession.fromDate;
            this.timeLineVoucher = this.dataSession.timeLineVoucher ? this.dataSession.timeLineVoucher : '0';
        } else {
            this.dataSession = {};
        }
    }

    setSessionData() {
        this.dataSession.toDate = this.toDate;
        this.dataSession.fromDate = this.fromDate;
        this.dataSession.timeLineVoucher = this.timeLineVoucher ? this.timeLineVoucher : '';
        sessionStorage.setItem('searchSoTheoDoiTHCPTheoKhoanChiPhi', JSON.stringify(this.dataSession));
    }

    changeOptionData(reload?) {
        if (reload) {
            this.dependent = false;
        }
        this.costSetService
            .getCostSetList({
                companyID: this.organizationUnit.parent.id,
                dependent: this.dependent
            })
            .subscribe((res: HttpResponse<ICostSet[]>) => {
                this.costSet = res.body.sort((a, b) => a.costSetCode.localeCompare(b.costSetCode));
                this.costSetFilter = this.costSet;
            });
        this.expenseItemService
            .getExpenseItemsAndDependent({ isDependent: this.dependent, orgID: this.organizationUnit.parent.id })
            .subscribe((res: HttpResponse<IExpenseItem[]>) => {
                this.expenseItems = res.body;
                this.index = 0;
                this.listParentAccount = [];
                this.flatTreeAccountList = [];
                this.expenseItemsFilter = res.body;
                for (let i = 0; i < this.expenseItemsFilter.length; i++) {
                    if (!this.expenseItemsFilter[i].expenseItemCode) {
                        this.expenseItemsFilter[i].expenseItemCode = '';
                    }
                    if (!this.expenseItemsFilter[i].expenseItemName) {
                        this.expenseItemsFilter[i].expenseItemName = '';
                    }
                }
                this.expenseItems = res.body;
                if (this.expenseItems && this.expenseItems.length > 0) {
                    this.min = this.expenseItems[0].grade;
                    for (let i = 0; i < this.expenseItems.length; i++) {
                        if (this.min > this.expenseItems[i].grade) {
                            this.min = this.expenseItems[i].grade;
                        }
                    }
                    const expenseItem = this.expenseItems.filter(a => a.grade === 1);
                    for (let i = 0; i < expenseItem.length; i++) {
                        this.listParentAccount.push(Object.assign({}));
                        this.listParentAccount[i].parent = expenseItem[i];
                    }
                    this.tree(this.listParentAccount, this.min);
                    this.setIndexTreeExpenseItem(this.listParentAccount, this.min);
                }
            });
    }

    export() {
        if (this.checkErr()) {
            this.setSessionData();
            this.requestReport = {};
            this.listExpenseItemsChecked = [];
            this.requestReport.typeReport = BaoCao.Gia_Thanh.SO_THEO_DOI_DOI_TUONG_THCP_THEO_KHOAN_MUC_CHI_PHI;
            this.requestReport.fileName = BaoCao.Gia_Thanh.SO_THEO_DOI_DOI_TUONG_THCP_THEO_KHOAN_MUC_CHI_PHI_XSL;
            this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
            this.requestReport.dependent = this.dependent;
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT);
            this.requestReport.listCostSetID = this.costSet.filter(c => c.checked).map(c => c.id);
            this.addAllExpenseItems(this.listParentAccount, this.min);
            this.requestReport.listExpenseItems = this.listExpenseItemsChecked;
            this.baoCaoService.exportExcel(this.requestReport).subscribe(
                res => {
                    const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                    const fileURL = URL.createObjectURL(blob);

                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    const name = BaoCao.Gia_Thanh.SO_THEO_DOI_DOI_TUONG_THCP_THEO_KHOAN_MUC_CHI_PHI_XSL;
                    link.setAttribute('download', name);
                    link.href = fileURL;
                    link.click();
                },
                () => {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.saReturn.error.undefined'),
                        this.translate.instant('ebwebApp.mCReceipt.error.error')
                    );
                }
            );
        }
    }

    accept() {
        if (this.checkErr()) {
            this.setSessionData();
            this.requestReport = {};
            this.listExpenseItemsChecked = [];
            this.requestReport.typeReport = BaoCao.Gia_Thanh.SO_THEO_DOI_DOI_TUONG_THCP_THEO_KHOAN_MUC_CHI_PHI;
            this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
            this.requestReport.dependent = this.dependent;
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT);
            this.requestReport.listCostSetID = this.costSet.filter(c => c.checked).map(c => c.id);
            this.addAllExpenseItems(this.listParentAccount, this.min);
            this.requestReport.listExpenseItems = this.listExpenseItemsChecked;
            this.baoCaoService.getReport(this.requestReport);
        }
    }

    private registerChangeCheckBox() {
        this.eventSubscriber = this.eventManager.subscribe('sendTree', response => {
            this.listParentAccount = [];
            this.listExpenseItemsChecked = [];
            this.listParentAccount = response.data;
        });
    }
}
