import { Component, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import * as moment from 'moment';
import { Moment } from 'moment';
import { DATE_FORMAT, DATE_FORMAT_SEARCH } from 'app/shared';
import { BaoCao } from 'app/app.constants';
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
import { SystemOption } from 'app/shared/model/system-option.model';
import { DataSessionStorage, IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { ROLE } from 'app/role.constants';
import { TreeOrganizationUnit } from 'app/shared/model/organization-unit-tree.model';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';

@Component({
    selector: 'eb-cong-no-phai-tra',
    templateUrl: './cong-no-phai-tra.component.html',
    styleUrls: ['./cong-no-phai-tra.component.css']
})
export class CongNoPhaiTraComponent implements OnInit {
    isSimilarSum: boolean;
    createDate: Moment;
    status: boolean;
    fromDate: Moment;
    toDate: Moment;
    requestReport: RequestReport;
    requestReportSearch: RequestReport;
    listTimeLine: any[];
    account: any;
    modalRef: NgbModalRef;
    showAccumAmount: boolean;
    groupTheSameItem: boolean;
    timeLineVoucher: any;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    organizationUnit: any;
    treeOrganizationUnits: any[];
    accountLists: IAccountList[];
    accountingObjects: any[];
    accountingObjectGroups: IAccountingObjectGroup[];
    accountItem: any;
    currencies: ICurrency[];
    currencyCode: any;
    accountingObjectGroupID: any;
    timeLineVoucherStatus: any;
    ROLE_KetXuat = ROLE.TongHopCongNoPhaiTra_KetXuat;
    ROLE_KetXuatChiTiet = ROLE.ChiTietCongNoPhaiThu_KetXuat;
    ROLE_Xem = ROLE.TongHopCongNoPhaiTra_Xem;
    ROLE_XemChiTiet = ROLE.ChiTietCongNoPhaiTra_Xem;
    dependent: boolean;
    isShowDependent: boolean;
    acCodeFilter: any;
    acNameFilter: any;
    accountingObjectsSearch: any[];
    acAddressFilter: any;
    isKetXuat: boolean;
    getAmountOriginal: boolean;
    currencyCodeDefault: any;
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
        private accountListService: AccountListService
    ) {}

    ngOnInit() {
        this.getAmountOriginal = false;
        this.isSimilarSum = true;
        this.groupTheSameItem = true;
        this.showAccumAmount = true;
        this.principal.identity().then(account => {
            this.account = account;
            this.currencyCode = this.account.organizationUnit.currencyID;
            this.isKetXuat = !this.utilsService.isPackageDemo(this.account);
            this.treeOrganizationUnits = [];
            this.currencyCode = this.account.organizationUnit.currencyID;
            this.currencyCodeDefault = this.account.organizationUnit.currencyID;
            this.listTimeLine = this.utilsService.getCbbTimeLine();
            this.getSessionData();
            // this.accountingObjectService.getAccountingObjectActive().subscribe((res: HttpResponse<any[]>) => {
            //     this.accountingObjects = res.body;
            //     this.accountingObjectsSearch = res.body;
            // });
            // this.accountingObjectService.getAccountingObjectActiveByReportRequest(this.requestReport).subscribe((res: HttpResponse<any[]>) => {
            //     this.accountingObjects = res.body;
            // });
            this.organizationUnitService.getChildCompanyByID().subscribe((res: HttpResponse<any>) => {
                this.treeOrganizationUnits = res.body.orgTrees;
                this.baoCaoService.checkHiddenDependent(this.treeOrganizationUnits);
                this.organizationUnit = res.body.currentOrgLogin;
                this.selectChangeOrg();
            });
            this.currencyService.findAllActive().subscribe((req: HttpResponse<ICurrency[]>) => {
                this.currencies = req.body;
            });
            this.accountListService.getAccountDetailType().subscribe((res: HttpResponse<IAccountList[]>) => {
                this.accountLists = res.body;
                if (res.body && res.body.length > 0) {
                    this.accountItem = this.accountLists.some(x => x.accountNumber === '331') ? '331' : this.accountLists[0].accountNumber;
                }
            });
        });
    }

    accept() {
        if (this.checkEmpty() || this.isNotChecked() || !this.checkErr()) {
            return;
        }
        if (!this.requestReport) {
            this.requestReport = new RequestReport();
        }
        this.requestReport.toDate = moment(this.toDate, DATE_FORMAT_SEARCH);
        this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT_SEARCH);
        this.requestReport.accountNumber = this.accountItem;
        this.requestReport.typeReport = this.status ? BaoCao.MuaHang.CHI_TIET_CONG_NO_PHAI_TRA : BaoCao.MuaHang.TONG_HOP_CONG_NO_PHAI_TRA;
        this.requestReport.listMaterialGoods = this.accountingObjects.filter(n => n.checked).map(n => n.id);
        this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
        this.requestReport.timeLineVoucher = this.timeLineVoucher
            ? this.listTimeLine.find(x => x.value === this.timeLineVoucher).display
            : '';
        this.requestReport.currencyID = this.currencyCode;
        this.requestReport.getAmountOriginal = this.getAmountOriginal;
        this.requestReport.dependent = this.dependent;
        this.requestReport.groupTheSameItem = this.isSimilarSum;
        this.setSessionData();
        this.baoCaoService.getReport(this.requestReport);
    }

    checkErr() {
        if (!this.organizationUnit) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.organizationUnitNotBeBlank'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
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
            this.fromDate = moment(this.objTimeLine.dtBeginDate);
            this.toDate = moment(this.objTimeLine.dtEndDate);
            this.createDate = this.fromDate;
        }
    }

    isNotChecked() {
        const isNotChecked = this.accountingObjects && this.accountingObjects.every(item => !item.checked);
        if (isNotChecked) {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountingObject.notCheckNCC'),
                this.translate.instant('ebwebApp.accountingObject.error')
            );
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
        this.accountingObjects.forEach(item => (item.checked = !isCheck));
    }

    check(accountingObjectItem: any) {
        accountingObjectItem.checked = !accountingObjectItem.checked;
    }

    selectChangeDependent() {
        this.accountingObjectService
            .getAccountingObjectByGroupID({
                id: this.accountingObjectGroupID ? this.accountingObjectGroupID : '',
                similarBranch: this.dependent ? this.dependent : '',
                companyID: this.organizationUnit ? this.organizationUnit.value : ''
            })
            .subscribe((res: HttpResponse<any[]>) => {
                this.accountingObjects = res.body;
                this.accountingObjectsSearch = res.body;
            });
        this.accountingObjectGroupService
            .getAllAccountingObjectGroup({
                similarBranch: this.dependent ? this.dependent : '',
                companyID: this.organizationUnit ? this.organizationUnit.value : ''
            })
            .subscribe((res: HttpResponse<IAccountingObjectGroup[]>) => {
                this.accountingObjectGroups = res.body;
            });
        this.selectChangeOrg();
    }

    getSessionData() {
        this.requestReportSearch = new RequestReport();
        if (this.status) {
            this.requestReportSearch = JSON.parse(sessionStorage.getItem('searchChiTietCongNoPhaiTra'))
                ? JSON.parse(sessionStorage.getItem('searchChiTietCongNoPhaiTra'))
                : '';
        } else {
            this.requestReportSearch = JSON.parse(sessionStorage.getItem('searchTongHopCongNoPhaiTra'))
                ? JSON.parse(sessionStorage.getItem('searchTongHopCongNoPhaiTra'))
                : '';
        }
        if (this.requestReportSearch) {
            this.timeLineVoucher = this.requestReportSearch.timeLineVoucher;
            this.fromDate = moment(this.requestReportSearch.fromDate);
            this.toDate = moment(this.requestReportSearch.toDate);
            this.accountItem = this.requestReportSearch.accountNumber;
            this.currencyCode = this.requestReportSearch.currencyID;
            this.getAmountOriginal = this.requestReportSearch.getAmountOriginal;
            this.dependent = this.requestReportSearch.dependent;
        } else {
            if (this.status) {
                sessionStorage.removeItem('searchChiTietCongNoPhaiTra');
            } else {
                sessionStorage.removeItem('searchTongHopCongNoPhaiTra');
            }
            this.timeLineVoucher = this.listTimeLine[4].value;
            this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
        }
    }

    setSessionData() {
        if (this.status) {
            sessionStorage.removeItem('searchChiTietCongNoPhaiTra');
        } else {
            sessionStorage.removeItem('searchTongHopCongNoPhaiTra');
        }
        // this.dataSession.createDate  = this.createDate ? moment(this.createDate, DATE_FORMAT_SEARCH).format(DATE_FORMAT) : '';
        // this.timeLineVoucherStatus = this.timeLineVoucher ? this.timeLineVoucher : '';
        this.requestReportSearch = new RequestReport();
        this.requestReportSearch.toDate = moment(this.toDate, DATE_FORMAT_SEARCH);
        this.requestReportSearch.fromDate = moment(this.fromDate, DATE_FORMAT_SEARCH);
        this.requestReportSearch.timeLineVoucher = this.timeLineVoucher;
        this.requestReportSearch.accountNumber = this.accountItem;
        this.requestReportSearch.currencyID = this.currencyCode;
        this.requestReportSearch.companyID = this.account.organizationUnit.id;
        this.requestReportSearch.getAmountOriginal = this.getAmountOriginal;
        this.requestReportSearch.dependent = this.dependent;

        if (this.status) {
            this.requestReportSearch.groupTheSameItem = this.isSimilarSum;
        }
        if (this.status) {
            sessionStorage.setItem('searchChiTietCongNoPhaiTra', JSON.stringify(this.requestReportSearch ? this.requestReportSearch : ''));
        } else {
            sessionStorage.setItem('searchTongHopCongNoPhaiTra', JSON.stringify(this.requestReportSearch ? this.requestReportSearch : ''));
        }
    }

    exportExcel() {
        if (this.checkErr()) {
            this.setSessionData();
            this.requestReport = {};
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT_SEARCH);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT_SEARCH);
            this.requestReport.accountNumber = this.accountItem;
            this.requestReport.typeReport = this.status
                ? BaoCao.MuaHang.CHI_TIET_CONG_NO_PHAI_TRA
                : BaoCao.MuaHang.TONG_HOP_CONG_NO_PHAI_TRA;
            this.requestReport.listMaterialGoods = this.accountingObjects.filter(n => n.checked).map(n => n.id);
            this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
            this.requestReport.timeLineVoucher = this.timeLineVoucher
                ? this.listTimeLine.find(x => x.value === this.timeLineVoucher).display
                : '';
            this.requestReport.currencyID = this.currencyCode;
            this.requestReport.dependent = this.dependent;
            this.requestReport.groupTheSameItem = this.isSimilarSum;
            this.requestReport.getAmountOriginal = this.getAmountOriginal;
            this.requestReport.typeReport = this.status
                ? BaoCao.MuaHang.CHI_TIET_CONG_NO_PHAI_TRA
                : BaoCao.MuaHang.TONG_HOP_CONG_NO_PHAI_TRA;
            this.requestReport.fileName = this.status
                ? BaoCao.MuaHang.CHI_TIET_CONG_NO_PHAI_TRA_XLS
                : BaoCao.MuaHang.TONG_HOP_CONG_NO_PHAI_TRA_XLS;
            // if (this.typeName === BaoCao.BaoCaoTaiChinh.KET_QUA_HOAT_DONG_KINH_DOANH) {
            //     this.requestReport.typeReport = BaoCao.BaoCaoTaiChinh.KET_QUA_HOAT_DONG_KINH_DOANH_XLS;
            // } else if (this.typeName === BaoCao.BaoCaoTaiChinh.LUU_CHUYEN_TIEN_TE_TT) {
            //     this.requestReport.typeReport = BaoCao.BaoCaoTaiChinh.LUU_CHUYEN_TIEN_TE_TT_XLS;
            //     this.requestReport.option = true;
            // } else if (this.typeName === BaoCao.BaoCaoTaiChinh.LUU_CHUYEN_TIEN_TE_GT) {
            //     this.requestReport.typeReport = BaoCao.BaoCaoTaiChinh.LUU_CHUYEN_TIEN_TE_GT_XLS;
            //     this.requestReport.option = false;
            // } else if (this.typeName === BaoCao.BaoCaoTaiChinh.THUYET_MINH_BAO_CAO_TAI_CHINH) {
            //     this.requestReport.typeReport = BaoCao.BaoCaoTaiChinh.THUYET_MINH_BAO_CAO_TAI_CHINH_XLS;
            // }
            this.baoCaoService.exportExcel(this.requestReport).subscribe(
                res => {
                    const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                    const fileURL = URL.createObjectURL(blob);

                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    const name = this.status ? BaoCao.MuaHang.CHI_TIET_CONG_NO_PHAI_TRA_XLS : BaoCao.MuaHang.TONG_HOP_CONG_NO_PHAI_TRA_XLS;
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

    changeMGFilter() {
        if (this.acNameFilter && this.acCodeFilter && this.acAddressFilter) {
            this.accountingObjects = this.accountingObjectsSearch.filter(
                x =>
                    (x.accountingObjectCode ? x.accountingObjectCode.toLowerCase() : '').includes(
                        this.acCodeFilter ? this.acCodeFilter.toLowerCase() : ''
                    ) &&
                    (x.accountingObjectAddress ? x.accountingObjectAddress.toLowerCase() : '').includes(
                        this.acAddressFilter ? this.acAddressFilter.toLowerCase() : ''
                    ) &&
                    (x.accountingObjectName ? x.accountingObjectName.toLowerCase() : '').includes(
                        this.acNameFilter ? this.acNameFilter.toLowerCase() : ''
                    )
            );
        } else if (this.acCodeFilter && this.acAddressFilter) {
            this.accountingObjects = this.accountingObjectsSearch.filter(
                x =>
                    (x.accountingObjectCode ? x.accountingObjectCode.toLowerCase() : '')
                        .toLowerCase()
                        .includes(this.acCodeFilter.toLowerCase()) &&
                    (x.accountingObjectAddress ? x.accountingObjectAddress.toLowerCase() : '')
                        .toLowerCase()
                        .includes(this.acAddressFilter.toLowerCase())
            );
        } else if (this.acCodeFilter && this.acNameFilter) {
            this.accountingObjects = this.accountingObjectsSearch.filter(
                x =>
                    (x.accountingObjectCode ? x.accountingObjectCode.toLowerCase() : '')
                        .toLowerCase()
                        .includes(this.acCodeFilter.toLowerCase()) &&
                    (x.accountingObjectName ? x.accountingObjectName.toLowerCase() : '')
                        .toLowerCase()
                        .includes(this.acNameFilter.toLowerCase())
            );
        } else if (this.acNameFilter && this.acAddressFilter) {
            this.accountingObjects = this.accountingObjectsSearch.filter(
                x =>
                    (x.accountingObjectName ? x.accountingObjectName.toLowerCase() : '')
                        .toLowerCase()
                        .includes(this.acNameFilter.toLowerCase()) &&
                    (x.accountingObjectAddress ? x.accountingObjectAddress.toLowerCase() : '')
                        .toLowerCase()
                        .includes(this.acAddressFilter.toLowerCase())
            );
        } else if (this.acAddressFilter) {
            this.accountingObjects = this.accountingObjectsSearch.filter(x =>
                (x.accountingObjectAddress ? x.accountingObjectAddress.toLowerCase() : '')
                    .toLowerCase()
                    .includes(this.acAddressFilter.toLowerCase())
            );
        } else if (this.acCodeFilter) {
            this.accountingObjects = this.accountingObjectsSearch.filter(x =>
                (x.accountingObjectCode ? x.accountingObjectCode.toLowerCase() : '').toLowerCase().includes(this.acCodeFilter.toLowerCase())
            );
        } else if (this.acNameFilter) {
            this.accountingObjects = this.accountingObjectsSearch.filter(x =>
                (x.accountingObjectName ? x.accountingObjectName.toLowerCase() : '').toLowerCase().includes(this.acNameFilter.toLowerCase())
            );
        } else {
            this.accountingObjects = this.accountingObjectsSearch;
        }
    }

    selectChangeAccountingObjectGroup() {
        this.accountingObjectService
            .getAccountingObjectByGroupID({
                id: this.accountingObjectGroupID ? this.accountingObjectGroupID : '',
                similarBranch: this.dependent ? this.dependent : '',
                companyID: this.organizationUnit ? this.organizationUnit.value : ''
            })
            .subscribe((res: HttpResponse<any[]>) => {
                this.accountingObjects = res.body;
                this.accountingObjectsSearch = res.body;
            });
    }

    selectChangeOrg() {
        if (this.organizationUnit && this.organizationUnit.parent.id) {
            if (this.organizationUnit.parent.unitType === 1) {
                this.dependent = false;
                this.isShowDependent = false;
            } else {
                this.isShowDependent = this.checkChildren(this.organizationUnit);
            }
        }
        // if (this.organizationUnit && this.organizationUnit.parent.id) {
        //     this.bankAccountDetailsService
        //         .getBankAccountDetailsByOrgID({ orgID: this.organizationUnit.parent.id })
        //         .subscribe((res: HttpResponse<IBankAccountDetails[]>) => {
        //             this.bankAccountDetails = [];
        //             this.bankAccountDetails.push(Object.assign({}));
        //             this.bankAccountDetails[0].id = 'Tất cả';
        //             this.bankAccountDetails[0].bankAccount = 'Tất cả';
        //             this.bankAccountDetails[0].bankName = 'Tất cả';
        //             for (let i = 0; i < res.body.length; i++) {
        //                 this.bankAccountDetails.push(res.body[i]);
        //             }
        //         });
        // }
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
