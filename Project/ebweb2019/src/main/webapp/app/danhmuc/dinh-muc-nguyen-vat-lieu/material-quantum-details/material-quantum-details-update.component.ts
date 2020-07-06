import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IMaterialQuantumDetails } from 'app/shared/model/material-quantum-details.model';
import { MaterialQuantumDetailsService } from './material-quantum-details.service';
import { IMaterialQuantum } from 'app/shared/model/material-quantum.model';
import { MaterialQuantumService } from 'app/danhmuc/dinh-muc-nguyen-vat-lieu/material-quantum';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';

@Component({
    selector: 'eb-material-quantum-details-update',
    templateUrl: './material-quantum-details-update.component.html'
})
export class MaterialQuantumDetailsUpdateComponent implements OnInit {
    private _materialQuantumDetails: IMaterialQuantumDetails;
    isSaving: boolean;

    materialquantums: IMaterialQuantum[];

    materialgoods: IMaterialGoods[];

    units: IUnit[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private materialQuantumDetailsService: MaterialQuantumDetailsService,
        private materialQuantumService: MaterialQuantumService,
        private materialGoodsService: MaterialGoodsService,
        private unitService: UnitService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ materialQuantumDetails }) => {
            this.materialQuantumDetails = materialQuantumDetails;
        });
        this.materialQuantumService.query().subscribe(
            (res: HttpResponse<IMaterialQuantum[]>) => {
                this.materialquantums = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
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
        if (this.materialQuantumDetails.id !== undefined) {
            this.subscribeToSaveResponse(this.materialQuantumDetailsService.update(this.materialQuantumDetails));
        } else {
            this.subscribeToSaveResponse(this.materialQuantumDetailsService.create(this.materialQuantumDetails));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMaterialQuantumDetails>>) {
        result.subscribe(
            (res: HttpResponse<IMaterialQuantumDetails>) => this.onSaveSuccess(),
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

    trackMaterialQuantumById(index: number, item: IMaterialQuantum) {
        return item.id;
    }

    trackMaterialGoodsById(index: number, item: IMaterialGoods) {
        return item.id;
    }

    trackUnitById(index: number, item: IUnit) {
        return item.id;
    }
    get materialQuantumDetails() {
        return this._materialQuantumDetails;
    }

    set materialQuantumDetails(materialQuantumDetails: IMaterialQuantumDetails) {
        this._materialQuantumDetails = materialQuantumDetails;
    }
}
