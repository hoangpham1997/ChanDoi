package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.Report.TIDecrementDTO;
import vn.softdreams.ebweb.service.dto.TIDecrementDetailsConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.OrganizationUnitCustomDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A TIDecrementDetails.
 */
@Entity
@Table(name = "tidecrementdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings( value = {
    @SqlResultSetMapping(
        name = "TIDecrementDTOList",
        classes = {
            @ConstructorResult(
                targetClass = TIDecrementDTO.class,
                columns = {
                    @ColumnResult(name = "toolCode", type = String.class),
                    @ColumnResult(name = "toolName", type = String.class),
                    @ColumnResult(name = "OrganizationUnitCode", type = String.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "decrementQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "remainingDecrementAmount", type = BigDecimal.class)
                }
            )
        }),
    @SqlResultSetMapping(
        name = "TIDecrementDetailsConvertDTO" ,
        classes = {
            @ConstructorResult(
                targetClass = TIDecrementDetailsConvertDTO.class ,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "tiDecrementID", type = UUID.class),
                    @ColumnResult(name = "toolsID", type = UUID.class),
                    @ColumnResult(name = "toolsCode", type = String.class),
                    @ColumnResult(name = "toolsName", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "departmentID", type = UUID.class),
                    @ColumnResult(name = "departmentCode", type = String.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "decrementQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "remainingDecrementAmount", type = BigDecimal.class),
                    @ColumnResult(name = "tiAuditID", type = UUID.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
                }
            )
        })
})
public class TIDecrementDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tidecrementid")
    private UUID tiDecrementID;

    @Column(name = "toolsid")
    private UUID toolsID;

    @Column(name = "departmentid")
    private UUID departmentID;

    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "decrementquantity", precision = 10, scale = 2)
    private BigDecimal decrementQuantity;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "remainingdecrementamount", precision = 10, scale = 2)
    private BigDecimal remainingDecrementAmount;

    @Column(name = "tiauditid")
    private UUID tiAuditID;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private List<OrganizationUnitCustomDTO> organizationUnits;
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public List<OrganizationUnitCustomDTO> getOrganizationUnits() {
        return organizationUnits;
    }

    public void setOrganizationUnits(List<OrganizationUnitCustomDTO> organizationUnits) {
        this.organizationUnits = organizationUnits;
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

    public UUID getTiDecrementID() {
        return tiDecrementID;
    }

    public void setTiDecrementID(UUID tiDecrementID) {
        this.tiDecrementID = tiDecrementID;
    }

    public UUID getToolsID() {
        return toolsID;
    }

    public void setToolsID(UUID toolsID) {
        this.toolsID = toolsID;
    }

    public UUID getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(UUID departmentID) {
        this.departmentID = departmentID;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getDecrementQuantity() {
        return decrementQuantity;
    }

    public void setDecrementQuantity(BigDecimal decrementQuantity) {
        this.decrementQuantity = decrementQuantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRemainingDecrementAmount() {
        return remainingDecrementAmount;
    }

    public void setRemainingDecrementAmount(BigDecimal remainingDecrementAmount) {
        this.remainingDecrementAmount = remainingDecrementAmount;
    }

    public UUID getTiAuditID() {
        return tiAuditID;
    }

    public void setTiAuditID(UUID tiAuditID) {
        this.tiAuditID = tiAuditID;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public TIDecrementDetails orderPriority(Integer orderPriority) {
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
        TIDecrementDetails tIDecrementDetails = (TIDecrementDetails) o;
        if (tIDecrementDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tIDecrementDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TIDecrementDetails{" +
            "id=" + getId() +
            ", tiAuditID='" + getTiDecrementID() + "'" +
            ", toolsID='" + getToolsID() + "'" +
            ", departmentID='" + getDepartmentID() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
