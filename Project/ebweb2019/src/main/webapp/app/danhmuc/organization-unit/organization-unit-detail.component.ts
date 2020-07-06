import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';

@Component({
    selector: 'eb-organization-unit-detail',
    templateUrl: './organization-unit-detail.component.html'
})
export class OrganizationUnitDetailComponent implements OnInit {
    organizationUnit: IOrganizationUnit;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ organizationUnit }) => {
            this.organizationUnit = organizationUnit;
        });
    }

    previousState() {
        window.history.back();
    }
}
