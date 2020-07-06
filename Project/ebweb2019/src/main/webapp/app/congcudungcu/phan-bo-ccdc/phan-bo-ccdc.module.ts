import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { PhanBoCcdcComponent } from 'app/congcudungcu/phan-bo-ccdc/phan-bo-ccdc.component';
import { PhanBoCcdcDetailComponent } from 'app/congcudungcu/phan-bo-ccdc/phan-bo-ccdc-detail.component';
import { PhanBoCcdcUpdateComponent } from 'app/congcudungcu/phan-bo-ccdc/phan-bo-ccdc-update.component';
import {
    PhanBoCcdcDeleteDialogComponent,
    PhanBoCcdcDeletePopupComponent
} from 'app/congcudungcu/phan-bo-ccdc/phan-bo-ccdc-delete-dialog.component';
import { PhanBoCcdcPopupRoute, phanBoCcdcRoute } from 'app/congcudungcu/phan-bo-ccdc/phan-bo-ccdc.route';

const ENTITY_STATES = [...phanBoCcdcRoute, ...PhanBoCcdcPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PhanBoCcdcComponent,
        PhanBoCcdcDetailComponent,
        PhanBoCcdcUpdateComponent,
        PhanBoCcdcDetailComponent,
        PhanBoCcdcDeleteDialogComponent,
        PhanBoCcdcDeletePopupComponent
    ],
    entryComponents: [
        PhanBoCcdcComponent,
        PhanBoCcdcDetailComponent,
        PhanBoCcdcUpdateComponent,
        PhanBoCcdcDetailComponent,
        PhanBoCcdcDeleteDialogComponent,
        PhanBoCcdcDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebPhanBoCCDCModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
