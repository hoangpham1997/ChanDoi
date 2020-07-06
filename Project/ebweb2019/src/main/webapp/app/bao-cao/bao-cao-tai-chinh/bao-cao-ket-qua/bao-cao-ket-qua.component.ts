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
import { BaoCao } from 'app/app.constants';
import { IRequestReport, RequestReport } from 'app/bao-cao/reqest-report.model';
import { BaoCaoService } from 'app/bao-cao/bao-cao.service';
import { HttpResponse } from '@angular/common/http';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';
import { ROLE } from 'app/role.constants';

@Component({
    selector: 'eb-calculate-ow-repository',
    templateUrl: './bao-cao-ket-qua.component.html',
    styleUrls: ['./bao-cao-ket-qua.component.css']
})
export class BaoCaoKetQuaComponent implements OnInit {
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
    typeName: string;
    KET_QUA_HOAT_DONG_KINH_DOANH = BaoCao.BaoCaoTaiChinh.KET_QUA_HOAT_DONG_KINH_DOANH;
    LUU_CHUYEN_TIEN_TE_TT = BaoCao.BaoCaoTaiChinh.LUU_CHUYEN_TIEN_TE_TT;
    LUU_CHUYEN_TIEN_TE_GT = BaoCao.BaoCaoTaiChinh.LUU_CHUYEN_TIEN_TE_GT;
    THUYET_MINH_BAO_CAO_TAI_CHINH = BaoCao.BaoCaoTaiChinh.THUYET_MINH_BAO_CAO_TAI_CHINH;
    ROLE_KetXuat: any;
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
        if (this.typeName === this.KET_QUA_HOAT_DONG_KINH_DOANH) {
            this.ROLE_KetXuat = ROLE.BaoCaoKetQuaHoatDongKinhDoanh_KetXuat;
        } else if (this.typeName === this.LUU_CHUYEN_TIEN_TE_TT) {
            this.ROLE_KetXuat = ROLE.BaoCaoLuuChuyenTienTeTT_KetXuat;
        } else if (this.typeName === this.LUU_CHUYEN_TIEN_TE_GT) {
            this.ROLE_KetXuat = ROLE.BaoCaoLuuChuyenTienTeGT_KetXuat;
        } else if (this.typeName === this.THUYET_MINH_BAO_CAO_TAI_CHINH) {
            this.ROLE_KetXuat = ROLE.BanThuyetMinhBaoCaoTaiChinh_KetXuat;
        }
        this.principal.identity().then(account => {
            this.account = account;
            this.isKetXuat = !this.utilsService.isPackageDemo(account);
            this.listTimeLine = this.utilsService.getCbbTimeLine();
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
        if (this.typeName === this.KET_QUA_HOAT_DONG_KINH_DOANH) {
            this.dataSession = JSON.parse(sessionStorage.getItem('searchHoatDongKinhDoanh'));
        } else if (this.typeName === this.LUU_CHUYEN_TIEN_TE_TT) {
            this.dataSession = JSON.parse(sessionStorage.getItem('searchTienTeTT'));
        } else if (this.typeName === this.LUU_CHUYEN_TIEN_TE_GT) {
            this.dataSession = JSON.parse(sessionStorage.getItem('searchTienTeGT'));
        } else if (this.typeName === this.THUYET_MINH_BAO_CAO_TAI_CHINH) {
            this.dataSession = JSON.parse(sessionStorage.getItem('searchBaoCaoTaiChinh'));
        }
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
        if (this.typeName === this.KET_QUA_HOAT_DONG_KINH_DOANH) {
            sessionStorage.setItem('searchHoatDongKinhDoanh', JSON.stringify(this.dataSession));
        } else if (this.typeName === this.LUU_CHUYEN_TIEN_TE_TT) {
            sessionStorage.setItem('searchTienTeTT', JSON.stringify(this.dataSession));
        } else if (this.typeName === this.LUU_CHUYEN_TIEN_TE_GT) {
            sessionStorage.setItem('searchTienTeGT', JSON.stringify(this.dataSession));
        } else if (this.typeName === this.THUYET_MINH_BAO_CAO_TAI_CHINH) {
            sessionStorage.setItem('searchBaoCaoTaiChinh', JSON.stringify(this.dataSession));
        }
    }

    showReport() {
        if (this.checkErr()) {
            this.setSessionData();
            this.requestReport = {};
            this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT);
            this.requestReport.dependent = this.similarBranch;
            if (this.typeName === BaoCao.BaoCaoTaiChinh.KET_QUA_HOAT_DONG_KINH_DOANH) {
                this.requestReport.typeReport = BaoCao.BaoCaoTaiChinh.KET_QUA_HOAT_DONG_KINH_DOANH;
            } else if (this.typeName === BaoCao.BaoCaoTaiChinh.LUU_CHUYEN_TIEN_TE_TT) {
                this.requestReport.typeReport = BaoCao.BaoCaoTaiChinh.LUU_CHUYEN_TIEN_TE_TT;
                this.requestReport.option = true;
            } else if (this.typeName === BaoCao.BaoCaoTaiChinh.LUU_CHUYEN_TIEN_TE_GT) {
                this.requestReport.typeReport = BaoCao.BaoCaoTaiChinh.LUU_CHUYEN_TIEN_TE_GT;
                this.requestReport.option = false;
            } else if (this.typeName === BaoCao.BaoCaoTaiChinh.THUYET_MINH_BAO_CAO_TAI_CHINH) {
                this.requestReport.typeReport = BaoCao.BaoCaoTaiChinh.THUYET_MINH_BAO_CAO_TAI_CHINH;
            }
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
            this.requestReport.dependent = this.similarBranch;
            if (this.typeName === BaoCao.BaoCaoTaiChinh.KET_QUA_HOAT_DONG_KINH_DOANH) {
                this.requestReport.typeReport = BaoCao.BaoCaoTaiChinh.KET_QUA_HOAT_DONG_KINH_DOANH;
                this.requestReport.fileName = BaoCao.BaoCaoTaiChinh.KET_QUA_HOAT_DONG_KINH_DOANH_XLS;
            } else if (this.typeName === BaoCao.BaoCaoTaiChinh.LUU_CHUYEN_TIEN_TE_TT) {
                this.requestReport.typeReport = BaoCao.BaoCaoTaiChinh.LUU_CHUYEN_TIEN_TE_TT;
                this.requestReport.fileName = BaoCao.BaoCaoTaiChinh.LUU_CHUYEN_TIEN_TE_TT_XLS;
                this.requestReport.option = true;
            } else if (this.typeName === BaoCao.BaoCaoTaiChinh.LUU_CHUYEN_TIEN_TE_GT) {
                this.requestReport.typeReport = BaoCao.BaoCaoTaiChinh.LUU_CHUYEN_TIEN_TE_GT;
                this.requestReport.fileName = BaoCao.BaoCaoTaiChinh.LUU_CHUYEN_TIEN_TE_GT_XLS;
                this.requestReport.option = false;
            } else if (this.typeName === BaoCao.BaoCaoTaiChinh.THUYET_MINH_BAO_CAO_TAI_CHINH) {
                this.requestReport.typeReport = BaoCao.BaoCaoTaiChinh.THUYET_MINH_BAO_CAO_TAI_CHINH;
                this.requestReport.fileName = BaoCao.BaoCaoTaiChinh.THUYET_MINH_BAO_CAO_TAI_CHINH_XLS;
            }
            this.baoCaoService.exportExcel(this.requestReport).subscribe(
                res => {
                    const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                    const fileURL = URL.createObjectURL(blob);

                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    const name = this.requestReport.fileName;
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
