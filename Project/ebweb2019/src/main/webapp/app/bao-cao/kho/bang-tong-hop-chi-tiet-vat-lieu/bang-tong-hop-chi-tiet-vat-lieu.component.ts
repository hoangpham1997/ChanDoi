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
import { IAccountList } from 'app/shared/model/account-list.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { MaterialGoodsConvertUnitService } from 'app/entities/material-goods-convert-unit';
import { ROLE } from 'app/role.constants';

@Component({
    selector: 'eb-calculate-ow-repository',
    templateUrl: './bang-tong-hop-chi-tiet-vat-lieu.component.html',
    styleUrls: ['./bang-tong-hop-chi-tiet-vat-lieu.component.css']
})
export class BangTongHopChiTietVatLieuComponent implements OnInit {
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
    organizationUnit: any;
    treeOrganizationUnits: any[];
    accounts: IAccountList[];
    accountNumber: string;
    units: any[];
    unitID: number;
    description: string;
    defaultDescription: string;
    similarBranch: boolean;
    ROLE_KetXuat = ROLE.BangTongHopChiTietVatLieu_KetXuat;
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
        private organizationUnitService: OrganizationUnitService,
        private accountListService: AccountListService
    ) {}

    ngOnInit() {
        this.option = 1;
        this.defaultDescription = this.translate.instant('ebwebApp.baoCao.soChiTietVatLieuDungCuSPHH.donViTinh');
        this.description = this.translate.instant('ebwebApp.baoCao.soChiTietVatLieuDungCuSPHH.donViTinhCD');
        this.units = [];
        this.principal.identity().then(account => {
            this.account = account;
            this.isKetXuat = !this.utilsService.isPackageDemo(account);
            this.listTimeLine = this.utilsService.getCbbTimeLine();
            this.timeLineVoucher = this.listTimeLine[4].value;
            this.similarBranch = false;
            this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
            this.getSessionData();
            // this.organizationUnitService.getChildCompanyByID().subscribe((res: HttpResponse<any>) => {
            //     this.treeOrganizationUnits = res.body.orgTrees;
            //     this.organizationUnit = res.body.currentOrgLogin;
            //     this.baoCaoService.checkHiddenDependent(this.treeOrganizationUnits);
            //     this.baoCaoService.checkHiddenFirstCompany(this.organizationUnit);
            // });
            this.accountListService.getAccountLists().subscribe((res: HttpResponse<IAccountList[]>) => {
                this.accounts = res.body.filter(x => x.accountNumber.startsWith('15') && !x.accountNumber.startsWith('151'));
            });
        });
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('searchBangTongHopChiTietVatLieu'));
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
        sessionStorage.setItem('searchBangTongHopChiTietVatLieu', JSON.stringify(this.dataSession));
    }

    showReport() {
        if (this.checkErr()) {
            this.setSessionData();
            this.requestReport = {};
            this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT);
            this.requestReport.accountList = [];
            this.requestReport.typeReport = BaoCao.Kho.TONG_HOP_CHI_TIET_VAT_LIEU;
            this.requestReport.dependent = this.similarBranch;
            if (this.accountNumber) {
                this.requestReport.accountList.push(this.accountNumber);
            } else {
                this.accounts.forEach(item => {
                    this.requestReport.accountList.push(item.accountNumber);
                });
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
            this.requestReport.accountList = [];
            this.requestReport.typeReport = BaoCao.Kho.TONG_HOP_CHI_TIET_VAT_LIEU;
            this.requestReport.fileName = BaoCao.Kho.TONG_HOP_CHI_TIET_VAT_LIEU_XLS;
            this.requestReport.dependent = this.similarBranch;
            if (this.accountNumber) {
                this.requestReport.accountList.push(this.accountNumber);
            } else {
                this.accounts.forEach(item => {
                    this.requestReport.accountList.push(item.accountNumber);
                });
            }
            this.baoCaoService.exportExcel(this.requestReport).subscribe(
                res => {
                    const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                    const fileURL = URL.createObjectURL(blob);

                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    const name = BaoCao.Kho.TONG_HOP_CHI_TIET_VAT_LIEU_XLS;
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
            this.createDate = this.fromDate;
        }
    }

    getCurrentDate(): { year; month; day } {
        const _date = moment();
        return { year: _date.year(), month: _date.month() + 1, day: _date.date() };
    }
}
