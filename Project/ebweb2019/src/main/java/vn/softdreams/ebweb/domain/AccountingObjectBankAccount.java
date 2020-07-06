package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.web.rest.dto.AccountingObjectBankAccountDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.ViewVoucherDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A AccountingObjectBankAccount.
 */
@Entity
@Table(name = "accountingobjectbankaccount")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "AccountingObjectBankAccountDTO",
        classes = {
            @ConstructorResult(
                targetClass = AccountingObjectBankAccountDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "bankAccount", type = String.class),
                    @ColumnResult(name = "bankName", type = String.class),
                    @ColumnResult(name = "bankBranchName", type = String.class),
                    @ColumnResult(name = "accountHolderName", type = String.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
                    @ColumnResult(name = "accountingObjectId", type = UUID.class)
                }
            )
        }
    )})
public class AccountingObjectBankAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Size(max = 25)
    @Column(name = "bankaccount", length = 25)
    private String bankAccount;

    @Size(max = 512)
    @Column(name = "bankname", length = 512)
    private String bankName;

    @Size(max = 512)
    @Column(name = "bankbranchname", length = 512)
    private String bankBranchName;

    @Size(max = 512)
    @Column(name = "accountholdername", length = 512)
    private String accountHolderName;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
        AccountingObjectBankAccount accountingObjectBankAccount = (AccountingObjectBankAccount) o;
        if (accountingObjectBankAccount.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accountingObjectBankAccount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AccountingObjectBankAccount{" +
            "id=" + getId() +
            "}";
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public UUID getAccountingObjectId() {
        return accountingObjectId;
    }

    public void setAccountingObjectId(UUID accountingObjectId) {
        this.accountingObjectId = accountingObjectId;
    }
}
