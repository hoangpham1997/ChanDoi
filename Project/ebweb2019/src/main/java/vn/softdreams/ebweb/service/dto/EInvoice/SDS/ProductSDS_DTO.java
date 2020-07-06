package vn.softdreams.ebweb.service.dto.EInvoice.SDS;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductSDS_DTO {
    private String Code;
    private String ProdName;
    private String ProdUnit;
    private BigDecimal ProdQuantity;
    private BigDecimal ProdPrice;
    private BigDecimal Total;
    private Integer VATRate;
    private BigDecimal VATAmount;
    private BigDecimal Amount;
    private String Extra;
    private String lotNo;
    private LocalDate expiryDate;

    @XmlElement(name = "Code")
    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    @XmlElement(name = "ProdName")
    public String getProdName() {
        return ProdName;
    }

    public void setProdName(String prodName) {
        ProdName = prodName;
    }

    @XmlElement(name = "ProdUnit")
    public String getProdUnit() {
        return ProdUnit;
    }

    public void setProdUnit(String prodUnit) {
        ProdUnit = prodUnit;
    }

    @XmlElement(name = "ProdQuantity")
    public BigDecimal getProdQuantity() {
        return ProdQuantity;
    }

    public void setProdQuantity(BigDecimal prodQuantity) {
        ProdQuantity = prodQuantity;
    }

    @XmlElement(name = "ProdPrice")
    public BigDecimal getProdPrice() {
        return ProdPrice;
    }

    public void setProdPrice(BigDecimal prodPrice) {
        ProdPrice = prodPrice;
    }

    @XmlElement(name = "Total")
    public BigDecimal getTotal() {
        return Total;
    }

    public void setTotal(BigDecimal total) {
        Total = total;
    }

    @XmlElement(name = "VATRate")
    public Integer getVATRate() {
        return VATRate;
    }

    public void setVATRate(Integer VATRate) {
        this.VATRate = VATRate;
    }

    @XmlElement(name = "VATAmount")
    public BigDecimal getVATAmount() {
        return VATAmount;
    }

    public void setVATAmount(BigDecimal VATAmount) {
        this.VATAmount = VATAmount;
    }

    @XmlElement(name = "Amount")
    public BigDecimal getAmount() {
        return Amount;
    }

    public void setAmount(BigDecimal amount) {
        Amount = amount;
    }

    @XmlElement(name = "Extra")
    public String getExtra() {
        return Extra;
    }

    public void setExtra(String extra) {
        Extra = extra;
    }

    @XmlTransient
    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    @XmlTransient
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}
