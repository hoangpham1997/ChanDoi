package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class TIAdjustmentDetailAllConvertDTO {

    List<TIAdjustmentDetailConvertDTO> tiAdjustmentDetailConvertDTOS;
    List<RefVoucherDTO> viewVouchers;

    public TIAdjustmentDetailAllConvertDTO() {
    }

    public TIAdjustmentDetailAllConvertDTO(List<TIAdjustmentDetailConvertDTO> tiAdjustmentDetailConvertDTOS, List<RefVoucherDTO> viewVouchers) {
        this.tiAdjustmentDetailConvertDTOS = tiAdjustmentDetailConvertDTOS;
        this.viewVouchers = viewVouchers;
    }

    public List<TIAdjustmentDetailConvertDTO> getTiAdjustmentDetailConvertDTOS() {
        return tiAdjustmentDetailConvertDTOS;
    }

    public void setTiAdjustmentDetailConvertDTOS(List<TIAdjustmentDetailConvertDTO> tiAdjustmentDetailConvertDTOS) {
        this.tiAdjustmentDetailConvertDTOS = tiAdjustmentDetailConvertDTOS;
    }

    public List<RefVoucherDTO> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucherDTO> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }
}
