import { Component, OnInit, ViewChild } from '@angular/core';
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
import { IAReport } from 'app/shared/model/ia-report.model';
import { IAReportService } from 'app/quan-ly-hoa-don/khoi-tao-mau-hoa-don/ia-report.service';
import { InvoiceType } from 'app/shared/model/invoice-type.model';
import { IAInvoiceTemplate } from 'app/shared/model/ia-invoice-template.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { INVOICE_FORM } from 'app/app.constants';
import { ROLE } from 'app/role.constants';

@Component({
    selector: 'eb-don-mua-hang-update',
    templateUrl: './khoi-tao-hoa-don-update.component.html',
    styleUrls: ['./khoi-tao-hoa-don-update.component.css']
})
export class KhoiTaoHoaDonUpdateComponent extends BaseComponent implements OnInit {
    @ViewChild('content') content;
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
    iaReport: IAReport;
    iaReportCopy: IAReport;
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
    iaInvoiceTemplates: IAInvoiceTemplate[];
    iaInvoiceTemplate: IAInvoiceTemplate;
    gridArray: any[];
    defaultArray: any[];
    checkModalRef: NgbModalRef;
    selectedColor: string;
    currentIndex: number;
    private isClosed: boolean;
    private isSaved: boolean;
    private gridArrayCopy: any[];
    SO_LIEN = 9;
    isSpecialInvoiceType: boolean;
    SPECIALINVOICECODE_1 = '01/';
    SPECIALINVOICECODE_2 = '02/';
    checkEditRoleSua: boolean;
    checkEditRoleThem: boolean;
    arrAuthorities: any[];
    isNew: boolean;

    ROLE_XEM = ROLE.KTHD_XEM;
    ROLE_THEM = ROLE.KTHD_THEM;
    ROLE_SUA = ROLE.KTHD_SUA;
    ROLE_XOA = ROLE.KTHD_XOA;
    ROLE_KETXUAT = ROLE.KTHD_KET_XUAT;

    constructor(
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private jhiAlertService: JhiAlertService,
        private toastrService: ToastrService,
        private iaReportService: IAReportService,
        private translateService: TranslateService,
        private eventManager: JhiEventManager,
        public utilsService: UtilsService,
        private principal: Principal,
        private modalService: NgbModal
    ) {
        super();
    }

    ngOnInit() {
        this.getSessionData();
        this.initValues();
        this.isEdit = true;
        this.isEditUrl = window.location.href.includes('edit');
        this.isNew = window.location.href.includes('new');
        this.activatedRoute.data.subscribe(data => {
            this.principal.identity().then(account => {
                this.account = account;
                this.arrAuthorities = account.authorities;
                this.checkEditRoleSua = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_SUA) : true;
                this.checkEditRoleThem = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_THEM) : true;
                if (data.iaReport && data.iaReport.id) {
                    this.iaReport = data && data.iaReport ? data.iaReport : {};
                    this.gridArray = [];
                    for (let i = 1; i < this.iaReport.copyNumber + 1; i++) {
                        const item = {
                            name: this.translateService.instant('ebwebApp.iAReport.copyNumberPrefix') + i,
                            purpose: this.iaReport[`purpose${i}`],
                            color: this.iaReport[`codeColor${i}`]
                        };
                        this.gridArray.push(item);
                    }
                    if (this.checkSpecialInvoiceCode()) {
                        this.isSpecialInvoiceType = true;
                    }
                    this.copy();
                }
                this.iaReportDataSetup();
            });
        });
    }

    iaReportDataSetup() {
        this.invoiceTypes = [];
        if (this.iaReport.id) {
            this.isEdit = false;
        }
        this.iaReportService.queryInvoiceType().subscribe(res => {
            this.invoiceTypes = res.body;
            this.copy();
        });
        this.iaReportService.queryIAInvoiceTemplate().subscribe(res => {
            this.iaInvoiceTemplates = res.body;
            this.copy();
        });
    }

    initValues() {
        this.translateService
            .get([
                'ebwebApp.iAReport.copyNumberPrefix',
                'ebwebApp.iAReport.defaultPurpose1',
                'ebwebApp.iAReport.defaultPurpose2',
                'ebwebApp.iAReport.defaultPurpose3'
            ])
            .subscribe(res => {
                this.defaultArray = [
                    {
                        name: res['ebwebApp.iAReport.copyNumberPrefix'] + '1',
                        purpose: res['ebwebApp.iAReport.defaultPurpose1'],
                        color: 'rgba(0,0,0,1)'
                    },
                    {
                        name: res['ebwebApp.iAReport.copyNumberPrefix'] + '2',
                        purpose: res['ebwebApp.iAReport.defaultPurpose2'],
                        color: 'rgba(0,0,0,1)'
                    },
                    {
                        name: res['ebwebApp.iAReport.copyNumberPrefix'] + '3',
                        purpose: res['ebwebApp.iAReport.defaultPurpose3'],
                        color: 'rgba(0,0,0,1)'
                    }
                ];
                this.gridArray = [...this.defaultArray];
            });
        this.iaReport = {};
        this.iaReport.copyNumber = 3;
        this.iaReport.tempSortOrder = 1;
        this.isSaving = false;
        this.copy();
    }

    /**
     * Hàm thêm, sửa xóa mảng hiển thị chi tiết khi thay đổi số liên
     * Nếu số liên < 4 thì khi thêm lấy phần tử trong mảng mặc định (defaultArray)
     * Ngược lại thì thêm như thường
     */
    addArrayValue() {
        if (!this.iaReport.copyNumber) {
            return;
        }
        if (!this.gridArray || !this.gridArray.length) {
            this.gridArray = [];
            if (this.iaReport.copyNumber > 3) {
                this.gridArray.push(...this.defaultArray);
                for (let i = 3; i < this.iaReport.copyNumber; i++) {
                    this.gridArray.push({
                        name: `${this.translateService.instant('ebwebApp.iAReport.copyNumberPrefix')}${i + 1}`,
                        purpose: '',
                        color: 'rgba(0,0,0,1)'
                    });
                }
            } else {
                this.gridArray = [];
                this.gridArray.push(...this.defaultArray.slice(0, this.iaReport.copyNumber));
            }
        } else {
            this.gridArray = [];
            if (this.iaReport.copyNumber > 3) {
                this.gridArray.push(...this.defaultArray);
                for (let i = 3; i < this.iaReport.copyNumber; i++) {
                    this.gridArray.push({
                        name: `${this.translateService.instant('ebwebApp.iAReport.copyNumberPrefix')}${i + 1}`,
                        purpose: '',
                        color: 'rgba(0,0,0,1)'
                    });
                }
            } else {
                this.gridArray = [];
                this.gridArray.push(...this.defaultArray.slice(0, this.iaReport.copyNumber));
            }
        }
    }

    save() {
        event.preventDefault();
        if (this.modalRef) {
            this.modalRef.close();
        }

        /**
         * Trước khi save lấy mảng chi tiết gán lại cho object chính
         */
        for (let i = 0; i < this.SO_LIEN; i++) {
            if (this.gridArray[i]) {
                this.iaReport[`codeColor${i + 1}`] = this.gridArray[i].color;
                this.iaReport[`purpose${i + 1}`] = this.gridArray[i].purpose;
            } else {
                this.iaReport[`codeColor${i + 1}`] = null;
                this.iaReport[`purpose${i + 1}`] = null;
            }
        }

        if (!this.checkError()) {
            this.isClosing = false;
            return;
        }
        this.isSaving = true;
        if (this.iaReport.id) {
            this.iaReportService.update(this.iaReport).subscribe(
                res => {
                    this.handleSuccessResponse(res);
                },
                error => {
                    this.handleError(error);
                }
            );
        } else {
            this.iaReportService.create(this.iaReport).subscribe(
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
        if (!this.iaReport.reportName) {
            this.toastrService.error(this.translateService.instant('ebwebApp.iAReport.error.reportName.required'));
            return false;
        } else if (this.iaReport.invoiceForm === null || this.iaReport.invoiceForm === undefined) {
            this.toastrService.error(this.translateService.instant('ebwebApp.iAReport.error.invoiceForm.required'));
            return false;
        } else if (this.iaReport.tempSortOrder === null || this.iaReport.tempSortOrder === undefined) {
            this.toastrService.error(this.translateService.instant('ebwebApp.iAReport.error.tempSortOrder.required'));
            return false;
        } else if (this.iaReport.copyNumber === null || this.iaReport.copyNumber === undefined) {
            this.toastrService.error(this.translateService.instant('ebwebApp.iAReport.error.copyNumber.required'));
            return false;
        } else if (!this.iaReport.invoiceType) {
            this.toastrService.error(this.translateService.instant('ebwebApp.iAReport.error.invoiceType.required'));
            return false;
        } else if (!this.iaReport.invoiceSeries) {
            this.toastrService.error(this.translateService.instant('ebwebApp.iAReport.error.invoiceSeries.required'));
            return false;
        }

        if (this.isSpecialInvoiceType) {
            if (
                this.iaReport.invoiceTemplate !== this.SPECIALINVOICECODE_1 &&
                this.iaReport.invoiceTemplate !== this.SPECIALINVOICECODE_2
            ) {
                this.toastrService.error(this.translateService.instant('ebwebApp.iAReport.error.invoiceTemplate.wrongFormat'));
                return false;
            }
        }

        /**
         * Ký hiệu hóa đơn có thể có 6 hoặc 8 kí tự
         * Ký hiệu cuối cùng của nó thuộc 1 trong 3 giá trị 'E', 'P', 'T' tùy thuộc theo loại hóa đơn
         */
        let last;
        if (this.iaReport.invoiceForm === INVOICE_FORM.HD_DIEN_TU) {
            last = INVOICE_FORM.E;
        } else if (this.iaReport.invoiceForm === INVOICE_FORM.HD_DAT_IN) {
            last = INVOICE_FORM.P;
        } else if (this.iaReport.invoiceForm === INVOICE_FORM.HD_TU_IN) {
            last = INVOICE_FORM.T;
        }
        if (last) {
            const regex6 = new RegExp(`^[A-Z]{2}/\\d{2}${last}$`);
            const regex8 = new RegExp(`^\\d{2}[A-Z]{2}/\\d{2}${last}$`);
            if (!regex6.test(this.iaReport.invoiceSeries) && !regex8.test(this.iaReport.invoiceSeries)) {
                this.toastrService.error(this.translateService.instant('ebwebApp.iAReport.error.invoiceSeries.regex'));
                return false;
            }
        }
        return true;
    }

    handleSuccessResponse(res) {
        this.isSaving = false;
        if (!this.iaReport.id) {
            this.toastrService.success(this.translateService.instant('ebwebApp.iAReport.created'));
        } else {
            this.toastrService.success(this.translateService.instant('ebwebApp.iAReport.updated'));
        }
        this.iaReport.id = res.body.id;
        this.isSaved = true;
        this.closeForm();
    }

    handleError(err) {
        this.isSaving = false;
        this.isClosing = false;
        this.toastrService.error(this.translateService.instant(`ebwebApp.iAReport.${err.error.message}`));
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    checkTempSortOrder() {
        if (!this.iaReport.tempSortOrder) {
            this.iaReport.tempSortOrder = 0;
        }
        this.updateInvoiceTemplate();
    }

    onBlurCopyNum() {
        if (!this.iaReport.copyNumber) {
            this.iaReport.copyNumber = 0;
        }
        this.addArrayValue();
        this.updateInvoiceTemplate();
    }

    changeInvoiceType() {
        this.updateInvoiceTemplate();
        this.addArrayValue();
    }

    updateInvoiceTemplate() {
        // Trường hợp chọn Loại hóa đơn là Tem, Vé, Thẻ
        // thì mở trường Mẫu số hóa đơn
        // Mặc định giá trị là 01/
        if (this.checkSpecialInvoiceCode()) {
            this.isSpecialInvoiceType = true;
            this.iaReport.invoiceTemplate = this.SPECIALINVOICECODE_1;
        } else {
            this.isSpecialInvoiceType = false;
            if (
                this.iaReport &&
                (this.iaReport.copyNumber !== null && this.iaReport.copyNumber !== undefined) &&
                (this.iaReport.tempSortOrder !== null && this.iaReport.tempSortOrder !== undefined) &&
                this.iaReport.invoiceType
            ) {
                this.iaReport.invoiceTemplate = `${this.iaReport.invoiceType.invoiceTypeCode}${this.iaReport.copyNumber}/00${
                    this.iaReport.tempSortOrder
                }`;
            } else {
                this.iaReport.invoiceTemplate = null;
            }
        }
    }

    checkSpecialInvoiceCode(): boolean {
        const regex = /.*01\/.*02\/.*/;
        return this.iaReport && this.iaReport.invoiceType && regex.test(this.iaReport.invoiceType.invoiceTypeCode);
    }

    invoiceFormChange() {
        if (this.iaReport.invoiceForm === 0) {
            this.iaReport.copyNumber = 0;
            this.gridArray = [];
            this.updateInvoiceTemplate();
            this.addArrayValue();
        } else if (this.iaReport.invoiceForm === 1 || this.iaReport.invoiceForm === 2) {
            this.iaReport.copyNumber = 3;
            this.updateInvoiceTemplate();
            this.addArrayValue();
        }
    }

    onInputCopyNum(event: any) {
        if (!event.target.value || event.target.value === '0') {
            this.gridArray = [];
        }
        this.addArrayValue();
    }

    openModelDelete(content, index) {
        this.currentIndex = index;
        this.checkModalRef = this.modalService.open(content, { backdrop: 'static' });
    }

    selectColor() {
        this.gridArray[this.currentIndex].color = this.selectedColor;
    }

    copy() {
        this.iaReportCopy = Object.assign({}, this.iaReport);
        if (this.gridArray) {
            this.gridArrayCopy = this.gridArray.map(item => ({ ...item }));
        }
    }

    closeForm() {
        this.isClosing = true;
        if (
            this.isSaved ||
            !this.iaReportCopy ||
            this.isSaved ||
            (this.utilsService.isEquivalent(this.iaReport, this.iaReportCopy) &&
                this.utilsService.isEquivalentArray(this.gridArray, this.gridArrayCopy))
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
            ['/khoi-tao-mau-hoa-don'],
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
        if (this.isClosing || !this.iaReportCopy || this.isClosed) {
            return true;
        }
        return (
            this.utilsService.isEquivalent(this.iaReport, this.iaReportCopy) &&
            this.utilsService.isEquivalentArray(this.gridArrayCopy, this.gridArray)
        );
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('KTHD_DataSession'));
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

    previewInvoiceTemplate() {
        if (this.iaInvoiceTemplate) {
            this.iaReportService.previewInvoiceTemplate({
                templatePath:
                    this.iaInvoiceTemplate && this.iaInvoiceTemplate.invoiceTemplatePath ? this.iaInvoiceTemplate.invoiceTemplatePath : ''
            });
        }
    }
}
