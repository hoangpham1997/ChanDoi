import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ICurrency } from 'app/shared/model/currency.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { IBank } from 'app/shared/model/bank.model';
import { CategoryName, SO_LAM_VIEC, SU_DUNG_SO_QUAN_TRI } from 'app/app.constants';
import { Principal } from 'app/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { CurrencyService } from 'app/danhmuc/currency/currency.service';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ICreditCard } from 'app/shared/model/credit-card.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'eb-currency-update',
    templateUrl: './currency-update.component.html',
    styleUrls: ['./currency-update.component.css']
})
export class CurrencyComboboxComponent extends BaseComponent implements OnInit {
    @ViewChild('content') content: TemplateRef<any>;
    private _currency: ICurrency;
    isSaving: boolean;
    isSaveAndCreate: boolean;
    isEditUrl: boolean;
    currencyCode: any;
    modalRef: NgbModalRef;
    isClose: boolean;
    currencyCopy: ICurrency;
    ROLE_LoaiTien_Them = ROLE.DanhMucLoaiTien_Them;
    ROLE_LoaiTien_Sua = ROLE.DanhMucLoaiTien_Sua;
    ROLE_LoaiTien_Xoa = ROLE.DanhMucLoaiTien_Xoa;
    currentAccount: any;
    data: ICurrency;

    constructor(
        private currencyService: CurrencyService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private toastr: ToastrService,
        private translate: TranslateService,
        private principal: Principal,
        public utilsService: UtilsService,
        private modalService: NgbModal,
        private eventManager: JhiEventManager,
        private activeModal: NgbActiveModal
    ) {
        super();
        this.getOnInit();
    }

    ngOnInit() {
        this.isSaving = false;
        this.copy();
        this.reloadCurrency();
        this.currency = this.data ? this.data : {};
        this.currency.formula = this.currency.formula ? this.currency.formula : '*';
    }

    getOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
            if (account) {
                this.currencyCode = this.currentAccount.organizationUnit.currencyID;
                console.log(this.currencyCode);
                if (this.currencyCode === this.currency.currencyCode) {
                    this.currency.exchangeRate = 1;
                }
            }
        });
    }

    previousState() {
        this.router.navigate(['/currency']);
    }

    copy() {
        this.currencyCopy = Object.assign({}, this.currency);
    }

    reloadCurrency() {
        this.currency = {};
    }

    closeEdit(content) {
        if (!this.utilsService.isEquivalent(this.currency, this.currencyCopy)) {
            this.modalRef = this.modalService.open(content, { backdrop: 'static' });
        } else {
            this.close();
        }
    }

    save() {
        event.preventDefault();
        this.isSaveAndCreate = true;
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isSaving = true;
        if (!this.currency.currencyCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.currency.currencyCodeNotNull'),
                this.translate.instant('ebwebApp.currency.message')
            );
            return false;
        }
        if (!this.currency.currencyName) {
            this.toastr.error(
                this.translate.instant('ebwebApp.currency.currencyNameNotNull'),
                this.translate.instant('ebwebApp.currency.message')
            );
            return false;
        }
        if (this.currency.id !== undefined) {
            this.subscribeToSaveResponse(this.currencyService.update(this.currency));
        } else {
            this.currency.isActive = true;
            this.subscribeToSaveResponse(this.currencyService.create(this.currency));
        }
        this.close();
    }

    saveAndCreate() {
        this.isSaveAndCreate = true;
        this.isSaving = true;
        if (!this.currency.currencyCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.currency.currencyCodeNotNull'),
                this.translate.instant('ebwebApp.currency.message')
            );
            return false;
        }
        if (!this.currency.currencyName) {
            this.toastr.error(
                this.translate.instant('ebwebApp.currency.currencyNameNotNull'),
                this.translate.instant('ebwebApp.currency.message')
            );
            return false;
        }
        if (this.currency.id !== undefined) {
            this.subscribeToSaveResponse(this.currencyService.update(this.currency));
        } else {
            this.currency.isActive = true;
            this.subscribeToSaveResponse(this.currencyService.create(this.currency));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveSuccess();
                } else {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.currency.check'),
                        this.translate.instant('ebwebApp.currency.message')
                    );
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.toastr.success(
            this.translate.instant('ebwebApp.currency.insertDataSuccess'),
            this.translate.instant('ebwebApp.currency.message')
        );
        if (!this.isSaveAndCreate) {
            this.eventManager.broadcast({
                name: 'saveSuccess',
                content: { name: CategoryName.LOAI_TIEN, data: this.data }
            });
            this.close();
        } else {
            this.eventManager.broadcast({
                name: 'saveAndNewSuccess',
                content: { name: CategoryName.LOAI_TIEN, data: this.data }
            });
            this.reloadCurrency();
        }
    }

    private onSaveError() {
        this.toastr.error(
            this.translate.instant('ebwebApp.currency.insertDataFailed'),
            this.translate.instant('ebwebApp.currency.message')
        );
        this.isSaving = false;
    }

    get currency() {
        return this._currency;
    }

    set currency(currency: ICurrency) {
        this._currency = currency;
    }

    delete() {
        event.preventDefault();
        this.router.navigate(['/currency', { outlets: { popup: this.currency.id + '/delete' } }]);
    }

    getAlert(): string {
        let alert = '';
        if (this.currency.currencyCode && this.currency.exchangeRate && this.currency.currencyCode) {
            alert =
                '1 ' +
                this.currency.currencyCode +
                ' = 1 ' +
                this.currency.formula +
                ' ' +
                this.currency.exchangeRate +
                ' ' +
                this.currencyCode;
        }
        return alert;
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
            return this.utilsService.isEquivalent(this.currency, this.currencyCopy);
        }
        return true;
    }

    closeForm() {
        event.preventDefault();
        if (this.currencyCode) {
            if (!this.utilsService.isEquivalent(this.currency, this.currencyCode)) {
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
        this.router.navigate(['/currency']);
    }
}
