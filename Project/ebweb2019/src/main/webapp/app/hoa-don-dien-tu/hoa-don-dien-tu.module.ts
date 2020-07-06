import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';

import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { EbwebDanhSachHoaDonModule } from 'app/hoa-don-dien-tu/danh_sach_hoa_don/danh-sach-hoa-don.module';
import { EbwebHoaDonChoKyModule } from 'app/hoa-don-dien-tu/danh_sach_hoa_don_cho_ky/hoa-don-cho-ky.module';
import { EbwebHoaDonHuyModule } from 'app/hoa-don-dien-tu/hoa_don_huy/hoa-don-huy.module';
import { EbwebChuyenDoiHoaDonModule } from 'app/hoa-don-dien-tu/chuyen_doi_hoa_don/chuyen-doi-hoa-don.module';
import { EbwebHoaDonDieuChinhModule } from 'app/hoa-don-dien-tu/hoa_don_dieu_chinh/hoa-don-dieu-chinh.module';
import { EbwebHoaDonThayTheModule } from 'app/hoa-don-dien-tu/hoa_don_thay_the/hoa-don-thay-the.module';

/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        EbwebDanhSachHoaDonModule,
        EbwebHoaDonChoKyModule,
        EbwebHoaDonHuyModule,
        EbwebChuyenDoiHoaDonModule,
        EbwebHoaDonDieuChinhModule,
        EbwebHoaDonThayTheModule
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebHoaDonDienTuModule {}
