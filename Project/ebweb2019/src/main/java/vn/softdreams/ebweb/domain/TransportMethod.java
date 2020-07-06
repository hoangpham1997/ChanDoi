package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A TransportMethod.
 */
@Entity
@Table(name = "transportmethod")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TransportMethod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Size(max = 25)
    @Column(name = "transportmethodcode", length = 25, nullable = false)
    private String transportMethodCode;

    @NotNull
    @Size(max = 512)
    @Column(name = "transportmethodname", length = 512, nullable = false)
    private String transportMethodName;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "isactive", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "issecurity", nullable = false)
    private Boolean isSecurity;

    @Column(name = "companyid", nullable = false)
    private UUID companyId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTransportMethodCode() {
        return transportMethodCode;
    }

    public TransportMethod transportMethodCode(String transportMethodCode) {
        this.transportMethodCode = transportMethodCode;
        return this;
    }

    public void setTransportMethodCode(String transportMethodCode) {
        this.transportMethodCode = transportMethodCode;
    }

    public String getTransportMethodName() {
        return transportMethodName;
    }

    public TransportMethod transportMethodName(String transportMethodName) {
        this.transportMethodName = transportMethodName;
        return this;
    }

    public void setTransportMethodName(String transportMethodName) {
        this.transportMethodName = transportMethodName;
    }

    public String getDescription() {
        return description;
    }

    public TransportMethod description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public TransportMethod isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isIsSecurity() {
        return isSecurity;
    }

    public TransportMethod isSecurity(Boolean isSecurity) {
        this.isSecurity = isSecurity;
        return this;
    }

    public void setIsSecurity(Boolean isSecurity) {
        this.isSecurity = isSecurity;
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
        TransportMethod transportMethod = (TransportMethod) o;
        if (transportMethod.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transportMethod.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransportMethod{" +
            "id=" + getId() +
            ", transportMethodCode='" + getTransportMethodCode() + "'" +
            ", transportMethodName='" + getTransportMethodName() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + isIsActive() + "'" +
            ", isSecurity='" + isIsSecurity() + "'" +
            "}";
    }
}
