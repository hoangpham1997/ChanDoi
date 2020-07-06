package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class KetChuyenChiPhiDTO {
    private UUID costSetID;
    private UUID contractID;
    private String costSetCode;
    private String costSetName;
    private String reason;
    private String debitAccount;
    private String creditAccount;
    private BigDecimal amount;
    private UUID statisticsCodeID;
    private UUID expenseItemID;

    public KetChuyenChiPhiDTO() {
    }

    public KetChuyenChiPhiDTO(UUID costSetID, UUID contractID,String costSetCode, String costSetName, String reason, String debitAccount, String creditAccount, BigDecimal amount, UUID statisticsCodeID, UUID expenseItemID) {
        this.costSetID = costSetID;
        this.contractID = contractID;
        this.costSetCode = costSetCode;
        this.costSetName = costSetName;
        this.reason = reason;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.amount = amount;
        this.statisticsCodeID = statisticsCodeID;
        this.expenseItemID = expenseItemID;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public String getReason() {
        return reason;
    }

    public String getCostSetName() {
        return costSetName;
    }

    public void setCostSetName(String costSetName) {
        this.costSetName = costSetName;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UUID getStatisticsCodeID() {
        return statisticsCodeID;
    }

    public void setStatisticsCodeID(UUID statisticsCodeID) {
        this.statisticsCodeID = statisticsCodeID;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public UUID getContractID() {
        return contractID;
    }

    public void setContractID(UUID contractID) {
        this.contractID = contractID;
    }

    public String getCostSetCode() {
        return costSetCode;
    }

    public void setCostSetCode(String costSetCode) {
        this.costSetCode = costSetCode;
    }
}
