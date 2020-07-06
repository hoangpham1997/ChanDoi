import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IMaterialGoodsPurchasePrice } from 'app/shared/model/material-goods-purchase-price.model';
import { MaterialGoodsPurchasePriceService } from './material-goods-purchase-price.service';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';

@Component({
    selector: 'eb-material-goods-purchase-price-update',
    templateUrl: './material-goods-purchase-price-update.component.html'
})
export class MaterialGoodsPurchasePriceUpdateComponent implements OnInit {
    private _materialGoodsPurchasePrice: IMaterialGoodsPurchasePrice;
    isSaving: boolean;

    materialgoods: IMaterialGoods[];

    currencies: ICurrency[];

    units: IUnit[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private materialGoodsPurchasePriceService: MaterialGoodsPurchasePriceService,
        private materialGoodsService: MaterialGoodsService,
        private currencyService: CurrencyService,
        private unitService: UnitService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ materialGoodsPurchasePrice }) => {
            this.materialGoodsPurchasePrice = materialGoodsPurchasePrice;
        });
        this.materialGoodsService.query().subscribe(
            (res: HttpResponse<IMaterialGoods[]>) => {
                this.materialgoods = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.currencyService.query().subscribe(
            (res: HttpResponse<ICurrency[]>) => {
                this.currencies = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.unitService.query().subscribe(
            (res: HttpResponse<IUnit[]>) => {
                this.units = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.materialGoodsPurchasePrice.id !== undefined) {
            this.subscribeToSaveResponse(this.materialGoodsPurchasePriceService.update(this.materialGoodsPurchasePrice));
        } else {
            this.subscribeToSaveResponse(this.materialGoodsPurchasePriceService.create(this.materialGoodsPurchasePrice));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMaterialGoodsPurchasePrice>>) {
        result.subscribe(
            (res: HttpResponse<IMaterialGoodsPurchasePrice>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackMaterialGoodsById(index: number, item: IMaterialGoods) {
        return item.id;
    }

    trackCurrencyById(index: number, item: ICurrency) {
        return item.id;
    }

    trackUnitById(index: number, item: IUnit) {
        return item.id;
    }
    get materialGoodsPurchasePrice() {
        return this._materialGoodsPurchasePrice;
    }

    set materialGoodsPurchasePrice(materialGoodsPurchasePrice: IMaterialGoodsPurchasePrice) {
        this._materialGoodsPurchasePrice = materialGoodsPurchasePrice;
    }
}
