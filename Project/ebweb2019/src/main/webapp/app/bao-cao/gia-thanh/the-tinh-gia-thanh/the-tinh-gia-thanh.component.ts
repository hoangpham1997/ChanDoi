import { Component, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { JhiEventManager } from 'ng-jhipster';
import { RequestReport } from 'app/bao-cao/reqest-report.model';
import * as moment from 'moment';
import { Moment } from 'moment';
import { BaoCao, SO_LAM_VIEC } from 'app/app.constants';
import { DATE_FORMAT, DATE_FORMAT_SEARCH } from 'app/shared';
import { BaoCaoService } from 'app/bao-cao/bao-cao.service';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';
import { HttpResponse } from '@angular/common/http';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { AccountListService } from 'app/danhmuc/account-list';
import { IAccountList } from 'app/shared/model/account-list.model';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { ROLE } from 'app/role.constants';
import { TreeOrganizationUnit } from 'app/shared/model/organization-unit-tree.model';
import { ICPPeriod } from 'app/shared/model/cp-period.model';
import { CostSetMaterialGoodService } from 'app/entities/cost-set-material-good';
import { ICostSetMaterialGood } from 'app/shared/model/cost-set-material-good.model';
import { CostSetService } from 'app/entities/cost-set';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { EMContract, IEMContract } from 'app/shared/model/em-contract.model';
import { EMContractService } from 'app/entities/em-contract';
import { forEach } from '@angular/router/src/utils/collection';
import { PpGianDonService } from 'app/giathanh/phuong_phap_gian_don';

@Component({
    selector: 'eb-the-tinh-gia-thanh',
    templateUrl: './the-tinh-gia-thanh.component.html',
    styleUrls: ['the-tinh-gia-thanh.component.css']
})
export class TheTinhGiaThanhComponent implements OnInit {
    createDate: Moment;
    fromDate: Moment;
    toDate: Moment;
    requestReport: RequestReport;
    dataSession: RequestReport;
    account: any;
    modalRef: NgbModalRef;
    groupTheSameItem: boolean;
    organizationUnit: TreeOrganizationUnit;
    treeOrganizationUnits: any[];
    accountLists: IAccountList[];
    isDependent: boolean;
    ROLE_KetXuat = ROLE.TheTinhGiaThanh_KetXuat;
    isShowDependent: boolean;
    isKetXuat: boolean;
    typeMethod: any;
    listTypeMethod: any[];
    cPPeriods: ICPPeriod[];
    cPPeriodsData: ICPPeriod[];
    cPPeriodID: string;
    listQuantums: any;
    listQuantumsNotChange: any;
    costSets: ICostSet[];
    eMContracts: IEMContract[];
    listCostSets1: ICostSet[];
    listCostSets1NotChange: ICostSet[];
    listCostSets0: ICostSet[];
    listCostSets0NotChange: ICostSet[];
    isSoTaiChinh: boolean;
    searchQuantumCode: any;
    searchQuantumName: any;
    searchCostSetCode: any;
    searchCostSetCode1: any;
    searchCostSetName1: any;
    searchCostSetCode2: any;
    searchCostSetName2: any;

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
        private cPPeriodService: PpGianDonService,
        private costSetMaterialGoods: CostSetMaterialGoodService,
        private costSetService: CostSetService,
        private eMContractService: EMContractService
    ) {
        this.requestReport = {};
    }

    ngOnInit(): void {
        this.searchQuantumCode = '';
        this.searchQuantumName = '';
        this.searchCostSetCode = '';
        this.typeMethod = 0;
        this.groupTheSameItem = true;
        this.isDependent = false;
        this.principal.identity().then(account => {
            this.account = account;
            this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
            this.dataSession = {};
            this.translate
                .get([
                    'ebwebApp.baoCao.theTinhGiaThanh.gianDon',
                    'ebwebApp.baoCao.theTinhGiaThanh.heSo',
                    'ebwebApp.baoCao.theTinhGiaThanh.tiLe',
                    'ebwebApp.baoCao.theTinhGiaThanh.ctvv',
                    'ebwebApp.baoCao.theTinhGiaThanh.donHang',
                    'ebwebApp.baoCao.theTinhGiaThanh.hopDong'
                ])
                .subscribe(res => {
                    this.listTypeMethod = [
                        { value: 0, name: res['ebwebApp.baoCao.theTinhGiaThanh.gianDon'] },
                        { value: 1, name: res['ebwebApp.baoCao.theTinhGiaThanh.heSo'] },
                        { value: 2, name: res['ebwebApp.baoCao.theTinhGiaThanh.tiLe'] },
                        { value: 3, name: res['ebwebApp.baoCao.theTinhGiaThanh.ctvv'] },
                        { value: 4, name: res['ebwebApp.baoCao.theTinhGiaThanh.donHang'] },
                        { value: 5, name: res['ebwebApp.baoCao.theTinhGiaThanh.hopDong'] }
                    ];
                });
            this.isKetXuat = !this.utilsService.isPackageDemo(this.account);
            this.treeOrganizationUnits = [];
            this.accountListService.getAccountLists().subscribe((res: HttpResponse<IAccountList[]>) => {
                this.accountLists = res.body;
            });
            this.cPPeriodService.getCPPeriod().subscribe((res: HttpResponse<ICPPeriod[]>) => {
                this.cPPeriodsData = res.body;
                this.cPPeriods = res.body.filter(a => a.type === this.typeMethod);
            });
            this.costSetMaterialGoods.getAllByCompanyID({ typeMethod: 0 }).subscribe((res: HttpResponse<ICostSetMaterialGood[]>) => {
                this.listQuantums = res.body;
                this.listQuantumsNotChange = res.body;
            });
            this.eMContractService.getEMContracts().subscribe((res: HttpResponse<IEMContract[]>) => {
                this.eMContracts = res.body;
            });
            this.costSetService.getCostSets().subscribe((res: HttpResponse<ICostSet[]>) => {
                this.costSets = res.body;
                this.listCostSets0 = res.body.filter(a => a.costSetType === 0);
                this.listCostSets0NotChange = res.body.filter(a => a.costSetType === 0);
                this.listCostSets1 = res.body.filter(a => a.costSetType === 1);
                this.listCostSets1NotChange = res.body.filter(a => a.costSetType === 1);
            });
            this.getSessionData();
            this.organizationUnitService.getChildCompanyByID().subscribe((res: HttpResponse<any>) => {
                this.treeOrganizationUnits = res.body.orgTrees;
                this.organizationUnit = res.body.currentOrgLogin;
                this.selectChangeOrg();
            });
        });
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('searchSoKeToanChiTietQuyTienMat'));
        if (this.dataSession) {
            this.toDate = this.dataSession.toDate;
            this.fromDate = this.dataSession.fromDate;
        } else {
            this.dataSession = {};
        }
    }

    setSessionData() {
        this.dataSession.toDate = this.toDate;
        this.dataSession.fromDate = this.fromDate;
        sessionStorage.setItem('searchSoKeToanChiTietQuyTienMat', JSON.stringify(this.dataSession));
    }

    showReport() {
        if (this.checkError()) {
            this.setSessionData();
            this.requestReport.groupTheSameItem = this.groupTheSameItem;
            this.requestReport.typeReport = BaoCao.GiaThanh.THE_TINH_GIA_THANH;
            this.requestReport.companyID = this.organizationUnit.parent.id;
            this.requestReport.dependent = this.isDependent;
            this.requestReport.cPPeriodID = this.cPPeriodID;
            this.requestReport.typeMethod = this.typeMethod;
            if (this.typeMethod === 0 || this.typeMethod === 1 || this.typeMethod === 2) {
                this.requestReport.listCostSets = this.listQuantums.filter(n => n.checked).map(n => n.costSetID);
                this.requestReport.listMaterialGoods = this.listQuantums.filter(n => n.checked).map(n => n.materialGoodsID);
                if (
                    (this.requestReport.listMaterialGoods && this.requestReport.listMaterialGoods.length === 0) ||
                    !this.requestReport.listMaterialGoods
                ) {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.baoCao.theTinhGiaThanh.expenseItemsNotBeBlank'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    return;
                }
                if ((this.requestReport.listCostSets && this.requestReport.listCostSets.length === 0) || !this.requestReport.listCostSets) {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.baoCao.theTinhGiaThanh.costSetsNotBeBlank'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    return;
                }
            } else if (this.typeMethod === 3) {
                this.requestReport.listCostSets = this.listCostSets1.filter(n => n.checked).map(n => n.id);
                if ((this.requestReport.listCostSets && this.requestReport.listCostSets.length === 0) || !this.requestReport.listCostSets) {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.baoCao.theTinhGiaThanh.costSetsNotBeBlank'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    return;
                }
            } else if (this.typeMethod === 4) {
                this.requestReport.listCostSets = this.listCostSets0.filter(n => n.checked).map(n => n.id);
                if ((this.requestReport.listCostSets && this.requestReport.listCostSets.length === 0) || !this.requestReport.listCostSets) {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.baoCao.theTinhGiaThanh.costSetsNotBeBlank'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    return;
                }
            }
            this.baoCaoService.getReport(this.requestReport);
            this.requestReport.listCostSets = [];
            this.requestReport.listMaterialGoods = [];
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
        if (this.typeMethod === null || this.typeMethod === undefined || this.typeMethod === '') {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.theTinhGiaThanh.typeMethodNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
        }
        if (!this.cPPeriodID) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.theTinhGiaThanh.cPPeriodNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
        }
        // if (!this.toDate) {
        //     this.toastr.error(
        //         this.translate.instant('ebwebApp.baoCao.soQuyTienMatPopup.toDateNotBeBlank'),
        //         this.translate.instant('ebwebApp.mBDeposit.message')
        //     );
        //     return false;
        // }
        // if (this.fromDate > this.toDate) {
        //     this.toastr.error(
        //         this.translate.instant('ebwebApp.mBDeposit.fromDateMustBeLessThanToDate'),
        //         this.translate.instant('ebwebApp.mBDeposit.message')
        //     );
        //     return false;
        // }
        return true;
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
        if (this.typeMethod === 0 || this.typeMethod === 1 || this.typeMethod === 2) {
            if (this.listQuantums) {
                return this.listQuantums.every(item => item.checked) && this.listQuantums.length;
            } else {
                return false;
            }
        } else if (this.typeMethod === 3) {
            if (this.listCostSets1) {
                return this.listCostSets1.every(item => item.checked) && this.listCostSets1.length;
            } else {
                return false;
            }
        } else if (this.typeMethod === 4) {
            if (this.listCostSets0) {
                return this.listCostSets0.every(item => item.checked) && this.listCostSets0.length;
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
        if (this.typeMethod === 0 || this.typeMethod === 1 || this.typeMethod === 2) {
            const isCheck = this.listQuantums.every(item => item.checked) && this.listQuantums.length;
            this.listQuantums.forEach(item => (item.checked = !isCheck));
        } else if (this.typeMethod === 3) {
            const isCheck = this.listCostSets1.every(item => item.checked) && this.listCostSets1.length;
            this.listCostSets1.forEach(item => (item.checked = !isCheck));
        } else if (this.typeMethod === 4) {
            const isCheck = this.listCostSets0.every(item => item.checked) && this.listCostSets0.length;
            this.listCostSets0.forEach(item => (item.checked = !isCheck));
        } else if (this.typeMethod === 5) {
            const isCheck = this.eMContracts.every(item => item.checked) && this.eMContracts.length;
            this.eMContracts.forEach(item => (item.checked = !isCheck));
        }
    }

    check(accountList: any) {
        accountList.checked = !accountList.checked;
    }

    exportExcel() {
        if (this.checkError()) {
            this.setSessionData();
            this.requestReport.groupTheSameItem = this.groupTheSameItem;
            this.requestReport.typeReport = BaoCao.GiaThanh.THE_TINH_GIA_THANH;
            this.requestReport.fileName = BaoCao.GiaThanh.THE_TINH_GIA_THANH_XLS;
            this.requestReport.companyID = this.organizationUnit.parent.id;
            this.requestReport.dependent = this.isDependent;
            this.requestReport.cPPeriodID = this.cPPeriodID;
            this.requestReport.typeMethod = this.typeMethod;
            if (this.typeMethod === 0 || this.typeMethod === 1 || this.typeMethod === 2) {
                this.requestReport.listCostSets = this.listQuantums.filter(n => n.checked).map(n => n.costSetID);
                this.requestReport.listMaterialGoods = this.listQuantums.filter(n => n.checked).map(n => n.materialGoodsID);
                if (
                    (this.requestReport.listMaterialGoods && this.requestReport.listMaterialGoods.length === 0) ||
                    !this.requestReport.listMaterialGoods
                ) {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.baoCao.theTinhGiaThanh.expenseItemsNotBeBlank'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    return;
                }
                if ((this.requestReport.listCostSets && this.requestReport.listCostSets.length === 0) || !this.requestReport.listCostSets) {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.baoCao.theTinhGiaThanh.costSetsNotBeBlank'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    return;
                }
            } else if (this.typeMethod === 3) {
                this.requestReport.listCostSets = this.listCostSets1.filter(n => n.checked).map(n => n.id);
                if ((this.requestReport.listCostSets && this.requestReport.listCostSets.length === 0) || !this.requestReport.listCostSets) {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.baoCao.theTinhGiaThanh.costSetsNotBeBlank'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    return;
                }
            } else if (this.typeMethod === 4) {
                this.requestReport.listCostSets = this.listCostSets0.filter(n => n.checked).map(n => n.id);
                if ((this.requestReport.listCostSets && this.requestReport.listCostSets.length === 0) || !this.requestReport.listCostSets) {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.baoCao.theTinhGiaThanh.costSetsNotBeBlank'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    return;
                }
            }
            this.baoCaoService.exportExcel(this.requestReport).subscribe(
                res => {
                    const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                    const fileURL = URL.createObjectURL(blob);

                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    const name = BaoCao.GiaThanh.THE_TINH_GIA_THANH_XLS;
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
                this.isShowDependent = false;
            } else {
                this.isShowDependent = this.checkChildren(this.organizationUnit);
            }
        }
        this.cPPeriodService
            .getCPPeriodForReport({
                isDependent: this.isDependent ? this.isDependent : false,
                orgID: this.organizationUnit.parent.id
            })
            .subscribe((res: HttpResponse<ICPPeriod[]>) => {
                this.cPPeriodsData = res.body;
                this.cPPeriods = res.body.filter(a => a.type === this.typeMethod);
            });
        this.costSetMaterialGoods
            .getAllForReport({
                typeMethod: this.typeMethod,
                isDependent: this.isDependent ? this.isDependent : false,
                orgID: this.organizationUnit.parent.id
            })
            .subscribe((res: HttpResponse<ICostSetMaterialGood[]>) => {
                this.listQuantums = res.body;
            });
        this.eMContractService
            .getEMContractsForReport({
                isDependent: this.isDependent ? this.isDependent : false,
                orgID: this.organizationUnit.parent.id
            })
            .subscribe((res: HttpResponse<IEMContract[]>) => {
                this.eMContracts = res.body;
            });
        this.costSetService
            .getCostSetsByOrg({
                isDependent: this.isDependent ? this.isDependent : false,
                orgID: this.organizationUnit.parent.id
            })
            .subscribe((res: HttpResponse<ICostSet[]>) => {
                this.costSets = res.body;
                this.listCostSets0 = res.body.filter(a => a.costSetType === 0);
                this.listCostSets1 = res.body.filter(a => a.costSetType === 1);
                this.listCostSets0NotChange = res.body.filter(a => a.costSetType === 0);
                this.listCostSets1NotChange = res.body.filter(a => a.costSetType === 1);
            });
    }

    selectChangeIsDependent() {
        this.cPPeriodService
            .getCPPeriodForReport({
                isDependent: this.isDependent ? this.isDependent : false,
                orgID: this.organizationUnit.parent.id
            })
            .subscribe((res: HttpResponse<ICPPeriod[]>) => {
                this.cPPeriodsData = res.body;
                this.cPPeriods = res.body.filter(a => a.type === this.typeMethod);
            });
        this.costSetMaterialGoods
            .getAllForReport({
                typeMethod: this.typeMethod,
                isDependent: this.isDependent ? this.isDependent : false,
                orgID: this.organizationUnit.parent.id
            })
            .subscribe((res: HttpResponse<ICostSetMaterialGood[]>) => {
                this.listQuantums = res.body;
                this.listQuantumsNotChange = res.body;
            });
        this.eMContractService
            .getEMContractsForReport({
                isDependent: this.isDependent ? this.isDependent : false,
                orgID: this.organizationUnit.parent.id
            })
            .subscribe((res: HttpResponse<IEMContract[]>) => {
                this.eMContracts = res.body;
            });
        this.costSetService
            .getCostSetsByOrg({
                isDependent: this.isDependent ? this.isDependent : false,
                orgID: this.organizationUnit.parent.id
            })
            .subscribe((res: HttpResponse<ICostSet[]>) => {
                this.costSets = res.body;
                this.listCostSets0 = res.body.filter(a => a.costSetType === 0);
                this.listCostSets1 = res.body.filter(a => a.costSetType === 1);
                this.listCostSets0NotChange = res.body.filter(a => a.costSetType === 0);
                this.listCostSets1NotChange = res.body.filter(a => a.costSetType === 1);
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

    selectChangeTypeMethod() {
        if (this.typeMethod !== null && this.typeMethod !== undefined && this.cPPeriodsData) {
            this.cPPeriods = this.cPPeriodsData.filter(a => a.type === this.typeMethod);
            this.costSetMaterialGoods
                .getAllForReport({
                    typeMethod: this.typeMethod,
                    isDependent: this.isDependent ? this.isDependent : false,
                    orgID: this.organizationUnit.parent.id
                })
                .subscribe((res: HttpResponse<ICostSetMaterialGood[]>) => {
                    this.listQuantums = res.body;
                    this.listQuantumsNotChange = res.body;
                });
            this.cPPeriodID = null;
        }
    }

    changeSearchValueCSM() {
        this.listQuantums = this.listQuantumsNotChange.filter(
            n =>
                (this.searchQuantumCode
                    ? n.materialGoodsCode.toUpperCase().includes(this.searchQuantumCode.toUpperCase())
                    : n.materialGoodsCode.toUpperCase().includes('')) &&
                (this.searchQuantumName
                    ? n.materialGoodsName.toUpperCase().includes(this.searchQuantumName.toUpperCase())
                    : n.materialGoodsName.toUpperCase().includes('')) &&
                (this.searchCostSetCode
                    ? n.costSetCode.toUpperCase().includes(this.searchCostSetCode.toUpperCase())
                    : n.costSetCode.toUpperCase().includes(''))
        );
    }

    changeSearchValueListCostSet1() {
        this.listCostSets0 = this.listCostSets0NotChange.filter(
            n =>
                (this.searchCostSetCode1
                    ? n.costSetCode.toUpperCase().includes(this.searchCostSetCode1.toUpperCase())
                    : n.costSetCode.toUpperCase().includes('')) &&
                (this.searchCostSetName1
                    ? n.costSetName.toUpperCase().includes(this.searchCostSetName1.toUpperCase())
                    : n.costSetName.toUpperCase().includes(''))
        );
    }

    changeSearchValueListCostSet2() {
        this.listCostSets1 = this.listCostSets1NotChange.filter(
            n =>
                (this.searchCostSetCode2
                    ? n.costSetCode.toUpperCase().includes(this.searchCostSetCode2.toUpperCase())
                    : n.costSetCode.toUpperCase().includes('')) &&
                (this.searchCostSetName2
                    ? n.costSetName.toUpperCase().includes(this.searchCostSetName2.toUpperCase())
                    : n.costSetName.toUpperCase().includes(''))
        );
    }
}
