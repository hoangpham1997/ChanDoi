package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import vn.softdreams.ebweb.web.rest.dto.InformationVoucherDTO;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Immutable()
@Subselect(
    "SELECT * FROM ViewEInvoice"
)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "EInvoiceDetails",
        classes = {
            @ConstructorResult(
                targetClass = EInvoiceDetails.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "eInvoiceID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "isPromotion", type = Boolean.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "unitName", type = String.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "unitPriceOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "mainUnitID", type = UUID.class),
                    @ColumnResult(name = "mainQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "mainUnitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "mainConvertRate", type = BigDecimal.class),
                    @ColumnResult(name = "formula", type = String.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "amountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "discountRate", type = BigDecimal.class),
                    @ColumnResult(name = "discountAmount", type = BigDecimal.class),
                    @ColumnResult(name = "discountAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "vATRate", type = BigDecimal.class),
                    @ColumnResult(name = "vATAmount", type = BigDecimal.class),
                    @ColumnResult(name = "vATAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "lotNo", type = String.class),
                    @ColumnResult(name = "expiryDate", type = LocalDate.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "InformationVoucherDTO",
        classes = {
            @ConstructorResult(
                targetClass = InformationVoucherDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                }
            )
        }
    ),
})
public class EInvoice {
    @Id
//    @org.hibernate.annotations.Type(type="uuid-char")
    private UUID id;
    @Column(name = "companyid")
    private UUID companyID;
    @Column(name = "typeid")
    private Integer typeID;
    @Column(name = "invoicetypecode")
    private String invoiceTypeCode;
    @Column(name = "invoicetemplate")
    private String invoiceTemplate;
    @Column(name = "invoiceseries")
    private String invoiceSeries;
    @Column(name = "invoiceno")
    private String invoiceNo;
    @Column(name = "invoicedate")
    private LocalDate invoiceDate;
    @Column(name = "accountingobjectcode")
    private String accountingObjectCode;
    @Column(name = "contactname")
    private String contactName;
    @Column(name = "accountingobjectname")
    private String accountingObjectName;
    @Column(name = "accountingobjectaddress")
    private String accountingObjectAddress;
    @Column(name = "accountingobjectbankname")
    private String accountingObjectBankName;
    @Column(name = "accountingobjectbankaccount")
    private String accountingObjectBankAccount;
    @Column(name = "contactmobile")
    private String contactMobile;
    @Column(name = "accountingobjectemail")
    private String accountingObjectEmail;
    @Column(name = "paymentmethod")
    private String paymentMethod;
    @Column(name = "companytaxcode")
    private String companyTaxCode;
    @Column(name = "exchangerate")
    private BigDecimal exchangeRate;
    @Column(name = "currencyid")
    private String currencyID;
    @Column(name = "totalamountoriginal")
    private BigDecimal totalAmountOriginal;
    @Column(name = "totaldiscountamount")
    private BigDecimal totalDiscountAmount;
    @Column(name = "totaldiscountamountoriginal")
    private BigDecimal totalDiscountAmountOriginal;
    @Column(name = "totalvatamount")
    private BigDecimal totalVATAmount;
    @Column(name = "totalvatamountoriginal")
    private BigDecimal totalVATAmountOriginal;
    @Column(name = "totalamount")
    private BigDecimal totalAmount;
    @Column(name = "statusinvoice")
    private Integer statusInvoice;
    @Column(name = "statussendmail")
    private Boolean statusSendMail;
    @Column(name = "email")
    private String email;
    @Column(name = "statusconverted")
    private Boolean statusConverted;
    @Column(name = "idadjustinv")
    private UUID iDAdjustInv;
    @Column(name = "invocienoadjusted")
    private String invocieNoAdjusted;
    @Column(name = "invoicetemplateadjusted")
    private String invoiceTemplateAdjusted;
    @Column(name = "invoiceseriesadjusted")
    private String InvoiceSeriesAdjusted;
    @Column(name = "invoicedateadjusted")
    private LocalDate invoiceDateAdjusted;
    @Column(name = "refdatetimeadjusted")
    private LocalDateTime refDateTimeAdjusted;
    @Column(name = "idreplaceinv")
    private UUID iDReplaceInv;
    @Column(name = "invocienoreplaced")
    private String invocieNoReplaced;
    @Column(name = "invoicetemplatereplaced")
    private String invoiceTemplateReplaced;
    @Column(name = "invoiceseriesreplaced")
    private String invoiceSeriesReplaced;
    @Column(name = "invoicedatereplaced")
    private LocalDate invoiceDateReplaced;
    @Column(name = "refdatetimereplaced")
    private LocalDateTime refDateTimeReplaced;
    @Column(name = "type")
    private Integer type;
    @Column(name = "documentno")
    private String documentNo;
    @Column(name = "documentdate")
    private LocalDate documentDate;
    @Column(name = "documentnote")
    private String documentNote;
    @Column(name = "reftable")
    private String RefTable;
    @Column(name = "refdatetime")
    private LocalDateTime refDateTime;
    @Column(name = "recorded")
    private Boolean recorded;
    @Column(name = "orderpriority")
    private Long orderPriority;
    @Column(name = "idmiv")
    private UUID iDMIV;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private List<EInvoiceDetails> eInvoiceDetails;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private List<EInvoiceDetails> eInvoiceDetailsAjRp;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Number total;

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

    public String getInvoiceTypeCode() {
        return invoiceTypeCode;
    }

    public void setInvoiceTypeCode(String invoiceTypeCode) {
        this.invoiceTypeCode = invoiceTypeCode;
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

    public Boolean getStatusSendMail() {
        return statusSendMail;
    }

    public void setStatusSendMail(Boolean statusSendMail) {
        this.statusSendMail = statusSendMail;
    }

    public String getRefTable() {
        return RefTable;
    }

    public void setRefTable(String refTable) {
        RefTable = refTable;
    }

    public List<EInvoiceDetails> geteInvoiceDetails() {
        return eInvoiceDetails;
    }

    public void seteInvoiceDetails(List<EInvoiceDetails> eInvoiceDetails) {
        this.eInvoiceDetails = eInvoiceDetails;
    }

    public Boolean getStatusConverted() {
        return statusConverted;
    }

    public void setStatusConverted(Boolean statusConverted) {
        this.statusConverted = statusConverted;
    }

    public UUID getiDAdjustInv() {
        return iDAdjustInv;
    }

    public void setiDAdjustInv(UUID iDAdjustInv) {
        this.iDAdjustInv = iDAdjustInv;
    }

    public String getInvocieNoAdjusted() {
        return invocieNoAdjusted;
    }

    public void setInvocieNoAdjusted(String invocieNoAdjusted) {
        this.invocieNoAdjusted = invocieNoAdjusted;
    }

    public UUID getiDReplaceInv() {
        return iDReplaceInv;
    }

    public void setiDReplaceInv(UUID iDReplaceInv) {
        this.iDReplaceInv = iDReplaceInv;
    }

    public String getInvocieNoReplaced() {
        return invocieNoReplaced;
    }

    public void setInvocieNoReplaced(String invocieNoReplaced) {
        this.invocieNoReplaced = invocieNoReplaced;
    }

    public String getInvoiceTemplateAdjusted() {
        return invoiceTemplateAdjusted;
    }

    public void setInvoiceTemplateAdjusted(String invoiceTemplateAdjusted) {
        this.invoiceTemplateAdjusted = invoiceTemplateAdjusted;
    }

    public String getInvoiceSeriesAdjusted() {
        return InvoiceSeriesAdjusted;
    }

    public void setInvoiceSeriesAdjusted(String invoiceSeriesAdjusted) {
        InvoiceSeriesAdjusted = invoiceSeriesAdjusted;
    }

    public String getInvoiceTemplateReplaced() {
        return invoiceTemplateReplaced;
    }

    public void setInvoiceTemplateReplaced(String invoiceTemplateReplaced) {
        this.invoiceTemplateReplaced = invoiceTemplateReplaced;
    }

    public String getInvoiceSeriesReplaced() {
        return invoiceSeriesReplaced;
    }

    public void setInvoiceSeriesReplaced(String invoiceSeriesReplaced) {
        this.invoiceSeriesReplaced = invoiceSeriesReplaced;
    }

    public List<EInvoiceDetails> geteInvoiceDetailsAjRp() {
        return eInvoiceDetailsAjRp;
    }

    public void seteInvoiceDetailsAjRp(List<EInvoiceDetails> eInvoiceDetailsAjRp) {
        this.eInvoiceDetailsAjRp = eInvoiceDetailsAjRp;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAccountingObjectEmail() {
        return accountingObjectEmail;
    }

    public void setAccountingObjectEmail(String accountingObjectEmail) {
        this.accountingObjectEmail = accountingObjectEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public BigDecimal getTotalDiscountAmountOriginal() {
        return totalDiscountAmountOriginal;
    }

    public void setTotalDiscountAmountOriginal(BigDecimal totalDiscountAmountOriginal) {
        this.totalDiscountAmountOriginal = totalDiscountAmountOriginal;
    }

    public BigDecimal getTotalVATAmount() {
        return totalVATAmount;
    }

    public void setTotalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
    }

    public BigDecimal getTotalVATAmountOriginal() {
        return totalVATAmountOriginal;
    }

    public void setTotalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
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

    public Long getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Long orderPriority) {
        this.orderPriority = orderPriority;
    }

    public LocalDate getInvoiceDateAdjusted() {
        return invoiceDateAdjusted;
    }

    public void setInvoiceDateAdjusted(LocalDate invoiceDateAdjusted) {
        this.invoiceDateAdjusted = invoiceDateAdjusted;
    }

    public LocalDate getInvoiceDateReplaced() {
        return invoiceDateReplaced;
    }

    public void setInvoiceDateReplaced(LocalDate invoiceDateReplaced) {
        this.invoiceDateReplaced = invoiceDateReplaced;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public LocalDate getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(LocalDate documentDate) {
        this.documentDate = documentDate;
    }

    public String getDocumentNote() {
        return documentNote;
    }

    public void setDocumentNote(String documentNote) {
        this.documentNote = documentNote;
    }

    public UUID getiDMIV() {
        return iDMIV;
    }

    public void setiDMIV(UUID iDMIV) {
        this.iDMIV = iDMIV;
    }

    public LocalDateTime getRefDateTime() {
        return refDateTime;
    }

    public void setRefDateTime(LocalDateTime refDateTime) {
        this.refDateTime = refDateTime;
    }

    public LocalDateTime getRefDateTimeAdjusted() {
        return refDateTimeAdjusted;
    }

    public void setRefDateTimeAdjusted(LocalDateTime refDateTimeAdjusted) {
        this.refDateTimeAdjusted = refDateTimeAdjusted;
    }

    public LocalDateTime getRefDateTimeReplaced() {
        return refDateTimeReplaced;
    }

    public void setRefDateTimeReplaced(LocalDateTime refDateTimeReplaced) {
        this.refDateTimeReplaced = refDateTimeReplaced;
    }
}
