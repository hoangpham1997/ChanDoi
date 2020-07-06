package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.CPUncompleteDetailDTO;
import vn.softdreams.ebweb.web.rest.dto.CPPeriodDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A CPUncompleteDetails.
 */
@Entity
@Table(name = "cpuncompletedetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "CPUncompleteDetailDTO",
        classes = {
            @ConstructorResult(
                targetClass = CPUncompleteDetailDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "cPUncompleteID", type = UUID.class),
                    @ColumnResult(name = "cPPeriodID", type = UUID.class),
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "costSetName", type = String.class),
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
                    @ColumnResult(name = "totalCostAmount", type = BigDecimal.class)
                }
            )
        }
    )
})
public class CPUncompleteDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "cpuncompleteid")
    private UUID cPUncompleteID;

    @Column(name = "cpperiodid")
    private UUID cPPeriodID;

    @Column(name = "costsetid")
    private UUID costSetID;

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

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private BigDecimal quantity;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getcPUncompleteID() {
        return cPUncompleteID;
    }

    public CPUncompleteDetails cPUncompleteID(UUID cPUncompleteID) {
        this.cPUncompleteID = cPUncompleteID;
        return this;
    }

    public void setcPUncompleteID(UUID cPUncompleteID) {
        this.cPUncompleteID = cPUncompleteID;
    }

    public UUID getcPPeriodID() {
        return cPPeriodID;
    }

    public CPUncompleteDetails cPPeriodID(UUID cPPeriodID) {
        this.cPPeriodID = cPPeriodID;
        return this;
    }

    public void setcPPeriodID(UUID cPPeriodID) {
        this.cPPeriodID = cPPeriodID;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public CPUncompleteDetails costSetID(UUID costSetID) {
        this.costSetID = costSetID;
        return this;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public BigDecimal getDirectMatetialAmount() {
        return directMatetialAmount;
    }

    public CPUncompleteDetails directMatetialAmount(BigDecimal directMatetialAmount) {
        this.directMatetialAmount = directMatetialAmount;
        return this;
    }

    public void setDirectMatetialAmount(BigDecimal directMatetialAmount) {
        this.directMatetialAmount = directMatetialAmount;
    }

    public BigDecimal getDirectLaborAmount() {
        return directLaborAmount;
    }

    public CPUncompleteDetails directLaborAmount(BigDecimal directLaborAmount) {
        this.directLaborAmount = directLaborAmount;
        return this;
    }

    public void setDirectLaborAmount(BigDecimal directLaborAmount) {
        this.directLaborAmount = directLaborAmount;
    }

    public BigDecimal getMachineMatetialAmount() {
        return machineMatetialAmount;
    }

    public CPUncompleteDetails machineMatetialAmount(BigDecimal machineMatetialAmount) {
        this.machineMatetialAmount = machineMatetialAmount;
        return this;
    }

    public void setMachineMatetialAmount(BigDecimal machineMatetialAmount) {
        this.machineMatetialAmount = machineMatetialAmount;
    }

    public BigDecimal getMachineLaborAmount() {
        return machineLaborAmount;
    }

    public CPUncompleteDetails machineLaborAmount(BigDecimal machineLaborAmount) {
        this.machineLaborAmount = machineLaborAmount;
        return this;
    }

    public void setMachineLaborAmount(BigDecimal machineLaborAmount) {
        this.machineLaborAmount = machineLaborAmount;
    }

    public BigDecimal getMachineToolsAmount() {
        return machineToolsAmount;
    }

    public CPUncompleteDetails machineToolsAmount(BigDecimal machineToolsAmount) {
        this.machineToolsAmount = machineToolsAmount;
        return this;
    }

    public void setMachineToolsAmount(BigDecimal machineToolsAmount) {
        this.machineToolsAmount = machineToolsAmount;
    }

    public BigDecimal getMachineDepreciationAmount() {
        return machineDepreciationAmount;
    }

    public CPUncompleteDetails machineDepreciationAmount(BigDecimal machineDepreciationAmount) {
        this.machineDepreciationAmount = machineDepreciationAmount;
        return this;
    }

    public void setMachineDepreciationAmount(BigDecimal machineDepreciationAmount) {
        this.machineDepreciationAmount = machineDepreciationAmount;
    }

    public BigDecimal getMachineServiceAmount() {
        return machineServiceAmount;
    }

    public CPUncompleteDetails machineServiceAmount(BigDecimal machineServiceAmount) {
        this.machineServiceAmount = machineServiceAmount;
        return this;
    }

    public void setMachineServiceAmount(BigDecimal machineServiceAmount) {
        this.machineServiceAmount = machineServiceAmount;
    }

    public BigDecimal getMachineGeneralAmount() {
        return machineGeneralAmount;
    }

    public CPUncompleteDetails machineGeneralAmount(BigDecimal machineGeneralAmount) {
        this.machineGeneralAmount = machineGeneralAmount;
        return this;
    }

    public void setMachineGeneralAmount(BigDecimal machineGeneralAmount) {
        this.machineGeneralAmount = machineGeneralAmount;
    }

    public BigDecimal getGeneralMatetialAmount() {
        return generalMatetialAmount;
    }

    public CPUncompleteDetails generalMatetialAmount(BigDecimal generalMatetialAmount) {
        this.generalMatetialAmount = generalMatetialAmount;
        return this;
    }

    public void setGeneralMatetialAmount(BigDecimal generalMatetialAmount) {
        this.generalMatetialAmount = generalMatetialAmount;
    }

    public BigDecimal getGeneralLaborAmount() {
        return generalLaborAmount;
    }

    public CPUncompleteDetails generalLaborAmount(BigDecimal generalLaborAmount) {
        this.generalLaborAmount = generalLaborAmount;
        return this;
    }

    public void setGeneralLaborAmount(BigDecimal generalLaborAmount) {
        this.generalLaborAmount = generalLaborAmount;
    }

    public BigDecimal getGeneralToolsAmount() {
        return generalToolsAmount;
    }

    public CPUncompleteDetails generalToolsAmount(BigDecimal generalToolsAmount) {
        this.generalToolsAmount = generalToolsAmount;
        return this;
    }

    public void setGeneralToolsAmount(BigDecimal generalToolsAmount) {
        this.generalToolsAmount = generalToolsAmount;
    }

    public BigDecimal getGeneralDepreciationAmount() {
        return generalDepreciationAmount;
    }

    public CPUncompleteDetails generalDepreciationAmount(BigDecimal generalDepreciationAmount) {
        this.generalDepreciationAmount = generalDepreciationAmount;
        return this;
    }

    public void setGeneralDepreciationAmount(BigDecimal generalDepreciationAmount) {
        this.generalDepreciationAmount = generalDepreciationAmount;
    }

    public BigDecimal getGeneralServiceAmount() {
        return generalServiceAmount;
    }

    public CPUncompleteDetails generalServiceAmount(BigDecimal generalServiceAmount) {
        this.generalServiceAmount = generalServiceAmount;
        return this;
    }

    public void setGeneralServiceAmount(BigDecimal generalServiceAmount) {
        this.generalServiceAmount = generalServiceAmount;
    }

    public BigDecimal getOtherGeneralAmount() {
        return otherGeneralAmount;
    }

    public CPUncompleteDetails otherGeneralAmount(BigDecimal otherGeneralAmount) {
        this.otherGeneralAmount = otherGeneralAmount;
        return this;
    }

    public void setOtherGeneralAmount(BigDecimal otherGeneralAmount) {
        this.otherGeneralAmount = otherGeneralAmount;
    }

    public BigDecimal getTotalCostAmount() {
        return totalCostAmount;
    }

    public CPUncompleteDetails totalCostAmount(BigDecimal totalCostAmount) {
        this.totalCostAmount = totalCostAmount;
        return this;
    }

    public void setTotalCostAmount(BigDecimal totalCostAmount) {
        this.totalCostAmount = totalCostAmount;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
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
        CPUncompleteDetails cPUncompleteDetails = (CPUncompleteDetails) o;
        if (cPUncompleteDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cPUncompleteDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CPUncompleteDetails{" +
            "id=" + getId() +
            ", cPUncompleteID=" + getcPUncompleteID() +
            ", cPPeriodID=" + getcPPeriodID() +
            ", costSetID=" + getCostSetID() +
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
            "}";
    }
}
