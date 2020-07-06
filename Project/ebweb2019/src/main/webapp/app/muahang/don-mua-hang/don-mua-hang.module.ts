import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import { DonMuaHangComponent, DonMuaHangPopupRoute, DonMuaHangRoute, DonMuaHangUpdateComponent } from './index';
import { ReactiveFormsModule } from '@angular/forms';
import {
    DonMuaHangDeleteDialogComponent,
    DonMuaHangDeletePopupComponent
} from 'app/muahang/don-mua-hang/don-mua-hang-delete-dialog.component';
import { NgxMaskModule } from 'ngx-mask';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';

const ENTITY_STATES = [...DonMuaHangRoute, ...DonMuaHangPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES), ReactiveFormsModule, NgxMaskModule],
    declarations: [DonMuaHangComponent, DonMuaHangUpdateComponent, DonMuaHangDeleteDialogComponent, DonMuaHangDeletePopupComponent],
    entryComponents: [DonMuaHangComponent, DonMuaHangUpdateComponent, DonMuaHangDeleteDialogComponent, DonMuaHangDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    providers: [UtilsService]
})
export class EbwebDonMuaHangModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
