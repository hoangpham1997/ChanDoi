package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Strings;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.cashandbank.MBCreditCardExportDTO;
import vn.softdreams.ebweb.web.rest.dto.MBCreditCardViewDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * A MBDeposit.
 */
@Entity
@Table(name = "mbcreditcard")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "MBCreditCardExportDTO",
        classes = {
            @ConstructorResult(
                targetClass = MBCreditCardExportDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "noBook", type = String.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "typeIDInWord", type = String.class),
                    @ColumnResult(name = "creditCardNumber", type = String.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "MBCreditCardViewDTO",
        classes = {
            @ConstructorResult(
                targetClass = MBCreditCardViewDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "typeLedger", type = Integer.class),
                    @ColumnResult(name = "creditCardNumber", type = String.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "accountingObjectAddress", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "currencyID", type = String.class),
                    @ColumnResult(name = "exchangeRate", type = BigDecimal.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "totalVATAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalVATAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "recorded", type = Boolean.class)
                }
            )
        }
    )})
public class MBCreditCard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "branchid")
    private UUID branchID;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "creditcardid")
    private UUID creditCardID;

    //    @NotNull
    @Column(name = "typeid")
    private Integer typeID;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "posteddate", nullable = false)
    private LocalDate postedDate;

    @Column(name = "typeledger")
    private Integer typeLedger;

    //    @NotNull
    @Size(max = 25)
    @Column(name = "nofbook", length = 25)
    private String noFBook;

    //    @NotNull
    @Size(max = 25)
    @Column(name = "nombook", length = 25)
    private String noMBook;

    @Column(name = "creditcardnumber", length = 25)
    private String creditCardNumber;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

    //    @NotNull
    @Size(max = 512)
    @Column(name = "accountingobjectname", length = 512)
    private String accountingObjectName;

    @Size(max = 512)
    @Column(name = "accountingobjectaddress", length = 512)
    private String accountingObjectAddress;


    @Column(name = "accountingobjectbankaccountdetailid")
    private UUID accountingObjectBankAccountDetailID;

    @Size(max = 512)
    @Column(name = "accountingobjectbankname", length = 512)
    private String accountingObjectBankName;

    @Size(max = 512)
    @Column(name = "reason", length = 512)
    private String reason;


    @Column(name = "exchangerate", precision = 10, scale = 2)
    private BigDecimal exchangeRate;

    @Column(name = "taxcode")
    private String taxCode;

    @Column(name = "isimportpurchase")
    private Boolean IsImportPurchase;

    @Column(name = "paymentclauseid")
    private String paymentClauseID;

    @Column(name = "transportmethodid")
    private String transportMethodID;

    @NotNull
    @Column(name = "totalamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @NotNull
    @Column(name = "totalamountoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmountOriginal;

    //    @NotNull
    @Column(name = "totalvatamount", precision = 10, scale = 2)
    private BigDecimal totalVATAmount;

    //    @NotNull
    @Column(name = "totalvatamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalVATAmountOriginal;


//    @ManyToOne
//    @JsonIgnoreProperties("")
//    @JoinColumn(name = "employeeid")
//    private AccountingObject employee;

    @Column(name = "employeeid", length = 3)
    private UUID employeeID;

    @Column(name = "currencyid", length = 3)
    private String currencyID;

    @Column(name = "accountingobjecttype")
    private Integer accountingObjectType;

    @Column(name = "templateid")
    private String templateID;

    @Column(name = "recorded")
    private Boolean recorded;


    @Column(name = "ismatch")
    private boolean isMatch;

    @Column(name = "matchdate")
    private LocalDate matchDate;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private List<RefVoucherDTO> viewVouchers;

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
    Boolean storedInRepository;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "mbcreditcardid")
    private Set<MBCreditCardDetails> mbCreditCardDetails = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "mbcreditcardid")
    private Set<MBCreditCardDetailTax> mbCreditCardDetailTax = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "mbcreditcardid")
    private Set<MBCreditCardDetailVendor> mBCreditCardDetailVendor = new HashSet<>();

    public Set<MBCreditCardDetailTax> getMbCreditCardDetailTax() {
        return mbCreditCardDetailTax;
    }

    public void setMbCreditCardDetailTax(Set<MBCreditCardDetailTax> mBCreditCardDetailTax) {
        this.mbCreditCardDetailTax = mBCreditCardDetailTax;
    }

    public MBCreditCard(UUID id, Boolean recorded, Integer typeId, String creditCardNumber, String reason, UUID accountReceiverId,
                        String accountReceiverName, UUID accountingObjectId, String accountObjectName,
                        String accountingObjectAddress, UUID employeeId,
                        Integer typeLedger, String noFBook, String noMBook, LocalDate receiptDate, LocalDate postedDate,
                        UUID companyId, String currencyID,
                        BigDecimal totalAmount, BigDecimal totalAmountOriginal, BigDecimal totalVATAmount,
                        BigDecimal totalVATAmountOriginal, BigDecimal exchangeRate, UUID creditCardId) {
        if (creditCardId != null) {
            this.creditCardID = creditCardId;
        }
        if (id != null) {
            this.setId(id);
        }
        this.setRecorded(recorded != null ? recorded : false);
        if (totalAmount != null) {
            this.setTotalAmount(totalAmount);
        }
        if (totalAmountOriginal != null) {
            this.setTotalAmountOriginal(totalAmountOriginal);
        }
        if (totalVATAmount != null) {
            this.setTotalVATAmount(totalVATAmount);
        }
        if (totalVATAmountOriginal != null) {
            this.setTotalVATAmountOriginal(totalVATAmountOriginal);
        }
        if (exchangeRate != null) {
            this.setExchangeRate(exchangeRate);
        }
        if (typeId != null) {
            this.setTypeID(typeId);
        }
        if (creditCardNumber != null) {
            this.setCreditCardNumber(creditCardNumber);
        }
        if (reason != null) {
            this.setReason(reason);
        }
        if (accountReceiverId != null) {
            this.setAccountingObjectBankAccountDetailID(accountReceiverId);
        }
        if (accountReceiverName != null) {
            this.setAccountingObjectBankName(accountReceiverName);
        }
        if (accountingObjectId != null) {
            this.setAccountingObjectID(accountingObjectId);
        }
        if (accountObjectName != null) {
            this.setAccountingObjectName(accountObjectName);
        }
        if (accountingObjectAddress != null) {
            this.setAccountingObjectAddress(accountingObjectAddress);
        }
        if (employeeId != null) {
            this.setEmployeeID(employeeId);
        }

        if (typeLedger != null) {
            this.setTypeLedger(typeLedger);

        }
        if (!Strings.isNullOrEmpty(noFBook)) {
            this.setNoFBook(noFBook);
        }
        if (!Strings.isNullOrEmpty(noMBook)) {
            this.setNoMBook(noMBook);
        }
        if (receiptDate != null) {
            this.setDate(receiptDate);
        }
        if (postedDate != null) {
            this.setPostedDate(postedDate);
        }
        if (companyId != null) {
            this.setCompanyID(companyId);
        }
        if (currencyID != null) {
            this.setCurrencyID(currencyID);
        }
    }

    public List<RefVoucherDTO> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucherDTO> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public void setMatch(boolean match) {
        isMatch = match;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public Boolean getIsImportPurchase() {
        return IsImportPurchase;
    }


    public void setmBCreditCardDetails(Set<MBCreditCardDetails> mBCreditCardDetails) {
        this.mbCreditCardDetails = mBCreditCardDetails;
    }

    public Set<MBCreditCardDetails> getmBCreditCardDetails() {
        return mbCreditCardDetails;
    }

    public MBCreditCard() {
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

    public MBCreditCard branchID(UUID branchID) {
        this.branchID = branchID;
        return this;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public MBCreditCard typeID(Integer typeID) {
        this.typeID = typeID;
        return this;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public LocalDate getDate() {
        return date;
    }

    public MBCreditCard date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public MBCreditCard postedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
        return this;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public MBCreditCard typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public MBCreditCard noFBook(String noFBook) {
        this.noFBook = noFBook;
        return this;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public MBCreditCard noMBook(String noMBook) {
        this.noMBook = noMBook;
        return this;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public MBCreditCard accountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
        return this;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getAccountingObjectAddress() {
        return accountingObjectAddress;
    }

    public MBCreditCard accountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
        return this;
    }

    public void setAccountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
    }

    public UUID getAccountingObjectBankAccountDetailID() {
        return accountingObjectBankAccountDetailID;
    }

    public void setAccountingObjectBankAccountDetailID(UUID accountingObjectBankAccountDetailID) {
        this.accountingObjectBankAccountDetailID = accountingObjectBankAccountDetailID;
    }

    public String getAccountingObjectBankName() {
        return accountingObjectBankName;
    }

    public void setAccountingObjectBankName(String accountingObjectBankName) {
        this.accountingObjectBankName = accountingObjectBankName;
    }

    public String getReason() {
        return reason;
    }

    public MBCreditCard reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public MBCreditCard exchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }
    //
//    public String getpPOrderID() {
//        return pPOrderID;
//    }
//
//    public MBCreditCard pPOrderID(String pPOrderID) {
//        this.pPOrderID = pPOrderID;
//        return this;
//    }
//
//    public void setpPOrderID(String pPOrderID) {
//        this.pPOrderID = pPOrderID;
//    }


    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public Boolean getImportPurchase() {
        return IsImportPurchase;
    }

    public void setImportPurchase(Boolean importPurchase) {
        IsImportPurchase = importPurchase;
    }


    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public String getPaymentClauseID() {
        return paymentClauseID;
    }

    public MBCreditCard paymentClauseID(String paymentClauseID) {
        this.paymentClauseID = paymentClauseID;
        return this;
    }

    public void setPaymentClauseID(String paymentClauseID) {
        this.paymentClauseID = paymentClauseID;
    }

    public String getTransportMethodID() {
        return transportMethodID;
    }

    public MBCreditCard transportMethodID(String transportMethodID) {
        this.transportMethodID = transportMethodID;
        return this;
    }

    public void setTransportMethodID(String transportMethodID) {
        this.transportMethodID = transportMethodID;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public MBCreditCard totalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public MBCreditCard totalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
        return this;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }

    public BigDecimal getTotalVATAmount() {
        return totalVATAmount;
    }

    public MBCreditCard totalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
        return this;
    }

    public void setTotalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
    }

    public BigDecimal getTotalVATAmountOriginal() {
        return totalVATAmountOriginal;
    }

    public MBCreditCard totalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
        return this;
    }

    public void setTotalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }


//    public AccountingObject getEmployee() {
//        return employee;
//    }
//
//    public void setEmployee(AccountingObject employee) {
//        this.employee = employee;
//    }

    public Integer getAccountingObjectType() {
        return accountingObjectType;
    }

    public MBCreditCard accountingObjectType(Integer accountingObjectType) {
        this.accountingObjectType = accountingObjectType;
        return this;
    }

    public void setAccountingObjectType(Integer accountingObjectType) {
        this.accountingObjectType = accountingObjectType;
    }

    public String getTemplateID() {
        return templateID;
    }

    public MBCreditCard templateID(String templateID) {
        this.templateID = templateID;
        return this;
    }

    public void setTemplateID(String templateID) {
        this.templateID = templateID;
    }

    public Boolean isRecorded() {
        return recorded;
    }

    public MBCreditCard recorded(Boolean recorded) {
        this.recorded = recorded;
        return this;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public void setIsImportPurchase(Boolean isImportPurchase) {
        this.IsImportPurchase = isImportPurchase;
    }

    public Set<MBCreditCardDetails> getMbCreditCardDetails() {
        return mbCreditCardDetails;
    }

    public void setMbCreditCardDetails(Set<MBCreditCardDetails> mbCreditCardDetails) {
        this.mbCreditCardDetails = mbCreditCardDetails;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public Boolean isIsMatch() {
        return isMatch;
    }

    public MBCreditCard isMatch(Boolean isMatch) {
        this.isMatch = isMatch;
        return this;
    }

    public void setIsMatch(Boolean isMatch) {
        this.isMatch = isMatch;
    }

    public Set<MBCreditCardDetailVendor> getmBCreditCardDetailVendor() {
        return mBCreditCardDetailVendor;
    }

    public void setmBCreditCardDetailVendor(Set<MBCreditCardDetailVendor> mBCreditCardDetailVendor) {
        this.mBCreditCardDetailVendor = mBCreditCardDetailVendor;
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

    public UUID getCreditCardID() {
        return creditCardID;
    }

    public void setCreditCardID(UUID creditCardID) {
        this.creditCardID = creditCardID;
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
        MBCreditCard mBCreditCard = (MBCreditCard) o;
        if (mBCreditCard.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mBCreditCard.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MBCreditCard{" +
            "id=" + getId() +
            ", branchID='" + getBranchID() + "'" +
            ", typeID=" + getTypeID() +
            ", date='" + getDate() + "'" +
            ", postedDate='" + getPostedDate() + "'" +
            ", typeLedger=" + getTypeLedger() +
            ", noFBook='" + getNoFBook() + "'" +
            ", noMBook='" + getNoMBook() + "'" +
//            ", accountingObjectID='" + getAccountingObjectID() + "'" +
            ", accountingObjectName='" + getAccountingObjectName() + "'" +
            ", accountingObjectAddress='" + getAccountingObjectAddress() + "'" +
//            ", accountingObjectBankAccountDetailID='" + getBankAccountDetails() + "'" +
//            ", bankName='" + getBankName() + "'" +
            ", reason='" + getReason() + "'" +
//            ", currencyID='" + getCurrency() + "'" +
            ", exchangeRate=" + getExchangeRate() +
//            ", pPOrderID='" + getpPOrderID() + "'" +
            ", isImportPurchase='" + getIsImportPurchase() + "'" +
            ", paymentClauseID='" + getPaymentClauseID() + "'" +
            ", transportMethodID='" + getTransportMethodID() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", totalAmountOriginal=" + getTotalAmountOriginal() +
            ", totalVATAmount=" + getTotalVATAmount() +
            ", totalVATAmountOriginal=" + getTotalVATAmountOriginal() +
//            ", employeeID='" + getEmployee() + "'" +
            ", accountingObjectType=" + getAccountingObjectType() +
            ", templateID='" + getTemplateID() + "'" +
            ", recorded='" + isRecorded() + "'" +

            ", ismatch='" + isIsMatch() + "'" +
            "}";
    }
}
