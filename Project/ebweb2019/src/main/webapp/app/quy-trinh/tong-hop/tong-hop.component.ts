import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SoQuyTienMatComponent } from 'app/bao-cao/quy/so-quy-tien-mat/so-quy-tien-mat.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SoTienGuiNganHangComponent } from 'app/bao-cao/ngan-hang/so-tien-gui-ngan-hang/so-tien-gui-ngan-hang.component';
import { BaoCaoKetQuaComponent } from 'app/bao-cao/bao-cao-tai-chinh/bao-cao-ket-qua/bao-cao-ket-qua.component';
import { KhoaSoKyKeToanComponent } from 'app/tonghop/khoa-so-ky-ke-toan/khoa-so-ky-ke-toan.component';
import { ROLE } from 'app/role.constants';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'eb-quy-trinh-tong-hop',
    templateUrl: './tong-hop.component.html',
    styleUrls: ['./tong-hop.component.css']
})
export class QuyTrinhTongHopComponent implements OnInit {
    idTab: number;
    arrAuthorities: any[];
    currentAccount: any;
    isRoleCTNVK: boolean;
    isRoleKCLL: boolean;
    isRoleKhoaSoKeToan: boolean;
    isRoleBaoCaoTaiChinh: boolean;

    ROLE_CTNVK_Xem = ROLE.ChungTuNghiepVuKhac_Xem;
    ROLE_KCLL_Xem = ROLE.KetChuyenLaiLo_Xem;
    ROLE_KhoaSoKeToan_Xem = ROLE.TinhGiaXuatKho_Xem;
    ROLE_BaoCaoTaiChinh_Xem = ROLE.BaoCao;

    constructor(
        private router: Router,
        private modalService: NgbModal,
        private principal: Principal,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {
        this.principal.identity().then(account => {
            this.arrAuthorities = account.authorities;
            this.isRoleCTNVK = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_CTNVK_Xem) : true;
            this.isRoleKCLL = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_KCLL_Xem) : true;
            this.isRoleKhoaSoKeToan = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_KhoaSoKeToan_Xem)
                : true;
            // this.isRoleKiemKeKho = !this.arrAuthorities.includes('ROLE_ADMIN')
            //     ? this.arrAuthorities.includes(this.ROLE_KiemKeKho_Xem)
            //     : true;
            this.isRoleBaoCaoTaiChinh = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_BaoCaoTaiChinh_Xem)
                : true;
        });
    }

    ngOnInit() {
        this.idTab = 1;
    }

    navigateGOtherVoucher() {
        if (this.isRoleCTNVK) {
            this.router.navigate(['g-other-voucher']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateKetChuyenLaiLo() {
        if (this.isRoleKCLL) {
            this.router.navigate(['ket-chuyen-lai-lo']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateBaoCao() {
        if (this.isRoleBaoCaoTaiChinh) {
            this.router.navigate(['bao-cao']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    showReportKhoaSoKeToan() {
        if (this.isRoleKhoaSoKeToan) {
            this.modalService.open(KhoaSoKyKeToanComponent as Component, {
                size: 'lg',
                backdrop: 'static',
                windowClass: 'width-80 width-40'
            });
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }
}
