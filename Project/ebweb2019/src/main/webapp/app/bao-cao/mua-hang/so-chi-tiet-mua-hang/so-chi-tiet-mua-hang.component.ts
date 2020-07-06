import { Component, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import * as moment from 'moment';
import { Moment } from 'moment';
import { DATE_FORMAT, DATE_FORMAT_SEARCH } from 'app/shared';
import { BaoCao, MATERIAL_GOODS_TYPE } from 'app/app.constants';
import { RequestReport } from 'app/bao-cao/reqest-report.model';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { JhiEventManager } from 'ng-jhipster';
import { BaoCaoService } from 'app/bao-cao/bao-cao.service';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { AccountListService } from 'app/danhmuc/account-list';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';
import { IAccountList } from 'app/shared/model/account-list.model';
import { HttpResponse } from '@angular/common/http';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { IAccountingObjectGroup } from 'app/shared/model/accounting-object-group.model';
import { AccountingObjectGroupService } from 'app/danhmuc/accounting-object-group';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { MaterialGoodsCategoryService } from 'app/danhmuc/material-goods-category';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { ROLE } from 'app/role.constants';
import { TreeOrganizationUnit } from 'app/shared/model/organization-unit-tree.model';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';

@Component({
    selector: 'eb-so-chi-tiet-mua-hang',
    templateUrl: './so-chi-tiet-mua-hang.component.html',
    styleUrls: ['./so-chi-tiet-mua-hang.css', '../../bao-cao.component.css']
})
export class SoChiTietMuaHangComponent implements OnInit {
    status: boolean;
    requestReport: RequestReport;
    listTimeLine: any[];
    account: any;
    modalRef: NgbModalRef;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    treeOrganizationUnits: any[];
    accountLists: IAccountList[];
    accountingObjects: any[];
    accountingObjectFilter: any[];
    accountingObjectGroups: IAccountingObjectGroup[];
    accountItem: any;
    currencies: ICurrency[];
    employees: IAccountingObject[];
    materialGoodsCategories: IMaterialGoodsCategory[];
    ROLE_KetXuat = ROLE.SoChiTietMuaHang_KetXuat;

    materialGoods: IMaterialGoods[];
    materialGoodsFilter: IMaterialGoods[];
    BaoCao = BaoCao;
    reportType: string;
    sessionSearch: {
        type: string;
        data: {
            dependent: boolean;
            isSimilarSum: boolean;
            showAccumAmount: boolean;
            groupTheSameItem: boolean;
            timeLineVoucher: any;
            fromDate: string;
            toDate: string;
            organizationUnit: TreeOrganizationUnit;
            currencyCode: any;
            accountingObjectGroupID: any;
            employee: IAccountingObject;
            materialGoodsCategoryID: string;
            customerAddress: string;
            customerCode: string;
            customerName: string;
            mgCodeFilter: string;
            mgNameFilter: string;
            materialGoodsSelected: string[];
            accountingObjectsSelected: string[];
            hiddenDependent: boolean;
        };
    };
    isShowDependent: boolean;
    constructor(
        public activeModal: NgbActiveModal,
        private principal: Principal,
        private toastr: ToastrService,
        public translate: TranslateService,
        public eventManager: JhiEventManager,
        private baoCaoService: BaoCaoService,
        public utilsService: UtilsService,
        public currencyService: CurrencyService,
        public accountingObjectService: AccountingObjectService,
        public accountingObjectGroupService: AccountingObjectGroupService,
        private organizationUnitService: OrganizationUnitService,
        private accountListService: AccountListService,
        private materialGoodsCategoryService: MaterialGoodsCategoryService,
        private materialGoodsService: MaterialGoodsService
    ) {}

    ngOnInit() {
        this.sessionSearch = this.baoCaoService.getSessionData(this.reportType);

        if (this.sessionSearch.data.isSimilarSum === undefined || this.sessionSearch.data.isSimilarSum === null) {
            this.sessionSearch.data.isSimilarSum = true;
        }
        if (this.sessionSearch.data.groupTheSameItem === undefined || this.sessionSearch.data.groupTheSameItem === null) {
            this.sessionSearch.data.groupTheSameItem = true;
        }
        if (this.sessionSearch.data.showAccumAmount === undefined || this.sessionSearch.data.showAccumAmount === null) {
            this.sessionSearch.data.showAccumAmount = true;
        }

        this.principal.identity().then(account => {
            this.account = account;
            this.treeOrganizationUnits = [];
            this.sessionSearch.data.currencyCode = this.sessionSearch.data.currencyCode || this.account.organizationUnit.currencyID;
            this.listTimeLine = this.utilsService.getCbbTimeLine();
            if (this.sessionSearch.data.timeLineVoucher === null || this.sessionSearch.data.timeLineVoucher === undefined) {
                this.sessionSearch.data.timeLineVoucher = this.listTimeLine[4].value;
            }
            this.selectChangeBeginDateAndEndDate(this.sessionSearch.data.timeLineVoucher);
            this.organizationUnitService.getChildCompanyByID().subscribe((res: HttpResponse<any>) => {
                this.treeOrganizationUnits = res.body.orgTrees;

                // if (this.treeOrganizationUnits && this.treeOrganizationUnits.length > 0) {
                //     this.sessionSearch.data.organizationUnit =
                //         this.treeOrganizationUnits.find(
                //             x => this.sessionSearch.data.organizationUnit && x.value === this.sessionSearch.data.organizationUnit.parent.id
                //         ) || this.treeOrganizationUnits.find(x => (x.value = res.body.currentOrg.value));
                // } else {
                this.sessionSearch.data.organizationUnit = res.body.currentOrgLogin;
                // }
                this.baoCaoService.putSessionData(this.sessionSearch);
                this.sessionSearch = this.baoCaoService.getSessionData(this.reportType);
                this.selectChangeOrg();
            });
            this.currencyService.findAllActive().subscribe((req: HttpResponse<ICurrency[]>) => {
                this.currencies = req.body;
            });
            this.accountListService.getAccountDetailType().subscribe((res: HttpResponse<IAccountList[]>) => {
                this.accountLists = res.body;
            });
            if (this.reportType === BaoCao.MuaHang.SO_CHI_TIET_MUA_HANG) {
                this.getEmployees();
            }
        });
    }

    reloadData() {
        this.sessionSearch.data.employee = null;
        this.sessionSearch.data.materialGoodsCategoryID = null;
        this.sessionSearch.data.accountingObjectGroupID = null;
        this.getAccountingObjectGroup();
        this.getMaterialGoodsCategory();
        if (this.reportType === BaoCao.MuaHang.SO_CHI_TIET_MUA_HANG) {
            this.getEmployees();
        }
    }

    getAccountingObjectGroup() {
        this.accountingObjectGroupService
            .getAllAccountingObjectGroupSimilarBranch({
                similarBranch: this.sessionSearch.data.dependent ? this.sessionSearch.data.dependent : '',
                companyID: this.sessionSearch.data.organizationUnit ? this.sessionSearch.data.organizationUnit.parent.id : ''
            })
            .subscribe((res: HttpResponse<IAccountingObjectGroup[]>) => {
                this.accountingObjectGroups = res.body
                    .filter(group => group.isActive === true)
                    .sort((a, b) => a.accountingObjectGroupCode.localeCompare(b.accountingObjectGroupCode));
                this.getAccountingObjectByGroupID();
            });
        // this.accountingObjectGroupService.getAllAccountingObjectGroup().subscribe((res: HttpResponse<IAccountingObjectGroup[]>) => {
        //     this.accountingObjectGroups = res.body
        //         .filter(group => group.isActive === true)
        //         .sort((a, b) => a.accountingObjectGroupCode.localeCompare(b.accountingObjectGroupCode));
        //     this.getAccountingObjectByGroupID();
        // });
    }

    accept() {
        if (this.isNotChecked() || this.checkErr()) {
            return;
        }
        if (!this.requestReport) {
            this.requestReport = new RequestReport();
        }
        this.requestReport.toDate = moment(this.sessionSearch.data.toDate, DATE_FORMAT);
        this.requestReport.fromDate = moment(this.sessionSearch.data.fromDate, DATE_FORMAT);
        this.requestReport.companyID = this.sessionSearch.data.organizationUnit ? this.sessionSearch.data.organizationUnit.parent.id : null;
        this.requestReport.typeReport = this.reportType;
        this.requestReport.dependent = this.sessionSearch.data.dependent;
        this.requestReport.accountingObjects = this.accountingObjects.filter(n => n.checked).map(n => n.id);
        this.requestReport.listMaterialGoods = this.materialGoods.filter(x => x.checked).map(c => c.id);
        if (this.reportType === BaoCao.MuaHang.SO_CHI_TIET_MUA_HANG) {
            this.requestReport.employeeID = this.sessionSearch.data.employee ? this.sessionSearch.data.employee.id : null;
        }

        this.baoCaoService.getReport(this.requestReport);
    }

    checkErr() {
        if (!this.sessionSearch.data.organizationUnit) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.error.nullOrg'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return true;
        }
        if (!this.sessionSearch.data.fromDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.nullFromDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return true;
        }
        if (!this.sessionSearch.data.toDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.nullToDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return true;
        }
        if (this.sessionSearch.data.toDate && this.sessionSearch.data.fromDate) {
            if (moment(this.sessionSearch.data.toDate, DATE_FORMAT_SEARCH) < moment(this.sessionSearch.data.fromDate, DATE_FORMAT_SEARCH)) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mCReceipt.error.fromDateGreaterToDate'),
                    this.translate.instant('ebwebApp.mCReceipt.error.error')
                );
                return true;
            }
        }
        if (this.materialGoods.filter(x => x.checked).length === 0) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.error.nullMaterialGoods'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return true;
        }
        return false;
    }

    checkEmpty() {
        if (!this.accountItem) {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountList.accountNumberIsNotNull'),
                this.translate.instant('ebwebApp.accountList.error')
            );
            return true;
        }
        return false;
    }

    selectChangeBeginDateAndEndDate(intTimeLine: String) {
        if (intTimeLine) {
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
            this.sessionSearch.data.fromDate = this.objTimeLine.dtBeginDate;
            this.sessionSearch.data.toDate = this.objTimeLine.dtEndDate;
            this.changeSessionSearch();
        }
    }

    isNotChecked() {
        const isNotChecked = this.accountingObjects && this.accountingObjects.every(item => !item.checked);
        if (isNotChecked) {
            if (this.reportType === BaoCao.MuaHang.SO_CHI_TIET_MUA_HANG) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.accountingObject.notCheckNCC'),
                    this.translate.instant('ebwebApp.accountingObject.error')
                );
            } else {
                this.toastr.error(
                    this.translate.instant('ebwebApp.accountingObject.notCheckCustomer'),
                    this.translate.instant('ebwebApp.accountingObject.error')
                );
            }

            return true;
        }
        return false;
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
        if (this.accountingObjects) {
            return this.accountingObjects.every(item => item.checked) && this.accountingObjects.length;
        } else {
            return false;
        }
    }

    checkAll() {
        const isCheck = this.accountingObjects.every(item => item.checked) && this.accountingObjects.length;
        this.accountingObjects.forEach(item => {
            item.checked = !isCheck;
            this.saveAccountingObjectsSearch(item);
        });
    }

    check(accountingObjectItem: any) {
        accountingObjectItem.checked = !accountingObjectItem.checked;
        this.saveAccountingObjectsSearch(accountingObjectItem);
    }
    saveAccountingObjectsSearch(item) {
        if (item.checked) {
            if (!this.sessionSearch.data.accountingObjectsSelected) {
                this.sessionSearch.data.accountingObjectsSelected = [item.id];
            } else {
                this.sessionSearch.data.accountingObjectsSelected.push(item.id);
            }
        } else {
            this.sessionSearch.data.accountingObjectsSelected = this.sessionSearch.data.accountingObjectsSelected.filter(
                x => x !== item.id
            );
        }
        this.changeSessionSearch();
    }

    selectChangeAccountingObjectGroup() {
        this.sessionSearch.data.accountingObjectsSelected = [];
        this.getAccountingObjectByGroupID();
    }

    getAccountingObjectByGroupID() {
        if (this.reportType === BaoCao.MuaHang.SO_CHI_TIET_MUA_HANG) {
            this.accountingObjectService
                .getAccountingObjectByGroupIDSimilarBranch({
                    id: this.sessionSearch.data.accountingObjectGroupID ? this.sessionSearch.data.accountingObjectGroupID : '',
                    similarBranch: this.sessionSearch.data.dependent ? this.sessionSearch.data.dependent : '',
                    companyID: this.sessionSearch.data.organizationUnit ? this.sessionSearch.data.organizationUnit.parent.id : ''
                })
                .subscribe((res: HttpResponse<any[]>) => {
                    for (const item of res.body) {
                        item.accountingObjectAddress = item.accountingObjectAddress ? item.accountingObjectAddress : '';
                        item.accountingObjectName = item.accountingObjectName ? item.accountingObjectName : '';
                        item.accountingObjectCode = item.accountingObjectCode ? item.accountingObjectCode : '';
                    }
                    this.accountingObjects = res.body;
                    this.accountingObjectFilter = this.accountingObjects;
                    this.accountingObjectFilter.forEach(item => {
                        if (
                            this.sessionSearch.data.accountingObjectsSelected &&
                            this.sessionSearch.data.accountingObjectsSelected.includes(item.id)
                        ) {
                            item.checked = true;
                        }
                    });
                    this.changeSupplierACFilter();
                });
        } else {
            this.accountingObjectService
                .getAccountingObjectByGroupIDKHSimilarBranch({
                    id: this.sessionSearch.data.accountingObjectGroupID ? this.sessionSearch.data.accountingObjectGroupID : '',
                    similarBranch: this.sessionSearch.data.dependent ? this.sessionSearch.data.dependent : '',
                    companyID: this.sessionSearch.data.organizationUnit ? this.sessionSearch.data.organizationUnit.parent.id : ''
                })
                .subscribe((res: HttpResponse<any[]>) => {
                    for (const item of res.body) {
                        item.accountingObjectAddress = item.accountingObjectAddress ? item.accountingObjectAddress : '';
                        item.accountingObjectName = item.accountingObjectName ? item.accountingObjectName : '';
                        item.accountingObjectCode = item.accountingObjectCode ? item.accountingObjectCode : '';
                    }
                    this.accountingObjects = res.body;
                    this.accountingObjectFilter = this.accountingObjects;
                    this.accountingObjectFilter.forEach(item => {
                        if (
                            this.sessionSearch.data.accountingObjectsSelected &&
                            this.sessionSearch.data.accountingObjectsSelected.includes(item.id)
                        ) {
                            item.checked = true;
                        }
                    });
                    this.changeSupplierACFilter();
                });
        }

        this.changeSessionSearch();
    }

    // nhân viên thực hiện
    getEmployees() {
        this.accountingObjectService
            .getAccountingObjectEmployeeSimilarBranch({
                similarBranch: this.sessionSearch.data.dependent ? this.sessionSearch.data.dependent : '',
                companyID: this.sessionSearch.data.organizationUnit ? this.sessionSearch.data.organizationUnit.parent.id : ''
            })
            .subscribe((res: HttpResponse<IAccountingObject[]>) => {
                this.employees = res.body;
            });
    }

    getMaterialGoodsCategory() {
        this.materialGoodsCategoryService
            .getMaterialGoodsCategorySimilarBranch({
                similarBranch: this.sessionSearch.data.dependent ? this.sessionSearch.data.dependent : '',
                companyID: this.sessionSearch.data.organizationUnit ? this.sessionSearch.data.organizationUnit.parent.id : ''
            })
            .subscribe((res: HttpResponse<IMaterialGoodsCategory[]>) => {
                this.materialGoodsCategories = res.body;
                this.changMaterialGoodsCategory();
            });
    }
    onChangMaterialGoodsCategory() {
        this.sessionSearch.data.materialGoodsSelected = [];
        this.changMaterialGoodsCategory();
    }
    changMaterialGoodsCategory() {
        if (this.sessionSearch.data.materialGoodsCategoryID) {
            this.materialGoodsService
                .getAllMaterialGoodsDTOSimilarBranch({
                    similarBranch: this.sessionSearch.data.dependent ? this.sessionSearch.data.dependent : false,
                    companyID: this.sessionSearch.data.organizationUnit ? this.sessionSearch.data.organizationUnit.parent.id : ''
                })
                .subscribe((res: HttpResponse<IMaterialGoods[]>) => {
                    this.materialGoods = res.body
                        .filter(
                            x =>
                                x.materialGoodsCategoryID === this.sessionSearch.data.materialGoodsCategoryID &&
                                x.materialGoodsType !== MATERIAL_GOODS_TYPE.DIFF &&
                                x.materialGoodsType !== MATERIAL_GOODS_TYPE.SERVICE
                        )
                        .sort((a, b) => a.materialGoodsCode.localeCompare(b.materialGoodsCode));
                    for (const item of this.materialGoods) {
                        item.materialGoodsCode = item.materialGoodsCode ? item.materialGoodsCode : '';
                        item.materialGoodsName = item.materialGoodsName ? item.materialGoodsName : '';
                    }
                    this.changeMGFilter();
                });
        } else {
            this.materialGoodsService
                .getAllMaterialGoodsDTOSimilarBranch({
                    similarBranch: this.sessionSearch.data.dependent ? this.sessionSearch.data.dependent : false,
                    companyID: this.sessionSearch.data.organizationUnit ? this.sessionSearch.data.organizationUnit.parent.id : ''
                })
                .subscribe((res: HttpResponse<IMaterialGoods[]>) => {
                    this.materialGoods = res.body.sort((a, b) => a.materialGoodsCode.localeCompare(b.materialGoodsCode));
                    for (const item of this.materialGoods) {
                        item.materialGoodsCode = item.materialGoodsCode ? item.materialGoodsCode : '';
                        item.materialGoodsName = item.materialGoodsName ? item.materialGoodsName : '';
                    }
                    this.changeMGFilter();
                });
        }
        this.changeSessionSearch();
    }

    isCheckAllMateria() {
        if (this.materialGoods) {
            return this.materialGoods.every(item => item.checked) && this.materialGoods.length;
        }
        return false;
    }

    checkAllMateria() {
        const isCheck = this.materialGoods.every(item => item.checked) && this.materialGoods.length;
        this.materialGoods.forEach(item => {
            item.checked = !isCheck;
            this.saveMaterialGoodsSearch(item);
        });
    }

    checkMateria(materialGood: IMaterialGoods) {
        materialGood.checked = !materialGood.checked;
        this.saveMaterialGoodsSearch(materialGood);
    }

    saveMaterialGoodsSearch(item) {
        if (item.checked) {
            if (!this.sessionSearch.data.materialGoodsSelected) {
                this.sessionSearch.data.materialGoodsSelected = [item.id];
            } else {
                this.sessionSearch.data.materialGoodsSelected.push(item.id);
            }
        } else {
            this.sessionSearch.data.materialGoodsSelected = this.sessionSearch.data.materialGoodsSelected.filter(x => x !== item.id);
        }
        this.changeSessionSearch();
    }

    changeMGFilter() {
        if (this.sessionSearch.data.mgNameFilter && this.sessionSearch.data.mgCodeFilter) {
            this.materialGoodsFilter = this.materialGoods.filter(
                x =>
                    x.materialGoodsCode.toLowerCase().includes(this.sessionSearch.data.mgCodeFilter.toLowerCase()) &&
                    x.materialGoodsName.toLowerCase().includes(this.sessionSearch.data.mgNameFilter.toLowerCase())
            );
        } else if (this.sessionSearch.data.mgCodeFilter) {
            this.materialGoodsFilter = this.materialGoods.filter(x =>
                x.materialGoodsCode.toLowerCase().includes(this.sessionSearch.data.mgCodeFilter.toLowerCase())
            );
        } else if (this.sessionSearch.data.mgNameFilter) {
            this.materialGoodsFilter = this.materialGoods.filter(x =>
                x.materialGoodsName.toLowerCase().includes(this.sessionSearch.data.mgNameFilter.toLowerCase())
            );
        } else {
            this.materialGoodsFilter = this.materialGoods;
        }
        this.materialGoodsFilter.forEach(item => {
            if (this.sessionSearch.data.materialGoodsSelected && this.sessionSearch.data.materialGoodsSelected.includes(item.id)) {
                item.checked = true;
            }
        });
        this.changeSessionSearch();
    }

    changeSupplierACFilter() {
        if (this.sessionSearch.data.customerCode && this.sessionSearch.data.customerName && this.sessionSearch.data.customerAddress) {
            this.accountingObjectFilter = this.accountingObjects.filter(
                x =>
                    x.accountingObjectCode.toLowerCase().includes(this.sessionSearch.data.customerCode.toLowerCase()) &&
                    x.accountingObjectName.toLowerCase().includes(this.sessionSearch.data.customerName.toLowerCase()) &&
                    x.accountingObjectAddress.toLowerCase().includes(this.sessionSearch.data.customerAddress.toLowerCase())
            );
        } else if (this.sessionSearch.data.customerName && this.sessionSearch.data.customerCode) {
            this.accountingObjectFilter = this.accountingObjects.filter(
                x =>
                    x.accountingObjectCode.toLowerCase().includes(this.sessionSearch.data.customerCode.toLowerCase()) &&
                    x.accountingObjectName.toLowerCase().includes(this.sessionSearch.data.customerName.toLowerCase())
            );
        } else if (this.sessionSearch.data.customerCode && this.sessionSearch.data.customerAddress) {
            this.accountingObjectFilter = this.accountingObjects.filter(
                x =>
                    x.accountingObjectCode.toLowerCase().includes(this.sessionSearch.data.customerCode.toLowerCase()) &&
                    x.accountingObjectAddress.toLowerCase().includes(this.sessionSearch.data.customerAddress.toLowerCase())
            );
        } else if (this.sessionSearch.data.customerName && this.sessionSearch.data.customerAddress) {
            this.accountingObjectFilter = this.accountingObjects.filter(
                x =>
                    x.accountingObjectCode.toLowerCase().includes(this.sessionSearch.data.customerCode.toLowerCase()) &&
                    x.accountingObjectAddress.toLowerCase().includes(this.sessionSearch.data.customerAddress.toLowerCase())
            );
        } else if (this.sessionSearch.data.customerName) {
            this.accountingObjectFilter = this.accountingObjects.filter(x =>
                x.accountingObjectName.toLowerCase().includes(this.sessionSearch.data.customerName.toLowerCase())
            );
        } else if (this.sessionSearch.data.customerCode) {
            this.accountingObjectFilter = this.accountingObjects.filter(x =>
                x.accountingObjectCode.toLowerCase().includes(this.sessionSearch.data.customerCode.toLowerCase())
            );
        } else if (this.sessionSearch.data.customerAddress) {
            this.accountingObjectFilter = this.accountingObjects.filter(x =>
                x.accountingObjectAddress.toLowerCase().includes(this.sessionSearch.data.customerAddress.toLowerCase())
            );
        } else {
            this.accountingObjectFilter = this.accountingObjects;
        }
        this.changeSessionSearch();
    }

    changeSessionSearch() {
        this.baoCaoService.putSessionData(this.sessionSearch);
        this.sessionSearch = this.baoCaoService.getSessionData(this.reportType);
    }

    export() {
        if (this.isNotChecked() || this.checkErr()) {
            return;
        }
        if (!this.requestReport) {
            this.requestReport = new RequestReport();
        }
        this.requestReport.toDate = moment(this.sessionSearch.data.toDate, DATE_FORMAT_SEARCH);
        this.requestReport.fromDate = moment(this.sessionSearch.data.fromDate, DATE_FORMAT_SEARCH);
        this.requestReport.companyID = this.sessionSearch.data.organizationUnit ? this.sessionSearch.data.organizationUnit.parent.id : null;
        this.requestReport.typeReport = this.reportType;
        if (this.reportType === BaoCao.MuaHang.SO_CHI_TIET_MUA_HANG) {
            this.requestReport.fileName = BaoCao.MuaHang.SO_CHI_TIET_MUA_HANG_XLS;
        } else if (this.reportType === BaoCao.BanHang.SO_CHI_TIET_BAN_HANG) {
            this.requestReport.fileName = BaoCao.BanHang.SO_CHI_TIET_BAN_HANG_XLS;
        }
        this.requestReport.dependent = this.sessionSearch.data.dependent;
        this.requestReport.accountingObjects = this.accountingObjects.filter(n => n.checked).map(n => n.id);
        this.requestReport.listMaterialGoods = this.materialGoods.filter(x => x.checked).map(c => c.id);
        if (this.reportType === BaoCao.MuaHang.SO_CHI_TIET_MUA_HANG) {
            this.requestReport.employeeID = this.sessionSearch.data.employee ? this.sessionSearch.data.employee.id : null;
        }
        this.baoCaoService.exportExcel(this.requestReport).subscribe(
            res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);

                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                let fileExcel;
                if (this.reportType === BaoCao.MuaHang.SO_CHI_TIET_MUA_HANG) {
                    fileExcel = BaoCao.MuaHang.SO_CHI_TIET_MUA_HANG_XLS;
                } else if (this.reportType === BaoCao.BanHang.SO_CHI_TIET_BAN_HANG) {
                    fileExcel = BaoCao.BanHang.SO_CHI_TIET_BAN_HANG_XLS;
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

    changeDependent() {
        this.changeSessionSearch();
        this.reloadData();
    }

    changeOrganizationUnit() {
        this.sessionSearch.data.dependent = false;
        this.changeSessionSearch();
        this.selectChangeOrg();
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
            this.reloadData();
        }
    }
}
