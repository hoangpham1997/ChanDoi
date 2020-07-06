package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.ViewVoucherNo;

import java.util.List;

public class ResultCalculateOWDTO {

    private List<ViewVoucherNo> voucherResultDTOs;

    private Integer status;

    public ResultCalculateOWDTO() {
    }

    public List<ViewVoucherNo> getVoucherResultDTOs() {
        return voucherResultDTOs;
    }

    public void setVoucherResultDTOs(List<ViewVoucherNo> voucherResultDTOs) {
        this.voucherResultDTOs = voucherResultDTOs;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
