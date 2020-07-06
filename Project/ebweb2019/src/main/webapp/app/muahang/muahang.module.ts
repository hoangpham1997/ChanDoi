import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { EbwebPPInvoiceModule } from 'app/muahang/mua_hang_qua_kho/pp-invoice.module';
import { EbwebDonMuaHangModule } from './don-mua-hang/don-mua-hang.module';

@NgModule({
    // prettier-ignore
    imports: [
        EbwebDonMuaHangModule
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebMuaHangModule {}
