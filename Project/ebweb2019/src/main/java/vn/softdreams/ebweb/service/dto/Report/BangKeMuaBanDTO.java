package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BangKeMuaBanDTO {
    private UUID id;
    private Integer goodsServicePurchaseCode;
    private String goodsServicePurchaseName;
    private String invoiceNo;
    private LocalDate invoiceDate;
    private String invoiceDateString;
    private String accountingObjectName;
    private String companyTaxCode;
    private String description;
    private BigDecimal amount;
    private String amountString;
    private BigDecimal vatRate;
    private String vatRateString;
    private BigDecimal vatAmount;
    private String vatAmountString;
    private String vatAccount;
    private List<String> vatAccounts;
    private String textColor;
    private String textColorVAT;
    private Integer orderPriority;
    private String linkRef;
    private Integer typeID;
    private Boolean isBold;
//    private UUID accountingObjectID;
//    private UUID refID;
//    private Integer typeId;

    public BangKeMuaBanDTO(Integer goodsServicePurchaseCode, String goodsServicePurchaseName,
                           String invoiceNo, LocalDate invoiceDate, String accountingObjectName,
                           String companyTaxCode, String description, BigDecimal amount, BigDecimal vatRate,
                           BigDecimal vatAmount, String vatAccount, Integer orderPriority) {
        this.goodsServicePurchaseCode = goodsServicePurchaseCode;
        this.goodsServicePurchaseName = goodsServicePurchaseName;
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.accountingObjectName = accountingObjectName;
        this.companyTaxCode = companyTaxCode;
        this.description = description;
        this.amount = amount;
        this.vatRate = vatRate;
        this.vatAmount = vatAmount;
        this.vatAccount = vatAccount;
        this.orderPriority = orderPriority;
//        this.accountingObjectID = accountingObjectID;
//        this.refID = refID;
//        this.typeId = typeId;
    }

    public BangKeMuaBanDTO(UUID id, String invoiceNo, LocalDate invoiceDate, String accountingObjectName,
                           String companyTaxCode, String description, BigDecimal amount, BigDecimal vatRate,
                           BigDecimal vatAmount, String vatAccount, Integer orderPriority, Integer typeID) {
        this.id = id;
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.vatRate = vatRate;
        this.accountingObjectName = accountingObjectName;
        this.companyTaxCode = companyTaxCode;
        this.description = description;
        this.amount = amount;
        this.vatAmount = vatAmount;
        this.vatAccount = vatAccount;
        this.orderPriority = orderPriority;
        this.typeID = typeID;
    }

    public BangKeMuaBanDTO() {
        this.goodsServicePurchaseCode = 0;
        this.goodsServicePurchaseName = "";
        this.vatRateString = "";
        this.amountString = "";
        this.vatAmountString = "";
        this.vatAccount = "";
    }

    public Integer getOrderPriority() {
        return orderPriority != null ? orderPriority : 0;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public List<String> getVatAccounts() {
        return vatAccounts;
    }

    public void setVatAccounts(List<String> vatAccounts) {
        this.vatAccounts = vatAccounts;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getTextColorVAT() {
        return textColorVAT;
    }

    public void setTextColorVAT(String textColorVAT) {
        this.textColorVAT = textColorVAT;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInvoiceDateString() {
        return invoiceDateString;
    }

    public void setInvoiceDateString(String invoiceDateString) {
        this.invoiceDateString = invoiceDateString;
    }

    public String getAmountString() {
        return amountString;
    }

    public void setAmountString(String amountString) {
        this.amountString = amountString.equals("0") ? "" : amountString;
    }

    public String getVatRateString() {
        return vatRateString;
    }

    public void setVatRateString(String vatRateString) {
        this.vatRateString = vatRateString;
    }

    public String getVatAmountString() {
        return vatAmountString;
    }

    public void setVatAmountString(String vatAmountString) {
        this.vatAmountString = vatAmountString.equals("0") ? "" : vatAmountString;
    }

    public Integer getGoodsServicePurchaseCode() {
        return goodsServicePurchaseCode;
    }

    public void setGoodsServicePurchaseCode(Integer goodsServicePurchaseCode) {
        this.goodsServicePurchaseCode = goodsServicePurchaseCode;
    }

    public String getGoodsServicePurchaseName() {
        return goodsServicePurchaseName;
    }

    public void setGoodsServicePurchaseName(String goodsServicePurchaseName) {
        this.goodsServicePurchaseName = goodsServicePurchaseName;
    }

    public String getInvoiceNo() {
        return invoiceNo != null ? invoiceNo : "";
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

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getCompanyTaxCode() {
        return companyTaxCode;
    }

    public void setCompanyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
    }

    public BigDecimal getAmount() {
        return amount != null ? amount : BigDecimal.ZERO;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    public BigDecimal getVatAmount() {
        return vatAmount != null ? vatAmount : BigDecimal.ZERO;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public String getVatAccount() {
        return vatAccount;
    }

    public void setVatAccount(String vatAccount) {
        this.vatAccount = vatAccount;
    }

    public String getLinkRef() {
        return linkRef;
    }

    public void setLinkRef(String linkRef) {
        this.linkRef = linkRef;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    //    public UUID getAccountingObjectID() {
//        return accountingObjectID;
//    }
//
//    public void setAccountingObjectID(UUID accountingObjectID) {
//        this.accountingObjectID = accountingObjectID;
//    }
//
//    public UUID getRefID() {
//        return refID;
//    }
//
//    public void setRefID(UUID refID) {
//        this.refID = refID;
//    }
//
//    public Integer getTypeId() {
//        return typeId;
//    }
//
//    public void setTypeId(Integer typeId) {
//        this.typeId = typeId;
//    }

    public Boolean getBold() {
        return isBold;
    }

    public void setBold(Boolean bold) {
        isBold = bold;
    }
}
