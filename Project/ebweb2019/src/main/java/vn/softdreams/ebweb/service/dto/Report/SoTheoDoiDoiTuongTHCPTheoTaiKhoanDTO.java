package vn.softdreams.ebweb.service.dto.Report;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SoTheoDoiDoiTuongTHCPTheoTaiKhoanDTO {
    private UUID refID;
    private Integer typeID;
    private String costSetCode;
    private String costSetName;
    private LocalDate date;
    private LocalDate dateHT;
    private String no;
    private String reason;
    private String account;
    private String accountCorresponding;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;

    private String costSetCodeString;
    private String costSetNameString;
    private String noString;
    private String reasonString;
    private String accountString;
    private String accountCorrespondingString;
    private String totalCreditAmountString;
    private String totalDebitAmountString;
    private String debitAmountString;
    private String creditAmountString;
    private String linkRef;
    private String dateString;
    private String dateHTString;
    private Integer page;

    public SoTheoDoiDoiTuongTHCPTheoTaiKhoanDTO() {
    }

    public SoTheoDoiDoiTuongTHCPTheoTaiKhoanDTO(String costSetCodeString, String costSetNameString, String noString, String reasonString, String accountString, String accountCorrespondingString, String totalCreditAmountString, String totalDebitAmountString, String debitAmountString, String creditAmountString, String linkRef, String dateString) {
        this.costSetCodeString = costSetCodeString;
        this.costSetNameString = costSetNameString;
        this.noString = noString;
        this.reasonString = reasonString;
        this.accountString = accountString;
        this.accountCorrespondingString = accountCorrespondingString;
        this.totalCreditAmountString = totalCreditAmountString;
        this.totalDebitAmountString = totalDebitAmountString;
        this.debitAmountString = debitAmountString;
        this.creditAmountString = creditAmountString;
        this.linkRef = linkRef;
        this.dateString = dateString;
    }

    public SoTheoDoiDoiTuongTHCPTheoTaiKhoanDTO(UUID refID, Integer typeID, String costSetCode, String costSetName, LocalDate date, LocalDate dateHT, String no, String reason, String account, String accountCorresponding, BigDecimal debitAmount, BigDecimal creditAmount) {
        this.refID = refID;
        this.typeID = typeID;
        this.costSetCode = costSetCode;
        this.costSetName = costSetName;
        this.date = date;
        this.dateHT = dateHT;
        this.no = no;
        this.reason = reason;
        this.account = account;
        this.accountCorresponding = accountCorresponding;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
    }

    public String getDateHTString() {
        return dateHTString;
    }

    public void setDateHTString(String dateHTString) {
        this.dateHTString = dateHTString;
    }

    public LocalDate getDateHT() {
        return dateHT;
    }

    public void setDateHT(LocalDate dateHT) {
        this.dateHT = dateHT;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public UUID getRefID() {
        return refID;
    }

    public void setRefID(UUID refID) {
        this.refID = refID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountCorresponding() {
        return accountCorresponding;
    }

    public void setAccountCorresponding(String accountCorresponding) {
        this.accountCorresponding = accountCorresponding;
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

    public String getCostSetCodeString() {
        return costSetCodeString;
    }

    public void setCostSetCodeString(String costSetCodeString) {
        this.costSetCodeString = costSetCodeString;
    }

    public String getCostSetNameString() {
        return costSetNameString;
    }

    public void setCostSetNameString(String costSetNameString) {
        this.costSetNameString = costSetNameString;
    }

    public String getNoString() {
        return noString;
    }

    public void setNoString(String noString) {
        this.noString = noString;
    }

    public String getReasonString() {
        return reasonString;
    }

    public void setReasonString(String reasonString) {
        this.reasonString = reasonString;
    }

    public String getAccountString() {
        return accountString;
    }

    public void setAccountString(String accountString) {
        this.accountString = accountString;
    }

    public String getAccountCorrespondingString() {
        return accountCorrespondingString;
    }

    public void setAccountCorrespondingString(String accountCorrespondingString) {
        this.accountCorrespondingString = accountCorrespondingString;
    }

    public String getTotalCreditAmountString() {
        return totalCreditAmountString;
    }

    public void setTotalCreditAmountString(String totalCreditAmountString) {
        this.totalCreditAmountString = totalCreditAmountString;
    }

    public String getTotalDebitAmountString() {
        return totalDebitAmountString;
    }

    public void setTotalDebitAmountString(String totalDebitAmountString) {
        this.totalDebitAmountString = totalDebitAmountString;
    }

    public String getDebitAmountString() {
        return debitAmountString;
    }

    public void setDebitAmountString(String debitAmountString) {
        this.debitAmountString = debitAmountString;
    }

    public String getCreditAmountString() {
        return creditAmountString;
    }

    public void setCreditAmountString(String creditAmountString) {
        this.creditAmountString = creditAmountString;
    }

    public String getLinkRef() {
        return linkRef;
    }

    public void setLinkRef(String linkRef) {
        this.linkRef = linkRef;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }
}
