import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISAQuote } from 'app/shared/model/sa-quote.model';

@Component({
    selector: 'eb-sa-quote-detail',
    templateUrl: './sa-quote-detail.component.html'
})
export class SAQuoteDetailComponent implements OnInit {
    sAQuote: ISAQuote;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sAQuote }) => {
            this.sAQuote = sAQuote;
        });
    }

    previousState() {
        window.history.back();
    }
}
