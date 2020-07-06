import { AfterViewChecked, Component, OnInit, ViewChild } from '@angular/core';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { Principal } from 'app/core';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs';
import { InvoiceType } from 'app/shared/model/invoice-type.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { IaPublishInvoice } from 'app/shared/model/ia-publish-invoice.model';
import { IaPublishInvoiceService } from 'app/quan-ly-hoa-don/thong-bao-phat-hanh-hoa-don/ia-publish-invoice.service';
import * as moment from 'moment';
import { IAReportService } from 'app/quan-ly-hoa-don/khoi-tao-mau-hoa-don/ia-report.service';
import { IARegisterInvoiceService } from 'app/quan-ly-hoa-don/dang-ky-su-dung/ia-register-invoice.service';
import { IaPublishInvoiceDetails } from 'app/shared/model/ia-publish-invoice-details.model';
import { INVOICE_FORM, TypeID } from 'app/app.constants';
import { DATE_FORMAT } from 'app/shared';
import { ROLE } from 'app/role.constants';

@Component({
    selector: 'eb-don-mua-hang-update',
    templateUrl: './thong-bao-phat-hanh-update.component.html',
    styleUrls: ['./thong-bao-phat-hanh-update.component.css']
})
export class ThongBaoPhatHanhUpdateComponent extends BaseComponent implements OnInit {
    @ViewChild('content') content;
    TYPE_THONG_BAO_PHAT_HANH_HDDT = 820;
    isSaving: boolean;
    dateDp: any;
    isCreateUrl: boolean;
    page: number;
    itemsPerPage: number;
    pageCount: number;
    totalItems: number;
    rowNum: number;
    predicate: any;
    reverse: any;
    dataSession: IDataSessionStorage;
    iaPublishInvoice: IaPublishInvoice;
    iaPublishInvoiceCopy: IaPublishInvoice;
    iaPublishInvoiceDetails: IaPublishInvoiceDetails[];
    iaPublishInvoiceDetailsCopy: IaPublishInvoiceDetails[];
    isEditUrl: boolean;
    isEdit: boolean;
    searchData: string;
    account: any;
    contextMenu: ContextMenu;
    modalRef: NgbModalRef;
    eventSubscriber: Subscription;
    isClosing: boolean;
    invoiceTypes: InvoiceType[];
    invoiceType: InvoiceType;
    checkModalRef: NgbModalRef;
    currentIndex: number;
    private isClosed: boolean;
    private isSaved: boolean;
    iaReports: any[];
    DEFAULT_FROM_NO = '0000001';
    checkAllBox: boolean;
    CHUA_CO_HIEU_LUC = 0;
    DA_CO_HIEU_LUC = 1;
    HD_DIEN_TU = INVOICE_FORM.HD_DIEN_TU;
    HD_TU_IN = INVOICE_FORM.HD_TU_IN;
    HD_DAT_IN = INVOICE_FORM.HD_DAT_IN;
    PREFIX_LENGTH = 7;
    checkEditRoleSua: boolean;
    checkEditRoleThem: boolean;
    arrAuthorities: any[];
    isNew: boolean;
    status: number;

    ROLE_XEM = ROLE.TBPH_XEM;
    ROLE_THEM = ROLE.TBPH_THEM;
    ROLE_SUA = ROLE.TBPH_SUA;
    ROLE_XOA = ROLE.TBPH_XOA;
    ROLE_KETXUAT = ROLE.TBPH_KET_XUAT;
    ROLE_IN = ROLE.TBPH_IN;

    constructor(
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private jhiAlertService: JhiAlertService,
        private toastrService: ToastrService,
        private iaPublishInvoiceService: IaPublishInvoiceService,
        private iaRegisterService: IARegisterInvoiceService,
        private iaReportService: IAReportService,
        private translateService: TranslateService,
        private eventManager: JhiEventManager,
        public utilsService: UtilsService,
        private principal: Principal,
        private modalService: NgbModal,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {
        super();
    }

    ngOnInit() {
        this.getSessionData();
        this.initValues();
        // this.onChangeStatus();
        this.isCreateUrl = window.location.href.includes('/new');
        this.isEdit = true;
        this.isEditUrl = window.location.href.includes('/edit');
        this.isNew = window.location.href.includes('new');
        this.activatedRoute.data.subscribe(data => {
            this.principal.identity().then(account => {
                this.account = account;
                this.arrAuthorities = account.authorities;
                this.checkEditRoleSua = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_SUA) : true;
                this.checkEditRoleThem = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_THEM) : true;
                if (data.iaPublishInvoice && data.iaPublishInvoice.id) {
                    this.iaPublishInvoice = data && data.iaPublishInvoice ? data.iaPublishInvoice : {};
                    this.copy();
                }
                this.iaReportDataSetup();
            });
        });
    }

    iaReportDataSetup() {
        this.invoiceTypes = [];
        this.iaPublishInvoiceDetails = [];
        if (this.iaPublishInvoice.id) {
            this.isEdit = false;
        } else {
            this.iaPublishInvoice.date = !this.iaPublishInvoice.date ? moment(new Date()) : this.iaPublishInvoice.date;
            this.iaRegisterService.findOneByCompanyID().subscribe(res => {
                this.iaPublishInvoice.representationInLaw = res.body.director;
                this.iaPublishInvoice.receiptedTaxOffical = res.body.taxAgentName;
                this.copy();
            });
        }
        this.iaReportService.query().subscribe(res => {
            if (res.body && res.body.length) {
                if (this.iaPublishInvoice.id) {
                    for (const item of this.iaPublishInvoice.iaPublishInvoiceDetails) {
                        item.isCheck = true;
                        this.iaPublishInvoiceDetails.push(item);
                    }
                    this.updateToNo();
                } else {
                    for (const item of res.body) {
                        const detail = new IaPublishInvoiceDetails();
                        detail.iaReportID = item.id;
                        detail.invoiceForm = item.invoiceForm;
                        detail.invoiceType = item.invoiceType;
                        detail.invoiceTemplate = item.invoiceTemplate;
                        detail.invoiceSeries = item.invoiceSeries;
                        detail.fromNo = this.DEFAULT_FROM_NO;
                        if (this.iaPublishInvoice.date) {
                            detail.startUsingHolder = moment(this.iaPublishInvoice.date).add(2, 'days');
                        }
                        this.iaPublishInvoiceService.findCurrentMaxFromNo(detail.iaReportID).subscribe(response => {
                            if (response.body) {
                                const raw = (response.body + 1).toString();
                                detail.fromNo = '0'.repeat(raw.length > this.PREFIX_LENGTH ? 0 : this.PREFIX_LENGTH - raw.length) + raw;
                                this.copy();
                            }
                        });
                        this.iaPublishInvoiceDetails.push(detail);
                    }
                }
                this.copy();
                this.isCheckAll();
            }
        });
    }

    // onChangeStatus() {
    //     if (this.iaPublishInvoice.status === this.CHUA_CO_HIEU_LUC) {
    //         this.status = this.CHUA_CO_HIEU_LUC;
    //     } else {
    //         this.status = this.DA_CO_HIEU_LUC;
    //     }
    //     this.iaReportDataSetup();
    // }

    getToNo(quantity: number, fromNo: string): string {
        if ((quantity || quantity === 0) && fromNo) {
            const raw = (parseInt(fromNo, 10) + quantity - 1).toString();
            return '0'.repeat(raw.length > this.PREFIX_LENGTH ? 0 : this.PREFIX_LENGTH - raw.length) + raw;
        }
    }

    updateToNo() {
        for (let i = 0; i < this.iaPublishInvoiceDetails.length; i++) {
            this.updateToNoByIndex(i);
        }
    }

    initValues() {
        this.iaPublishInvoice = {};
        this.iaPublishInvoice.status = this.CHUA_CO_HIEU_LUC;
        this.iaPublishInvoice.typeId = TypeID.DANG_KY_SU_DUNG_HOA_DON;
        this.isSaving = false;
        this.copy();
    }

    save() {
        event.preventDefault();
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isSaving = true;
        this.iaPublishInvoice.iaPublishInvoiceDetails = this.iaPublishInvoiceDetails.filter(item => item.isCheck).map(item => {
            item.startUsing =
                item.startUsingHolder != null && item.startUsingHolder.isValid() ? item.startUsingHolder.format(DATE_FORMAT) : null;
            item.contractDate =
                item.contractDateHolder != null && item.contractDateHolder.isValid() ? item.contractDateHolder.format(DATE_FORMAT) : null;
            return item;
        });
        if (!this.checkError()) {
            this.isClosing = false;
            return;
        }
        if (this.iaPublishInvoice.id) {
            this.iaPublishInvoiceService.update(this.iaPublishInvoice).subscribe(
                res => {
                    this.handleSuccessResponse(res);
                },
                error => {
                    this.handleError(error);
                }
            );
        } else {
            this.iaPublishInvoiceService.create(this.iaPublishInvoice).subscribe(
                res => {
                    this.handleSuccessResponse(res);
                },
                error => {
                    this.handleError(error);
                }
            );
        }
    }

    checkError(): boolean {
        if (!this.iaPublishInvoice.date) {
            this.toastError('ebwebApp.iaPublishInvoice.error.date.required');

            return false;
        }
        if (!this.iaPublishInvoice.iaPublishInvoiceDetails || !this.iaPublishInvoice.iaPublishInvoiceDetails.length) {
            this.toastError('ebwebApp.iaPublishInvoice.error.detail.required');

            return false;
        }
        if (this.iaPublishInvoice && this.iaPublishInvoice.iaPublishInvoiceDetails) {
            for (const item of this.iaPublishInvoice.iaPublishInvoiceDetails) {
                if (!item.quantity || item.quantity < 0) {
                    this.toastError('ebwebApp.iaPublishInvoice.error.quantity.required');
                    return false;
                }
                if (!item.fromNo || parseInt(item.fromNo, 10) <= 0) {
                    this.toastError('ebwebApp.iaPublishInvoice.error.fromNo.required');
                    return false;
                }
                if (!item.startUsingHolder) {
                    this.toastError('ebwebApp.iaPublishInvoice.startUsingNotNull');
                    return false;
                }
            }
        }

        return true;
    }

    toastError(key: string) {
        this.toastrService.error(this.translateService.instant(key));
    }

    handleSuccessResponse(res) {
        this.isSaving = false;
        if (!this.iaPublishInvoice.id) {
            this.toastrService.success(this.translateService.instant('ebwebApp.iAReport.created'));
        } else {
            this.toastrService.success(this.translateService.instant('ebwebApp.iAReport.updated'));
        }
        this.iaPublishInvoice.id = res.body.id;
        this.isSaved = true;
        this.closeForm();
    }

    handleError(err) {
        this.isSaving = false;
        this.isClosing = false;
        if (err && err.error) {
            this.toastrService.error(
                this.translateService.instant(`ebwebApp.iaPublishInvoice.${err.error.message}`, { title: err.error.title })
            );
        }
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    openModelDelete(content, index) {
        this.currentIndex = index;
        this.checkModalRef = this.modalService.open(content, { backdrop: 'static' });
    }

    copy() {
        this.iaPublishInvoiceCopy = this.utilsService.deepCopy([this.iaPublishInvoice])[0];
        this.iaPublishInvoiceDetailsCopy = this.utilsService.deepCopy(this.iaPublishInvoiceDetails);
    }

    closeForm() {
        this.isClosing = true;
        if (
            !this.iaPublishInvoiceCopy ||
            this.isSaved ||
            (this.utilsService.isEquivalent(this.iaPublishInvoice, this.iaPublishInvoiceCopy) &&
                this.utilsService.isEquivalentArray(this.iaPublishInvoiceDetails, this.iaPublishInvoiceDetailsCopy))
        ) {
            this.closeAll();
            return;
        }
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.modalRef = this.modalService.open(this.content, { backdrop: 'static' });
    }

    close() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.isClosing) {
            this.closeAll();
        }
        this.isClosing = false;
    }

    closeAll() {
        this.isClosed = true;
        this.router.navigate(
            ['/thong-bao-phat-hanh-hoa-don'],
            this.dataSession
                ? {
                      queryParams: {
                          page: this.page,
                          size: this.itemsPerPage,
                          sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                      }
                  }
                : {}
        );
    }

    canDeactive() {
        if (this.isClosing || !this.iaPublishInvoiceCopy || this.isClosed) {
            return true;
        }
        return (
            this.utilsService.isEquivalent(this.iaPublishInvoice, this.iaPublishInvoiceCopy) &&
            this.utilsService.isEquivalentArray(this.iaPublishInvoiceDetails, this.iaPublishInvoiceDetailsCopy)
        );
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('TBPH_DataSession'));
        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.pageCount = this.dataSession.pageCount;
            this.totalItems = this.dataSession.totalItems;
            this.rowNum = this.dataSession.rowNum;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
        } else {
            this.dataSession = null;
        }
    }

    checkAll() {
        this.iaPublishInvoiceDetails.forEach(item => (item.isCheck = this.checkAllBox));
    }

    isCheckAll() {
        this.checkAllBox = this.iaPublishInvoiceDetails && this.iaPublishInvoiceDetails.every(item => item.isCheck);
    }

    updateToNoByIndex(i: number) {
        if (this.iaPublishInvoiceDetails[i].fromNo === '') {
            this.iaPublishInvoiceDetails[i].fromNo = '0000001';
        } else {
            const raw = parseInt(this.iaPublishInvoiceDetails[i].fromNo, 10).toString();
            this.iaPublishInvoiceDetails[i].fromNo =
                '0'.repeat(raw.length > this.PREFIX_LENGTH ? 0 : this.PREFIX_LENGTH - raw.length) + raw;
            this.iaPublishInvoiceDetails[i].toNo = this.getToNo(
                this.iaPublishInvoiceDetails[i].quantity,
                this.iaPublishInvoiceDetails[i].fromNo
            );
        }
    }

    onChangeDate() {
        if (this.iaPublishInvoiceDetails) {
            this.iaPublishInvoiceDetails.forEach(item => {
                if (this.iaPublishInvoice.date) {
                    item.startUsingHolder = moment(this.iaPublishInvoice.date).add(2, 'days');
                }
            });
        }
    }

    exportPDF(isDownload, typeReports: number) {
        if (!this.isCreateUrl) {
            this.iaPublishInvoiceService
                .getCustomerReport({
                    id: this.iaPublishInvoice.id,
                    typeID: this.TYPE_THONG_BAO_PHAT_HANH_HDDT,
                    typeReport: typeReports
                })
                .subscribe(response => {
                    // this.showReport(response);
                    const file = new Blob([response.body], { type: 'application/pdf' });
                    const fileURL = window.URL.createObjectURL(file);
                    if (isDownload) {
                        const link = document.createElement('a');
                        document.body.appendChild(link);
                        link.download = fileURL;
                        link.setAttribute('style', 'display: none');
                        const name = 'Thong_bao_phat_hanh_hddt.pdf';
                        link.setAttribute('download', name);
                        link.href = fileURL;
                        link.click();
                    } else {
                        const contentDispositionHeader = response.headers.get('Content-Disposition');
                        const result = contentDispositionHeader
                            .split(';')[1]
                            .trim()
                            .split('=')[1];
                        const newWin = window.open(fileURL, '_blank');

                        // add a load listener to the window so that the title gets changed on page load
                        newWin.addEventListener('load', function() {
                            newWin.document.title = result.replace(/"/g, '');
                            // this.router.navigate(['/report/buy']);
                        });
                    }
                });
            if (typeReports === 1) {
                this.toastr.success(
                    this.translate.instant('ebwebApp.mBDeposit.printing') +
                        this.translate.instant('ebwebApp.iaPublishInvoice.temp1') +
                        '...',
                    this.translate.instant('ebwebApp.mBDeposit.message')
                );
            } else if (typeReports === 2) {
                this.toastr.success(
                    this.translate.instant('ebwebApp.mBDeposit.printing') +
                        this.translate.instant('ebwebApp.iaPublishInvoice.temp2') +
                        '...',
                    this.translate.instant('ebwebApp.mBDeposit.message')
                );
            }
        }
    }
}
