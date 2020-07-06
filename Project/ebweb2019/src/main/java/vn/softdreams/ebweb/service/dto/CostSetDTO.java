package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class CostSetDTO {

    private List<UUID> costSetIDs;
    private String fromDate;
    private String toDate;
    private UUID costSetID;
    private String costSetCode;
    private String costSetName;
    private BigDecimal revenueAmount;

    public CostSetDTO(UUID costSetID, String costSetCode, String costSetName, BigDecimal revenueAmount) {
        this.costSetID = costSetID;
        this.costSetCode = costSetCode;
        this.costSetName = costSetName;
        this.revenueAmount = revenueAmount;
    }

    public CostSetDTO() {
    }

    public List<UUID> getCostSetIDs() {
        return costSetIDs;
    }

    public void setCostSetIDs(List<UUID> costSetIDs) {
        this.costSetIDs = costSetIDs;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
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

    public BigDecimal getRevenueAmount() {
        return revenueAmount;
    }

    public void setRevenueAmount(BigDecimal revenueAmount) {
        this.revenueAmount = revenueAmount;
    }
}
