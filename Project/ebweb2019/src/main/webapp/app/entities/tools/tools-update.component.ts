import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ITools } from 'app/shared/model/tools.model';
import { ToolsService } from './tools.service';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';

@Component({
    selector: 'eb-tools-update',
    templateUrl: './tools-update.component.html'
})
export class ToolsUpdateComponent implements OnInit {
    private _tools: ITools;
    isSaving: boolean;

    organizationunits: IOrganizationUnit[];

    units: IUnit[];
    postedDateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private toolsService: ToolsService,
        private organizationUnitService: OrganizationUnitService,
        private unitService: UnitService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ tools }) => {
            this.tools = tools;
        });
        this.organizationUnitService.query().subscribe(
            (res: HttpResponse<IOrganizationUnit[]>) => {
                this.organizationunits = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.unitService.query().subscribe(
            (res: HttpResponse<IUnit[]>) => {
                this.units = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.tools.id !== undefined) {
            this.subscribeToSaveResponse(this.toolsService.update(this.tools));
        } else {
            this.subscribeToSaveResponse(this.toolsService.create(this.tools));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITools>>) {
        result.subscribe((res: HttpResponse<ITools>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackOrganizationUnitById(index: number, item: IOrganizationUnit) {
        return item.id;
    }

    trackUnitById(index: number, item: IUnit) {
        return item.id;
    }
    get tools() {
        return this._tools;
    }

    set tools(tools: ITools) {
        this._tools = tools;
    }
}
