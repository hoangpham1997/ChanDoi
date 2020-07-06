import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Principal } from 'app/core';
import { IPaymentClause, PaymentClause } from 'app/shared/model/payment-clause.model';
import { PaymentClauseService } from './payment-clause.service';
// import {PaymentClauseComponent} from "app/danhmuc/payment-clause";
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
@Component({
    selector: 'eb-payment-clause-update',
    templateUrl: './payment-clause-update.component.html',
    styleUrls: ['./payment-clause-update.component.css']
})
export class PaymentClauseUpdateComponent extends BaseComponent implements OnInit, AfterViewInit {
    paymentClause: IPaymentClause;
    paymentClauseCopy: IPaymentClause;
    account: any;
    paymentClauses: IPaymentClause[];
    isSaveAndCreate: boolean;
    modalRef: NgbModalRef;
    isClose: boolean;
    @ViewChild('content') content: any;
    ROLE_DKTT_Them = ROLE.DanhMucDieuKhoanThanhToan_Them;
    ROLE_DKTT_Sua = ROLE.DanhMucDieuKhoanThanhToan_Sua;
    ROLE_DKTT_Xoa = ROLE.DanhMucDieuKhoanThanhToan_Xoa;
    ROLE_DKTT_Xem = ROLE.DanhMucDieuKhoanThanhToan_Xem;
    isEditUrl: boolean;
    isRoleSua: boolean;
    isRoleThem: boolean;
    isCreateUrl: boolean;
    arrAuthorities: any[];
    constructor(
        private paymentClauseService: PaymentClauseService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private toastr: ToastrService,
        private principal: Principal,
        private translate: TranslateService, // private paymentClauseComponent: PaymentClauseComponent
        public utilService: UtilsService,
        private modalService: NgbModal
    ) {
        super();
        this.activatedRoute.data.subscribe(data => {
            this.principal.identity().then(account => {
                this.account = account;
                this.isEditUrl = window.location.href.includes('edit');
                this.isCreateUrl = window.location.href.includes('/new');
                this.arrAuthorities = account.authorities;
                this.isRoleSua = !this.arrAuthorities.includes('ROLE_ADMIN')
                    ? this.arrAuthorities.includes(ROLE.DanhMucDieuKhoanThanhToan_Sua)
                    : true;
                this.isRoleThem = !this.arrAuthorities.includes('ROLE_ADMIN')
                    ? this.arrAuthorities.includes(ROLE.DanhMucDieuKhoanThanhToan_Them)
                    : true;
            });
        });
    }

    ngOnInit() {
        this.isClose = false;
        this.isSaveAndCreate = false;
        this.activatedRoute.data.subscribe(({ paymentClause }) => {
            this.paymentClause = paymentClause;
        });

        this.copy();
    }

    previousState() {
        this.router.navigate(['payment-clause']);
    }

    reload() {
        this.paymentClause = new PaymentClause();
        this.isSaveAndCreate = false;
        this.copy();
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentClause>>) {
        result.subscribe(
            (res: HttpResponse<IPaymentClause>) => this.onSaveSuccess(),
            httpErrorResponse => this.onSaveError(httpErrorResponse)
        );
    }

    checkEmpty() {
        console.log(this.paymentClause.paymentClauseCode);
        if (this.paymentClause.paymentClauseCode === undefined || this.paymentClause.paymentClauseCode.length === 0) {
            this.toastr.error(
                this.translate.instant('ebwebApp.paymentClause.pmcCodeNotNull'),
                this.translate.instant('ebwebApp.paymentClause.error')
            );
            return true;
        } else if (this.paymentClause.paymentClauseName === undefined || this.paymentClause.paymentClauseName.length === 0) {
            this.toastr.error(
                this.translate.instant('ebwebApp.paymentClause.pmcNameNotNull'),
                this.translate.instant('ebwebApp.paymentClause.error')
            );
            return true;
        }
        return false;
    }

    save() {
        // this.isSaving = true;
        event.preventDefault();
        if (!this.checkEmpty()) {
            if (this.paymentClause.id !== undefined) {
                this.subscribeToSaveResponse(this.paymentClauseService.update(this.paymentClause));
            } else {
                this.subscribeToSaveResponse(this.paymentClauseService.create(this.paymentClause));
            }
        }
        this.closeContent();
    }

    saveAndNew() {
        event.preventDefault();
        this.isSaveAndCreate = true;
        this.save();
        // this.paymentClause = {};
    }

    private onSaveSuccess() {
        this.copy();
        this.toastr.success(
            this.translate.instant('ebwebApp.paymentClause.saveSuccessfully'),
            this.translate.instant('ebwebApp.paymentClause.message')
        );
        if (!this.isSaveAndCreate) {
            this.previousState();
        } else {
            this.reload();
        }
    }

    private onSaveError(response) {
        this.toastr.error(this.translate.instant(`ebwebApp.paymentClause.${response.error.errorKey}`));
        if (this.isSaveAndCreate) {
            this.ngOnInit();
        }
    }

    delete() {
        event.preventDefault();
        if (this.paymentClause.id) {
            this.router.navigate(['/payment-clause', { outlets: { popup: this.paymentClause.id + '/delete' } }]);
        }
    }

    close() {
        if (this.modalRef) {
            this.modalRef.close();
            this.modalRef = null;
        }
        this.isClose = true;
        this.router.navigate(['payment-clause']);
    }

    closeContent() {
        if (this.modalRef) {
            this.modalRef.close();
            this.modalRef = null;
        }
    }

    closeForm() {
        if (!this.utilService.isEquivalent(this.paymentClause, this.paymentClauseCopy)) {
            if (!this.modalRef) {
                this.modalRef = this.modalService.open(this.content, { backdrop: 'static' });
            }
        } else {
            this.close();
        }
    }

    copy() {
        this.paymentClauseCopy = Object.assign({}, this.paymentClause);
    }

    canDeactive(): boolean {
        if (!this.isClose) {
            return this.utilService.isEquivalent(this.paymentClause, this.paymentClauseCopy);
        }
        return true;
    }

    ngAfterViewInit(): void {
        this.focusFirstInput();
    }
}
