package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A FADecrementDetails.
 */
@Entity
@Table(name = "fadecrementdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FaDecrementDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "fadecrementid")
    private UUID faDecrementID;

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

    @Column(name = "originalpriceaccount")
    private String originalPriceAccount;

    @Column(name = "depreciationaccount")
    private String depreciationAccount;

    @Column(name = "expenditureaccount")
    private String expenditureAccount;

    @Column(name = "remainingaccount")
    private String remainingAccount;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "fadecrementdetailid")
    private Set<FADecrementDetailPost> faDecrementDetailPosts = new HashSet<>();

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

    public UUID getFaDecrementID() {
        return faDecrementID;
    }

    public void setFaDecrementID(UUID faDecrementID) {
        this.faDecrementID = faDecrementID;
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

    public String getOriginalPriceAccount() {
        return originalPriceAccount;
    }

    public void setOriginalPriceAccount(String originalPriceAccount) {
        this.originalPriceAccount = originalPriceAccount;
    }

    public String getDepreciationAccount() {
        return depreciationAccount;
    }

    public void setDepreciationAccount(String depreciationAccount) {
        this.depreciationAccount = depreciationAccount;
    }

    public String getExpenditureAccount() {
        return expenditureAccount;
    }

    public void setExpenditureAccount(String expenditureAccount) {
        this.expenditureAccount = expenditureAccount;
    }

    public String getRemainingAccount() {
        return remainingAccount;
    }

    public void setRemainingAccount(String remainingAccount) {
        this.remainingAccount = remainingAccount;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public Set<FADecrementDetailPost> getFaDecrementDetailPosts() {
        return faDecrementDetailPosts;
    }

    public void setFaDecrementDetailPosts(Set<FADecrementDetailPost> faDecrementDetailPosts) {
        if (this.faDecrementDetailPosts == null) {
            this.faDecrementDetailPosts = faDecrementDetailPosts;
        } else if (this.faDecrementDetailPosts != faDecrementDetailPosts) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
            this.faDecrementDetailPosts.clear();
            if (faDecrementDetailPosts != null) {
                this.faDecrementDetailPosts.addAll(faDecrementDetailPosts);
            }
        }
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
        FaDecrementDetails tIDecrementDetails = (FaDecrementDetails) o;
        if (tIDecrementDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tIDecrementDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
