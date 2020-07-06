package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.CPAcceptanceDetailDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A CPAcceptanceDetails.
 */
@Entity
@Table(name = "cpacceptancedetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "CPAcceptanceDetailDTO",
        classes = {
            @ConstructorResult(
                targetClass = CPAcceptanceDetailDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "cPAcceptanceID", type = UUID.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "revenueAmount", type = BigDecimal.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "acceptedRate", type = BigDecimal.class),
                    @ColumnResult(name = "totalAcceptedAmount", type = BigDecimal.class),
                    @ColumnResult(name = "expenseItemID", type = UUID.class),
                    @ColumnResult(name = "statisticsCodeID", type = UUID.class),
                    @ColumnResult(name = "cPPeriodID", type = UUID.class),
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "costSetName", type = String.class),
                }
            )
        }
    )})
public class CPAcceptanceDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "cpacceptanceid")
    private UUID cPAcceptanceID;

    @Column(name = "description")
    private String description;

    @Column(name = "debitaccount")
    private String debitAccount;

    @Column(name = "creditaccount")
    private String creditAccount;

    @Column(name = "revenueamount")
    private BigDecimal revenueAmount;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "acceptedrate")
    private BigDecimal acceptedRate;

    @Column(name = "totalacceptedamount")
    private BigDecimal totalAcceptedAmount;

    @Column(name = "expenseitemid")
    private UUID expenseItemID;

    @Column(name = "statisticscodeid")
    private UUID statisticsCodeID;

    @Column(name = "cpperiodid")
    private UUID cPPeriodID;

    @Column(name = "costsetid")
    private UUID costSetID;

    @Column(name = "contractid")
    private UUID contractID;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getcPAcceptanceID() {
        return cPAcceptanceID;
    }

    public void setcPAcceptanceID(UUID cPAcceptanceID) {
        this.cPAcceptanceID = cPAcceptanceID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public BigDecimal getRevenueAmount() {
        return revenueAmount;
    }

    public void setRevenueAmount(BigDecimal revenueAmount) {
        this.revenueAmount = revenueAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAcceptedRate() {
        return acceptedRate;
    }

    public void setAcceptedRate(BigDecimal acceptedRate) {
        this.acceptedRate = acceptedRate;
    }

    public BigDecimal getTotalAcceptedAmount() {
        return totalAcceptedAmount;
    }

    public void setTotalAcceptedAmount(BigDecimal totalAcceptedAmount) {
        this.totalAcceptedAmount = totalAcceptedAmount;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public UUID getStatisticsCodeID() {
        return statisticsCodeID;
    }

    public void setStatisticsCodeID(UUID statisticsCodeID) {
        this.statisticsCodeID = statisticsCodeID;
    }

    public UUID getcPPeriodID() {
        return cPPeriodID;
    }

    public void setcPPeriodID(UUID cPPeriodID) {
        this.cPPeriodID = cPPeriodID;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public UUID getContractID() {
        return contractID;
    }

    public void setContractID(UUID contractID) {
        this.contractID = contractID;
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
        CPAcceptanceDetails cPAcceptanceDetails = (CPAcceptanceDetails) o;
        if (cPAcceptanceDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cPAcceptanceDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CPAcceptanceDetails{" +
            "id=" + getId() +
            "}";
    }
}
