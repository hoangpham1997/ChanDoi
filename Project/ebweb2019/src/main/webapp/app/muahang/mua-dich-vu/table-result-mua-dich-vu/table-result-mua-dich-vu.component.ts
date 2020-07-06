import { AfterViewChecked, Component, HostListener, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { MuaDichVuService } from '../mua-dich-vu.service';
import { JhiEventManager } from 'ng-jhipster';
import { IMuaDichVuResult, MuaDichVuResult } from 'app/muahang/mua-dich-vu/model/mua-dich-vu-result.model';
import { Router } from '@angular/router';
import { IMuaDichVuSearch, MuaDichVuSearch } from 'app/muahang/mua-dich-vu/model/mua-dich-vu-search.model';
import { HttpResponse } from '@angular/common/http';
import { ONBROADCASTEVENT } from '../mua-dich-vu-event-name.constant';
import { ACCOUNT_TYPE, PP_SERVICE_TYPE, GEN_CODE_TYPE_GROUP, UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';
import { DATE_FORMAT_SLASH, ITEMS_PER_PAGE } from 'app/shared';
import * as moment from 'moment';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { Principal } from 'app/core';
import { BROADCAST_EVENT, GROUP_TYPEID, MAU_BO_GHI_SO, MSGERROR, SO_LAM_VIEC, TOOLTIPS } from 'app/app.constants';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { Irecord } from 'app/shared/model/record';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { TypeService } from 'app/entities/type';
import { IType } from 'app/shared/model/type.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { DataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { getEmptyRow } from 'app/shared/util/row-util';
import { Subscription } from 'rxjs';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { DialogDeleteComponent } from 'app/shared/modal/dialog-delete/dialog-delete.component';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { Location } from '@angular/common';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { IAReport } from 'app/shared/model/ia-report.model';
import { RequestRecordListDtoModel } from 'app/shared/model/request-record-list-dto.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';

@Component({
    selector: 'eb-mua-dich-vu-table-result',
    templateUrl: './table-result-mua-dich-vu.component.html',
    styleUrls: ['../mua-dich-vu.css', 'table-result-mua-dich-vu.component.css']
})
export class TableResultMuaDichVuComponent extends BaseComponent implements OnInit, AfterViewChecked, OnDestroy {
    @ViewChild('unRecordModal') unRecordModal: any;
    @ViewChild('checkHadPaidModal') checkHadPaidModal: any;
    @ViewChild('checkHadReferencePaidModal') checkHadReferencePaidModal: any;
    @ViewChild('popUpMultiActions') popUpMultiActions: TemplateRef<any>;
    muaDichVuResults: MuaDichVuResult[];
    ROLE = ROLE;
    TOOLTIPS = TOOLTIPS;
    predicate: any;
    havingRecord = false;
    havingUnRecord = false;
    previousPage: any;
    statusSearch: boolean;
    muaDichVuResult: MuaDichVuResult;
    muaDichVuSearch: IMuaDichVuSearch;
    modalRef: NgbModalRef;
    account: any;
    currentBook: string;
    isRecording: boolean;
    receiptTypes: IType[];
    currencies: ICurrency[];
    accountObjects: IAccountingObject[];
    dataSession: DataSessionStorage;
    isLoading: boolean;
    eventSubscriber: Subscription;
    statusRecord: any;
    MUA_DICH_VU = parseInt(PP_SERVICE_TYPE.PP_SERVICE_UNPAID, 0);
    totalResultAmountOriginal: number;
    isVNDFormat: boolean;
    loadDetails: boolean;
    indexScroll: number;
    isScroll: boolean;
    isScrollToTop: boolean;
    selectedRow: MuaDichVuResult;
    isCareerGroup: boolean;
    typeMultiAction: number;

    constructor(
        private muaDichVuService: MuaDichVuService,
        private refModalService: RefModalService,
        private eventManager: JhiEventManager,
        private router: Router,
        public utilsService: UtilsService,
        private principal: Principal,
        private toastrService: ToastrService,
        private translateService: TranslateService,
        private generalLedgerService: GeneralLedgerService,
        private typeService: TypeService,
        private currencyService: CurrencyService,
        private accountingObjectService: AccountingObjectService,
        private modalService: NgbModal,
        private location: Location
    ) {
        super();
    }

    ngOnInit(): void {
        this.onSetStatusRecord();
        this.statusSearch = this.muaDichVuService.getStatusSearchBar();
        this.dataSession = this.muaDichVuService.getDataSession();
        this.muaDichVuSearch = this.muaDichVuService.getMuaDichVuSearchSnapShot();
        this.isLoading = true;
        if (!this.dataSession.itemsPerPage) {
            this.dataSession.itemsPerPage = ITEMS_PER_PAGE;
            this.muaDichVuService.setDataSession(this.dataSession);
        }
        this.principal.identity().then(account => {
            // Set default theo systemOption
            this.account = account;
            this.currentBook = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
            for (let i = 0; i < this.account.systemOption.length; i++) {
                if (!this.account.systemOption[i].data) {
                    this.account.systemOption[i].data = this.account.systemOption[i].defaultData;
                }
            }
            this.isCareerGroup = true;
            if (this.account.organizationUnit.taxCalculationMethod === 1) {
                this.isCareerGroup = false;
            }
            this.color = this.account.systemOption.find(item => item.code === MAU_BO_GHI_SO).data;
            this.onBindPPServiceData();
        });

        this.onGetTypeName();
        this.onGetCurrencies();
        this.onGetAccountObjects();
        this.registerExport();
        this.registerLockSuccess();
        this.registerUnlockSuccess();
    }

    registerLockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('lockSuccess', response => {
            this.updateSearchValue();
        });
    }
    registerUnlockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('unlockSuccess', response => {
            this.updateSearchValue();
        });
    }

    onSetStatusRecord() {
        this.translateService
            .get(['ebwebApp.muaHang.muaDichVu.search.status.hasRecord', 'ebwebApp.muaHang.muaDichVu.search.status.nonRecord'])
            .subscribe((res: any) => {
                this.statusRecord = [
                    {
                        name: '',
                        value: null
                    },
                    {
                        name: res['ebwebApp.muaHang.muaDichVu.search.status.hasRecord'],
                        value: 1
                    },
                    {
                        name: res['ebwebApp.muaHang.muaDichVu.search.status.nonRecord'],
                        value: 0
                    }
                ];
            });
    }

    getVATRateValue(vatRate: number) {
        if (vatRate != null) {
            return (vatRate === 0 ? 0 : vatRate === 1 ? 5 : 10) + '%';
        }
        return '';
    }

    getEmptyRow() {
        const listDetail = this.muaDichVuResult && this.muaDichVuResult.ppServiceDetailDTOS ? this.muaDichVuResult.ppServiceDetailDTOS : [];
        return getEmptyRow(listDetail);
    }

    getEmptyRowResult() {
        const listDetail = this.muaDichVuResults || [];
        return getEmptyRow(listDetail);
    }

    updateStatusBar() {
        this.muaDichVuService.setStatusSearchBar(!this.statusSearch);
        this.statusSearch = this.muaDichVuService.getStatusSearchBar();
        if (this.statusSearch) {
            this.muaDichVuSearch = new MuaDichVuSearch();
        }
    }

    onGetTypeName() {
        this.receiptTypes = [];
        this.typeService.findAllTypesByGroupId({ groupId: GROUP_TYPEID.GROUP_PPSERVICE }).subscribe(res => {
            this.receiptTypes = res.body;
            this.receiptTypes.sort((a, b) => a.id - b.id);
        });
    }

    onGetAccountObjects() {
        this.accountingObjectService
            .getAccountingObjectDTOByTaskMethod({ taskMethod: 0 })
            .subscribe((res: HttpResponse<IAccountingObject[]>) => {
                this.accountObjects = res.body;
                this.accountObjects.sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
            });
    }

    onGetCurrencies() {
        this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencies = res.body;
            this.currencies.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
        });
    }

    getRequestFindAll(): any {
        return {
            page: this.dataSession.page ? this.dataSession.page - 1 : '',
            size: this.dataSession.itemsPerPage,
            noBookType: this.currentBook,
            receiptType: this.muaDichVuSearch.receiptType || '',
            toDate: this.muaDichVuSearch.toDate || '',
            fromDate: this.muaDichVuSearch.fromDate || '',
            hasRecord:
                this.muaDichVuSearch.hasRecord !== null && this.muaDichVuSearch.hasRecord !== undefined
                    ? this.muaDichVuSearch.hasRecord
                    : '',
            currencyID: this.muaDichVuSearch.currencyID || '',
            accountingObjectID: this.muaDichVuSearch.accountingObjectID || '',
            freeSearch: this.muaDichVuSearch.freeSearch || '',
            currentPPService: this.muaDichVuService.getPPServiceSelected().id || ''
        };
    }

    onBindPPServiceData(selectLastItem?, isResetSelected?) {
        this.muaDichVuResult = {};
        this.isVNDFormat = true;
        this.muaDichVuService.setMuaDichVuSearchSnapShot(this.getRequestFindAll());
        this.muaDichVuService.findAllPPServices(this.muaDichVuService.getMuaDichVuSearchSnapShot()).subscribe(res => {
            this.totalResultAmountOriginal = parseInt(res.headers.get('total-result-amount-original'), 0);
            this.muaDichVuResults = res.body.content;
            this.objects = this.muaDichVuResults;
            if (this.muaDichVuResults.length > 0) {
                if (res.body.last && this.muaDichVuResults[0]) {
                    this.muaDichVuResults[0].rowNum = 1;
                }
                if (res.body.first && this.muaDichVuResults[this.muaDichVuResults.length - 1]) {
                    this.muaDichVuResults[this.muaDichVuResults.length - 1].rowNum = -1;
                }
                if (isResetSelected) {
                    this.muaDichVuResult = this.muaDichVuResults[0];
                } else {
                    this.muaDichVuResult =
                        this.muaDichVuResults.find(x => x.id === this.muaDichVuService.getPPServiceSelected().id) ||
                        this.muaDichVuResults[selectLastItem ? this.muaDichVuResults.length - 1 : 0];
                }
                this.selectedRow = this.muaDichVuResult;
                this.selectedRows = [this.selectedRow];
                this.onSelect(this.muaDichVuResult);
                this.isScroll = true;
            }
            this.isLoading = false;
            this.dataSession.totalItems = res.body.totalElements;
            this.dataSession.page = res.body.number + 1;
        });
    }
    trackIdentity(index, item: IMuaDichVuResult) {
        return item.receiptDate;
    }

    selectedItemPerPage() {
        this.transition();
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.onBindPPServiceData();
    }

    previousEdit() {}

    nextEdit() {}

    save() {}

    saveAndNew() {}

    @ebAuth(['ROLE_ADMIN', ROLE.MuaDichVu_GhiSo])
    record() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = 2;
            this.modalRef = this.modalService.open(this.popUpMultiActions, { backdrop: 'static' });
            return;
        }
        if (this.muaDichVuResult && this.muaDichVuResult.recorded) {
            return;
        }
        const recordData = { id: this.muaDichVuResult.id, typeID: parseInt(PP_SERVICE_TYPE.PP_SERVICE_UNPAID, 0) };
        this.isRecording = true;
        this.generalLedgerService.record(recordData).subscribe((res: HttpResponse<Irecord>) => {
            if (res.body.success) {
                this.selectedRow.recorded = true;
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBTellerPaper.recordToast.done'),
                    this.translateService.instant('ebwebApp.mBTellerPaper.message.title')
                );
            } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                this.toastrService.error(
                    this.translateService.instant('global.messages.error.checkTonQuyQT'),
                    this.translateService.instant('ebwebApp.mCReceipt.error.error')
                );
            } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                this.toastrService.error(
                    this.translateService.instant('global.messages.error.checkTonQuyTC'),
                    this.translateService.instant('ebwebApp.mCReceipt.error.error')
                );
            } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY) {
                this.toastrService.error(
                    this.translateService.instant('global.messages.error.checkTonQuy'),
                    this.translateService.instant('ebwebApp.mCReceipt.error.error')
                );
            }
            this.isRecording = false;
        });
    }

    recordMultiRows() {
        const rq: RequestRecordListDtoModel = {};
        if (this.modalRef) {
            this.modalRef.close();
        }
        const typeID = parseInt(PP_SERVICE_TYPE.PP_SERVICE_UNPAID, 0);
        rq.records = this.selectedRows.map(n => {
            return { id: n.id, typeID: n.typeId };
        });
        rq.typeIDMain = typeID;
        this.generalLedgerService.recordList(rq).subscribe((res: HttpResponse<HandlingResult>) => {
            this.selectedRows.forEach(slr => {
                const update = this.muaDichVuResults.find(n => n.id === slr.id && !res.body.listFail.some(m => m.id === slr.id));
                if (update) {
                    update.recorded = true;
                }
            });
            if (res.body.countFailVouchers > 0) {
                this.modalRef = this.refModalService.open(
                    res.body,
                    HandlingResultComponent,
                    null,
                    false,
                    null,
                    null,
                    null,
                    null,
                    null,
                    true
                );
            } else {
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBCreditCard.recordSuccess'),
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
            }
            this.onBindPPServiceData();
        });
    }

    unRecord() {
        if (this.selectedRows.length > 1) {
            this.unRecordMultiRows();
            return;
        }
        if (!this.muaDichVuResult.recorded || this.checkCloseBook(this.account, this.selectedRow.postedDate)) {
            return;
        }

        this.muaDichVuService.checkHadReference({ ppServiceId: this.muaDichVuResult.id }).subscribe(res => {
            if (res.body.messages === UpdateDataMessages.HAD_REFERENCE_AND_PAID) {
                this.modalRef = this.modalService.open(this.checkHadReferencePaidModal, { backdrop: 'static' });
            } else if (res.body.messages === UpdateDataMessages.HAD_REFERENCE) {
                this.modalRef = this.modalService.open(this.unRecordModal, { backdrop: 'static' });
            } else if (res.body.messages === UpdateDataMessages.HAD_PAID) {
                this.modalRef = this.modalService.open(this.checkHadPaidModal, { backdrop: 'static' });
            } else {
                this.unRecording();
            }
        });
    }

    unRecordMultiRows() {
        this.typeMultiAction = 1;
        this.modalRef = this.modalService.open(this.popUpMultiActions, { backdrop: 'static' });
    }

    unRecording() {
        const recordData = { id: this.muaDichVuResult.id, typeID: parseInt(PP_SERVICE_TYPE.PP_SERVICE_UNPAID, 0) };
        this.isRecording = true;
        this.generalLedgerService.unrecord(recordData).subscribe((res: HttpResponse<Irecord>) => {
            if (res.body.success === true) {
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBTellerPaper.unrecordToast.done'),
                    this.translateService.instant('ebwebApp.mBTellerPaper.message.title')
                );
                this.muaDichVuResult.recorded = false;
            } else {
                this.toastrService.error(
                    this.translateService.instant('ebwebApp.mBTellerPaper.unrecordToast.fail'),
                    this.translateService.instant('ebwebApp.mBTellerPaper.error.error')
                );
            }
            if (this.modalRef) {
                this.modalRef.close();
            }
            this.isRecording = false;
        });
    }

    exportPdf() {
        this.muaDichVuService.export(this.getRequestFindAll()).subscribe(res => {
            this.refModalService.open(null, EbReportPdfPopupComponent, res, false, this.MUA_DICH_VU);
        });
    }

    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${this.MUA_DICH_VU}`, () => {
            this.exportExcel();
        });
    }

    exportExcel() {
        this.muaDichVuService.exportExcel(this.getRequestFindAll()).subscribe(res => {
            const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
            const fileURL = URL.createObjectURL(blob);

            const link = document.createElement('a');
            document.body.appendChild(link);
            link.download = fileURL;
            link.setAttribute('style', 'display: none');
            const name = 'DS_MuaDichVu.xls';
            link.setAttribute('download', name);
            link.href = fileURL;
            link.click();
        });
    }

    copyAndNew() {}

    previousState() {}

    getCurrentDate() {
        return this.muaDichVuService.getCurrentDate();
    }

    getFromToMomentStr(date?: string, isMaxDate?: boolean) {
        return this.muaDichVuService.getFromToMoment(moment(date, 'DD/MM/YYYY'), isMaxDate);
    }

    getFormat(ppService: MuaDichVuResult) {
        return this.account.organizationUnit.currencyID === ppService.currencyID;
    }

    onDisableUserSelected() {
        if (this.selectedRows.length > 1) {
            this.eventManager.broadcast({
                name: BROADCAST_EVENT.DISABLE_USER_SELECT,
                content: true
            });
        } else {
            this.eventManager.broadcast({
                name: BROADCAST_EVENT.DISABLE_USER_SELECT,
                content: false
            });
        }
    }

    onSelect(ppService: MuaDichVuResult) {
        event.preventDefault();
        this.havingRecord = false;
        this.havingUnRecord = false;
        this.onDisableUserSelected();
        if (this.selectedRows.length > 1) {
            for (let i = 0; i < this.selectedRows.length; i++) {
                if (this.selectedRows[i].recorded) {
                    this.havingRecord = true;
                } else {
                    this.havingUnRecord = true;
                }
            }
        }
        this.indexScroll = null;
        this.muaDichVuResult = ppService;
        this.isVNDFormat = this.muaDichVuResult.currencyID
            ? this.account.organizationUnit.currencyID === this.muaDichVuResult.currencyID
            : true;

        ppService.totalquantity = 0;
        if (this.muaDichVuResult && this.muaDichVuResult.id) {
            this.muaDichVuService.findAllPPServiceDetails({ ppServiceId: ppService.id }).subscribe(resDetail => {
                ppService.ppServiceDetailDTOS = resDetail.body ? resDetail.body : [];
                //
                this.loadDetails = false;
                ppService.ppServiceDetailDTOS.forEach(detail => (detail.quantityFromDB = detail.quantity));
                for (let i = 0; i < ppService.ppServiceDetailDTOS.length; i++) {
                    ppService.totalquantity += ppService.ppServiceDetailDTOS[i].quantity;
                }
            });
            this.muaDichVuService
                .findRefVoucherByRefId({ refId: ppService.id, currentBook: this.currentBook })
                .subscribe(resDetail => (ppService.refVouchers = resDetail.body ? resDetail.body : []));
        }
    }

    openModal(content) {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.modalRef = this.modalService.open(content, { backdrop: 'static' });
    }

    onMultiDelete() {
        this.muaDichVuService.deletePPServiceInId(this.selectedRows.filter(x => !x.recorded).map(x => x.id)).subscribe(res => {
            const ppServiceNotRecord = this.selectedRows.filter(x => x.recorded);
            if (this.modalRef) {
                this.modalRef.close();
            }
            if (ppServiceNotRecord && ppServiceNotRecord.length > 0) {
                this.modalRef = this.refModalService.open(
                    {
                        listFail: ppServiceNotRecord.map(x => {
                            x.date = moment(x.receiptDate, DATE_FORMAT_SLASH);
                            x.reasonFail = this.translateService.instant('global.data.warningRecorded');
                            return x;
                        }),
                        countTotalVouchers: this.selectedRows.length,
                        countSuccessVouchers: this.selectedRows.length - ppServiceNotRecord.length,
                        countFailVouchers: ppServiceNotRecord.length
                    },
                    HandlingResultComponent,
                    null,
                    false,
                    null,
                    null,
                    null,
                    null,
                    null,
                    true
                );
            }
            this.toastrService.success(this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.deletedSuccess'));
            this.onBindPPServiceData();
        });
        return;
    }

    removePPService() {
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = 0;
            this.modalRef = this.modalService.open(this.popUpMultiActions, { backdrop: 'static' });
            return;
        }
        if (this.muaDichVuResult && !this.checkCloseBook(this.account, this.selectedRow.postedDate)) {
            this.modalRef = this.refModalService.open(
                null,
                DialogDeleteComponent,
                {
                    id: this.muaDichVuResult.id,
                    confirmDelete: id => {
                        this.muaDichVuService.deletePPServiceById({ id }).subscribe(response => {
                            if (response.body.messages === UpdateDataMessages.DELETE_SUCCESS) {
                                this.onBindPPServiceData();
                            } else if (response.body.messages === UpdateDataMessages.HAD_REFERENCE) {
                                this.toastrService.error(
                                    this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.hasReferenceSuccess')
                                );
                            }
                        });
                    }
                },
                null,
                null,
                true
            );
        }
    }

    doubleClickRow(ppService: MuaDichVuResult) {
        if (this.selectedRows.length > 1) {
            return;
        }
        event.preventDefault();
        this.muaDichVuService.setPPServiceList(this.muaDichVuResults);
        this.muaDichVuService.setPPServiceSelected(ppService);
        this.muaDichVuService.setReadOnly(true);
        this.muaDichVuService.setEdit(false);
        this.eventManager.broadcast({
            name: ONBROADCASTEVENT.UPDATE_VIEW_TYPE_MUA_DICH_VU,
            content: 1
        });
        this.location.go(`/mua-dich-vu/${ppService.id}/edit/pp-service`);
    }

    onUpdateViewType() {
        const ppService = new MuaDichVuResult();
        ppService.reason = 'Mua dịch vụ';
        ppService.otherReason = ppService.reason;
        this.muaDichVuService.setPPServiceSelected(ppService);
        this.muaDichVuService.setReadOnly(false);
        this.eventManager.broadcast({
            name: ONBROADCASTEVENT.UPDATE_VIEW_TYPE_MUA_DICH_VU,
            content: 1
        });
        this.location.go(`/mua-dich-vu/new`);
    }

    resetSearch() {
        this.muaDichVuSearch = new MuaDichVuSearch();
        this.updateSearchValue();
        this.onBindPPServiceData();
    }

    updateSearchValue() {
        this.muaDichVuService.setMuaDichVuSearchSnapShot(this.muaDichVuSearch);
        // this.isVNDFormat = this.account.organizationUnit.currencyID === this.muaDichVuSearch.currencyID;
    }

    canDeactive() {
        super.canDeactive();
    }

    /**
     * base event
     */
    toggleSearch(event) {
        event.preventDefault();
        this.updateStatusBar();
    }

    @ebAuth(['ROLE_ADMIN', ROLE.MuaDichVu_Them])
    addNew($event) {
        event.preventDefault();
        this.onUpdateViewType();
    }
    @ebAuth(['ROLE_ADMIN', ROLE.MuaDichVu_Sua])
    edit() {
        event.preventDefault();
        if (!this.muaDichVuResult.recorded) {
            this.doubleClickRow(this.muaDichVuResult);
            this.muaDichVuService.setEdit(true);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.MuaDichVu_KetXuat])
    export() {
        event.preventDefault();
        this.exportPdf();
    }

    @ebAuth(['ROLE_ADMIN', ROLE.MuaDichVu_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRows.length === 1) {
            if (!this.muaDichVuResult || this.muaDichVuResult.recorded) {
                return;
            }
        }
        this.removePPService();
    }

    @ebAuth(['ROLE_ADMIN', ROLE.MuaDichVu_GhiSo])
    unrecord() {
        event.preventDefault();
        if (!this.muaDichVuResult && !this.muaDichVuResult.recorded) {
            return;
        }
        this.unRecord();
    }

    arrowUp() {
        event.preventDefault();
        this.selectedRows = [];
        // trong lúc loading detail thì không cho thao tác 2 nút mũi tên
        if (this.loadDetails) {
            return;
        }
        // check phải có item selected
        if (this.muaDichVuResult) {
            // Tìm tra vị trí của item hiện  tại
            const index = this.muaDichVuResults.findIndex(x => x.id === this.muaDichVuResult.id);
            // Nếu là item đầu danh sách thì load trang trươc
            if (index === 0) {
                // Nếu là trang đầu thì return
                if (this.dataSession.page - 1 === 0) {
                    return;
                }
                this.loadDetails = true;
                // Set trang về trang trước
                this.dataSession.page = this.dataSession.page - 1;
                // Load dữ liệu trang trước
                this.onBindPPServiceData(true);
                return;
            } else {
                this.loadDetails = true;
                this.onSelect(this.muaDichVuResults[index - 1]);
            }
        }
    }
    arrowDown() {
        event.preventDefault();
        this.selectedRows = [];
        if (this.loadDetails) {
            return;
        }
        if (this.muaDichVuResult) {
            this.loadDetails = true;
            const index = this.muaDichVuResults.findIndex(x => x.id === this.muaDichVuResult.id);
            // Nếu là chứng từ cuối cùng return luôn
            if (
                this.dataSession.totalItems -
                    parseInt(this.dataSession.itemsPerPage.toString(), 0) * (this.dataSession.page - 1) -
                    index ===
                1
            ) {
                return;
            }
            // Nếu index là phần tử cuối của list tại màn
            // Méo hiểu sai itemPerPage thành String r ^_^
            if ((index + 1).toString() === this.dataSession.itemsPerPage.toString()) {
                this.dataSession.page = this.dataSession.page ? this.dataSession.page + 1 : 1;
                this.isScrollToTop = true;
                this.onBindPPServiceData();
                return;
            } else {
                this.onSelect(this.muaDichVuResults[index + 1]);
            }
        }
    }

    enter() {
        if (this.muaDichVuResult && this.selectedRows.length === 1) {
            this.doubleClickRow(this.muaDichVuResult);
        }
    }

    ngAfterViewChecked(): void {
        if (this.isScroll) {
            this.scrollItemSelected(null, this.isScrollToTop ? 'UP' : 'DOWN');
            this.isScroll = false;
            this.isScrollToTop = false;
        }
    }
    ngOnDestroy(): void {
        this.eventManager.broadcast({
            name: BROADCAST_EVENT.DISABLE_USER_SELECT,
            content: false
        });
    }

    continueMultiAction() {
        if (this.typeMultiAction === 0) {
            this.onMultiDelete();
        } else if (this.typeMultiAction === 1) {
            const rq: RequestRecordListDtoModel = {};
            if (this.modalRef) {
                this.modalRef.close();
            }
            const typeID = parseInt(PP_SERVICE_TYPE.PP_SERVICE_UNPAID, 0);
            rq.records = this.selectedRows.filter(x => !this.checkCloseBook(this.account, x.postedDate)).map(n => {
                return { id: n.id, typeID: n.typeId };
            });
            rq.typeIDMain = typeID;
            this.generalLedgerService.unRecordList(rq).subscribe((res: HttpResponse<HandlingResult>) => {
                const closeBookList = this.selectedRows.filter(x => this.checkCloseBook(this.account, x.postedDate)).map(x => {
                    x.reasonFail = 'Chứng từ đã khóa sổ!';
                    x.date = moment(x.receiptDate, DATE_FORMAT_SLASH);
                    return x;
                });
                const unRecordList = this.selectedRows
                    .filter(x => !this.checkCloseBook(this.account, x.postedDate))
                    .filter(x => !x.recorded)
                    .map(x => {
                        x.reasonFail = 'Chứng từ đang bỏ ghi sổ !';
                        x.date = moment(x.receiptDate, DATE_FORMAT_SLASH);
                        return x;
                    });
                const deleteList = this.selectedRows.filter(x => !this.checkCloseBook(this.account, x.postedDate)).filter(x => x.recorded);
                const result: HandlingResult = new HandlingResult();
                result.countTotalVouchers = this.selectedRows.length;
                result.countFailVouchers = closeBookList.length + unRecordList.length;
                result.countSuccessVouchers = deleteList.length;
                result.listFail = [...closeBookList, ...unRecordList];
                if (result.countFailVouchers > 0) {
                    this.modalRef = this.refModalService.open(
                        result,
                        HandlingResultComponent,
                        null,
                        false,
                        null,
                        null,
                        null,
                        null,
                        null,
                        true
                    );
                } else {
                    this.toastrService.success(
                        this.translateService.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                        this.translateService.instant('ebwebApp.mBDeposit.message')
                    );
                }
                this.onBindPPServiceData();
            });
        } else {
            this.recordMultiRows();
        }
    }

    closePopUp() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }
}
