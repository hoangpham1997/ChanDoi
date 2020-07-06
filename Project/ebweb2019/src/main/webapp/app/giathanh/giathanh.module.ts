import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { EbwebDinhMucGiaThanhThanhPhamModule } from 'app/giathanh/dinh_muc_gia_thanh_thanh_pham/dinh-muc-gia-thanh-thanh-pham.module';
import { EbwebCPPeriodModule } from 'app/giathanh/phuong_phap_gian_don/pp-gian-don.module';
import { EbwebCPAllocationQuantumModule } from 'app/giathanh/dinh_muc_phan_bo_chi_phi/cp-allocation-quantum.module';
import { EbwebGiaThanhTheoCongTrinhVuViecModule } from 'app/giathanh/phuong_phap_cong_trinh_vu_viec/pp-cong-trinh-vu-viec.module';

/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        EbwebDinhMucGiaThanhThanhPhamModule,
        EbwebCPPeriodModule,
        EbwebCPAllocationQuantumModule,
        EbwebGiaThanhTheoCongTrinhVuViecModule
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebGiaThanhModule {}
