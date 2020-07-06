import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    SAOrderComponent,
    SAOrderDetailComponent,
    SAOrderUpdateComponent,
    SAOrderDeletePopupComponent,
    SAOrderDeleteDialogComponent,
    sAOrderRoute,
    sAOrderPopupRoute
} from './index';
import { NgxMaskModule } from 'ngx-mask';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';

const ENTITY_STATES = [...sAOrderRoute, ...sAOrderPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES), NgxMaskModule],
    declarations: [
        SAOrderComponent,
        SAOrderDetailComponent,
        SAOrderUpdateComponent,
        SAOrderDeleteDialogComponent,
        SAOrderDeletePopupComponent
    ],
    entryComponents: [SAOrderComponent, SAOrderUpdateComponent, SAOrderDeleteDialogComponent, SAOrderDeletePopupComponent],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebSAOrderModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
