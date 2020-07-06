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
import { ROLE } from 'app/role.constants';
import { TreeOrganizationUnit } from 'app/shared/model/organization-unit-tree.model';

@Component({
    selector: 'eb-bang-ke-mua-ban',
    templateUrl: './bang-ke-mua-ban.component.html',
    styleUrls: ['bang-ke-mua-ban.component.css', '../bao-cao.component.css']
})
export class BangKeMuaBanComponent implements OnInit {
    requestReport: RequestReport;
    isBill: boolean;
    listTimeLine: any[];
    account: any;
    modalRef: NgbModalRef;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    treeOrganizationUnits: any[];
    organizationUnit: any;
    reportType: string;
    modalData: any;
    BaoCao = BaoCao;
    sessionSearch: {
        type: string;
        data: {
            dependent: boolean;
            isSimilarSum: boolean;
            timeLineVoucher: any;
            fromDate: string;
            toDate: string;
            organizationUnit: TreeOrganizationUnit;
            hiddenDependent: boolean;
        };
    };

    ROLE_KetXuat = ROLE.BangKeHDChungTuVTHHBanRaQuanTri_KetXuat;
    ROLE_KetXuat_BRQT = ROLE.BangKeHDChungTuVTHHBanRaQuanTri_KetXuat;
    ROLE_KetXuat_BR = ROLE.BangKeHDChungTuVTHHBanRa_KetXuat;
    ROLE_KetXuat_MV = ROLE.BangKeHDChungTuVTHHMuaVao_KetXuat;
    ROLE_KetXuat_MVQT = ROLE.BangKeHDChungTuVTHHMuaVaoQuanTri_KetXuat;
    isShowDependent: boolean;
    phanQuyenBaoCao: any;
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
        public translateService: TranslateService,
        private currencyService: CurrencyService,
        private bankAccountDetailService: BankAccountDetailsService
    ) {
        this.requestReport = {};
        if (this.reportType === BaoCao.BANG_KE_BAN_RA && !this.isBill) {
            this.ROLE_KetXuat = ROLE.BangKeHDChungTuVTHHBanRa_KetXuat;
        } else if (this.reportType === BaoCao.BANG_KE_BAN_RA && this.isBill) {
            this.ROLE_KetXuat = ROLE.BangKeHDChungTuVTHHBanRaQuanTri_KetXuat;
        } else if (this.reportType === BaoCao.BANG_KE_BAN_RA && !this.isBill) {
            this.ROLE_KetXuat = ROLE.BangKeHDChungTuVTHHMuaVao_KetXuat;
        } else if (this.reportType === BaoCao.BANG_KE_BAN_RA && this.isBill) {
            this.ROLE_KetXuat = ROLE.BangKeHDChungTuVTHHMuaVaoQuanTri_KetXuat;
        }
    }

    ngOnInit(): void {
        this.sessionSearch = this.baoCaoService.getSessionData(this.reportType + this.isBill);
        if (this.sessionSearch.data.isSimilarSum === undefined || this.sessionSearch.data.isSimilarSum === null) {
            this.sessionSearch.data.isSimilarSum = true;
        }
        this.principal.identity().then(account => {
            this.account = account;
            this.isKetXuat = !this.utilsService.isPackageDemo(this.account);
            if (this.reportType === BaoCao.BANG_KE_BAN_RA && !this.isBill) {
                this.phanQuyenBaoCao = this.ROLE_KetXuat_BR;
            } else if (this.reportType === BaoCao.BANG_KE_BAN_RA && this.isBill) {
                this.phanQuyenBaoCao = this.ROLE_KetXuat_BRQT;
            } else if (this.reportType === BaoCao.BANG_KE_MUA_VAO && !this.isBill) {
                this.phanQuyenBaoCao = this.ROLE_KetXuat_MV;
            } else if (this.reportType === BaoCao.BANG_KE_MUA_VAO && this.isBill) {
                this.phanQuyenBaoCao = this.ROLE_KetXuat_MVQT;
            }
            this.listTimeLine = this.utilsService.getCbbTimeLine();
            if (this.sessionSearch.data.timeLineVoucher === null || this.sessionSearch.data.timeLineVoucher === undefined) {
                this.sessionSearch.data.timeLineVoucher = this.listTimeLine[4].value;
            }
            // }
            this.sessionSearch.data.organizationUnit = this.organizationUnit;
            this.selectChangeOrg();
            this.baoCaoService.putSessionData(this.sessionSearch);
            this.sessionSearch = this.baoCaoService.getSessionData(this.reportType + this.isBill);

            this.selectChangeBeginDateAndEndDate();
            // this.organizationUnitService.getChildCompanyByID().subscribe((res: HttpResponse<any>) => {
            //     this.treeOrganizationUnits = res.body.orgTrees;
            //     // if (this.treeOrganizationUnits && this.treeOrganizationUnits.length > 0) {
            //     //     this.sessionSearch.data.organizationUnit =
            //     //         this.treeOrganizationUnits.find(
            //     //             x => this.sessionSearch.data.organizationUnit && x.value === this.sessionSearch.data.organizationUnit.parent.id
            //     //         ) || this.treeOrganizationUnits.find(x => (x.value = res.body.currentOrg.value));
            //     // } else {
            //     this.sessionSearch.data.organizationUnit = res.body.currentOrgLogin;
            //     this.selectChangeOrg();
            //     // }
            //     this.baoCaoService.putSessionData(this.sessionSearch);
            //     this.sessionSearch = this.baoCaoService.getSessionData(this.reportType + this.isBill);
            // });
        });
    }

    accept() {
        if (this.checkErr()) {
            this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
            this.requestReport.toDate = moment(this.sessionSearch.data.toDate, DATE_FORMAT);
            this.requestReport.fromDate = moment(this.sessionSearch.data.fromDate, DATE_FORMAT);
            this.requestReport.similarSum = this.sessionSearch.data.isSimilarSum;
            this.requestReport.typeReport = this.reportType;
            this.requestReport.bill = this.isBill;
            this.requestReport.dependent = this.sessionSearch.data.dependent;
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

    getCurrentDate(): { year; month; day } {
        const _date = moment();
        return { year: _date.year(), month: _date.month() + 1, day: _date.date() };
    }

    changeSessionSearch() {
        this.selectChangeOrg();
        this.baoCaoService.putSessionData(this.sessionSearch);
        this.sessionSearch = this.baoCaoService.getSessionData(this.reportType + this.isBill);
    }

    exportExcel() {
        if (this.checkErr()) {
            this.requestReport = {};
            this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
            this.requestReport.toDate = moment(this.sessionSearch.data.toDate, DATE_FORMAT_SEARCH);
            this.requestReport.fromDate = moment(this.sessionSearch.data.fromDate, DATE_FORMAT_SEARCH);
            this.requestReport.similarSum = this.sessionSearch.data.isSimilarSum;
            this.requestReport.typeReport = this.reportType;
            this.requestReport.bill = this.isBill;
            this.requestReport.dependent = this.sessionSearch.data.dependent;
            if (this.requestReport.typeReport === BaoCao.BANG_KE_BAN_RA && this.requestReport.bill) {
                this.requestReport.fileName = BaoCao.BANG_KE_BAN_RA_QUAN_TRI_XLS;
            } else if (this.requestReport.typeReport === BaoCao.BANG_KE_BAN_RA && !this.requestReport.bill) {
                this.requestReport.fileName = BaoCao.BANG_KE_BAN_RA_XLS;
            } else if (this.requestReport.typeReport === BaoCao.BANG_KE_MUA_VAO && this.requestReport.bill) {
                this.requestReport.fileName = BaoCao.BANG_KE_MUA_VAO_QUAN_TRI_XLS;
            } else {
                this.requestReport.fileName = BaoCao.BANG_KE_MUA_VAO_XLS;
            }
            this.baoCaoService.exportExcel(this.requestReport).subscribe(
                res => {
                    const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                    const fileURL = URL.createObjectURL(blob);

                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    if (this.requestReport.typeReport === BaoCao.BANG_KE_BAN_RA && this.requestReport.bill) {
                        const name = BaoCao.BANG_KE_BAN_RA_QUAN_TRI_XLS;
                        link.setAttribute('download', name);
                        link.href = fileURL;
                        link.click();
                    } else if (this.requestReport.typeReport === BaoCao.BANG_KE_BAN_RA && !this.requestReport.bill) {
                        const name = BaoCao.BANG_KE_BAN_RA_XLS;
                        link.setAttribute('download', name);
                        link.href = fileURL;
                        link.click();
                    } else if (this.requestReport.typeReport === BaoCao.BANG_KE_MUA_VAO && this.requestReport.bill) {
                        const name = BaoCao.BANG_KE_MUA_VAO_QUAN_TRI_XLS;
                        link.setAttribute('download', name);
                        link.href = fileURL;
                        link.click();
                    } else {
                        const name = BaoCao.BANG_KE_MUA_VAO_XLS;
                        link.setAttribute('download', name);
                        link.href = fileURL;
                        link.click();
                    }
                    // const name = BaoCao.BANG_KE_BAN_RA_QUAN_TRI_XLS;
                    // link.setAttribute('download', name);
                    // link.href = fileURL;
                    // link.click();
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
        if (this.sessionSearch.data.organizationUnit && this.sessionSearch.data.organizationUnit.parent.id) {
            if (this.sessionSearch.data.organizationUnit.parent.unitType === 1) {
                this.isShowDependent = false;
            } else {
                this.isShowDependent = this.checkChildren(this.sessionSearch.data.organizationUnit);
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
