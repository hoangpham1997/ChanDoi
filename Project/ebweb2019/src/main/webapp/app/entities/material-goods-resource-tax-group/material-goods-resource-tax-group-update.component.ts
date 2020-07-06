import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IMaterialGoodsResourceTaxGroup } from 'app/shared/model/material-goods-resource-tax-group.model';
import { MaterialGoodsResourceTaxGroupService } from './material-goods-resource-tax-group.service';
import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';

@Component({
    selector: 'eb-material-goods-resource-tax-group-update',
    templateUrl: './material-goods-resource-tax-group-update.component.html'
})
export class MaterialGoodsResourceTaxGroupUpdateComponent implements OnInit {
    private _materialGoodsResourceTaxGroup: IMaterialGoodsResourceTaxGroup;
    isSaving: boolean;

    units: IUnit[];

    materialgoodsresourcetaxgroups: IMaterialGoodsResourceTaxGroup[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private materialGoodsResourceTaxGroupService: MaterialGoodsResourceTaxGroupService,
        private unitService: UnitService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ materialGoodsResourceTaxGroup }) => {
            this.materialGoodsResourceTaxGroup = materialGoodsResourceTaxGroup;
        });
        this.unitService.query().subscribe(
            (res: HttpResponse<IUnit[]>) => {
                this.units = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.materialGoodsResourceTaxGroupService.query().subscribe(
            (res: HttpResponse<IMaterialGoodsResourceTaxGroup[]>) => {
                this.materialgoodsresourcetaxgroups = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.materialGoodsResourceTaxGroup.id !== undefined) {
            this.subscribeToSaveResponse(this.materialGoodsResourceTaxGroupService.update(this.materialGoodsResourceTaxGroup));
        } else {
            this.subscribeToSaveResponse(this.materialGoodsResourceTaxGroupService.create(this.materialGoodsResourceTaxGroup));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMaterialGoodsResourceTaxGroup>>) {
        result.subscribe(
            (res: HttpResponse<IMaterialGoodsResourceTaxGroup>) => this.onSaveSuccess(),
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

    trackUnitById(index: number, item: IUnit) {
        return item.id;
    }

    trackMaterialGoodsResourceTaxGroupById(index: number, item: IMaterialGoodsResourceTaxGroup) {
        return item.id;
    }
    get materialGoodsResourceTaxGroup() {
        return this._materialGoodsResourceTaxGroup;
    }

    set materialGoodsResourceTaxGroup(materialGoodsResourceTaxGroup: IMaterialGoodsResourceTaxGroup) {
        this._materialGoodsResourceTaxGroup = materialGoodsResourceTaxGroup;
    }
}
