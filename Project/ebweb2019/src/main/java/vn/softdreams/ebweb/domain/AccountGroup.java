package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A AccountGroup.
 */
@Entity
@Table(name = "accountgroup")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AccountGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Column(name = "accountingtype")
    private Integer accountingType;

    @Size(max = 512)
    @Column(name = "accountgroupname", length = 512)
    private String accountGroupName;

    @Column(name = "accountgroupkind")
    private Integer accountGroupKind;

    @Column(name = "detailtype", length = 50)
    private String detailType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountGroupName() {
        return accountGroupName;
    }

    public AccountGroup accountGroupName(String accountGroupName) {
        this.accountGroupName = accountGroupName;
        return this;
    }

    public void setAccountGroupName(String accountGroupName) {
        this.accountGroupName = accountGroupName;
    }

    public Integer getAccountGroupKind() {
        return accountGroupKind;
    }

    public AccountGroup accountGroupKind(Integer accountGroupKind) {
        this.accountGroupKind = accountGroupKind;
        return this;
    }

    public void setAccountGroupKind(Integer accountGroupKind) {
        this.accountGroupKind = accountGroupKind;
    }

    public String getDetailType() {
        return detailType;
    }

    public AccountGroup detailType(String detailType) {
        this.detailType = detailType;
        return this;
    }

    public Integer getAccountingType() {
        return accountingType;
    }

    public void setAccountingType(Integer accountingType) {
        this.accountingType = accountingType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
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
        AccountGroup accountGroup = (AccountGroup) o;
        if (accountGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accountGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AccountGroup{" +
            "id=" + getId() +
            ", accountGroupName='" + getAccountGroupName() + "'" +
            ", accountGroupKind=" + getAccountGroupKind() +
            ", detailType=" + getDetailType() +
            "}";
    }
}
