import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFixedAssetAccessories } from 'app/shared/model/fixed-asset-accessories.model';

@Component({
    selector: 'eb-fixed-asset-accessories-detail',
    templateUrl: './fixed-asset-accessories-detail.component.html'
})
export class FixedAssetAccessoriesDetailComponent implements OnInit {
    fixedAssetAccessories: IFixedAssetAccessories;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ fixedAssetAccessories }) => {
            this.fixedAssetAccessories = fixedAssetAccessories;
        });
    }

    previousState() {
        window.history.back();
    }
}
