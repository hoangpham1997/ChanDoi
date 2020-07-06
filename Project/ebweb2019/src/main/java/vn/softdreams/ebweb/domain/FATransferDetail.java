package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.FAIncrementDetailsConvertDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A FATransferDetail.
 */
@Entity
@Table(name = "fatransferdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FATransferDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "fatransferlid")
    private UUID faTransferlID;

    @Column(name = "fixedassetid")
    private UUID fixedAssetID;

    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "fromdepartmentid")
    private UUID fromDepartmentID;

    @Column(name = "todepartmentid")
    private UUID toDepartmentID;

    @Column(name = "costaccount")
    private UUID costAccount;

    @Column(name = "employeeid")
    private UUID employeeID;

    @Column(name = "statisticcodeid")
    private UUID statisticCodeID;

    @Column(name = "costsetid")
    private UUID costSetID;

    @Column(name = "expenseitemid")
    private UUID expenseItemID;

    @Column(name = "budgetitemid")
    private UUID budgetItemID;

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

    public UUID getFaTransferlID() {
        return faTransferlID;
    }

    public void setFaTransferlID(UUID faTransferlID) {
        this.faTransferlID = faTransferlID;
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

    public UUID getFromDepartmentID() {
        return fromDepartmentID;
    }

    public void setFromDepartmentID(UUID fromDepartmentID) {
        this.fromDepartmentID = fromDepartmentID;
    }

    public UUID getToDepartmentID() {
        return toDepartmentID;
    }

    public void setToDepartmentID(UUID toDepartmentID) {
        this.toDepartmentID = toDepartmentID;
    }

    public UUID getCostAccount() {
        return costAccount;
    }

    public void setCostAccount(UUID costAccount) {
        this.costAccount = costAccount;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public UUID getStatisticCodeID() {
        return statisticCodeID;
    }

    public void setStatisticCodeID(UUID statisticCodeID) {
        this.statisticCodeID = statisticCodeID;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public UUID getBudgetItemID() {
        return budgetItemID;
    }

    public void setBudgetItemID(UUID budgetItemID) {
        this.budgetItemID = budgetItemID;
    }

    public Integer getOrderPriority() {
        return orderPriority;
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
        FATransferDetail tIIncrementDetails = (FATransferDetail) o;
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
