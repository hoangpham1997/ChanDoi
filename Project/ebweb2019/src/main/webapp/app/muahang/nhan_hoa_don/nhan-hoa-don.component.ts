import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { Principal } from 'app/core';

import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { NhanHoaDonService } from './nhan-hoa-don.service';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { DatePipe } from '@angular/common';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { TranslateService } from '@ngx-translate/core';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { ToastrService } from 'ngx-toastr';
import * as moment from 'moment';
import { Moment } from 'moment';
import { IPPInvoice } from 'app/shared/model/pp-invoice.model';
import { IPPInvoiceDetails } from 'app/shared/model/pp-invoice-details.model';
import { IAccountDefault } from 'app/shared/model/account-default.model';
import { AccountDefaultService } from 'app/danhmuc/account-default';
import { PPInvoiceDetailsService } from 'app/entities/nhan-hoa-don/pp-invoice-details.service';
import { IReceiveBill } from 'app/shared/model/receive-bill.model';
import { IGoodsServicePurchase } from 'app/shared/model/goods-service-purchase.model';
import { GoodsServicePurchaseService } from 'app/entities/goods-service-purchase';
import { AccountListService } from 'app/danhmuc/account-list';
import { IAccountList } from 'app/shared/model/account-list.model';
import { PPInvoiceService } from 'app/muahang/mua_hang_qua_kho';
import { SO_LAM_VIEC, TCKHAC_SDSOQUANTRI } from 'app/app.constants';
import { ROLE } from 'app/role.constants';

@Component({
    selector: 'eb-nhan-hoa-don',
    templateUrl: './nhan-hoa-don.component.html',
    styleUrls: ['./nhan-hoa-don.component.css'],
    providers: [DatePipe]
})
export class NhanHoaDonComponent implements OnInit, AfterViewInit {
    currentAccount: any;
    isSaving: boolean;
    pPInvoices: IPPInvoice[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    selectedRow?: any;
    accountingObjects: IAccountingObject[];
    timeLine: any;
    type: any;
    formality: any;
    accountingObjectCode: any;
    dtBeginDate: Moment;
    dtEndDate: Moment;
    listTimeLine: any;
    isFormalityReadOnly: Boolean;
    objSearch: ISearchVoucher;
    receiveBill: IReceiveBill;
    pPInvoice: IPPInvoice[];
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    listIDPP: string[];
    dataAfterSearch: string;
    receiveBills: IReceiveBill[];
    goodsServicePurchases: IGoodsServicePurchase[];
    allCheck: boolean;
    isOnSelect: boolean;
    listType: [{ name: string | any; value: number }, { name: string | any; value: number }];
    listColumnsType: string[] = ['name'];
    listHeaderColumnsType: string[] = ['Loại'];
    accountList: any;
    goodServicePurchaseID: any;
    goodsID: any;
    accountIsParentNode: any;
    accountIsNotParentNode: any;
    isParentNode: boolean;
    TCKHAC_SDSoQuanTri: any;
    isSoTaiChinh: boolean;
    dblClickRow: IReceiveBill;
    listVAT: any[];
    // navigate update form
    dataSession: IDataSessionStorage = new class implements IDataSessionStorage {
        accountingObjectName: string;
        itemsPerPage: number;
        page: number;
        pageCount: number;
        predicate: number;
        reverse: number;
        rowNum: number;
        totalItems: number;
    }();
    ROLE_NhanHoaDon_Sua = ROLE.NhanHoaDon_Sua;

    constructor(
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private datepipe: DatePipe,
        private gLService: GeneralLedgerService,
        public utilsService: UtilsService,
        private accountingObjectService: AccountingObjectService,
        private toastr: ToastrService,
        private translate: TranslateService,
        private nhanHoaDonService: NhanHoaDonService,
        private ppInvoiceDetailService: PPInvoiceDetailsService,
        private accountDefaultsService: AccountDefaultService,
        private accountListService: AccountListService,
        private goodsServicePurchaseService: GoodsServicePurchaseService,
        private pPInvoiceService: PPInvoiceService
    ) {
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.TCKHAC_SDSoQuanTri = this.currentAccount.systemOption.find(x => x.code === TCKHAC_SDSOQUANTRI).data;
            if (account) {
                if (this.TCKHAC_SDSoQuanTri === '1') {
                    this.isSoTaiChinh = this.currentAccount.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                } else {
                    this.isSoTaiChinh = true;
                }
            }
            this.utilsService.getCbbTimeLine2().then(data => {
                this.listTimeLine = data;
                this.timeLine = this.listTimeLine[4].value;
                this.selectChangeBeginDateAndEndDate(this.timeLine);
            });
            this.goodServicePurchaseID = this.currentAccount.organizationUnit.goodsServicePurchaseID;
            this.goodsServicePurchaseService.getGoodServicePurchases().subscribe((res: HttpResponse<IGoodsServicePurchase[]>) => {
                this.goodsServicePurchases = res.body;
                this.goodsID = this.goodsServicePurchases.find(goodsID => goodsID.id === this.goodServicePurchaseID);
            });
            this.translate.get(['ebwebApp.receiveBill.proofOfPurchase', 'ebwebApp.receiveBill.proofOfServicePurchase']).subscribe(res => {
                this.listType = [
                    { value: 1, name: res['ebwebApp.receiveBill.proofOfPurchase'] },
                    { value: 2, name: res['ebwebApp.receiveBill.proofOfServicePurchase'] }
                ];
            });
        });
    }

    search() {
        this.dataAfterSearch = JSON.stringify(this.saveSearchVoucher());
        if (!this.objSearch.accountingObjectID) {
            this.toastr.error(
                this.translate.instant('ebwebApp.receiveBill.notInsertNull'),
                this.translate.instant('ebwebApp.receiveBill.message')
            );
            return;
        }
        if (!this.dtBeginDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.receiveBill.notInsertNull'),
                this.translate.instant('ebwebApp.receiveBill.message')
            );
            return;
        }
        if (!this.dtEndDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.receiveBill.notInsertNull'),
                this.translate.instant('ebwebApp.receiveBill.message')
            );
            return;
        }
        if (!this.type) {
            this.nhanHoaDonService
                .findAll({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort(),
                    searchVoucher: JSON.stringify(this.saveSearchVoucher())
                })
                .subscribe((res: HttpResponse<IReceiveBill[]>) => {
                    this.receiveBills = res.body;
                    this.paginatePPInvoicesTemp(res.body, res.headers);
                });
        } else if (this.type === 1) {
            this.nhanHoaDonService
                .findAllPPInvoices({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort(),
                    searchVoucher: JSON.stringify(this.saveSearchVoucher()),
                    formality: this.formality
                })
                .subscribe((res: HttpResponse<IReceiveBill[]>) => {
                    this.receiveBills = res.body;
                    this.paginatePPInvoicesTemp(res.body, res.headers);
                });
        } else {
            this.nhanHoaDonService
                .findAllPPServices({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort(),
                    searchVoucher: JSON.stringify(this.saveSearchVoucher()),
                    formality: null
                })
                .subscribe((res: HttpResponse<IReceiveBill[]>) => {
                    this.receiveBills = res.body;
                    this.paginatePPInvoicesTemp(res.body, res.headers);
                });
        }
    }

    private paginatePPInvoicesTemp(data: IReceiveBill[], headers: HttpHeaders) {
        this.paginatePPInvoices(data, headers);
        if (this.receiveBills.length > 0) {
            this.toastr.success(
                this.translate.instant('ebwebApp.pPInvoice.found') +
                    this.receiveBills.length +
                    ' ' +
                    this.translate.instant('ebwebApp.pPInvoice.record'),
                this.translate.instant('ebwebApp.pPInvoice.message')
            );
        } else {
            this.toastr.warning(
                this.translate.instant('ebwebApp.pPInvoice.notFoundRecord'),
                this.translate.instant('ebwebApp.pPInvoice.message')
            );
        }
    }

    ngOnInit() {
        this.objSearch = new class implements ISearchVoucher {
            accountingObjectID: string;
            currencyID: string;
            fromDate: moment.Moment;
            statusRecorded: boolean;
            textSearch: string;
            toDate: moment.Moment;
            typeID: number;
        }();
        this.listVAT = [{ name: '0%', data: 0 }, { name: '5%', data: 1 }, { name: '10%', data: 2 }];
        this.isParentNode = true;
        this.allCheck = false;
        this.dataAfterSearch = '';
        this.accountList = [];
        this.receiveBill = {};
        // this.loadAll();
        this.selectedRow = {};
        this.objTimeLine = {};
        this.type = '';
        this.formality = null;
        this.isOnSelect = false;
        this.isFormalityReadOnly = false;
        this.accountingObjectService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.accountingObjects = res.body
                .filter(
                    accountingObject =>
                        accountingObject.isActive && (accountingObject.objectType === 0 || accountingObject.objectType === 2)
                )
                .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
        });
        this.accountListService.getAccountLike133().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.accountList = res.body.sort((a, b) => a.accountNumber.localeCompare(b.accountNumber));
        });
    }

    selectChangeBeginDateAndEndDate(intTimeLine: String) {
        this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
        if (this.objTimeLine) {
            this.dtBeginDate = moment(this.objTimeLine.dtBeginDate);
            this.dtEndDate = moment(this.objTimeLine.dtEndDate);
        }
    }

    selectChangeType() {
        if (this.type === 2) {
            this.formality = null;
            this.isFormalityReadOnly = false;
        } else {
            this.formality = 0;
            this.isFormalityReadOnly = true;
        }
    }

    sort() {
        const result = ['date' + ',' + (this.reverse ? 'asc' : 'desc')];
        result.push('postedDate' + ',' + (this.reverse ? 'asc' : 'desc'));
        result.push('noFBook' + ',' + (this.reverse ? 'asc' : 'desc'));
        // const result = ['date, desc', 'postedDate, desc'];
        return result;
    }

    private paginatePPInvoices(data: IReceiveBill[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.receiveBills = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    onSelect() {
        this.isOnSelect = true;
        if (this.goodsID) {
            this.receiveBill.goodsServicePurchaseID = this.selectedRow.goodsServicePurchaseID
                ? this.selectedRow.goodsServicePurchaseID
                : this.goodServicePurchaseID;
        } else {
            this.receiveBill.goodsServicePurchaseID = this.selectedRow.goodsServicePurchaseID
                ? this.selectedRow.goodsServicePurchaseID
                : null;
        }
        this.receiveBill.accountingObjectID = this.objSearch.accountingObjectID ? this.objSearch.accountingObjectID : null;
        this.receiveBill.listIDPPDetail = [];
        this.listIDPP = [];
    }

    doubleClickRow(id: string) {}

    saveSearchVoucher(): ISearchVoucher {
        const searchObj: ISearchVoucher = {};
        searchObj.typeID = this.objSearch.typeID !== undefined ? this.objSearch.typeID : null;
        searchObj.statusRecorded = this.objSearch.statusRecorded !== undefined ? this.objSearch.statusRecorded : null;
        searchObj.currencyID = this.objSearch.currencyID !== undefined ? this.objSearch.currencyID : null;
        searchObj.fromDate = this.dtBeginDate !== undefined ? this.dtBeginDate : null;
        searchObj.toDate = this.dtEndDate !== undefined ? this.dtEndDate : null;
        searchObj.accountingObjectID = this.objSearch.accountingObjectID !== undefined ? this.objSearch.accountingObjectID : null;
        searchObj.textSearch = this.objSearch.textSearch !== undefined ? this.objSearch.textSearch : null;
        return this.convertDateFromClient(searchObj);
    }

    private convertDateFromClient(searchVoucher: ISearchVoucher): ISearchVoucher {
        const copy: ISearchVoucher = Object.assign({}, searchVoucher, {
            fromDate:
                searchVoucher.fromDate != null && searchVoucher.fromDate.isValid() ? searchVoucher.fromDate.format(DATE_FORMAT) : null,
            toDate: searchVoucher.toDate != null && searchVoucher.toDate.isValid() ? searchVoucher.toDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    save() {
        this.receiveBill.listIDPPDetail = [];
        this.listIDPP = [];
        this.isSaving = true;
        if (!this.type) {
            if (this.receiveBills) {
                for (let i = 0; i < this.receiveBills.length; i++) {
                    if (this.receiveBills[i].isSelected) {
                        this.receiveBill.listIDPPDetail.push(this.receiveBills[i].pPID);
                        this.listIDPP.push(this.receiveBills[i].id);
                    }
                }
                this.receiveBill.listIDPP = this.listIDPP;
            } else {
                this.toastr.warning(
                    this.translate.instant('ebwebApp.receiveBill.notHaveDataUpdate'),
                    this.translate.instant('ebwebApp.receiveBill.message')
                );
                return;
            }
            if (this.receiveBill.listIDPPDetail.length > 0) {
                if (this.checkError()) {
                    this.subscribeToSaveResponse(this.nhanHoaDonService.updateAll(this.receiveBill));
                }
            } else {
                this.toastr.warning(
                    this.translate.instant('ebwebApp.receiveBill.noLineSelected'),
                    this.translate.instant('ebwebApp.receiveBill.message')
                );
                return;
            }
        } else if (this.type === 1) {
            for (let i = 0; i < this.receiveBills.length; i++) {
                if (this.receiveBills[i].isSelected) {
                    this.receiveBill.listIDPPDetail.push(this.receiveBills[i].pPID);
                    this.listIDPP.push(this.receiveBills[i].id);
                }
            }
            if (this.receiveBill.listIDPPDetail.length > 0) {
                if (this.checkError()) {
                    this.subscribeToSaveResponse(this.nhanHoaDonService.updatePPInvoiceDetails(this.receiveBill));
                }
            } else {
                this.toastr.warning(
                    this.translate.instant('ebwebApp.receiveBill.noLineSelected'),
                    this.translate.instant('ebwebApp.receiveBill.message')
                );
                return;
            }
        } else {
            for (let i = 0; i < this.receiveBills.length; i++) {
                if (this.receiveBills[i].isSelected) {
                    this.receiveBill.listIDPPDetail.push(this.receiveBills[i].pPID);
                    this.listIDPP.push(this.receiveBills[i].id);
                }
            }
            if (this.receiveBill.listIDPPDetail.length > 0) {
                if (this.checkError()) {
                    this.subscribeToSaveResponse(this.nhanHoaDonService.updatePPServiceDetails(this.receiveBill));
                }
            } else {
                this.toastr.warning(
                    this.translate.instant('ebwebApp.receiveBill.noLineSelected'),
                    this.translate.instant('ebwebApp.receiveBill.message')
                );
                return;
            }
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IReceiveBill>>) {
        if (this.type === 1) {
            result.subscribe(
                (res: HttpResponse<IReceiveBill>) => {
                    this.onSaveSuccessPPInvoice();
                    this.receiveBill = {};
                    this.isOnSelect = false;
                },
                (res: HttpErrorResponse) => this.onSaveError()
            );
        } else if (this.type === 2) {
            result.subscribe(
                (res: HttpResponse<IReceiveBill>) => {
                    this.onSaveSuccessPPService();
                    this.receiveBill = {};
                    this.isOnSelect = false;
                },
                (res: HttpErrorResponse) => this.onSaveError()
            );
        } else {
            result.subscribe(
                (res: HttpResponse<IReceiveBill>) => {
                    this.onSaveSuccessAll();
                    this.receiveBill = {};
                    this.isOnSelect = false;
                },
                (res: HttpErrorResponse) => this.onSaveError()
            );
        }
    }

    private onSaveSuccessPPInvoice() {
        this.nhanHoaDonService.updatePPInvoices(this.listIDPP).subscribe((res2: HttpResponse<string[]>) =>
            this.nhanHoaDonService
                .findAllPPInvoices({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort(),
                    searchVoucher: this.dataAfterSearch,
                    formality: this.formality
                })
                .subscribe((res: HttpResponse<IReceiveBill[]>) => {
                    this.receiveBills = res.body;
                    this.paginatePPInvoices(res.body, res.headers);
                })
        );
        this.toastr.success(
            this.translate.instant('ebwebApp.receiveBill.receiveBillSuccess'),
            this.translate.instant('ebwebApp.receiveBill.message')
        );
        this.isSaving = false;
    }

    checkError() {
        if (!this.receiveBill.invoiceNo) {
            this.toastr.warning(
                this.translate.instant('ebwebApp.receiveBill.invoiceNoNotNull'),
                this.translate.instant('ebwebApp.receiveBill.message')
            );
            return false;
        }
        if (!this.receiveBill.invoiceDate) {
            this.toastr.warning(
                this.translate.instant('ebwebApp.receiveBill.invoiceDateNotNull'),
                this.translate.instant('ebwebApp.receiveBill.message')
            );
            return false;
        }
        return true;
    }

    private onSaveSuccessPPService() {
        this.nhanHoaDonService.updatePPServices(this.listIDPP).subscribe((res2: HttpResponse<string[]>) =>
            this.nhanHoaDonService
                .findAllPPServices({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort(),
                    searchVoucher: this.dataAfterSearch,
                    formality: null
                })
                .subscribe((res: HttpResponse<IReceiveBill[]>) => {
                    this.receiveBills = res.body;
                    this.paginatePPInvoices(res.body, res.headers);
                })
        );
        this.toastr.success(
            this.translate.instant('ebwebApp.receiveBill.receiveBillSuccess'),
            this.translate.instant('ebwebApp.receiveBill.message')
        );
        this.isSaving = false;
    }

    private onSaveSuccessAll() {
        this.nhanHoaDonService
            .findAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                searchVoucher: JSON.stringify(this.saveSearchVoucher())
            })
            .subscribe((res: HttpResponse<IReceiveBill[]>) => {
                this.receiveBills = res.body;
                this.paginatePPInvoices(res.body, res.headers);
            });
        this.toastr.success(
            this.translate.instant('ebwebApp.receiveBill.receiveBillSuccess'),
            this.translate.instant('ebwebApp.receiveBill.message')
        );
        this.isSaving = false;
    }

    private onSaveError() {
        this.isSaving = false;
        this.toastr.error(this.translate.instant('ebwebApp.receiveBill.error'), this.translate.instant('ebwebApp.receiveBill.message'));
    }

    checkAll() {
        if (this.receiveBills) {
            this.allCheck = !this.allCheck;
            for (let i = 0; i < this.receiveBills.length; i++) {
                this.receiveBills[i].isSelected = this.allCheck;
            }
            this.onSelect();
        }
    }

    onDoubleClick(select: IReceiveBill) {
        this.dblClickRow = select;
        if (this.dblClickRow.typeID === 210) {
            this.router.navigate(['./pp-invoice', this.dblClickRow.id, 'edit', 'receiveBill']);
        } else if ([240, 241, 242, 243, 244, 245].includes(this.dblClickRow.typeID)) {
            this.router.navigate(['./mua-dich-vu', this.dblClickRow.id, 'edit', 'receiveBill']);
        }
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    ngAfterViewInit(): void {}
}
