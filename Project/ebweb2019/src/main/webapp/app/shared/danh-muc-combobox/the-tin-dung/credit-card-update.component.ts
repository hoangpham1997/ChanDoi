import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { ICreditCard } from 'app/shared/model/credit-card.model';
import { IBank } from 'app/shared/model/bank.model';
import { BankService } from 'app/danhmuc/bank';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { IAccountList } from 'app/shared/model/account-list.model';
import { ROLE } from 'app/role.constants';
import { CreditCardService } from 'app/danhmuc/credit-card';
import { CategoryName } from 'app/app.constants';

@Component({
    selector: 'eb-credit-card-update',
    templateUrl: './credit-card-update.component.html',
    styleUrls: ['./credit-card-update.component.css']
})
export class CreditCardComboboxComponent extends BaseComponent implements OnInit {
    @ViewChild('content') content: TemplateRef<any>;
    private _creditCard: ICreditCard;
    isSaving: boolean;
    months: any[];
    years: any[];
    banks: IBank[];
    bankPopup: IBank[];
    listColumnsBank: string[];
    listHeaderColumnsBank: string[];
    isSaveAndCreate: boolean;
    isEditUrl: boolean;
    isClose: boolean;
    modalRef: NgbModalRef;
    creditCardCopy: ICreditCard;
    eventSubscriber: Subscription;

    organizationunits: IOrganizationUnit[];
    ROLE_TheTinDung_Them = ROLE.DanhMucTheTinDung_Them;
    ROLE_TheTinDung_Sua = ROLE.DanhMucTheTinDung_Sua;
    ROLE_TheTinDung_Xoa = ROLE.DanhMucTheTinDung_Xoa;
    data: ICreditCard;

    constructor(
        private translate: TranslateService,
        private toastr: ToastrService,
        private jhiAlertService: JhiAlertService,
        private creditCardService: CreditCardService,
        private bankService: BankService,
        private organizationUnitService: OrganizationUnitService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        public utilsService: UtilsService,
        private modalService: NgbModal,
        private eventManager: JhiEventManager,
        private activeModal: NgbActiveModal
    ) {
        super();
    }

    reloadCreditCard() {
        this.creditCard = {};
    }

    ngOnInit() {
        this.isClose = false;
        this.isSaving = false;
        this.isSaveAndCreate = false;
        this.creditCard = this.data ? this.data : {};
        this.reloadCreditCard();
        this.bankService.getBanks().subscribe(
            (res: HttpResponse<IBank[]>) => {
                this.bankPopup = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.organizationUnitService.query().subscribe(
            (res: HttpResponse<IOrganizationUnit[]>) => {
                this.organizationunits = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.years = [];
        this.months = [];
        for (let i = 1; i <= 12; i++) {
            this.months.push(i);
        }
        for (let i = 1970; i <= 2050; i++) {
            this.years.push(i);
        }
        this.isEditUrl = window.location.href.includes('/edit') || window.location.href.includes('/view');
        this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
        });
        this.eventSubscriber = this.eventManager.subscribe('saveAndNewSuccess', response => {
            this.isCbbSaveAndNew = true;
            this.registerComboboxSave(response);
        });
        this.copy();
    }

    previousState() {
        this.router.navigate(['/credit-card']);
    }

    copy() {
        this.creditCardCopy = Object.assign({}, this.creditCard);
    }

    closeEdit(content) {
        if (!this.utilsService.isEquivalent(this.creditCard, this.creditCardCopy)) {
            this.modalRef = this.modalService.open(content, { backdrop: 'static' });
        } else {
            this.close();
        }
    }

    save() {
        event.preventDefault();
        this.isSaving = true;
        this.isSaveAndCreate = false;
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (!this.creditCard.creditCardNumber) {
            this.toastr.error(
                this.translate.instant('ebwebApp.creditCard.creditCardNumberNotNull'),
                this.translate.instant('ebwebApp.creditCard.message')
            );
            return false;
        }
        if (!this.creditCard.creditCardType) {
            this.toastr.error(
                this.translate.instant('ebwebApp.creditCard.creditCardTypeNotNull'),
                this.translate.instant('ebwebApp.creditCard.message')
            );
            return false;
        }
        if (!this.creditCard.ownerCard) {
            this.toastr.error(
                this.translate.instant('ebwebApp.creditCard.ownerCardNotNull'),
                this.translate.instant('ebwebApp.creditCard.message')
            );
            return false;
        }
        if (!this.creditCard.bankIDIssueCard) {
            this.toastr.error(
                this.translate.instant('ebwebApp.creditCard.bankIDIssueCardNotNull'),
                this.translate.instant('ebwebApp.creditCard.message')
            );
            return false;
        }
        if (!this.check()) {
            this.toastr.error(
                this.translate.instant('ebwebApp.creditCard.invalidCreditCardNumber'),
                this.translate.instant('ebwebApp.creditCard.message')
            );
            return false;
        }
        if (this.creditCard.id !== undefined) {
            this.subscribeToSaveResponse(this.creditCardService.update(this.creditCard));
        } else {
            this.creditCard.isActive = true;
            this.subscribeToSaveResponse(this.creditCardService.create(this.creditCard));
        }
        this.closeContent();
    }

    saveAndCreate() {
        this.isSaveAndCreate = true;
        this.isSaving = true;
        if (!this.creditCard.creditCardNumber) {
            this.toastr.error(
                this.translate.instant('ebwebApp.creditCard.creditCardNumberNotNull'),
                this.translate.instant('ebwebApp.creditCard.message')
            );
            return false;
        }
        if (!this.creditCard.creditCardType) {
            this.toastr.error(
                this.translate.instant('ebwebApp.creditCard.creditCardTypeNotNull'),
                this.translate.instant('ebwebApp.creditCard.message')
            );
            return false;
        }
        if (!this.creditCard.ownerCard) {
            this.toastr.error(
                this.translate.instant('ebwebApp.creditCard.ownerCardNotNull'),
                this.translate.instant('ebwebApp.creditCard.message')
            );
            return false;
        }
        if (!this.creditCard.bankIDIssueCard) {
            this.toastr.error(
                this.translate.instant('ebwebApp.creditCard.bankIDIssueCardNotNull'),
                this.translate.instant('ebwebApp.creditCard.message')
            );
            return false;
        }
        if (!this.check()) {
            this.toastr.error(
                this.translate.instant('ebwebApp.creditCard.invalidCreditCardNumber'),
                this.translate.instant('ebwebApp.creditCard.message')
            );
            return false;
        }
        if (this.creditCard.id !== undefined) {
            this.subscribeToSaveResponse(this.creditCardService.update(this.creditCard));
        } else {
            this.creditCard.isActive = true;
            this.subscribeToSaveResponse(this.creditCardService.create(this.creditCard));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.data = res.body.creditCard;
                    this.onSaveSuccess();
                } else {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.creditCard.check'),
                        this.translate.instant('ebwebApp.creditCard.message')
                    );
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.toastr.success(
            this.translate.instant('ebwebApp.creditCard.insertDataSuccess'),
            this.translate.instant('ebwebApp.creditCard.message')
        );
        if (!this.isSaveAndCreate) {
            this.eventManager.broadcast({
                name: 'saveSuccess',
                content: { name: CategoryName.THE_TIN_DUNG, data: this.data }
            });
            this.close();
        } else {
            this.eventManager.broadcast({
                name: 'saveAndNewSuccess',
                content: { name: CategoryName.THE_TIN_DUNG, data: this.data }
            });
            this.reloadCreditCard();
        }
    }

    private onSaveError() {
        this.toastr.error(
            this.translate.instant('ebwebApp.creditCard.insertDataFailed'),
            this.translate.instant('ebwebApp.creditCard.message')
        );
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackBankById(index: number, item: IBank) {
        return item.id;
    }

    trackOrganizationUnitById(index: number, item: IOrganizationUnit) {
        return item.id;
    }

    get creditCard() {
        return this._creditCard;
    }

    set creditCard(creditCard: ICreditCard) {
        this._creditCard = creditCard;
    }

    clearValue() {
        this.creditCard.creditCardNumber = null;
        this.creditCard.creditCardType = null;
        this.creditCard.ownerCard = null;
        this.creditCard.bankIDIssueCard = null;
        this.creditCard.description = null;
        this.creditCard.exFromMonth = null;
        this.creditCard.exFromYear = null;
        this.creditCard.exToMonth = null;
        this.creditCard.exToYear = null;
        this.creditCard.isActive = false;
    }

    delete() {
        event.preventDefault();
        this.router.navigate(['/credit-card', { outlets: { popup: this.creditCard.id + '/delete' } }]);
    }

    close() {
        this.eventManager.broadcast({
            name: 'closePopup',
            content: null
        });
        this.activeModal.dismiss(false);
    }

    closeContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    canDeactive(): boolean {
        if (!this.isClose) {
            return this.utilsService.isEquivalent(this.creditCard, this.creditCardCopy);
        }
        return true;
    }

    closeForm() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.creditCardCopy) {
            if (!this.utilsService.isEquivalentArray(this.creditCard, this.creditCardCopy)) {
                this.modalRef = this.modalService.open(this.content, { backdrop: 'static' });
                return;
            } else {
                this.closeAll();
                return;
            }
        } else {
            this.copy();
            this.closeAll();
            return;
        }
    }

    closeAll() {
        window.history.back();
    }

    check() {
        const noPattern = new RegExp(
            '^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35d{3})d{11}){1,16}$'
        );
        return noPattern.test(this.creditCard.creditCardNumber);
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.creditCard;
    }

    addDataToDetail() {
        this.creditCards = this.creditCards ? this.details : this.creditCards;
        this.creditCard = this.creditCard ? this.parent : this.creditCard;
        this.bankService.findIdPopup(this.creditCard.id).subscribe(res => {
            this.creditCard.bankIDIssueCard = res.body.id;
        });
        this.creditCard.id = undefined;
    }
}
