package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A PPDiscountReturn.
 */
@Entity
@Table(name = "ppdiscountreturn")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "PPDiscountReturnDTO",
        classes = {
            @ConstructorResult(
                targetClass = PPDiscountReturnDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "companyID", type = UUID.class),
                    @ColumnResult(name = "branchID", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "typeLedger", type = Integer.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "rsInwardOutwardID", type = UUID.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "accountingObjectAddress", type = String.class),
                    @ColumnResult(name = "accountingObjectBankAccount", type = String.class),
                    @ColumnResult(name = "accountingObjectBankName", type = String.class),
                    @ColumnResult(name = "companyTaxCode", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "invoiceTypeID", type = UUID.class),
                    @ColumnResult(name = "invoiceTemplate", type = String.class),
                    @ColumnResult(name = "paymentMethod", type = String.class),
                    @ColumnResult(name = "invoiceDate", type = LocalDate.class),
                    @ColumnResult(name = "invoiceForm", type = Integer.class),
                    @ColumnResult(name = "invoiceNo", type = String.class),
                    @ColumnResult(name = "numberAttach", type = String.class),
                    @ColumnResult(name = "invoiceSeries", type = String.class),
                    @ColumnResult(name = "currencyID", type = String.class),
                    @ColumnResult(name = "exchangeRate", type = BigDecimal.class),
                    @ColumnResult(name = "transportMethodID", type = UUID.class),
                    @ColumnResult(name = "dueDate", type = LocalDate.class),
                    @ColumnResult(name = "paymentClauseID", type = UUID.class),
                    @ColumnResult(name = "employeeID", type = UUID.class),
                    @ColumnResult(name = "isAttachList", type = Boolean.class),
                    @ColumnResult(name = "listNo", type = String.class),
                    @ColumnResult(name = "listDate", type = LocalDate.class),
                    @ColumnResult(name = "listCommonNameInventory", type = String.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "totalVATAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalDiscountAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalDiscountAmounOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "statusInvoice", type = Integer.class),
                    @ColumnResult(name = "statusSendMail", type = Integer.class),
                    @ColumnResult(name = "statusConverted", type = Integer.class),
                    @ColumnResult(name = "dateSendMail", type = LocalDate.class),
                    @ColumnResult(name = "email", type = String.class),
                    @ColumnResult(name = "idAdjustInv", type = String.class),
                    @ColumnResult(name = "idReplaceInv", type = String.class),
                    @ColumnResult(name = "isBill", type = Boolean.class),
                    @ColumnResult(name = "ID_MIV", type = String.class),
                    @ColumnResult(name = "templateID", type = String.class),
                    @ColumnResult(name = "recorded", type = Boolean.class),
                    @ColumnResult(name = "contactName", type = String.class),
                    @ColumnResult(name = "isDeliveryVoucher", type = Boolean.class),
                    @ColumnResult(name = "isExportInvoice", type = Boolean.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "PPDiscountReturnSearch2DTO",
        classes = {
            @ConstructorResult(
                targetClass = PPDiscountReturnSearch2DTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "companyID", type = UUID.class),
                    @ColumnResult(name = "branchID", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "typeLedger", type = Integer.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "rsInwardOutwardID", type = UUID.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "accountingObjectAddress", type = String.class),
                    @ColumnResult(name = "accountingObjectBankAccount", type = String.class),
                    @ColumnResult(name = "accountingObjectBankName", type = String.class),
                    @ColumnResult(name = "companyTaxCode", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "invoiceTypeID", type = UUID.class),
                    @ColumnResult(name = "invoiceTemplate", type = String.class),
                    @ColumnResult(name = "paymentMethod", type = String.class),
                    @ColumnResult(name = "invoiceDate", type = LocalDate.class),
                    @ColumnResult(name = "invoiceForm", type = Integer.class),
                    @ColumnResult(name = "invoiceNo", type = String.class),
                    @ColumnResult(name = "numberAttach", type = String.class),
                    @ColumnResult(name = "invoiceSeries", type = String.class),
                    @ColumnResult(name = "currencyID", type = String.class),
                    @ColumnResult(name = "exchangeRate", type = BigDecimal.class),
                    @ColumnResult(name = "transportMethodID", type = UUID.class),
                    @ColumnResult(name = "dueDate", type = LocalDate.class),
                    @ColumnResult(name = "paymentClauseID", type = UUID.class),
                    @ColumnResult(name = "employeeID", type = UUID.class),
                    @ColumnResult(name = "isAttachList", type = Boolean.class),
                    @ColumnResult(name = "listNo", type = String.class),
                    @ColumnResult(name = "listDate", type = LocalDate.class),
                    @ColumnResult(name = "listCommonNameInventory", type = String.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "totalVATAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalVATAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "totalDiscountAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalDiscountAmounOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "statusInvoice", type = Integer.class),
                    @ColumnResult(name = "statusSendMail", type = Integer.class),
                    @ColumnResult(name = "statusConverted", type = Integer.class),
                    @ColumnResult(name = "dateSendMail", type = LocalDate.class),
                    @ColumnResult(name = "email", type = String.class),
                    @ColumnResult(name = "idAdjustInv", type = String.class),
                    @ColumnResult(name = "idReplaceInv", type = String.class),
                    @ColumnResult(name = "isBill", type = Boolean.class),
                    @ColumnResult(name = "ID_MIV", type = String.class),
                    @ColumnResult(name = "templateID", type = String.class),
                    @ColumnResult(name = "recorded", type = Boolean.class),
                    @ColumnResult(name = "contactName", type = String.class),
                    @ColumnResult(name = "isDeliveryVoucher", type = Boolean.class),
                    @ColumnResult(name = "isExportInvoice", type = Boolean.class),
                    @ColumnResult(name = "saBillInvoiceNo", type = String.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "PPDiscountReturnOutWardDTO",
        classes = {
            @ConstructorResult(
                targetClass = PPDiscountReturnOutWardDTO.class,
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
    ),
    @SqlResultSetMapping(
        name = "PPDiscountReturnSearchDTO",
        classes = {
            @ConstructorResult(
                targetClass = PPDiscountReturnSearchDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "noBook", type = String.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "accountingObjectAddress", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalVATAmount", type = BigDecimal.class),
                    @ColumnResult(name = "Recorded", type = Boolean.class),
                }
            )
        })
})
public class PPDiscountReturn implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    @Column(name = "typeid")
    private Integer typeID;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "posteddate")
    private LocalDate postedDate;

    @Column(name = "typeledger")
    private Integer typeLedger;


    @Size(max = 25)
    @Column(name = "nofbook", length = 25)
    private String noFBook;


    @Size(max = 25)
    @Column(name = "nombook", length = 25)
    private String noMBook;

    @Column(name = "rsinwardoutwardid")
    private UUID rsInwardOutwardID;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

    //    @NotNull
    @Size(max = 512)
    @Column(name = "accountingobjectname", length = 512)
    private String accountingObjectName;

    //    @NotNull
    @Size(max = 512)
    @Column(name = "accountingobjectaddress", length = 512)
    private String accountingObjectAddress;

    //    @NotNull
    @Size(max = 512)
    @Column(name = "accountingobjectbankaccount", length = 512)
    private String accountingObjectBankAccount;


    @Size(max = 512)
    @Column(name = "accountingobjectbankname", length = 512)
    private String accountingObjectBankName;

    //    @NotNull
    @Size(max = 512)
    @Column(name = "companytaxcode", length = 512)
    private String companyTaxCode;

    //    @NotNull
    @Size(max = 512)
    @Column(name = "reason", length = 512)
    private String reason;

    @Column(name = "invoicetypeid")
    private UUID invoiceTypeID;

//    //    @NotNull
    @Size(max = 25)
    @Column(name = "invoicetemplate", length = 25)
    private String invoiceTemplate;

//    //    @NotNull
    @Size(max = 25)
    @Column(name = "paymentmethod", length = 25)
    private String paymentMethod;

    @Column(name = "invoicedate")
    private LocalDate invoiceDate;

    @Column(name = "invoiceform")
    private Integer invoiceForm;

    //    @NotNull
    @Size(max = 25)
    @Column(name = "invoiceno", length = 25)
    private String invoiceNo;

    //    @NotNull
    @Size(max = 512)
    @Column(name = "numberattach", length = 512)
    private String numberAttach;

    //    @NotNull
    @Size(max = 25)
    @Column(name = "invoiceseries", length = 25)
    private String invoiceSeries;

    //    @NotNull
    @Size(max = 3)
    @Column(name = "currencyid", length = 3)
    private String currencyID;

    @Column(name = "exchangerate", precision = 10, scale = 2)
    private BigDecimal exchangeRate;

    @Column(name = "transportmethodid")
    private UUID transportMethodID;

    @Column(name = "duedate")
    private LocalDate dueDate;

    @Column(name = "paymentclauseid")
    private UUID paymentClauseID;

    @Column(name = "employeeid")
    private UUID employeeID;

    @Column(name = "isattachlist")
    private Boolean isAttachList;

    //    @NotNull
    @Size(max = 25)
    @Column(name = "listno", length = 25)
    private String listNo;

    @Column(name = "listdate")
    private LocalDate listDate;

    //    @NotNull
    @Size(max = 512)
    @Column(name = "listcommonnameinventory", length = 512)
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

    @Column(name = "totaldiscountamounoriginal", precision = 10, scale = 2)
    private BigDecimal totalDiscountAmounOriginal;

    @Column(name = "statusinvoice")
    private Integer statusInvoice;

    @Column(name = "statussendmail")
    private Integer statusSendMail;

    @Column(name = "statusconverted")
    private Integer statusConverted;

    @Column(name = "datesendmail")
    private LocalDate dateSendMail;

    //    @NotNull
    @Size(max = 512)
    @Column(name = "email", length = 512)
    private String email;

    @Column(name = "idadjustinv")
    private String idAdjustInv;

    @Column(name = "idreplaceinv")
    private String idReplaceInv;

    @JsonProperty
    @Column(name = "isbill")
    private Boolean isBill;

    @Column(name = "id_miv")
    private String idMIV;

    @Column(name = "templateid")
    private String templateID;

    @Column(name = "recorded")
    private Boolean recorded;

    @Size(max = 512)
    @Column(name = "contactname", length = 512)
    private String contactName;

    @JsonProperty
    @Column(name = "isdeliveryvoucher")
    private Boolean isDeliveryVoucher;

    @Column(name = "isexportinvoice")
    private Boolean isExportInvoice;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "ppdiscountreturnid")
    private Set<PPDiscountReturnDetails> ppDiscountReturnDetails = new HashSet<>();

    public PPDiscountReturn() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public UUID getBranchID() {
        return branchID;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
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

    public Integer getTypeLedger() {
        return typeLedger;
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

    public UUID getRsInwardOutwardID() {
        return rsInwardOutwardID;
    }

    public void setRsInwardOutwardID(UUID rsInwardOutwardID) {
        this.rsInwardOutwardID = rsInwardOutwardID;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
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

    public String getCompanyTaxCode() {
        return companyTaxCode;
    }

    public void setCompanyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UUID getInvoiceTypeID() {
        return invoiceTypeID;
    }

    public void setInvoiceTypeID(UUID invoiceTypeID) {
        this.invoiceTypeID = invoiceTypeID;
    }

    public String getInvoiceTemplate() {
        return invoiceTemplate;
    }

    public void setInvoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Integer getInvoiceForm() {
        return invoiceForm;
    }

    public void setInvoiceForm(Integer invoiceForm) {
        this.invoiceForm = invoiceForm;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getNumberAttach() {
        return numberAttach;
    }

    public void setNumberAttach(String numberAttach) {
        this.numberAttach = numberAttach;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public UUID getTransportMethodID() {
        return transportMethodID;
    }

    public void setTransportMethodID(UUID transportMethodID) {
        this.transportMethodID = transportMethodID;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public UUID getPaymentClauseID() {
        return paymentClauseID;
    }

    public void setPaymentClauseID(UUID paymentClauseID) {
        this.paymentClauseID = paymentClauseID;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public Boolean getIsAttachList() {
        return isAttachList;
    }

    public void setIsAttachList(Boolean isAttachList) {
        this.isAttachList = isAttachList;
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
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
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

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public BigDecimal getTotalDiscountAmounOriginal() {
        return totalDiscountAmounOriginal;
    }

    public void setTotalDiscountAmounOriginal(BigDecimal totalDiscountAmounOriginal) {
        this.totalDiscountAmounOriginal = totalDiscountAmounOriginal;
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

    public Integer getStatusConverted() {
        return statusConverted;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdAdjustInv() {
        return idAdjustInv;
    }

    public void setIdAdjustInv(String idAdjustInv) {
        this.idAdjustInv = idAdjustInv;
    }

    public String getIdReplaceInv() {
        return idReplaceInv;
    }

    public void setIdReplaceInv(String idReplaceInv) {
        this.idReplaceInv = idReplaceInv;
    }

    public Boolean getIsBill() {
        return isBill;
    }

    public void setIsBill(Boolean bill) {
        isBill = bill;
    }

    public String getIdMIV() {
        return idMIV;
    }

    public void setIdMIV(String idMIV) {
        this.idMIV = idMIV;
    }

    public String getTemplateID() {
        return templateID;
    }

    public void setTemplateID(String templateID) {
        this.templateID = templateID;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Boolean getIsDeliveryVoucher() {
        return isDeliveryVoucher;
    }

    public void setIsDeliveryVoucher(Boolean deliveryVoucher) {
        isDeliveryVoucher = deliveryVoucher;
    }

    public Boolean getIsExportInvoice() {
        return isExportInvoice;
    }

    public void setIsExportInvoice(Boolean exportInvoice) {
        isExportInvoice = exportInvoice;
    }

    public Set<PPDiscountReturnDetails> getPpDiscountReturnDetails() {
        return ppDiscountReturnDetails;
    }

    public void setPpDiscountReturnDetails(Set<PPDiscountReturnDetails> ppDiscountReturnDetails) {
        this.ppDiscountReturnDetails = ppDiscountReturnDetails;
    }

}
