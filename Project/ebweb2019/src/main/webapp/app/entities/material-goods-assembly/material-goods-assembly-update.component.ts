import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IMaterialGoodsAssembly } from 'app/shared/model/material-goods-assembly.model';
import { MaterialGoodsAssemblyService } from './material-goods-assembly.service';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';

@Component({
    selector: 'eb-material-goods-assembly-update',
    templateUrl: './material-goods-assembly-update.component.html'
})
export class MaterialGoodsAssemblyUpdateComponent implements OnInit {
    private _materialGoodsAssembly: IMaterialGoodsAssembly;
    isSaving: boolean;

    materialgoods: IMaterialGoods[];

    units: IUnit[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private materialGoodsAssemblyService: MaterialGoodsAssemblyService,
        private materialGoodsService: MaterialGoodsService,
        private unitService: UnitService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ materialGoodsAssembly }) => {
            this.materialGoodsAssembly = materialGoodsAssembly;
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
        if (this.materialGoodsAssembly.id !== undefined) {
            this.subscribeToSaveResponse(this.materialGoodsAssemblyService.update(this.materialGoodsAssembly));
        } else {
            this.subscribeToSaveResponse(this.materialGoodsAssemblyService.create(this.materialGoodsAssembly));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMaterialGoodsAssembly>>) {
        result.subscribe(
            (res: HttpResponse<IMaterialGoodsAssembly>) => this.onSaveSuccess(),
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
    get materialGoodsAssembly() {
        return this._materialGoodsAssembly;
    }

    set materialGoodsAssembly(materialGoodsAssembly: IMaterialGoodsAssembly) {
        this._materialGoodsAssembly = materialGoodsAssembly;
    }
}
