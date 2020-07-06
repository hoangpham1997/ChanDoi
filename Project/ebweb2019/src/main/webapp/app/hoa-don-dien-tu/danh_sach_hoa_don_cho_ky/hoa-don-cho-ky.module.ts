import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { EbwebSharedModule } from 'app/shared';
import { NgSelectModule } from '@ng-select/ng-select';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { HoaDonChoKyComponent } from 'app/hoa-don-dien-tu/danh_sach_hoa_don_cho_ky/hoa-don-cho-ky';
import { HoaDonChoKyRoute } from 'app/hoa-don-dien-tu/danh_sach_hoa_don_cho_ky/hoa-don-cho-ky.route';
import { NgxMaskModule } from 'ngx-mask';

const ENTITY_STATES = [...HoaDonChoKyRoute];

@NgModule({
    imports: [EbwebSharedModule, ReactiveFormsModule, RouterModule.forChild(ENTITY_STATES), NgSelectModule, NgxMaskModule],
    declarations: [HoaDonChoKyComponent],
    entryComponents: [HoaDonChoKyComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebHoaDonChoKyModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
