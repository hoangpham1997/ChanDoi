import { Component, OnInit } from '@angular/core';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';
import * as moment from 'moment';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { HttpResponse } from '@angular/common/http';
import { Moment } from 'moment';
import { RequestReport } from 'app/bao-cao/reqest-report.model';
import { DATE_FORMAT_SEARCH } from 'app/shared';
import { BaoCao } from 'app/app.constants';
import { BaoCaoService } from 'app/bao-cao/bao-cao.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { ROLE } from 'app/role.constants';
import { TreeOrganizationUnit } from 'app/shared/model/organization-unit-tree.model';

@Component({
    selector: 'eb-phan-bo-chi-phi-tra-truoc',
    templateUrl: './phan-bo-chi-phi-tra-truoc.component.html'
})
export class PhanBoChiPhiTraTruocComponent implements OnInit {
    organizationUnit: any;
    treeOrganizationUnits: any[];
    account: any;
    listTimeLine: any[];
    timeLineVoucher: any;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    fromDate: Moment;
    toDate: Moment;
    requestReport: RequestReport;
    requestReportSearch: any;
    dependent: any;
    ROLE_KetXuat = ROLE.BangPhanBoChiPhiTraTruoc_KetXuat;
    isKetXuat: any;
    isShowDependent: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        public principal: Principal,
        public organizationUnitService: OrganizationUnitService,
        private baoCaoService: BaoCaoService,
        private toastr: ToastrService,
        public translate: TranslateService,
        public utilsService: UtilsService
    ) {}

    ngOnInit(): void {
        this.principal.identity().then(account => {
            this.account = account;
            this.isKetXuat = !this.utilsService.isPackageDemo(this.account);
            this.treeOrganizationUnits = [];
            this.listTimeLine = this.utilsService.getCbbTimeLine();
            this.getSessionData();
            this.organizationUnitService.getChildCompanyByID().subscribe((res: HttpResponse<any>) => {
                this.treeOrganizationUnits = res.body.orgTrees;
                this.baoCaoService.checkHiddenDependent(this.treeOrganizationUnits);
                this.organizationUnit = res.body.currentOrgLogin;
            });
        });
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    selectChangeBeginDateAndEndDate(intTimeLine: String) {
        if (intTimeLine) {
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
            this.fromDate = moment(this.objTimeLine.dtBeginDate);
            this.toDate = moment(this.objTimeLine.dtEndDate);
            // this.createDate = this.fromDate;
        }
    }

    accept() {
        if (!this.checkErr()) {
            return;
        }
        if (!this.requestReport) {
            this.requestReport = new RequestReport();
        }
        this.requestReport.toDate = moment(this.toDate, DATE_FORMAT_SEARCH);
        this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT_SEARCH);
        this.requestReport.typeReport = BaoCao.TongHop.PHAN_BO_CHI_PHI_TRA_TRUOC;
        this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
        this.requestReport.dependent = this.dependent;
        this.setSessionData();
        this.baoCaoService.getReport(this.requestReport);
    }

    checkErr() {
        if (!this.organizationUnit) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.organizationUnitNotBeBlank'),
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
        return true;
    }

    getSessionData() {
        console.log(JSON.parse(sessionStorage.getItem('searchPhanBoChiPhiTraTruoc')));
        this.requestReportSearch = JSON.parse(sessionStorage.getItem('searchPhanBoChiPhiTraTruoc'))
            ? JSON.parse(sessionStorage.getItem('searchPhanBoChiPhiTraTruoc'))
            : '';
        if (this.requestReportSearch) {
            this.timeLineVoucher = this.requestReportSearch.timeLineVoucher;
            this.fromDate = moment(this.requestReportSearch.fromDate);
            this.toDate = moment(this.requestReportSearch.toDate);
            this.dependent = this.requestReportSearch.dependent;
        } else {
            this.timeLineVoucher = this.listTimeLine[4].value;
            this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
        }
    }

    setSessionData() {
        sessionStorage.removeItem('searchPhanBoChiPhiTraTruoc');
        // this.dataSession.createDate  = this.createDate ? moment(this.createDate, DATE_FORMAT_SEARCH).format(DATE_FORMAT) : '';
        // this.timeLineVoucherStatus = this.timeLineVoucher ? this.timeLineVoucher : '';
        this.requestReportSearch = new RequestReport();
        this.requestReportSearch.toDate = moment(this.toDate, DATE_FORMAT_SEARCH);
        this.requestReportSearch.fromDate = moment(this.fromDate, DATE_FORMAT_SEARCH);
        this.requestReportSearch.timeLineVoucher = this.timeLineVoucher;
        this.requestReportSearch.companyID = this.account.organizationUnit.id;
        this.requestReportSearch.dependent = this.dependent;
        sessionStorage.setItem('searchPhanBoChiPhiTraTruoc', JSON.stringify(this.requestReportSearch ? this.requestReportSearch : ''));
    }

    exportExcel() {
        if (this.checkErr()) {
            this.setSessionData();
            this.requestReport = {};
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT_SEARCH);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT_SEARCH);
            this.requestReport.typeReport = BaoCao.TongHop.PHAN_BO_CHI_PHI_TRA_TRUOC;
            this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
            this.requestReport.typeReport = BaoCao.TongHop.PHAN_BO_CHI_PHI_TRA_TRUOC;
            this.requestReport.fileName = BaoCao.TongHop.PHAN_BO_CHI_PHI_TRA_TRUOC_XLS;
            this.setSessionData();
            // this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT_SEARCH).format(DATE_FORMAT);
            this.baoCaoService.exportExcel(this.requestReport).subscribe(
                res => {
                    const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                    const fileURL = URL.createObjectURL(blob);

                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    const name = BaoCao.TongHop.PHAN_BO_CHI_PHI_TRA_TRUOC_XLS;
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
                this.dependent = false;
                this.isShowDependent = false;
            } else {
                this.isShowDependent = this.checkChildren(this.organizationUnit);
            }
        }
        // if (this.organizationUnit && this.organizationUnit.parent.id) {
        //     this.bankAccountDetailsService
        //         .getBankAccountDetailsByOrgID({ orgID: this.organizationUnit.parent.id })
        //         .subscribe((res: HttpResponse<IBankAccountDetails[]>) => {
        //             this.bankAccountDetails = [];
        //             this.bankAccountDetails.push(Object.assign({}));
        //             this.bankAccountDetails[0].id = 'Tất cả';
        //             this.bankAccountDetails[0].bankAccount = 'Tất cả';
        //             this.bankAccountDetails[0].bankName = 'Tất cả';
        //             for (let i = 0; i < res.body.length; i++) {
        //                 this.bankAccountDetails.push(res.body[i]);
        //             }
        //         });
        // }
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
