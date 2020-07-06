import { Component, OnInit } from '@angular/core';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { TreeViewItemComponent } from 'app/shared/tree-combo-box/tree-combo-box.component';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';
import { Principal } from 'app/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import * as moment from 'moment';
import { Moment } from 'moment';
import { AccountListService } from 'app/danhmuc/account-list';
import { IAccountList } from 'app/shared/model/account-list.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { AccountingObjectGroupService } from 'app/danhmuc/accounting-object-group';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { IAccountingObjectGroup } from 'app/shared/model/accounting-object-group.model';
import { RequestReport } from 'app/bao-cao/reqest-report.model';
import { DATE_FORMAT, DATE_FORMAT_SEARCH } from 'app/shared';
import { BaoCao } from 'app/app.constants';
import { BaoCaoService } from 'app/bao-cao/bao-cao.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { ROLE } from 'app/role.constants';
import { TreeOrganizationUnit } from 'app/shared/model/organization-unit-tree.model';

@Component({
    selector: 'eb-cong-no-phai-thu',
    templateUrl: './cong-no-phai-thu.component.html',
    styleUrls: ['./cong-no-phai-thu.component.css']
})
export class CongNoPhaiThuComponent implements OnInit {
    isSimilarSum: boolean;
    organizationUnit: any;
    treeOrganizationUnits: any[];
    account: any;
    listTimeLine: any[];
    timeLineVoucher: any;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    fromDate: Moment;
    toDate: Moment;
    accountLists: IAccountList[];
    accountItem: any;
    currencies: ICurrency[];
    currencyCode: any;
    accountingObjects: any[];
    accountingObjectGroups: IAccountingObjectGroup[];
    accountingObjectGroupID: any;
    status: boolean;
    requestReport: RequestReport;
    requestReportSearch: any;
    acCodeFilter: any;
    acNameFilter: any;
    accountingObjectsSearch: any[];
    acAddressFilter: any;
    dependent: any;
    ROLE_KetXuat = ROLE.TongHopCongNoPhaiThu_KetXuat;
    ROLE_KetXuatChiTiet = ROLE.ChiTietCongNoPhaiThu_KetXuat;
    ROLE_Xem = ROLE.TongHopCongNoPhaiThu_Xem;
    ROLE_XemChiTiet = ROLE.ChiTietCongNoPhaiThu_Xem;
    isKetXuat: boolean;
    getAmountOriginal: boolean;
    currencyCodeDefault: any;
    isShowDependent: boolean;
    constructor(
        public activeModal: NgbActiveModal,
        private principal: Principal,
        private baoCaoService: BaoCaoService,
        private organizationUnitService: OrganizationUnitService,
        private toastr: ToastrService,
        public translate: TranslateService,
        public utilsService: UtilsService,
        private accountListService: AccountListService,
        private currencyService: CurrencyService,
        private accountingObjectGroupService: AccountingObjectGroupService,
        private accountingObjectService: AccountingObjectService
    ) {}

    ngOnInit() {
        this.getAmountOriginal = false;
        this.isSimilarSum = true;
        this.principal.identity().then(account => {
            this.account = account;
            if (!this.currencyCode) {
                this.currencyCode = this.account.organizationUnit.currencyID;
            }
            this.currencyCodeDefault = this.account.organizationUnit.currencyID;
            this.isKetXuat = !this.utilsService.isPackageDemo(this.account);
            this.treeOrganizationUnits = [];
            this.listTimeLine = this.utilsService.getCbbTimeLine();
            this.getSessionData();
            this.organizationUnitService.getChildCompanyByID().subscribe((res: HttpResponse<any>) => {
                this.treeOrganizationUnits = res.body.orgTrees;
                this.baoCaoService.checkHiddenDependent(this.treeOrganizationUnits);
                this.organizationUnit = res.body.currentOrgLogin;
                this.changeDependent();
            });
            this.accountListService.getAccountDetailTypeActive().subscribe((res: HttpResponse<IAccountList[]>) => {
                this.accountLists = res.body;
                if (res.body && res.body.length > 0 && !this.accountItem) {
                    this.accountItem = this.accountLists.some(x => x.accountNumber === '131') ? '131' : this.accountLists[0].accountNumber;
                }
            });
            this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
                this.currencies = res.body;
            });
            // this.accountingObjectGroupService
            //     .getAllAccountingObjectGroup({
            //         similarBranch: this.dependent ? this.dependent : false,
            //         companyID: this.organizationUnit ? this.organizationUnit.value : null
            //     })
            //     .subscribe((res: HttpResponse<IAccountingObjectGroup[]>) => {
            //         this.accountingObjectGroups = res.body;
            //     });
            // this.accountingObjectService
            //     .getAccountingObjectByGroupIDKH({
            //         id: this.accountingObjectGroupID ? this.accountingObjectGroupID : '',
            //         similarBranch: this.dependent ? this.dependent : false,
            //         companyID: this.organizationUnit ? this.organizationUnit.value : ''
            //     })
            //     .subscribe((res: HttpResponse<any[]>) => {
            //         this.accountingObjects = res.body;
            //         this.accountingObjectsSearch = res.body;
            //     });
        });
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    selectChangeBeginDateAndEndDate(intTimeLine: String) {
        if (intTimeLine) {
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
            this.fromDate = moment(this.objTimeLine.dtBeginDate);
            this.toDate = moment(this.objTimeLine.dtEndDate);
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

    check(accountingObjectsItem: any) {
        accountingObjectsItem.checked = !accountingObjectsItem.checked;
    }

    accept() {
        this.setSessionData();
        if (!this.checkErr()) {
            return;
        }
        if (!this.requestReport) {
            this.requestReport = new RequestReport();
        }
        this.requestReport.toDate = moment(this.toDate, DATE_FORMAT_SEARCH);
        this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT_SEARCH);
        this.requestReport.accountNumber = this.accountItem;
        this.requestReport.timeLineVoucher = this.timeLineVoucher.display;
        this.requestReport.typeReport = this.status ? BaoCao.BanHang.CHI_TIET_CONG_NO_PHAI_THU : BaoCao.BanHang.TONG_HOP_CONG_NO_PHAI_THU;
        this.requestReport.listMaterialGoods = this.accountingObjects.filter(n => n.checked).map(n => n.id);
        this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
        this.requestReport.currencyID = this.currencyCode;
        this.requestReport.dependent = this.dependent;
        this.requestReport.getAmountOriginal = this.getAmountOriginal;
        if (this.status) {
            this.requestReport.groupTheSameItem = this.isSimilarSum;
        }
        this.baoCaoService.getReport(this.requestReport);
        // }
    }

    checkErr() {
        if (!this.organizationUnit) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.organizationUnitNotBeBlank'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
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
        if (!this.currencyCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.bangKeSoDuNganHangPopup.currencyCodeNotBeBlank'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
        }
        if (!this.accountItem) {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountList.accountNumberIsNotNull'),
                this.translate.instant('ebwebApp.accountList.error')
            );
            return false;
        }
        const isNotChecked = this.accountingObjects && this.accountingObjects.every(item => !item.checked);
        if (isNotChecked) {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountingObject.notCheckNCC'),
                this.translate.instant('ebwebApp.accountingObject.error')
            );
            return false;
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

    isNotCheck() {
        const isNotCheck = this.accountingObjects && this.accountingObjects.every(item => !item.checked);
        if (isNotCheck) {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountingObject.notCheckNCC'),
                this.translate.instant('ebwebApp.accountingObject.error')
            );
            return true;
        }
        return false;
    }

    selectChangeInAccountingObjectGroup() {
        this.accountingObjectService
            .getAccountingObjectByGroupIDKH({
                id: this.accountingObjectGroupID ? this.accountingObjectGroupID : '',
                similarBranch: this.dependent ? this.dependent : '',
                companyID: this.organizationUnit ? this.organizationUnit.value : ''
            })
            .subscribe((res: HttpResponse<any[]>) => {
                this.accountingObjects = res.body;
                this.accountingObjectsSearch = res.body;
            });
    }

    getSessionData() {
        this.requestReportSearch = new RequestReport();
        if (this.status) {
            this.requestReportSearch = JSON.parse(sessionStorage.getItem('searchChiTietCongNoPhaiThu'))
                ? JSON.parse(sessionStorage.getItem('searchChiTietCongNoPhaiThu'))
                : '';
        } else {
            this.requestReportSearch = JSON.parse(sessionStorage.getItem('searchTongHopCongNoPhaiThu'))
                ? JSON.parse(sessionStorage.getItem('searchTongHopCongNoPhaiThu'))
                : '';
        }
        if (this.requestReportSearch) {
            this.timeLineVoucher = this.requestReportSearch.timeLineVoucher;
            this.fromDate = moment(this.requestReportSearch.fromDate);
            this.toDate = moment(this.requestReportSearch.toDate);
            this.accountItem = this.requestReportSearch.accountNumber;
            this.currencyCode = this.requestReportSearch.currencyID;
            this.dependent = this.requestReportSearch.dependent ? this.requestReportSearch.dependent : false;
            this.getAmountOriginal = this.requestReportSearch.getAmountOriginal;
        } else {
            if (this.status) {
                sessionStorage.removeItem('searchChiTietCongNoPhaiThu');
            } else {
                sessionStorage.removeItem('searchTongHopCongNoPhaiThu');
            }
            this.timeLineVoucher = this.listTimeLine[4].value;
            this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
        }
    }

    setSessionData() {
        if (this.status) {
            sessionStorage.removeItem('searchChiTietCongNoPhaiThu');
        } else {
            sessionStorage.removeItem('searchTongHopCongNoPhaiThu');
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
        this.requestReportSearch.dependent = this.dependent;
        this.requestReportSearch.getAmountOriginal = this.getAmountOriginal;
        if (this.status) {
            this.requestReportSearch.groupTheSameItem = this.isSimilarSum;
        }
        if (this.status) {
            sessionStorage.setItem('searchChiTietCongNoPhaiThu', JSON.stringify(this.requestReportSearch ? this.requestReportSearch : ''));
        } else {
            sessionStorage.setItem('searchTongHopCongNoPhaiThu', JSON.stringify(this.requestReportSearch ? this.requestReportSearch : ''));
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
    exportExcel() {
        if (this.checkErr()) {
            this.setSessionData();
            this.requestReport = {};
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT_SEARCH);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT_SEARCH);
            this.requestReport.accountNumber = this.accountItem;
            this.requestReport.timeLineVoucher = this.timeLineVoucher.display;
            this.requestReport.getAmountOriginal = this.getAmountOriginal;
            this.requestReport.typeReport = this.status
                ? BaoCao.BanHang.CHI_TIET_CONG_NO_PHAI_THU
                : BaoCao.BanHang.TONG_HOP_CONG_NO_PHAI_THU;
            this.requestReport.listMaterialGoods = this.accountingObjects.filter(n => n.checked).map(n => n.id);
            this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
            this.requestReport.currencyID = this.currencyCode;
            this.requestReport.typeReport = this.status
                ? BaoCao.BanHang.CHI_TIET_CONG_NO_PHAI_THU
                : BaoCao.BanHang.TONG_HOP_CONG_NO_PHAI_THU;
            this.requestReport.fileName = this.status
                ? BaoCao.BaoCaoTaiChinh.CHI_TIET_CONG_NO_PHAI_THU_XLS
                : BaoCao.BaoCaoTaiChinh.TONG_HOP_CONG_NO_PHAI_THU_XLS;
            // this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT_SEARCH).format(DATE_FORMAT);
            this.baoCaoService.exportExcel(this.requestReport).subscribe(
                res => {
                    const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                    const fileURL = URL.createObjectURL(blob);

                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    const name = this.status
                        ? BaoCao.BaoCaoTaiChinh.CHI_TIET_CONG_NO_PHAI_THU_XLS
                        : BaoCao.BaoCaoTaiChinh.TONG_HOP_CONG_NO_PHAI_THU_XLS;
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

    changeDependent() {
        this.accountingObjectGroupService
            .getAllAccountingObjectGroup({
                similarBranch: this.dependent ? this.dependent : '',
                companyID: this.organizationUnit ? this.organizationUnit.value : ''
            })
            .subscribe((res: HttpResponse<IAccountingObjectGroup[]>) => {
                this.accountingObjectGroups = res.body;
            });
        this.accountingObjectService
            .getAccountingObjectByGroupIDKH({
                id: this.accountingObjectGroupID ? this.accountingObjectGroupID : '',
                similarBranch: this.dependent ? this.dependent : '',
                companyID: this.organizationUnit ? this.organizationUnit.value : ''
            })
            .subscribe((res: HttpResponse<any[]>) => {
                this.accountingObjects = res.body;
                this.accountingObjectsSearch = res.body;
            });
        this.selectChangeOrg();
    }

    changeOrganizationUnit() {
        console.log(this.organizationUnit);
        this.changeDependent();
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
