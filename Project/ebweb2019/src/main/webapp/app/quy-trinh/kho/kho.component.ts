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

@Component({
    selector: 'eb-quy-trinh-kho',
    templateUrl: './kho.component.html',
    styleUrls: ['./kho.component.css']
})
export class QuyTrinhKhoComponent implements OnInit {
    idTab: number;
    arrAuthorities: any[];
    currentAccount: any;
    isRoleNhapKho: boolean;
    isRoleXuatKho: boolean;
    isRoleTinhGiaXuatKho: boolean;
    isRoleKiemKeKho: boolean;
    isRoleTheKho: boolean;

    ROLE_NhapKho_Xem = ROLE.NhapKho_XEM;
    ROLE_XuatKho_Xem = ROLE.XuatKho_XEM;
    ROLE_TinhGiaXuatKho_Xem = ROLE.TinhGiaXuatKho_Xem;
    ROLE_TheKho_Xem = ROLE.TheKho_Xem;

    // ROLE_KiemKeKho_Xem = ROLE.KiemKeKho_Xem;

    constructor(
        private router: Router,
        private modalService: NgbModal,
        private principal: Principal,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {
        this.principal.identity().then(account => {
            this.arrAuthorities = account.authorities;
            this.isRoleNhapKho = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_NhapKho_Xem) : true;
            this.isRoleXuatKho = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_XuatKho_Xem) : true;
            this.isRoleTinhGiaXuatKho = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_TinhGiaXuatKho_Xem)
                : true;
            // this.isRoleKiemKeKho = !this.arrAuthorities.includes('ROLE_ADMIN')
            //     ? this.arrAuthorities.includes(this.ROLE_KiemKeKho_Xem)
            //     : true;
            this.isRoleTheKho = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_TheKho_Xem) : true;
        });
    }

    ngOnInit() {
        this.idTab = 1;
    }

    navigateNhapKho() {
        if (this.isRoleNhapKho) {
            this.router.navigate(['nhap-kho']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateXuatKho() {
        if (this.isRoleXuatKho) {
            this.router.navigate(['xuat-kho']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    showTinhGiaXuatKho() {
        if (this.isRoleTinhGiaXuatKho) {
            this.modalService.open(CalculateOWRepositoryComponent as Component, {
                size: 'lg',
                backdrop: 'static',
                windowClass: 'width-80 width-50'
            });
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    showTheKho() {
        if (this.isRoleTheKho) {
            this.modalService.open(TheKhoComponent as Component, {
                size: 'lg',
                backdrop: 'static',
                windowClass: 'width-80 width-50'
            });
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }
}
