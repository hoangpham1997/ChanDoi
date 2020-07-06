package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.util.UUID;

public class TongHopCPTheoKMCPDTO {
    private Integer stt;
    private UUID expenseItemID;
    private String expenseItemCode;
    private String expenseItemName;
    private String account;
    private BigDecimal amount;
    private String amountToString;
    private String amountSumToString;

    public TongHopCPTheoKMCPDTO() {
    }

    public TongHopCPTheoKMCPDTO(UUID expenseItemID, String expenseItemCode, String expenseItemName, String account, BigDecimal amount) {
        this.expenseItemID = expenseItemID;
        this.expenseItemCode = expenseItemCode;
        this.expenseItemName = expenseItemName;
        this.account = account;
        this.amount = amount;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public String getExpenseItemCode() {
        return expenseItemCode;
    }

    public void setExpenseItemCode(String expenseItemCode) {
        this.expenseItemCode = expenseItemCode;
    }

    public String getExpenseItemName() {
        return expenseItemName;
    }

    public void setExpenseItemName(String expenseItemName) {
        this.expenseItemName = expenseItemName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAmountToString() {
        return amountToString;
    }

    public void setAmountToString(String amountToString) {
        this.amountToString = amountToString;
    }

    public Integer getStt() {
        return stt;
    }

    public void setStt(Integer stt) {
        this.stt = stt;
    }

    public String getAmountSumToString() {
        return amountSumToString;
    }

    public void setAmountSumToString(String amountSumToString) {
        this.amountSumToString = amountSumToString;
    }
}

