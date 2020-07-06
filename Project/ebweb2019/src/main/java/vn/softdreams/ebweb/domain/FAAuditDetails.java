package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A FAAuditDetails.
 */
@Entity
@Table(name = "faauditdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FAAuditDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "faauditid")
    private UUID faAuditID;

    @Column(name = "fixedassetid")
    private UUID fixedAssetID;

    @Column(name = "departmentid")
    private UUID departmentID;

    @Column(name = "originalprice")
    private BigDecimal originalPrice;

    @Column(name = "depreciationamount")
    private BigDecimal depreciationAmount;

    @Column(name = "acdepreciationamount")
    private BigDecimal acDepreciationAmount;

    @Column(name = "remainingamount")
    private BigDecimal remainingAmount;

    @Column(name = "existinstock")
    private Integer existInStock;

    @Column(name = "recommendation")
    private Integer recommendation;

    @Column(name = "note")
    private String note;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFaAuditID() {
        return faAuditID;
    }

    public void setFaAuditID(UUID faAuditID) {
        this.faAuditID = faAuditID;
    }

    public UUID getFixedAssetID() {
        return fixedAssetID;
    }

    public void setFixedAssetID(UUID fixedAssetID) {
        this.fixedAssetID = fixedAssetID;
    }

    public UUID getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(UUID departmentID) {
        this.departmentID = departmentID;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getDepreciationAmount() {
        return depreciationAmount;
    }

    public void setDepreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    public BigDecimal getAcDepreciationAmount() {
        return acDepreciationAmount;
    }

    public void setAcDepreciationAmount(BigDecimal acDepreciationAmount) {
        this.acDepreciationAmount = acDepreciationAmount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public Integer getExistInStock() {
        return existInStock;
    }

    public void setExistInStock(Integer existInStock) {
        this.existInStock = existInStock;
    }

    public Integer getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(Integer recommendation) {
        this.recommendation = recommendation;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
        TIAuditDetails tIAuditDetails = (TIAuditDetails) o;
        if (tIAuditDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tIAuditDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
