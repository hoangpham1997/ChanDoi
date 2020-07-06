package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.FAIncrementDetailsConvertDTO;
import vn.softdreams.ebweb.service.dto.TIIncrementDetailsConvertDTO;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A FAIncrementDetails.
 */
@Entity
@Table(name = "faincrementdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings(value = {
    @SqlResultSetMapping(
        name = "faIncrementDetailsConvertDTO",
        classes = {
            @ConstructorResult(
                targetClass = FAIncrementDetailsConvertDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "faIncrementID", type = UUID.class),
                    @ColumnResult(name = "fixedAssetID", type = UUID.class),
                    @ColumnResult(name = "fixedAssetCode", type = String.class),
                    @ColumnResult(name = "fixedAssetName", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "accountingObjectCode", type = String.class),
                    @ColumnResult(name = "budgetItemID", type = String.class),
                    @ColumnResult(name = "budgetItemCode", type = String.class),
                    @ColumnResult(name = "costSetID", type = String.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "statisticCodeID", type = String.class),
                    @ColumnResult(name = "statisticsCode", type = String.class),
                    @ColumnResult(name = "departmentID", type = String.class),
                    @ColumnResult(name = "organizationUnitCode", type = String.class),
                    @ColumnResult(name = "expenseItemID", type = String.class),
                    @ColumnResult(name = "expenseItemCode", type = String.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class)
                }
            )
        })
})
public class FAIncrementDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "faincrementid")
    private UUID faIncrementID;

    @Column(name = "fixedassetid")
    private UUID fixedAssetID;

    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

    @Column(name = "budgetitemid")
    private UUID budgetItemID;

    @Column(name = "costsetid")
    private UUID costSetID;

    @Column(name = "contractid")
    private UUID contractID;

    @Column(name = "statisticcodeid")
    private UUID statisticCodeID;

    @Column(name = "departmentid")
    private UUID departmentID;

    @Column(name = "expenseitemid")
    private UUID expenseItemID;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UUID getFaIncrementID() {
        return faIncrementID;
    }

    public void setFaIncrementID(UUID faIncrementID) {
        this.faIncrementID = faIncrementID;
    }

    public UUID getFixedAssetID() {
        return fixedAssetID;
    }

    public void setFixedAssetID(UUID fixedAssetID) {
        this.fixedAssetID = fixedAssetID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public UUID getBudgetItemID() {
        return budgetItemID;
    }

    public void setBudgetItemID(UUID budgetItemID) {
        this.budgetItemID = budgetItemID;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public UUID getContractID() {
        return contractID;
    }

    public void setContractID(UUID contractID) {
        this.contractID = contractID;
    }

    public UUID getStatisticCodeID() {
        return statisticCodeID;
    }

    public void setStatisticCodeID(UUID statisticCodeID) {
        this.statisticCodeID = statisticCodeID;
    }

    public UUID getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(UUID departmentID) {
        this.departmentID = departmentID;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public FAIncrementDetails orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
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
        FAIncrementDetails tIIncrementDetails = (FAIncrementDetails) o;
        if (tIIncrementDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tIIncrementDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
