import { Component, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { JhiEventManager } from 'ng-jhipster';
import { IRequestReport, RequestReport } from 'app/bao-cao/reqest-report.model';
import * as moment from 'moment';
import { Moment } from 'moment';
import { BaoCao } from 'app/app.constants';
import { DATE_FORMAT_SEARCH } from 'app/shared';
import { BaoCaoService } from 'app/bao-cao/bao-cao.service';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';
import { HttpResponse } from '@angular/common/http';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { AccountListService } from 'app/danhmuc/account-list';
import { IAccountList } from 'app/shared/model/account-list.model';
import { ToolsService } from 'app/entities/tools';
import { ITools } from 'app/shared/model/tools.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { ITreeOrganizationUnit, TreeOrganizationUnit } from 'app/shared/model/organization-unit-tree.model';
import { ROLE } from 'app/role.constants';

@Component({
    selector: 'eb-so-theo-doi-lai-lo-theo-hoa-don',
    templateUrl: './so-theo-doi-lai-lo-theo-hoa-don.component.html',
    styleUrls: ['so-theo-doi-lai-lo-theo-hoa-don.component.css']
})
export class SoTheoDoiLaiLoTheoHoaDonComponent implements OnInit {
    createDate: Moment;
    fromDate: Moment;
    toDate: Moment;
    requestReport: RequestReport;
    listTimeLine: any[];
    account: any;
    modalRef: NgbModalRef;
    showAccumAmount: boolean;
    groupTheSameItem: boolean;
    timeLineVoucher: any;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    organizationUnit: ITreeOrganizationUnit;
    dependent: boolean;
    treeOrganizationUnits: any[];
    accountLists: IAccountList[];
    tools: ITools[];
    toolsAll: ITools[];

    accountNumber: string;
    accountName: string;
    grade: number;
    accountListsData: any;
    isShowDependent: any;
    toolCode: any;
    toolName: any;
    dataSession: IRequestReport;
    isKetXuat: any;
    ROLE_KetXuat = ROLE.SoTheoDoiLaiLoTheoHoaDon_KetXuat;

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
        private toolsService: ToolsService
    ) {
        this.requestReport = {};
    }

    ngOnInit(): void {
        this.groupTheSameItem = true;
        this.showAccumAmount = true;
        this.principal.identity().then(account => {
            this.account = account;
            this.isKetXuat = !this.utilsService.isPackageDemo(account);
            this.listTimeLine = this.utilsService.getCbbTimeLine();
            this.timeLineVoucher = this.listTimeLine[4].value;
            this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
            this.getSessionData();
            this.checkShowDependent();
        });
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('searchTheoDoi_LaiLoHD'));
        if (this.dataSession) {
            this.toDate = this.dataSession.toDate;
            this.fromDate = this.dataSession.fromDate;
            this.timeLineVoucher = this.dataSession.timeLineVoucher ? this.dataSession.timeLineVoucher : '';
        } else {
            this.dataSession = {};
        }
    }

    setSessionData() {
        this.dataSession.toDate = this.toDate;
        this.dataSession.fromDate = this.fromDate;
        this.dataSession.timeLineVoucher = this.timeLineVoucher ? this.timeLineVoucher : '';
        sessionStorage.setItem('searchTheoDoi_LaiLoHD', JSON.stringify(this.dataSession));
    }

    checkShowDependent() {
        if (this.organizationUnit && this.organizationUnit.parent.id) {
            if (this.organizationUnit.parent.unitType === 1) {
                this.isShowDependent = false;
            } else {
                this.isShowDependent = this.checkChildren(this.organizationUnit);
            }
        }
    }

    checkChildren(treeOrganizationUnit: TreeOrganizationUnit): boolean {
        return (
            treeOrganizationUnit &&
            treeOrganizationUnit.children &&
            treeOrganizationUnit.children.length > 0 &&
            treeOrganizationUnit.children.some(item => item.parent.accType === 0)
        );
    }

    showReport() {
        if (this.checkErr()) {
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT_SEARCH);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT_SEARCH);
            this.setSessionData();
            this.requestReport.showAccumAmount = this.showAccumAmount;
            // this.requestReport.groupTheSameItem = this.groupTheSameItem;
            this.requestReport.dependent = this.dependent;
            this.requestReport.typeReport = BaoCao.BanHang.SO_THEO_DOI_LAI_LO_THEO_HOA_DON;
            this.requestReport.companyID = this.organizationUnit.parent.id;
            // this.requestReport.tools = this.tools.filter(x => x.checked).map(x => x.id);
            this.baoCaoService.getReport(this.requestReport);
        }
    }

    exportExcel() {
        if (this.checkErr()) {
            // this.setSessionData();
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT_SEARCH);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT_SEARCH);
            this.setSessionData();
            // this.requestReport.groupTheSameItem = this.groupTheSameItem;
            this.requestReport.typeReport = BaoCao.BanHang.SO_THEO_DOI_LAI_LO_THEO_HOA_DON;
            this.requestReport.fileName = BaoCao.BanHang.SO_THEO_DOI_LAI_LO_THEO_HOA_DON_XLS;
            this.requestReport.companyID = this.organizationUnit.parent.id;
            // this.requestReport.currencyID = this.currencyCode;
            this.requestReport.dependent = this.dependent;
            // this.requestReport.tools = this.tools.filter(x => x.checked).map(x => x.id);
            // if ((this.requestReport.accountList && this.requestReport.accountList.length === 0) || !this.requestReport.accountList) {
            //     this.toastr.error(
            //         this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.accountNumberNotBeBlank'),
            //         this.translate.instant('ebwebApp.mBDeposit.message')
            //     );
            //     return;
            // }
            this.baoCaoService.exportExcel(this.requestReport).subscribe(
                res => {
                    const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                    const fileURL = URL.createObjectURL(blob);

                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    const name = BaoCao.BanHang.SO_THEO_DOI_LAI_LO_THEO_HOA_DON_XLS;
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

    checkErr() {
        if (!this.fromDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.error.nullFromDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (!this.toDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.error.nullToDate'),
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
        if (this.tools) {
            return this.tools.every(item => item.checked) && this.tools.length;
        } else {
            return false;
        }
    }

    checkAll() {
        const isCheck = this.tools.every(item => item.checked) && this.tools.length;
        this.tools.forEach(item => (item.checked = !isCheck));
    }

    check(iTools: ITools) {
        iTools.checked = !iTools.checked;
    }

    changeMGFilter() {
        if (this.toolCode && this.toolName) {
            this.tools = this.toolsAll.filter(
                x =>
                    x.toolCode.toLowerCase().includes(this.toolCode.toLowerCase()) &&
                    x.toolName.toLowerCase().includes(this.toolName.toLowerCase())
            );
        } else if (this.toolCode) {
            this.tools = this.toolsAll.filter(x => x.toolCode.toLowerCase().includes(this.toolCode.toLowerCase()));
        } else if (this.toolName) {
            this.tools = this.toolsAll.filter(x => x.toolName.toLowerCase().includes(this.toolName.toLowerCase()));
        } else {
            this.tools = this.toolsAll;
        }
    }
}
