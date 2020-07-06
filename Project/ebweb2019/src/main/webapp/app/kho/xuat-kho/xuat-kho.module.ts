import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { EbwebSharedModule } from 'app/shared';
import { ReactiveFormsModule } from '@angular/forms';
import { NgxMaskModule } from 'ngx-mask';
import { XuatKhoPopupRoute, XuatKhoRoute } from 'app/kho/xuat-kho/xuat-kho.route';
import { XuatKhoDeleteDialogComponent, XuatKhoDeletePopupComponent } from 'app/kho/xuat-kho/xuat-kho-delete-dialog.component';
import { XuatKhoComponent } from 'app/kho/xuat-kho/xuat-kho.component';
import { XuatKhoUpdateComponent } from 'app/kho/xuat-kho/xuat-kho-update.component';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';

const ENTITY_STATES = [...XuatKhoRoute, ...XuatKhoPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES), ReactiveFormsModule, NgxMaskModule],
    declarations: [XuatKhoComponent, XuatKhoUpdateComponent, XuatKhoDeleteDialogComponent, XuatKhoDeletePopupComponent],
    entryComponents: [XuatKhoComponent, XuatKhoUpdateComponent, XuatKhoDeleteDialogComponent, XuatKhoDeletePopupComponent],
    // providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebXuatKhoModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
