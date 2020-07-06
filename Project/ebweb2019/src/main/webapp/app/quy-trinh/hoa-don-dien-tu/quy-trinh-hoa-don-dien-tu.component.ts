import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SoQuyTienMatComponent } from 'app/bao-cao/quy/so-quy-tien-mat/so-quy-tien-mat.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SoTienGuiNganHangComponent } from 'app/bao-cao/ngan-hang/so-tien-gui-ngan-hang/so-tien-gui-ngan-hang.component';
import { BaoCaoKetQuaComponent } from 'app/bao-cao/bao-cao-tai-chinh/bao-cao-ket-qua/bao-cao-ket-qua.component';
import { KhoaSoKyKeToanComponent } from 'app/tonghop/khoa-so-ky-ke-toan/khoa-so-ky-ke-toan.component';
import { ConnnectEInvoiceComponent } from 'app/hoa-don-dien-tu/ket_noi_hoa_don_dien_tu/ket_noi_hoa_don_dien_tu';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { ROLE } from 'app/role.constants';

@Component({
    selector: 'eb-quy-trinh-hoa-don-dien-tu',
    templateUrl: './quy-trinh-hoa-don-dien-tu.component.html',
    styleUrls: ['./quy-trinh-hoa-don-dien-tu.component.css']
})
export class QuyTrinhHoaDonDienTuComponent implements OnInit {
    idTab: number;
    arrAuthorities: any[];
    currentAccount: any;
    isRoleDSHD: boolean;
    isRoleDSHDCK: boolean;
    isRoleHDTT: boolean;
    isRoleHDDC: boolean;
    isRoleHDHuy: boolean;
    isRoleCDHD: boolean;
    isRoleKNHDDT: boolean;
    isRoleXHD: boolean;
    isRoleHDDT: boolean;
    isCheckUseHDDT: boolean;

    ROLE_DSHD_Xem = ROLE.DSHD_XEM;
    ROLE_DSHD_CHO_KY_Xem = ROLE.DSHD_CHO_KY_XEM;
    ROLE_HDTT_Xem = ROLE.HD_THAY_THE_XEM;
    ROLE_HDDC_Xem = ROLE.HD_DIEU_CHINH_XEM;
    ROLE_HD_HUY_Xem = ROLE.HD_HUY_XEM;
    ROLE_CDHD_Xem = ROLE.CDHD_XEM;
    ROLE_KNHDDT_Xem = ROLE.KNHD_KET_NOI_HD;
    ROLE_XUAT_HOA_DON_XEM = ROLE.XuatHoaDon_Xem;
    ROLE_HDDT = ROLE.HDDT;

    constructor(
        private router: Router,
        private modalService: NgbModal,
        private principal: Principal,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {
        this.principal.identity().then(account => {
            this.arrAuthorities = account.authorities;
            this.isCheckUseHDDT = account.systemOption.find(a => a.code === 'TCKHAC_SDTichHopHDDT' && a.data).data === '1';
            this.isRoleKNHDDT = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_KNHDDT_Xem) : true;
            this.isRoleDSHD = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_DSHD_Xem) : true;
            this.isRoleDSHDCK = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_DSHD_CHO_KY_Xem)
                : true;
            this.isRoleHDTT = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_HDTT_Xem) : true;
            this.isRoleHDDC = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_HDDC_Xem) : true;
            this.isRoleHDHuy = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_HD_HUY_Xem) : true;
            this.isRoleCDHD = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_CDHD_Xem) : true;
            this.isRoleXHD = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_XUAT_HOA_DON_XEM) : true;
            this.isRoleHDDT = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_HDDT) : true;
        });
    }

    ngOnInit() {
        this.idTab = 1;
    }

    navigateXuatHoaDon() {
        if (this.isRoleXHD && this.isCheckUseHDDT) {
            this.router.navigate(['xuat-hoa-don']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateDanhSachHoaDon() {
        if (this.isRoleDSHD && this.isCheckUseHDDT) {
            this.router.navigate(['danh-sach-hoa-don']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateChuyenDoiHoaDon() {
        if (this.isRoleCDHD && this.isCheckUseHDDT) {
            this.router.navigate(['chuyen-doi-hoa-don']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    ConnectEInvocie() {
        if (this.isRoleKNHDDT && this.isCheckUseHDDT) {
            this.modalService.open(ConnnectEInvoiceComponent as Component, {
                size: 'lg',
                backdrop: 'static',
                windowClass: 'width-80 width-50'
            });
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }
}
