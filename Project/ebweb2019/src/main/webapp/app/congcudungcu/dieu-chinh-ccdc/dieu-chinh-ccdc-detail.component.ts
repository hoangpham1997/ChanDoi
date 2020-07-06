import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGOtherVoucher } from 'app/shared/model/g-other-voucher.model';

@Component({
    selector: 'eb-dieu-chinh-ccdc-delete-dialog',
    templateUrl: './dieu-chinh-ccdc-detail.component.html'
})
export class DieuChinhCcdcDetailComponent implements OnInit {
    gOtherVoucher: IGOtherVoucher;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ gOtherVoucher }) => {
            this.gOtherVoucher = gOtherVoucher;
        });
    }

    previousState() {
        window.history.back();
    }
}
