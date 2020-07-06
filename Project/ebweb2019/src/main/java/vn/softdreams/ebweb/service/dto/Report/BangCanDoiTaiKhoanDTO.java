package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.util.UUID;

public class BangCanDoiTaiKhoanDTO {
    private UUID accountID;
    private UUID id;
    private UUID parentAccountID;
    private Boolean isParentNode;
    private String accountNumber;
    private String accountName;
    private Integer accountCategoryKind;
    private BigDecimal openingDebitAmount;
    private BigDecimal openingCreditAmount;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private BigDecimal debitAmountAccum;
    private BigDecimal creditAmountAccum;
    private BigDecimal closingDebitAmount;
    private String closingDebitAmountString;
    private BigDecimal closingCreditAmount;
    private String closingCreditAmountString;
    private Integer grade;
    private String noDauKy;
    private String coDauKy;
    private String noPhatSinh;
    private String coPhatSinh;
    private String noCuoiKy;
    private String coCuoiKy;

    public BangCanDoiTaiKhoanDTO() {
    }

    public BangCanDoiTaiKhoanDTO(UUID accountID, String accountNumber, String accountName, Integer accountCategoryKind, BigDecimal openingDebitAmount, BigDecimal openingCreditAmount, BigDecimal debitAmount, BigDecimal creditAmount, BigDecimal debitAmountAccum, BigDecimal creditAmountAccum, BigDecimal closingDebitAmount, BigDecimal closingCreditAmount, Integer grade, UUID id, UUID parentAccountID, Boolean isParentNode) {
        this.accountID = accountID;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.accountCategoryKind = accountCategoryKind;
        this.openingDebitAmount = openingDebitAmount;
        this.openingCreditAmount = openingCreditAmount;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
        this.debitAmountAccum = debitAmountAccum;
        this.creditAmountAccum = creditAmountAccum;
        this.closingDebitAmount = closingDebitAmount;
        this.closingCreditAmount = closingCreditAmount;
        this.grade = grade;
        this.id = id;
        this.parentAccountID = parentAccountID;
        this.isParentNode = isParentNode;
    }

    public BangCanDoiTaiKhoanDTO(Integer accountCategoryKind, BigDecimal openingDebitAmount, BigDecimal openingCreditAmount, BigDecimal debitAmount, BigDecimal creditAmount, BigDecimal debitAmountAccum, BigDecimal creditAmountAccum, BigDecimal closingDebitAmount, BigDecimal closingCreditAmount, Integer grade) {
        this.accountCategoryKind = accountCategoryKind;
        this.openingDebitAmount = openingDebitAmount;
        this.openingCreditAmount = openingCreditAmount;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
        this.debitAmountAccum = debitAmountAccum;
        this.creditAmountAccum = creditAmountAccum;
        this.closingDebitAmount = closingDebitAmount;
        this.closingCreditAmount = closingCreditAmount;
        this.grade = grade;
    }

    public UUID getAccountID() {
        return accountID;
    }

    public void setAccountID(UUID accountID) {
        this.accountID = accountID;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getAccountCategoryKind() {
        return accountCategoryKind;
    }

    public void setAccountCategoryKind(Integer accountCategoryKind) {
        this.accountCategoryKind = accountCategoryKind;
    }

    public BigDecimal getOpeningDebitAmount() {
        return openingDebitAmount;
    }

    public void setOpeningDebitAmount(BigDecimal openingDebitAmount) {
        this.openingDebitAmount = openingDebitAmount;
    }

    public BigDecimal getOpeningCreditAmount() {
        return openingCreditAmount;
    }

    public void setOpeningCreditAmount(BigDecimal openingCreditAmount) {
        this.openingCreditAmount = openingCreditAmount;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getDebitAmountAccum() {
        return debitAmountAccum;
    }

    public void setDebitAmountAccum(BigDecimal debitAmountAccum) {
        this.debitAmountAccum = debitAmountAccum;
    }

    public BigDecimal getCreditAmountAccum() {
        return creditAmountAccum;
    }

    public void setCreditAmountAccum(BigDecimal creditAmountAccum) {
        this.creditAmountAccum = creditAmountAccum;
    }

    public BigDecimal getClosingDebitAmount() {
        return closingDebitAmount;
    }

    public void setClosingDebitAmount(BigDecimal closingDebitAmount) {
        this.closingDebitAmount = closingDebitAmount;
    }

    public BigDecimal getClosingCreditAmount() {
        return closingCreditAmount;
    }

    public void setClosingCreditAmount(BigDecimal closingCreditAmount) {
        this.closingCreditAmount = closingCreditAmount;
    }

    public String getNoDauKy() {
        return noDauKy;
    }

    public void setNoDauKy(String noDauKy) {
        this.noDauKy = noDauKy;
    }

    public String getCoDauKy() {
        return coDauKy;
    }

    public void setCoDauKy(String coDauKy) {
        this.coDauKy = coDauKy;
    }

    public String getNoPhatSinh() {
        return noPhatSinh;
    }

    public void setNoPhatSinh(String noPhatSinh) {
        this.noPhatSinh = noPhatSinh;
    }

    public String getCoPhatSinh() {
        return coPhatSinh;
    }

    public void setCoPhatSinh(String coPhatSinh) {
        this.coPhatSinh = coPhatSinh;
    }

    public String getNoCuoiKy() {
        return noCuoiKy;
    }

    public void setNoCuoiKy(String noCuoiKy) {
        this.noCuoiKy = noCuoiKy;
    }

    public String getCoCuoiKy() {
        return coCuoiKy;
    }

    public void setCoCuoiKy(String coCuoiKy) {
        this.coCuoiKy = coCuoiKy;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getParentAccountID() {
        return parentAccountID;
    }

    public void setParentAccountID(UUID parentAccountID) {
        this.parentAccountID = parentAccountID;
    }

    public Boolean getIsParentNode() {
        return isParentNode;
    }

    public void setIsParentNode(Boolean parentNode) {
        isParentNode = parentNode;
    }

    public Boolean getParentNode() {
        return isParentNode;
    }

    public void setParentNode(Boolean parentNode) {
        isParentNode = parentNode;
    }

    public String getClosingDebitAmountString() {
        return closingDebitAmountString;
    }

    public void setClosingDebitAmountString(String closingDebitAmountString) {
        this.closingDebitAmountString = closingDebitAmountString;
    }

    public String getClosingCreditAmountString() {
        return closingCreditAmountString;
    }

    public void setClosingCreditAmountString(String closingCreditAmountString) {
        this.closingCreditAmountString = closingCreditAmountString;
    }
}
