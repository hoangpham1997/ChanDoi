import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';

import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { KetChuyenLaiLoUpdateComponent } from 'app/tonghop/ket_chuyen_lai_lo/ket-chuyen-lai-lo-update.component';
import { KetChuyenLaiLoRoute } from 'app/tonghop/ket_chuyen_lai_lo/ket-chuyen-lai-lo.route';
import { KetChuyenLaiLoComponent } from 'app/tonghop/ket_chuyen_lai_lo/ket-chuyen-lai-lo.component';

const ENTITY_STATES = [...KetChuyenLaiLoRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [KetChuyenLaiLoComponent, KetChuyenLaiLoUpdateComponent],
    providers: [UtilsService],
    entryComponents: [KetChuyenLaiLoComponent, KetChuyenLaiLoUpdateComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebKetChuyenLaiLoModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
