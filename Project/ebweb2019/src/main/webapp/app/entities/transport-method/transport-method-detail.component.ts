import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITransportMethod } from 'app/shared/model/transport-method.model';

@Component({
    selector: 'eb-transport-method-detail',
    templateUrl: './transport-method-detail.component.html'
})
export class TransportMethodDetailComponent implements OnInit {
    transportMethod: ITransportMethod;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ transportMethod }) => {
            this.transportMethod = transportMethod;
        });
    }

    previousState() {
        window.history.back();
    }
}
