import { Component, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
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
import { SoQuyTienMatComponent } from 'app/bao-cao/quy/so-quy-tien-mat/so-quy-tien-mat.component';
import { ViewBaoCaoHddtComponent } from 'app/bao-cao/hoa-don-dien-tu/view/view-bao-cao-hddt.component';
import { NhatKyChungComponent } from 'app/bao-cao/tong-hop/nhat-ky-chung/nhat-ky-chung.component';
import { TreeOrganizationUnit } from 'app/shared/model/organization-unit-tree.model';

@Component({
    selector: 'eb-tinh-hinh-su-dung-hoa-don',
    templateUrl: './tinh-hinh-su-dung-hoa-don.component.html',
    styleUrls: ['tinh-hinh-su-dung-hoa-don.component.css']
})
export class TinhHinhSuDungHoaDonComponent implements OnInit {
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
    organizationUnit: TreeOrganizationUnit;
    treeOrganizationUnits: any[];
    private ngbModalRef: NgbModalRef;

    constructor(
        public activeModal: NgbActiveModal,
        private principal: Principal,
        private toastr: ToastrService,
        public translate: TranslateService,
        public eventManager: JhiEventManager,
        private baoCaoService: BaoCaoService,
        public utilsService: UtilsService,
        private organizationUnitService: OrganizationUnitService,
        private modalService: NgbModal
    ) {
        this.requestReport = {};
    }

    ngOnInit(): void {
        this.groupTheSameItem = true;
        this.showAccumAmount = true;
        this.principal.identity().then(account => {
            this.account = account;
            this.treeOrganizationUnits = [];
            this.listTimeLine = this.utilsService.getCbbTimeLine();
            this.timeLineVoucher = this.listTimeLine[4].value;
            this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
            this.organizationUnitService.getChildCompanyByID().subscribe((res: HttpResponse<any>) => {
                this.treeOrganizationUnits = res.body.orgTrees;
                this.organizationUnit = res.body.currentOrgLogin;
            });
        });
    }

    accept() {
        if (this.checkErr()) {
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT_SEARCH);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT_SEARCH);
            this.requestReport.showAccumAmount = this.showAccumAmount;
            this.requestReport.groupTheSameItem = this.groupTheSameItem;
            this.requestReport.typeReport = BaoCao.HDDT.BAO_CAO_TINH_HINH_SU_DUNG_HD;
            this.requestReport.companyID = this.organizationUnit.parent.id;
            this.baoCaoService.getReportHDDT(this.requestReport).subscribe((res: HttpResponse<string>) => {
                this.ngbModalRef = this.modalService.open(ViewBaoCaoHddtComponent as Component, {
                    size: 'lg',
                    backdrop: 'static',
                    windowClass: 'width-80 width-50'
                });
                this.ngbModalRef.componentInstance.data = res.body;
            });
        }
    }

    view() {
        this.modalService.open(ViewBaoCaoHddtComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
    }

    checkErr() {
        if (!this.toDate || !this.fromDate) {
            this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
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
}
