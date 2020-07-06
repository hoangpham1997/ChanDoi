import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import { ReactiveFormsModule } from '@angular/forms';
import { NgxMaskModule } from 'ngx-mask';
import { NhapKhoComponent } from 'app/kho/nhap-kho/nhap-kho.component';
import { NhapKhoUpdateComponent } from 'app/kho/nhap-kho/nhap-kho-update.component';
import { NhapKhoDeleteDialogComponent, NhapKhoDeletePopupComponent } from 'app/kho/nhap-kho/nhap-kho-delete-dialog.component';
import { NhapKhoPopupRoute, NhapKhoRoute } from 'app/kho/nhap-kho/nhap-kho.route';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';

const ENTITY_STATES = [...NhapKhoRoute, ...NhapKhoPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES), ReactiveFormsModule, NgxMaskModule],
    declarations: [NhapKhoComponent, NhapKhoUpdateComponent, NhapKhoDeleteDialogComponent, NhapKhoDeletePopupComponent],
    entryComponents: [NhapKhoComponent, NhapKhoUpdateComponent, NhapKhoDeleteDialogComponent, NhapKhoDeletePopupComponent],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebNhapKhoModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
