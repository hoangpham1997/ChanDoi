import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { EbwebSharedModule } from 'app/shared';
import { ReactiveFormsModule } from '@angular/forms';

import { NgxMaskModule } from 'ngx-mask';
import { EbVirtualScrollerModule } from 'app/virtual-scroller/virtual-scroller';
import { TimKiemChungTuComponent } from 'app/tien-ich/tim-kiem-chung-tu/tim-kiem-chung-tu.component';
import { TimKiemChungTuRoute } from 'app/tien-ich/tim-kiem-chung-tu/tim-kiem-chung-tu.route';

const ENTITY_STATES = [...TimKiemChungTuRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES), ReactiveFormsModule, NgxMaskModule, EbVirtualScrollerModule],
    declarations: [TimKiemChungTuComponent],
    entryComponents: [TimKiemChungTuComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebTimKiemChungTuModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
