package vn.softdreams.ebweb.service.dto;

import javax.persistence.Column;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class CPExpenseListDTO {
    private UUID id;
    private UUID cPPeriodID;
    private Integer typeVoucher;
    private UUID costSetID;
    private String costSetCode;
    private UUID contractID;
    private Integer typeID;
    private LocalDate date;
    private LocalDate postedDate;
    private String no;
    private String description;
    private BigDecimal amount;
    private String accountNumber;
    private UUID expenseItemID;
    private String expenseItemCode;
    private Integer typeGroupID;
    private UUID refID2;

    public CPExpenseListDTO(UUID id, UUID cPPeriodID, Integer typeVoucher, UUID costSetID, String costSetCode, Integer typeID, LocalDate date, LocalDate postedDate, String no, String description, BigDecimal amount, String accountNumber, UUID expenseItemID, String expenseItemCode, Integer typeGroupID, UUID refID2) {
        this.id = id;
        this.cPPeriodID = cPPeriodID;
        this.typeVoucher = typeVoucher;
        this.costSetID = costSetID;
        this.costSetCode = costSetCode;
        this.typeID = typeID;
        this.date = date;
        this.postedDate = postedDate;
        this.no = no;
        this.description = description;
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.expenseItemID = expenseItemID;
        this.expenseItemCode = expenseItemCode;
        this.typeGroupID = typeGroupID;
        this.refID2 = refID2;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getcPPeriodID() {
        return cPPeriodID;
    }

    public void setcPPeriodID(UUID cPPeriodID) {
        this.cPPeriodID = cPPeriodID;
    }

    public Integer getTypeVoucher() {
        return typeVoucher;
    }

    public void setTypeVoucher(Integer typeVoucher) {
        this.typeVoucher = typeVoucher;
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

    public UUID getContractID() {
        return contractID;
    }

    public void setContractID(UUID contractID) {
        this.contractID = contractID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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

    public Integer getTypeGroupID() {
        return typeGroupID;
    }

    public void setTypeGroupID(Integer typeGroupID) {
        this.typeGroupID = typeGroupID;
    }

    public UUID getRefID2() {
        return refID2;
    }

    public void setRefID2(UUID refID2) {
        this.refID2 = refID2;
    }
}

