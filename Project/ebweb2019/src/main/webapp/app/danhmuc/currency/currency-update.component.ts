import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ICurrency } from 'app/shared/model/currency.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { IBank } from 'app/shared/model/bank.model';
import { SO_LAM_VIEC, SU_DUNG_SO_QUAN_TRI } from 'app/app.constants';
import { Principal } from 'app/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { CurrencyService } from 'app/danhmuc/currency/currency.service';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ICreditCard } from 'app/shared/model/credit-card.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-currency-update',
    templateUrl: './currency-update.component.html',
    styleUrls: ['./currency-update.component.css']
})
export class CurrencyUpdateComponent extends BaseComponent implements OnInit {
    @ViewChild('content') content: TemplateRef<any>;
    private _currency: ICurrency;
    isSaving: boolean;
    isSaveAndCreate: boolean;
    currencyCode: any;
    modalRef: NgbModalRef;
    isClose: boolean;
    currencyCopy: ICurrency;
    ROLE_LoaiTien_Them = ROLE.DanhMucLoaiTien_Them;
    ROLE_LoaiTien_Sua = ROLE.DanhMucLoaiTien_Sua;
    ROLE_LoaiTien_Xoa = ROLE.DanhMucLoaiTien_Xoa;
    ROLE_LoaiTien_Xem = ROLE.DanhMucTheTinDung_Xem;
    arrAuthorities: any[];
    isRoleSua: boolean;
    isRoleThem: boolean;
    currentAccount: any;
    isCreateUrl: boolean;
    isEditUrl: boolean;
    formulaList = [{ id: 0, name: '*' }, { id: 1, name: '/' }];

    constructor(
        private currencyService: CurrencyService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private toastr: ToastrService,
        private translate: TranslateService,
        private principal: Principal,
        public utilsService: UtilsService,
        private modalService: NgbModal
    ) {
        super();
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.arrAuthorities = account.authorities;
            this.isEditUrl = window.location.href.includes('edit');
            this.isCreateUrl = window.location.href.includes('/new');
            this.isRoleSua = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_LoaiTien_Sua) : true;
            this.isRoleThem = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_LoaiTien_Them) : true;
        });
        this.getOnInit();
    }

    ngOnInit() {
        this.isSaving = false;
        this.copy();
        this.currency.formula = this.currency.formula ? this.currency.formula : '*';
    }

    getOnInit() {
        this.activatedRoute.data.subscribe(({ currency }) => {
            this.currency = currency;
            this.principal.identity().then(account => {
                this.currentAccount = account;
                // const currencyID = this.currentAccount.organizationUnit.currencyID;
                if (account) {
                    this.currencyCode = this.currentAccount.organizationUnit.currencyID;
                    console.log(this.currencyCode);
                    if (this.currencyCode === this.currency.currencyCode) {
                        this.currency.exchangeRate = 1;
                    }
                }
            });
        });
    }

    previousState() {
        this.router.navigate(['/currency']);
    }

    copy() {
        this.currencyCopy = Object.assign({}, this.currency);
    }

    closeEdit(content) {
        if (!this.utilsService.isEquivalent(this.currency, this.currencyCopy)) {
            this.modalRef = this.modalService.open(content, { backdrop: 'static' });
        } else {
            this.close();
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucLoaiTien_Them, ROLE.DanhMucLoaiTien_Sua])
    save() {
        event.preventDefault();
        this.isSaveAndCreate = false;
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
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucLoaiTien_Them])
    saveAndNew() {
        event.preventDefault();
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
        this.isClose = true;
        if (this.isSaveAndCreate) {
            this.currency = {};
        } else {
            this.isSaving = false;
            this.previousState();
        }
        this.toastr.success(
            this.translate.instant('ebwebApp.currency.insertDataSuccess'),
            this.translate.instant('ebwebApp.currency.message')
        );
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

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucLoaiTien_Xoa])
    delete() {
        event.preventDefault();
        if (this.modalRef) {
            this.modalRef.close();
        }
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
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isClose = true;
        this.router.navigate(['currency']);
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
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.currencyCopy) {
            if (!this.utilsService.isEquivalent(this.currency, this.currencyCopy)) {
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
