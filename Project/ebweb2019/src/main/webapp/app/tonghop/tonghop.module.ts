import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { UtilsService } from '../entities/UtilsService/Utils.service';
import { EbwebGOtherVoucherModule } from './chung_tu_nghiep_vu_khac/g-other-voucher.module';
import { EbwebChiPhiTraTruocModule } from './chi_phi_tra_truoc/chi-phi-tra-truoc.module';
import { EbwebPhanBoChiPhiTraTruocModule } from './phan_bo_chi_phi_tra_truoc/phan-bo-chi-phi-tra-truoc.module';

/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        EbwebGOtherVoucherModule,
        EbwebChiPhiTraTruocModule,
        EbwebPhanBoChiPhiTraTruocModule
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebTongHopModule {}
