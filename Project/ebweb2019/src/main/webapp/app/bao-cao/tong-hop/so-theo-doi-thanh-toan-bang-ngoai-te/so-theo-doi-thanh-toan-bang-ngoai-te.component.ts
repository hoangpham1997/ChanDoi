import { Component, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { JhiEventManager } from 'ng-jhipster';
import { IRequestReport, RequestReport } from 'app/bao-cao/reqest-report.model';
import * as moment from 'moment';
import { BaoCao, MATERIAL_GOODS_TYPE } from 'app/app.constants';
import { DATE_FORMAT, DATE_FORMAT_SEARCH } from 'app/shared';
import { BaoCaoService } from 'app/bao-cao/bao-cao.service';
import { Moment } from 'moment';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';
import { HttpResponse } from '@angular/common/http';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { AccountListService } from 'app/danhmuc/account-list';
import { IAccountList } from 'app/shared/model/account-list.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { BankAccountDetails, IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { TreeOrganizationUnit } from 'app/shared/model/organization-unit-tree.model';
import { ROLE } from 'app/role.constants';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { IRepository } from 'app/shared/model/repository.model';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { RepositoryService } from 'app/danhmuc/repository';
import { MaterialGoodsCategoryService } from 'app/danhmuc/material-goods-category';
import { MaterialGoodsConvertUnitService } from 'app/entities/material-goods-convert-unit';
import { AccountingObject, IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';

@Component({
    selector: 'eb-so-theo-doi-thanh-toan-bang-ngoai-te',
    templateUrl: './so-theo-doi-thanh-toan-bang-ngoai-te.component.html',
    styleUrls: ['so-theo-doi-thanh-toan-bang-ngoai-te.component.css']
})
export class SoTheoDoiThanhToanBangNgoaiTeComponent implements OnInit {
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
    organizationUnit: any;
    treeOrganizationUnits: any[];
    currencies: ICurrency[];
    accountList: IAccountList[];
    accountingObjects: IAccountingObject[];
    accountingObjectsFilter: IAccountingObject[];
    currencyCode: string;
    accountNumber: string;
    accountingObjectsCode: string;
    accountingObjectsName: string;
    accountingObjectsAddress: string;
    dependent: boolean;
    ROLE_KetXuat = ROLE.BaoCaoSoTheoDoiThanhToanBangNgoaiTe_KetXuat;
    isKetXuat: boolean;
    isShowDependent: boolean;
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
        private accountListService: AccountListService,
        private currencyService: CurrencyService,
        private accountingObjectService: AccountingObjectService
    ) {}

    ngOnInit() {
        this.dependent = false;
        this.principal.identity().then(account => {
            this.account = account;
            this.isKetXuat = !this.utilsService.isPackageDemo(account);
            // this.treeOrganizationUnits = [];
            this.listTimeLine = this.utilsService.getCbbTimeLine();
            this.timeLineVoucher = this.listTimeLine[4].value;
            this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
            this.getSessionData();
            // this.currencyCode = this.account.organizationUnit.currencyID;
            // this.organizationUnitService.getChildCompanyByID().subscribe((res: HttpResponse<any>) => {
            //     this.treeOrganizationUnits = res.body.orgTrees;
            //     this.organizationUnit = res.body.currentOrgLogin;
            //     this.baoCaoService.checkHiddenDependent(this.treeOrganizationUnits);
            //     this.baoCaoService.checkHiddenFirstCompany(this.organizationUnit);
            //
            // });
            this.changeOptionData();
        });
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('searchSoTheoDoiThanhToanBangNgoaiTe'));
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
        this.dataSession.currencyID = this.currencyCode;
        this.dataSession.accountNumber = this.accountNumber;
        this.dataSession.timeLineVoucher = this.timeLineVoucher ? this.timeLineVoucher : '';
        sessionStorage.setItem('searchSoTheoDoiThanhToanBangNgoaiTe', JSON.stringify(this.dataSession));
    }

    showReport() {
        if (this.checkErr()) {
            this.setSessionData();
            this.requestReport = {};
            this.requestReport.companyID = this.organizationUnit ? this.organizationUnit.parent.id : null;
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT);
            this.requestReport.currencyID = this.currencyCode;
            this.requestReport.accountNumber = this.accountNumber;
            this.requestReport.accountingObjects = this.accountingObjectsFilter.filter(x => x.checked).map(c => c.id);
            this.requestReport.typeReport = BaoCao.TongHop.SO_THEO_DOI_THANH_TOAN_BANG_NGOAI_TE;
            this.requestReport.dependent = this.dependent;
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
            this.requestReport.currencyID = this.currencyCode;
            this.requestReport.accountNumber = this.accountNumber;
            this.requestReport.accountingObjects = this.accountingObjectsFilter.filter(x => x.checked).map(c => c.id);
            this.requestReport.typeReport = BaoCao.TongHop.SO_THEO_DOI_THANH_TOAN_BANG_NGOAI_TE;
            this.requestReport.fileName = BaoCao.TongHop.SO_THEO_DOI_THANH_TOAN_BANG_NGOAI_TE_XLS;
            this.requestReport.dependent = this.dependent;
            this.baoCaoService.exportExcel(this.requestReport).subscribe(
                res => {
                    const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                    const fileURL = URL.createObjectURL(blob);

                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    const name = BaoCao.TongHop.SO_THEO_DOI_THANH_TOAN_BANG_NGOAI_TE_XLS;
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

    changeOptionData(reload?) {
        if (reload) {
            this.dependent = false;
        }
        // if (this.organizationUnit && this.organizationUnit.parent.id) {
        //     if (this.organizationUnit.parent.unitType === 1) {
        //         this.isShowDependent = false;
        //     } else {
        //         this.isShowDependent = this.checkChildren(this.organizationUnit);
        //     }
        // }
        this.accountListService.getAllAccountList().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.accountList = res.body;
        });
        this.currencyService.findCurrencyByCompanyID().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencies = res.body;
        });
        this.accountingObjectService
            .findAllAccountingObjectByCompany({
                companyID: this.organizationUnit.parent.id,
                dependent: this.dependent
            })
            .subscribe((res: HttpResponse<IAccountingObject[]>) => {
                this.accountingObjects = res.body;
                this.accountingObjectsFilter = this.accountingObjects.sort((a, b) =>
                    a.accountingObjectCode.localeCompare(b.accountingObjectCode)
                );
            });
    }

    changeMGFilter() {
        if (this.accountingObjectsCode && this.accountingObjectsName && this.accountingObjectsAddress) {
            this.accountingObjectsFilter = this.accountingObjects.filter(
                x =>
                    x.accountingObjectCode.toLowerCase().includes(this.accountingObjectsCode.toLowerCase()) &&
                    x.accountingObjectName.toLowerCase().includes(this.accountingObjectsName.toLowerCase()) &&
                    x.accountingObjectAddress.toLowerCase().includes(this.accountingObjectsAddress.toLowerCase())
            );
        } else if (this.accountingObjectsCode) {
            this.accountingObjectsFilter = this.accountingObjects.filter(x =>
                x.accountingObjectCode.toLowerCase().includes(this.accountingObjectsCode.toLowerCase())
            );
        } else if (this.accountingObjectsName) {
            this.accountingObjectsFilter = this.accountingObjects.filter(x =>
                x.accountingObjectName.toLowerCase().includes(this.accountingObjectsName.toLowerCase())
            );
        } else if (this.accountingObjectsAddress) {
            this.accountingObjectsFilter = this.accountingObjects.filter(x =>
                (x.accountingObjectAddress == null ? '' : x.accountingObjectAddress)
                    .toLowerCase()
                    .includes(this.accountingObjectsAddress.toLowerCase())
            );
        } else {
            this.accountingObjectsFilter = this.accountingObjects;
        }
    }

    checkErr() {
        if (!this.organizationUnit) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.error.nullOrg'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
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
        if (!this.accountNumber) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.error.nullAccount'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (!this.currencyCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.error.nullCurrency'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (this.accountingObjectsFilter.filter(x => x.checked).length === 0) {
            this.toastr.error(
                this.translate.instant('ebwebApp.baoCao.error.nullAccountingObject'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        return true;
    }

    isCheckAll() {
        if (this.accountingObjectsFilter) {
            return this.accountingObjectsFilter.every(item => item.checked) && this.accountingObjectsFilter.length;
        }
        return false;
    }

    checkAll() {
        const isCheck = this.accountingObjectsFilter.every(item => item.checked) && this.accountingObjectsFilter.length;
        this.accountingObjectsFilter.forEach(item => (item.checked = !isCheck));
    }

    check(accountingObject: IAccountingObject) {
        accountingObject.checked = !accountingObject.checked;
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

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    // checkChildren(treeOrganizationUnit: TreeOrganizationUnit): boolean {
    //     if (treeOrganizationUnit && treeOrganizationUnit.children && treeOrganizationUnit.children.length > 0) {
    //         for (let i = 0; i < treeOrganizationUnit.children.length; i++) {
    //             if (
    //                 treeOrganizationUnit.children[i].parent.accType === 0 &&
    //                 treeOrganizationUnit.children[i].parent.unitType === 1 &&
    //                 treeOrganizationUnit.children[i].parent.parentID === this.organizationUnit.parent.id
    //             ) {
    //                 return true;
    //             }
    //         }
    //     }
    //     return false;
    // }
}
