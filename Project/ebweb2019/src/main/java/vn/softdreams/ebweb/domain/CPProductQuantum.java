package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.CPProductQuantumDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A CPProductQuantum.
 */
@Entity
@Table(name = "cpproductquantum")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "CPProductQuantumDTO",
        classes = {
            @ConstructorResult(
                targetClass = CPProductQuantumDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "materialGoodsName", type = String.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
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
                    @ColumnResult(name = "materialGoodsID", type = UUID.class)
                }
            )
        }
    )})
public class CPProductQuantum implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

//    @Column(name = "materialgoodsid")
//    private UUID materialGoodsID;

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

    public CPProductQuantum companyID(UUID companyID) {
        this.companyID = companyID;
        return this;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public CPProductQuantum branchID(UUID branchID) {
        this.branchID = branchID;
        return this;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public CPProductQuantum typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public BigDecimal getDirectMaterialAmount() {
        return directMaterialAmount;
    }

    public CPProductQuantum directMaterialAmount(BigDecimal directMaterialAmount) {
        this.directMaterialAmount = directMaterialAmount;
        return this;
    }

    public void setDirectMaterialAmount(BigDecimal directMaterialAmount) {
        this.directMaterialAmount = directMaterialAmount;
    }

    public BigDecimal getDirectLaborAmount() {
        return directLaborAmount;
    }

    public CPProductQuantum directLaborAmount(BigDecimal directLaborAmount) {
        this.directLaborAmount = directLaborAmount;
        return this;
    }

    public void setDirectLaborAmount(BigDecimal directLaborAmount) {
        this.directLaborAmount = directLaborAmount;
    }

    public BigDecimal getMachineMaterialAmount() {
        return machineMaterialAmount;
    }

    public CPProductQuantum machineMaterialAmount(BigDecimal machineMaterialAmount) {
        this.machineMaterialAmount = machineMaterialAmount;
        return this;
    }

    public void setMachineMaterialAmount(BigDecimal machineMaterialAmount) {
        this.machineMaterialAmount = machineMaterialAmount;
    }

    public BigDecimal getMachineLaborAmount() {
        return machineLaborAmount;
    }

    public CPProductQuantum machineLaborAmount(BigDecimal machineLaborAmount) {
        this.machineLaborAmount = machineLaborAmount;
        return this;
    }

    public void setMachineLaborAmount(BigDecimal machineLaborAmount) {
        this.machineLaborAmount = machineLaborAmount;
    }

    public BigDecimal getMachineToolsAmount() {
        return machineToolsAmount;
    }

    public CPProductQuantum machineToolsAmount(BigDecimal machineToolsAmount) {
        this.machineToolsAmount = machineToolsAmount;
        return this;
    }

    public void setMachineToolsAmount(BigDecimal machineToolsAmount) {
        this.machineToolsAmount = machineToolsAmount;
    }

    public BigDecimal getMachineDepreciationAmount() {
        return machineDepreciationAmount;
    }

    public CPProductQuantum machineDepreciationAmount(BigDecimal machineDepreciationAmount) {
        this.machineDepreciationAmount = machineDepreciationAmount;
        return this;
    }

    public void setMachineDepreciationAmount(BigDecimal machineDepreciationAmount) {
        this.machineDepreciationAmount = machineDepreciationAmount;
    }

    public BigDecimal getMachineServiceAmount() {
        return machineServiceAmount;
    }

    public CPProductQuantum machineServiceAmount(BigDecimal machineServiceAmount) {
        this.machineServiceAmount = machineServiceAmount;
        return this;
    }

    public void setMachineServiceAmount(BigDecimal machineServiceAmount) {
        this.machineServiceAmount = machineServiceAmount;
    }

    public BigDecimal getMachineGeneralAmount() {
        return machineGeneralAmount;
    }

    public CPProductQuantum machineGeneralAmount(BigDecimal machineGeneralAmount) {
        this.machineGeneralAmount = machineGeneralAmount;
        return this;
    }

    public void setMachineGeneralAmount(BigDecimal machineGeneralAmount) {
        this.machineGeneralAmount = machineGeneralAmount;
    }


    public BigDecimal getGeneralMaterialAmount() {
        return generalMaterialAmount;
    }

    public void setGeneralMaterialAmount(BigDecimal generalMaterialAmount) {
        this.generalMaterialAmount = generalMaterialAmount;
    }

    public BigDecimal getGeneralLaborAmount() {
        return generalLaborAmount;
    }

    public CPProductQuantum generalLaborAmount(BigDecimal generalLaborAmount) {
        this.generalLaborAmount = generalLaborAmount;
        return this;
    }

    public void setGeneralLaborAmount(BigDecimal generalLaborAmount) {
        this.generalLaborAmount = generalLaborAmount;
    }

    public BigDecimal getGeneralToolsAmount() {
        return generalToolsAmount;
    }

    public CPProductQuantum generalToolsAmount(BigDecimal generalToolsAmount) {
        this.generalToolsAmount = generalToolsAmount;
        return this;
    }

    public void setGeneralToolsAmount(BigDecimal generalToolsAmount) {
        this.generalToolsAmount = generalToolsAmount;
    }

    public BigDecimal getGeneralDepreciationAmount() {
        return generalDepreciationAmount;
    }

    public CPProductQuantum generalDepreciationAmount(BigDecimal generalDepreciationAmount) {
        this.generalDepreciationAmount = generalDepreciationAmount;
        return this;
    }

    public void setGeneralDepreciationAmount(BigDecimal generalDepreciationAmount) {
        this.generalDepreciationAmount = generalDepreciationAmount;
    }

    public BigDecimal getGeneralServiceAmount() {
        return generalServiceAmount;
    }

    public CPProductQuantum generalServiceAmount(BigDecimal generalServiceAmount) {
        this.generalServiceAmount = generalServiceAmount;
        return this;
    }

    public void setGeneralServiceAmount(BigDecimal generalServiceAmount) {
        this.generalServiceAmount = generalServiceAmount;
    }

    public BigDecimal getOtherGeneralAmount() {
        return otherGeneralAmount;
    }

    public CPProductQuantum otherGeneralAmount(BigDecimal otherGeneralAmount) {
        this.otherGeneralAmount = otherGeneralAmount;
        return this;
    }

    public void setOtherGeneralAmount(BigDecimal otherGeneralAmount) {
        this.otherGeneralAmount = otherGeneralAmount;
    }

    public BigDecimal getTotalCostAmount() {
        return totalCostAmount;
    }

    public CPProductQuantum totalCostAmount(BigDecimal totalCostAmount) {
        this.totalCostAmount = totalCostAmount;
        return this;
    }

    public void setTotalCostAmount(BigDecimal totalCostAmount) {
        this.totalCostAmount = totalCostAmount;
    }

//    public UUID getMaterialGoodsID() {
//        return materialGoodsID;
//    }
//
//    public void setMaterialGoodsID(UUID materialGoodsID) {
//        this.materialGoodsID = materialGoodsID;
//    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CPProductQuantum cPProductQuantum = (CPProductQuantum) o;
        if (cPProductQuantum.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cPProductQuantum.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CPProductQuantum{" +
            "id=" + getId() +
            ", companyID='" + getCompanyID() + "'" +
            ", branchID='" + getBranchID() + "'" +
            ", typeLedger=" + getTypeLedger() +
            ", directMaterialAmount=" + getDirectMaterialAmount() +
            ", directLaborAmount=" + getDirectLaborAmount() +
            ", machineMaterialAmount=" + getMachineMaterialAmount() +
            ", machineLaborAmount=" + getMachineLaborAmount() +
            ", machineToolsAmount=" + getMachineToolsAmount() +
            ", machineDepreciationAmount=" + getMachineDepreciationAmount() +
            ", machineServiceAmount=" + getMachineServiceAmount() +
            ", machineGeneralAmount=" + getMachineGeneralAmount() +
            ", generalMaterialAmount='" + getGeneralMaterialAmount() + "'" +
            ", generalLaborAmount=" + getGeneralLaborAmount() +
            ", generalToolsAmount=" + getGeneralToolsAmount() +
            ", generalDepreciationAmount=" + getGeneralDepreciationAmount() +
            ", generalServiceAmount=" + getGeneralServiceAmount() +
            ", otherGeneralAmount=" + getOtherGeneralAmount() +
            ", totalCostAmount=" + getTotalCostAmount() +
            "}";
    }
}
