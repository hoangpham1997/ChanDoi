package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.Report.SoTheTinhGiaThanhDTO;
import vn.softdreams.ebweb.service.dto.TheTinhGiaThanhDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MBDepositExportDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A CostSetMaterialGood.
 */
@Entity
@Table(name = "costsetmaterialgoods")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "TheTinhGiaThanhDTO",
        classes = {
            @ConstructorResult(
                targetClass = TheTinhGiaThanhDTO.class,
                columns = {
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "costSetName", type = String.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "materialGoodsName", type = String.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "SoTheTinhGiaThanhDTO",
        classes = {
            @ConstructorResult(
                targetClass = SoTheTinhGiaThanhDTO.class,
                columns = {
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "costSetName", type = String.class),
                    @ColumnResult(name = "quantumID", type = UUID.class),
                    @ColumnResult(name = "quantumCode", type = String.class),
                    @ColumnResult(name = "quantumName", type = String.class),
                    @ColumnResult(name = "target", type = String.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalAmountNLVL", type = BigDecimal.class),
                    @ColumnResult(name = "totalAmountNCTT", type = BigDecimal.class),
                    @ColumnResult(name = "totalAmountCPSDMTC", type = BigDecimal.class),
                    @ColumnResult(name = "totalAmountCPSXC", type = BigDecimal.class),
                    @ColumnResult(name = "row", type = Integer.class),
                }
            )
        }
    )})
public class CostSetMaterialGood implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String deScription;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true, name = "materialgoodsid")
    private MaterialGoods materialGoods;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDeScription() {
        return deScription;
    }

    public CostSetMaterialGood deScription(String deScription) {
        this.deScription = deScription;
        return this;
    }

    public void setDeScription(String deScription) {
        this.deScription = deScription;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public CostSetMaterialGood orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public MaterialGoods getMaterialGoods() {
        return materialGoods;
    }

    public CostSetMaterialGood materialGoods(MaterialGoods materialGoods) {
        this.materialGoods = materialGoods;
        return this;
    }

    public void setMaterialGoods(MaterialGoods materialGoods) {
        this.materialGoods = materialGoods;
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
        CostSetMaterialGood costSetMaterialGood = (CostSetMaterialGood) o;
        if (costSetMaterialGood.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), costSetMaterialGood.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CostSetMaterialGood{" +
            "id=" + getId() +
            ", deScription='" + getDeScription() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
