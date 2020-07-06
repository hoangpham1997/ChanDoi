import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { EbwebSharedModule } from 'app/shared';
import { DinhMucGiaThanhThanhPhamComponent, dinhMucGiaThanhThanhPhamRoute } from './index';
import { NgSelectModule } from '@ng-select/ng-select';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...dinhMucGiaThanhThanhPhamRoute];

@NgModule({
    imports: [EbwebSharedModule, ReactiveFormsModule, RouterModule.forChild(ENTITY_STATES), NgSelectModule],
    declarations: [DinhMucGiaThanhThanhPhamComponent],
    entryComponents: [DinhMucGiaThanhThanhPhamComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebDinhMucGiaThanhThanhPhamModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
