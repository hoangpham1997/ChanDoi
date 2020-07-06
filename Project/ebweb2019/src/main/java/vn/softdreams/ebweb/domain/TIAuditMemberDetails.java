package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.web.rest.dto.TIAuditDetailByIDDTO;
import vn.softdreams.ebweb.web.rest.dto.TIAuditMemberDetailByIDDTO;
import vn.softdreams.ebweb.web.rest.dto.TIAuditMemberDetailDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A TIAuditMemberDetails.
 */
@Entity
@Table(name = "tiauditmemberdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "TIAuditMemberDetailsDTO",
        classes = {
            @ConstructorResult(
                targetClass = TIAuditMemberDetailDTO.class,
                columns = {
                    @ColumnResult(name = "accountObjectName", type = String.class),
                    @ColumnResult(name = "accountingObjectTitle", type = String.class),
                    @ColumnResult(name = "organizationUnitName", type = String.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "TIAuditMemberDetailByIDDTO",
        classes = {
            @ConstructorResult(
                targetClass = TIAuditMemberDetailByIDDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "tiAuditID", type = UUID.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "accountingObjectCode", type = String.class),
                    @ColumnResult(name = "accountObjectName", type = String.class),
                    @ColumnResult(name = "accountingObjectTitle", type = String.class),
                    @ColumnResult(name = "role", type = String.class),
                    @ColumnResult(name = "departmentID", type = UUID.class),
                    @ColumnResult(name = "departmentCode", type = String.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
                }
            )
        }
    )})
public class TIAuditMemberDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tiauditid")
    private UUID tiAuditID;

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

    public String getAccountObjectName() {
        return accountObjectName;
    }

    public TIAuditMemberDetails accountObjectName(String accountObjectName) {
        this.accountObjectName = accountObjectName;
        return this;
    }

    public void setAccountObjectName(String accountObjectName) {
        this.accountObjectName = accountObjectName;
    }

    public String getAccountingObjectTitle() {
        return accountingObjectTitle;
    }

    public TIAuditMemberDetails accountingObjectTitle(String accountingObjectTitle) {
        this.accountingObjectTitle = accountingObjectTitle;
        return this;
    }

    public void setAccountingObjectTitle(String accountingObjectTitle) {
        this.accountingObjectTitle = accountingObjectTitle;
    }

    public String getRole() {
        return role;
    }

    public TIAuditMemberDetails role(String role) {
        this.role = role;
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTiAuditID() {
        return tiAuditID;
    }

    public void setTiAuditID(UUID tiAuditID) {
        this.tiAuditID = tiAuditID;
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

    public TIAuditMemberDetails orderPriority(Integer orderPriority) {
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

    @Override
    public String toString() {
        return "TIAuditMemberDetails{" +
            "id=" + getId() +
            ", tiAuditID='" + getTiAuditID() + "'" +
            ", accountObjectName='" + getAccountObjectName() + "'" +
            ", accountingObjectTitle='" + getAccountingObjectTitle() + "'" +
            ", role='" + getRole() + "'" +
            ", departmentID='" + getDepartmentID() + "'" +
            ", accountingObjectID='" + getAccountingObjectID() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
