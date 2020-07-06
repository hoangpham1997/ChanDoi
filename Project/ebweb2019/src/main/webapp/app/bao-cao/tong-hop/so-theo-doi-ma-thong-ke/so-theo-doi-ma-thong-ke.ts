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
import { HttpResponse } from '@angular/common/http';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { AccountListService } from 'app/danhmuc/account-list';
import { AccountList, IAccountList } from 'app/shared/model/account-list.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { ROLE } from 'app/role.constants';
import { TreeOrganizationUnit } from 'app/shared/model/organization-unit-tree.model';
import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { ITreeStatisticsCode } from 'app/shared/model/statistics-code-tree.model';
import { StatisticsCodeService } from 'app/danhmuc/statistics-code';
import { IStatisticsCode, StatisticsCode } from 'app/shared/model/statistics-code.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { KhoanMucChiPhiListService } from 'app/danhmuc/khoan-muc-chi-phi-list';
import { ITreeAccountList } from 'app/shared/model/account-list-tree.model';
import { ITreeExpenseList } from 'app/shared/model/expense-list-tree.model';
import { Subscription } from 'rxjs';

@Component({
    selector: 'eb-so-theo-doi-ma-thong-ke',
    templateUrl: './so-theo-doi-ma-thong-ke.html',
    styleUrls: ['so-theo-doi-ma-thong-ke.css']
})
export class SoTheoDoiMaThongKeComponent implements OnInit {
    status: boolean;
    requestReport: RequestReport;
    eventSubscriber: Subscription;
    listTimeLine: any[];
    account: any;
    modalRef: NgbModalRef;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    treeOrganizationUnits: any[];
    accountLists: IAccountList[];
    materialGoodsCategories: IMaterialGoodsCategory[];
    ROLE_KetXuat = ROLE.SoChiTietMuaHang_KetXuat;
    listParentStatisticsCode: ITreeStatisticsCode[];
    listParentAccount: ITreeAccountList[];
    listParentExpenseItem: ITreeExpenseList[];
    statisticsCodes: IStatisticsCode[];
    expenseItems: IExpenseItem[];
    expenseItemsFilter: IExpenseItem[];
    listKeyStatisticsCode: any[];
    listHeadStatisticsCode: string[];
    listKeyAccountList: any[];
    listHeadAccountList: string[];
    listKeyExpenseItem: any[];
    listHeadExpenseItem: string[];
    isKetXuat: boolean;
    navigateForm: string;
    listSearchStatisticsCode: any;
    listSearchExpenseItem: any;
    listSearchAccountList: any;
    keySearchStatisticsCode: any[];
    keySearchExpenseItem: any[];
    keySearchAccountList: any[];
    accountNumber: string;
    accountName: string;
    expenseItemCode: string;
    expenseItemName: string;
    statisticsCode: string;
    statisticsCodeName: string;
    index: number;
    minS: number;
    minA: number;
    minE: number;
    listExpenseItemsChecked: any[];
    expenseItemsData: IExpenseItem[];
    listAccountListChecked: any[];
    accountListData: IAccountList[];
    listStatisticsCodeChecked: any[];
    statisticsCodeData: IStatisticsCode[];
    nameSearchStatisticsCode: string;
    nameSearchAccountList: string;
    nameSearchExpenseItem: string;

    statisticsCodesFilter: StatisticsCode[];
    accountListsFilter: AccountList[];
    BaoCao = BaoCao;
    reportType: string;
    sessionSearch: {
        type: string;
        data: {
            dependent: boolean;
            isSimilarSum: boolean;
            showAccumAmount: boolean;
            groupTheSameItem: boolean;
            timeLineVoucher: any;
            fromDate: string;
            toDate: string;
            organizationUnit: TreeOrganizationUnit;
            statisticsCodes: any;
            statisticsCodeFilter: string;
            statisticsNameFilter: string;
            statisticsCodesSelected: string[];
            accountLists: any;
            accountListCodeFilter: string;
            accountListNameFilter: string;
            accountListsSelected: string[];
            accountListsFilter: string[];
            expenseItems: any;
            expenseItemCodeFilter: string;
            expenseItemNameFilter: string;
            expenseItemsSelected: string[];
            expenseItemsFilter: string[];
            hiddenDependent: boolean;
        };
    };
    isShowDependent: boolean;
    constructor(
        public activeModal: NgbActiveModal,
        private principal: Principal,
        private toastr: ToastrService,
        public translate: TranslateService,
        public eventManager: JhiEventManager,
        private baoCaoService: BaoCaoService,
        public utilsService: UtilsService,
        public currencyService: CurrencyService,
        public statisticsCodeService: StatisticsCodeService,
        private organizationUnitService: OrganizationUnitService,
        private accountListService: AccountListService,
        private expenseItemService: KhoanMucChiPhiListService
    ) {}

    ngOnInit() {
        this.minS = 0;
        this.minA = 0;
        this.minE = 0;
        this.nameSearchExpenseItem = 'ExpenseItem';
        this.nameSearchAccountList = 'AccountList';
        this.nameSearchStatisticsCode = 'StatisticsCode';
        this.listKeyStatisticsCode = [];
        this.listHeadStatisticsCode = [];
        this.listKeyAccountList = [];
        this.listHeadAccountList = [];
        this.listKeyExpenseItem = [];
        this.listHeadExpenseItem = [];
        this.keySearchStatisticsCode = [];
        this.listSearchStatisticsCode = {};
        this.keySearchAccountList = [];
        this.listSearchAccountList = {};
        this.keySearchExpenseItem = [];
        this.listSearchExpenseItem = {};
        this.listStatisticsCodeChecked = [];
        this.listExpenseItemsChecked = [];
        this.listAccountListChecked = [];

        this.sessionSearch = this.baoCaoService.getSessionData(this.reportType);

        if (this.sessionSearch.data.isSimilarSum === undefined || this.sessionSearch.data.isSimilarSum === null) {
            this.sessionSearch.data.isSimilarSum = true;
        }
        if (this.sessionSearch.data.groupTheSameItem === undefined || this.sessionSearch.data.groupTheSameItem === null) {
            this.sessionSearch.data.groupTheSameItem = true;
        }
        if (this.sessionSearch.data.showAccumAmount === undefined || this.sessionSearch.data.showAccumAmount === null) {
            this.sessionSearch.data.showAccumAmount = true;
        }

        this.sessionSearch.data.statisticsCodeFilter = '';
        this.sessionSearch.data.statisticsNameFilter = '';

        this.listHeadExpenseItem.push('checked');
        this.listHeadExpenseItem.push('ebwebApp.expenseItem.expenseItemCode');
        this.listHeadExpenseItem.push('ebwebApp.expenseItem.expenseItemName');
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

        this.listHeadStatisticsCode.push('checked');
        this.listHeadStatisticsCode.push('ebwebApp.statisticsCode.statisticsCode');
        this.listHeadStatisticsCode.push('ebwebApp.statisticsCode.statisticsCodeName');
        this.listKeyStatisticsCode.push({ key: 'checked', type: 1 });
        this.listKeyStatisticsCode.push({ key: 'statisticsCode', type: 1 });
        this.listKeyStatisticsCode.push({ key: 'statisticsCodeName', type: 1 });
        this.listSearchStatisticsCode = {
            statisticsCode: '',
            statisticsCodeName: ''
        };
        this.keySearchStatisticsCode.push(this.nameSearchStatisticsCode);
        this.keySearchStatisticsCode.push('statisticsCode');
        this.keySearchStatisticsCode.push('statisticsCodeName');

        this.listHeadAccountList.push('checked');
        this.listHeadAccountList.push('ebwebApp.accountList.accountNumber');
        this.listHeadAccountList.push('ebwebApp.accountList.accountName');
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
            this.treeOrganizationUnits = [];
            this.listTimeLine = this.utilsService.getCbbTimeLine();
            this.isKetXuat = !this.utilsService.isPackageDemo(account);
            if (this.sessionSearch.data.timeLineVoucher === null || this.sessionSearch.data.timeLineVoucher === undefined) {
                this.sessionSearch.data.timeLineVoucher = this.listTimeLine[4].value;
            }
            this.selectChangeBeginDateAndEndDate(this.sessionSearch.data.timeLineVoucher);
            this.organizationUnitService.getChildCompanyByID().subscribe((res: HttpResponse<any>) => {
                this.treeOrganizationUnits = res.body.orgTrees;
                // if (this.treeOrganizationUnits && this.treeOrganizationUnits.length > 0) {
                //     this.sessionSearch.data.organizationUnit =
                //         this.treeOrganizationUnits.find(
                //             x => this.sessionSearch.data.organizationUnit && x.value === this.sessionSearch.data.organizationUnit.parent.id
                //         ) || this.treeOrganizationUnits.find(x => (x.value = res.body.currentOrg.value));
                // } else {
                this.sessionSearch.data.organizationUnit = res.body.currentOrgLogin;
                // }
                this.baoCaoService.putSessionData(this.sessionSearch);
                this.sessionSearch = this.baoCaoService.getSessionData(this.reportType);
                this.selectChangeOrg();
            });
        });
        this.registerChangeListSearchStatisticsCode();
        // if (this.reportType === BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_TAI_KHOAN) {
        //     this.registerChangeListSearchAccountList();
        // } else if (this.reportType === BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_KHOAN_MUC_CHI_PHI) {
        //     this.registerChangeListSearchExpenseItem();
        // }
        this.registerChangeCheckBox();
    }

    loadStatisticsCodes() {
        this.statisticsCodeService
            .getStatisticsCodesSimilarBranch({
                similarBranch: this.sessionSearch.data.dependent ? this.sessionSearch.data.dependent : '',
                companyID: this.sessionSearch.data.organizationUnit ? this.sessionSearch.data.organizationUnit.parent.id : ''
            })
            .subscribe(res => {
                this.listParentStatisticsCode = [];
                this.index = 0;
                this.statisticsCodesFilter = this.statisticsCodes;
                this.statisticsCodes = res.body;
                this.statisticsCodeData = res.body;
                for (let i = 0; i < this.statisticsCodes.length; i++) {
                    if (!this.statisticsCodeData[i].statisticsCode) {
                        this.statisticsCodeData[i].statisticsCode = '';
                    }
                    if (!this.statisticsCodeData[i].statisticsCodeName) {
                        this.statisticsCodeData[i].statisticsCodeName = '';
                    }
                }

                if (this.statisticsCodes && this.statisticsCodes.length > 0) {
                    this.minS = this.statisticsCodes[0].grade;
                    for (let i = 0; i < this.statisticsCodes.length; i++) {
                        if (this.minS > this.statisticsCodes[i].grade) {
                            this.minS = this.statisticsCodes[i].grade;
                        }
                    }
                    const statisticsCode = this.statisticsCodes.filter(a => a.grade === 1);
                    for (let i = 0; i < statisticsCode.length; i++) {
                        this.listParentStatisticsCode.push(Object.assign({}));
                        this.listParentStatisticsCode[i].parent = statisticsCode[i];
                    }
                    this.tree(this.listParentStatisticsCode, this.minS);
                    this.setIndexTreeStatisticsCode(this.listParentStatisticsCode, this.minS);
                }
            });
    }

    tree(statisticsCode: ITreeStatisticsCode[], grade) {
        for (let i = 0; i < statisticsCode.length; i++) {
            const newList = this.statisticsCodes.filter(s => s.parentID === statisticsCode[i].parent.id);
            statisticsCode[i].children = [];
            for (let j = 0; j < newList.length; j++) {
                statisticsCode[i].children.push(Object.assign({}));
                statisticsCode[i].children[j].parent = newList[j];
            }
            if (statisticsCode[i].children && statisticsCode[i].children.length > 0) {
                this.tree(statisticsCode[i].children, grade + 1);
            }
        }
    }

    setIndexTreeStatisticsCode(statisticsCode: ITreeStatisticsCode[], grade) {
        for (let i = 0; i < statisticsCode.length; i++) {
            statisticsCode[i].index = this.index;
            this.index++;
            if (statisticsCode[i].children && statisticsCode[i].children.length > 0) {
                this.setIndexTreeStatisticsCode(statisticsCode[i].children, grade + 1);
            }
        }
    }

    treeAccount(accountList: ITreeAccountList[], grade) {
        for (let i = 0; i < accountList.length; i++) {
            const newList = this.accountLists.filter(s => s.parentAccountID === accountList[i].parent.id);
            accountList[i].children = [];
            for (let j = 0; j < newList.length; j++) {
                accountList[i].children.push(Object.assign({}));
                accountList[i].children[j].parent = newList[j];
            }
            if (accountList[i].children && accountList[i].children.length > 0) {
                this.treeAccount(accountList[i].children, grade + 1);
            }
        }
    }

    setIndexTreeAccountList(accountList: ITreeAccountList[], grade) {
        for (let i = 0; i < accountList.length; i++) {
            accountList[i].index = this.index;
            this.index++;
            if (accountList[i].children && accountList[i].children.length > 0) {
                this.setIndexTreeAccountList(accountList[i].children, grade + 1);
            }
        }
    }

    treeExpenseItem(expenseItem: ITreeExpenseList[], grade) {
        for (let i = 0; i < expenseItem.length; i++) {
            const newList = this.expenseItems.filter(s => s.parentID === expenseItem[i].parent.id);
            expenseItem[i].children = [];
            for (let j = 0; j < newList.length; j++) {
                expenseItem[i].children.push(Object.assign({}));
                expenseItem[i].children[j].parent = newList[j];
            }
            if (expenseItem[i].children && expenseItem[i].children.length > 0) {
                this.treeExpenseItem(expenseItem[i].children, grade + 1);
            }
        }
    }

    setIndexTreeExpenseItem(expenseItem: ITreeExpenseList[], grade) {
        for (let i = 0; i < expenseItem.length; i++) {
            expenseItem[i].index = this.index;
            this.index++;
            if (expenseItem[i].children && expenseItem[i].children.length > 0) {
                this.setIndexTreeExpenseItem(expenseItem[i].children, grade + 1);
            }
        }
    }

    loadAccountLists() {
        this.accountListService
            .getAccountListSimilarBranch({
                similarBranch: this.sessionSearch.data.dependent ? this.sessionSearch.data.dependent : '',
                companyID: this.sessionSearch.data.organizationUnit ? this.sessionSearch.data.organizationUnit.parent.id : ''
            })
            .subscribe(res => {
                this.listParentAccount = [];
                this.accountLists = res.body;
                this.accountListsFilter = this.accountLists;
                this.index = 0;

                this.accountListData = res.body;
                for (let i = 0; i < this.accountListData.length; i++) {
                    if (!this.accountListData[i].accountNumber) {
                        this.accountListData[i].accountNumber = '';
                    }
                    if (!this.accountListData[i].accountName) {
                        this.accountListData[i].accountName = '';
                    }
                }

                this.accountLists = res.body;
                if (this.accountLists && this.accountLists.length > 0) {
                    this.minA = this.accountLists[0].grade;
                    for (let i = 0; i < this.accountLists.length; i++) {
                        if (this.minA > this.accountLists[i].grade) {
                            this.minA = this.accountLists[i].grade;
                        }
                    }
                    const accountList = this.accountLists.filter(a => a.grade === 1);
                    for (let i = 0; i < accountList.length; i++) {
                        this.listParentAccount.push(Object.assign({}));
                        this.listParentAccount[i].parent = accountList[i];
                    }
                    this.treeAccount(this.listParentAccount, this.minA);
                    this.setIndexTreeAccountList(this.listParentAccount, this.minA);
                }
            });
    }

    loadExpenseItem() {
        this.expenseItemService
            .getExpenseItemSimilarBranch({
                similarBranch: this.sessionSearch.data.dependent ? this.sessionSearch.data.dependent : '',
                companyID: this.sessionSearch.data.organizationUnit ? this.sessionSearch.data.organizationUnit.parent.id : ''
            })
            .subscribe((res: HttpResponse<IExpenseItem[]>) => {
                this.listParentExpenseItem = [];
                this.expenseItems = res.body;
                this.expenseItemsFilter = this.expenseItems;
                this.index = 0;

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
                    this.minE = this.expenseItems[0].grade;
                    for (let i = 0; i < this.expenseItems.length; i++) {
                        if (this.minE > this.expenseItems[i].grade) {
                            this.minE = this.expenseItems[i].grade;
                        }
                    }
                    const expenseItem = this.expenseItems.filter(a => a.grade === 1);
                    for (let i = 0; i < expenseItem.length; i++) {
                        this.listParentExpenseItem.push(Object.assign({}));
                        this.listParentExpenseItem[i].parent = expenseItem[i];
                    }
                    this.treeExpenseItem(this.listParentExpenseItem, this.minE);
                    this.setIndexTreeExpenseItem(this.listParentExpenseItem, this.minE);
                }
            });
    }

    reloadData() {
        this.sessionSearch.data.accountLists = null;
        this.sessionSearch.data.statisticsCodes = null;
        this.loadStatisticsCodes();
        if (this.reportType === BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_TAI_KHOAN) {
            this.loadAccountLists();
        } else if (this.reportType === BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_KHOAN_MUC_CHI_PHI) {
            this.loadExpenseItem();
        }
    }

    accept() {
        if (this.isNotChecked() || this.checkErr()) {
            return;
        }
        if (!this.requestReport) {
            this.requestReport = new RequestReport();
        }
        this.requestReport.statisticsCodes = [];
        this.requestReport.accountList = [];
        this.requestReport.expenseItems = [];
        this.listExpenseItemsChecked = [];
        this.listAccountListChecked = [];
        this.listStatisticsCodeChecked = [];
        this.requestReport.toDate = moment(this.sessionSearch.data.toDate, DATE_FORMAT);
        this.requestReport.fromDate = moment(this.sessionSearch.data.fromDate, DATE_FORMAT);
        this.requestReport.companyID = this.sessionSearch.data.organizationUnit ? this.sessionSearch.data.organizationUnit.parent.id : null;
        this.requestReport.typeReport = this.reportType;
        this.requestReport.dependent = this.sessionSearch.data.dependent || false;
        // this.requestReport.statisticsCodes = this.statisticsCodes.filter(x => x.checked).map(c => c.id);
        this.addAllStatisticsCode(this.listParentStatisticsCode, this.minS);
        if (this.reportType === BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_TAI_KHOAN) {
            this.addAllAccountList(this.listParentAccount, this.minS);
            this.requestReport.accountList = this.listAccountListChecked;
        } else if (this.reportType === BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_KHOAN_MUC_CHI_PHI) {
            this.addAllExpenseItem(this.listParentExpenseItem, this.minE);
            this.requestReport.expenseItems = this.listExpenseItemsChecked;
        }
        this.requestReport.statisticsCodes = this.listStatisticsCodeChecked;
        if (
            (this.requestReport.statisticsCodes && this.requestReport.statisticsCodes.length === 0) ||
            !this.requestReport.statisticsCodes
        ) {
            this.toastr.error(
                this.translate.instant('ebwebApp.statisticsCode.nullStatisticsCode'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return;
        }
        // if (this.reportType === BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_TAI_KHOAN) {
        //     //this.requestReport.accountList = this.accountLists.filter(n => n.checked).map(n => n.accountNumber);
        //
        // } else if (this.reportType === BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_KHOAN_MUC_CHI_PHI) {
        //
        // }
        this.baoCaoService.getReport(this.requestReport);
    }

    checkErr() {
        if (!this.sessionSearch.data.organizationUnit) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.error.nullOrg'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return true;
        }
        if (!this.sessionSearch.data.fromDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.nullFromDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return true;
        }
        if (!this.sessionSearch.data.toDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.nullToDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return true;
        }
        if (this.sessionSearch.data.toDate && this.sessionSearch.data.fromDate) {
            if (moment(this.sessionSearch.data.toDate, DATE_FORMAT_SEARCH) < moment(this.sessionSearch.data.fromDate, DATE_FORMAT_SEARCH)) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mCReceipt.error.fromDateGreaterToDate'),
                    this.translate.instant('ebwebApp.mCReceipt.error.error')
                );
                return true;
            }
        }
        // if (this.statisticsCodes.filter(x => x.checked).length === 0) {
        //     this.toastr.error(
        //         this.translate.instant('ebwebApp.statisticsCode.nullStatisticsCode'),
        //         this.translate.instant('ebwebApp.mCReceipt.error.error')
        //     );
        //     return true;
        // }
        return false;
    }

    selectChangeBeginDateAndEndDate(intTimeLine: String) {
        if (intTimeLine) {
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
            this.sessionSearch.data.fromDate = this.objTimeLine.dtBeginDate;
            this.sessionSearch.data.toDate = this.objTimeLine.dtEndDate;
            this.changeSessionSearch();
        }
    }

    isNotChecked() {
        if (this.reportType === BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_TAI_KHOAN) {
            const isNotChecked = this.accountLists && this.accountLists.every(item => !item.checked);
            if (isNotChecked) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.accountList.notCheckAccount'),
                    this.translate.instant('ebwebApp.accountingObject.error')
                );
                return true;
            }
        } else if (this.reportType === BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_KHOAN_MUC_CHI_PHI) {
            const isNotChecked = this.expenseItems && this.expenseItems.every(item => !item.checked);
            if (isNotChecked) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.expenseItem.notCheckExpense'),
                    this.translate.instant('ebwebApp.accountingObject.error')
                );
                return true;
            }
        }
        return false;
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

    isCheckAll() {
        if (this.statisticsCodes) {
            return this.statisticsCodes.every(item => item.checked) && this.statisticsCodes.length;
        } else {
            return false;
        }
    }

    checkAll() {
        const isCheck = this.statisticsCodes.every(item => item.checked) && this.statisticsCodes.length;
        this.statisticsCodes.forEach(item => {
            item.checked = !isCheck;
            this.saveStatisticsCodeSearch(item);
        });
    }

    isCheckAllExpense() {
        if (this.expenseItems) {
            return this.expenseItems.every(item => item.checked) && this.expenseItems.length;
        }
        return false;
    }

    checkAllExpense() {
        const isCheck = this.expenseItems.every(item => item.checked) && this.expenseItems.length;
        this.expenseItems.forEach(item => {
            item.checked = !isCheck;
            this.saveExpenseItemSearch(item);
        });
    }

    isCheckAllAccount() {
        if (this.accountLists) {
            return this.accountLists.every(item => item.checked) && this.accountLists.length;
        }
        return false;
    }

    checkAllAccount() {
        const isCheck = this.accountLists.every(item => item.checked) && this.accountLists.length;
        this.accountLists.forEach(item => {
            item.checked = !isCheck;
            this.saveAccountListSearch(item);
        });
    }

    checkAccount(accountList) {
        accountList.checked = !accountList.checked;
        this.saveAccountListSearch(accountList);
    }

    checkExpense(expenseItem) {
        expenseItem.checked = !expenseItem.checked;
        this.saveExpenseItemSearch(expenseItem);
    }

    checkStatistics(statisticsCode: StatisticsCode) {
        statisticsCode.checked = !statisticsCode.checked;
        this.saveStatisticsCodeSearch(statisticsCode);
    }

    saveStatisticsCodeSearch(item) {
        if (item.checked) {
            if (!this.sessionSearch.data.statisticsCodesSelected) {
                this.sessionSearch.data.statisticsCodesSelected = [item.id];
            } else {
                this.sessionSearch.data.statisticsCodesSelected.push(item.id);
            }
        } else {
            this.sessionSearch.data.statisticsCodesSelected = this.sessionSearch.data.statisticsCodesSelected.filter(x => x !== item.id);
        }
        this.changeSessionSearch();
    }

    saveAccountListSearch(item) {
        if (item.checked) {
            if (!this.sessionSearch.data.accountListsSelected) {
                this.sessionSearch.data.accountListsSelected = [item.id];
            } else {
                this.sessionSearch.data.accountListsSelected.push(item.id);
            }
        } else {
            this.sessionSearch.data.accountListsSelected = this.sessionSearch.data.accountListsSelected.filter(x => x !== item.id);
        }
        this.changeSessionSearch();
    }

    saveExpenseItemSearch(item) {
        if (item.checked) {
            if (!this.sessionSearch.data.expenseItemsSelected) {
                this.sessionSearch.data.expenseItemsSelected = [item.id];
            } else {
                this.sessionSearch.data.expenseItemsSelected.push(item.id);
            }
        } else {
            this.sessionSearch.data.expenseItemsSelected = this.sessionSearch.data.expenseItemsSelected.filter(x => x !== item.id);
        }
        this.changeSessionSearch();
    }

    changeSCFilter() {
        if (this.sessionSearch.data.statisticsNameFilter && this.sessionSearch.data.statisticsCodeFilter) {
            this.statisticsCodesFilter = this.statisticsCodes.filter(
                x =>
                    x.statisticsCode.toLowerCase().includes(this.sessionSearch.data.statisticsCodeFilter.toLowerCase()) &&
                    x.statisticsCodeName.toLowerCase().includes(this.sessionSearch.data.statisticsNameFilter.toLowerCase())
            );
        } else if (this.sessionSearch.data.statisticsCodeFilter) {
            this.statisticsCodesFilter = this.statisticsCodes.filter(x =>
                x.statisticsCode.toLowerCase().includes(this.sessionSearch.data.statisticsCodeFilter.toLowerCase())
            );
        } else if (this.sessionSearch.data.statisticsNameFilter) {
            this.statisticsCodesFilter = this.statisticsCodes.filter(x =>
                x.statisticsCodeName.toLowerCase().includes(this.sessionSearch.data.statisticsNameFilter.toLowerCase())
            );
        } else {
            this.statisticsCodesFilter = this.statisticsCodes;
        }
        this.statisticsCodesFilter.forEach(item => {
            if (this.sessionSearch.data.statisticsCodesSelected && this.sessionSearch.data.statisticsCodesSelected.includes(item.id)) {
                item.checked = true;
            }
        });
        this.changeSessionSearch();
    }

    changeALFilter() {
        if (this.sessionSearch.data.accountListCodeFilter && this.sessionSearch.data.accountListNameFilter) {
            this.accountListsFilter = this.accountLists.filter(
                x =>
                    x.accountNumber.toLowerCase().includes(this.sessionSearch.data.accountListCodeFilter.toLowerCase()) &&
                    x.accountName.toLowerCase().includes(this.sessionSearch.data.accountListNameFilter.toLowerCase())
            );
        } else if (this.sessionSearch.data.accountListCodeFilter) {
            this.accountListsFilter = this.accountLists.filter(x =>
                x.accountNumber.toLowerCase().includes(this.sessionSearch.data.accountListCodeFilter.toLowerCase())
            );
        } else if (this.sessionSearch.data.accountListNameFilter) {
            this.accountListsFilter = this.accountLists.filter(x =>
                x.accountName.toLowerCase().includes(this.sessionSearch.data.accountListNameFilter.toLowerCase())
            );
        } else {
            this.accountListsFilter = this.accountLists;
        }
        this.accountListsFilter.forEach(item => {
            if (this.sessionSearch.data.accountListsSelected && this.sessionSearch.data.accountListsSelected.includes(item.id)) {
                item.checked = true;
            }
        });
        this.changeSessionSearch();
    }

    changeEIFilter() {
        if (this.sessionSearch.data.expenseItemCodeFilter && this.sessionSearch.data.expenseItemNameFilter) {
            this.expenseItemsFilter = this.expenseItems.filter(
                x =>
                    x.expenseItemCode.toLowerCase().includes(this.sessionSearch.data.expenseItemCodeFilter.toLowerCase()) &&
                    x.expenseItemName.toLowerCase().includes(this.sessionSearch.data.expenseItemNameFilter.toLowerCase())
            );
        } else if (this.sessionSearch.data.expenseItemCodeFilter) {
            this.expenseItemsFilter = this.expenseItems.filter(x =>
                x.expenseItemCode.toLowerCase().includes(this.sessionSearch.data.expenseItemCodeFilter.toLowerCase())
            );
        } else if (this.sessionSearch.data.expenseItemNameFilter) {
            this.expenseItemsFilter = this.expenseItems.filter(x =>
                x.expenseItemName.toLowerCase().includes(this.sessionSearch.data.expenseItemNameFilter.toLowerCase())
            );
        } else {
            this.expenseItemsFilter = this.expenseItems;
        }
        this.expenseItemsFilter.forEach(item => {
            if (this.sessionSearch.data.expenseItemsSelected && this.sessionSearch.data.expenseItemsSelected.includes(item.id)) {
                item.checked = true;
            }
        });
        this.changeSessionSearch();
    }

    changeSessionSearch() {
        this.baoCaoService.putSessionData(this.sessionSearch);
        this.sessionSearch = this.baoCaoService.getSessionData(this.reportType);
    }

    export() {
        if (this.isNotChecked() || this.checkErr()) {
            return;
        }
        if (!this.requestReport) {
            this.requestReport = new RequestReport();
        }
        this.requestReport.statisticsCodes = [];
        this.requestReport.accountList = [];
        this.requestReport.expenseItems = [];
        this.listExpenseItemsChecked = [];
        this.listAccountListChecked = [];
        this.listStatisticsCodeChecked = [];
        this.requestReport.toDate = moment(this.sessionSearch.data.toDate, DATE_FORMAT_SEARCH);
        this.requestReport.fromDate = moment(this.sessionSearch.data.fromDate, DATE_FORMAT_SEARCH);
        this.requestReport.companyID = this.sessionSearch.data.organizationUnit ? this.sessionSearch.data.organizationUnit.parent.id : null;
        this.requestReport.typeReport = this.reportType;
        if (this.reportType === BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_TAI_KHOAN) {
            this.requestReport.fileName = BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_TAI_KHOAN_XLS;
            //this.requestReport.accountList = this.accountLists.filter(n => n.checked).map(n => n.accountNumber);
        } else if (this.reportType === BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_KHOAN_MUC_CHI_PHI) {
            this.requestReport.fileName = BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_KHOAN_MUC_CHI_PHI_XLS;
            //this.requestReport.expenseItems = this.expenseItems.filter(n => n.checked).map(m => m.id);
        }
        this.requestReport.dependent = this.sessionSearch.data.dependent;
        //this.requestReport.statisticsCodes = this.statisticsCodes.filter(x => x.checked).map(c => c.id);
        this.addAllStatisticsCode(this.listParentStatisticsCode, this.minS);
        if (this.reportType === BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_TAI_KHOAN) {
            this.addAllAccountList(this.listParentAccount, this.minA);
            this.requestReport.accountList = this.listAccountListChecked;
        } else if (this.reportType === BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_KHOAN_MUC_CHI_PHI) {
            this.addAllExpenseItem(this.listParentExpenseItem, this.minE);
            this.requestReport.expenseItems = this.listExpenseItemsChecked;
        }
        this.requestReport.statisticsCodes = this.listStatisticsCodeChecked;
        if (
            (this.requestReport.statisticsCodes && this.requestReport.statisticsCodes.length === 0) ||
            !this.requestReport.statisticsCodes
        ) {
            this.toastr.error(
                this.translate.instant('ebwebApp.statisticsCode.nullStatisticsCode'),
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
                let fileExcel;
                if (this.reportType === BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_TAI_KHOAN) {
                    fileExcel = BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_TAI_KHOAN_XLS;
                } else if (this.reportType === BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_KHOAN_MUC_CHI_PHI) {
                    fileExcel = BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_KHOAN_MUC_CHI_PHI_XLS;
                }
                link.setAttribute('download', fileExcel);
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

    changeDependent() {
        this.changeSessionSearch();
        this.loadStatisticsCodes();
        if (this.reportType === BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_KHOAN_MUC_CHI_PHI) {
            this.loadExpenseItem();
        }
        // this.reloadData();
    }

    changeOrganizationUnit() {
        this.sessionSearch.data.dependent = false;
        this.changeSessionSearch();
        this.selectChangeOrg();
    }

    checkChildren(treeOrganizationUnit: TreeOrganizationUnit): boolean {
        if (treeOrganizationUnit && treeOrganizationUnit.children && treeOrganizationUnit.children.length > 0) {
            for (let i = 0; i < treeOrganizationUnit.children.length; i++) {
                if (treeOrganizationUnit.children[i].parent.accType === 0) {
                    return true;
                }
            }
        }
        return false;
    }

    selectChangeOrg() {
        if (this.sessionSearch.data.organizationUnit && this.sessionSearch.data.organizationUnit.parent.id) {
            if (this.sessionSearch.data.organizationUnit.parent.unitType === 1) {
                this.isShowDependent = false;
            } else {
                this.isShowDependent = this.checkChildren(this.sessionSearch.data.organizationUnit);
            }
            this.reloadData();
            // this.loadStatisticsCodes();
            // this.loadExpenseItem();
        }
    }

    registerChangeListSearchStatisticsCode() {
        this.eventSubscriber = this.eventManager.subscribe('listSearch' + this.nameSearchStatisticsCode, response => {
            this.listSearchStatisticsCode = response.data;
            this.listParentStatisticsCode = [];
            this.statisticsCodes = this.statisticsCodeData.filter(
                a =>
                    a.statisticsCode.toUpperCase().includes(this.listSearchStatisticsCode.statisticsCode.toUpperCase()) &&
                    a.statisticsCodeName.toUpperCase().includes(this.listSearchStatisticsCode.statisticsCodeName.toUpperCase())
            );
            this.treeforSearchStatisticsCode();
            let grade;
            if (this.statisticsCodes.length) {
                grade = this.statisticsCodes[0].grade;
            }
            for (let i = 0; i < this.statisticsCodes.length; i++) {
                if (this.statisticsCodes[i].grade < grade) {
                    grade = this.statisticsCodes[i].grade;
                }
            }
            const listAccount = this.statisticsCodes.filter(a => a.grade === grade);
            for (let i = 0; i < listAccount.length; i++) {
                this.listParentStatisticsCode.push(Object.assign({}));
                this.listParentStatisticsCode[i].parent = listAccount[i];
            }
            this.tree(this.listParentStatisticsCode, grade);
            this.setIndexTreeStatisticsCode(this.listParentStatisticsCode, grade);
        });
        if (this.reportType === BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_TAI_KHOAN) {
            this.registerChangeListSearchAccountList();
        } else if (this.reportType === BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_KHOAN_MUC_CHI_PHI) {
            this.registerChangeListSearchExpenseItem();
        }
    }

    registerChangeListSearchAccountList() {
        this.eventSubscriber = this.eventManager.subscribe('listSearch' + this.nameSearchAccountList, response => {
            this.listSearchAccountList = response.data;
            this.listParentAccount = [];
            this.accountLists = this.accountListData.filter(
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
                this.listParentAccount.push(Object.assign({}));
                this.listParentAccount[i].parent = listAccount[i];
            }
            this.treeAccount(this.listParentAccount, grade);
            this.setIndexTreeStatisticsCode(this.listParentAccount, grade);
        });
    }

    registerChangeListSearchExpenseItem() {
        this.eventSubscriber = this.eventManager.subscribe('listSearch' + this.nameSearchExpenseItem, response => {
            this.listSearchExpenseItem = response.data;
            this.listParentExpenseItem = [];
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
            this.tree(this.listParentAccount, grade);
            this.setIndexTreeExpenseItem(this.listParentExpenseItem, grade);
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
        const addAccount = this.expenseItemsData.find(a => a.id === expenseItem.parentID);
        const checkExistAccount = this.expenseItems.find(a => a.id === addAccount.id);
        if (addAccount && !checkExistAccount) {
            this.expenseItems.push(addAccount);
            if (addAccount.parentID) {
                this.getAllParentExpenseItem(addAccount);
            }
        }
    }

    treeforSearchStatisticsCode() {
        for (let i = 0; i < this.statisticsCodes.length; i++) {
            if (this.statisticsCodes[i].parentID) {
                this.getAllParentStatisticsCode(this.statisticsCodes[i]);
            }
        }
    }

    getAllParentStatisticsCode(statisticsCode: IStatisticsCode) {
        const addAccount = this.statisticsCodeData.find(a => a.id === statisticsCode.parentID);
        const checkExistAccount = this.statisticsCodes.find(a => a.id === addAccount.id);
        if (addAccount && !checkExistAccount) {
            this.statisticsCodes.push(addAccount);
            if (addAccount.parentID) {
                this.getAllParentStatisticsCode(addAccount);
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

    getAllParentAccountList(accountList: IAccountList) {
        const addAccount = this.accountListData.find(a => a.id === accountList.parentAccountID);
        const checkExistAccount = this.accountLists.find(a => a.id === addAccount.id);
        if (addAccount && !checkExistAccount) {
            this.accountLists.push(addAccount);
            if (addAccount.parentAccountID) {
                this.getAllParentAccountList(addAccount);
            }
        }
    }

    registerChangeCheckBox() {
        this.eventSubscriber = this.eventManager.subscribe(
            'sendTree' + (this.nameSearchStatisticsCode ? this.nameSearchStatisticsCode : ''),
            response => {
                this.listParentStatisticsCode = [];
                this.listStatisticsCodeChecked = [];
                this.listParentStatisticsCode = response.data;
            }
        );
        if (this.reportType === BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_TAI_KHOAN) {
            this.eventSubscriber = this.eventManager.subscribe(
                'sendTree' + (this.nameSearchAccountList ? this.nameSearchAccountList : ''),
                response => {
                    this.listParentAccount = [];
                    this.listAccountListChecked = [];
                    this.listParentAccount = response.data;
                }
            );
        } else if (this.reportType === BaoCao.TongHop.SO_THEO_DOI_MA_THONG_KE_THEO_KHOAN_MUC_CHI_PHI) {
            this.eventSubscriber = this.eventManager.subscribe(
                'sendTree' + (this.nameSearchExpenseItem ? this.nameSearchExpenseItem : ''),
                response => {
                    this.listParentExpenseItem = [];
                    this.listExpenseItemsChecked = [];
                    this.listParentExpenseItem = response.data;
                }
            );
        }
    }

    addAllStatisticsCode(listParentStatisticsCode: ITreeStatisticsCode[], grade) {
        for (let i = 0; i < listParentStatisticsCode.length; i++) {
            if (listParentStatisticsCode[i].parent.checked) {
                this.listStatisticsCodeChecked.push(listParentStatisticsCode[i].parent.id);
            }
            if (listParentStatisticsCode[i].children && listParentStatisticsCode[i].children.length > 0) {
                this.addAllStatisticsCode(listParentStatisticsCode[i].children, grade + 1);
            }
        }
    }

    addAllAccountList(listParentAccountList: ITreeAccountList[], grade) {
        for (let i = 0; i < listParentAccountList.length; i++) {
            if (listParentAccountList[i].parent.checked) {
                this.listAccountListChecked.push(listParentAccountList[i].parent.accountNumber);
            }
            if (listParentAccountList[i].children && listParentAccountList[i].children.length > 0) {
                this.addAllAccountList(listParentAccountList[i].children, grade + 1);
            }
        }
    }

    addAllExpenseItem(listParentExpenseItem: ITreeExpenseList[], grade) {
        for (let i = 0; i < listParentExpenseItem.length; i++) {
            if (listParentExpenseItem[i].parent.checked) {
                this.listExpenseItemsChecked.push(listParentExpenseItem[i].parent.id);
            }
            if (listParentExpenseItem[i].children && listParentExpenseItem[i].children.length > 0) {
                this.addAllExpenseItem(listParentExpenseItem[i].children, grade + 1);
            }
        }
    }
}
