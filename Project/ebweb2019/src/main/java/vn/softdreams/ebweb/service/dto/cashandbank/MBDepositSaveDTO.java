package vn.softdreams.ebweb.service.dto.cashandbank;

import vn.softdreams.ebweb.domain.MBDeposit;

public class MBDepositSaveDTO {
    private MBDeposit mbDeposit;

    private int status;

    public MBDepositSaveDTO() {
    }

    public MBDepositSaveDTO(MBDeposit mbDeposit, int status) {
        this.mbDeposit = mbDeposit;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MBDeposit getMbDeposit() {
        return mbDeposit;
    }

    public void setMbDeposit(MBDeposit mbDeposit) {
        this.mbDeposit = mbDeposit;
    }
}
