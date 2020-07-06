import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import {
    PhanBoChiPhiTraTruocPopupRoute,
    phanBoChiPhiTraTruocRoute
} from 'app/tonghop/phan_bo_chi_phi_tra_truoc/phan-bo-chi-phi-tra-truoc.route';
import { PhanBoChiPhiTraTruocComponent } from 'app/tonghop/phan_bo_chi_phi_tra_truoc/phan-bo-chi-phi-tra-truoc.component';
import { PhanBoChiPhiTraTruocDetailComponent } from 'app/tonghop/phan_bo_chi_phi_tra_truoc/phan-bo-chi-phi-tra-truoc-detail.component';
import { PhanBoChiPhiTraTruocUpdateComponent } from 'app/tonghop/phan_bo_chi_phi_tra_truoc/phan-bo-chi-phi-tra-truoc-update.component';
import {
    PhanBoChiPhiTraTruocDeleteDialogComponent,
    PhanBoChiPhiTraTruocDeletePopupComponent
} from 'app/tonghop/phan_bo_chi_phi_tra_truoc/phan-bo-chi-phi-tra-truoc-delete-dialog.component';

const ENTITY_STATES = [...phanBoChiPhiTraTruocRoute, ...PhanBoChiPhiTraTruocPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PhanBoChiPhiTraTruocComponent,
        PhanBoChiPhiTraTruocDetailComponent,
        PhanBoChiPhiTraTruocUpdateComponent,
        PhanBoChiPhiTraTruocDeleteDialogComponent,
        PhanBoChiPhiTraTruocDeletePopupComponent
    ],
    entryComponents: [
        PhanBoChiPhiTraTruocComponent,
        PhanBoChiPhiTraTruocUpdateComponent,
        PhanBoChiPhiTraTruocDeleteDialogComponent,
        PhanBoChiPhiTraTruocDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebPhanBoChiPhiTraTruocModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
