import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { EbwebSharedModule } from 'app/shared';
import { ReactiveFormsModule } from '@angular/forms';

import { NgxMaskModule } from 'ngx-mask';
import { EbVirtualScrollerModule } from 'app/virtual-scroller/virtual-scroller';
import { DanhLaiSoChungTuComponent } from 'app/tien-ich/danh-lai-so-chung-tu/danh-lai-so-chung-tu.component';
import { DanhLaiSoChungTuRoute } from 'app/tien-ich/danh-lai-so-chung-tu/danh-lai-so-chung-tu.route';

const ENTITY_STATES = [...DanhLaiSoChungTuRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES), ReactiveFormsModule, NgxMaskModule, EbVirtualScrollerModule],
    declarations: [DanhLaiSoChungTuComponent],
    entryComponents: [DanhLaiSoChungTuComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebDanhLaiSoChungTuModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
