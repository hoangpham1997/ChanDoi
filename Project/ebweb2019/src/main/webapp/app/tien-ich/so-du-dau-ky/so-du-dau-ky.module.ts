import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { EbwebSharedModule } from 'app/shared';
import { SoDuDauKyResultComponent, SoDuDauKyInsertUpdateComponent, soDuDauKyRoute } from './index';
import { ReactiveFormsModule } from '@angular/forms';

import { NgxMaskModule } from 'ngx-mask';
import { EbVirtualScrollerModule } from 'app/virtual-scroller/virtual-scroller';

const ENTITY_STATES = [...soDuDauKyRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES), ReactiveFormsModule, NgxMaskModule, EbVirtualScrollerModule],
    declarations: [SoDuDauKyResultComponent, SoDuDauKyInsertUpdateComponent],
    entryComponents: [SoDuDauKyResultComponent, SoDuDauKyInsertUpdateComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    providers: [UtilsService]
})
export class EbwebSoDuDauKyModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
