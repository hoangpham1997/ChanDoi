package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class RSTransferSearchDTO {
    private Integer typeID;
    private UUID id;
    private LocalDate date;
    private LocalDate postedDate;
    private String noFBook;
    private String noMBook;
    private String accountingObjectName;
    private String typeName;
    private String reason;
    private BigDecimal totalAmount;
    private Boolean recorded;
    private String invoiceNo;
    private String employeeName;
    private BigDecimal sumTotalAmount;
    private Integer refInvoiceForm;
    private String refInvoiceNo;
    private Boolean refIsBill;
    private Integer typeLedger;

    public RSTransferSearchDTO() {
    }

    public RSTransferSearchDTO(Integer typeID, UUID id, LocalDate date, LocalDate postedDate, String noFBook, String noMBook, String accountingObjectName, String typeName, String reason, BigDecimal totalAmount, Boolean recorded, String invoiceNo, String employeeName) {
        this.typeID = typeID;
        this.id = id;
        this.date = date;
        this.postedDate = postedDate;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.accountingObjectName = accountingObjectName;
        this.typeName = typeName;
        this.reason = reason;
        this.totalAmount = totalAmount;
        this.recorded = recorded;
        this.invoiceNo = invoiceNo;
        this.employeeName = employeeName;
    }

    public RSTransferSearchDTO(Integer typeID, UUID id) {
        this.typeID = typeID;
        this.id = id;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public BigDecimal getSumTotalAmount() {
        return sumTotalAmount;
    }

    public void setSumTotalAmount(BigDecimal sumTotalAmount) {
        this.sumTotalAmount = sumTotalAmount;
    }

    public Integer getRefInvoiceForm() {
        return refInvoiceForm;
    }

    public void setRefInvoiceForm(Integer refInvoiceForm) {
        this.refInvoiceForm = refInvoiceForm;
    }

    public String getRefInvoiceNo() {
        return refInvoiceNo;
    }

    public void setRefInvoiceNo(String refInvoiceNo) {
        this.refInvoiceNo = refInvoiceNo;
    }

    public Boolean getRefIsBill() {
        return refIsBill;
    }

    public void setRefIsBill(Boolean refIsBill) {
        this.refIsBill = refIsBill;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }
}
