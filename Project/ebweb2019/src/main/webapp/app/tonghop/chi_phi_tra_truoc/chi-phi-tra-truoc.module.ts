import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import { chiPhiTraTruocPopupRoute, chiPhiTraTruocRoute } from 'app/tonghop/chi_phi_tra_truoc/chi-phi-tra-truoc.route';
import { ChiPhiTraTruocComponent } from 'app/tonghop/chi_phi_tra_truoc/chi-phi-tra-truoc.component';
import { ChiPhiTraTruocDetailComponent } from 'app/tonghop/chi_phi_tra_truoc/chi-phi-tra-truoc-detail.component';
import { ChiPhiTraTruocUpdateComponent } from 'app/tonghop/chi_phi_tra_truoc/chi-phi-tra-truoc-update.component';
import {
    ChiPhiTraTruocDeleteDialogComponent,
    ChiPhiTraTruocDeleteDialogPopupComponent
} from 'app/tonghop/chi_phi_tra_truoc/chi-phi-tra-truoc-delete-dialog.component';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...chiPhiTraTruocRoute, ...chiPhiTraTruocPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ChiPhiTraTruocComponent,
        ChiPhiTraTruocDetailComponent,
        ChiPhiTraTruocUpdateComponent,
        ChiPhiTraTruocDeleteDialogComponent,
        ChiPhiTraTruocDeleteDialogPopupComponent
    ],
    entryComponents: [
        ChiPhiTraTruocComponent,
        ChiPhiTraTruocDetailComponent,
        ChiPhiTraTruocUpdateComponent,
        ChiPhiTraTruocDeleteDialogComponent,
        ChiPhiTraTruocDeleteDialogPopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebChiPhiTraTruocModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
