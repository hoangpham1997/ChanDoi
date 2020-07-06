import { Component, OnInit, OnDestroy, ViewChild, TemplateRef } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { ICPPeriod } from 'app/shared/model/cp-period.model';
import { Principal } from 'app/core';
import { ITEMS_PER_PAGE } from 'app/shared';
import { PpGianDonService } from './pp-gian-don.service';
import { CPUncompleteDetailsService } from 'app/entities/cp-uncomplete-details';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { CostSetService } from 'app/entities/cost-set';
import { ExpenseItemService } from 'app/entities/expense-item';
import { GIA_THANH, TypeID } from 'app/app.constants';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { ROLE } from 'app/role.constants';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';

@Component({
    selector: 'eb-cp-period',
    templateUrl: './pp-gian-don.component.html',
    styleUrls: ['./pp-gian-don.component.css']
})
export class PpGianDonComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('deleteItem') deleteItem;
    @ViewChild('checkDelete') checkDelete;
    @ViewChild('popUpMultiDelete') popUpMultiDelete: TemplateRef<any>;
    account: any;
    cPPeriods: any[];
    cPPeriod: ICPPeriod;
    cPResults: any[];
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
    selectedRow: any;
    cPExcpenseList0: any[];
    cPExcpenseList1: any[];
    cPAllocationGeneralExpenseDetails: any[];
    cPUncompleteDetails: any[];
    modalRef: NgbModalRef;
    ROLE_Them = ROLE.PhuongPhapGianDon_Them;
    ROLE_Xoa = ROLE.PhuongPhapGianDon_Xoa;
    ROLE_KetXuat = ROLE.PhuongPhapGianDon_KetXuat;

    constructor(
        private cPPeriodService: PpGianDonService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private cPUncompleteDetailsService: CPUncompleteDetailsService,
        private toasService: ToastrService,
        private modalService: NgbModal,
        private translateService: TranslateService,
        private costSetService: CostSetService,
        private expenseItemService: ExpenseItemService,
        private refModalService: RefModalService,
        public utilsService: UtilsService,
        public activeModal: NgbActiveModal
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {
        this.cPPeriodService
            .getAllCPPeriod({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                type: GIA_THANH.PHUONG_PHAP_GIAN_DON
            })
            .subscribe(
                (res: HttpResponse<ICPPeriod[]>) => {
                    this.paginateCPPeriods(res.body, res.headers);
                    if (res.body.length > 0) {
                        this.onSelect(res.body[0]);
                    } else {
                        this.cPExcpenseList0 = [];
                        this.cPExcpenseList1 = [];
                        this.cPAllocationGeneralExpenseDetails = [];
                        this.cPUncompleteDetails = [];
                        this.cPResults = [];
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/gia-thanh/gian-don'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    ngOnInit() {
        this.cPExcpenseList0 = [];
        this.cPExcpenseList1 = [];
        this.cPAllocationGeneralExpenseDetails = [];
        this.cPUncompleteDetails = [];
        this.cPResults = [];
        this.loadAll();
        this.principal.identity().then(account => {
            this.account = account;
            this.registerExport();
        });
        this.registerChangeInCPPeriods();
    }

    onSelect(select: ICPPeriod) {
        this.selectedRow = select;
        this.cPPeriodService.getCPPeriodByID({ id: select.id }).subscribe(res => {
            this.cPPeriod = res.body;
            this.cPExcpenseList0 = this.cPPeriod.cPExpenseList.filter(x => x.typeVoucher === 0);
            this.cPExcpenseList1 = this.cPPeriod.cPExpenseList.filter(x => x.typeVoucher === 1);
            if (this.cPPeriod.cPAllocationGeneralExpenseDetails) {
                this.cPAllocationGeneralExpenseDetails = this.cPPeriod.cPAllocationGeneralExpenseDetails;
            }
            if (this.cPPeriod.cPUncompleteDetails) {
                this.cPUncompleteDetails = this.cPPeriod.cPUncompleteDetails;
            }
            if (this.cPPeriod.cPResults) {
                this.cPResults = this.cPPeriod.cPResults;
            }
        });
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    selectedItemPerPage() {
        this.loadAll();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ICPPeriod) {
        return item.id;
    }

    registerChangeInCPPeriods() {
        this.eventSubscriber = this.eventManager.subscribe('cPPeriodListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateCPPeriods(data: ICPPeriod[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.cPPeriods = data;
        this.objects = data;
        if (this.cPPeriods && this.cPPeriods.length > 0) {
            this.selectedRows.push(this.cPPeriods[0]);
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhuongPhapGianDon_Them])
    addNew($event) {
        this.router.navigate(['./gia-thanh/gian-don', 'new']);
    }

    doubleClickRow(id: string) {
        this.router.navigate(['./gia-thanh/gian-don', id, 'edit']);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhuongPhapGianDon_Sua])
    edit() {
        event.preventDefault();
        this.doubleClickRow(this.selectedRow.id);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhuongPhapGianDon_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRows.length === 1) {
            this.cPPeriodService.checkDelete({ id: this.selectedRow.id }).subscribe(res => {
                if (res.body) {
                    this.modalRef = this.modalService.open(this.checkDelete, { backdrop: 'static' });
                } else {
                    this.modalRef = this.modalService.open(this.deleteItem, { backdrop: 'static' });
                }
            });
        } else {
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        }
    }

    deleteForm() {
        if (this.selectedRows.length === 1) {
            const index = this.cPPeriods.indexOf(this.selectedRow);
            this.cPPeriodService.delete(this.selectedRow.id).subscribe(
                ref => {
                    this.toasService.success(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.success'));
                    this.loadAll();
                    this.modalRef.close();
                },
                ref => {
                    this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.error'));
                }
            );
        } else {
            this.cPPeriodService.multiDelete(this.selectedRows).subscribe(
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
                            null,
                            null,
                            null,
                            null,
                            null,
                            true
                        );
                    } else {
                        this.toasService.success(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.success'));
                    }
                    this.activeModal.close();
                    this.loadAll();
                },
                (res: HttpErrorResponse) => {
                    if (res.error.errorKey === 'errorDeleteList') {
                        this.toasService.error(
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

    closePopUp() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    sumCPExpenseListType0(prop) {
        let total = 0;
        if (this.cPExcpenseList0) {
            for (let i = 0; i < this.cPExcpenseList0.length; i++) {
                total += this.cPExcpenseList0[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    sumCPExpenseListType1(prop) {
        let total = 0;
        if (this.cPExcpenseList1) {
            for (let i = 0; i < this.cPExcpenseList1.length; i++) {
                total += this.cPExcpenseList1[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    sumCPAllocationDetail(prop) {
        let total = 0;
        if (this.cPAllocationGeneralExpenseDetails) {
            for (let i = 0; i < this.cPAllocationGeneralExpenseDetails.length; i++) {
                total += this.cPAllocationGeneralExpenseDetails[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    sumcPUncompleteDetail(prop) {
        let total = 0;
        if (this.cPUncompleteDetails) {
            for (let i = 0; i < this.cPUncompleteDetails.length; i++) {
                total += this.cPUncompleteDetails[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    sumcPResult(prop) {
        let total = 0;
        if (this.cPResults) {
            for (let i = 0; i < this.cPResults.length; i++) {
                total += this.cPResults[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    exportPdf() {
        this.cPPeriodService
            .export({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                type: GIA_THANH.PHUONG_PHAP_GIAN_DON
            })
            .subscribe(res => {
                this.refModalService.open(null, EbReportPdfPopupComponent, res, false, GIA_THANH.PHUONG_PHAP_GIAN_DON);
            });
    }

    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${GIA_THANH.PHUONG_PHAP_GIAN_DON}`, () => {
            this.exportExcel();
        });
    }

    exportExcel() {
        this.cPPeriodService
            .exportExcel({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                type: GIA_THANH.PHUONG_PHAP_GIAN_DON
            })
            .subscribe(res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);
                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = 'DS_PPGianDon.xls';
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
            });
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhuongPhapGianDon_KetXuat])
    export() {
        event.preventDefault();
        this.exportPdf();
    }
}
