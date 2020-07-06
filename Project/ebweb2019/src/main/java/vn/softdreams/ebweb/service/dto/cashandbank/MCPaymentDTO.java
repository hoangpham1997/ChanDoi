package vn.softdreams.ebweb.service.dto.cashandbank;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class MCPaymentDTO {
    private UUID id;

    private UUID companyID;

    private UUID branchID;

    private Integer typeID;

    private LocalDate date;

    private LocalDate postedDate;

    private Integer typeLedger;

    private String noFBook;

    private String noMBook;

    private UUID accountingObjectID;

    private String accountingObjectName;

    private String accountingObjectAddress;

    private String receiver;

    private String reason;

    private String numberAttach;

    private String currencyID;

    private BigDecimal exchangeRate;

    private Boolean isImportPurchase;

    private UUID paymentClauseID;

    private UUID transportMethodID;

    private BigDecimal totalAmount;

    private BigDecimal totalAmountOriginal;

    private BigDecimal totalVATAmount;

    private BigDecimal totalVATAmountOriginal;

    private UUID employeeID;

    private Integer accountingObjectType;

    private UUID templateID;

    private UUID mCAuditID;

    private String taxCode;

    private Boolean recorded;

    private String typeName;

    private Number total;

    public MCPaymentDTO(UUID id) {
        this.id = id;
    }

    public MCPaymentDTO(UUID id, UUID companyID, UUID branchID, Integer typeID, LocalDate date,
                        LocalDate postedDate, Integer typeLedger, String noFBook, String noMBook,
                        UUID accountingObjectID, String accountingObjectName, String accountingObjectAddress,
                        String receiver, String reason, String taxCode, BigDecimal totalAmount, Boolean recorded,
                        String currencyID,
                        BigDecimal totalAmountOriginal,
                        String typeName) {
        this.id = id;
        this.companyID = companyID;
        this.branchID = branchID;
        this.typeID = typeID;
        this.date = date;
        this.postedDate = postedDate;
        this.typeLedger = typeLedger;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.accountingObjectID = accountingObjectID;
        this.accountingObjectName = accountingObjectName;
        this.accountingObjectAddress = accountingObjectAddress;
        this.receiver = receiver;
        this.reason = reason;
        this.taxCode = taxCode;
        this.totalAmount = totalAmount;
        this.recorded = recorded;
        this.currencyID = currencyID;
        this.totalAmountOriginal = totalAmountOriginal;
        this.typeName = typeName;
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

    public UUID getBranchID() {
        return branchID;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
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

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getaccountingObjectAddress() {
        return accountingObjectAddress;
    }

    public void setaccountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNumberAttach() {
        return numberAttach;
    }

    public void setNumberAttach(String numberAttach) {
        this.numberAttach = numberAttach;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Boolean getImportPurchase() {
        return isImportPurchase;
    }

    public void setImportPurchase(Boolean importPurchase) {
        isImportPurchase = importPurchase;
    }

    public UUID getPaymentClauseID() {
        return paymentClauseID;
    }

    public void setPaymentClauseID(UUID paymentClauseID) {
        this.paymentClauseID = paymentClauseID;
    }

    public UUID getTransportMethodID() {
        return transportMethodID;
    }

    public void setTransportMethodID(UUID transportMethodID) {
        this.transportMethodID = transportMethodID;
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

    public BigDecimal getTotalVATAmount() {
        return totalVATAmount;
    }

    public void setTotalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
    }

    public BigDecimal getTotalVATAmountOriginal() {
        return totalVATAmountOriginal;
    }

    public void setTotalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public Integer getAccountingObjectType() {
        return accountingObjectType;
    }

    public void setAccountingObjectType(Integer accountingObjectType) {
        this.accountingObjectType = accountingObjectType;
    }

    public UUID getTemplateID() {
        return templateID;
    }

    public void setTemplateID(UUID templateID) {
        this.templateID = templateID;
    }

    public UUID getmCAuditID() {
        return mCAuditID;
    }

    public void setmCAuditID(UUID mCAuditID) {
        this.mCAuditID = mCAuditID;
    }

    public String gettaxCode() {
        return taxCode;
    }

    public void settaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Number getTotal() {
        return total;
    }

    public void setTotal(Number total) {
        this.total = total;
    }
}
