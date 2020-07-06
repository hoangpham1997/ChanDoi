import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    MBInternalTransferTaxComponent,
    MBInternalTransferTaxDetailComponent,
    MBInternalTransferTaxUpdateComponent,
    MBInternalTransferTaxDeletePopupComponent,
    MBInternalTransferTaxDeleteDialogComponent,
    mBInternalTransferTaxRoute,
    mBInternalTransferTaxPopupRoute
} from './index';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...mBInternalTransferTaxRoute, ...mBInternalTransferTaxPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MBInternalTransferTaxComponent,
        MBInternalTransferTaxDetailComponent,
        MBInternalTransferTaxUpdateComponent,
        MBInternalTransferTaxDeleteDialogComponent,
        MBInternalTransferTaxDeletePopupComponent
    ],
    entryComponents: [
        MBInternalTransferTaxComponent,
        MBInternalTransferTaxUpdateComponent,
        MBInternalTransferTaxDeleteDialogComponent,
        MBInternalTransferTaxDeletePopupComponent
    ],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebMBInternalTransferTaxModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
