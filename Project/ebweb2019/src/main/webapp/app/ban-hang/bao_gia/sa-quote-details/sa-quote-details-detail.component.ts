import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISAQuoteDetails } from 'app/shared/model/sa-quote-details.model';

@Component({
    selector: 'eb-sa-quote-details-detail',
    templateUrl: './sa-quote-details-detail.component.html'
})
export class SAQuoteDetailsDetailComponent implements OnInit {
    sAQuoteDetails: ISAQuoteDetails;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sAQuoteDetails }) => {
            this.sAQuoteDetails = sAQuoteDetails;
        });
    }

    previousState() {
        window.history.back();
    }
}
