import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SoQuyTienMatComponent } from 'app/bao-cao/quy/so-quy-tien-mat/so-quy-tien-mat.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SoTienGuiNganHangComponent } from 'app/bao-cao/ngan-hang/so-tien-gui-ngan-hang/so-tien-gui-ngan-hang.component';
import { KhoaSoKyKeToanComponent } from 'app/tonghop/khoa-so-ky-ke-toan/khoa-so-ky-ke-toan.component';
import { CalculateOWRepositoryComponent } from 'app/kho/tinh_gia_xuat_kho/tinh-gia-xuat-kho.component';
import { TheKhoComponent } from 'app/bao-cao/kho/the-kho/the-kho.component';
import { ROLE } from 'app/role.constants';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { TheTinhGiaThanhComponent } from 'app/bao-cao/gia-thanh/the-tinh-gia-thanh/the-tinh-gia-thanh.component';

@Component({
    selector: 'eb-quy-trinh-gia-thanh',
    templateUrl: './quy-trinh-gia-thanh.component.html',
    styleUrls: ['./quy-trinh-gia-thanh.component.css']
})
export class QuyTrinhGiaThanhComponent implements OnInit {
    idTab: number;
    arrAuthorities: any[];
    currentAccount: any;
    isRoleKyTinhGiaThanh: boolean;
    isRolePhanBoChiPhi: boolean;
    isRoleDanhGiaDoDang: boolean;
    isRoleTinhGiaThanh: boolean;
    isRoleTheTinhGiaThanh: boolean;
    isRoleKetChuyenChiPhi: boolean;

    ROLE_KetChuyenChiPhi_Xem = ROLE.KetChuyenChiPhi_Xem;
    ROLE_TheTinhGiaThanh_Xem = ROLE.TheTinhGiaThanh_Xem;

    constructor(
        private router: Router,
        private modalService: NgbModal,
        private principal: Principal,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {
        this.principal.identity().then(account => {
            this.arrAuthorities = account.authorities;
            this.isRoleKetChuyenChiPhi = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_KetChuyenChiPhi_Xem)
                : true;
            this.isRoleTheTinhGiaThanh = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_TheTinhGiaThanh_Xem)
                : true;
        });
    }

    ngOnInit() {
        this.idTab = 1;
    }

    navigateKyTinhGiaThanh() {
        // if (this.isRoleNhapKho) {
        //     this.router.navigate(['nhap-kho']);
        // } else {
        //     this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        // }
    }

    navigatePhanBoChiPhi() {
        // if (this.isRoleXuatKho) {
        //     this.router.navigate(['xuat-kho']);
        // } else {
        //     this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        // }
    }

    navigateDanhGiaDoDang() {
        // if (this.isRoleXuatKho) {
        //     this.router.navigate(['xuat-kho']);
        // } else {
        //     this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        // }
    }

    navigateTinhGiaThanh() {
        // if (this.isRoleXuatKho) {
        //     this.router.navigate(['xuat-kho']);
        // } else {
        //     this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        // }
    }

    navigateTheTinhGiaThanh() {
        if (this.isRoleTheTinhGiaThanh) {
            this.modalService.open(TheTinhGiaThanhComponent as Component, {
                size: 'lg',
                backdrop: 'static',
                windowClass: 'width-80 width-50'
            });
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateKetChuyenChiPhi() {
        if (this.isRoleKetChuyenChiPhi) {
            this.router.navigate(['ket-chuyen-chi-phi']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }
}
