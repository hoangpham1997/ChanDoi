package vn.softdreams.ebweb.service.dto;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.UUID;

public class CPAcceptanceDetailDTO {
    private UUID id;
    private UUID cPAcceptanceID;
    private String description;
    private String debitAccount;
    private String creditAccount;
    private BigDecimal revenueAmount;
    private BigDecimal amount;
    private BigDecimal acceptedRate;
    private BigDecimal totalAcceptedAmount;
    private UUID expenseItemID;
    private UUID statisticsCodeID;
    private UUID cPPeriodID;
    private UUID costSetID;
    private String costSetCode;
    private String costSetName;
    private UUID contractID;

    public CPAcceptanceDetailDTO(UUID id, UUID cPAcceptanceID, String description, String debitAccount, String creditAccount, BigDecimal revenueAmount, BigDecimal amount, BigDecimal acceptedRate, BigDecimal totalAcceptedAmount, UUID expenseItemID, UUID statisticsCodeID, UUID cPPeriodID, UUID costSetID, String costSetCode, String costSetName) {
        this.id = id;
        this.cPAcceptanceID = cPAcceptanceID;
        this.description = description;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.revenueAmount = revenueAmount;
        this.amount = amount;
        this.acceptedRate = acceptedRate;
        this.totalAcceptedAmount = totalAcceptedAmount;
        this.expenseItemID = expenseItemID;
        this.statisticsCodeID = statisticsCodeID;
        this.cPPeriodID = cPPeriodID;
        this.costSetID = costSetID;
        this.costSetCode = costSetCode;
        this.costSetName = costSetName;
    }

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

    public String getCostSetCode() {
        return costSetCode;
    }

    public void setCostSetCode(String costSetCode) {
        this.costSetCode = costSetCode;
    }

    public String getCostSetName() {
        return costSetName;
    }

    public void setCostSetName(String costSetName) {
        this.costSetName = costSetName;
    }

    public UUID getContractID() {
        return contractID;
    }

    public void setContractID(UUID contractID) {
        this.contractID = contractID;
    }
}

