package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.Report.TongHopCPTheoKMCPDTO;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A ExpenseItem.
 */
@Entity
@Table(name = "expenseitem")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "TongHopCPTheoKMCPDTO",
        classes = {
            @ConstructorResult(
                targetClass = TongHopCPTheoKMCPDTO.class,
                columns = {
                    @ColumnResult(name = "expenseItemID", type = UUID.class),
                    @ColumnResult(name = "expenseItemCode", type = String.class),
                    @ColumnResult(name = "expenseItemName", type = String.class),
                    @ColumnResult(name = "account", type = String.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                }
            )
        }
    )})
public class ExpenseItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private  UUID companyID;

    @NotNull
    @Size(max = 25)
    @Column(name = "expenseitemcode", length = 25, nullable = false)
    private String expenseItemCode;

    @NotNull
    @Size(max = 512)
    @Column(name = "expenseitemname", length = 512, nullable = false)
    private String expenseItemName;

    @Column(name = "expensetype")
    private Integer expenseType;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "parentid")
    private UUID parentID;

    @Column(name = "isparentnode")
    private Boolean isParentNode;

    @Size(max = 200)
    @Column(name = "orderfixcode", length = 200)
    private String orderFixCode;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "isactive")
    private Boolean isActive;

    @NotNull
    @Column(name = "issecurity")
    private Boolean isSecurity;

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getExpenseItemCode() {
        return expenseItemCode;
    }

    public ExpenseItem expenseItemCode(String expenseItemCode) {
        this.expenseItemCode = expenseItemCode;
        return this;
    }

    public void setExpenseItemCode(String expenseItemCode) {
        this.expenseItemCode = expenseItemCode;
    }

    public String getExpenseItemName() {
        return expenseItemName;
    }

    public ExpenseItem expenseItemName(String expenseItemName) {
        this.expenseItemName = expenseItemName;
        return this;
    }

    public void setExpenseItemName(String expenseItemName) {
        this.expenseItemName = expenseItemName;
    }

    public Integer getExpenseType() {
        return expenseType;
    }

    public ExpenseItem expenseType(Integer expenseType) {
        this.expenseType = expenseType;
        return this;
    }

    public void setExpenseType(Integer expenseType) {
        this.expenseType = expenseType;
    }

    public String getDescription() {
        return description;
    }

    public ExpenseItem description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getParentID() {
        return parentID;
    }

    public ExpenseItem parentID(UUID parentID) {
        this.parentID = parentID;
        return this;
    }

    public void setParentID(UUID parentID) {
        this.parentID = parentID;
    }

    public Boolean getIsParentNode() {
        return isParentNode;
    }

    public ExpenseItem isParentNode(Boolean isParentNode) {
        this.isParentNode = isParentNode;
        return this;
    }

    public void setIsParentNode(Boolean isParentNode) {
        this.isParentNode = isParentNode;
    }

    public String getOrderFixCode() {
        return orderFixCode;
    }

    public ExpenseItem orderFixCode(String orderFixCode) {
        this.orderFixCode = orderFixCode;
        return this;
    }

    public void setOrderFixCode(String orderFixCode) {
        this.orderFixCode = orderFixCode;
    }

    public Integer getGrade() {
        return grade;
    }

    public ExpenseItem grade(Integer grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public ExpenseItem isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsSecurity() {
        return isSecurity;
    }

    public ExpenseItem isSecurity(Boolean isSecurity) {
        this.isSecurity = isSecurity;
        return this;
    }

    public void setIsSecurity(Boolean isSecurity) {
        this.isSecurity = isSecurity;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public String toString() {
        return "ExpenseItem{" +
            "id=" + getId() +
            ", CompanyID='" + getCompanyID() + "'" +
            ", expenseItemCode='" + getExpenseItemCode() + "'" +
            ", expenseItemName='" + getExpenseItemName() + "'" +
            ", expenseType=" + getExpenseType() +
            ", description='" + getDescription() + "'" +
            ", parentID='" + getParentID() + "'" +
            ", isParentNode='" + getIsParentNode() + "'" +
            ", orderFixCode='" + getOrderFixCode() + "'" +
            ", grade=" + getGrade() +
            ", isActive='" + getIsActive() + "'" +
            ", isSecurity='" + getIsSecurity() + "'" +
            "}";
    }
}
