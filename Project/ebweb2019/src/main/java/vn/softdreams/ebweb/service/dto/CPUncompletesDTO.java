package vn.softdreams.ebweb.service.dto;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.UUID;

public class CPUncompletesDTO {
    private UUID id;
    private UUID cPPeriodID;
    private UUID quantumID;
    private String quantumCode;
    private String quantumName;
    private UUID costSetID;
    private String costSetCode;
    private Integer uncompleteType;
    private BigDecimal quantityClosing;
    private BigDecimal rate;

    public CPUncompletesDTO(UUID id, UUID cPPeriodID, UUID quantumID, String quantumCode, String quantumName, UUID costSetID, String costSetCode, Integer uncompleteType, BigDecimal quantityClosing, BigDecimal rate) {
        this.id = id;
        this.cPPeriodID = cPPeriodID;
        this.quantumID = quantumID;
        this.quantumCode = quantumCode;
        this.quantumName = quantumName;
        this.costSetID = costSetID;
        this.costSetCode = costSetCode;
        this.uncompleteType = uncompleteType;
        this.quantityClosing = quantityClosing;
        this.rate = rate;
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

    public UUID getQuantumID() {
        return quantumID;
    }

    public void setQuantumID(UUID quantumID) {
        this.quantumID = quantumID;
    }

    public String getQuantumCode() {
        return quantumCode;
    }

    public void setQuantumCode(String quantumCode) {
        this.quantumCode = quantumCode;
    }

    public String getQuantumName() {
        return quantumName;
    }

    public void setQuantumName(String quantumName) {
        this.quantumName = quantumName;
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

    public Integer getUncompleteType() {
        return uncompleteType;
    }

    public void setUncompleteType(Integer uncompleteType) {
        this.uncompleteType = uncompleteType;
    }

    public BigDecimal getQuantityClosing() {
        return quantityClosing;
    }

    public void setQuantityClosing(BigDecimal quantityClosing) {
        this.quantityClosing = quantityClosing;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}

