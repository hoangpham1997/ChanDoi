import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { EbwebSharedModule } from 'app/shared';
import { NgSelectModule } from '@ng-select/ng-select';
import { EbwebContextMenuModule } from 'app/contextmenu/contexmenu.module';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { khoRoute } from 'app/quy-trinh/kho/kho.route';
import { QuyTrinhKhoComponent } from 'app/quy-trinh/kho/kho.component';
import { QuyTrinhGiaThanhComponent } from 'app/quy-trinh/gia-thanh/quy-trinh-gia-thanh.component';
import { quyTrinhGiaThanhRoute } from 'app/quy-trinh/gia-thanh/quy-trinh-gia-thanh.route';

const ENTITY_STATES = [...quyTrinhGiaThanhRoute];

@NgModule({
    imports: [EbwebSharedModule, ReactiveFormsModule, RouterModule.forChild(ENTITY_STATES), NgSelectModule, EbwebContextMenuModule],
    declarations: [QuyTrinhGiaThanhComponent],
    providers: [UtilsService],
    entryComponents: [QuyTrinhGiaThanhComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebQuyTrinhGiaThanhModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
