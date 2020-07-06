import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    GhiTangComponent,
    GhiTangDeleteDialogComponent,
    GhiTangDeletePopupComponent,
    GhiTangDetailComponent,
    ghiTangRoute,
    GhiTangUpdateComponent,
    mCReceiptPopupRoute
} from './index';
import { EbwebContextMenuModule } from 'app/contextmenu/contexmenu.module';
import { NgxMaskModule } from 'ngx-mask';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...ghiTangRoute, ...mCReceiptPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES), EbwebContextMenuModule, NgxMaskModule],
    declarations: [
        GhiTangComponent,
        GhiTangDetailComponent,
        GhiTangUpdateComponent,
        GhiTangDeleteDialogComponent,
        GhiTangDeletePopupComponent
    ],
    providers: [UtilsService],
    entryComponents: [GhiTangComponent, GhiTangUpdateComponent, GhiTangDeleteDialogComponent, GhiTangDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebGhiTangModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
