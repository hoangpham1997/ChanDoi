package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.BankAccountDetailDTO;
import vn.softdreams.ebweb.service.dto.RepositoryDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A Repository.
 */
@Entity
@Table(name = "repository")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "RepositoryDTO",
        classes = {
            @ConstructorResult(
                targetClass = RepositoryDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "repositoryCode", type = String.class),
                    @ColumnResult(name = "repositoryName", type = String.class),
                }
            )
        }
    ),
})
public class Repository implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @NotNull
    @Size(max = 25)
    @Column(name = "repositorycode", length = 25, nullable = false)
    private String repositoryCode;

    @NotNull
    @Size(max = 512)
    @Column(name = "repositoryname", length = 512, nullable = false)
    private String repositoryName;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Size(max = 25)
    @Column(name = "defaultaccount", length = 25)
    private String defaultAccount;

    @NotNull
    @Column(name = "isactive", nullable = false)
    private Boolean isActive;

    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name="branchID")
    private OrganizationUnit branchID;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public Repository repositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
        return this;
    }

    public void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public Repository repositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
        return this;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getDescription() {
        return description;
    }

    public Repository description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultAccount() {
        return defaultAccount;
    }

    public Repository defaultAccount(String defaultAccount) {
        this.defaultAccount = defaultAccount;
        return this;
    }

    public void setDefaultAccount(String defaultAccount) {
        this.defaultAccount = defaultAccount;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public OrganizationUnit getBranchID() {
        return branchID;
    }

    public Repository branchID(OrganizationUnit organizationUnit) {
        this.branchID = organizationUnit;
        return this;
    }

    public void setBranchID(OrganizationUnit organizationUnit) {
        this.branchID = organizationUnit;
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
        Repository repository = (Repository) o;
        if (repository.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), repository.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Repository{" +
            "id=" + getId() +
            ", repositoryCode='" + getRepositoryCode() + "'" +
            ", repositoryName='" + getRepositoryName() + "'" +
            ", description='" + getDescription() + "'" +
            ", defaultAccount='" + getDefaultAccount() + "'" +
            "}";
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }
}
