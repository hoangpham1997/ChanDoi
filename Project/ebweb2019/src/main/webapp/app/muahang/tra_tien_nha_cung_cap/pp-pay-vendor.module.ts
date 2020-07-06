import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { EbwebSharedModule } from 'app/shared';
import { PPPayVendorComponent } from 'app/muahang/tra_tien_nha_cung_cap/pp-pay-vendor.component';
import { PPPayVendorDetailComponent } from 'app/muahang/tra_tien_nha_cung_cap/pp-pay-vendor-detail.component';
import { pPPayVendorRoute } from 'app/muahang/tra_tien_nha_cung_cap/pp-pay-vendor.route';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...pPPayVendorRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [PPPayVendorComponent, PPPayVendorDetailComponent],
    entryComponents: [PPPayVendorComponent, PPPayVendorDetailComponent],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebPPPayVendorModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
