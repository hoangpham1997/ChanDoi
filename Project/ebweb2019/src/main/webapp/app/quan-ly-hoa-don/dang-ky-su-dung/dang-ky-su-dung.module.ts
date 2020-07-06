import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import { ReactiveFormsModule } from '@angular/forms';
import { NgxMaskModule } from 'ngx-mask';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { DangKySuDungRoute, DangKySuDungsPopupRoute } from 'app/quan-ly-hoa-don/dang-ky-su-dung/dang-ky-su-dung.route';
import {
    DangKySuDungDeleteDialogComponent,
    DangKySuDungDeletePopupComponent
} from 'app/quan-ly-hoa-don/dang-ky-su-dung/dang-ky-su-dung-delete-dialog.component';
import { DangKySuDungComponent } from 'app/quan-ly-hoa-don/dang-ky-su-dung/dang-ky-su-dung.component';
import { DangKySuDungUpdateComponent } from 'app/quan-ly-hoa-don/dang-ky-su-dung/dang-ky-su-dung-update.component';

const ENTITY_STATES = [...DangKySuDungRoute, ...DangKySuDungsPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES), ReactiveFormsModule, NgxMaskModule],
    declarations: [DangKySuDungComponent, DangKySuDungUpdateComponent, DangKySuDungDeletePopupComponent, DangKySuDungDeleteDialogComponent],
    entryComponents: [
        DangKySuDungComponent,
        DangKySuDungUpdateComponent,
        DangKySuDungDeletePopupComponent,
        DangKySuDungDeleteDialogComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    providers: [UtilsService]
})
export class EbwebDangKySuDungModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
