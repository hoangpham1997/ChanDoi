import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IGOtherVoucher } from 'app/shared/model/g-other-voucher.model';

@Component({
    selector: 'eb-chi-phi-tra-truoc-detail',
    templateUrl: './chi-phi-tra-truoc-detail.component.html'
})
export class ChiPhiTraTruocDetailComponent implements OnInit {
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
