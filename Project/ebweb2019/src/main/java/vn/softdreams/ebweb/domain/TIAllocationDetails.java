package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.web.rest.dto.TIAllocationDetailDTO;
import vn.softdreams.ebweb.web.rest.dto.TIAuditDetailDTO;
import vn.softdreams.ebweb.service.dto.TIAllocationDetailConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.AccountingObjectBankAccountDTO;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A TIAllocationDetails.
 */
@Entity
@Table(name = "tiallocationdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "TIAllocationDetailDTO",
        classes = {
            @ConstructorResult(
                targetClass = TIAllocationDetailDTO.class,
                columns = {
                    @ColumnResult(name = "toolCode", type = String.class),
                    @ColumnResult(name = "toolName", type = String.class),
                    @ColumnResult(name = "allocationAmount", type = BigDecimal.class),
                    @ColumnResult(name = "remainingAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalAllocationAmount", type = BigDecimal.class),
                }
            )
        }
    ),@SqlResultSetMapping(
        name = "TIAllocationDetailConvertDTO",
        classes = {
            @ConstructorResult(
                targetClass = TIAllocationDetailConvertDTO.class,
                columns = {
                    @ColumnResult(name = "toolsID", type = UUID.class),
                    @ColumnResult(name = "toolsName", type = String.class),
                    @ColumnResult(name = "toolsCode", type = String.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "allocationAwaitAccount", type = String.class),
                    @ColumnResult(name = "totalAllocationAmount", type = BigDecimal.class),
                    @ColumnResult(name = "allocationAmount", type = BigDecimal.class),
                    @ColumnResult(name = "remainingAmount", type = BigDecimal.class),

                }
            )
        }
    ),@SqlResultSetMapping(
        name = "TIAllocationDetailConvertDTOs",
        classes = {
            @ConstructorResult(
                targetClass = TIAllocationDetailConvertDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "tiAllocationID", type = UUID.class),
                    @ColumnResult(name = "toolsID", type = UUID.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "totalAllocationAmount", type = BigDecimal.class),
                    @ColumnResult(name = "allocationAmount", type = BigDecimal.class),
                    @ColumnResult(name = "remainingAmount", type = BigDecimal.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
                    @ColumnResult(name = "toolsName", type = String.class),
                }
            )
        }
    )
})
public class TIAllocationDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tiallocationid")
    private UUID tiAllocationID;

    @Column(name = "toolsid")
    private UUID toolsID;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "totalallocationamount", precision = 10, scale = 2)
    private BigDecimal totalAllocationAmount;

    @Column(name = "allocationamount", precision = 10, scale = 2)
    private BigDecimal allocationAmount;

    @Column(name = "remainingamount", precision = 10, scale = 2)
    private BigDecimal remainingAmount;

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

    public UUID getTiAllocationID() {
        return tiAllocationID;
    }

    public void setTiAllocationID(UUID tiAllocationID) {
        this.tiAllocationID = tiAllocationID;
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

    public TIAllocationDetails description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTotalAllocationAmount() {
        return totalAllocationAmount;
    }

    public TIAllocationDetails totalAllocationAmount(BigDecimal totalAllocationAmount) {
        this.totalAllocationAmount = totalAllocationAmount;
        return this;
    }

    public void setTotalAllocationAmount(BigDecimal totalAllocationAmount) {
        this.totalAllocationAmount = totalAllocationAmount;
    }

    public BigDecimal getAllocationAmount() {
        return allocationAmount;
    }

    public TIAllocationDetails allocationAmount(BigDecimal allocationAmount) {
        this.allocationAmount = allocationAmount;
        return this;
    }

    public void setAllocationAmount(BigDecimal allocationAmount) {
        this.allocationAmount = allocationAmount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public TIAllocationDetails remainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
        return this;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public TIAllocationDetails orderPriority(Integer orderPriority) {
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
        TIAllocationDetails tIAllocationDetails = (TIAllocationDetails) o;
        if (tIAllocationDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tIAllocationDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TIAllocationDetails{" +
            "id=" + getId() +
            ", tiAllocationID='" + getTiAllocationID() + "'" +
            ", toolsID='" + getToolsID() + "'" +
            ", description='" + getDescription() + "'" +
            ", totalAllocationAmount=" + getTotalAllocationAmount() +
            ", allocationAmount=" + getAllocationAmount() +
            ", remainingAmount=" + getRemainingAmount() +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
