import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ISAQuoteDetails } from 'app/shared/model/sa-quote-details.model';
import { SAQuoteDetailsService } from './sa-quote-details.service';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';

@Component({
    selector: 'eb-sa-quote-details-update',
    templateUrl: './sa-quote-details-update.component.html'
})
export class SAQuoteDetailsUpdateComponent implements OnInit {
    private _sAQuoteDetails: ISAQuoteDetails;
    isSaving: boolean;

    materialgoods: IMaterialGoods[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private sAQuoteDetailsService: SAQuoteDetailsService,
        private materialGoodsService: MaterialGoodsService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ sAQuoteDetails }) => {
            this.sAQuoteDetails = sAQuoteDetails;
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
        if (this.sAQuoteDetails.id !== undefined) {
            this.subscribeToSaveResponse(this.sAQuoteDetailsService.update(this.sAQuoteDetails));
        } else {
            this.subscribeToSaveResponse(this.sAQuoteDetailsService.create(this.sAQuoteDetails));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISAQuoteDetails>>) {
        result.subscribe((res: HttpResponse<ISAQuoteDetails>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get sAQuoteDetails() {
        return this._sAQuoteDetails;
    }

    set sAQuoteDetails(sAQuoteDetails: ISAQuoteDetails) {
        this._sAQuoteDetails = sAQuoteDetails;
    }
}
