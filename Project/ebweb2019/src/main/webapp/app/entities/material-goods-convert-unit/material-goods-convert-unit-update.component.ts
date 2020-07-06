import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IMaterialGoodsConvertUnit } from 'app/shared/model/material-goods-convert-unit.model';
import { MaterialGoodsConvertUnitService } from './material-goods-convert-unit.service';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';

@Component({
    selector: 'eb-material-goods-convert-unit-update',
    templateUrl: './material-goods-convert-unit-update.component.html'
})
export class MaterialGoodsConvertUnitUpdateComponent implements OnInit {
    private _materialGoodsConvertUnit: IMaterialGoodsConvertUnit;
    isSaving: boolean;

    materialgoods: IMaterialGoods[];

    units: IUnit[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private materialGoodsConvertUnitService: MaterialGoodsConvertUnitService,
        private materialGoodsService: MaterialGoodsService,
        private unitService: UnitService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ materialGoodsConvertUnit }) => {
            this.materialGoodsConvertUnit = materialGoodsConvertUnit;
        });
        this.materialGoodsService.query().subscribe(
            (res: HttpResponse<IMaterialGoods[]>) => {
                this.materialgoods = res.body;
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
        if (this.materialGoodsConvertUnit.id !== undefined) {
            this.subscribeToSaveResponse(this.materialGoodsConvertUnitService.update(this.materialGoodsConvertUnit));
        } else {
            this.subscribeToSaveResponse(this.materialGoodsConvertUnitService.create(this.materialGoodsConvertUnit));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMaterialGoodsConvertUnit>>) {
        result.subscribe(
            (res: HttpResponse<IMaterialGoodsConvertUnit>) => this.onSaveSuccess(),
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

    trackUnitById(index: number, item: IUnit) {
        return item.id;
    }
    get materialGoodsConvertUnit() {
        return this._materialGoodsConvertUnit;
    }

    set materialGoodsConvertUnit(materialGoodsConvertUnit: IMaterialGoodsConvertUnit) {
        this._materialGoodsConvertUnit = materialGoodsConvertUnit;
    }
}
