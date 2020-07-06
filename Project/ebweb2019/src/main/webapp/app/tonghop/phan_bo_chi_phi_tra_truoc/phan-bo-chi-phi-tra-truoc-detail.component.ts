import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGOtherVoucher } from 'app/shared/model/g-other-voucher.model';

@Component({
    selector: 'eb-phan-bo-chi-phi-tra-truoc-delete-dialog',
    templateUrl: './phan-bo-chi-phi-tra-truoc-detail.component.html'
})
export class PhanBoChiPhiTraTruocDetailComponent implements OnInit {
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
