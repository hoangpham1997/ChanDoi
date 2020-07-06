import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';

import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { EbwebXuatHoaDonModule } from 'app/ban-hang/xuat-hoa-don/xuat-hoa-don.module';
import { EbwebBankCompareModule } from 'app/TienMatNganHang/doi_chieu_ngan_hang/bank-compare.module';
import { EbwebSAQuoteModule } from 'app/ban-hang/bao_gia/sa-quote/sa-quote.module';
import { EbwebSAQuoteDetailsModule } from 'app/ban-hang/bao_gia/sa-quote-details/sa-quote-details.module';
import { EbwebHangBanTraLaiModule } from 'app/ban-hang/hang-ban-tra-lai-giam-gia/hang-ban-tra-lai.module';
import { EbwebSAInvoiceModule } from 'app/ban-hang/ban_hang_chua_thu_tien/sa-invoice.module';
import { EbwebSAReceiptDebitModule } from 'app/ban-hang/thu_tien_khach_hang/sa-receipt-debit.module';

/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        EbwebSAInvoiceModule,
        EbwebSAQuoteModule,
        EbwebSAQuoteDetailsModule,
        EbwebBankCompareModule,
        EbwebXuatHoaDonModule,
        EbwebHangBanTraLaiModule,
        EbwebSAReceiptDebitModule
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebBanHangModule {}
