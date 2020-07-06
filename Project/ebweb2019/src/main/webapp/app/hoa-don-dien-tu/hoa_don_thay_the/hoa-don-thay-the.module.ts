import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { EbwebSharedModule } from 'app/shared';
import { NgSelectModule } from '@ng-select/ng-select';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { HoaDonThayTheRoute } from 'app/hoa-don-dien-tu/hoa_don_thay_the/hoa-don-thay-the.route';
import { HoaDonThayTheComponent } from 'app/hoa-don-dien-tu/hoa_don_thay_the/hoa-don-thay-the';
import { NgxMaskModule } from 'ngx-mask';

const ENTITY_STATES = [...HoaDonThayTheRoute];

@NgModule({
    imports: [EbwebSharedModule, ReactiveFormsModule, RouterModule.forChild(ENTITY_STATES), NgSelectModule, NgxMaskModule],
    declarations: [HoaDonThayTheComponent],
    entryComponents: [HoaDonThayTheComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebHoaDonThayTheModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
