import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { ICPAllocationQuantum } from 'app/shared/model/cp-allocation-quantum.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { CPAllocationQuantumService } from './cp-allocation-quantum.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ROLE } from 'app/role.constants';
import { BaseComponent } from 'app/shared/base-component/base.component';

@Component({
    selector: 'eb-cp-allocation-quantum',
    templateUrl: './cp-allocation-quantum.component.html',
    styleUrls: ['./cp-allocation-quantum.component.css']
})
export class CPAllocationQuantumComponent extends BaseComponent implements OnInit, OnDestroy {
    currentAccount: any;
    cPAllocationQuantums: ICPAllocationQuantum[];
    cPAllocationQuantumsCopy: ICPAllocationQuantum[];

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
    isSaving: boolean;
    modalRef: NgbModalRef;
    ROLE_DinhMucPhanBoChiPhi_Sua = ROLE.DinhMucPhanBoChiPhi_Sua;
    buttonSaveTranslate = 'ebwebApp.mBDeposit.toolTip.save';

    constructor(
        private cPAllocationQuantumService: CPAllocationQuantumService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private toastr: ToastrService,
        private translate: TranslateService,
        public utilsService: UtilsService
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
        this.cPAllocationQuantumService.getCPAllocationQuantums().subscribe(
            (res: HttpResponse<ICPAllocationQuantum[]>) => {
                this.paginateCPAllocationQuantums(res.body, res.headers);
            },
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
        this.router.navigate(['/cp-allocation-quantum'], {
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
            '/cp-allocation-quantum',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInCPAllocationQuantums();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ICPAllocationQuantum) {
        return item.id;
    }

    registerChangeInCPAllocationQuantums() {
        this.eventSubscriber = this.eventManager.subscribe('cPAllocationQuantumListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateCPAllocationQuantums(data: ICPAllocationQuantum[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 20);
        this.queryCount = this.totalItems;
        this.cPAllocationQuantums = data;
        this.copy();
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    selectChangeTotalCostAmount(i) {
        this.cPAllocationQuantums[i].totalCostAmount =
            (this.cPAllocationQuantums[i].directMaterialAmount ? this.cPAllocationQuantums[i].directMaterialAmount : 0) +
            (this.cPAllocationQuantums[i].directLaborAmount ? this.cPAllocationQuantums[i].directLaborAmount : 0) +
            (this.cPAllocationQuantums[i].machineMaterialAmount ? this.cPAllocationQuantums[i].machineMaterialAmount : 0) +
            (this.cPAllocationQuantums[i].machineLaborAmount ? this.cPAllocationQuantums[i].machineLaborAmount : 0) +
            (this.cPAllocationQuantums[i].machineToolsAmount ? this.cPAllocationQuantums[i].machineToolsAmount : 0) +
            (this.cPAllocationQuantums[i].machineDepreciationAmount ? this.cPAllocationQuantums[i].machineDepreciationAmount : 0) +
            (this.cPAllocationQuantums[i].machineServiceAmount ? this.cPAllocationQuantums[i].machineServiceAmount : 0) +
            (this.cPAllocationQuantums[i].machineGeneralAmount ? this.cPAllocationQuantums[i].machineGeneralAmount : 0) +
            (this.cPAllocationQuantums[i].generalMaterialAmount ? this.cPAllocationQuantums[i].generalMaterialAmount : 0) +
            (this.cPAllocationQuantums[i].generalLaborAmount ? this.cPAllocationQuantums[i].generalLaborAmount : 0) +
            (this.cPAllocationQuantums[i].generalToolsAmount ? this.cPAllocationQuantums[i].generalToolsAmount : 0) +
            (this.cPAllocationQuantums[i].generalDepreciationAmount ? this.cPAllocationQuantums[i].generalDepreciationAmount : 0) +
            (this.cPAllocationQuantums[i].generalServiceAmount ? this.cPAllocationQuantums[i].generalServiceAmount : 0) +
            (this.cPAllocationQuantums[i].otherGeneralAmount ? this.cPAllocationQuantums[i].otherGeneralAmount : 0);
    }

    selectedItemPerPage() {
        this.loadAll();
    }

    save() {
        event.preventDefault();
        this.isSaving = true;
        for (let i = 0; i < this.cPAllocationQuantums.length; i++) {
            if (!this.cPAllocationQuantums[i].id) {
                this.cPAllocationQuantums[i].id = this.cPAllocationQuantums[i].objectID;
            }
        }
        this.cPAllocationQuantumService.save(this.cPAllocationQuantums).subscribe(
            (res: HttpResponse<any>) => {
                this.onSaveSuccess();
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    previousState() {
        window.history.back();
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.toastr.success(
            this.translate.instant('ebwebApp.cPProductQuantum.saveSuccess'),
            this.translate.instant('ebwebApp.cPProductQuantum.message')
        );
    }

    private onSaveError() {
        this.isSaving = false;
        this.toastr.success(
            this.translate.instant('ebwebApp.cPProductQuantum.error'),
            this.translate.instant('ebwebApp.cPProductQuantum.message')
        );
    }

    canDeactive(): boolean {
        return this.utilsService.isEquivalentArray(this.cPAllocationQuantums, this.cPAllocationQuantumsCopy);
    }

    copy() {
        this.cPAllocationQuantumsCopy = this.cPAllocationQuantums.map(object => ({ ...object }));
    }

    exit() {
        if (this.modalRef) {
            this.modalRef.close();
            return;
        }
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }
}
