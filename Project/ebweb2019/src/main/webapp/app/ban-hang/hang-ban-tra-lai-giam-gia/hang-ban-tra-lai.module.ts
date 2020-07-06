import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { EbwebSharedModule } from 'app/shared';
import { NgSelectModule } from '@ng-select/ng-select';
import { EbContextMenuModule } from 'app/shared/context-menu/contex-menu.module';
import { hangBanTraLaiPopupRoute, hangBanTraLaiRoute } from 'app/ban-hang/hang-ban-tra-lai-giam-gia/hang-ban-tra-lai.route';
import { HangBanTraLaiComponent } from 'app/ban-hang/hang-ban-tra-lai-giam-gia/hang-ban-tra-lai.component';
import { HangBanTraLaiUpdateComponent } from 'app/ban-hang/hang-ban-tra-lai-giam-gia/hang-ban-tra-lai-update.component';
import {
    HangBanTraLaiDeleteDialogComponent,
    HangBanTraLainDeletePopupComponent
} from 'app/ban-hang/hang-ban-tra-lai-giam-gia/hang-ban-tra-lai-delete-dialog.component';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';

const ENTITY_STATES = [...hangBanTraLaiRoute, ...hangBanTraLaiPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, ReactiveFormsModule, RouterModule.forChild(ENTITY_STATES), NgSelectModule, EbContextMenuModule],
    declarations: [
        HangBanTraLaiComponent,
        HangBanTraLaiUpdateComponent,
        HangBanTraLainDeletePopupComponent,
        HangBanTraLaiDeleteDialogComponent
    ],
    entryComponents: [
        HangBanTraLaiComponent,
        HangBanTraLaiUpdateComponent,
        HangBanTraLainDeletePopupComponent,
        HangBanTraLaiDeleteDialogComponent
    ],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebHangBanTraLaiModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
