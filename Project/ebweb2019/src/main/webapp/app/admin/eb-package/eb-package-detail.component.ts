import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEbPackage } from 'app/shared/model/eb-package.model';

@Component({
    selector: 'eb-eb-package-detail',
    templateUrl: './eb-package-detail.component.html'
})
export class EbPackageDetailComponent implements OnInit {
    ebPackage: IEbPackage;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ ebPackage }) => {
            this.ebPackage = ebPackage;
        });
    }

    previousState() {
        window.history.back();
    }
}
