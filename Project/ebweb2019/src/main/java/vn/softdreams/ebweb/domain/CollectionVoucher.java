package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * A CollectionVoucher.
 */
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CollectionVoucher implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private Boolean status;

    private Integer typeId;

    private Date date;

    private String no;

    private String accountObject;

    private Double rate;

    private Double amount;

    private Double amountOriginal;

    private String description;

    private UUID bankAccountDetailId;

    private Date matchDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getAccountingObjectName() {
        return accountObject;
    }

    public void setAccountingObjectName(String accountObject) {
        this.accountObject = accountObject;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmountOriginal() {
        return amountOriginal;
    }

    public void setAmountOriginal(Double amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getBankAccountDetailId() {
        return bankAccountDetailId;
    }

    public void setBankAccountDetailId(UUID bankAccountDetailId) {
        this.bankAccountDetailId = bankAccountDetailId;
    }

    public Date getMathDate() {
        return matchDate;
    }

    public void setMathDate(Date matchDate) {
        this.matchDate = matchDate;
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
        CollectionVoucher costSet = (CollectionVoucher) o;
        if (costSet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), costSet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
