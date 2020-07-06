import { Component, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { JhiEventManager } from 'ng-jhipster';
import { RequestReport } from 'app/bao-cao/reqest-report.model';
import * as moment from 'moment';
import { Moment } from 'moment';
import { BaoCao } from 'app/app.constants';
import { DATE_FORMAT, DATE_FORMAT_SEARCH } from 'app/shared';
import { BaoCaoService } from 'app/bao-cao/bao-cao.service';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';
import { HttpResponse } from '@angular/common/http';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { AccountListService } from 'app/danhmuc/account-list';
import { IAccountList } from 'app/shared/model/account-list.model';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { ROLE } from 'app/role.constants';
import { TreeOrganizationUnit } from 'app/shared/model/organization-unit-tree.model';
import { ExpenseItemService } from 'app/entities/expense-item';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { ITreeAccountList } from 'app/shared/model/account-list-tree.model';
import { ITreeExpenseList } from 'app/shared/model/expense-list-tree.model';
import { Subscription } from 'rxjs';

@Component({
    selector: 'eb-tong-hop-chi-phi-theo-khoan-muc-chi-phi',
    templateUrl: './tong-hop-chi-phi-theo-khoan-muc-chi-phi.component.html',
    styleUrls: ['tong-hop-chi-phi-theo-khoan-muc-chi-phi.component.css']
})
export class TongHopChiPhiTheoKhoanMucChiPhiComponent implements OnInit {
    createDate: Moment;
    fromDate: Moment;
    toDate: Moment;
    eventSubscriber: Subscription;
    requestReport: RequestReport;
    dataSession: RequestReport;
    listTimeLine: any[];
    account: any;
    modalRef: NgbModalRef;
    groupTheSameItem: boolean;
    timeLineVoucher: any;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    organizationUnit: TreeOrganizationUnit;
    treeOrganizationUnits: any[];
    accountLists: IAccountList[];
    accountListsData: IAccountList[];
    currencies: ICurrency[];
    expenseItems: IExpenseItem[];
    listExpenseItemsChecked: any[];
    listAccountListsChecked: any[];
    expenseItemsData: IExpenseItem[];
    currencyCode: string;
    isDependent: boolean;
    ROLE_KetXuat = ROLE.SoKeToanChiTietQuyTienMat_KetXuat;
    isShowDependent: boolean;
    typeShowCurrency: boolean;
    currencyID: string;
    isKetXuat: boolean;
    accountNumber: string;
    accountName: string;
    expenseItemCode: string;
    expenseItemName: string;
    listTHeadExpenseItem: string[];
    listKeyExpenseItem: any[];
    listSearchExpenseItem: any;
    navigateFormExpenseItem: string;
    keySearchExpenseItem: any[];
    treeExpenseLists: ITreeExpenseList[];
    listParentExpenseItem: ITreeExpenseList[];
    flatTreeExpenseItem: ITreeExpenseList[];
    indexExpenseItem: number;
    minExpenseItem: number;
    nameSearchExpenseItem: string;

    listTHeadAccountList: string[];
    listKeyAccountList: any[];
    listSearchAccountList: any;
    navigateFormAccountList: string;
    keySearchAccountList: any[];
    treeAccountLists: ITreeExpenseList[];
    listParentAccountList: ITreeExpenseList[];
    flatTreeAccountList: ITreeExpenseList[];
    indexAccountList: number;
    minAccountList: number;
    nameSearchAccountList: string;

    constructor(
        public activeModal: NgbActiveModal,
        private principal: Principal,
        private toastr: ToastrService,
        public translate: TranslateService,
        public eventManager: JhiEventManager,
        private baoCaoService: BaoCaoService,
        public utilsService: UtilsService,
        private organizationUnitService: OrganizationUnitService,
        private accountListService: AccountListService,
        private currencyService: CurrencyService,
        private expenseItemService: ExpenseItemService
    ) {
        this.requestReport = {};
    }

    ngOnInit(): void {
        this.nameSearchExpenseItem = 'ExpenseItem';
        this.nameSearchAccountList = 'AccountList';
        this.minExpenseItem = 0;
        this.typeShowCurrency = false;
        this.groupTheSameItem = true;
        this.isDependent = false;
        this.treeExpenseLists = [];
        this.treeAccountLists = [];
        this.accountLists = [];
        this.accountListsData = [];

        //Expense Item
        this.listTHeadExpenseItem = [];
        this.listKeyExpenseItem = [];
        this.keySearchExpenseItem = [];
        this.listSearchExpenseItem = {};
        this.listExpenseItemsChecked = [];
        this.listTHeadExpenseItem.push('checked');
        this.listTHeadExpenseItem.push('ebwebApp.expenseItem.expenseItemCode');
        this.listTHeadExpenseItem.push('ebwebApp.expenseItem.expenseItemName');
        this.listKeyExpenseItem.push({ key: 'checked', type: 1 });
        this.listKeyExpenseItem.push({ key: 'expenseItemCode', type: 1 });
        this.listKeyExpenseItem.push({ key: 'expenseItemName', type: 1 });
        this.listSearchExpenseItem = {
            expenseItemCode: '',
            expenseItemName: ''
        };
        this.keySearchExpenseItem.push(this.nameSearchExpenseItem);
        this.keySearchExpenseItem.push('expenseItemCode');
        this.keySearchExpenseItem.push('expenseItemName');

        //Account Lists
        this.listTHeadAccountList = [];
        this.listKeyAccountList = [];
        this.keySearchAccountList = [];
        this.listSearchAccountList = {};
        this.listAccountListsChecked = [];
        this.listTHeadAccountList.push('checked');
        this.listTHeadAccountList.push('ebwebApp.accountList.accountNumber');
        this.listTHeadAccountList.push('ebwebApp.accountList.accountName');
        this.listKeyAccountList.push({ key: 'checked', type: 1 });
        this.listKeyAccountList.push({ key: 'accountNumber', type: 1 });
        this.listKeyAccountList.push({ key: 'accountName', type: 1 });
        this.listSearchAccountList = {
            accountNumber: '',
            accountName: ''
        };
        this.keySearchAccountList.push(this.nameSearchAccountList);
        this.keySearchAccountList.push('accountNumber');
        this.keySearchAccountList.push('accountName');
        this.principal.identity().then(account => {
            this.account = account;
            this.dataSession = {};
            this.currencyCode = this.account.organizationUnit.currencyID;
            this.currencyID = this.account.organizationUnit.currencyID;
            this.isKetXuat = !this.utilsService.isPackageDemo(this.account);
            this.treeOrganizationUnits = [];
            this.listTimeLine = this.utilsService.getCbbTimeLine();
            this.timeLineVoucher = this.listTimeLine[4].value;
            this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
            // this.accountListService.getAccountForTHCPTheoKMCP().subscribe((res: HttpResponse<IAccountList[]>) => {
            //     this.accountListsData = res.body.sort((a, b) => a.accountNumber.localeCompare(b.accountNumber));
            //     this.accountLists = res.body.sort((a, b) => a.accountNumber.localeCompare(b.accountNumber));
            // });
            this.getSessionData();
            this.organizationUnitService.getChildCompanyByID().subscribe((res: HttpResponse<any>) => {
                this.treeOrganizationUnits = res.body.orgTrees;
                this.organizationUnit = res.body.currentOrgLogin;
                this.selectChangeOrg();
            });
        });
        this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencies = res.body;
        });
        this.loadAll();
        this.registerChangeListSearchExpenseItem();
        this.registerChangeCheckBox();
        // this.expenseItemService.getExpenseItems().subscribe((res: HttpResponse<IExpenseItem[]>) => {
        //     this.expenseItemsData = res.body;
        //     this.expenseItems = res.body;
        // });
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('searchTongHopChiPhiTheoKhoanMucChiPhi'));
        if (this.dataSession) {
            this.toDate = this.dataSession.toDate;
            this.fromDate = this.dataSession.fromDate;
            this.currencyCode = this.dataSession.currencyID;
            this.timeLineVoucher = this.dataSession.timeLineVoucher ? this.dataSession.timeLineVoucher : '0';
        } else {
            this.dataSession = {};
        }
    }

    setSessionData() {
        this.dataSession.toDate = this.toDate;
        this.dataSession.fromDate = this.fromDate;
        this.dataSession.timeLineVoucher = this.timeLineVoucher ? this.timeLineVoucher : '';
        this.dataSession.currencyID = this.currencyCode;
        sessionStorage.setItem('searchTongHopChiPhiTheoKhoanMucChiPhi', JSON.stringify(this.dataSession));
    }

    showReport() {
        if (this.checkError()) {
            this.setSessionData();
            this.listExpenseItemsChecked = [];
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT_SEARCH);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT_SEARCH);
            this.requestReport.groupTheSameItem = this.groupTheSameItem;
            this.requestReport.typeReport = BaoCao.TongHop.TONG_HOP_CHI_PHI_THEO_KHOAN_MUC_CHI_PHI;
            this.requestReport.companyID = this.organizationUnit.parent.id;
            this.requestReport.dependent = this.isDependent;
            this.addAllExpenseItems(this.listParentExpenseItem, this.minExpenseItem);
            this.addAllAccountLists(this.listParentAccountList, this.minAccountList);
            this.requestReport.listExpenseItems = this.listExpenseItemsChecked;
            this.requestReport.accountList = this.listAccountListsChecked;
            if (
                (this.requestReport.listExpenseItems && this.requestReport.listExpenseItems.length === 0) ||
                !this.requestReport.listExpenseItems
            ) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.statisticsCode.nullStatisticsCode'),
                    this.translate.instant('ebwebApp.mCReceipt.error.error')
                );
                return true;
            }
            if ((this.requestReport.accountList && this.requestReport.accountList.length === 0) || !this.requestReport.accountList) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.accountNumberNotBeBlank'),
                    this.translate.instant('ebwebApp.mBDeposit.message')
                );
                return;
            }
            this.baoCaoService.getReport(this.requestReport);
        }
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

    addAllAccountLists(listParentAccount: ITreeAccountList[], grade) {
        for (let i = 0; i < listParentAccount.length; i++) {
            if (listParentAccount[i].parent.checked) {
                this.listAccountListsChecked.push(listParentAccount[i].parent.accountNumber);
            }
            if (listParentAccount[i].children && listParentAccount[i].children.length > 0) {
                this.addAllAccountLists(listParentAccount[i].children, grade + 1);
            }
        }
    }

    checkError(): boolean {
        if (!this.organizationUnit) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.organizationUnitNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
        }
        if (!this.fromDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.soQuyTienMatPopup.fromDateNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
        }
        if (!this.toDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.soQuyTienMatPopup.toDateNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
        }
        if (this.fromDate > this.toDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBDeposit.fromDateMustBeLessThanToDate'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
        }
        if (!this.currencyCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.currencyCodeNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
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

    isCheckAllAccountList() {
        if (this.accountLists) {
            return this.accountLists.every(item => item.checked) && this.accountLists.length;
        } else {
            return false;
        }
    }

    checkAllAccountList() {
        const isCheck = this.accountLists.every(item => item.checked) && this.accountLists.length;
        this.accountLists.forEach(item => (item.checked = !isCheck));
    }

    isCheckAllExpenseItem() {
        if (this.accountLists) {
            return this.expenseItems.every(item => item.checked) && this.expenseItems.length;
        } else {
            return false;
        }
    }

    checkAllExpenseItem() {
        const isCheck = this.expenseItems.every(item => item.checked) && this.expenseItems.length;
        this.expenseItems.forEach(item => (item.checked = !isCheck));
    }

    check(accountList: IAccountList) {
        accountList.checked = !accountList.checked;
    }

    exportExcel() {
        if (this.checkError()) {
            this.setSessionData();
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT_SEARCH);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT_SEARCH);
            this.requestReport.groupTheSameItem = this.groupTheSameItem;
            this.requestReport.typeReport = BaoCao.TongHop.TONG_HOP_CHI_PHI_THEO_KHOAN_MUC_CHI_PHI;
            this.requestReport.fileName = BaoCao.TongHop.TONG_HOP_CHI_PHI_THEO_KHOAN_MUC_CHI_PHI_XLS;
            this.requestReport.companyID = this.organizationUnit.parent.id;
            this.requestReport.dependent = this.isDependent;
            if (this.currencyID === this.currencyCode) {
                this.requestReport.typeShowCurrency = false;
            } else {
                this.requestReport.typeShowCurrency = this.typeShowCurrency;
            }
            this.addAllExpenseItems(this.listParentExpenseItem, this.minExpenseItem);
            this.addAllAccountLists(this.listParentAccountList, this.minAccountList);
            this.requestReport.listExpenseItems = this.listExpenseItemsChecked;
            this.requestReport.accountList = this.listAccountListsChecked;
            this.requestReport.listExpenseItems = this.listExpenseItemsChecked;
            if (
                (this.requestReport.listExpenseItems && this.requestReport.listExpenseItems.length === 0) ||
                !this.requestReport.listExpenseItems
            ) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.baoCao.tongHopChiPhiTheoKhoanMucChiPhiPopup.expenseItemCodeNotBeBlank'),
                    this.translate.instant('ebwebApp.mBDeposit.message')
                );
                return;
            }
            if ((this.requestReport.accountList && this.requestReport.accountList.length === 0) || !this.requestReport.accountList) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.accountNumberNotBeBlank'),
                    this.translate.instant('ebwebApp.mBDeposit.message')
                );
                return;
            }
            if (
                (this.requestReport.listExpenseItems && this.requestReport.listExpenseItems.length === 0) ||
                !this.requestReport.listExpenseItems
            ) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.baoCao.tongHopChiPhiKhoanMucChiPhi.expenseItemCodeNotBeBlank'),
                    this.translate.instant('ebwebApp.mBDeposit.message')
                );
                return;
            }
            this.baoCaoService.exportExcel(this.requestReport).subscribe(
                res => {
                    const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                    const fileURL = URL.createObjectURL(blob);

                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    const name = BaoCao.TongHop.TONG_HOP_CHI_PHI_THEO_KHOAN_MUC_CHI_PHI_XLS;
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

    selectChangeOrg() {
        if (this.organizationUnit && this.organizationUnit.parent.id) {
            if (this.organizationUnit.parent.unitType === 1) {
                this.isShowDependent = false;
            } else {
                this.isShowDependent = this.checkChildren(this.organizationUnit);
            }
            this.expenseItemService
                .getExpenseItemsAndDependent({ isDependent: this.isDependent, orgID: this.organizationUnit.parent.id })
                .subscribe((res: HttpResponse<IExpenseItem[]>) => {
                    this.expenseItems = res.body;
                    this.indexExpenseItem = 0;
                    this.listParentExpenseItem = [];
                    this.flatTreeExpenseItem = [];
                    this.expenseItemsData = res.body;
                    for (let i = 0; i < this.expenseItemsData.length; i++) {
                        if (!this.expenseItemsData[i].expenseItemCode) {
                            this.expenseItemsData[i].expenseItemCode = '';
                        }
                        if (!this.expenseItemsData[i].expenseItemName) {
                            this.expenseItemsData[i].expenseItemName = '';
                        }
                    }
                    this.expenseItems = res.body;
                    if (this.expenseItems && this.expenseItems.length > 0) {
                        this.minExpenseItem = this.expenseItems[0].grade;
                        for (let i = 0; i < this.expenseItems.length; i++) {
                            if (this.minExpenseItem > this.expenseItems[i].grade) {
                                this.minExpenseItem = this.expenseItems[i].grade;
                            }
                        }
                        const expenseItem = this.expenseItems.filter(a => a.grade === 1);
                        for (let i = 0; i < expenseItem.length; i++) {
                            this.listParentExpenseItem.push(Object.assign({}));
                            this.listParentExpenseItem[i].parent = expenseItem[i];
                        }
                        this.treeExpenseItems(this.listParentExpenseItem, this.minExpenseItem);
                        this.setIndexTreeExpenseItem(this.listParentExpenseItem, this.minExpenseItem);
                    }
                });
        }
    }

    checkChildren(treeOrganizationUnit: TreeOrganizationUnit): boolean {
        if (treeOrganizationUnit && treeOrganizationUnit.children && treeOrganizationUnit.children.length > 0) {
            for (let i = 0; i < treeOrganizationUnit.children.length; i++) {
                if (
                    treeOrganizationUnit.children[i].parent.accType === 0 &&
                    treeOrganizationUnit.children[i].parent.unitType === 1 &&
                    treeOrganizationUnit.children[i].parent.parentID === this.organizationUnit.parent.id
                ) {
                    return true;
                }
            }
        }
        return false;
    }

    selectChangeIsDependent() {
        this.expenseItemService
            .getExpenseItemsAndDependent({ isDependent: this.isDependent, orgID: this.organizationUnit.parent.id })
            .subscribe((res: HttpResponse<IExpenseItem[]>) => {
                this.expenseItems = res.body;
                this.indexExpenseItem = 0;
                this.listParentExpenseItem = [];
                this.flatTreeExpenseItem = [];
                this.expenseItemsData = res.body;
                for (let i = 0; i < this.expenseItemsData.length; i++) {
                    if (!this.expenseItemsData[i].expenseItemCode) {
                        this.expenseItemsData[i].expenseItemCode = '';
                    }
                    if (!this.expenseItemsData[i].expenseItemName) {
                        this.expenseItemsData[i].expenseItemName = '';
                    }
                }
                this.expenseItems = res.body;
                if (this.expenseItems && this.expenseItems.length > 0) {
                    this.minExpenseItem = this.expenseItems[0].grade;
                    for (let i = 0; i < this.expenseItems.length; i++) {
                        if (this.minExpenseItem > this.expenseItems[i].grade) {
                            this.minExpenseItem = this.expenseItems[i].grade;
                        }
                    }
                    const expenseItem = this.expenseItems.filter(a => a.grade === 1);
                    for (let i = 0; i < expenseItem.length; i++) {
                        this.listParentExpenseItem.push(Object.assign({}));
                        this.listParentExpenseItem[i].parent = expenseItem[i];
                    }
                    this.treeExpenseItems(this.listParentExpenseItem, this.minExpenseItem);
                    this.setIndexTreeExpenseItem(this.listParentExpenseItem, this.minExpenseItem);
                }
            });
    }

    changeSearchAccount() {
        this.accountLists = this.accountListsData.filter(
            x =>
                (this.accountNumber ? x.accountNumber.toLowerCase().startsWith(this.accountNumber.toLowerCase()) : true) &&
                (this.accountName ? x.accountName.toLowerCase().includes(this.accountName.toLowerCase()) : true)
        );
    }

    changeSearchExpenseItem() {
        this.expenseItems = this.expenseItemsData.filter(
            x =>
                (this.expenseItemCode ? x.expenseItemCode.toLowerCase().startsWith(this.expenseItemCode.toLowerCase()) : true) &&
                (this.expenseItemName ? x.expenseItemName.toLowerCase().includes(this.expenseItemName.toLowerCase()) : true)
        );
    }

    loadAll() {
        // let index = 0;
        this.expenseItemService.getExpenseItems().subscribe((res: HttpResponse<IExpenseItem[]>) => {
            this.indexExpenseItem = 0;
            this.listParentExpenseItem = [];
            this.flatTreeExpenseItem = [];
            this.expenseItemsData = res.body;
            for (let i = 0; i < this.expenseItemsData.length; i++) {
                if (!this.expenseItemsData[i].expenseItemCode) {
                    this.expenseItemsData[i].expenseItemCode = '';
                }
                if (!this.expenseItemsData[i].expenseItemName) {
                    this.expenseItemsData[i].expenseItemName = '';
                }
            }
            this.expenseItems = res.body;
            if (this.expenseItems && this.expenseItems.length > 0) {
                this.minExpenseItem = this.expenseItems[0].grade;
                for (let i = 0; i < this.expenseItems.length; i++) {
                    if (this.minExpenseItem > this.expenseItems[i].grade) {
                        this.minExpenseItem = this.expenseItems[i].grade;
                    }
                }
                const expenseItem = this.expenseItems.filter(a => a.grade === 1);
                for (let i = 0; i < expenseItem.length; i++) {
                    this.listParentExpenseItem.push(Object.assign({}));
                    this.listParentExpenseItem[i].parent = expenseItem[i];
                }
                this.treeExpenseItems(this.listParentExpenseItem, this.minExpenseItem);
                this.setIndexTreeExpenseItem(this.listParentExpenseItem, this.minExpenseItem);
            }
        });
        // let index = 0;
        this.accountListService.getAccountForTHCPTheoKMCP().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.indexAccountList = 0;
            this.listParentAccountList = [];
            this.flatTreeAccountList = [];
            this.accountListsData = res.body;
            for (let i = 0; i < this.accountListsData.length; i++) {
                if (!this.accountListsData[i].accountNumber) {
                    this.accountListsData[i].accountNumber = '';
                }
                if (!this.accountListsData[i].accountName) {
                    this.accountListsData[i].accountName = '';
                }
            }
            this.accountLists = res.body;
            if (this.accountLists && this.accountLists.length > 0) {
                this.minAccountList = this.accountLists[0].grade;
                for (let i = 0; i < this.accountLists.length; i++) {
                    if (this.minAccountList > this.accountLists[i].grade) {
                        this.minAccountList = this.accountLists[i].grade;
                    }
                }
                const accountList = this.accountLists.filter(a => a.grade === 1);
                for (let i = 0; i < accountList.length; i++) {
                    this.listParentAccountList.push(Object.assign({}));
                    this.listParentAccountList[i].parent = accountList[i];
                }
                this.treeAccount(this.listParentAccountList, this.minAccountList);
                this.setIndexTreeAccountLists(this.listParentAccountList, this.minAccountList);
            }
        });
    }

    treeExpenseItems(expenseItem: ITreeExpenseList[], grade) {
        for (let i = 0; i < expenseItem.length; i++) {
            const newList = this.expenseItems.filter(a => a.parentID === expenseItem[i].parent.id);
            expenseItem[i].children = [];
            for (let j = 0; j < newList.length; j++) {
                // if (j === 0) {
                //     accountList[i].children = [];
                // }
                expenseItem[i].children.push(Object.assign({}));
                expenseItem[i].children[j].parent = newList[j];
            }
            if (expenseItem[i].children && expenseItem[i].children.length > 0) {
                this.treeExpenseItems(expenseItem[i].children, grade + 1);
            }
        }
        // for (let i = 0; i < accountList.length; i++) {
        //     if (accountList[i].parent.accountGroupKind !== null || accountList[i].parent.accountGroupKind !== undefined) {
        //         accountList[i].parent.getAccountGroupKind = this.getAccountGroupKind(accountList[i].parent.accountGroupKind);
        //     }
        // }
    }

    treeAccount(accountLists: ITreeAccountList[], grade) {
        for (let i = 0; i < accountLists.length; i++) {
            const newList = this.accountLists.filter(a => a.parentAccountID === accountLists[i].parent.id);
            accountLists[i].children = [];
            for (let j = 0; j < newList.length; j++) {
                // if (j === 0) {
                //     accountList[i].children = [];
                // }
                accountLists[i].children.push(Object.assign({}));
                accountLists[i].children[j].parent = newList[j];
            }
            if (accountLists[i].children && accountLists[i].children.length > 0) {
                this.treeAccount(accountLists[i].children, grade + 1);
            }
        }
        // for (let i = 0; i < accountList.length; i++) {
        //     if (accountList[i].parent.accountGroupKind !== null || accountList[i].parent.accountGroupKind !== undefined) {
        //         accountList[i].parent.getAccountGroupKind = this.getAccountGroupKind(accountList[i].parent.accountGroupKind);
        //     }
        // }
    }

    registerChangeListSearchExpenseItem() {
        this.eventSubscriber = this.eventManager.subscribe('listSearch' + this.nameSearchExpenseItem, response => {
            this.listSearchExpenseItem = response.data;
            this.listParentExpenseItem = [];
            this.flatTreeExpenseItem = [];
            this.expenseItems = this.expenseItemsData.filter(
                a =>
                    a.expenseItemCode.toUpperCase().includes(this.listSearchExpenseItem.expenseItemCode.toUpperCase()) &&
                    a.expenseItemName.toUpperCase().includes(this.listSearchExpenseItem.expenseItemName.toUpperCase())
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
                this.listParentExpenseItem.push(Object.assign({}));
                this.listParentExpenseItem[i].parent = listAccount[i];
            }
            this.treeExpenseItems(this.listParentExpenseItem, grade);
            this.setIndexTreeExpenseItem(this.listParentExpenseItem, grade);
        });
        this.eventSubscriber = this.eventManager.subscribe('listSearch' + this.nameSearchAccountList, response => {
            this.listSearchAccountList = response.data;
            this.listParentAccountList = [];
            this.flatTreeAccountList = [];
            this.accountLists = this.accountListsData.filter(
                a =>
                    a.accountNumber.toUpperCase().includes(this.listSearchAccountList.accountNumber.toUpperCase()) &&
                    a.accountName.toUpperCase().includes(this.listSearchAccountList.accountName.toUpperCase())
            );
            this.treeforSearchAccountList();
            let grade;
            if (this.accountLists.length) {
                grade = this.accountLists[0].grade;
            }
            for (let i = 0; i < this.accountLists.length; i++) {
                if (this.accountLists[i].grade < grade) {
                    grade = this.accountLists[i].grade;
                }
            }
            const listAccount = this.accountLists.filter(a => a.grade === grade);
            for (let i = 0; i < listAccount.length; i++) {
                this.listParentAccountList.push(Object.assign({}));
                this.listParentAccountList[i].parent = listAccount[i];
            }
            this.treeAccount(this.listParentAccountList, grade);
            this.setIndexTreeAccountLists(this.listParentAccountList, grade);
        });
    }

    registerChangeCheckBox() {
        this.eventSubscriber = this.eventManager.subscribe(
            'sendTree' + (this.nameSearchExpenseItem ? this.nameSearchExpenseItem : ''),
            response => {
                this.listParentExpenseItem = [];
                this.listExpenseItemsChecked = [];
                this.listParentExpenseItem = response.data;
            }
        );
        this.eventSubscriber = this.eventManager.subscribe(
            'sendTree' + (this.nameSearchAccountList ? this.nameSearchAccountList : ''),
            response => {
                this.listParentAccountList = [];
                this.listAccountListsChecked = [];
                this.listParentAccountList = response.data;
            }
        );
    }

    treeforSearchExpenseItem() {
        for (let i = 0; i < this.expenseItems.length; i++) {
            if (this.expenseItems[i].parentID) {
                this.getAllParentExpenseItem(this.expenseItems[i]);
            }
        }
    }

    treeforSearchAccountList() {
        for (let i = 0; i < this.accountLists.length; i++) {
            if (this.accountLists[i].parentAccountID) {
                this.getAllParentAccountList(this.accountLists[i]);
            }
        }
    }

    getAllParentExpenseItem(expenseItem: IExpenseItem) {
        const addAccount = this.expenseItemsData.find(a => a.id === expenseItem.parentID);
        const checkExistAccount = this.expenseItems.find(a => a.id === addAccount.id);
        if (addAccount && !checkExistAccount) {
            this.expenseItems.push(addAccount);
            if (addAccount.parentID) {
                this.getAllParentExpenseItem(addAccount);
            }
        }
    }

    getAllParentAccountList(accountList: IAccountList) {
        const addAccount = this.accountListsData.find(a => a.id === accountList.parentAccountID);
        const checkExistAccount = this.accountLists.find(a => a.id === addAccount.id);
        if (addAccount && !checkExistAccount) {
            this.accountLists.push(addAccount);
            if (addAccount.parentAccountID) {
                this.getAllParentAccountList(addAccount);
            }
        }
    }

    setIndexTreeExpenseItem(expenseLists: ITreeExpenseList[], grade) {
        for (let i = 0; i < expenseLists.length; i++) {
            expenseLists[i].index = this.indexExpenseItem;
            this.indexExpenseItem++;
            if (expenseLists[i].children && expenseLists[i].children.length > 0) {
                this.setIndexTreeExpenseItem(expenseLists[i].children, grade + 1);
            }
        }
    }

    setIndexTreeAccountLists(accountLists: ITreeAccountList[], grade) {
        for (let i = 0; i < accountLists.length; i++) {
            accountLists[i].index = this.indexAccountList;
            this.indexAccountList++;
            if (accountLists[i].children && accountLists[i].children.length > 0) {
                this.setIndexTreeAccountLists(accountLists[i].children, grade + 1);
            }
        }
    }
}
