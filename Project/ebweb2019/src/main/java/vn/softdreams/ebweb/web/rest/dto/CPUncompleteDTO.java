package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.CPAllocationGeneralExpenseDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CPUncompleteDTO {
    private UUID quantumID;
    private String quantumCode;
    private String quantumName;
    private BigDecimal quantityClosing;
    private BigDecimal rate;
    private UUID costSetID;
    private String costSetCode;
    private UUID materialGoodsID;

    public CPUncompleteDTO() {
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
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
}
