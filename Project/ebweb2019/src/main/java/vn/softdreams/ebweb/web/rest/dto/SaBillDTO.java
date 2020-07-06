package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class SaBillDTO {
    private UUID id;

    private String accountingObjectName;

    private BigDecimal totalAmount;

    private BigDecimal totalDiscountAmount;

    private BigDecimal totalVATAmount;

    private String invoiceTemplate;

    private String invoiceSeries;

    private String invoiceNo;

    private LocalDate invoiceDate;

    private Integer rowIndex;

    private String currencyID;

    private Integer invoiceForm;

    private Integer totalRow;

    private Number total;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getInvoiceTemplate() {
        return invoiceTemplate;
    }

    public void setInvoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
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

    public Integer getInvoiceForm() {
        return invoiceForm;
    }

    public void setInvoiceForm(Integer invoiceForm) {
        this.invoiceForm = invoiceForm;
    }

    public SaBillDTO(UUID id, String accountingObjectName, BigDecimal totalAmount, BigDecimal totalDiscountAmount,
                     BigDecimal totalVATAmount, String invoiceTemplate, String invoiceSeries, String invoiceNo,
                     LocalDate invoiceDate, Integer rowIndex, String currencyID, Integer invoiceForm) {
        this.id = id;
        this.accountingObjectName = accountingObjectName;
        this.totalAmount = totalAmount;
        this.totalDiscountAmount = totalDiscountAmount;
        this.totalVATAmount = totalVATAmount;
        this.invoiceTemplate = invoiceTemplate;
        this.invoiceSeries = invoiceSeries;
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.rowIndex = rowIndex;
        this.currencyID = currencyID;
        this.invoiceForm = invoiceForm;
    }

    public SaBillDTO() {
    }

    public Integer getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(Integer totalRow) {
        this.totalRow = totalRow;
    }

    public Number getTotal() {
        return total;
    }

    public void setTotal(Number total) {
        this.total = total;
    }
}
