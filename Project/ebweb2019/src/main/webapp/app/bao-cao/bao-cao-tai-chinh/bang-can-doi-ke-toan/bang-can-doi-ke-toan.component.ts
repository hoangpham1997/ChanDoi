import { Component, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { JhiEventManager } from 'ng-jhipster';
import * as moment from 'moment';
import { Moment } from 'moment';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { RefModalService } from '../../../core/login/ref-modal.service';
import { DATE_FORMAT, DATE_FORMAT_SEARCH } from 'app/shared';
import { BaoCao, TypeID } from 'app/app.constants';
import { IRequestReport, RequestReport } from 'app/bao-cao/reqest-report.model';
import { BaoCaoService } from 'app/bao-cao/bao-cao.service';
import { HttpResponse } from '@angular/common/http';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';
import { ROLE } from 'app/role.constants';

@Component({
    selector: 'eb-calculate-ow-repository',
    templateUrl: './bang-can-doi-ke-toan.component.html',
    styleUrls: ['./bang-can-doi-ke-toan.component.css']
})
export class BangCanDoiKeToanComponent implements OnInit {
    requestReport: RequestReport;
    dataSession: IRequestReport;
    createDate: Moment;
    fromDate: Moment;
    toDate: Moment;
    listTimeLine: any[];
    timeLineVoucher: any;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    account: any;
    modalRef: NgbModalRef;
    option: number;
    similarBranch: boolean;
    organizationUnit: any;
    treeOrganizationUnits: any[];
    ROLE_KetXuat = ROLE.BangCanDoiKeToan_KetXuat;
    isKetXuat: boolean;

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
        private organizationUnitService: OrganizationUnitService
    ) {}

    ngOnInit() {
        this.option = 1;
        this.principal.identity().then(account => {
            this.account = account;
            this.dataSession = {};
            this.listTimeLine = this.utilsService.getCbbTimeLine();
            this.isKetXuat = !this.utilsService.isPackageDemo(account);
            this.timeLineVoucher = this.listTimeLine[4].value;
            this.createDate = moment(Date());
            this.similarBranch = false;
            this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
            this.getSessionData();
            // this.organizationUnitService.getChildCompanyByID().subscribe((res: HttpResponse<any>) => {
            //     this.treeOrganizationUnits = res.body.orgTrees;
            //     this.organizationUnit = res.body.currentOrgLogin;
            //     this.baoCaoService.checkHiddenDependent(this.treeOrganizationUnits);
            //     this.baoCaoService.checkHiddenFirstCompany(this.organizationUnit);
            // });
        });
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('searchBangCanDoiKeToan'));
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
        sessionStorage.setItem('searchBangCanDoiKeToan', JSON.stringify(this.dataSession));
    }

    showReport() {
        if (this.checkErr()) {
            this.setSessionData();
            this.requestReport = {};
            this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT);
            this.requestReport.option = true;
            this.requestReport.dependent = this.similarBranch;
            this.requestReport.typeReport = BaoCao.BaoCaoTaiChinh.BANG_CAN_DOI_KE_TOAN;
            this.requestReport.createDate = moment(this.createDate, DATE_FORMAT_SEARCH).format(DATE_FORMAT);
            this.baoCaoService.getReport(this.requestReport);
        }
    }

    exportExcel() {
        if (this.checkErr()) {
            this.setSessionData();
            this.requestReport = {};
            this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT);
            this.requestReport.option = true;
            this.requestReport.dependent = this.similarBranch;
            this.requestReport.typeReport = BaoCao.BaoCaoTaiChinh.BANG_CAN_DOI_KE_TOAN;
            this.requestReport.fileName = BaoCao.BaoCaoTaiChinh.BANG_CAN_DOI_KE_TOAN_XLS;
            this.requestReport.createDate = moment(this.createDate, DATE_FORMAT_SEARCH).format(DATE_FORMAT);
            this.baoCaoService.exportExcel(this.requestReport).subscribe(
                res => {
                    const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                    const fileURL = URL.createObjectURL(blob);

                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    const name = BaoCao.BaoCaoTaiChinh.BANG_CAN_DOI_KE_TOAN_XLS;
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

    selectChangeBeginDateAndEndDate(intTimeLine: String) {
        if (intTimeLine) {
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
            this.fromDate = moment(this.objTimeLine.dtBeginDate);
            this.toDate = moment(this.objTimeLine.dtEndDate);
        }
    }

    getCurrentDate(): { year; month; day } {
        const _date = moment();
        return { year: _date.year(), month: _date.month() + 1, day: _date.date() };
    }
}
