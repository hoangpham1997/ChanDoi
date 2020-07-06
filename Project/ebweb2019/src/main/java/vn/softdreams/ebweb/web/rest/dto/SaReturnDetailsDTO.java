package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.RefVoucher;
import vn.softdreams.ebweb.domain.SaReturnDetails;

import java.util.List;

public class SaReturnDetailsDTO {
    private List<SaReturnDetails> saReturnDetails;
    private List<SaReturnDetailsViewDTO> saReturnDetailsViewDTOs;
    private List<RefVoucherDTO> refVoucherDTOS;

    public SaReturnDetailsDTO() {
    }

    public SaReturnDetailsDTO(List<SaReturnDetails> saReturnDetails, List<RefVoucherDTO> refVoucherDTOS) {
        this.saReturnDetails = saReturnDetails;
        this.refVoucherDTOS = refVoucherDTOS;
    }

    public List<SaReturnDetails> getSaReturnDetails() {
        return saReturnDetails;
    }

    public void setSaReturnDetails(List<SaReturnDetails> saReturnDetails) {
        this.saReturnDetails = saReturnDetails;
    }

    public List<RefVoucherDTO> getRefVoucherDTOS() {
        return refVoucherDTOS;
    }

    public void setRefVoucherDTOS(List<RefVoucherDTO> refVoucherDTOS) {
        this.refVoucherDTOS = refVoucherDTOS;
    }

    public List<SaReturnDetailsViewDTO> getSaReturnDetailsViewDTOs() {
        return saReturnDetailsViewDTOs;
    }

    public void setSaReturnDetailsViewDTOs(List<SaReturnDetailsViewDTO> saReturnDetailsViewDTOs) {
        this.saReturnDetailsViewDTOs = saReturnDetailsViewDTOs;
    }
}
