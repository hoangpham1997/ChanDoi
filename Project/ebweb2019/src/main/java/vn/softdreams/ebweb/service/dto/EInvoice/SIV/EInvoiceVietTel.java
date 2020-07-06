package vn.softdreams.ebweb.service.dto.EInvoice.SIV;

import java.util.ArrayList;
import java.util.List;

public class EInvoiceVietTel {
    private InvoiceInfo generalInvoiceInfo = new InvoiceInfo();
    private BuyerInfo buyerInfo = new BuyerInfo();
    private SellerInfo sellerInfo = new SellerInfo();
    private List<Payments> payments = new ArrayList<>();
    private List<ItemInfo> itemInfo = new ArrayList<>();
    private SummarizeInfo summarizeInfo = new SummarizeInfo();
    private List<TaxBreakdowns> taxBreakdowns = new ArrayList<>();

    public InvoiceInfo getGeneralInvoiceInfo() {
        return generalInvoiceInfo;
    }

    public void setGeneralInvoiceInfo(InvoiceInfo generalInvoiceInfo) {
        this.generalInvoiceInfo = generalInvoiceInfo;
    }

    public BuyerInfo getBuyerInfo() {
        return buyerInfo;
    }

    public void setBuyerInfo(BuyerInfo buyerInfo) {
        this.buyerInfo = buyerInfo;
    }

    public SellerInfo getSellerInfo() {
        return sellerInfo;
    }

    public void setSellerInfo(SellerInfo sellerInfo) {
        this.sellerInfo = sellerInfo;
    }

    public List<Payments> getPayments() {
        return payments;
    }

    public void setPayments(List<Payments> payments) {
        this.payments = payments;
    }

    public List<ItemInfo> getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(List<ItemInfo> itemInfo) {
        this.itemInfo = itemInfo;
    }

    public SummarizeInfo getSummarizeInfo() {
        return summarizeInfo;
    }

    public void setSummarizeInfo(SummarizeInfo summarizeInfo) {
        this.summarizeInfo = summarizeInfo;
    }

    public List<TaxBreakdowns> getTaxBreakdowns() {
        return taxBreakdowns;
    }

    public void setTaxBreakdowns(List<TaxBreakdowns> taxBreakdowns) {
        this.taxBreakdowns = taxBreakdowns;
    }
}
