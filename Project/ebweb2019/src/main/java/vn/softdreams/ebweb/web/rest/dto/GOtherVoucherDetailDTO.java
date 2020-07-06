package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.StatisticsCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class GOtherVoucherDetailDTO {
    private UUID ID;
    private UUID gOtherVoucherID;
    private String description;
    private String debitAccount;
    private String creditAccount;
    private BigDecimal amount;
    private BigDecimal amountOriginal;
    private BigDecimal cashOutAmountVoucherOriginal;
    private BigDecimal cashOutAmountVoucher;
    private BigDecimal cashOutExchangeRateFB;
    private BigDecimal cashOutAmountFB;
    private BigDecimal cashOutDifferAmountFB;
    private String cashOutDifferAccountFB;
    private BigDecimal cashOutExchangeRateMB;
    private BigDecimal cashOutAmountMB;
    private BigDecimal cashOutDifferAmountMB;
    private String cashOutDifferAccountMB;
    private Boolean isMatch;
    private LocalDate matchDate;
    private Integer orderPriority;
    private String budgetItemCode;
    private String costSetCode;
    private String debitAccountingObjectCode;
    private String creditAccountingObjectCode;
    private String employeeCode;
    private String departmentCode;
    private String statisticsCode;
    private String expenseItemCode;
    private String contractCode;

    public GOtherVoucherDetailDTO() {
    }

    public GOtherVoucherDetailDTO(UUID ID, UUID gOtherVoucherID, String description, String debitAccount, String creditAccount, BigDecimal amount, BigDecimal amountOriginal, BigDecimal cashOutAmountVoucherOriginal, BigDecimal cashOutAmountVoucher, BigDecimal cashOutExchangeRateFB, BigDecimal cashOutAmountFB, BigDecimal cashOutDifferAmountFB, String cashOutDifferAccountFB, BigDecimal cashOutExchangeRateMB, BigDecimal cashOutAmountMB, BigDecimal cashOutDifferAmountMB, String cashOutDifferAccountMB, Boolean isMatch, LocalDate matchDate, Integer orderPriority, String budgetItemCode, String costSetCode, String debitAccountingObjectCode, String creditAccountingObjectCode, String employeeCode, String departmentCode, String statisticsCode, String expenseItemCode, String contractCode) {
        this.ID = ID;
        this.gOtherVoucherID = gOtherVoucherID;
        this.description = description;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.amount = amount;
        this.amountOriginal = amountOriginal;
        this.cashOutAmountVoucherOriginal = cashOutAmountVoucherOriginal;
        this.cashOutAmountVoucher = cashOutAmountVoucher;
        this.cashOutExchangeRateFB = cashOutExchangeRateFB;
        this.cashOutAmountFB = cashOutAmountFB;
        this.cashOutDifferAmountFB = cashOutDifferAmountFB;
        this.cashOutDifferAccountFB = cashOutDifferAccountFB;
        this.cashOutExchangeRateMB = cashOutExchangeRateMB;
        this.cashOutAmountMB = cashOutAmountMB;
        this.cashOutDifferAmountMB = cashOutDifferAmountMB;
        this.cashOutDifferAccountMB = cashOutDifferAccountMB;
        this.isMatch = isMatch;
        this.matchDate = matchDate;
        this.orderPriority = orderPriority;
        this.budgetItemCode = budgetItemCode;
        this.costSetCode = costSetCode;
        this.debitAccountingObjectCode = debitAccountingObjectCode;
        this.creditAccountingObjectCode = creditAccountingObjectCode;
        this.employeeCode = employeeCode;
        this.departmentCode = departmentCode;
        this.statisticsCode = statisticsCode;
        this.expenseItemCode = expenseItemCode;
        this.contractCode = contractCode;
    }

    public String getExpenseItemCode() {
        return expenseItemCode;
    }

    public void setExpenseItemCode(String expenseItemCode) {
        this.expenseItemCode = expenseItemCode;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public UUID getID() {
        return ID;
    }

    public void setID(UUID ID) {
        this.ID = ID;
    }

    public UUID getgOtherVoucherID() {
        return gOtherVoucherID;
    }

    public void setgOtherVoucherID(UUID gOtherVoucherID) {
        this.gOtherVoucherID = gOtherVoucherID;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
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

    public BigDecimal getCashOutExchangeRateFB() {
        return cashOutExchangeRateFB;
    }

    public void setCashOutExchangeRateFB(BigDecimal cashOutExchangeRateFB) {
        this.cashOutExchangeRateFB = cashOutExchangeRateFB;
    }

    public BigDecimal getCashOutAmountFB() {
        return cashOutAmountFB;
    }

    public void setCashOutAmountFB(BigDecimal cashOutAmountFB) {
        this.cashOutAmountFB = cashOutAmountFB;
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

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getBudgetItemCode() {
        return budgetItemCode;
    }

    public void setBudgetItemCode(String budgetItemCode) {
        this.budgetItemCode = budgetItemCode;
    }

    public String getCostSetCode() {
        return costSetCode;
    }

    public void setCostSetCode(String costSetCode) {
        this.costSetCode = costSetCode;
    }

    public String getDebitAccountingObjectCode() {
        return debitAccountingObjectCode;
    }

    public void setDebitAccountingObjectCode(String debitAccountingObjectCode) {
        this.debitAccountingObjectCode = debitAccountingObjectCode;
    }

    public String getCreditAccountingObjectCode() {
        return creditAccountingObjectCode;
    }

    public void setCreditAccountingObjectCode(String creditAccountingObjectCode) {
        this.creditAccountingObjectCode = creditAccountingObjectCode;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getStatisticsCode() {
        return statisticsCode;
    }

    public void setStatisticsCode(String statisticsCode) {
        this.statisticsCode = statisticsCode;
    }
}
