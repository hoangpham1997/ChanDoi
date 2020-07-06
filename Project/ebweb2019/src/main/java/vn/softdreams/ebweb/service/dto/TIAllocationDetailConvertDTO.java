package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.TIAllocationAllocated;
import vn.softdreams.ebweb.domain.TIAllocationPost;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TIAllocationDetailConvertDTO {
    private UUID id;
    private UUID tiAllocationID;
    private UUID toolsID;
    private String toolsName;
    private String toolsCode;
    private LocalDate postedDate;
    private String allocationAwaitAccount;
    private String description;
    private BigDecimal totalAllocationAmount;
    private BigDecimal allocationAmount;
    private BigDecimal remainingAmount;
    private Integer orderPriority;
    private List<ToolsDetailsConvertDTO> toolsDetailsConvertDTOS;
    private List<TIAllocationAllocated> tiAllocationAllocateds;
    private List<TIAllocationPost> tiAllocationPosts;
    public TIAllocationDetailConvertDTO() {
    }

    // lấy dữ liệu lúc thêm mới
    public TIAllocationDetailConvertDTO(UUID toolsID, String toolsName, String toolsCode, LocalDate postedDate, String allocationAwaitAccount, BigDecimal totalAllocationAmount, BigDecimal allocationAmount, BigDecimal remainingAmount) {
        this.toolsID = toolsID;
        this.postedDate = postedDate;
        this.allocationAwaitAccount = allocationAwaitAccount;
        this.totalAllocationAmount = totalAllocationAmount;
        this.allocationAmount = allocationAmount;
        this.remainingAmount = remainingAmount;
        this.toolsName = toolsName;
        this.toolsCode= toolsCode;
    }

    // lấy dữ liệu lúc xem
    public TIAllocationDetailConvertDTO(UUID id, UUID tiAllocationID, UUID toolsID, String description, BigDecimal totalAllocationAmount, BigDecimal allocationAmount, BigDecimal remainingAmount, Integer orderPriority, String toolsName) {
        this.id = id;
        this.tiAllocationID = tiAllocationID;
        this.toolsID = toolsID;
        this.description = description;
        this.totalAllocationAmount = totalAllocationAmount;
        this.allocationAmount = allocationAmount;
        this.remainingAmount = remainingAmount;
        this.orderPriority = orderPriority;
        this.toolsName = toolsName;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public String getAllocationAwaitAccount() {
        return allocationAwaitAccount;
    }

    public void setAllocationAwaitAccount(String allocationAwaitAccount) {
        this.allocationAwaitAccount = allocationAwaitAccount;
    }

    public List<ToolsDetailsConvertDTO> getToolsDetailsConvertDTOS() {
        return toolsDetailsConvertDTOS;
    }

    public void setToolsDetailsConvertDTOS(List<ToolsDetailsConvertDTO> toolsDetailsConvertDTOS) {
        this.toolsDetailsConvertDTOS = toolsDetailsConvertDTOS;
    }

    public List<TIAllocationAllocated> getTiAllocationAllocateds() {
        return tiAllocationAllocateds;
    }

    public void setTiAllocationAllocateds(List<TIAllocationAllocated> tiAllocationAllocateds) {
        this.tiAllocationAllocateds = tiAllocationAllocateds;
    }

    public List<TIAllocationPost> getTiAllocationPosts() {
        return tiAllocationPosts;
    }

    public void setTiAllocationPosts(List<TIAllocationPost> tiAllocationPosts) {
        this.tiAllocationPosts = tiAllocationPosts;
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
}
