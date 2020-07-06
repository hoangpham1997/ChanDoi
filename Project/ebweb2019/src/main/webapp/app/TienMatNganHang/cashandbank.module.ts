import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { EbwebMBDepositModule } from './BaoCo/mb-deposit/mb-deposit.module';
import { EbwebMBInternalTransferModule } from './mb-internal-transfer/mb-internal-transfer.module';
import { EbwebMCReceiptModule } from './phieu-thu/mc-receipt/mc-receipt.module';
import { EbwebMBInternalTransferDetailsModule } from './mb-internal-transfer-details/mb-internal-transfer-details.module';
import { EbwebMBInternalTransferTaxModule } from './mb-internal-transfer-tax/mb-internal-transfer-tax.module';
import { EbwebMCAuditModule } from './kiem_ke_quy/mc-audit.module';
import { EbwebMBCreditCardModule } from './TheTinDung/mb-credit-card/mb-credit-card.module';
import { EbwebMCPaymentModule } from './phieu-chi/mc-payment/mc-payment.module';
import { EbwebMBTellerPaperModule } from './BaoNo/mb-teller-paper/mb-teller-paper.module';
import { EbwebMBTellerPaperDetailsModule } from './BaoNo/mb-teller-paper-details/mb-teller-paper-details.module';
import { EbwebMBTellerPaperDetailTaxModule } from './BaoNo/mb-teller-paper-detail-tax/mb-teller-paper-detail-tax.module';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { EbwebBankCompareModule } from 'app/TienMatNganHang/doi_chieu_ngan_hang/bank-compare.module';

/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        EbwebMCReceiptModule,
        EbwebMCPaymentModule,
        EbwebMBTellerPaperModule,
        EbwebMBTellerPaperDetailsModule,
        EbwebMBTellerPaperDetailTaxModule,
        EbwebMBDepositModule,
        EbwebMBCreditCardModule,
        EbwebMBInternalTransferModule,
        EbwebMBInternalTransferDetailsModule,
        EbwebMBInternalTransferTaxModule,
        EbwebMCAuditModule,
        EbwebBankCompareModule
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebCashAndBankModule {}
