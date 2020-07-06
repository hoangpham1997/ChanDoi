import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    MBInternalTransferDetailsComponent,
    MBInternalTransferDetailsDetailComponent,
    MBInternalTransferDetailsUpdateComponent,
    MBInternalTransferDetailsDeletePopupComponent,
    MBInternalTransferDetailsDeleteDialogComponent,
    mBInternalTransferDetailsRoute,
    mBInternalTransferDetailsPopupRoute
} from './index';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...mBInternalTransferDetailsRoute, ...mBInternalTransferDetailsPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MBInternalTransferDetailsComponent,
        MBInternalTransferDetailsDetailComponent,
        MBInternalTransferDetailsUpdateComponent,
        MBInternalTransferDetailsDeleteDialogComponent,
        MBInternalTransferDetailsDeletePopupComponent
    ],
    entryComponents: [
        MBInternalTransferDetailsComponent,
        MBInternalTransferDetailsUpdateComponent,
        MBInternalTransferDetailsDeleteDialogComponent,
        MBInternalTransferDetailsDeletePopupComponent
    ],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebMBInternalTransferDetailsModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
