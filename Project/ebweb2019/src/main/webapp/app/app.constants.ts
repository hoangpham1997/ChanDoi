// These constants are injected via webpack environment variables.
// You can add more variables in webpack.common.js or in profile specific webpack.<dev|prod>.js files.
// If you change the values in the webpack config files, you need to re run webpack to update the application

export const VERSION = process.env.VERSION;
export const DEBUG_INFO_ENABLED: boolean = !!process.env.DEBUG_INFO_ENABLED;
export const SERVER_API_URL = process.env.SERVER_API_URL;
export const BUILD_TIMESTAMP = process.env.BUILD_TIMESTAMP;

// role, authorities
export const ROLE_ADMIN = 'ROLE_ADMIN';
export const ROLE_USER = 'ROLE_USER';

// System option
export const MAU_BO_GHI_SO = 'TCKHAC_MauCTuChuaGS';
export const SO_LAM_VIEC = 'PHIEN_SoLamViec';
export const TCKHAC_MauCTuChuaGS = 'TCKHAC_MauCTuChuaGS';
export const VTHH_NHAP_DON_GIA_VON = 'VTHH_NhapDonGiaVon';
export const SU_DUNG_DM_KHO = 'TCKHAC_SDDMKho';
export const SU_DUNG_DM_TK_NGan_Hang = 'TCKHAC_SDDMTKNH';
export const SU_DUNG_DM_DOI_TUONG = 'TCKHAC_SDDMDoiTuong';
export const SU_DUNG_DM_VTHH = 'TCKHAC_SDDMVTHH';
export const SU_DUNG_DM_THE_TIN_DUNG = 'TCKHAC_SDDMTheTD';
export const DBDateClosed = 'DBDateClosed';
export const HH_XUATQUASLTON = 'VTHH_XuatQuaSLTon';
export const DBDateClosedOld = 'DBDateClosedOld';
export const CANH_BAO_NHAN_VIEN = 'TCKHAC_CBKoChonNVBH';
export const HMTL_KIEM_PXUAT = 'VTHH_HMTLKiemPXuat';
export const HMTL_KIEM_HOA_DON = 'VTHH_HMTLKiemHD';
export const TangSoChungTu = 'TangSoChungTu';
/*Hóa đơn điện tử*/
export const EI_Path = 'EI_Path';
export const EI_IDNhaCungCapDichVu = 'EI_IDNhaCungCapDichVu';
export const EI_TenDangNhap = 'EI_TenDangNhap';
export const EI_MatKhau = 'EI_MatKhau';
export const TCKHAC_SDTichHopHDDT = 'TCKHAC_SDTichHopHDDT';
export const CheckHDCD = 'CheckHDCD';
export const SignType = 'SignType';
/*HDDT*/
export const SD_SO_QUAN_TRI = 'TCKHAC_SDSoQuanTri';
export const NHAP_DON_GIA = 'VTHH_NhapDonGiaVon';
export const LAP_KEM_HOA_DON = 'VTHH_HBGGKiemHD';
export const KIEM_PHIEU_NHAP_KHO = 'VTHH_HBTLKiemPNhap';
export const NGAY_HACH_TOAN = 'NgayHachToan';
export const TCKHAC_SDSOQUANTRI = 'TCKHAC_SDSoQuanTri';
export const TCKHAC_HANCHETK = 'TCKHAC_HanCheTK';
export const BH_KIEM_HD = 'VTHH_BHKiemHD';
export const BH_KIEM_PX = 'VTHH_BHKiemPXuat';
export const NHAP_DON_GIA_VON = 'VTHH_NhapDonGiaVon';
export const PP_TINH_GIA_XUAT_KHO = 'VTHH_PPTinhGiaXKho';
export const CHI_QUA_TON_QUY = 'TCKHAC_ChiQuaTonQuy';
/**
 * Currency directive setting
 */
export const DECIMAL = ',';
export const THOUSANDS = '.';
export const SUFFIX = '';

export const HINH_THUC_HOA_DON_HOA_DON_DIEN_TU = 0;
export const DANH_SACH_HOA_DON = 0;
export const DANH_SACH_HOA_DON_CHO_KY = 1;
export const DANH_SACH_HOA_DON_THAY_THE = 2;
export const DANH_SACH_HOA_DON_DIEU_CHINH = 3;
export const DANH_SACH_HOA_DON_HUY = 4;
export const DANH_SACH_HOA_CHUYEN_DOI = 5;

export const TCKHAC_SDSoQuanTri = 'TCKHAC_SDSoQuanTri'; // check có hiển thị trường vào sổ hay ko
export const TCKHAC_GhiSo = 'TCKHAC_GhiSo'; // check khi lưu có đồng thời ghi sổ hay ko
export const SU_DUNG_SO_QUAN_TRI = 'TCKHAC_SDSoQuanTri';

/**
 * Constant phần dấu thập phân
 */
export const DDSo_DonGia = 'DDSo_DonGia'; // Đơn giá - 1
export const DDSo_DonGiaNT = 'DDSo_DonGiaNT'; // Đơn giá ngoại tệ - 2
export const DDSo_SoLuong = 'DDSo_SoLuong'; // Số lượng - 3
export const DDSo_TyGia = 'DDSo_TyGia'; // Tỷ giá - 4
export const DDSo_TyLe = 'DDSo_TyLe'; // Hệ số, tỷ lệ - 5
export const DDSo_TyLePBo = 'DDSo_TyLePBo'; // Tỷ lệ phân bổ - 6
export const DDSo_TienVND = 'DDSo_TienVND'; // Tiền Việt Nam Đồng - 7
export const DDSo_NgoaiTe = 'DDSo_NgoaiTe'; // Tiền ngoại tệ - 8
export const SO_NGUYEN = 9; // Chỉ nhập số nguyên - 9
export const DDSo_SoAm = 'DDSo_SoAm'; // Hiển thị số âm
export const DDSo_DocTienLe = 'DDSo_DocTienLe'; // Cách đọc số tiền lẻ
export const DDSo_NCachHangNghin = 'DDSo_NCachHangNghin'; // Ngăn cách hàng nghìn
export const DDSo_NCachHangDVi = 'DDSo_NCachHangDVi'; // Ngăn cách hàng đơn vị

export enum Enum {}

export enum AccountType {
    TK_DON_GIA = 'CostAccount',
    TK_KHO = 'RepositoryAccount',
    TK_CHIET_KHAU = 'DiscountAccount',
    TK_NO = 'DebitAccount',
    TK_CO = 'CreditAccount',
    TK_THUE_GTGT = 'VATAccount',
    TKDU_THUE_GTGT = 'DeductionDebitAccount',
    TK_THUE_NK = 'ImportTaxAccount',
    TK_THUE_TTDB = 'SpecialConsumeTaxAccount',
    TK_THUE_XK = 'ExportTaxAccount',
    TK_DU_THUE_XK = 'ExportTaxAccountCorresponding'
}

export enum CategoryName {
    DOI_TUONG = 'AccountingObjects', // Combobox đối tượng
    NHAN_VIEN = 'Employees', // Combobox nhân viên
    TAI_KHOAN_NGAN_HANG = 'BankAccountDetails', // Combobox tài khoản ngân hàng
    KHO = 'Repositories', // Combobox kho
    DON_VI_TINH = 'Unit', // Combobox đơn vị tính
    MA_THONG_KE = 'StatisticsCode',
    KHOAN_MUC_CHI_PHI = 'ExpenseItem',
    HOP_DONG = 'EMContracts',
    MUC_THU_CHI = 'BudgetItems',
    KHACH_HANG = 'Customer',
    NHA_CUNG_CAP = 'Supplier',
    KHAC = 'Customer/Supplier',
    THE_TIN_DUNG = 'CreditCard',
    NHOM_HHDV_CHIU_THUE_TTĐB = 'MaterialGoodsSpecialTaxGroup',
    LOAI_VAT_TU_HANG_HOA = 'MaterialGoodsCategory',
    DINH_KHOAN_TU_DONG = 'autoPrinciple',
    NGAN_HANG = 'bank',
    LOAI_TIEN = 'currency',
    VAT_TU_HANG_HOA = 'MaterialGoods',
    VAT_TU_HANG_HOA_PP_SERVICE = 'MaterialGoods_PPService',
    DOI_TUONG_TAP_HOP_CHI_PHI = 'CostSet',
    PHONG_BAN = 'OrganizationUnit',
    PHUONG_THUC_VAN_CHUYEN = 'TransportMethod'
}

export enum UnitTypeByOrganizationUnit {
    TONG_CONG_TY = 0,
    CHI_NHANH = 1,
    VAN_PHONG_DAI_DIEN = 2,
    DIA_DIEM_KINH_DOANH = 3,
    PHONG_BAN = 4,
    KHAC = 5
}

export enum PPDanhGiaDoDang {
    SP_HOAN_THANH_TUONG_DUONG = 0,
    NVLTT = 1,
    DINH_MUC = 2
}

export enum TieuThucPhanBo {
    NVLTT = 0,
    NHAN_CONG_TT = 1,
    CHI_PHI_TT = 2,
    DINH_MUC = 3,
    DOANH_THU = 3
}

export enum ObjectTypeByCPOPN {
    SAN_PHAM = 0,
    PHAN_XUONG_PHONG_BAN = 1,
    CONG_TRINH_VU_VIEC = 2,
    DON_HANG = 3,
    HOP_DONG = 4
}

export enum EINVOICE {
    HOA_DON_MOI_TAO_LAP = 0,
    HOA_DON_CO_CHU_KY_SO = 1,
    HOA_DON_BI_THAY_THE = 3,
    HOA_DON_BI_DIEU_CHINH = 4,
    HOA_DON_HUY = 5,
    HOA_DON_MOI_TAO_LAP_THAY_THE = 7,
    HOA_DON_MOI_TAO_LAP_DIEU_CHINH = 8,
    TYPE_HANG_MUA_TRA_LAI = 220,
    TYPE_BAN_HANG_CHUA_THU_TIEN = 320,
    TYPE_BAN_HANG_THU_TIEN_NGAY_TM = 321,
    TYPE_BAN_HANG_THU_TIEN_NGAY_CK = 322,
    TYPE_HANG_BAN_TRA_LAI = 330
}

export enum TypeID {
    MUA_HANG = 210,
    HANG_BAN_TRA_LAI = 330,
    HANG_GIAM_GIA = 340,
    XUAT_HOA_DON = 350,
    XUAT_HOA_DON_BH = 351,
    XUAT_HOA_DON_HBGG = 353,
    BAN_HANG_CHUA_THU_TIEN = 320,
    BAN_HANG_THU_TIEN_NGAY_TM = 321,
    BAN_HANG_THU_TIEN_NGAY_CK = 322,
    BANG_KE_HANG_HOA_DICH_VU = 323,
    PHIEU_THU_TU_BAN_HANG = 102,
    NOP_TIEN_TU_BAN_HANG = 162,
    NHAP_KHO = 400,
    NHAP_KHO_TU_DIEU_CHINH = 401,
    NHAP_KHO_TU_MUA_HANG = 402,
    NHAP_KHO_TU_BAN_HANG_TRA_LAI = 403,
    XUAT_KHO = 410,
    XUAT_KHO_TU_BAN_HANG = 411,
    XUAT_KHO_TU_MUA_HANG = 412,
    XUAT_KHO_TU_DIEU_CHINH = 413,
    MUA_HANG_TRA_LAI = 220,
    MUA_HANG_GIAM_GIA = 230,
    PHIEU_THU_TIEN_KHACH_HANG = 101,
    NOP_TIEN_TU_KHACH_HANG = 161,
    PHAN_BO_CHI_PHI_TRA_TRUOC = 709,
    CHUNG_TU_NGHIEP_VU_KHAC = 700,
    KET_CHUYEN_LAI_LO = 702,
    DANG_KY_SU_DUNG_HOA_DON = 810,
    PHIEU_THU = 100,
    PHIEU_CHI = 110,
    PHIEU_CHI_NHA_CUNG_CAP = 116,
    PHIEU_CHI_MUA_DICH_VU = 114,
    PHIEU_CHI_MUA_QUA_KHO_VA_KHONG_QUA_KHO = 115,
    UY_NHIEM_CHI = 120,
    SEC_CHUYEN_KHOAN = 130,
    SEC_TIEN_MAT = 140,
    NOP_TIEN_TK = 160,
    THE_TIN_DUNG = 170,
    BAO_GIA = 300,
    DON_DAT_HANG = 310,
    CHUYEN_KHO = 420,
    CHUYEN_KHO_KIEM_VAN_CHUYEN = 420,
    CHUYEN_KHO_GUI_DAI_LY = 421,
    CHUYEN_KHO_NOI_BO = 422,
    HOP_DONG_BAN = 730,
    MUA_DICH_VU = 240,
    OPN_DOI_TUONG = 742,
    DON_MUA_HANG = 200,

    GHI_GIAM_CCDC = 530,
    DIEU_CHINH_CCDC = 550,
    DIEU_CHUYEN_CCDC = 540,

    PHAN_BO_CCDC = 560,
    KIEM_KE_CCDC = 570,
    GHI_TANG_CCDC = 520,

    // tự đặt không có trong database
    CHI_PHI_TRA_TRUOC = 1111,
    TSCD_GHI_TANG = 620
}

export enum EbPackage {
    CO_PACKAGE = 1,
    HET_PACKAGE = 0,
    NULL_PACKAGE = 2
}

export enum statusOfUser {
    CHUA_DUNG = 0,
    DANG_DUNG = 1,
    HUY = 2,
    XOA = 3
}

export enum GROUP_TYPEID {
    GROUP_SAReturn = 33,
    GROUP_RSOUTWARD = 41,
    GROUP_RSINWARD = 40,
    GROUP_RSTRANFER = 42,
    GROUP_MCRECEIPT = 10,
    GROUP_MBDEPOSIT = 16,
    GROUP_SAQUOTE = 30,
    GROUP_SAORDER = 31,
    GROUP_SARETURN = 34,
    GROUP_SAINVOICE = 32,
    GROUP_PPINVOICE = 21,
    GROUP_PPDISCOUNTRETURN = 22,
    GROUP_PPDISCOUNTPURCHASE = 23,
    GROUP_GOTHERVOUCHER = 70,
    GROUP_PPSERVICE = 24,
    GROUP_PPORDER = 20,
    GROUP_TSCD_GHI_TANG = 62
}

export const PPINVOICE_TYPE = {
    GROUP_CHUNG_TU_MUA_HANG: 21,
    GROUP_PHIEU_NHAP_KHO: 40,
    GROUP_PHIEU_CHI: 11,
    GROUP_UY_NHIEM_CHI: 12,
    GROUP_SEC_CHUYEN_KHOAN: 13,
    GROUP_SEC_TIEN_MAT: 14,
    GROUP_THE_TIN_DUNG: 17,

    TYPE_ID_CHUA_THANH_TOAN: 210,
    TYPE_ID_TIEN_MAT: 211,
    TYPE_ID_UY_NHIEM_CHI: 212,
    TYPE_ID_SEC_CK: 213,
    TYPE_ID_SEC_TIEN_MAT: 215,
    TYPE_ID_THE_TIN_DUNG: 214,

    TYPE_REPORT_NHAP_KHO: 1,
    TYPE_REPORT_NHAP_KHO_A5: 6,
    TYPE_REPORT_CHUNG_TU: 2,
    TYPE_REPORT_CHUNG_TU_QD: 3,
    TYPE_REPORT_BAO_NO: 4,
    TYPE_REPORT_PHIEU_CHI: 5,
    TYPE_REPORT_PHIEU_CHI_A5: 7
};

export const RSOUTWARD_TYPE = {
    TYPE_REPORT_XUAT_KHO: 9,
    TYPE_REPORT_XUAT_KHO_A5: 14,
    TYPE_REPORT_ChungTuKeToan: 1,
    TYPE_REPORT_ChungTuKeToanQuyDoi: 3
};

export const RSTRANSFER_TYPE = {
    TYPE_REPORT_XUAT_KHO: 2,
    TYPE_REPORT_XUAT_KHO_A5: 3,
    TYPE_REPORT_ChungTuKeToan: 1,
    TYPE_REPORT_PhieuNhapKho: 4,
    TYPE_REPORT_PhieuNhapKhoA5: 5
};

export const PPINVOICE_COMPONENT_TYPE = {
    MUA_HANG_QUA_KHO: 1,
    MUA_HANG_KHONG_QUA_KHO: 2,
    REF_QUA_KHO: 3,
    REF_KHONG_QUA_KHO: 4
};

export const ACCOUNT_DETAIL_TYPE = {
    ACCOUNT_DEBIT: '6',
    ACCOUNT_SUPPLIER: '0',
    ACCOUNT_CUSTOMER: '1',
    ACCOUNT_EMPLOYEE: '2',
    ACCOUNT_BANK: '5'
};

export const MATERIAL_GOODS_TYPE = {
    SERVICE: 2,
    DIFF: 4
};

export const CURRENCY_ID = 'VND';

export const ACCOUNTING_TYPE_ID = {
    NORMAL_ACCOUNT: 740,
    MATERIAL_GOODS_TYPE: 741,
    ACCOUNTING_OBJECT: 742
};

export const ACCOUNTING_TYPE = {
    MT: 'VTHH',
    AO: 'DoiTuong',
    NH: 'NganHang'
};

export const EINVOICE_STATUS = {
    SUCCESS: 2,
    UNSUSSCESS: 1
};

export const INVOICE_FORM = {
    E: 'E',
    P: 'P',
    T: 'T',
    HD_DIEN_TU: 0,
    HD_DAT_IN: 1,
    HD_TU_IN: 2
};

export const KET_CHUYEN = {
    ACCOUNT_SPECIAL: '413',
    NO: 0,
    CO: 1,
    NO_CO: 2,
    CO_NO: 3,
    ACCOUNT_911: '911'
};

export const FORMULA = {
    DIVIDE: '/',
    MULTIPLY: '*'
};

export enum EXPENSE_ITEM {
    NGUYEN_VAT_LIEU_TRUC_TIEP = 0,
    NHAN_CONG_TRUC_TIEP = 1,
    CHI_PHI_NHAN_CONG = 2,
    CHI_PHI_NGUYEN_VAT_LIEU = 3,
    CHI_PHI_DUNG_CU_SAN_XUAT = 4,
    CHI_PHI_KHAU_HAO_MAY = 5,
    CHI_PHI_DICH_VU_MUA_NGOAI = 6,
    CHI_PHI_BANG_TIEN_KHAC = 7,
    PHI_PHI_NHAN_VIEN_PHAN_XUONG = 8,
    CHI_PHI_NGUYEN_VAT_LIEU_CHUNG = 9,
    CHI_PHI_DUNG_CU_SAN_XUAT_CHUNG = 10,
    CHI_PHI_KHAU_HAO_MAY_CHUNG = 11,
    CHI_PHI_DICH_VU_MUA_NGOAI_CHUNG,
    CHI_PHI_BANG_TIEN_MAT_KHAC,
    GIA_TRI_TRONG
}

export const CURRENCY = {
    VND: 'VND'
};

export const ClOSE_BOOK = {
    RECORD: 1,
    CHANG_POSTEDDATE: 2,
    DELETE: 3
};

export const CALCULATE_OW = {
    BINH_QUAN_CUOI_KY: 'Bình quân cuối kỳ',
    BINH_QUAN_TUC_THOI: 'Bình quân tức thời',
    NHAP_TRUOC_XUAT_TRUOC: 'Nhập trước xuất trước',
    DICH_DANH: 'Đích danh',
    GIA_TRI: 'Giá trị',
    BINH_QUAN_CUOI_KY_CODE: '0',
    BINH_QUAN_TUC_THOI_CODE: '1',
    NHAP_TRUOC_XUAT_TRUOC_CODE: '2',
    DICH_DANH_CODE: '3',
    GIA_TRI_CODE: '4'
};

export const REPORT = {
    ChungTuKeToan: 1,
    ChungTuKeToanCCDC: 112,
    ChungTuPhanBoCCDC: 113,
    BienBanKiemKeCCDC: 111,
    BieuMau: 2,
    ChungTuKeToanQuyDoi: 3,
    PhieuChi: 4,
    PhieuChiTT2Lien: 41,
    PhieuChiA5: 42,
    GiayBaoNo: 5,
    PhieuNhapKho: 6,
    PhieuThu: 7,
    PhieuThuTT2Lien: 71,
    PhieuThuA5: 72,
    GiayBaoCo: 8,
    HoadonGTGT: 10,
    HoadonBanHang: 11,
    PhieuXuatKho: 9,
    PhieuNhapKhoA5: 12,
    BangKeHangHoaDichVu: 13,
    GhiGiamCCDC: 973,
    DieuChinhCCDC: 974,
    DieuChuyenCCDC: 975
};

export const BaoCao = {
    TongHop: {
        SO_NHAT_KY_CHUNG: 'SO_NHAT_KY_CHUNG',
        SO_NHAT_KY_CHUNG_XLS: 'so_nhat_ky_chung.xlsx',
        SO_CAI_HT_NHAT_KY_CHUNG: 'SO_CAI_HT_NHAT_KY_CHUNG',
        SO_CAI_HT_NHAT_KY_CHUNG_XLS: 'so_cai_nhat_ky_chung.xlsx',
        PHAN_BO_CHI_PHI_TRA_TRUOC: 'PHAN_BO_CHI_PHI_TRA_TRUOC',
        PHAN_BO_CHI_PHI_TRA_TRUOC_XLS: 'bang_phan_bo_chi_phi_tra_truoc.xlsx',
        SO_CHI_TIET_CAC_TAI_KHOAN: 'SO_CHI_TIET_CAC_TAI_KHOAN',
        SO_CHI_TIET_CAC_TAI_KHOAN_XLS: 'so_chi_tiet_cac_tai_khoan.xlsx',
        SO_THEO_DOI_MA_THONG_KE_THEO_TAI_KHOAN: 'SO_THEO_DOI_MA_THONG_KE_THEO_TAI_KHOAN',
        SO_THEO_DOI_MA_THONG_KE_THEO_TAI_KHOAN_XLS: 'SoTheoDoiMaThongKeTheoTaiKhoan.xlsx',
        SO_THEO_DOI_MA_THONG_KE_THEO_KHOAN_MUC_CHI_PHI: 'SO_THEO_DOI_MA_THONG_KE_THEO_KHOAN_MUC_CHI_PHI',
        SO_THEO_DOI_MA_THONG_KE_THEO_KHOAN_MUC_CHI_PHI_XLS: 'SoTheoDoiMaThongKeTheoKhoanMucChiPhi.xlsx',
        TONG_HOP_CHI_PHI_THEO_KHOAN_MUC_CHI_PHI: 'TONG_HOP_CHI_PHI_THEO_KHOAN_MUC_CHI_PHI',
        TONG_HOP_CHI_PHI_THEO_KHOAN_MUC_CHI_PHI_XLS: 'Tong_Hop_Chi_Phi_Theo_Khoan_Muc_Chi_Phi.xlsx',
        SO_THEO_DOI_THANH_TOAN_BANG_NGOAI_TE: 'SO_THEO_DOI_THANH_TOAN_BANG_NGOAI_TE',
        SO_THEO_DOI_THANH_TOAN_BANG_NGOAI_TE_XLS: 'SO_THEO_DOI_THANH_TOAN_BANG_NGOAI_TE.xlsx'
    },
    BaoCaoTaiChinh: {
        BANG_CAN_DOI_KE_TOAN: 'BANG_CAN_DOI_KE_TOAN',
        BANG_CAN_DOI_KE_TOAN_XLS: 'BangCanDoiKeToan.xlsx',
        BANG_CAN_DOI_TAI_KHOAN: 'BANG_CAN_DOI_TAI_KHOAN',
        BANG_CAN_DOI_TAI_KHOAN_XLS: 'BanCanDoiSoPhatSinh.xlsx',
        KET_QUA_HOAT_DONG_KINH_DOANH: 'KET_QUA_HOAT_DONG_KINH_DOANH',
        KET_QUA_HOAT_DONG_KINH_DOANH_XLS: 'BaoCaoKetQuaHoatDongKinhDoanh.xlsx',
        LUU_CHUYEN_TIEN_TE_TT: 'LUU_CHUYEN_TIEN_TE_TT',
        LUU_CHUYEN_TIEN_TE_TT_XLS: 'BaoCaoLuuChuyenTienTeTT.xlsx',
        LUU_CHUYEN_TIEN_TE_GT: 'LUU_CHUYEN_TIEN_TE_GT',
        LUU_CHUYEN_TIEN_TE_GT_XLS: 'BaoCaoLuuChuyenTienTeGT.xlsx',
        THUYET_MINH_BAO_CAO_TAI_CHINH: 'THUYET_MINH_BAO_CAO_TAI_CHINH',
        TONG_HOP_CONG_NO_PHAI_THU_XLS: 'TongHopCongNoPhaiThu.xlsx',
        CHI_TIET_CONG_NO_PHAI_THU_XLS: 'ChiTietCongNoPhaiThu.xlsx',
        THUYET_MINH_BAO_CAO_TAI_CHINH_XLS: 'ThuyetMinhBaoCaoTaiChinh.xlsx'
    },
    BanHang: {
        SO_NHAT_KY_BAN_HANG: 'SO_NHAT_KY_BAN_HANG',
        TONG_HOP_CONG_NO_PHAI_THU: 'TONG_HOP_CONG_NO_PHAI_THU',
        CHI_TIET_CONG_NO_PHAI_THU: 'CHI_TIET_CONG_NO_PHAI_THU',
        SO_CHI_TIET_BAN_HANG: 'SO_CHI_TIET_BAN_HANG',
        SO_NHAT_KY_BAN_HANG_XLS: 'SoNhatKiBanHang.xlsx',
        SO_CHI_TIET_BAN_HANG_XLS: 'SoChiTietBanHang.xlsx',
        SO_THEO_DOI_LAI_LO_THEO_HOA_DON: 'SO_THEO_DOI_LAI_LO_THEO_HOA_DON',
        SO_THEO_DOI_CONG_NO_PHAI_THU_THEO_HOA_DON: 'SO_THEO_DOI_CONG_NO_PHAI_THU_THEO_HOA_DON',
        SO_THEO_DOI_LAI_LO_THEO_HOA_DON_XLS: 'SoTheoDoiLaiLoTheoHoaDon.xlsx',
        SO_THEO_DOI_CONG_NO_PHAI_THU_THEO_HOA_DON_XLS: 'SoTheoDoiCongNoPhaiThuTheoHoaDon.xlsx'
    },
    Kho: {
        SO_CHI_TIET_VAT_LIEU: 'SO_CHI_TIET_VAT_LIEU',
        SO_CHI_TIET_VAT_LIEU_XLS: 'So_chi_tiet_vat_lieu_dung_cu.xlsx',
        THE_KHO: 'THE_KHO',
        THE_KHO_XLS: 'the_kho.xlsx',
        TONG_HOP_TON_KHO: 'TONG_HOP_TON_KHO',
        TONG_HOP_TON_KHO_XLS: 'tong_hop_ton_kho.xlsx',
        TONG_HOP_CHI_TIET_VAT_LIEU: 'TONG_HOP_CHI_TIET_VAT_LIEU',
        TONG_HOP_CHI_TIET_VAT_LIEU_XLS: 'bang_tong_hop_chi_tiet_vat_dung.xlsx'
    },
    Quy: {
        SO_KE_TOAN_CHI_TIET_QUY_TIEN_MAT: 'SO_KE_TOAN_CHI_TIET_QUY_TIEN_MAT',
        SO_KE_TOAN_CHI_TIET_QUY_TIEN_MAT_XLS: 'SoKeToanChiTietQuyTienMat.xlsx',
        SO_QUY_TIEN_MAT: 'SO_QUY_TIEN_MAT',
        SO_QUY_TIEN_MAT_XLS: 'SoQuyTienMat.xlsx'
    },
    GiaThanh: {
        THE_TINH_GIA_THANH: 'THE_TINH_GIA_THANH',
        THE_TINH_GIA_THANH_XLS: 'The_Tinh_Gia_Thanh.xlsx'
    },
    SO_NHAT_KY_THU_TIEN: 'SO_NHAT_KY_THU_TIEN',
    SO_NHAT_KY_THU_TIEN_XLS: 'SO_NHAT_KY_THU_TIEN.xlsx',
    SO_NHAT_KY_CHI_TIEN: 'SO_NHAT_KY_CHI_TIEN',
    SO_NHAT_KY_CHI_TIEN_XLS: 'SO_NHAT_KY_CHI_TIEN.xlsx',
    BANG_KE_BAN_RA: 'BANG_KE_BAN_RA',
    BANG_KE_BAN_RA_XLS: 'BangKe_HH_DV_BanRa.xlsx',
    BANG_KE_BAN_RA_QUAN_TRI_XLS: 'BangKe_HH_DV_BanRa_QT.xlsx',
    BANG_KE_MUA_VAO: 'BANG_KE_MUA_VAO',
    BANG_KE_MUA_VAO_XLS: 'BangKe_HH_DV_MuaVao.xlsx',
    BANG_KE_MUA_VAO_QUAN_TRI_XLS: 'BangKe_HH_DV_MuaVao_QT.xlsx',
    MuaHang: {
        SO_NHAT_KY_MUA_HANG: 'SO_NHAT_KY_MUA_HANG',
        TONG_HOP_CONG_NO_PHAI_TRA: 'TONG_HOP_CONG_NO_PHAI_TRA',
        TONG_HOP_CONG_NO_PHAI_TRA_XLS: 'TongHopCongNoPhaiTra.xlsx',
        CHI_TIET_CONG_NO_PHAI_TRA: 'CHI_TIET_CONG_NO_PHAI_TRA',
        CHI_TIET_CONG_NO_PHAI_TRA_XLS: 'ChiTietCongNoPhaiTra.xlsx',
        SO_CHI_TIET_MUA_HANG: 'SO_CHI_TIET_MUA_HANG',
        SO_CHI_TIET_MUA_HANG_XLS: 'SoChiTietMuaHang.xlsx',
        SO_NHAT_KY_MUA_HANG_XLS: 'SoNhatKiMuaHang.xlsx'
    },
    Ngan_Hang: {
        SO_TIEN_GUI_NGAN_HANG: 'SO_TIEN_GUI_NGAN_HANG',
        BANG_KE_SO_DU_NGAN_HANG: 'BANG_KE_SO_DU_NGAN_HANG',
        SO_TIEN_GUI_NGAN_HANG_XLS: 'TienGuiNganHang.xlsx',
        BANG_KE_SO_DU_NGAN_HANG_XLS: 'BangKeSoDuNganHang.xlsx'
    },
    HDDT: {
        BAO_CAO_TINH_HINH_SU_DUNG_HD: 'BAO_CAO_TINH_HINH_SU_DUNG_HD',
        BANG_KE_HD_HHDV: 'BANG_KE_HD_HHDV',
        BAO_CAO_DOANH_THU_THEO_SP: 'BAO_CAO_DOANH_THU_THEO_SP',
        BAO_CAO_DOANH_THU_THEO_BEN_MUA: 'BAO_CAO_DOANH_THU_THEO_BEN_MUA'
    },
    Gia_Thanh: {
        SO_CHI_PHI_SAN_XUAT_KINH_DOANH: 'SO_CHI_PHI_SAN_XUAT_KINH_DOANH',
        SO_CHI_PHI_SAN_XUAT_KINH_DOANH_XLS: 'SO_CHI_PHI_SAN_XUAT_KINH_DOANH.xlsx',
        SO_THEO_DOI_DOI_TUONG_THCP_THEO_TAI_KHOAN: 'SO_THEO_DOI_DOI_TUONG_THCP_THEO_TAI_KHOAN',
        SO_THEO_DOI_DOI_TUONG_THCP_THEO_TAI_KHOAN_XSL: 'So_theo_doi_doi_tuong_THCP_theo_tai_khoan.xlsx',
        SO_THEO_DOI_DOI_TUONG_THCP_THEO_KHOAN_MUC_CHI_PHI: 'SO_THEO_DOI_DOI_TUONG_THCP_THEO_KHOAN_MUC_CHI_PHI',
        SO_THEO_DOI_DOI_TUONG_THCP_THEO_KHOAN_MUC_CHI_PHI_XSL: 'So_theo_doi_doi_tuong_THCP_theo_khoan_muc_chi_phi.xlsx'
    },
    CCDC: {
        SO_CONG_CU_DUNG_CU: 'SO_CONG_CU_DUNG_CU',
        SO_CONG_CU_DUNG_CU_XLS: 'SoCongCuDungCu.xlsx',
        SO_THEO_DOI_CCDC_TAI_NOI_SU_DUNG: 'SO_THEO_DOI_CCDC_TAI_NOI_SU_DUNG',
        SO_THEO_DOI_CCDC_TAI_NOI_SU_DUNG_XLS: 'SoTheoDoiCCDCTaiNoiSuDung.xlsx'
    }
};

export const TOOLTIPS = {
    DELETE: 'ebwebApp.mBDeposit.toolTip.delete',
    ADD: 'ebwebApp.mBDeposit.toolTip.add',
    EDIT: 'ebwebApp.mBDeposit.toolTip.edit',
    SAVE: 'ebwebApp.mBDeposit.toolTip.save',
    RECORD: 'ebwebApp.mBDeposit.toolTip.record',
    UNRECORD: 'ebwebApp.mBDeposit.toolTip.unrecord',
    EXPORT: 'ebwebApp.mBDeposit.toolTip.export',
    PRINT: 'ebwebApp.mBDeposit.toolTip.print',
    SEARCH: 'ebwebApp.mBDeposit.toolTip.search',
    SAVE_AND_NEW: 'ebwebApp.mBDeposit.toolTip.saveAndNew',
    COPY_AND_NEW: 'ebwebApp.mBDeposit.toolTip.copyAndNew',
    CLOSE_FORM: 'ebwebApp.mBDeposit.toolTip.closeForm',
    INSERT_LINE: 'ebwebApp.mBDeposit.toolTip.insertLine',
    DELETE_LINE: 'ebwebApp.mBDeposit.toolTip.deleteLine'
};

export const WarningPackage = {
    limitedNo: 1,
    expiredTime: 1
};

export const MSGERROR = {
    XUAT_QUA_SO_TON: 'xuat qua so ton',
    XUAT_QUA_TON_QUY: 'xuat qua ton quy',
    XUAT_QUA_TON_QUY_QT: 'xuat qua ton quy so quan tri',
    XUAT_QUA_TON_QUY_TC: 'xuat qua ton quy so tai chinh',
    KC_DU_LIEU_AM: 'kcDataError'
};

export const MultiAction = {
    GHI_SO: 'GHI_SO',
    BO_GHI_SO: 'BO_GHI_SO',
    XOA: 'XOA'
};

export const BROADCAST_EVENT = {
    DISABLE_USER_SELECT: 'DISABLE_USER_SELECT'
};

export const GIA_THANH = {
    PHUONG_PHAP_TY_LE: 2,
    PHUONG_PHAP_GIAN_DON: 0,
    PHUONG_PHAP_HE_SO: 1,
    PHUONG_PHAP_CONG_TRINH_VU_VIEC: 3,
    PHUONG_PHAP_DON_HANG: 4
};

export const DTTHCP = {
    CONG_TRINH_VU_VIEC: 1
};

export const NCCDV_EINVOICE = {
    SDS: 'SDS',
    SIV: 'SIV',
    MIV: 'MIV',
    VNPT: 'VNPT'
};

export const EINVOICE_RESPONE = {
    Success: 2,
    InvalidData: 4,
    Fail: 1,
    errorCode: 'SUCCESS'
};

export const ImportExcel = {
    SELECT_SHEET: 'SELECT_SHEET'
};

export const DanhMucType = {
    KH: 'KH',
    NV: 'NV',
    NCC: 'NCC',
    VTHH: 'VTHH'
};

export const SDSoQuanTri = {
    USE: '1',
    NOT_USE: '0'
};
