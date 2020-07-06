package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A CareerGroup.
 */
@Entity
@Table(name = "careergroup")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CareerGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Size(max = 25)
    @Column(name = "careergroupcode", length = 25, nullable = false)
    private String careerGroupCode;

    @NotNull
    @Size(max = 512)
    @Column(name = "careergroupname", length = 512, nullable = false)
    private String careerGroupName;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCareerGroupCode() {
        return careerGroupCode;
    }

    public CareerGroup careerGroupCode(String careerGroupCode) {
        this.careerGroupCode = careerGroupCode;
        return this;
    }

    public void setCareerGroupCode(String careerGroupCode) {
        this.careerGroupCode = careerGroupCode;
    }

    public String getCareerGroupName() {
        return careerGroupName;
    }

    public CareerGroup careerGroupName(String careerGroupName) {
        this.careerGroupName = careerGroupName;
        return this;
    }

    public void setCareerGroupName(String careerGroupName) {
        this.careerGroupName = careerGroupName;
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
        CareerGroup careerGroup = (CareerGroup) o;
        if (careerGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), careerGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CareerGroup{" +
            "id=" + getId() +
            ", careerGroupCode='" + getCareerGroupCode() + "'" +
            ", careerGroupName='" + getCareerGroupName() + "'" +
            "}";
    }
}
