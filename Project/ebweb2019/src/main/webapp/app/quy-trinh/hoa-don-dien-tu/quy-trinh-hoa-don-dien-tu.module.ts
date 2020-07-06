import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { EbwebSharedModule } from 'app/shared';
import { NgSelectModule } from '@ng-select/ng-select';
import { EbwebContextMenuModule } from 'app/contextmenu/contexmenu.module';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { QuyTrinhTongHopComponent } from 'app/quy-trinh/tong-hop/tong-hop.component';
import { quyTrinhHoaDonDienTuRoute } from './quy-trinh-hoa-don-dien-tu.route';
import { QuyTrinhHoaDonDienTuComponent } from './quy-trinh-hoa-don-dien-tu.component';

const ENTITY_STATES = [...quyTrinhHoaDonDienTuRoute];
@NgModule({
    imports: [EbwebSharedModule, ReactiveFormsModule, RouterModule.forChild(ENTITY_STATES), NgSelectModule, EbwebContextMenuModule],
    declarations: [QuyTrinhHoaDonDienTuComponent],
    providers: [UtilsService],
    entryComponents: [QuyTrinhHoaDonDienTuComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebQuyTrinhHoaDonDienTuModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
