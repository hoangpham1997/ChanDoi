import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiLanguageService } from 'ng-jhipster';

import { NGAY_HACH_TOAN, ROLE_ADMIN, SD_SO_QUAN_TRI, SO_LAM_VIEC, VERSION } from 'app/app.constants';
import { JhiLanguageHelper, LoginModalService, LoginService, Principal, UserService } from 'app/core';
import { TreeviewItem } from 'app/shared/tree-combo-box/tree-item';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { PasswordService } from 'app/account';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import * as moment from 'moment';
import { Moment } from 'moment';
import { SystemOptionService } from 'app/he-thong/system-option';
import { DatePipe } from '@angular/common';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { DATE_FORMAT_TYPE1 } from 'app/shared';
import { ROLE } from 'app/role.constants';
import { BaoCaoService } from 'app/bao-cao/bao-cao.service';
import { TreeAccountListItem } from 'app/shared/tree-grid/tree-item';
import { Subscription } from 'rxjs';

@Component({
    selector: 'eb-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['navbar.css']
})
export class NavbarComponent implements OnInit {
    inProduction: boolean;
    isNavbarCollapsed: boolean;
    languages: any[];
    swaggerEnabled: boolean;
    modalRef: NgbModalRef;
    version: string;
    currentAccount: any;
    data: any;
    org: TreeAccountListItem;
    book: string;
    isShow: boolean;
    ngbModalRef: any;
    ngbModalRef2: any;
    confirmPassword: string;
    currentPassword: string;
    newPassword: string;
    postedDate: Moment;
    isAdmin: boolean;
    isAdminLoginRoute: boolean;
    // Role
    ROLE = ROLE;
    ROLE_HETHONG = ROLE.HeThong;
    ROLE_TIENICH = ROLE.TienIch;
    ROLE_BAOCAO = ROLE.BaoCao;
    ROLE_DANHMUC = ROLE.DanhMuc;
    ROLE_MGT = ROLE.ROLE_MGT;
    ROLE_DanhMucKhachHang = ROLE.DanhMucKhachHang;
    ROLE_DanhMucNhaCungCap = ROLE.DanhMucNhaCungCap;
    ROLE_DanhMucNhanVien = ROLE.DanhMucNhanVien;
    ROLE_DanhMucKhachHangVaNhaCungCap = ROLE.DanhMucKhachHangVaNhaCungCap;
    ROLE_DanhMucKho = ROLE.DanhMucKho;
    ROLE_DanhMucDonViTinh = ROLE.DanhMucDonViTinh;
    ROLE_DanhMucLoaiVatTuHangHoa = ROLE.DanhMucLoaiVatTuHangHoa;
    ROLE_DanhMucTaiKhoanNganHang = ROLE.DanhMucTaiKhoanNganHang;
    ROLE_DanhMucDinhKhoanTuDong_Xem = ROLE.DanhMucDinhKhoanTuDong_Xem;
    ROLE_DanhMucVatTuHangHoa_Xem = ROLE.DanhMucVatTuHangHoa_Xem;
    ROLE_DanhMucNganHang_Xem = ROLE.DanhMucNganHang_Xem;
    ROLE_DanhMucTheTinDung_Xem = ROLE.DanhMucTheTinDung_Xem;
    ROLE_DanhMucLoaiTien_Xem = ROLE.DanhMucLoaiTien_Xem;
    ROLE_DanhMucKhoanMucChiPhi_Xem = ROLE.DanhMucKhoanMucChiPhi_Xem;
    ROLE_DanhMucThuChi_Xem = ROLE.DanhMucThuChi_Xem;
    ROLE_DanhMucPhuongThucVanChuyen_Xem = ROLE.DanhMucPhuongThucVanChuyen_Xem;
    ROLE_DanhMucDieuKhoanThanhToan_Xem = ROLE.DanhMucDieuKhoanThanhToan_Xem;
    ROLE_DanhMucMaThongKe_Xem = ROLE.DanhMucMaThongKe_Xem;
    ROLE_DanhMucHHDVChiuThueTTDB_Xem = ROLE.DanhMucHHDVChiuThueTTDB_Xem;
    ROLE_HeThongTaiKhoan_Xem = ROLE.HeThongTaiKhoan_Xem;
    ROLE_TaiKhoanKetChuyen_Xem = ROLE.TaiKhoanKetChuyen_Xem;
    ROLE_TaiKhoanNgamDinh_Xem = ROLE.TaiKhoanNgamDinh_Xem;
    ROLE_CoCauToChuc_Xem = ROLE.CoCauToChuc_Xem;
    ROLE_NgayHachToan_Xem = ROLE.NgayHachToan_Xem;
    ROLE_NgayHachToan_Sua = ROLE.NgayHachToan_Sua;
    ROLE_TuyChon_Xem = ROLE.TuyChon_Xem;
    ROLE_DanhMucDTTHCP = ROLE.DanhMucDTTHCP;
    DanhMucDTTHCP_Xem = ROLE.DanhMucDTTHCP_Xem;
    ROLE_DanhMucKhachHang_Xem = ROLE.DanhMucKhachHang_Xem;
    ROLE_DanhMucNhaCungCap_Xem = ROLE.DanhMucNhaCungCap_Xem;
    ROLE_DanhMucNhanVien_Xem = ROLE.DanhMucNhanVien_Xem;
    ROLE_DanhMucKhachHangVaNhaCungCap_Xem = ROLE.DanhMucKhachHangVaNhaCungCap_Xem;
    ROLE_DanhMucKho_Xem = ROLE.DanhMucKho_Xem;
    ROLE_DanhMucDonViTinh_Xem = ROLE.DanhMucDonViTinh_Xem;
    ROLE_DanhMucLoaiVatTuHangHoa_Xem = ROLE.DanhMucLoaiVatTuHangHoa_Xem;
    ROLE_DanhMucTaiKhoanNganHang_Xem = ROLE.DanhMucTaiKhoanNganHang_Xem;
    soLamViecDuocPhanQuyen: any;
    isRoleUser: boolean;

    constructor(
        private loginService: LoginService,
        private languageService: JhiLanguageService,
        private languageHelper: JhiLanguageHelper,
        private principal: Principal,
        private loginModalService: LoginModalService,
        public router: Router,
        private eventManager: JhiEventManager,
        private modalService: NgbModal,
        private orgService: OrganizationUnitService,
        private userService: UserService,
        private passwordService: PasswordService,
        private translateService: TranslateService,
        private toastrService: ToastrService,
        private systemOptionService: SystemOptionService,
        private datepipe: DatePipe,
        public utilsService: UtilsService,
        private baoCaoService: BaoCaoService
    ) {
        this.version = VERSION ? 'v' + VERSION : '';
        this.isNavbarCollapsed = true;
        /*this.router.events.subscribe((routerEvent: Event) => {
            if (routerEvent instanceof NavigationStart) {
                this.showLoading = true;
            }

            if (routerEvent instanceof NavigationEnd) {
                this.showLoading = false;
            }
        });*/
        this.isAdminLoginRoute = window.location.href.includes('/admin/login');
    }

    ngOnInit() {
        this.data = [];
        this.languageHelper.getAll().then(languages => {
            this.languages = languages;
        });

        this.principal.identity().then(account => {
            this.currentAccount = account;
            if (this.currentAccount) {
                this.isShow = this.currentAccount.systemOption.some(x => x.code === SD_SO_QUAN_TRI && x.data === '1');
                const data = this.currentAccount.systemOption.find(x => x.code === NGAY_HACH_TOAN && x.data);
                this.postedDate = moment(data.data, DATE_FORMAT_TYPE1);
                this.isAdmin = this.currentAccount.authorities.includes(ROLE_ADMIN);
            }
        });
        this.registerAuthenticationSuccess();
        this.eventManager.subscribe('changeUserInfo', res => {
            this.currentAccount.fullName = res.content;
        });
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
            });
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
        this.removeSession();
        this.router.navigate(['/login']);
    }

    toggleNavbar() {
        this.isNavbarCollapsed = !this.isNavbarCollapsed;
        this.eventManager.broadcast({
            name: 'toggleNavbar',
            content: this.isNavbarCollapsed
        });
    }

    getImageUrl() {
        return this.isAuthenticated() ? this.principal.getImageUrl() : null;
    }

    showPopup(popup) {
        this.principal.identity(true).then(account => {
            this.currentAccount = account;
            this.isRoleUser = !this.currentAccount.authorities.includes(ROLE.ROLE_PERMISSION);
            this.soLamViecDuocPhanQuyen = -1;
            if (this.currentAccount) {
                this.isShow = this.currentAccount.systemOption.some(x => x.code === SD_SO_QUAN_TRI && x.data === '1');
                if (this.isRoleUser) {
                    this.userService.getCurrentBookOfUser().subscribe(res => {
                        if (this.isShow) {
                            this.soLamViecDuocPhanQuyen = res;
                        }
                    });
                }
            }
        });
        this.orgService.getOuTree().subscribe(res => {
            this.data = res.body.orgTrees;
            this.book = res.body.book;
            this.org = res.body.currentOrgLogin;
            if (this.ngbModalRef) {
                this.ngbModalRef.close();
            }
            this.ngbModalRef = this.modalService.open(popup, { size: 'lg', backdrop: 'static' });
        });
    }

    showChangePostedDate(popupChangePostedDate) {
        this.principal.identity(true).then(account => {
            this.currentAccount = account;
            if (this.currentAccount) {
                const data = this.currentAccount.systemOption.find(x => x.code === NGAY_HACH_TOAN && x.data);
                this.postedDate = this.utilsService.ngayHachToan(this.currentAccount);
                // this.postedDate = moment(ngayHachToan);
                // this.testPostedDate = '11/11/2019'
                this.ngbModalRef = this.modalService.open(popupChangePostedDate, { size: 'lg', backdrop: 'static' });
            }
        });
    }

    showTutorialUse(popupTutorialUse) {
        this.ngbModalRef = this.modalService.open(popupTutorialUse, { size: 'lg', backdrop: 'static' });
    }

    showPopupChangePassword(popup) {
        this.confirmPassword = this.currentPassword = this.newPassword = '';
        this.ngbModalRef2 = this.modalService.open(popup, { size: 'lg', backdrop: 'static' });
    }

    changeOrg(org) {
        if (org) {
            this.systemOptionService.getSystemOptionsByCompanyID({ companyID: org.parent.id }).subscribe(res => {
                this.isShow = res.body.some(x => x.code === SD_SO_QUAN_TRI && x.data === '1');
                this.book = res.body.find(x => x.code === SO_LAM_VIEC).data;
            });
        }
    }

    apply() {
        if (this.org) {
            // update dữ liệu
            this.baoCaoService.changeCompany();
            this.userService.updateSession({ currentBook: this.book, org: this.org.parent.id }).subscribe(res => {
                const bearerToken = res.headers.get('Authorization');
                if (bearerToken && bearerToken.slice(0, 7) === 'Bearer ') {
                    const jwt = bearerToken.slice(7, bearerToken.length);
                    this.loginService.storeAuthenticationToken(jwt, false);
                    this.loginService.storeAuthenticationToken(jwt, true);
                }

                this.principal.identity(true).then(account => {
                    this.currentAccount = account;
                    if (this.currentAccount) {
                        this.isShow = this.currentAccount.systemOption.some(x => x.code === SD_SO_QUAN_TRI && x.data === '1');
                    }
                    this.eventManager.broadcast({
                        name: 'changeSession',
                        content: 'Change Authentication Session Success'
                    });
                });
                this.ngbModalRef.close();
            });
        } else {
            this.toastrService.error(this.translateService.instant('navbar.changePostedDate.error.org'));
        }
    }

    changePassword() {
        if (!this.newPassword) {
            this.toastrService.error(this.translateService.instant('navbar.password.error.newPassword'));
            return;
        }
        if (!this.confirmPassword) {
            this.toastrService.error(this.translateService.instant('navbar.password.error.newPassword'));
            return;
        }
        if (!this.currentPassword) {
            this.toastrService.error(this.translateService.instant('navbar.password.error.newPassword'));
            return;
        }
        if (this.newPassword.length < 4) {
            this.toastrService.error(
                this.translateService.instant('userManagement.error.passwordLength'),
                this.translateService.instant('ebwebApp.ebPackage.error.error')
            );
            return;
        }
        if (this.newPassword !== this.confirmPassword) {
            this.toastrService.error(this.translateService.instant('navbar.password.error.doNotMatch'));
        } else {
            this.passwordService.save(this.newPassword, this.currentPassword).subscribe(
                () => {
                    this.toastrService.success(this.translateService.instant('navbar.password.success'));
                    this.ngbModalRef2.close();
                },
                error => {
                    if (error.error.type.includes('invalid-password')) {
                        this.toastrService.error(this.translateService.instant('navbar.password.error.invalidPassword'));
                    }
                }
            );
        }
    }

    updatePostedDate() {
        let data = '';
        const defaultData = this.datepipe.transform(new Date(), 'yyyy-MM-dd');
        if (!this.postedDate) {
            this.toastrService.error(
                this.translateService.instant('navbar.changePostedDate.postedDateIsNotNull'),
                this.translateService.instant('navbar.changePostedDate.message')
            );
            return;
        } else {
            data = this.datepipe.transform(this.postedDate, 'yyyy-MM-dd');
        }
        const obj = { data, defaultData };
        this.systemOptionService.updatePostedDate(JSON.stringify(obj)).subscribe(
            () => {
                this.toastrService.success(this.translateService.instant('navbar.changePostedDate.success'));
                this.principal.identity(true).then(account => {});
                this.ngbModalRef.close();
            },
            () => {}
        );
    }

    navigateUser() {
        this.router.navigate(['user-management', 'edit', { login: this.currentAccount.login }]);
    }

    getInstructionForUse() {
        this.baoCaoService.getInstruction();
    }

    toolSign() {
        window.open('https://easyinvoice.vn/tai-nguyen/', '_blank');
    }

    removeSession() {
        sessionStorage.removeItem('searchBangCanDoiKeToan');
        sessionStorage.removeItem('searchBangCanDoiTaiKhoan');
        sessionStorage.removeItem('searchHoatDongKinhDoanh');
        sessionStorage.removeItem('searchTienTeTT');
        sessionStorage.removeItem('searchTienTeGT');
        sessionStorage.removeItem('searchBaoCaoTaiChinh');
        sessionStorage.removeItem('searchSoChiTietVatLieu');
        sessionStorage.removeItem('searchBangTongHopChiTietVatLieu');
        sessionStorage.removeItem('searchTheKho');
        sessionStorage.removeItem('searchTongHopTonKho');
        sessionStorage.removeItem('searchSoQuyTienMat');
        sessionStorage.removeItem('searchSoKeToanChiTietQuyTienMat');
        sessionStorage.removeItem('searchBangKeSoDuNganHang');
        sessionStorage.removeItem('searchSoTienGuiNganHang');
        sessionStorage.removeItem('searchSoChiPhiSXKD');
    }
}
