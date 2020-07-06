import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    AutoPrincipleComponent,
    AutoPrincipleDetailComponent,
    AutoPrincipleUpdateComponent,
    AutoPrincipleDeletePopupComponent,
    AutoPrincipleDeleteDialogComponent,
    autoPrincipleRoute,
    autoPrinciplePopupRoute
} from './index';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...autoPrincipleRoute, ...autoPrinciplePopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AutoPrincipleComponent,
        AutoPrincipleDetailComponent,
        AutoPrincipleUpdateComponent,
        AutoPrincipleDeleteDialogComponent,
        AutoPrincipleDeletePopupComponent
    ],
    entryComponents: [
        AutoPrincipleComponent,
        AutoPrincipleUpdateComponent,
        AutoPrincipleDeleteDialogComponent,
        AutoPrincipleDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebAutoPrincipleModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
