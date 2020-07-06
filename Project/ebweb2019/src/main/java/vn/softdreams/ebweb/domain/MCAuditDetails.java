package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A MCAuditDetails.
 */
@Entity
@Table(name = "mcauditdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MCAuditDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "description")
    private String description;

    @Column(name = "orderpriority", insertable = false, updatable = false)
    private Integer orderPriority;

    @Column(name = "valueofmoney")
    private BigDecimal valueOfMoney;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "mcauditid")
    private UUID mCAuditId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public MCAuditDetails quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public MCAuditDetails description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public MCAuditDetails orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public BigDecimal getValueOfMoney() {
        return valueOfMoney;
    }

    public MCAuditDetails valueOfMoney(BigDecimal valueOfMoney) {
        this.valueOfMoney = valueOfMoney;
        return this;
    }

    public void setValueOfMoney(BigDecimal valueOfMoney) {
        this.valueOfMoney = valueOfMoney;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UUID getmCAuditId() {
        return mCAuditId;
    }

    public void setmCAuditId(UUID mCAuditId) {
        this.mCAuditId = mCAuditId;
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
        MCAuditDetails mCAuditDetails = (MCAuditDetails) o;
        if (mCAuditDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mCAuditDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MCAuditDetails{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", description='" + getDescription() + "'" +
            ", orderPriority=" + getOrderPriority() +
            ", valueOfMoney=" + getValueOfMoney() +
            "}";
    }
}
