import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { KiemKeCcdcComponent } from 'app/congcudungcu/kiem-ke-ccdc/kiem-ke-ccdc.component';
import { KiemKeCcdcDetailComponent } from 'app/congcudungcu/kiem-ke-ccdc/kiem-ke-ccdc-detail.component';
import { KiemKeCcdcUpdateComponent } from 'app/congcudungcu/kiem-ke-ccdc/kiem-ke-ccdc-update.component';
import {
    KiemKeCcdcDeleteDialogComponent,
    KiemKeCcdcDeletePopupComponent
} from 'app/congcudungcu/kiem-ke-ccdc/kiem-ke-ccdc-delete-dialog.component';
import { KiemkeCcdcPopupRoute, KiemKeCcdcRoute } from 'app/congcudungcu/kiem-ke-ccdc/kiem-ke-ccdc.route';

const ENTITY_STATES = [...KiemKeCcdcRoute, ...KiemkeCcdcPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        KiemKeCcdcComponent,
        KiemKeCcdcDetailComponent,
        KiemKeCcdcUpdateComponent,
        KiemKeCcdcDeleteDialogComponent,
        KiemKeCcdcDeletePopupComponent
    ],
    entryComponents: [
        KiemKeCcdcComponent,
        KiemKeCcdcDetailComponent,
        KiemKeCcdcUpdateComponent,
        KiemKeCcdcDeleteDialogComponent,
        KiemKeCcdcDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebKiemKeCCDCModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
