package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;
import vn.softdreams.ebweb.service.dto.SABillReportDTO;
import vn.softdreams.ebweb.web.rest.dto.SaBillCreatedDTO;
import vn.softdreams.ebweb.web.rest.dto.SaBillDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


/**
 * A SaBill.
 */
@Entity
@Table(name = "sabill")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "SaBillDTO",
        classes = {
            @ConstructorResult(
                targetClass = SaBillDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalDiscountAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalVATAmount", type = BigDecimal.class),
                    @ColumnResult(name = "invoiceTemplate", type = String.class),
                    @ColumnResult(name = "invoiceSeries", type = String.class),
                    @ColumnResult(name = "invoiceNo", type = String.class),
                    @ColumnResult(name = "invoiceDate", type = LocalDate.class),
                    @ColumnResult(name = "rowIndex", type = Integer.class),
                    @ColumnResult(name = "currencyID", type = String.class),
                    @ColumnResult(name = "invoiceForm", type = Integer.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "SaBillCreatedDTO",
        classes = {
            @ConstructorResult(
                targetClass = SaBillCreatedDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "CompanyID", type = UUID.class),
                    @ColumnResult(name = "typeGroupID", type = Integer.class),
                    @ColumnResult(name = "refID2", type = UUID.class),
                    @ColumnResult(name = "invoiceTemplate", type = String.class),
                    @ColumnResult(name = "invoiceSeries", type = String.class),
                    @ColumnResult(name = "invoiceNo", type = String.class),
                    @ColumnResult(name = "invoiceDate", type = LocalDate.class),
                    @ColumnResult(name = "AccountingObjectName", type = String.class),
                    @ColumnResult(name = "totalSabill", type = BigDecimal.class),
                }
            )
        }
    ),

    @SqlResultSetMapping(
        name = "SABillDTOReport",
        classes = {
            @ConstructorResult(
                targetClass = SABillReportDTO.class,
                columns = {
                    @ColumnResult(name = "materialGoodsName", type = String.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "unitPriceOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "unitName", type = String.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "amountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "vATRate", type = BigDecimal.class),
                    @ColumnResult(name = "vATAmount", type = BigDecimal.class),
                    @ColumnResult(name = "vATAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "discountAmount", type = BigDecimal.class),
                    @ColumnResult(name = "discountAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "discountRate", type = BigDecimal.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "listCommonNameInventory", type = String.class),
                    @ColumnResult(name = "isPromotion", type = Boolean.class),
                }
            )
        }
    )
})
public class SaBill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "typeid")
    private Integer typeID;

    @Column(name = "currencyid")
    private String currencyID;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "exchangerate", precision = 10, scale = 2)
    private BigDecimal exchangeRate;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Column(name = "accountingobjectname")
    private String accountingObjectName;

    @Column(name = "accountingobjectaddress")
    private String accountingObjectAddress;

    @Column(name = "companytaxcode")
    private String companyTaxCode;

    @Column(name = "accountingobjectbankaccount")
    private String accountingObjectBankAccount;

    @Column(name = "accountingobjectbankname")
    private String accountingObjectBankName;

    @Column(name = "contactname")
    private String contactName;

    @Column(name = "reason")
    private String reason;

    @Column(name = "isattachlist")
    private Boolean isAttachList;

    @Column(name = "listno")
    private String listNo;

    @Column(name = "listdate")
    private LocalDate listDate;

    @Column(name = "listcommonnameinventory")
    private String listCommonNameInventory;

    @Column(name = "totalamount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "totalamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalAmountOriginal;

    @Column(name = "totalvatamount", precision = 10, scale = 2)
    private BigDecimal totalVATAmount;

    @Column(name = "totalvatamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalVATAmountOriginal;

    @Column(name = "totaldiscountamount", precision = 10, scale = 2)
    private BigDecimal totalDiscountAmount;

    @Column(name = "totaldiscountamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalDiscountAmountOriginal;

    @Column(name = "invoiceform")
    private Integer invoiceForm;

    @Column(name = "invoicetemplate")
    private String invoiceTemplate;

    @Column(name = "invoiceseries")
    private String invoiceSeries;

    @Column(name = "invoiceno")
    private String invoiceNo;

    @Column(name = "invoicedate")
    private LocalDate invoiceDate;

    @Column(name = "paymentmethod")
    private String paymentMethod;

    @Column(name = "type")
    private Integer type;

    @ManyToOne
    @JoinColumn(name = "accountingobjectid")
    private AccountingObject accountingObject;

    @Column(name = "invoicetypeid")
    private UUID invoiceTypeID;

    @Column(name = "templateid")
    private UUID templateID;

    @Column(name = "statusinvoice")
    private Integer statusInvoice;

    @Column(name = "idreplaceinv")
    private UUID iDReplaceInv;

    @Column(name = "idadjustinv")
    private UUID iDAdjustInv;

    @Column(name = "refDateTime")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime refDateTime;

    @Column(name = "documentno")
    private String documentNo;
    @Column(name = "documentdate")
    private LocalDate documentDate;
    @Column(name = "documentnote")
    private String documentNote;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "sabillid")
    private Set<SaBillDetails> saBillDetails = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    @Override
    public String toString() {
        return "SaBill{" +
            "id=" + id +
            ", currencyID='" + currencyID + '\'' +
            ", companyID=" + companyID +
            ", exchangeRate=" + exchangeRate +
            ", typeLedger=" + typeLedger +
            ", accountingObjectAddress='" + accountingObjectAddress + '\'' +
            ", companyTaxCode='" + companyTaxCode + '\'' +
            ", accountingObjectBankAccount='" + accountingObjectBankAccount + '\'' +
            ", accountingObjectBankName='" + accountingObjectBankName + '\'' +
            ", contactName='" + contactName + '\'' +
            ", reason='" + reason + '\'' +
            ", isAttachList=" + isAttachList +
            ", listNo='" + listNo + '\'' +
            ", listDate=" + listDate +
            ", listCommonNameInventory='" + listCommonNameInventory + '\'' +
            ", totalAmount=" + totalAmount +
            ", totalAmountOriginal=" + totalAmountOriginal +
            ", totalVATAmount=" + totalVATAmount +
            ", totalVATAmountOriginal=" + totalVATAmountOriginal +
            ", invoiceTemplate='" + invoiceTemplate + '\'' +
            ", invoiceSeries='" + invoiceSeries + '\'' +
            ", invoiceNo='" + invoiceNo + '\'' +
            ", invoiceDate=" + invoiceDate +
            ", paymentMethod='" + paymentMethod + '\'' +
            ", templateID=" + templateID +
            ", saBillDetails=" + saBillDetails +
            '}';
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getAccountingObjectAddress() {
        return accountingObjectAddress;
    }

    public void setAccountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
    }

    public String getCompanyTaxCode() {
        return companyTaxCode;
    }

    public void setCompanyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
    }

    public String getAccountingObjectBankAccount() {
        return accountingObjectBankAccount;
    }

    public void setAccountingObjectBankAccount(String accountingObjectBankAccount) {
        this.accountingObjectBankAccount = accountingObjectBankAccount;
    }

    public String getAccountingObjectBankName() {
        return accountingObjectBankName;
    }

    public void setAccountingObjectBankName(String accountingObjectBankName) {
        this.accountingObjectBankName = accountingObjectBankName;
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

    public Boolean getIsAttachList() {
        return isAttachList;
    }

    public void setIsAttachList(Boolean attachList) {
        isAttachList = attachList;
    }

    public String getListNo() {
        return listNo;
    }

    public void setListNo(String listNo) {
        this.listNo = listNo;
    }

    public LocalDate getListDate() {
        return listDate;
    }

    public void setListDate(LocalDate listDate) {
        this.listDate = listDate;
    }

    public String getListCommonNameInventory() {
        return listCommonNameInventory;
    }

    public void setListCommonNameInventory(String listCommonNameInventory) {
        this.listCommonNameInventory = listCommonNameInventory;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount == null ? BigDecimal.ZERO : totalAmount;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal == null ? BigDecimal.ZERO : totalAmountOriginal;
    }

    public BigDecimal getTotalVATAmount() {
        return totalVATAmount;
    }

    public void setTotalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount == null ? BigDecimal.ZERO : totalVATAmount;;
    }

    public BigDecimal getTotalVATAmountOriginal() {
        return totalVATAmountOriginal;
    }

    public void setTotalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal == null ? BigDecimal.ZERO : totalVATAmountOriginal;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public AccountingObject getAccountingObject() {
        return accountingObject;
    }

    public void setAccountingObject(AccountingObject accountingObject) {
        this.accountingObject = accountingObject;
    }

    public UUID getInvoiceTypeID() {
        return invoiceTypeID;
    }

    public void setInvoiceTypeID(UUID invoiceTypeID) {
        this.invoiceTypeID = invoiceTypeID;
    }

    public UUID getTemplateID() {
        return templateID;
    }

    public void setTemplateID(UUID templateID) {
        this.templateID = templateID;
    }

    public Set<SaBillDetails> getSaBillDetails() {
        return saBillDetails;
    }

    public void setSaBillDetails(Set<SaBillDetails> saBillDetails) {
        this.saBillDetails = saBillDetails;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount == null ? BigDecimal.ZERO : totalDiscountAmount;
    }

    public BigDecimal getTotalDiscountAmountOriginal() {
        return totalDiscountAmountOriginal;
    }

    public void setTotalDiscountAmountOriginal(BigDecimal totalDiscountAmountOriginal) {
        this.totalDiscountAmountOriginal = totalDiscountAmountOriginal == null ? BigDecimal.ZERO : totalDiscountAmountOriginal;
    }

    public Integer getStatusInvoice() {
        return statusInvoice;
    }

    public void setStatusInvoice(Integer statusInvoice) {
        this.statusInvoice = statusInvoice;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public SaBill() {
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public UUID getiDReplaceInv() {
        return iDReplaceInv;
    }

    public void setiDReplaceInv(UUID iDReplaceInv) {
        this.iDReplaceInv = iDReplaceInv;
    }

    public UUID getiDAdjustInv() {
        return iDAdjustInv;
    }

    public void setiDAdjustInv(UUID iDAdjustInv) {
        this.iDAdjustInv = iDAdjustInv;
    }

    public Integer getInvoiceForm() {
        return invoiceForm;
    }

    public void setInvoiceForm(Integer invoiceForm) {
        this.invoiceForm = invoiceForm;
    }

    public LocalDateTime getRefDateTime() {
        return refDateTime;
    }

    public void setRefDateTime(LocalDateTime refDateTime) {
        this.refDateTime = refDateTime;
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

    public boolean equals(SaBill o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return exchangeRate.compareTo(o.exchangeRate) == 0
            && accountingObjectName.compareTo(o.accountingObjectName) == 0
            && accountingObjectAddress.compareTo(o.accountingObjectAddress) == 0
            && companyTaxCode.compareTo(o.companyTaxCode) == 0
            && accountingObjectBankAccount.compareTo(o.accountingObjectBankAccount) == 0
            && accountingObjectBankName.compareTo(o.accountingObjectBankName) == 0
            && contactName.compareTo(o.contactName) == 0
            && reason.compareTo(o.reason) == 0
            && invoiceForm.compareTo(o.invoiceForm) == 0
            && invoiceTemplate.compareTo(o.invoiceTemplate) == 0
            && invoiceSeries.compareTo(o.invoiceSeries) == 0
            && invoiceNo.compareTo(o.invoiceNo) == 0
            && invoiceDate.compareTo(o.invoiceDate) == 0
            && paymentMethod.compareTo(o.paymentMethod) == 0
            && ((accountingObject == null && o.accountingObject == null) || (accountingObject != null && o.accountingObject != null && accountingObject.getId().compareTo(o.accountingObject.getId()) == 0))
            && statusInvoice.compareTo(o.statusInvoice) == 0;
    }
}
