package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A MaterialGoodsSpecifications.
 */
@Entity
@Table(name = "materialgoodsspecifications")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MaterialGoodsSpecifications implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "isfollow")
    private Boolean isFollow;

    @Size(max = 25)
    @Column(name = "materialgoodsspecificationscode", length = 25)
    private String materialGoodsSpecificationsCode;

    @Size(max = 512)
    @Column(name = "materialgoodsspecificationsname", length = 512)
    private String materialGoodsSpecificationsName;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "materialgoodsid")
    private UUID materialGoodsID;


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean isIsFollow() {
        return isFollow;
    }

    public MaterialGoodsSpecifications isFollow(Boolean isFollow) {
        this.isFollow = isFollow;
        return this;
    }

    public void setIsFollow(Boolean isFollow) {
        this.isFollow = isFollow;
    }

    public String getMaterialGoodsSpecificationsCode() {
        return materialGoodsSpecificationsCode;
    }

    public MaterialGoodsSpecifications materialGoodsSpecificationsCode(String materialGoodsSpecificationsCode) {
        this.materialGoodsSpecificationsCode = materialGoodsSpecificationsCode;
        return this;
    }

    public void setMaterialGoodsSpecificationsCode(String materialGoodsSpecificationsCode) {
        this.materialGoodsSpecificationsCode = materialGoodsSpecificationsCode;
    }

    public String getMaterialGoodsSpecificationsName() {
        return materialGoodsSpecificationsName;
    }

    public MaterialGoodsSpecifications materialGoodsSpecificationsName(String materialGoodsSpecificationsName) {
        this.materialGoodsSpecificationsName = materialGoodsSpecificationsName;
        return this;
    }

    public void setMaterialGoodsSpecificationsName(String materialGoodsSpecificationsName) {
        this.materialGoodsSpecificationsName = materialGoodsSpecificationsName;
    }

    public String getDescription() {
        return description;
    }

    public MaterialGoodsSpecifications description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
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
        MaterialGoodsSpecifications materialGoodsSpecifications = (MaterialGoodsSpecifications) o;
        if (materialGoodsSpecifications.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), materialGoodsSpecifications.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MaterialGoodsSpecifications{" +
            "id=" + getId() +
            ", isFollow='" + isIsFollow() + "'" +
            ", materialGoodsSpecificationsCode='" + getMaterialGoodsSpecificationsCode() + "'" +
            ", materialGoodsSpecificationsName='" + getMaterialGoodsSpecificationsName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }


}
