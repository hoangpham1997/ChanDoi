package vn.softdreams.ebweb.service.dto.cashandbank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import vn.softdreams.ebweb.domain.*;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class MBTellerPaperDTO {
    private UUID id;
    private UUID companyId;
    private UUID branchId;
    private Integer typeId;
    private LocalDate date;
    private LocalDate postedDate;
    private Integer typeLedger;
    private String noFBook;
    private String noMBook;
    private String bankName;
    private String reason;
    private Integer accountingObjectType;
    private String accountingObjectName;
    private String accountingObjectAddress;
    private String accountingObjectBankName;
    private String taxCode;
    private String receiver;
    private String identificationNo;
    private LocalDate issueDate;
    private String issueBy;
    private String currencyId;
    private BigDecimal exchangeRate;
    private Boolean isImportPurchase;
    private BigDecimal totalAmount;
    private BigDecimal totalAmountOriginal;
    private BigDecimal totalVatAmount;
    private BigDecimal totalVatAmountOriginal;
    private UUID templateId;
    private Boolean isMatch;
    private LocalDate matchDate;
    private Boolean recorded;
    private Number total;
    private String typeName;

    public MBTellerPaperDTO(UUID id, UUID companyId, UUID branchId,
                            Integer typeId, LocalDate date, LocalDate postedDate,
                            Integer typeLedger, String noFBook, String noMBook,
                            String accountingObjectName, String accountingObjectAddress,
                            String reason, BigDecimal totalAmount, BigDecimal totalAmountOriginal,
                            Boolean recorded, String currencyId, String typeName
    ) {
        this.id = id;
        this.companyId = companyId;
        this.branchId = branchId;
        this.typeId = typeId;
        this.date = date;
        this.postedDate = postedDate;
        this.typeLedger = typeLedger;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.accountingObjectName = accountingObjectName;
        this.accountingObjectAddress = accountingObjectAddress;
        this.reason = reason;
        this.totalAmount = totalAmount;
        this.totalAmountOriginal = totalAmountOriginal;
        this.recorded = recorded;
        this.currencyId = currencyId;
        this.typeName = typeName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public UUID getBranchId() {
        return branchId;
    }

    public void setBranchId(UUID branchId) {
        this.branchId = branchId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getAccountingObjectType() {
        return accountingObjectType;
    }

    public void setAccountingObjectType(Integer accountingObjectType) {
        this.accountingObjectType = accountingObjectType;
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

    public String getAccountingObjectBankName() {
        return accountingObjectBankName;
    }

    public void setAccountingObjectBankName(String accountingObjectBankName) {
        this.accountingObjectBankName = accountingObjectBankName;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getIdentificationNo() {
        return identificationNo;
    }

    public void setIdentificationNo(String identificationNo) {
        this.identificationNo = identificationNo;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssueBy() {
        return issueBy;
    }

    public void setIssueBy(String issueBy) {
        this.issueBy = issueBy;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
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

    public BigDecimal getTotalVatAmount() {
        return totalVatAmount;
    }

    public void setTotalVatAmount(BigDecimal totalVatAmount) {
        this.totalVatAmount = totalVatAmount;
    }

    public BigDecimal getTotalVatAmountOriginal() {
        return totalVatAmountOriginal;
    }

    public void setTotalVatAmountOriginal(BigDecimal totalVatAmountOriginal) {
        this.totalVatAmountOriginal = totalVatAmountOriginal;
    }

    public UUID getTemplateId() {
        return templateId;
    }

    public void setTemplateId(UUID templateId) {
        this.templateId = templateId;
    }

    public Boolean getMatch() {
        return isMatch;
    }

    public void setMatch(Boolean match) {
        isMatch = match;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public Number getTotal() {
        return total;
    }

    public void setTotal(Number total) {
        this.total = total;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
