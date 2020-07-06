import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { EbwebSharedModule } from 'app/shared';
import { NgSelectModule } from '@ng-select/ng-select';
import { EbwebContextMenuModule } from 'app/contextmenu/contexmenu.module';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { quyTrinhQuanLyHoaDonRoute } from 'app/quy-trinh/quan-ly-hoa-don/quy-trinh-quan-ly-hoa-don.route';
import { QuyTrinhQuanLyHoaDonComponent } from 'app/quy-trinh/quan-ly-hoa-don/quy-trinh-quan-ly-hoa-don.component';

const ENTITY_STATES = [...quyTrinhQuanLyHoaDonRoute];
@NgModule({
    imports: [EbwebSharedModule, ReactiveFormsModule, RouterModule.forChild(ENTITY_STATES), NgSelectModule, EbwebContextMenuModule],
    declarations: [QuyTrinhQuanLyHoaDonComponent],
    providers: [UtilsService],
    entryComponents: [QuyTrinhQuanLyHoaDonComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebQuyTrinhQuanLyHoaDonModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
