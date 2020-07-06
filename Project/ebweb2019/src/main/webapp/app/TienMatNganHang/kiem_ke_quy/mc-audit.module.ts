import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    MCAuditComponent,
    MCAuditUpdateComponent,
    MCAuditDeletePopupComponent,
    MCAuditDeleteDialogComponent,
    mCAuditRoute,
    mCAuditPopupRoute
} from './index';
import { NgSelectModule } from '@ng-select/ng-select';
import { EbwebContextMenuModule } from 'app/contextmenu/contexmenu.module';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...mCAuditRoute, ...mCAuditPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES), NgSelectModule, EbwebContextMenuModule],
    declarations: [MCAuditComponent, MCAuditUpdateComponent, MCAuditDeleteDialogComponent, MCAuditDeletePopupComponent],
    entryComponents: [MCAuditComponent, MCAuditUpdateComponent, MCAuditDeleteDialogComponent, MCAuditDeletePopupComponent],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebMCAuditModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
