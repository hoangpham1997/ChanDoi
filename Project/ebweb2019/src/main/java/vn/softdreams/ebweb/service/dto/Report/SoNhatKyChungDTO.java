package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SoNhatKyChungDTO {
    public LocalDate postedDate;    // Ngày hạch toán
    public String postedDateString;    // Ngày hạch toán
    public LocalDate date;    // Ngày chứng từ
    public String dateString;    // Ngày chứng từ
    public String no;    // Số chứng từ
    public String description;    // Diễn giải
    public String accountNumber;    // Tài khoản
    public String correspondingAccountNumber;    // Tài khoản đối ứng
    public BigDecimal debitAmount;    // Phát sinh nợ
    public BigDecimal creditAmount;    // Phát sinh có
    public String debitAmountString;    // Phát sinh nợ
    public String creditAmountString;    // Phát sinh có
    public UUID referenceID;    // id chung tu
    public Integer typeID;    // loai chung tu
    public Integer oderType;//  thêm để set sắp xếp hiển thị lũy kế kỳ trước chuyển sang lên đầu
    public String linkRef;

    public SoNhatKyChungDTO() {
    }

    public SoNhatKyChungDTO(LocalDate postedDate,
                            LocalDate date,
                            String no,
                            String description,
                            String accountNumber,
                            String correspondingAccountNumber,
                            BigDecimal debitAmount,
                            BigDecimal creditAmount,
                            UUID referenceID,
                            Integer typeID,
                            Integer oderType) {
        this.postedDate = postedDate;
        this.date = date;
        this.no = no;
        this.description = description;
        this.accountNumber = accountNumber;
        this.correspondingAccountNumber = correspondingAccountNumber;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
        this.referenceID = referenceID;
        this.typeID = typeID;
        this.oderType = oderType;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
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

    public UUID getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(UUID referenceID) {
        this.referenceID = referenceID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public Integer getOderType() {
        return oderType;
    }

    public void setOderType(Integer oderType) {
        this.oderType = oderType;
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

    public String getPostedDateString() {
        return postedDateString;
    }

    public void setPostedDateString(String postedDateString) {
        this.postedDateString = postedDateString;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getLinkRef() {
        return linkRef;
    }

    public void setLinkRef(String linkRef) {
        this.linkRef = linkRef;
    }
}
