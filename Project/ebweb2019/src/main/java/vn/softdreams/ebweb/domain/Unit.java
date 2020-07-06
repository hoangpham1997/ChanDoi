package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.UnitConvertRateDTO;
import vn.softdreams.ebweb.web.rest.dto.SaBillDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A Unit.
 */
@Entity
@Table(name = "unit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "UnitConvertRateDTO",
        classes = {
            @ConstructorResult(
                targetClass = UnitConvertRateDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "unitName", type = String.class),
                    @ColumnResult(name = "convertRate", type = BigDecimal.class),
                    @ColumnResult(name = "formula", type = String.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "OPUnitConvertRateDTO",
        classes = {
            @ConstructorResult(
                targetClass = UnitConvertRateDTO.class,
                columns = {
                    @ColumnResult(name = "repositoryId", type = String.class),
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "convertRate", type = BigDecimal.class),
                    @ColumnResult(name = "formula", type = String.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "MainUnitDTO",
        classes = {
            @ConstructorResult(
                targetClass = UnitConvertRateDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "MainUnitNameDTO",
        classes = {
            @ConstructorResult(
                targetClass = UnitConvertRateDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "unitName", type = String.class),
                    @ColumnResult(name = "convertRate", type = BigDecimal.class),
                    @ColumnResult(name = "formula", type = String.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "UnitConvertRateDTOForFindAll",
        classes = {
            @ConstructorResult(
                targetClass = UnitConvertRateDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "unitName", type = String.class),
                    @ColumnResult(name = "convertRate", type = BigDecimal.class),
                    @ColumnResult(name = "formula", type = String.class),
                }
            )
        }
    )
})
public class Unit implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @NotNull
    @Size(max = 25)
    @Column(name = "unitname", length = 25, nullable = false)
    private String unitName;

    @Size(max = 512)
    @Column(name = "unitdescription", length = 512)
    private String unitDescription;

    @Column(name = "isactive")
    private Boolean isActive;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private BigDecimal convertRate;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String formula;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public Unit unitName(String unitName) {
        this.unitName = unitName;
        return this;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitDescription() {
        return unitDescription;
    }

    public Unit unitDescription(String unitDescription) {
        this.unitDescription = unitDescription;
        return this;
    }

    public void setUnitDescription(String unitDescription) {
        this.unitDescription = unitDescription;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Unit isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public BigDecimal getConvertRate() {
        return convertRate;
    }

    public void setConvertRate(BigDecimal convertRate) {
        this.convertRate = convertRate;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
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
        Unit unit = (Unit) o;
        if (unit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), unit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Unit{" +
            "id=" + getId() +
            ", unitName='" + getUnitName() + "'" +
            ", unitDescription='" + getUnitDescription() + "'" +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
