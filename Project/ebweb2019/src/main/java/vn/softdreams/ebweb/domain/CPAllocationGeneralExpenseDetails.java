package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.CPAllocationExpenseDTO;
import vn.softdreams.ebweb.service.dto.CPAllocationExpenseDetailDTO;
import vn.softdreams.ebweb.web.rest.dto.CPPeriodDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A CPAllocationGeneralExpenseDetails.
 */
@Entity
@Table(name = "cpallocationgeneralexpensedetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "CPAllocationExpenseDetailDTO",
        classes = {
            @ConstructorResult(
                targetClass = CPAllocationExpenseDetailDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "cPPeriodID", type = UUID.class),
                    @ColumnResult(name = "cPAllocationGeneralExpenseID", type = UUID.class),
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "costSetName", type = String.class),
                    @ColumnResult(name = "accountNumber", type = String.class),
                    @ColumnResult(name = "expenseItemID", type = UUID.class),
                    @ColumnResult(name = "expenseItemCode", type = String.class),
                    @ColumnResult(name = "allocatedRate", type = BigDecimal.class),
                    @ColumnResult(name = "allocatedAmount", type = BigDecimal.class),
                    @ColumnResult(name = "expenseItemType", type = Integer.class)
                }
            )
        }
    )
})
public class CPAllocationGeneralExpenseDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "cpperiodid")
    private UUID cPPeriodID;

    @Column(name = "cpallocationgeneralexpenseid")
    private UUID cPAllocationGeneralExpenseID;

    @Column(name = "costsetid")
    private UUID costSetID;

    @Column(name = "contractid")
    private UUID contractID;

    @Size(max = 25)
    @Column(name = "accountnumber", length = 25)
    private String accountNumber;

    @Column(name = "expenseitemid")
    private UUID expenseItemID;

    @Column(name = "allocatedrate", precision = 10, scale = 2)
    private BigDecimal allocatedRate;

    @Column(name = "allocatedamount", precision = 10, scale = 2)
    private BigDecimal allocatedAmount;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Integer expenseItemType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getcPAllocationGeneralExpenseID() {
        return cPAllocationGeneralExpenseID;
    }

    public CPAllocationGeneralExpenseDetails cPAllocationGeneralExpenseID(UUID cPAllocationGeneralExpenseID) {
        this.cPAllocationGeneralExpenseID = cPAllocationGeneralExpenseID;
        return this;
    }

    public void setcPAllocationGeneralExpenseID(UUID cPAllocationGeneralExpenseID) {
        this.cPAllocationGeneralExpenseID = cPAllocationGeneralExpenseID;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public CPAllocationGeneralExpenseDetails costSetID(UUID costSetID) {
        this.costSetID = costSetID;
        return this;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public UUID getContractID() {
        return contractID;
    }

    public CPAllocationGeneralExpenseDetails contractID(UUID contractID) {
        this.contractID = contractID;
        return this;
    }

    public void setContractID(UUID contractID) {
        this.contractID = contractID;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public CPAllocationGeneralExpenseDetails accountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public CPAllocationGeneralExpenseDetails expenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
        return this;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public BigDecimal getAllocatedRate() {
        return allocatedRate;
    }

    public CPAllocationGeneralExpenseDetails allocatedRate(BigDecimal allocatedRate) {
        this.allocatedRate = allocatedRate;
        return this;
    }

    public void setAllocatedRate(BigDecimal allocatedRate) {
        this.allocatedRate = allocatedRate;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public CPAllocationGeneralExpenseDetails allocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
        return this;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public Integer getExpenseItemType() {
        return expenseItemType;
    }

    public void setExpenseItemType(Integer expenseItemType) {
        this.expenseItemType = expenseItemType;
    }

    public UUID getcPPeriodID() {
        return cPPeriodID;
    }

    public void setcPPeriodID(UUID cPPeriodID) {
        this.cPPeriodID = cPPeriodID;
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
        CPAllocationGeneralExpenseDetails cPAllocationGeneralExpenseDetails = (CPAllocationGeneralExpenseDetails) o;
        if (cPAllocationGeneralExpenseDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cPAllocationGeneralExpenseDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CPAllocationGeneralExpenseDetails{" +
            "id=" + getId() +
            ", cPAllocationGeneralExpenseID=" + getcPAllocationGeneralExpenseID() +
            ", costSetID=" + getCostSetID() +
            ", contractID=" + getContractID() +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", expenseItemID=" + getExpenseItemID() +
            ", allocatedRate=" + getAllocatedRate() +
            ", allocatedAmount=" + getAllocatedAmount() +
            "}";
    }
}
