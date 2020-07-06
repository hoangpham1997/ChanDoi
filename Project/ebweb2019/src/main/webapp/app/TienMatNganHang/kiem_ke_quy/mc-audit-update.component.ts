import { AfterViewChecked, Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { IMCAudit, MCAudit } from 'app/shared/model/mc-audit.model';
import { MCAuditService } from './mc-audit.service';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { IMCAuditDetails, MCAuditDetails } from 'app/shared/model/mc-audit-details.model';
import { IMCAuditDetailMember, MCAuditDetailMember } from 'app/shared/model/mc-audit-detail-member.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { DATE_FORMAT } from 'app/shared';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import * as moment from 'moment';
import { CategoryName, SO_LAM_VIEC } from 'app/app.constants';
import { Principal } from 'app/core';
import { NgbModal, NgbModalRef, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { MCPayment } from 'app/shared/model/mc-payment.model';
import { MCReceipt } from 'app/shared/model/mc-receipt.model';
import { Irecord } from 'app/shared/model/record';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-mc-audit-update',
    templateUrl: './mc-audit-update.component.html',
    styleUrls: ['./mc-audit-update.component.css']
})
export class MCAuditUpdateComponent extends BaseComponent implements OnInit, AfterViewChecked {
    @ViewChild('content') content;
    @ViewChild('question') question;
    @ViewChild('info') info;
    mCAudit: IMCAudit;
    private TYPE_GROUP_MC_AUDIT = 18;
    account: any;
    DDSo_TienVND: number;
    DDSo_NgoaiTe: number;
    newmCAuditDetails: IMCAuditDetails[];
    newmCAuditDetailMember: IMCAuditDetailMember[];
    mCAuditCopy: IMCAudit;
    mCAuditDetailsCopy: IMCAuditDetails[];
    mCAuditDetailMemberCopy: IMCAuditDetailMember[];
    isSaving: boolean;
    isEditForm: boolean;
    clickEdit: boolean;
    organizationUnits: IOrganizationUnit[];
    currencys: ICurrency[];
    employees: IAccountingObject[];
    dateDp: any;
    isEdit: boolean;
    isShowDetail = { value: false };
    isCreateUrl: boolean;
    predicate: any;
    reverse: any;
    dataSession: IDataSessionStorage;
    page: number;
    itemsPerPage: number;
    pageCount: number;
    totalItems: number;
    rowNum: number;
    searchData: string;
    searchDataSearch: any;
    viewVouchersSelected: any[];
    modalRef: NgbModalRef;
    eventSubscriber: Subscription;
    contextMenu: ContextMenu;
    isCompare: boolean;
    isClose: boolean;
    mCPayment: MCPayment;
    mCReceipt: MCReceipt;
    voucherNo: string;
    isViewFromRef: boolean;
    checkData: boolean;
    isFirstReason: boolean;
    currencyAccount: string;
    gridNumber: number;
    indexFocusDetailRow: any;
    indexFocusDetailCol: any;
    idIndex: any;
    select: number;

    ROLE_KiemKeQuy = ROLE.KiemKeQuy_Xem;
    ROLE_Them = ROLE.KiemKeQuy_Them;
    ROLE_Sua = ROLE.KiemKeQuy_Sua;
    ROLE_Xoa = ROLE.KiemKeQuy_Xoa;
    ROLE_In = ROLE.KiemKeQuy_In;
    constructor(
        private jhiAlertService: JhiAlertService,
        private refModalService: RefModalService,
        private mCAuditService: MCAuditService,
        private branchIDService: OrganizationUnitService,
        private currencyIDService: CurrencyService,
        private accountingObjectIDService: AccountingObjectService,
        private activatedRoute: ActivatedRoute,
        public utilsService: UtilsService,
        private gLService: GeneralLedgerService,
        private router: Router,
        private toastr: ToastrService,
        private translate: TranslateService,
        private principal: Principal,
        private eventManager: JhiEventManager,
        private modalService: NgbModal
    ) {
        super();
        this.isViewFromRef = window.location.href.includes('/from-ref');
    }

    ngOnInit() {
        this.voucherNo = '';
        this.isSaving = false;
        this.isClose = false;
        this.checkData = false;
        this.gridNumber = 0;
        this.employees = [];
        this.activatedRoute.data.subscribe(({ mCAudit }) => {
            this.principal.identity().then(account => {
                this.account = account;
                this.getSessionData();
                if (this.account) {
                    this.currencyAccount = this.account.organizationUnit.currencyID;
                    this.mCAudit = mCAudit;
                    this.viewVouchersSelected = mCAudit.viewVouchers ? mCAudit.viewVouchers : [];
                    if (this.mCAudit.id !== undefined) {
                        if (this.account.organizationUnit && this.account.organizationUnit.currencyID) {
                            this.currencyIDService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
                                this.currencys = res.body.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
                            });
                        }
                        this.isEdit = false;
                        this.isEditForm = true;
                        this.checkData = true;
                        this.newmCAuditDetails = this.mCAudit.mcAuditDetails.sort((n1, n2) => {
                            return n1.orderPriority - n2.orderPriority;
                        });
                        this.newmCAuditDetailMember = this.mCAudit.mcAuditDetailMembers.sort((n1, n2) => {
                            return n1.orderPriority - n2.orderPriority;
                        });
                    } else {
                        this.isFirstReason = true;
                        this.translate.get(['ebwebApp.mCAudit.descriptionValue']).subscribe((res: any) => {
                            this.mCAudit.description =
                                res['ebwebApp.mCAudit.descriptionValue'] +
                                moment(new Date())
                                    .format('DD-MM-YYYY')
                                    .toString();
                        });
                        this.mCAudit.date = moment(new Date(), DATE_FORMAT);
                        this.mCAudit.auditDate = moment(new Date(), DATE_FORMAT);
                        this.mCAudit.typeLedger = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                        if (this.account.organizationUnit && this.account.organizationUnit.currencyID) {
                            this.mCAudit.currencyID = this.account.organizationUnit.currencyID;
                            this.currencyIDService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
                                this.currencys = res.body.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
                                this.getTotalBalanceAmount(this.mCAudit);
                            });
                        }
                        this.mCAudit.totalAuditAmount = 0;
                        this.utilsService
                            .getGenCodeVoucher({
                                typeGroupID: this.TYPE_GROUP_MC_AUDIT,
                                displayOnBook: 2
                            })
                            .subscribe((res: HttpResponse<string>) => {
                                this.mCAudit.no = res.body;
                            });
                        this.isEdit = true;
                        this.isEditForm = false;
                        this.newmCAuditDetails = [];
                        this.newmCAuditDetailMember = [];
                        this.mCAudit.mcAuditDetails = [];
                        this.mCAudit.mcAuditDetailMembers = [];
                    }
                    this.isCreateUrl = window.location.href.includes('/mc-audit/new');
                }
            });
        });
        this.accountingObjectIDService.getAllDTO().subscribe(
            (res: HttpResponse<IAccountingObject[]>) => {
                this.employees = res.body
                    .filter(n => n.isActive && n.isEmployee)
                    .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.organizationUnits = [];
        this.mCReceipt = {};
        this.mCPayment = {};
        this.registerRef();
        this.registerAddNewRow();
        this.registerDeleteRow();
        this.registerCopyRow();
        this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscriber = this.eventManager.subscribe('saveAndNewSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
            this.utilsService.setShowPopup(false);
        });
        this.contextMenu = new ContextMenu();
        this.eventSubscriber = this.eventManager.subscribe('closePopup', response => {
            this.utilsService.setShowPopup(response.content);
        });
    }

    registerRef() {
        this.eventSubscriber = this.eventManager.subscribe('selectViewVoucher', response => {
            this.viewVouchersSelected = response.content;
        });
    }

    actionFocus(indexCol, indexRow, id) {
        this.indexFocusDetailCol = indexCol;
        this.indexFocusDetailRow = indexRow;
        this.idIndex = id;
    }

    previousState() {
        if (this.isCreateUrl) {
            this.router.navigate(['mc-audit']);
        } else {
            window.history.back();
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.KiemKeQuy_Them, ROLE.KiemKeQuy_Sua])
    save(isSave = false) {
        event.preventDefault();
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.isEdit && !this.utilsService.isShowPopup) {
            this.isSaving = true;
            if (this.isCreateUrl && this.mCAudit.id !== undefined) {
                this.mCAudit.id = undefined;
            }
            this.mCAudit.typeID = 180;
            this.mCAudit.exchangeRate = this.currencys.find(n => n.currencyCode === this.mCAudit.currencyID).exchangeRate;
            this.mCAudit.mcAuditDetails = this.newmCAuditDetails;
            this.mCAudit.mcAuditDetailMembers = this.newmCAuditDetailMember;
            this.mCAudit.viewVouchers = this.viewVouchersSelected;
            if (!this.checkErr()) {
                return;
            }
            if (this.mCAudit.id !== undefined) {
                this.subscribeToSaveResponse(this.mCAuditService.update(this.mCAudit), this.question);
            } else {
                this.subscribeToSaveResponse(this.mCAuditService.create(this.mCAudit), this.question);
            }
        }
    }

    viewVoucher() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.isCompare) {
            this.mCAuditService
                .findMCReceiptByMCAuditID({
                    ID: this.mCAudit.id
                })
                .subscribe((res: HttpResponse<MCReceipt>) => {
                    console.log(res.body);
                    this.mCReceipt = res.body;
                    this.router.navigate(['/mc-receipt', res.body.id, 'edit']);
                });
        } else {
            this.mCAuditService
                .findMCPaymentByMCAuditID({
                    ID: this.mCAudit.id
                })
                .subscribe((res: HttpResponse<MCPayment>) => {
                    console.log(res.body);
                    this.mCPayment = res.body;
                    this.router.navigate(['/mc-payment', res.body.id, 'edit']);
                });
        }
    }

    closeQuestion() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    private checkErr() {
        if (this.mCAudit.no === undefined || this.mCAudit.no === '') {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBDeposit.voucherNumber') +
                    ' ' +
                    this.translate.instant('ebwebApp.mBDeposit.isNotNull') +
                    '\n',
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
        }
        if (this.mCAudit.no === undefined || this.mCAudit.no === '') {
            this.toastr.error(
                this.translate.instant('ebwebApp.mCAudit.no') + ' ' + this.translate.instant('ebwebApp.mBDeposit.isNotNull'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
        }
        if (this.mCAudit.date === undefined) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBDeposit.date') + ' ' + this.translate.instant('ebwebApp.mBDeposit.isNotNull'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
        }
        if (this.mCAudit.auditDate === undefined) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mCAudit.auditDate') + ' ' + this.translate.instant('ebwebApp.mBDeposit.isNotNull'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
        }
        if (this.mCAudit.currencyID === undefined || this.mCAudit.currencyID === '') {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBDeposit.detail.currencyType') +
                    ' ' +
                    this.translate.instant('ebwebApp.mBDeposit.isNotNull'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
        }
        if (this.mCAudit.mcAuditDetails.length === 0) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBDeposit.home.details') + ' ' + this.translate.instant('ebwebApp.mBDeposit.isNotNull'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
        }
        if (!this.utilsService.checkNoBook(this.mCAudit.no, this.account)) {
            return false;
        }
        return true;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.KiemKeQuy_Them])
    addNew($event?) {
        event.preventDefault();
        if (!this.isEdit && !this.utilsService.isShowPopup) {
            this.router.navigate(['mc-audit', 'new']);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.KiemKeQuy_Them])
    saveAndNew() {
        event.preventDefault();
        if (this.isEdit && !this.utilsService.isShowPopup) {
            this.isSaving = true;
            this.mCAudit.typeID = 180;
            this.mCAudit.mcAuditDetails = this.newmCAuditDetails;
            this.mCAudit.mcAuditDetailMembers = this.newmCAuditDetailMember;
            if (!this.checkErr()) {
                return;
            }
            if (this.mCAudit.id !== undefined && this.mCAudit.id !== null) {
                this.subscribeToSaveAndAddResponse(this.mCAuditService.update(this.mCAudit));
            } else {
                this.subscribeToSaveAndAddResponse(this.mCAuditService.create(this.mCAudit));
            }
            this.router.navigate(['mc-audit', 'new']);
        }
    }

    closeForm() {
        if (this.checkData && this.dataSession && !this.utilsService.isShowPopup) {
            if (!this.page) {
                window.history.back();
            }
            this.dataSession.page = this.page;
            this.dataSession.itemsPerPage = this.itemsPerPage;
            this.dataSession.pageCount = this.pageCount;
            this.dataSession.totalItems = this.totalItems;
            this.dataSession.rowNum = this.rowNum;
            this.dataSession.predicate = this.predicate;
            this.dataSession.reverse = this.reverse;
            sessionStorage.setItem('dataSearchMCAudit', JSON.stringify(this.dataSession));
            if (this.isEdit && !this.checkValidateClose()) {
                this.modalRef = this.modalService.open(this.content, { backdrop: 'static' });
            } else {
                this.isClose = true;
                this.router.navigate(['mc-audit']);
            }
        } else if (!this.utilsService.isShowPopup) {
            if (this.isEdit && !this.checkValidateClose()) {
                this.modalRef = this.modalService.open(this.content, { backdrop: 'static' });
            } else {
                this.isClose = true;
                this.router.navigate(['mc-audit']);
            }
        }
    }

    checkValidateClose() {
        if (
            this.isEdit &&
            (!this.utilsService.isEquivalent(this.mCAudit, this.mCAuditCopy) ||
                !this.utilsService.isEquivalentArray(this.newmCAuditDetails, this.mCAuditDetailsCopy) ||
                !this.utilsService.isEquivalentArray(this.newmCAuditDetailMember, this.mCAuditDetailMemberCopy))
        ) {
            return true;
        } else {
            return false;
        }
    }

    close() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isClose = true;
        this.router.navigate(['mc-audit']);
        this.dataSession = JSON.parse(sessionStorage.getItem('dataSearchMCAudit'));
        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
        } else {
            this.dataSession = null;
        }
    }

    closeContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    closeModalRef() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>, question) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.mCAudit.id = res.body.mcAudit.id;
                    if (this.mCAudit.differAmount > 0) {
                        this.mCAuditService
                            .findMCReceiptByMCAuditID({
                                ID: this.mCAudit.id
                            })
                            .subscribe((res1: HttpResponse<MCReceipt>) => {
                                console.log(res1.body);
                                this.mCReceipt = res1.body;
                                this.voucherNo = this.mCReceipt.noFBook ? this.mCReceipt.noFBook : this.mCReceipt.noMBook;
                                if (this.mCAudit.differAmount !== 0) {
                                    this.isCompare = this.mCAudit.differAmount > 0;
                                    this.modalRef = this.modalService.open(question, { backdrop: 'static' });
                                }
                            });
                    } else if (this.mCAudit.differAmount < 0) {
                        this.mCAuditService
                            .findMCPaymentByMCAuditID({
                                ID: this.mCAudit.id
                            })
                            .subscribe((res1: HttpResponse<MCPayment>) => {
                                console.log(res1.body);
                                this.mCPayment = res1.body;
                                this.voucherNo = this.mCPayment.noFBook ? this.mCPayment.noFBook : this.mCPayment.noMBook;
                                if (this.mCAudit.differAmount !== 0) {
                                    this.isCompare = this.mCAudit.differAmount > 0;
                                    this.modalRef = this.modalService.open(question, { backdrop: 'static' });
                                }
                            });
                    }
                    this.onSaveSuccess();
                    this.router.navigate(['./mc-audit', this.mCAudit.id, 'edit']);
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private noVoucherExist() {
        this.toastr.error(
            this.translate.instant('global.data.noVocherAlreadyExist'),
            this.translate.instant('ebwebApp.mCReceipt.home.message')
        );
    }

    private subscribeToSaveAndAddResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe((res: HttpResponse<any>) => {
            if (res.body.status === 0) {
                this.onSaveAndAddSuccessTemp();
                this.mCAudit.id = res.body.mcAudit.id;
                if (this.mCAudit.differAmount > 0) {
                    this.mCAuditService
                        .findMCReceiptByMCAuditID({
                            ID: this.mCAudit.id
                        })
                        .subscribe((res1: HttpResponse<MCReceipt>) => {
                            console.log(res1.body);
                            this.mCReceipt = res1.body;
                            const record_: Irecord = {};
                            record_.id = this.mCReceipt.id;
                            record_.typeID = this.mCReceipt.typeID;
                            this.gLService.record(record_).subscribe((res2: HttpResponse<Irecord>) => {
                                if (res2.body.success) {
                                    this.mCReceipt.recorded = true;
                                }
                            });
                        });
                } else if (this.mCAudit.differAmount < 0) {
                    this.mCAuditService
                        .findMCPaymentByMCAuditID({
                            ID: this.mCAudit.id
                        })
                        .subscribe((res1: HttpResponse<MCPayment>) => {
                            console.log(res1.body);
                            this.mCPayment = res1.body;
                            const record_: Irecord = {};
                            record_.id = this.mCPayment.id;
                            record_.typeID = this.mCPayment.typeID;
                            this.gLService.record(record_).subscribe((res2: HttpResponse<Irecord>) => {
                                if (res2.body.success) {
                                    this.mCPayment.recorded = true;
                                }
                            });
                        });
                }
                this.copy();
                this.router.navigate(['/mc-audit', res.body.mcAudit.id, 'edit']).then(() => {
                    this.router.navigate(['/mc-audit', 'new']);
                });
            } else if (res.body.status === 1) {
                this.noVoucherExist();
            }
        });
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.isEdit = false;
        this.isCreateUrl = false;
        this.toastr.success(
            this.translate.instant('ebwebApp.mCReceipt.home.saveSuccess'),
            this.translate.instant('ebwebApp.mBDeposit.message')
        );
    }

    private onSaveAndAddSuccessTemp() {
        this.isSaving = false;
        this.toastr.success(
            this.translate.instant('ebwebApp.mCReceipt.home.saveSuccess'),
            this.translate.instant('ebwebApp.mBDeposit.message')
        );
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(
            this.translate.instant('ebwebApp.mBDeposit.error'),
            this.translate.instant('ebwebApp.mBDeposit.message')
        );
    }

    trackAccountingObjectById(index: number, item: IAccountingObject) {
        return item.id;
    }

    registerAddNewRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            this.addNewRow(this.select, true);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerDeleteRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            this.keyPress(this.contextMenu.selectedData, this.select);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerCopyRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            this.copyRow(this.contextMenu.selectedData, this.select, true);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    addNewRow(select: number, isRightClick?) {
        if (this.isEdit) {
            if (select === 0) {
                let length = 0;
                if (isRightClick) {
                    this.newmCAuditDetails.splice(this.indexFocusDetailRow + 1, 0, {});
                    length = this.indexFocusDetailRow + 2;
                } else {
                    this.newmCAuditDetails.push({});
                    length = this.newmCAuditDetails.length;
                }
                this.newmCAuditDetails[length - 1].amount = 0;
                this.newmCAuditDetails[length - 1].valueOfMoney = 0;
                this.newmCAuditDetails[length - 1].quantity = 0;
                if (isRightClick && this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                    const id = this.idIndex;
                    const row = this.indexFocusDetailRow + 1;
                    this.indexFocusDetailRow = row;
                    setTimeout(function() {
                        const element: HTMLElement = document.getElementById(id + row);
                        if (element) {
                            element.focus();
                        }
                    }, 0);
                } else {
                    const nameTag: string = event.srcElement.id;
                    const idx: number = this.newmCAuditDetails.length - 1;
                    const nameTagIndex: string = nameTag + String(idx);
                    setTimeout(function() {
                        const element: HTMLElement = document.getElementById(nameTagIndex);
                        if (element) {
                            element.focus();
                        }
                    }, 0);
                }
            } else if (select === 1) {
                let length = 0;
                if (isRightClick) {
                    this.newmCAuditDetails.splice(this.indexFocusDetailRow + 1, 0, {});
                    length = this.indexFocusDetailRow + 2;
                } else {
                    this.newmCAuditDetailMember.push({});
                    length = this.newmCAuditDetails.length;
                }
                if (isRightClick && this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                    const id = this.idIndex;
                    const row = this.indexFocusDetailRow + 1;
                    this.indexFocusDetailRow = row;
                    setTimeout(function() {
                        const element: HTMLElement = document.getElementById(id + row);
                        if (element) {
                            element.focus();
                        }
                    }, 0);
                } else {
                    const nameTag: string = event.srcElement.id;
                    const idx: number = this.newmCAuditDetailMember.length - 1;
                    const nameTagIndex: string = nameTag + String(idx);
                    setTimeout(function() {
                        const element: HTMLElement = document.getElementById(nameTagIndex);
                        if (element) {
                            element.focus();
                        }
                    }, 0);
                }
            }
        }
    }

    copyRow(detail, select, isRigthClick?) {
        if (!this.getSelectionText() || isRigthClick) {
            const detailCopy: any = Object.assign({}, detail);
            detailCopy.id = null;
            if (select === 0) {
                this.newmCAuditDetails.push(detailCopy);
                if (this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                    const id = this.idIndex;
                    const row = this.newmCAuditDetails.length - 1;
                    this.indexFocusDetailRow = row;
                    setTimeout(function() {
                        const element: HTMLElement = document.getElementById(id + row);
                        if (element) {
                            element.focus();
                        }
                    }, 0);
                }
            } else {
                this.newmCAuditDetailMember.push(detailCopy);
                if (this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                    const id = this.idIndex;
                    const row = this.newmCAuditDetailMember.length - 1;
                    this.indexFocusDetailRow = row;
                    setTimeout(function() {
                        const element: HTMLElement = document.getElementById(id + row);
                        if (element) {
                            element.focus();
                        }
                    }, 0);
                }
            }
        }
    }

    onRightClick($event, data, selectedData, isNew, isDelete, select, isCopy) {
        this.contextMenu.isNew = isNew;
        this.contextMenu.isDelete = isDelete;
        this.contextMenu.isCopy = isCopy;
        this.contextMenu.isShow = true;
        this.contextMenu.event = $event;
        this.contextMenu.data = data;
        this.contextMenu.selectedData = selectedData;
        this.select = select;
    }

    keyPress(detail, select: number) {
        if (this.isEdit) {
            if (select === 0) {
                this.newmCAuditDetails.splice(this.newmCAuditDetails.indexOf(detail), 1);
                if (this.indexFocusDetailCol !== undefined && this.indexFocusDetailCol !== null) {
                    // vì còn trường hợp = 0
                    if (this.newmCAuditDetails.length > 0) {
                        let row = 0;
                        if (this.indexFocusDetailRow > this.newmCAuditDetails.length - 1) {
                            row = this.indexFocusDetailRow - 1;
                        } else {
                            row = this.indexFocusDetailRow;
                        }
                        const id = this.idIndex;
                        setTimeout(function() {
                            const element: HTMLElement = document.getElementById(id + row);
                            if (element) {
                                element.focus();
                            }
                        }, 0);
                    }
                }
            } else if (select === 1) {
                this.newmCAuditDetailMember.splice(this.newmCAuditDetailMember.indexOf(detail), 1);
                if (this.indexFocusDetailCol !== undefined && this.indexFocusDetailCol !== null) {
                    // vì còn trường hợp = 0
                    if (this.newmCAuditDetailMember.length > 0) {
                        let row = 0;
                        if (this.indexFocusDetailRow > this.newmCAuditDetailMember.length - 1) {
                            row = this.indexFocusDetailRow - 1;
                        } else {
                            row = this.indexFocusDetailRow;
                        }
                        const id = this.idIndex;
                        setTimeout(function() {
                            const element: HTMLElement = document.getElementById(id + row);
                            if (element) {
                                element.focus();
                            }
                        }, 0);
                    }
                }
            } else if (select === 2) {
                this.viewVouchersSelected.splice(this.viewVouchersSelected.indexOf(detail), 1);
            }
        }
    }

    calculateAmount(mCAuditDetail: MCAuditDetails) {
        mCAuditDetail.amount = mCAuditDetail.valueOfMoney * mCAuditDetail.quantity;
        const total = this.newmCAuditDetails.reduce(function(prev, cur) {
            return prev + cur.amount;
        }, 0);
        this.mCAudit.totalAuditAmount = total;
        this.mCAudit.differAmount = this.mCAudit.totalAuditAmount - this.mCAudit.totalBalanceAmount;
    }

    calculateTotalAmount(mCAuditDetails: MCAuditDetails[]) {
        const total = mCAuditDetails.reduce(function(prev, cur) {
            return prev + cur.amount;
        }, 0);
        this.mCAudit.totalAuditAmount = total;
    }

    calculateDifferAmount(mCAudit: MCAudit) {
        mCAudit.differAmount = mCAudit.totalAuditAmount - mCAudit.totalBalanceAmount;
    }

    getTotalBalanceAmount(mCAudit: MCAudit, isDate?) {
        if (this.currencys) {
            this.mCAudit.exchangeRate = this.currencys.find(n => n.currencyCode === this.mCAudit.currencyID).exchangeRate;
        }
        if (this.isEdit) {
            if (this.isFirstReason) {
                this.translate.get(['ebwebApp.mCAudit.descriptionValue']).subscribe((res: any) => {
                    this.mCAudit.description = res['ebwebApp.mCAudit.descriptionValue'] + mCAudit.auditDate.format('DD-MM-YYYY').toString();
                });
            }
            this.gLService
                .getSoDuSoQuy({
                    auditDate: mCAudit.auditDate != null && mCAudit.auditDate.isValid() ? mCAudit.auditDate.format(DATE_FORMAT) : null,
                    currencyID: mCAudit.currencyID
                })
                .subscribe((res: HttpResponse<number>) => {
                    mCAudit.totalBalanceAmount = res.body;
                    mCAudit.differAmount = mCAudit.totalAuditAmount - mCAudit.totalBalanceAmount;
                });
        }
        if (isDate) {
            this.mCAudit.date = this.mCAudit.auditDate;
        }
    }

    selectEmployeeMC(mCAuditDetailMember: MCAuditDetailMember) {
        mCAuditDetailMember.accountingObjectName = mCAuditDetailMember.accountingObject.accountingObjectName;
        mCAuditDetailMember.accountingObjectTitle = mCAuditDetailMember.accountingObject.contactTitle;
        if (mCAuditDetailMember.accountingObject.departmentId !== null) {
            this.branchIDService
                .find(mCAuditDetailMember.accountingObject.departmentId)
                .subscribe((res: HttpResponse<IOrganizationUnit>) => {
                    console.log(res.body);
                    mCAuditDetailMember.organizationUnit = res.body;
                });
        } else {
            mCAuditDetailMember.organizationUnit = null;
        }
    }

    changeReason() {
        this.isFirstReason = false;
    }

    move(direction: number) {
        this.isClose = true;
        if ((direction === -1 && this.rowNum === 1) || (direction === 1 && this.rowNum === this.totalItems)) {
            return;
        }
        this.rowNum += direction;
        const searchDataIndex = JSON.parse(this.searchData);
        this.searchDataSearch = {
            currency: searchDataIndex.currencyID ? searchDataIndex.currencyID : '',
            fromDate: searchDataIndex.fromDate ? searchDataIndex.fromDate : '',
            toDate: searchDataIndex.toDate ? searchDataIndex.toDate : '',
            searchValue: searchDataIndex.textSearch ? searchDataIndex.textSearch : ''
        };
        // goi service get by row num
        return this.mCAuditService
            .findByRowNum({
                fromDate: searchDataIndex.fromDate ? searchDataIndex.fromDate : '',
                toDate: searchDataIndex.toDate ? searchDataIndex.toDate : '',
                currencyID: searchDataIndex.currency ? searchDataIndex.currency : '',
                keySearch: searchDataIndex.searchValue ? searchDataIndex.searchValue : '',
                id: this.mCAudit.id,
                rowNum: this.rowNum
            })
            .subscribe(
                (res: HttpResponse<IMCAudit>) => {
                    this.mCAudit = res.body;
                    this.getDetailByID(this.mCAudit);
                    this.dataSession.page = this.page;
                    this.dataSession.rowNum = this.rowNum;
                    sessionStorage.setItem('dataSearchMCAudit', JSON.stringify(this.dataSession));
                },
                (res: HttpErrorResponse) => {
                    this.handleError(res);
                    this.getSessionData();
                }
            );
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('dataSearchMCAudit'));
        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.pageCount = this.dataSession.pageCount;
            this.totalItems = this.dataSession.totalItems;
            this.rowNum = this.dataSession.rowNum;
            this.searchData = this.dataSession.searchVoucher;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
        } else {
            this.dataSession = null;
        }
    }

    handleError(err) {
        this.isSaving = false;
    }

    getDetailByID(item: IMCAudit) {
        this.mCAuditService.getDetailByID({ mCAuditID: item.id }).subscribe(ref => {
            this.newmCAuditDetails = ref.body.mcAuditDetails;
            this.newmCAuditDetailMember = ref.body.mcAuditDetailMembers;
            this.viewVouchersSelected = ref.body.refVouchers;
        });
    }

    @ebAuth(['ROLE_ADMIN', ROLE.KiemKeQuy_Them])
    copyAndNew() {
        event.preventDefault();
        if (!this.isEdit && !this.utilsService.isShowPopup) {
            this.mCAudit.id = undefined;
            for (let i = 0; i < this.mCAudit.mcAuditDetails.length; i++) {
                this.mCAudit.mcAuditDetails[i].id = undefined;
            }
            for (let i = 0; i < this.mCAudit.mcAuditDetailMembers.length; i++) {
                this.mCAudit.mcAuditDetailMembers[i].id = undefined;
            }
            this.mCAudit.viewVouchers = [];
            this.viewVouchersSelected = [];
            this.utilsService
                .getGenCodeVoucher({
                    typeGroupID: this.TYPE_GROUP_MC_AUDIT,
                    displayOnBook: 2
                })
                .subscribe((res: HttpResponse<string>) => {
                    this.mCAudit.no = res.body;
                });
            this.isSaving = false;
            this.isEdit = true;
        }
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    public beforeChange($event: NgbTabChangeEvent) {
        if ($event.nextId === 'ngb-tab-0') {
            this.gridNumber = 0;
        } else if ($event.nextId === 'ngb-tab-1') {
            this.gridNumber = 1;
        } else if ($event.nextId === 'refdoc') {
            this.gridNumber = 2;
        } else if ($event.nextId === 'reference') {
            $event.preventDefault();
            if (this.isEdit) {
                this.modalRef = this.refModalService.open(this.viewVouchersSelected);
            }
        }
    }

    disableContextMenu() {
        this.contextMenu.isShow = false;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.KiemKeQuy_Sua])
    edit() {
        event.preventDefault();
        if (!this.isEdit && !this.utilsService.isShowPopup) {
            this.clickEdit = true;
            if (this.mCAudit.differAmount > 0) {
                this.mCAuditService
                    .findMCReceiptByMCAuditID({
                        ID: this.mCAudit.id
                    })
                    .subscribe((res: HttpResponse<MCReceipt>) => {
                        console.log(res.body);
                        this.mCReceipt = res.body;
                        if (this.mCReceipt !== undefined && this.mCReceipt !== null) {
                            this.modalRef = this.modalService.open(this.info, { backdrop: 'static' });
                        } else {
                            this.isEdit = true;
                            this.copy();
                        }
                    });
            } else if (this.mCAudit.differAmount < 0) {
                this.mCAuditService
                    .findMCPaymentByMCAuditID({
                        ID: this.mCAudit.id
                    })
                    .subscribe((res: HttpResponse<MCPayment>) => {
                        console.log(res.body);
                        this.mCPayment = res.body;
                        if (this.mCPayment !== undefined && this.mCPayment !== null) {
                            this.modalRef = this.modalService.open(this.info, { backdrop: 'static' });
                        } else {
                            this.isEdit = true;
                            this.copy();
                        }
                    });
            } else {
                this.isEdit = true;
                this.copy();
            }
        }
    }

    copy() {
        this.mCAuditCopy = Object.assign({}, this.mCAudit);
        this.mCAuditDetailsCopy = this.newmCAuditDetails.map(object => ({ ...object }));
        this.mCAuditDetailMemberCopy = this.newmCAuditDetailMember.map(object => ({ ...object }));
    }

    @ebAuth(['ROLE_ADMIN', ROLE.KiemKeQuy_Xoa])
    delete() {
        event.preventDefault();
        if (!this.isEdit && !this.utilsService.isShowPopup) {
            this.clickEdit = false;
            if (this.mCAudit.differAmount > 0) {
                this.mCAuditService
                    .findMCReceiptByMCAuditID({
                        ID: this.mCAudit.id
                    })
                    .subscribe((res: HttpResponse<MCReceipt>) => {
                        console.log(res.body);
                        this.mCReceipt = res.body;
                        if (this.mCReceipt !== undefined && this.mCReceipt !== null) {
                            this.modalRef = this.modalService.open(this.info, { backdrop: 'static' });
                        } else {
                            this.router.navigate(['/mc-audit', { outlets: { popup: this.mCAudit.id + '/delete' } }]);
                        }
                    });
            } else if (this.mCAudit.differAmount < 0) {
                this.mCAuditService
                    .findMCPaymentByMCAuditID({
                        ID: this.mCAudit.id
                    })
                    .subscribe((res: HttpResponse<MCPayment>) => {
                        console.log(res.body);
                        this.mCPayment = res.body;
                        if (this.mCPayment !== undefined && this.mCPayment !== null) {
                            this.modalRef = this.modalService.open(this.info, { backdrop: 'static' });
                        } else {
                            this.router.navigate(['/mc-audit', { outlets: { popup: this.mCAudit.id + '/delete' } }]);
                        }
                    });
            } else {
                this.router.navigate(['/mc-audit', { outlets: { popup: this.mCAudit.id + '/delete' } }]);
            }
        }
    }

    deleteVoucher() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.mCAuditService.delete(this.mCAudit.id).subscribe(response => {
            this.toastr.success(
                this.translate.instant('ebwebApp.mBDeposit.deleteSuccessful'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            window.history.back();
        });
    }

    editVoucher() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.mCAuditService.edit(this.mCAudit.id).subscribe(response => {
            this.toastr.success(
                this.translate.instant('ebwebApp.mBDeposit.deleteSuccessful'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            this.isEdit = true;
        });
    }

    /*
    * hàm ss du lieu 2 object và 2 mảng object
    * return true: neu tat ca giong nhau
    * return fale: neu 1 trong cac object ko giong nhau
    * */
    canDeactive(): boolean {
        if (this.isEdit && !this.isSaving && !this.isClose) {
            return (
                this.utilsService.isEquivalent(this.mCAudit, this.mCAuditCopy) &&
                this.utilsService.isEquivalentArray(this.newmCAuditDetails, this.mCAuditDetailsCopy) &&
                this.utilsService.isEquivalentArray(this.newmCAuditDetailMember, this.mCAuditDetailMemberCopy)
            );
        }
        return true;
    }

    exportPdf(isDownload, typeReports: number) {
        this.utilsService.getCustomerReportPDF(
            {
                id: this.mCAudit.id,
                typeID: this.mCAudit.typeID,
                typeReport: typeReports
            },
            isDownload
        );
        if (typeReports === 1) {
            this.toastr.success(
                this.translate.instant('ebwebApp.mBDeposit.printing') + this.translate.instant('ebwebApp.mBDeposit.financialPaper') + '...',
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === 2) {
            this.toastr.success(
                this.translate.instant('ebwebApp.mBDeposit.printing') + this.translate.instant('ebwebApp.mCReceipt.home.title') + '...',
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        }
    }

    addNewRowInGrid() {
        if (this.newmCAuditDetails.length === 0 && this.gridNumber === 0) {
            this.addNewRow(0);
        } else if (this.newmCAuditDetailMember.length === 0 && this.gridNumber === 1) {
            this.addNewRow(1);
        }
    }

    addIfLastInput(i, num) {
        if (num === 0) {
            if (i === this.newmCAuditDetails.length - 1) {
                this.addNewRow(num);
            }
        } else {
            if (i === this.newmCAuditDetailMember.length - 1) {
                this.addNewRow(num);
            }
        }
    }

    ngAfterViewChecked(): void {
        this.disableInput();
    }

    saveDetails(i) {
        this.currentRow = i;
        this.details = this.newmCAuditDetailMember;
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.mCAudit;
    }

    addDataToDetail() {
        this.newmCAuditDetailMember = this.details ? this.details : this.newmCAuditDetailMember;
        this.mCAudit = this.parent ? this.parent : this.mCAudit;
    }

    sum(prop) {
        let total = 0;
        for (let i = 0; i < this.newmCAuditDetails.length; i++) {
            total += this.newmCAuditDetails[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }
}
