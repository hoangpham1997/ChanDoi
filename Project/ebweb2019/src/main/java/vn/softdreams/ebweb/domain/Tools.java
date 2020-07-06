package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.dto.Report.SoCongCuDungCuDTO;
import vn.softdreams.ebweb.web.rest.dto.SaReturnDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A Tools.
 */
@Entity
@Table(name = "tools")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "ToolsConvertDTO2",
        classes = {
            @ConstructorResult(
                targetClass = ToolsConvertDTO.class,
                columns = {
                    @ColumnResult(name = "ID", type = UUID.class),
                    @ColumnResult(name = "declareType", type = Integer.class),
                    @ColumnResult(name = "toolsCode", type = String.class),
                    @ColumnResult(name = "toolsName", type = String.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "quantityRest", type = BigDecimal.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "allocationAwaitAccount", type = String.class),
                }
            )
        }),
    @SqlResultSetMapping(
        name = "ToolsConvertDTO",
        classes = {
            @ConstructorResult(
                targetClass = ToolsConvertDTO.class,
                columns = {
                    @ColumnResult(name = "ID", type = UUID.class),
                    @ColumnResult(name = "declareType", type = Integer.class),
                    @ColumnResult(name = "toolsCode", type = String.class),
                    @ColumnResult(name = "toolsName", type = String.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "allocationAwaitAccount", type = String.class),
                }
            )
        }),
    @SqlResultSetMapping(
        name = "ToolsDetailsConvertDTO",
        classes = {
            @ConstructorResult(
                targetClass = ToolsDetailsConvertDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "toolsID", type = UUID.class),
                    @ColumnResult(name = "objectID", type = UUID.class),
                    @ColumnResult(name = "objectType", type = Integer.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "rate", type = BigDecimal.class),
                    @ColumnResult(name = "costAccount", type = String.class),
                    @ColumnResult(name = "expenseItemID", type = UUID.class),
                    @ColumnResult(name = "statisticsCodeID", type = UUID.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
                }
            )
        }),
    @SqlResultSetMapping(
        name = "ToolsDetailsTiAuditConvertDTO",
        classes = {
            @ConstructorResult(
                targetClass = ToolsDetailsTiAuditConvertDTO.class,
                columns = {
                    @ColumnResult(name = "toolsID", type = UUID.class),
                    @ColumnResult(name = "toolsName", type = String.class),
                    @ColumnResult(name = "toolsCode", type = String.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "departmentID", type = UUID.class),
                    @ColumnResult(name = "expenseItemID", type = UUID.class),
                    @ColumnResult(name = "statisticsCodeID", type = UUID.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "costAccount", type = String.class),
                }
            )
        }),
    @SqlResultSetMapping(
        name = "ToolsItemConvertDTO",
        classes = {
            @ConstructorResult(
                targetClass = ToolsDetailsTiAuditConvertDTO.class,
                columns = {
                    @ColumnResult(name = "toolsID", type = UUID.class),
                    @ColumnResult(name = "toolsName", type = String.class),
                    @ColumnResult(name = "toolsCode", type = String.class),
                }
            )
        }),
    @SqlResultSetMapping(
        name = "ToolsViewDTO",
        classes = {
            @ConstructorResult(
                targetClass = ToolsViewDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "toolName", type = String.class),
                    @ColumnResult(name = "toolCode", type = String.class),
                }
            )
        })
})
public class Tools implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "declaretype")
    private Integer declareType;

    @Column(name = "posteddate")
    private LocalDate postedDate;

    @Column(name = "branchid")
    private UUID branchID;

    @Size(max = 25)
    @Column(name = "toolcode", length = 25)
    private String toolCode;

    @Size(max = 255)
    @Column(name = "toolname", length = 255)
    private String toolName;

    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unitprice", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "allocationtimes")
    private Integer allocationTimes;

    @Column(name = "remainallocationtimes")
    private Integer remainAllocationTimes;

    @Column(name = "allocatedamount", precision = 10, scale = 2)
    private BigDecimal allocatedAmount;

    @Column(name = "allocationamount", precision = 10, scale = 2)
    private BigDecimal allocationAmount;

    @Column(name = "remainamount", precision = 10, scale = 2)
    private BigDecimal remainAmount;

    @Size(max = 15)
    @Column(name = "allocationawaitaccount", length = 15)
    private String allocationAwaitAccount;

    @NotNull
    @Column(name = "isactive", nullable = false)
    private Boolean isActive;
//
//    @ManyToOne    @JsonIgnoreProperties("")
//    @JoinColumn(name = "organizationunitid")
//    private OrganizationUnit organizationUnit;
//

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name = "unitid")
    private Unit unit;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getDeclareType() {
        return declareType;
    }
    public Tools declareType(Integer declareType) {
        this.declareType = declareType;
        return this;
    }

    public void setDeclareType(Integer declareType) {
        this.declareType = declareType;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public Tools postedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
        return this;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public Tools branchID(UUID branchID) {
        this.branchID = branchID;
        return this;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public String getToolCode() {
        return toolCode;
    }

    public Tools toolCode(String toolCode) {
        this.toolCode = toolCode;
        return this ;
    }

    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    public String getToolName() {
        return toolName;
    }

    public Tools toolName(String toolName) {
        this.toolName = toolName;
        return this;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public Tools quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Tools unitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Tools amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getAllocationTimes() {
        return allocationTimes;
    }

    public Tools allocationTimes(Integer allocationTimes) {
        this.allocationTimes = allocationTimes;
        return this;
    }

    public void setAllocationTimes(Integer allocationTimes) {
        this.allocationTimes = allocationTimes;
    }

    public Integer getRemainAllocationTimes() {
        return remainAllocationTimes;
    }

    public Tools remainAllocationTimes(Integer remainAllocationTimes) {
        this.remainAllocationTimes = remainAllocationTimes;
        return this;
    }

    public void setRemainAllocationTimes(Integer remainAllocationTimes) {
        this.remainAllocationTimes = remainAllocationTimes;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public Tools allocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
        return this;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public BigDecimal getAllocationAmount() {
        return allocationAmount;
    }

    public Tools allocationAmount(BigDecimal allocationAmount) {
        this.allocationAmount = allocationAmount;
        return this;
    }

    public void setAllocationAmount(BigDecimal allocationAmount) {
        this.allocationAmount = allocationAmount;
    }

    public BigDecimal getRemainAmount() {
        return remainAmount;
    }

    public Tools remainAmount(BigDecimal remainAmount) {
        this.remainAmount = remainAmount;
        return this;
    }

    public void setRemainAmount(BigDecimal remainAmount) {
        this.remainAmount = remainAmount;
    }

    public String getAllocationAwaitAccount() {
        return allocationAwaitAccount;
    }

    public Tools allocationAwaitAccount(String allocationAwaitAccount) {
        this.allocationAwaitAccount = allocationAwaitAccount;
        return this;
    }

    public void setAllocationAwaitAccount(String allocationAwaitAccount) {
        this.allocationAwaitAccount = allocationAwaitAccount;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Tools isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
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
        Tools tools = (Tools) o;
        if (tools.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tools.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tools{" +
            "id=" + getId() +
            ", declareType=" + getDeclareType() +
            ", postedDate='" + getPostedDate() + "'" +
            ", branchID=" + getBranchID() +
            ", toolCode='" + getToolCode() + "'" +
            ", toolName='" + getToolName() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", amount=" + getAmount() +
            ", allocationTimes=" + getAllocationTimes() +
            ", remainAllocationTimes=" + getRemainAllocationTimes() +
            ", allocatedAmount=" + getAllocatedAmount() +
            ", allocationAmount=" + getAllocationAmount() +
            ", remainAmount=" + getRemainAmount() +
            ", allocationAwaitAccount='" + getAllocationAwaitAccount() + "'" +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }
}
