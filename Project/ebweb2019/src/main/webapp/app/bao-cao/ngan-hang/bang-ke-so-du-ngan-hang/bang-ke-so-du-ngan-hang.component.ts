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
import { DATE_FORMAT } from 'app/shared';
import { BaoCao } from 'app/app.constants';
import { RequestReport } from 'app/bao-cao/reqest-report.model';
import { BaoCaoService } from 'app/bao-cao/bao-cao.service';
import { HttpResponse } from '@angular/common/http';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { IAccountList } from 'app/shared/model/account-list.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { ROLE } from 'app/role.constants';
import { TreeOrganizationUnit } from 'app/shared/model/organization-unit-tree.model';

@Component({
    selector: 'eb-bang-ke-so-du-ngan-hang',
    templateUrl: './bang-ke-so-du-ngan-hang.component.html',
    styleUrls: ['./bang-ke-so-du-ngan-hang.component.css']
})
export class BangKeSoDuNganHangComponent implements OnInit {
    requestReport: RequestReport;
    dataSession: RequestReport;
    createDate: Moment;
    fromDate: Moment;
    toDate: Moment;
    listTimeLine: any[];
    timeLineVoucher: any;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    account: any;
    modalRef: NgbModalRef;
    option: number;
    organizationUnit: TreeOrganizationUnit;
    treeOrganizationUnits: any[];
    currencies: ICurrency[];
    currencyCode: string;
    currency: ICurrency;
    accountLists: IAccountList[];
    accountNumber: string;
    isDependent: boolean;
    isShowDependent: boolean;
    typeShowCurrency: boolean;
    currencyID: string;
    ROLE = ROLE;
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
        private currencyService: CurrencyService,
        private accountListService: AccountListService
    ) {}

    ngOnInit() {
        this.option = 1;
        this.isDependent = false;
        this.typeShowCurrency = false;
        this.principal.identity().then(account => {
            this.account = account;
            this.currencyCode = this.account.organizationUnit.currencyID;
            this.currencyID = this.account.organizationUnit.currencyID;
            this.requestReport = {};
            this.treeOrganizationUnits = [];
            this.isKetXuat = !this.utilsService.isPackageDemo(account);
            this.listTimeLine = this.utilsService.getCbbTimeLine();
            this.timeLineVoucher = this.listTimeLine[4].value;
            this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
            this.getSessionData();
            this.organizationUnitService.getChildCompanyByID().subscribe((res: HttpResponse<any>) => {
                this.treeOrganizationUnits = res.body.orgTrees;
                this.organizationUnit = res.body.currentOrgLogin;
                this.selectChangeOrg();
            });
            this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
                this.currencies = res.body;
            });
            this.accountListService.getAccountStartWith112().subscribe((res: HttpResponse<ICurrency[]>) => {
                this.accountLists = res.body;
            });
        });
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

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('searchBangKeSoDuNganHang'));
        if (this.dataSession) {
            this.toDate = this.dataSession.toDate;
            this.fromDate = this.dataSession.fromDate;
            this.currencyCode = this.dataSession.currencyID;
            this.accountNumber = this.dataSession.accountNumber;
            this.timeLineVoucher = this.dataSession.timeLineVoucher ? this.dataSession.timeLineVoucher : '0';
        } else {
            this.dataSession = {};
        }
    }

    setSessionData() {
        this.dataSession.toDate = this.toDate;
        this.dataSession.fromDate = this.fromDate;
        this.dataSession.timeLineVoucher = this.timeLineVoucher ? this.timeLineVoucher : '';
        this.dataSession.accountNumber = this.accountNumber;
        this.dataSession.currencyID = this.currencyCode;
        sessionStorage.setItem('searchBangKeSoDuNganHang', JSON.stringify(this.dataSession));
    }

    showReport() {
        if (!this.organizationUnit) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.organizationUnitNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return;
        }
        if (!this.fromDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.soQuyTienMatPopup.fromDateNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return;
        }
        if (!this.toDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.soQuyTienMatPopup.toDateNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return;
        }
        if (this.fromDate > this.toDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBDeposit.fromDateMustBeLessThanToDate'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return;
        }
        if (!this.currencyCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.currencyCodeNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return;
        }
        if (!this.accountNumber) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.accountNumberNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return;
        }
        this.setSessionData();
        this.requestReport = {};
        this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
        this.requestReport.toDate = moment(this.toDate, DATE_FORMAT);
        this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT);
        this.requestReport.typeReport = BaoCao.Ngan_Hang.BANG_KE_SO_DU_NGAN_HANG;
        this.requestReport.currencyID = this.currencyCode;
        this.requestReport.dependent = this.isDependent;
        this.requestReport.accountNumber = this.accountNumber;
        if (this.currencyID === this.currencyCode) {
            this.requestReport.typeShowCurrency = false;
        } else {
            this.requestReport.typeShowCurrency = this.typeShowCurrency;
        }
        this.baoCaoService.getReport(this.requestReport);
    }

    selectChangeBeginDateAndEndDate(intTimeLine: String) {
        if (intTimeLine) {
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
            this.fromDate = moment(this.objTimeLine.dtBeginDate);
            this.toDate = moment(this.objTimeLine.dtEndDate);
            this.createDate = this.fromDate;
        }
    }

    exportExcel() {
        if (!this.organizationUnit) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.organizationUnitNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return;
        }
        if (!this.fromDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.soQuyTienMatPopup.fromDateNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return;
        }
        if (!this.toDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.soQuyTienMatPopup.toDateNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return;
        }
        if (this.fromDate > this.toDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBDeposit.fromDateMustBeLessThanToDate'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return;
        }
        if (!this.currencyCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.currencyCodeNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return;
        }
        if (!this.accountNumber) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.accountNumberNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return;
        }
        this.setSessionData();

        this.setSessionData();
        this.requestReport = {};
        this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
        this.requestReport.toDate = moment(this.toDate, DATE_FORMAT);
        this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT);
        this.requestReport.typeReport = BaoCao.Ngan_Hang.BANG_KE_SO_DU_NGAN_HANG;
        this.requestReport.fileName = BaoCao.Ngan_Hang.BANG_KE_SO_DU_NGAN_HANG_XLS;
        this.requestReport.currencyID = this.currencyCode;
        this.requestReport.dependent = this.isDependent;
        this.requestReport.accountNumber = this.accountNumber;
        this.baoCaoService.exportExcel(this.requestReport).subscribe(
            res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);

                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = BaoCao.Ngan_Hang.BANG_KE_SO_DU_NGAN_HANG_XLS;
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
