package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.CPAllocationRateDTO;
import vn.softdreams.ebweb.service.dto.ObjectsMaterialQuantumDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A CPAllocationRate.
 */
@Entity
@Table(name = "cpallocationrate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "CPAllocationRateDTO",
        classes = {
            @ConstructorResult(
                targetClass = CPAllocationRateDTO.class,
                columns = {
                    @ColumnResult(name = "allocationMethod", type = Integer.class),
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "isStandardItem", type = Boolean.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "priceQuantum", type = BigDecimal.class),
                    @ColumnResult(name = "coefficient", type = BigDecimal.class),
                    @ColumnResult(name = "quantityStandard", type = BigDecimal.class),
                    @ColumnResult(name = "allocationStandard", type = BigDecimal.class),
                    @ColumnResult(name = "allocatedRate", type = BigDecimal.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "CPAllocationRateDTO1",
        classes = {
            @ConstructorResult(
                targetClass = CPAllocationRateDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "cPPeriodID", type = UUID.class),
                    @ColumnResult(name = "allocationMethod", type = Integer.class),
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "materialGoodsName", type = String.class),
                    @ColumnResult(name = "isStandardItem", type = Boolean.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "priceQuantum", type = BigDecimal.class),
                    @ColumnResult(name = "coefficient", type = BigDecimal.class),
                    @ColumnResult(name = "quantityStandard", type = BigDecimal.class),
                    @ColumnResult(name = "allocationStandard", type = BigDecimal.class),
                    @ColumnResult(name = "allocatedRate", type = BigDecimal.class),
                }
            )
        }
    )})
public class CPAllocationRate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "cpperiodid")
    private UUID cPPeriodID;

    @Column(name = "cpperioddetailid")
    private UUID cPPeriodDetailID;

    @Column(name = "allocationmethod")
    private Integer allocationMethod;

    @Column(name = "costsetid")
    private UUID costSetID;

    @Column(name = "materialgoodsid")
    private UUID materialGoodsID;

    @Column(name = "isstandarditem")
    private Boolean isStandardItem;

    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "pricequantum", precision = 10, scale = 2)
    private BigDecimal priceQuantum;

    @Column(name = "coefficient", precision = 10, scale = 2)
    private BigDecimal coefficient;

    @Column(name = "quantitystandard", precision = 10, scale = 2)
    private BigDecimal quantityStandard;

    @Column(name = "allocationstandard", precision = 10, scale = 2)
    private BigDecimal allocationStandard;

    @Column(name = "allocatedrate", precision = 10, scale = 2)
    private BigDecimal allocatedRate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public Integer getAllocationMethod() {
        return allocationMethod;
    }

    public CPAllocationRate allocationMethod(Integer allocationMethod) {
        this.allocationMethod = allocationMethod;
        return this;
    }

    public void setAllocationMethod(Integer allocationMethod) {
        this.allocationMethod = allocationMethod;
    }

    public Boolean isIsStandardItem() {
        return isStandardItem;
    }

    public CPAllocationRate isStandardItem(Boolean isStandardItem) {
        this.isStandardItem = isStandardItem;
        return this;
    }

    public void setIsStandardItem(Boolean isStandardItem) {
        this.isStandardItem = isStandardItem;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public CPAllocationRate quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceQuantum() {
        return priceQuantum;
    }

    public CPAllocationRate priceQuantum(BigDecimal priceQuantum) {
        this.priceQuantum = priceQuantum;
        return this;
    }

    public void setPriceQuantum(BigDecimal priceQuantum) {
        this.priceQuantum = priceQuantum;
    }

    public BigDecimal getCoefficient() {
        return coefficient;
    }

    public CPAllocationRate coefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
        return this;
    }

    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }

    public BigDecimal getQuantityStandard() {
        return quantityStandard;
    }

    public CPAllocationRate quantityStandard(BigDecimal quantityStandard) {
        this.quantityStandard = quantityStandard;
        return this;
    }

    public void setQuantityStandard(BigDecimal quantityStandard) {
        this.quantityStandard = quantityStandard;
    }

    public BigDecimal getAllocationStandard() {
        return allocationStandard;
    }

    public CPAllocationRate allocationStandard(BigDecimal allocationStandard) {
        this.allocationStandard = allocationStandard;
        return this;
    }

    public void setAllocationStandard(BigDecimal allocationStandard) {
        this.allocationStandard = allocationStandard;
    }

    public BigDecimal getAllocatedRate() {
        return allocatedRate;
    }

    public CPAllocationRate allocatedRate(BigDecimal allocatedRate) {
        this.allocatedRate = allocatedRate;
        return this;
    }

    public void setAllocatedRate(BigDecimal allocatedRate) {
        this.allocatedRate = allocatedRate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getcPPeriodID() {
        return cPPeriodID;
    }

    public void setcPPeriodID(UUID cPPeriodID) {
        this.cPPeriodID = cPPeriodID;
    }

    public UUID getcPPeriodDetailID() {
        return cPPeriodDetailID;
    }

    public void setcPPeriodDetailID(UUID cPPeriodDetailID) {
        this.cPPeriodDetailID = cPPeriodDetailID;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public Boolean getStandardItem() {
        return isStandardItem;
    }

    public void setStandardItem(Boolean standardItem) {
        isStandardItem = standardItem;
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
        CPAllocationRate cPAllocationRate = (CPAllocationRate) o;
        if (cPAllocationRate.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cPAllocationRate.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CPAllocationRate{" +
            "id=" + getId() +
            ", cPPeriodID='" + getcPPeriodID() + "'" +
            ", cPPeriodDetailID='" + getcPPeriodDetailID() + "'" +
            ", allocationMethod=" + getAllocationMethod() +
            ", costSetID='" + getCostSetID() + "'" +
            ", materialGoodsID='" + getMaterialGoodsID() + "'" +
            ", isStandardItem='" + isIsStandardItem() + "'" +
            ", quantity=" + getQuantity() +
            ", priceQuantum=" + getPriceQuantum() +
            ", coefficient=" + getCoefficient() +
            ", quantityStandard=" + getQuantityStandard() +
            ", allocationStandard=" + getAllocationStandard() +
            ", allocatedRate=" + getAllocatedRate() +
            "}";
    }
}
