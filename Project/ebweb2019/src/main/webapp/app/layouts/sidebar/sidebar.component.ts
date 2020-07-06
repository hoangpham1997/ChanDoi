import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiLanguageService } from 'ng-jhipster';

import { CheckHDCD, EI_IDNhaCungCapDichVu, NCCDV_EINVOICE, SO_LAM_VIEC, TCKHAC_SDTichHopHDDT, VERSION } from 'app/app.constants';
import { ROLE } from 'app/role.constants';
import { JhiLanguageHelper, LoginModalService, LoginService, Principal } from 'app/core';
import { ConnnectEInvoiceComponent } from 'app/hoa-don-dien-tu/ket_noi_hoa_don_dien_tu/ket_noi_hoa_don_dien_tu';
import { Subscription } from 'rxjs';
import { CalculateOWRepositoryComponent } from 'app/kho/tinh_gia_xuat_kho/tinh-gia-xuat-kho.component';
import { KhoaSoKyKeToanComponent } from 'app/tonghop/khoa-so-ky-ke-toan/khoa-so-ky-ke-toan.component';
import { BoKhoaSoKyKeToanComponent } from 'app/tonghop/bo-khoa-so-ky-ke-toan/bo-khoa-so-ky-ke-toan.component';

@Component({
    selector: 'eb-sidebar',
    templateUrl: './sidebar.component.html',
    styleUrls: ['sidebar.css']
})
export class SidebarComponent implements OnInit {
    inProduction: boolean;
    isNavbarCollapsed: boolean;
    languages: any[];
    swaggerEnabled: boolean;
    modalRef: NgbModalRef;
    version: string;
    isHover: boolean;
    isInside: boolean;
    isTichHop: boolean; // Add by Hautv
    useInvoiceWait: boolean; // Add by Hautv
    account: any;
    eventSubscriber: Subscription; // Add by Hautv
    // role
    ROLE = ROLE;
    ROLE_TIENMATNGANHANG = ROLE.TienMatNganHang;
    ROLE_HDDT = ROLE.HDDT;
    ROLE_BANHANG = ROLE.BanHang;
    ROLE_TONGHOP = ROLE.TongHop;
    ROLE_BaoNo_Xem = ROLE.BaoNo_Xem;
    ROLE_BaoCo_Xem = ROLE.BaoCo_Xem;
    ROLE_TheTinDung_Xem = ROLE.TheTinDung_Xem;
    ROLE_DonMuaHang_Xem = ROLE.DonMuaHang_XEM;
    ROLE_MuaHangKhongQuaKho_Xem = ROLE.MuaHangKhongQuaKho_Xem;
    ROLE_MuaHangQuaKho_Xem = ROLE.MuaHangQuaKho_Xem;
    ROLE_QLHD = ROLE.QLHD;
    ROLE_KTHD_XEM = ROLE.KTHD_XEM;
    ROLE_DKSD_XEM = ROLE.DKSD_XEM;
    ROLE_TBPH_XEM = ROLE.TBPH_XEM;
    ROLE_NhanHoaDon_Xem = ROLE.NhanHoaDon_Xem;
    ROLE_MuaHang = ROLE.MuaHang;
    ROLE_NhapKho = ROLE.NhapKho;
    ROLE_NhapKho_Xem = ROLE.NhapKho_XEM;
    ROLE_XuatKho = ROLE.XuatKho;
    ROLE_XuatKho_Xem = ROLE.XuatKho_XEM;
    ROLE_ChuyenKho_Xem = ROLE.ChuyenKho_XEM;
    ROLE_Kho = ROLE.Kho;
    ROLE_GiaThanh = ROLE.GiaThanh;
    ROLE_TheTinDung = ROLE.TheTinDung;
    ROLE_KiemKeQuy = ROLE.KiemKeQuy_Xem;
    ROLE_NhanHoaDon = ROLE.NhanHoaDon;
    ROLE_DonDatHang = ROLE.DonDatHang_Xem;
    ROLE_TraTienNhaCungCap = ROLE.TraTienNhaCungCap_Xem;
    ROLE_CTBanHang = ROLE.ChungTuBanHang_Xem;
    ROLE_XuatHoaDon = ROLE.XuatHoaDon_Xem;
    ROLE_HangBanTraLai = ROLE.HangBanTraLai_Xem;
    ROLE_HangBanGiamGia = ROLE.HangBanGiamGia_Xem;
    ROLE_ThuTienKhachHang = ROLE.ThuTienKhachHang_Xem;
    ROLE_TinhGiaXuatKho = ROLE.TinhGiaXuatKho_Xem;
    ROLE_HangMuaTraLai = ROLE.HangMuaTraLai;
    ROLE_HangMuaGiamGia = ROLE.HangMuaGiamGia;
    ROLE_ChungTuNghiepVuKhac_Xem = ROLE.ChungTuNghiepVuKhac_Xem;
    ROLE_KetChuyenLaiLo_Xem = ROLE.KetChuyenLaiLo_Xem;
    ROLE_BaoGia_Xem = ROLE.BaoGia_Xem;
    ROLE_DinhMucNguyenVatLieu_Xem = ROLE.DinhMucNguyenVatLieu_Xem;
    ROLE_DinhMucGiaThanhThanhPham_Xem = ROLE.DinhMucGiaThanhThanhPham_Xem;
    ROLE_DinhMucPhanBoChiPhi_Xem = ROLE.DinhMucPhanBoChiPhi_Xem;
    ROLE_ChiPhiDoDangDauKy_Xem = ROLE.ChiPhiDoDangDauKy_Xem;
    ROLE_ChiPhiTraTruoc_Xem = ROLE.ChiPhiTRaTruoc_Xem;
    ROLE_PhanBoChiPhiTRaTruoc_Xem = ROLE.PhanBoChiPhiTRaTruoc_Xem;
    ROLE_PhuongPhapGianDon_Xem = ROLE.PhuongPhapGianDon_Xem;
    ROLE_PhuongPhapHeSo_Xem = ROLE.PhuongPhapHeSo_Xem;
    ROLE_PhuongPhapTyLe_Xem = ROLE.PhuongPhapTyLe_Xem;
    ROLE_GiaThanhTheoCTVV_Xem = ROLE.GiaThanhTheoCTVV_Xem;
    ROLE_GiaThanhTheoDonHang_Xem = ROLE.GiaThanhTheoDonHang_Xem;
    ROLE_GiaThanhTheoHopDong_Xem = ROLE.GiaThanhTheoHopDong_Xem;
    ROLE_KetChuyenChiPhi_Xem = ROLE.KetChuyenChiPhi_Xem;

    NCCDV: string;
    NCCDV_EINVOICE = NCCDV_EINVOICE;

    constructor(
        private loginService: LoginService,
        private languageService: JhiLanguageService,
        private languageHelper: JhiLanguageHelper,
        private principal: Principal,
        private loginModalService: LoginModalService,
        private router: Router,
        private modalService: NgbModal,
        private eventManager: JhiEventManager
    ) {
        this.version = VERSION ? 'v' + VERSION : '';
        this.isNavbarCollapsed = true;
        this.loadHDDT();
        this.registerLoginSucss();
    }

    ngOnInit() {
        this.languageHelper.getAll().then(languages => {
            this.languages = languages;
        });
    }

    changeLanguage(languageKey: string) {
        this.languageService.changeLanguage(languageKey);
    }

    collapseNavbar() {
        this.isNavbarCollapsed = true;
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    logout() {
        this.collapseNavbar();
        this.loginService.logout();
        this.router.navigate(['']);
    }

    toggleNavbar() {
        this.isNavbarCollapsed = !this.isNavbarCollapsed;
    }

    getImageUrl() {
        return this.isAuthenticated() ? this.principal.getImageUrl() : null;
    }

    addClass() {
        // if (this.isInside === true) {
        //     this.isHover = true;
        //     console.log('HOVER!!!');
        // }
        // alert('a!!!');
    }

    removeClass() {
        // if (this.isInside === false) {
        //     this.isHover = false;
        //     console.log('LEAVE!!!');
        // }
        // alert('b!!!');
    }

    test() {
        console.log('CLICKED!!!');
        // alert('CLICKED!!!');
    }

    inSideBar() {
        // this.isInside = true;
        // console.log('INSIDE!!!');
    }

    outSideBar() {
        // this.isInside = false;
        // console.log('OUTSIDE!!!');
    }

    /*
    * Chuongnv
    * */
    calculateOWRepository() {
        this.modalService.open(CalculateOWRepositoryComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
    }

    /*
    * Hautv
    * */
    ConnectEInvocie() {
        this.modalService.open(ConnnectEInvoiceComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
    }

    registerLoginSucss() {
        this.eventSubscriber = this.eventManager.subscribe('authenticationSuccess', response => {
            this.loadHDDT(true);
        });
    }

    loadHDDT(force?) {
        if (force) {
            this.principal.identity(true).then(account => {
                this.account = account;
                if (account) {
                    this.isTichHop = this.account.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '1');
                    this.useInvoiceWait = this.account.systemOption.some(x => x.code === CheckHDCD && x.data === '1');
                    this.NCCDV = this.account.systemOption.find(x => x.code === EI_IDNhaCungCapDichVu).data;
                }
            });
        } else {
            this.principal.identity().then(account => {
                this.account = account;
                if (account) {
                    this.isTichHop = this.account.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '1');
                    this.useInvoiceWait = this.account.systemOption.some(x => x.code === CheckHDCD && x.data === '1');
                    this.NCCDV = this.account.systemOption.find(x => x.code === EI_IDNhaCungCapDichVu).data;
                }
            });
        }
    }

    /*
    * @Author Hautv
    * */
    lockBook() {
        this.modalService.open(KhoaSoKyKeToanComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-40'
        });
    }

    /*
    * @Author Hautv
    * */
    unlockBook() {
        this.modalService.open(BoKhoaSoKyKeToanComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-35'
        });
    }

    navigateTMNH() {
        this.router.navigate(['quy-trinh-tien-mat-ngan-hang']);
    }

    navigateKho() {
        this.router.navigate(['quy-trinh-kho']);
    }

    navigateTongHop() {
        this.router.navigate(['quy-trinh-tong-hop']);
    }

    navigateMuaHang() {
        this.router.navigate(['quy-trinh-mua-hang']);
    }

    navigateHoaDonDienTu() {
        this.router.navigate(['quy-trinh-hoa-don-dien-tu']);
    }

    navigateQuanLyHoaDon() {
        this.router.navigate(['quy-trinh-quan-ly-hoa-don']);
    }

    navigateBanHang() {
        this.router.navigate(['quy-trinh-ban-hang']);
    }

    navigateGiaThanh() {
        this.router.navigate(['quy-trinh-gia-thanh']);
    }
}
