import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { EbwebSharedModule } from 'app/shared';
import { NgSelectModule } from '@ng-select/ng-select';
import { DanhSachHoaDonComponent } from 'app/hoa-don-dien-tu/danh_sach_hoa_don/danh-sach-hoa-don';
import { DanhSachHoaDonRoute } from 'app/hoa-don-dien-tu/danh_sach_hoa_don/danh-sach-hoa-don.route';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { NgxMaskModule } from 'ngx-mask';

const ENTITY_STATES = [...DanhSachHoaDonRoute];

@NgModule({
    imports: [EbwebSharedModule, ReactiveFormsModule, RouterModule.forChild(ENTITY_STATES), NgSelectModule, NgxMaskModule],
    declarations: [DanhSachHoaDonComponent],
    entryComponents: [DanhSachHoaDonComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebDanhSachHoaDonModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
