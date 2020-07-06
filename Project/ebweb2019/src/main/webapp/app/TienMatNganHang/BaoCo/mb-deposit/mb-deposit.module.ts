import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { EbwebSharedModule } from 'app/shared';
import {
    MBDepositComponent,
    MBDepositDetailComponent,
    MBDepositUpdateComponent,
    MBDepositDeletePopupComponent,
    MBDepositDeleteDialogComponent,
    mBDepositRoute,
    mBDepositPopupRoute
} from './index';
import { NgSelectModule } from '@ng-select/ng-select';
import { EbwebContextMenuModule } from 'app/contextmenu/contexmenu.module';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...mBDepositRoute, ...mBDepositPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, ReactiveFormsModule, RouterModule.forChild(ENTITY_STATES), NgSelectModule, EbwebContextMenuModule],
    declarations: [
        MBDepositComponent,
        MBDepositDetailComponent,
        MBDepositUpdateComponent,
        MBDepositDeleteDialogComponent,
        MBDepositDeletePopupComponent
    ],
    providers: [UtilsService],
    entryComponents: [MBDepositComponent, MBDepositUpdateComponent, MBDepositDeleteDialogComponent, MBDepositDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebMBDepositModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
