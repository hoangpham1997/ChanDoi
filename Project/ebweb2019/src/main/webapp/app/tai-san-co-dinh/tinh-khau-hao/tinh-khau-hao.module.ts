import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import { TinhKhauHaoComponent, tinhKhauHaoRoute, TinhKhauHaoUpdateComponent } from './index';
import { EbwebContextMenuModule } from 'app/contextmenu/contexmenu.module';
import { NgxMaskModule } from 'ngx-mask';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...tinhKhauHaoRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES), EbwebContextMenuModule, NgxMaskModule],
    declarations: [TinhKhauHaoComponent, TinhKhauHaoUpdateComponent],
    providers: [UtilsService],
    entryComponents: [TinhKhauHaoComponent, TinhKhauHaoUpdateComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TinhKhauHaoModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
