import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { ICPProductQuantum } from 'app/shared/model/cp-product-quantum.model';
import { CPProductQuantumService } from 'app/entities/cp-product-quantum';
import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { DinhMucGiaThanhThanhPhamService } from 'app/giathanh/dinh_muc_gia_thanh_thanh_pham/dinh-muc-gia-thanh-thanh-pham.service';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { JhiParseLinks } from 'ng-jhipster';
import { ITEMS_PER_PAGE } from 'app/shared';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ROLE } from 'app/role.constants';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-nhan-hoa-don',
    templateUrl: './dinh-muc-gia-thanh-thanh-pham.component.html',
    styleUrls: ['./dinh-muc-gia-thanh-thanh-pham.component.css'],
    providers: [DatePipe]
})
export class DinhMucGiaThanhThanhPhamComponent extends BaseComponent implements OnInit {
    private _materialGoods: IMaterialGoods;
    isSaving: boolean;
    materialGoodsDetails: IMaterialGoods[];
    cPProductQuantums: ICPProductQuantum[];
    units: IUnit[];
    currentAccount: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    routeData: any;
    cPProductQuantumsCopy: ICPProductQuantum[];
    modalRef: NgbModalRef;
    ROLE_DinhMucGiaThanhThanhPham_Sua = ROLE.DinhMucGiaThanhThanhPham_Sua;
    buttonSaveTranslate = 'ebwebApp.mBDeposit.toolTip.save';

    constructor(
        private materialGoodsService: MaterialGoodsService,
        private activatedRoute: ActivatedRoute,
        private cPProductQuantumService: CPProductQuantumService,
        private parseLinks: JhiParseLinks,
        private unitService: UnitService,
        public utilsService: UtilsService,
        private router: Router,
        private dinhMucGiaThanhThanhPhamService: DinhMucGiaThanhThanhPhamService,
        private principal: Principal,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {
        super();
        this.itemsPerPage = 20;
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    ngOnInit() {
        this.isSaving = false;
        this.cPProductQuantums = [];
        this.activatedRoute.data.subscribe(({ materialGoods }) => {
            this.materialGoods = materialGoods;
        });
        this.loadAll();
        this.unitService.getUnits().subscribe(
            (res: HttpResponse<IUnit[]>) => {
                this.units = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadAll() {
        this.cPProductQuantumService.getCPProductQuantums().subscribe(
            (res: HttpResponse<ICPProductQuantum[]>) => {
                this.paginateCPProductQuantums(res.body, res.headers);
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    private onError(errorMessage: string) {
        // this.jhiAlertService.error(errorMessage, null, null);
    }

    previousState() {
        window.history.back();
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DinhMucGiaThanhThanhPham_Sua])
    save() {
        event.preventDefault();
        this.isSaving = true;
        for (let i = 0; i < this.cPProductQuantums.length; i++) {
            if (!this.cPProductQuantums[i].id) {
                this.cPProductQuantums[i].id = this.cPProductQuantums[i].materialGoodsID;
            }
        }
        this.dinhMucGiaThanhThanhPhamService.save(this.cPProductQuantums).subscribe(
            (res: HttpResponse<any>) => {
                this.onSaveSuccess();
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
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
        this.toastr.error(
            this.translate.instant('ebwebApp.cPProductQuantum.error'),
            this.translate.instant('ebwebApp.cPProductQuantum.message')
        );
    }

    get materialGoods() {
        return this._materialGoods;
    }

    set materialGoods(materialGoods: IMaterialGoods) {
        this._materialGoods = materialGoods;
    }

    private paginateCPProductQuantums(data: ICPProductQuantum[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 20);
        this.queryCount = this.totalItems;

        // if (this.cPProductQuantumsCopy) {
        this.cPProductQuantums.push(...data.filter(n => !this.cPProductQuantums.some(m => m.materialGoodsID === n.materialGoodsID)));
        // this.cPProductQuantums.push()
        //     this.cPProductQuantums = this.cPProductQuantumsCopy;
        // }
        this.copy();
    }

    transition() {
        this.router.navigate(['/dinh-muc-gia-thanh-thanh-pham'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    selectedItemPerPage() {
        this.cPProductQuantums;
        this.loadAll();
    }

    selectChangeTotalCostAmount(i) {
        this.cPProductQuantums[i].totalCostAmount =
            (this.cPProductQuantums[i].directMaterialAmount ? this.cPProductQuantums[i].directMaterialAmount : 0) +
            (this.cPProductQuantums[i].directLaborAmount ? this.cPProductQuantums[i].directLaborAmount : 0) +
            (this.cPProductQuantums[i].machineMaterialAmount ? this.cPProductQuantums[i].machineMaterialAmount : 0) +
            (this.cPProductQuantums[i].machineLaborAmount ? this.cPProductQuantums[i].machineLaborAmount : 0) +
            (this.cPProductQuantums[i].machineToolsAmount ? this.cPProductQuantums[i].machineToolsAmount : 0) +
            (this.cPProductQuantums[i].machineDepreciationAmount ? this.cPProductQuantums[i].machineDepreciationAmount : 0) +
            (this.cPProductQuantums[i].machineServiceAmount ? this.cPProductQuantums[i].machineServiceAmount : 0) +
            (this.cPProductQuantums[i].machineGeneralAmount ? this.cPProductQuantums[i].machineGeneralAmount : 0) +
            (this.cPProductQuantums[i].generalMaterialAmount ? this.cPProductQuantums[i].generalMaterialAmount : 0) +
            (this.cPProductQuantums[i].generalLaborAmount ? this.cPProductQuantums[i].generalLaborAmount : 0) +
            (this.cPProductQuantums[i].generalToolsAmount ? this.cPProductQuantums[i].generalToolsAmount : 0) +
            (this.cPProductQuantums[i].generalDepreciationAmount ? this.cPProductQuantums[i].generalDepreciationAmount : 0) +
            (this.cPProductQuantums[i].generalServiceAmount ? this.cPProductQuantums[i].generalServiceAmount : 0) +
            (this.cPProductQuantums[i].otherGeneralAmount ? this.cPProductQuantums[i].otherGeneralAmount : 0);
    }

    getUnitByID(id) {
        if (this.units) {
            const unit = this.units.find(n => n.id === id);
            if (unit) {
                return unit.unitName;
            }
        }
    }

    canDeactive(): boolean {
        return this.utilsService.isEquivalentArray(this.cPProductQuantums, this.cPProductQuantumsCopy);
    }

    copy() {
        this.cPProductQuantumsCopy = this.cPProductQuantums.map(object => ({ ...object }));
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
