import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
// import {KhoanMucChiPhiListUpdateComponent} from "app/danhmuc/khoan-muc-chi-phi-list/khoan-muc-chi-phi-list-update.component";
import {
    KhoanMucChiPhiListDeleteDialogComponent,
    KhoanMucChiPhiListComponent,
    KhoanMucChiPhiListDeletePopupComponent,
    KhoanMucChiPhiListUpdateComponent,
    khoanMucChiPhiListRoute,
    expenseItemPopupRoute
} from './';

const ENTITY_STATES = [...khoanMucChiPhiListRoute, ...expenseItemPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        KhoanMucChiPhiListComponent,
        KhoanMucChiPhiListUpdateComponent,
        KhoanMucChiPhiListDeleteDialogComponent,
        KhoanMucChiPhiListDeletePopupComponent
    ],
    entryComponents: [
        KhoanMucChiPhiListComponent,
        KhoanMucChiPhiListUpdateComponent,
        KhoanMucChiPhiListDeleteDialogComponent,
        KhoanMucChiPhiListDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KhoanMucChiPhiListModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
