package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.CPResultDTO;
import vn.softdreams.ebweb.service.dto.CPUncompleteDetailDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A CPResult.
 */
@Entity
@Table(name = "cpresult")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "CPResultDTO",
        classes = {
            @ConstructorResult(
                targetClass = CPResultDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "cPPeriodID", type = UUID.class),
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "materialGoodsName", type = String.class),
                    @ColumnResult(name = "directMatetialAmount", type = BigDecimal.class),
                    @ColumnResult(name = "directLaborAmount", type = BigDecimal.class),
                    @ColumnResult(name = "machineMatetialAmount", type = BigDecimal.class),
                    @ColumnResult(name = "machineLaborAmount", type = BigDecimal.class),
                    @ColumnResult(name = "machineToolsAmount", type = BigDecimal.class),
                    @ColumnResult(name = "machineDepreciationAmount", type = BigDecimal.class),
                    @ColumnResult(name = "machineServiceAmount", type = BigDecimal.class),
                    @ColumnResult(name = "machineGeneralAmount", type = BigDecimal.class),
                    @ColumnResult(name = "generalMatetialAmount", type = BigDecimal.class),
                    @ColumnResult(name = "generalLaborAmount", type = BigDecimal.class),
                    @ColumnResult(name = "generalToolsAmount", type = BigDecimal.class),
                    @ColumnResult(name = "generalDepreciationAmount", type = BigDecimal.class),
                    @ColumnResult(name = "generalServiceAmount", type = BigDecimal.class),
                    @ColumnResult(name = "otherGeneralAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalCostAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "coefficien", type = BigDecimal.class)
                }
            )
        }
    )
})
public class CPResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "cpperiodid")
    private UUID cPPeriodID;

    @Column(name = "cpperioddetailid")
    private UUID cPPeriodDetailID;

    @Column(name = "costsetid")
    private UUID costSetID;

    @Column(name = "materialgoodsid")
    private UUID materialGoodsID;

    @Column(name = "coefficien", precision = 10, scale = 2)
    private BigDecimal coefficien;

    @Column(name = "directmatetialamount", precision = 10, scale = 2)
    private BigDecimal directMatetialAmount;

    @Column(name = "directlaboramount", precision = 10, scale = 2)
    private BigDecimal directLaborAmount;

    @Column(name = "machinematetialamount", precision = 10, scale = 2)
    private BigDecimal machineMatetialAmount;

    @Column(name = "machinelaboramount", precision = 10, scale = 2)
    private BigDecimal machineLaborAmount;

    @Column(name = "machinetoolsamount", precision = 10, scale = 2)
    private BigDecimal machineToolsAmount;

    @Column(name = "machinedepreciationamount", precision = 10, scale = 2)
    private BigDecimal machineDepreciationAmount;

    @Column(name = "machineserviceamount", precision = 10, scale = 2)
    private BigDecimal machineServiceAmount;

    @Column(name = "machinegeneralamount", precision = 10, scale = 2)
    private BigDecimal machineGeneralAmount;

    @Column(name = "generalmatetialamount", precision = 10, scale = 2)
    private BigDecimal generalMatetialAmount;

    @Column(name = "generallaboramount", precision = 10, scale = 2)
    private BigDecimal generalLaborAmount;

    @Column(name = "generaltoolsamount", precision = 10, scale = 2)
    private BigDecimal generalToolsAmount;

    @Column(name = "generaldepreciationamount", precision = 10, scale = 2)
    private BigDecimal generalDepreciationAmount;

    @Column(name = "generalserviceamount", precision = 10, scale = 2)
    private BigDecimal generalServiceAmount;

    @Column(name = "othergeneralamount", precision = 10, scale = 2)
    private BigDecimal otherGeneralAmount;

    @Column(name = "totalcostamount", precision = 10, scale = 2)
    private BigDecimal totalCostAmount;

    @Column(name = "totalquantity", precision = 10, scale = 2)
    private BigDecimal totalQuantity;

    @Column(name = "unitprice", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private LocalDate fromDate;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private LocalDate toDate;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Integer type;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getcPPeriodID() {
        return cPPeriodID;
    }

    public CPResult cPPeriodID(UUID cPPeriodID) {
        this.cPPeriodID = cPPeriodID;
        return this;
    }

    public void setcPPeriodID(UUID cPPeriodID) {
        this.cPPeriodID = cPPeriodID;
    }

    public UUID getcPPeriodDetailID() {
        return cPPeriodDetailID;
    }

    public CPResult cPPeriodDetailID(UUID cPPeriodDetailID) {
        this.cPPeriodDetailID = cPPeriodDetailID;
        return this;
    }

    public void setcPPeriodDetailID(UUID cPPeriodDetailID) {
        this.cPPeriodDetailID = cPPeriodDetailID;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public CPResult costSetID(UUID costSetID) {
        this.costSetID = costSetID;
        return this;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public CPResult materialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
        return this;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public BigDecimal getCoefficien() {
        return coefficien;
    }

    public CPResult coefficien(BigDecimal coefficien) {
        this.coefficien = coefficien;
        return this;
    }

    public void setCoefficien(BigDecimal coefficien) {
        this.coefficien = coefficien;
    }

    public BigDecimal getDirectMatetialAmount() {
        return directMatetialAmount;
    }

    public CPResult directMatetialAmount(BigDecimal directMatetialAmount) {
        this.directMatetialAmount = directMatetialAmount;
        return this;
    }

    public void setDirectMatetialAmount(BigDecimal directMatetialAmount) {
        this.directMatetialAmount = directMatetialAmount;
    }

    public BigDecimal getDirectLaborAmount() {
        return directLaborAmount;
    }

    public CPResult directLaborAmount(BigDecimal directLaborAmount) {
        this.directLaborAmount = directLaborAmount;
        return this;
    }

    public void setDirectLaborAmount(BigDecimal directLaborAmount) {
        this.directLaborAmount = directLaborAmount;
    }

    public BigDecimal getMachineMatetialAmount() {
        return machineMatetialAmount;
    }

    public CPResult machineMatetialAmount(BigDecimal machineMatetialAmount) {
        this.machineMatetialAmount = machineMatetialAmount;
        return this;
    }

    public void setMachineMatetialAmount(BigDecimal machineMatetialAmount) {
        this.machineMatetialAmount = machineMatetialAmount;
    }

    public BigDecimal getMachineLaborAmount() {
        return machineLaborAmount;
    }

    public CPResult machineLaborAmount(BigDecimal machineLaborAmount) {
        this.machineLaborAmount = machineLaborAmount;
        return this;
    }

    public void setMachineLaborAmount(BigDecimal machineLaborAmount) {
        this.machineLaborAmount = machineLaborAmount;
    }

    public BigDecimal getMachineToolsAmount() {
        return machineToolsAmount;
    }

    public CPResult machineToolsAmount(BigDecimal machineToolsAmount) {
        this.machineToolsAmount = machineToolsAmount;
        return this;
    }

    public void setMachineToolsAmount(BigDecimal machineToolsAmount) {
        this.machineToolsAmount = machineToolsAmount;
    }

    public BigDecimal getMachineDepreciationAmount() {
        return machineDepreciationAmount;
    }

    public CPResult machineDepreciationAmount(BigDecimal machineDepreciationAmount) {
        this.machineDepreciationAmount = machineDepreciationAmount;
        return this;
    }

    public void setMachineDepreciationAmount(BigDecimal machineDepreciationAmount) {
        this.machineDepreciationAmount = machineDepreciationAmount;
    }

    public BigDecimal getMachineServiceAmount() {
        return machineServiceAmount;
    }

    public CPResult machineServiceAmount(BigDecimal machineServiceAmount) {
        this.machineServiceAmount = machineServiceAmount;
        return this;
    }

    public void setMachineServiceAmount(BigDecimal machineServiceAmount) {
        this.machineServiceAmount = machineServiceAmount;
    }

    public BigDecimal getMachineGeneralAmount() {
        return machineGeneralAmount;
    }

    public CPResult machineGeneralAmount(BigDecimal machineGeneralAmount) {
        this.machineGeneralAmount = machineGeneralAmount;
        return this;
    }

    public void setMachineGeneralAmount(BigDecimal machineGeneralAmount) {
        this.machineGeneralAmount = machineGeneralAmount;
    }

    public BigDecimal getGeneralMatetialAmount() {
        return generalMatetialAmount;
    }

    public CPResult generalMatetialAmount(BigDecimal generalMatetialAmount) {
        this.generalMatetialAmount = generalMatetialAmount;
        return this;
    }

    public void setGeneralMatetialAmount(BigDecimal generalMatetialAmount) {
        this.generalMatetialAmount = generalMatetialAmount;
    }

    public BigDecimal getGeneralLaborAmount() {
        return generalLaborAmount;
    }

    public CPResult generalLaborAmount(BigDecimal generalLaborAmount) {
        this.generalLaborAmount = generalLaborAmount;
        return this;
    }

    public void setGeneralLaborAmount(BigDecimal generalLaborAmount) {
        this.generalLaborAmount = generalLaborAmount;
    }

    public BigDecimal getGeneralToolsAmount() {
        return generalToolsAmount;
    }

    public CPResult generalToolsAmount(BigDecimal generalToolsAmount) {
        this.generalToolsAmount = generalToolsAmount;
        return this;
    }

    public void setGeneralToolsAmount(BigDecimal generalToolsAmount) {
        this.generalToolsAmount = generalToolsAmount;
    }

    public BigDecimal getGeneralDepreciationAmount() {
        return generalDepreciationAmount;
    }

    public CPResult generalDepreciationAmount(BigDecimal generalDepreciationAmount) {
        this.generalDepreciationAmount = generalDepreciationAmount;
        return this;
    }

    public void setGeneralDepreciationAmount(BigDecimal generalDepreciationAmount) {
        this.generalDepreciationAmount = generalDepreciationAmount;
    }

    public BigDecimal getGeneralServiceAmount() {
        return generalServiceAmount;
    }

    public CPResult generalServiceAmount(BigDecimal generalServiceAmount) {
        this.generalServiceAmount = generalServiceAmount;
        return this;
    }

    public void setGeneralServiceAmount(BigDecimal generalServiceAmount) {
        this.generalServiceAmount = generalServiceAmount;
    }

    public BigDecimal getOtherGeneralAmount() {
        return otherGeneralAmount;
    }

    public CPResult otherGeneralAmount(BigDecimal otherGeneralAmount) {
        this.otherGeneralAmount = otherGeneralAmount;
        return this;
    }

    public void setOtherGeneralAmount(BigDecimal otherGeneralAmount) {
        this.otherGeneralAmount = otherGeneralAmount;
    }

    public BigDecimal getTotalCostAmount() {
        return totalCostAmount;
    }

    public CPResult totalCostAmount(BigDecimal totalCostAmount) {
        this.totalCostAmount = totalCostAmount;
        return this;
    }

    public void setTotalCostAmount(BigDecimal totalCostAmount) {
        this.totalCostAmount = totalCostAmount;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    public CPResult totalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
        return this;
    }

    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public CPResult unitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
        CPResult cPResult = (CPResult) o;
        if (cPResult.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cPResult.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CPResult{" +
            "id=" + getId() +
            ", cPPeriodID=" + getcPPeriodID() +
            ", cPPeriodDetailID=" + getcPPeriodDetailID() +
            ", costSetID=" + getCostSetID() +
            ", materialGoodsID=" + getMaterialGoodsID() +
            ", coefficien=" + getCoefficien() +
            ", directMatetialAmount=" + getDirectMatetialAmount() +
            ", directLaborAmount=" + getDirectLaborAmount() +
            ", machineMatetialAmount=" + getMachineMatetialAmount() +
            ", machineLaborAmount=" + getMachineLaborAmount() +
            ", machineToolsAmount=" + getMachineToolsAmount() +
            ", machineDepreciationAmount=" + getMachineDepreciationAmount() +
            ", machineServiceAmount=" + getMachineServiceAmount() +
            ", machineGeneralAmount=" + getMachineGeneralAmount() +
            ", generalMatetialAmount=" + getGeneralMatetialAmount() +
            ", generalLaborAmount=" + getGeneralLaborAmount() +
            ", generalToolsAmount=" + getGeneralToolsAmount() +
            ", generalDepreciationAmount=" + getGeneralDepreciationAmount() +
            ", generalServiceAmount=" + getGeneralServiceAmount() +
            ", otherGeneralAmount=" + getOtherGeneralAmount() +
            ", totalCostAmount=" + getTotalCostAmount() +
            ", totalQuantity=" + getTotalQuantity() +
            ", unitPrice=" + getUnitPrice() +
            "}";
    }
}
