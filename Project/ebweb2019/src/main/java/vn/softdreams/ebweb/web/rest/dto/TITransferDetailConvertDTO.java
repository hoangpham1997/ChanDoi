package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class TITransferDetailConvertDTO {
    private UUID id;
    private UUID tiTransferID;
    private UUID toolsID;
    private String description;
    private UUID fromDepartmentID;
    private UUID toDepartmentID;
    private BigDecimal quantity;
    private BigDecimal transferQuantity;
    private String costAccount;
    private UUID budgetItemID;
    private UUID costSetID;
    private UUID statisticCodeID;
    private UUID expenseItemID;
    private Integer orderPriority;
    private String toolsCode;
    private String toolsName;
    private String fromDepartmentCode;
    private String toDepartmentCode;
    private String  budgetItemCode;
    private String  costSetCode;
    private String statisticsCode;
    private String  expenseItemCode;

    public TITransferDetailConvertDTO() {
    }

    public TITransferDetailConvertDTO(UUID id, UUID tiTransferID, UUID toolsID, String description, UUID fromDepartmentID, UUID toDepartmentID, BigDecimal quantity, BigDecimal transferQuantity, String costAccount, UUID budgetItemID, UUID costSetID, UUID statisticCodeID, UUID expenseItemID, Integer orderPriority, String toolsCode, String toolsName, String fromDepartmentCode, String toDepartmentCode, String budgetItemCode, String costSetCode, String statisticsCode, String expenseItemCode) {
        this.id = id;
        this.tiTransferID = tiTransferID;
        this.toolsID = toolsID;
        this.description = description;
        this.fromDepartmentID = fromDepartmentID;
        this.toDepartmentID = toDepartmentID;
        this.quantity = quantity;
        this.transferQuantity = transferQuantity;
        this.costAccount = costAccount;
        this.budgetItemID = budgetItemID;
        this.costSetID = costSetID;
        this.statisticCodeID = statisticCodeID;
        this.expenseItemID = expenseItemID;
        this.orderPriority = orderPriority;
        this.toolsCode = toolsCode;
        this.toolsName = toolsName;
        this.fromDepartmentCode = fromDepartmentCode;
        this.toDepartmentCode = toDepartmentCode;
        this.budgetItemCode = budgetItemCode;
        this.costSetCode = costSetCode;
        this.statisticsCode = statisticsCode;
        this.expenseItemCode = expenseItemCode;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTiTransferID() {
        return tiTransferID;
    }

    public void setTiTransferID(UUID tiTransferID) {
        this.tiTransferID = tiTransferID;
    }

    public UUID getToolsID() {
        return toolsID;
    }

    public void setToolsID(UUID toolsID) {
        this.toolsID = toolsID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getFromDepartmentID() {
        return fromDepartmentID;
    }

    public void setFromDepartmentID(UUID fromDepartmentID) {
        this.fromDepartmentID = fromDepartmentID;
    }

    public UUID getToDepartmentID() {
        return toDepartmentID;
    }

    public void setToDepartmentID(UUID toDepartmentID) {
        this.toDepartmentID = toDepartmentID;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTransferQuantity() {
        return transferQuantity;
    }

    public void setTransferQuantity(BigDecimal transferQuantity) {
        this.transferQuantity = transferQuantity;
    }

    public String getCostAccount() {
        return costAccount;
    }

    public void setCostAccount(String costAccount) {
        this.costAccount = costAccount;
    }

    public UUID getBudgetItemID() {
        return budgetItemID;
    }

    public void setBudgetItemID(UUID budgetItemID) {
        this.budgetItemID = budgetItemID;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public UUID getStatisticCodeID() {
        return statisticCodeID;
    }

    public void setStatisticCodeID(UUID statisticCodeID) {
        this.statisticCodeID = statisticCodeID;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getToolsCode() {
        return toolsCode;
    }

    public void setToolsCode(String toolsCode) {
        this.toolsCode = toolsCode;
    }

    public String getToolsName() {
        return toolsName;
    }

    public void setToolsName(String toolsName) {
        this.toolsName = toolsName;
    }

    public String getFromDepartmentCode() {
        return fromDepartmentCode;
    }

    public void setFromDepartmentCode(String fromDepartmentCode) {
        this.fromDepartmentCode = fromDepartmentCode;
    }

    public String getToDepartmentCode() {
        return toDepartmentCode;
    }

    public void setToDepartmentCode(String toDepartmentCode) {
        this.toDepartmentCode = toDepartmentCode;
    }

    public String getBudgetItemCode() {
        return budgetItemCode;
    }

    public void setBudgetItemCode(String budgetItemCode) {
        this.budgetItemCode = budgetItemCode;
    }

    public String getCostSetCode() {
        return costSetCode;
    }

    public void setCostSetCode(String costSetCode) {
        this.costSetCode = costSetCode;
    }

    public String getStatisticsCode() {
        return statisticsCode;
    }

    public void setStatisticsCode(String statisticsCode) {
        this.statisticsCode = statisticsCode;
    }

    public String getExpenseItemCode() {
        return expenseItemCode;
    }

    public void setExpenseItemCode(String expenseItemCode) {
        this.expenseItemCode = expenseItemCode;
    }
}
