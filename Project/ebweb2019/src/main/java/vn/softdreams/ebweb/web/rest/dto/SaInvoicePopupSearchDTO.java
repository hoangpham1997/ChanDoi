package vn.softdreams.ebweb.web.rest.dto;

import java.util.List;
import java.util.UUID;

public class SaInvoicePopupSearchDTO {
    private List<SAInvoiceDetailPopupDTO> ids;
    private String fromDate;
    private String toDate;
    private UUID accountingObjectID;

    public List<SAInvoiceDetailPopupDTO> getIds() {
        return ids;
    }

    public void setIds(List<SAInvoiceDetailPopupDTO> ids) {
        this.ids = ids;
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

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public SaInvoicePopupSearchDTO() {
    }
}
