import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { EbwebSharedModule } from 'app/shared';
import { BaoCaoComponent, baoCaoRoute } from './index';
import { ReactiveFormsModule } from '@angular/forms';

import { NgxMaskModule } from 'ngx-mask';
import { BangKeSoDuNganHangComponent } from 'app/bao-cao/ngan-hang/bang-ke-so-du-ngan-hang/bang-ke-so-du-ngan-hang.component';
import { SoTienGuiNganHangComponent } from 'app/bao-cao/ngan-hang/so-tien-gui-ngan-hang/so-tien-gui-ngan-hang.component';
import { SoKeToanChiTietQuyTienMatComponent } from 'app/bao-cao/quy/so-chi-tiet-quy-tien-mat/so-ke-toan-chi-tiet-quy-tien-mat.component';
import { SoQuyTienMatComponent } from 'app/bao-cao/quy/so-quy-tien-mat/so-quy-tien-mat.component';
import { BangCanDoiKeToanComponent } from 'app/bao-cao/bao-cao-tai-chinh/bang-can-doi-ke-toan/bang-can-doi-ke-toan.component';
import { TongHopTonKhoComponent } from 'app/bao-cao/kho/tong-hop-ton-kho/tong-hop-ton-kho.component';
import { BangCanDoiTaiKhoanComponent } from 'app/bao-cao/bao-cao-tai-chinh/bang-can-doi-tai-khoan/bang-can-doi-tai-khoan.component';
import { TheKhoComponent } from 'app/bao-cao/kho/the-kho/the-kho.component';
import { SoNhatKyThuTienComponent } from 'app/bao-cao/so-nhat-ky-thu-tien/so-nhat-ky-thu-tien.component';
import { BangKeMuaBanComponent } from 'app/bao-cao/bang-ke-mua-ban/bang-ke-mua-ban.component';
import { SoChiTietTaiKhoanComponent } from 'app/bao-cao/tong-hop/so-chi-tiet-tai-khoan/so-chi-tiet-tai-khoan.component';
import { NhatKyChungComponent } from 'app/bao-cao/tong-hop/nhat-ky-chung/nhat-ky-chung.component';
import { SoChiTietVatLieuComponent } from 'app/bao-cao/kho/so-chi-tiet-vat-lieu/so-chi-tiet-vat-lieu.component';
import { SoCaiHinhThucNhatKyChungComponent } from 'app/bao-cao/tong-hop/so-cai-hinh-thuc-nhat-ky-chung/so-cai-hinh-thuc-nhat-ky-chung.component';
import { BangTongHopChiTietVatLieuComponent } from 'app/bao-cao/kho/bang-tong-hop-chi-tiet-vat-lieu/bang-tong-hop-chi-tiet-vat-lieu.component';
import { TinhHinhSuDungHoaDonComponent } from 'app/bao-cao/hoa-don-dien-tu/tinh-hinh-su-dung-hoa-don/tinh-hinh-su-dung-hoa-don.component';
import { BaoCaoKetQuaComponent } from 'app/bao-cao/bao-cao-tai-chinh/bao-cao-ket-qua/bao-cao-ket-qua.component';
import { SoChiPhiSanXuatKinhDoanhComponent } from 'app/bao-cao/gia-thanh/so-chi-phi-san-xuat-kinh-doanh/so-chi-phi-san-xuat-kinh-doanh.component';
import { TheTinhGiaThanhComponent } from 'app/bao-cao/gia-thanh/the-tinh-gia-thanh/the-tinh-gia-thanh.component';
import { SoTheoDoiDoiTuongTHCPTheoTaiKhoanComponent } from 'app/bao-cao/gia-thanh/so-theo-doi-THCP-theoTK/so-theo-doi-doi-tuong-THCP-theo-tai-khoan.component';
import { SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiComponent } from 'app/bao-cao/gia-thanh/so-theo-doi-THCP-theo-khoan-muc-chi-phi/so-theo-doi-doi-tuong-THCP-theo-khoan-muc-chi-phi.component';
import { TongHopChiPhiTheoKhoanMucChiPhiComponent } from 'app/bao-cao/tong-hop/tong-hop-chi-phi-theo-khoan-muc-chi-phi/tong-hop-chi-phi-theo-khoan-muc-chi-phi.component';
import { SoTheoDoiThanhToanBangNgoaiTeComponent } from 'app/bao-cao/tong-hop/so-theo-doi-thanh-toan-bang-ngoai-te/so-theo-doi-thanh-toan-bang-ngoai-te.component';
import { SoTheoDoiMaThongKeComponent } from 'app/bao-cao/tong-hop/so-theo-doi-ma-thong-ke/so-theo-doi-ma-thong-ke';
import { SoCongCuDungCuComponent } from 'app/bao-cao/cong-cu-dung-cu/so-cong-cu-dung-cu/so-cong-cu-dung-cu.component';
import { SoTheoDoiCongCuDungCuTaiNoiSuDungComponent } from 'app/bao-cao/cong-cu-dung-cu/so-theo-doi-cong-cu-dung-cu-tai-noi-sd/so-theo-doi-cong-cu-dung-cu-tai-noi-su-dung.component';
import { SoTheoDoiLaiLoTheoHoaDonComponent } from 'app/bao-cao/ban-hang/so-theo-doi-lai-lo-theo-hoa-don/so-theo-doi-lai-lo-theo-hoa-don.component';
import { SoTheoDoiCongNoPhaiThuTheoHoaDonComponent } from 'app/bao-cao/ban-hang/so-theo-doi-cong-no-phai-thu-theo-hoa-don/so-theo-doi-cong-no-phai-thu-theo-hoa-don.component';

const ENTITY_STATES = [...baoCaoRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES), ReactiveFormsModule, NgxMaskModule],
    declarations: [
        BaoCaoComponent,
        BangKeSoDuNganHangComponent,
        SoKeToanChiTietQuyTienMatComponent,
        BangCanDoiKeToanComponent,
        TongHopTonKhoComponent,
        BangCanDoiTaiKhoanComponent,
        SoNhatKyThuTienComponent,
        BangKeMuaBanComponent,
        SoChiTietTaiKhoanComponent,
        NhatKyChungComponent,
        SoChiTietVatLieuComponent,
        SoCaiHinhThucNhatKyChungComponent,
        BangTongHopChiTietVatLieuComponent,
        TinhHinhSuDungHoaDonComponent,
        SoChiPhiSanXuatKinhDoanhComponent,
        SoTheoDoiDoiTuongTHCPTheoTaiKhoanComponent,
        SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiComponent,
        TongHopChiPhiTheoKhoanMucChiPhiComponent,
        SoTheoDoiMaThongKeComponent,
        TongHopChiPhiTheoKhoanMucChiPhiComponent,
        SoCongCuDungCuComponent,
        SoTheoDoiCongCuDungCuTaiNoiSuDungComponent,
        SoTheoDoiLaiLoTheoHoaDonComponent,
        SoTheoDoiCongNoPhaiThuTheoHoaDonComponent,
        SoTheoDoiThanhToanBangNgoaiTeComponent
    ],
    entryComponents: [
        BaoCaoComponent,
        BangKeSoDuNganHangComponent,
        SoKeToanChiTietQuyTienMatComponent,
        BangCanDoiKeToanComponent,
        TongHopTonKhoComponent,
        BangCanDoiTaiKhoanComponent,
        SoNhatKyThuTienComponent,
        BangKeMuaBanComponent,
        SoChiTietTaiKhoanComponent,
        NhatKyChungComponent,
        SoChiTietVatLieuComponent,
        SoCaiHinhThucNhatKyChungComponent,
        BangTongHopChiTietVatLieuComponent,
        TinhHinhSuDungHoaDonComponent,
        BaoCaoKetQuaComponent,
        SoChiPhiSanXuatKinhDoanhComponent,
        SoTheoDoiDoiTuongTHCPTheoTaiKhoanComponent,
        SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiComponent,
        TongHopChiPhiTheoKhoanMucChiPhiComponent,
        SoTheoDoiMaThongKeComponent,
        TongHopChiPhiTheoKhoanMucChiPhiComponent,
        SoCongCuDungCuComponent,
        SoTheoDoiCongCuDungCuTaiNoiSuDungComponent,
        SoTheoDoiLaiLoTheoHoaDonComponent,
        SoTheoDoiCongNoPhaiThuTheoHoaDonComponent,
        SoTheoDoiThanhToanBangNgoaiTeComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    providers: [UtilsService]
})
export class EbwebBaoCaoModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
