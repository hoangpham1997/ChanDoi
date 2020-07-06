package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.PPPayVendorDTO;
import vn.softdreams.ebweb.web.rest.dto.CPPeriodDTO;

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
 * A CPPeriod.
 */
@Entity
@Table(name = "cpperiod")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "CPPeriodDTO",
        classes = {
            @ConstructorResult(
                targetClass = CPPeriodDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "fromDate", type = LocalDate.class),
                    @ColumnResult(name = "toDate", type = LocalDate.class),
                    @ColumnResult(name = "name", type = String.class),
                    @ColumnResult(name = "totalIncompleteOpenning", type = BigDecimal.class),
                    @ColumnResult(name = "totalAmountInPeriod", type = BigDecimal.class),
                    @ColumnResult(name = "totalIncompleteClosing", type = BigDecimal.class),
                    @ColumnResult(name = "totalCost", type = BigDecimal.class),
                    @ColumnResult(name = "type", type = Integer.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "CPPeriodDTO1",
        classes = {
            @ConstructorResult(
                targetClass = CPPeriodDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "fromDate", type = LocalDate.class),
                    @ColumnResult(name = "toDate", type = LocalDate.class),
                    @ColumnResult(name = "name", type = String.class),
                    @ColumnResult(name = "type", type = Integer.class)
                }
            )
        }
    )
})
public class CPPeriod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Column(name = "type")
    private Integer type;

    @Column(name = "fromdate")
    private LocalDate fromDate;

    @Column(name = "todate")
    private LocalDate toDate;

    @Size(max = 512)
    @Column(name = "name", length = 512)
    private String name;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "cpperiodid")
    private Set<CPPeriodDetails> cPPeriodDetails = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "cpperiodid")
    private Set<CPExpenseList> cPExpenseList = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "cpperiodid")
    private Set<CPAllocationGeneralExpense> cPAllocationGeneralExpenses = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "cpperiodid")
    private Set<CPAllocationGeneralExpenseDetails> cPAllocationGeneralExpenseDetails = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "cpperiodid")
    private Set<CPUncomplete> cPUncompletes = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "cpperiodid")
    private Set<CPUncompleteDetails> cPUncompleteDetails = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "cpperiodid")
    private Set<CPResult> cPResults = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "cpperiodid")
    private Set<CPAllocationRate> cPAllocationRates = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "cpperiodid")
    private Set<CPAcceptance> cPAcceptances = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "cpperiodid")
    private Set<CPAcceptanceDetails> cPAcceptanceDetails = new HashSet<>();

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String debitAccount;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String creditAccount;

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

    public CPPeriod companyID(UUID companyID) {
        this.companyID = companyID;
        return this;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public CPPeriod branchID(UUID branchID) {
        this.branchID = branchID;
        return this;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public CPPeriod typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public Integer getType() {
        return type;
    }

    public CPPeriod type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public CPPeriod fromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public CPPeriod toDate(LocalDate toDate) {
        this.toDate = toDate;
        return this;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getName() {
        return name;
    }

    public CPPeriod name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CPAllocationGeneralExpense> getcPAllocationGeneralExpenses() {
        return cPAllocationGeneralExpenses;
    }

    public void setcPAllocationGeneralExpenses(Set<CPAllocationGeneralExpense> cPAllocationGeneralExpenses) {
        this.cPAllocationGeneralExpenses = cPAllocationGeneralExpenses;
    }

    public Set<CPAllocationGeneralExpenseDetails> getcPAllocationGeneralExpenseDetails() {
        return cPAllocationGeneralExpenseDetails;
    }

    public void setcPAllocationGeneralExpenseDetails(Set<CPAllocationGeneralExpenseDetails> cPAllocationGeneralExpenseDetails) {
        this.cPAllocationGeneralExpenseDetails = cPAllocationGeneralExpenseDetails;
    }

    public Set<CPUncomplete> getcPUncompletes() {
        return cPUncompletes;
    }

    public void setcPUncompletes(Set<CPUncomplete> cPUncompletes) {
        this.cPUncompletes = cPUncompletes;
    }

    public Set<CPUncompleteDetails> getcPUncompleteDetails() {
        return cPUncompleteDetails;
    }

    public void setcPUncompleteDetails(Set<CPUncompleteDetails> cPUncompleteDetails) {
        this.cPUncompleteDetails = cPUncompleteDetails;
    }

    public Set<CPResult> getcPResults() {
        return cPResults;
    }

    public void setcPResults(Set<CPResult> cPResults) {
        this.cPResults = cPResults;
    }

    public Set<CPPeriodDetails> getcPPeriodDetails() {
        return cPPeriodDetails;
    }

    public void setcPPeriodDetails(Set<CPPeriodDetails> cPPeriodDetails) {
        this.cPPeriodDetails = cPPeriodDetails;
    }

    public Set<CPExpenseList> getcPExpenseList() {
        return cPExpenseList;
    }

    public void setcPExpenseList(Set<CPExpenseList> cPExpenseList) {
        this.cPExpenseList = cPExpenseList;
    }

    public Set<CPAllocationRate> getcPAllocationRates() {
        return cPAllocationRates;
    }

    public void setcPAllocationRates(Set<CPAllocationRate> cPAllocationRates) {
        this.cPAllocationRates = cPAllocationRates;
    }

    public Set<CPAcceptance> getcPAcceptances() {
        return cPAcceptances;
    }

    public void setcPAcceptances(Set<CPAcceptance> cPAcceptances) {
        this.cPAcceptances = cPAcceptances;
    }

    public Set<CPAcceptanceDetails> getcPAcceptanceDetails() {
        return cPAcceptanceDetails;
    }

    public void setcPAcceptanceDetails(Set<CPAcceptanceDetails> cPAcceptanceDetails) {
        this.cPAcceptanceDetails = cPAcceptanceDetails;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
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
        CPPeriod cPPeriod = (CPPeriod) o;
        if (cPPeriod.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cPPeriod.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CPPeriod{" +
            "id=" + getId() +
            ", companyID=" + getCompanyID() +
            ", branchID=" + getBranchID() +
            ", typeLedger=" + getTypeLedger() +
            ", type=" + getType() +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
