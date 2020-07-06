import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { EbwebSharedModule } from 'app/shared';
import {
    MBCreditCardComponent,
    MBCreditCardDetailComponent,
    MBCreditCardUpdateComponent,
    MBCreditCardDeletePopupComponent,
    MBCreditCardDeleteDialogComponent,
    mBCreditCardRoute,
    mBCreditCardPopupRoute
} from './index';
import { NgSelectModule } from '@ng-select/ng-select';
import { EbwebContextMenuModule } from 'app/contextmenu/contexmenu.module';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...mBCreditCardRoute, ...mBCreditCardPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, ReactiveFormsModule, RouterModule.forChild(ENTITY_STATES), NgSelectModule, EbwebContextMenuModule],
    declarations: [
        MBCreditCardComponent,
        MBCreditCardDetailComponent,
        MBCreditCardUpdateComponent,
        MBCreditCardDeleteDialogComponent,
        MBCreditCardDeletePopupComponent
    ],
    entryComponents: [
        MBCreditCardComponent,
        MBCreditCardUpdateComponent,
        MBCreditCardDeleteDialogComponent,
        MBCreditCardDeletePopupComponent
    ],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebMBCreditCardModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
