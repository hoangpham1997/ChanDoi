import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { EbwebSharedModule } from 'app/shared';
import { NgSelectModule } from '@ng-select/ng-select';
import { EbwebContextMenuModule } from 'app/contextmenu/contexmenu.module';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { quyTrinhMuaHangRoute } from 'app/quy-trinh/mua-hang/quy-trinh-mua-hang.route';
import { QuyTrinhMuaHangComponent } from 'app/quy-trinh/mua-hang/quy-trinh-mua-hang.component';

const ENTITY_STATES = [...quyTrinhMuaHangRoute];

@NgModule({
    imports: [EbwebSharedModule, ReactiveFormsModule, RouterModule.forChild(ENTITY_STATES), NgSelectModule, EbwebContextMenuModule],
    declarations: [QuyTrinhMuaHangComponent],
    providers: [UtilsService],
    entryComponents: [QuyTrinhMuaHangComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebQuyTrinhMuaHangModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
