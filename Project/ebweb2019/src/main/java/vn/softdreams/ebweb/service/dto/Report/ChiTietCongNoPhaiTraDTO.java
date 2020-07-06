package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ChiTietCongNoPhaiTraDTO {
    private String postedDate;
    private String date;
    private String no;
    private String description;
    private Integer refType;
    private UUID referenceID;
    private String correspondingAccountNumber;
    private String accountObjectCode;
    private String accountObjectName;
    private UUID accountObjectID;
    private String accountNumber;
    private BigDecimal openingDebitAmount;
    private BigDecimal openingCreditAmount;
    private BigDecimal debitAmount;
    private BigDecimal debitAmountOC;
    private BigDecimal creditAmount;
    private BigDecimal creditAmountOC;
    private BigDecimal closingDebitAmount;
    private BigDecimal closingDebitAmountOC;
    private BigDecimal closingCreditAmount;
    private BigDecimal closingCreditAmountOC;
    private String openingDebitAmountToString;
    private String openingCreditAmountToString;
    private String debitAmountToString;
    private String debitAmountOCToString;
    private String creditAmountToString;
    private String creditAmountOCToString;
    private String closingDebitAmountToString;
    private String closingDebitAmountOCToString;
    private String closingCreditAmountToString;
    private String closingCreditAmountOCToString;
    private String sumOpeningDebitAmountToString;
    private String sumOpeningCreditAmountToString;
    private String sumDebitAmountToString;
    private String sumDebitAmountOCToString;
    private String sumCreditAmountToString;
    private String sumCreditAmountOCToString;
    private String sumClosingDebitAmountToString;
    private String sumClosingDebitAmountOCToString;
    private String sumClosingCreditAmountToString;
    private String sumClosingCreditAmountOCToString;
    private String linkRef;
    public ChiTietCongNoPhaiTraDTO() {
    }

    public ChiTietCongNoPhaiTraDTO(String postedDate, String date, String no, String description, Integer refType, UUID referenceID, String correspondingAccountNumber, String accountObjectCode, String accountObjectName, UUID accountObjectID, String accountNumber, BigDecimal openingDebitAmount, BigDecimal openingCreditAmount, BigDecimal debitAmount, BigDecimal debitAmountOC, BigDecimal creditAmount, BigDecimal creditAmountOC, BigDecimal closingDebitAmount, BigDecimal closingDebitAmountOC, BigDecimal closingCreditAmount, BigDecimal closingCreditAmountOC) {
        this.postedDate = postedDate;
        this.date = date;
        this.no = no;
        this.description = description;
        this.refType = refType;
        this.referenceID = referenceID;
        this.correspondingAccountNumber = correspondingAccountNumber;
        this.accountObjectCode = accountObjectCode;
        this.accountObjectName = accountObjectName;
        this.accountNumber = accountNumber;
        this.openingDebitAmount = openingDebitAmount;
        this.openingCreditAmount = openingCreditAmount;
        this.debitAmount = debitAmount;
        this.debitAmountOC = debitAmountOC;
        this.creditAmount = creditAmount;
        this.creditAmountOC = creditAmountOC;
        this.closingDebitAmount = closingDebitAmount;
        this.closingDebitAmountOC = closingDebitAmountOC;
        this.closingCreditAmount = closingCreditAmount;
        this.accountObjectID = accountObjectID;
        this.closingCreditAmountOC = closingCreditAmountOC;
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

    public UUID getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(UUID referenceID) {
        this.referenceID = referenceID;
    }

    public String getLinkRef() {
        return linkRef;
    }

    public void setLinkRef(String linkRef) {
        this.linkRef = linkRef;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCorrespondingAccountNumber() {
        return correspondingAccountNumber;
    }

    public void setCorrespondingAccountNumber(String correspondingAccountNumber) {
        this.correspondingAccountNumber = correspondingAccountNumber;
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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

    public BigDecimal getDebitAmountOC() {
        return debitAmountOC;
    }

    public void setDebitAmountOC(BigDecimal debitAmountOC) {
        this.debitAmountOC = debitAmountOC;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getCreditAmountOC() {
        return creditAmountOC;
    }

    public void setCreditAmountOC(BigDecimal creditAmountOC) {
        this.creditAmountOC = creditAmountOC;
    }

    public BigDecimal getClosingDebitAmount() {
        return closingDebitAmount;
    }

    public void setClosingDebitAmount(BigDecimal closingDebitAmount) {
        this.closingDebitAmount = closingDebitAmount;
    }

    public BigDecimal getClosingDebitAmountOC() {
        return closingDebitAmountOC;
    }

    public void setClosingDebitAmountOC(BigDecimal closingDebitAmountOC) {
        this.closingDebitAmountOC = closingDebitAmountOC;
    }

    public BigDecimal getClosingCreditAmount() {
        return closingCreditAmount;
    }

    public void setClosingCreditAmount(BigDecimal closingCreditAmount) {
        this.closingCreditAmount = closingCreditAmount;
    }

    public BigDecimal getClosingCreditAmountOC() {
        return closingCreditAmountOC;
    }

    public void setClosingCreditAmountOC(BigDecimal closingCreditAmountOC) {
        this.closingCreditAmountOC = closingCreditAmountOC;
    }

    public String getOpeningDebitAmountToString() {
        return openingDebitAmountToString;
    }

    public void setOpeningDebitAmountToString(String openingDebitAmountToString) {
        this.openingDebitAmountToString = openingDebitAmountToString;
    }

    public String getOpeningCreditAmountToString() {
        return openingCreditAmountToString;
    }

    public void setOpeningCreditAmountToString(String openingCreditAmountToString) {
        this.openingCreditAmountToString = openingCreditAmountToString;
    }

    public String getDebitAmountToString() {
        return debitAmountToString;
    }

    public void setDebitAmountToString(String debitAmountToString) {
        this.debitAmountToString = debitAmountToString;
    }

    public String getDebitAmountOCToString() {
        return debitAmountOCToString;
    }

    public void setDebitAmountOCToString(String debitAmountOCToString) {
        this.debitAmountOCToString = debitAmountOCToString;
    }

    public String getCreditAmountToString() {
        return creditAmountToString;
    }

    public void setCreditAmountToString(String creditAmountToString) {
        this.creditAmountToString = creditAmountToString;
    }

    public String getCreditAmountOCToString() {
        return creditAmountOCToString;
    }

    public void setCreditAmountOCToString(String creditAmountOCToString) {
        this.creditAmountOCToString = creditAmountOCToString;
    }

    public String getClosingDebitAmountToString() {
        return closingDebitAmountToString;
    }

    public void setClosingDebitAmountToString(String closingDebitAmountToString) {
        this.closingDebitAmountToString = closingDebitAmountToString;
    }

    public String getClosingDebitAmountOCToString() {
        return closingDebitAmountOCToString;
    }

    public void setClosingDebitAmountOCToString(String closingDebitAmountOCToString) {
        this.closingDebitAmountOCToString = closingDebitAmountOCToString;
    }

    public String getClosingCreditAmountToString() {
        return closingCreditAmountToString;
    }

    public void setClosingCreditAmountToString(String closingCreditAmountToString) {
        this.closingCreditAmountToString = closingCreditAmountToString;
    }

    public String getClosingCreditAmountOCToString() {
        return closingCreditAmountOCToString;
    }

    public void setClosingCreditAmountOCToString(String closingCreditAmountOCToString) {
        this.closingCreditAmountOCToString = closingCreditAmountOCToString;
    }

    public String getSumOpeningDebitAmountToString() {
        return sumOpeningDebitAmountToString;
    }

    public void setSumOpeningDebitAmountToString(String sumOpeningDebitAmountToString) {
        this.sumOpeningDebitAmountToString = sumOpeningDebitAmountToString;
    }

    public String getSumOpeningCreditAmountToString() {
        return sumOpeningCreditAmountToString;
    }

    public void setSumOpeningCreditAmountToString(String sumOpeningCreditAmountToString) {
        this.sumOpeningCreditAmountToString = sumOpeningCreditAmountToString;
    }

    public String getSumDebitAmountToString() {
        return sumDebitAmountToString;
    }

    public void setSumDebitAmountToString(String sumDebitAmountToString) {
        this.sumDebitAmountToString = sumDebitAmountToString;
    }

    public String getSumDebitAmountOCToString() {
        return sumDebitAmountOCToString;
    }

    public void setSumDebitAmountOCToString(String sumDebitAmountOCToString) {
        this.sumDebitAmountOCToString = sumDebitAmountOCToString;
    }

    public String getSumCreditAmountToString() {
        return sumCreditAmountToString;
    }

    public void setSumCreditAmountToString(String sumCreditAmountToString) {
        this.sumCreditAmountToString = sumCreditAmountToString;
    }

    public String getSumCreditAmountOCToString() {
        return sumCreditAmountOCToString;
    }

    public void setSumCreditAmountOCToString(String sumCreditAmountOCToString) {
        this.sumCreditAmountOCToString = sumCreditAmountOCToString;
    }

    public String getSumClosingDebitAmountToString() {
        return sumClosingDebitAmountToString;
    }

    public void setSumClosingDebitAmountToString(String sumClosingDebitAmountToString) {
        this.sumClosingDebitAmountToString = sumClosingDebitAmountToString;
    }

    public String getSumClosingDebitAmountOCToString() {
        return sumClosingDebitAmountOCToString;
    }

    public void setSumClosingDebitAmountOCToString(String sumClosingDebitAmountOCToString) {
        this.sumClosingDebitAmountOCToString = sumClosingDebitAmountOCToString;
    }

    public String getSumClosingCreditAmountToString() {
        return sumClosingCreditAmountToString;
    }

    public void setSumClosingCreditAmountToString(String sumClosingCreditAmountToString) {
        this.sumClosingCreditAmountToString = sumClosingCreditAmountToString;
    }

    public String getSumClosingCreditAmountOCToString() {
        return sumClosingCreditAmountOCToString;
    }

    public void setSumClosingCreditAmountOCToString(String sumClosingCreditAmountOCToString) {
        this.sumClosingCreditAmountOCToString = sumClosingCreditAmountOCToString;
    }
}

