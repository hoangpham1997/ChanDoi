package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.Report.TITransferDTO;
import vn.softdreams.ebweb.web.rest.dto.OrganizationUnitCustomDTO;
import vn.softdreams.ebweb.web.rest.dto.TITransferDetailConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.TITransferDetailsAllConvertDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A TITransferDetails.
 */
@Entity
@Table(name = "titransferdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings( value = {
    @SqlResultSetMapping(
        name = "TITransferDTOList",
        classes = {
            @ConstructorResult(
                targetClass = TITransferDTO.class,
                columns = {
                    @ColumnResult(name = "toolCode", type = String.class),
                    @ColumnResult(name = "toolName", type = String.class),
                    @ColumnResult(name = "toDepartmentID", type = String.class),
                    @ColumnResult(name = "fromDepartmentID", type = String.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "transferQuantity", type = BigDecimal.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "TITransferDetailConvertDTO",
        classes = {
            @ConstructorResult(
                targetClass = TITransferDetailConvertDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "tiTransferID", type = UUID.class),
                    @ColumnResult(name = "toolsID", type = UUID.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "fromDepartmentID", type = UUID.class),
                    @ColumnResult(name = "toDepartmentID", type = UUID.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "transferQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "costAccount", type = String.class),
                    @ColumnResult(name = "budgetItemID", type = UUID.class),
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "statisticCodeID", type = UUID.class),
                    @ColumnResult(name = "expenseItemID", type = UUID.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
                    @ColumnResult(name = "toolsCode", type = String.class),
                    @ColumnResult(name = "toolsName", type = String.class),
                    @ColumnResult(name = "fromDepartmentCode", type = String.class),
                    @ColumnResult(name = "toDepartmentCode", type = String.class),
                    @ColumnResult(name = "budgetItemCode", type = String.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "statisticsCode", type = String.class),
                    @ColumnResult(name = "expenseItemCode", type = String.class),
                }
            )
        }
    )
})
public class TITransferDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "titransferid")
    private UUID tiTransferID;

    @Column(name = "toolsid")
    private UUID toolsID;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "fromdepartmentid")
    private UUID fromDepartmentID;

    @Column(name = "todepartmentid")
    private UUID toDepartmentID;

    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "transferquantity", precision = 10, scale = 2)
    private BigDecimal transferQuantity;

    @Size(max = 25)
    @Column(name = "costaccount", length = 25)
    private String costAccount;

    @Column(name = "budgetitemid", length = 25)
    private UUID budgetItemID;

    @Column(name = "costsetid", length = 25)
    private UUID costSetID;

    @Column(name = "statisticcodeid", length = 25)
    private UUID statisticCodeID;

    @Column(name = "expenseitemid", length = 25)
    private UUID expenseItemID;

    @Column(name = "orderpriority")
    private Integer orderPriority;


    @Transient
    @JsonDeserialize
    @JsonSerialize
    private List<OrganizationUnitCustomDTO> organizationUnitsFrom;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public List<OrganizationUnitCustomDTO> getOrganizationUnitsFrom() {
        return organizationUnitsFrom;
    }

    public void setOrganizationUnitsFrom(List<OrganizationUnitCustomDTO> organizationUnitsFrom) {
        this.organizationUnitsFrom = organizationUnitsFrom;
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

    public UUID getStatisticCodeID() {
        return statisticCodeID;
    }

    public void setStatisticCodeID(UUID statisticCodeID) {
        this.statisticCodeID = statisticCodeID;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public String getDescription() {
        return description;
    }

    public TITransferDetails description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getFromDepartmentID() {
        return fromDepartmentID;
    }

    public void setFromDepartmentID(UUID fromDepartmentID) {
        this.fromDepartmentID = fromDepartmentID;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public TITransferDetails quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTransferQuantity() {
        return transferQuantity;
    }

    public TITransferDetails transferQuantity(BigDecimal transferQuantity) {
        this.transferQuantity = transferQuantity;
        return this;
    }

    public void setTransferQuantity(BigDecimal transferQuantity) {
        this.transferQuantity = transferQuantity;
    }

    public String getCostAccount() {
        return costAccount;
    }

    public TITransferDetails costAccount(String costAccount) {
        this.costAccount = costAccount;
        return this;
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

    public UUID getTiTransferID() {
        return tiTransferID;
    }

    public void setTiTransferID(UUID tiTransferID) {
        this.tiTransferID = tiTransferID;
    }

    public UUID getToolsID() {
        return toolsID;
    }

    public void setToolsID(UUID toolsID) {
        this.toolsID = toolsID;
    }

    public UUID getToDepartmentID() {
        return toDepartmentID;
    }

    public void setToDepartmentID(UUID toDepartmentID) {
        this.toDepartmentID = toDepartmentID;
    }

    public void setCostAccount(String costAccount) {
        this.costAccount = costAccount;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public TITransferDetails orderPriority(Integer orderPriority) {
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
        TITransferDetails tITransferDetails = (TITransferDetails) o;
        if (tITransferDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tITransferDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TITransferDetails{" +
            "id=" + getId() +
            ", tiTransferID='" + getTiTransferID() + "'" +
            ", toolsID='" + getToolsID() + "'" +
            ", description='" + getDescription() + "'" +
            ", fromDepartmentID='" + getFromDepartmentID() + "'" +
            ", toDepartmentID='" + getToDepartmentID() + "'" +
            ", quantity=" + getQuantity() +
            ", transferQuantity=" + getTransferQuantity() +
            ", costAccount='" + getCostAccount() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
