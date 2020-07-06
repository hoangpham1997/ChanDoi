package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.SAInvoiceForMCReceiptDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.SAInvoicePopupDTO;
import vn.softdreams.ebweb.web.rest.dto.SAInvoiceViewDTO;
import vn.softdreams.ebweb.web.rest.dto.SaInvoiceDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * A SAInvoice.
 */
@Entity
@Table(name = "sainvoice")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "SAInvoiceSaBillPopupDTO",
        classes = {
            @ConstructorResult(
                targetClass = SAInvoicePopupDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "saInvoiceID", type = UUID.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "total", type = BigDecimal.class)

                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "SAInvoiceSaReturnPopupDTO",
        classes = {
            @ConstructorResult(
                targetClass = SAInvoicePopupDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "saInvoiceID", type = UUID.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "materialGoodsName", type = String.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "returnQuantity", type = BigDecimal.class)

                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "SAInvoiceViewDTO",
        classes = {
            @ConstructorResult(
                targetClass = SAInvoiceViewDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "noBook", type = String.class),
                    @ColumnResult(name = "invoiceNo", type = String.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "accountingObjectAddress", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalDiscountAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalVATAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalAllAmount", type = BigDecimal.class),
                    @ColumnResult(name = "recorded", type = Boolean.class),
                    @ColumnResult(name = "currencyID", type = String.class),
                    @ColumnResult(name = "exported", type = Boolean.class),
                    @ColumnResult(name = "rsInwardOutwardID", type = UUID.class),
                    @ColumnResult(name = "mcReceiptID", type = UUID.class),
                    @ColumnResult(name = "mbDepositID", type = UUID.class),
                    @ColumnResult(name = "invoiceForm", type = Integer.class),
                    @ColumnResult(name = "typeName", type = String.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "ViewFromMCReceiptDTO",
        classes = {
            @ConstructorResult(
                targetClass = SAInvoiceForMCReceiptDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "paymentClauseCode", type = String.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "dueDate", type = LocalDate.class),
                }
            )
        }
    )
})
public class SAInvoice implements Serializable {

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

    @Column(name = "nofbook")
    private String noFBook;

    @Column(name = "nombook")
    private String noMBook;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

    @Column(name = "paymentvoucherid")
    private String paymentVoucherID;

    @Column(name = "accountingobjectname")
    private String accountingObjectName;

    @Column(name = "accountingobjectaddress")
    private String accountingObjectAddress;

    @Column(name = "accountingobjectbankaccount")
    private String accountingObjectBankAccount;

    @Column(name = "accountingobjectbankname")
    private String accountingObjectBankName;

    @Column(name = "companytaxcode")
    private String companyTaxCode;

    @Column(name = "contactname")
    private String contactName;

    @Column(name = "reason")
    private String reason;

    @Column(name = "numberattach")
    private String numberAttach;

    @Column(name = "isdeliveryvoucher")
    private Boolean isDeliveryVoucher;

    @Column(name = "paymentmethod")
    private String paymentMethod;

    @Column(name = "invoiceform")
    private Integer invoiceForm;

    @Column(name = "invoicetemplate")
    private String invoiceTemplate;

    @Column(name = "invoicetypeid")
    private UUID invoiceTypeID;

    @Column(name = "invoiceseries")
    private String invoiceSeries;

    @Column(name = "invoiceno")
    private String invoiceNo;

    @Column(name = "invoicedate")
    private LocalDate invoiceDate;

    @Column(name = "currencyid")
    private String currencyID;

    @Column(name = "exchangerate", precision = 10, scale = 2)
    private BigDecimal exchangeRate;

    @Column(name = "paymentclauseid")
    private UUID paymentClauseID;

    @Column(name = "duedate")
    private LocalDate dueDate;

    @Column(name = "transportmethodid")
    private UUID transportMethodID;

    @Column(name = "employeeid")
    private UUID employeeID;

    @Column(name = "rsinwardoutwardid")
    private UUID rsInwardOutwardID;

    @Column(name = "mcreceiptid")
    private UUID mcReceiptID;

    @Column(name = "mbdepositid")
    private UUID mbDepositID;

    @Column(name = "totalamount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "totalamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalAmountOriginal;

    @Column(name = "totaldiscountamount", precision = 10, scale = 2)
    private BigDecimal totalDiscountAmount;

    @Column(name = "totaldiscountamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalDiscountAmountOriginal;

    @Column(name = "totalvatamount", precision = 10, scale = 2)
    private BigDecimal totalVATAmount;

    @Column(name = "totalvatamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalVATAmountOriginal;

    @Column(name = "totalcapitalamount", precision = 10, scale = 2)
    private BigDecimal totalCapitalAmount;

    @Column(name = "placeofdelivery")
    private String placeOfDelivery;

    @Column(name = "listno")
    private String listNo;

    @Column(name = "listdate")
    private LocalDate listDate;

    @Column(name = "listcommonnameinventory")
    private String listCommonNameInventory;

    @Column(name = "isattachlist")
    private Boolean isAttachList;

    @Column(name = "isdiscountbyvoucher")
    private Boolean isDiscountByVoucher;

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

    @Column(name = "idadjustinv")
    private UUID iDAdjustInv;

    @Column(name = "idreplaceinv")
    private UUID iDReplaceInv;

    @Column(name = "isbill")
    private Boolean isBill ;

    @Column(name = "totalexporttaxamount", precision = 10, scale = 2)
    private BigDecimal totalExportTaxAmount;

    @Column(name = "id_miv")
    private UUID iD_MIV;

    @Column(name = "templateid")
    private UUID templateID;

    @Column(name = "recorded")
    private Boolean recorded;

    @Column(name = "ismatch")
    private Boolean isMatch;

    @Column(name = "matchdate")
    private LocalDate matchDate;

    @Column(name = "exported")
    private Boolean exported;

    @Column(name = "isexportinvoice")
    private Boolean isExportInvoice;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "sainvoiceid")
    private Set<SAInvoiceDetails> sAInvoiceDetails = new HashSet<>();

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private List<RefVoucherDTO> viewVouchers;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private RSInwardOutward rsInwardOutward;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private SaBill saBill;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private MCReceipt mcReceipt;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private MBDeposit mbDeposit;


    public void setPayers(String payers) {
        this.payers = payers;
    }

    @Transient
    private String payers;


    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Boolean checkRecord;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String noBook;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPayers() {
        return payers;
    }

    public SAInvoice payers(String payers) {
        this.payers = payers;
        return this;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public SAInvoice companyID(UUID companyID) {
        this.companyID = companyID;
        return this;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public SAInvoice branchID(UUID branchID) {
        this.branchID = branchID;
        return this;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public SAInvoice typeID(Integer typeID) {
        this.typeID = typeID;
        return this;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public LocalDate getDate() {
        return date;
    }

    public SAInvoice date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public SAInvoice postedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
        return this;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public SAInvoice typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public SAInvoice noFBook(String noFBook) {
        this.noFBook = noFBook;
        return this;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public SAInvoice noMBook(String noMBook) {
        this.noMBook = noMBook;
        return this;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public SAInvoice accountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
        return this;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public String getPaymentVoucherID() {
        return paymentVoucherID;
    }

    public SAInvoice paymentVoucherID(String paymentVoucherID) {
        this.paymentVoucherID = paymentVoucherID;
        return this;
    }

    public void setPaymentVoucherID(String paymentVoucherID) {
        this.paymentVoucherID = paymentVoucherID;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public SAInvoice accountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
        return this;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getAccountingObjectAddress() {
        return accountingObjectAddress;
    }

    public SAInvoice accountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
        return this;
    }

    public void setAccountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
    }

    public String getAccountingObjectBankAccount() {
        return accountingObjectBankAccount;
    }

    public SAInvoice accountingObjectBankAccount(String accountingObjectBankAccount) {
        this.accountingObjectBankAccount = accountingObjectBankAccount;
        return this;
    }

    public void setAccountingObjectBankAccount(String accountingObjectBankAccount) {
        this.accountingObjectBankAccount = accountingObjectBankAccount;
    }

    public String getAccountingObjectBankName() {
        return accountingObjectBankName;
    }

    public SAInvoice accountingObjectBankName(String accountingObjectBankName) {
        this.accountingObjectBankName = accountingObjectBankName;
        return this;
    }

    public void setAccountingObjectBankName(String accountingObjectBankName) {
        this.accountingObjectBankName = accountingObjectBankName;
    }

    public String getCompanyTaxCode() {
        return companyTaxCode;
    }

    public SAInvoice companyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
        return this;
    }

    public void setCompanyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
    }

    public String getContactName() {
        return contactName;
    }

    public SAInvoice contactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getReason() {
        return reason;
    }

    public SAInvoice reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNumberAttach() {
        return numberAttach;
    }

    public SAInvoice numberAttach(String numberAttach) {
        this.numberAttach = numberAttach;
        return this;
    }

    public void setNumberAttach(String numberAttach) {
        this.numberAttach = numberAttach;
    }

    public Boolean isIsDeliveryVoucher() {
        return isDeliveryVoucher;
    }

    public SAInvoice isDeliveryVoucher(Boolean isDeliveryVoucher) {
        this.isDeliveryVoucher = isDeliveryVoucher;
        return this;
    }

    public void setIsDeliveryVoucher(Boolean isDeliveryVoucher) {
        this.isDeliveryVoucher = isDeliveryVoucher;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public SAInvoice paymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getInvoiceForm() {
        return invoiceForm;
    }

    public SAInvoice invoiceForm(Integer invoiceForm) {
        this.invoiceForm = invoiceForm;
        return this;
    }

    public void setInvoiceForm(Integer invoiceForm) {
        this.invoiceForm = invoiceForm;
    }

    public String getInvoiceTemplate() {
        return invoiceTemplate;
    }

    public SAInvoice invoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
        return this;
    }

    public void setInvoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
    }

    public UUID getInvoiceTypeID() {
        return invoiceTypeID;
    }

    public SAInvoice invoiceTypeID(UUID invoiceTypeID) {
        this.invoiceTypeID = invoiceTypeID;
        return this;
    }

    public void setInvoiceTypeID(UUID invoiceTypeID) {
        this.invoiceTypeID = invoiceTypeID;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public SAInvoice invoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
        return this;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public SAInvoice invoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
        return this;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public SAInvoice invoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public SAInvoice currencyID(String currencyID) {
        this.currencyID = currencyID;
        return this;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public SAInvoice exchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public UUID getRsInwardOutwardID() {
        return rsInwardOutwardID;
    }

    public void setRsInwardOutwardID(UUID rsInwardOutwardID) {
        this.rsInwardOutwardID = rsInwardOutwardID;
    }

    public UUID getPaymentClauseID() {
        return paymentClauseID;
    }

    public void setPaymentClauseID(UUID paymentClauseID) {
        this.paymentClauseID = paymentClauseID;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public UUID getTransportMethodID() {
        return transportMethodID;
    }

    public void setTransportMethodID(UUID transportMethodID) {
        this.transportMethodID = transportMethodID;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public SAInvoice totalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public SAInvoice totalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
        return this;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public SAInvoice totalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
        return this;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public BigDecimal getTotalDiscountAmountOriginal() {
        return totalDiscountAmountOriginal;
    }

    public SAInvoice totalDiscountAmountOriginal(BigDecimal totalDiscountAmountOriginal) {
        this.totalDiscountAmountOriginal = totalDiscountAmountOriginal;
        return this;
    }

    public void setTotalDiscountAmountOriginal(BigDecimal totalDiscountAmountOriginal) {
        this.totalDiscountAmountOriginal = totalDiscountAmountOriginal;
    }

    public BigDecimal getTotalVATAmount() {
        return totalVATAmount;
    }

    public SAInvoice totalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
        return this;
    }

    public void setTotalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
    }

    public BigDecimal getTotalVATAmountOriginal() {
        return totalVATAmountOriginal;
    }

    public SAInvoice totalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
        return this;
    }

    public void setTotalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
    }

    public BigDecimal getTotalCapitalAmount() {
        return totalCapitalAmount;
    }

    public SAInvoice totalCapitalAmount(BigDecimal totalCapitalAmount) {
        this.totalCapitalAmount = totalCapitalAmount;
        return this;
    }

    public void setTotalCapitalAmount(BigDecimal totalCapitalAmount) {
        this.totalCapitalAmount = totalCapitalAmount;
    }

    public String getPlaceOfDelivery() {
        return placeOfDelivery;
    }

    public SAInvoice placeOfDelivery(String placeOfDelivery) {
        this.placeOfDelivery = placeOfDelivery;
        return this;
    }

    public void setPlaceOfDelivery(String placeOfDelivery) {
        this.placeOfDelivery = placeOfDelivery;
    }

    public String getListNo() {
        return listNo;
    }

    public SAInvoice listNo(String listNo) {
        this.listNo = listNo;
        return this;
    }

    public void setListNo(String listNo) {
        this.listNo = listNo;
    }

    public LocalDate getListDate() {
        return listDate;
    }

    public SAInvoice listDate(LocalDate listDate) {
        this.listDate = listDate;
        return this;
    }

    public void setListDate(LocalDate listDate) {
        this.listDate = listDate;
    }

    public String getListCommonNameInventory() {
        return listCommonNameInventory;
    }

    public SAInvoice listCommonNameInventory(String listCommonNameInventory) {
        this.listCommonNameInventory = listCommonNameInventory;
        return this;
    }

    public void setListCommonNameInventory(String listCommonNameInventory) {
        this.listCommonNameInventory = listCommonNameInventory;
    }

    public Boolean isIsAttachList() {
        return isAttachList;
    }

    public SAInvoice isAttachList(Boolean isAttachList) {
        this.isAttachList = isAttachList;
        return this;
    }

    public void setIsAttachList(Boolean isAttachList) {
        this.isAttachList = isAttachList;
    }

    public Boolean isIsDiscountByVoucher() {
        return isDiscountByVoucher;
    }

    public SAInvoice isDiscountByVoucher(Boolean isDiscountByVoucher) {
        this.isDiscountByVoucher = isDiscountByVoucher;
        return this;
    }

    public void setIsDiscountByVoucher(Boolean isDiscountByVoucher) {
        this.isDiscountByVoucher = isDiscountByVoucher;
    }

    public Integer getStatusInvoice() {
        return statusInvoice;
    }

    public SAInvoice statusInvoice(Integer statusInvoice) {
        this.statusInvoice = statusInvoice;
        return this;
    }

    public void setStatusInvoice(Integer statusInvoice) {
        this.statusInvoice = statusInvoice;
    }

    public Integer getStatusSendMail() {
        return statusSendMail;
    }

    public SAInvoice statusSendMail(Integer statusSendMail) {
        this.statusSendMail = statusSendMail;
        return this;
    }

    public void setStatusSendMail(Integer statusSendMail) {
        this.statusSendMail = statusSendMail;
    }

    public Integer getStatusConverted() {
        return statusConverted;
    }

    public SAInvoice statusConverted(Integer statusConverted) {
        this.statusConverted = statusConverted;
        return this;
    }

    public void setStatusConverted(Integer statusConverted) {
        this.statusConverted = statusConverted;
    }

    public LocalDate getDateSendMail() {
        return dateSendMail;
    }

    public SAInvoice dateSendMail(LocalDate dateSendMail) {
        this.dateSendMail = dateSendMail;
        return this;
    }

    public void setDateSendMail(LocalDate dateSendMail) {
        this.dateSendMail = dateSendMail;
    }

    public String getEmail() {
        return email;
    }

    public SAInvoice email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getiDAdjustInv() {
        return iDAdjustInv;
    }

    public SAInvoice iDAdjustInv(UUID iDAdjustInv) {
        this.iDAdjustInv = iDAdjustInv;
        return this;
    }

    public void setiDAdjustInv(UUID iDAdjustInv) {
        this.iDAdjustInv = iDAdjustInv;
    }

    public UUID getiDReplaceInv() {
        return iDReplaceInv;
    }

    public SAInvoice iDReplaceInv(UUID iDReplaceInv) {
        this.iDReplaceInv = iDReplaceInv;
        return this;
    }

    public void setiDReplaceInv(UUID iDReplaceInv) {
        this.iDReplaceInv = iDReplaceInv;
    }

    public Boolean isIsBill () {
        return isBill ;
    }

    public SAInvoice isBill (Boolean isBill ) {
        this.isBill  = isBill ;
        return this;
    }

    public void setIsBill (Boolean isBill ) {
        this.isBill  = isBill ;
    }

    public BigDecimal getTotalExportTaxAmount() {
        return totalExportTaxAmount;
    }

    public SAInvoice totalExportTaxAmount(BigDecimal totalExportTaxAmount) {
        this.totalExportTaxAmount = totalExportTaxAmount;
        return this;
    }

    public void setTotalExportTaxAmount(BigDecimal totalExportTaxAmount) {
        this.totalExportTaxAmount = totalExportTaxAmount;
    }

    public UUID getiD_MIV() {
        return iD_MIV;
    }

    public SAInvoice iD_MIV(UUID iD_MIV) {
        this.iD_MIV = iD_MIV;
        return this;
    }

    public void setiD_MIV(UUID iD_MIV) {
        this.iD_MIV = iD_MIV;
    }

    public UUID getTemplateID() {
        return templateID;
    }

    public SAInvoice templateID(UUID templateID) {
        this.templateID = templateID;
        return this;
    }

    public void setTemplateID(UUID templateID) {
        this.templateID = templateID;
    }

    public Boolean isRecorded() {
        return recorded;
    }

    public SAInvoice recorded(Boolean recorded) {
        this.recorded = recorded;
        return this;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public Boolean isIsMatch() {
        return isMatch;
    }

    public SAInvoice isMatch(Boolean isMatch) {
        this.isMatch = isMatch;
        return this;
    }

    public void setIsMatch(Boolean isMatch) {
        this.isMatch = isMatch;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public SAInvoice matchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
        return this;
    }

    public Boolean isIsExportInvoice () {
        return isExportInvoice ;
    }

    public void setIsExportInvoice (Boolean isExportInvoice ) {
        this.isExportInvoice  = isExportInvoice ;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public Boolean isExported() {
        return exported;
    }

    public SAInvoice exported(Boolean exported) {
        this.exported = exported;
        return this;
    }

    public void setExported(Boolean exported) {
        this.exported = exported;
    }

    public List<RefVoucherDTO> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucherDTO> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }

    public RSInwardOutward getRsInwardOutward() {
        return rsInwardOutward;
    }

    public void setRsInwardOutward(RSInwardOutward rsInwardOutward) {
        this.rsInwardOutward = rsInwardOutward;
    }

    public SaBill getSaBill() {
        return saBill;
    }

    public void setSaBill(SaBill saBill) {
        this.saBill = saBill;
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

    public MCReceipt getMcReceipt() {
        return mcReceipt;
    }

    public void setMcReceipt(MCReceipt mcReceipt) {
        this.mcReceipt = mcReceipt;
    }

    public MBDeposit getMbDeposit() {
        return mbDeposit;
    }

    public void setMbDeposit(MBDeposit mbDeposit) {
        this.mbDeposit = mbDeposit;
    }

    public Set<SAInvoiceDetails> getsAInvoiceDetails() {
        return sAInvoiceDetails;
    }

    public void setsAInvoiceDetails(Set<SAInvoiceDetails> sAInvoiceDetails) {
        this.sAInvoiceDetails = sAInvoiceDetails;
    }

    public Boolean getCheckRecord() {
        return checkRecord;
    }

    public void setCheckRecord(Boolean checkRecord) {
        this.checkRecord = checkRecord;
    }

    public String getNoBook() {
        return noBook;
    }

    public void setNoBook(String noBook) {
        this.noBook = noBook;
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
        SAInvoice sAInvoice = (SAInvoice) o;
        if (sAInvoice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sAInvoice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SAInvoice{" +
            "id=" + getId() +
            ", companyID='" + getCompanyID() + "'" +
            ", branchID='" + getBranchID() + "'" +
            ", typeID=" + getTypeID() +
            ", date='" + getDate() + "'" +
            ", postedDate='" + getPostedDate() + "'" +
            ", typeLedger=" + getTypeLedger() +
            ", noFBook='" + getNoFBook() + "'" +
            ", noMBook='" + getNoMBook() + "'" +
            ", accountingObjectID='" + getAccountingObjectID() + "'" +
            ", paymentVoucherID='" + getPaymentVoucherID() + "'" +
            ", accountingObjectName='" + getAccountingObjectName() + "'" +
            ", accountingObjectAddress='" + getAccountingObjectAddress() + "'" +
            ", accountingObjectBankAccount='" + getAccountingObjectBankAccount() + "'" +
            ", accountingObjectBankName='" + getAccountingObjectBankName() + "'" +
            ", companyTaxCode='" + getCompanyTaxCode() + "'" +
            ", contactName='" + getContactName() + "'" +
            ", reason='" + getReason() + "'" +
            ", numberAttach='" + getNumberAttach() + "'" +
            ", isDeliveryVoucher='" + isIsDeliveryVoucher() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", invoiceForm=" + getInvoiceForm() +
            ", invoiceTemplate='" + getInvoiceTemplate() + "'" +
            ", invoiceTypeID='" + getInvoiceTypeID() + "'" +
            ", invoiceSeries='" + getInvoiceSeries() + "'" +
            ", invoiceNo='" + getInvoiceNo() + "'" +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", currencyID='" + getCurrencyID() + "'" +
            ", exchangeRate=" + getExchangeRate() +
            ", PaymentClauseID='" + getPaymentClauseID() + "'" +
            ", DueDate='" + getDueDate() + "'" +
            ", TransportMethodID='" + getTransportMethodID() + "'" +
            ", EmployeeID='" + getEmployeeID() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", totalAmountOriginal=" + getTotalAmountOriginal() +
            ", totalDiscountAmount=" + getTotalDiscountAmount() +
            ", totalDiscountAmountOriginal=" + getTotalDiscountAmountOriginal() +
            ", totalVATAmount=" + getTotalVATAmount() +
            ", totalVATAmountOriginal=" + getTotalVATAmountOriginal() +
            ", totalCapitalAmount=" + getTotalCapitalAmount() +
            ", placeOfDelivery='" + getPlaceOfDelivery() + "'" +
            ", listNo='" + getListNo() + "'" +
            ", listDate='" + getListDate() + "'" +
            ", listCommonNameInventory='" + getListCommonNameInventory() + "'" +
            ", isAttachList='" + isIsAttachList() + "'" +
            ", isDiscountByVoucher='" + isIsDiscountByVoucher() + "'" +
            ", statusInvoice=" + getStatusInvoice() +
            ", statusSendMail=" + getStatusSendMail() +
            ", statusConverted=" + getStatusConverted() +
            ", dateSendMail='" + getDateSendMail() + "'" +
            ", email='" + getEmail() + "'" +
            ", iDAdjustInv='" + getiDAdjustInv() + "'" +
            ", iDReplaceInv='" + getiDReplaceInv() + "'" +
            ", isBill ='" + isIsBill () + "'" +
            ", totalExportTaxAmount=" + getTotalExportTaxAmount() +
            ", iD_MIV='" + getiD_MIV() + "'" +
            ", templateID='" + getTemplateID() + "'" +
            ", recorded='" + isRecorded() + "'" +
            ", isMatch='" + isIsMatch() + "'" +
            ", matchDate='" + getMatchDate() + "'" +
            ", exported='" + isExported() + "'" +
            "}";
    }
}
