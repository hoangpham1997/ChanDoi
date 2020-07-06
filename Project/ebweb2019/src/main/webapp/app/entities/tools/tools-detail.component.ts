import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITools } from 'app/shared/model/tools.model';

@Component({
    selector: 'eb-tools-detail',
    templateUrl: './tools-detail.component.html'
})
export class ToolsDetailComponent implements OnInit {
    tools: ITools;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ tools }) => {
            this.tools = tools;
        });
    }

    previousState() {
        window.history.back();
    }
}
