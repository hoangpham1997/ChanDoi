package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.web.rest.dto.TIAuditDetailByIDDTO;
import vn.softdreams.ebweb.web.rest.dto.TIAuditDetailDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A TIAuditDetails.
 */
@Entity
@Table(name = "tiauditdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "TIAuditDetailDTO",
        classes = {
            @ConstructorResult(
                targetClass = TIAuditDetailDTO.class,
                columns = {
                    @ColumnResult(name = "toolCode", type = String.class),
                    @ColumnResult(name = "toolName", type = String.class),
                    @ColumnResult(name = "unitName", type = String.class),
                    @ColumnResult(name = "organizationUnitName", type = String.class),
                    @ColumnResult(name = "quantityONBook", type = BigDecimal.class),
                    @ColumnResult(name = "quantityInventory", type = BigDecimal.class),
                    @ColumnResult(name = "diffQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "executeQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "recommendation", type = Integer.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "TIAuditDetailByIDDTO",
        classes = {
            @ConstructorResult(
                targetClass = TIAuditDetailByIDDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "tiAuditID", type = UUID.class),
                    @ColumnResult(name = "toolsID", type = UUID.class),
                    @ColumnResult(name = "toolsCode", type = String.class),
                    @ColumnResult(name = "toolsName", type = String.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "unitName", type = String.class),
                    @ColumnResult(name = "departmentID", type = UUID.class),
                    @ColumnResult(name = "departmentCode", type = String.class),
                    @ColumnResult(name = "quantityONBook", type = BigDecimal.class),
                    @ColumnResult(name = "quantityInventory", type = BigDecimal.class),
                    @ColumnResult(name = "diffQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "executeQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "recommendation", type = int.class),
                    @ColumnResult(name = "note", type = String.class),
                    @ColumnResult(name = "orderPriority", type = int.class),
                }
            )
        }
    )
})
public class TIAuditDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tiauditid")
    private UUID tiAuditID;

    @Column(name = "toolsid")
    private UUID toolsID;

    @Column(name = "departmentid")
    private UUID departmentID;

    @Column(name = "quantityonbook", precision = 10, scale = 2)
    private BigDecimal quantityOnBook;

    @Column(name = "quantityinventory", precision = 10, scale = 2)
    private BigDecimal quantityInventory;

    @Column(name = "diffquantity", precision = 10, scale = 2)
    private BigDecimal diffQuantity;

    @Column(name = "executequantity", precision = 10, scale = 2)
    private BigDecimal executeQuantity;

    @Column(name = "recommendation")
    private Integer recommendation;

    @Size(max = 225)
    @Column(name = "note", length = 225)
    private String note;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private  BigDecimal unitPrice;
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
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

    public BigDecimal getQuantityOnBook() {
        return quantityOnBook;
    }

    public TIAuditDetails quantityOnBook(BigDecimal quantityOnBook) {
        this.quantityOnBook = quantityOnBook;
        return this;
    }

    public void setQuantityOnBook(BigDecimal quantityOnBook) {
        this.quantityOnBook = quantityOnBook;
    }

    public BigDecimal getQuantityInventory() {
        return quantityInventory;
    }

    public TIAuditDetails quantityInventory(BigDecimal quantityInventory) {
        this.quantityInventory = quantityInventory;
        return this;
    }

    public void setQuantityInventory(BigDecimal quantityInventory) {
        this.quantityInventory = quantityInventory;
    }

    public BigDecimal getDiffQuantity() {
        return diffQuantity;
    }

    public TIAuditDetails diffQuantity(BigDecimal diffQuantity) {
        this.diffQuantity = diffQuantity;
        return this;
    }

    public void setDiffQuantity(BigDecimal diffQuantity) {
        this.diffQuantity = diffQuantity;
    }

    public BigDecimal getExecuteQuantity() {
        return executeQuantity;
    }

    public TIAuditDetails executeQuantity(BigDecimal executeQuantity) {
        this.executeQuantity = executeQuantity;
        return this;
    }

    public void setExecuteQuantity(BigDecimal executeQuantity) {
        this.executeQuantity = executeQuantity;
    }

    public Integer getRecommendation() {
        return recommendation;
    }

    public TIAuditDetails recommendation(Integer recommendation) {
        this.recommendation = recommendation;
        return this;
    }

    public void setRecommendation(Integer recommendation) {
        this.recommendation = recommendation;
    }

    public String getNote() {
        return note;
    }

    public TIAuditDetails note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public TIAuditDetails orderPriority(Integer orderPriority) {
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
        TIAuditDetails tIAuditDetails = (TIAuditDetails) o;
        if (tIAuditDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tIAuditDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TIAuditDetails{" +
            "id=" + getId() +
            ", tiAuditID='" + getTiAuditID() + "'" +
            ", toolsID='" + getToolsID() + "'" +
            ", departmentID='" + getDepartmentID() + "'" +
            ", quantityOnBook=" + getQuantityOnBook() +
            ", quantityInventory=" + getQuantityInventory() +
            ", diffQuantity=" + getDiffQuantity() +
            ", executeQuantity=" + getExecuteQuantity() +
            ", recommendation=" + getRecommendation() +
            ", note='" + getNote() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
