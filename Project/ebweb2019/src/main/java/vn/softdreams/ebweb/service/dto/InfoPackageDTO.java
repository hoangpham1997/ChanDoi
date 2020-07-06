package vn.softdreams.ebweb.service.dto;

import java.util.UUID;

public class InfoPackageDTO {
    private int warningLimitedQuantity;
    private int warningExpiredTime;

    public int getWarningLimitedQuantity() {
        return warningLimitedQuantity;
    }

    public void setWarningLimitedQuantity(int warningLimitedQuantity) {
        this.warningLimitedQuantity = warningLimitedQuantity;
    }

    public int getWarningExpiredTime() {
        return warningExpiredTime;
    }

    public void setWarningExpiredTime(int warningExpiredTime) {
        this.warningExpiredTime = warningExpiredTime;
    }
}
