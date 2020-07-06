import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { ROLE } from 'app/role.constants';
import { CongNoPhaiThuModalService } from 'app/bao-cao/ban-hang/cong-no-phai-thu/cong-no-phai-thu-modal.service';
import { SoChiTietMuaHangComponent } from 'app/bao-cao/mua-hang/so-chi-tiet-mua-hang/so-chi-tiet-mua-hang.component';
import { BaoCao } from 'app/app.constants';

@Component({
    selector: 'eb-quy-trinh-ban-hang',
    templateUrl: './quy-trinh-ban-hang.component.html',
    styleUrls: ['./quy-trinh-ban-hang.component.css']
})
export class QuyTrinhBanHangComponent implements OnInit {
    idTab: number;
    arrAuthorities: any[];
    currentAccount: any;
    isRoleBHTTN: boolean;
    isRoleBHCTT: boolean;
    isRoleHBTL: boolean;
    isRoleHBGG: boolean;
    isRoleTHCNPT: boolean;
    isRoleSoCTBH: boolean;
    isRoleTTKH: boolean;
    ROLE_TTKH_Xem = ROLE.ThuTienKhachHang_Xem;
    ROLE_BH_Xem = ROLE.ChungTuBanHang_Xem;
    ROLE_HBTL_Xem = ROLE.HangBanTraLai_Xem;
    ROLE_HBGG_Xem = ROLE.HangBanGiamGia_Xem;
    ROLE_THCNPT_Xem = ROLE.TongHopCongNoPhaiThu_Xem;
    ROLE_CTBH_Xem = ROLE.SoChiTietBanHang_Xem;

    // ROLE_SoCTBH_Xem = ROLE.SoChiTietBanHang_Xem;

    constructor(
        private router: Router,
        private modalService: NgbModal,
        private congNoPhaiThuModalService: CongNoPhaiThuModalService,
        private principal: Principal,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {
        this.principal.identity().then(account => {
            this.arrAuthorities = account.authorities;
            this.isRoleTTKH = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_TTKH_Xem) : true;
            this.isRoleBHTTN = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_BH_Xem) : true;
            this.isRoleBHCTT = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_BH_Xem) : true;
            this.isRoleHBTL = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_HBTL_Xem) : true;
            this.isRoleHBGG = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_HBGG_Xem) : true;
            this.isRoleTHCNPT = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_THCNPT_Xem) : true;
            this.isRoleSoCTBH = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_CTBH_Xem) : true;
            // this.isRoleSoCTBH = !this.arrAuthorities.includes('ROLE_ADMIN')
            //     ? this.arrAuthorities.includes(this.ROLE_)
            //     : true;
        });
    }

    ngOnInit() {
        this.idTab = 1;
    }

    navigateThuTienKhachHang() {
        if (this.isRoleTTKH) {
            this.router.navigate(['sa-receipt-debit']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateBanHangThuTienNgay() {
        if (this.isRoleBHTTN) {
            this.router.navigate(['chung-tu-ban-hang']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateBanHangChuaThuTien() {
        if (this.isRoleBHCTT) {
            this.router.navigate(['chung-tu-ban-hang']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateHangBanTraLai() {
        if (this.isRoleHBTL) {
            this.router.navigate(['hang-ban', 'tra-lai']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateHangBanGiamGia() {
        if (this.isRoleHBGG) {
            this.router.navigate(['hang-ban', 'giam-gia']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    showTongHopCongNoPhaiThu() {
        if (this.isRoleTHCNPT) {
            this.congNoPhaiThuModalService.open(status);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    showSoChiTietBanHang() {
        if (this.isRoleSoCTBH) {
            const modalRef = this.modalService.open(SoChiTietMuaHangComponent as Component, {
                size: 'lg',
                windowClass: 'width-80 width-ct',
                backdrop: 'static'
            });
            modalRef.componentInstance.reportType = BaoCao.BanHang.SO_CHI_TIET_BAN_HANG;
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }
}
