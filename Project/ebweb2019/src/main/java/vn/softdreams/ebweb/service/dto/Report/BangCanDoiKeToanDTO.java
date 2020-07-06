package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class BangCanDoiKeToanDTO {
    private String itemCode;    // Mã số
    private String itemName;    // Tài sản
    private Integer itemIndex;    // Thứ tự
    private String description;    // Thuyết minh
    private Boolean isBold;    // In đậm
    private Boolean isItalic;    // In nghiêng
    private BigDecimal amount;    // Số cuối năm
    private BigDecimal prevAmount;    // Số đầu năm
    private String amountString;    // Số cuối năm
    private String prevAmountString;    // Số đầu năm

    public BangCanDoiKeToanDTO(String itemCode, String itemName, Integer itemIndex, String description, Boolean isBold, Boolean isItalic, BigDecimal amount, BigDecimal prevAmount) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemIndex = itemIndex;
        this.description = description;
        this.isBold = isBold;
        this.isItalic = isItalic;
        this.amount = amount;
        this.prevAmount = prevAmount;
    }

    public BangCanDoiKeToanDTO(BigDecimal amount, BigDecimal prevAmount) {
        this.amount = amount;
        this.prevAmount = prevAmount;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(Integer itemIndex) {
        this.itemIndex = itemIndex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getBold() {
        return isBold;
    }

    public void setBold(Boolean bold) {
        isBold = bold;
    }

    public Boolean getItalic() {
        return isItalic;
    }

    public void setItalic(Boolean italic) {
        isItalic = italic;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPrevAmount() {
        return prevAmount;
    }

    public void setPrevAmount(BigDecimal preAmount) {
        this.prevAmount = preAmount;
    }

    public String getAmountString() {
        return amountString;
    }

    public void setAmountString(String amountString) {
        this.amountString = amountString;
    }

    public String getPrevAmountString() {
        return prevAmountString;
    }

    public void setPrevAmountString(String prevAmountString) {
        this.prevAmountString = prevAmountString;
    }
}
