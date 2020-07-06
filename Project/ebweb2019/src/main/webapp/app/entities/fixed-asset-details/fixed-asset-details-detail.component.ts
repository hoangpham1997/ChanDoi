import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFixedAssetDetails } from 'app/shared/model/fixed-asset-details.model';

@Component({
    selector: 'eb-fixed-asset-details-detail',
    templateUrl: './fixed-asset-details-detail.component.html'
})
export class FixedAssetDetailsDetailComponent implements OnInit {
    fixedAssetDetails: IFixedAssetDetails;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ fixedAssetDetails }) => {
            this.fixedAssetDetails = fixedAssetDetails;
        });
    }

    previousState() {
        window.history.back();
    }
}
