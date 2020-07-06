package vn.softdreams.ebweb.service.dto;
import vn.softdreams.ebweb.domain.*;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RSInwardOutwardDTO {
    private UUID id;
    private UUID companyID;
    private UUID branchID;
    private Integer typeID;
    private String typeName;
    private LocalDate postedDate;
    private LocalDate date;
    private Integer typeLedger;
    private String noFBook;
    private String noMBook;
    private UUID accountingObjectID;
    private String accountingObjectName;
    private String accountingObjectCode;
    private String accountingObjectAddress;
    private String contactName;
    private String reason;
    private String currencyID;
    private BigDecimal exchangeRate;
    private UUID employeeID;
    private Boolean isImportPurchase;
    private UUID transportMethodID;
    private BigDecimal totalAmount;
    private BigDecimal totalAmountOriginal;
    private UUID templateID;
    private Boolean recorded;
    private Boolean exported;
    private String numberAttach;
    private Set<RSInwardOutwardDetailsDTO> rsInwardOutwardDetails = new HashSet<>();

    public Set<RSInwardOutwardDetailsDTO> getRsInwardOutwardDetails() {
        return rsInwardOutwardDetails;
    }

    public void setRsInwardOutwardDetails(Set<RSInwardOutwardDetailsDTO> rsInwardOutwardDetails) {
        this.rsInwardOutwardDetails = rsInwardOutwardDetails;
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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public String getAccountingObjectCode() {
        return accountingObjectCode;
    }

    public void setAccountingObjectCode(String accountingObjectCode) {
        this.accountingObjectCode = accountingObjectCode;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public UUID getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public Boolean getImportPurchase() {
        return isImportPurchase;
    }

    public void setImportPurchase(Boolean importPurchase) {
        isImportPurchase = importPurchase;
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

    public UUID getTemplateID() {
        return templateID;
    }

    public void setTemplateID(UUID templateID) {
        this.templateID = templateID;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public Boolean getExported() {
        return exported;
    }

    public void setExported(Boolean exported) {
        this.exported = exported;
    }

    public String getNumberAttach() {
        return numberAttach;
    }

    public void setNumberAttach(String numberAttach) {
        this.numberAttach = numberAttach;
    }

    public RSInwardOutwardDTO(UUID id, UUID companyID, UUID branchID, Integer typeID, String typeName, LocalDate postedDate, LocalDate date, Integer typeLedger, String noFBook, String noMBook, UUID accountingObjectID, String accountingObjectName, String accountingObjectCode, String accountingObjectAddress, String contactName, String reason, String currencyID, BigDecimal exchangeRate, UUID employeeID, Boolean isImportPurchase, UUID transportMethodID, BigDecimal totalAmount, BigDecimal totalAmountOriginal, UUID templateID, Boolean recorded, Boolean exported, String numberAttach) {
        this.id = id;
        this.companyID = companyID;
        this.branchID = branchID;
        this.typeID = typeID;
        this.typeName = typeName;
        this.postedDate = postedDate;
        this.date = date;
        this.typeLedger = typeLedger;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.accountingObjectID = accountingObjectID;
        this.accountingObjectName = accountingObjectName;
        this.accountingObjectCode = accountingObjectCode;
        this.accountingObjectAddress = accountingObjectAddress;
        this.contactName = contactName;
        this.reason = reason;
        this.currencyID = currencyID;
        this.exchangeRate = exchangeRate;
        this.employeeID = employeeID;
        this.isImportPurchase = isImportPurchase;
        this.transportMethodID = transportMethodID;
        this.totalAmount = totalAmount;
        this.totalAmountOriginal = totalAmountOriginal;
        this.templateID = templateID;
        this.recorded = recorded;
        this.exported = exported;
        this.numberAttach = numberAttach;
    }

    public RSInwardOutwardDTO() {
    }
}
