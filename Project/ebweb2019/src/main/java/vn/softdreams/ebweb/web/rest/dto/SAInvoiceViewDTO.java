package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SAInvoiceViewDTO {
    private UUID id;
    private Integer typeID;
    private LocalDate date;
    private LocalDate postedDate;
    private String noBook;
    private String invoiceNo;
    private String accountingObjectName;
    private String accountingObjectAddress;
    private String reason;
    private BigDecimal totalAmount;
    private BigDecimal totalDiscountAmount;
    private BigDecimal totalVATAmount;
    private BigDecimal totalAllAmount;
    private Boolean recorded;
    private String currencyID;
    private Boolean exported;
    private UUID rsInwardOutwardID;
    private UUID mcReceiptID;
    private UUID mbDepositID;
    private Integer invoiceForm;
    private String typeName;
    private Number total;

    public SAInvoiceViewDTO() {
    }

    public SAInvoiceViewDTO(UUID id, Integer typeID, LocalDate date, LocalDate postedDate, String noBook, String invoiceNo, String accountingObjectName, String accountingObjectAddress, String reason, BigDecimal totalAmount, BigDecimal totalDiscountAmount, BigDecimal totalVATAmount, BigDecimal totalAllAmount, Boolean recorded, String currencyID, Boolean exported, UUID rsInwardOutwardID, UUID mcReceiptID, UUID mbDepositID,
                            Integer invoiceForm, String typeName) {
        this.id = id;
        this.typeID = typeID;
        this.date = date;
        this.postedDate = postedDate;
        this.noBook = noBook;
        this.invoiceNo = invoiceNo;
        this.accountingObjectName = accountingObjectName;
        this.accountingObjectAddress = accountingObjectAddress;
        this.reason = reason;
        this.totalAmount = totalAmount;
        this.totalDiscountAmount = totalDiscountAmount;
        this.totalVATAmount = totalVATAmount;
        this.totalAllAmount = totalAllAmount;
        this.recorded = recorded;
        this.currencyID = currencyID;
        this.exported = exported;
        this.rsInwardOutwardID = rsInwardOutwardID;
        this.mcReceiptID = mcReceiptID;
        this.mbDepositID = mbDepositID;
        this.invoiceForm = invoiceForm;
        this.typeName = typeName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getNoBook() {
        return noBook;
    }

    public void setNoBook(String noBook) {
        this.noBook = noBook;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
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

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public BigDecimal getTotalVATAmount() {
        return totalVATAmount;
    }

    public void setTotalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
    }

    public BigDecimal getTotalAllAmount() {
        return totalAllAmount;
    }

    public void setTotalAllAmount(BigDecimal totalAllAmount) {
        this.totalAllAmount = totalAllAmount;
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

    public UUID getRsInwardOutwardID() {
        return rsInwardOutwardID;
    }

    public void setRsInwardOutwardID(UUID rsInwardOutwardID) {
        this.rsInwardOutwardID = rsInwardOutwardID;
    }

    public UUID getMcReceiptID() {
        return mcReceiptID;
    }

    public void setMcReceiptID(UUID mcReceiptID) {
        this.mcReceiptID = mcReceiptID;
    }

    public UUID getMbDepositID() {
        return mbDepositID;
    }

    public void setMbDepositID(UUID mbDepositID) {
        this.mbDepositID = mbDepositID;
    }

    public Number getTotal() {
        return total;
    }

    public void setTotal(Number total) {
        this.total = total;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public Integer getInvoiceForm() {
        return invoiceForm;
    }

    public void setInvoiceForm(Integer invoiceForm) {
        this.invoiceForm = invoiceForm;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
