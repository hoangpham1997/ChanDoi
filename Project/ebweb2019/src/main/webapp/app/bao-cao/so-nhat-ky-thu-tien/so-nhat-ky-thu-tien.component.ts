import { Component, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { JhiEventManager } from 'ng-jhipster';
import { RequestReport } from 'app/bao-cao/reqest-report.model';
import * as moment from 'moment';
import { BaoCao } from 'app/app.constants';
import { DATE_FORMAT, DATE_FORMAT_SEARCH } from 'app/shared';
import { BaoCaoService } from 'app/bao-cao/bao-cao.service';
import { Moment } from 'moment';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';
import { HttpResponse } from '@angular/common/http';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { AccountListService } from 'app/danhmuc/account-list';
import { IAccountList } from 'app/shared/model/account-list.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { BankAccountDetails, IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { TreeOrganizationUnit } from 'app/shared/model/organization-unit-tree.model';
import { ROLE } from 'app/role.constants';

@Component({
    selector: 'eb-so-nhat-ky-thu-tien',
    templateUrl: './so-nhat-ky-thu-tien.component.html',
    styleUrls: ['so-nhat-ky-thu-tien.component.css', '../bao-cao.component.css']
})
export class SoNhatKyThuTienComponent implements OnInit {
    requestReport: RequestReport;
    listTimeLine: any[];
    accountLists: IAccountList[];
    account: any;
    modalRef: NgbModalRef;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    treeOrganizationUnits: any[];
    currencies: ICurrency[];
    bankAccountDetails: BankAccountDetails[];
    reportType: string;
    modalData: any;
    BaoCao = BaoCao;
    sessionSearch: {
        type: string;
        data: {
            dependent: boolean;
            isSimilarSum: boolean;
            timeLineVoucher: any;
            accountNumber: string;
            currency: string;
            bankAccountDetail: string;
            fromDate: string;
            toDate: string;
            organizationUnit: TreeOrganizationUnit;
            hiddenDependent: boolean;
            getAmountOriginal: boolean;
        };
    };
    currencyID: string;
    isKetXuat: boolean;
    isShowDependent: boolean;
    ROLE_KetXuat_TT = ROLE.SoNhatKyThuTien_KetXuat;
    ROLE_KetXuat_CT = ROLE.SoNhatKyChiTien_KetXuat;

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
        public translateService: TranslateService,
        private currencyService: CurrencyService,
        private bankAccountDetailService: BankAccountDetailsService
    ) {
        this.requestReport = {};
    }

    ngOnInit(): void {
        this.sessionSearch = this.baoCaoService.getSessionData(this.reportType);
        if (this.sessionSearch.data.isSimilarSum === null || this.sessionSearch.data.isSimilarSum === undefined) {
            this.sessionSearch.data.isSimilarSum = true;
        }
        this.principal.identity().then(account => {
            this.account = account;
            this.currencyID = this.account.organizationUnit.currencyID;
            this.isKetXuat = !this.utilsService.isPackageDemo(this.account);
            this.treeOrganizationUnits = [];
            this.listTimeLine = this.utilsService.getCbbTimeLine();
            if (this.sessionSearch.data.timeLineVoucher === null || this.sessionSearch.data.timeLineVoucher === undefined) {
                this.sessionSearch.data.timeLineVoucher = this.listTimeLine[4].value;
            }
            this.selectChangeBeginDateAndEndDate();
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
                if (this.sessionSearch.data.organizationUnit && this.sessionSearch.data.organizationUnit.parent.id) {
                    if (this.sessionSearch.data.organizationUnit.parent.unitType === 1) {
                        this.sessionSearch.data.dependent = false;
                        this.isShowDependent = false;
                    } else {
                        this.isShowDependent = this.checkChildren(this.sessionSearch.data.organizationUnit);
                    }
                }
            });
            this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
                this.currencies = res.body.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
                this.sessionSearch.data.currency = this.sessionSearch.data.currency || this.account.organizationUnit.currencyID;
            });
        });
        this.accountListService
            .getAccountForAccountDefault({ listFilterAccount: '111;112' })
            .subscribe((res2: HttpResponse<IAccountList[]>) => {
                this.accountLists = res2.body.sort((a, b) => a.accountNumber.localeCompare(b.accountNumber));
            });
        this.bankAccountDetailService.getBankAccountDetails().subscribe((res: HttpResponse<IBankAccountDetails[]>) => {
            this.bankAccountDetails = res.body.filter(x => x.isActive).sort((a, b) => a.bankAccount.localeCompare(b.bankAccount));
        });
    }

    accept() {
        if (this.checkErr()) {
            this.requestReport.toDate = moment(this.sessionSearch.data.toDate, DATE_FORMAT);
            this.requestReport.fromDate = moment(this.sessionSearch.data.fromDate, DATE_FORMAT);
            this.requestReport.similarSum = this.sessionSearch.data.isSimilarSum;
            this.requestReport.dependent = this.sessionSearch.data.dependent;
            this.requestReport.typeReport = this.reportType;
            this.requestReport.bankAccountDetail = this.sessionSearch.data.bankAccountDetail;
            this.requestReport.accountNumber = this.sessionSearch.data.accountNumber;
            this.requestReport.currencyID = this.sessionSearch.data.currency;
            this.requestReport.companyID = this.sessionSearch.data.organizationUnit.parent.id;
            this.requestReport.getAmountOriginal = this.sessionSearch.data.getAmountOriginal;
            this.baoCaoService.getReport(this.requestReport);
        }
    }

    checkErr() {
        if (!this.sessionSearch.data.organizationUnit) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.organizationUnitNotBeBlank'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (this.sessionSearch.data.timeLineVoucher === undefined || this.sessionSearch.data.timeLineVoucher === null) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.periodCanNOtBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.error')
            );
            return false;
        }
        if (!this.sessionSearch.data.accountNumber) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.accountNumberNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.error')
            );
            return false;
        }
        if (!this.sessionSearch.data.fromDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.muaHang.muaDichVu.toastr.fromDateNull'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (!this.sessionSearch.data.toDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.muaHang.muaDichVu.toastr.toDateNull'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (this.sessionSearch.data.toDate && this.sessionSearch.data.fromDate) {
            if (moment(this.sessionSearch.data.toDate, DATE_FORMAT_SEARCH) < moment(this.sessionSearch.data.fromDate, DATE_FORMAT_SEARCH)) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mCReceipt.error.fromDateGreaterToDate'),
                    this.translate.instant('ebwebApp.mCReceipt.error.error')
                );
                return false;
            }
        }
        if (!this.sessionSearch.data.currency) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.currencyCodeNotBeBlank'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        return true;
    }

    selectChangeBeginDateAndEndDate() {
        if (this.sessionSearch.data.timeLineVoucher) {
            this.objTimeLine = this.utilsService.getTimeLine(this.sessionSearch.data.timeLineVoucher);
            this.sessionSearch.data.fromDate = this.objTimeLine.dtBeginDate;
            this.sessionSearch.data.toDate = this.objTimeLine.dtEndDate;
            this.changeSessionSearch();
        }
    }

    changeSessionSearch() {
        if (this.sessionSearch.data.organizationUnit && this.sessionSearch.data.organizationUnit.parent.id) {
            if (this.sessionSearch.data.organizationUnit.parent.unitType === 1) {
                this.sessionSearch.data.dependent = false;
                this.isShowDependent = false;
            } else {
                this.isShowDependent = this.checkChildren(this.sessionSearch.data.organizationUnit);
            }
        }
        this.baoCaoService.putSessionData(this.sessionSearch);
        this.sessionSearch = this.baoCaoService.getSessionData(this.reportType);
    }

    getCurrentDate(): { year; month; day } {
        const _date = moment();
        return { year: _date.year(), month: _date.month() + 1, day: _date.date() };
    }

    exportExcel() {
        if (this.checkErr()) {
            this.requestReport = {};
            this.requestReport.toDate = moment(this.sessionSearch.data.toDate, DATE_FORMAT);
            this.requestReport.fromDate = moment(this.sessionSearch.data.fromDate, DATE_FORMAT);
            this.requestReport.similarSum = this.sessionSearch.data.isSimilarSum;
            this.requestReport.dependent = this.sessionSearch.data.dependent;
            this.requestReport.typeReport = this.reportType;
            this.requestReport.bankAccountDetail = this.sessionSearch.data.bankAccountDetail;
            this.requestReport.accountNumber = this.sessionSearch.data.accountNumber;
            this.requestReport.currencyID = this.sessionSearch.data.currency;
            this.requestReport.companyID = this.sessionSearch.data.organizationUnit.parent.id;
            this.requestReport.getAmountOriginal = this.sessionSearch.data.getAmountOriginal;
            if (this.requestReport.typeReport === BaoCao.SO_NHAT_KY_THU_TIEN) {
                this.requestReport.fileName = BaoCao.SO_NHAT_KY_THU_TIEN_XLS;
            } else {
                this.requestReport.fileName = BaoCao.SO_NHAT_KY_CHI_TIEN_XLS;
            }
            this.baoCaoService.exportExcel(this.requestReport).subscribe(
                res => {
                    const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                    const fileURL = URL.createObjectURL(blob);

                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    if (this.requestReport.typeReport === BaoCao.SO_NHAT_KY_THU_TIEN) {
                        const name = BaoCao.SO_NHAT_KY_THU_TIEN_XLS;
                        link.setAttribute('download', name);
                        link.href = fileURL;
                        link.click();
                    } else {
                        const name = BaoCao.SO_NHAT_KY_CHI_TIEN_XLS;
                        link.setAttribute('download', name);
                        link.href = fileURL;
                        link.click();
                    }
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

    checkChildren(treeOrganizationUnit: TreeOrganizationUnit): boolean {
        if (treeOrganizationUnit && treeOrganizationUnit.children && treeOrganizationUnit.children.length > 0) {
            for (let i = 0; i < treeOrganizationUnit.children.length; i++) {
                if (
                    treeOrganizationUnit.children[i].parent.accType === 0 &&
                    treeOrganizationUnit.children[i].parent.unitType === 1 &&
                    treeOrganizationUnit.children[i].parent.parentID === this.sessionSearch.data.organizationUnit.parent.id
                ) {
                    return true;
                }
            }
        }
        return false;
    }
}
