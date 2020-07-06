package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.Report.TIAdjustmentDTO;
import vn.softdreams.ebweb.service.dto.TIAdjustmentDetailConvertDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A TIAdjustmentDetails.
 */
@Entity
@Table(name = "tiadjustmentdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings( value = {
    @SqlResultSetMapping(
        name = "TIAdjustmentDTOList",
        classes = {
            @ConstructorResult(
                targetClass = TIAdjustmentDTO.class,
                columns = {
                    @ColumnResult(name = "toolCode", type = String.class),
                    @ColumnResult(name = "toolName", type = String.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "remainingAmount", type = BigDecimal.class),
                    @ColumnResult(name = "newRemainingAmount", type = BigDecimal.class),
                    @ColumnResult(name = "diffRemainingAmount", type = BigDecimal.class),
                    @ColumnResult(name = "remainAllocationTimes", type = Integer.class),
                    @ColumnResult(name = "newRemainingAllocationTime", type = Integer.class),
                    @ColumnResult(name = "differAllocationTime", type = Integer.class)
                }

            )
        }
    ),@SqlResultSetMapping(
        name = "TIAdjustmentDetailConvertDTO",
        classes = {
            @ConstructorResult(
                targetClass = TIAdjustmentDetailConvertDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "tiAdjustmentID", type = UUID.class),
                    @ColumnResult(name = "toolsID", type = UUID.class),
                    @ColumnResult(name = "toolsName", type = String.class),
                    @ColumnResult(name = "toolsCode", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "remainingAmount", type = BigDecimal.class),
                    @ColumnResult(name = "newRemainingAmount", type = BigDecimal.class),
                    @ColumnResult(name = "diffRemainingAmount", type = BigDecimal.class),
                    @ColumnResult(name = "remainAllocationTimes", type = Integer.class),
                    @ColumnResult(name = "newRemainingAllocationTime", type = Integer.class),
                    @ColumnResult(name = "differAllocationTime", type = Integer.class),
                    @ColumnResult(name = "allocatedAmount", type = BigDecimal.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
                }

            )
        }
    ),
})
public class TIAdjustmentDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tiadjustmentid")
    private UUID tiAdjustmentID;

    @Column(name = "toolsid")
    private UUID toolsID;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Size(max = 25)
    @Column(name = "debitaccount", length = 25)
    private String debitAccount;

    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "remainingamount", precision = 10, scale = 2)
    private BigDecimal remainingAmount;

    @Column(name = "newremainingamount", precision = 10, scale = 2)
    private BigDecimal newRemainingAmount;

    @Column(name = "diffremainingamount", precision = 10, scale = 2)
    private BigDecimal diffRemainingAmount;

    @Column(name = "remainallocationtimes")
    private Integer remainAllocationTimes;

    @Column(name = "newremainingallocationtime")
    private Integer newRemainingAllocationTime;

    @Column(name = "differallocationtime")
    private Integer differAllocationTime;

    @Column(name = "allocatedamount", precision = 10, scale = 2)
    private BigDecimal AllocatedAmount;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
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

    public UUID getTiAdjustmentID() {
        return tiAdjustmentID;
    }

    public void setTiAdjustmentID(UUID tiAdjustmentID) {
        this.tiAdjustmentID = tiAdjustmentID;
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

    public TIAdjustmentDetails description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public TIAdjustmentDetails quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public TIAdjustmentDetails remainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
        return this;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public BigDecimal getNewRemainingAmount() {
        return newRemainingAmount;
    }

    public TIAdjustmentDetails newRemainingAmount(BigDecimal newRemainingAmount) {
        this.newRemainingAmount = newRemainingAmount;
        return this;
    }

    public void setNewRemainingAmount(BigDecimal newRemainingAmount) {
        this.newRemainingAmount = newRemainingAmount;
    }

    public BigDecimal getDiffRemainingAmount() {
        return diffRemainingAmount;
    }

    public TIAdjustmentDetails diffRemainingAmount(BigDecimal diffRemainingAmount) {
        this.diffRemainingAmount = diffRemainingAmount;
        return this;
    }

    public void setDiffRemainingAmount(BigDecimal diffRemainingAmount) {
        this.diffRemainingAmount = diffRemainingAmount;
    }

    public Integer getRemainAllocationTimes() {
        return remainAllocationTimes;
    }

    public TIAdjustmentDetails remainAllocationTimes(Integer remainAllocationTimes) {
        this.remainAllocationTimes = remainAllocationTimes;
        return this;
    }

    public void setRemainAllocationTimes(Integer remainAllocationTimes) {
        this.remainAllocationTimes = remainAllocationTimes;
    }

    public Integer getNewRemainingAllocationTime() {
        return newRemainingAllocationTime;
    }

    public TIAdjustmentDetails newRemainingAllocationTime(Integer newRemainingAllocationTime) {
        this.newRemainingAllocationTime = newRemainingAllocationTime;
        return this;
    }

    public void setNewRemainingAllocationTime(Integer newRemainingAllocationTime) {
        this.newRemainingAllocationTime = newRemainingAllocationTime;
    }

    public Integer getDifferAllocationTime() {
        return differAllocationTime;
    }

    public TIAdjustmentDetails differAllocationTime(Integer differAllocationTime) {
        this.differAllocationTime = differAllocationTime;
        return this;
    }

    public void setDifferAllocationTime(Integer differAllocationTime) {
        this.differAllocationTime = differAllocationTime;
    }

    public BigDecimal getAllocatedAmount() {
        return AllocatedAmount;
    }

    public TIAdjustmentDetails AllocatedAmount(BigDecimal AllocatedAmount) {
        this.AllocatedAmount = AllocatedAmount;
        return this;
    }

    public void setAllocatedAmount(BigDecimal AllocatedAmount) {
        this.AllocatedAmount = AllocatedAmount;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public TIAdjustmentDetails orderPriority(Integer orderPriority) {
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
        TIAdjustmentDetails tIAdjustmentDetails = (TIAdjustmentDetails) o;
        if (tIAdjustmentDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tIAdjustmentDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TIAdjustmentDetails{" +
            "id=" + getId() +
            ", tiAdjustmentID='" + getTiAdjustmentID() + "'" +
            ", toolsID='" + getToolsID() + "'" +
            ", description='" + getDescription() + "'" +
            ", quantity=" + getQuantity() +
            ", remainingAmount=" + getRemainingAmount() +
            ", newRemainingAmount=" + getNewRemainingAmount() +
            ", diffRemainingAmount=" + getDiffRemainingAmount() +
            ", remainAllocationTimes=" + getRemainAllocationTimes() +
            ", newRemainingAllocationTime=" + getNewRemainingAllocationTime() +
            ", differAllocationTime=" + getDifferAllocationTime() +
            ", AllocatedAmount=" + getAllocatedAmount() +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
