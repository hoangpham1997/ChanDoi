package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.CPOPNSDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A CPOPN.
 */
@Entity
@Table(name = "cpopn")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "CPOPNSDTO",
        classes = {
            @ConstructorResult(
                targetClass = CPOPNSDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "objectType", type = Integer.class),
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "costSetName", type = String.class),
                    @ColumnResult(name = "contractID", type = UUID.class),
                    @ColumnResult(name = "contractCode", type = String.class),
                    @ColumnResult(name = "signedDate", type = LocalDate.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
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
                    @ColumnResult(name = "acceptedAmount", type = BigDecimal.class),
                    @ColumnResult(name = "notAcceptedAmount", type = BigDecimal.class),
                    @ColumnResult(name = "uncompletedAccount", type = String.class),
                    @ColumnResult(name = "objectID", type = UUID.class),
                }
            )
        }
    )})
public class CPOPN implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @SequenceGenerator(name = "sequenceGenerator")
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Column(name = "objecttype")
    private Integer objectType;

    @Column(name = "costsetid")
    private UUID costSetID;

    @Column(name = "contractid")
    private UUID contractID;

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

    @Column(name = "acceptedamount", precision = 10, scale = 2)
    private BigDecimal acceptedAmount;

    @Column(name = "notacceptedamount", precision = 10, scale = 2)
    private BigDecimal notAcceptedAmount;

    @Column(name = "uncompletedaccount")
    private String uncompletedAccount;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public BigDecimal getDirectMaterialAmount() {
        return directMaterialAmount;
    }

    public void setDirectMaterialAmount(BigDecimal directMaterialAmount) {
        this.directMaterialAmount = directMaterialAmount;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public UUID getContractID() {
        return contractID;
    }

    public void setContractID(UUID contractID) {
        this.contractID = contractID;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public CPOPN typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public Integer getObjectType() {
        return objectType;
    }

    public CPOPN objectType(Integer objectType) {
        this.objectType = objectType;
        return this;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }



    public BigDecimal getDirectLaborAmount() {
        return directLaborAmount;
    }

    public CPOPN directLaborAmount(BigDecimal directLaborAmount) {
        this.directLaborAmount = directLaborAmount;
        return this;
    }

    public void setDirectLaborAmount(BigDecimal directLaborAmount) {
        this.directLaborAmount = directLaborAmount;
    }

    public BigDecimal getMachineMaterialAmount() {
        return machineMaterialAmount;
    }

    public CPOPN machineMaterialAmount(BigDecimal machineMaterialAmount) {
        this.machineMaterialAmount = machineMaterialAmount;
        return this;
    }

    public void setMachineMaterialAmount(BigDecimal machineMaterialAmount) {
        this.machineMaterialAmount = machineMaterialAmount;
    }

    public BigDecimal getMachineLaborAmount() {
        return machineLaborAmount;
    }

    public CPOPN machineLaborAmount(BigDecimal machineLaborAmount) {
        this.machineLaborAmount = machineLaborAmount;
        return this;
    }

    public void setMachineLaborAmount(BigDecimal machineLaborAmount) {
        this.machineLaborAmount = machineLaborAmount;
    }

    public BigDecimal getMachineToolsAmount() {
        return machineToolsAmount;
    }

    public CPOPN machineToolsAmount(BigDecimal machineToolsAmount) {
        this.machineToolsAmount = machineToolsAmount;
        return this;
    }

    public void setMachineToolsAmount(BigDecimal machineToolsAmount) {
        this.machineToolsAmount = machineToolsAmount;
    }

    public BigDecimal getMachineDepreciationAmount() {
        return machineDepreciationAmount;
    }

    public CPOPN machineDepreciationAmount(BigDecimal machineDepreciationAmount) {
        this.machineDepreciationAmount = machineDepreciationAmount;
        return this;
    }

    public void setMachineDepreciationAmount(BigDecimal machineDepreciationAmount) {
        this.machineDepreciationAmount = machineDepreciationAmount;
    }

    public BigDecimal getMachineServiceAmount() {
        return machineServiceAmount;
    }

    public CPOPN machineServiceAmount(BigDecimal machineServiceAmount) {
        this.machineServiceAmount = machineServiceAmount;
        return this;
    }

    public void setMachineServiceAmount(BigDecimal machineServiceAmount) {
        this.machineServiceAmount = machineServiceAmount;
    }

    public BigDecimal getMachineGeneralAmount() {
        return machineGeneralAmount;
    }

    public CPOPN machineGeneralAmount(BigDecimal machineGeneralAmount) {
        this.machineGeneralAmount = machineGeneralAmount;
        return this;
    }

    public void setMachineGeneralAmount(BigDecimal machineGeneralAmount) {
        this.machineGeneralAmount = machineGeneralAmount;
    }

    public BigDecimal getGeneralMaterialAmount() {
        return generalMaterialAmount;
    }

    public CPOPN generalMaterialAmount(BigDecimal generalMaterialAmount) {
        this.generalMaterialAmount = generalMaterialAmount;
        return this;
    }

    public void setGeneralMaterialAmount(BigDecimal generalMaterialAmount) {
        this.generalMaterialAmount = generalMaterialAmount;
    }

    public BigDecimal getGeneralLaborAmount() {
        return generalLaborAmount;
    }

    public CPOPN generalLaborAmount(BigDecimal generalLaborAmount) {
        this.generalLaborAmount = generalLaborAmount;
        return this;
    }

    public void setGeneralLaborAmount(BigDecimal generalLaborAmount) {
        this.generalLaborAmount = generalLaborAmount;
    }

    public BigDecimal getGeneralToolsAmount() {
        return generalToolsAmount;
    }

    public CPOPN generalToolsAmount(BigDecimal generalToolsAmount) {
        this.generalToolsAmount = generalToolsAmount;
        return this;
    }

    public void setGeneralToolsAmount(BigDecimal generalToolsAmount) {
        this.generalToolsAmount = generalToolsAmount;
    }

    public BigDecimal getGeneralDepreciationAmount() {
        return generalDepreciationAmount;
    }

    public CPOPN generalDepreciationAmount(BigDecimal generalDepreciationAmount) {
        this.generalDepreciationAmount = generalDepreciationAmount;
        return this;
    }

    public void setGeneralDepreciationAmount(BigDecimal generalDepreciationAmount) {
        this.generalDepreciationAmount = generalDepreciationAmount;
    }

    public BigDecimal getGeneralServiceAmount() {
        return generalServiceAmount;
    }

    public CPOPN generalServiceAmount(BigDecimal generalServiceAmount) {
        this.generalServiceAmount = generalServiceAmount;
        return this;
    }

    public void setGeneralServiceAmount(BigDecimal generalServiceAmount) {
        this.generalServiceAmount = generalServiceAmount;
    }

    public BigDecimal getOtherGeneralAmount() {
        return otherGeneralAmount;
    }

    public CPOPN otherGeneralAmount(BigDecimal otherGeneralAmount) {
        this.otherGeneralAmount = otherGeneralAmount;
        return this;
    }

    public void setOtherGeneralAmount(BigDecimal otherGeneralAmount) {
        this.otherGeneralAmount = otherGeneralAmount;
    }

    public BigDecimal getTotalCostAmount() {
        return totalCostAmount;
    }

    public CPOPN totalCostAmount(BigDecimal totalCostAmount) {
        this.totalCostAmount = totalCostAmount;
        return this;
    }

    public void setTotalCostAmount(BigDecimal totalCostAmount) {
        this.totalCostAmount = totalCostAmount;
    }

    public BigDecimal getAcceptedAmount() {
        return acceptedAmount;
    }

    public CPOPN acceptedAmount(BigDecimal acceptedAmount) {
        this.acceptedAmount = acceptedAmount;
        return this;
    }

    public void setAcceptedAmount(BigDecimal acceptedAmount) {
        this.acceptedAmount = acceptedAmount;
    }

    public BigDecimal getNotAcceptedAmount() {
        return notAcceptedAmount;
    }

    public CPOPN notAcceptedAmount(BigDecimal notAcceptedAmount) {
        this.notAcceptedAmount = notAcceptedAmount;
        return this;
    }

    public void setNotAcceptedAmount(BigDecimal notAcceptedAmount) {
        this.notAcceptedAmount = notAcceptedAmount;
    }

    public String getUncompletedAccount() {
        return uncompletedAccount;
    }

    public CPOPN uncompletedAccount(String uncompletedAccount) {
        this.uncompletedAccount = uncompletedAccount;
        return this;
    }

    public void setUncompletedAccount(String uncompletedAccount) {
        this.uncompletedAccount = uncompletedAccount;
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
        CPOPN cPOPN = (CPOPN) o;
        if (cPOPN.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cPOPN.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CPOPN{" +
            "id=" + getId() +
            ", companyID='" + getCompanyID() + "'" +
            ", branchID='" + getBranchID() + "'" +
            ", typeLedger=" + getTypeLedger() +
            ", objectType=" + getObjectType() +
            ", costSetID='" + getCostSetID() + "'" +
            ", contractID='" + getContractID() + "'" +
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
            ", acceptedAmount=" + getAcceptedAmount() +
            ", notAcceptedAmount=" + getNotAcceptedAmount() +
            ", uncompletedAccount='" + getUncompletedAccount() + "'" +
            "}";
    }
}
