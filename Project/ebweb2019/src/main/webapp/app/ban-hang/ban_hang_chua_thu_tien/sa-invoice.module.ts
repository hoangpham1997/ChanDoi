import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    SAInvoiceComponent,
    SAInvoiceUpdateComponent,
    SAInvoiceDeletePopupComponent,
    SAInvoiceDeleteDialogComponent,
    sAInvoiceRoute,
    sAInvoicePopupRoute
} from './';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...sAInvoiceRoute, ...sAInvoicePopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [SAInvoiceComponent, SAInvoiceUpdateComponent, SAInvoiceDeleteDialogComponent, SAInvoiceDeletePopupComponent],
    entryComponents: [SAInvoiceComponent, SAInvoiceUpdateComponent, SAInvoiceDeleteDialogComponent, SAInvoiceDeletePopupComponent],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebSAInvoiceModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
