import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    PPInvoiceComponent,
    PPInvoiceDetailComponent,
    PPInvoiceUpdateComponent,
    PPInvoiceDeletePopupComponent,
    PPInvoiceDeleteDialogComponent,
    pPInvoiceRoute,
    pPInvoicePopupRoute
} from './';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';

const ENTITY_STATES = [...pPInvoiceRoute, ...pPInvoicePopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PPInvoiceComponent,
        PPInvoiceDetailComponent,
        PPInvoiceUpdateComponent,
        PPInvoiceDeleteDialogComponent,
        PPInvoiceDeletePopupComponent
    ],
    providers: [UtilsService],
    entryComponents: [PPInvoiceComponent, PPInvoiceUpdateComponent, PPInvoiceDeleteDialogComponent, PPInvoiceDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebPPInvoiceModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
