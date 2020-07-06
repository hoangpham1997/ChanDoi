package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.AccountDefaultDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A AccountDefault.
 */
@Entity
@Table(name = "accountdefault")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "AccountDefaultDTO",
        classes = {
            @ConstructorResult(
                targetClass = AccountDefaultDTO.class,
                columns = {
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "typeName", type = String.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "defaultDebitAccount", type = String.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "defaultCreditAccount", type = String.class),
                }
            )
        }
    )})
public class AccountDefault implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    @Column(name = "accountingtype")
    private Integer accountingType;

    @Column(name = "typeid")
    private Integer typeID;

    @Column(name = "columnname")
    private String columnName;

    @Column(name = "columncaption")
    private String columnCaption;

    @Column(name = "filteraccount")
    private String filterAccount;

    @Column(name = "defaultaccount")
    private String defaultAccount;

    @Size(max = 512)
    @Column(name = "reduceaccount")
    private String reduceAccount;

    @Column(name = "pptype")
    private Boolean pPType;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public Integer getAccountingType() {
        return accountingType;
    }

    public void setAccountingType(Integer accountingType) {
        this.accountingType = accountingType;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public Boolean getpPType() {
        return pPType;
    }

    public String getColumnName() {
        return columnName;
    }

    public AccountDefault columnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnCaption() {
        return columnCaption;
    }

    public AccountDefault columnCaption(String columnCaption) {
        this.columnCaption = columnCaption;
        return this;
    }

    public void setColumnCaption(String columnCaption) {
        this.columnCaption = columnCaption;
    }

    public String getFilterAccount() {
        return filterAccount;
    }

    public AccountDefault filterAccount(String filterAccount) {
        this.filterAccount = filterAccount;
        return this;
    }

    public void setFilterAccount(String filterAccount) {
        this.filterAccount = filterAccount;
    }

    public String getDefaultAccount() {
        return defaultAccount;
    }

    public AccountDefault defaultAccount(String defaultAccount) {
        this.defaultAccount = defaultAccount;
        return this;
    }

    public void setDefaultAccount(String defaultAccount) {
        this.defaultAccount = defaultAccount;
    }

    public String getReduceAccount() {
        return reduceAccount;
    }

    public AccountDefault reduceAccount(String reduceAccount) {
        this.reduceAccount = reduceAccount;
        return this;
    }

    public void setReduceAccount(String reduceAccount) {
        this.reduceAccount = reduceAccount;
    }

    public Boolean ispPType() {
        return pPType;
    }

    public AccountDefault pPType(Boolean pPType) {
        this.pPType = pPType;
        return this;
    }

    public void setpPType(Boolean pPType) {
        this.pPType = pPType;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public AccountDefault orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
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
        AccountDefault accountDefault = (AccountDefault) o;
        if (accountDefault.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accountDefault.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AccountDefault{" +
            "id=" + getId() +
            ", typeId=" + getTypeID() +
            ", columnName='" + getColumnName() + "'" +
            ", columnCaption='" + getColumnCaption() + "'" +
            ", filterAccount='" + getFilterAccount() + "'" +
            ", defaultAccount='" + getDefaultAccount() + "'" +
            ", reduceAccount='" + getReduceAccount() + "'" +
            ", pPType='" + ispPType() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
