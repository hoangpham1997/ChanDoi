package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class PPInvoiceDetailCostDTO {
    private UUID id;
    private Boolean costType;
    private UUID refID;
    private Integer typeID;
    private UUID ppServiceID;
    private UUID accountObjectID;
    private BigDecimal totalFreightAmount;
    private BigDecimal amount;
    private BigDecimal accumulatedAllocateAmount;
    private BigDecimal totalFreightAmountOriginal;
    private BigDecimal amountOriginal;
    private BigDecimal accumulatedAllocateAmountOriginal;
    private UUID templateID;
    private Integer orderPriority;
    private String accountingObjectName;
    private UUID accountingObjectID;
    private LocalDate date;
    private LocalDate postedDate;
    private String noMBook;
    private String noFBook;

    public PPInvoiceDetailCostDTO() {
    }

    public PPInvoiceDetailCostDTO(UUID id, Boolean costType, UUID refID, Integer typeID, UUID ppServiceID, UUID accountObjectID, BigDecimal totalFreightAmount, BigDecimal amount, BigDecimal accumulatedAllocateAmount, BigDecimal totalFreightAmountOriginal, BigDecimal amountOriginal, BigDecimal accumulatedAllocateAmountOriginal, UUID templateID, Integer orderPriority, String accountingObjectName, UUID accountingObjectID, LocalDate date, LocalDate postedDate, String noMBook, String noFBook) {
        this.id = id;
        this.costType = costType;
        this.refID = refID;
        this.typeID = typeID;
        this.ppServiceID = ppServiceID;
        this.accountObjectID = accountObjectID;
        this.totalFreightAmount = totalFreightAmount;
        this.amount = amount;
        this.accumulatedAllocateAmount = accumulatedAllocateAmount;
        this.totalFreightAmountOriginal = totalFreightAmountOriginal;
        this.amountOriginal = amountOriginal;
        this.accumulatedAllocateAmountOriginal = accumulatedAllocateAmountOriginal;
        this.templateID = templateID;
        this.orderPriority = orderPriority;
        this.accountingObjectName = accountingObjectName;
        this.accountingObjectID = accountingObjectID;
        this.date = date;
        this.postedDate = postedDate;
        this.noMBook = noMBook;
        this.noFBook = noFBook;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getCostType() {
        return costType;
    }

    public void setCostType(Boolean costType) {
        this.costType = costType;
    }

    public UUID getRefID() {
        return refID;
    }

    public void setRefID(UUID refID) {
        this.refID = refID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public UUID getPpServiceID() {
        return ppServiceID;
    }

    public void setPpServiceID(UUID ppServiceID) {
        this.ppServiceID = ppServiceID;
    }

    public UUID getAccountObjectID() {
        return accountObjectID;
    }

    public void setAccountObjectID(UUID accountObjectID) {
        this.accountObjectID = accountObjectID;
    }

    public BigDecimal getTotalFreightAmount() {
        return totalFreightAmount;
    }

    public void setTotalFreightAmount(BigDecimal totalFreightAmount) {
        this.totalFreightAmount = totalFreightAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAccumulatedAllocateAmount() {
        return accumulatedAllocateAmount;
    }

    public void setAccumulatedAllocateAmount(BigDecimal accumulatedAllocateAmount) {
        this.accumulatedAllocateAmount = accumulatedAllocateAmount;
    }

    public BigDecimal getTotalFreightAmountOriginal() {
        return totalFreightAmountOriginal;
    }

    public void setTotalFreightAmountOriginal(BigDecimal totalFreightAmountOriginal) {
        this.totalFreightAmountOriginal = totalFreightAmountOriginal;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public BigDecimal getAccumulatedAllocateAmountOriginal() {
        return accumulatedAllocateAmountOriginal;
    }

    public void setAccumulatedAllocateAmountOriginal(BigDecimal accumulatedAllocateAmountOriginal) {
        this.accumulatedAllocateAmountOriginal = accumulatedAllocateAmountOriginal;
    }

    public UUID getTemplateID() {
        return templateID;
    }

    public void setTemplateID(UUID templateID) {
        this.templateID = templateID;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }
}
