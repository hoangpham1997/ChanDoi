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

const ENTITY_STATES = [...khoRoute];

@NgModule({
    imports: [EbwebSharedModule, ReactiveFormsModule, RouterModule.forChild(ENTITY_STATES), NgSelectModule, EbwebContextMenuModule],
    declarations: [QuyTrinhKhoComponent],
    providers: [UtilsService],
    entryComponents: [QuyTrinhKhoComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebQuyTrinhKhoModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
