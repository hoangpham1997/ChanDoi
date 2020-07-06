import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { EbwebSharedModule } from 'app/shared';
import { ReactiveFormsModule } from '@angular/forms';
import { NgxMaskModule } from 'ngx-mask';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { ChuyenKhoPopupRoute, ChuyenKhoRoute } from 'app/kho/chuyen-kho/chuyen-kho.route';
import { ChuyenKhoComponent } from 'app/kho/chuyen-kho/chuyen-kho.component';
import { ChuyenKhoDeleteDialogComponent, ChuyenKhoDeletePopupComponent } from 'app/kho/chuyen-kho/chuyen-kho-delete-dialog.component';
import { ChuyenKhoUpdateComponent } from 'app/kho/chuyen-kho/chuyen-kho-update.component';

const ENTITY_STATES = [...ChuyenKhoRoute, ...ChuyenKhoPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES), ReactiveFormsModule, NgxMaskModule],
    declarations: [ChuyenKhoComponent, ChuyenKhoUpdateComponent, ChuyenKhoDeleteDialogComponent, ChuyenKhoDeletePopupComponent],
    entryComponents: [ChuyenKhoComponent, ChuyenKhoUpdateComponent, ChuyenKhoDeleteDialogComponent, ChuyenKhoDeletePopupComponent],
    // providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebChuyenKhoModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
