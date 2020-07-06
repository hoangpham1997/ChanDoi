package vn.softdreams.ebweb.domain;

import org.apache.poi.hpsf.Decimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.PPPayVendorBillDTO;
import vn.softdreams.ebweb.service.dto.PPPayVendorDTO;
import vn.softdreams.ebweb.service.dto.Report.*;
import vn.softdreams.ebweb.web.rest.dto.ViewGLPayExceedCashDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;


/**
 * A GeneralLedger.
 */
@Entity
@Table(name = "generalledger")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "PPPayVendorDTO",
        classes = {
            @ConstructorResult(
                targetClass = PPPayVendorDTO.class,
                columns = {
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "accountingObjectCode", type = String.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "soDuDauNam", type = BigDecimal.class),
                    @ColumnResult(name = "soPhatSinh", type = BigDecimal.class),
                    @ColumnResult(name = "soDaTra", type = BigDecimal.class),
                    @ColumnResult(name = "soConPhaiTra", type = BigDecimal.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "PPPayVendorBillDTO",
        classes = {
            @ConstructorResult(
                targetClass = PPPayVendorBillDTO.class,
                columns = {
                    @ColumnResult(name = "referenceID", type = UUID.class),
                    @ColumnResult(name = "date", type = String.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "invoiceNo", type = String.class),
                    @ColumnResult(name = "dueDate", type = String.class),
                    @ColumnResult(name = "totalDebitOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "totalDebit", type = BigDecimal.class),
                    @ColumnResult(name = "debitAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "debitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "account", type = String.class),
                    @ColumnResult(name = "currencyID", type = String.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "employeeName", type = String.class),
                    @ColumnResult(name = "paymentClause", type = String.class),
                    @ColumnResult(name = "refVoucherExchangeRate", type = BigDecimal.class),
                    @ColumnResult(name = "lastExchangeRate", type = BigDecimal.class),
                    @ColumnResult(name = "differAmount", type = BigDecimal.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "SAReceiptDebitDTO",
        classes = {
            @ConstructorResult(
                targetClass = SAReceiptDebit.class,
                columns = {
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "accountingObjectCode", type = String.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "soDuDauNam", type = BigDecimal.class),
                    @ColumnResult(name = "soPhatSinh", type = BigDecimal.class),
                    @ColumnResult(name = "soDaThu", type = BigDecimal.class),
                    @ColumnResult(name = "soConPhaiThu", type = BigDecimal.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "SAReceiptDebitBillDTO",
        classes = {
            @ConstructorResult(
                targetClass = SAReceiptDebitBill.class,
                columns = {
                    @ColumnResult(name = "referenceID", type = UUID.class),
                    @ColumnResult(name = "date", type = String.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "invoiceNo", type = String.class),
                    @ColumnResult(name = "dueDate", type = String.class),
                    @ColumnResult(name = "totalCreditOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "totalCredit", type = BigDecimal.class),
                    @ColumnResult(name = "creditAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "creditAmount", type = BigDecimal.class),
                    @ColumnResult(name = "account", type = String.class),
                    @ColumnResult(name = "currencyID", type = String.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "employeeName", type = String.class),
                    @ColumnResult(name = "paymentClause", type = String.class),
                    @ColumnResult(name = "refVoucherExchangeRate", type = BigDecimal.class),
                    @ColumnResult(name = "lastExchangeRate", type = BigDecimal.class),
                    @ColumnResult(name = "differAmount", type = BigDecimal.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "SoNhatKyChungDTO",
        classes = {
            @ConstructorResult(
                targetClass = SoNhatKyChungDTO.class,
                columns = {
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "no", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "accountNumber", type = String.class),
                    @ColumnResult(name = "correspondingAccountNumber", type = String.class),
                    @ColumnResult(name = "debitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "creditAmount", type = BigDecimal.class),
                    @ColumnResult(name = "referenceID", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "oderType", type = Integer.class)

                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "BangCanDoiKeToanDTO",
        classes = {
            @ConstructorResult(
                targetClass = BangCanDoiKeToanDTO.class,
                columns = {
                    @ColumnResult(name = "itemCode", type = String.class),
                    @ColumnResult(name = "itemName", type = String.class),
                    @ColumnResult(name = "itemIndex", type = Integer.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "isBold", type = Boolean.class),
                    @ColumnResult(name = "isItalic", type = Boolean.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "prevAmount", type = BigDecimal.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "SoCaiHTNhatKyChungDTO",
        classes = {
            @ConstructorResult(
                targetClass = SoCaiHTNhatKyChungDTO.class,
                columns = {
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "no", type = String.class),
                    @ColumnResult(name = "journalMemo", type = String.class),
                    @ColumnResult(name = "accountNumber", type = String.class),
                    @ColumnResult(name = "detailAccountNumber", type = String.class),
                    @ColumnResult(name = "correspondingAccountNumber", type = String.class),
                    @ColumnResult(name = "debitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "creditAmount", type = BigDecimal.class),
                    @ColumnResult(name = "refType", type = Integer.class),
                    @ColumnResult(name = "referenceID", type = UUID.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "SoCaiHTNhatKyChungCalcularDTO",
        classes = {
            @ConstructorResult(
                targetClass = SoCaiHTNhatKyChungDTO.class,
                columns = {
                    @ColumnResult(name = "accountNumber", type = String.class),
                    @ColumnResult(name = "accountName", type = String.class),
                    @ColumnResult(name = "isParent", type = Boolean.class),
                    @ColumnResult(name = "openningDebitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "OpenningCreditAmount", type = BigDecimal.class),
                    @ColumnResult(name = "ClosingDebitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "ClosingCreditAmount", type = BigDecimal.class),
                    @ColumnResult(name = "AccumDebitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "AccumCreditAmount", type = BigDecimal.class),
                    @ColumnResult(name = "AccountCategoryKind", type = Integer.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "SoNhatKyThuTienDTO",
        classes = {
            @ConstructorResult(
                targetClass = SoNhatKyThuTienDTO.class,
                columns = {
                    @ColumnResult(name = "refType", type = Integer.class),
                    @ColumnResult(name = "refID", type = UUID.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "no", type = String.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "accountNumber", type = String.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "col2", type = BigDecimal.class),
                    @ColumnResult(name = "col3", type = BigDecimal.class),
                    @ColumnResult(name = "col4", type = BigDecimal.class),
                    @ColumnResult(name = "col5", type = BigDecimal.class),
                    @ColumnResult(name = "col6", type = BigDecimal.class),
                    @ColumnResult(name = "colOtherAccount", type = String.class),
                    @ColumnResult(name = "accountNumberList", type = String.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "BangKeMuaVaoDTO",
        classes = {
            @ConstructorResult(
                targetClass = BangKeMuaBanDTO.class,
                columns = {
                    @ColumnResult(name = "goodsServicePurchaseCode", type = Integer.class),
                    @ColumnResult(name = "goodsServicePurchaseName", type = String.class),
                    @ColumnResult(name = "invoiceNo", type = String.class),
                    @ColumnResult(name = "invoiceDate", type = LocalDate.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "companyTaxCode", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "vatRate", type = BigDecimal.class),
                    @ColumnResult(name = "vatAmount", type = BigDecimal.class),
                    @ColumnResult(name = "vatAccount", type = String.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
//                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
//                    @ColumnResult(name = "refID", type = UUID.class),
//                    @ColumnResult(name = "typeId", type = Integer.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "BangKeBanRaDTO",
        classes = {
            @ConstructorResult(
                targetClass = BangKeMuaBanDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "invoiceNo", type = String.class),
                    @ColumnResult(name = "invoiceDate", type = LocalDate.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "companyTaxCode", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "vatRate", type = BigDecimal.class),
                    @ColumnResult(name = "vatAmount", type = BigDecimal.class),
                    @ColumnResult(name = "vatAccount", type = String.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
//                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
//                    @ColumnResult(name = "refID", type = UUID.class),
                    @ColumnResult(name = "typeId", type = Integer.class),
                }
            )
        }
    ),

    @SqlResultSetMapping(
        name = "SoChiTietTaiKhoanDTO",
        classes = {
            @ConstructorResult(
                targetClass = SoChiTietTaiKhoanDTO.class,
                columns = {
                    @ColumnResult(name = "accountNumber", type = String.class),
                    @ColumnResult(name = "accountNameWithAccountNumber", type = String.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "no", type = String.class),
                    @ColumnResult(name = "referenceID", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "journalMemo", type = String.class),
                    @ColumnResult(name = "accountCorresponding", type = String.class),
                    @ColumnResult(name = "debitAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "debitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "creditAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "creditAmount", type = BigDecimal.class),
                    @ColumnResult(name = "closingDebitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "closingDebitAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "closingCreditAmount", type = BigDecimal.class),
                    @ColumnResult(name = "closingCreditAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "isBold", type = Boolean.class),
                    @ColumnResult(name = "unResonableCost", type = String.class),
                    @ColumnResult(name = "orderNumber", type = Integer.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "TongHopCongNoPhaiTraDTO",
        classes = {
            @ConstructorResult(
                targetClass = TongHopCongNoPhaiTraDTO.class,
                columns = {
                    @ColumnResult(name = "accountObjectCode", type = String.class),
                    @ColumnResult(name = "accountObjectName", type = String.class),
                    @ColumnResult(name = "accountNumber", type = String.class),
                    @ColumnResult(name = "openingDebitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "openingCreditAmount", type = BigDecimal.class),
                    @ColumnResult(name = "debitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "creditAmount", type = BigDecimal.class),
                    @ColumnResult(name = "closeDebitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "closeCreditAmount", type = BigDecimal.class),
                    @ColumnResult(name = "openingDebitAmountOC", type = BigDecimal.class),
                    @ColumnResult(name = "openingCreditAmountOC", type = BigDecimal.class),
                    @ColumnResult(name = "debitAmountOC", type = BigDecimal.class),
                    @ColumnResult(name = "creditAmountOC", type = BigDecimal.class),
                    @ColumnResult(name = "closeDebitAmountOC", type = BigDecimal.class),
                    @ColumnResult(name = "closeCreditAmountOC", type = BigDecimal.class),
                    @ColumnResult(name = "accumDebitAmountOC", type = BigDecimal.class),
                    @ColumnResult(name = "accumDebitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "accumCreditAmountOC", type = BigDecimal.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "TongHopCongNoPhaiThuDTO",
        classes = {
            @ConstructorResult(
                targetClass = TongHopCongNoPhaiThuDTO.class,
                columns = {
                    @ColumnResult(name = "accountingObjectID", type = String.class),
                    @ColumnResult(name = "accountingObjectCode", type = String.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "accountObjectAddress", type = String.class),
                    @ColumnResult(name = "accountObjectTaxCode", type = String.class),
                    @ColumnResult(name = "accountNumber", type = String.class),
                    @ColumnResult(name = "openningDebitAmountOC", type = BigDecimal.class),
                    @ColumnResult(name = "openningDebitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "openningCreditAmountOC", type = BigDecimal.class),
                    @ColumnResult(name = "openningCreditAmount", type = BigDecimal.class),
                    @ColumnResult(name = "debitAmountOC", type = BigDecimal.class),
                    @ColumnResult(name = "debitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "creditAmountOC", type = BigDecimal.class),
                    @ColumnResult(name = "creditAmount", type = BigDecimal.class),
                    @ColumnResult(name = "accumDebitAmountOC", type = BigDecimal.class),
                    @ColumnResult(name = "accumDebitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "accumCreditAmountOC", type = BigDecimal.class),
                    @ColumnResult(name = "accumCreditAmount", type = BigDecimal.class),
                    @ColumnResult(name = "closeDebitAmountOC", type = BigDecimal.class),
                    @ColumnResult(name = "closeCreditAmountOC", type = BigDecimal.class),
                    @ColumnResult(name = "closeDebitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "closeCreditAmount", type = BigDecimal.class),
                    @ColumnResult(name = "accountObjectGroupListCode", type = String.class),
                    @ColumnResult(name = "accountObjectCategoryName", type = String.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "ChiTietCongNoPhaiTraDTO",
        classes = {
            @ConstructorResult(
                targetClass = ChiTietCongNoPhaiTraDTO.class,
                columns = {
                    @ColumnResult(name = "postedDate", type = String.class),
                    @ColumnResult(name = "date", type = String.class),
                    @ColumnResult(name = "no", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "refType", type = Integer.class),
                    @ColumnResult(name = "referenceID", type = UUID.class),
                    @ColumnResult(name = "correspondingAccountNumber", type = String.class),
                    @ColumnResult(name = "accountObjectCode", type = String.class),
                    @ColumnResult(name = "accountObjectName", type = String.class),
                    @ColumnResult(name = "accountObjectID", type = UUID.class),
                    @ColumnResult(name = "accountNumber", type = String.class),
                    @ColumnResult(name = "openingDebitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "openingCreditAmount", type = BigDecimal.class),
                    @ColumnResult(name = "debitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "debitAmountOC", type = BigDecimal.class),
                    @ColumnResult(name = "creditAmount", type = BigDecimal.class),
                    @ColumnResult(name = "creditAmountOC", type = BigDecimal.class),
                    @ColumnResult(name = "closingDebitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "closingDebitAmountOC",type = BigDecimal.class),
                    @ColumnResult(name = "closingCreditAmount", type = BigDecimal.class),
                    @ColumnResult(name = "closingCreditAmountOC", type = BigDecimal.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "ChiTietCongNoPhaiThuDTO",
        classes = {
            @ConstructorResult(
                targetClass = ChiTietCongNoPhaiThuDTO.class,
                columns = {
                    @ColumnResult(name = "refType", type = Integer.class),
                    @ColumnResult(name = "postedDate", type = String.class),
                    @ColumnResult(name = "refDate", type = String.class),
                    @ColumnResult(name = "refID", type = UUID.class),
                    @ColumnResult(name = "refNo", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "accountNumber", type = String.class),
                    @ColumnResult(name = "correspondingAccountNumber", type = String.class),
                    @ColumnResult(name = "debitAmountOC", type = BigDecimal.class),
                    @ColumnResult(name = "debitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "creditAmountOC", type = BigDecimal.class),
                    @ColumnResult(name = "creditAmount", type = BigDecimal.class),
                    @ColumnResult(name = "closingDebitAmountOC", type = BigDecimal.class),
                    @ColumnResult(name = "closingDebitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "closingCreditAmountOC", type = BigDecimal.class),
                    @ColumnResult(name = "closingCreditAmount", type = BigDecimal.class),
                    @ColumnResult(name = "accountObjectCode", type = String.class),
                    @ColumnResult(name = "accountObjectName", type = String.class),
                    @ColumnResult(name = "accountObjectID", type = UUID.class),
                    @ColumnResult(name = "ordercode", type = Long.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "ViewGLPayExceedCashDTO",
        classes = {
            @ConstructorResult(
                targetClass = ViewGLPayExceedCashDTO.class,
                columns = {
                    @ColumnResult(name = "account", type = String.class),
                    @ColumnResult(name = "companyID", type = UUID.class),
                    @ColumnResult(name = "typeLedger", type = Integer.class),
                    @ColumnResult(name = "debitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "debitAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "creditAmount", type = BigDecimal.class),
                    @ColumnResult(name = "creditAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "bankAccountDetailID", type = UUID.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "SoTheoDoiDoiTuongTHCPTheoTaiKhoanDTO",
        classes = {
            @ConstructorResult(
                targetClass = SoTheoDoiDoiTuongTHCPTheoTaiKhoanDTO.class,
                columns = {
                    @ColumnResult(name = "refID", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "costSetName", type = String.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "dateHT", type = LocalDate.class),
                    @ColumnResult(name = "no", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "account", type = String.class),
                    @ColumnResult(name = "accountCorresponding", type = String.class),
                    @ColumnResult(name = "debitAmount", type = BigDecimal.class),
                    @ColumnResult(name = "creditAmount", type = BigDecimal.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiDTO",
        classes = {
            @ConstructorResult(
                targetClass = SoTheoDoiDoiTuongTHCPTheoKhoanMucChiPhiDTO.class,
                columns = {
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "costSetName", type = String.class),
                    @ColumnResult(name = "expenseItemID", type = UUID.class),
                    @ColumnResult(name = "expenseItemCode", type = String.class),
                    @ColumnResult(name = "expenseItemName", type = String.class),
                    @ColumnResult(name = "soDauky", type = BigDecimal.class),
                    @ColumnResult(name = "soPhatSinh", type = BigDecimal.class),
                    @ColumnResult(name = "luyKeCuoiKy", type = BigDecimal.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "SoNhatKyMuaHangDTO",
        classes = {
            @ConstructorResult(
                targetClass = SoNhatKyMuaHangDTO.class,
                columns = {
                    @ColumnResult(name = "ngayGhiSo", type = LocalDate.class),
                    @ColumnResult(name = "soCTu", type = String.class),
                    @ColumnResult(name = "ngayCTu", type = LocalDate.class),
                    @ColumnResult(name = "dienGiai", type = String.class),
                    @ColumnResult(name = "hangHoa", type = BigDecimal.class),
                    @ColumnResult(name = "phaiTraNguoiBan", type = BigDecimal.class),
                    @ColumnResult(name = "nguyenVatLieu", type = BigDecimal.class),
                    @ColumnResult(name = "soTien", type = BigDecimal.class),
                    @ColumnResult(name = "account", type = String.class),
                    @ColumnResult(name = "referenceID", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "SoNhatKyBanHangDTO",
        classes = {
            @ConstructorResult(
                targetClass = SoNhatKyBanHangDTO.class,
                columns = {
                    @ColumnResult(name = "detailID", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "refDate", type = LocalDate.class),
                    @ColumnResult(name = "refNo", type = String.class),
                    @ColumnResult(name = "invoiceDate", type = LocalDate.class),
                    @ColumnResult(name = "invoiceNo", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "refID", type = UUID.class),
                    @ColumnResult(name = "nameCustomer", type = String.class),
                    @ColumnResult(name = "doanhThuHangHoa", type = BigDecimal.class),
                    @ColumnResult(name = "doanhThuThanhPham", type = BigDecimal.class),
                    @ColumnResult(name = "doanhThuDichVu", type = BigDecimal.class),
                    @ColumnResult(name = "doanhThuTroCap", type = BigDecimal.class),
                    @ColumnResult(name = "doanhThuBDSDautu", type = BigDecimal.class),
                    @ColumnResult(name = "doanhThuKhac", type = BigDecimal.class),
                    @ColumnResult(name = "chietKhau", type = BigDecimal.class),
                    @ColumnResult(name = "giaTriTraLai", type = BigDecimal.class),
                    @ColumnResult(name = "giaTriGiamGia", type = BigDecimal.class),
                    @ColumnResult(name = "sum", type = BigDecimal.class),
                    @ColumnResult(name = "doanhThuThuan", type = BigDecimal.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "PhanBoChiPhiTraTruocDTO",
        classes = {
            @ConstructorResult(
                targetClass = PhanBoChiPhiTraTruocDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = String.class),
                    @ColumnResult(name = "companyID", type = String.class),
                    @ColumnResult(name = "typeLedger", type = Integer.class),
                    @ColumnResult(name = "typeExpense", type = Boolean.class),
                    @ColumnResult(name = "prepaidExpenseCode", type = String.class),
                    @ColumnResult(name = "prepaidExpenseName", type = String.class),
                    @ColumnResult(name = "date", type = String.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "allocationTime", type = BigDecimal.class),
                    @ColumnResult(name = "allocatedPeriod", type = BigDecimal.class),
                    @ColumnResult(name = "allocatedAmount", type = BigDecimal.class),
                    @ColumnResult(name = "allocationAccount", type = String.class),
                    @ColumnResult(name = "isActive", type = Boolean.class),
                    @ColumnResult(name = "isAllocation", type = Boolean.class),
                    @ColumnResult(name = "allocationAmountAfter", type = BigDecimal.class),
                    @ColumnResult(name = "allocatedPeriodAfter", type = BigDecimal.class),
                    @ColumnResult(name = "allocatedPeriodRest", type = BigDecimal.class),
                    @ColumnResult(name = "accumulated", type = BigDecimal.class),
                    @ColumnResult(name = "amountRest", type = BigDecimal.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "SoChiTietMuaHangDTO",
        classes = {
            @ConstructorResult(
                targetClass = SoChiTietMuaHangDTO.class,
                columns = {
                    @ColumnResult(name = "maKH", type = String.class),
                    @ColumnResult(name = "tenKH", type = String.class),
                    @ColumnResult(name = "ngayHachToan", type = LocalDate.class),
                    @ColumnResult(name = "ngayCTu", type = LocalDate.class),
                    @ColumnResult(name = "soCTu", type = String.class),
                    @ColumnResult(name = "soHoaDon", type = String.class),
                    @ColumnResult(name = "ngayHoaDon", type = LocalDate.class),
                    @ColumnResult(name = "mahang", type = String.class),
                    @ColumnResult(name = "tenhang", type = String.class),
                    @ColumnResult(name = "dvt", type = String.class),
                    @ColumnResult(name = "soLuongMua", type = BigDecimal.class),
                    @ColumnResult(name = "donGia", type = BigDecimal.class),
                    @ColumnResult(name = "giaTriMua", type = BigDecimal.class),
                    @ColumnResult(name = "chietKhau", type = BigDecimal.class),
                    @ColumnResult(name = "soLuongTraLai", type = BigDecimal.class),
                    @ColumnResult(name = "giaTriTraLai", type = BigDecimal.class),
                    @ColumnResult(name = "giaTriGiamGia", type = BigDecimal.class),
                    @ColumnResult(name = "refID", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "SoChiTietBanHangDTO",
        classes = {
            @ConstructorResult(
                targetClass = SoChiTietBanHangDTO.class,
                columns = {
                    @ColumnResult(name = "ngayCTu", type = LocalDate.class),
                    @ColumnResult(name = "ngayHachToan", type = LocalDate.class),
                    @ColumnResult(name = "soHieu", type = String.class),
                    @ColumnResult(name = "ngayHoaDon", type = LocalDate.class),
                    @ColumnResult(name = "soHoaDon", type = String.class),
                    @ColumnResult(name = "dienGiai", type = String.class),
                    @ColumnResult(name = "tkDoiUng", type = String.class),
                    @ColumnResult(name = "dvt", type = String.class),
                    @ColumnResult(name = "soLuong", type = BigDecimal.class),
                    @ColumnResult(name = "donGia", type = BigDecimal.class),
                    @ColumnResult(name = "thanhTien", type = BigDecimal.class),
                    @ColumnResult(name = "thue", type = BigDecimal.class),
                    @ColumnResult(name = "khac", type = BigDecimal.class),
                    @ColumnResult(name = "materialGoodsName", type = String.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "refID", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "giaVon", type = BigDecimal.class),
                    @ColumnResult(name = "tongSoLuong", type = BigDecimal.class),
                    @ColumnResult(name = "tongThanhTien", type = BigDecimal.class),
                    @ColumnResult(name = "tongThue", type = BigDecimal.class),
                    @ColumnResult(name = "tongKhac", type = BigDecimal.class),
                    @ColumnResult(name = "doanhThuThuan", type = BigDecimal.class),
                    @ColumnResult(name = "laiGop", type = BigDecimal.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "SoTheoDoiThanhToanBangNgoaiTeDTO",
        classes = {
            @ConstructorResult(
                targetClass = SoTheoDoiThanhToanBangNgoaiTeDTO.class,
                columns = {
                    @ColumnResult(name = "account", type = String.class),
                    @ColumnResult(name = "idGroup", type = Integer.class),
                    @ColumnResult(name = "accountingObjectID", type = String.class),
                    @ColumnResult(name = "accountingObjectCode", type = String.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "ngayHoachToan", type = LocalDate.class),
                    @ColumnResult(name = "ngayChungTu", type = LocalDate.class),
                    @ColumnResult(name = "soChungTu", type = String.class),
                    @ColumnResult(name = "dienGiai", type = String.class),
                    @ColumnResult(name = "tKDoiUng", type = String.class),
                    @ColumnResult(name = "tyGiaHoiDoai", type = BigDecimal.class),
                    @ColumnResult(name = "pSNSoTien", type = BigDecimal.class),
                    @ColumnResult(name = "pSNQuyDoi", type = BigDecimal.class),
                    @ColumnResult(name = "pSCSoTien", type = BigDecimal.class),
                    @ColumnResult(name = "pSCQuyDoi", type = BigDecimal.class),
                    @ColumnResult(name = "duNoSoTien", type = BigDecimal.class),
                    @ColumnResult(name = "duNoQuyDoi", type = BigDecimal.class),
                    @ColumnResult(name = "duCoSoTien", type = BigDecimal.class),
                    @ColumnResult(name = "duCoQuyDoi", type = BigDecimal.class),
                    @ColumnResult(name = "sDDKSoTien", type = BigDecimal.class),
                    @ColumnResult(name = "sDDKQuyDoi", type = BigDecimal.class),
                    @ColumnResult(name = "refID", type = UUID.class),
                    @ColumnResult(name = "refType", type = Integer.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "GiaThanhPoPupDTO",
        classes = {
            @ConstructorResult(
                targetClass = GiaThanhPoPupDTO.class,
                columns = {
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "postedDate", type = String.class),
                    @ColumnResult(name = "date", type = String.class),
                    @ColumnResult(name = "no", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "accountNumber", type = String.class),
                    @ColumnResult(name = "expenseItemCode", type = String.class),
                    @ColumnResult(name = "expenseItemType", type = Integer.class),
                    @ColumnResult(name = "expenseItemID", type = UUID.class),
                    @ColumnResult(name = "typeVoucher", type = Integer.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "typeGroupID", type = Integer.class),
                    @ColumnResult(name = "refID2", type = UUID.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "GiaThanhAllocationPoPupDTOSum",
        classes = {
            @ConstructorResult(
                targetClass = GiaThanhAllocationPoPupDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "accountNumber", type = String.class),
                    @ColumnResult(name = "expenseItemID", type = UUID.class),
                    @ColumnResult(name = "expenseItemCode", type = String.class),
                    @ColumnResult(name = "totalCost", type = BigDecimal.class),
                    @ColumnResult(name = "unallocatedAmount", type = BigDecimal.class),
                    @ColumnResult(name = "allocatedRate", type = BigDecimal.class),
                    @ColumnResult(name = "allocatedAmount", type = BigDecimal.class),
                    @ColumnResult(name = "allocationMethod", type = Integer.class),
                    @ColumnResult(name = "expenseItemType", type = Integer.class),

                }
            )
        }
    ), @SqlResultSetMapping(
    name = "GiaThanhAllocationPoPupDTO",
    classes = {
        @ConstructorResult(
            targetClass = GiaThanhAllocationPoPupDTO.class,
            columns = {
                @ColumnResult(name = "id", type = UUID.class),
                @ColumnResult(name = "accountNumber", type = String.class),
                @ColumnResult(name = "expenseItemID", type = UUID.class),
                @ColumnResult(name = "expenseItemCode", type = String.class),
                @ColumnResult(name = "totalCost", type = BigDecimal.class),
                @ColumnResult(name = "unallocatedAmount", type = BigDecimal.class),
                @ColumnResult(name = "allocatedRate", type = BigDecimal.class),
                @ColumnResult(name = "allocatedAmount", type = BigDecimal.class),
                @ColumnResult(name = "allocationMethod", type = Integer.class),
                @ColumnResult(name = "refID", type = UUID.class),
                @ColumnResult(name = "refDetailID", type = UUID.class),
                @ColumnResult(name = "expenseItemType", type = Integer.class),
                @ColumnResult(name = "postedDate", type = LocalDate.class),
                @ColumnResult(name = "date", type = LocalDate.class),
                @ColumnResult(name = "noFBook", type = String.class),
                @ColumnResult(name = "noMBook", type = String.class),
                @ColumnResult(name = "reason", type = String.class)

            }
        )
    }),
    @SqlResultSetMapping(
        name = "SoTheoDoiLaiLoTheoHoaDonDTO",
        classes = {
            @ConstructorResult(
                targetClass = SoTheoDoiLaiLoTheoHoaDonDTO.class,
                columns = {
                    @ColumnResult(name = "sAInvoiceID", type = UUID.class),
                    @ColumnResult(name = "refType", type = Integer.class),
                    @ColumnResult(name = "refID", type = UUID.class),
                    @ColumnResult(name = "invoiceDate", type = LocalDate.class),
                    @ColumnResult(name = "invoiceNo", type = String.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "giaTriHHDV", type = BigDecimal.class),
                    @ColumnResult(name = "chietKhau", type = BigDecimal.class),
                    @ColumnResult(name = "giamGia", type = BigDecimal.class),
                    @ColumnResult(name = "traLai", type = BigDecimal.class),
                    @ColumnResult(name = "giaVon", type = BigDecimal.class),
                    @ColumnResult(name = "laiLo", type = BigDecimal.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "SoTheoDoiCongNoPhaiThuTheoHoaDonDTO",
        classes = {
            @ConstructorResult(
                targetClass = SoTheoDoiCongNoPhaiThuTheoHoaDonDTO.class,
                columns = {
                    @ColumnResult(name = "sAInvoiceID", type = UUID.class),
                    @ColumnResult(name = "refType", type = Integer.class),
                    @ColumnResult(name = "refID", type = UUID.class),
                    @ColumnResult(name = "invoiceDate", type = LocalDate.class),
                    @ColumnResult(name = "invoiceNo", type = String.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "giaTriHoaDon", type = BigDecimal.class),
                    @ColumnResult(name = "traLai", type = BigDecimal.class),
                    @ColumnResult(name = "giamGia", type = BigDecimal.class),
                    @ColumnResult(name = "chietKhauTT_GiamTruKhac", type = BigDecimal.class),
                    @ColumnResult(name = "soDaThu", type = BigDecimal.class),
                    @ColumnResult(name = "soConPhaiThu", type = BigDecimal.class)
                }
            )
        }
    )
})
public class GeneralLedger implements Serializable {

//    private static final UUID serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    @NotNull
    @Column(name = "referenceid", nullable = false)
    private UUID referenceID;

    @Column(name = "detailid")
    private UUID detailID;

    @NotNull
    @Column(name = "typeid", nullable = false)
    private Integer typeID;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "posteddate", nullable = false)
    private LocalDate postedDate;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Size(max = 25)
    @Column(name = "nofbook", length = 25)
    private String noFBook;

    @Size(max = 25)
    @Column(name = "nombook", length = 25)
    private String noMBook;

    @Size(max = 25)
    @Column(name = "invoiceseries", length = 25)
    private String invoiceSeries;

    @Column(name = "invoicedate")
    private LocalDate invoiceDate;

    @Size(max = 25)
    @Column(name = "invoiceno", length = 25)
    private String invoiceNo;

    @Size(max = 25)
    @Column(name = "account", length = 25)
    private String account;

    @Size(max = 25)
    @Column(name = "accountcorresponding", length = 25)
    private String accountCorresponding;

    @Column(name = "bankaccountdetailid")
    private UUID bankAccountDetailID;

    @Size(max = 50)
    @Column(name = "bankaccount", length = 50)
    private String bankAccount;

    @Size(max = 512)
    @Column(name = "bankname", length = 512)
    private String bankName;

    //    @Size(max = 3)
    @Column(name = "currencyid")
    private String currencyID;

    @Column(name = "exchangerate", precision = 10, scale = 2)
    private BigDecimal exchangeRate;

    @Column(name = "debitamount", precision = 10, scale = 2)
    private BigDecimal debitAmount;

    @Column(name = "debitamountoriginal", precision = 10, scale = 2)
    private BigDecimal debitAmountOriginal;

    @Column(name = "creditamount", precision = 10, scale = 2)
    private BigDecimal creditAmount;

    @Column(name = "creditamountoriginal", precision = 10, scale = 2)
    private BigDecimal creditAmountOriginal;

    @Size(max = 512)
    @Column(name = "reason", length = 512)
    private String reason;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

    @Size(max = 25)
    @Column(name = "accountingobjectcode", length = 25)
    private String accountingObjectCode;

    @Size(max = 512)
    @Column(name = "accountingobjectname", length = 512)
    private String accountingObjectName;

    @Size(max = 512)
    @Column(name = "accountingobjectaddress", length = 512)
    private String accountingObjectAddress;

    @Size(max = 512)
    @Column(name = "contactname", length = 512)
    private String contactName;

    @Column(name = "employeeid")
    private UUID employeeID;

    @Size(max = 25)
    @Column(name = "employeecode", length = 25)
    private String employeeCode;

    @Size(max = 512)
    @Column(name = "employeename", length = 512)
    private String employeeName;

    @Column(name = "materialgoodsid")
    private UUID materialGoodsID;

    @Size(max = 50)
    @Column(name = "materialgoodscode", length = 50, nullable = false)
    private String materialGoodsCode;

    @Size(max = 512)
    @Column(name = "materialgoodsname", length = 512, nullable = false)
    private String materialGoodsName;

    @Column(name = "repositoryid", nullable = false)
    private UUID repositoryID;

    @Size(max = 25)
    @Column(name = "repositorycode", length = 25, nullable = false)
    private String repositoryCode;

    @Column(name = "repositoryname", nullable = false)
    private String repositoryName;

    @Column(name = "unitid")
    private UUID unitID;

    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unitprice", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "unitpriceoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPriceOriginal;

    @Column(name = "mainunitid")
    private UUID mainUnitID;

    @Column(name = "mainquantity", precision = 10, scale = 2)
    private BigDecimal mainQuantity;

    @Column(name = "mainunitprice", precision = 10, scale = 2, nullable = false)
    private BigDecimal mainUnitPrice;

    @Column(name = "mainconvertrate", precision = 10, scale = 2)
    private BigDecimal mainConvertRate;

    @Size(max = 25)
    @Column(name = "formula", length = 25)
    private String formula;

    @Column(name = "departmentid")
    private UUID departmentID;

    @Column(name = "expenseitemid")
    private UUID expenseItemID;

    @Column(name = "budgetitemid")
    private UUID budgetItemID;

    @Column(name = "costsetid")
    private UUID costSetID;

    @Column(name = "contractid")
    private UUID contractID;

    @Column(name = "statisticscodeid")
    private UUID statisticsCodeID;

    @Column(name = "refdatetime")
    private LocalDate refDateTime;

    @Column(name = "orderpriority", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer orderPriority;

    @Transient
    private String refTable;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public GeneralLedger branchID(UUID branchID) {
        this.branchID = branchID;
        return this;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public UUID getReferenceID() {
        return referenceID;
    }

    public GeneralLedger referenceID(UUID referenceID) {
        this.referenceID = referenceID;
        return this;
    }

    public void setReferenceID(UUID referenceID) {
        this.referenceID = referenceID;
    }

    public UUID getDetailID() {
        return detailID;
    }

    public GeneralLedger detailID(UUID detailID) {
        this.detailID = detailID;
        return this;
    }

    public void setDetailID(UUID detailID) {
        this.detailID = detailID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public GeneralLedger typeID(Integer typeID) {
        this.typeID = typeID;
        return this;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public LocalDate getDate() {
        return date;
    }

    public GeneralLedger date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public GeneralLedger postedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
        return this;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public GeneralLedger typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public GeneralLedger noFBook(String noFBook) {
        this.noFBook = noFBook;
        return this;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public GeneralLedger noMBook(String noMBook) {
        this.noMBook = noMBook;
        return this;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public GeneralLedger invoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
        return this;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public GeneralLedger invoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public GeneralLedger invoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
        return this;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getAccount() {
        return account;
    }

    public GeneralLedger account(String account) {
        this.account = account;
        return this;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountCorresponding() {
        return accountCorresponding;
    }

    public GeneralLedger accountCorresponding(String accountCorresponding) {
        this.accountCorresponding = accountCorresponding;
        return this;
    }

    public void setAccountCorresponding(String accountCorresponding) {
        this.accountCorresponding = accountCorresponding;
    }

    public UUID getBankAccountDetailID() {
        return bankAccountDetailID;
    }

    public GeneralLedger bankAccountDetailID(UUID bankAccountDetailID) {
        this.bankAccountDetailID = bankAccountDetailID;
        return this;
    }

    public void setBankAccountDetailID(UUID bankAccountDetailID) {
        this.bankAccountDetailID = bankAccountDetailID;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public GeneralLedger bankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
        return this;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public GeneralLedger bankName(String bankName) {
        this.bankName = bankName;
        return this;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public GeneralLedger currencyID(String currencyID) {
        this.currencyID = currencyID;
        return this;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public GeneralLedger exchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public GeneralLedger debitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
        return this;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getDebitAmountOriginal() {
        return debitAmountOriginal;
    }

    public GeneralLedger debitAmountOriginal(BigDecimal debitAmountOriginal) {
        this.debitAmountOriginal = debitAmountOriginal;
        return this;
    }

    public void setDebitAmountOriginal(BigDecimal debitAmountOriginal) {
        this.debitAmountOriginal = debitAmountOriginal;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public GeneralLedger creditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
        return this;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getCreditAmountOriginal() {
        return creditAmountOriginal;
    }

    public GeneralLedger creditAmountOriginal(BigDecimal creditAmountOriginal) {
        this.creditAmountOriginal = creditAmountOriginal;
        return this;
    }

    public void setCreditAmountOriginal(BigDecimal creditAmountOriginal) {
        this.creditAmountOriginal = creditAmountOriginal;
    }

    public String getReason() {
        return reason;
    }

    public GeneralLedger reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public GeneralLedger description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public GeneralLedger accountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
        return this;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public String getAccountingObjectCode() {
        return accountingObjectCode;
    }

    public GeneralLedger accountingObjectCode(String accountingObjectCode) {
        this.accountingObjectCode = accountingObjectCode;
        return this;
    }

    public void setAccountingObjectCode(String accountingObjectCode) {
        this.accountingObjectCode = accountingObjectCode;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public GeneralLedger accountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
        return this;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getAccountingObjectAddress() {
        return accountingObjectAddress;
    }

    public GeneralLedger accountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
        return this;
    }

    public void setAccountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
    }

    public String getContactName() {
        return contactName;
    }

    public GeneralLedger contactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public GeneralLedger employeeID(UUID employeeID) {
        this.employeeID = employeeID;
        return this;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public GeneralLedger employeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
        return this;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public GeneralLedger employeeName(String employeeName) {
        this.employeeName = employeeName;
        return this;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public GeneralLedger materialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
        return this;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public String getMaterialGoodsCode() {
        return materialGoodsCode;
    }

    public GeneralLedger materialGoodsCode(String materialGoodsCode) {
        this.materialGoodsCode = materialGoodsCode;
        return this;
    }

    public void setMaterialGoodsCode(String materialGoodsCode) {
        this.materialGoodsCode = materialGoodsCode;
    }

    public String getMaterialGoodsName() {
        return materialGoodsName;
    }

    public GeneralLedger materialGoodsName(String materialGoodsName) {
        this.materialGoodsName = materialGoodsName;
        return this;
    }

    public void setMaterialGoodsName(String materialGoodsName) {
        this.materialGoodsName = materialGoodsName;
    }

    public UUID getRepositoryID() {
        return repositoryID;
    }

    public GeneralLedger repositoryID(UUID repositoryID) {
        this.repositoryID = repositoryID;
        return this;
    }

    public void setRepositoryID(UUID repositoryID) {
        this.repositoryID = repositoryID;
    }

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public GeneralLedger repositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
        return this;
    }

    public void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public GeneralLedger repositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
        return this;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public UUID getUnitID() {
        return unitID;
    }

    public GeneralLedger unitID(UUID unitID) {
        this.unitID = unitID;
        return this;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public GeneralLedger quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public GeneralLedger unitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPriceOriginal() {
        return unitPriceOriginal;
    }

    public GeneralLedger unitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
        return this;
    }

    public void setUnitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
    }

    public UUID getMainUnitID() {
        return mainUnitID;
    }

    public GeneralLedger mainUnitID(UUID mainUnitID) {
        this.mainUnitID = mainUnitID;
        return this;
    }

    public void setMainUnitID(UUID mainUnitID) {
        this.mainUnitID = mainUnitID;
    }

    public BigDecimal getMainQuantity() {
        return mainQuantity;
    }

    public GeneralLedger mainQuantity(BigDecimal mainQuantity) {
        this.mainQuantity = mainQuantity;
        return this;
    }

    public void setMainQuantity(BigDecimal mainQuantity) {
        this.mainQuantity = mainQuantity;
    }

    public BigDecimal getMainUnitPrice() {
        return mainUnitPrice;
    }

    public GeneralLedger mainUnitPrice(BigDecimal mainUnitPrice) {
        this.mainUnitPrice = mainUnitPrice;
        return this;
    }

    public void setMainUnitPrice(BigDecimal mainUnitPrice) {
        this.mainUnitPrice = mainUnitPrice;
    }

    public BigDecimal getMainConvertRate() {
        return mainConvertRate;
    }

    public GeneralLedger mainConvertRate(BigDecimal mainConvertRate) {
        this.mainConvertRate = mainConvertRate;
        return this;
    }

    public void setMainConvertRate(BigDecimal mainConvertRate) {
        this.mainConvertRate = mainConvertRate;
    }

    public String getFormula() {
        return formula;
    }

    public GeneralLedger formula(String formula) {
        this.formula = formula;
        return this;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public UUID getDepartmentID() {
        return departmentID;
    }

    public GeneralLedger departmentID(UUID departmentID) {
        this.departmentID = departmentID;
        return this;
    }

    public void setDepartmentID(UUID departmentID) {
        this.departmentID = departmentID;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public GeneralLedger expenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
        return this;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public UUID getBudgetItemID() {
        return budgetItemID;
    }

    public GeneralLedger budgetItemID(UUID budgetItemID) {
        this.budgetItemID = budgetItemID;
        return this;
    }

    public void setBudgetItemID(UUID budgetItemID) {
        this.budgetItemID = budgetItemID;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public GeneralLedger costSetID(UUID costSetID) {
        this.costSetID = costSetID;
        return this;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public UUID getContractID() {
        return contractID;
    }

    public GeneralLedger contractID(UUID contractID) {
        this.contractID = contractID;
        return this;
    }

    public void setContractID(UUID contractID) {
        this.contractID = contractID;
    }

    public UUID getStatisticsCodeID() {
        return statisticsCodeID;
    }

    public GeneralLedger statisticsCodeID(UUID statisticsCodeID) {
        this.statisticsCodeID = statisticsCodeID;
        return this;
    }

    public void setStatisticsCodeID(UUID statisticsCodeID) {
        this.statisticsCodeID = statisticsCodeID;
    }

    public LocalDate getRefDateTime() {
        return refDateTime;
    }

    public GeneralLedger refDateTime(LocalDate refDateTime) {
        this.refDateTime = refDateTime;
        return this;
    }

    public void setRefDateTime(LocalDate refDateTime) {
        this.refDateTime = refDateTime;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public GeneralLedger orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getRefTable() {
        return refTable;
    }

    public void setRefTable(String refTable) {
        this.refTable = refTable;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GeneralLedger generalLedger = (GeneralLedger) o;
        if (generalLedger.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), generalLedger.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GeneralLedger{" +
            "id=" + getId() +
            ", branchID=" + getBranchID() +
            ", referenceID=" + getReferenceID() +
            ", detailID=" + getDetailID() +
            ", typeID=" + getTypeID() +
            ", date='" + getDate() + "'" +
            ", postedDate='" + getPostedDate() + "'" +
            ", typeLedger=" + getTypeLedger() +
            ", noFBook='" + getNoFBook() + "'" +
            ", noMBook='" + getNoMBook() + "'" +
            ", invoiceSeries='" + getInvoiceSeries() + "'" +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", invoiceNo='" + getInvoiceNo() + "'" +
            ", account='" + getAccount() + "'" +
            ", accountCorresponding='" + getAccountCorresponding() + "'" +
            ", bankAccountDetailID=" + getBankAccountDetailID() +
            ", bankAccount='" + getBankAccount() + "'" +
            ", bankName='" + getBankName() + "'" +
            ", currencyID='" + getCurrencyID() + "'" +
            ", exchangeRate=" + getExchangeRate() +
            ", debitAmount=" + getDebitAmount() +
            ", debitAmountOriginal=" + getDebitAmountOriginal() +
            ", creditAmount=" + getCreditAmount() +
            ", creditAmountOriginal=" + getCreditAmountOriginal() +
            ", reason='" + getReason() + "'" +
            ", description='" + getDescription() + "'" +
            ", accountingObjectID=" + getAccountingObjectID() +
            ", accountingObjectCode='" + getAccountingObjectCode() + "'" +
            ", accountingObjectName='" + getAccountingObjectName() + "'" +
            ", accountingObjectAddress='" + getAccountingObjectAddress() + "'" +
            ", contactName='" + getContactName() + "'" +
            ", employeeID=" + getEmployeeID() +
            ", employeeCode='" + getEmployeeCode() + "'" +
            ", employeeName='" + getEmployeeName() + "'" +
            ", materialGoodsID=" + getMaterialGoodsID() +
            ", materialGoodsCode='" + getMaterialGoodsCode() + "'" +
            ", materialGoodsName='" + getMaterialGoodsName() + "'" +
            ", repositoryID=" + getRepositoryID() +
            ", repositoryCode='" + getRepositoryCode() + "'" +
            ", repositoryName='" + getRepositoryName() + "'" +
            ", unitID=" + getUnitID() +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", unitPriceOriginal=" + getUnitPriceOriginal() +
            ", mainUnitID=" + getMainUnitID() +
            ", mainQuantity=" + getMainQuantity() +
            ", mainUnitPrice=" + getMainUnitPrice() +
            ", mainConvertRate=" + getMainConvertRate() +
            ", formula='" + getFormula() + "'" +
            ", departmentID=" + getDepartmentID() +
            ", expenseItemID=" + getExpenseItemID() +
            ", budgetItemID=" + getBudgetItemID() +
            ", costSetID=" + getCostSetID() +
            ", contractID=" + getContractID() +
            ", statisticsCodeID=" + getStatisticsCodeID() +
            ", refDateTime='" + getRefDateTime() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}

