package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class FAIncrementDetailsConvertDTO {
    private UUID id;
    private UUID faIncrementID;
    private UUID fixedAssetID;
    private String fixedAssetCode;
    private String fixedAssetName;
    private String description;
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

    public FAIncrementDetailsConvertDTO() {
    }

    public FAIncrementDetailsConvertDTO(UUID id, UUID faIncrementID, UUID fixedAssetID, String fixedAssetCode, String fixedAssetName, String description, BigDecimal amount, UUID accountingObjectID, String accountingObjectCode, String budgetItemID, String budgetItemCode, String costSetID, String costSetCode, String statisticCodeID, String statisticsCode, String departmentID, String organizationUnitCode, String expenseItemID, String expenseItemCode, Integer orderPriority) {
        this.id = id;
        this.faIncrementID = faIncrementID;
        this.fixedAssetID = fixedAssetID;
        this.fixedAssetCode = fixedAssetCode;
        this.fixedAssetName = fixedAssetName;
        this.description = description;
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

    public UUID getFaIncrementID() {
        return faIncrementID;
    }

    public void setFaIncrementID(UUID faIncrementID) {
        this.faIncrementID = faIncrementID;
    }

    public UUID getFixedAssetID() {
        return fixedAssetID;
    }

    public void setFixedAssetID(UUID fixedAssetID) {
        this.fixedAssetID = fixedAssetID;
    }

    public String getFixedAssetCode() {
        return fixedAssetCode;
    }

    public void setFixedAssetCode(String fixedAssetCode) {
        this.fixedAssetCode = fixedAssetCode;
    }

    public String getFixedAssetName() {
        return fixedAssetName;
    }

    public void setFixedAssetName(String fixedAssetName) {
        this.fixedAssetName = fixedAssetName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
