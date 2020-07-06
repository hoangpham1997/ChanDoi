import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import { ReactiveFormsModule } from '@angular/forms';
import { NgxMaskModule } from 'ngx-mask';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ThongBaoPhatHanhComponent } from 'app/quan-ly-hoa-don/thong-bao-phat-hanh-hoa-don/thong-bao-phat-hanh.component';
import { ThongBaoPhatHanhUpdateComponent } from 'app/quan-ly-hoa-don/thong-bao-phat-hanh-hoa-don/thong-bao-phat-hanh-update.component';
import {
    ThongBaoPhatHanhDeleteDialogComponent,
    ThongBaoPhatHanhDeletePopupComponent
} from 'app/quan-ly-hoa-don/thong-bao-phat-hanh-hoa-don/thong-bao-phat-hanh-delete-dialog.component';
import {
    ThongBaoPhatHanhPopupRoute,
    ThongBaoPhatHanhRoute
} from 'app/quan-ly-hoa-don/thong-bao-phat-hanh-hoa-don/thong-bao-phat-hanh.route';

const ENTITY_STATES = [...ThongBaoPhatHanhRoute, ...ThongBaoPhatHanhPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES), ReactiveFormsModule, NgxMaskModule],
    declarations: [
        ThongBaoPhatHanhComponent,
        ThongBaoPhatHanhUpdateComponent,
        ThongBaoPhatHanhDeleteDialogComponent,
        ThongBaoPhatHanhDeletePopupComponent
    ],
    entryComponents: [
        ThongBaoPhatHanhComponent,
        ThongBaoPhatHanhUpdateComponent,
        ThongBaoPhatHanhDeleteDialogComponent,
        ThongBaoPhatHanhDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    providers: [UtilsService]
})
export class EbwebThongBaoPhatHanhModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
