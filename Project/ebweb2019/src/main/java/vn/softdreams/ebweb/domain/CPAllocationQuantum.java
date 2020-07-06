package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.CPAllocationQuantumDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A CPAllocationQuantum.
 */
@Entity
@Table(name = "cpallocationquantum")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "CPAllocationQuantumDTO",
        classes = {
            @ConstructorResult(
                targetClass = CPAllocationQuantumDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "objectCode", type = String.class),
                    @ColumnResult(name = "objectName", type = String.class),
                    @ColumnResult(name = "directMaterialAmount", type = BigDecimal.class),
                    @ColumnResult(name = "directLaborAmount", type = BigDecimal.class),
                    @ColumnResult(name = "machineMaterialAmount", type = BigDecimal.class),
                    @ColumnResult(name = "machineLaborAmount", type = BigDecimal.class),
                    @ColumnResult(name = "machineToolsAmount", type = BigDecimal.class),
                    @ColumnResult(name = "machineDepreciationAmount", type = BigDecimal.class),
                    @ColumnResult(name = "machineServiceAmount", type = BigDecimal.class),
                    @ColumnResult(name = "machineGeneralAmount", type = BigDecimal.class),
                    @ColumnResult(name = "generalMaterialAmount", type = BigDecimal.class),
                    @ColumnResult(name = "generalLaborAmount", type = BigDecimal.class),
                    @ColumnResult(name = "generalToolsAmount", type = BigDecimal.class),
                    @ColumnResult(name = "generalDepreciationAmount", type = BigDecimal.class),
                    @ColumnResult(name = "generalServiceAmount", type = BigDecimal.class),
                    @ColumnResult(name = "otherGeneralAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalCostAmount", type = BigDecimal.class),
                    @ColumnResult(name = "objectID", type = UUID.class),
                }
            )
        }
    )})
public class CPAllocationQuantum implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Column(name = "directmaterialamount", precision = 10, scale = 2)
    private BigDecimal directMaterialAmount;

    @Column(name = "directlaboramount", precision = 10, scale = 2)
    private BigDecimal directLaborAmount;

    @Column(name = "machinematerialamount", precision = 10, scale = 2)
    private BigDecimal machineMaterialAmount;

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

    @Column(name = "generalmaterialamount", precision = 10, scale = 2)
    private BigDecimal generalMaterialAmount;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


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

    public BigDecimal getDirectMaterialAmount() {
        return directMaterialAmount;
    }

    public void setDirectMaterialAmount(BigDecimal directMaterialAmount) {
        this.directMaterialAmount = directMaterialAmount;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public CPAllocationQuantum typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }



    public BigDecimal getDirectLaborAmount() {
        return directLaborAmount;
    }

    public CPAllocationQuantum directLaborAmount(BigDecimal directLaborAmount) {
        this.directLaborAmount = directLaborAmount;
        return this;
    }

    public void setDirectLaborAmount(BigDecimal directLaborAmount) {
        this.directLaborAmount = directLaborAmount;
    }

    public BigDecimal getMachineMaterialAmount() {
        return machineMaterialAmount;
    }

    public CPAllocationQuantum machineMaterialAmount(BigDecimal machineMaterialAmount) {
        this.machineMaterialAmount = machineMaterialAmount;
        return this;
    }

    public void setMachineMaterialAmount(BigDecimal machineMaterialAmount) {
        this.machineMaterialAmount = machineMaterialAmount;
    }

    public BigDecimal getMachineLaborAmount() {
        return machineLaborAmount;
    }

    public CPAllocationQuantum machineLaborAmount(BigDecimal machineLaborAmount) {
        this.machineLaborAmount = machineLaborAmount;
        return this;
    }

    public void setMachineLaborAmount(BigDecimal machineLaborAmount) {
        this.machineLaborAmount = machineLaborAmount;
    }

    public BigDecimal getMachineToolsAmount() {
        return machineToolsAmount;
    }

    public CPAllocationQuantum machineToolsAmount(BigDecimal machineToolsAmount) {
        this.machineToolsAmount = machineToolsAmount;
        return this;
    }

    public void setMachineToolsAmount(BigDecimal machineToolsAmount) {
        this.machineToolsAmount = machineToolsAmount;
    }

    public BigDecimal getMachineDepreciationAmount() {
        return machineDepreciationAmount;
    }

    public CPAllocationQuantum machineDepreciationAmount(BigDecimal machineDepreciationAmount) {
        this.machineDepreciationAmount = machineDepreciationAmount;
        return this;
    }

    public void setMachineDepreciationAmount(BigDecimal machineDepreciationAmount) {
        this.machineDepreciationAmount = machineDepreciationAmount;
    }

    public BigDecimal getMachineServiceAmount() {
        return machineServiceAmount;
    }

    public CPAllocationQuantum machineServiceAmount(BigDecimal machineServiceAmount) {
        this.machineServiceAmount = machineServiceAmount;
        return this;
    }

    public void setMachineServiceAmount(BigDecimal machineServiceAmount) {
        this.machineServiceAmount = machineServiceAmount;
    }

    public BigDecimal getMachineGeneralAmount() {
        return machineGeneralAmount;
    }

    public CPAllocationQuantum machineGeneralAmount(BigDecimal machineGeneralAmount) {
        this.machineGeneralAmount = machineGeneralAmount;
        return this;
    }

    public void setMachineGeneralAmount(BigDecimal machineGeneralAmount) {
        this.machineGeneralAmount = machineGeneralAmount;
    }

    public BigDecimal getGeneralMaterialAmount() {
        return generalMaterialAmount;
    }

    public CPAllocationQuantum generalMaterialAmount(BigDecimal generalMaterialAmount) {
        this.generalMaterialAmount = generalMaterialAmount;
        return this;
    }

    public void setGeneralMaterialAmount(BigDecimal generalMaterialAmount) {
        this.generalMaterialAmount = generalMaterialAmount;
    }

    public BigDecimal getGeneralLaborAmount() {
        return generalLaborAmount;
    }

    public CPAllocationQuantum generalLaborAmount(BigDecimal generalLaborAmount) {
        this.generalLaborAmount = generalLaborAmount;
        return this;
    }

    public void setGeneralLaborAmount(BigDecimal generalLaborAmount) {
        this.generalLaborAmount = generalLaborAmount;
    }

    public BigDecimal getGeneralToolsAmount() {
        return generalToolsAmount;
    }

    public CPAllocationQuantum generalToolsAmount(BigDecimal generalToolsAmount) {
        this.generalToolsAmount = generalToolsAmount;
        return this;
    }

    public void setGeneralToolsAmount(BigDecimal generalToolsAmount) {
        this.generalToolsAmount = generalToolsAmount;
    }

    public BigDecimal getGeneralDepreciationAmount() {
        return generalDepreciationAmount;
    }

    public CPAllocationQuantum generalDepreciationAmount(BigDecimal generalDepreciationAmount) {
        this.generalDepreciationAmount = generalDepreciationAmount;
        return this;
    }

    public void setGeneralDepreciationAmount(BigDecimal generalDepreciationAmount) {
        this.generalDepreciationAmount = generalDepreciationAmount;
    }

    public BigDecimal getGeneralServiceAmount() {
        return generalServiceAmount;
    }

    public CPAllocationQuantum generalServiceAmount(BigDecimal generalServiceAmount) {
        this.generalServiceAmount = generalServiceAmount;
        return this;
    }

    public void setGeneralServiceAmount(BigDecimal generalServiceAmount) {
        this.generalServiceAmount = generalServiceAmount;
    }

    public BigDecimal getOtherGeneralAmount() {
        return otherGeneralAmount;
    }

    public CPAllocationQuantum otherGeneralAmount(BigDecimal otherGeneralAmount) {
        this.otherGeneralAmount = otherGeneralAmount;
        return this;
    }

    public void setOtherGeneralAmount(BigDecimal otherGeneralAmount) {
        this.otherGeneralAmount = otherGeneralAmount;
    }

    public BigDecimal getTotalCostAmount() {
        return totalCostAmount;
    }

    public CPAllocationQuantum totalCostAmount(BigDecimal totalCostAmount) {
        this.totalCostAmount = totalCostAmount;
        return this;
    }

    public void setTotalCostAmount(BigDecimal totalCostAmount) {
        this.totalCostAmount = totalCostAmount;
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
        CPAllocationQuantum cPAllocationQuantum = (CPAllocationQuantum) o;
        if (cPAllocationQuantum.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cPAllocationQuantum.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CPAllocationQuantum{" +
            "id=" + getId() +
            ", companyID='" + getCompanyID() + "'" +
            ", branchID='" + getBranchID() + "'" +
            ", typeLedger=" + getTypeLedger() +
//            ", directMatetialAmount=" + getDirectMatetialAmount() +
            ", directLaborAmount=" + getDirectLaborAmount() +
            ", machineMaterialAmount=" + getMachineMaterialAmount() +
            ", machineLaborAmount=" + getMachineLaborAmount() +
            ", machineToolsAmount=" + getMachineToolsAmount() +
            ", machineDepreciationAmount=" + getMachineDepreciationAmount() +
            ", machineServiceAmount=" + getMachineServiceAmount() +
            ", machineGeneralAmount=" + getMachineGeneralAmount() +
            ", generalMaterialAmount=" + getGeneralMaterialAmount() +
            ", generalLaborAmount=" + getGeneralLaborAmount() +
            ", generalToolsAmount=" + getGeneralToolsAmount() +
            ", generalDepreciationAmount=" + getGeneralDepreciationAmount() +
            ", generalServiceAmount=" + getGeneralServiceAmount() +
            ", otherGeneralAmount=" + getOtherGeneralAmount() +
            ", totalCostAmount=" + getTotalCostAmount() +
            "}";
    }
}
