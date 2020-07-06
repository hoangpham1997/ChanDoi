package vn.softdreams.ebweb.service.dto;

import org.apache.poi.hpsf.Decimal;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class PPServiceReceiveBillDTO {
    private UUID id;
    private UUID pPID;
    private Integer typeID;
    private LocalDate date;
    private LocalDate postedDate;
    private String noFBook;
    private String noMBook;
    private String description;
    private BigDecimal totalAmount;
    private BigDecimal vATRate;
    private String vATAccount;
    private String invoiceTemplate;
    private String invoiceSeries;
    private String invoiceNo;
    private LocalDate invoiceDate;
    private UUID goodsServicePurchaseID;
    private UUID accountingObjectID;

    public PPServiceReceiveBillDTO(UUID id, UUID pPID, Integer typeID, LocalDate date, LocalDate postedDate, String noFBook, String noMBook, String description, BigDecimal totalAmount,
                                   BigDecimal vATRate, String vATAccount, String invoiceTemplate, String invoiceSeries, String invoiceNo,
                                   LocalDate invoiceDate, UUID goodsServicePurchaseID, UUID accountingObjectID) {
        this.id = id;
        this.pPID = pPID;
        this.typeID = typeID;
        this.date = date;
        this.postedDate = postedDate;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.description = description;
        this.totalAmount = totalAmount;
        this.vATRate = vATRate;
        this.vATAccount = vATAccount;
        this.invoiceTemplate = invoiceTemplate;
        this.invoiceSeries = invoiceSeries;
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.goodsServicePurchaseID = goodsServicePurchaseID;
        this.accountingObjectID = accountingObjectID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public UUID getGoodsServicePurchaseID() {
        return goodsServicePurchaseID;
    }

    public void setGoodsServicePurchaseID(UUID goodsServicePurchaseID) {
        this.goodsServicePurchaseID = goodsServicePurchaseID;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public String getvATAccount() {
        return vATAccount;
    }

    public void setvATAccount(String vATAccount) {
        this.vATAccount = vATAccount;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getvATRate() {
        return vATRate;
    }

    public void setvATRate(BigDecimal vATRate) {
        this.vATRate = vATRate;
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

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getpPID() {
        return pPID;
    }

    public void setpPID(UUID pPID) {
        this.pPID = pPID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }
}
