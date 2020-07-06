import { Component, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { JhiEventManager } from 'ng-jhipster';
import { RequestReport } from 'app/bao-cao/reqest-report.model';
import * as moment from 'moment';
import { Moment } from 'moment';
import { BaoCao } from 'app/app.constants';
import { DATE_FORMAT_SEARCH } from 'app/shared';
import { BaoCaoService } from 'app/bao-cao/bao-cao.service';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';
import { HttpResponse } from '@angular/common/http';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { AccountListService } from 'app/danhmuc/account-list';
import { IAccountList } from 'app/shared/model/account-list.model';
import { ROLE } from 'app/role.constants';
import { TreeOrganizationUnit } from 'app/shared/model/organization-unit-tree.model';

@Component({
    selector: 'eb-so-cai-hinh-thuc-nhat-ky-chung',
    templateUrl: './so-cai-hinh-thuc-nhat-ky-chung.component.html',
    styleUrls: ['so-cai-hinh-thuc-nhat-ky-chung.component.css']
})
export class SoCaiHinhThucNhatKyChungComponent implements OnInit {
    createDate: Moment;
    fromDate: Moment;
    toDate: Moment;
    requestReport: RequestReport;
    listTimeLine: any[];
    account: any;
    modalRef: NgbModalRef;
    showAccumAmount: boolean;
    groupTheSameItem: boolean;
    timeLineVoucher: any;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    organizationUnit: any;
    dependent: boolean;
    treeOrganizationUnits: any[];
    accountLists: IAccountList[];

    accountNumber: string;
    accountName: string;
    grade: number;
    accountListsData: any;
    ROLE = ROLE;
    isShowDependent: boolean;
    isKetXuat: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private principal: Principal,
        private toastr: ToastrService,
        public translate: TranslateService,
        public eventManager: JhiEventManager,
        private baoCaoService: BaoCaoService,
        public utilsService: UtilsService,
        private organizationUnitService: OrganizationUnitService,
        private accountListService: AccountListService
    ) {
        this.requestReport = {};
        this.accountListService.getAccountLists().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.accountLists = res.body;
            this.accountListsData = res.body;
        });
    }

    ngOnInit(): void {
        this.groupTheSameItem = true;
        this.showAccumAmount = true;
        this.principal.identity().then(account => {
            this.account = account;
            // this.treeOrganizationUnits = [];
            this.listTimeLine = this.utilsService.getCbbTimeLine();
            const fromDateSS = sessionStorage.getItem('fromDate_SoCai');
            const toDateSS = sessionStorage.getItem('toDate_SoCai');
            const timeLineSS = sessionStorage.getItem('timeLineVoucher_SoCai');
            this.isKetXuat = !this.utilsService.isPackageDemo(account);
            if (fromDateSS && fromDateSS) {
                this.fromDate = moment(fromDateSS, DATE_FORMAT_SEARCH);
                this.toDate = moment(toDateSS, DATE_FORMAT_SEARCH);
                this.createDate = this.fromDate;
                this.timeLineVoucher = timeLineSS;
            } else {
                this.timeLineVoucher = this.listTimeLine[4].value;
                this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
            }
            /*this.organizationUnitService.getChildCompanyByID().subscribe((res: HttpResponse<any>) => {
                this.treeOrganizationUnits = res.body.orgTrees;
                this.organizationUnit = res.body.currentOrgLogin;
                this.selectChangeOrg();
            });*/
        });
    }

    accept() {
        if (this.checkErr()) {
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT_SEARCH);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT_SEARCH);
            sessionStorage.setItem('fromDate_SoCai', this.requestReport.fromDate.format(DATE_FORMAT_SEARCH));
            sessionStorage.setItem('toDate_SoCai', this.requestReport.toDate.format(DATE_FORMAT_SEARCH));
            sessionStorage.setItem('timeLineVoucher_SoCai', this.timeLineVoucher);
            this.requestReport.showAccumAmount = this.showAccumAmount;
            this.requestReport.groupTheSameItem = this.groupTheSameItem;
            this.requestReport.typeReport = BaoCao.TongHop.SO_CAI_HT_NHAT_KY_CHUNG;
            this.requestReport.companyID = this.organizationUnit.parent.id;
            this.requestReport.accountList = this.accountLists.filter(n => n.checked).map(n => n.accountNumber);
            this.baoCaoService.getReport(this.requestReport);
        }
    }

    checkErr() {
        if (!this.fromDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.materialQuantum.fromDateIsNotBlank'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (!this.toDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.materialQuantum.toDateIsNotBlank'),
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
        if (!this.accountListsData.some(n => n.checked)) {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountList.accountNumberIsNotNull'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
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
        if (this.accountLists) {
            return this.accountLists.every(item => item.checked) && this.accountLists.length;
        } else {
            return false;
        }
    }

    checkAll() {
        const isCheck = this.accountLists.every(item => item.checked) && this.accountLists.length;
        this.accountLists.forEach(item => (item.checked = !isCheck));
    }

    check(accountList: IAccountList) {
        accountList.checked = !accountList.checked;
    }

    changeMGFilter() {
        this.accountLists = this.accountListsData.filter(
            x =>
                (this.accountNumber ? x.accountNumber.toLowerCase().startsWith(this.accountNumber.toLowerCase()) : true) &&
                (this.accountName ? x.accountName.toLowerCase().includes(this.accountName.toLowerCase()) : true) &&
                (this.grade || this.grade === 0 ? x.grade === this.grade : true)
        );
    }

    exportExcel() {
        if (this.checkErr()) {
            this.requestReport.toDate = moment(this.toDate, DATE_FORMAT_SEARCH);
            this.requestReport.fromDate = moment(this.fromDate, DATE_FORMAT_SEARCH);
            sessionStorage.setItem('fromDate_SoCai', this.requestReport.fromDate.format(DATE_FORMAT_SEARCH));
            sessionStorage.setItem('toDate_SoCai', this.requestReport.toDate.format(DATE_FORMAT_SEARCH));
            sessionStorage.setItem('timeLineVoucher_SoCai', this.timeLineVoucher);
            this.requestReport.showAccumAmount = this.showAccumAmount;
            this.requestReport.groupTheSameItem = this.groupTheSameItem;
            this.requestReport.typeReport = BaoCao.TongHop.SO_CAI_HT_NHAT_KY_CHUNG;
            this.requestReport.fileName = BaoCao.TongHop.SO_CAI_HT_NHAT_KY_CHUNG_XLS;
            this.requestReport.companyID = this.organizationUnit.parent.id;
            this.requestReport.accountList = this.accountListsData.filter(n => n.checked).map(n => n.accountNumber);
            this.requestReport.dependent = this.dependent;
            this.baoCaoService.exportExcel(this.requestReport).subscribe(
                res => {
                    const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                    const fileURL = URL.createObjectURL(blob);

                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    const name = BaoCao.TongHop.SO_CAI_HT_NHAT_KY_CHUNG_XLS;
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
}
