package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import vn.softdreams.ebweb.service.dto.ViewVoucherNoDetailDTO;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Immutable()
@Subselect(
    "SELECT * FROM ViewVoucherNoForCloseBook"
)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "ViewVoucherNoDetailDTO",
        classes = {
            @ConstructorResult(
                targetClass = ViewVoucherNoDetailDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "refID", type = UUID.class),
                    @ColumnResult(name = "refParentID", type = UUID.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "bankAccountDetailID", type = UUID.class),
                    @ColumnResult(name = "bankAccount", type = String.class),
                    @ColumnResult(name = "bankName", type = String.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "amountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "vATRate", type = BigDecimal.class),
                    @ColumnResult(name = "vATAccount", type = String.class),
                    @ColumnResult(name = "deductionDebitAccount", type = String.class),
                    @ColumnResult(name = "vATAmount", type = BigDecimal.class),
                    @ColumnResult(name = "vATAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "discountRate", type = BigDecimal.class),
                    @ColumnResult(name = "discountAccount", type = String.class),
                    @ColumnResult(name = "discountAmount", type = BigDecimal.class),
                    @ColumnResult(name = "discountAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "exportTaxAmount", type = BigDecimal.class),
                    @ColumnResult(name = "exportTaxAmountAccount", type = String.class),
                    @ColumnResult(name = "exportTaxAccountCorresponding", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "accountingObjectCode", type = String.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "accountingObjectAddress", type = String.class),
                    @ColumnResult(name = "debitAccountingObjectID", type = UUID.class),
                    @ColumnResult(name = "debitAccountingObjectCode", type = String.class),
                    @ColumnResult(name = "debitAccountingObjectName", type = String.class),
                    @ColumnResult(name = "debitAccountingObjectAddress", type = String.class),
                    @ColumnResult(name = "creditAccountingObjectID", type = UUID.class),
                    @ColumnResult(name = "creditAccountingObjectCode", type = String.class),
                    @ColumnResult(name = "creditAccountingObjectName", type = String.class),
                    @ColumnResult(name = "creditAccountingObjectAddress", type = String.class),
                    @ColumnResult(name = "employeeID", type = UUID.class),
                    @ColumnResult(name = "employeeCode", type = String.class),
                    @ColumnResult(name = "employeeName", type = String.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "materialGoodsName", type = String.class),
                    @ColumnResult(name = "materialGoodsType", type = Integer.class),
                    @ColumnResult(name = "repositoryID", type = UUID.class),
                    @ColumnResult(name = "repositoryCode", type = String.class),
                    @ColumnResult(name = "repositoryName", type = String.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "unitPriceOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "mainUnitID", type = UUID.class),
                    @ColumnResult(name = "mainQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "mainUnitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "mainConvertRate", type = BigDecimal.class),
                    @ColumnResult(name = "formula", type = String.class),
                    @ColumnResult(name = "departmentID", type = UUID.class),
                    @ColumnResult(name = "expenseItemID", type = UUID.class),
                    @ColumnResult(name = "budgetItemID", type = UUID.class),
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "contractID", type = UUID.class),
                    @ColumnResult(name = "statisticsCodeID", type = UUID.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
                    @ColumnResult(name = "refTable", type = String.class),
                    @ColumnResult(name = "expiryDate", type = LocalDate.class),
                    @ColumnResult(name = "lotNo", type = String.class),
                    @ColumnResult(name = "confrontID", type = UUID.class),
                    @ColumnResult(name = "confrontDetailID", type = UUID.class),
                    @ColumnResult(name = "repositoryAccount", type = String.class),
                    @ColumnResult(name = "costAccount", type = String.class),
                    @ColumnResult(name = "vatDescription", type = String.class),
                    @ColumnResult(name = "invoiceNo", type = String.class),
                    @ColumnResult(name = "oWPrice", type = BigDecimal.class),
                    @ColumnResult(name = "oWAmount", type = BigDecimal.class),
                    @ColumnResult(name = "PPOrderDetailId", type = UUID.class),
                    @ColumnResult(name = "importTaxAmount", type = BigDecimal.class),
                    @ColumnResult(name = "importTaxAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "importTaxAccount", type = String.class),
                    @ColumnResult(name = "specialConsumeTaxAmount", type = BigDecimal.class),
                    @ColumnResult(name = "specialConsumeTaxAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "specialConsumeTaxAccount", type = String.class),
                    @ColumnResult(name = "PPOrderDetailQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "isPromotion", type = Boolean.class),
                    @ColumnResult(name = "inwardAmount", type = BigDecimal.class),
                    @ColumnResult(name = "fromRepositoryID", type = UUID.class),
                    @ColumnResult(name = "toRepositoryID", type = UUID.class)
                }
            )
        }
    )})

public class ViewVoucherNo {
    @Id
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "typeid")
    private Integer typeID;

    @Column(name = "refid")
    private UUID refID;

    @Column(name = "typegroupid")
    private Integer typeGroupID;

    @Column(name = "typegroupname")
    private String typeGroupName;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Column(name = "nombook")
    private String noMBook;

    @Column(name = "nofbook")
    private String noFBook;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "posteddate")
    private LocalDate postedDate;

    @Column(name = "currencyid")
    private String currencyID;

    @Column(name = "reason")
    private String reason;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

    @Column(name = "employeeid")
    private UUID employeeID;

    @Column(name = "recorded")
    private Boolean recorded;

    @Column(name = "totalamount")
    private BigDecimal totalAmount;

    @Column(name = "totalamountoriginal")
    private BigDecimal totalAmountOriginal;

    @Column(name = "invoiceseries")
    private String invoiceSeries;

    @Column(name = "invoicedate")
    private LocalDate invoiceDate;

    @Column(name = "invoiceno")
    private String invoiceNo;

    @Column(name = "bankaccountdetailid")
    private UUID bankAccountDetailID;

    @Column(name = "bankaccount")
    private String bankAccount;

    @Column(name = "bankname")
    private String bankName;

    @Column(name = "exchangerate")
    private BigDecimal exchangeRate;

    @Column(name = "description")
    private String description;

    @Column(name = "accountingobjectcode")
    private String accountingObjectCode;

    @Column(name = "accountingobjectname")
    private String accountingObjectName;

    @Column(name = "accountingobjectaddress")
    private String accountingObjectAddress;

    @Column(name = "contactname")
    private String contactName;

    @Column(name = "employeecode")
    private String employeeCode;

    @Column(name = "employeename")
    private String employeeName;

    @Column(name = "mcreceiptid")
    private UUID mCReceiptID;

    @Column(name = "mbdepositid")
    private UUID mBDepositID;

    @Column(name = "paymentvoucherid")
    private UUID paymentVoucherID;

    @Column(name = "rsinwardoutwardid")
    private UUID rSInwardOutwardID;

    @Column(name = "refdatetime")
    private LocalDate refDateTime;

    @Column(name = "reftable")
    private String refTable;

    @Column(name = "storedInRepository")
    private Boolean storedInRepository;

    @Column(name = "exported")
    private Boolean exported;

    @Column(name = "isdeliveryvoucher")
    private Boolean isDeliveryVoucher;

    @Column(name = "isimportpurchase")
    private Boolean isImportPurchase;

    @Column(name = "invoiceform")
    private Integer invoiceForm;

    @Transient
    private List<ViewVoucherNoDetailDTO> viewVoucherNoDetailDTOS;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private LocalDate postedDateChange;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Boolean checked1;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Boolean checked2;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Boolean checked3;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String reasonFail;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String typeName;

    public Boolean getStoredInRepository() {
        return storedInRepository;
    }

    public void setStoredInRepository(Boolean storedInRepository) {
        this.storedInRepository = storedInRepository;
    }

    public String getReasonFail() {
        return reasonFail;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setReasonFail(String reasonFail) {
        this.reasonFail = reasonFail;
    }

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

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public String getTypeGroupName() {
        return typeGroupName;
    }

    public void setTypeGroupName(String typeGroupName) {
        this.typeGroupName = typeGroupName;
    }

    public UUID getRefID() {
        return refID;
    }

    public void setRefID(UUID refID) {
        this.refID = refID;
    }

    public Integer getTypeGroupID() {
        return typeGroupID;
    }

    public void setTypeGroupID(Integer typeGroupID) {
        this.typeGroupID = typeGroupID;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public UUID getBankAccountDetailID() {
        return bankAccountDetailID;
    }

    public void setBankAccountDetailID(UUID bankAccountDetailID) {
        this.bankAccountDetailID = bankAccountDetailID;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccountingObjectCode() {
        return accountingObjectCode;
    }

    public void setAccountingObjectCode(String accountingObjectCode) {
        this.accountingObjectCode = accountingObjectCode;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getAccountingObjectAddress() {
        return accountingObjectAddress;
    }

    public void setAccountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public LocalDate getRefDateTime() {
        return refDateTime;
    }

    public void setRefDateTime(LocalDate refDateTime) {
        this.refDateTime = refDateTime;
    }

    public List<ViewVoucherNoDetailDTO> getViewVoucherNoDetailDTOS() {
        return viewVoucherNoDetailDTOS;
    }

    public void setViewVoucherNoDetailDTOS(List<ViewVoucherNoDetailDTO> viewVoucherNoDetailDTOS) {
        this.viewVoucherNoDetailDTOS = viewVoucherNoDetailDTOS;
    }

    public String getRefTable() {
        return refTable;
    }

    public void setRefTable(String refTable) {
        this.refTable = refTable;
    }

    public UUID getmCReceiptID() {
        return mCReceiptID;
    }

    public void setmCReceiptID(UUID mCReceiptID) {
        this.mCReceiptID = mCReceiptID;
    }

    public UUID getmBDepositID() {
        return mBDepositID;
    }

    public void setmBDepositID(UUID mBDepositID) {
        this.mBDepositID = mBDepositID;
    }

    public UUID getPaymentVoucherID() {
        return paymentVoucherID;
    }

    public void setPaymentVoucherID(UUID paymentVoucherID) {
        this.paymentVoucherID = paymentVoucherID;
    }

    public UUID getrSInwardOutwardID() {
        return rSInwardOutwardID;
    }

    public void setrSInwardOutwardID(UUID rSInwardOutwardID) {
        this.rSInwardOutwardID = rSInwardOutwardID;
    }

    public LocalDate getPostedDateChange() {
        return postedDateChange;
    }

    public void setPostedDateChange(LocalDate postedDateChange) {
        this.postedDateChange = postedDateChange;
    }

    public Boolean getChecked1() {
        return checked1;
    }

    public void setChecked1(Boolean checked1) {
        this.checked1 = checked1;
    }

    public Boolean getChecked2() {
        return checked2;
    }

    public void setChecked2(Boolean checked2) {
        this.checked2 = checked2;
    }

    public Boolean getChecked3() {
        return checked3;
    }

    public void setChecked3(Boolean checked3) {
        this.checked3 = checked3;
    }

    public Boolean getExported() {
        return exported;
    }

    public void setExported(Boolean exported) {
        this.exported = exported;
    }

    public Boolean getDeliveryVoucher() {
        return isDeliveryVoucher;
    }

    public void setDeliveryVoucher(Boolean deliveryVoucher) {
        isDeliveryVoucher = deliveryVoucher;
    }

    public Boolean getImportPurchase() {
        return isImportPurchase;
    }

    public void setImportPurchase(Boolean importPurchase) {
        isImportPurchase = importPurchase;
    }

    public Integer getInvoiceForm() {
        return invoiceForm;
    }

    public void setInvoiceForm(Integer invoiceForm) {
        this.invoiceForm = invoiceForm;
    }
}
