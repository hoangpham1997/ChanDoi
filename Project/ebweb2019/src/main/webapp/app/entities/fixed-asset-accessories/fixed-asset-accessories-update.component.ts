import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IFixedAssetAccessories } from 'app/shared/model/fixed-asset-accessories.model';
import { FixedAssetAccessoriesService } from './fixed-asset-accessories.service';
import { IFixedAsset } from 'app/shared/model/fixed-asset.model';
import { FixedAssetService } from 'app/entities/fixed-asset';
import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';

@Component({
    selector: 'eb-fixed-asset-accessories-update',
    templateUrl: './fixed-asset-accessories-update.component.html'
})
export class FixedAssetAccessoriesUpdateComponent implements OnInit {
    private _fixedAssetAccessories: IFixedAssetAccessories;
    isSaving: boolean;

    fixedassets: IFixedAsset[];

    units: IUnit[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private fixedAssetAccessoriesService: FixedAssetAccessoriesService,
        private fixedAssetService: FixedAssetService,
        private unitService: UnitService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ fixedAssetAccessories }) => {
            this.fixedAssetAccessories = fixedAssetAccessories;
        });
        this.fixedAssetService.query().subscribe(
            (res: HttpResponse<IFixedAsset[]>) => {
                this.fixedassets = res.body;
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
        if (this.fixedAssetAccessories.id !== undefined) {
            this.subscribeToSaveResponse(this.fixedAssetAccessoriesService.update(this.fixedAssetAccessories));
        } else {
            this.subscribeToSaveResponse(this.fixedAssetAccessoriesService.create(this.fixedAssetAccessories));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IFixedAssetAccessories>>) {
        result.subscribe(
            (res: HttpResponse<IFixedAssetAccessories>) => this.onSaveSuccess(),
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

    trackFixedAssetById(index: number, item: IFixedAsset) {
        return item.id;
    }

    trackUnitById(index: number, item: IUnit) {
        return item.id;
    }
    get fixedAssetAccessories() {
        return this._fixedAssetAccessories;
    }

    set fixedAssetAccessories(fixedAssetAccessories: IFixedAssetAccessories) {
        this._fixedAssetAccessories = fixedAssetAccessories;
    }
}
