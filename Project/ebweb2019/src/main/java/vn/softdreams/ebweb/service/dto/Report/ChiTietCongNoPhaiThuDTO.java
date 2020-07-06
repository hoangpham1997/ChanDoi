package vn.softdreams.ebweb.service.dto.Report;

import io.swagger.models.refs.RefType;

import java.math.BigDecimal;
import java.util.UUID;

public class ChiTietCongNoPhaiThuDTO {
    private Integer refType;
    private String postedDate;
    private String refDate;
    private UUID refID;
    private String refNo;
    private String description;
    private String accountNumber;
    private String correspondingAccountNumber;
    private BigDecimal debitAmountOC;
    private BigDecimal debitAmount;
    private BigDecimal creditAmountOC;
    private BigDecimal creditAmount;
    private BigDecimal closingDebitAmountOC;
    private BigDecimal closingDebitAmount;
    private BigDecimal closingCreditAmountOC;
    private BigDecimal closingCreditAmount;
    private String accountObjectCode;
    private String accountObjectName;
    private UUID accountObjectID;
    private long ordercode;
    private String debitAmountOCToString;
    private String debitAmountToString;
    private String creditAmountOCToString;
    private String creditAmountToString;
    private String closingDebitAmountOCToString;
    private String closingDebitAmountToString;
    private String closingCreditAmountOCToString;
    private String closingCreditAmountToString;
    private String totalDebitAmountOCToString;
    private String totalDebitAmountToString;
    private String totalCreditAmountOCToString;
    private String totalCreditAmountToString;
    private String totalClosingDebitAmountOCToString;
    private String totalClosingDebitAmountToString;
    private String totalClosingCreditAmountOCToString;
    private String totalClosingCreditAmountToString;
    private String linkRef;

    public ChiTietCongNoPhaiThuDTO() {
    }

    public ChiTietCongNoPhaiThuDTO(Integer refType, String postedDate, String refDate, UUID refID, String refNo, String description, String accountNumber, String correspondingAccountNumber, BigDecimal debitAmountOC, BigDecimal debitAmount, BigDecimal creditAmountOC, BigDecimal creditAmount, BigDecimal closingDebitAmountOC, BigDecimal closingDebitAmount, BigDecimal closingCreditAmountOC, BigDecimal closingCreditAmount, String accountObjectCode, String accountObjectName, UUID accountObjectID, long ordercode) {
        this.refType = refType;
        this.postedDate = postedDate;
        this.refID = refID;
        this.refDate = refDate;
        this.refNo = refNo;
        this.description = description;
        this.accountNumber = accountNumber;
        this.correspondingAccountNumber = correspondingAccountNumber;
        this.debitAmountOC = debitAmountOC;
        this.debitAmount = debitAmount;
        this.creditAmountOC = creditAmountOC;
        this.creditAmount = creditAmount;
        this.closingDebitAmountOC = closingDebitAmountOC;
        this.closingDebitAmount = closingDebitAmount;
        this.closingCreditAmountOC = closingCreditAmountOC;
        this.closingCreditAmount = closingCreditAmount;
        this.accountObjectCode = accountObjectCode;
        this.accountObjectName = accountObjectName;
        this.accountObjectID = accountObjectID;
        this.ordercode = ordercode;
    }

    public UUID getAccountObjectID() {
        return accountObjectID;
    }

    public void setAccountObjectID(UUID accountObjectID) {
        this.accountObjectID = accountObjectID;
    }

    public Integer getRefType() {
        return refType;
    }

    public void setRefType(Integer refType) {
        this.refType = refType;
    }

    public UUID getRefID() {
        return refID;
    }

    public void setRefID(UUID refID) {
        this.refID = refID;
    }

    public String getLinkRef() {
        return linkRef;
    }

    public void setLinkRef(String linkRef) {
        this.linkRef = linkRef;
    }

    public String getTotalDebitAmountOCToString() {
        return totalDebitAmountOCToString;
    }

    public void setTotalDebitAmountOCToString(String totalDebitAmountOCToString) {
        this.totalDebitAmountOCToString = totalDebitAmountOCToString;
    }

    public String getTotalDebitAmountToString() {
        return totalDebitAmountToString;
    }

    public void setTotalDebitAmountToString(String totalDebitAmountToString) {
        this.totalDebitAmountToString = totalDebitAmountToString;
    }

    public String getTotalCreditAmountOCToString() {
        return totalCreditAmountOCToString;
    }

    public void setTotalCreditAmountOCToString(String totalCreditAmountOCToString) {
        this.totalCreditAmountOCToString = totalCreditAmountOCToString;
    }

    public String getTotalCreditAmountToString() {
        return totalCreditAmountToString;
    }

    public void setTotalCreditAmountToString(String totalCreditAmountToString) {
        this.totalCreditAmountToString = totalCreditAmountToString;
    }

    public String getTotalClosingDebitAmountOCToString() {
        return totalClosingDebitAmountOCToString;
    }

    public void setTotalClosingDebitAmountOCToString(String totalClosingDebitAmountOCToString) {
        this.totalClosingDebitAmountOCToString = totalClosingDebitAmountOCToString;
    }

    public String getTotalClosingDebitAmountToString() {
        return totalClosingDebitAmountToString;
    }

    public void setTotalClosingDebitAmountToString(String totalClosingDebitAmountToString) {
        this.totalClosingDebitAmountToString = totalClosingDebitAmountToString;
    }

    public String getTotalClosingCreditAmountOCToString() {
        return totalClosingCreditAmountOCToString;
    }

    public void setTotalClosingCreditAmountOCToString(String totalClosingCreditAmountOCToString) {
        this.totalClosingCreditAmountOCToString = totalClosingCreditAmountOCToString;
    }

    public String getTotalClosingCreditAmountToString() {
        return totalClosingCreditAmountToString;
    }

    public void setTotalClosingCreditAmountToString(String totalClosingCreditAmountToString) {
        this.totalClosingCreditAmountToString = totalClosingCreditAmountToString;
    }

    public String getDebitAmountOCToString() {
        return debitAmountOCToString;
    }

    public void setDebitAmountOCToString(String debitAmountOCToString) {
        this.debitAmountOCToString = debitAmountOCToString;
    }

    public String getDebitAmountToString() {
        return debitAmountToString;
    }

    public void setDebitAmountToString(String debitAmountToString) {
        this.debitAmountToString = debitAmountToString;
    }

    public String getCreditAmountOCToString() {
        return creditAmountOCToString;
    }

    public void setCreditAmountOCToString(String creditAmountOCToString) {
        this.creditAmountOCToString = creditAmountOCToString;
    }

    public String getCreditAmountToString() {
        return creditAmountToString;
    }

    public void setCreditAmountToString(String creditAmountToString) {
        this.creditAmountToString = creditAmountToString;
    }

    public String getClosingDebitAmountOCToString() {
        return closingDebitAmountOCToString;
    }

    public void setClosingDebitAmountOCToString(String closingDebitAmountOCToString) {
        this.closingDebitAmountOCToString = closingDebitAmountOCToString;
    }

    public String getClosingDebitAmountToString() {
        return closingDebitAmountToString;
    }

    public void setClosingDebitAmountToString(String closingDebitAmountToString) {
        this.closingDebitAmountToString = closingDebitAmountToString;
    }

    public String getClosingCreditAmountOCToString() {
        return closingCreditAmountOCToString;
    }

    public void setClosingCreditAmountOCToString(String closingCreditAmountOCToString) {
        this.closingCreditAmountOCToString = closingCreditAmountOCToString;
    }

    public String getClosingCreditAmountToString() {
        return closingCreditAmountToString;
    }

    public void setClosingCreditAmountToString(String closingCreditAmountToString) {
        this.closingCreditAmountToString = closingCreditAmountToString;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getRefDate() {
        return refDate;
    }

    public void setRefDate(String refDate) {
        this.refDate = refDate;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCorrespondingAccountNumber() {
        return correspondingAccountNumber;
    }

    public void setCorrespondingAccountNumber(String correspondingAccountNumber) {
        this.correspondingAccountNumber = correspondingAccountNumber;
    }

    public BigDecimal getDebitAmountOC() {
        return debitAmountOC;
    }

    public void setDebitAmountOC(BigDecimal debitAmountOC) {
        this.debitAmountOC = debitAmountOC;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditAmountOC() {
        return creditAmountOC;
    }

    public void setCreditAmountOC(BigDecimal creditAmountOC) {
        this.creditAmountOC = creditAmountOC;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getClosingDebitAmountOC() {
        return closingDebitAmountOC;
    }

    public void setClosingDebitAmountOC(BigDecimal closingDebitAmountOC) {
        this.closingDebitAmountOC = closingDebitAmountOC;
    }

    public BigDecimal getClosingDebitAmount() {
        return closingDebitAmount;
    }

    public void setClosingDebitAmount(BigDecimal closingDebitAmount) {
        this.closingDebitAmount = closingDebitAmount;
    }

    public BigDecimal getClosingCreditAmountOC() {
        return closingCreditAmountOC;
    }

    public void setClosingCreditAmountOC(BigDecimal closingCreditAmountOC) {
        this.closingCreditAmountOC = closingCreditAmountOC;
    }

    public BigDecimal getClosingCreditAmount() {
        return closingCreditAmount;
    }

    public void setClosingCreditAmount(BigDecimal closingCreditAmount) {
        this.closingCreditAmount = closingCreditAmount;
    }

    public String getAccountObjectCode() {
        return accountObjectCode;
    }

    public void setAccountObjectCode(String accountObjectCode) {
        this.accountObjectCode = accountObjectCode;
    }

    public String getAccountObjectName() {
        return accountObjectName;
    }

    public void setAccountObjectName(String accountObjectName) {
        this.accountObjectName = accountObjectName;
    }

    public long getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(long ordercode) {
        this.ordercode = ordercode;
    }
}

