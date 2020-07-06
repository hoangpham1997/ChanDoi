package vn.softdreams.ebweb.service.dto.EInvoice.SIV;

import java.math.BigDecimal;

public class TaxBreakdowns {
    private Integer taxPercentage;
    private BigDecimal taxableAmount;
    private BigDecimal taxAmount;

    public Integer getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(Integer taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public BigDecimal getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
}
