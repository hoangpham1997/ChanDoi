import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ISaleDiscountPolicy } from 'app/shared/model/sale-discount-policy.model';
import { SaleDiscountPolicyService } from './sale-discount-policy.service';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';

@Component({
    selector: 'eb-sale-discount-policy-update',
    templateUrl: './sale-discount-policy-update.component.html'
})
export class SaleDiscountPolicyUpdateComponent implements OnInit {
    private _saleDiscountPolicy: ISaleDiscountPolicy;
    isSaving: boolean;

    materialgoods: IMaterialGoods[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private saleDiscountPolicyService: SaleDiscountPolicyService,
        private materialGoodsService: MaterialGoodsService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ saleDiscountPolicy }) => {
            this.saleDiscountPolicy = saleDiscountPolicy;
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
        if (this.saleDiscountPolicy.id !== undefined) {
            this.subscribeToSaveResponse(this.saleDiscountPolicyService.update(this.saleDiscountPolicy));
        } else {
            this.subscribeToSaveResponse(this.saleDiscountPolicyService.create(this.saleDiscountPolicy));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISaleDiscountPolicy>>) {
        result.subscribe((res: HttpResponse<ISaleDiscountPolicy>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get saleDiscountPolicy() {
        return this._saleDiscountPolicy;
    }

    set saleDiscountPolicy(saleDiscountPolicy: ISaleDiscountPolicy) {
        this._saleDiscountPolicy = saleDiscountPolicy;
    }
}
