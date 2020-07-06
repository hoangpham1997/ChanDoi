package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.MBDepositDetailTax;
import vn.softdreams.ebweb.domain.MBDepositDetails;

import java.util.List;

public class MBDepositDTO {
    private List<MBDepositDetails> mbDepositDetails;
    private List<MBDepositDetailTax> mbDepositDetailTaxes;

    public List<MBDepositDetails> getMbDepositDetails() {
        return mbDepositDetails;
    }

    public void setMbDepositDetails(List<MBDepositDetails> mbDepositDetails) {
        this.mbDepositDetails = mbDepositDetails;
    }

    public List<MBDepositDetailTax> getMbDepositDetailTaxes() {
        return mbDepositDetailTaxes;
    }

    public void setMbDepositDetailTaxes(List<MBDepositDetailTax> mbDepositDetailTaxes) {
        this.mbDepositDetailTaxes = mbDepositDetailTaxes;
    }

    public MBDepositDTO() {
    }
}
