import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { EbwebSharedModule } from 'app/shared';
import { NgSelectModule } from '@ng-select/ng-select';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { HoaDonHuyComponent } from 'app/hoa-don-dien-tu/hoa_don_huy/hoa-don-huy';
import { HoaDonHuyRoute } from 'app/hoa-don-dien-tu/hoa_don_huy/hoa-don-huy.route';
import { NgxMaskModule } from 'ngx-mask';

const ENTITY_STATES = [...HoaDonHuyRoute];

@NgModule({
    imports: [EbwebSharedModule, ReactiveFormsModule, RouterModule.forChild(ENTITY_STATES), NgSelectModule, NgxMaskModule],
    declarations: [HoaDonHuyComponent],
    entryComponents: [HoaDonHuyComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebHoaDonHuyModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
