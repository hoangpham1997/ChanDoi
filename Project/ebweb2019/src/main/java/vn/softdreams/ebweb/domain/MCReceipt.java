package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.cashandbank.MCReceiptDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * A MCReceipt.
 */
@Entity
@Table(name = "mcreceipt")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "MCReceiptDTO",
        classes = {
            @ConstructorResult(
                targetClass = MCReceiptDTO.class,
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
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "accountingObjectAddress", type = String.class),
                    @ColumnResult(name = "payers", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "totalamount", type = BigDecimal.class),
                    @ColumnResult(name = "recorded", type = Boolean.class),
                    @ColumnResult(name = "currencyID", type = String.class),
                    @ColumnResult(name = "totalAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "typeName", type = String.class),
                }
            )
        }
    )})
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
//@JsonIgnoreType
public class MCReceipt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    @NotNull
    @Column(name = "typeid", nullable = false)
    private Integer typeID;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "posteddate", nullable = false)
    private LocalDate postedDate;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Size(max = 25)
    @Column(name = "nofbook", length = 25)
    private String noFBook;

    @Size(max = 25)
    @Column(name = "nombook", length = 25)
    private String noMBook;

    @Size(max = 512)
    @Column(name = "accountingobjectname", length = 512)
    private String accountingObjectName;

    @Size(max = 512)
    @Column(name = "accountingobjectaddress", length = 512)
    private String accountingObjectAddress;

    @Size(max = 512)
    @Column(name = "payers", length = 512)
    private String payers;

    @Size(max = 512)
    @Column(name = "reason", length = 512)
    private String reason;

    @Size(max = 512)
    @Column(name = "numberattach", length = 512)
    private String numberAttach;

    @Column(name = "currencyid")
    private String currencyID;

    @Column(name = "exchangerate", precision = 10, scale = 2)
    private BigDecimal exchangeRate;

    @NotNull
    @Column(name = "totalamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @NotNull
    @Column(name = "totalamountoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmountOriginal;

    @NotNull
    @Column(name = "totalvatamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalVATAmount;

    @NotNull
    @Column(name = "totalvatamountoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalVATAmountOriginal;

    @Column(name = "employeeid")
    private UUID employeeID;

    @Column(name = "accountingobjecttype")
    private Integer accountingObjectType;

    @Column(name = "templateid")
    private UUID templateID;

    @NotNull
    @Column(name = "recorded", nullable = false)
    private Boolean recorded;

    @NotNull
    @Column(name = "exported", nullable = false)
    private Boolean exported;

    @Column(name = "taxcode")
    private String taxCode;

    @Column(name = "mcauditid")
    private UUID mCAuditID;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "mcreceiptid")
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<MCReceiptDetails> mCReceiptDetails = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("")
    @JoinColumn(name = "accountingobjectid")
    private AccountingObject accountingObject;

    @ManyToOne
    @JsonIgnoreProperties("")
    @JoinColumn(name = "transportmethodid")
    private TransportMethod transportMethod;

    @ManyToOne
    @JsonIgnoreProperties("")
    @JoinColumn(name = "paymentclauseid")
    private PaymentClause paymentClause;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "mcreceiptid")
    private Set<MCReceiptDetailCustomer> mCReceiptDetailCustomers = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "mcreceiptid")
    private Set<MCReceiptDetailTax> mCReceiptDetailTaxes = new HashSet<>();

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private List<RefVoucherDTO> viewVouchers;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private UUID sAInvoiceID;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Integer typeIDSAInvoice;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private UUID rSInwardOutwardID;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Boolean sAIsBill;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Integer sAInvoiceForm;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String sAInvoiceNo;


    public Integer getTypeIDSAInvoice() {
        return typeIDSAInvoice;
    }

    public void setTypeIDSAInvoice(Integer typeIDSAInvoice) {
        this.typeIDSAInvoice = typeIDSAInvoice;
    }

    public UUID getrSInwardOutwardID() {
        return rSInwardOutwardID;
    }

    public void setrSInwardOutwardID(UUID rSInwardOutwardID) {
        this.rSInwardOutwardID = rSInwardOutwardID;
    }

    public UUID getsAInvoiceID() {
        return sAInvoiceID;
    }

    public void setsAInvoiceID(UUID sAInvoiceID) {
        this.sAInvoiceID = sAInvoiceID;
    }

    public List<RefVoucherDTO> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucherDTO> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public MCReceipt branchID(UUID branchID) {
        this.branchID = branchID;
        return this;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public MCReceipt typeID(Integer typeID) {
        this.typeID = typeID;
        return this;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public LocalDate getDate() {
        return date;
    }

    public MCReceipt date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public MCReceipt postedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
        return this;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public MCReceipt typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public MCReceipt noFBook(String noFBook) {
        this.noFBook = noFBook;
        return this;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public MCReceipt noMBook(String noMBook) {
        this.noMBook = noMBook;
        return this;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public MCReceipt accountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
        return this;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getAccountingObjectAddress() {
        return accountingObjectAddress;
    }

    public MCReceipt accountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
        return this;
    }

    public void setAccountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
    }

    public String getPayers() {
        return payers;
    }

    public MCReceipt payers(String payers) {
        this.payers = payers;
        return this;
    }

    public void setPayers(String payers) {
        this.payers = payers;
    }

    public String getReason() {
        return reason;
    }

    public MCReceipt reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNumberAttach() {
        return numberAttach;
    }

    public MCReceipt numberAttach(String numberAttach) {
        this.numberAttach = numberAttach;
        return this;
    }

    public void setNumberAttach(String numberAttach) {
        this.numberAttach = numberAttach;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public MCReceipt currencyID(String currencyID) {
        this.currencyID = currencyID;
        return this;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public MCReceipt exchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public MCReceipt totalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public MCReceipt totalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
        return this;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }

    public BigDecimal getTotalVATAmount() {
        return totalVATAmount;
    }

    public MCReceipt totalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
        return this;
    }

    public void setTotalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
    }

    public BigDecimal getTotalVATAmountOriginal() {
        return totalVATAmountOriginal;
    }

    public MCReceipt totalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
        return this;
    }

    public void setTotalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public MCReceipt employeeID(UUID employeeID) {
        this.employeeID = employeeID;
        return this;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public Integer getAccountingObjectType() {
        return accountingObjectType;
    }

    public MCReceipt accountingObjectType(Integer accountingObjectType) {
        this.accountingObjectType = accountingObjectType;
        return this;
    }

    public void setAccountingObjectType(Integer accountingObjectType) {
        this.accountingObjectType = accountingObjectType;
    }

    public UUID getTemplateID() {
        return templateID;
    }

    public MCReceipt templateID(UUID templateID) {
        this.templateID = templateID;
        return this;
    }

    public void setTemplateID(UUID templateID) {
        this.templateID = templateID;
    }

    public Boolean isRecorded() {
        return recorded;
    }

    public MCReceipt recorded(Boolean recorded) {
        this.recorded = recorded;
        return this;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public Boolean isExported() {
        return exported;
    }

    public MCReceipt exported(Boolean exported) {
        this.exported = exported;
        return this;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public MCReceipt taxCode(String taxCode) {
        this.taxCode = taxCode;
        return this;
    }

    public void setExported(Boolean exported) {
        this.exported = exported;
    }

    public Set<MCReceiptDetails> getMCReceiptDetails() {
        return mCReceiptDetails;
    }

    public MCReceipt mCReceiptDetails(Set<MCReceiptDetails> mCReceiptDetails) {
        this.mCReceiptDetails = mCReceiptDetails;
        return this;
    }

//    public MCReceipt addMCReceiptDetails(MCReceiptDetails mCReceiptDetails) {
//        this.mCReceiptDetails.add(mCReceiptDetails);
//        mCReceiptDetails.setMCReceipt(this);
//        return this;
//    }
//
//    public MCReceipt removeMCReceiptDetails(MCReceiptDetails mCReceiptDetails) {
//        this.mCReceiptDetails.remove(mCReceiptDetails);
//        mCReceiptDetails.setMCReceipt(null);
//        return this;
//    }

    public void setMCReceiptDetails(Set<MCReceiptDetails> mCReceiptDetails) {
        this.mCReceiptDetails = mCReceiptDetails;
    }

    public AccountingObject getAccountingObject() {
        return accountingObject;
    }

    public MCReceipt accountingObject(AccountingObject accountingObject) {
        this.accountingObject = accountingObject;
        return this;
    }

    public void setAccountingObject(AccountingObject accountingObject) {
        this.accountingObject = accountingObject;
    }

    public TransportMethod getTransportMethod() {
        return transportMethod;
    }

    public MCReceipt transportMethod(TransportMethod transportMethod) {
        this.transportMethod = transportMethod;
        return this;
    }

    public void setTransportMethod(TransportMethod transportMethod) {
        this.transportMethod = transportMethod;
    }

    public PaymentClause getPaymentClause() {
        return paymentClause;
    }

    public MCReceipt paymentClause(PaymentClause paymentClause) {
        this.paymentClause = paymentClause;
        return this;
    }

    public void setPaymentClause(PaymentClause paymentClause) {
        this.paymentClause = paymentClause;
    }

    public Set<MCReceiptDetailCustomer> getMCReceiptDetailCustomers() {
        return mCReceiptDetailCustomers;
    }

    public MCReceipt mCReceiptDetailCustomers(Set<MCReceiptDetailCustomer> mCReceiptDetailCustomers) {
        this.mCReceiptDetailCustomers = mCReceiptDetailCustomers;
        return this;
    }

    public void setMCReceiptDetailCustomers(Set<MCReceiptDetailCustomer> mCReceiptDetailCustomers) {
        this.mCReceiptDetailCustomers = mCReceiptDetailCustomers;
    }

    public Set<MCReceiptDetailTax> getMCReceiptDetailTaxes() {
        return mCReceiptDetailTaxes;
    }

    public MCReceipt mCReceiptDetailTaxes(Set<MCReceiptDetailTax> mCReceiptDetailTaxes) {
        this.mCReceiptDetailTaxes = mCReceiptDetailTaxes;
        return this;
    }

//    public MCReceipt addMCReceiptDetailTax(MCReceiptDetailTax mCReceiptDetailTax) {
//        this.mCReceiptDetailTaxes.add(mCReceiptDetailTax);
//        mCReceiptDetailTax.setMCReceipt(this);
//        return this;
//    }
//
//    public MCReceipt removeMCReceiptDetailTax(MCReceiptDetailTax mCReceiptDetailTax) {
//        this.mCReceiptDetailTaxes.remove(mCReceiptDetailTax);
//        mCReceiptDetailTax.setMCReceipt(null);
//        return this;
//    }

    public void setMCReceiptDetailTaxes(Set<MCReceiptDetailTax> mCReceiptDetailTaxes) {
        this.mCReceiptDetailTaxes = mCReceiptDetailTaxes;
    }

    public UUID getmCAuditID() {
        return mCAuditID;
    }

    public void setmCAuditID(UUID mCAuditID) {
        this.mCAuditID = mCAuditID;
    }

    public Boolean getsAIsBill() {
        return sAIsBill;
    }

    public void setsAIsBill(Boolean sAIsBill) {
        this.sAIsBill = sAIsBill;
    }

    public Integer getsAInvoiceForm() {
        return sAInvoiceForm;
    }

    public void setsAInvoiceForm(Integer sAInvoiceForm) {
        this.sAInvoiceForm = sAInvoiceForm;
    }

    public String getsAInvoiceNo() {
        return sAInvoiceNo;
    }

    public void setsAInvoiceNo(String sAInvoiceNo) {
        this.sAInvoiceNo = sAInvoiceNo;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public Boolean getExported() {
        return exported;
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
        MCReceipt mCReceipt = (MCReceipt) o;
        if (mCReceipt.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mCReceipt.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MCReceipt{" +
            "id=" + getId() +
            ", branchID=" + getBranchID() +
            ", typeID=" + getTypeID() +
            ", date='" + getDate() + "'" +
            ", postedDate='" + getPostedDate() + "'" +
            ", typeLedger=" + getTypeLedger() +
            ", noFBook='" + getNoFBook() + "'" +
            ", noMBook='" + getNoMBook() + "'" +
            ", accountingObjectName='" + getAccountingObjectName() + "'" +
            ", accountingObjectAddress='" + getAccountingObjectAddress() + "'" +
            ", payers='" + getPayers() + "'" +
            ", reason='" + getReason() + "'" +
            ", numberAttach='" + getNumberAttach() + "'" +
            ", currencyID='" + getCurrencyID() + "'" +
            ", exchangeRate=" + getExchangeRate() +
            /*", sAQuoteID=" + getsAQuoteID() +
            ", sAOrderID=" + getsAOrderID() +*/
            ", totalAmount=" + getTotalAmount() +
            ", totalAmountOriginal=" + getTotalAmountOriginal() +
            ", totalVATAmount=" + getTotalVATAmount() +
            ", totalVATAmountOriginal=" + getTotalVATAmountOriginal() +
            ", employeeID=" + getEmployeeID() +
            ", accountingObjectType=" + getAccountingObjectType() +
            ", templateID=" + getTemplateID() +
            ", recorded='" + isRecorded() + "'" +
            ", exported='" + isExported() + "'" +
            ", taxCode='" + getTaxCode() + "'" +
            ", mCAuditID='" + getmCAuditID() + "'" +
            "}";
    }
}
