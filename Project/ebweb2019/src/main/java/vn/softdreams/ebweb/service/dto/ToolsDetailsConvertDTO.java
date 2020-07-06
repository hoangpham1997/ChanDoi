package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ToolsDetailsConvertDTO {
    private UUID id;
    private UUID toolsID;
    private UUID toolsCode;
    private UUID toolsName;
    private UUID objectID;
    private Integer objectType;
    private BigDecimal quantity;
    private BigDecimal rate;
    private String costAccount;
    private UUID expenseItemID;
    private UUID statisticsCodeID;
    private Integer orderPriority;

    public ToolsDetailsConvertDTO() {
    }

    public ToolsDetailsConvertDTO(UUID id, UUID toolsID, UUID objectID, Integer objectType, BigDecimal quantity, BigDecimal rate, String costAccount, UUID expenseItemID, UUID statisticsCodeID, Integer orderPriority) {
        this.id = id;
        this.toolsID = toolsID;
        this.objectID = objectID;
        this.objectType = objectType;
        this.quantity = quantity;
        this.rate = rate;
        this.costAccount = costAccount;
        this.expenseItemID = expenseItemID;
        this.statisticsCodeID = statisticsCodeID;
        this.orderPriority = orderPriority;
    }

    public String getCostAccount() {
        return costAccount;
    }

    public void setCostAccount(String costAccount) {
        this.costAccount = costAccount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getToolsID() {
        return toolsID;
    }

    public void setToolsID(UUID toolsID) {
        this.toolsID = toolsID;
    }

    public UUID getObjectID() {
        return objectID;
    }

    public void setObjectID(UUID objectID) {
        this.objectID = objectID;
    }

    public Integer getObjectType() {
        return objectType;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
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

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

}
