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
import { BaoCao, SO_LAM_VIEC } from 'app/app.constants';
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
import { ICostSet } from 'app/shared/model/cost-set.model';
import { CostSetService } from 'app/entities/cost-set';
import { EMContractService } from 'app/entities/em-contract';
import { IEMContract } from 'app/shared/model/em-contract.model';

@Component({
    selector: 'eb-so-chi-phi-san-xuat-kinh-doanht',
    templateUrl: './so-chi-phi-san-xuat-kinh-doanh.component.html',
    styleUrls: ['./so-chi-phi-san-xuat-kinh-doanh.component.css']
})
export class SoChiPhiSanXuatKinhDoanhComponent implements OnInit {
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
    groupTheSameItem: boolean;
    bankAccountDetailID: string;
    isDependent: boolean;
    isDungChungTKNH: string;
    ROLE_KetXuat = ROLE.SoChiPhiSanXuatKinhDoanh_KetXuat;
    isShowDependent: boolean;
    isKetXuat: boolean;
    costSets: ICostSet[];
    costSetsNotChange: ICostSet[];
    eMContracts: IEMContract[];
    objectType: any;
    isSoTaiChinh: boolean;
    listObjectType: any[];
    searchCostSetCode: any;
    searchCostSetName: any;

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
        private bankAccountDetailsService: BankAccountDetailsService,
        private costSetService: CostSetService,
        private eMContractService: EMContractService
    ) {}

    ngOnInit() {
        this.option = 1;
        this.objectType = 1;
        this.isDependent = false;
        this.groupTheSameItem = true;
        this.principal.identity().then(account => {
            this.account = account;
            this.isDungChungTKNH = account.systemOption.find(a => a.code === 'TCKHAC_SDDMTKNH' && a.data).data;
            this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
            this.isKetXuat = !this.utilsService.isPackageDemo(this.account);
            this.dataSession = {};
            this.currencyCode = this.account.organizationUnit.currencyID;
            this.treeOrganizationUnits = [];
            this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
                this.currencies = res.body;
                this.listTimeLine = this.utilsService.getCbbTimeLine();
                this.timeLineVoucher = this.listTimeLine[4].value;
                this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
                this.getSessionData();
            });
            this.organizationUnitService.getChildCompanyByID().subscribe((res: HttpResponse<any>) => {
                this.translate
                    .get(['ebwebApp.baoCao.soChiPhiSanXuatKinhDoanh.costSet', 'ebwebApp.baoCao.soChiPhiSanXuatKinhDoanh.contract'])
                    .subscribe(res => {
                        this.listObjectType = [
                            { value: 1, name: res['ebwebApp.baoCao.soChiPhiSanXuatKinhDoanh.costSet'] },
                            { value: 2, name: res['ebwebApp.baoCao.soChiPhiSanXuatKinhDoanh.contract'] }
                        ];
                    });
                this.treeOrganizationUnits = res.body.orgTrees;
                this.organizationUnit = res.body.currentOrgLogin;
                this.selectChangeOrg();
            });

            this.accountListService.getAccountLists().subscribe((res: HttpResponse<IAccountList[]>) => {
                this.accountLists = res.body.filter(
                    a =>
                        (a.isActive &&
                            (a.accountNumber.startsWith('621') ||
                                a.accountNumber.startsWith('622') ||
                                a.accountNumber.startsWith('623') ||
                                a.accountNumber.startsWith('627') ||
                                a.accountNumber.startsWith('154'))) ||
                        a.accountNumber.startsWith('631') ||
                        a.accountNumber.startsWith('641') ||
                        a.accountNumber.startsWith('642') ||
                        a.accountNumber.startsWith('242') ||
                        a.accountNumber.startsWith('335') ||
                        a.accountNumber.startsWith('632')
                );
            });
            this.costSetService.getCostSets().subscribe((res: HttpResponse<ICostSet[]>) => {
                this.costSets = res.body;
                this.costSetsNotChange = res.body;
            });
            this.eMContractService.getEMContracts().subscribe((res: HttpResponse<IEMContract[]>) => {
                this.eMContracts = res.body;
            });
        });
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('searchSoChiPhiSXKD'));
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
        sessionStorage.setItem('searchSoChiPhiSXKD', JSON.stringify(this.dataSession));
    }

    showReport() {
        if (this.checkError()) {
            this.setSessionData();
            this.requestReport = {};
            this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
            this.requestReport.groupTheSameItem = this.groupTheSameItem;
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
            this.requestReport.currencyID = this.currencyCode;
            this.requestReport.accountNumber = this.accountNumber;
            this.requestReport.dependent = this.isDependent;
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT);
            this.requestReport.typeReport = BaoCao.Gia_Thanh.SO_CHI_PHI_SAN_XUAT_KINH_DOANH;
            this.requestReport.fileName = BaoCao.Gia_Thanh.SO_CHI_PHI_SAN_XUAT_KINH_DOANH_XLS;
            this.requestReport.listCostSets = this.costSets.filter(n => n.checked).map(n => n.id);
            if (
                this.objectType === 1 &&
                ((this.requestReport.listCostSets && this.requestReport.listCostSets.length === 0) || !this.requestReport.listCostSets)
            ) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.baoCao.soChiPhiSanXuatKinhDoanh.costSetsNotBeBlank'),
                    this.translate.instant('ebwebApp.mBDeposit.message')
                );
                return;
            }
            if (this.objectType === 2) {
                return;
            }
            this.baoCaoService.exportExcel(this.requestReport).subscribe(
                res => {
                    const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                    const fileURL = URL.createObjectURL(blob);

                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    const name = BaoCao.Gia_Thanh.SO_CHI_PHI_SAN_XUAT_KINH_DOANH_XLS;
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
        this.costSetService
            .getCostSetsByOrg({ orgID: this.organizationUnit.parent.id, isDependent: this.isDependent ? this.isDependent : false })
            .subscribe((res: HttpResponse<ICostSet[]>) => {
                this.costSets = res.body;
            });
        // this.accountListService.getAccountListsByOrg({orgID: this.organizationUnit.parent.id}).subscribe((res: HttpResponse<ICostSet[]>) => {
        //     this.costSets = res.body;
        // });
    }

    selectChangeIsDependent() {
        this.costSetService
            .getCostSetsByOrg({ orgID: this.organizationUnit.parent.id, isDependent: this.isDependent ? this.isDependent : false })
            .subscribe((res: HttpResponse<ICostSet[]>) => {
                this.costSets = res.body;
            });
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

    isCheckAll() {
        if (this.objectType === 1) {
            if (this.costSets) {
                return this.costSets.every(item => item.checked) && this.costSets.length;
            } else {
                return false;
            }
        } else {
            if (this.eMContracts) {
                return this.eMContracts.every(item => item.checked) && this.eMContracts.length;
            } else {
                return false;
            }
        }
    }

    checkAll() {
        if (this.objectType === 1) {
            const isCheck = this.costSets.every(item => item.checked) && this.costSets.length;
            this.costSets.forEach(item => (item.checked = !isCheck));
        } else {
            const isCheck = this.eMContracts.every(item => item.checked) && this.eMContracts.length;
            this.eMContracts.forEach(item => (item.checked = !isCheck));
        }
    }

    check(accountList: any) {
        accountList.checked = !accountList.checked;
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }
    changeSearchValue() {
        this.costSets = this.costSetsNotChange.filter(
            n =>
                (this.searchCostSetCode
                    ? n.costSetCode.toUpperCase().includes(this.searchCostSetCode.toUpperCase())
                    : n.costSetCode.toUpperCase().includes('')) &&
                (this.searchCostSetName
                    ? n.costSetName.toUpperCase().includes(this.searchCostSetName.toUpperCase())
                    : n.costSetName.toUpperCase().includes(''))
        );
    }
}
