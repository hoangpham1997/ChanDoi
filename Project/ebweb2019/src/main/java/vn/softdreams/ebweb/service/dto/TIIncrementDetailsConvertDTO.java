package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TIIncrementDetailsConvertDTO {
    private UUID id;
    private UUID tiIncrementID;
    private UUID toolsID;
    private String toolCode;
    private String toolName;
    private String description;
    private UUID unitID;
    private String unitName;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal amount;
    private UUID accountingObjectID;
    private String accountingObjectCode;
    private String budgetItemID;
    private String budgetItemCode;
    private String costSetID;
    private String costSetCode;
    private String statisticCodeID;
    private String statisticsCode;
    private String departmentID;
    private String organizationUnitCode;
    private String expenseItemID;
    private String expenseItemCode;
    private Integer orderPriority;


    public TIIncrementDetailsConvertDTO() {
    }

    public TIIncrementDetailsConvertDTO(UUID id, UUID tiIncrementID, UUID toolsID, String toolCode, String toolName, String description, UUID unitID, String unitName, BigDecimal quantity, BigDecimal unitPrice, BigDecimal amount, UUID accountingObjectID, String accountingObjectCode, String budgetItemID, String budgetItemCode, String costSetID, String costSetCode, String statisticCodeID, String statisticsCode, String departmentID, String organizationUnitCode, String expenseItemID, String expenseItemCode, Integer orderPriority) {
        this.id = id;
        this.tiIncrementID = tiIncrementID;
        this.toolsID = toolsID;
        this.toolCode = toolCode;
        this.toolName = toolName;
        this.description = description;
        this.unitID = unitID;
        this.unitName = unitName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.amount = amount;
        this.accountingObjectID = accountingObjectID;
        this.accountingObjectCode = accountingObjectCode;
        this.budgetItemID = budgetItemID;
        this.budgetItemCode = budgetItemCode;
        this.costSetID = costSetID;
        this.costSetCode = costSetCode;
        this.statisticCodeID = statisticCodeID;
        this.statisticsCode = statisticsCode;
        this.departmentID = departmentID;
        this.organizationUnitCode = organizationUnitCode;
        this.expenseItemID = expenseItemID;
        this.expenseItemCode = expenseItemCode;
        this.orderPriority = orderPriority;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTiIncrementID() {
        return tiIncrementID;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public void setTiIncrementID(UUID tiIncrementID) {
        this.tiIncrementID = tiIncrementID;
    }

    public UUID getToolsID() {
        return toolsID;
    }

    public void setToolsID(UUID toolsID) {
        this.toolsID = toolsID;
    }

    public String getToolCode() {
        return toolCode;
    }

    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public String getAccountingObjectCode() {
        return accountingObjectCode;
    }

    public void setAccountingObjectCode(String accountingObjectCode) {
        this.accountingObjectCode = accountingObjectCode;
    }

    public String getBudgetItemID() {
        return budgetItemID;
    }

    public void setBudgetItemID(String budgetItemID) {
        this.budgetItemID = budgetItemID;
    }

    public String getBudgetItemCode() {
        return budgetItemCode;
    }

    public void setBudgetItemCode(String budgetItemCode) {
        this.budgetItemCode = budgetItemCode;
    }

    public String getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(String costSetID) {
        this.costSetID = costSetID;
    }

    public String getCostSetCode() {
        return costSetCode;
    }

    public void setCostSetCode(String costSetCode) {
        this.costSetCode = costSetCode;
    }

    public String getStatisticCodeID() {
        return statisticCodeID;
    }

    public void setStatisticCodeID(String statisticCodeID) {
        this.statisticCodeID = statisticCodeID;
    }

    public String getStatisticsCode() {
        return statisticsCode;
    }

    public void setStatisticsCode(String statisticsCode) {
        this.statisticsCode = statisticsCode;
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }

    public String getOrganizationUnitCode() {
        return organizationUnitCode;
    }

    public void setOrganizationUnitCode(String organizationUnitCode) {
        this.organizationUnitCode = organizationUnitCode;
    }

    public String getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(String expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public String getExpenseItemCode() {
        return expenseItemCode;
    }

    public void setExpenseItemCode(String expenseItemCode) {
        this.expenseItemCode = expenseItemCode;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

}
