package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.TIIncrementConvertDTO;
import vn.softdreams.ebweb.service.dto.TIIncrementDetailsConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.OrganizationUnitCustomDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A TIIncrementDetails.
 */
@Entity
@Table(name = "tiincrementdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings(value = {
    @SqlResultSetMapping(
        name = "TIIncrementDetailsConvertDTO",
        classes = {
            @ConstructorResult(
                targetClass = TIIncrementDetailsConvertDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "tiIncrementID", type = UUID.class),
                    @ColumnResult(name = "toolsID", type = UUID.class),
                    @ColumnResult(name = "toolCode", type = String.class),
                    @ColumnResult(name = "toolName", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "unitName", type = String.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
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
                    @ColumnResult(name = "orderPriority", type = Integer.class),
                }
            )
        })
})
public class TIIncrementDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tiincrementid")
    private UUID tiIncrementID;

    @Column(name = "toolsid")
    private UUID toolsID;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "unitid")
    private UUID unitID;

    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unitprice", precision = 10, scale = 2)
    private BigDecimal unitPrice;

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

    @Column(name = "tiauditid ")
    private UUID tiAuditID;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private List<OrganizationUnitCustomDTO> organizationUnits;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public List<OrganizationUnitCustomDTO> getOrganizationUnits() {
        return organizationUnits;
    }

    public void setOrganizationUnits(List<OrganizationUnitCustomDTO> organizationUnits) {
        this.organizationUnits = organizationUnits;
    }

    public UUID getTiAuditID() {
        return tiAuditID;
    }

    public void setTiAuditID(UUID tiAuditID) {
        this.tiAuditID = tiAuditID;
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

    public BigDecimal getQuantity() {
        return quantity;
    }

    public TIIncrementDetails quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public TIIncrementDetails unitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UUID getTiIncrementID() {
        return tiIncrementID;
    }

    public void setTiIncrementID(UUID tiIncrementID) {
        this.tiIncrementID = tiIncrementID;
    }

    public UUID getToolsID() {
        return toolsID;
    }

    public void setToolsID(UUID toolsID) {
        this.toolsID = toolsID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
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

    public TIIncrementDetails orderPriority(Integer orderPriority) {
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
        TIIncrementDetails tIIncrementDetails = (TIIncrementDetails) o;
        if (tIIncrementDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tIIncrementDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TIIncrementDetails{" +
            "id=" + getId() +
            ", tiIncrementID='" + getTiIncrementID() + "'" +
            ", toolsID='" + getToolsID() + "'" +
            ", description='" + getDescription() + "'" +
            ", unitID='" + getUnitID() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", amount=" + getAmount() +
            ", accountingObjectID='" + getAccountingObjectID() + "'" +
            ", budgetItemID='" + getBudgetItemID() + "'" +
            ", costSetID='" + getCostSetID() + "'" +
            ", contractID='" + getContractID() + "'" +
            ", statisticCodeID='" + getStatisticCodeID() + "'" +
            ", departmentID='" + getDepartmentID() + "'" +
            ", expenseItemID='" + getExpenseItemID() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
