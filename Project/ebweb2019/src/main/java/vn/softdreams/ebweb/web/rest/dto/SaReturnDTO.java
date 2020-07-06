package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SaReturnDTO {
    private UUID id;
    private Integer typeID;
    private UUID rsInwardOutwardID;
    private LocalDate date;
    private Boolean recorded;
    private LocalDate postedDate;
    private String noFBook;
    private String noMBook;
    private String accountingObjectName;
    private BigDecimal totalAmount;
    private BigDecimal totalDiscountAmount;
    private BigDecimal totalVATAmount;
    private BigDecimal totalPaymentAmount;
    private Integer rowIndex;
    private String currencyID;
    private Integer totalRow;
    private String reason;
    private String invoiceNo;
    private Boolean isDeliveryVoucher;
    private Integer invoiceForm;
    private Number total;

    public SaReturnDTO(UUID id, Integer typeID, UUID rsInwardOutwardID, LocalDate date, Boolean recorded, LocalDate postedDate, String noFBook, String noMBook,
                       String accountingObjectName, BigDecimal totalAmount, BigDecimal totalDiscountAmount, BigDecimal totalVATAmount,
                       BigDecimal totalPaymentAmount, Integer rowIndex, String currencyID, String reason, String invoiceNo, Boolean isDeliveryVoucher,
                       Integer invoiceForm) {
        this.id = id;
        this.typeID = typeID;
        this.rsInwardOutwardID = rsInwardOutwardID;
        this.date = date;
        this.recorded = recorded;
        this.postedDate = postedDate;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.accountingObjectName = accountingObjectName;
        this.totalAmount = totalAmount;
        this.totalDiscountAmount = totalDiscountAmount;
        this.totalVATAmount = totalVATAmount;
        this.totalPaymentAmount = totalPaymentAmount;
        this.rowIndex = rowIndex;
        this.currencyID = currencyID;
        this.reason = reason;
        this.invoiceNo = invoiceNo;
        this.isDeliveryVoucher = isDeliveryVoucher;
        this.invoiceForm = invoiceForm;
    }

    public SaReturnDTO() {
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalVATAmount() {
        return totalVATAmount;
    }

    public void setTotalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
    }

    public BigDecimal getTotalPaymentAmount() {
        return totalPaymentAmount;
    }

    public void setTotalPaymentAmount(BigDecimal totalPaymentAmount) {
        this.totalPaymentAmount = totalPaymentAmount;
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public Integer getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(Integer totalRow) {
        this.totalRow = totalRow;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public UUID getRsInwardOutwardID() {
        return rsInwardOutwardID;
    }

    public void setRsInwardOutwardID(UUID rsInwardOutwardID) {
        this.rsInwardOutwardID = rsInwardOutwardID;
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

    public Number getTotal() {
        return total;
    }

    public void setTotal(Number total) {
        this.total = total;
    }

    public Boolean isIsDeliveryVoucher() {
        return this.isDeliveryVoucher;
    }

    public void setIsDeliveryVoucher(Boolean isDeliveryVoucher) {
        this.isDeliveryVoucher = isDeliveryVoucher;
    }

    public Integer getInvoiceForm() {
        return invoiceForm;
    }

    public void setInvoiceForm(Integer invoiceForm) {
        this.invoiceForm = invoiceForm;
    }
}
