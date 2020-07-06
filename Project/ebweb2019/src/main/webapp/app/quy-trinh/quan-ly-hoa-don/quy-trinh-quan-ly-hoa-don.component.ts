import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SoQuyTienMatComponent } from 'app/bao-cao/quy/so-quy-tien-mat/so-quy-tien-mat.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SoTienGuiNganHangComponent } from 'app/bao-cao/ngan-hang/so-tien-gui-ngan-hang/so-tien-gui-ngan-hang.component';
import { BaoCaoKetQuaComponent } from 'app/bao-cao/bao-cao-tai-chinh/bao-cao-ket-qua/bao-cao-ket-qua.component';
import { KhoaSoKyKeToanComponent } from 'app/tonghop/khoa-so-ky-ke-toan/khoa-so-ky-ke-toan.component';
import { ConnnectEInvoiceComponent } from 'app/hoa-don-dien-tu/ket_noi_hoa_don_dien_tu/ket_noi_hoa_don_dien_tu';
import { ROLE } from 'app/role.constants';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'eb-quy-trinh-quan-ly-hoa-don',
    templateUrl: './quy-trinh-quan-ly-hoa-don.component.html',
    styleUrls: ['./quy-trinh-quan-ly-hoa-don.component.css']
})
export class QuyTrinhQuanLyHoaDonComponent implements OnInit {
    idTab: number;
    arrAuthorities: any[];
    currentAccount: any;
    isRoleKhoiTaoMauHD: boolean;
    isRoleDangKySuDungHD: boolean;
    isRoleThongBaoPhatHanhHD: boolean;

    ROLE_KhoiTaoMauHD_Xem = ROLE.KTHD_XEM;
    ROLE_DangKySuDungHD_Xem = ROLE.DKSD_XEM;
    ROLE_ThongBaoPhatHanhHD_Xem = ROLE.TBPH_XEM;

    constructor(
        private router: Router,
        private modalService: NgbModal,
        private principal: Principal,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {
        this.principal.identity().then(account => {
            this.arrAuthorities = account.authorities;
            this.isRoleKhoiTaoMauHD = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_KhoiTaoMauHD_Xem)
                : true;
            this.isRoleDangKySuDungHD = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_DangKySuDungHD_Xem)
                : true;
            this.isRoleThongBaoPhatHanhHD = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_ThongBaoPhatHanhHD_Xem)
                : true;
        });
    }

    ngOnInit() {
        this.idTab = 1;
    }

    navigateKhoiTaoMauHoaDon() {
        if (this.isRoleKhoiTaoMauHD) {
            this.router.navigate(['khoi-tao-mau-hoa-don']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateDangKySuDungHoaDon() {
        if (this.isRoleDangKySuDungHD) {
            this.router.navigate(['dang-ky-su-dung-hoa-don']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateThongBaoPhatHanhHoaDon() {
        if (this.isRoleThongBaoPhatHanhHD) {
            this.router.navigate(['thong-bao-phat-hanh-hoa-don']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }
}
