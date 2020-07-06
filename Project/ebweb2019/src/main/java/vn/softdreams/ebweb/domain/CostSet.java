package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.CostSetDTO;
import vn.softdreams.ebweb.service.dto.CostSetMaterialGoodsDTO;
import vn.softdreams.ebweb.service.dto.ObjectsMaterialQuantumDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MBCreditCardExportDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A CostSet.
 */
@Entity
@Table(name = "costset")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "CostSetMaterialGoodsDTO",
        classes = {
            @ConstructorResult(
                targetClass = CostSetMaterialGoodsDTO.class,
                columns = {
                    @ColumnResult(name = "quantumID", type = UUID.class),
                    @ColumnResult(name = "quantumCode", type = String.class),
                    @ColumnResult(name = "quantumName", type = String.class),
                    @ColumnResult(name = "quantityClosing", type = BigDecimal.class),
                    @ColumnResult(name = "rate", type = BigDecimal.class),
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "CostSetAndMaterialGoodsDTO",
        classes = {
            @ConstructorResult(
                targetClass = CostSetMaterialGoodsDTO.class,
                columns = {
                    @ColumnResult(name = "quantumID", type = UUID.class),
                    @ColumnResult(name = "costSetID", type = UUID.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "CostSetDTO",
        classes = {
            @ConstructorResult(
                targetClass = CostSetDTO.class,
                columns = {
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "costSetName", type = String.class),
                    @ColumnResult(name = "revenueAmount", type = BigDecimal.class)
                }
            )
        }
    )})
public class CostSet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "branchid")
    private UUID branchID;

    @Column(name = "materialgoodsid")
    private UUID materialGoodsID;

    @Column(name = "companyid")
    private UUID companyID;

    @NotNull
    @Size(max = 50)
    @Column(name = "costsetcode", length = 50, nullable = false)
    private String costSetCode;

    @NotNull
    @Size(max = 512)
    @Column(name = "costsetname", length = 512, nullable = false)
    private String costSetName;

    @Column(name = "costsettype")
    private Integer costSetType;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String deScription;

    @Column(name = "parentid")
    private UUID parentID;

    @Column(name = "isparentnode", nullable = false)
    private Boolean isParentNode;

    @Size(max = 200)
    @Column(name = "orderfixcode", length = 200)
    private String orderFixCode;

    @Column(name = "grade", nullable = false)
    private Integer grade;

    @Column(name = "isactive", nullable = false)
    private Boolean isActive;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "costsetid")
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<CostSetMaterialGood> costSetMaterialGoods = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public CostSet branchID(UUID branchID) {
        this.branchID = branchID;
        return this;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public String getCostSetCode() {
        return costSetCode;
    }

    public CostSet costSetCode(String costSetCode) {
        this.costSetCode = costSetCode;
        return this;
    }

    public void setCostSetCode(String costSetCode) {
        this.costSetCode = costSetCode;
    }

    public String getCostSetName() {
        return costSetName;
    }

    public CostSet costSetName(String costSetName) {
        this.costSetName = costSetName;
        return this;
    }

    public void setCostSetName(String costSetName) {
        this.costSetName = costSetName;
    }

    public Integer getCostSetType() {
        return costSetType;
    }

    public CostSet costSetType(Integer costSetType) {
        this.costSetType = costSetType;
        return this;
    }

    public void setCostSetType(Integer costSetType) {
        this.costSetType = costSetType;
    }

    public String getDeScription() {
        return deScription;
    }

    public CostSet deScription(String deScription) {
        this.deScription = deScription;
        return this;
    }

    public void setDeScription(String deScription) {
        this.deScription = deScription;
    }

    public UUID getParentID() {
        return parentID;
    }

    public CostSet parentID(UUID parentID) {
        this.parentID = parentID;
        return this;
    }

    public void setParentID(UUID parentID) {
        this.parentID = parentID;
    }

    public Boolean isIsParentNode() {
        return isParentNode;
    }

    public CostSet isParentNode(Boolean isParentNode) {
        this.isParentNode = isParentNode;
        return this;
    }

    public void setIsParentNode(Boolean isParentNode) {
        this.isParentNode = isParentNode;
    }

    public String getOrderFixCode() {
        return orderFixCode;
    }

    public CostSet orderFixCode(String orderFixCode) {
        this.orderFixCode = orderFixCode;
        return this;
    }

    public void setOrderFixCode(String orderFixCode) {
        this.orderFixCode = orderFixCode;
    }

    public Integer getGrade() {
        return grade;
    }

    public CostSet grade(Integer grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public CostSet isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<CostSetMaterialGood> getCostSetMaterialGoods() {
        return costSetMaterialGoods;
    }

    public void setCostSetMaterialGoods(Set<CostSetMaterialGood> costSetMaterialGoods) {
        this.costSetMaterialGoods = costSetMaterialGoods;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
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
        CostSet costSet = (CostSet) o;
        if (costSet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), costSet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CostSet{" +
            "id=" + getId() +
            ", branchID=" + getBranchID() +
            ", costSetCode='" + getCostSetCode() + "'" +
            ", costSetName='" + getCostSetName() + "'" +
            ", costSetType=" + getCostSetType() +
            ", deScription='" + getDeScription() + "'" +
            ", parentID=" + getParentID() +
            ", isParentNode='" + isIsParentNode() + "'" +
            ", orderFixCode='" + getOrderFixCode() + "'" +
            ", grade=" + getGrade() +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
