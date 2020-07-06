package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class TIAllocationAllocatedConvertDTO {
    private UUID id;
    private UUID tiAllocationID;
    private UUID toolID;
    private String description;
    private BigDecimal totalAllocationAmount;
    private BigDecimal allocationAmount;
    private BigDecimal remainingAmount;
    private Integer orderPriority;
    private String toolName;

    public TIAllocationAllocatedConvertDTO() {
    }

    // lấy dữ liệu lúc thêm mới
    public TIAllocationAllocatedConvertDTO(UUID toolID, String toolName, BigDecimal totalAllocationAmount, BigDecimal allocationAmount, BigDecimal remainingAmount) {
        this.toolID = toolID;
        this.totalAllocationAmount = totalAllocationAmount;
        this.allocationAmount = allocationAmount;
        this.remainingAmount = remainingAmount;
        this.toolName = toolName;
    }

    // lấy dữ liệu lúc xem
    public TIAllocationAllocatedConvertDTO(UUID id, UUID tiAllocationID, UUID toolID, String description, BigDecimal totalAllocationAmount, BigDecimal allocationAmount, BigDecimal remainingAmount, Integer orderPriority, String toolName) {
        this.id = id;
        this.tiAllocationID = tiAllocationID;
        this.toolID = toolID;
        this.description = description;
        this.totalAllocationAmount = totalAllocationAmount;
        this.allocationAmount = allocationAmount;
        this.remainingAmount = remainingAmount;
        this.orderPriority = orderPriority;
        this.toolName = toolName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTiAllocationID() {
        return tiAllocationID;
    }

    public void setTiAllocationID(UUID tiAllocationID) {
        this.tiAllocationID = tiAllocationID;
    }

    public UUID getToolID() {
        return toolID;
    }

    public void setToolID(UUID toolID) {
        this.toolID = toolID;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTotalAllocationAmount() {
        return totalAllocationAmount;
    }

    public void setTotalAllocationAmount(BigDecimal totalAllocationAmount) {
        this.totalAllocationAmount = totalAllocationAmount;
    }

    public BigDecimal getAllocationAmount() {
        return allocationAmount;
    }

    public void setAllocationAmount(BigDecimal allocationAmount) {
        this.allocationAmount = allocationAmount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }



}
