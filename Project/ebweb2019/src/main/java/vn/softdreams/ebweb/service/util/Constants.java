package vn.softdreams.ebweb.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface Constants {
    String BCC_MAIL = "sent@easybooks.vn";
    interface InvoiceForm {
        String HOA_DON_DIEN_TU = "Hóa đơn điện tử";
        String HOA_DON_DAT_IN = "Hóa đơn đặt in";
        String HOA_DON_TU_IN = "Hóa đơn tự in";
        int HD_DIEN_TU = 0;
    }

    interface MSGRecord {
        String XUAT_QUA_SO_TON = "xuat qua so ton";
        String XUAT_QUA_TON_QUY = "xuat qua ton quy";
        String XUAT_QUA_TON_QUY_QT = "xuat qua ton quy so quan tri";
        String XUAT_QUA_TON_QUY_TC = "xuat qua ton quy so tai chinh";
        String KC_DU_LIEU_AM = "kcDataError";
    }

    interface Formular {
        String PHEP_NHAN = "*";
        String PHEP_CHIA = "/";

    }

    interface CalculationMethod {
        Integer PP_TINH_GIA_BINH_QUAN_CUOI_KY = 0;
        Integer PP_TINH_GIA_BINH_QUAN_TUC_THOI = 1;
        Integer PP_TINH_GIA_NHAP_TRUOC_XUAT_TRUOC = 2;
        Integer PP_TINH_GIA_DICH_DANH = 3;
        Integer PP_TINH_GIA_GIA_TRI = 4;
        String TINH_NGAY_KHI_LAP_CHUNG_TU = "1";
    }

    interface GiaThanh {
        Integer GIAN_DON = 0;
        Integer HE_SO = 1;
        Integer TY_LE = 2;
        Integer CTVV = 3;
        Integer DON_HANG = 4;
        Integer HOP_DONG = 5;
    }

    interface TypeLedger {
        String SO_TAI_CHINH = "Sổ tài chính";
        String SO_QUAN_TRI = "Sổ quản trị";
        Integer FINANCIAL_BOOK = 0;
        Integer MANAGEMENT_BOOK = 1;
        Integer BOTH_BOOK = 2;
    }

    interface StatusInvoice {
        String MOI_TAO_LAP = "Hóa đơn mới tạo lập";
        String CHU_KY_SO = "Hóa đơn có chữ ký số";
        String THAY_THE = "Hóa đơn bị thay thế";
        String DIEU_CHINH = "Hóa đơn bị điều chỉnh";
        String HUY = "Hóa đơn bị huỷ";
    }

    interface Vat {
        String ZERO_PERCENT = "0";
        String FIVE_PERCENT = "5";
        String TEN_PERCENT = "10";
        String KCT = "KCT";
        String KTT = "KTT";
        String FIVE_PERCENT_NAME = "5%";
        String TEN_PERCENT_NAME = "10%";
        String ZERO_PERCENT_NAME = "0%";
        Integer ZERO_PERCENT_VAL = 0;
        Integer FIVE_PERCENT_VAL = 1;
        Integer TEN_PERCENT_VAL = 2;
        Integer KCT_VAL = 3;
        Integer KTT_VAL = 4;
    }

    interface SystemOption {
        String Zero = "0";
        String VTHH_PPTinhGiaXKho = "VTHH_PPTinhGiaXKho";
        String TCKHAC_SDTichHopHDDT = "TCKHAC_SDTichHopHDDT";
        String CheckHDCD = "CheckHDCD";
        String SignType = "SignType";
        String PHIEN_SoLamViec = "PHIEN_SoLamViec";
        String TCKHAC_GhiSo = "TCKHAC_GhiSo";
        String TCKHAC_SDSOQUANTRI = "TCKHAC_SDSoQuanTri";
        String TCKHAC_HanCheTK = "TCKHAC_HanCheTK";
        String DDSo_NCachHangNghin = "DDSo_NCachHangNghin";
        String TCKHAC_MauSoAm = "TCKHAC_MauSoAm";
        String DDSo_NCachHangDVi = "DDSo_NCachHangDVi";
        String DDSo_SoAm = "DDSo_SoAm";
        String DDSo_TienVND = "DDSo_TienVND";
        String DDSo_NgoaiTe = "DDSo_NgoaiTe";
        String DDSo_DonGia = "DDSo_DonGia";
        String DDSo_DonGiaNT = "DDSo_DonGiaNT";
        String DDSo_SoLuong = "DDSo_SoLuong";
        String DDSo_TyGia = "DDSo_TyGia";
        String DDSo_TyLe = "DDSo_TyLe";
        String DDSo_TyLePBo = "DDSo_TyLePBo";
        String EI_IDNhaCungCapDichVu = "EI_IDNhaCungCapDichVu";
        String EI_TenDangNhap = "EI_TenDangNhap";
        String EI_MatKhau = "EI_MatKhau";
        String EI_Path = "EI_Path";
        String Token_MIV = "Token_MIV";
        String TCKHAC_SDDMVTHH = "TCKHAC_SDDMVTHH";
        String TCKHAC_SDDMDoiTuong = "TCKHAC_SDDMDoiTuong";
        String TCKHAC_SDDMKho = "TCKHAC_SDDMKho";
        String TCKHAC_SDDMCCDC = "TCKHAC_SDDMVTHH";
        String TCKHAC_SDDMTSCD = "TCKHAC_SDDMTSCĐ";
        String TCKHAC_SDDMDTTHCP = "TCKHAC_SDDMĐTTHCP";
        String TCKHAC_SDDMTKNH = "TCKHAC_SDDMTKNH";
        String TCKHAC_SDDMTheTD = "TCKHAC_SDDMTheTD";
        String VTHH_TinhgiaTungKho = "VTHH_TinhgiaTungKho";
        String NgayHachToan = "NgayHachToan";
        String DBDateClosed = "DBDateClosed";
        String DBDateClosedOld = "DBDateClosedOld";
        String XUAT_QUA_TON_QUY = "TCKHAC_ChiQuaTonQuy";
        String HH_XUATQUASLTON = "VTHH_XuatQuaSLTon";
        String VTHH_TDTGia_NgayLapCTu = "VTHH_TDTGia_NgayLapCTu";
        String DDSo_DocTienLe = "DDSo_DocTienLe";
        String TCKHAC_DocTienBangChu = "TCKHAC_DocTienBangChu";
        String TCKHAC_PBCCDC = "TCKHAC_PBCCDC";
    }

    interface Report {
        int HeaderSetting0 = 0; // Hiển thị tên công ty, địa chỉ, mã số thuế
        int HeaderSetting1 = 1; // Hiển thị tên công ty, và mã số thuế
        int HeaderSetting2 = 2; // Hiển thị tên công ty và địa chỉ
        int ChungTuKeToan = 1;
        int BieuMau = 2;
        int ChungTuKeToanQuyDoi = 3;
        int PhieuChi = 4;
        int PhieuChiTT2Lien = 41;
        int PhieuChiA5 = 42;
        int GiayBaoNo = 5;
        int PhieuNhapKho = 6;
        int PhieuThu = 7;
        int PhieuThuTT2Lien = 71;
        int PhieuThuA5 = 72;
        int GiayBaoCo = 8;
        int PhieuXuatKho = 9;
        int HoadonGTGT = 10;
        int HoadonBanHang = 11;
        int PhieuNhapKhoA5 = 12;
        int BangKeDichVu = 13;
        int PhieuXuatKhoA5 = 14;

        int GhiGiamCCDC = 973;
        int DieuChinhCCDC = 974;
        int DieuchuyenCCDC = 975;

        int BienBanKiemKeCCDC = 111;
        int ChungTuKeToanCCDC = 112;
        int ChungTuPhanBoCCDC = 113;

    }

    /**
     * @author jsp
     */
    interface SYSTEM_OPTION_CODE {
        String RESTRICT_EDIT = "TCKHAC_HanCheTK";
    }

    interface SYSTEM_OPTION_DATA {
        Integer STORAGE = 1;
    }

    interface PPServiceType {
        int PPSERVICE_GROUP_ID = 24;
        int PPSERVICE_UNPAID = 240;
        int PPSERVICE_CASH = 241;
        int PPSERVICE_PAYMENT_ORDER = 242;
        int PPSERVICE_TRANSFER_SEC = 243;
        int PPSERVICE_CREDIT_CARD = 244;
        int PPSERVICE_CASH_SEC = 245;

        int MC_PAYMENT = 114;
        int PAYMENT_ORDER = 124;
        int TRANSFER_SEC = 133;
        int CASH_SEC = 143;
        int CREDIT_CARD = 173;
    }

    interface TypeGroupId {
        int PP_SERVICE_LICENSE = 24;
        int MC_PAYMENT = 11;
        int PAYMENT_ORDER = 12;
        int TRANSFER_SEC = 13;
        int CASH_SEC = 14;
        int CREDIT_CARD = 17;
        int PP_INVOICE = 21;
        int RS_IN_WARD_OUT_WARD = 40;
        int RS_OUT_WARD = 41;
        int G_OTHER_VOUCHER_KC = 71;
    }

    interface TypeId {
        int TYPE_G_OTHER_VOUCHER_KC = 702;
    }


    interface AccountDefaultColumnName {
        String DEBIT_ACCOUNT = "DebitAccount";
        String CREDIT_ACCOUNT = "CreditAccount";
    }

    interface UpdateDataDTOMessages {
        String SAVE_SUCCESS = "SAVE_SUCCESS";
        String UPDATE_SUCCESS = "UPDATE_SUCCESS";
        String CANNOT_UPDATE_OTHER_NO_BOOK = "CANNOT_UPDATE_OTHER_NO_BOOK";
        String DUPLICATE_OTHER_NO_BOOK = "DUPLICATE_OTHER_NO_BOOK";
        String CANNOT_UPDATE_NO_BOOK = "CANNOT_UPDATE_NO_BOOK";
        String CANNOT_FIND_CURRENT_BOOK = "CANNOT_FIND_CURRENT_BOOK";
        String CANNOT_UPDATE_NO_BOOK_RS = "CANNOT_UPDATE_NO_BOOK_RS";
        String DUPLICATE_NO_BOOK = "DUPLICATE_NO_BOOK";
        String DUPLICATE_NO_BOOK_RS = "DUPLICATE_NO_BOOK_RS";
        String CURRENT_USER_IS_NOT_PRESENT = "CURRENT_USER_IS_NOT_PRESENT";
        String EXPENSIVE_ITEM_HAS_CHILD = "EXPENSIVE_ITEM_HAS_CHILD";
        String DATE_NULL = "DATE_NULL";
        String PP_SERVICE_IS_NOT_PRESET = "PP_SERVICE_IS_NOT_PRESET";
        String MC_PAYMENT_IS_NOT_PRESET = "MC_PAYMENT_IS_NOT_PRESET";
        String MB_TELLER_PAPER_IS_NOT_PRESET = "MB_TELLER_PAPER_IS_NOT_PRESET";
        String MB_CREDIT_CARD_IS_NOT_PRESET = "MB_CREDIT_CARD_IS_NOT_PRESET";
        String DELETE_SUCCESS = "DELETE_SUCCESS";
        String HAD_RECORDED = "HAD_RECORDED";
        String HAD_REFERENCE = "HAD_REFERENCE";
        String HAD_PAID = "HAD_PAID";
        String HAD_REFERENCE_AND_PAID = "HAD_REFERENCE_AND_PAID";
        String RSI_ERROR = "RSI_ERROR";
        String SUCCESS = "SUCCESS";
        String FAIL = "FAIL";
        String IS_LAST_ITEM = "IS_LAST_ITEM";
        String IS_FIST_ITEM = "IS_FIST_ITEM";
        String QUANTITY_RECEIPT_GREATER_THAN = "QUANTITY_RECEIPT_GREATER_THAN";
        String QUANTITY_RECEIPT_GREATER_THAN_MESS = "Số lượng mua đang lớn hơn số lượng còn lại trên đơn mua hàng gốc! Bạn có chắc chắn muốn lưu chứng từ này?";
        String NO_BOOK_NULL = "NO_BOOK_NULL";
        String NO_BOOK_RSI_NULL = "NO_BOOK_RSI_NULL";
        String NO_BOOK_OTHER_NULL = "NO_BOOK_OTHER_NULL";
        String NOT_FOUND = "NOT_FOUND";
        String DUPLICATE = "DUPLICATE";
        String updateReceipt = "updateReceipt";
        String POSTDATE_INVALID = "postdateInvalid";
        String REQUIRED_ITEM = "REQUIRED_ITEM";
        String FILE_TOO_LARGE = "FILE_TOO_LARGE";
    }

    interface PPInvoiceType {
        int TYPE_ID_MUA_HANG_CHUA_THANH_TOAN = 210;
        int TYPE_ID_MUA_HANG_TIEN_MAT = 211;
        int TYPE_ID_MUA_HANG_UY_NHIEM_CHI = 212;
        int TYPE_ID_MUA_HANG_SEC_CK = 213;
        int TYPE_ID_MUA_HANG_SEC_TIEN_MAT = 215;
        int TYPE_ID_MUA_HANG_THE_TIN_DUNG = 214;

        int TYPE_ID_NHAP_KHO_MUA_HANG = 402;
        int TYPE_ID_PHIEU_CHI_MUA_HANG = 115;
        int TYPE_ID_PHIEU_CHI_MUA_HANG_DICH_VU = 114;
        int TYPE_ID_UY_NHIEM_CHI_MUA_HANG = 125;
        int TYPE_ID_SEC_CHUYEN_KHOAN_MUA_HANG = 131;
        int TYPE_ID_SEC_TIEN_MAT_MUA_HANG = 141;
        int TYPE_ID_THE_TIN_DUNG_MUA_HANG = 171;
        int TYPE_ID_THE_TIN_DUNG_TRA_TIEN_NCC = 174;
        int TYPE_ID_UNC_MUA_DICH_VU = 124;
        int TYPE_ID_SCK_MUA_DICH_VU = 133;
        int TYPE_ID_STM_MUA_DICH_VU = 143;
        int TYPE_ID_THE_TIN_DUNG_MUA_DICH_VU = 173;
        int TYPE_ID_UNC_MUA_HANG = 125;
        int TYPE_ID_SCK_MUA_HANG = 131;
        int TYPE_ID_STM_MUA_HANG = 141;
        int UY_NHIEM_CHI = 120;
        int SEC_CHUYEN_KHOAN = 130;
        int SEC_TIEN_MAT = 140;

        String MUA_HANG_CHUA_THANH_TOAN = "Chưa thanh toán";
        String MUA_HANG_TIEN_MAT = "Tiền mặt";
        String MUA_HANG_UY_NHIEM_CHI = "Ủy nhiệm chi";
        String MUA_HANG_SEC_CK = "Sec chuyển khoản";
        String MUA_HANG_SEC_TIEN_MAT = "Sec tiền mặt";
        String MUA_HANG_THE_TIN_DUNG = "Thẻ tín dụng";

        String DA_NHAN_HOA_DON = "Đã nhận hóa đơn";
        String CHUA_NHAN_HOA_DON = "Chưa nhận hóa đơn";
    }

    interface MCReceipt {
        int TYPE_PHIEU_THU = 100;
    }

    interface MBDeposit {
        int TYPE_BAO_CO = 160;
        int TYPE_NOP_TIEN_TU_KHACH_HANG = 161;
        int TYPE_NOP_TIEN_TU_BAN_HANG = 162;
    }

    interface MCPayment {
        int TYPE_PHIEU_CHI = 110;
    }

    interface SAOrder {
        int TYPE_DON_DAT_HANG = 310;

        interface Status {
            int CHUA_THUC_HIEN = 0;
            int DANG_THUC_HIEN = 1;
            int DA_HOAN_THANH = 2;
            int DA_HUY_BO = 3;
        }
    }

    interface PPOrder {
        int TYPE_DON_MUA_HANG = 200;
    }

    interface MBTellerPaper {
        int TYPE_BAO_NO_UNC = 120;
        int TYPE_BAO_NO_SCK = 130;
        int TYPE_BAO_NO_STM = 140;

    }

    interface MultiDelete_TypeID {
        int Type_XK = 410;
        int Type_XK_BH = 411;
        int Type_XK_HMTL = 412;
        int Type_NK = 400;
        int Type_NK_MH = 402;
        int Type_NK_HBTL = 403;
        int Type_CK_KNB = 420;
        int Type_CK_GDL = 421;
        int Type_CK_NB = 422;
    }

    interface PPDiscountReturn {
        int TYPE_MUA_HANG_TRA_LAI = 220;
        int TYPE_MUA_HANG_GIAM_GIA = 230;
    }

    interface PPInvoiceResult {
        String PPINVOICE_NOT_FOUND = "PPINVOICE_NOT_FOUND";
        String RSI_NOT_FOUND = "RSI_NOT_FOUND";
        String DELETE_SUCCESS = "DELETE_SUCCESS";
        String PPINVOICE_USED = "PPINVOICE_USED";
        String NOTHING = "NOTHING";
        String STOCK_TRUE = "STOCK_TRUE";
        String STOCK_FALSE = "STOCK_FALSE";
        String ERROR = "ERROR";
    }


    interface OrgUnitTypeConstant {
        int TONG_CONG_TY = 0;
        int CHI_NHANH = 1;
        int VAN_PHONG_DAI_DIEN = 2;
        int DIA_DIEM_KINH_DOANH = 3;
        int PHONG_BAN = 4;
        int KHAC = 5;
    }

    interface API_CRM {
        String ACTIVE_PACKAGE_CRM = "http://localhost:81/api/updateStatusPackage";
//        String ACTIVE_PACKAGE_CRM = "http://14.225.3.218:81/api/updateStatusPackage";
        String EasyBooks_Web_Address = "https://app.easybooks.vn";
        String EasyBooks_Web_Hotline = "0868.213.466 hoặc 0983.308.499";
        String Email_Title = "Tài khoản truy cập hệ thống Kế toán EasyBooks - ";
    }

    interface EInvoice {
        String CHUA_TICH_HOP = "Chưa tích hợp hóa đơn điện tử";
        int HD_MOI_TAO_LAP = 0;
        int HD_CO_CHU_KY_SO = 1;
        int HD_BI_THAY_THE = 3;
        int HD_BI_DIEU_CHINH = 4;
        int HD_HUY = 5;
        int HD_MOI_TAO_LAP_THAY_THE = 7;
        int HD_MOI_TAO_LAP_DIEU_CHINH = 8;

        Integer DANH_SACH_HOA_DON = 0;
        Integer DANH_SACH_HOA_DON_CHO_KY = 1;
        Integer DANH_SACH_HOA_DON_THAY_THE = 2;
        Integer DANH_SACH_HOA_DON_DIEU_CHINH = 3;
        Integer DANH_SACH_HOA_DON_HUY = 4;
        Integer DANH_SACH_HOA_CHUYEN_DOI = 5;

        interface DataMIV {
            int Editmode_Edit = 2;
            int Editmode_New = 1;

            interface StatusInvoice {
                String choKy = "Chờ ký";
                String daKy = "Đã ký";
                String xoaBo = "Xóa bỏ";
            }
        }

        interface Type {
            int DieuChinhThongTin = 4;
            int DieuChinhGiam = 3;
            int DieuChinhTang = 2;
        }

        interface SupplierCode {
            String SDS = "SDS";
            String SIV = "SIV";
            String MIV = "MIV";
            String VNPT = "VNPT";
        }

        interface Respone {
            Integer Success = 2;
            Integer InvalidData = 4;
            Integer Fail = 1;
            String errorCode = "SUCCESS";
        }

        interface API_SDS {
            String PUBLISH_INVOICE_SERVER = "api/publish/importAndPublishInvoice"; // phát hành hóa TH ký server
            String CREATE_INVOICE_DRAFT = "api/publish/importInvoice"; // tạo hóa đơn chưa ký số
            String GET_INVOCIE_PDF = "api/publish/getInvoicePdf"; // tải file pdf hóa đơn, chuyển dổi hóa đơn, ...
            String GET_INVOCIES_BY_IKEYS = "api/publish/GetInvoicesByIkeys"; // Lấy thông tin hóa đơn
            String REMOVE_UNSIGNED_INVOICE = "api/business/removeUnsignedInvoice"; // Xóa hóa đơn theo IKeys
            String CREATE_INVOICE_STRIP = "api/business/createInvoiceStrip"; // Tạo dải hóa đơn chờ ký trống từ tham số IkeyDate
            String IMPORT_RESERVED_INVOICE = "api/publish/importReservedInvoice"; // Tạo hóa đơn chờ ký có dữ liệu
            String CANCEL_INVOICE = "api/business/cancelInvoice"; // Hủy hóa đơn
            String IMPORT_UNSIGNED_ADJUSTMENT = "api/business/importUnsignedAdjustment"; // tạo hóa đơn điều chỉnh chưa ký số
            String IMPORT_UNSIGNED_REPLACEMENT = "api/business/importUnsignedReplacement"; // tạo hóa đơn thay thế chưa ký số
            String PUBLISH_INVOCIE_ADJUST_SERVER = "api/business/adjustInvoice"; // phát hành hóa đơn điều chỉnh - server
            String PUBLISH_INVOCIE_REPLACE_SERVER = "api/business/replaceInvoice"; // phát hành hóa đơn thay thế - server
            String EXTERNAL_GET_DIGES_FOR_IMPORTATION = "api/publish/externalGetDigestForImportation"; // Tạo hoá đơn tạm (được lưu cache, ký client) Hóa đơn mới tạo lập
            String EXTERNAL_WRAP_AND_LAUNCH_IMPORTAION = "api/publish/externalWrapAndLaunchImportation"; // phát hành hoá đơn tạm (được lưu cache, ký client) Hóa đơn mới tạo lập
            String EXTERNAL_GET_DIGES_FOR_REPLACEMENT = "api/publish/externalGetDigestForReplacement"; // Tạo hoá đơn tạm (được lưu cache, ký client) Hóa đơn thay thế
            String EXTERNAL_WRAP_AND_LAUNCH_REPLACEMENT = "api/publish/externalWrapAndLaunchReplacement"; // phát hành hoá đơn tạm (được lưu cache, ký client) Hóa đơn thay thế
            String EXTERNAL_GET_DIGES_FOR_ADJUSTMENT = "api/publish/externalGetDigestForAdjustment"; // Tạo hoá đơn tạm (được lưu cache, ký client) Hóa đơn điều chỉnh
            String EXTERNAL_WRAP_AND_LAUNCH_ADJUSTMENT = "api/publish/externalWrapAndLaunchAdjustment"; // phát hành hoá đơn tạm (được lưu cache, ký client) Hóa đơn điều chỉnh
            String SEND_MAIL = "api/business/sendIssuanceNotice"; // gửi lại thông báo phát hành cho khách hàng

            interface BAO_CAO {
                String TINH_HINH_SDHD = "api/business/getInvoiceUsageReport";
                String BANG_KE_HD_HHDV = "api/business/GetInvoiceReport";
                String BAO_CAO_DOANH_THU_THEO_SP = "api/business/GetRevenueReportByProduct";
                String BAO_CAO_DOANH_THU_THEO_BEN_MUA = "api/business/GetRevenueReportByCustomer";
            }
        }

        interface API_VIETTEL {
            String GetListInvoiceWithDate = "InvoiceAPI/InvoiceUtilsWS/getListInvoiceDataControl/"; // Lấy thông tin hóa đơn theo khoảng thời gian
            String CreateInvoice = "InvoiceAPI/InvoiceWS/createInvoice/"; // phát hành hóa đơn (Server)
            String CancelTransactionInvoice = "InvoiceAPI/InvoiceWS/cancelTransactionInvoice/"; // hủy hóa đơn
            String CreateOrUpdateInvoiceDraft = "InvoiceAPI/InvoiceWS/createOrUpdateInvoiceDraft/"; // tạo hóa đơn nháp
            String GetInvoiceRepresentationFile = "InvoiceAPI/InvoiceUtilsWS/getInvoiceRepresentationFile/"; // Lấy file hóa đơn
            String CreateInvoiceDraftPreview = "InvoiceAPI/InvoiceUtilsWS/createInvoiceDraftPreview/"; // Xem trc hóa đơn nháp
            String CreateBatchInvoice = "InvoiceAPI/InvoiceWS/createBatchInvoice/"; // lập hóa đơn theo lô
            String CreateExchangeInvoiceFile = "InvoiceAPI/InvoiceWS/createExchangeInvoiceFile/"; // Chuyển đổi hóa đơn
            String CreateInvoiceUsbTokenGetHash = "InvoiceAPI/InvoiceWS/createInvoiceUsbTokenGetHash/"; // Chuyển đổi hóa đơn
            String CreateInvoiceUsbTokenInsertSignature = "InvoiceAPI/InvoiceWS/createInvoiceUsbTokenInsertSignature/"; // Chuyển đổi hóa đơn
        }

        interface API_MIV {
            String Login = "api/Account/Login";
            String Save = "api/InvoiceAPI/Save"; //khởi tạo hóa đơn
            String Preview = "api/Invoice/Preview";
            String GetInfoInvoice = "api/InvoiceAPI/GetInfoInvoice";
            String xoaboHD = "api/Invoice/xoaboHD";
            String DcDinhdanh = "api/InvoiceAPI/DcDinhdanh";
            String DcTang = "api/InvoiceAPI/DcTang";
            String DcGiam = "api/InvoiceAPI/DcGiam";
        }
    }


    interface PPOrderDetailReferenceTable {
        List<String> referenceTables = Arrays.asList("PPServiceDetail", "PPInvoiceDetail", "RSInwardOutwardDetail");
    }

    interface PPInvoiceDetailReferenceTable {
        List<String> referenceTablesPayVendor = Arrays.asList("MCPaymentDetailVendor", "MBTellerPaperDetailVendor", "MBCreditCardDetailVendor");
        List<String> referenceTables = Arrays.asList("PPDiscountReturnDetail", "SAInvoiceDetail");
        String PP_INVOICE_ID = "PPInvoiceID";
    }

    interface GOtherVoucher {
        String KET_CHUYEN = "KET_CHUYEN";
        String ACCOUNT_4131 = "4131";
        String ACCOUNT_4212 = "4212";
        String ACCOUNT_635 = "635";
        String ACCOUNT_515 = "515";
        String ACCOUNT_911 = "911";
        String ACCOUNT_CURRENCY = "1111";
        String ACCOUNT_FOREIGNCURRENCY = "1112";
        List<String> LIST_ACCOUNT = new ArrayList<String>(Arrays.asList(ACCOUNT_4131, ACCOUNT_4212));
    }

    interface typeAccountingObject {
        Integer KHACH_HANG = 1;
        Integer NHA_CUNG_CAP = 0;
        Integer NHAN_VIEN = 3;
    }

    interface typeChungRieng {
        Integer DUNG_CHUNG = 0;
        Integer DUNG_RIENG = 1;
    }

    interface GenCodeOpeningBalance {
        String NO_BOOK = "OPN";
    }

    interface CurrencyID {
        String TIEN_VND = "VND";
    }

    interface Account {
        String TK_GIA_VON = "632";
        Integer GRADE_PARENT = 1;
    }

    interface ClOSE_BOOK {
        int RECORD = 1;
        int CHANG_POSTEDDATE = 2;
        int DELETE = 3;
    }

    interface EbPackage {
        int CO_PACKAGE = 1;
        int HET_PACKAGE = 0;
        int NULL_PACKAGE = 2;
        int COMTYPE_1_NOBRANCH = 1;
        int COMTYPE_2_HASBRANCH = 2;
        int COMTYPE_3_SERVICEACC = 3;
        int UNLIMITED_PACKAGE = -1;
    }

    interface EbUserPackage {
        int CHUA_DUNG = 0;
        int DANG_DUNG = 1;
        int HUY = 2;
        int XOA = 3;
    }

    interface WarningPackage {
        Integer limitedNo = 1;
        Integer expiredTime = 1;
        Integer notLimitedNo = 0;
        Integer notExpiredTime = 0;
    }

    interface Message {
        String VoucherRecorded = "Chứng từ đang được ghi sổ";
    }

    interface CRMResStatus {
        int Fail = 0;
        int Done = 1;
        int Fail_UserAndPassRequired = 2;
        int Fail_UserAndPassInvalid = 3;
        int Fail_UserNotAllowed = 4;
        int Fail_UserBlocked = 5;
        int Fail_UserNotAuthorized = 6;
        int Fail_TaxCodeExist = 7;
        int Fail_EbPackageInvalid = 8;
        int Fail_UnAuthenticated = 9;
        int Fail_EbPackageExist = 10;
        int Fail_SendEmailError = 11;
        int Fail_EmailExist = 12;
        int Fail_EmailInvalid = 13;
        int Fail_EmailNotFound = 14;
    }

    interface CRMReqStatus {
        int Cancel = 14;
    }

    interface CRMResActiveStatus {
        int Active_Fail = 0;
        int Active_Done = 1;
    }

    interface Salt {
        String value = "143a508b559cd1005507f03bef80f1e4";
    }
    interface ExcelDanhMucType {
        String KH = "KH";
        String NCC = "NCC";
        String NV = "NV";
        String VTHH = "VTHH";
    }

    interface ImportType {
        int importNew = 0;
        int importUpdate = 1;
        int importOverride = 2;
    }

    interface TaxCalculationMethod {
        int METHOD_DIRECT = 1;
        int METHOD_ABATEMENT = 0;
    }

    interface ImportExcel {
        String SELECT_SHEET = "SELECT_SHEET";
    }

    interface ComTypeForPackage {
        int KE_TOAN_THUONG = 1;
        int KE_TOAN_DA_CHI_NHANH = 2;
        int KE_TOAN_DICH_VU = 3;
    }

    interface ExpenseType {
        int NGUYEN_VAT_LIEU_TRUC_TIEP = 0;
        int NHAN_CONG_TRUC_TIEP = 1;
        int CHI_PHI_NHAN_CONG = 2;
        int CHI_PHI_NGUYEN_VAT_LIEU = 3;
        int CHI_PHI_DUNG_CU_SAN_XUAT = 4;
        int CHI_PHI_KHAU_HAO_MAY = 5;
        int CHI_PHI_DICH_VU_MUA_NGOAI = 6;
        int CHI_PHI_BANG_TIEN_KHAC = 7;
        int PHI_PHI_NHAN_VIEN_PHAN_XUONG = 8;
        int CHI_PHI_NGUYEN_VAT_LIEU_CHUNG = 9;
        int CHI_PHI_DUNG_CU_SAN_XUAT_CHUNG = 10;
        int CHI_PHI_KHAU_HAO_MAY_CHUNG = 11;
        int CHI_PHI_DICH_VU_MUA_NGOAI_CHUNG = 12;
        int CHI_PHI_BANG_TIEN_MAT_KHAC = 13;
    }

    interface TypeDungChungRieng {
        String DUNG_CHUNG = "0";
        String DUNG_RIENG = "1";
    }

    interface OrgCode {
        String Ma_Cong_Ty = "ORG_CODE_";
    }

    interface TCKHAC_SDSOQUANTRI {
        String NOT_USE = "0";
        String USE = "1";
    }

    interface MaterialGoodsType {
        Integer SERVICE = 2;
        Integer DIFF = 4;
    }

    interface ResetNoVoucher {
        Integer Fail = 0;
        Integer Duplicate = 1;
        Integer Success = 2;
    }

    interface searchVoucher {
        int LoaiChungTu = 1;
        int SoChungTu = 2;
        int SoHoaDon = 3;
        int NgayHachToan = 4;
        int NgayHoaDon = 5;
        int LoaiTien = 6;
        int THNo = 7;
        int TKCo = 8;
        int TKNganHang = 9;
        int DoiTuong = 10;
        int VTHH = 11;
        int Kho = 12;
        int DoiTuongTHVP = 13;
        int KhoanMucCP = 14;
        int MaThongKe = 15;
        int MucThuChi = 16;
    }
}
