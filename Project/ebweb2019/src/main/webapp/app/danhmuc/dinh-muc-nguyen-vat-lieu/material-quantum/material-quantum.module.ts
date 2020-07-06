import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    MaterialQuantumComponent,
    MaterialQuantumDetailComponent,
    MaterialQuantumUpdateComponent,
    MaterialQuantumDeletePopupComponent,
    MaterialQuantumDeleteDialogComponent,
    materialQuantumRoute,
    materialQuantumPopupRoute
} from './index';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...materialQuantumRoute, ...materialQuantumPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MaterialQuantumComponent,
        MaterialQuantumDetailComponent,
        MaterialQuantumUpdateComponent,
        MaterialQuantumDeleteDialogComponent,
        MaterialQuantumDeletePopupComponent
    ],
    entryComponents: [
        MaterialQuantumComponent,
        MaterialQuantumUpdateComponent,
        MaterialQuantumDeleteDialogComponent,
        MaterialQuantumDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebMaterialQuantumModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
