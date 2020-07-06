package vn.softdreams.ebweb.service.dto.EInvoice.SDS;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EInvoiceSDS_DTO {
    private UUID key;
    private String InvNo;
    private String CusCode; // Mã khách hàng
    private String Buyer; // Tên người mua
    private String CusName;
    private String CusAddress;
    private String CusBankName;
    private String CusBankNo;
    private String CusPhone;
    private String CusTaxCode;
    private String Email;
    private String PaymentMethod;
    private String ArisingDate; // Ngày tạo lập đồng thời ngày phát hành
    private BigDecimal ExchangeRate; //Tỷ giá chuyển đổi
    private String CurrencyUnit;
    private ProductsSDS_DTO Products;
    private BigDecimal Total;
    private BigDecimal VATAmount;
    private Integer VATRate;
    private BigDecimal Amount;
    private String AmountInWords;
    private String Extra;

    public EInvoiceSDS_DTO() {
        Products = new ProductsSDS_DTO();
    }

    @XmlElement(name = "Ikey")
    public UUID getKey() {
        return key;
    }

    @JsonProperty("Ikey")
    public void setKey(UUID key) {
        this.key = key;
    }

    @XmlElement(name = "InvNo")
    public String getInvNo() {
        return InvNo;
    }

    @JsonProperty("No")
    public void setInvNo(String invNo) {
        InvNo = invNo;
    }

    @XmlElement(name = "CusCode")
    public String getCusCode() {
        return CusCode;
    }

    @JsonProperty("CustomerCode")
    public void setCusCode(String cusCode) {
        CusCode = cusCode;
    }

    @XmlElement(name = "Buyer")
    public String getBuyer() {
        return Buyer;
    }

    @JsonProperty("Buyer")
    public void setBuyer(String buyer) {
        Buyer = buyer;
    }

    @XmlElement(name = "CusName")
    public String getCusName() {
        return CusName;
    }

    @JsonProperty("CustomerName")
    public void setCusName(String cusName) {
        CusName = cusName;
    }

    @XmlElement(name = "CusAddress")
    public String getCusAddress() {
        return CusAddress;
    }

    @JsonProperty("CustomerAddress")
    public void setCusAddress(String cusAddress) {
        CusAddress = cusAddress;
    }

    @XmlElement(name = "CusBankName")
    public String getCusBankName() {
        return CusBankName;
    }

    @JsonProperty("CusBankName")
    public void setCusBankName(String cusBankName) {
        CusBankName = cusBankName;
    }

    @XmlElement(name = "CusBankNo")
    public String getCusBankNo() {
        return CusBankNo;
    }

    @JsonProperty("CusBankNo")
    public void setCusBankNo(String cusBankNo) {
        CusBankNo = cusBankNo;
    }

    @XmlElement(name = "CusPhone")
    public String getCusPhone() {
        return CusPhone;
    }

    @JsonProperty("CusPhone")
    public void setCusPhone(String cusPhone) {
        CusPhone = cusPhone;
    }

    @XmlElement(name = "CusTaxCode")
    public String getCusTaxCode() {
        return CusTaxCode;
    }

    @JsonProperty("CustomerTaxCode")
    public void setCusTaxCode(String cusTaxCode) {
        CusTaxCode = cusTaxCode;
    }

    @XmlElement(name = "PaymentMethod")
    public String getPaymentMethod() {
        return PaymentMethod;
    }

    @JsonProperty("PaymentMethod")
    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    @XmlElement(name = "ArisingDate")
    public String getArisingDate() {
        return ArisingDate;
    }

    @JsonProperty("ArisingDate")
    public void setArisingDate(String arisingDate) {
        ArisingDate = arisingDate;
    }

    @XmlElement(name = "ExchangeRate")
    public BigDecimal getExchangeRate() {
        return ExchangeRate;
    }

    @JsonProperty("ExchangeRate")
    public void setExchangeRate(BigDecimal exchangeRate) {
        ExchangeRate = exchangeRate;
    }

    @XmlElement(name = "CurrencyUnit")
    public String getCurrencyUnit() {
        return CurrencyUnit;
    }

    @JsonProperty("CurrencyUnit")
    public void setCurrencyUnit(String currencyUnit) {
        CurrencyUnit = currencyUnit;
    }

    @XmlElement(name = "Products")
    public ProductsSDS_DTO getProducts() {
        return Products;
    }

    @JsonProperty("Products")
    public void setProducts(ProductsSDS_DTO products) {
        Products = products;
    }

    @XmlElement(name = "Total")
    public BigDecimal getTotal() {
        return Total;
    }

    @JsonProperty("Total")
    public void setTotal(BigDecimal total) {
        Total = total;
    }

    @XmlElement(name = "VATAmount")
    public BigDecimal getVATAmount() {
        return VATAmount;
    }

    @JsonProperty("VATAmount")
    public void setVATAmount(BigDecimal VATAmount) {
        this.VATAmount = VATAmount;
    }

    @XmlElement(name = "VATRate")
    public Integer getVATRate() {
        return VATRate;
    }

    @JsonProperty("VATRate")
    public void setVATRate(Integer VATRate) {
        this.VATRate = VATRate;
    }

    @XmlElement(name = "Amount")
    public BigDecimal getAmount() {
        return Amount;
    }

    @JsonProperty("Amount")
    public void setAmount(BigDecimal amount) {
        Amount = amount;
    }

    @XmlElement(name = "AmountInWords")
    public String getAmountInWords() {
        return AmountInWords;
    }

    @JsonProperty("AmountInWords")
    public void setAmountInWords(String amountInWords) {
        AmountInWords = amountInWords;
    }

    @XmlElement(name = "Extra")
    public String getExtra() {
        return Extra;
    }

    @JsonProperty("Extra")
    public void setExtra(String extra) {
        Extra = extra;
    }

    @XmlElement(name = "Email")
    public String getEmail() {
        return Email;
    }

    @JsonProperty("Email")
    public void setEmail(String email) {
        Email = email;
    }
}
