package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Strings;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.cashandbank.MBTellerPaperDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * A MBTellerPaper.
 */
@Entity
@Table(name = "mbtellerpaper")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "MBTellerPaperDTO",
        classes = {
            @ConstructorResult(
                targetClass = MBTellerPaperDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "companyId", type = UUID.class),
                    @ColumnResult(name = "branchId", type = UUID.class),
                    @ColumnResult(name = "typeId", type = Integer.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "typeLedger", type = Integer.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "accountingObjectAddress", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "recorded", type = Boolean.class),
                    @ColumnResult(name = "currencyId", type = String.class),
                    @ColumnResult(name = "typeName", type = String.class),
                }
            )
        }
    )})
public class MBTellerPaper implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyId;

    @Column(name = "branchid")
    private UUID branchId;

    @Column(name = "typeid", nullable = false)
    private Integer typeId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "posteddate", nullable = false)
    private LocalDate postedDate;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Column(name = "nofbook")
    private String noFBook;

    @Column(name = "nombook")
    private String noMBook;

    @Column(name = "bankaccountdetailid")
    private UUID bankAccountDetailID;

    @Column(name = "bankname")
    private String bankName;

    @Column(name = "reason")
    private String reason;

    @Column(name = "accountingobjecttype")
    private Integer accountingObjectType;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

    @Column(name = "accountingobjectname")
    private String accountingObjectName;

    @Column(name = "accountingobjectaddress")
    private String accountingObjectAddress;


    @Column(name = "accountingobjectbankaccount")
    private UUID accountingObjectBankAccount;

    @Column(name = "accountingobjectbankname")
    private String accountingObjectBankName;

    @Column(name = "taxcode")
    private String taxCode;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "identificationno")
    private String identificationNo;

    @Column(name = "issuedate")
    private LocalDate issueDate;

    @Column(name = "issueby")
    private String issueBy;

    @Column(name = "currencyid")
    private String currencyId;

    @Column(name = "exchangerate")
    private BigDecimal exchangeRate;

    @Column(name = "isimportpurchase")
    private Boolean isImportPurchase;

    @Column(name = "paymentclauseid")
    private UUID paymentClauseID;

    @Column(name = "transportmethodid")
    private UUID transportMethodID;

    @Column(name = "totalamount")
    private BigDecimal totalAmount;

    @Column(name = "totalamountoriginal")
    private BigDecimal totalAmountOriginal;

    @Column(name = "totalvatamount")
    private BigDecimal totalVatAmount;

    @NotNull
    @Column(name = "totalvatamountoriginal")
    private BigDecimal totalVatAmountOriginal;

    @Column(name = "employeeid")
    private UUID employeeID;

    @Column(name = "templateid")
    private UUID templateId;

    @Column(name = "ismatch")
    private Boolean isMatch;

    @Column(name = "matchdate")
    private LocalDate matchDate;

    @Column(name = "recorded")
    private Boolean recorded;

    @OneToMany(cascade={CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "mbtellerpaperid")
    private Set<MBTellerPaperDetails> mBTellerPaperDetails = new HashSet<>();

    @OneToMany(cascade={CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "mbtellerpaperid")
    private Set<MBTellerPaperDetailTax> mBTellerPaperDetailTaxs = new HashSet<>();

    @OneToMany(cascade={CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "mbtellerpaperid")
    private Set<MBTellerPaperDetailVendor> mBTellerPaperDetailVendor = new HashSet<>();

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private List<RefVoucherDTO> viewVouchers;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Number total;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private UUID ppServiceID;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private UUID ppInvocieID;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Boolean storedInRepository;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String typeName;

    public List<RefVoucherDTO> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucherDTO> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }

    public MBTellerPaper() {
    }

    public MBTellerPaper(UUID id, Boolean recorded, Integer typeId, UUID accountPaymentId, String accountPaymentName, String reason,
                         AccountingObjectBankAccount accountReceiver, String accountReceiverName, AccountingObject accountingObject,
                         String accountingObjectName, String accountingObjectAddress, AccountingObject employee,
                         Integer typeLedger, String noFBook, String noMBook, LocalDate receiptDate, LocalDate postedDate,
                         UUID companyId, String currencyID,
                         BigDecimal totalAmount, BigDecimal totalAmountOriginal, BigDecimal totalVATAmount,
                         BigDecimal totalVATAmountOriginal, BigDecimal exchangeRate, String receiver, String identificationNo,
                         LocalDate issueDate, String issueBy) {
        if (issueDate != null) {
            this.issueDate = issueDate;
        }
        if (issueBy != null) {
            this.issueBy = issueBy;
        }
        if (receiver != null) {
            this.receiver = receiver; // Người nhận tiền
        }
        if (identificationNo != null) {
            this.identificationNo = identificationNo; // Số CMND người lĩnh tiền
        }

        if (accountReceiver != null) {
            this.setAccountingObjectBankAccount(accountReceiver.getId());
        }

        if (accountingObject != null) {
            this.setAccountingObjectID(accountingObject.getId());
        }

        if (employee != null) {
            this.setEmployeeID(employee.getId());
        }

        if (id != null) {
            this.setId(id);
        }
        this.setRecorded(recorded != null ? recorded : false);
        if (typeId != null) {
            this.setTypeId(typeId);
        }
        if (accountPaymentId != null) {
            this.setBankAccountDetailID(accountPaymentId);
        }
        if (accountPaymentName != null) {
            this.setBankName(accountPaymentName);
        }
        if (reason != null) {
            this.setReason(reason);
        }
        if (accountReceiverName != null) {
            this.setAccountingObjectBankName(accountReceiverName);
        }
        if (accountingObjectName != null) {
            this.setAccountingObjectName(accountingObjectName);
        }
        if (accountingObjectAddress != null) {
            this.setAccountingObjectAddress(accountingObjectAddress);
        }
        if (totalAmount != null) {
            this.setTotalAmount(totalAmount);
        }
        if (totalAmountOriginal != null) {
            this.setTotalAmountOriginal(totalAmountOriginal);
        }
        if (totalVATAmount != null) {
            this.setTotalVatAmount(totalVATAmount);
        }
        if (totalVATAmountOriginal != null) {
            this.setTotalVatAmountOriginal(totalVATAmountOriginal);
        }
        if (exchangeRate != null) {
            this.setExchangeRate(exchangeRate);
        }
        if (!Strings.isNullOrEmpty(noFBook)) {
            this.setNoFBook(noFBook);
        }
        if (!Strings.isNullOrEmpty(noMBook)) {
            this.setNoMBook(noMBook);
        }
        if (typeLedger != null) {
            this.setTypeLedger(typeLedger);
        }
        if (receiptDate != null) {
            this.setDate(receiptDate);
        }
        if (postedDate != null) {
            this.setPostedDate(postedDate);
        }
        if (companyId != null) {
            this.setCompanyId(companyId);
        }
        if (currencyID != null) {
            this.setCurrencyId(currencyID);
        }
    }
    public MBTellerPaper(UUID id, UUID companyId, UUID branchId,
                         Integer typeId, LocalDate date, LocalDate postedDate,
                         Integer typeLedger, String noFBook, String noMBook,
                         String accountingObjectName, String accountingObjectAddress,
                         String reason, BigDecimal totalAmount, BigDecimal totalAmountOriginal,
                         Boolean recorded, String currencyId, String typeName
    ) {
        this.id = id;
        this.companyId = companyId;
        this.branchId = branchId;
        this.typeId = typeId;
        this.date = date;
        this.postedDate = postedDate;
        this.typeLedger = typeLedger;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.accountingObjectName = accountingObjectName;
        this.accountingObjectAddress = accountingObjectAddress;
        this.reason = reason;
        this.totalAmount = totalAmount;
        this.totalAmountOriginal = totalAmountOriginal;
        this.recorded = recorded;
        this.currencyId = currencyId;
        this.typeName = typeName;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public MBTellerPaper date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public MBTellerPaper reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReceiver() {
        return receiver;
    }

    public MBTellerPaper receiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

/*    public UUID getPporderid() {
        return pporderid;
    }

    public MBTellerPaper pporderid(UUID pporderid) {
        this.pporderid = pporderid;
        return this;
    }

    public void setPporderid(UUID pporderid) {
        this.pporderid = pporderid;
    }*/

    public Boolean isRecorded() {
        return recorded;
    }

    public MBTellerPaper recorded(Boolean recorded) {
        this.recorded = recorded;
        return this;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public Boolean getRecorded() {
        return recorded;
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
        MBTellerPaper mBTellerPaper = (MBTellerPaper) o;
        if (mBTellerPaper.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mBTellerPaper.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MBTellerPaper{" +
            "id=" + getId() +
            ", recorded='" + isRecorded() + "'" +
            "}";
    }

//    public Set<MBTellerPaperDetails> getmBTellerPaperDetails() {
//        return mBTellerPaperDetails;
//    }
//
//    public void setmBTellerPaperDetails(Set<MBTellerPaperDetails> mBTellerPaperDetails) {
//        this.mBTellerPaperDetails = mBTellerPaperDetails;
//    }


    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public UUID getAccountingObjectBankAccount() {
        return accountingObjectBankAccount;
    }

    public void setAccountingObjectBankAccount(UUID accountingObjectBankAccount) {
        this.accountingObjectBankAccount = accountingObjectBankAccount;
    }

    public UUID getPaymentClauseID() {
        return paymentClauseID;
    }

    public void setPaymentClauseID(UUID paymentClauseID) {
        this.paymentClauseID = paymentClauseID;
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

    public Set<MBTellerPaperDetails> getmBTellerPaperDetails() {
        return mBTellerPaperDetails;
    }

    public void setmBTellerPaperDetails(Set<MBTellerPaperDetails> mBTellerPaperDetails) {
        this.mBTellerPaperDetails = mBTellerPaperDetails;
    }
    public MBTellerPaper mBTellerPaperDetails(Set<MBTellerPaperDetails> mBTellerPaperDetails) {
        this.mBTellerPaperDetails = mBTellerPaperDetails;
        return this;
    }

    public Set<MBTellerPaperDetailTax> getmBTellerPaperDetailTaxs() {
        return mBTellerPaperDetailTaxs;
    }

    public void setmBTellerPaperDetailTaxs(Set<MBTellerPaperDetailTax> mBTellerPaperDetailTaxs) {
        this.mBTellerPaperDetailTaxs = mBTellerPaperDetailTaxs;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public UUID getBranchId() {
        return branchId;
    }

    public void setBranchId(UUID branchId) {
        this.branchId = branchId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getAccountingObjectType() {
        return accountingObjectType;
    }

    public void setAccountingObjectType(Integer accountingObjectType) {
        this.accountingObjectType = accountingObjectType;
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

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getIdentificationNo() {
        return identificationNo;
    }

    public void setIdentificationNo(String identificationNo) {
        this.identificationNo = identificationNo;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssueBy() {
        return issueBy;
    }

    public void setIssueBy(String issueBy) {
        this.issueBy = issueBy;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Boolean getImportPurchase() {
        return isImportPurchase;
    }

    public MBTellerPaper isImportPurchase(Boolean isImportPurchase) {
        this.isImportPurchase = isImportPurchase;
        return this;
    }

    public void setImportPurchase(Boolean importPurchase) {
        isImportPurchase = importPurchase;
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

    public BigDecimal getTotalVatAmount() {
        return totalVatAmount;
    }

    public void setTotalVatAmount(BigDecimal totalVatAmount) {
        this.totalVatAmount = totalVatAmount;
    }

    public BigDecimal getTotalVatAmountOriginal() {
        return totalVatAmountOriginal;
    }

    public void setTotalVatAmountOriginal(BigDecimal totalVatAmountOriginal) {
        this.totalVatAmountOriginal = totalVatAmountOriginal;
    }

    public UUID getTemplateId() {
        return templateId;
    }

    public void setTemplateId(UUID templateId) {
        this.templateId = templateId;
    }

    public Boolean getMatch() {
        return isMatch;
    }

    public MBTellerPaper isMatch(Boolean isMatch) {
        this.isMatch = isMatch;
        return this;
    }

    public void setMatch(Boolean match) {
        isMatch = match;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public UUID getBankAccountDetailID() {
        return bankAccountDetailID;
    }

    public void setBankAccountDetailID(UUID bankAccountDetailID) {
        this.bankAccountDetailID = bankAccountDetailID;
    }

    public Number getTotal() {
        return total;
    }

    public void setTotal(Number total) {
        this.total = total;
    }

    public UUID getPpServiceID() {
        return ppServiceID;
    }

    public void setPpServiceID(UUID ppServiceID) {
        this.ppServiceID = ppServiceID;
    }

    public UUID getPpInvocieID() {
        return ppInvocieID;
    }

    public void setPpInvocieID(UUID ppInvocieID) {
        this.ppInvocieID = ppInvocieID;
    }

    public Boolean getStoredInRepository() {
        return storedInRepository;
    }

    public void setStoredInRepository(Boolean storedInRepository) {
        this.storedInRepository = storedInRepository;
    }

    public Set<MBTellerPaperDetailVendor> getmBTellerPaperDetailVendor() {
        return mBTellerPaperDetailVendor;
    }

    public void setmBTellerPaperDetailVendor(Set<MBTellerPaperDetailVendor> mBTellerPaperDetailVendor) {
        this.mBTellerPaperDetailVendor = mBTellerPaperDetailVendor;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}

