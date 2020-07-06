import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFixedAssetCategory } from 'app/shared/model/fixed-asset-category.model';

@Component({
    selector: 'eb-fixed-asset-category-detail',
    templateUrl: './fixed-asset-category-detail.component.html'
})
export class FixedAssetCategoryDetailComponent implements OnInit {
    fixedAssetCategory: IFixedAssetCategory;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ fixedAssetCategory }) => {
            this.fixedAssetCategory = fixedAssetCategory;
        });
    }

    previousState() {
        window.history.back();
    }
}
