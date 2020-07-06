import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGOtherVoucher } from 'app/shared/model/g-other-voucher.model';

@Component({
    selector: 'eb-ghi-giam-ccdc-delete-dialog',
    templateUrl: './ghi-giam-ccdc-detail.component.html'
})
export class GhiGiamCcdcDetailComponent implements OnInit {
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
