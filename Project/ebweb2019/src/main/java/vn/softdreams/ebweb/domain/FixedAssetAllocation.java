package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A FixedAssetAllocation.
 */
@Entity
@Table(name = "fixedassetallocation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FixedAssetAllocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "fixedassetid")
    private UUID fixedassetID;


    @Column(name = "objecttype")
    private Integer objectType;

    @Column(name = "rate", precision = 10, scale = 2)
    private BigDecimal rate;

//    @NotNull
    @Column(name = "orderpriority")
    private Integer orderPriority;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "objectid")
    private CostSet objectID;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "expenseitemid")
    private ExpenseItem expenseItem;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "costaccount")
    private AccountList costAccount;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "statisticscodeid")
    private StatisticsCode statisticsCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setStatisticsCode(StatisticsCode statisticsCode) {
        this.statisticsCode = statisticsCode;
    }

    public void setExpenseItem(ExpenseItem expenseItem) {
        this.expenseItem = expenseItem;
    }

    public void setObjectID(CostSet objectID) {
        this.objectID = objectID;
    }

    public ExpenseItem getExpenseItem() {
        return expenseItem;
    }

    public StatisticsCode getStatisticsCode() {
        return statisticsCode;
    }

    public CostSet getObjectID() {
        return objectID;
    }

    public Integer getObjectType() {
        return objectType;
    }

    public FixedAssetAllocation objectType(Integer objectType) {
        this.objectType = objectType;
        return this;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public FixedAssetAllocation rate(BigDecimal rate) {
        this.rate = rate;
        return this;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public FixedAssetAllocation orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public UUID getFixedassetID() {
        return fixedassetID;
    }

    public void setFixedassetID(UUID fixedassetID) {
        this.fixedassetID = fixedassetID;
    }

    public ExpenseItem getExpenseitem() {
        return expenseItem;
    }

    public FixedAssetAllocation expenseitem(ExpenseItem expenseItem) {
        this.expenseItem = expenseItem;
        return this;
    }

    public void setExpenseitem(ExpenseItem expenseItem) {
        this.expenseItem = expenseItem;
    }

    public AccountList getCostAccount() {
        return costAccount;
    }

    public FixedAssetAllocation costAccount(AccountList accountList) {
        this.costAccount = accountList;
        return this;
    }

    public void setCostAccount(AccountList accountList) {
        this.costAccount = accountList;
    }

    public StatisticsCode getStatisticscode() {
        return statisticsCode;
    }

    public FixedAssetAllocation statisticscode(StatisticsCode statisticsCode) {
        this.statisticsCode = statisticsCode;
        return this;
    }

    public void setStatisticscode(StatisticsCode statisticsCode) {
        this.statisticsCode = statisticsCode;
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
        FixedAssetAllocation fixedAssetAllocation = (FixedAssetAllocation) o;
        if (fixedAssetAllocation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fixedAssetAllocation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FixedAssetAllocation{" +
            "id=" + getId() +
            ", objectID=" + getObjectID() +
            ", objectType=" + getObjectType() +
            ", rate=" + getRate() +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
