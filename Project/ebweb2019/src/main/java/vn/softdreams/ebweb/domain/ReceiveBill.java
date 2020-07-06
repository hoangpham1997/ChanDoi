package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ReceiveBill {
    private List<UUID> listIDPP;
    private List<UUID> listIDPPDetail;
    private Integer typeID;
    private Integer vATRate;
    private String vATAccount;
    private String invoiceTemplate;
    private String invoiceSeries;
    private String invoiceNo;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate invoiceDate;
    private UUID goodsServicePurchaseID;
    private UUID accountingObjectID;

    public Integer getvATRate() {
        return vATRate;
    }

    public void setvATRate(Integer vATRate) {
        this.vATRate = vATRate;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public String getInvoiceTemplate() {
        return invoiceTemplate;
    }

    public void setInvoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
    }

    public String getvATAccount() {
        return vATAccount;
    }

    public void setvATAccount(String vATAccount) {
        this.vATAccount = vATAccount;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public UUID getGoodsServicePurchaseID() {
        return goodsServicePurchaseID;
    }

    public void setGoodsServicePurchaseID(UUID goodsServicePurchaseID) {
        this.goodsServicePurchaseID = goodsServicePurchaseID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public List<UUID> getListIDPP() {
        return listIDPP;
    }

    public void setListIDPP(List<UUID> listIDPP) {
        this.listIDPP = listIDPP;
    }

    public List<UUID> getListIDPPDetail() {
        return listIDPPDetail;
    }

    public void setListIDPPDetail(List<UUID> listIDPPDetail) {
        this.listIDPPDetail = listIDPPDetail;
    }
}
