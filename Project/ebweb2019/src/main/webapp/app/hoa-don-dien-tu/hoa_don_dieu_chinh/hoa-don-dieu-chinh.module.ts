import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { EbwebSharedModule } from 'app/shared';
import { NgSelectModule } from '@ng-select/ng-select';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { HoaDonDieuChinhComponent } from 'app/hoa-don-dien-tu/hoa_don_dieu_chinh/hoa-don-dieu-chinh';
import { HoaDonDieuChinhRoute } from 'app/hoa-don-dien-tu/hoa_don_dieu_chinh/hoa-don-dieu-chinh.route';
import { NgxMaskModule } from 'ngx-mask';

const ENTITY_STATES = [...HoaDonDieuChinhRoute];

@NgModule({
    imports: [EbwebSharedModule, ReactiveFormsModule, RouterModule.forChild(ENTITY_STATES), NgSelectModule, NgxMaskModule],
    declarations: [HoaDonDieuChinhComponent],
    entryComponents: [HoaDonDieuChinhComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebHoaDonDieuChinhModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
