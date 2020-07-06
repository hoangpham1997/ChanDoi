import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    MBInternalTransferComponent,
    MBInternalTransferDetailComponent,
    MBInternalTransferUpdateComponent,
    MBInternalTransferDeletePopupComponent,
    MBInternalTransferDeleteDialogComponent,
    mBInternalTransferRoute,
    mBInternalTransferPopupRoute
} from './index';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...mBInternalTransferRoute, ...mBInternalTransferPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MBInternalTransferComponent,
        MBInternalTransferDetailComponent,
        MBInternalTransferUpdateComponent,
        MBInternalTransferDeleteDialogComponent,
        MBInternalTransferDeletePopupComponent
    ],
    entryComponents: [
        MBInternalTransferComponent,
        MBInternalTransferUpdateComponent,
        MBInternalTransferDeleteDialogComponent,
        MBInternalTransferDeletePopupComponent
    ],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebMBInternalTransferModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
