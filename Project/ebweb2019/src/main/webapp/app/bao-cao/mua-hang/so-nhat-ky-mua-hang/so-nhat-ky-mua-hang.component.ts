import { Component, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { JhiEventManager } from 'ng-jhipster';
import * as moment from 'moment';
import { Moment } from 'moment';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { DATE_FORMAT, DATE_FORMAT_SEARCH } from 'app/shared';
import { BaoCao } from 'app/app.constants';
import { RequestReport } from 'app/bao-cao/reqest-report.model';
import { BaoCaoService } from 'app/bao-cao/bao-cao.service';
import { HttpResponse } from '@angular/common/http';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';
import { AccountListService } from 'app/danhmuc/account-list';
import { ROLE } from 'app/role.constants';
import { TreeOrganizationUnit } from 'app/shared/model/organization-unit-tree.model';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';

@Component({
    selector: 'eb-so-nhat-ky-mua-hang',
    templateUrl: './so-nhat-ky-mua-hang.component.html',
    styleUrls: ['./so-nhat-ky-mua-hang.css', '../../bao-cao.component.css']
})
export class SoNhatKyMuaHangComponent implements OnInit {
    requestReport: RequestReport;
    listTimeLine: any[];
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    account: any;
    modalRef: NgbModalRef;
    treeOrganizationUnits: any[];
    BaoCao = BaoCao;
    reportType: string;
    ROLE_KetXuat = ROLE.SoNhatKyMuaHang_KetXuat;
    sessionSearch: {
        type: string;
        data: {
            dependent: boolean;
            option: boolean;
            timeLineVoucher: any;
            fromDate: string;
            toDate: string;
            organizationUnit: TreeOrganizationUnit;
            hiddenDependent: boolean;
        };
    };
    isShowDependent: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private principal: Principal,
        public translate: TranslateService,
        public eventManager: JhiEventManager,
        public utilsService: UtilsService,
        private modalService: NgbModal,
        private refModalService: RefModalService,
        private toastr: ToastrService,
        private baoCaoService: BaoCaoService,
        private organizationUnitService: OrganizationUnitService,
        private accountListService: AccountListService
    ) {}

    ngOnInit() {
        this.sessionSearch = this.baoCaoService.getSessionData(this.reportType);
        if (this.sessionSearch.data.option === null || this.sessionSearch.data.option === undefined) {
            this.sessionSearch.data.option = false;
        }
        this.treeOrganizationUnits = [];
        this.principal.identity().then(account => {
            this.account = account;
            this.listTimeLine = this.utilsService.getCbbTimeLine();
            if (this.sessionSearch.data.timeLineVoucher === null || this.sessionSearch.data.timeLineVoucher === undefined) {
                this.sessionSearch.data.timeLineVoucher = this.listTimeLine[4].value;
            }
            this.selectChangeBeginDateAndEndDate(this.sessionSearch.data.timeLineVoucher);
            this.organizationUnitService.getChildCompanyByID().subscribe((res: HttpResponse<any>) => {
                this.treeOrganizationUnits = res.body.orgTrees;
                // if (this.treeOrganizationUnit    s && this.treeOrganizationUnits.length > 0) {
                //     this.sessionSearch.data.organizationUnit =
                //         this.treeOrganizationUnits.find(
                //             x => this.sessionSearch.data.organizationUnit && x.parent === this.sessionSearch.data.organizationUnit.parent.id
                //         ) || this.treeOrganizationUnits.find(x => (x.parent.id = res.body.currentOrg.parent.id));
                // } else {
                this.sessionSearch.data.organizationUnit = res.body.currentOrgLogin;
                // }
                this.baoCaoService.putSessionData(this.sessionSearch);
                this.sessionSearch = this.baoCaoService.getSessionData(this.reportType);
                this.selectChangeOrg();
            });
        });
    }

    showReport() {
        if (this.checkErr()) {
            this.requestReport = {};
            this.requestReport.companyID = this.sessionSearch.data.organizationUnit
                ? this.sessionSearch.data.organizationUnit.parent.id
                : null;
            this.requestReport.toDate = moment(this.sessionSearch.data.toDate, DATE_FORMAT);
            this.requestReport.fromDate = moment(this.sessionSearch.data.fromDate, DATE_FORMAT);
            this.requestReport.typeReport = this.reportType;
            this.requestReport.option = this.sessionSearch.data.option;
            this.requestReport.dependent = this.sessionSearch.data.dependent;
            this.baoCaoService.getReport(this.requestReport);
        }
    }

    checkErr() {
        if (!this.sessionSearch.data.organizationUnit) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.error.nullOrg'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (!this.sessionSearch.data.fromDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.nullFromDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (!this.sessionSearch.data.toDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.nullToDate'),
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

    selectChangeBeginDateAndEndDate(intTimeLine: String) {
        if (intTimeLine) {
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
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
        this.baoCaoService.putSessionData(this.sessionSearch);
        this.sessionSearch = this.baoCaoService.getSessionData(this.reportType);
    }

    export() {
        if (this.checkErr()) {
            this.requestReport = {};
            this.requestReport.companyID = this.sessionSearch.data.organizationUnit
                ? this.sessionSearch.data.organizationUnit.parent.id
                : null;
            this.requestReport.toDate = moment(this.sessionSearch.data.toDate, DATE_FORMAT);
            this.requestReport.fromDate = moment(this.sessionSearch.data.fromDate, DATE_FORMAT);
            this.requestReport.typeReport = this.reportType;
            if (this.reportType === BaoCao.MuaHang.SO_NHAT_KY_MUA_HANG) {
                this.requestReport.fileName = BaoCao.MuaHang.SO_NHAT_KY_MUA_HANG_XLS;
            } else if (this.reportType === BaoCao.BanHang.SO_NHAT_KY_BAN_HANG) {
                this.requestReport.fileName = BaoCao.BanHang.SO_NHAT_KY_BAN_HANG_XLS;
            }
            this.requestReport.option = this.sessionSearch.data.option;
            this.requestReport.dependent = this.sessionSearch.data.dependent;
            this.baoCaoService.exportExcel(this.requestReport).subscribe(
                res => {
                    const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                    const fileURL = URL.createObjectURL(blob);

                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    let fileExcel;
                    if (this.reportType === BaoCao.MuaHang.SO_NHAT_KY_MUA_HANG) {
                        fileExcel = BaoCao.MuaHang.SO_NHAT_KY_MUA_HANG_XLS;
                    } else if (this.reportType === BaoCao.BanHang.SO_NHAT_KY_BAN_HANG) {
                        fileExcel = BaoCao.BanHang.SO_NHAT_KY_BAN_HANG_XLS;
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
            this.changeSessionSearch();
        }
    }

    changeOrganizationUnit() {
        this.sessionSearch.data.dependent = false;
        this.selectChangeOrg();
    }
}
