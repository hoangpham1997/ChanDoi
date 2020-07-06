package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A FAAuditMemberDetail.
 */
@Entity
@Table(name = "faauditmemberdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FAAuditMemberDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "faauditid")
    private UUID faAuditID;

    @Size(max = 128)
    @Column(name = "accountobjectname", length = 128)
    private String accountObjectName;

    @Size(max = 128)
    @Column(name = "accountingobjecttitle", length = 128)
    private String accountingObjectTitle;

    @Size(max = 255)
    @Column(name = "role", length = 255)
    private String role;

    @Column(name = "departmentid")
    private UUID departmentID;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

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

    public UUID getFaAuditID() {
        return faAuditID;
    }

    public void setFaAuditID(UUID faAuditID) {
        this.faAuditID = faAuditID;
    }

    public String getAccountObjectName() {
        return accountObjectName;
    }

    public void setAccountObjectName(String accountObjectName) {
        this.accountObjectName = accountObjectName;
    }

    public String getAccountingObjectTitle() {
        return accountingObjectTitle;
    }

    public void setAccountingObjectTitle(String accountingObjectTitle) {
        this.accountingObjectTitle = accountingObjectTitle;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UUID getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(UUID departmentID) {
        this.departmentID = departmentID;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
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
        TIAuditMemberDetails tIAuditMemberDetails = (TIAuditMemberDetails) o;
        if (tIAuditMemberDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tIAuditMemberDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
