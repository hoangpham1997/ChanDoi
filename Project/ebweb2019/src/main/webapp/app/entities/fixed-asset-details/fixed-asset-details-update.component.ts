import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IFixedAssetDetails } from 'app/shared/model/fixed-asset-details.model';
import { FixedAssetDetailsService } from './fixed-asset-details.service';
import { IFixedAsset } from 'app/shared/model/fixed-asset.model';
import { FixedAssetService } from 'app/entities/fixed-asset';

@Component({
    selector: 'eb-fixed-asset-details-update',
    templateUrl: './fixed-asset-details-update.component.html'
})
export class FixedAssetDetailsUpdateComponent implements OnInit {
    private _fixedAssetDetails: IFixedAssetDetails;
    isSaving: boolean;

    fixedassets: IFixedAsset[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private fixedAssetDetailsService: FixedAssetDetailsService,
        private fixedAssetService: FixedAssetService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ fixedAssetDetails }) => {
            this.fixedAssetDetails = fixedAssetDetails;
        });
        this.fixedAssetService.query().subscribe(
            (res: HttpResponse<IFixedAsset[]>) => {
                this.fixedassets = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.fixedAssetDetails.id !== undefined) {
            this.subscribeToSaveResponse(this.fixedAssetDetailsService.update(this.fixedAssetDetails));
        } else {
            this.subscribeToSaveResponse(this.fixedAssetDetailsService.create(this.fixedAssetDetails));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IFixedAssetDetails>>) {
        result.subscribe((res: HttpResponse<IFixedAssetDetails>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get fixedAssetDetails() {
        return this._fixedAssetDetails;
    }

    set fixedAssetDetails(fixedAssetDetails: IFixedAssetDetails) {
        this._fixedAssetDetails = fixedAssetDetails;
    }
}
