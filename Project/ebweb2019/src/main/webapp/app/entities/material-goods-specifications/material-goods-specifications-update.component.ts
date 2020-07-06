import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IMaterialGoodsSpecifications } from 'app/shared/model/material-goods-specifications.model';
import { MaterialGoodsSpecificationsService } from './material-goods-specifications.service';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';

@Component({
    selector: 'eb-material-goods-specifications-update',
    templateUrl: './material-goods-specifications-update.component.html'
})
export class MaterialGoodsSpecificationsUpdateComponent implements OnInit {
    private _materialGoodsSpecifications: IMaterialGoodsSpecifications;
    isSaving: boolean;

    materialgoods: IMaterialGoods[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private materialGoodsSpecificationsService: MaterialGoodsSpecificationsService,
        private materialGoodsService: MaterialGoodsService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ materialGoodsSpecifications }) => {
            this.materialGoodsSpecifications = materialGoodsSpecifications;
        });
        this.materialGoodsService.query().subscribe(
            (res: HttpResponse<IMaterialGoods[]>) => {
                this.materialgoods = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.materialGoodsSpecifications.id !== undefined) {
            this.subscribeToSaveResponse(this.materialGoodsSpecificationsService.update(this.materialGoodsSpecifications));
        } else {
            this.subscribeToSaveResponse(this.materialGoodsSpecificationsService.create(this.materialGoodsSpecifications));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMaterialGoodsSpecifications>>) {
        result.subscribe(
            (res: HttpResponse<IMaterialGoodsSpecifications>) => this.onSaveSuccess(),
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
    get materialGoodsSpecifications() {
        return this._materialGoodsSpecifications;
    }

    set materialGoodsSpecifications(materialGoodsSpecifications: IMaterialGoodsSpecifications) {
        this._materialGoodsSpecifications = materialGoodsSpecifications;
    }
}
