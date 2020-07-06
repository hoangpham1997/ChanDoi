import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SoQuyTienMatComponent } from 'app/bao-cao/quy/so-quy-tien-mat/so-quy-tien-mat.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SoTienGuiNganHangComponent } from 'app/bao-cao/ngan-hang/so-tien-gui-ngan-hang/so-tien-gui-ngan-hang.component';
import { KhoaSoKyKeToanComponent } from 'app/tonghop/khoa-so-ky-ke-toan/khoa-so-ky-ke-toan.component';
import { CalculateOWRepositoryComponent } from 'app/kho/tinh_gia_xuat_kho/tinh-gia-xuat-kho.component';
import { CongNoPhaiTraModalService } from 'app/bao-cao/mua-hang/cong-no-phai-tra/cong-no-phai-tra-modal.service';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { ROLE } from 'app/role.constants';
import { SoChiTietMuaHangComponent } from 'app/bao-cao/mua-hang/so-chi-tiet-mua-hang/so-chi-tiet-mua-hang.component';
import { BaoCao } from 'app/app.constants';

@Component({
    selector: 'eb-quy-trinh-mua-hang',
    templateUrl: './quy-trinh-mua-hang.component.html',
    styleUrls: ['./quy-trinh-mua-hang.component.css']
})
export class QuyTrinhMuaHangComponent implements OnInit {
    idTab: number;
    arrAuthorities: any[];
    currentAccount: any;
    isRoleMHQK: boolean;
    isRoleMHKQK: boolean;
    isRoleNHD: boolean;
    isRoleMDV: boolean;
    isRoleHMTL: boolean;
    isRoleHMGG: boolean;
    isRoleTTNCC: boolean;
    isRoleTHCN: boolean;
    isRoleSoCTMH: boolean;
    ROLE_MHQK_Xem = ROLE.MuaHangQuaKho_Xem;
    ROLE_MHKQK_Xem = ROLE.MuaHangKhongQuaKho_Xem;
    ROLE_NhanHoaDon_Xem = ROLE.NhanHoaDon_Xem;
    ROLE_HMTL_Xem = ROLE.HangMuaTraLai_Xem;
    ROLE_HMGG_Xem = ROLE.HangMuaGiamGia_Xem;
    ROLE_TTNCC_Xem = ROLE.TraTienNhaCungCap_Xem;
    ROLE_MDV_Xem = ROLE.MuaDichVu_Xem;
    ROLE_TongHopCongNo_Xem = ROLE.TongHopCongNoPhaiThu_Xem;
    ROLE_CTMH_Xem = ROLE.SoChiTietMuaHang_Xem;

    // ROLE_SoCTMH_Xem = ROLE.SoChiTietMuaHang_Xem;

    constructor(
        private router: Router,
        private modalService: NgbModal,
        private congNoPhaiTraModalService: CongNoPhaiTraModalService,
        private principal: Principal,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {
        this.principal.identity().then(account => {
            this.arrAuthorities = account.authorities;
            this.isRoleMHQK = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_MHQK_Xem) : true;
            this.isRoleMHKQK = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_MHKQK_Xem) : true;
            this.isRoleNHD = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_NhanHoaDon_Xem) : true;
            this.isRoleMDV = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_MDV_Xem) : true;
            this.isRoleHMTL = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_HMTL_Xem) : true;
            this.isRoleHMGG = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_HMGG_Xem) : true;
            this.isRoleTTNCC = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_TTNCC_Xem) : true;
            this.isRoleSoCTMH = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_CTMH_Xem) : true;
            this.isRoleTHCN = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_TongHopCongNo_Xem)
                : true;
        });
    }

    ngOnInit() {
        this.idTab = 1;
    }

    navigateMuaHangKhongQuaKho() {
        if (this.isRoleMHKQK) {
            this.router.navigate(['mua-hang', 'khong-qua-kho']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateMuaHangQuaKho() {
        if (this.isRoleMHQK) {
            this.router.navigate(['mua-hang', 'qua-kho']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateNhanHoaDon() {
        if (this.isRoleNHD) {
            this.router.navigate(['nhan-hoa-don']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateMuaDichVu() {
        if (this.isRoleMDV) {
            this.router.navigate(['mua-dich-vu']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateHangMuaTraLai() {
        if (this.isRoleHMTL) {
            this.router.navigate(['hang-mua', 'tra-lai']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateHangMuaGiamGia() {
        if (this.isRoleHMGG) {
            this.router.navigate(['hang-mua', 'giam-gia']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateTraTienNhaCC() {
        if (this.isRoleTTNCC) {
            this.router.navigate(['pp-pay-vendor']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    showCongNoPhaiTra(status) {
        if (this.isRoleTHCN) {
            this.congNoPhaiTraModalService.open(status);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    showSoChiTietMuaHang() {
        if (this.isRoleSoCTMH) {
            const modalRef = this.modalService.open(SoChiTietMuaHangComponent as Component, {
                size: 'lg',
                windowClass: 'width-80 width-ct',
                backdrop: 'static'
            });
            modalRef.componentInstance.reportType = BaoCao.MuaHang.SO_CHI_TIET_MUA_HANG;
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }
}
