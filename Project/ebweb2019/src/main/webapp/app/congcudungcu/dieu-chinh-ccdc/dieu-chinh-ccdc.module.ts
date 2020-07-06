import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import {
    PhanBoChiPhiTraTruocPopupRoute,
    phanBoChiPhiTraTruocRoute
} from 'app/tonghop/phan_bo_chi_phi_tra_truoc/phan-bo-chi-phi-tra-truoc.route';
import { PhanBoCcdcComponent } from 'app/congcudungcu/phan-bo-ccdc/phan-bo-ccdc.component';
import { PhanBoCcdcDetailComponent } from 'app/congcudungcu/phan-bo-ccdc/phan-bo-ccdc-detail.component';
import { PhanBoCcdcUpdateComponent } from 'app/congcudungcu/phan-bo-ccdc/phan-bo-ccdc-update.component';
import {
    PhanBoCcdcDeleteDialogComponent,
    PhanBoCcdcDeletePopupComponent
} from 'app/congcudungcu/phan-bo-ccdc/phan-bo-ccdc-delete-dialog.component';
import { PhanBoCcdcPopupRoute, phanBoCcdcRoute } from 'app/congcudungcu/phan-bo-ccdc/phan-bo-ccdc.route';
import { DieuChinhCcdcPopupRoute, dieuChinhCcdcRoute } from 'app/congcudungcu/dieu-chinh-ccdc/dieu-chinh-ccdc.route';
import { DieuChinhCcdcComponent } from 'app/congcudungcu/dieu-chinh-ccdc/dieu-chinh-ccdc.component';
import { DieuChinhCcdcDetailComponent } from 'app/congcudungcu/dieu-chinh-ccdc/dieu-chinh-ccdc-detail.component';
import { DieuChinhCcdcUpdateComponent } from 'app/congcudungcu/dieu-chinh-ccdc/dieu-chinh-ccdc-update.component';
import {
    DieuChinhCcdcDeleteDialogComponent,
    DieuChinhCcdcDeletePopupComponent
} from 'app/congcudungcu/dieu-chinh-ccdc/dieu-chinh-ccdc-delete-dialog.component';

const ENTITY_STATES = [...dieuChinhCcdcRoute, ...DieuChinhCcdcPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DieuChinhCcdcComponent,
        DieuChinhCcdcDetailComponent,
        DieuChinhCcdcUpdateComponent,
        DieuChinhCcdcDeleteDialogComponent,
        DieuChinhCcdcDeletePopupComponent
    ],
    entryComponents: [
        DieuChinhCcdcComponent,
        DieuChinhCcdcDetailComponent,
        DieuChinhCcdcUpdateComponent,
        DieuChinhCcdcDetailComponent,
        DieuChinhCcdcDeleteDialogComponent,
        DieuChinhCcdcDeletePopupComponent
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
