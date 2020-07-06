import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFixedAsset } from 'app/shared/model/fixed-asset.model';

@Component({
    selector: 'eb-fixed-asset-detail',
    templateUrl: './fixed-asset-detail.component.html'
})
export class FixedAssetDetailComponent implements OnInit {
    fixedAsset: IFixedAsset;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ fixedAsset }) => {
            this.fixedAsset = fixedAsset;
        });
    }

    previousState() {
        window.history.back();
    }
}
