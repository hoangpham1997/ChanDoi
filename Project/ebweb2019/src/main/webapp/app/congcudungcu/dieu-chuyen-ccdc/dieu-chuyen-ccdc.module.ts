import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { DieuChuyenCcdcComponent } from 'app/congcudungcu/dieu-chuyen-ccdc/dieu-chuyen-ccdc.component';
import { DieuChuyenCcdcDetailComponent } from 'app/congcudungcu/dieu-chuyen-ccdc/dieu-chuyen-ccdc-detail.component';
import { DieuChuyenCcdcUpdateComponent } from 'app/congcudungcu/dieu-chuyen-ccdc/dieu-chuyen-ccdc-update.component';
import {
    DieuChuyenCcdcDeleteDialogComponent,
    DieuChuyenCcdcDeletePopupComponent
} from 'app/congcudungcu/dieu-chuyen-ccdc/dieu-chuyen-ccdc-delete-dialog.component';
import { DieuChuyenCcdcPopupRoute, DieuChuyenCcdcRoute } from 'app/congcudungcu/dieu-chuyen-ccdc/dieu-chuyen-ccdc.route';

const ENTITY_STATES = [...DieuChuyenCcdcRoute, ...DieuChuyenCcdcPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DieuChuyenCcdcComponent,
        DieuChuyenCcdcDetailComponent,
        DieuChuyenCcdcUpdateComponent,
        DieuChuyenCcdcDeleteDialogComponent,
        DieuChuyenCcdcDeletePopupComponent
    ],
    entryComponents: [
        DieuChuyenCcdcComponent,
        DieuChuyenCcdcDetailComponent,
        DieuChuyenCcdcUpdateComponent,
        DieuChuyenCcdcDetailComponent,
        DieuChuyenCcdcDeleteDialogComponent,
        DieuChuyenCcdcDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebDieuChuyenCCDCModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
