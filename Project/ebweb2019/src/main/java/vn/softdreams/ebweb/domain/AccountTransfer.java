package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A AccountTransfer.
 */
@Entity
@Table(name = "accounttransfer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AccountTransfer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "accountingtype")
    private Integer accountingType;

    @Column(name = "accounttransfercode")
    private String accountTransferCode;

    @Column(name = "accounttransferorder")
    private Integer accountTransferOrder;

    @Column(name = "description")
    private String description;

    @Column(name = "fromaccount")
    private String fromAccount;

    @Column(name = "toaccount")
    private String toAccount;

    @Column(name = "fromaccountdata")
    private Integer fromAccountData;

    @Column(name = "debitaccount")
    private Integer debitAccount;

    @Column(name = "creditaccount")
    private Integer creditAccount;

    @Column(name = "isactive")
    private Boolean isActive;

    @Column(name = "issecurity")
    private Boolean isSecurity;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAccountTransferCode() {
        return accountTransferCode;
    }

    public AccountTransfer accountTransferCode(String accountTransferCode) {
        this.accountTransferCode = accountTransferCode;
        return this;
    }

    public void setAccountTransferCode(String accountTransferCode) {
        this.accountTransferCode = accountTransferCode;
    }

    public Integer getAccountTransferOrder() {
        return accountTransferOrder;
    }

    public AccountTransfer accountTransferOrder(Integer accountTransferOrder) {
        this.accountTransferOrder = accountTransferOrder;
        return this;
    }

    public void setAccountTransferOrder(Integer accountTransferOrder) {
        this.accountTransferOrder = accountTransferOrder;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public Integer getAccountingType() {
        return accountingType;
    }

    public void setAccountingType(Integer accountingType) {
        this.accountingType = accountingType;
    }

    public Integer getFromAccountData() {
        return fromAccountData;
    }

    public void setFromAccountData(Integer fromAccountData) {
        this.fromAccountData = fromAccountData;
    }

    public Integer getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(Integer debitAccount) {
        this.debitAccount = debitAccount;
    }

    public Integer getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(Integer creditAccount) {
        this.creditAccount = creditAccount;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }



    public String getFromAccount() {
        return fromAccount;
    }

    public AccountTransfer fromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
        return this;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public AccountTransfer toAccount(String toAccount) {
        this.toAccount = toAccount;
        return this;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getDescription() {
        return description;
    }

    public AccountTransfer description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsSecurity() {
        return isSecurity;
    }

    public void setIsSecurity(Boolean security) {
        isSecurity = security;
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
        AccountTransfer accountTransfer = (AccountTransfer) o;
        if (accountTransfer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accountTransfer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AccountTransfer{" +
            "id=" + getId() +
            ", accountTransferCode='" + getAccountTransferCode() + "'" +
            ", accountTransferOrder=" + getAccountTransferOrder() +
            ", fromAccount='" + getFromAccount() + "'" +
            ", toAccount='" + getToAccount() + "'" +
            ", description='" + getDescription() + "'" +
            ", isSecurity='" + getIsSecurity() + "'" +
            "}";
    }
}
