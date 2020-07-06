import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { EbwebSharedModule } from 'app/shared';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { SAReceiptDebitComponent } from 'app/ban-hang/thu_tien_khach_hang/sa-receipt-debit.component';
import { SAReceiptDebitDetailComponent } from 'app/ban-hang/thu_tien_khach_hang/sa-receipt-debit-detail.component';
import { sAReceiptDebitRoute } from 'app/ban-hang/thu_tien_khach_hang/sa-receipt-debit.route';

const ENTITY_STATES = [...sAReceiptDebitRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [SAReceiptDebitComponent, SAReceiptDebitDetailComponent],
    entryComponents: [SAReceiptDebitComponent, SAReceiptDebitDetailComponent],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebSAReceiptDebitModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
