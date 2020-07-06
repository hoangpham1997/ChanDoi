import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { ICPOPN } from 'app/shared/model/cpopn.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { CPOPNService } from './cpopn.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { IAccountList } from 'app/shared/model/account-list.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { ObjectTypeByCPOPN } from 'app/app.constants';
import { ROLE } from 'app/role.constants';
import { BaseComponent } from 'app/shared/base-component/base.component';

@Component({
    selector: 'eb-cpopn',
    templateUrl: './cpopn.component.html',
    styleUrls: ['./cpopn.component.css']
})
export class CPOPNComponent extends BaseComponent implements OnInit {
    private _cPOPN: ICPOPN;
    currentAccount: any;
    cPOPNS: ICPOPN[];
    cPOPNSCopy: ICPOPN[];
    products: ICPOPN[];
    factoryAndDepartmennts: ICPOPN[];
    orders: ICPOPN[];
    constructors: ICPOPN[];
    contracts: ICPOPN[];
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
    objectType: number;
    accountLists: IAccountList[];
    modalRef: NgbModalRef;
    accountingObjects: IAccountingObject[];
    ROLE_ChiPhiDoDangDauKy_Sua = ROLE.ChiPhiDoDangDauKy_Sua;

    buttonSaveTranslate = 'ebwebApp.mBDeposit.toolTip.save';

    constructor(
        private cPOPNService: CPOPNService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private toastr: ToastrService,
        private translate: TranslateService,
        private accountListService: AccountListService,
        public utilsService: UtilsService,
        private accountingObjectService: AccountingObjectService
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
        this.cPOPNService.getCPOPNs().subscribe(
            (res: HttpResponse<ICPOPN[]>) => {
                this.paginateCPOPNS(res.body, res.headers);
                this.products = this.cPOPNS.filter(n => n.objectType === ObjectTypeByCPOPN.SAN_PHAM);
                this.factoryAndDepartmennts = this.cPOPNS.filter(n => n.objectType === ObjectTypeByCPOPN.PHAN_XUONG_PHONG_BAN);
                this.constructors = this.cPOPNS.filter(n => n.objectType === ObjectTypeByCPOPN.CONG_TRINH_VU_VIEC);
                this.orders = this.cPOPNS.filter(n => n.objectType === ObjectTypeByCPOPN.DON_HANG);
                this.contracts = this.cPOPNS.filter(n => n.objectType === ObjectTypeByCPOPN.HOP_DONG);
                if (sessionStorage.getItem('objectType')) {
                    this.objectType = parseInt(sessionStorage.getItem('objectType'), 20);
                    this.selectChangeObject(this.objectType);
                } else {
                    this.objectType = ObjectTypeByCPOPN.CONG_TRINH_VU_VIEC;
                    this.totalItems = this.products.length;
                }
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
        this.router.navigate(['/cpopn'], {
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
            '/cpopn',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.products = [];
        this.accountListService.getAccountStartWith154().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.accountLists = res.body;
            console.log(this.accountLists.length);
        });
        sessionStorage.removeItem('objectType');
        this.factoryAndDepartmennts = [];
        this.orders = [];
        this.constructors = [];
        this.contracts = [];
        this.accountingObjects = [];
        this.accountingObjectService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.accountingObjects = res.body;
        });
        this.loadAll();
        this.isSaving = false;
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.cPOPN = {};
        this.registerChangeInCPOPNS();
    }

    // ngOnDestroy() {
    //     this.eventManager.destroy(this.eventSubscriber);
    // }

    trackId(index: number, item: ICPOPN) {
        return item.id;
    }

    registerChangeInCPOPNS() {
        this.eventSubscriber = this.eventManager.subscribe('cPOPNListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateCPOPNS(data: ICPOPN[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 20);
        this.queryCount = this.totalItems;
        this.cPOPNS = data;
        this.copy();
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    save() {
        event.preventDefault();
        this.isSaving = true;
        this.cPOPNS = [];
        for (let i = 0; i < this.products.length; i++) {
            this.cPOPNS.push(this.products[i]);
        }
        for (let i = 0; i < this.factoryAndDepartmennts.length; i++) {
            this.cPOPNS.push(this.factoryAndDepartmennts[i]);
        }
        for (let i = 0; i < this.constructors.length; i++) {
            this.cPOPNS.push(this.constructors[i]);
        }
        for (let i = 0; i < this.orders.length; i++) {
            this.cPOPNS.push(this.orders[i]);
        }
        for (let i = 0; i < this.contracts.length; i++) {
            this.cPOPNS.push(this.contracts[i]);
        }
        for (let i = 0; i < this.cPOPNS.length; i++) {
            if (!this.cPOPNS[i].id) {
                if (
                    this.cPOPNS[i].objectType === 0 ||
                    this.cPOPNS[i].objectType === 1 ||
                    this.cPOPNS[i].objectType === 2 ||
                    this.cPOPNS[i].objectType === 3
                ) {
                    this.cPOPNS[i].costSetID = this.cPOPNS[i].objectID;
                } else {
                    this.cPOPNS[i].contractID = this.cPOPNS[i].objectID;
                }
            }
        }
        this.cPOPNService.save(this.cPOPNS).subscribe(
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
        this.copy();
    }

    private onSaveError() {
        this.isSaving = false;
        this.toastr.success(
            this.translate.instant('ebwebApp.cPProductQuantum.error'),
            this.translate.instant('ebwebApp.cPProductQuantum.message')
        );
    }

    selectedItemPerPage() {
        this.loadAll();
    }

    get cPOPN() {
        return this._cPOPN;
    }

    set cPOPN(cPOPN: ICPOPN) {
        this._cPOPN = cPOPN;
    }

    selectChangeTotalCostAmount(i, array: ICPOPN[]) {
        array[i].totalCostAmount =
            (array[i].directMaterialAmount ? array[i].directMaterialAmount : 0) +
            (array[i].directLaborAmount ? array[i].directLaborAmount : 0) +
            (array[i].machineMaterialAmount ? array[i].machineMaterialAmount : 0) +
            (array[i].machineLaborAmount ? array[i].machineLaborAmount : 0) +
            (array[i].machineToolsAmount ? array[i].machineToolsAmount : 0) +
            (array[i].machineDepreciationAmount ? array[i].machineDepreciationAmount : 0) +
            (array[i].machineServiceAmount ? array[i].machineServiceAmount : 0) +
            (array[i].machineGeneralAmount ? array[i].machineGeneralAmount : 0) +
            (array[i].generalMaterialAmount ? array[i].generalMaterialAmount : 0) +
            (array[i].generalLaborAmount ? array[i].generalLaborAmount : 0) +
            (array[i].generalToolsAmount ? array[i].generalToolsAmount : 0) +
            (array[i].generalDepreciationAmount ? array[i].generalDepreciationAmount : 0) +
            (array[i].generalServiceAmount ? array[i].generalServiceAmount : 0) +
            (array[i].otherGeneralAmount ? array[i].otherGeneralAmount : 0);
        this.selectChangeNotAcceptedAmount(i, array);
    }

    selectChangeObject(objectType) {
        if (objectType === '0') {
            this.totalItems = this.products.length;
            sessionStorage.setItem('objectType', '0');
        } else if (objectType === '1') {
            this.totalItems = this.factoryAndDepartmennts.length;
            sessionStorage.setItem('objectType', '1');
        } else if (objectType === '2') {
            this.totalItems = this.constructors.length;
            sessionStorage.setItem('objectType', '2');
        } else if (objectType === '3') {
            this.totalItems = this.orders.length;
            sessionStorage.setItem('objectType', '3');
        } else {
            this.totalItems = this.contracts.length;
            sessionStorage.setItem('objectType', '4');
        }
    }

    canDeactive(): boolean {
        return this.utilsService.isEquivalentArray(this.cPOPNS, this.cPOPNSCopy);
    }

    copy() {
        this.cPOPNSCopy = this.cPOPNS.map(object => ({ ...object }));
    }

    exit() {
        if (this.modalRef) {
            this.modalRef.close();
            return;
        }
    }

    selectChangeNotAcceptedAmount(i, array: ICPOPN[]) {
        array[i].notAcceptedAmount =
            (array[i].totalCostAmount ? array[i].totalCostAmount : 0) - (array[i].acceptedAmount ? array[i].acceptedAmount : 0);
    }

    getAccountingObjectName(accountingObjectID) {
        if (accountingObjectID) {
            const acc = this.accountingObjects.find(n => n.id === accountingObjectID);
            if (acc) {
                return acc.accountingObjectCode;
            } else {
                return '';
            }
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
