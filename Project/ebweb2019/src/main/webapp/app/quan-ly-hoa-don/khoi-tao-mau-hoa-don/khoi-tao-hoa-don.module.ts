import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import { ReactiveFormsModule } from '@angular/forms';
import { NgxMaskModule } from 'ngx-mask';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { KhoiTaoHoaDonPopupRoute, KhoiTaoHoaDonRoute } from 'app/quan-ly-hoa-don/khoi-tao-mau-hoa-don/khoi-tao-hoa-don.route';
import { KhoiTaoHoaDonComponent } from 'app/quan-ly-hoa-don/khoi-tao-mau-hoa-don/khoi-tao-hoa-don.component';
import { KhoiTaoHoaDonUpdateComponent } from 'app/quan-ly-hoa-don/khoi-tao-mau-hoa-don/khoi-tao-hoa-don-update.component';
import {
    KhoiTaoHoaDonDeleteDialogComponent,
    KhoiTaoHoaDonDeletePopupComponent
} from 'app/quan-ly-hoa-don/khoi-tao-mau-hoa-don/khoi-tao-hoa-don-delete-dialog.component';

const ENTITY_STATES = [...KhoiTaoHoaDonRoute, ...KhoiTaoHoaDonPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES), ReactiveFormsModule, NgxMaskModule],
    declarations: [
        KhoiTaoHoaDonComponent,
        KhoiTaoHoaDonUpdateComponent,
        KhoiTaoHoaDonDeleteDialogComponent,
        KhoiTaoHoaDonDeletePopupComponent
    ],
    entryComponents: [
        KhoiTaoHoaDonComponent,
        KhoiTaoHoaDonUpdateComponent,
        KhoiTaoHoaDonDeleteDialogComponent,
        KhoiTaoHoaDonDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    providers: [UtilsService]
})
export class EbwebKhoiTaoHoaDonModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
