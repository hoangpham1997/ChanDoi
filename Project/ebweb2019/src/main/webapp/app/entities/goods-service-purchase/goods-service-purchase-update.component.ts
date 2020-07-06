import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IGoodsServicePurchase } from 'app/shared/model/goods-service-purchase.model';
import { GoodsServicePurchaseService } from './goods-service-purchase.service';

@Component({
    selector: 'eb-goods-service-purchase-update',
    templateUrl: './goods-service-purchase-update.component.html'
})
export class GoodsServicePurchaseUpdateComponent implements OnInit {
    private _goodsServicePurchase: IGoodsServicePurchase;
    isSaving: boolean;

    constructor(private goodsServicePurchaseService: GoodsServicePurchaseService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ goodsServicePurchase }) => {
            this.goodsServicePurchase = goodsServicePurchase;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.goodsServicePurchase.id !== undefined) {
            this.subscribeToSaveResponse(this.goodsServicePurchaseService.update(this.goodsServicePurchase));
        } else {
            this.subscribeToSaveResponse(this.goodsServicePurchaseService.create(this.goodsServicePurchase));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IGoodsServicePurchase>>) {
        result.subscribe(
            (res: HttpResponse<IGoodsServicePurchase>) => this.onSaveSuccess(),
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
    get goodsServicePurchase() {
        return this._goodsServicePurchase;
    }

    set goodsServicePurchase(goodsServicePurchase: IGoodsServicePurchase) {
        this._goodsServicePurchase = goodsServicePurchase;
    }
}
