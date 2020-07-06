import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { EbwebSharedModule } from 'app/shared';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { EbwebPPPayVendorModule } from './tra_tien_nha_cung_cap/pp-pay-vendor.module';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { EbwebDonMuaHangModule } from 'app/muahang/don-mua-hang/don-mua-hang.module';
import { EbwebPPInvoiceModule } from 'app/muahang/mua_hang_qua_kho/pp-invoice.module';
import { EbwebMuaDichVuModule } from 'app/muahang/mua-dich-vu/mua-dich-vu.module';
import { NgxMaskModule } from 'ngx-mask';

@NgModule({
    imports: [EbwebPPPayVendorModule, EbwebSharedModule, EbwebDonMuaHangModule, EbwebPPInvoiceModule, EbwebMuaDichVuModule, NgxMaskModule],
    entryComponents: [],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebMuaHangModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
