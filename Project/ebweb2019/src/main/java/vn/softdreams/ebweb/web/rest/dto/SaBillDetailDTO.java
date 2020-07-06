package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.SaBillDetails;

import java.util.List;

public class SaBillDetailDTO {
    private List<SaBillDetails> saBillDetails;
    private List<RefVoucherDTO> refVoucherDTOS;

    public SaBillDetailDTO(List<SaBillDetails> saBillDetails, List<RefVoucherDTO> refVoucherDTOS) {
        this.saBillDetails = saBillDetails;
        this.refVoucherDTOS = refVoucherDTOS;
    }

    public SaBillDetailDTO() {
    }

    public List<SaBillDetails> getSaBillDetails() {
        return saBillDetails;
    }

    public void setSaBillDetails(List<SaBillDetails> saBillDetails) {
        this.saBillDetails = saBillDetails;
    }

    public List<RefVoucherDTO> getRefVoucherDTOS() {
        return refVoucherDTOS;
    }

    public void setRefVoucherDTOS(List<RefVoucherDTO> refVoucherDTOS) {
        this.refVoucherDTOS = refVoucherDTOS;
    }
}
