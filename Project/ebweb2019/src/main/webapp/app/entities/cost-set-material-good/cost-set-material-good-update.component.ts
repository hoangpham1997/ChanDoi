import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ICostSetMaterialGood } from 'app/shared/model/cost-set-material-good.model';
import { CostSetMaterialGoodService } from './cost-set-material-good.service';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { CostSetService } from 'app/entities/cost-set';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';

@Component({
    selector: 'eb-cost-set-material-good-update',
    templateUrl: './cost-set-material-good-update.component.html'
})
export class CostSetMaterialGoodUpdateComponent implements OnInit {
    private _costSetMaterialGood: ICostSetMaterialGood;
    isSaving: boolean;

    costsets: ICostSet[];

    materialgoods: IMaterialGoods[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private costSetMaterialGoodService: CostSetMaterialGoodService,
        private costSetService: CostSetService,
        private materialGoodsService: MaterialGoodsService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ costSetMaterialGood }) => {
            this.costSetMaterialGood = costSetMaterialGood;
        });
        this.costSetService.query().subscribe(
            (res: HttpResponse<ICostSet[]>) => {
                this.costsets = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.materialGoodsService.query({ filter: 'costsetmaterialgood-is-null' }).subscribe(
            (res: HttpResponse<IMaterialGoods[]>) => {
                if (!this.costSetMaterialGood.materialGoods || !this.costSetMaterialGood.materialGoods.id) {
                    this.materialgoods = res.body;
                } else {
                    this.materialGoodsService.find(this.costSetMaterialGood.materialGoods.id).subscribe(
                        (subRes: HttpResponse<IMaterialGoods>) => {
                            this.materialgoods = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.costSetMaterialGood.id !== undefined) {
            this.subscribeToSaveResponse(this.costSetMaterialGoodService.update(this.costSetMaterialGood));
        } else {
            this.subscribeToSaveResponse(this.costSetMaterialGoodService.create(this.costSetMaterialGood));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICostSetMaterialGood>>) {
        result.subscribe((res: HttpResponse<ICostSetMaterialGood>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackCostSetById(index: number, item: ICostSet) {
        return item.id;
    }

    trackMaterialGoodsById(index: number, item: IMaterialGoods) {
        return item.id;
    }
    get costSetMaterialGood() {
        return this._costSetMaterialGood;
    }

    set costSetMaterialGood(costSetMaterialGood: ICostSetMaterialGood) {
        this._costSetMaterialGood = costSetMaterialGood;
    }
}
