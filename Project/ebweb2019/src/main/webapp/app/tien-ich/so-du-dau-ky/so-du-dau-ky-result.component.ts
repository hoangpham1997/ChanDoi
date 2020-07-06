import { Component, OnInit, ViewChild } from '@angular/core';
import { BaseComponent } from 'app/shared/base-component/base.component';

import { IAccountList } from 'app/shared/model/account-list.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { SoDuDauKyService } from 'app/tien-ich/so-du-dau-ky/so-du-dau-ky.service';
import { Router } from '@angular/router';
import * as moment from 'moment';
import { ACCOUNTING_TYPE, ACCOUNT_DETAIL_TYPE, ACCOUNTING_TYPE_ID, CURRENCY_ID, ImportExcel } from 'app/app.constants';
import { DATE_FORMAT_SLASH } from 'app/shared';
import { Principal } from 'app/core';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { EbVirtualScrollerComponent } from 'app/virtual-scroller/virtual-scroller';
import { ROLE } from 'app/role.constants';
import { getEmptyRow } from 'app/shared/util/row-util';
import { EbMaterialGoodsSpecificationsModalComponent } from 'app/shared/modal/material-goods-specifications/material-goods-specifications.component';

@Component({
    selector: 'eb-so-du-dau-ky-result',
    templateUrl: './so-du-dau-ky-result.component.html',
    styleUrls: ['so-du-dau-ky-insert-update.component.css']
})
export class SoDuDauKyResultComponent extends BaseComponent implements OnInit {
    @ViewChild('chooseSheetModal') public chooseSheetModal: NgbModalRef;
    ROLE = ROLE;
    isLoading: boolean;
    accountList: IAccountList[];
    openingBalanceDetails: any[];
    openingBalance: IAccountList;
    accountingType: string[];
    isForeignCurrency: boolean;
    account: any;
    currency: string;
    ACCOUNTING_TYPE = ACCOUNTING_TYPE;
    OPENING_BALANCE = 74;
    eventSubscriber: Subscription;
    refModel: any;
    opsData: any;
    refModel2: any;
    refModel3: any;
    link: any;
    file: any;
    balanceType: number;
    isVNDFormat: boolean;
    totalCreditAmount: number;
    totalDebitAmount: number;
    sheetNames: string[];
    @ViewChild(EbVirtualScrollerComponent) private virtualScroller: EbVirtualScrollerComponent;
    searchAccountNumber: string;
    searchAccountName: string;
    accountListTemp: any[];
    constructor(
        private accountListService: AccountListService,
        private soDuDauKyService: SoDuDauKyService,
        private toastrService: ToastrService,
        private translateService: TranslateService,
        private router: Router,
        private principal: Principal,
        private refModalService: RefModalService,
        private modalService: NgbModal,
        public utilsService: UtilsService,
        private eventManager: JhiEventManager
    ) {
        super();
        this.isLoading = false;
    }

    ngOnInit(): void {
        this.totalDebitAmount = 0;
        this.totalCreditAmount = 0;
        this.accountListService.getAccountListsActiveAndOP().subscribe(res => this.onSetupAccountList(res));
        this.principal.identity().then(account => {
            this.account = account;
            this.currency = this.account.organizationUnit.currencyID;
            this.isVNDFormat = this.currency === CURRENCY_ID;
        });
        this.registerExport();
    }

    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${this.OPENING_BALANCE}`, () => {
            this.exportExcel();
        });
    }

    getDataBalanceType(): any[] {
        return [
            {
                typeName: this.translateService.instant('ebwebApp.tienIch.soDuDauKy.opMaterialGoods'),
                value: ACCOUNTING_TYPE_ID.MATERIAL_GOODS_TYPE
            },
            {
                typeName: this.translateService.instant('ebwebApp.tienIch.soDuDauKy.opAccountingObject'),
                value: ACCOUNTING_TYPE_ID.ACCOUNTING_OBJECT
            },
            {
                typeName: this.translateService.instant('ebwebApp.tienIch.soDuDauKy.opAccountNormal'),
                value: ACCOUNTING_TYPE_ID.NORMAL_ACCOUNT
            }
        ];
    }
    onSetupAccountList(res: any) {
        this.accountList = res.body;
        this.accountList = this.accountList.sort((a, b) => a.accountNumber.localeCompare(b.accountNumber));
        if (!this.accountList || this.accountList.length === 0) {
            return;
        }
        for (let i = 0; i < this.accountList.length; i++) {
            if (this.accountList[i].children && this.accountList[i].children.length) {
                this.accountList.splice(i + 1, 0, ...this.accountList[i].children);
            }
        }
        const item =
            this.accountList.find(x => x.accountNumber === this.soDuDauKyService.getAccountList().accountNumber) || this.accountList[0];
        this.openingBalance = null;
        for (let i = 0; i < this.accountList.length; i++) {
            if (!this.accountList[i].parentAccountID) {
                this.totalCreditAmount += this.accountList[i].creditAmountOriginal;
                this.totalDebitAmount += this.accountList[i].debitAmountOriginal;
                for (let j = 0; j < this.accountList[i].children.length; j++) {
                    if (this.accountList[i].children[j].opAccountDTOList.length > 0) {
                        for (let k = 0; k < this.accountList[i].children[j].opAccountDTOList.length; k++) {
                            this.accountList[i].children[j].creditAmountOriginal = this.accountList[i].children[j].opAccountDTOList[
                                k
                            ].creditAmount;
                            this.accountList[i].creditAmountOriginal = this.accountList[i].children[j].opAccountDTOList[k].creditAmount;
                            this.accountList[i].children[j].debitAmountOriginal = this.accountList[i].children[j].opAccountDTOList[
                                k
                            ].debitAmount;
                        }
                    }
                }
            }
        }
        this.accountListTemp = this.accountList;
        this.onSelect(item, null);
        this.scrollToItem(item);
    }

    canDeactive() {
        return true;
    }

    trackIdentity(index, item: any) {
        return item.receiptDate;
    }

    importFromExcel(detailModal) {
        this.refModel = this.modalService.open(detailModal, { size: 'lg', backdrop: 'static' });
    }

    exportExcel() {
        this.accountListService.exportResultOP().subscribe(res => {
            this.refModalService.open(null, EbReportPdfPopupComponent, res, false, this.OPENING_BALANCE);
        });
    }

    onSelect(result, isDoubleClick?: boolean) {
        if (!result) {
            return;
        }

        this.accountingType = [];
        this.openingBalance = result;
        if (this.accountList) {
            if (isDoubleClick) {
                if (result.parentNode) {
                    this.toastrService.warning(this.translateService.instant('ebwebApp.tienIch.soDuDauKy.canNotClickParentNode'));
                    return;
                }
                this.setOpeningBalances();
            } else {
                if (result.parentNode) {
                    this.openingBalance = result;
                    this.openingBalanceDetails = [];
                    return;
                }
            }
        }
        this.soDuDauKyService.setAccountList(this.openingBalance);
        this.isForeignCurrency = result.isForeignCurrency;
        if (result.detailType) {
            const accLst = result.detailType.split(';');
            this.isForeignCurrency = result.isForeignCurrency;
            if (accLst.includes(ACCOUNT_DETAIL_TYPE.ACCOUNT_DEBIT)) {
                this.accountingType.push(ACCOUNTING_TYPE.NH);
            }
            if (
                accLst.includes(ACCOUNT_DETAIL_TYPE.ACCOUNT_SUPPLIER) ||
                accLst.includes(ACCOUNT_DETAIL_TYPE.ACCOUNT_CUSTOMER) ||
                accLst.includes(ACCOUNT_DETAIL_TYPE.ACCOUNT_EMPLOYEE)
            ) {
                this.accountingType.push(ACCOUNTING_TYPE.AO);
            }
            if (accLst.includes(ACCOUNT_DETAIL_TYPE.ACCOUNT_BANK)) {
                this.accountingType.push(ACCOUNTING_TYPE.MT);
            }
        }
        if (
            this.openingBalanceDetails &&
            this.openingBalanceDetails.length > 0 &&
            this.openingBalanceDetails[0].accountNumber === this.openingBalance.accountNumber
        ) {
            return;
        }

        if (this.accountingType.includes(ACCOUNTING_TYPE.MT)) {
            this.soDuDauKyService.findAllOPMaterialGoods({ accountNumber: this.openingBalance.accountNumber }).subscribe(res => {
                this.openingBalanceDetails = res.body;
                for (const obd of this.openingBalanceDetails) {
                    obd.expiredDate = moment(obd.expiryDate).format(DATE_FORMAT_SLASH);
                    obd.postedDateStr = moment(obd.postedDate).format(DATE_FORMAT_SLASH);
                }
                this.openingBalance.opMaterialGoodsDTOs = this.openingBalanceDetails;
                for (let i = 0; i < this.openingBalanceDetails.length; i++) {
                    if (this.currency && this.openingBalanceDetails[i].currencyId !== this.currency) {
                        this.openingBalanceDetails[i].amountOriginal = this.openingBalanceDetails[i].amount;
                    }
                }
                this.sortOpeningBalanceDetails();
            });
        } else {
            if (result.opAccountDTOList) {
                this.openingBalanceDetails = this.getOPAccountInAccountList(result);
                // this.openingBalanceDetails = result.opAccountDTOList.filter(
                //     x => x.creditAmount > 0 || x.creditAmountOriginal > 0 || x.debitAmount > 0 || x.debitAmountOriginal > 0
                // );
                for (let i = 0; i < this.openingBalanceDetails.length; i++) {
                    if (this.currency && this.openingBalanceDetails[i].currencyId === this.currency) {
                        this.openingBalanceDetails[i].debitAmountOriginal = this.openingBalanceDetails[i].debitAmount;
                        this.openingBalanceDetails[i].creditAmountOriginal = this.openingBalanceDetails[i].creditAmount;
                    }
                    // else {
                    //     this.openingBalanceDetails[i].debitAmountOriginal = this.openingBalanceDetails[i].debitAmount;
                    //     this.openingBalanceDetails[i].creditAmountOriginal = this.openingBalanceDetails[i].creditAmount;
                    // }
                }
            }
            this.sortOpeningBalanceDetails();
        }
    }

    sortOpeningBalanceDetails() {
        if (this.accountingType.includes(ACCOUNTING_TYPE.MT)) {
            this.openingBalanceDetails = this.openingBalanceDetails.sort(
                (a, b) => a.materialGoodsCode.localeCompare(b.materialGoodsCode) || a.orderPriority - b.orderPriority
            );
        } else if (this.accountingType.includes(ACCOUNTING_TYPE.AO)) {
            this.openingBalanceDetails = this.openingBalanceDetails.sort(
                (a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode) || a.orderPriority - b.orderPriority
            );
        } else {
            this.openingBalanceDetails = this.openingBalanceDetails.sort((a, b) => a.orderPriority - b.orderPriority);
        }
    }
    getOPAccountInAccountList(accountList: IAccountList) {
        const opAccountDTOList = [];
        if (accountList.children.length > 0) {
            for (let i = 0; i < accountList.children.length; i++) {
                opAccountDTOList.push(...this.getOPAccountInAccountList(accountList.children[i]));
            }
        } else {
            return accountList.opAccountDTOList.filter(
                x => x.creditAmount > 0 || x.creditAmountOriginal > 0 || x.debitAmount > 0 || x.debitAmountOriginal > 0
            );
        }
        return opAccountDTOList;
    }

    scrollToItem(item) {
        if (!item) {
            return;
        }
        this.virtualScroller.items = this.accountList;
        let indexSelected = this.virtualScroller.items.findIndex(x => x.accountNumber === item.accountNumber);
        this.openingBalance = this.virtualScroller.items[indexSelected];
        if (indexSelected > 5) {
            indexSelected -= 5;
        } else {
            indexSelected = 0;
        }
        this.virtualScroller.scrollInto(this.virtualScroller.items[indexSelected]);
        this.virtualScroller.refresh();
    }

    setOpeningBalances() {
        this.soDuDauKyService.setAccountingType(this.accountingType);
        this.soDuDauKyService.setAccountLists(this.accountList);
        this.router.navigate([`so-du-dau-ky/update`]);
    }

    selectedItemPerPage() {}

    downloadTem() {
        this.isLoading = true;
        this.soDuDauKyService.downloadTem({ type: this.balanceType }).subscribe(
            res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);

                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name =
                    this.balanceType === ACCOUNTING_TYPE_ID.NORMAL_ACCOUNT
                        ? 'SoDuDauKy_TKThongThuong.xlsx'
                        : this.balanceType === ACCOUNTING_TYPE_ID.ACCOUNTING_OBJECT
                            ? 'SoDuDauKy_TKChiTietDoiTuong.xlsx'
                            : 'SoDuDauKy_TKChiTietVTHH.xlsx';
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
                this.isLoading = false;
            },
            () => {
                this.isLoading = false;
            }
        );
    }

    changeFile(event) {
        const file = event.target.files;
        this.file = null;
        if (file && file.length) {
            this.file = file[0];
        }
    }

    checkHaveData(download: any, updLoadForm: any) {
        if (this.balanceType === ACCOUNTING_TYPE_ID.MATERIAL_GOODS_TYPE) {
            this.soDuDauKyService.getCheckHaveDataMG().subscribe(res => {
                if (res.body) {
                    this.refModel2 = this.modalService.open(updLoadForm, { size: 'lg', backdrop: 'static' });
                } else {
                    this.acceptedData();
                }
            });
        } else {
            this.soDuDauKyService.getCheckHaveData({ typeId: this.balanceType }).subscribe(res => {
                if (res.body) {
                    this.refModel2 = this.modalService.open(updLoadForm, { size: 'lg', backdrop: 'static' });
                } else {
                    this.acceptedData();
                }
            });
        }
    }

    acceptedData() {
        if (this.balanceType === ACCOUNTING_TYPE_ID.MATERIAL_GOODS_TYPE) {
            this.soDuDauKyService.acceptedOPMaterialGoods(this.opsData).subscribe(() => this.onAcceptedDataSuccess());
        } else {
            this.soDuDauKyService.acceptedOPAccount(this.opsData, this.balanceType).subscribe(() => this.onAcceptedDataSuccess());
        }
    }

    onAcceptedDataSuccess() {
        if (this.refModel) {
            this.refModel.close();
        }
        if (this.refModel2) {
            this.refModel2.close();
        }
        if (this.refModel3) {
            this.refModel3.close();
        }
        this.toastrService.success(this.translateService.instant('ebwebApp.saBill.upload.success'));
        this.accountListService.getAccountListsActiveAndOP().subscribe(resx => this.onSetupAccountList(resx));
    }

    upload(download: any, updLoadForm: any) {
        this.isEdit = false;
        if (this.balanceType === ACCOUNTING_TYPE_ID.MATERIAL_GOODS_TYPE) {
            this.soDuDauKyService.uploadMaterialGoods(this.file).subscribe(
                res => this.onUpdateRes(res, download, updLoadForm),
                err => {
                    this.isEdit = true;
                }
            );
        } else if (this.balanceType === ACCOUNTING_TYPE_ID.ACCOUNTING_OBJECT) {
            this.soDuDauKyService.uploadAccountingObject(this.file).subscribe(
                res => this.onUpdateRes(res, download, updLoadForm),
                err => {
                    this.isEdit = true;
                }
            );
        } else {
            this.soDuDauKyService.uploadAccountNormal(this.file).subscribe(
                res => this.onUpdateRes(res, download, updLoadForm),
                err => {
                    this.isEdit = true;
                }
            );
        }
    }
    chooseSheet(name: string, download: any, updLoadForm: any) {
        this.isEdit = false;
        if (this.refModel2) {
            this.refModel2.close();
        }
        if (this.balanceType === ACCOUNTING_TYPE_ID.MATERIAL_GOODS_TYPE) {
            this.soDuDauKyService.uploadMaterialGoods(this.file, name).subscribe(
                res => this.onUpdateRes(res, download, updLoadForm),
                err => {
                    this.isEdit = true;
                }
            );
        } else if (this.balanceType === ACCOUNTING_TYPE_ID.ACCOUNTING_OBJECT) {
            this.soDuDauKyService.uploadAccountingObject(this.file, name).subscribe(
                res => this.onUpdateRes(res, download, updLoadForm),
                err => {
                    this.isEdit = true;
                }
            );
        } else {
            this.soDuDauKyService.uploadAccountNormal(this.file, name).subscribe(
                res => this.onUpdateRes(res, download, updLoadForm),
                err => {
                    this.isEdit = true;
                }
            );
        }
    }

    onUpdateRes(res, download, updLoadForm) {
        this.isEdit = true;
        // this.file = null;
        if (res.headers.get('isError') === '1') {
            if (res.headers.get('message')) {
                if (res.headers.get('message') === ImportExcel.SELECT_SHEET) {
                    const blob = new Blob([res.body], { type: 'aapplication/json' });
                    const reader = new FileReader();
                    reader.onload = () => {
                        this.sheetNames = JSON.parse(reader.result);
                        this.refModel2 = this.modalService.open(this.chooseSheetModal, { size: 'lg', backdrop: 'static' });
                    };
                    reader.readAsBinaryString(blob);
                } else {
                    this.toastrService.error(this.translateService.instant('ebwebApp.saBill.upload.' + res.headers.get('message')));
                }
            } else {
                // this.refModel.close();
                this.refModel3 = this.modalService.open(download, { size: 'lg', backdrop: 'static' });
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);

                this.link = document.createElement('a');
                document.body.appendChild(this.link);
                this.link.download = fileURL;
                this.link.setAttribute('style', 'display: none');
                const name =
                    this.balanceType === ACCOUNTING_TYPE_ID.NORMAL_ACCOUNT
                        ? 'SoDuDauKy_TKThongThuong_Loi.xlsx'
                        : this.balanceType === ACCOUNTING_TYPE_ID.ACCOUNTING_OBJECT
                            ? 'SoDuDauKy_TKChiTietDoiTuong_Loi.xlsx'
                            : 'SoDuDauKy_TKChiTietVTHH_Loi.xlsx';
                this.link.setAttribute('download', name);
                this.link.href = fileURL;
            }
        } else {
            this.opsData = res.body;
            this.checkHaveData(download, updLoadForm);
        }
    }

    download() {
        this.link.click();
    }
    getEmptyRow() {
        const listDetail = this.openingBalanceDetails || [];
        return getEmptyRow(listDetail);
    }

    onChangeSearchAN() {
        this.accountList = this.accountListTemp
            .filter(x => {
                if (this.searchAccountNumber && this.searchAccountNumber !== '') {
                    return x.accountNumber.toLowerCase().includes(this.searchAccountNumber.toLowerCase());
                }
                return true;
            })
            .filter(x => {
                if (this.searchAccountName && this.searchAccountName !== '') {
                    return x.accountName.toLowerCase().includes(this.searchAccountName.toLowerCase());
                }
                return true;
            });
    }
}
