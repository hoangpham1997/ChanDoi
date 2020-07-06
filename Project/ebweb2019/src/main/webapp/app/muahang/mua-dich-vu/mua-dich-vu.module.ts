import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { EbwebSharedModule } from 'app/shared';
import { MuaDichVuComponent, TabInsertUpdateReceiptMuaDichVuComponent, TableResultMuaDichVuComponent, muaDichVuRoute } from './index';
import { ReactiveFormsModule } from '@angular/forms';

import { NgxMaskModule } from 'ngx-mask';

const ENTITY_STATES = [...muaDichVuRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES), ReactiveFormsModule, NgxMaskModule],
    declarations: [MuaDichVuComponent, TabInsertUpdateReceiptMuaDichVuComponent, TableResultMuaDichVuComponent],
    entryComponents: [MuaDichVuComponent, TabInsertUpdateReceiptMuaDichVuComponent, TableResultMuaDichVuComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    providers: [UtilsService]
})
export class EbwebMuaDichVuModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
