import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute, navbarRoute, sidebarRoute } from './layouts';
import { loginRoute } from 'app/layouts/login/login.route';
import { loginAdminRoute } from 'app/admin/login/login.route';

const LAYOUT_ROUTES = [navbarRoute, sidebarRoute, ...errorRoute, ...loginRoute, ...loginAdminRoute];

@NgModule({
    imports: [
        RouterModule.forRoot(
            [
                ...LAYOUT_ROUTES,
                {
                    path: 'admin',
                    loadChildren: './admin/admin.module#EbwebAdminModule'
                },

                /**
                 * Route màn KHO
                 */
                {
                    path: 'nhap-kho',
                    loadChildren: './kho/nhap-kho/nhap-kho.module#EbwebNhapKhoModule'
                },
                {
                    path: 'xuat-kho',
                    loadChildren: './kho/xuat-kho/xuat-kho.module#EbwebXuatKhoModule'
                },
                {
                    path: 'chuyen-kho',
                    loadChildren: './kho/chuyen-kho/chuyen-kho.module#EbwebChuyenKhoModule'
                },

                /**
                 * Route Mua hàng
                 */
                {
                    path: 'don-mua-hang',
                    loadChildren: './muahang/don-mua-hang/don-mua-hang.module#EbwebDonMuaHangModule'
                },
                {
                    path: 'mua-dich-vu',
                    loadChildren: './muahang/mua-dich-vu/mua-dich-vu.module#EbwebMuaDichVuModule'
                },
                {
                    path: 'hang-mua',
                    loadChildren:
                        './muahang/hang_mua_tra_lai_giam_gia/pp-discount-return/pp-discount-return.module#EbwebPPDiscountReturnModule'
                },
                {
                    path: 'mua-hang',
                    loadChildren: './muahang/mua_hang_qua_kho/pp-invoice.module#EbwebPPInvoiceModule'
                },
                {
                    path: 'nhan-hoa-don',
                    loadChildren: './muahang/nhan_hoa_don/nhan-hoa-don.module#EbwebNhanHoaDonModule'
                },
                {
                    path: 'pp-pay-vendor',
                    loadChildren: './muahang/tra_tien_nha_cung_cap/pp-pay-vendor.module#EbwebPPPayVendorModule'
                },

                /**
                 * Route Bán hàng
                 */
                {
                    path: 'chung-tu-ban-hang',
                    loadChildren: './ban-hang/ban_hang_chua_thu_tien/sa-invoice.module#EbwebSAInvoiceModule'
                },
                {
                    path: 'sa-quote',
                    loadChildren: './ban-hang/bao_gia/sa-quote/sa-quote.module#EbwebSAQuoteModule'
                },
                {
                    path: 'sa-quote-details',
                    loadChildren: './ban-hang/bao_gia/sa-quote-details/sa-quote-details.module#EbwebSAQuoteDetailsModule'
                },
                {
                    path: 'sa-order',
                    loadChildren: './ban-hang/don_dat_hang/sa-order/sa-order.module#EbwebSAOrderModule'
                },
                {
                    path: 'hang-ban',
                    loadChildren: './ban-hang/hang-ban-tra-lai-giam-gia/hang-ban-tra-lai.module#EbwebHangBanTraLaiModule'
                },
                {
                    path: 'xuat-hoa-don',
                    loadChildren: './ban-hang/xuat-hoa-don/xuat-hoa-don.module#EbwebXuatHoaDonModule'
                },
                {
                    path: 'sa-receipt-debit',
                    loadChildren: './ban-hang/thu_tien_khach_hang/sa-receipt-debit.module#EbwebSAReceiptDebitModule'
                },

                /**
                 * Route Tiền Mặt Ngân Hàng
                 */
                {
                    path: 'mb-deposit',
                    loadChildren: './TienMatNganHang/BaoCo/mb-deposit/mb-deposit.module#EbwebMBDepositModule'
                },
                {
                    path: 'mb-teller-paper',
                    loadChildren: './TienMatNganHang/BaoNo/mb-teller-paper/mb-teller-paper.module#EbwebMBTellerPaperModule'
                },
                {
                    path: 'mb-teller-paper-detail-tax',
                    loadChildren:
                        './TienMatNganHang/BaoNo/mb-teller-paper-detail-tax/mb-teller-paper-detail-tax.module#EbwebMBTellerPaperDetailTaxModule'
                },
                {
                    path: 'mb-teller-paper-details',
                    loadChildren:
                        './TienMatNganHang/BaoNo/mb-teller-paper-details/mb-teller-paper-details.module#EbwebMBTellerPaperDetailsModule'
                },
                {
                    path: 'mc-audit',
                    loadChildren: './TienMatNganHang/kiem_ke_quy/mc-audit.module#EbwebMCAuditModule'
                },
                {
                    path: 'mb-internal-transfer',
                    loadChildren: './TienMatNganHang/mb-internal-transfer/mb-internal-transfer.module#EbwebMBInternalTransferModule'
                },
                {
                    path: 'mb-internal-transfer-details',
                    loadChildren:
                        './TienMatNganHang/mb-internal-transfer-details/mb-internal-transfer-details.module#EbwebMBInternalTransferDetailsModule'
                },
                {
                    path: 'mb-internal-transfer-tax',
                    loadChildren:
                        './TienMatNganHang/mb-internal-transfer-tax/mb-internal-transfer-tax.module#EbwebMBInternalTransferTaxModule'
                },
                {
                    path: 'mc-payment',
                    loadChildren: './TienMatNganHang/phieu-chi/mc-payment/mc-payment.module#EbwebMCPaymentModule'
                },
                {
                    path: 'mc-receipt',
                    loadChildren: './TienMatNganHang/phieu-thu/mc-receipt/mc-receipt.module#EbwebMCReceiptModule'
                },
                {
                    path: 'mb-credit-card',
                    loadChildren: './TienMatNganHang/TheTinDung/mb-credit-card/mb-credit-card.module#EbwebMBCreditCardModule'
                },

                /**
                 * Route Tổng hợp
                 */
                {
                    path: 'g-other-voucher',
                    loadChildren: './tonghop/chung_tu_nghiep_vu_khac/g-other-voucher.module#EbwebGOtherVoucherModule'
                },
                {
                    path: 'ket-chuyen-lai-lo',
                    loadChildren: './tonghop/ket_chuyen_lai_lo/ket-chuyen-lai-lo.module#EbwebKetChuyenLaiLoModule'
                },
                {
                    path: 'chi-phi-tra-truoc',
                    loadChildren: './tonghop/chi_phi_tra_truoc/chi-phi-tra-truoc.module#EbwebChiPhiTraTruocModule'
                },
                {
                    path: 'phan-bo-chi-phi-tra-truoc',
                    loadChildren: './tonghop/phan_bo_chi_phi_tra_truoc/phan-bo-chi-phi-tra-truoc.module#EbwebPhanBoChiPhiTraTruocModule'
                },

                /**
                 * Route Giá thành
                 */
                {
                    path: 'dinh-muc-gia-thanh-thanh-pham',
                    loadChildren:
                        './giathanh/dinh_muc_gia_thanh_thanh_pham/dinh-muc-gia-thanh-thanh-pham.module#EbwebDinhMucGiaThanhThanhPhamModule'
                },
                {
                    path: 'cp-allocation-quantum',
                    loadChildren: './giathanh/dinh_muc_phan_bo_chi_phi/cp-allocation-quantum.module#EbwebCPAllocationQuantumModule'
                },
                {
                    path: 'cpopn',
                    loadChildren: './giathanh/chi_phi_do_dang_dau_ky/cpopn.module#EbwebCPOPNModule'
                },
                {
                    path: 'gia-thanh',
                    loadChildren: './giathanh/phuong_phap_gian_don/pp-gian-don.module#EbwebCPPeriodModule'
                },
                {
                    path: 'gia-thanh',
                    loadChildren: './giathanh/phuong_phap_he_so/pp-he-so.module#EbwebPhuongPhapHeSoModule'
                },
                {
                    path: 'gia-thanh',
                    loadChildren:
                        './giathanh/phuong_phap_cong_trinh_vu_viec/pp-cong-trinh-vu-viec.module#EbwebGiaThanhTheoCongTrinhVuViecModule'
                },
                {
                    path: 'material-quantum',
                    loadChildren: './danhmuc/dinh-muc-nguyen-vat-lieu/material-quantum/material-quantum.module#EbwebMaterialQuantumModule'
                },
                {
                    path: 'ket-chuyen-chi-phi',
                    loadChildren: './giathanh/ket_chuyen_chi_phi/cp-expense-tranfer.module#EbwebCPExpenseTranferModule'
                },
                /**
                 * Route Danh mục
                 */
                {
                    path: 'account-list',
                    loadChildren: './danhmuc/account-list/account-list.module#EbwebAccountListModule'
                },
                /**
                 * khoan  muc chi phi
                 */
                {
                    path: 'khoan-muc-chi-phi-list',
                    loadChildren: './danhmuc/khoan-muc-chi-phi-list/khoan-muc-chi-phi-list.module#KhoanMucChiPhiListModule'
                },
                {
                    path: 'transport-method',
                    loadChildren: './danhmuc/transport-method/transport-method.module#TransportMethodModule'
                },
                {
                    path: 'budget-item',
                    loadChildren: './danhmuc/budget-item/budget-item.module#BudgetItemModule'
                },
                {
                    path: 'unit',
                    loadChildren: './danhmuc/unit/unit.module#EbwebUnitModule'
                },
                {
                    path: 'bank-account-details',
                    loadChildren: './danhmuc/bank-account-details/bank-account-details.module#EbwebBankAccountDetailsModule'
                },
                {
                    path: 'repository',
                    loadChildren: './danhmuc/repository/repository.module#EbwebRepositoryModule'
                },
                {
                    path: 'material-goods-category',
                    loadChildren: './danhmuc/material-goods-category/material-goods-category.module#EbwebMaterialGoodsCategoryModule'
                },
                {
                    path: 'auto-principle',
                    loadChildren: './danhmuc/auto-principle/auto-principle.module#EbwebAutoPrincipleModule'
                },
                {
                    path: 'bank',
                    loadChildren: './danhmuc/bank/bank.module#EbwebBankModule'
                },
                {
                    path: 'credit-card',
                    loadChildren: './danhmuc/credit-card/credit-card.module#EbwebCreditCardModule'
                },
                {
                    path: 'currency',
                    loadChildren: './danhmuc/currency/currency.module#EbwebCurrencyModule'
                },
                {
                    path: 'material-quantum',
                    loadChildren: './danhmuc/dinh-muc-nguyen-vat-lieu/material-quantum/material-quantum.module#EbwebMaterialQuantumModule'
                },
                {
                    path: 'accounting-object',
                    loadChildren: './danhmuc/accounting-object/accounting-object.module#EbwebAccountingObjectModule'
                },
                {
                    path: 'accounting-object-group',
                    loadChildren: './danhmuc/accounting-object-group/accounting-object-group.module#EbwebAccountingObjectGroupModule'
                },
                {
                    path: 'employee',
                    loadChildren: './danhmuc/employee/employee.module#EbwebEmployeeModule'
                },
                {
                    path: 'supplier',
                    loadChildren: './danhmuc/supplier/supplier.module#EbwebSupplierModule'
                },
                {
                    path: 'account-default',
                    loadChildren: './danhmuc/account-default/account-default.module#EbwebAccountDefaultModule'
                },
                {
                    path: 'account-transfer',
                    loadChildren: './danhmuc/account-transfer/account-transfer.module#EbwebAccountTransferModule'
                },
                {
                    path: 'organization-unit',
                    loadChildren: './danhmuc/organization-unit/organization-unit.module#EbwebOrganizationUnitModule'
                },
                {
                    path: 'material-goods',
                    loadChildren: './danhmuc/material-goods/material-goods.module#EbwebMaterialGoodsModule'
                },
                {
                    path: 'payment-clause',
                    loadChildren: './danhmuc/payment-clause/payment-clause.module#EbwebPaymentClauseModule'
                },
                {
                    path: 'cost-set',
                    loadChildren: './danhmuc/doi-tuong-tap-hop-chi-phi/cost-set.module#EbwebCostSetModule'
                },

                {
                    path: 'material-goods-special-tax-group',
                    loadChildren:
                        './danhmuc/material-goods-special-tax-group/material-goods-special-tax-group.module#EbwebMaterialGoodsSpecialTaxGroupModule'
                },

                {
                    path: 'statistics-code',
                    loadChildren: './danhmuc/statistics-code/statistics-code.module#EbwebStatisticsCodeModule'
                },
                /*
                * Hóa đơn điện tử
                * */
                {
                    path: 'danh-sach-hoa-don',
                    loadChildren: './hoa-don-dien-tu/danh_sach_hoa_don/danh-sach-hoa-don.module#EbwebDanhSachHoaDonModule'
                },

                {
                    path: 'hoa-don-cho-ky',
                    loadChildren: './hoa-don-dien-tu/danh_sach_hoa_don_cho_ky/hoa-don-cho-ky.module#EbwebHoaDonChoKyModule'
                },
                {
                    path: 'hoa-don-huy',
                    loadChildren: './hoa-don-dien-tu/hoa_don_huy/hoa-don-huy.module#EbwebHoaDonHuyModule'
                },
                {
                    path: 'chuyen-doi-hoa-don',
                    loadChildren: './hoa-don-dien-tu/chuyen_doi_hoa_don/chuyen-doi-hoa-don.module#EbwebChuyenDoiHoaDonModule'
                },
                {
                    path: 'hoa-don-thay-the',
                    loadChildren: './hoa-don-dien-tu/hoa_don_thay_the/hoa-don-thay-the.module#EbwebHoaDonThayTheModule'
                },
                {
                    path: 'hoa-don-dieu-chinh',
                    loadChildren: './hoa-don-dien-tu/hoa_don_dieu_chinh/hoa-don-dieu-chinh.module#EbwebHoaDonDieuChinhModule'
                },
                /**
                 * Route Tiện ích
                 */
                {
                    path: 'so-du-dau-ky',
                    loadChildren: './tien-ich/so-du-dau-ky/so-du-dau-ky.module#EbwebSoDuDauKyModule'
                },
                {
                    path: 'danh-lai-so-chung-tu',
                    loadChildren: './tien-ich/danh-lai-so-chung-tu/danh-lai-so-chung-tu.module#EbwebDanhLaiSoChungTuModule'
                },
                {
                    path: 'tim-kiem-chung-tu',
                    loadChildren: './tien-ich/tim-kiem-chung-tu/tim-kiem-chung-tu.module#EbwebTimKiemChungTuModule'
                },
                /**
                 * Route Báo cáo
                 */
                {
                    path: 'bao-cao',
                    loadChildren: './bao-cao/bao-cao.module#EbwebBaoCaoModule'
                },
                /**
                 * Route Phan Quyen
                 */
                {
                    path: 'eb-group',
                    loadChildren: './phan-quyen/eb-group/eb-group.module#EbwebEbGroupModule'
                },
                {
                    path: 'permission-user',
                    loadChildren: './phan-quyen/permission-user/permission-user.module#EbwebPermissionUserModule'
                },
                {
                    path: 'user-management',
                    loadChildren: './phan-quyen/user-management/user-management.module#EbwebUserManagementModule'
                },
                {
                    path: 'role-permission',
                    loadChildren: './phan-quyen/role-permission/role-permission.module#EbwebRolePermissionModule'
                },
                /**
                 * Route Quản lý hóa đơn
                 */
                {
                    path: 'dang-ky-su-dung-hoa-don',
                    loadChildren: './quan-ly-hoa-don/dang-ky-su-dung/dang-ky-su-dung.module#EbwebDangKySuDungModule'
                },
                {
                    path: 'khoi-tao-mau-hoa-don',
                    loadChildren: './quan-ly-hoa-don/khoi-tao-mau-hoa-don/khoi-tao-hoa-don.module#EbwebKhoiTaoHoaDonModule'
                },
                {
                    path: 'thong-bao-phat-hanh-hoa-don',
                    loadChildren: './quan-ly-hoa-don/thong-bao-phat-hanh-hoa-don/thong-bao-phat-hanh.module#EbwebThongBaoPhatHanhModule'
                },
                /**
                 * Route Hệ thống
                 */
                {
                    path: 'system-option',
                    loadChildren: './he-thong/system-option/system-option.module#EbwebSystemOptionModule'
                },
                /**
                 * Route Quy trình
                 */
                {
                    path: 'quy-trinh-tien-mat-ngan-hang',
                    loadChildren: './quy-trinh/tien-mat-ngan-hang/tien-mat-ngan-hang.module#EbwebQuyTrinhTienMatNganHangModule'
                },
                {
                    path: 'quy-trinh-tong-hop',
                    loadChildren: './quy-trinh/tong-hop/tong-hop.module#EbwebQuyTrinhTongHopModule'
                },
                {
                    path: 'quy-trinh-kho',
                    loadChildren: './quy-trinh/kho/kho.module#EbwebQuyTrinhKhoModule'
                },
                {
                    path: 'quy-trinh-mua-hang',
                    loadChildren: './quy-trinh/mua-hang/quy-trinh-mua-hang.module#EbwebQuyTrinhMuaHangModule'
                },
                {
                    path: 'quy-trinh-hoa-don-dien-tu',
                    loadChildren: './quy-trinh/hoa-don-dien-tu/quy-trinh-hoa-don-dien-tu.module#EbwebQuyTrinhHoaDonDienTuModule'
                },
                {
                    path: 'quy-trinh-quan-ly-hoa-don',
                    loadChildren: './quy-trinh/quan-ly-hoa-don/quy-trinh-quan-ly-hoa-don.module#EbwebQuyTrinhQuanLyHoaDonModule'
                },
                {
                    path: 'quy-trinh-ban-hang',
                    loadChildren: './quy-trinh/ban-hang/quy-trinh-ban-hang.module#EbwebQuyTrinhBanHangModule'
                },
                {
                    path: 'quy-trinh-gia-thanh',
                    loadChildren: './quy-trinh/gia-thanh/quy-trinh-gia-thanh.module#EbwebQuyTrinhGiaThanhModule'
                },
                /**
                 * Route quản lý dữ liệu
                 */
                {
                    path: 'data-backup',
                    loadChildren: './data/data-backup.module#EbwebDataBackupModule'
                },
                /**
                 * ccdc
                 */
                {
                    path: 'phan-bo-ccdc',
                    loadChildren: './congcudungcu/phan-bo-ccdc/phan-bo-ccdc.module#EbwebPhanBoCCDCModule'
                },
                {
                    path: 'dieu-chinh-ccdc',
                    loadChildren: './congcudungcu/dieu-chinh-ccdc/dieu-chinh-ccdc.module#EbwebPhanBoCCDCModule'
                },
                {
                    path: 'ghi-tang-ccdc',
                    loadChildren: './congcudungcu/ghi-tang-ccdc-khac/ghi-tang.module#EbwebGhiTangModule'
                },
                {
                    path: 'dieu-chuyen-ccdc',
                    loadChildren: './congcudungcu/dieu-chuyen-ccdc/dieu-chuyen-ccdc.module#EbwebDieuChuyenCCDCModule'
                },
                {
                    path: 'ghi-giam-ccdc',
                    loadChildren: './congcudungcu/ghi-giam-ccdc/ghi-giam-ccdc.module#EbwebGhiGiamCCDCModule'
                },
                {
                    path: 'kiem-ke-ccdc',
                    loadChildren: './congcudungcu/kiem-ke-ccdc/kiem-ke-ccdc.module#EbwebKiemKeCCDCModule'
                },
                /**
                 * ttcd
                 */
                {
                    path: 'ghi-tang-tscd',
                    loadChildren: './tai-san-co-dinh/ghi-tang/ghi-tang.module#EbwebGhiTangModule'
                },
                {
                    path: 'tinh-khau-hao-tscd',
                    loadChildren: './tai-san-co-dinh/tinh-khau-hao/tinh-khau-hao.module#TinhKhauHaoModule'
                }
            ],
            { useHash: true, enableTracing: false }
        )
    ],
    exports: [RouterModule]
})
export class EbwebAppRoutingModule {}
