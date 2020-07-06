package vn.softdreams.ebweb.web.rest.dto;

import java.util.List;

public class RequestResetNoDTO {
    private List<ViewVoucherDTO> voucher;
    private String fromDate;
    private String toDate;
    private Integer typeGroupID;
    private String prefix;
    private String suffix;
    private String currentValue;

    public List<ViewVoucherDTO> getVoucher() {
        return voucher;
    }

    public void setVoucher(List<ViewVoucherDTO> voucher) {
        this.voucher = voucher;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }


    public Integer getTypeGroupID() {
        return typeGroupID;
    }

    public void setTypeGroupID(Integer typeGroupID) {
        this.typeGroupID = typeGroupID;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }
}
