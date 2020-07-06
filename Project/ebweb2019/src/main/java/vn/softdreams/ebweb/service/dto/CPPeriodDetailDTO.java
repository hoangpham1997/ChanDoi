package vn.softdreams.ebweb.service.dto;
import java.util.UUID;

public class CPPeriodDetailDTO {
    private UUID id;
    private UUID cPPeriodID;
    private UUID costSetID;
    private String costSetCode;
    private String costSetName;
    private Integer costSetType;
    private UUID contractID;

    public CPPeriodDetailDTO(UUID id, UUID cPPeriodID, UUID costSetID, String costSetCode, String costSetName, Integer costSetType) {
        this.id = id;
        this.cPPeriodID = cPPeriodID;
        this.costSetID = costSetID;
        this.costSetCode = costSetCode;
        this.costSetName = costSetName;
        this.costSetType = costSetType;
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

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public String getCostSetCode() {
        return costSetCode;
    }

    public void setCostSetCode(String costSetCode) {
        this.costSetCode = costSetCode;
    }

    public String getCostSetName() {
        return costSetName;
    }

    public void setCostSetName(String costSetName) {
        this.costSetName = costSetName;
    }

    public Integer getCostSetType() {
        return costSetType;
    }

    public void setCostSetType(Integer costSetType) {
        this.costSetType = costSetType;
    }

    public UUID getContractID() {
        return contractID;
    }

    public void setContractID(UUID contractID) {
        this.contractID = contractID;
    }
}

