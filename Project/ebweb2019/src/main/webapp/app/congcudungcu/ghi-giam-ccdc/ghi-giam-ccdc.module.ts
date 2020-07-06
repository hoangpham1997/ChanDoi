import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { GhiGiamCcdcComponent } from 'app/congcudungcu/ghi-giam-ccdc/ghi-giam-ccdc.component';
import { GhiGiamCcdcDetailComponent } from 'app/congcudungcu/ghi-giam-ccdc/ghi-giam-ccdc-detail.component';
import { GhiGiamCcdcUpdateComponent } from 'app/congcudungcu/ghi-giam-ccdc/ghi-giam-ccdc-update.component';
import {
    GhiGiamCcdcDeleteDialogComponent,
    GhiGiamCcdcDeletePopupComponent
} from 'app/congcudungcu/ghi-giam-ccdc/ghi-giam-ccdc-delete-dialog.component';
import { GhiGiamCcdcPopupRoute, GhiGiamCcdcRoute } from 'app/congcudungcu/ghi-giam-ccdc/ghi-giam-ccdc.route';

const ENTITY_STATES = [...GhiGiamCcdcRoute, ...GhiGiamCcdcPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        GhiGiamCcdcComponent,
        GhiGiamCcdcDetailComponent,
        GhiGiamCcdcUpdateComponent,
        GhiGiamCcdcDeleteDialogComponent,
        GhiGiamCcdcDeletePopupComponent
    ],
    entryComponents: [
        GhiGiamCcdcComponent,
        GhiGiamCcdcDetailComponent,
        GhiGiamCcdcUpdateComponent,
        GhiGiamCcdcDetailComponent,
        GhiGiamCcdcDeleteDialogComponent,
        GhiGiamCcdcDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebGhiGiamCCDCModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
