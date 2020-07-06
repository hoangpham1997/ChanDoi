package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.PPDiscountReturn;
import vn.softdreams.ebweb.domain.RSInwardOutward;
import vn.softdreams.ebweb.domain.RefVoucher;
import vn.softdreams.ebweb.domain.SaBill;
import vn.softdreams.ebweb.web.rest.dto.SaBillSaveDTO;

import java.util.List;
import java.util.UUID;

public class DiscountAndBillAndRSIDTO {
    PPDiscountReturn ppDiscountReturn;
    RSInwardOutward rsInwardOutward;
    SaBill saBill;
    List<RefVoucher> viewVouchers;
    Boolean statusPurchase;
    Boolean record;

    public DiscountAndBillAndRSIDTO() {
    }

    public DiscountAndBillAndRSIDTO(PPDiscountReturn ppDiscountReturn, RSInwardOutward rsInwardOutward, SaBill saBill, List<RefVoucher> viewVouchers, Boolean statusPurchase, Boolean record) {
        this.ppDiscountReturn = ppDiscountReturn;
        this.rsInwardOutward = rsInwardOutward;
        this.saBill = saBill;
        this.viewVouchers = viewVouchers;
        this.statusPurchase = statusPurchase;
        this.record = record;
    }

    public Boolean getRecord() {
        return record;
    }

    public void setRecord(Boolean record) {
        this.record = record;
    }

    public Boolean getStatusPurchase() {
        return statusPurchase;
    }

    public void setStatusPurchase(Boolean statusPurchase) {
        this.statusPurchase = statusPurchase;
    }

    public void setPpDiscountReturn(PPDiscountReturn ppDiscountReturn) {
        this.ppDiscountReturn = ppDiscountReturn;
    }

    public void setRsInwardOutward(RSInwardOutward rsInwardOutward) {
        this.rsInwardOutward = rsInwardOutward;
    }

    public SaBill getSaBill() {
        return saBill;
    }

    public void setSaBill(SaBill saBill) {
        this.saBill = saBill;
    }

    public List<RefVoucher> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucher> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }

    public PPDiscountReturn getPpDiscountReturn() {
        return ppDiscountReturn;
    }

    public RSInwardOutward getRsInwardOutward() {
        return rsInwardOutward;
    }



}
