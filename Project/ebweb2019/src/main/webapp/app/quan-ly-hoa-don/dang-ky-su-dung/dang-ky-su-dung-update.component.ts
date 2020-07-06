import { AfterViewChecked, AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
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
import { IARegisterInvoiceService } from 'app/quan-ly-hoa-don/dang-ky-su-dung/ia-register-invoice.service';
import { IARegisterInvoice } from 'app/shared/model/ia-register-invoice.model';
import { IAReportService } from 'app/quan-ly-hoa-don/khoi-tao-mau-hoa-don/ia-report.service';
import { IARegisterInvoiceDetails } from 'app/shared/model/ia-register-invoice-details.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared';
import { ROLE } from 'app/role.constants';

@Component({
    selector: 'eb-don-mua-hang-update',
    templateUrl: './dang-ky-su-dung-update.component.html',
    styleUrls: ['./dang-ky-su-dung-update.component.css']
})
export class DangKySuDungUpdateComponent extends BaseComponent implements OnInit {
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
    iaRegisterInvoice: IARegisterInvoice;
    iaRegisterInvoiceCopy: IARegisterInvoice;
    iaReports: any[];
    iaReportsCopy: any[];
    isEditUrl: boolean;
    isNew: boolean;
    isEdit: boolean;
    searchData: string;
    account: any;
    contextMenu: ContextMenu;
    modalRef: NgbModalRef;
    eventSubscriber: Subscription;
    isClosing: boolean;
    checkAllBox: boolean;
    private isClosed: boolean;
    private isSaved: boolean;
    isPublished: boolean;
    file: File;
    checkEditRoleSua: boolean;
    checkEditRoleThem: boolean;
    arrAuthorities: any[];

    ROLE_XEM = ROLE.DKSD_XEM;
    ROLE_THEM = ROLE.DKSD_THEM;
    ROLE_SUA = ROLE.DKSD_SUA;
    ROLE_XOA = ROLE.DKSD_XOA;
    ROLE_KETXUAT = ROLE.DKSD_KET_XUAT;

    constructor(
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private jhiAlertService: JhiAlertService,
        private toastrService: ToastrService,
        private iaRegisterService: IARegisterInvoiceService,
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
        this.isEdit = true;
        this.isEditUrl = window.location.href.includes('edit');
        this.isNew = window.location.href.includes('new');
        this.activatedRoute.data.subscribe(data => {
            this.principal.identity().then(account => {
                this.account = account;
                this.arrAuthorities = account.authorities;
                this.checkEditRoleSua = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_SUA) : true;
                this.checkEditRoleThem = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_THEM) : true;
                if (data.iaRegisterInvoice && data.iaRegisterInvoice.id) {
                    this.iaRegisterInvoice = data && data.iaRegisterInvoice ? data.iaRegisterInvoice : {};
                }
                this.iaRegisterInvoiceDataSetup();
            });
        });
    }

    iaRegisterInvoiceDataSetup() {
        if (this.iaRegisterInvoice && this.iaRegisterInvoice.id) {
            this.isEdit = false;
        } else {
            this.initValues();
        }
        this.iaRegisterService.findOneByCompanyID().subscribe(res => {
            this.iaRegisterInvoice.signer = res.body.director;
            this.copy();
        });

        if (this.iaRegisterInvoice.id) {
            if (this.iaRegisterInvoice && this.iaRegisterInvoice.iaRegisterInvoiceDetails) {
                const ids = this.iaRegisterInvoice.iaRegisterInvoiceDetails.map(item => item.iaReportID);
                this.iaReportService.checkPublished({ ids }).subscribe(response => {
                    if (response.body) {
                        this.isPublished = true;
                    }
                });
                this.iaReportService
                    .findByIds({
                        ids
                    })
                    .subscribe(response => {
                        if (response.body) {
                            for (const item of response.body) {
                                item.isCheck = true;
                                const detail = this.iaRegisterInvoice.iaRegisterInvoiceDetails.find(
                                    register => register.iaReportID === item.id
                                );
                                item.purpose = detail && detail.purpose ? detail.purpose : '';
                            }
                            this.iaReports = response.body;
                            this.copy();
                            this.isCheckAll();
                        }
                    });
            }
        } else {
            this.iaReportService
                .query({
                    isUnregistered: true
                })
                .subscribe(res => {
                    this.iaReports = res.body;
                    this.copy();
                    this.isCheckAll();
                });
        }

        // this.iaReportService
        //     .query({
        //         isUnregistered: true
        //     })
        //     .subscribe(res => {
        //         this.iaReports = res.body;
        //         if (this.iaReports && this.iaRegisterInvoice && this.iaRegisterInvoice.iaRegisterInvoiceDetails) {
        //             const ids = this.iaRegisterInvoice.iaRegisterInvoiceDetails.map(item => item.iaReportID);
        //             this.iaReportService.checkPublished({ ids }).subscribe(response => {
        //                 if (response.body) {
        //                     this.isPublished = true;
        //                 }
        //             });
        //             this.iaReportService
        //                 .findByIds({
        //                     ids
        //                 })
        //                 .subscribe(response => {
        //                     if (response.body) {
        //                         for (const item of response.body) {
        //                             item.isCheck = true;
        //                             const detail = this.iaRegisterInvoice.iaRegisterInvoiceDetails.find(
        //                                 register => register.iaReportID === item.id
        //                             );
        //                             item.purpose = detail && detail.purpose ? detail.purpose : '';
        //                         }
        //                         this.iaReports.unshift(...response.body);
        //                         this.copy();
        //                     }
        //                 });
        //         }
        //         this.isCheckAll();
        //         this.copy();
        //     });
    }

    initValues() {
        this.iaRegisterInvoice = {};
        console.log(this.iaRegisterInvoice);
        this.iaRegisterInvoice.date = moment(new Date(), DATE_FORMAT);
        this.iaRegisterInvoice.status = 1;
        this.isSaving = false;
    }

    save() {
        event.preventDefault();
        this.iaRegisterInvoice.attachFileContent = this.iaRegisterInvoice.attachFileContent || '';
        this.iaRegisterInvoice.iaRegisterInvoiceDetails = this.iaReports
            .filter(item => item.isCheck)
            .map(report => new IARegisterInvoiceDetails(null, this.iaRegisterInvoice.id, report.id, report.purpose));
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (!this.checkError()) {
            this.isClosing = false;
            return;
        }
        this.isSaving = true;
        if (this.iaRegisterInvoice.id) {
            this.iaRegisterService.update(this.iaRegisterInvoice).subscribe(
                res => {
                    this.handleSuccessResponse();
                },
                error => {
                    this.handleError(error);
                }
            );
        } else {
            this.iaRegisterService.create(this.iaRegisterInvoice).subscribe(
                res => {
                    this.handleSuccessResponse();
                },
                error => {
                    this.handleError(error);
                }
            );
        }
    }

    checkError(): boolean {
        if (!this.iaRegisterInvoice.date) {
            this.toastrService.error(this.translateService.instant('ebwebApp.iARegisterInvoice.error.date.required'));
            return false;
        }
        const date = moment(this.iaRegisterInvoice.date, DATE_FORMAT);
        if (date.year() < 1920) {
            this.toastrService.error(this.translateService.instant('ebwebApp.iARegisterInvoice.error.dateIsValid'));
            return false;
        }
        if (!this.iaRegisterInvoice.iaRegisterInvoiceDetails.length) {
            this.toastrService.error(this.translateService.instant('ebwebApp.iARegisterInvoice.error.details.required'));
            return false;
        }
        return true;
    }

    handleSuccessResponse() {
        this.isSaving = false;
        if (!this.iaRegisterInvoice.id) {
            this.toastrService.success(this.translateService.instant('ebwebApp.iARegisterInvoice.created'));
        } else {
            this.toastrService.success(this.translateService.instant('ebwebApp.iARegisterInvoice.updated'));
        }
        this.isSaved = true;
        this.closeForm();
    }

    handleError(err) {
        this.isSaving = false;
        this.isClosing = false;
        this.close();
        this.toastrService.error(this.translateService.instant(`ebwebApp.iARegisterInvoice.${err.error.message}`));
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    checkAll() {
        this.iaReports.forEach(item => (item.isCheck = this.checkAllBox));
    }

    isCheckAll() {
        this.checkAllBox = this.iaReports && this.iaReports.every(item => item.isCheck);
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
            ['/dang-ky-su-dung-hoa-don'],
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

    copy() {
        this.iaRegisterInvoiceCopy = this.utilsService.deepCopyObject(this.iaRegisterInvoice);
        if (this.iaReports) {
            this.iaReportsCopy = this.utilsService.deepCopy(this.iaReports);
        }
    }

    closeForm() {
        this.isClosing = true;
        if (
            !this.iaRegisterInvoiceCopy ||
            this.isSaved ||
            (this.utilsService.isEquivalent(this.iaRegisterInvoice, this.iaRegisterInvoiceCopy) &&
                this.utilsService.isEquivalentArray(this.iaReports, this.iaReportsCopy))
        ) {
            this.closeAll();
            return;
        }
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.modalRef = this.modalService.open(this.content, { backdrop: 'static' });
    }

    canDeactive() {
        if (this.isClosing || !this.iaRegisterInvoiceCopy || this.isClosed || this.isSaved) {
            return true;
        }
        return (
            this.utilsService.isEquivalent(this.iaRegisterInvoiceCopy, this.iaRegisterInvoice) &&
            this.utilsService.isEquivalentArray(this.iaReports, this.iaReportsCopy)
        );
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('DKSD_DataSession'));
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

    changeFiles(target: any) {
        if (target && target.files[0]) {
            this.file = target.files[0];
            this.iaRegisterInvoice.attachFileName = this.file.name;
            const that = this;
            const reader = new FileReader();
            reader.onload = function() {
                that.iaRegisterInvoice.attachFileContent = btoa(this.result);
            };
            reader.readAsBinaryString(target.files[0]);
        }
    }

    downloadAttachFile() {
        this.iaRegisterService.downloadAttachFile(this.iaRegisterInvoice.id).subscribe(res => {
            if (res.body.size) {
                const blob = new Blob([res.body]);
                const fileURL = URL.createObjectURL(blob);
                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                link.setAttribute('download', res.headers.get('fileName'));
                link.href = fileURL;
                link.click();
            }
        });
    }
}
