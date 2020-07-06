import { Component, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
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
import { RefModalService } from 'app/core/login/ref-modal.service';
import { ITreeAccountList } from 'app/shared/model/account-list-tree.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { ITreeExpenseList } from 'app/shared/model/expense-list-tree.model';
import { Subscription } from 'rxjs';

@Component({
    selector: 'eb-soTHCPTheoTaiKhoan',
    templateUrl: './so-theo-doi-doi-tuong-THCP-theo-tai-khoan.component.html',
    styleUrls: ['so-theo-doi-doi-tuong-THCP-theo-tai-khoan.component.css']
})
export class SoTheoDoiDoiTuongTHCPTheoTaiKhoanComponent implements OnInit {
    createDate: Moment;
    fromDate: Moment;
    toDate: Moment;
    eventSubscriber: Subscription;
    isKetXuat: boolean;
    role_Ketxuat = ROLE.SoTheoDoiDoiTuongTHCPTheoTaiKhoan_KetXuat;
    dependent: boolean;
    isDependent: boolean;
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
    accountLists: IAccountList[];
    costSetCode: string;
    costSetName: string;
    costSetFilter: ICostSet[];
    accountListsFilter: IAccountList[];
    option: number;
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
    listAccountListsChecked: any[];

    constructor(
        private modalService: NgbModal,
        private refModalService: RefModalService,
        public activeModal: NgbActiveModal,
        private principal: Principal,
        private toastr: ToastrService,
        public translate: TranslateService,
        public eventManager: JhiEventManager,
        private baoCaoService: BaoCaoService,
        public utilsService: UtilsService,
        private organizationUnitService: OrganizationUnitService,
        private costSetService: CostSetService,
        private accountListService: AccountListService
    ) {
        this.requestReport = {};
    }

    ngOnInit(): void {
        this.option = 1;
        this.dependent = false;
        this.nameSearchAccountList = 'AccountList';
        this.treeAccountLists = [];
        this.accountLists = [];
        this.accountListsFilter = [];
        //account
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
            this.isKetXuat = !this.utilsService.isPackageDemo(account);
            this.listTimeLine = this.utilsService.getCbbTimeLine();
            this.timeLineVoucher = this.listTimeLine[4].value;
            this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
            this.getSessionData();
            // this.organizationUnitService.getChildCompanyByID().subscribe((res: HttpResponse<any>) => {
            //     this.treeOrganizationUnits = res.body.orgTrees;
            //     this.organizationUnit = res.body.currentOrgLogin;
            //     // this.baoCaoService.checkHiddenDependent(this.treeOrganizationUnits);
            //     // this.baoCaoService.checkHiddenFirstCompany(this.organizationUnit);
            //     this.changeOptionData();
            // });
            this.changeOptionData();
        });
        // this.loadAll();
        this.registerChangeListSearchExpenseItem();
        this.registerChangeCheckBox();
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
        if (this.accountListsFilter.filter(x => x.checked).length === 0) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.error.nullAccount'),
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

    // isCheckAll() {
    //     if (this.accountListsFilter) {
    //         return this.accountListsFilter.every(item => item.checked) && this.accountListsFilter.length;
    //     } else {
    //         return false;
    //     }
    // }

    // checkAll() {
    //     const isCheck = this.accountListsFilter.every(item => item.checked) && this.accountListsFilter.length;
    //     this.accountListsFilter.forEach(item => (item.checked = !isCheck));
    // }
    //
    // check(accountList: IAccountList) {
    //     accountList.checked = !accountList.checked;
    // }

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

    // changeMGFilter() {
    //     if (this.accountName && this.accountNumber) {
    //         this.accountListsFilter = this.accountLists.filter(
    //             x =>
    //                 x.accountName.toLowerCase().includes(this.accountName.toLowerCase()) &&
    //                 x.accountNumber.toLowerCase().includes(this.accountNumber.toLowerCase())
    //         );
    //     } else if (this.accountName) {
    //         this.accountListsFilter = this.accountLists.filter(x => x.accountName.toLowerCase().includes(this.accountName));
    //     } else if (this.accountNumber) {
    //         this.accountListsFilter = this.accountLists.filter(x => x.accountNumber.toLowerCase().includes(this.accountNumber));
    //     } else this.accountListsFilter = this.accountLists;
    // }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('searchSoTheoDoiTHCPTheoTaiKhoan'));
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
        sessionStorage.setItem('searchSoTheoDoiTHCPTheoTaiKhoan', JSON.stringify(this.dataSession));
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

        this.accountListService.getAccountLists().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.indexAccountList = 0;
            this.listParentAccountList = [];
            this.flatTreeAccountList = [];
            this.accountListsFilter = res.body;
            for (let i = 0; i < this.accountListsFilter.length; i++) {
                if (!this.accountListsFilter[i].accountNumber) {
                    this.accountListsFilter[i].accountNumber = '';
                }
                if (!this.accountListsFilter[i].accountName) {
                    this.accountListsFilter[i].accountName = '';
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

    export() {
        if (this.checkErr()) {
            this.setSessionData();
            this.requestReport = {};
            this.requestReport.typeReport = BaoCao.Gia_Thanh.SO_THEO_DOI_DOI_TUONG_THCP_THEO_TAI_KHOAN;
            this.requestReport.fileName = BaoCao.Gia_Thanh.SO_THEO_DOI_DOI_TUONG_THCP_THEO_TAI_KHOAN_XSL;
            this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
            this.requestReport.dependent = this.dependent;
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT);
            this.requestReport.listCostSetID = this.costSetFilter.filter(c => c.checked).map(c => c.id);
            this.addAllAccountLists(this.listParentAccountList, this.minAccountList);
            this.requestReport.accountList = this.listAccountListsChecked;
            this.baoCaoService.exportExcel(this.requestReport).subscribe(
                res => {
                    const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                    const fileURL = URL.createObjectURL(blob);

                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    const name = BaoCao.Gia_Thanh.SO_THEO_DOI_DOI_TUONG_THCP_THEO_TAI_KHOAN_XSL;
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
            this.requestReport.typeReport = BaoCao.Gia_Thanh.SO_THEO_DOI_DOI_TUONG_THCP_THEO_TAI_KHOAN;
            this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
            this.requestReport.dependent = this.dependent;
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT);
            this.requestReport.listCostSetID = this.costSetFilter.filter(c => c.checked).map(c => c.id);
            this.addAllAccountLists(this.listParentAccountList, this.minAccountList);
            this.requestReport.accountList = this.listAccountListsChecked;
            this.baoCaoService.getReport(this.requestReport);
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

    getAllParentAccountList(accountList: IAccountList) {
        const addAccount = this.accountListsFilter.find(a => a.id === accountList.parentAccountID);
        const checkExistAccount = this.accountLists.find(a => a.id === addAccount.id);
        if (addAccount && !checkExistAccount) {
            this.accountLists.push(addAccount);
            if (addAccount.parentAccountID) {
                this.getAllParentAccountList(addAccount);
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

    registerChangeCheckBox() {
        this.eventSubscriber = this.eventManager.subscribe(
            'sendTree' + (this.nameSearchAccountList ? this.nameSearchAccountList : ''),
            response => {
                this.listParentAccountList = [];
                this.listAccountListsChecked = [];
                this.listParentAccountList = response.data;
            }
        );
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
    }

    loadAll() {
        this.accountListService.getAccountLists().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.indexAccountList = 0;
            this.listParentAccountList = [];
            this.flatTreeAccountList = [];
            this.accountListsFilter = res.body;
            for (let i = 0; i < this.accountListsFilter.length; i++) {
                if (!this.accountListsFilter[i].accountNumber) {
                    this.accountListsFilter[i].accountNumber = '';
                }
                if (!this.accountListsFilter[i].accountName) {
                    this.accountListsFilter[i].accountName = '';
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

    registerChangeListSearchExpenseItem() {
        this.eventSubscriber = this.eventManager.subscribe('listSearch' + this.nameSearchAccountList, response => {
            this.listSearchAccountList = response.data;
            this.listParentAccountList = [];
            this.flatTreeAccountList = [];
            this.accountLists = this.accountListsFilter.filter(
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
}
