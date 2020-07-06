package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.softdreams.ebweb.service.dto.AccountListDTO;
import vn.softdreams.ebweb.service.dto.BankAccountDetailDTO;
import vn.softdreams.ebweb.service.dto.ComboboxBankAccountDetailDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A BankAccountDetails.
 */
@Entity
@Table(name = "bankaccountdetail")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "bankAccountDetailDTO",
        classes = {
            @ConstructorResult(
                targetClass = BankAccountDetailDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "bankAccount", type = String.class),
                    @ColumnResult(name = "bankName", type = String.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "ComboboxBankAccountDetailDTO",
        classes = {
            @ConstructorResult(
                targetClass = ComboboxBankAccountDetailDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "bankAccount", type = String.class),
                    @ColumnResult(name = "bankName", type = String.class),
                }
            )
        }
    )
})
public class BankAccountDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @NotNull
    @Size(max = 50)
    @Column(name = "bankaccount", length = 50, nullable = false)
    private String bankAccount;

    @NotNull
    @Size(max = 512)
    @Column(name = "bankname", length = 512, nullable = false)
    private String bankName;

    @Size(max = 512)
    @Column(name = "address", length = 512)
    private String address;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Size(max = 512)
    @Column(name = "bankbranchname", length = 512)
    private String bankBranchName;

    @NotNull
    @Column(name = "isactive", nullable = false)
    private Boolean isActive;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)    @NotNull
    @JsonIgnoreProperties("")
    @JoinColumn(name="bankID")
    private Bank bankID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("")
    @JoinColumn(name="branchID")
    private OrganizationUnit branchID;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public BankAccountDetails bankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
        return this;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public BankAccountDetails bankName(String bankName) {
        this.bankName = bankName;
        return this;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAddress() {
        return address;
    }

    public BankAccountDetails address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public BankAccountDetails description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public BankAccountDetails bankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
        return this;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public Bank getBankID() {
        return bankID;
    }

    public BankAccountDetails bankID(Bank bank) {
        this.bankID = bank;
        return this;
    }

    public void setBankID(Bank bank) {
        this.bankID = bank;
    }

    public OrganizationUnit getBranchID() {
        return branchID;
    }

    public BankAccountDetails branchID(OrganizationUnit organizationUnit) {
        this.branchID = organizationUnit;
        return this;
    }

    public void setBranchID(OrganizationUnit organizationUnit) {
        this.branchID = organizationUnit;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
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
        BankAccountDetails bankAccountDetails = (BankAccountDetails) o;
        if (bankAccountDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bankAccountDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BankAccountDetails{" +
            "id=" + getId() +
            ", bankAccount='" + getBankAccount() + "'" +
            ", bankName='" + getBankName() + "'" +
            ", address='" + getAddress() + "'" +
            ", description='" + getDescription() + "'" +
            ", bankBranchName='" + getBankBranchName() + "'" +
            "}";
    }
}
