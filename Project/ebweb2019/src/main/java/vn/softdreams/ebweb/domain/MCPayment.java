package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Strings;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.cashandbank.MCPaymentDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * A MCPayment.
 */
@Entity
@Table(name = "mcpayment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "MCPaymentDTO",
        classes = {
            @ConstructorResult(
                targetClass = MCPaymentDTO.class,
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
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "accountingObjectAddress", type = String.class),
                    @ColumnResult(name = "receiver", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "taxCode", type = String.class),
                    @ColumnResult(name = "totalamount", type = BigDecimal.class),
                    @ColumnResult(name = "recorded", type = Boolean.class),
                    @ColumnResult(name = "currencyID", type = String.class),
                    @ColumnResult(name = "totalAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "typeName", type = String.class),
                }
            )
        }
    )})
public class MCPayment implements Serializable {

//    private static final UUID serialVersionUID = 1L;

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

    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

    @Size(max = 512)
    @Column(name = "accountingobjectname", length = 512)
    private String accountingObjectName;

    @Size(max = 512)
    @Column(name = "accountingobjectaddress", length = 512)
    private String accountingObjectAddress;

    @Size(max = 512)
    @Column(name = "receiver", length = 512)
    private String receiver;

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

    @Column(name = "isimportpurchase")
    private Boolean isImportPurchase;

    @Column(name = "paymentclauseid")
    private UUID paymentClauseID;

    @Column(name = "transportmethodid")
    private UUID transportMethodID;

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

    @Column(name = "mcauditid")
    private UUID mCAuditID;

    @Column(name = "taxcode")
    private String taxCode;

    @Column(name = "recorded")
    private Boolean recorded;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "mcpaymentid")
    private Set<MCPaymentDetails> mCPaymentDetails = new HashSet<>();
    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "mcpaymentid")
    private Set<MCPaymentDetailInsurance> mCPaymentDetailInsurances = new HashSet<>();
    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "mcpaymentid")
    private Set<MCPaymentDetailSalary> mCPaymentDetailSalaries = new HashSet<>();
    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "mcpaymentid")
    private Set<MCPaymentDetailTax> mCPaymentDetailTaxes = new HashSet<>();
    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "mcpaymentid")
    private Set<MCPaymentDetailVendor> mCPaymentDetailVendors = new HashSet<>();
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

    public Boolean getStoredInRepository() {
        return storedInRepository;
    }

    public void setStoredInRepository(Boolean storedInRepository) {
        this.storedInRepository = storedInRepository;
    }

    public List<RefVoucherDTO> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucherDTO> viewVouchers) {
        this.viewVouchers = viewVouchers;
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

    public MCPayment companyID(UUID companyID) {
        this.companyID = companyID;
        return this;
    }



    public MCPayment() {
    }

    public MCPayment(UUID id, Boolean recorded, Integer typeId, String receiver, String numberAttach, String reason,
                     UUID acountingObjectID, String accountingObjectName, String accountingObjectAddress,
                     String companyTaxCode, UUID employeeID, Integer typeLedger, String noFBook, String noMBook,
                     LocalDate receiptDate, LocalDate postedDate, UUID companyId, String currencyID,
                     BigDecimal totalAmount, BigDecimal totalAmountOriginal, BigDecimal totalVATAmount,
                     BigDecimal totalVATAmountOriginal, BigDecimal exchangeRate) {
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
        if (receiver != null) {
            this.setReceiver(receiver);
        }
        if (numberAttach != null) {
            this.setNumberAttach(numberAttach);
        }
        if (reason != null) {
            this.setReason(reason);
        }
        if (acountingObjectID != null) {
            this.setAccountingObjectID(acountingObjectID);
        }
        if (accountingObjectName != null) {
            this.setAccountingObjectName(accountingObjectName);
        }
        if (accountingObjectAddress != null) {
            this.setAccountingObjectAddress(accountingObjectAddress);
        }
        if (companyTaxCode != null) {
            this.setTaxCode(companyTaxCode);
        }
        if (employeeID != null) {
            this.setEmployeeID(employeeID);
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

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public MCPayment branchID(UUID branchID) {
        this.branchID = branchID;
        return this;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public MCPayment typeID(Integer typeID) {
        this.typeID = typeID;
        return this;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public LocalDate getDate() {
        return date;
    }

    public MCPayment date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public MCPayment postedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
        return this;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public MCPayment typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public MCPayment noFBook(String noFBook) {
        this.noFBook = noFBook;
        return this;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public MCPayment noMBook(String noMBook) {
        this.noMBook = noMBook;
        return this;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public MCPayment accountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
        return this;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public MCPayment accountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
        return this;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getAccountingObjectAddress() {
        return accountingObjectAddress;
    }

    public MCPayment accountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
        return this;
    }

    public void setAccountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
    }

    public String getReceiver() {
        return receiver;
    }

    public MCPayment receiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReason() {
        return reason;
    }

    public MCPayment reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNumberAttach() {
        return numberAttach;
    }

    public MCPayment numberAttach(String numberAttach) {
        this.numberAttach = numberAttach;
        return this;
    }

    public void setNumberAttach(String numberAttach) {
        this.numberAttach = numberAttach;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public MCPayment currencyID(String currencyID) {
        this.currencyID = currencyID;
        return this;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public MCPayment exchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Boolean isIsImportPurchase() {
        return isImportPurchase;
    }

    public MCPayment isImportPurchase(Boolean isImportPurchase) {
        this.isImportPurchase = isImportPurchase;
        return this;
    }

    public void setIsImportPurchase(Boolean isImportPurchase) {
        this.isImportPurchase = isImportPurchase;
    }

    public UUID getPaymentClauseID() {
        return paymentClauseID;
    }

    public MCPayment paymentClauseID(UUID paymentClauseID) {
        this.paymentClauseID = paymentClauseID;
        return this;
    }

    public void setPaymentClauseID(UUID paymentClauseID) {
        this.paymentClauseID = paymentClauseID;
    }

    public UUID getTransportMethodID() {
        return transportMethodID;
    }

    public MCPayment transportMethodID(UUID transportMethodID) {
        this.transportMethodID = transportMethodID;
        return this;
    }

    public void setTransportMethodID(UUID transportMethodID) {
        this.transportMethodID = transportMethodID;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public MCPayment totalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public MCPayment totalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
        return this;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }

    public BigDecimal getTotalVATAmount() {
        return totalVATAmount;
    }

    public MCPayment totalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
        return this;
    }

    public void setTotalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
    }

    public BigDecimal getTotalVATAmountOriginal() {
        return totalVATAmountOriginal;
    }

    public MCPayment totalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
        return this;
    }

    public void setTotalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public MCPayment employeeID(UUID employeeID) {
        this.employeeID = employeeID;
        return this;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public Integer getAccountingObjectType() {
        return accountingObjectType;
    }

    public MCPayment accountingObjectType(Integer accountingObjectType) {
        this.accountingObjectType = accountingObjectType;
        return this;
    }

    public void setAccountingObjectType(Integer accountingObjectType) {
        this.accountingObjectType = accountingObjectType;
    }

    public UUID getTemplateID() {
        return templateID;
    }

    public MCPayment templateID(UUID templateID) {
        this.templateID = templateID;
        return this;
    }

    public void setTemplateID(UUID templateID) {
        this.templateID = templateID;
    }

    public Boolean isRecorded() {
        return recorded;
    }

    public MCPayment recorded(Boolean recorded) {
        this.recorded = recorded;
        return this;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public Set<MCPaymentDetails> getMCPaymentDetails() {
        return mCPaymentDetails;
    }

    public MCPayment mCPaymentDetails(Set<MCPaymentDetails> mCPaymentDetails) {
        this.mCPaymentDetails = mCPaymentDetails;
        return this;
    }

//    public MCPayment addMCPaymentDetails(MCPaymentDetails mCPaymentDetails) {
//        this.mCPaymentDetails.add(mCPaymentDetails);
//        mCPaymentDetails.setMCPayment(this);
//        return this;
//    }
//
//    public MCPayment removeMCPaymentDetails(MCPaymentDetails mCPaymentDetails) {
//        this.mCPaymentDetails.remove(mCPaymentDetails);
//        mCPaymentDetails.setMCPayment(null);
//        return this;
//    }

    public void setMCPaymentDetails(Set<MCPaymentDetails> mCPaymentDetails) {
        this.mCPaymentDetails = mCPaymentDetails;
    }

    public Set<MCPaymentDetailInsurance> getMCPaymentDetailInsurances() {
        return mCPaymentDetailInsurances;
    }

    public MCPayment mCPaymentDetailInsurances(Set<MCPaymentDetailInsurance> mCPaymentDetailInsurances) {
        this.mCPaymentDetailInsurances = mCPaymentDetailInsurances;
        return this;
    }

//    public MCPayment addMCPaymentDetailInsurance(MCPaymentDetailInsurance mCPaymentDetailInsurance) {
//        this.mCPaymentDetailInsurances.add(mCPaymentDetailInsurance);
//        mCPaymentDetailInsurance.setMCPayment(this);
//        return this;
//    }
//
//    public MCPayment removeMCPaymentDetailInsurance(MCPaymentDetailInsurance mCPaymentDetailInsurance) {
//        this.mCPaymentDetailInsurances.remove(mCPaymentDetailInsurance);
//        mCPaymentDetailInsurance.setMCPayment(null);
//        return this;
//    }

    public void setMCPaymentDetailInsurances(Set<MCPaymentDetailInsurance> mCPaymentDetailInsurances) {
        this.mCPaymentDetailInsurances = mCPaymentDetailInsurances;
    }

    public Set<MCPaymentDetailSalary> getMCPaymentDetailSalaries() {
        return mCPaymentDetailSalaries;
    }

    public MCPayment mCPaymentDetailSalaries(Set<MCPaymentDetailSalary> mCPaymentDetailSalaries) {
        this.mCPaymentDetailSalaries = mCPaymentDetailSalaries;
        return this;
    }

//    public MCPayment addMCPaymentDetailSalary(MCPaymentDetailSalary mCPaymentDetailSalary) {
//        this.mCPaymentDetailSalaries.add(mCPaymentDetailSalary);
//        mCPaymentDetailSalary.setMCPayment(this);
//        return this;
//    }
//
//    public MCPayment removeMCPaymentDetailSalary(MCPaymentDetailSalary mCPaymentDetailSalary) {
//        this.mCPaymentDetailSalaries.remove(mCPaymentDetailSalary);
//        mCPaymentDetailSalary.setMCPayment(null);
//        return this;
//    }

    public void setMCPaymentDetailSalaries(Set<MCPaymentDetailSalary> mCPaymentDetailSalaries) {
        this.mCPaymentDetailSalaries = mCPaymentDetailSalaries;
    }

    public Set<MCPaymentDetailTax> getMCPaymentDetailTaxes() {
        return mCPaymentDetailTaxes;
    }

    public MCPayment mCPaymentDetailTaxes(Set<MCPaymentDetailTax> mCPaymentDetailTaxes) {
        this.mCPaymentDetailTaxes = mCPaymentDetailTaxes;
        return this;
    }

//    public MCPayment addMCPaymentDetailTax(MCPaymentDetailTax mCPaymentDetailTax) {
//        this.mCPaymentDetailTaxes.add(mCPaymentDetailTax);
//        mCPaymentDetailTax.setMCPayment(this);
//        return this;
//    }
//
//    public MCPayment removeMCPaymentDetailTax(MCPaymentDetailTax mCPaymentDetailTax) {
//        this.mCPaymentDetailTaxes.remove(mCPaymentDetailTax);
//        mCPaymentDetailTax.setMCPayment(null);
//        return this;
//    }

    public void setMCPaymentDetailTaxes(Set<MCPaymentDetailTax> mCPaymentDetailTaxes) {
        this.mCPaymentDetailTaxes = mCPaymentDetailTaxes;
    }

    public Set<MCPaymentDetailVendor> getMCPaymentDetailVendors() {
        return mCPaymentDetailVendors;
    }

    public MCPayment mCPaymentDetailVendors(Set<MCPaymentDetailVendor> mCPaymentDetailVendors) {
        this.mCPaymentDetailVendors = mCPaymentDetailVendors;
        return this;
    }

//    public MCPayment addMCPaymentDetailVendor(MCPaymentDetailVendor mCPaymentDetailVendor) {
//        this.mCPaymentDetailVendors.add(mCPaymentDetailVendor);
//        mCPaymentDetailVendor.setMCPayment(this);
//        return this;
//    }
//
//    public MCPayment removeMCPaymentDetailVendor(MCPaymentDetailVendor mCPaymentDetailVendor) {
//        this.mCPaymentDetailVendors.remove(mCPaymentDetailVendor);
//        mCPaymentDetailVendor.setMCPayment(null);
//        return this;
//    }

    public void setMCPaymentDetailVendors(Set<MCPaymentDetailVendor> mCPaymentDetailVendors) {
        this.mCPaymentDetailVendors = mCPaymentDetailVendors;
    }

    public UUID getmCAuditID() {
        return mCAuditID;
    }

    public void setmCAuditID(UUID mCAuditID) {
        this.mCAuditID = mCAuditID;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
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
        MCPayment mCPayment = (MCPayment) o;
        if (mCPayment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mCPayment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MCPayment{" +
            "id=" + getId() +
            ", branchID=" + getBranchID() +
            ", typeID=" + getTypeID() +
            ", date='" + getDate() + "'" +
            ", postedDate='" + getPostedDate() + "'" +
            ", typeLedger=" + getTypeLedger() +
            ", noFBook='" + getNoFBook() + "'" +
            ", noMBook='" + getNoMBook() + "'" +
            ", accountingObjectID=" + getAccountingObjectID() +
            ", accountingObjectName='" + getAccountingObjectName() + "'" +
            ", accountingObjectAddress='" + getAccountingObjectAddress() + "'" +
            ", receiver='" + getReceiver() + "'" +
            ", reason='" + getReason() + "'" +
            ", numberAttach='" + getNumberAttach() + "'" +
            ", currencyID='" + getCurrencyID() + "'" +
            ", exchangeRate=" + getExchangeRate() +
            ", isImportPurchase='" + isIsImportPurchase() + "'" +
            /*", pPOrderID=" + getpPOrderID() +*/
            ", paymentClauseID=" + getPaymentClauseID() +
            ", transportMethodID=" + getTransportMethodID() +
            ", totalAmount=" + getTotalAmount() +
            ", totalAmountOriginal=" + getTotalAmountOriginal() +
            ", totalVATAmount=" + getTotalVATAmount() +
            ", totalVATAmountOriginal=" + getTotalVATAmountOriginal() +
            ", employeeID=" + getEmployeeID() +
            ", accountingObjectType=" + getAccountingObjectType() +
            ", templateID=" + getTemplateID() +
            ", recorded='" + isRecorded() + "'" +
            ", mCAuditID='" + getmCAuditID() + "'" +
            ", taxCode='" + getTaxCode() + "'" +
            "}";
    }
}
