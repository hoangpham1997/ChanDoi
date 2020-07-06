import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISystemOption } from 'app/shared/model/system-option.model';

@Component({
    selector: 'eb-system-option-detail',
    templateUrl: './system-option-detail.component.html'
})
export class SystemOptionDetailComponent implements OnInit {
    systemOption: ISystemOption;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ systemOption }) => {
            this.systemOption = systemOption;
        });
    }

    previousState() {
        window.history.back();
    }
}
