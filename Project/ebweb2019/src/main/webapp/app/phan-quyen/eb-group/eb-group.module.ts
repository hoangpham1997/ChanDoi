import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { EbGroupUpdateComponent } from 'app/phan-quyen/eb-group/eb-group-update.component';
import { EbGroupComponent } from 'app/phan-quyen/eb-group/eb-group.component';
import { EbGroupDeleteDialogComponent } from 'app/phan-quyen/eb-group/eb-group-delete-dialog.component';
import { ebGroupRoute } from 'app/phan-quyen/eb-group/eb-group.route';

const ENTITY_STATES = [...ebGroupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [EbGroupComponent, EbGroupUpdateComponent, EbGroupDeleteDialogComponent],
    entryComponents: [EbGroupComponent, EbGroupUpdateComponent, EbGroupDeleteDialogComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebEbGroupModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
