import { Component, OnInit, OnDestroy, ViewChild, TemplateRef } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IAccountTransfer } from 'app/shared/model/account-transfer.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { AccountTransferService } from './account-transfer.service';
import { IAccountList } from 'app/shared/model/account-list.model';
import { TranslateService } from '@ngx-translate/core';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-account-transfer',
    templateUrl: './account-transfer.component.html',
    styleUrls: ['./account-transfer.component.css']
})
export class AccountTransferComponent extends BaseComponent implements OnInit, OnDestroy {
    currentAccount: any;
    accountTransfers: IAccountTransfer[];
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
    selectedRow: IAccountTransfer;

    ROLE_TaiKhoanKetChuyen_Them = ROLE.TaiKhoanKetChuyen_Them;
    ROLE_TaiKhoanKetChuyen_Xem = ROLE.TaiKhoanKetChuyen_Xem;
    ROLE_TaiKhoanKetChuyen_Sua = ROLE.TaiKhoanKetChuyen_Sua;
    ROLE_TaiKhoanKetChuyen_Xoa = ROLE.TaiKhoanKetChuyen_Xoa;

    buttonDeleteTranslate = 'ebwebApp.mBDeposit.toolTip.delete';
    buttonAddTranslate = 'ebwebApp.mBDeposit.toolTip.add';
    buttonEditTranslate = 'ebwebApp.mBDeposit.toolTip.edit';
    buttonRecordTranslate = 'ebwebApp.mBDeposit.toolTip.record';
    buttonUnRecordTranslate = 'ebwebApp.mBDeposit.toolTip.unrecord';
    buttonExportTranslate = 'ebwebApp.mBDeposit.toolTip.export';
    buttonSearchTranslate = 'ebwebApp.mBDeposit.toolTip.search';

    constructor(
        private accountTransferService: AccountTransferService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private translate: TranslateService
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

    doubleClickRow(any?) {
        this.edit();
    }

    loadAll() {
        this.accountTransferService.getAccountTransfers().subscribe((res: HttpResponse<IAccountTransfer[]>) => {
            this.accountTransfers = res.body;
            this.objects = res.body;
            if (sessionStorage.getItem('currentRow')) {
                const currentRow: number = JSON.parse(sessionStorage.getItem('currentRow'));
                this.selectedRows.push(this.accountTransfers[currentRow]);
                this.selectedRow = this.accountTransfers[currentRow];
            } else {
                this.selectedRows.push(this.accountTransfers[0]);
                this.selectedRow = this.accountTransfers[0];
            }
            sessionStorage.removeItem('currentRow');
        });
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/account-transfer'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/account-transfer',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.accountTransfers = [];
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInAccountTransfers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IAccountTransfer) {
        return item.id;
    }

    registerChangeInAccountTransfers() {
        this.eventSubscriber = this.eventManager.subscribe('accountTransferListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateAccountTransfers(data: IAccountTransfer[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.accountTransfers = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    onSelect(select: IAccountTransfer) {
        this.selectedRow = select;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.TaiKhoanKetChuyen_Xem])
    edit() {
        event.preventDefault();
        sessionStorage.setItem('currentRow', JSON.stringify(this.accountTransfers.indexOf(this.selectedRow)));
        if (this.selectedRow.id) {
            this.router.navigate(['./account-transfer', this.selectedRow.id, 'edit']);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.TaiKhoanKetChuyen_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRow.id) {
            this.router.navigate(['/account-transfer', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
        }
    }

    getFromDataAccount(type) {
        if (type === 0) {
            return this.translate.instant('ebwebApp.accountTransfer.debit');
        }
        if (type === 1) {
            return this.translate.instant('ebwebApp.accountTransfer.credit');
        }
        if (type === 2) {
            return this.translate.instant('ebwebApp.accountTransfer.debit-credit');
        }
        if (type === 3) {
            return this.translate.instant('ebwebApp.accountTransfer.credit-debit');
        }
    }

    getDebitAccount(type) {
        if (type === 0) {
            return this.translate.instant('ebwebApp.accountTransfer.accountFrom');
        }
        if (type === 1) {
            return this.translate.instant('ebwebApp.accountTransfer.accountTo');
        }
    }

    getCreditAccount(type) {
        if (type === 0) {
            return this.translate.instant('ebwebApp.accountTransfer.accountFrom');
        }
        if (type === 1) {
            return this.translate.instant('ebwebApp.accountTransfer.accountTo');
        }
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.TaiKhoanKetChuyen_Them])
    addNew(event) {
        event.preventDefault();
        this.router.navigate(['./account-transfer', 'new']);
    }
}
