package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ToolsDetailsTiAuditConvertDTO {
    private UUID toolsID;
    private String toolsName;
    private String toolsCode;
    private UUID unitID;
    private UUID departmentID;
    private UUID expenseItemID;
    private UUID statisticsCodeID;
    private BigDecimal quantity;
    private String costAccount;

    public ToolsDetailsTiAuditConvertDTO() {
    }

    // lấy danh sách tool khi xem màn kiểm kê ccdc
    public ToolsDetailsTiAuditConvertDTO(UUID toolsID, String toolsName, String toolsCode) {
        this.toolsID = toolsID;
        this.toolsName = toolsName;
        this.toolsCode = toolsCode;
    }

    public ToolsDetailsTiAuditConvertDTO(UUID toolsID, String toolsName, String toolsCode, UUID unitID, UUID departmentID, UUID expenseItemID, UUID statisticsCodeID, BigDecimal quantity, String costAccount) {
        this.toolsID = toolsID;
        this.toolsName = toolsName;
        this.toolsCode = toolsCode;
        this.unitID = unitID;
        this.departmentID = departmentID;
        this.expenseItemID = expenseItemID;
        this.statisticsCodeID = statisticsCodeID;
        this.quantity = quantity;
        this.costAccount = costAccount;
    }

    public UUID getToolsID() {
        return toolsID;
    }

    public void setToolsID(UUID toolsID) {
        this.toolsID = toolsID;
    }

    public String getToolsName() {
        return toolsName;
    }

    public void setToolsName(String toolsName) {
        this.toolsName = toolsName;
    }

    public String getToolsCode() {
        return toolsCode;
    }

    public void setToolsCode(String toolsCode) {
        this.toolsCode = toolsCode;
    }

    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }

    public UUID getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(UUID departmentID) {
        this.departmentID = departmentID;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public UUID getStatisticsCodeID() {
        return statisticsCodeID;
    }

    public void setStatisticsCodeID(UUID statisticsCodeID) {
        this.statisticsCodeID = statisticsCodeID;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getCostAccount() {
        return costAccount;
    }

    public void setCostAccount(String costAccount) {
        this.costAccount = costAccount;
    }
}
