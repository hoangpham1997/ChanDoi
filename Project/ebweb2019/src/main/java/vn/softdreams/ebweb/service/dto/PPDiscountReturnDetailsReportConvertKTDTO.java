package vn.softdreams.ebweb.service.dto;
import java.math.BigDecimal;

public class PPDiscountReturnDetailsReportConvertKTDTO {
    private String description;
    private String creditAccount;
    private String debitAccount;
    private BigDecimal amount;
    private BigDecimal amountOriginal;
    private Long orderPriority;
    private Long exchangeRate;
    private Boolean checkVAT;
    private String amountToString;
    private String amountOriginalToString;

    public PPDiscountReturnDetailsReportConvertKTDTO() {
    }

    public PPDiscountReturnDetailsReportConvertKTDTO(String description, String creditAccount, String debitAccount, BigDecimal amount, BigDecimal amountOriginal, Long orderPriority, Long exchangeRate, Boolean checkVAT) {
        this.description = description;
        this.creditAccount = creditAccount;
        this.debitAccount = debitAccount;
        this.amount = amount;
        this.amountOriginal = amountOriginal;
        this.orderPriority = orderPriority;
        this.exchangeRate = exchangeRate;
        this.checkVAT = checkVAT;
    }

    public Long getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Long exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Long getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Long orderPriority) {
        this.orderPriority = orderPriority;
    }

    public Boolean getCheckVAT() {
        return checkVAT;
    }

    public void setCheckVAT(Boolean checkVAT) {
        this.checkVAT = checkVAT;
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

    public String getAmountToString() {
        return amountToString;
    }

    public void setAmountToString(String amountToString) {
        this.amountToString = amountToString;
    }

    public String getAmountOriginalToString() {
        return amountOriginalToString;
    }

    public void setAmountOriginalToString(String amountOriginalToString) {
        this.amountOriginalToString = amountOriginalToString;
    }
}
