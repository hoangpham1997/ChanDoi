import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import { CostSetComponent } from 'app/danhmuc/doi-tuong-tap-hop-chi-phi/cost-set.component';
import { CostSetDetailComponent } from 'app/danhmuc/doi-tuong-tap-hop-chi-phi/cost-set-detail.component';
import { CostSetUpdateComponent } from 'app/danhmuc/doi-tuong-tap-hop-chi-phi/cost-set-update.component';
import {
    CostSetDeleteDialogComponent,
    CostSetDeletePopupComponent
} from 'app/danhmuc/doi-tuong-tap-hop-chi-phi/cost-set-delete-dialog.component';
import { costSetPopupRoute, costSetRoute } from 'app/danhmuc/doi-tuong-tap-hop-chi-phi/cost-set.route';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { TranslateService } from '@ngx-translate/core';

const ENTITY_STATES = [...costSetRoute, ...costSetPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CostSetComponent,
        CostSetDetailComponent,
        CostSetUpdateComponent,
        CostSetDeleteDialogComponent,
        CostSetDeletePopupComponent
    ],
    entryComponents: [CostSetComponent, CostSetUpdateComponent, CostSetDeleteDialogComponent, CostSetDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebCostSetModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
