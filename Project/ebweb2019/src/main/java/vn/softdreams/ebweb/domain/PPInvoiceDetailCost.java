package vn.softdreams.ebweb.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.PPOrderDTO;
import vn.softdreams.ebweb.web.rest.dto.PPInvoiceDetailCostDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * A PPOrder.
 */
@Entity
@Table(name = "ppinvoicedetailcost")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)

@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "PPInvoiceDetailCostDTO",
                classes = {
                        @ConstructorResult(
                                targetClass = PPInvoiceDetailCostDTO.class,
                                columns = {
                                        @ColumnResult(name = "id", type = UUID.class),
                                        @ColumnResult(name = "costType", type = Boolean.class),
                                        @ColumnResult(name = "refID", type = UUID.class),
                                        @ColumnResult(name = "typeID", type = Integer.class),
                                        @ColumnResult(name = "ppServiceID", type = UUID.class),
                                        @ColumnResult(name = "accountObjectID", type = UUID.class),
                                        @ColumnResult(name = "totalFreightAmount", type = BigDecimal.class),
                                        @ColumnResult(name = "amount", type = BigDecimal.class),
                                        @ColumnResult(name = "accumulatedAllocateAmount", type = BigDecimal.class),
                                        @ColumnResult(name = "totalFreightAmountOriginal", type = BigDecimal.class),
                                        @ColumnResult(name = "amountOriginal", type = BigDecimal.class),
                                        @ColumnResult(name = "accumulatedAllocateAmountOriginal", type = BigDecimal.class),
                                        @ColumnResult(name = "templateID", type = UUID.class),
                                        @ColumnResult(name = "orderPriority", type = Integer.class),
                                        @ColumnResult(name = "accountingObjectName", type = String.class),
                                        @ColumnResult(name = "accountingObjectID", type = UUID.class),
                                        @ColumnResult(name = "date", type = LocalDate.class),
                                        @ColumnResult(name = "postedDate", type = LocalDate.class),
                                        @ColumnResult(name = "noMBook", type = String.class),
                                        @ColumnResult(name = "noFBook", type = String.class)
                                })
                })
})

public class PPInvoiceDetailCost implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "costtype")
    private Boolean costType;

    @Column(name = "refid")
    private UUID refID;

    @Column(name = "typeid")
    private Integer typeID;

    @Column(name = "ppserviceid")
    private UUID ppServiceID;

    @Column(name = "accountobjectid")
    private UUID accountObjectID;

    @Column(name = "totalfreightamount")
    private BigDecimal totalFreightAmount;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "accumulatedallocateamount")
    private BigDecimal accumulatedAllocateAmount;

    @Column(name = "totalfreightamountoriginal")
    private BigDecimal totalFreightAmountOriginal;

    @Column(name = "amountoriginal")
    private BigDecimal amountOriginal;

    @Column(name = "accumulatedallocateamountoriginal")
    private BigDecimal accumulatedAllocateAmountOriginal;

    @Column(name = "templateid")
    private UUID templateID;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    public PPInvoiceDetailCost() {
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
}
