import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    RepositoryComponent,
    RepositoryDetailComponent,
    RepositoryUpdateComponent,
    RepositoryDeletePopupComponent,
    RepositoryDeleteDialogComponent,
    repositoryRoute,
    repositoryPopupRoute
} from './index';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...repositoryRoute, ...repositoryPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RepositoryComponent,
        RepositoryDetailComponent,
        RepositoryUpdateComponent,
        RepositoryDeleteDialogComponent,
        RepositoryDeletePopupComponent
    ],
    entryComponents: [RepositoryComponent, RepositoryUpdateComponent, RepositoryDeleteDialogComponent, RepositoryDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebRepositoryModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
