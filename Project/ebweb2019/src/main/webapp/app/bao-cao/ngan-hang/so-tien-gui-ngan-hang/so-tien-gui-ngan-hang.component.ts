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
import { RequestReport } from 'app/bao-cao/reqest-report.model';
import { BaoCaoService } from 'app/bao-cao/bao-cao.service';
import { HttpResponse } from '@angular/common/http';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { IAccountList } from 'app/shared/model/account-list.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { BankAccountDetails, IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { IBank } from 'app/shared/model/bank.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { ROLE } from 'app/role.constants';
import { TreeOrganizationUnit } from 'app/shared/model/organization-unit-tree.model';

@Component({
    selector: 'eb-so-quy-tien-mat',
    templateUrl: './so-tien-gui-ngan-hang.component.html',
    styleUrls: ['./so-tien-gui-ngan-hang.component.css']
})
export class SoTienGuiNganHangComponent implements OnInit {
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
    bankAccountDetails: IBankAccountDetails[];
    bankAccountDetailsForAccType: IBankAccountDetails[];
    accountNumber: string;
    groupTheSameItem: boolean;
    bankAccountDetailID: string;
    isDependent: boolean;
    isDungChungTKNH: string;
    ROLE_KetXuat = ROLE.SoTienGuiNganHang_KetXuat;
    isShowDependent: boolean;
    typeShowCurrency: boolean;
    currencyID: string;
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
        private accountListService: AccountListService,
        private bankAccountDetailsService: BankAccountDetailsService
    ) {}

    ngOnInit() {
        this.option = 1;
        this.isDependent = false;
        this.typeShowCurrency = false;
        this.groupTheSameItem = true;
        this.principal.identity().then(account => {
            this.account = account;
            this.isDungChungTKNH = account.systemOption.find(a => a.code === 'TCKHAC_SDDMTKNH' && a.data).data;
            this.isKetXuat = !this.utilsService.isPackageDemo(this.account);
            this.dataSession = {};
            this.currencyCode = this.account.organizationUnit.currencyID;
            this.currencyID = this.account.organizationUnit.currencyID;
            this.treeOrganizationUnits = [];
            this.organizationUnitService.getChildCompanyByID().subscribe((res: HttpResponse<any>) => {
                this.treeOrganizationUnits = res.body.orgTrees;
                this.organizationUnit = res.body.currentOrgLogin;
                this.selectChangeOrg();
            });
            this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
                this.currencies = res.body;
            });
            this.accountListService.getAccountStartWith112().subscribe((res: HttpResponse<IAccountList[]>) => {
                this.accountLists = res.body;
            });
            this.bankAccountDetails = [];
            this.bankAccountDetailsService.getBankAccountDetails().subscribe((res: HttpResponse<IBankAccountDetails[]>) => {
                this.bankAccountDetails.push(Object.assign({}));
                this.bankAccountDetails[0].id = 'Tất cả';
                this.bankAccountDetails[0].bankAccount = 'Tất cả';
                this.bankAccountDetails[0].bankName = 'Tất cả';
                for (let i = 0; i < res.body.length; i++) {
                    this.bankAccountDetails.push(res.body[i]);
                }
                this.bankAccountDetailID = 'Tất cả';
                console.log(this.bankAccountDetails);
                this.listTimeLine = this.utilsService.getCbbTimeLine();
                this.timeLineVoucher = this.listTimeLine[4].value;
                this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
                this.getSessionData();
            });
            this.bankAccountDetailsForAccType = [];
            this.bankAccountDetailsService.getBankAccountDetailsForAccType().subscribe((res: HttpResponse<IBankAccountDetails[]>) => {
                this.bankAccountDetailsForAccType.push(Object.assign({}));
                this.bankAccountDetailsForAccType[0].id = 'Tất cả';
                this.bankAccountDetailsForAccType[0].bankAccount = 'Tất cả';
                this.bankAccountDetailsForAccType[0].bankName = 'Tất cả';
                console.log(this.bankAccountDetailsForAccType);
                for (let i = 0; i < res.body.length; i++) {
                    this.bankAccountDetailsForAccType.push(res.body[i]);
                }
            });
        });
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('searchSoTienGuiNganHang'));
        if (this.dataSession) {
            this.toDate = this.dataSession.toDate;
            this.fromDate = this.dataSession.fromDate;
            this.currencyCode = this.dataSession.currencyID;
            this.accountNumber = this.dataSession.accountNumber;
            this.bankAccountDetailID = this.dataSession.bankAccountDetailID != null ? this.dataSession.bankAccountDetailID : null;
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
        this.dataSession.bankAccountDetailID = this.bankAccountDetailID !== null ? this.bankAccountDetailID : null;
        sessionStorage.setItem('searchSoTienGuiNganHang', JSON.stringify(this.dataSession));
    }

    showReport() {
        if (this.checkError()) {
            this.setSessionData();
            this.requestReport = {};
            this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
            this.requestReport.groupTheSameItem = this.groupTheSameItem;
            if (this.bankAccountDetailID === 'Tất cả') {
                this.requestReport.bankAccountDetail = null;
            } else {
                this.requestReport.bankAccountDetailID = this.bankAccountDetailID;
            }
            if (this.currencyID === this.currencyCode) {
                this.requestReport.typeShowCurrency = false;
            } else {
                this.requestReport.typeShowCurrency = this.typeShowCurrency;
            }
            this.requestReport.currencyID = this.currencyCode;
            this.requestReport.accountNumber = this.accountNumber;
            this.requestReport.dependent = this.isDependent;
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT);
            this.requestReport.typeReport = BaoCao.Ngan_Hang.SO_TIEN_GUI_NGAN_HANG;
            this.baoCaoService.getReport(this.requestReport);
        }
    }

    checkError(): boolean {
        if (!this.organizationUnit) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.organizationUnitNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
        }
        if (!this.fromDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.soQuyTienMatPopup.fromDateNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
        }
        if (!this.toDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.soQuyTienMatPopup.toDateNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
        }
        if (this.fromDate > this.toDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBDeposit.fromDateMustBeLessThanToDate'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
        }
        if (!this.accountNumber) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.accountNumberNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
        }
        if (!this.currencyCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.currencyCodeNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
        }
        if (!this.bankAccountDetailID) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.soTienGuiNganHangPopup.bankAccountDetailIDNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
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

    exportExcel() {
        if (this.checkError()) {
            this.setSessionData();
            this.requestReport = {};
            this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
            this.requestReport.groupTheSameItem = this.groupTheSameItem;
            if (this.bankAccountDetailID === 'Tất cả') {
                this.requestReport.bankAccountDetail = null;
            } else {
                this.requestReport.bankAccountDetailID = this.bankAccountDetailID;
            }
            this.requestReport.currencyID = this.currencyCode;
            this.requestReport.accountNumber = this.accountNumber;
            this.requestReport.dependent = this.isDependent;
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT);
            this.requestReport.typeReport = BaoCao.Ngan_Hang.SO_TIEN_GUI_NGAN_HANG;
            this.requestReport.fileName = BaoCao.Ngan_Hang.SO_TIEN_GUI_NGAN_HANG_XLS;
            this.baoCaoService.exportExcel(this.requestReport).subscribe(
                res => {
                    const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                    const fileURL = URL.createObjectURL(blob);

                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    const name = BaoCao.Ngan_Hang.SO_TIEN_GUI_NGAN_HANG_XLS;
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
                this.isDependent = false;
                this.isShowDependent = false;
            } else {
                this.isShowDependent = this.checkChildren(this.organizationUnit);
            }
        }
        if (this.organizationUnit && this.organizationUnit.parent.id) {
            this.bankAccountDetailsService
                .getBankAccountDetailsByOrgID({ orgID: this.organizationUnit.parent.id })
                .subscribe((res: HttpResponse<IBankAccountDetails[]>) => {
                    this.bankAccountDetails = [];
                    this.bankAccountDetails.push(Object.assign({}));
                    this.bankAccountDetails[0].id = 'Tất cả';
                    this.bankAccountDetails[0].bankAccount = 'Tất cả';
                    this.bankAccountDetails[0].bankName = 'Tất cả';
                    for (let i = 0; i < res.body.length; i++) {
                        this.bankAccountDetails.push(res.body[i]);
                    }
                });
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
