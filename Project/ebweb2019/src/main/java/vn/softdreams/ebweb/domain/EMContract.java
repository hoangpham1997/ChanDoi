package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A EMContract.
 */
@Entity
@Table(name = "emcontract")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EMContract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private String companyID;

    @Column(name = "branchid")
    private String branchID;

    @NotNull
    @Column(name = "typeid", nullable = false)
    private Integer typeID;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @NotNull
    @Size(max = 50)
    @Column(name = "nofbook", length = 50, nullable = false)
    private String noFBook;

    @NotNull
    @Size(max = 50)
    @Column(name = "nombook", length = 50, nullable = false)
    private String noMBook;

    @Size(max = 512)
    @Column(name = "name", length = 512)
    private String name;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "signeddate")
    private LocalDate signedDate;

    @Size(max = 3)
    @Column(name = "currencyid", length = 3)
    private String currencyID;

    @Column(name = "exchangerate", precision = 10, scale = 2)
    private BigDecimal exchangeRate;

    @NotNull
    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "amountoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal amountOriginal;

    @Column(name = "accountingobjectid")
    private String accountingObjectID;

    @Size(max = 512)
    @Column(name = "signname", length = 512)
    private String signName;

    @Column(name = "starteddate")
    private LocalDate startedDate;

    @Column(name = "closeddate")
    private LocalDate closedDate;

    @Column(name = "contractstate")
    private Integer contractState;

    @NotNull
    @Column(name = "iswatchforcostprice", nullable = false)
    private Boolean isWatchForCostPrice;

    @NotNull
    @Column(name = "billreceived", nullable = false)
    private Boolean billReceived;

    @NotNull
    @Column(name = "isactive", nullable = false)
    private Boolean isActive;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCompanyID() {
        return companyID;
    }

    public EMContract companyID(String companyID) {
        this.companyID = companyID;
        return this;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getBranchID() {
        return branchID;
    }

    public EMContract branchID(String branchID) {
        this.branchID = branchID;
        return this;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public EMContract typeID(Integer typeID) {
        this.typeID = typeID;
        return this;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public EMContract typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public EMContract noFBook(String noFBook) {
        this.noFBook = noFBook;
        return this;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public EMContract noMBook(String noMBook) {
        this.noMBook = noMBook;
        return this;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getName() {
        return name;
    }

    public EMContract name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public EMContract description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getSignedDate() {
        return signedDate;
    }

    public EMContract signedDate(LocalDate signedDate) {
        this.signedDate = signedDate;
        return this;
    }

    public void setSignedDate(LocalDate signedDate) {
        this.signedDate = signedDate;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public EMContract currencyID(String currencyID) {
        this.currencyID = currencyID;
        return this;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public EMContract exchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public EMContract amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public EMContract amountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
        return this;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public String getAccountingObjectID() {
        return accountingObjectID;
    }

    public EMContract accountingObjectID(String accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
        return this;
    }

    public void setAccountingObjectID(String accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public String getSignName() {
        return signName;
    }

    public EMContract signName(String signName) {
        this.signName = signName;
        return this;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public LocalDate getStartedDate() {
        return startedDate;
    }

    public EMContract startedDate(LocalDate startedDate) {
        this.startedDate = startedDate;
        return this;
    }

    public void setStartedDate(LocalDate startedDate) {
        this.startedDate = startedDate;
    }

    public LocalDate getClosedDate() {
        return closedDate;
    }

    public EMContract closedDate(LocalDate closedDate) {
        this.closedDate = closedDate;
        return this;
    }

    public void setClosedDate(LocalDate closedDate) {
        this.closedDate = closedDate;
    }

    public Integer getContractState() {
        return contractState;
    }

    public EMContract contractState(Integer contractState) {
        this.contractState = contractState;
        return this;
    }

    public void setContractState(Integer contractState) {
        this.contractState = contractState;
    }

    public Boolean isIsWatchForCostPrice() {
        return isWatchForCostPrice;
    }

    public EMContract isWatchForCostPrice(Boolean isWatchForCostPrice) {
        this.isWatchForCostPrice = isWatchForCostPrice;
        return this;
    }

    public void setIsWatchForCostPrice(Boolean isWatchForCostPrice) {
        this.isWatchForCostPrice = isWatchForCostPrice;
    }

    public Boolean isBillReceived() {
        return billReceived;
    }

    public EMContract billReceived(Boolean billReceived) {
        this.billReceived = billReceived;
        return this;
    }

    public void setBillReceived(Boolean billReceived) {
        this.billReceived = billReceived;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public EMContract isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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
        EMContract eMContract = (EMContract) o;
        if (eMContract.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), eMContract.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EMContract{" +
            "id=" + getId() +
            ", companyID='" + getCompanyID() + "'" +
            ", branchID='" + getBranchID() + "'" +
            ", typeID=" + getTypeID() +
            ", typeLedger=" + getTypeLedger() +
            ", noFBook='" + getNoFBook() + "'" +
            ", noMBook='" + getNoMBook() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", signedDate='" + getSignedDate() + "'" +
            ", currencyID='" + getCurrencyID() + "'" +
            ", exchangeRate=" + getExchangeRate() +
            ", amount=" + getAmount() +
            ", amountOriginal=" + getAmountOriginal() +
            ", accountingObjectID='" + getAccountingObjectID() + "'" +
            ", signName='" + getSignName() + "'" +
            ", startedDate='" + getStartedDate() + "'" +
            ", closedDate='" + getClosedDate() + "'" +
            ", contractState=" + getContractState() +
            ", isWatchForCostPrice='" + isIsWatchForCostPrice() + "'" +
            ", billReceived='" + isBillReceived() + "'" +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
