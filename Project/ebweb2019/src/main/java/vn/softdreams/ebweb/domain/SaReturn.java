package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.SaReturnRSInwardDTO;
import vn.softdreams.ebweb.web.rest.dto.SaReturnDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A SaReturn.
 */
@Entity
@Table(name = "sareturn")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "SaReturnDTO",
        classes = {
            @ConstructorResult(
                targetClass = SaReturnDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "rsInwardOutwardID", type = UUID.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "recorded", type = Boolean.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalDiscountAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalVATAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalPaymentAmount", type = BigDecimal.class),
                    @ColumnResult(name = "rowIndex", type = Integer.class),
                    @ColumnResult(name = "currencyID", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "invoiceNo", type = String.class),
                    @ColumnResult(name = "isDeliveryVoucher", type = Boolean.class),
                    @ColumnResult(name = "invoiceForm", type = Integer.class)
                }
            )
        }
    ),

    @SqlResultSetMapping(
        name = "SaReturnRSInwardDTO",
        classes = {
            @ConstructorResult(
                targetClass = SaReturnRSInwardDTO.class,
                columns = {
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "book", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "total", type = BigDecimal.class),
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "typeGroupID", type = Integer.class)
                }
            )
        }
    )

})
public class SaReturn implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "typeid")
    private Integer typeID;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "posteddate")
    private LocalDate postedDate;

    @Column(name = "duedate")
    private LocalDate dueDate;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Column(name = "nofbook")
    private String noFBook;

    @Column(name = "nombook")
    private String noMBook;

    @Column(name = "autoowamountcal")
    private Integer autoOWAmountCal;

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

    @Column(name = "numberattach")
    private String numberAttach;

    @Column(name = "invoicetemplate")
    private String invoiceTemplate;

    @Column(name = "invoiceseries")
    private String invoiceSeries;

    @Column(name = "invoiceno")
    private String invoiceNo;

    @Column(name = "invoiceform")
    private Integer invoiceForm;

    @Column(name = "invoicedate")
    private LocalDate invoiceDate;

    @Column(name = "paymentmethod")
    private String paymentMethod;

    @Column(name = "currencyid")
    private String currencyID;

    @Column(name = "exchangerate", precision = 10, scale = 2)
    private BigDecimal exchangeRate;

    @Column(name = "totalamount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "totalamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalAmountOriginal;

    @Column(name = "totaldiscountamount", precision = 10, scale = 2)
    private BigDecimal totalDiscountAmount;

    @Column(name = "totaldiscountamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalDiscountAmountOriginal;

    @Column(name = "totalpaymentamount", precision = 10, scale = 2)
    private BigDecimal totalPaymentAmount;

    @Column(name = "totalpaymentamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalPaymentAmountOriginal;

    @Column(name = "totalvatamount", precision = 10, scale = 2)
    private BigDecimal totalVATAmount;

    @Column(name = "totalvatamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalVATAmountOriginal;

    @Column(name = "totalowamount", precision = 10, scale = 2)
    private BigDecimal totalOWAmount;

    @Column(name = "recorded")
    private Boolean recorded;

    @Column(name = "statusinvoice")
    private Integer statusInvoice;

    @Column(name = "statussendmail")
    private Integer statusSendMail;

    @Column(name = "statusconverted")
    private Integer statusConverted;

    @Column(name = "datesendmail")
    private LocalDate dateSendMail;

    @Column(name = "email")
    private String email;

    @Column(name = "totalexporttaxamount", precision = 10, scale = 2)
    private BigDecimal totalExportTaxAmount;

    @Column(name = "isbill")
    private Boolean isBill;

    @Column(name = "isexportinvoice")
    private Boolean isExportInvoice;

    @Column(name = "idadjustinv")
    private String IDAdjustInv;

    @Column(name = "id_miv")
    private String idMIV;

    @Column(name = "idreplaceinv")
    private String idReplaceInv;

    @ManyToOne
    @JoinColumn(name = "accountingobjectid")
    private AccountingObject accountingObject;

    @ManyToOne
    @JoinColumn(name = "employeeid")
    private AccountingObject employee;

    @Column(name = "invoicetypeid")
    private UUID invoiceTypeID;

    @Column(name = "templateid")
    private UUID templateID;

    @Column(name = "isdeliveryvoucher")
    private Boolean isDeliveryVoucher;

    @Column(name = "rsinwardoutwardid")
    private UUID rsInwardOutwardID;

    @Column(name = "isAttachlist")
    private Boolean isAttachList;

    @Column(name = "listno")
    private String listNo;

    @Column(name = "listdate")
    private LocalDate listDate;

    @Column(name = "listcommonnameinventory")
    private String listCommonNameInventory;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "sareturnid")
    private Set<SaReturnDetails> saReturnDetails = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public SaReturn companyID(UUID companyID) {
        this.companyID = companyID;
        return this;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public SaReturn typeID(Integer typeID) {
        this.typeID = typeID;
        return this;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public LocalDate getDate() {
        return date;
    }

    public SaReturn date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public SaReturn postedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
        return this;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public SaReturn typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
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

    public Integer getAutoOWAmountCal() {
        return autoOWAmountCal;
    }

    public SaReturn autoOWAmountCal(Integer autoOWAmountCal) {
        this.autoOWAmountCal = autoOWAmountCal;
        return this;
    }

    public void setAutoOWAmountCal(Integer autoOWAmountCal) {
        this.autoOWAmountCal = autoOWAmountCal;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public SaReturn accountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
        return this;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getAccountingObjectAddress() {
        return accountingObjectAddress;
    }

    public SaReturn accountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
        return this;
    }

    public void setAccountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
    }

    public String getCompanyTaxCode() {
        return companyTaxCode;
    }

    public SaReturn companyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
        return this;
    }

    public void setCompanyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
    }

    public String getAccountingObjectBankAccount() {
        return accountingObjectBankAccount;
    }

    public SaReturn accountingObjectBankAccount(String accountingObjectBankAccount) {
        this.accountingObjectBankAccount = accountingObjectBankAccount;
        return this;
    }

    public void setAccountingObjectBankAccount(String accountingObjectBankAccount) {
        this.accountingObjectBankAccount = accountingObjectBankAccount;
    }

    public String getAccountingObjectBankName() {
        return accountingObjectBankName;
    }

    public SaReturn accountingObjectBankName(String accountingObjectBankName) {
        this.accountingObjectBankName = accountingObjectBankName;
        return this;
    }

    public void setAccountingObjectBankName(String accountingObjectBankName) {
        this.accountingObjectBankName = accountingObjectBankName;
    }

    public String getContactName() {
        return contactName;
    }

    public SaReturn contactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getReason() {
        return reason;
    }

    public SaReturn reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNumberAttach() {
        return numberAttach;
    }

    public SaReturn numberAttach(String numberAttach) {
        this.numberAttach = numberAttach;
        return this;
    }

    public void setNumberAttach(String numberAttach) {
        this.numberAttach = numberAttach;
    }

    public String getInvoiceTemplate() {
        return invoiceTemplate;
    }

    public SaReturn invoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
        return this;
    }

    public void setInvoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public SaReturn invoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
        return this;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public SaReturn invoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
        return this;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public SaReturn invoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public SaReturn paymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public SaReturn currencyID(String currencyID) {
        this.currencyID = currencyID;
        return this;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public SaReturn exchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public SaReturn totalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public SaReturn totalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
        return this;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public SaReturn totalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
        return this;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public BigDecimal getTotalDiscountAmountOriginal() {
        return totalDiscountAmountOriginal;
    }

    public SaReturn totalDiscountAmountOriginal(BigDecimal totalDiscountAmountOriginal) {
        this.totalDiscountAmountOriginal = totalDiscountAmountOriginal;
        return this;
    }

    public void setTotalDiscountAmountOriginal(BigDecimal totalDiscountAmountOriginal) {
        this.totalDiscountAmountOriginal = totalDiscountAmountOriginal;
    }

    public BigDecimal getTotalPaymentAmount() {
        return totalPaymentAmount;
    }

    public SaReturn totalPaymentAmount(BigDecimal totalPaymentAmount) {
        this.totalPaymentAmount = totalPaymentAmount;
        return this;
    }

    public void setTotalPaymentAmount(BigDecimal totalPaymentAmount) {
        this.totalPaymentAmount = totalPaymentAmount;
    }

    public BigDecimal getTotalPaymentAmountOriginal() {
        return totalPaymentAmountOriginal;
    }

    public SaReturn totalPaymentAmountOriginal(BigDecimal totalPaymentAmountOriginal) {
        this.totalPaymentAmountOriginal = totalPaymentAmountOriginal;
        return this;
    }

    public void setTotalPaymentAmountOriginal(BigDecimal totalPaymentAmountOriginal) {
        this.totalPaymentAmountOriginal = totalPaymentAmountOriginal;
    }

    public BigDecimal getTotalOWAmount() {
        return totalOWAmount;
    }

    public SaReturn totalOWAmount(BigDecimal totalOWAmount) {
        this.totalOWAmount = totalOWAmount;
        return this;
    }

    public void setTotalOWAmount(BigDecimal totalOWAmount) {
        this.totalOWAmount = totalOWAmount;
    }

    public Boolean isRecorded() {
        return recorded;
    }

    public SaReturn recorded(Boolean recorded) {
        this.recorded = recorded;
        return this;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public Integer getStatusInvoice() {
        return statusInvoice;
    }

    public SaReturn statusInvoice(Integer statusInvoice) {
        this.statusInvoice = statusInvoice;
        return this;
    }

    public void setStatusInvoice(Integer statusInvoice) {
        this.statusInvoice = statusInvoice;
    }

    public Integer getStatusSendMail() {
        return statusSendMail;
    }

    public SaReturn statusSendMail(Integer statusSendMail) {
        this.statusSendMail = statusSendMail;
        return this;
    }

    public void setStatusSendMail(Integer statusSendMail) {
        this.statusSendMail = statusSendMail;
    }

    public Integer getStatusConverted() {
        return statusConverted;
    }

    public SaReturn statusConverted(Integer statusConverted) {
        this.statusConverted = statusConverted;
        return this;
    }

    public void setStatusConverted(Integer statusConverted) {
        this.statusConverted = statusConverted;
    }

    public LocalDate getDateSendMail() {
        return dateSendMail;
    }

    public void setDateSendMail(LocalDate dateSendMail) {
        this.dateSendMail = dateSendMail;
    }

    public String getEmail() {
        return email;
    }

    public SaReturn email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getTotalExportTaxAmount() {
        return totalExportTaxAmount;
    }

    public SaReturn totalExportTaxAmount(BigDecimal totalExportTaxAmount) {
        this.totalExportTaxAmount = totalExportTaxAmount;
        return this;
    }

    public void setTotalExportTaxAmount(BigDecimal totalExportTaxAmount) {
        this.totalExportTaxAmount = totalExportTaxAmount;
    }

    public Boolean isIsBill() {
        return isBill;
    }

    public SaReturn isBill(Boolean isBill) {
        this.isBill = isBill;
        return this;
    }

    public void setIsBill(Boolean isBill) {
        this.isBill = isBill;
    }

    public String getIDAdjustInv() {
        return IDAdjustInv;
    }

    public SaReturn IDAdjustInv(String IDAdjustInv) {
        this.IDAdjustInv = IDAdjustInv;
        return this;
    }

    public void setIDAdjustInv(String IDAdjustInv) {
        this.IDAdjustInv = IDAdjustInv;
    }

    public String getIdMIV() {
        return idMIV;
    }

    public void setIdMIV(String idMIV) {
        this.idMIV = idMIV;
    }

    public String getIdReplaceInv() {
        return idReplaceInv;
    }

    public void setIdReplaceInv(String idReplaceInv) {
        this.idReplaceInv = idReplaceInv;
    }

    public AccountingObject getAccountingObject() {
        return accountingObject;
    }

    public SaReturn accountingObject(AccountingObject accountingObject) {
        this.accountingObject = accountingObject;
        return this;
    }

    public void setAccountingObject(AccountingObject accountingObject) {
        this.accountingObject = accountingObject;
    }

    public AccountingObject getEmployee() {
        return employee;
    }

    public SaReturn employee(AccountingObject accountingObject) {
        this.employee = accountingObject;
        return this;
    }

    public void setEmployee(AccountingObject accountingObject) {
        this.employee = accountingObject;
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

    public Set<SaReturnDetails> getSaReturnDetails() {
        return saReturnDetails;
    }

    public SaReturn saReturnDetails(Set<SaReturnDetails> saReturnDetails) {
        this.saReturnDetails = saReturnDetails;
        return this;
    }

    public void setSaReturnDetails(Set<SaReturnDetails> saReturnDetails) {
        this.saReturnDetails = saReturnDetails;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getInvoiceForm() {
        return invoiceForm;
    }

    public void setInvoiceForm(Integer invoiceForm) {
        this.invoiceForm = invoiceForm;
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

    public Boolean isIsDeliveryVoucher() {
        return this.isDeliveryVoucher;
    }

    public void setIsDeliveryVoucher(Boolean isDeliveryVoucher) {
        this.isDeliveryVoucher = isDeliveryVoucher;
    }

    public UUID getRsInwardOutwardID() {
        return rsInwardOutwardID;
    }

    public void setRsInwardOutwardID(UUID rsInwardOutwardID) {
        this.rsInwardOutwardID = rsInwardOutwardID;
    }

    public Boolean isIsExportInvoice() {
        return isExportInvoice;
    }

    public void setIsExportInvoice(Boolean exportInvoice) {
        isExportInvoice = exportInvoice;
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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SaReturn saReturn = (SaReturn) o;
        if (saReturn.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), saReturn.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SaReturn{" +
            "id=" + getId() +
            ", companyID='" + getCompanyID() + "'" +
            ", typeID=" + getTypeID() +
            ", date='" + getDate() + "'" +
            ", postedDate='" + getPostedDate() + "'" +
            ", typeLedger=" + getTypeLedger() +
            ", NoFBook='" + getNoFBook() + "'" +
            ", NoMBook='" + getNoMBook() + "'" +
            ", autoOWAmountCal=" + getAutoOWAmountCal() +
            ", accountingObjectName='" + getAccountingObjectName() + "'" +
            ", accountingObjectAddress='" + getAccountingObjectAddress() + "'" +
            ", companyTaxCode='" + getCompanyTaxCode() + "'" +
            ", accountingObjectBankAccount='" + getAccountingObjectBankAccount() + "'" +
            ", accountingObjectBankName='" + getAccountingObjectBankName() + "'" +
            ", contactName='" + getContactName() + "'" +
            ", reason='" + getReason() + "'" +
            ", numberAttach='" + getNumberAttach() + "'" +
            ", invoiceTemplate='" + getInvoiceTemplate() + "'" +
            ", invoiceSeries='" + getInvoiceSeries() + "'" +
            ", invoiceNo='" + getInvoiceNo() + "'" +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", currencyID='" + getCurrencyID() + "'" +
            ", exchangeRate=" + getExchangeRate() +
            ", totalAmount=" + getTotalAmount() +
            ", totalAmountOriginal=" + getTotalAmountOriginal() +
            ", totalDiscountAmount=" + getTotalDiscountAmount() +
            ", totalDiscountAmountOriginal=" + getTotalDiscountAmountOriginal() +
            ", totalPaymentAmount=" + getTotalPaymentAmount() +
            ", totalPaymentAmountOriginal=" + getTotalPaymentAmountOriginal() +
            ", totalOWAmount=" + getTotalOWAmount() +
            ", recorded='" + isRecorded() + "'" +
            ", statusInvoice=" + getStatusInvoice() +
            ", statusSendMail=" + getStatusSendMail() +
            ", statusConverted=" + getStatusConverted() +
            ", DateSendMail='" + getDateSendMail() + "'" +
            ", email='" + getEmail() + "'" +
            ", totalExportTaxAmount=" + getTotalExportTaxAmount() +
            ", isBill='" + isIsBill() + "'" +
            ", IDAdjustInv='" + getIDAdjustInv() + "'" +
            "}";
    }
}
