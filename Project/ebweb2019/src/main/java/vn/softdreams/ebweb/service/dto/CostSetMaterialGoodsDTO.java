package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class CostSetMaterialGoodsDTO {

    private UUID quantumID;
    private String quantumCode;
    private String quantumName;
    private BigDecimal quantityClosing;
    private BigDecimal rate;
    private UUID costSetID;
    private String costSetCode;

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

    public CostSetMaterialGoodsDTO() {
    }

    public CostSetMaterialGoodsDTO(UUID quantumID, String quantumCode, String quantumName, BigDecimal quantityClosing, BigDecimal rate, UUID costSetID, String costSetCode) {
        this.quantumID = quantumID;
        this.quantumCode = quantumCode;
        this.quantumName = quantumName;
        this.quantityClosing = quantityClosing;
        this.rate = rate;
        this.costSetID = costSetID;
        this.costSetCode = costSetCode;
    }

    public CostSetMaterialGoodsDTO(UUID quantumID, UUID costSetID) {
        this.quantumID = quantumID;
        this.costSetID = costSetID;
    }
}
