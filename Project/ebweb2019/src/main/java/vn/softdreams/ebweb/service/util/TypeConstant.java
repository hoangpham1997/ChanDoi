package vn.softdreams.ebweb.service.util;

import java.util.Arrays;
import java.util.List;

public interface TypeConstant {
    int TYPE_BAO_NO_UNC = 120;
    int TYPE_BAO_NO_NCC = 126;
    int TYPE_BAO_NO_SCK = 130;
    int TYPE_BAO_NO_STM = 140;
    int XUAT_HOA_DON = 350;
    int XUAT_HOA_DON_TRA_LAI = 352;
    int XUAT_HOA_DON_BH = 351;
    int XUAT_HOA_DON_HBGG = 353;
    int HANG_BAN_TRA_LAI = 330;
    int HANG_GIAM_GIA = 340;
    int DON_MUA_HANG = 200;
    int DON_DAT_HANG = 310;
    int NHAP_KHO = 400;
    int NHAP_KHO_TU_DIEU_CHINH = 401;
    int NHAP_KHO_TU_MUA_HANG = 402;
    int NHAP_KHO_TU_BAN_HANG_TRA_LAI = 403;
    int XUAT_KHO = 410;
    int XUAT_KHO_TU_BAN_HANG = 411;
    int XUAT_KHO_TU_HANG_MUA_TRA_LAI = 412;
    int XUAT_KHO_TU_DIEU_CHINH = 413;
    int KIEM_XUAT_KHO = 412;
    int CHUYEN_KHO = 420;
    int CHUYEN_KHO_GUI_DAI_LY = 421;
    int CHUYEN_KHO_CHUYEN_NOI_BO = 422;
    int MUA_HANG_TRA_LAI = 220;
    int MUA_HANG_GIAM_GIA = 230;
    int PHAN_BO_CCDC = 560;

    int MUA_HANG_TRA_LAI_GC = 22;
    int RS_INWARD_OUTWARD = 403;
    int PP_SERVICE = 240;
    int MUA_HANG_QUA_KHO = 210;
    int MUA_HANG_CHUA_THANH_TOAN = 210;
    int BAN_HANG_CHUA_THU_TIEN = 320;
    int BAN_HANG_THU_TIEN_NGAY_TM = 321;
    int BAN_HANG_THU_TIEN_NGAY_CK = 322;
    int BANG_KE_HANG_HOA_DICH_VU = 323;
    int KET_CHUYEN_LAI_LO = 702;
    int GHI_TANG_CCDC = 520;
    int PHAN_BO_CHI_PHI_TRA_TRUOC = 709;
    int OPENING_BLANCE = 740;
    int OPENING_BLANCE_MT = 741;
    int OPENING_BLANCE_AC = 742;
    int PHIEU_CHI_TRA_TIEN_NHA_CUNG_CAP = 116;
    int PHIEU_CHI_MUA_HANG = 115;
    int PHIEU_CHI_MUA_DICH_VU = 114;
    int PHIEU_THU = 100;
    int PHIEU_CHI = 110;
    int PHIEU_THU_TIEN_KHACH_HANG = 101;
    int PHIEU_THU_TU_BAN_HANG = 102;
    int BAO_NO_UNC_TRA_TIEN_NCC = 126;
    int BAO_NO_SCK_TRA_TIEN_NCC = 134;
    int BAO_NO_STM_TRA_TIEN_NCC = 144;
    int NOP_TIEN_TU_TAI_KHOAN = 160;
    int NOP_TIEN_TU_KHACH_HANG = 161;
    int NOP_TIEN_TU_BAN_HANG = 162;
    int THE_TIN_DUNG = 170;
    int BAO_CO = 160;
    int BAO_NO = 120;
    int THE_TIN_DUNG_MUA_HANG = 171;
    int THE_TIN_DUNG_MUA_TSCD = 172;
    int THE_TIN_DUNG_MUA_DICH_VU = 173;
    int THE_TIN_DUNG_TRA_TIEN_NCC = 174;
    int THE_TIN_DUNG_MUA_CCDC = 175;
    int KIEM_KE_QUY = 180;
    int BAO_GIA = 300;
    int THONG_BAO_PHAT_HANH_HDDT = 820;
    int CHUNG_TU_NGHIEP_VU_KHAC = 700;
    int CHUNG_TU_NGHIEP_VU_KHAC_NT = 703;
    int KHOI_TAO_MAU_HOA_DON = 800;
    int DANG_KY_SU_DUNG_HOA_DON = 810;
    int THONG_BAO_PHAT_HANH_HOA_DON = 820;
    int KIEM_KE_CCDC = 570;
    List<Integer> LIST_TYPE_BAO_CO = Arrays.asList(160, 161, 162);
    int GHI_GIAM_CCDC = 530;
    int DIEU_CHINH_CCDC = 550;
    int DIEU_CHUYEN_CCDC = 540;
    int TSCD_GHI_TANG = 620;
    int KET_CHUYEN_CHI_PHI = 701;

    interface BAO_CAO {
        interface TONG_HOP {
            String SO_NHAT_KY_CHUNG = "SO_NHAT_KY_CHUNG";
            String SO_CAI_HT_NHAT_KY_CHUNG = "SO_CAI_HT_NHAT_KY_CHUNG";
            String SO_CHI_TIET_CAC_TAI_KHOAN = "SO_CHI_TIET_CAC_TAI_KHOAN";
            String PHAN_BO_CHI_PHI_TRA_TRUOC = "PHAN_BO_CHI_PHI_TRA_TRUOC";
            String SO_THEO_DOI_MA_THONG_KE_THEO_TAI_KHOAN = "SO_THEO_DOI_MA_THONG_KE_THEO_TAI_KHOAN";
            String SO_THEO_DOI_MA_THONG_KE_THEO_KHOAN_MUC_CHI_PHI = "SO_THEO_DOI_MA_THONG_KE_THEO_KHOAN_MUC_CHI_PHI";
            String TONG_HOP_CHI_PHI_THEO_KHOAN_MUC_CHI_PHI = "TONG_HOP_CHI_PHI_THEO_KHOAN_MUC_CHI_PHI";
            String SO_THEO_DOI_THANH_TOAN_BANG_NGOAI_TE = "SO_THEO_DOI_THANH_TOAN_BANG_NGOAI_TE";
        }

        interface GIA_THANH {
            String SO_THEO_DOI_DOI_TUONG_THCP_THEO_TAI_KHOAN = "SO_THEO_DOI_DOI_TUONG_THCP_THEO_TAI_KHOAN";
            String SO_THEO_DOI_DOI_TUONG_THCP_THEO_KHOAN_MUC_CHI_PHI = "SO_THEO_DOI_DOI_TUONG_THCP_THEO_KHOAN_MUC_CHI_PHI";
            String SO_CHI_PHI_SAN_XUAT_KINH_DOANH = "SO_CHI_PHI_SAN_XUAT_KINH_DOANH";
            String THE_TINH_GIA_THANH = "THE_TINH_GIA_THANH";
        }

        interface BAO_CAO_TAI_CHINH {
            String BANG_CAN_DOI_KE_TOAN = "BANG_CAN_DOI_KE_TOAN";
            String BANG_CAN_DOI_TAI_KHOAN = "BANG_CAN_DOI_TAI_KHOAN";
            String KET_QUA_HOAT_DONG_KINH_DOANH = "KET_QUA_HOAT_DONG_KINH_DOANH";
            String LUU_CHUYEN_TIEN_TE_TT = "LUU_CHUYEN_TIEN_TE_TT";
            String LUU_CHUYEN_TIEN_TE_GT = "LUU_CHUYEN_TIEN_TE_GT";
            String THUYET_MINH_BAO_CAO_TAI_CHINH = "THUYET_MINH_BAO_CAO_TAI_CHINH";
        }

        interface KHO {
            String SO_CHI_TIET_VAT_LIEU = "SO_CHI_TIET_VAT_LIEU";
            String THE_KHO = "THE_KHO";
            String TONG_HOP_TON_KHO = "TONG_HOP_TON_KHO";
            String TONG_HOP_CHI_TIET_VAT_LIEU = "TONG_HOP_CHI_TIET_VAT_LIEU";
        }

        interface QUY {
            String SO_KE_TOAN_CHI_TIET_QUY_TIEN_MAT = "SO_KE_TOAN_CHI_TIET_QUY_TIEN_MAT";
            String SO_QUY_TIEN_MAT = "SO_QUY_TIEN_MAT";
        }

        interface NGAN_HANG {
            String SO_TIEN_GUI_NGAN_HANG = "SO_TIEN_GUI_NGAN_HANG";
            String BANG_KE_SO_DU_NGAN_HANG = "BANG_KE_SO_DU_NGAN_HANG";
        }
        interface MUA_HANG {
            String SO_NHAT_KY_MUA_HANG = "SO_NHAT_KY_MUA_HANG";
            String SO_CHI_TIET_MUA_HANG = "SO_CHI_TIET_MUA_HANG";
            String TONG_HOP_CONG_NO_PHAI_TRA = "TONG_HOP_CONG_NO_PHAI_TRA";
            String CHI_TIET_CONG_NO_PHAI_TRA = "CHI_TIET_CONG_NO_PHAI_TRA";
        }

        interface HDDT {
            String BAO_CAO_TINH_HINH_SU_DUNG_HD = "BAO_CAO_TINH_HINH_SU_DUNG_HD";
            String BANG_KE_HD_HHDV = "BANG_KE_HD_HHDV";
            String BAO_CAO_DOANH_THU_THEO_SP = "BAO_CAO_DOANH_THU_THEO_SP";
            String BAO_CAO_DOANH_THU_THEO_BEN_MUA = "BAO_CAO_DOANH_THU_THEO_BEN_MUA";
        }
        interface BAN_HANG {
            String SO_CHI_TIET_BAN_HANG = "SO_CHI_TIET_BAN_HANG";
            String SO_NHAT_KY_BAN_HANG = "SO_NHAT_KY_BAN_HANG";
            String TONG_HOP_CONG_NO_PHAI_THU = "TONG_HOP_CONG_NO_PHAI_THU";
            String CHI_TIET_CONG_NO_PHAI_THU = "CHI_TIET_CONG_NO_PHAI_THU";
            String SO_THEO_DOI_LAI_LO_THEO_HOA_DON = "SO_THEO_DOI_LAI_LO_THEO_HOA_DON";
            String SO_THEO_DOI_CONG_NO_PHAI_THU_THEO_HOA_DON = "SO_THEO_DOI_CONG_NO_PHAI_THU_THEO_HOA_DON";

        }
        interface CCDC {
            String SO_CONG_CU_DUNG_CU = "SO_CONG_CU_DUNG_CU";
            String SO_THEO_DOI_CCDC_TAI_NOI_SU_DUNG = "SO_THEO_DOI_CCDC_TAI_NOI_SU_DUNG";
        }
        String SO_NHAT_KY_THU_TIEN = "SO_NHAT_KY_THU_TIEN";
        String SO_NHAT_KY_CHI_TIEN = "SO_NHAT_KY_CHI_TIEN";
        String BANG_KE_BAN_RA = "BANG_KE_BAN_RA";
        String BANG_KE_MUA_VAO = "BANG_KE_MUA_VAO";
    }

    interface LOAI_TAI_KHOAN {
        int TK_DU_NO = 0;
        int TK_DU_CO = 1;
        int TK_LUONG_TINH = 2;
        int TK_KHONG_CO_SO_DU = 3;
    }
    interface ACCOUNT_DETAIL_TYPE {
        String ACCOUNT_DEBIT = "6";
        String ACCOUNT_SUPPLIER = "0";
        String ACCOUNT_CUSTOMER = "1";
        String ACCOUNT_EMPLOYEE = "2";
        String ACCOUNT_BANK = "5";
    }

    interface DESCRIPTION_VAT {
        String EXPORT_TAX = "Thuế XK ";
    }
}

