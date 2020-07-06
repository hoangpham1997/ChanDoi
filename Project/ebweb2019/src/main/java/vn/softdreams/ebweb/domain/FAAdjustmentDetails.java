package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A FAAdjustmentDetails.
 */
@Entity
@Table(name = "faadjustmentdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FAAdjustmentDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "faadjustmentid")
    private UUID faAdjustmentID;

    @Column(name = "fixedassetid")
    private UUID fixedAssetID;

    @Column(name = "oldremainingamountoriginal")
    private BigDecimal oldRemainingAmountOriginal;

    @Column(name = "newremainingamount")
    private BigDecimal newRemainingAmount;

    @Column(name = "differremainingamount")
    private BigDecimal differRemainingAmount;

    @Column(name = "oldusedtime")
    private BigDecimal oldUsedTime;

    @Column(name = "newusedtime")
    private BigDecimal newUsedTime;

    @Column(name = "differusedtime")
    private BigDecimal differUsedTime;

    @Column(name = "oldacdepreciationamount")
    private BigDecimal oldAcDepreciationAmount;

    @Column(name = "newacdepreciationamount")
    private Integer newAcDepreciationAmount;

    @Column(name = "differacdepreciationamount")
    private BigDecimal differAcDepreciationAmount;

    @Column(name = "olddepreciationamount")
    private BigDecimal oldDepreciationAmount;

    @Column(name = "newdepreciationamount")
    private BigDecimal newDepreciationAmount;

    @Column(name = "differdepreciationamount")
    private BigDecimal differDepreciationAmount;

    @Column(name = "costaccount")
    private String costAccount;

    @Column(name = "adjustaccount")
    private String adjustAccount;

    @Column(name = "newmonthlydepreciationamount")
    private String newMonthlyDepreciationAmount;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFaAdjustmentID() {
        return faAdjustmentID;
    }

    public void setFaAdjustmentID(UUID faAdjustmentID) {
        this.faAdjustmentID = faAdjustmentID;
    }

    public UUID getFixedAssetID() {
        return fixedAssetID;
    }

    public void setFixedAssetID(UUID fixedAssetID) {
        this.fixedAssetID = fixedAssetID;
    }

    public BigDecimal getOldRemainingAmountOriginal() {
        return oldRemainingAmountOriginal;
    }

    public void setOldRemainingAmountOriginal(BigDecimal oldRemainingAmountOriginal) {
        this.oldRemainingAmountOriginal = oldRemainingAmountOriginal;
    }

    public BigDecimal getNewRemainingAmount() {
        return newRemainingAmount;
    }

    public void setNewRemainingAmount(BigDecimal newRemainingAmount) {
        this.newRemainingAmount = newRemainingAmount;
    }

    public BigDecimal getDifferRemainingAmount() {
        return differRemainingAmount;
    }

    public void setDifferRemainingAmount(BigDecimal differRemainingAmount) {
        this.differRemainingAmount = differRemainingAmount;
    }

    public BigDecimal getOldUsedTime() {
        return oldUsedTime;
    }

    public void setOldUsedTime(BigDecimal oldUsedTime) {
        this.oldUsedTime = oldUsedTime;
    }

    public BigDecimal getNewUsedTime() {
        return newUsedTime;
    }

    public void setNewUsedTime(BigDecimal newUsedTime) {
        this.newUsedTime = newUsedTime;
    }

    public BigDecimal getDifferUsedTime() {
        return differUsedTime;
    }

    public void setDifferUsedTime(BigDecimal differUsedTime) {
        this.differUsedTime = differUsedTime;
    }

    public BigDecimal getOldAcDepreciationAmount() {
        return oldAcDepreciationAmount;
    }

    public void setOldAcDepreciationAmount(BigDecimal oldAcDepreciationAmount) {
        this.oldAcDepreciationAmount = oldAcDepreciationAmount;
    }

    public Integer getNewAcDepreciationAmount() {
        return newAcDepreciationAmount;
    }

    public void setNewAcDepreciationAmount(Integer newAcDepreciationAmount) {
        this.newAcDepreciationAmount = newAcDepreciationAmount;
    }

    public BigDecimal getDifferAcDepreciationAmount() {
        return differAcDepreciationAmount;
    }

    public void setDifferAcDepreciationAmount(BigDecimal differAcDepreciationAmount) {
        this.differAcDepreciationAmount = differAcDepreciationAmount;
    }

    public BigDecimal getOldDepreciationAmount() {
        return oldDepreciationAmount;
    }

    public void setOldDepreciationAmount(BigDecimal oldDepreciationAmount) {
        this.oldDepreciationAmount = oldDepreciationAmount;
    }

    public BigDecimal getNewDepreciationAmount() {
        return newDepreciationAmount;
    }

    public void setNewDepreciationAmount(BigDecimal newDepreciationAmount) {
        this.newDepreciationAmount = newDepreciationAmount;
    }

    public BigDecimal getDifferDepreciationAmount() {
        return differDepreciationAmount;
    }

    public void setDifferDepreciationAmount(BigDecimal differDepreciationAmount) {
        this.differDepreciationAmount = differDepreciationAmount;
    }

    public String getCostAccount() {
        return costAccount;
    }

    public void setCostAccount(String costAccount) {
        this.costAccount = costAccount;
    }

    public String getAdjustAccount() {
        return adjustAccount;
    }

    public void setAdjustAccount(String adjustAccount) {
        this.adjustAccount = adjustAccount;
    }

    public String getNewMonthlyDepreciationAmount() {
        return newMonthlyDepreciationAmount;
    }

    public void setNewMonthlyDepreciationAmount(String newMonthlyDepreciationAmount) {
        this.newMonthlyDepreciationAmount = newMonthlyDepreciationAmount;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TIAdjustmentDetails tIAdjustmentDetails = (TIAdjustmentDetails) o;
        if (tIAdjustmentDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tIAdjustmentDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
