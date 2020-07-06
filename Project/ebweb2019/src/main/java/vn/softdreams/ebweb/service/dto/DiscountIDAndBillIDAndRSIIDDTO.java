package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.PPDiscountReturn;
import vn.softdreams.ebweb.domain.RSInwardOutward;
import vn.softdreams.ebweb.web.rest.dto.SaBillSaveDTO;

import java.util.UUID;

public class DiscountIDAndBillIDAndRSIIDDTO {
    UUID ppDiscountReturnId;
    UUID rsInwardOutwardID;
    UUID saBillID;
    Boolean isSaBill;
    Boolean isDeliveryVoucher;

    public DiscountIDAndBillIDAndRSIIDDTO() {
    }

    public DiscountIDAndBillIDAndRSIIDDTO(UUID ppDiscountReturnId, UUID rsInwardOutwardID, UUID saBillID, Boolean isSaBill, Boolean isDeliveryVoucher) {
        this.ppDiscountReturnId = ppDiscountReturnId;
        this.rsInwardOutwardID = rsInwardOutwardID;
        this.saBillID = saBillID;
        this.isSaBill = isSaBill;
        this.isDeliveryVoucher = isDeliveryVoucher;
    }

    public UUID getPpDiscountReturnId() {
        return ppDiscountReturnId;
    }

    public void setPpDiscountReturnId(UUID ppDiscountReturnId) {
        this.ppDiscountReturnId = ppDiscountReturnId;
    }

    public UUID getRsInwardOutwardID() {
        return rsInwardOutwardID;
    }

    public void setRsInwardOutwardID(UUID rsInwardOutwardID) {
        this.rsInwardOutwardID = rsInwardOutwardID;
    }

    public UUID getSaBillID() {
        return saBillID;
    }

    public void setSaBillID(UUID saBillID) {
        this.saBillID = saBillID;
    }

    public Boolean getSaBill() {
        return isSaBill;
    }

    public void setSaBill(Boolean saBill) {
        isSaBill = saBill;
    }

    public Boolean getDeliveryVoucher() {
        return isDeliveryVoucher;
    }

    public void setDeliveryVoucher(Boolean deliveryVoucher) {
        isDeliveryVoucher = deliveryVoucher;
    }
}
