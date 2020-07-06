package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.GOtherVoucherDetailKcDTO;
import vn.softdreams.ebweb.service.dto.PPPayVendorDTO;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherDetailDTO;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherDetailDataKcDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A GOtherVoucherDetails.
 */
@Entity
@Table(name = "gothervoucherdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "GOtherVoucherDetailKcDTO",
        classes = {
            @ConstructorResult(
                targetClass = GOtherVoucherDetailKcDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "GOtherVoucherDetailDTO",
        classes = {
            @ConstructorResult(
                targetClass = GOtherVoucherDetailDTO.class,
                columns = {
                    @ColumnResult(name = "ID", type = UUID.class),
                    @ColumnResult(name = "gOtherVoucherID", type = UUID.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "amountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "cashOutAmountVoucherOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "cashOutAmountVoucher", type = BigDecimal.class),
                    @ColumnResult(name = "cashOutExchangeRateFB", type = BigDecimal.class),
                    @ColumnResult(name = "cashOutAmountFB", type = BigDecimal.class),
                    @ColumnResult(name = "cashOutDifferAmountFB", type = BigDecimal.class),
                    @ColumnResult(name = "cashOutDifferAccountFB", type = String.class),
                    @ColumnResult(name = "cashOutExchangeRateMB", type = BigDecimal.class),
                    @ColumnResult(name = "cashOutAmountMB", type = BigDecimal.class),
                    @ColumnResult(name = "cashOutDifferAmountMB", type = BigDecimal.class),
                    @ColumnResult(name = "cashOutDifferAccountMB", type = String.class),
                    @ColumnResult(name = "isMatch", type = Boolean.class),
                    @ColumnResult(name = "matchDate", type = LocalDate.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
                    @ColumnResult(name = "budgetItemCode", type = String.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "debitAccountingObjectCode", type = String.class),
                    @ColumnResult(name = "creditAccountingObjectCode", type = String.class),
                    @ColumnResult(name = "employeeCode", type = String.class),
                    @ColumnResult(name = "departmentCode", type = String.class),
                    @ColumnResult(name = "statisticsCodeCode", type = String.class),
                    @ColumnResult(name = "expenseItemCode", type = String.class),
                    @ColumnResult(name = "contractCode", type = String.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "GOtherVoucherDetailDataKcDTO",
        classes = {
            @ConstructorResult(
                targetClass = GOtherVoucherDetailDataKcDTO.class,
                columns = {
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "fromAccountData", type = Integer.class),
                    @ColumnResult(name = "fromAccount", type = String.class),
                    @ColumnResult(name = "isDebit", type = Boolean.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "GOtherVoucherDetailDataKcDiffDTO",
        classes = {
            @ConstructorResult(
                targetClass = GOtherVoucherDetailDataKcDTO.class,
                columns = {
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "fromAccountData", type = Integer.class)
                }
            )
        }
    )
})
public class GOtherVoucherDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "sequenceGenerator")
    private UUID id;

    @Column(name = "gothervoucherid")
    private UUID gOtherVoucherID;

    @Column(name = "description")
    private String description;

    @Column(name = "debitaccount")
    private String debitAccount;

    @Column(name = "creditaccount")
    private String creditAccount;

    @Column(name = "amountoriginal", precision = 10, scale = 2)
    private BigDecimal amountOriginal;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "debitaccountingobjectid")
    private UUID debitAccountingObjectID;

    @Column(name = "creditaccountingobjectid")
    private UUID creditAccountingObjectID;

    @Column(name = "employeeid")
    private UUID employeeID;

    @Column(name = "bankaccountdetailid")
    private UUID bankAccountDetailID;

    @Column(name = "expenseitemid")
    private UUID expenseItemID;

    @Column(name = "costsetid")
    private UUID costSetID;

    @Column(name = "contractid")
    private UUID contractID;

    @Column(name = "budgetitemid")
    private UUID budgetItemID;

    @Column(name = "departmentid")
    private UUID departmentID;

    @Column(name = "statisticscodeid")
    private UUID statisticCodeID;

    @Column(name = "cashoutamountvoucheroriginal", precision = 10, scale = 2)
    private BigDecimal cashOutAmountVoucherOriginal;

    @Column(name = "cashoutamountvoucher", precision = 10, scale = 2)
    private BigDecimal cashOutAmountVoucher;

    @Column(name = "cashoutexchangeratefb", precision = 10, scale = 2)
    private BigDecimal cashOutExchangeRateFB;

    @Column(name = "cashoutamountfb", precision = 10, scale = 2)
    private BigDecimal cashOutAmountFB;

    @Column(name = "cashoutdifferamountfb", precision = 10, scale = 2)
    private BigDecimal cashOutDifferAmountFB;

    @Column(name = "cashoutdifferaccountfb")
    private String cashOutDifferAccountFB;

    @Column(name = "cashoutexchangeratemb", precision = 10, scale = 2)
    private BigDecimal cashOutExchangeRateMB;

    @Column(name = "cashoutamountmb", precision = 10, scale = 2)
    private BigDecimal cashOutAmountMB;

    @Column(name = "cashoutdifferamountmb", precision = 10, scale = 2)
    private BigDecimal cashOutDifferAmountMB;

    @Column(name = "cashoutdifferaccountmb")
    private String cashOutDifferAccountMB;

    @Column(name = "ismatch")
    private Boolean isMatch;

    @Column(name = "matchdate")
    private LocalDate matchDate;

    @Column(name = "orderpriority", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer orderPriority;

    @Transient
    private String amountOriginalToString;

    @Transient
    private String amountToString;

    public String getAmountOriginalToString() {
        return amountOriginalToString;
    }

    public void setAmountOriginalToString(String amountOriginalToString) {
        this.amountOriginalToString = amountOriginalToString;
    }

    public String getAmountToString() {
        return amountToString;
    }

    public void setAmountToString(String amountToString) {
        this.amountToString = amountToString;
    }

    public BigDecimal getCashOutExchangeRateFB() {
        return cashOutExchangeRateFB;
    }

    public void setCashOutExchangeRateFB(BigDecimal cashOutExchangeRateFB) {
        this.cashOutExchangeRateFB = cashOutExchangeRateFB;
    }

    public BigDecimal getCashOutDifferAmountFB() {
        return cashOutDifferAmountFB;
    }

    public void setCashOutDifferAmountFB(BigDecimal cashOutDifferAmountFB) {
        this.cashOutDifferAmountFB = cashOutDifferAmountFB;
    }

    public String getCashOutDifferAccountFB() {
        return cashOutDifferAccountFB;
    }

    public void setCashOutDifferAccountFB(String cashOutDifferAccountFB) {
        this.cashOutDifferAccountFB = cashOutDifferAccountFB;
    }

    public BigDecimal getCashOutExchangeRateMB() {
        return cashOutExchangeRateMB;
    }

    public void setCashOutExchangeRateMB(BigDecimal cashOutExchangeRateMB) {
        this.cashOutExchangeRateMB = cashOutExchangeRateMB;
    }

    public BigDecimal getCashOutAmountMB() {
        return cashOutAmountMB;
    }

    public void setCashOutAmountMB(BigDecimal cashOutAmountMB) {
        this.cashOutAmountMB = cashOutAmountMB;
    }

    public BigDecimal getCashOutDifferAmountMB() {
        return cashOutDifferAmountMB;
    }

    public void setCashOutDifferAmountMB(BigDecimal cashOutDifferAmountMB) {
        this.cashOutDifferAmountMB = cashOutDifferAmountMB;
    }

    public String getCashOutDifferAccountMB() {
        return cashOutDifferAccountMB;
    }

    public void setCashOutDifferAccountMB(String cashOutDifferAccountMB) {
        this.cashOutDifferAccountMB = cashOutDifferAccountMB;
    }

    public Boolean getMatch() {
        return isMatch;
    }

    public void setMatch(Boolean match) {
        isMatch = match;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public GOtherVoucherDetails description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public GOtherVoucherDetails debitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
        return this;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public GOtherVoucherDetails creditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
        return this;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public GOtherVoucherDetails amountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
        return this;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public GOtherVoucherDetails amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UUID getDebitAccountingObjectID() {
        return debitAccountingObjectID;
    }

    public GOtherVoucherDetails debitAccountingObjectID(UUID debitAccountingObjectID) {
        this.debitAccountingObjectID = debitAccountingObjectID;
        return this;
    }

    public void setDebitAccountingObjectID(UUID debitAccountingObjectID) {
        this.debitAccountingObjectID = debitAccountingObjectID;
    }

    public UUID getCreditAccountingObjectID() {
        return creditAccountingObjectID;
    }

    public GOtherVoucherDetails creditAccountingObjectID(UUID creditAccountingObjectID) {
        this.creditAccountingObjectID = creditAccountingObjectID;
        return this;
    }

    public void setCreditAccountingObjectID(UUID creditAccountingObjectID) {
        this.creditAccountingObjectID = creditAccountingObjectID;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public UUID getContractID() {
        return contractID;
    }

    public void setContractID(UUID contractID) {
        this.contractID = contractID;
    }

    public UUID getBudgetItemID() {
        return budgetItemID;
    }

    public void setBudgetItemID(UUID budgetItemID) {
        this.budgetItemID = budgetItemID;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public UUID getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(UUID departmentID) {
        this.departmentID = departmentID;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public UUID getBankAccountDetailID() {
        return bankAccountDetailID;
    }

    public void setBankAccountDetailID(UUID bankAccountDetailID) {
        this.bankAccountDetailID = bankAccountDetailID;
    }

    public UUID getStatisticCodeID() {
        return statisticCodeID;
    }

    public void setStatisticCodeID(UUID statisticCodeID) {
        this.statisticCodeID = statisticCodeID;
    }

    public UUID getgOtherVoucherID() {
        return gOtherVoucherID;
    }

    public void setgOtherVoucherID(UUID gOtherVoucherID) {
        this.gOtherVoucherID = gOtherVoucherID;
    }

    public BigDecimal getCashOutAmountFB() {
        return cashOutAmountFB;
    }

    public void setCashOutAmountFB(BigDecimal cashOutAmountFB) {
        this.cashOutAmountFB = cashOutAmountFB;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
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
        GOtherVoucherDetails gOtherVoucherDetails = (GOtherVoucherDetails) o;
        if (gOtherVoucherDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gOtherVoucherDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GOtherVoucherDetails{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", debitAccount='" + getDebitAccount() + "'" +
            ", creditAccount='" + getCreditAccount() + "'" +
            ", amountOriginal=" + getAmountOriginal() +
            ", amount=" + getAmount() +
//            ", invoiceTemplate='" + getInvoiceTemplate() + "'" +
            ", debitAccountingObjectID='" + getDebitAccountingObjectID() + "'" +
            ", creditAccountingObjectID='" + getCreditAccountingObjectID() + "'" +
            ", employeeID='" + getEmployeeID() + "'" +
            ", bankAccountDetailID='" + getBankAccountDetailID() + "'" +
            ", expenseItemID='" + getExpenseItemID() + "'" +
            ", costSetID='" + getCostSetID() + "'" +
            ", contractID='" + getContractID() + "'" +
            ", budgetItemID='" + getBudgetItemID() + "'" +
            ", departmentID='" + getDepartmentID() + "'" +
            ", statisticCodeID='" + getStatisticCodeID() + "'" +
            "}";
    }

    public BigDecimal getCashOutAmountVoucherOriginal() {
        return cashOutAmountVoucherOriginal;
    }

    public void setCashOutAmountVoucherOriginal(BigDecimal cashOutAmountVoucherOriginal) {
        this.cashOutAmountVoucherOriginal = cashOutAmountVoucherOriginal;
    }

    public BigDecimal getCashOutAmountVoucher() {
        return cashOutAmountVoucher;
    }

    public void setCashOutAmountVoucher(BigDecimal cashOutAmountVoucher) {
        this.cashOutAmountVoucher = cashOutAmountVoucher;
    }
}
