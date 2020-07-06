import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { EbwebSharedModule } from 'app/shared';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { NgxMaskModule } from 'ngx-mask';
import { EbwebNhapKhoModule } from 'app/kho/nhap-kho/nhap-kho.module';
import { EbwebXuatKhoModule } from 'app/kho/xuat-kho/xuat-kho.module';
import { EbwebChuyenKhoModule } from 'app/kho/chuyen-kho/chuyen-kho.module';

@NgModule({
    imports: [EbwebSharedModule, EbwebNhapKhoModule, EbwebXuatKhoModule, EbwebChuyenKhoModule, NgxMaskModule],
    declarations: [],
    entryComponents: [],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebKhoModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
