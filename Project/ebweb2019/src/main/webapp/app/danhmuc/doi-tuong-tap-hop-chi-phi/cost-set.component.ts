import { Component, OnInit, OnDestroy, ViewChild, TemplateRef, AfterViewInit } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { CostSet, ICostSet } from 'app/shared/model/cost-set.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE_DMTHCP } from 'app/shared';
import { CostSetService } from './cost-set.service';
import { TranslateService } from '@ngx-translate/core';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IMultiDelete } from 'app/shared/model/multi-delete';
import { ROLE } from 'app/role.constants';
import { SU_DUNG_DM_DOI_TUONG } from 'app/app.constants';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { ToastrService } from 'ngx-toastr';
import { DeleteResultComponent } from 'app/shared/modal/delete-result/delete-result.component';

@Component({
    selector: 'eb-cost-set',
    styleUrls: ['./cost-set.component.css'],
    templateUrl: './cost-set.component.html'
})
export class CostSetComponent extends BaseComponent implements OnInit, OnDestroy, AfterViewInit {
    @ViewChild('popUpMultiDelete') popUpMultiDelete: TemplateRef<any>;

    currentAccount: any;
    costSets: ICostSet[];
    costSet: CostSet;
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
    costSetCode: string;
    selectedCostSet: ICostSet;
    selectedRow: any;
    stringListDelete: string;
    modalRef: NgbModalRef;
    listDeleteFail: IMultiDelete[];
    isGetAllCompany: boolean;
    loadDetails: boolean;
    dataSession: IDataSessionStorage = new class implements IDataSessionStorage {
        accountingObjectName: string;
        itemsPerPage: number;
        page: number;
        pageCount: number;
        predicate: number;
        reverse: number;
        rowNum: number;
        totalItems: number;
    }();
    account: any;
    listType: any;
    index: any;
    rowNum: number;

    ROLE_DanhMucDTTHCP_Them = ROLE.DanhMucDTTHCP_Them;
    ROLE_DanhMucDTTHCP_Sua = ROLE.DanhMucDTTHCP_Sua;
    ROLE_DanhMucDTTHCP_Xem = ROLE.DanhMucDTTHCP_Xem;
    ROLE_DanhMucDTTHCP_Xoa = ROLE.DanhMucDTTHCP_Xoa;

    constructor(
        private costSetService: CostSetService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private modalService: NgbModal,
        private refModalService: RefModalService,
        public activeModal: NgbActiveModal,
        private toastr: ToastrService,
        public translate: TranslateService
    ) {
        super();
        this.translate
            .get([
                'ebwebApp.costSet.Type.Order',
                'ebwebApp.costSet.Type.Construction',
                'ebwebApp.costSet.Type.Factory',
                'ebwebApp.costSet.Type.Manufacturing technology',
                'ebwebApp.costSet.Type.Product',
                'ebwebApp.costSet.Type.Others'
            ])
            .subscribe(res => {
                this.listType = [
                    { value: 0, name: this.translate.instant('ebwebApp.costSet.Type.Order') },
                    { value: 1, name: this.translate.instant('ebwebApp.costSet.Type.Construction') },
                    { value: 2, name: this.translate.instant('ebwebApp.costSet.Type.Factory') },
                    { value: 3, name: this.translate.instant('ebwebApp.costSet.Type.Manufacturing technology') },
                    { value: 4, name: this.translate.instant('ebwebApp.costSet.Type.Product') },
                    { value: 5, name: this.translate.instant('ebwebApp.costSet.Type.Others') }
                ];
            });
        this.itemsPerPage = ITEMS_PER_PAGE_DMTHCP;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    selectedItemPerPage() {
        this.loadAll();
    }

    loadAll() {
        if (sessionStorage.getItem('dataSession')) {
            this.dataSession = JSON.parse(sessionStorage.getItem('dataSession'));
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
            this.previousPage = this.dataSession.page;
            sessionStorage.removeItem('dataSession');
        }
        this.costSetService
            .getCostSetsByCompanyId({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                costSetCode: this.costSetCode ? this.costSetCode : ''
            })
            .subscribe(
                (res: HttpResponse<ICostSet[]>) => {
                    this.paginateCostSets(res.body, res.headers);
                    if (this.costSets.length > 0) {
                        this.onSelect(this.selectedRow);
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    changePage() {
        this.page = 1;
        this.loadAll();
    }

    RowSelected(u: any) {
        this.selectedCostSet = u; // declare variable in component.
        console.log(this.selectedCostSet);
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.dataSession.rowNum = 0;
        this.router.navigate(['/cost-set'], {
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
            '/cost-set',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.isGetAllCompany = this.currentAccount.systemOption.find(x => x.code === SU_DUNG_DM_DOI_TUONG).data === '0';
            this.loadAll();
        });
        this.registerChangeInCostSets();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ICostSet) {
        return item.id;
    }

    registerChangeInCostSets() {
        this.eventSubscriber = this.eventManager.subscribe('costSetListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    getRowNumberOfRecord(page: number, index: number): number {
        if (page > 0 && index !== -1) {
            return (page - 1) * this.itemsPerPage + index + 1;
        }
    }

    onSelect(select: ICostSet) {
        this.selectedRow = select;
        this.index = this.costSets.indexOf(this.selectedRow);
        this.rowNum = this.getRowNumberOfRecord(this.page, this.index);
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    delete() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            if (this.selectedRow) {
                this.router.navigate(['/cost-set', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
            }
        }
    }

    edit() {
        event.preventDefault();
        this.dataSession.page = this.page;
        this.dataSession.itemsPerPage = this.itemsPerPage;
        this.dataSession.totalItems = this.totalItems;
        this.dataSession.rowNum = this.costSets.indexOf(this.selectedRow);
        // sort
        this.dataSession.predicate = this.predicate;
        this.dataSession.reverse = this.reverse;
        sessionStorage.setItem('dataSession', JSON.stringify(this.dataSession));
        if (this.selectedRow.id) {
            this.router.navigate(['./cost-set', this.selectedRow.id, 'edit']);
        }
    }

    addNew($event) {
        event.preventDefault();
        this.router.navigate(['/cost-set/new']);
    }

    private paginateCostSets(data: ICostSet[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.costSets = data;
        this.objects = data;
        if (this.dataSession && this.dataSession.rowNum) {
            this.selectedRows.push(this.costSets[this.dataSession.rowNum]);
            this.selectedRow = this.costSets[this.dataSession.rowNum];
        } else {
            this.selectedRows.push(this.costSets[0]);
            this.selectedRow = this.costSets[0];
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    /*arrowUp() {ec
        event.preventDefault();
        if (this.loadDetails) {
            return;
        }
        if (this.costSet) {
            const index = this.costSets.findIndex(x => x.id === this.costSet.id);
            if (index === 0) {
                if (this.totalItems.page - 1 === 0) {
                    return;
                }
                this.loadDetails = true;
                this.totalItems.page = this.totalItems.page - 1;
                this.loadAll();
                return;
            } else {
                this.loadDetails = true;
                this.onSelect(this.costSets[index - 1]);
            }
        }
    }*/

    continueDelete() {
        this.costSetService.multiDelete(this.selectedRows.map(x => x.id)).subscribe(
            (res: HttpResponse<any>) => {
                if (this.modalRef) {
                    this.modalRef.close();
                }
                const listDelete = this.selectedRows;
                const listDeletedSuccess = res.body.deletedSuccess.map(x => (x = this.selectedRows.find(y => y.id === x)));
                const listDeletedFail = res.body.deletedFail.map(x => (x = this.selectedRows.find(y => y.id === x)));

                this.modalRef = this.refModalService.open(
                    res.body,
                    DeleteResultComponent,
                    {
                        listDelete,
                        listDeletedSuccess,
                        listDeletedFail,
                        messages: 'Không thể xóa vì phát sinh chứng từ liên quan'
                    },
                    false,
                    null,
                    null,
                    null,
                    null,
                    null,
                    true
                );
                this.selectedRows = [];
                this.loadAll();
            },
            (res: HttpErrorResponse) => {
                this.toastr.error(res.error.title, this.translate.instant('ebwebApp.mBDeposit.message'));
                if (this.modalRef) {
                    this.modalRef.close();
                }
            }
        );
    }

    closePopUpDelete() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    ngAfterViewInit(): void {
        this.focusFirstInput();
    }

    getCostSetName(type): string {
        return this.listType.find(x => x.value === type).name;
    }
}
