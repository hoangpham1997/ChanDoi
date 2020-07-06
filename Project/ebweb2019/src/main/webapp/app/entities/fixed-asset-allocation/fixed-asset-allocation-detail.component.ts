import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFixedAssetAllocation } from 'app/shared/model/fixed-asset-allocation.model';

@Component({
    selector: 'eb-fixed-asset-allocation-detail',
    templateUrl: './fixed-asset-allocation-detail.component.html'
})
export class FixedAssetAllocationDetailComponent implements OnInit {
    fixedAssetAllocation: IFixedAssetAllocation;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ fixedAssetAllocation }) => {
            this.fixedAssetAllocation = fixedAssetAllocation;
        });
    }

    previousState() {
        window.history.back();
    }
}
