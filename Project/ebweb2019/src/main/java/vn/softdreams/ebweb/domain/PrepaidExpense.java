package vn.softdreams.ebweb.domain;

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
 * A PrepaidExpense.
 */
@Entity
@Table(name = "prepaidexpense")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "PrepaidExpenseCodeDTO",
        classes = {
            @ConstructorResult(
                targetClass = PrepaidExpenseCodeDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "species", type = String.class),
                    @ColumnResult(name = "code", type = String.class),
                    @ColumnResult(name = "name", type = String.class),
                    @ColumnResult(name = "type", type = Integer.class),
                })
        }),
        @SqlResultSetMapping(
            name = "PrepaidExpenseConvertDTO",
            classes = {
                @ConstructorResult(
                    targetClass = PrepaidExpenseConvertDTO.class,
                    columns = {
                        @ColumnResult(name = "PrepaidExpenseID", type = UUID.class),
                        @ColumnResult(name = "companyID", type = UUID.class),
                        @ColumnResult(name = "branchID", type = UUID.class),
                        @ColumnResult(name = "typeLedger", type = Integer.class),
                        @ColumnResult(name = "typeExpense", type = Integer.class),
                        @ColumnResult(name = "prepaidExpenseCode", type = String.class),
                        @ColumnResult(name = "prepaidExpenseName", type = String.class),
                        @ColumnResult(name = "date", type = LocalDate.class),
                        @ColumnResult(name = "amount", type = BigDecimal.class),
                        @ColumnResult(name = "allocationTime", type = Integer.class),
                        @ColumnResult(name = "allocatedPeriod", type = Integer.class),
                        @ColumnResult(name = "allocatedAmount", type = BigDecimal.class),
                        @ColumnResult(name = "allocationAccount", type = String.class),
                        @ColumnResult(name = "isActive", type = Boolean.class),
                        @ColumnResult(name = "remainingAmount", type = BigDecimal.class),
                        @ColumnResult(name = "allocationAmount", type = BigDecimal.class),
                    })
            }
        ),
        @SqlResultSetMapping(
            name = "PrepaidExpenseAllocationConvertDTO",
            classes = {
                @ConstructorResult(
                    targetClass = PrepaidExpenseAllocationConvertDTO.class,
                    columns = {
                        @ColumnResult(name = "branchID", type = UUID.class),
                        @ColumnResult(name = "typeLedger", type = Integer.class),
                        @ColumnResult(name = "typeExpense", type = Integer.class),
                        @ColumnResult(name = "prepaidExpenseID", type = UUID.class),
                        @ColumnResult(name = "prepaidExpenseCode", type = String.class),
                        @ColumnResult(name = "prepaidExpenseName", type = String.class),
                        @ColumnResult(name = "date", type = LocalDate.class),
                        @ColumnResult(name = "amount", type = BigDecimal.class),
                        @ColumnResult(name = "allocationAmount", type = BigDecimal.class),
                        @ColumnResult(name = "allocationTime", type = Integer.class),
                        @ColumnResult(name = "allocatedPeriod", type = Integer.class),
                        @ColumnResult(name = "allocatedAmount", type = BigDecimal.class),
                        @ColumnResult(name = "allocationAccount", type = String.class),
                        @ColumnResult(name = "isActive", type = Boolean.class),
                        @ColumnResult(name = "allocationObjectID", type = UUID.class),
                        @ColumnResult(name = "allocationObjectType", type = Integer.class),
                        @ColumnResult(name = "allocationObjectName", type = String.class),
                        @ColumnResult(name = "allocationRate", type = BigDecimal.class),
                        @ColumnResult(name = "costAccount", type = String.class),
                        @ColumnResult(name = "expenseItemID", type = UUID.class),
                        @ColumnResult(name = "remainingAmount", type = BigDecimal.class),
                        @ColumnResult(name = "allocationAmountGo", type = BigDecimal.class),
                    })
            }
        ),
        @SqlResultSetMapping(
            name = "PrepaidExpenseAllDTO",
            classes = {
                @ConstructorResult(
                    targetClass = PrepaidExpenseAllDTO.class,
                    columns = {
                        @ColumnResult(name = "ID", type = UUID.class),
                        @ColumnResult(name = "companyID", type = UUID.class),
                        @ColumnResult(name = "branchID", type = UUID.class),
                        @ColumnResult(name = "typeLedger", type = Integer.class),
                        @ColumnResult(name = "typeExpense", type = Integer.class),
                        @ColumnResult(name = "prepaidExpenseCode", type = String.class),
                        @ColumnResult(name = "prepaidExpenseName", type = String.class),
                        @ColumnResult(name = "date", type = LocalDate.class),
                        @ColumnResult(name = "amount", type = BigDecimal.class),
                        @ColumnResult(name = "allocationAmount", type = BigDecimal.class),
                        @ColumnResult(name = "allocationTime", type = Integer.class),
                        @ColumnResult(name = "allocatedPeriod", type = Integer.class),
                        @ColumnResult(name = "allocatedAmount", type = BigDecimal.class),
                        @ColumnResult(name = "allocationAccount", type = String.class),
                        @ColumnResult(name = "isActive", type = Boolean.class),
                        @ColumnResult(name = "isAllocation", type = Boolean.class),
                    })
            }
        )
})
public class PrepaidExpense implements Serializable {

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

    @Column(name = "typeexpense")
    private Integer typeExpense;

    @NotNull
    @Size(max = 25)
    @Column(name = "prepaidexpensecode", length = 25,  nullable = false)
    private String prepaidExpenseCode;

    @Size(max = 512)
    @Column(name = "prepaidexpensename", length = 512)
    private String prepaidExpenseName;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

//    @NotNull
    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

//    @NotNull
    @Column(name = "allocationamount", precision = 10, scale = 2)
    private BigDecimal allocationAmount;

//    @NotNull
    @Column(name = "allocationtime")
    private Integer allocationTime;

//    @NotNull
    @Column(name = "allocatedperiod")
    private Integer allocatedPeriod;

//    @NotNull
    @Column(name = "allocatedamount", precision = 10, scale = 2)
    private BigDecimal allocatedAmount;

    @Size(max = 25)
    @Column(name = "allocationaccount", length = 25)
    private String allocationAccount;

    @Column(name = "isactive")
    private Boolean isActive;

    @OneToMany(cascade={CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "prepaidexpenseid")
    private Set<PrepaidExpenseAllocation> prepaidExpenseAllocation = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "prepaidexpenseid")
    private Set<PrepaidExpenseVoucher> prepaidExpenseVouchers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public Set<PrepaidExpenseVoucher> getPrepaidExpenseVouchers() {
        return prepaidExpenseVouchers;
    }

    public void setPrepaidExpenseVouchers(Set<PrepaidExpenseVoucher> prepaidExpenseVouchers) {
        if (this.prepaidExpenseVouchers == null) {
            this.prepaidExpenseVouchers = prepaidExpenseVouchers;
        } else if (this.prepaidExpenseVouchers != prepaidExpenseVouchers) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
            this.prepaidExpenseVouchers.clear();
            if (prepaidExpenseVouchers != null) {
                this.prepaidExpenseVouchers.addAll(prepaidExpenseVouchers);
            }
        }
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

    public Boolean getIsActive() {
        return isActive;
    }

//    public void setISActive(Boolean isActive) {
//        isActive = isActive;
//    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public PrepaidExpense typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public Integer getTypeExpense() {
        return typeExpense;
    }

    public PrepaidExpense typeExpense(Integer typeExpense) {
        this.typeExpense = typeExpense;
        return this;
    }

    public void setTypeExpense(Integer typeExpense) {
        this.typeExpense = typeExpense;
    }

    public String getPrepaidExpenseCode() {
        return prepaidExpenseCode;
    }

    public PrepaidExpense prepaidExpenseCode(String prepaidExpenseCode) {
        this.prepaidExpenseCode = prepaidExpenseCode;
        return this;
    }

    public void setPrepaidExpenseCode(String prepaidExpenseCode) {
        this.prepaidExpenseCode = prepaidExpenseCode;
    }

    public String getPrepaidExpenseName() {
        return prepaidExpenseName;
    }

    public PrepaidExpense prepaidExpenseName(String prepaidExpenseName) {
        this.prepaidExpenseName = prepaidExpenseName;
        return this;
    }

    public void setPrepaidExpenseName(String prepaidExpenseName) {
        this.prepaidExpenseName = prepaidExpenseName;
    }

    public LocalDate getDate() {
        return date;
    }

    public PrepaidExpense date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PrepaidExpense amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAllocationAmount() {
        return allocationAmount;
    }

    public PrepaidExpense allocationAmount(BigDecimal allocationAmount) {
        this.allocationAmount = allocationAmount;
        return this;
    }

    public void setAllocationAmount(BigDecimal allocationAmount) {
        this.allocationAmount = allocationAmount;
    }

    public Integer getAllocationTime() {
        return allocationTime;
    }

    public PrepaidExpense allocationTime(Integer allocationTime) {
        this.allocationTime = allocationTime;
        return this;
    }

    public void setAllocationTime(Integer allocationTime) {
        this.allocationTime = allocationTime;
    }

    public Integer getAllocatedPeriod() {
        return allocatedPeriod;
    }

    public PrepaidExpense allocatedPeriod(Integer allocatedPeriod) {
        this.allocatedPeriod = allocatedPeriod;
        return this;
    }

    public void setAllocatedPeriod(Integer allocatedPeriod) {
        this.allocatedPeriod = allocatedPeriod;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public PrepaidExpense allocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
        return this;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public String getAllocationAccount() {
        return allocationAccount;
    }

    public PrepaidExpense allocationAccount(String allocationAccount) {
        this.allocationAccount = allocationAccount;
        return this;
    }

    public void setAllocationAccount(String allocationAccount) {
        this.allocationAccount = allocationAccount;
    }

//    public Boolean isIsActive() {
//        return isActive;
//    }
//
//    public PrepaidExpense isActive(Boolean isActive) {
//        this.isActive = isActive;
//        return this;
//    }

    public Set<PrepaidExpenseAllocation> getPrepaidExpenseAllocation() {
        return prepaidExpenseAllocation;
    }

    public void setPrepaidExpenseAllocation(Set<PrepaidExpenseAllocation> prepaidExpenseAllocation) {
        if (this.prepaidExpenseAllocation == null) {
            this.prepaidExpenseAllocation = prepaidExpenseAllocation;
        } else if (this.prepaidExpenseAllocation != prepaidExpenseAllocation) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
            this.prepaidExpenseAllocation.clear();
            if (prepaidExpenseAllocation != null) {
                this.prepaidExpenseAllocation.addAll(prepaidExpenseAllocation);
            }
        }
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
        PrepaidExpense prepaidExpense = (PrepaidExpense) o;
        if (prepaidExpense.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), prepaidExpense.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PrepaidExpense{" +
            "id=" + getId() +
            ", companyID='" + getCompanyID() + "'" +
            ", branchID='" + getBranchID() + "'" +
            ", typeLedger=" + getTypeLedger() +
            ", typeExpense='" + getTypeExpense() + "'" +
            ", prepaidExpenseCode='" + getPrepaidExpenseCode() + "'" +
            ", prepaidExpenseName='" + getPrepaidExpenseName() + "'" +
            ", date='" + getDate() + "'" +
            ", amount=" + getAmount() +
            ", allocationAmount=" + getAllocationAmount() +
            ", allocationTime=" + getAllocationTime() +
            ", allocatedPeriod=" + getAllocatedPeriod() +
            ", allocatedAmount=" + getAllocatedAmount() +
            ", allocationAccount='" + getAllocationAccount() + "'" +
            "}";
    }
}
