package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.Report.SoCongCuDungCuDTO;
import vn.softdreams.ebweb.service.dto.Report.SoTheoDoiCCDCTaiNoiSuDungDTO;
import vn.softdreams.ebweb.service.dto.ToolsConvertDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A Toolledger.
 */
@Entity
@Table(name = "toolledger")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "SoCongCuDungCuDTO",
        classes = {
            @ConstructorResult(
                targetClass = SoCongCuDungCuDTO.class,
                columns = {
                    @ColumnResult(name = "toolsID", type = UUID.class),
                    @ColumnResult(name = "toolsCode", type = String.class),
                    @ColumnResult(name = "toolsName", type = String.class),
                    @ColumnResult(name = "incrementDate", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "allocationTimes", type = Integer.class),
                    @ColumnResult(name = "allocatedAmount", type = BigDecimal.class),
                    @ColumnResult(name = "decrementAllocationTime", type = Integer.class),
                    @ColumnResult(name = "remainingAllocationTime", type = Integer.class),
                    @ColumnResult(name = "decrementAmount", type = BigDecimal.class),
                    @ColumnResult(name = "remainingAmount", type = BigDecimal.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "SoTheoDoiCCDCTaiNoiSuDungDTO",
        classes = {
            @ConstructorResult(
                targetClass = SoTheoDoiCCDCTaiNoiSuDungDTO.class,
                columns = {
                    @ColumnResult(name = "iDPhongBan", type = UUID.class),
                    @ColumnResult(name = "maPhongBan", type = String.class),
                    @ColumnResult(name = "tenPhongBan", type = String.class),
                    @ColumnResult(name = "soChungTuGhiTang", type = String.class),
                    @ColumnResult(name = "ngayChungTuGhiTang", type = LocalDate.class),
                    @ColumnResult(name = "toolsName", type = String.class),
                    @ColumnResult(name = "unitName", type = String.class),
                    @ColumnResult(name = "incrementQuantity", type = Integer.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "incrementAmount", type = BigDecimal.class),
                    @ColumnResult(name = "soChungTuGhiGiam", type = String.class),
                    @ColumnResult(name = "ngayChungTuGhiGiam", type = LocalDate.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "decrementQuantity", type = Integer.class),
                    @ColumnResult(name = "decrementAmount", type = BigDecimal.class),
                }
            )
        })
})
public class Toolledger implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    @NotNull
    @Column(name = "typeid", nullable = false)
    private Integer typeID;

    @NotNull
    @Column(name = "referenceid", nullable = false)
    private UUID referenceID;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Size(max = 25)
    @Column(name = "nofbook", length = 25)
    private String noFBook;

    @Size(max = 25)
    @Column(name = "nombook", length = 25)
    private String noMBook;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "posteddate")
    private LocalDate postedDate;

    @Column(name = "toolsid")
    private UUID toolsID;

    @Size(max = 25)
    @Column(name = "toolcode", length = 25)
    private String toolCode;

    @Size(max = 255)
    @Column(name = "toolname", length = 255)
    private String toolName;

    @Size(max = 255)
    @Column(name = "reason", length = 255)
    private String reason;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "unitprice", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "incrementallocationtime", precision = 10, scale = 2)
    private BigDecimal incrementAllocationTime;

    @Column(name = "decrementallocationtime", precision = 10, scale = 2)
    private BigDecimal decrementAllocationTime;

    @Column(name = "incrementquantity", precision = 10, scale = 2)
    private BigDecimal incrementQuantity;

    @Column(name = "decrementquantity", precision = 10, scale = 2)
    private BigDecimal decrementQuantity;

    @Column(name = "incrementamount", precision = 10, scale = 2)
    private BigDecimal incrementAmount;

    @Column(name = "decrementamount", precision = 10, scale = 2)
    private BigDecimal decrementAmount;

    @Column(name = "allocationamount", precision = 10, scale = 2)
    private BigDecimal allocationAmount;

    @Column(name = "allocatedamount", precision = 10, scale = 2)
    private BigDecimal allocatedAmount;

    @Column(name = "departmentid")
    private UUID departmentID;

    @Column(name = "remainingquantity", precision = 10, scale = 2)
    private BigDecimal remainingQuantity;

    @Column(name = "remainingallocaitontimes", precision = 10, scale = 2)
    private BigDecimal remainingAllocaitonTimes;

    @Column(name = "remainingamount", precision = 10, scale = 2)
    private BigDecimal remainingAmount;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public Integer getTypeID() {
        return typeID;
    }

    public Toolledger typeID(Integer typeID) {
        this.typeID = typeID;
        return this;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }


    public Integer getTypeLedger() {
        return typeLedger;
    }

    public Toolledger typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public Toolledger noFBook(String noFBook) {
        this.noFBook = noFBook;
        return this;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public Toolledger noMBook(String noMBook) {
        this.noMBook = noMBook;
        return this;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public LocalDate getDate() {
        return date;
    }

    public Toolledger date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public Toolledger postedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
        return this;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }


    public String getToolCode() {
        return toolCode;
    }

    public Toolledger toolCode(String toolCode) {
        this.toolCode = toolCode;
        return this;
    }

    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    public String getToolName() {
        return toolName;
    }

    public Toolledger toolName(String toolName) {
        this.toolName = toolName;
        return this;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getReason() {
        return reason;
    }

    public Toolledger reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public Toolledger description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Toolledger unitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getIncrementAllocationTime() {
        return incrementAllocationTime;
    }

    public Toolledger incrementAllocationTime(BigDecimal incrementAllocationTime) {
        this.incrementAllocationTime = incrementAllocationTime;
        return this;
    }

    public void setIncrementAllocationTime(BigDecimal incrementAllocationTime) {
        this.incrementAllocationTime = incrementAllocationTime;
    }

    public BigDecimal getDecrementAllocationTime() {
        return decrementAllocationTime;
    }

    public Toolledger decrementAllocationTime(BigDecimal decrementAllocationTime) {
        this.decrementAllocationTime = decrementAllocationTime;
        return this;
    }

    public void setDecrementAllocationTime(BigDecimal decrementAllocationTime) {
        this.decrementAllocationTime = decrementAllocationTime;
    }

    public BigDecimal getIncrementQuantity() {
        return incrementQuantity;
    }

    public Toolledger incrementQuantity(BigDecimal incrementQuantity) {
        this.incrementQuantity = incrementQuantity;
        return this;
    }

    public void setIncrementQuantity(BigDecimal incrementQuantity) {
        this.incrementQuantity = incrementQuantity;
    }

    public BigDecimal getDecrementQuantity() {
        return decrementQuantity;
    }

    public Toolledger decrementQuantity(BigDecimal decrementQuantity) {
        this.decrementQuantity = decrementQuantity;
        return this;
    }

    public void setDecrementQuantity(BigDecimal decrementQuantity) {
        this.decrementQuantity = decrementQuantity;
    }

    public BigDecimal getIncrementAmount() {
        return incrementAmount;
    }

    public Toolledger incrementAmount(BigDecimal incrementAmount) {
        this.incrementAmount = incrementAmount;
        return this;
    }

    public void setIncrementAmount(BigDecimal incrementAmount) {
        this.incrementAmount = incrementAmount;
    }

    public BigDecimal getDecrementAmount() {
        return decrementAmount;
    }

    public Toolledger decrementAmount(BigDecimal decrementAmount) {
        this.decrementAmount = decrementAmount;
        return this;
    }

    public void setDecrementAmount(BigDecimal decrementAmount) {
        this.decrementAmount = decrementAmount;
    }

    public BigDecimal getAllocationAmount() {
        return allocationAmount;
    }

    public Toolledger allocationAmount(BigDecimal allocationAmount) {
        this.allocationAmount = allocationAmount;
        return this;
    }

    public void setAllocationAmount(BigDecimal allocationAmount) {
        this.allocationAmount = allocationAmount;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public Toolledger allocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
        return this;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public BigDecimal getRemainingQuantity() {
        return remainingQuantity;
    }

    public Toolledger remainingQuantity(BigDecimal remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
        return this;
    }

    public void setRemainingQuantity(BigDecimal remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public BigDecimal getRemainingAllocaitonTimes() {
        return remainingAllocaitonTimes;
    }

    public Toolledger remainingAllocaitonTimes(BigDecimal remainingAllocaitonTimes) {
        this.remainingAllocaitonTimes = remainingAllocaitonTimes;
        return this;
    }

    public void setRemainingAllocaitonTimes(BigDecimal remainingAllocaitonTimes) {
        this.remainingAllocaitonTimes = remainingAllocaitonTimes;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public Toolledger remainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
        return this;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public Toolledger orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
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

    public UUID getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(UUID referenceID) {
        this.referenceID = referenceID;
    }

    public UUID getToolsID() {
        return toolsID;
    }

    public void setToolsID(UUID toolsID) {
        this.toolsID = toolsID;
    }

    public UUID getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(UUID departmentID) {
        this.departmentID = departmentID;
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
        Toolledger toolledger = (Toolledger) o;
        if (toolledger.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), toolledger.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Toolledger{" +
            "id=" + getId() +
            ", companyID='" + getCompanyID() + "'" +
            ", branchID='" + getBranchID() + "'" +
            ", typeID=" + getTypeID() +
            ", referenceID='" + getReferenceID() + "'" +
            ", typeLedger=" + getTypeLedger() +
            ", noFBook='" + getNoFBook() + "'" +
            ", noMBook='" + getNoMBook() + "'" +
            ", date='" + getDate() + "'" +
            ", postedDate='" + getPostedDate() + "'" +
            ", toolsID='" + getToolsID() + "'" +
            ", toolCode='" + getToolCode() + "'" +
            ", toolName='" + getToolName() + "'" +
            ", reason='" + getReason() + "'" +
            ", description='" + getDescription() + "'" +
            ", unitPrice=" + getUnitPrice() +
            ", incrementAllocationTime=" + getIncrementAllocationTime() +
            ", decrementAllocationTime=" + getDecrementAllocationTime() +
            ", incrementQuantity=" + getIncrementQuantity() +
            ", decrementQuantity=" + getDecrementQuantity() +
            ", incrementAmount=" + getIncrementAmount() +
            ", decrementAmount=" + getDecrementAmount() +
            ", allocationAmount=" + getAllocationAmount() +
            ", allocatedAmount=" + getAllocatedAmount() +
            ", departmentID='" + getDepartmentID() + "'" +
            ", remainingQuantity=" + getRemainingQuantity() +
            ", remainingAllocaitonTimes=" + getRemainingAllocaitonTimes() +
            ", remainingAmount=" + getRemainingAmount() +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
