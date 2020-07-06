import { Component, OnInit } from '@angular/core';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { RequestReport } from 'app/bao-cao/reqest-report.model';
import { BaoCaoService } from 'app/bao-cao/bao-cao.service';
import { BangCanDoiKeToanComponent } from 'app/bao-cao/bao-cao-tai-chinh/bang-can-doi-ke-toan/bang-can-doi-ke-toan.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NhatKyChungComponent } from 'app/bao-cao/tong-hop/nhat-ky-chung/nhat-ky-chung.component';
import { SoChiTietVatLieuComponent } from 'app/bao-cao/kho/so-chi-tiet-vat-lieu/so-chi-tiet-vat-lieu.component';
import { SoQuyTienMatComponent } from 'app/bao-cao/quy/so-quy-tien-mat/so-quy-tien-mat.component';
import { SoCaiHinhThucNhatKyChungComponent } from 'app/bao-cao/tong-hop/so-cai-hinh-thuc-nhat-ky-chung/so-cai-hinh-thuc-nhat-ky-chung.component';
import { BangTongHopChiTietVatLieuComponent } from 'app/bao-cao/kho/bang-tong-hop-chi-tiet-vat-lieu/bang-tong-hop-chi-tiet-vat-lieu.component';
import { TongHopTonKhoComponent } from 'app/bao-cao/kho/tong-hop-ton-kho/tong-hop-ton-kho.component';
import { TheKhoComponent } from 'app/bao-cao/kho/the-kho/the-kho.component';
import { BangCanDoiTaiKhoanComponent } from 'app/bao-cao/bao-cao-tai-chinh/bang-can-doi-tai-khoan/bang-can-doi-tai-khoan.component';
import { BangKeSoDuNganHangComponent } from 'app/bao-cao/ngan-hang/bang-ke-so-du-ngan-hang/bang-ke-so-du-ngan-hang.component';
import { SoNhatKyThuTienComponent } from 'app/bao-cao/so-nhat-ky-thu-tien/so-nhat-ky-thu-tien.component';
import { BangKeMuaBanComponent } from 'app/bao-cao/bang-ke-mua-ban/bang-ke-mua-ban.component';
import { SoKeToanChiTietQuyTienMatComponent } from 'app/bao-cao/quy/so-chi-tiet-quy-tien-mat/so-ke-toan-chi-tiet-quy-tien-mat.component';
import { TinhHinhSuDungHoaDonComponent } from 'app/bao-cao/hoa-don-dien-tu/tinh-hinh-su-dung-hoa-don/tinh-hinh-su-dung-hoa-don.component';
import { SoTienGuiNganHangComponent } from 'app/bao-cao/ngan-hang/so-tien-gui-ngan-hang/so-tien-gui-ngan-hang.component';
import { PhanBoChiPhiTraTruocComponent } from 'app/bao-cao/tong-hop/phan-bo-chi-phi-tra-truoc/phan-bo-chi-phi-tra-truoc.component';
import { SoChiTietTaiKhoanComponent } from 'app/bao-cao/tong-hop/so-chi-tiet-tai-khoan/so-chi-tiet-tai-khoan.component';
import { BaoCao } from 'app/app.constants';
import { CongNoPhaiTraModalService } from 'app/bao-cao/mua-hang/cong-no-phai-tra/cong-no-phai-tra-modal.service';
import { CongNoPhaiThuModalService } from 'app/bao-cao/ban-hang/cong-no-phai-thu/cong-no-phai-thu-modal.service';
import { BaoCaoKetQuaService } from 'app/bao-cao/bao-cao-tai-chinh/bao-cao-ket-qua/bao-cao-ket-qua.service';
import { SoNhatKyMuaHangComponent } from 'app/bao-cao/mua-hang/so-nhat-ky-mua-hang/so-nhat-ky-mua-hang.component';
import { ROLE } from 'app/role.constants';
import { CongNoPhaiTraComponent } from 'app/bao-cao/mua-hang/cong-no-phai-tra/cong-no-phai-tra.component';
import { SoChiTietMuaHangComponent } from 'app/bao-cao/mua-hang/so-chi-tiet-mua-hang/so-chi-tiet-mua-hang.component';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { SoChiPhiSanXuatKinhDoanhComponent } from 'app/bao-cao/gia-thanh/so-chi-phi-san-xuat-kinh-doanh/so-chi-phi-san-xuat-kinh-doanh.component';
import { TheTinhGiaThanhComponent } from 'app/bao-cao/gia-thanh/the-tinh-gia-thanh/the-tinh-gia-thanh.component';
import { SoTheoDoiDoiTuongTHCPTheoTaiKhoanComponent } from 'app/bao-cao/gia-thanh/so-theo-doi-THCP-theoTK/so-theo-doi-doi-tuong-THCP-theo-tai-khoan.component';
import { SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiComponent } from 'app/bao-cao/gia-thanh/so-theo-doi-THCP-theo-khoan-muc-chi-phi/so-theo-doi-doi-tuong-THCP-theo-khoan-muc-chi-phi.component';
import { SoTheoDoiMaThongKeComponent } from 'app/bao-cao/tong-hop/so-theo-doi-ma-thong-ke/so-theo-doi-ma-thong-ke';
import { TongHopChiPhiTheoKhoanMucChiPhiComponent } from 'app/bao-cao/tong-hop/tong-hop-chi-phi-theo-khoan-muc-chi-phi/tong-hop-chi-phi-theo-khoan-muc-chi-phi.component';
import { SoTheoDoiThanhToanBangNgoaiTeComponent } from 'app/bao-cao/tong-hop/so-theo-doi-thanh-toan-bang-ngoai-te/so-theo-doi-thanh-toan-bang-ngoai-te.component';
import { HttpResponse } from '@angular/common/http';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { SoCongCuDungCuComponent } from 'app/bao-cao/cong-cu-dung-cu/so-cong-cu-dung-cu/so-cong-cu-dung-cu.component';
import { SoTheoDoiCongCuDungCuTaiNoiSuDungComponent } from 'app/bao-cao/cong-cu-dung-cu/so-theo-doi-cong-cu-dung-cu-tai-noi-sd/so-theo-doi-cong-cu-dung-cu-tai-noi-su-dung.component';
import { SoTheoDoiLaiLoTheoHoaDonComponent } from 'app/bao-cao/ban-hang/so-theo-doi-lai-lo-theo-hoa-don/so-theo-doi-lai-lo-theo-hoa-don.component';
import { SoTheoDoiCongNoPhaiThuTheoHoaDonComponent } from 'app/bao-cao/ban-hang/so-theo-doi-cong-no-phai-thu-theo-hoa-don/so-theo-doi-cong-no-phai-thu-theo-hoa-don.component';

@Component({
    selector: 'eb-so-bao-cao',
    templateUrl: './bao-cao.component.html',
    styleUrls: ['bao-cao.component.css']
})
export class BaoCaoComponent extends BaseComponent implements OnInit {
    requestReport: RequestReport;
    BaoCao = BaoCao;
    KET_QUA_HOAT_DONG_KINH_DOANH = BaoCao.BaoCaoTaiChinh.KET_QUA_HOAT_DONG_KINH_DOANH;
    LUU_CHUYEN_TIEN_TE_TT = BaoCao.BaoCaoTaiChinh.LUU_CHUYEN_TIEN_TE_TT;
    LUU_CHUYEN_TIEN_TE_GT = BaoCao.BaoCaoTaiChinh.LUU_CHUYEN_TIEN_TE_GT;
    THUYET_MINH_BAO_CAO_TAI_CHINH = BaoCao.BaoCaoTaiChinh.THUYET_MINH_BAO_CAO_TAI_CHINH;
    Role = ROLE;
    account: any;
    organizationUnit: any;
    treeOrganizationUnits: any[];

    constructor(
        private baoCaoService: BaoCaoService,
        private congNoPhaiTraModalService: CongNoPhaiTraModalService,
        private congNoPhaiThuModalService: CongNoPhaiThuModalService,
        private baoCaoKetQuaService: BaoCaoKetQuaService,
        private modalService: NgbModal,
        private principal: Principal,
        private toastrService: ToastrService,
        public translateService: TranslateService,
        private organizationUnitService: OrganizationUnitService
    ) {
        super();
    }

    ngOnInit(): void {
        this.requestReport = {};
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.organizationUnitService.getChildCompanyByID().subscribe((res: HttpResponse<any>) => {
            this.treeOrganizationUnits = res.body.orgTrees;
            this.organizationUnit = res.body.currentOrgLogin;
            this.baoCaoService.checkHiddenDependent(this.treeOrganizationUnits);
            this.baoCaoService.checkHiddenFirstCompany(this.organizationUnit);
        });
    }

    canDeactive() {
        return true;
    }

    getReportNhatKyThuChi(reportType: string, auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        const modalRef = this.modalService.open(SoNhatKyThuTienComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-ct'
        });
        modalRef.componentInstance.reportType = reportType;
    }

    getReportBangKeMuaBan(reportType: string, auth, isBill = false) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        const modalRef = this.modalService.open(BangKeMuaBanComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-ct'
        });
        modalRef.componentInstance.reportType = reportType;
        modalRef.componentInstance.isBill = isBill;
        modalRef.componentInstance.treeOrganizationUnits = this.treeOrganizationUnits;
        modalRef.componentInstance.organizationUnit = this.organizationUnit;
    }

    getReportNhatKyChung(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        const modalRef = this.modalService.open(NhatKyChungComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
        modalRef.componentInstance.treeOrganizationUnits = this.treeOrganizationUnits;
        modalRef.componentInstance.organizationUnit = this.organizationUnit;
    }

    showReportSoQuyTienMat(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        this.modalService.open(SoQuyTienMatComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
    }

    showReportSoChiTietQuyTienMat(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        this.modalService.open(SoKeToanChiTietQuyTienMatComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
    }

    showReportSoTienGuiNganHang(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        this.modalService.open(SoTienGuiNganHangComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
    }

    showReportBangKeSoDuNganHang(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        this.modalService.open(BangKeSoDuNganHangComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
    }

    getReportSoCaiNhatKyChung(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        const modalRef = this.modalService.open(SoCaiHinhThucNhatKyChungComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
        modalRef.componentInstance.treeOrganizationUnits = this.treeOrganizationUnits;
        modalRef.componentInstance.organizationUnit = this.organizationUnit;
    }

    showBangCanDoiKeToan(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        const modalRef = this.modalService.open(BangCanDoiKeToanComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
        modalRef.componentInstance.treeOrganizationUnits = this.treeOrganizationUnits;
        modalRef.componentInstance.organizationUnit = this.organizationUnit;
    }

    showSoChiTietVatLieu(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        const modalRef = this.modalService.open(SoChiTietVatLieuComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
        modalRef.componentInstance.treeOrganizationUnits = this.treeOrganizationUnits;
        modalRef.componentInstance.organizationUnit = this.organizationUnit;
    }

    showBaoCaoKetQua(typeName, auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        this.baoCaoKetQuaService.open(typeName, this.treeOrganizationUnits, this.organizationUnit);
    }

    showCongNoPhaiTra(status, auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        this.congNoPhaiTraModalService.open(status);
    }

    showCongNoPhaiThu(status, auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        this.congNoPhaiThuModalService.open(status);
    }

    showPhanBoChiPhiTraTruoc(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        this.modalService.open(PhanBoChiPhiTraTruocComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
    }

    showBangTongHopChiTietVatLieu(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        const modalRef = this.modalService.open(BangTongHopChiTietVatLieuComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
        modalRef.componentInstance.treeOrganizationUnits = this.treeOrganizationUnits;
        modalRef.componentInstance.organizationUnit = this.organizationUnit;
    }

    showTongHopTonKho(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        const modalRef = this.modalService.open(TongHopTonKhoComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
        modalRef.componentInstance.treeOrganizationUnits = this.treeOrganizationUnits;
        modalRef.componentInstance.organizationUnit = this.organizationUnit;
    }

    showTheKho(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        const modalRef = this.modalService.open(TheKhoComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
        modalRef.componentInstance.treeOrganizationUnits = this.treeOrganizationUnits;
        modalRef.componentInstance.organizationUnit = this.organizationUnit;
    }

    showTHCPTheoTK(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        const modalRef = this.modalService.open(SoTheoDoiDoiTuongTHCPTheoTaiKhoanComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
        modalRef.componentInstance.treeOrganizationUnits = this.treeOrganizationUnits;
        modalRef.componentInstance.organizationUnit = this.organizationUnit;
    }

    showTHCPTheoCP(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        const modalRef = this.modalService.open(SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
        modalRef.componentInstance.treeOrganizationUnits = this.treeOrganizationUnits;
        modalRef.componentInstance.organizationUnit = this.organizationUnit;
    }

    showBangCanDoiTaiKhoan(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        const modalRef = this.modalService.open(BangCanDoiTaiKhoanComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
        modalRef.componentInstance.treeOrganizationUnits = this.treeOrganizationUnits;
        modalRef.componentInstance.organizationUnit = this.organizationUnit;
    }

    getReportTinhHinhSuDungHDDT(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        this.modalService.open(TinhHinhSuDungHoaDonComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
    }

    getReportSoChiTietTaiKhoan(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        const modalRef = this.modalService.open(SoChiTietTaiKhoanComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
        modalRef.componentInstance.treeOrganizationUnits = this.treeOrganizationUnits;
        modalRef.componentInstance.organizationUnit = this.organizationUnit;
    }

    soNhatKyMuaHang(reportType, auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        const modalRef = this.modalService.open(SoNhatKyMuaHangComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-ct'
        });
        modalRef.componentInstance.reportType = reportType;
    }

    soChiTietMuaHang(reportType, auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        const modalRef = this.modalService.open(SoChiTietMuaHangComponent as Component, {
            size: 'lg',
            windowClass: 'width-80 width-ct',
            backdrop: 'static'
        });
        modalRef.componentInstance.reportType = reportType;
    }

    showTongHopChiPhiTheoKhoanMucChiPhi(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        this.modalService.open(TongHopChiPhiTheoKhoanMucChiPhiComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
    }

    showTheTinhGiaThanh(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        this.modalService.open(TheTinhGiaThanhComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
    }

    showSoTheoDoiMaThongKe(reportType, auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        const modalRef = this.modalService.open(SoTheoDoiMaThongKeComponent as Component, {
            size: 'lg',
            windowClass: 'width-80 width-ct',
            backdrop: 'static'
        });
        modalRef.componentInstance.reportType = reportType;
    }

    showReportSoChiPhiSanXuatKinhDoanh(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        this.modalService.open(SoChiPhiSanXuatKinhDoanhComponent as Component, {
            size: 'lg',
            windowClass: 'width-80 width-ct',
            backdrop: 'static'
        });
    }

    getReportSoCCDC(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        const modalRef = this.modalService.open(SoCongCuDungCuComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
        modalRef.componentInstance.treeOrganizationUnits = this.treeOrganizationUnits;
        modalRef.componentInstance.organizationUnit = this.organizationUnit;
    }

    soTheoDoiCCDCTaiNoiSD(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        const modalRef = this.modalService.open(SoTheoDoiCongCuDungCuTaiNoiSuDungComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
        modalRef.componentInstance.treeOrganizationUnits = this.treeOrganizationUnits;
        modalRef.componentInstance.organizationUnit = this.organizationUnit;
    }

    private hasAuthority(auth): boolean {
        if (this.account.authorities.includes(ROLE.ROLE_ADMIN) || this.account.authorities.includes(auth)) {
            return true;
        }
        this.toastrService.error(this.translateService.instant('ebwebApp.quyTrinh.notHasAccess'));
        return false;
    }

    soTheoDoiLaiLoTheoHoaDon(auth: string) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        const modalRef = this.modalService.open(SoTheoDoiLaiLoTheoHoaDonComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
        modalRef.componentInstance.treeOrganizationUnits = this.treeOrganizationUnits;
        modalRef.componentInstance.organizationUnit = this.organizationUnit;
    }

    soTheoCongNoPhaiThuTheoHoaDon(auth: string) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        const modalRef = this.modalService.open(SoTheoDoiCongNoPhaiThuTheoHoaDonComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
        modalRef.componentInstance.treeOrganizationUnits = this.treeOrganizationUnits;
        modalRef.componentInstance.organizationUnit = this.organizationUnit;
    }

    getReportSoTheoDoiThanhToanBangNgoaiTe(auth) {
        if (!this.hasAuthority(auth)) {
            return;
        }
        const modalRef = this.modalService.open(SoTheoDoiThanhToanBangNgoaiTeComponent as Component, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'width-80 width-50'
        });
        modalRef.componentInstance.treeOrganizationUnits = this.treeOrganizationUnits;
        modalRef.componentInstance.organizationUnit = this.organizationUnit;
    }
}
