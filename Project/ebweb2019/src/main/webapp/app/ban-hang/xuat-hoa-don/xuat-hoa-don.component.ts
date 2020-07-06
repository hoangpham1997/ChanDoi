import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { DatePipe } from '@angular/common';
import { getEmptyRow } from 'app/shared/util/row-util';
import { SaBillService } from 'app/ban-hang/xuat-hoa-don/sa-bill.service';
import { SaBill } from 'app/shared/model/sa-bill.model';
import { SaBillDetails } from 'app/shared/model/sa-bill-details.model';
import { AccountingObject } from 'app/shared/model/accounting-object.model';
import * as moment from 'moment';
import { Moment } from 'moment';
import { SaBillDetailsService } from 'app/ban-hang/xuat-hoa-don/sa-bill-details.service';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs';
import { Principal } from 'app/core';
import { DomSanitizer } from '@angular/platform-browser';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { IaPublishInvoiceDetails } from 'app/shared/model/ia-publish-invoice-details.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { TCKHAC_SDTichHopHDDT, TypeID } from 'app/app.constants';
import { ISaReturn } from 'app/shared/model/sa-return.model';
import { DataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { ICareerGroup } from 'app/shared/model/career-group.model';
import { CareerGroupService } from 'app/entities/career-group';
import { IaPublishInvoiceDetailsService } from 'app/ban-hang/xuat-hoa-don/ia-publish-invoice-details.service';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';

@Component({
    selector: 'eb-xuat-hoa-don',
    templateUrl: './xuat-hoa-don.component.html',
    styleUrls: ['./xuat-hoa-don.css'],
    providers: [DatePipe]
})
export class XuatHoaDonComponent extends BaseComponent implements OnInit {
    @ViewChild('popUpMultiDelete') popUpMultiDelete: TemplateRef<any>;
    @ViewChild('deleteItem') deleteItem;
    xuatHoaDons: SaBill[];
    xuatHoaDonDetails: SaBillDetails[];
    accountingObjects: AccountingObject[];
    templates: IaPublishInvoiceDetails[];
    template: IaPublishInvoiceDetails;
    typeDelete: number;
    isRequiredInvoiceNo: boolean;
    series: any;
    searchVoucher: any;
    modalRef: NgbModalRef;
    links: any;
    totalItems: any;
    queryCount: any;
    page: any;
    rowNum: number;
    index: number;
    itemsPerPage = ITEMS_PER_PAGE;
    predicate: any;
    previousPage: any;
    reverse: any;
    routeData: any;
    dataSession: any;
    selectedRow: any;
    eventSubscriber: Subscription;
    file: any;
    refVoucher: any;
    currencyCode: string;
    link: any;
    refModel: any;
    isSingleClick: any;
    currentAccount: any;
    XUAT_HOA_DON = 350;
    isLoading: boolean;
    warningDelete: boolean;
    hiddenVAT: boolean;
    listVAT = [
        { name: '0%', data: 0 },
        { name: '5%', data: 1 },
        { name: '10%', data: 2 },
        { name: 'KCT', data: 3 },
        { name: 'KTT', data: 4 }
    ];
    careerGroups: ICareerGroup[];
    ROLE_Them = ROLE.XuatHoaDon_Them;
    ROLE_Xoa = ROLE.XuatHoaDon_Xoa;
    ROLE_KetXuat = ROLE.XuatHoaDon_KetXuat;
    ROLE_Upload = ROLE.XuatHoaDon_Upload;

    constructor(
        private saBillService: SaBillService,
        private saBillDetailService: SaBillDetailsService,
        private activatedRoute: ActivatedRoute,
        private parseLinks: JhiParseLinks,
        private router: Router,
        private modalService: NgbModal,
        private eventManager: JhiEventManager,
        private sanitizer: DomSanitizer,
        private principal: Principal,
        private toastrService: ToastrService,
        private refModalService: RefModalService,
        private translateService: TranslateService,
        public activeModal: NgbActiveModal,
        private careerGroupService: CareerGroupService,
        private accountingObjectService: AccountingObjectService,
        private iaPublishInvoiceDetailsService: IaPublishInvoiceDetailsService,
        public utilService: UtilsService,
        private unitService: UnitService
    ) {
        super();
        this.registerChangeInAccountDefaults();
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    toggleSearch($event) {
        $event.preventDefault();
        this.searchVoucher.isShowSearch = !this.searchVoucher.isShowSearch;
    }

    getEmptyRow(data) {
        return getEmptyRow(data);
    }

    ngOnInit(): void {
        this.xuatHoaDons = [];
        this.searchVoucher = this.saBillService.searchVoucher ? this.saBillService.searchVoucher : { page: 1 };
        this.xuatHoaDonDetails = [];
        this.saBillService.getAllSearchData().subscribe(res => {
            this.series = res.body.seriDTOS;
        });
        this.accountingObjectService.getAllDTO().subscribe(res => {
            this.accountingObjects = res.body;
        });
        this.unitService.convertRateForMaterialGoodsComboboxCustom().subscribe((res: HttpResponse<IUnit[]>) => {
            this.units = res.body;
        });
        this.iaPublishInvoiceDetailsService.getAllByCompany().subscribe(res => {
            this.templates = res.body;
            this.templates.forEach(item => {
                if (item.invoiceForm === 0) {
                    item.invoiceFormName = 'Hóa đơn điện tử';
                } else if (item.invoiceForm === 1) {
                    item.invoiceFormName = 'Hóa đơn đặt in';
                } else if (item.invoiceForm === 2) {
                    item.invoiceFormName = 'Hóa đơn tự in';
                }
            });
        });
        this.careerGroupService.getCareerGroups().subscribe((res: HttpResponse<ICareerGroup[]>) => {
            this.careerGroups = res.body;
        });
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.currencyCode = account.organizationUnit.currencyID;
            this.isRequiredInvoiceNo = this.currentAccount.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '0');
            this.hiddenVAT = this.currentAccount.organizationUnit.taxCalculationMethod === 1;
        });
        this.getSessionData();
        this.registerExport();
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('dataSearchSaBill'));
        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.totalItems = this.dataSession.totalItems;
            this.searchVoucher = JSON.parse(this.dataSession.searchVoucher);
            this.rowNum = this.dataSession.rowNum;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
        }
        sessionStorage.removeItem('dataSearchSaBill');
        this.search();
    }

    doubleClickRow(id: any) {
        event.preventDefault();
        this.isLoading = true;
        this.isSingleClick = false;
        this.searchVoucher.rowIndex = id;
        this.saveSearchData(this.xuatHoaDons[id]);
        this.router.navigate(['./xuat-hoa-don', id, 'edit']);
    }

    saveSearchData(item: ISaReturn) {
        this.selectedRow = item;
        this.dataSession = new DataSessionStorage();
        this.dataSession.page = this.page;
        this.dataSession.itemsPerPage = this.itemsPerPage;
        this.dataSession.totalItems = this.totalItems;
        this.dataSession.rowNum = this.searchVoucher.rowIndex;
        this.dataSession.predicate = this.predicate;
        this.dataSession.reverse = this.reverse;
        this.dataSession.searchVoucher = JSON.stringify(this.searchVoucher);
        sessionStorage.setItem('dataSearchSaBill', JSON.stringify(this.dataSession));
    }

    edit() {
        event.preventDefault();
        this.doubleClickRow(this.selectedRow.rowIndex);
    }

    sumTotalAmount() {
        let total = 0;
        this.xuatHoaDons.forEach(xuatHoaDon => {
            total += xuatHoaDon.totalAmount - xuatHoaDon.totalDiscountAmount + xuatHoaDon.totalVATAmount;
        });
        return total;
    }

    onSelect(xuatHoaDon) {
        if (xuatHoaDon) {
            this.isSingleClick = true;
            setTimeout(() => {
                if (this.isSingleClick) {
                    this.isLoading = true;
                    this.selectedRow = xuatHoaDon;
                    if (xuatHoaDon) {
                        this.saBillDetailService.find(xuatHoaDon.id).subscribe(
                            res => {
                                this.xuatHoaDonDetails = res.body.saBillDetails;
                                this.xuatHoaDonDetails.forEach(item => {
                                    item.unit = this.units.find(x => x.id == item.unitID);
                                });
                                this.refVoucher = res.body.refVoucherDTOS;
                                this.isLoading = false;
                            },
                            () => {
                                this.isLoading = false;
                            }
                        );
                    }
                }
            }, 250);
        }
    }

    uploadInvoice(detailModalLoan) {
        this.file = null;
        this.refModel = this.modalService.open(detailModalLoan, { size: 'lg' });
    }

    private paginateSABill(data: ISaReturn[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.xuatHoaDons = data;
        this.objects = data;
        if (this.xuatHoaDons && this.xuatHoaDons.length > 0) {
            if (this.rowNum && !this.index) {
                this.index = this.rowNum % this.itemsPerPage;
                this.index = this.index || this.itemsPerPage;
                this.selectedRow = this.xuatHoaDons[this.index - 1] ? this.xuatHoaDons[this.index - 1] : {};
                this.selectedRows.push(this.selectedRow);
            } else if (this.index) {
                this.selectedRow = this.xuatHoaDons[this.index - 1] ? this.xuatHoaDons[this.index - 1] : {};
                this.selectedRows.push(this.selectedRow);
            } else {
                this.selectedRows.push(this.xuatHoaDons[0]);
                this.selectedRow = this.xuatHoaDons[0];
            }
            this.rowNum = this.getRowNumberOfRecord(this.page, 0);
        } else {
            this.xuatHoaDonDetails = [];
            this.refVoucher = [];
            this.selectedRow = {};
        }
    }

    getRowNumberOfRecord(page: number, index: number): number {
        if (page > 0 && index !== -1) {
            return (page - 1) * this.itemsPerPage + index + 1;
        }
    }

    search(index?) {
        this.isLoading = true;
        this.saBillService.searchVoucher = this.searchVoucher;
        this.saBillService
            .query({
                page: this.searchVoucher.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                accountingObjectID: this.searchVoucher.accountingObjectID ? this.searchVoucher.accountingObjectID : '',
                fromInvoiceDate: this.searchVoucher.fromInvoiceDate ? this.searchVoucher.fromInvoiceDate : '',
                toInvoiceDate: this.searchVoucher.toInvoiceDate ? this.searchVoucher.toInvoiceDate : '',
                invoiceTemplate: this.searchVoucher.invoiceTemplate ? this.searchVoucher.invoiceTemplate : '',
                invoiceSeries: this.searchVoucher.invoiceSeries ? this.searchVoucher.invoiceSeries : '',
                invoiceNo: this.searchVoucher.invoiceNo ? this.searchVoucher.invoiceNo : '',
                freeText: this.searchVoucher.freeText ? this.searchVoucher.freeText : ''
            })
            .subscribe(
                res => {
                    this.paginateSABill(res.body, res.headers);
                    if (!this.selectedRow && res.body) {
                        this.selectMultiRow(this.xuatHoaDons[0], event, this.xuatHoaDons);
                    } else {
                        const select = res.body.find(n => n.id === this.selectedRow.id);
                        if (!select) {
                            this.selectMultiRow(this.xuatHoaDons[0], event, this.xuatHoaDons);
                        } else if (index) {
                            this.selectMultiRow(this.xuatHoaDons[index], event, this.xuatHoaDons);
                        } else {
                            this.selectMultiRow(select, event, this.xuatHoaDons);
                        }
                    }
                    this.saBillService.total = res.body.length;
                    this.isLoading = false;
                },
                () => {
                    this.isLoading = false;
                }
            );
    }

    loadPage(page) {
        if (this.searchVoucher.page !== page) {
            this.searchVoucher.page = page;
        } else {
            this.search();
        }
    }

    reset() {
        this.searchVoucher.freeText = '';
        this.searchVoucher.invoiceNo = '';
        this.searchVoucher.fromInvoiceDate = '';
        this.searchVoucher.toInvoiceDate = '';
        this.searchVoucher.accountingObject = '';
        this.searchVoucher.accountingObjectID = '';
        this.searchVoucher.seri = {};
        this.searchVoucher.invoiceSeries = '';
        this.searchVoucher.template = {};
        this.searchVoucher.invoiceTemplate = '';
        this.page = 1;
        this.searchVoucher.currencyID = '';
        this.template = {};
        this.search();
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    getFromToMoment(date?: Moment, isMaxDate?: boolean) {
        const _date = date && moment(date).isValid() ? date : isMaxDate ? null : moment();
        return _date ? { year: _date.year(), month: _date.month() + 1, day: _date.date() } : null;
    }

    registerChangeInAccountDefaults() {
        this.eventSubscriber = this.eventManager.subscribe('xuatHoaDonModification', response => {
            this.search();
        });
    }

    // registerChangeSession() {
    //     this.eventManager.subscribe('changeSession', response => {
    //         this.search();
    //     });
    // }

    upload(download) {
        this.isLoading = true;
        if (!this.file) {
            this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.noFile'));
            this.isLoading = false;
            return;
        }
        this.toastrService.info(this.translateService.instant('ebwebApp.saBill.announce'));
        this.saBillService.upload(this.file).subscribe(
            res => {
                if (res.headers.get('isError') === '1') {
                    if (res.headers.get('message')) {
                        this.toastrService.error(this.translateService.instant('ebwebApp.saBill.upload.' + res.headers.get('message')));
                    } else {
                        // this.refModel.close();
                        this.modalService.open(download, { size: 'lg' });
                        const blob = new Blob([res.body], { type: 'application/pdf' });
                        const fileURL = URL.createObjectURL(blob);

                        this.link = document.createElement('a');
                        document.body.appendChild(this.link);
                        this.link.download = fileURL;
                        this.link.setAttribute('style', 'display: none');
                        const name = 'Upload_Invoice_Loi.xls';
                        this.link.setAttribute('download', name);
                        this.link.href = fileURL;
                    }
                } else {
                    this.loadPage(0);
                    this.refModel.close();
                    this.toastrService.success(this.translateService.instant('ebwebApp.saBill.upload.success'));
                }
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
            if (file[0].name.endsWith('xlsx') || file[0].name.endsWith('xls')) {
                this.file = file[0];
            } else {
                this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.notFormat'));
            }
        }
    }

    download() {
        this.link.click();
    }

    downloadTem() {
        this.isLoading = true;
        this.saBillService.downloadTem().subscribe(
            res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);

                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = 'Upload_Invoice.xlsx';
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

    exportExcel() {
        this.isLoading = true;
        this.saBillService.exportExcel().subscribe(
            res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);

                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = 'DS_XuatHoaDon.xls';
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

    exportPdf() {
        this.saBillService.export().subscribe(res => {
            this.refModalService.open(null, EbReportPdfPopupComponent, res, false, this.XUAT_HOA_DON);
        });
    }

    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${this.XUAT_HOA_DON}`, () => {
            this.exportExcel();
        });
    }

    @ebAuth(['ROLE_ADMIN', ROLE.XuatHoaDon_Them])
    addNew($event) {
        $event.preventDefault();
        this.router.navigate(['/xuat-hoa-don/new']);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.XuatHoaDon_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            this.saBillService
                .checkRelateVoucher({
                    sABillID: this.selectedRow.id
                })
                .subscribe((res: HttpResponse<boolean>) => {
                    this.warningDelete = res.body;
                    if (this.selectedRow.invoiceForm === 0 && !this.isRequiredInvoiceNo) {
                        if (this.selectedRow.invoiceNo) {
                            this.typeDelete = 3;
                        } else {
                            this.typeDelete = 0;
                        }
                    } else {
                        if (this.selectedRow.invoiceNo) {
                            this.typeDelete = 1;
                        } else {
                            this.typeDelete = 0;
                        }
                    }
                    this.modalRef = this.modalService.open(this.deleteItem, { backdrop: 'static' });
                });
        }
    }

    deleteForm() {
        if (this.selectedRow) {
            if (this.typeDelete === 3) {
                this.toastrService.error(this.translateService.instant('ebwebApp.sAInvoice.delete.notDeleteInvoice'));
            } else {
                const index = this.xuatHoaDons.indexOf(this.selectedRow);
                this.saBillService.delete(this.selectedRow.id).subscribe(
                    ref => {
                        this.toastrService.success(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.success'));
                        this.search(index);
                        this.modalRef.close();
                    },
                    ref => {
                        this.toastrService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.error'));
                    }
                );
            }
        }
    }

    deleteVoucher() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.selectedRows.length > 1) {
            this.selectedRows.forEach(item => {
                item.invoiceDate = item.invoiceDate ? item.invoiceDate.format(DATE_FORMAT) : null;
            });
            this.saBillService.multiDelete(this.selectedRows).subscribe(
                (res: HttpResponse<any>) => {
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                    this.selectedRows = [];
                    if (res.body.countTotalVouchers !== res.body.countSuccessVouchers) {
                        this.modalRef = this.refModalService.open(
                            res.body,
                            HandlingResultComponent,
                            null,
                            false,
                            TypeID.XUAT_HOA_DON,
                            null,
                            null,
                            null,
                            null,
                            true
                        );
                    } else {
                        this.toastrService.success(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.success'));
                    }
                    this.activeModal.close();
                    this.search();
                },
                (res: HttpErrorResponse) => {
                    if (res.error.errorKey === 'errorDeleteList') {
                        this.toastrService.error(
                            this.translateService.instant('ebwebApp.mBDeposit.errorDeleteVoucherNo'),
                            this.translateService.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                }
            );
        }
    }

    closePopUpDelete() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.XuatHoaDon_KetXuat])
    export() {
        event.preventDefault();
        this.exportPdf();
    }

    print($event) {
        $event.preventDefault();
        this.exportExcel();
    }

    sum(prop) {
        let total = 0;
        for (let i = 0; i < this.xuatHoaDonDetails.length; i++) {
            total += this.xuatHoaDonDetails[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    getVATRate(vatRate) {
        if (this.listVAT && vatRate) {
            const acc = this.listVAT.find(n => n.data === vatRate);
            if (acc) {
                return acc.name;
            }
        }
    }

    getCareerGroupID(id) {
        if (this.careerGroups && id) {
            const ca = this.careerGroups.find(n => n.id === id.toLowerCase());
            if (ca) {
                return ca.careerGroupCode;
            }
        }
    }

    selectTemplate() {
        if (this.template) {
            this.searchVoucher.invoiceTemplate = this.template.invoiceTemplate;
            this.searchVoucher.invoiceSeries = this.template.invoiceSeries;
        } else {
            this.searchVoucher.invoiceTemplate = null;
            this.searchVoucher.invoiceSeries = null;
        }
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }
}
