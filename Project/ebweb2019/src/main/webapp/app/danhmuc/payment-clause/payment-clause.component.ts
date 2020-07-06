import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IPaymentClause } from 'app/shared/model/payment-clause.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { PaymentClauseService } from './payment-clause.service';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { PrepaidExpensesComponent } from 'app/shared/modal/prepaid-expenses/prepaid-expenses.component';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';
import { DeleteMultipleLinesComponent } from 'app/shared/modal/delete-multiple-lines/delete-multiple-lines.component';
// import {items} from './items';

@Component({
    selector: 'eb-payment-clause',
    templateUrl: './payment-clause.component.html',
    styleUrls: ['./payment-clause.component.css']
})
export class PaymentClauseComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('deleteModal') public deleteModal: NgbModalRef;
    currentAccount: any;
    paymentClauses: IPaymentClause[];
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
    selectedRow: IPaymentClause;
    selectedRows: IPaymentClause[];
    ROLE_DKTT_Them = ROLE.DanhMucDieuKhoanThanhToan_Them;
    ROLE_DKTT_Sua = ROLE.DanhMucDieuKhoanThanhToan_Sua;
    ROLE_DKTT_Xoa = ROLE.DanhMucDieuKhoanThanhToan_Xoa;
    modalRef: NgbModalRef;
    constructor(
        private toast: ToastrService,
        private translate: TranslateService,
        private paymentClauseService: PaymentClauseService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private refModalService: RefModalService,
        private modalService: NgbModal
    ) {
        super();
        this.itemsPerPage = 20;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll() {
        this.paymentClauseService
            .getAllPaymentClauses({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IPaymentClause[]>) => this.paginatePaymentClauses(res.body, res.headers),
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
        this.router.navigate(['/payment-clause'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    addNew($event = null) {
        this.router.navigate(['/payment-clause/new']);
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/payment-clause',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }
    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }
    ngOnInit() {
        this.paymentClauses = [];
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInPaymentClauses();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IPaymentClause) {
        return item.id;
    }

    registerChangeInPaymentClauses() {
        this.eventSubscriber = this.eventManager.subscribe('paymentClauseListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginatePaymentClauses(data: IPaymentClause[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.paymentClauses = data;
        this.selectedRow = this.paymentClauses[0];
        this.selectedRows.push(this.selectedRow);
        this.objects = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    onSelect(select: IPaymentClause) {
        this.selectedRow = select;
    }

    edit() {
        event.preventDefault();
        if (this.selectedRow.id) {
            this.router.navigate(['./payment-clause', this.selectedRow.id, 'edit']);
        }
    }

    detail() {
        if (this.selectedRow.id) {
            this.router.navigate(['./payment-clause', this.selectedRow.id, 'view']);
        }
    }

    delete() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.modalRef = this.modalService.open(this.deleteModal, { size: 'lg', backdrop: 'static' });
        } else {
            if (this.selectedRow.id) {
                this.router.navigate(['/payment-clause', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
            }
        }
    }

    selectedItemPerPage() {
        this.loadAll();
    }

    deleteAfter() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.paymentClauseService.multiDelete(this.selectedRows).subscribe(res => {
            this.selectedRows = [];
            if (res.body.countSuccessVouchers > 0) {
                this.loadAll();
            }
            if (res.body.countFailVouchers === 0) {
                this.toast.success(this.translate.instant('ebwebApp.bank.deleteSuccess'));
            }
            if (res.body.countTotalVouchers !== res.body.countSuccessVouchers) {
                // const lstFailView = [];
                if (res.body.listFail.length > 0) {
                    // res.body.listFail.forEach(n => {
                    //     if (n) {
                    //         const viewNo: ViewVoucherNo = {};
                    //         viewNo.accountingObjectCode = n.accountingObjectCode;
                    //         viewNo.reasonFail = this.translate.instant('ebwebApp.paymentClause.existed');
                    //         lstFailView.push(viewNo);
                    //     }
                    // });
                    // res.body.listFail = lstFailView;
                    const lstHideColumn = ['paymentClauseCode'];
                    this.modalRef = this.refModalService.open(
                        res.body,
                        DeleteMultipleLinesComponent,
                        lstHideColumn,
                        false,
                        null,
                        null,
                        null,
                        null,
                        null,
                        true
                    );
                }
            }
            // this.searchAfterChangeRecord();
            // if (res.body.countTotalVouchers !== res.body.countSuccessVouchers) {
            //     this.modalRef = this.refModalService.open(
            //         res.body,
            //         ErrorVouchersComponent,
            //         null,
            //         false,
            //         null,
            //         null,
            //         null,
            //         null,
            //         null,
            //         true
            //     );
            //     this.loadAll();
            // } else {
            //     this.toast.success(this.translate.instant('ebwebApp.mBDeposit.deleteSuccessful'));
            // }
        });
    }
}
