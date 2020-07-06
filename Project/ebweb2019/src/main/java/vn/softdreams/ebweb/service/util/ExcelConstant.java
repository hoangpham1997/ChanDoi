package vn.softdreams.ebweb.service.util;

import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.dto.UserSystemOption;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface ExcelConstant {
    interface Unit {
        String NAME = "Tên";
        String DESCRIPTION = "Mô tả";
    }

    interface UnitField {
        String NAME = "unitName";
        String DESCRIPTION = "unitDescription";
    }

    interface Header {
        List<String> XUAT_HOA_DON = Arrays.asList("MaHD (*)", "VaoSo (*)", "HinhThucHD (*)", "LoaiHD (*)", "MauHD (*)", "KyHieuHD (*)", "SoHD",
            "NgayHD (*)", "HinhthucTT (*)", "LoaiTien (*)", "TyGia (*)", "TrangThaiHD", "MaDoiTuong (*)", "TenDoiTuong", "DiaChi", "MST",
            "TenNguoiLienHe", "DienGiai", "SoTK", "TenNganHang");
        List<String> XUAT_HOA_DON_REQUIRED = Arrays.asList("MaHD (*)", "VaoSo (*)", "HinhThucHD (*)", "LoaiHD (*)", "MauHD (*)", "KyHieuHD (*)", "SoHD",
            "NgayHD (*)", "HinhthucTT (*)", "LoaiTien (*)", "TyGia (*)", "TrangThaiHD");
        List<String> XUAT_HOA_DON_DETAIL = Arrays.asList("MaHang (*)", "TenHang", "DVT", "SoLuong", "DonGiaNT", "DonGiaQD",
            "ThanhTienNT", "ThanhTienQĐ", "TyLeCK", "TienCKNT", "TienCKQD", "ThueSuat", "TienThueNT", "TienThueQD", "SoLo", "HanDung");
        List<String> XUAT_HOA_DON_DETAIL_REQUIRED = Arrays.asList("MaHang (*)");


        List<String> OP_ACCOUNT_NORMAL = Arrays.asList("Số TK", "Loại tiền", "Tỷ giá", "Dư Nợ", "Dư Nợ quy đổi", "Dư Có", "Dư Có quy đổi",
                "TK ngân hàng", "Khoản mục CP", "Đối tượng THCP",
//            "Hợp đồng",
            "Mục thu/chi", "Phòng ban", "Mã thống kê");
        List<String> OP_ACCOUNT_NORMAL_REQUIRED = Arrays.asList("Số TK", "Loại tiền");
        List<String> OP_ACCOUNT_OBJECT = Arrays.asList("Mã Đối tượng", "Loại tiền", "Tỷ giá", "Dư Nợ", "Dư Nợ quy đổi", "Dư Có", "Dư Có quy đổi",
                "TK ngân hàng", "Khoản mục CP",  "Đối tượng THCP",
//            "Hợp đồng",
            "Mục thu/chi", "Phòng ban", "Mã thống kê", "Số TK");
        List<String> OP_ACCOUNT_OBJECT_REQUIRED  = Arrays.asList("Mã Đối tượng", "Loại tiền", "Số TK");
        List<String> OP_MATERIAL_GOODS = Arrays.asList("Mã kho", "Mã hàng", "Đơn vị tính", "Số lượng", "Đơn giá", "Thành tiền", "Số lô", "Hạn dùng",
                "TK ngân hàng", "Khoản mục CP", "Đối tượng THCP",
//            "Hợp đồng",
            "Mục thu/chi", "Phòng ban", "Mã thống kê", "Số TK");
        List<String> OP_MATERIAL_GOODS_REQUIRED = Arrays.asList("Mã kho", "Mã hàng", "Số TK");


        List<String> DANH_MUC_KH = Arrays.asList("AccountingObjectCode","AccountingObjectName","ScaleType",
                "ObjectType","Tel","TaxCode",
                "Email","Website","Fax",
                "MaximizaDebtAmount","DueTime","PaymentClauseCode",
                "AccountingObjectGroupCode","ContactName","ContactTitle",
                "ContactSex","ContactAddress","ContactMobile",
                "ContactEmail","ContactHomeTel","ContactOfficeTel",
                "IdentificationNo","IssueDate","IssueBy",
                "BankAccount","BankName","BankBranchName","AccountHolderName");
    }

    interface SaBill {
        String NAME = "Xuất hóa đơn";
        List<String> HEADER = Arrays.asList("Ngày hóa đơn", "Mẫu số hóa đơn", "Ký hiệu hóa đơn", "Số hóa đơn",
            "Đối tượng", "Tổng tiền hàng", "Tổng CK", "Tổng thuế GTGT", "Tổng tiền TT");
        List<String> FIELD = Arrays.asList("invoiceDate", "invoiceTemplate", "invoiceSeries", "invoiceNo",
            "accountingObjectName", "totalAmount", "totalDiscountAmount", "totalVATAmount", "totalAmount+totalVATAmount");
    }

    interface OpeningBalance {
        String NAME = "Số dư đầu kỳ";
        List<String> HEADER = Arrays.asList("Số tài khoản", "Tên tài khoản", "Dư nợ", "Dư có");
        List<String> FIELD = Arrays.asList("accountNumber", "accountName", "debitAmountOriginal", "creditAmountOriginal");
    }

    interface OPAccountNormal {
        String NAME = "Số dư đầu kỳ";
        List<String> HEADER = Arrays.asList("Số tài khoản", "Tên tài khoản", "Dư nợ nguyên tệ", "Dư có nguyên tệ", "TK ngân hàng",
                "Khoản mục CP", "Đối tượng THCP", "Hợp đồng", "Mục thu/chi", "Phòng ban", "Mã thống kê");
        List<String> FIELD = Arrays.asList("accountNumber", "accountName", "debitAmountOriginal", "creditAmountOriginal",
                "bankAccount", "expenseItemCode", "costSetCode", "noBookContract", "budgetItemCode",
                "organizationUnitCode", "statisticsCode");
    }

    interface OPAccountNormalForeignCurrency {
        String NAME = "Số dư đầu kỳ";
        List<String> HEADER = Arrays.asList("Số tài khoản", "Tên tài khoản", "Dư nợ nguyên tệ", "Dư nợ", "Dư có nguyên tệ", "Dư có", "Tỷ giá", "TK ngân hàng",
                "Khoản mục CP", "Đối tượng THCP", "Hợp đồng", "Mục thu/chi", "Phòng ban", "Mã thống kê");
        List<String> FIELD = Arrays.asList("accountNumber", "accountName", "debitAmountOriginal", "debitAmount", "creditAmountOriginal", "creditAmount", "exchangeRate",
                "bankAccount", "expenseItemCode", "costSetCode", "noBookContract", "budgetItemCode",
                "organizationUnitCode", "statisticsCode");
    }

    interface OPAccountAccountingObject {
        String NAME = "Số dư đầu kỳ";
        List<String> HEADER = Arrays.asList("Mã đối tượng", "Tên đối tượng", "Dư nợ nguyên tệ", "Dư có nguyên tệ", "TK ngân hàng",
                "Khoản mục CP", "Đối tượng THCP", "Hợp đồng", "Mục thu/chi", "Phòng ban", "Mã thống kê");
        List<String> FIELD = Arrays.asList("accountingObjectCode", "accountingObjectName", "debitAmountOriginal", "creditAmountOriginal",
                "bankAccount", "expenseItemCode", "costSetCode", "noBookContract", "budgetItemCode",
                "organizationUnitCode", "statisticsCode");
    }

    interface OPAccountAccountingObjectForeignCurrency {
        String NAME = "Số dư đầu kỳ";
        List<String> HEADER = Arrays.asList("Mã đối tượng", "Tên đối tượng", "Dư nợ nguyên tệ", "Dư nợ", "Dư có nguyên tệ", "Dư có", "Tỷ giá", "TK ngân hàng",
                "Khoản mục CP", "Đối tượng THCP", "Hợp đồng", "Mục thu/chi", "Phòng ban", "Mã thống kê");
        List<String> FIELD = Arrays.asList("accountingObjectCode", "accountingObjectName", "debitAmountOriginal", "debitAmount", "creditAmountOriginal", "creditAmount", "exchangeRate",
                "bankAccount", "expenseItemCode", "costSetCode", "noBookContract", "budgetItemCode",
                "organizationUnitCode", "statisticsCode");
    }

    interface OPMaterialGoods {
        String NAME = "Số dư đầu kỳ";
        List<String> HEADER = Arrays.asList("Mã VTHH", "Tên VTHH", "ĐVT", "Số lượng", "Đơn giá", "Thành tiền",
                "Số lô", "Hạn dùng",
                "TK ngân hàng", "Khoản mục CP", "Đối tượng THCP", "Hợp đồng", "Mục thu/chi", "Phòng ban", "Mã thống kê");
        List<String> FIELD = Arrays.asList("materialGoodsCode", "materialGoodsName", "unitName", "quantity",
                "unitPriceOriginal", "amountOriginal", "lotNo", "expiryDateStr",
                "bankAccount", "expenseItemCode", "costSetCode", "noBookContract", "budgetItemCode",
                "organizationUnitCode", "statisticsCode");
    }

    interface PPOrder {
        String NAME = "Đơn mua hàng";
        List<String> HEADER = Arrays.asList("Ngày đơn hàng", "Số đơn hàng", "Đối tượng", "Diễn giải", "Ngày nhận hàng",
            "Tổng giá trị", "Trạng thái");
        List<String> FIELD = Arrays.asList("date", "no", "accountingObjectName", "reason", "deliverDate",
            "total", "status");
    }
    interface RSInwardOutward {
        String NAME = "Nhập kho";
        List<String> HEADER = Arrays.asList("Ngày chứng từ", "Ngày hạch toán", "Số chứng từ", "Đối tượng", "Loại chứng từ", "Diễn giải", "Tổng giá trị");
        List<String> FIELD_NOFBOOK = Arrays.asList("date", "postedDate", "noFBook", "accountingObjectName", "typeName", "reason", "totalAmount");
        List<String> FIELD_NOMBOOK = Arrays.asList("date", "postedDate", "noMBook", "accountingObjectName", "typeName", "reason", "totalAmount");
    }

    interface RSTransfer {
        String NAME = "Chuyển kho";
        List<String> HEADER = Arrays.asList("Ngày chứng từ", "Ngày hạch toán", "Số chứng từ", "Số hóa đơn", "Loại chứng từ", "Đối tượng", "Người vận chuyển","Diễn giải", "Tổng giá trị");
        List<String> FIELD_NOFBOOK = Arrays.asList("date", "postedDate", "noFBook", "invoiceNo", "typeName", "accountingObjectName", "employeeName", "reason", "totalAmount");
        List<String> FIELD_NOMBOOK = Arrays.asList("date", "postedDate", "noMBook", "invoiceNo", "typeName", "accountingObjectName", "employeeName", "reason", "totalAmount");
    }

    interface IAReport {
        String NAME = "Khởi tạo mẫu hóa đơn";
        List<String> HEADER = Arrays.asList("Tên mẫu hóa đơn", "Hình thức hóa đơn", "Loại hóa đơn", "Mẫu số hóa đơn",
            "Ký hiệu hóa đơn");
        List<String> FIELD = Arrays.asList("reportName", "invoiceForm", "invoiceType", "invoiceTemplate", "invoiceSeries");
    }

    interface IAPublishInvoice {
        String NAME = "Thông báo phát hành hóa đơn";
        List<String> HEADER = Arrays.asList("Ngày lập", "Số thông báo", "Cơ quan thuế", "Người đại diện phát luật", "Trạng thái");
        List<String> FIELD = Arrays.asList("date", "no", "receiptedTaxOffical", "representationInLaw", "status");
    }

    interface IARegisterInvoice {
        String NAME = "Đăng ký sử dụng hóa đơn";
        List<String> HEADER = Arrays.asList("Ngày đăng ký", "Số đăng ký", "Nội dung", "Người ký", "Trạng thái");
        List<String> FIELD = Arrays.asList("date", "no", "description", "signer", "status");
    }

    interface PPService {
        String NAME = "Mua dịch vụ";
        List<String> HEADER = Arrays.asList("Ngày chứng từ", "Ngày hoạch toán", "Số chứng từ", "Số HĐ",
            "Loại chứng từ", "Đối tượng", "Tổng tiền DV", "Tổng tiền CK", "Tổng thuế GTGT", "Tổng tiền TT", "Đã lập hóa đơn");
        List<String> FIELD = Arrays.asList("receiptDate", "postedDate", "noBook", "invoiceNo",
            "receiptType", "accountingObjectName", "totalAmountOriginal", "totalDiscountAmountOriginal", "totalVATAmountOriginal", "resultAmount", "billReceivedStr");
    }

    interface PPDiscountReturn {
        String NAME = "Mua hàng trả lại";
        List<String> HEADER = Arrays.asList("Ngày chứng từ", "Ngày hạch toán", "Số chứng từ",
            "Đối tượng", "Diễn giải", "Tổng tiền hàng", "Tổng CK", "Tổng thuế GTGT", "Tổng tiền TT");
        List<String> FIELD = Arrays.asList("date", "postedDate", "NoBook", "accountingObjectName",
            "accountingObjectAddress", "reason", "totalAmount", "totalVATAmount", "totalAmount+totalVATAmount");
    }

    interface PPDiscountPurchase {
        String NAME = "Mua hàng giảm giá";
        List<String> HEADER = Arrays.asList("Ngày chứng từ", "Ngày hạch toán", "Số chứng từ",
            "Đối tượng", "Diễn giải", "Tổng tiền hàng", "Tổng CK", "Tổng thuế GTGT", "Tổng tiền TT");
        List<String> FIELD = Arrays.asList("date", "postedDate", "NoBook", "accountingObjectName",
            "accountingObjectAddress", "reason", "totalAmount", "totalVATAmount", "totalAmount+totalVATAmount");
    }

    interface MBDeposit {
        String NAME = "Báo có";
        List<String> HEADER = Arrays.asList("Ngày chứng từ", "Ngày hạch toán", "Số chứng từ", "Loại chứng từ", "Đối tượng nộp", "Diễn giải", "Tổng tiền thanh toán");
        List<String> FIELD = Arrays.asList("date", "postedDate", "noBook", "typeIDInWord", "accountingObjectName", "description", "totalAmount");
    }

    interface MBCreditCard {
        String NAME = "Thẻ tín dụng";
        List<String> HEADER = Arrays.asList("Ngày chứng từ", "Ngày hạch toán", "Số chứng từ", "Loại chứng từ", "Số thẻ", "Đối tượng nhận", "Diễn giải", "Tổng tiền thanh toán");
        List<String> FIELD = Arrays.asList("date", "postedDate", "noBook", "typeIDInWord", "creditCardNumber", "accountingObjectName", "description", "totalAmount");
    }

    interface GOtherVoucher {
        String NAME = "Chứng từ nghiệp vụ khác";
        List<String> HEADER = Arrays.asList("Ngày chứng từ", "Ngày hạch toán", "Số chứng từ", "Loại chứng từ", "Diễn giải", "Tổng tiền thanh toán");
        List<String> FIELD = Arrays.asList("date", "postedDate", "noBook", "typeIDInWord", "description", "totalAmount");
    }

    interface GOtherVoucherPB {
        String NAME = "Phân bổ chi phí trả trước";
        List<String> HEADER = Arrays.asList("Ngày chứng từ", "Ngày hạch toán", "Số chứng từ", "Diễn giải", "Tổng tiền thanh toán");
        List<String> FIELD = Arrays.asList("date", "postedDate", "noBook", "reason", "totalAmountOriginal");
    }

    interface SaReturn {
        String NAME = "Hàng bán trả lại";
        String NAME2 = "Hàng bán giảm giá";
        List<String> HEADER = Arrays.asList("Ngày chứng từ", "Ngày hạch toán", "Số chứng từ", "Khách hàng",
            "Diễn giải", "Tổng tiền hàng", "Tổng tiền GTGT", "Tổng tiền TT");
        List<String> FIELD = Arrays.asList("date", "postedDate", "noMBook", "accountingObjectName",
            "reason", "totalAmount", "totalVATAmount", "totalPaymentAmount");
    }

    interface MBTellerPaper {
        String NAME = "Báo Nợ";
        List<String> HEADER = Arrays.asList("Ngày chứng từ", "Ngày hạch toán", "Số chứng từ", "Loại chứng từ", "Đối tượng nhận", "Diễn giải", "Tổng tiền thanh toán");
        List<String> FIELD = Arrays.asList("date", "postedDate", "noFBook", "typeName", "accountingObjectName", "reason", "totalAmount");
    }

    interface SAInvoice2 {
        String NAME = "Chứng từ bán hàng";
        List<String> HEADER = Arrays.asList("Ngày chứng từ", "Ngày hạch toán", "Số chứng từ", "Số hóa đơn", "Loại chứng từ",
            "Đối tượng", "Diễn giải", "Tổng tiền hàng", "Tổng CK", "Tổng thuế GTGT", "Tổng tiền TT");
        List<String> FIELD = Arrays.asList("date", "postedDate", "noBook", "invoiceNo", "typeName", "accountingObjectName",
             "reason", "totalAmount", "totalDiscountAmount", "totalVATAmount", "totalAllAmount");
    }

    interface MCAudit {
        String NAME = "Kiểm kê quỹ";
        List<String> HEADER = Arrays.asList("Ngày chứng từ", "Số chứng từ", "Ngày kiểm kê", "Loại tiền",
            "Diễn giải", "Kết quả");
        List<String> FIELD = Arrays.asList("date", "no", "auditDate", "currencyID", "description",
            "summary");
    }

    interface SAQuote {
        String NAME = "Báo Giá";
        List<String> HEADER = Arrays.asList("Ngày báo giá", "Số báo giá", "Hiệu lực đến", "Đối tượng", "Diễn giải", "Tổng giá trị");
        List<String> FIELD = Arrays.asList("date", "no", "finalDate", "accountingObjectName", "reason", "totalAmount+totalVATAmount-totalDiscountAmount");
    }

    interface PPInvoice {
        String NAME = "Mua Hàng Qua Kho";
        List<String> HEADER = Arrays.asList("Ngày chứng từ", "Ngày hạch toán", "Số chứng từ", "Số HĐ", "Loại chứng từ", "Đối tượng", "Tổng tiền hàng", "Tổng tiền chiết khấu", "Tổng thuế GTGT", "Tổng tiền thanh toán", "TT nhận hóa đơn");
        List<String> FIELD = Arrays.asList("date", "postedDate", "no", "ppOrderNo", "typeIdStr", "accountingObjectName", "totalAmount", "totalDiscountAmount", "totalVATAmount", "amountTT", "billReceivedStr");
    }

    interface FAIncrement {
        String NAME = "Ghi tăng TSCD";
        List<String> HEADER = Arrays.asList("Ngày chứng từ", "Số chứng từ", "Tổng tiền");
        List<String> FIELD = Arrays.asList("date", "noBook", "totalAmount");
    }

    interface GOtherVoucherKc {
        String NAME = "Kết chuyễn lãi lỗ";
        List<String> HEADER = Arrays.asList("Ngày chứng từ", "Ngày hạch toán", "Số chứng từ",  "Diễn giải", "Số tiền");
        List<String> FIELD = Arrays.asList("date", "postedDate", "no", "reason", "totalAmount");
    }

    interface SaOrder {
        String NAME = "Đơn đặt hàng";
        List<String> HEADER = Arrays.asList("Ngày đơn hàng", "Số đơn hàng", "Ngày giao hàng", "Khách hàng",
            "Diễn giải", "Tổng giá trị", "Trạng thái");
        List<String> FIELD = Arrays.asList("date", "no", "deliverDate", "accountingObjectName",
            "reason", "totalAmount", "statusString");
    }

    interface MCReceipt {
        String NAME = "Phiếu thu";
        List<String> HEADER = Arrays.asList("Ngày chứng từ", "Ngày hạch toán", "Số chứng từ", "Loại chứng từ",
            "Đối tượng", "Lý do nộp", "Tổng tiền TT");
        List<String> FIELD_NoFBook = Arrays.asList("date", "postedDate", "noFBook", "typeName",
            "accountingObjectName", "reason", "totalAmount");
        List<String> FIELD_NoMBook = Arrays.asList("date", "postedDate", "noMBook", "typeName",
            "accountingObjectName", "reason", "totalAmount");
    }

    interface MCPayment {
        String NAME = "Phiếu chi";
        List<String> HEADER = Arrays.asList("Ngày chứng từ", "Ngày hạch toán", "Số chứng từ", "Loại chứng từ",
            "Đối tượng", "Lý do chi", "Tổng tiền TT");
        List<String> FIELD_NoFBook = Arrays.asList("date", "postedDate", "noFBook", "typeName",
            "accountingObjectName", "reason", "totalAmount");
        List<String> FIELD_NoMBook = Arrays.asList("date", "postedDate", "noMBook", "typeName",
            "accountingObjectName", "reason", "totalAmount");
    }

    interface CPExpenseTranfer {
        String NAME = "Kết chuyển chi phí";
        List<String> HEADER = Arrays.asList("Ngày chứng từ", "Ngày hạch toán", "Số chứng từ", "Loại chứng từ", "Diễn giải", "Tổng tiền thanh toán");
        List<String> FIELD = Arrays.asList("date", "postedDate", "noBook", "typeIDInWord", "description", "totalAmount");
    }

    interface GiaThanh1 {
        String NAME = "Giá thành";
        List<String> HEADER = Arrays.asList("Từ ngày", "Đến ngày", "Tên kỳ tính giá thành", "Dở dang đầu kỳ", "Chi phí phát sinh trong kỳ", "Dở giang cuối kỳ", "Tổng giá thành");
        List<String> FIELD = Arrays.asList("fromDate", "toDate", "name", "totalIncompleteOpenning", "totalAmountInPeriod", "totalIncompleteClosing", "totalCost");
    }

    interface GiaThanh2 {
        String NAME = "Giá thành";
        List<String> HEADER = Arrays.asList("Từ ngày", "Đến ngày", "Tên kỳ tính giá thành", "Lũy kế phát sinh kỳ trước", "Chi phí phát sinh trong kỳ", "Số chưa nghiệm thu", "Số tiền nghiệm thu");
        List<String> FIELD = Arrays.asList("fromDate", "toDate", "name", "totalIncompleteOpenning", "totalAmountInPeriod", "totalIncompleteClosing", "totalCost");
    }

    interface KeyCodeDanhMucKH {
        String AccountingObjectCode = "AccountingObjectCode";
        String AccountingObjectName = "AccountingObjectName";
        String ScaleType = "ScaleType";
        String ObjectType = "ObjectType";
        String Tel = "Tel";
        String AccountingObjectAddress = "AccountingObjectAddress";
        String TaxCode = "TaxCode";
        String Email = "Email";
        String Website = "Website";
        String Fax = "Fax";
        String MaximizaDebtAmount = "MaximizaDebtAmount";
        String DueTime = "DueTime";
        String PaymentClauseCode = "PaymentClauseCode";
        String AccountingObjectGroupCode = "AccountingObjectGroupCode";
        String ContactName = "ContactName";
        String ContactTitle = "ContactTitle";
        String ContactSex = "ContactSex";
        String ContactAddress = "ContactAddress";
        String ContactMobile = "ContactMobile";
        String ContactEmail = "ContactEmail";
        String ContactHomeTel = "ContactHomeTel";
        String ContactOfficeTel = "ContactOfficeTel";
        String IdentificationNo = "IdentificationNo";
        String IssueDate = "IssueDate";
        String IssueBy = "IssueBy";
        String BankAccount = "BankAccount";
        String BankName = "BankName";
        String BankBranchName = "BankBranchName";
        String AccountHolderName = "AccountHolderName";
    }

    interface KeyCodeDanhMucNCC {
        String AccountingObjectCode = "AccountingObjectCode";
        String AccountingObjectName = "AccountingObjectName";
        String ScaleType = "ScaleType";
        String ObjectType = "ObjectType";
        String Tel = "Tel";
        String AccountingObjectAddress = "AccountingObjectAddress";
        String TaxCode = "TaxCode";
        String Email = "Email";
        String Website = "Website";
        String Fax = "Fax";
        String AccountingObjectGroupCode = "AccountingObjectGroupCode";
        String ContactName = "ContactName";
        String ContactTitle = "ContactTitle";
        String ContactSex = "ContactSex";
        String ContactAddress = "ContactAddress";
        String ContactMobile = "ContactMobile";
        String ContactEmail = "ContactEmail";
        String ContactHomeTel = "ContactHomeTel";
        String ContactOfficeTel = "ContactOfficeTel";
        String IdentificationNo = "IdentificationNo";
        String IssueDate = "IssueDate";
        String IssueBy = "IssueBy";
        String BankAccount = "BankAccount";
        String BankName = "BankName";
        String BankBranchName = "BankBranchName";
        String AccountHolderName = "AccountHolderName";
    }
    interface KeyCodeDanhMucNV {
        String AccountingObjectCode = "AccountingObjectCode";
        String AccountingObjectName = "AccountingObjectName";
        String ContactTitle = "ContactTitle";
        String DepartmentCode = "DepartmentCode";
        String EmployeeBirthday = "EmployeeBirthday";
        String ContactSex = "ContactSex";
        String TaxCode = "TaxCode";
        String NumberOfDependent = "NumberOfDependent";
        String IdentificationNo = "IdentificationNo";
        String IssueDate = "IssueDate";
        String IssueBy = "IssueBy";
        String AgreementSalary = "AgreementSalary";
        String InsuranceSalary = "InsuranceSalary";
        String SalaryCoefficient = "SalaryCoefficient";
        String ObjectType = "ObjectType";
        String IsUnofficialStaff = "IsUnofficialStaff";
        String AccountingObjectAddress = "AccountingObjectAddress";
        String ContactHomeTel = "ContactHomeTel";
        String Tel = "Tel";
        String Email = "Email";
        String BankAccount = "BankAccount";
        String BankName = "BankName";
        String BankBranchName = "BankBranchName";
        String AccountHolderName = "AccountHolderName";
    }

    interface KeyCodeDanhMucVTHH {
        String MaterialGoodsCode = "MaterialGoodsCode";
        String MaterialGoodsName = "MaterialGoodsName";
        String MaterialGoodsType = "MaterialGoodsType";
        String UnitCode = "UnitCode";
        String MaterialGoodsCategoryCode = "MaterialGoodsCategoryCode";
        String Warranty = "Warranty";
        String MinimumStock = "MinimumStock";
        String ItemSource = "ItemSource";
        String RepositoryCode = "RepositoryCode";
        String ReponsitoryAccount = "ReponsitoryAccount";
        String ExpenseAccount = "ExpenseAccount";
        String RevenueAccount = "RevenueAccount";
        String VATTaxRate = "VATTaxRate";
        String ImportTaxRate = "ImportTaxRate";
        String ExportTaxRate = "ExportTaxRate";
        String SaleDiscountRate = "SaleDiscountRate";
        String PurchaseDiscountRate = "PurchaseDiscountRate";
        String MaterialGoodsGSTCode = "MaterialGoodsGSTCode";
        String CareerGroupCode = "CareerGroupCode";
        String FixedSalePrice = "FixedSalePrice";
        String SalePrice1 = "SalePrice1";
        String SalePrice2 = "SalePrice2";
        String SalePrice3 = "SalePrice3";
        String QuantityFrom = "QuantityFrom";
        String QuantityTo = "QuantityTo";
        String DiscountType = "DiscountType";
        String DiscountResult = "DiscountResult";
        String CurrencyID = "CurrencyID";
        String UnitCodePurchasePrice = "UnitCodePurchasePrice";
        String UnitPricePurchasePrice = "UnitPricePurchasePrice";
        String UnitCodeConvert = "UnitCodeConvert";
        String ConvertRate = "ConvertRate";
        String Formula = "Formula";
        String FixedSalePriceConvert = "FixedSalePriceConvert";
        String SalePriceConvert1 = "SalePriceConvert1";
        String SalePriceConvert2 = "SalePriceConvert2";
        String SalePriceConvert3 = "SalePriceConvert3";
        String MaterialAssemblyCode = "MaterialAssemblyCode";
        String UnitCodeAssembly = "UnitCodeAssembly";
        String QuantityAssembly = "QuantityAssembly";
        String UnitPriceAssembly = "UnitPriceAssembly";
        String AmountAssembly = "AmountAssembly";
    }

}
