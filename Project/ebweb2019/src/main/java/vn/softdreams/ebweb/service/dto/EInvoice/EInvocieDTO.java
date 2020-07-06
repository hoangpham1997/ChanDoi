package vn.softdreams.ebweb.service.dto.EInvoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class EInvocieDTO {
    private UUID id;
    private UUID companyID;
    private Integer typeID;
    private String invoiceTemplate;
    private String invoiceSeries;
    private String invoiceNo;
    private LocalDate invoiceDate;
    private String accountingObjectCode;
    private String contactName;
    private String accountingObjectName;
    private String accountingObjectAddress;
    private String accountingObjectBankName;
    private String accountingObjectBankAccount;
    private String contactMobile;
    private String paymentMethod;
    private String companyTaxCode;
    private BigDecimal exchangeRate;
    private String currencyID;
    private BigDecimal totalAmountOriginal;
    private BigDecimal totalAmount;
    private Integer statusInvoice;
    private Integer statusSendMail;

    public EInvocieDTO(UUID id,
                       UUID companyID,
                       Integer typeID,
                       String invoiceTemplate,
                       String invoiceSeries,
                       String invoiceNo,
                       LocalDate invoiceDate,
                       String accountingObjectCode,
                       String contactName,
                       String accountingObjectName,
                       String accountingObjectAddress,
                       String accountingObjectBankName,
                       String accountingObjectBankAccount,
                       String contactMobile,
                       String paymentMethod,
                       String companyTaxCode,
                       BigDecimal exchangeRate,
                       String currencyID,
                       BigDecimal totalAmountOriginal,
                       BigDecimal totalAmount,
                       Integer statusInvoice,
                       Integer statusSendMail) {
        this.id = id;
        this.companyID = companyID;
        this.typeID = typeID;
        this.invoiceTemplate = invoiceTemplate;
        this.invoiceSeries = invoiceSeries;
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.accountingObjectCode = accountingObjectCode;
        this.contactName = contactName;
        this.accountingObjectName = accountingObjectName;
        this.accountingObjectAddress = accountingObjectAddress;
        this.accountingObjectBankName = accountingObjectBankName;
        this.accountingObjectBankAccount = accountingObjectBankAccount;
        this.contactMobile = contactMobile;
        this.paymentMethod = paymentMethod;
        this.companyTaxCode = companyTaxCode;
        this.exchangeRate = exchangeRate;
        this.currencyID = currencyID;
        this.totalAmountOriginal = totalAmountOriginal;
        this.totalAmount = totalAmount;
        this.statusInvoice = statusInvoice;
        this.statusSendMail = statusSendMail;
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

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
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

    public String getAccountingObjectCode() {
        return accountingObjectCode;
    }

    public void setAccountingObjectCode(String accountingObjectCode) {
        this.accountingObjectCode = accountingObjectCode;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
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

    public String getAccountingObjectBankAccount() {
        return accountingObjectBankAccount;
    }

    public void setAccountingObjectBankAccount(String accountingObjectBankAccount) {
        this.accountingObjectBankAccount = accountingObjectBankAccount;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCompanyTaxCode() {
        return companyTaxCode;
    }

    public void setCompanyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getStatusInvoice() {
        return statusInvoice;
    }

    public void setStatusInvoice(Integer statusInvoice) {
        this.statusInvoice = statusInvoice;
    }

    public Integer getStatusSendMail() {
        return statusSendMail;
    }

    public void setStatusSendMail(Integer statusSendMail) {
        this.statusSendMail = statusSendMail;
    }
}
