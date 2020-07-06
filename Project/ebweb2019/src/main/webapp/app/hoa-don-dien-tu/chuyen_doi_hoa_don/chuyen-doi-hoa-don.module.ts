import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { EbwebSharedModule } from 'app/shared';
import { NgSelectModule } from '@ng-select/ng-select';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { ChuyenDoiHoaDonComponent } from 'app/hoa-don-dien-tu/chuyen_doi_hoa_don/chuyen-doi-hoa-don';
import { ChuyenDoiHoaDonRoute } from 'app/hoa-don-dien-tu/chuyen_doi_hoa_don/chuyen-doi-hoa-don.route';
import { NgxMaskModule } from 'ngx-mask';

const ENTITY_STATES = [...ChuyenDoiHoaDonRoute];

@NgModule({
    imports: [EbwebSharedModule, ReactiveFormsModule, RouterModule.forChild(ENTITY_STATES), NgSelectModule, NgxMaskModule],
    declarations: [ChuyenDoiHoaDonComponent],
    entryComponents: [ChuyenDoiHoaDonComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebChuyenDoiHoaDonModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
