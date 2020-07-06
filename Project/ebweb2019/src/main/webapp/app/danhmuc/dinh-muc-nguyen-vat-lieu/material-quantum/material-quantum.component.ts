import { Component, OnInit, OnDestroy, ViewChild, TemplateRef } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IMaterialQuantum } from 'app/shared/model/material-quantum.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { MaterialQuantumService } from './material-quantum.service';
import { IAccountList } from 'app/shared/model/account-list.model';
import { IObjectsMaterialQuantum } from 'app/shared/model/objects-material-quantum.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'eb-material-quantum',
    templateUrl: './material-quantum.component.html',
    styleUrls: ['./material-quantum.component.css']
})
export class MaterialQuantumComponent extends BaseComponent implements OnInit, OnDestroy {
    currentAccount: any;
    materialQuantums: IMaterialQuantum[];
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
    selectedRow: IMaterialQuantum;
    objectsMaterialQuantum: IObjectsMaterialQuantum[];
    modalRef: NgbModalRef;
    ROLE_DinhMucNguyenVatLieu_Xem = ROLE.DinhMucNguyenVatLieu_Xem;
    ROLE_DinhMucNguyenVatLieu_Them = ROLE.DinhMucNguyenVatLieu_Them;
    ROLE_DinhMucNguyenVatLieu_Sua = ROLE.DinhMucNguyenVatLieu_Sua;
    ROLE_DinhMucNguyenVatLieu_Xoa = ROLE.DinhMucNguyenVatLieu_Xoa;

    buttonDeleteTranslate = 'ebwebApp.mBDeposit.toolTip.delete';
    buttonAddTranslate = 'ebwebApp.mBDeposit.toolTip.add';
    buttonEditTranslate = 'ebwebApp.mBDeposit.toolTip.edit';
    buttonRecordTranslate = 'ebwebApp.mBDeposit.toolTip.record';
    buttonUnRecordTranslate = 'ebwebApp.mBDeposit.toolTip.unrecord';
    buttonExportTranslate = 'ebwebApp.mBDeposit.toolTip.export';
    buttonSearchTranslate = 'ebwebApp.mBDeposit.toolTip.search';

    constructor(
        private materialQuantumService: MaterialQuantumService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private modalService: NgbModal
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

    loadAll() {
        this.materialQuantumService.getMaterialQuantums().subscribe((res: HttpResponse<IMaterialQuantum[]>) => {
            this.materialQuantums = res.body;
            this.objects = res.body;
            this.selectedRow = this.materialQuantums[0];
            this.selectedRows.push(this.materialQuantums[0]);
        });
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/material-quantum'], {
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
            '/material-quantum',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.materialQuantumService.getObject().subscribe((res: HttpResponse<IObjectsMaterialQuantum[]>) => {
            this.objectsMaterialQuantum = res.body;
        });
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInMaterialQuantums();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IMaterialQuantum) {
        return item.id;
    }

    registerChangeInMaterialQuantums() {
        this.eventSubscriber = this.eventManager.subscribe('materialQuantumListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateMaterialQuantums(data: IMaterialQuantum[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.materialQuantums = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    doubleClickRow(any?) {
        this.edit();
    }

    edit() {
        event.preventDefault();
        if (this.selectedRow.id) {
            this.router.navigate(['./material-quantum', this.selectedRow.id, 'edit']);
        }
    }

    delete() {
        event.preventDefault();
        if (this.selectedRow.id) {
            this.router.navigate(['/material-quantum', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
        }
    }

    onSelect(select: IMaterialQuantum) {
        this.selectedRow = select;
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    addNew(event) {
        event.preventDefault();
        this.router.navigate(['./material-quantum', 'new']);
    }
}
