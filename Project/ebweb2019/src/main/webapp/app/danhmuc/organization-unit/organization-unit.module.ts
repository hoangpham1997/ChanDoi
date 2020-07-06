import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    OrganizationUnitComponent,
    OrganizationUnitDetailComponent,
    OrganizationUnitUpdateComponent,
    OrganizationUnitDeletePopupComponent,
    OrganizationUnitDeleteDialogComponent,
    organizationUnitRoute,
    organizationUnitPopupRoute
} from './index';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...organizationUnitRoute, ...organizationUnitPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        OrganizationUnitComponent,
        OrganizationUnitDetailComponent,
        OrganizationUnitUpdateComponent,
        OrganizationUnitDeleteDialogComponent,
        OrganizationUnitDeletePopupComponent
    ],
    entryComponents: [
        OrganizationUnitComponent,
        OrganizationUnitUpdateComponent,
        OrganizationUnitDeleteDialogComponent,
        OrganizationUnitDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebOrganizationUnitModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
