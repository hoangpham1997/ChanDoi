import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    MCReceiptComponent,
    MCReceiptDeleteDialogComponent,
    MCReceiptDeletePopupComponent,
    MCReceiptDetailComponent,
    mCReceiptPopupRoute,
    mCReceiptRoute,
    MCReceiptUpdateComponent
} from './index';
import { EbwebContextMenuModule } from 'app/contextmenu/contexmenu.module';
import { NgxMaskModule } from 'ngx-mask';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...mCReceiptRoute, ...mCReceiptPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES), EbwebContextMenuModule, NgxMaskModule],
    declarations: [
        MCReceiptComponent,
        MCReceiptDetailComponent,
        MCReceiptUpdateComponent,
        MCReceiptDeleteDialogComponent,
        MCReceiptDeletePopupComponent
    ],
    providers: [UtilsService],
    entryComponents: [MCReceiptComponent, MCReceiptUpdateComponent, MCReceiptDeleteDialogComponent, MCReceiptDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebMCReceiptModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
