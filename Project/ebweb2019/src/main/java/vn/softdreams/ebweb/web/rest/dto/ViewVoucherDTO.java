package vn.softdreams.ebweb.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import vn.softdreams.ebweb.domain.AccountingObject;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ViewVoucherDTO {

    private UUID id;

    private String refID;

    private Integer typeID;

    private Integer typeGroupID;

    private UUID companyID;

    private UUID branchID;

    private Integer typeLedger;

    private String noMBook;

    private String noFBook;

    private LocalDate date;

    private LocalDate postedDate;

    private String currencyID;

    private String reason;

    private UUID accountingObjectID;

    private UUID employeeID;

    private Boolean recorded;

    private BigDecimal totalAmount;

    private BigDecimal totalAmountOriginal;

    private String refTable;

    private String no;

    private String noNew;

    private String typeName;

    public ViewVoucherDTO(UUID id, String refID, Integer typeID, Integer typeGroupID, UUID companyID, UUID branchID, Integer typeLedger, String noMBook, String noFBook, LocalDate date, LocalDate postedDate, String currencyID, String reason, UUID accountingObjectID, UUID employeeID, Boolean recorded, BigDecimal totalAmount, BigDecimal totalAmountOriginal, String refTable) {
        this.id = id;
        this.refID = refID;
        this.typeID = typeID;
        this.typeGroupID = typeGroupID;
        this.companyID = companyID;
        this.branchID = branchID;
        this.typeLedger = typeLedger;
        this.noMBook = noMBook;
        this.noFBook = noFBook;
        this.date = date;
        this.postedDate = postedDate;
        this.currencyID = currencyID;
        this.reason = reason;
        this.accountingObjectID = accountingObjectID;
        this.employeeID = employeeID;
        this.recorded = recorded;
        this.totalAmount = totalAmount;
        this.totalAmountOriginal = totalAmountOriginal;
        this.refTable = refTable;
    }

    public ViewVoucherDTO(UUID id, Integer typeID, UUID companyID, String no, LocalDate date, LocalDate postedDate, String reason, Boolean recorded) {
        this.id = id;
        this.typeID = typeID;
        this.companyID = companyID;
        this.date = date;
        this.no = no;
        this.postedDate = postedDate;
        this.reason = reason;
        this.recorded = recorded;
    }

    public ViewVoucherDTO(UUID id, Integer typeID, Integer typeGroupID, UUID companyID, String noMBook, String noFBook, LocalDate date, LocalDate postedDate, String reason, Boolean recorded, String typeName, String refTable) {
        this.id = id;
        this.typeID = typeID;
        this.typeGroupID = typeGroupID;
        this.companyID = companyID;
        this.noMBook = noMBook;
        this.noFBook = noFBook;
        this.date = date;
        this.postedDate = postedDate;
        this.reason = reason;
        this.recorded = recorded;
        this.typeName = typeName;
        this.refTable = refTable;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public ViewVoucherDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRefID() {
        return refID;
    }

    public void setRefID(String refID) {
        this.refID = refID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public Integer getTypeGroupID() {
        return typeGroupID;
    }

    public void setTypeGroupID(Integer typeGroupID) {
        this.typeGroupID = typeGroupID;
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

    public String getRefTable() {
        return refTable;
    }

    public void setRefTable(String refTable) {
        this.refTable = refTable;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getNoNew() {
        return noNew;
    }

    public void setNoNew(String noNew) {
        this.noNew = noNew;
    }
}
