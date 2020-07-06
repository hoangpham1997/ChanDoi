import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { EbwebSharedModule } from 'app/shared';
import { NhanHoaDonComponent, nhanHoaDonRoute } from './index';
import { NgSelectModule } from '@ng-select/ng-select';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';

const ENTITY_STATES = [...nhanHoaDonRoute];

@NgModule({
    imports: [EbwebSharedModule, ReactiveFormsModule, RouterModule.forChild(ENTITY_STATES), NgSelectModule],
    declarations: [NhanHoaDonComponent],
    entryComponents: [NhanHoaDonComponent],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebNhanHoaDonModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
