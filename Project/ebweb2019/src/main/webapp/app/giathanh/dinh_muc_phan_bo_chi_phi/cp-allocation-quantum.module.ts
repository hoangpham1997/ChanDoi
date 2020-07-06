import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    CPAllocationQuantumComponent,
    CPAllocationQuantumDetailComponent,
    CPAllocationQuantumUpdateComponent,
    CPAllocationQuantumDeletePopupComponent,
    CPAllocationQuantumDeleteDialogComponent,
    cPAllocationQuantumRoute,
    cPAllocationQuantumPopupRoute
} from './index';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...cPAllocationQuantumRoute, ...cPAllocationQuantumPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CPAllocationQuantumComponent,
        CPAllocationQuantumDetailComponent,
        CPAllocationQuantumUpdateComponent,
        CPAllocationQuantumDeleteDialogComponent,
        CPAllocationQuantumDeletePopupComponent
    ],
    entryComponents: [
        CPAllocationQuantumComponent,
        CPAllocationQuantumUpdateComponent,
        CPAllocationQuantumDeleteDialogComponent,
        CPAllocationQuantumDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebCPAllocationQuantumModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
