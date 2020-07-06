package vn.softdreams.ebweb.service.dto.EInvoice.SIV;

import java.math.BigDecimal;

public class SummarizeInfo {
    private BigDecimal sumOfTotalLineAmountWithoutTax;
    private BigDecimal totalAmountWithoutTax;
    private BigDecimal totalTaxAmount;
    private BigDecimal totalAmountWithTax;
    private String totalAmountWithTaxInWords;
    private BigDecimal discountAmount;
    private BigDecimal settlementDiscountAmount;
    private int taxPercentage;
    private Boolean isTotalAmountPos;
    private Boolean isTotalTaxAmountPos;
    private Boolean isTotalAmtWithoutTaxPos;
    private Boolean isDiscountAmtPos;

    public BigDecimal getSumOfTotalLineAmountWithoutTax() {
        return sumOfTotalLineAmountWithoutTax;
    }

    public void setSumOfTotalLineAmountWithoutTax(BigDecimal sumOfTotalLineAmountWithoutTax) {
        this.sumOfTotalLineAmountWithoutTax = sumOfTotalLineAmountWithoutTax;
    }

    public BigDecimal getTotalAmountWithoutTax() {
        return totalAmountWithoutTax;
    }

    public void setTotalAmountWithoutTax(BigDecimal totalAmountWithoutTax) {
        this.totalAmountWithoutTax = totalAmountWithoutTax;
    }

    public BigDecimal getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public BigDecimal getTotalAmountWithTax() {
        return totalAmountWithTax;
    }

    public void setTotalAmountWithTax(BigDecimal totalAmountWithTax) {
        this.totalAmountWithTax = totalAmountWithTax;
    }

    public String getTotalAmountWithTaxInWords() {
        return totalAmountWithTaxInWords;
    }

    public void setTotalAmountWithTaxInWords(String totalAmountWithTaxInWords) {
        this.totalAmountWithTaxInWords = totalAmountWithTaxInWords;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getSettlementDiscountAmount() {
        return settlementDiscountAmount;
    }

    public void setSettlementDiscountAmount(BigDecimal settlementDiscountAmount) {
        this.settlementDiscountAmount = settlementDiscountAmount;
    }

    public int getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(int taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public Boolean getIsTotalAmountPos() {
        return isTotalAmountPos;
    }

    public void setIsTotalAmountPos(Boolean isTotalAmountPos) {
        this.isTotalAmountPos = isTotalAmountPos;
    }

    public Boolean getIsTotalTaxAmountPos() {
        return isTotalTaxAmountPos;
    }

    public void setIsTotalTaxAmountPos(Boolean isTotalTaxAmountPos) {
        this.isTotalTaxAmountPos = isTotalTaxAmountPos;
    }

    public Boolean getIsTotalAmtWithoutTaxPos() {
        return isTotalAmtWithoutTaxPos;
    }

    public void setIsTotalAmtWithoutTaxPos(Boolean isTotalAmtWithoutTaxPos) {
        this.isTotalAmtWithoutTaxPos = isTotalAmtWithoutTaxPos;
    }

    public Boolean getIsDiscountAmtPos() {
        return isDiscountAmtPos;
    }

    public void setIsDiscountAmtPos(Boolean isDiscountAmtPos) {
        this.isDiscountAmtPos = isDiscountAmtPos;
    }
}
