import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    GOtherVoucherComponent,
    GOtherVoucherDetailComponent,
    GOtherVoucherUpdateComponent,
    GOtherVoucherDeletePopupComponent,
    GOtherVoucherDeleteDialogComponent,
    gOtherVoucherRoute,
    gOtherVoucherPopupRoute
} from './index';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...gOtherVoucherRoute, ...gOtherVoucherPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        GOtherVoucherComponent,
        GOtherVoucherDetailComponent,
        GOtherVoucherUpdateComponent,
        GOtherVoucherDeleteDialogComponent,
        GOtherVoucherDeletePopupComponent
    ],
    entryComponents: [
        GOtherVoucherComponent,
        GOtherVoucherUpdateComponent,
        GOtherVoucherDeleteDialogComponent,
        GOtherVoucherDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebGOtherVoucherModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
