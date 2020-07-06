package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A MCAuditDetailMember.
 */
@Entity
@Table(name = "mcauditdetailmember")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MCAuditDetailMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "role")
    private String role;

    @Column(name = "accountingobjectname")
    private String accountingObjectName;

    @Column(name = "accountingobjecttitle")
    private String accountingObjectTitle;

    @Column(name = "orderpriority", insertable = false, updatable = false)
    private Integer orderPriority;

    @Column(name = "mcauditid")
    private UUID mCAuditId;

    @ManyToOne
    @JoinColumn(name = "accountingobjectid")
    private AccountingObject accountingObject;

    @ManyToOne
    @JoinColumn(name = "departmentid")
    private OrganizationUnit organizationUnit;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public MCAuditDetailMember role(String role) {
        this.role = role;
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public MCAuditDetailMember accountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
        return this;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getAccountingObjectTitle() {
        return accountingObjectTitle;
    }

    public MCAuditDetailMember accountingObjectTitle(String accountingObjectTitle) {
        this.accountingObjectTitle = accountingObjectTitle;
        return this;
    }

    public void setAccountingObjectTitle(String accountingObjectTitle) {
        this.accountingObjectTitle = accountingObjectTitle;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public MCAuditDetailMember orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }


    public UUID getmCAuditId() {
        return mCAuditId;
    }

    public void setmCAuditId(UUID mCAuditId) {
        this.mCAuditId = mCAuditId;
    }

    public AccountingObject getAccountingObject() {
        return accountingObject;
    }

    public MCAuditDetailMember accountingObject(AccountingObject accountingObject) {
        this.accountingObject = accountingObject;
        return this;
    }

    public void setAccountingObject(AccountingObject accountingObject) {
        this.accountingObject = accountingObject;
    }

    public OrganizationUnit getOrganizationUnit() {
        return organizationUnit;
    }

    public MCAuditDetailMember organizationUnit(OrganizationUnit organizationUnit) {
        this.organizationUnit = organizationUnit;
        return this;
    }

    public void setOrganizationUnit(OrganizationUnit organizationUnit) {
        this.organizationUnit = organizationUnit;
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
        MCAuditDetailMember mCAuditDetailMember = (MCAuditDetailMember) o;
        if (mCAuditDetailMember.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mCAuditDetailMember.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MCAuditDetailMember{" +
            "id=" + getId() +
            ", role='" + getRole() + "'" +
            ", accountingObjectName='" + getAccountingObjectName() + "'" +
            ", accountingObjectTitle='" + getAccountingObjectTitle() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
