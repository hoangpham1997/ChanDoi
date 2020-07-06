import { SystemFieldsModel } from 'app/shared/modal/import-excel-danh-muc/systemFields.model';

export const type = {
    DANH_MUC_KH: 'DANH_MUC_KH',
    DANH_MUC_NCC: 'DANH_MUC_NCC',
    DANH_MUC_NV: 'DANH_MUC_NV',
    DANH_MUC_VTHH: 'DANH_MUC_VTHH'
};
export const TYPE_VTHH = {
    INFO_VTHH: 'INFO_VTHH',
    DISCOUNT: 'DISCOUNT',
    P_PRICE: 'P_PRICE',
    S_PRICE: 'S_PRICE',
    UNIT_CONVERT: 'UNIT_CONVERT',
    ASSEMBLY: 'ASSEMBLY'
};

// field accounting object
// KH
export const EXCEL_FIELD_KH: SystemFieldsModel[] = [
    new SystemFieldsModel('AccountingObjectCode', 'Mã khách hàng (*)', 'Mã khách hàng', 0, true),
    new SystemFieldsModel('AccountingObjectName', 'Tên khách hàng (*)', 'Tên khách hàng', 1, true),
    new SystemFieldsModel('ScaleType', 'Quy mô (*)', 'Quy mô: 0-Tổ chức, 1-Cá nhân', 2, true),
    new SystemFieldsModel(
        'ObjectType',
        'Loại đối tượng (*)',
        'Loại đối tượng kế toán : 1-Khách hàng, 2-Vừa là KH vừa là NCC, 3-Khác',
        3,
        true
    ),
    new SystemFieldsModel('Tel', 'Điện thoại', 'Số điện thoại', 4),
    new SystemFieldsModel('AccountingObjectAddress', 'Địa chỉ', 'Địa chỉ KH', 5),
    new SystemFieldsModel('TaxCode', 'Mã số thuế', 'Mã số thuế', 6),
    new SystemFieldsModel('Email', 'Email', 'Email', 7),
    new SystemFieldsModel('Website', 'Website', 'Website', 8),
    new SystemFieldsModel('Fax', 'Fax', 'Fax', 9),
    new SystemFieldsModel('MaximizaDebtAmount', 'Số nợ tối đa', 'Số nợ tối đa', 10),
    new SystemFieldsModel('DueTime', 'Số ngày được nợ', 'Số ngày được nợ', 11),
    new SystemFieldsModel('PaymentClauseCode', 'Điều khoản thanh toán', 'Điều khoản thanh toán', 12),
    new SystemFieldsModel('AccountingObjectGroupCode', 'Nhóm KH', 'Nhóm KH', 13),
    new SystemFieldsModel('ContactName', 'Tên người liên hệ', 'Tên người liên hệ', 14),
    new SystemFieldsModel('ContactTitle', 'Chức vụ người liên hệ', 'Chức vụ người liên hệ', 15),
    new SystemFieldsModel('ContactSex', 'Giới tính người liên hệ', 'Giới tính: 0 - Nam, 1 - Nữ', 16),
    new SystemFieldsModel('ContactAddress', 'Địa chỉ người liên hệ', 'Địa chỉ người liên hệ', 17),
    new SystemFieldsModel('ContactMobile', 'ĐT di động người liên hệ', 'ĐT di động người liên hệ', 18),
    new SystemFieldsModel('ContactEmail', 'Email người liên hệ', 'Email người liên hệ', 19),
    new SystemFieldsModel('ContactHomeTel', 'ĐT nhà riêng', 'ĐT nhà riêng', 20),
    new SystemFieldsModel('ContactOfficeTel', 'ĐT cơ quan', 'ĐT cơ quan', 21),
    new SystemFieldsModel('IdentificationNo', 'Số CMND người liên hệ', 'Số CMND người liên hệ', 22),
    new SystemFieldsModel('IssueDate', 'Ngày cấp', 'Ngày cấp', 23),
    new SystemFieldsModel('IssueBy', 'Nơi cấp', 'Nơi cấp', 24),
    new SystemFieldsModel('BankAccount', 'Số tài khoản ngân hàng', 'Số tài khoản ngân hàng', 25),
    new SystemFieldsModel('BankName', 'Mã ngân hàng', 'Mã ngân hàng', 26),
    new SystemFieldsModel('BankBranchName', 'Tên chi nhánh', 'Tên chi nhánh', 27),
    new SystemFieldsModel('AccountHolderName', 'Tên chủ tài khoản', 'Tên chủ tài khoản', 28)
];

// NCC
export const EXCEL_FIELD_NCC: SystemFieldsModel[] = [
    new SystemFieldsModel('AccountingObjectCode', 'Mã NCC (*)', 'Mã nhà cung cấp', 0, true),
    new SystemFieldsModel('AccountingObjectName', 'Tên NCC (*)', 'Tên nhà cung cấp', 1, true),
    new SystemFieldsModel('ScaleType', 'Quy mô (*)', 'Quy mô: 0-Tổ chức, 1-Cá nhân', 2, true),
    new SystemFieldsModel(
        'ObjectType',
        'Loại đối tượng (*)',
        'Loại đối tượng kế toán:  0-Nhà cung cấp, 2-Vừa là KH vừa là NCC, 3-Khác',
        3,
        true
    ),
    new SystemFieldsModel('Tel', 'Điện thoại', 'Số điện thoại', 4),
    new SystemFieldsModel('AccountingObjectAddress', 'Địa chỉ', 'Địa chỉ KH', 5),
    new SystemFieldsModel('TaxCode', 'Mã số thuế', 'Mã số thuế', 6),
    new SystemFieldsModel('Email', 'Email', 'Email', 7),
    new SystemFieldsModel('Website', 'Website', 'Website', 8),
    new SystemFieldsModel('Fax', 'Fax', 'Fax', 9),
    new SystemFieldsModel('AccountingObjectGroupCode', 'Nhóm NCC', 'Nhóm NCC', 10),
    new SystemFieldsModel('ContactName', 'Tên người liên hệ', 'Tên người liên hệ', 11),
    new SystemFieldsModel('ContactTitle', 'Chức vụ người liên hệ', 'Chức vụ người liên hệ', 12),
    new SystemFieldsModel('ContactSex', 'Giới tính người liên hệ', 'Giới tính: 0 - Nam, 1 - Nữ', 13),
    new SystemFieldsModel('ContactAddress', 'Địa chỉ người liên hệ', 'Địa chỉ người liên hệ', 14),
    new SystemFieldsModel('ContactMobile', 'ĐT di động người liên hệ', 'ĐT di động người liên hệ', 15),
    new SystemFieldsModel('ContactEmail', 'Email người liên hệ', 'Email người liên hệ', 16),
    new SystemFieldsModel('ContactHomeTel', 'ĐT nhà riêng', 'ĐT nhà riêng', 17),
    new SystemFieldsModel('ContactOfficeTel', 'ĐT cơ quan', 'ĐT cơ quan', 18),
    new SystemFieldsModel('IdentificationNo', 'Số CMND người liên hệ', 'Số CMND người liên hệ', 19),
    new SystemFieldsModel('IssueDate', 'Ngày cấp', 'Ngày cấp', 20),
    new SystemFieldsModel('IssueBy', 'Nơi cấp', 'Nơi cấp', 21),
    new SystemFieldsModel('BankAccount', 'Số tài khoản ngân hàng', 'Số tài khoản ngân hàng', 22),
    new SystemFieldsModel('BankName', ' Mã ngân hàng', ' Mã ngân hàng', 23),
    new SystemFieldsModel('BankBranchName', 'Tên chi nhánh', 'Tên chi nhánh', 24),
    new SystemFieldsModel('AccountHolderName', 'Tên chủ tài khoản', 'Tên chủ tài khoản', 25)
];

export const EXCEL_FIELD_NV: SystemFieldsModel[] = [
    new SystemFieldsModel('AccountingObjectCode', 'Mã nhân viên (*)', 'Mã nhân viên', 0, true),
    new SystemFieldsModel('AccountingObjectName', 'Tên nhân viên (*)', 'Tên nhân viên', 1, true),
    new SystemFieldsModel('ContactTitle', 'Chức vụ', 'Chức vụ', 2),
    new SystemFieldsModel('DepartmentCode', 'Phòng ban (*)', 'Phòng ban', 3, true),
    new SystemFieldsModel('EmployeeBirthday', 'Ngày sinh', 'Ngày sinh', 4, false),
    new SystemFieldsModel('ContactSex', 'Giới tính', 'Giới tính: 0 - Nam, 1 - Nữ', 5),
    new SystemFieldsModel('TaxCode', 'Mã số thuế', 'Mã số thuế', 6),
    new SystemFieldsModel('NumberOfDependent', 'Số người phụ thuộc', 'Số người phụ thuộc', 7),
    new SystemFieldsModel('IdentificationNo', 'Số CMND', 'Số CMND người liên hệ', 8),
    new SystemFieldsModel('IssueDate', 'Ngày cấp', 'Ngày cấp', 9),
    new SystemFieldsModel('IssueBy', 'Nơi cấp', 'Nơi cấp', 10),
    new SystemFieldsModel('AgreementSalary', 'Lương thỏa thuận', 'Lương thỏa thuận', 11),
    new SystemFieldsModel('InsuranceSalary', 'Lương đóng BH', 'Lương đóng BH', 12),
    new SystemFieldsModel('SalaryCoefficient', 'Hệ số lương', 'Hệ số lương', 13),
    new SystemFieldsModel(
        'ObjectType',
        'Loại đối tượng KT',
        'Loại đối tượng kế toán: 0-Nhà cung cấp, 1-Khách hàng, 2-Vừa là KH vừa là NCC, 3-Khác',
        14,
        false
    ),
    new SystemFieldsModel(
        'IsUnofficialStaff',
        'Là NV không chính thức',
        'Là NV chính thức: 1-Nhân viên chính thức, 0-Nhân viên không chính thức',
        15
    ),
    new SystemFieldsModel('AccountingObjectAddress', 'Địa chỉ', 'Địa chỉ nhân viên', 16),
    new SystemFieldsModel('ContactHomeTel', 'ĐT nhà riêng', 'ĐT nhà riêng', 17),
    new SystemFieldsModel('Tel', 'ĐT di động', 'ĐT di động', 18),
    new SystemFieldsModel('Email', 'Địa chỉ email', 'Địa chỉ email', 19),
    new SystemFieldsModel('BankAccount', 'Số tài khoản ngân hàng', 'Số tài khoản ngân hàng', 20),
    new SystemFieldsModel('BankName', 'Mã ngân hàng', 'Mã ngân hàng', 21),
    new SystemFieldsModel('BankBranchName', 'Tên chi nhánh', 'Tên chi nhánh', 22),
    new SystemFieldsModel('AccountHolderName', 'Tên chủ tài khoản', 'Tên chủ tài khoản', 23)
];
// VTHH
export const EXCEL_FIELD_VTHH: SystemFieldsModel[] = [
    new SystemFieldsModel('MaterialGoodsCode', 'Mã VTHH (*)', 'Mã VTHH', 0, true, TYPE_VTHH.INFO_VTHH),
    new SystemFieldsModel('MaterialGoodsName', 'Tên VTHH (*)', 'Tên VTHH', 1, true, TYPE_VTHH.INFO_VTHH),
    new SystemFieldsModel('MaterialGoodsType', 'Tính chất (*)', 'Tính chất VTHH', 2, true, TYPE_VTHH.INFO_VTHH),
    new SystemFieldsModel('UnitCode', 'ĐVT chính', 'Mã Đơn vị tính chính', 3, false, TYPE_VTHH.INFO_VTHH),
    new SystemFieldsModel('MaterialGoodsCategoryCode', 'Loại VTHH', 'Mã danh mục loại VTHH', 4, false, TYPE_VTHH.INFO_VTHH),
    new SystemFieldsModel('Warranty', 'Thời hạn bảo hành', 'Thời hạn bảo hành', 5, false, TYPE_VTHH.INFO_VTHH),
    new SystemFieldsModel('MinimumStock', 'Số lượng tồn tối thiểu', 'Số lượng tồn tối thiểu', 6, false, TYPE_VTHH.INFO_VTHH),
    new SystemFieldsModel('ItemSource', 'Xuất xứ hàng hóa', 'Xuất xứ hàng hóa', 7, false, TYPE_VTHH.INFO_VTHH),
    new SystemFieldsModel('RepositoryCode', 'Kho', 'Mã kho', 8, false, TYPE_VTHH.INFO_VTHH),
    new SystemFieldsModel('ReponsitoryAccount', 'TK kho', 'Tài khoản kho', 9, false, TYPE_VTHH.INFO_VTHH),
    new SystemFieldsModel('ExpenseAccount', 'TK chi phí', 'Tài khoản chi phí', 10, false, TYPE_VTHH.INFO_VTHH),
    new SystemFieldsModel('RevenueAccount', 'TK doanh thu', 'Tài khoản doanh thu', 11, false, TYPE_VTHH.INFO_VTHH),
    new SystemFieldsModel('VATTaxRate', 'Thuế suất thuế  GTGT', 'Thuế suất thuế GTGT', 12, false, TYPE_VTHH.INFO_VTHH),
    new SystemFieldsModel('ImportTaxRate', 'Thuế suất thuế NK', 'Thuế suất thuế nhập khẩu', 13, false, TYPE_VTHH.INFO_VTHH),
    new SystemFieldsModel('ExportTaxRate', 'Thuế suất thuế XK', 'Thuế suất thuế xuất khẩu', 14, false, TYPE_VTHH.INFO_VTHH),
    new SystemFieldsModel('SaleDiscountRate', 'Tỷ lệ CKBH (%)', 'Tỷ lệ chiết khấu bán hàng', 15, false, TYPE_VTHH.INFO_VTHH),
    new SystemFieldsModel('PurchaseDiscountRate', 'Tỷ lệ CKMH (%)', 'Tỷ lệ chiết khấu mua hàng', 16, false, TYPE_VTHH.INFO_VTHH),
    new SystemFieldsModel(
        'MaterialGoodsGSTCode',
        'Nhóm HHDV chịu thuế TTĐB',
        'Mã nhóm hàng hóa dịch vụ chịu thuế tiêu thụ đặc biệt',
        17,
        false,
        TYPE_VTHH.INFO_VTHH
    ),
    new SystemFieldsModel(
        'CareerGroupCode',
        'Nhóm ngành nghề tính thuế GTGT',
        'Mã nhóm ngành nghề tính thuế GTGT (dùng cho thuế trực tiếp)',
        18,
        false,
        TYPE_VTHH.INFO_VTHH
    ),
    new SystemFieldsModel('FixedSalePrice', 'Giá bán cố định', 'Đơn giá bán cố định', 19, false, TYPE_VTHH.S_PRICE),
    new SystemFieldsModel('SalePrice1', 'Giá bán 1', 'Đơn giá bán 1', 20, false, TYPE_VTHH.S_PRICE),
    new SystemFieldsModel('SalePrice2', 'Giá bán 2', 'Đơn giá bán 2', 21, false, TYPE_VTHH.S_PRICE),
    new SystemFieldsModel('SalePrice3', 'Giá bán 3', 'Đơn giá bán 3', 22, false, TYPE_VTHH.S_PRICE),
    new SystemFieldsModel('QuantityFrom', 'Số lượng từ', 'Số lượng từ', 23, false, TYPE_VTHH.DISCOUNT),
    new SystemFieldsModel('QuantityTo', 'Số lượng đến', 'Số lượng đến', 24, false, TYPE_VTHH.DISCOUNT),
    new SystemFieldsModel('DiscountType', 'Loại chiết khấu', 'Loại chiết khấu', 25, false, TYPE_VTHH.DISCOUNT),
    new SystemFieldsModel('DiscountResult', '% hoặc số tiền CK', 'Kết quả chiết khấu', 26, false, TYPE_VTHH.DISCOUNT),
    new SystemFieldsModel('CurrencyID', 'Loại tiền', 'Loại tiền', 27, false, TYPE_VTHH.P_PRICE),
    new SystemFieldsModel('UnitCodePurchasePrice', 'Đơn vị tính', 'Đơn vị tính', 28, false, TYPE_VTHH.P_PRICE),
    new SystemFieldsModel('UnitPricePurchasePrice', 'Đơn giá mua', 'Đơn giá', 29, false, TYPE_VTHH.P_PRICE),
    new SystemFieldsModel('UnitCodeConvert', 'ĐVT chuyển đổi', 'Đơn vị chuyển đổi', 30, false, TYPE_VTHH.UNIT_CONVERT),
    new SystemFieldsModel('ConvertRate', 'Tỷ lệ chuyển đổi', 'Tỷ lệ chuyển đổi', 31, false, TYPE_VTHH.UNIT_CONVERT),
    new SystemFieldsModel('Formula', 'Phép tính', 'Phép tính (công thức tính)', 32, false, TYPE_VTHH.UNIT_CONVERT),
    new SystemFieldsModel(
        'FixedSalePriceConvert',
        'Gián bán cố định theo ĐVT chuyển đổi',
        'Đơn giá bán cố định',
        33,
        false,
        TYPE_VTHH.UNIT_CONVERT
    ),
    new SystemFieldsModel('SalePriceConvert1', 'Gián bán 1 theo ĐVT chuyển đổi', 'Đơn giá bán 1', 34, false, TYPE_VTHH.UNIT_CONVERT),
    new SystemFieldsModel('SalePriceConvert2', 'Gián bán 2 theo ĐVT chuyển đổi', 'Đơn giá bán 2', 35, false, TYPE_VTHH.UNIT_CONVERT),
    new SystemFieldsModel('SalePriceConvert3', 'Gián bán 3 theo ĐVT chuyển đổi', 'Đơn giá bán 3', 36, false, TYPE_VTHH.UNIT_CONVERT),
    new SystemFieldsModel(
        'MaterialAssemblyCode',
        'Mã VTHH lắp ráp/ tháo dỡ',
        'Mã vật tư hàng hóa lắp ráp/tháo dỡ',
        37,
        false,
        TYPE_VTHH.ASSEMBLY
    ),
    new SystemFieldsModel('UnitCodeAssembly', 'Đơn vị tính VTHH lắp ráp/ tháo dỡ', 'Đơn vị tính', 38, false, TYPE_VTHH.ASSEMBLY),
    new SystemFieldsModel('QuantityAssembly', 'Số lượng VTHH lắp ráp/ tháo dỡ', 'Số lượng', 39, false, TYPE_VTHH.ASSEMBLY),
    new SystemFieldsModel('UnitPriceAssembly', 'Đơn giá VTHH lắp ráp/ tháo dỡ', 'Đơn giá', 40, false, TYPE_VTHH.ASSEMBLY),
    new SystemFieldsModel('AmountAssembly', 'Thành tiền VTHH lắp ráp/ tháo dỡ', 'Thành tiền', 41, false, TYPE_VTHH.ASSEMBLY)
];
