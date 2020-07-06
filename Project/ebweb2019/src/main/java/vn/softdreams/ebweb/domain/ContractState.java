package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A ContractState.
 */
@Entity
@Table(name = "contractstate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ContractState implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @NotNull
//    @Size(max = 25)
//    @Column(name = "contractstatecode", length = 25, nullable = false)
//    private String contractStateCode;

    @NotNull
    @Size(max = 512)
    @Column(name = "contractstatename", length = 512, nullable = false)
    private String contractStateName;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @NotNull
    @Column(name = "issecurity", nullable = false)
    private Boolean isSecurity;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public String getContractStateCode() {
//        return contractStateCode;
//    }
//
//    public ContractState contractStateCode(String contractStateCode) {
//        this.contractStateCode = contractStateCode;
//        return this;
//    }
//
//    public void setContractStateCode(String contractStateCode) {
//        this.contractStateCode = contractStateCode;
//    }

    public String getContractStateName() {
        return contractStateName;
    }

    public ContractState contractStateName(String contractStateName) {
        this.contractStateName = contractStateName;
        return this;
    }

    public void setContractStateName(String contractStateName) {
        this.contractStateName = contractStateName;
    }

    public String getDescription() {
        return description;
    }

    public ContractState description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isIsSecurity() {
        return isSecurity;
    }

    public ContractState isSecurity(Boolean isSecurity) {
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
        ContractState contractState = (ContractState) o;
        if (contractState.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contractState.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ContractState{" +
            "id=" + getId() +
//            ", contractStateCode='" + getContractStateCode() + "'" +
            ", contractStateName='" + getContractStateName() + "'" +
            ", description='" + getDescription() + "'" +
            ", isSecurity='" + isIsSecurity() + "'" +
            "}";
    }
}
