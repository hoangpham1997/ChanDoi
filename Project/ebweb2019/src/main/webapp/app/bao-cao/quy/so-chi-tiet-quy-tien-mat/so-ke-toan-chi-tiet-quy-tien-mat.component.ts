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

@Component({
    selector: 'eb-so-chi-tiet-quy-tien-mat',
    templateUrl: './so-ke-toan-chi-tiet-quy-tien-mat.component.html',
    styleUrls: ['so-ke-toan-chi-tiet-quy-tien-mat.component.css']
})
export class SoKeToanChiTietQuyTienMatComponent implements OnInit {
    createDate: Moment;
    fromDate: Moment;
    toDate: Moment;
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
    currencies: ICurrency[];
    currencyCode: string;
    isDependent: boolean;
    ROLE_KetXuat = ROLE.SoKeToanChiTietQuyTienMat_KetXuat;
    isShowDependent: boolean;
    typeShowCurrency: boolean;
    currencyID: string;
    isKetXuat: boolean;

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
        private currencyService: CurrencyService
    ) {
        this.requestReport = {};
    }

    ngOnInit(): void {
        this.typeShowCurrency = false;
        this.groupTheSameItem = true;
        this.isDependent = false;
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
            this.accountListService.getAccountStartWith111().subscribe((res: HttpResponse<IAccountList[]>) => {
                this.accountLists = res.body;
            });
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
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('searchSoKeToanChiTietQuyTienMat'));
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
        sessionStorage.setItem('searchSoKeToanChiTietQuyTienMat', JSON.stringify(this.dataSession));
    }

    showReport() {
        if (this.checkError()) {
            this.setSessionData();
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT_SEARCH);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT_SEARCH);
            this.requestReport.groupTheSameItem = this.groupTheSameItem;
            this.requestReport.typeReport = BaoCao.Quy.SO_KE_TOAN_CHI_TIET_QUY_TIEN_MAT;
            this.requestReport.companyID = this.organizationUnit.parent.id;
            this.requestReport.currencyID = this.currencyCode;
            this.requestReport.dependent = this.isDependent;
            if (this.currencyID === this.currencyCode) {
                this.requestReport.typeShowCurrency = false;
            } else {
                this.requestReport.typeShowCurrency = this.typeShowCurrency;
            }
            this.requestReport.accountList = this.accountLists.filter(n => n.checked).map(n => n.accountNumber);
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

    isCheckAll() {
        if (this.accountLists) {
            return this.accountLists.every(item => item.checked) && this.accountLists.length;
        } else {
            return false;
        }
    }

    checkAll() {
        const isCheck = this.accountLists.every(item => item.checked) && this.accountLists.length;
        this.accountLists.forEach(item => (item.checked = !isCheck));
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
            this.requestReport.typeReport = BaoCao.Quy.SO_KE_TOAN_CHI_TIET_QUY_TIEN_MAT;
            this.requestReport.fileName = BaoCao.Quy.SO_KE_TOAN_CHI_TIET_QUY_TIEN_MAT_XLS;
            this.requestReport.companyID = this.organizationUnit.parent.id;
            this.requestReport.currencyID = this.currencyCode;
            this.requestReport.dependent = this.isDependent;
            this.requestReport.accountList = this.accountLists.filter(n => n.checked).map(n => n.accountNumber);
            if ((this.requestReport.accountList && this.requestReport.accountList.length === 0) || !this.requestReport.accountList) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.accountNumberNotBeBlank'),
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
                    const name = BaoCao.Quy.SO_KE_TOAN_CHI_TIET_QUY_TIEN_MAT_XLS;
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
}
