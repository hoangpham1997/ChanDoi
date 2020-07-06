import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SoQuyTienMatComponent } from 'app/bao-cao/quy/so-quy-tien-mat/so-quy-tien-mat.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SoTienGuiNganHangComponent } from 'app/bao-cao/ngan-hang/so-tien-gui-ngan-hang/so-tien-gui-ngan-hang.component';
import { KhoaSoKyKeToanComponent } from 'app/tonghop/khoa-so-ky-ke-toan/khoa-so-ky-ke-toan.component';
import { Principal } from 'app/core';
import { ROLE } from 'app/role.constants';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'eb-quy-trinh-tien-mat-ngan-hang',
    templateUrl: './tien-mat-ngan-hang.component.html',
    styleUrls: ['./tien-mat-ngan-hang.component.css']
})
export class QuyTrinhTienMatNganHangComponent implements OnInit {
    idTab: number;
    arrAuthorities: any[];
    currentAccount: any;
    isRolePhieuThu: boolean;
    isRolePhieuChi: boolean;
    isRoleBaoCo: boolean;
    isRoleBaoNo: boolean;
    isRoleTheTinDung: boolean;
    isRoleSoQuyTienMat: boolean;
    isRoleSoTienGuiNganHang: boolean;
    ROLE_PhieuThu_Xem = ROLE.PhieuThu_Xem;
    ROLE_PhieuChi_Xem = ROLE.PhieuChi_Xem;
    ROLE_BaoCo_Xem = ROLE.BaoCo_Xem;
    ROLE_BaoNo_Xem = ROLE.BaoNo_Xem;
    ROLE_TheTinDung_Xem = ROLE.TheTinDung_Xem;
    ROLE_SoQuyTienMat_Xem = ROLE.SoQuyTienMat_Xem;
    ROLE_SoTienGuiNganHang_Xem = ROLE.SoTienGuiNganHang_Xem;

    constructor(
        private router: Router,
        private modalService: NgbModal,
        private principal: Principal,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {
        this.principal.identity().then(account => {
            this.arrAuthorities = account.authorities;
            this.isRolePhieuThu = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_PhieuThu_Xem) : true;
            this.isRolePhieuChi = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_PhieuChi_Xem) : true;
            this.isRoleBaoNo = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_BaoNo_Xem) : true;
            this.isRoleBaoCo = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_BaoCo_Xem) : true;
            this.isRoleTheTinDung = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_TheTinDung_Xem)
                : true;
            this.isRoleSoQuyTienMat = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_SoQuyTienMat_Xem)
                : true;
            this.isRoleSoTienGuiNganHang = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_SoTienGuiNganHang_Xem)
                : true;
        });
    }

    ngOnInit() {
        this.idTab = 1;
    }

    navigateMCReceipt() {
        if (this.isRolePhieuThu) {
            this.router.navigate(['mc-receipt']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateMCPayment() {
        if (this.isRolePhieuChi) {
            this.router.navigate(['mc-payment']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateMBDeposit() {
        if (this.isRoleBaoCo) {
            this.router.navigate(['mb-deposit']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    navigateMBTellerPaper() {
        if (this.isRoleBaoNo) {
            this.router.navigate(['mb-teller-paper']);
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    showReportSoQuyTienMat() {
        if (this.isRoleSoQuyTienMat) {
            this.modalService.open(SoQuyTienMatComponent as Component, {
                size: 'lg',
                backdrop: 'static',
                windowClass: 'width-80 width-50'
            });
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }

    showReportSoTienGuiNganHang() {
        if (this.isRoleSoTienGuiNganHang) {
            this.modalService.open(SoTienGuiNganHangComponent as Component, {
                size: 'lg',
                backdrop: 'static',
                windowClass: 'width-80 width-50'
            });
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
        }
    }
}
