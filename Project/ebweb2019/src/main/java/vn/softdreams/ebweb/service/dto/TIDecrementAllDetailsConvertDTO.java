package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;

import java.util.List;

public class TIDecrementAllDetailsConvertDTO {
    List<TIDecrementDetailsConvertDTO> tiDecrementDetailsConvertDTOS;
    List<RefVoucherDTO> viewVouchers;

    public TIDecrementAllDetailsConvertDTO() {
    }

    public TIDecrementAllDetailsConvertDTO(List<TIDecrementDetailsConvertDTO> tiDecrementDetailsConvertDTOS, List<RefVoucherDTO> viewVouchers) {
        this.tiDecrementDetailsConvertDTOS = tiDecrementDetailsConvertDTOS;
        this.viewVouchers = viewVouchers;
    }

    public List<TIDecrementDetailsConvertDTO> getTiDecrementDetailsConvertDTOS() {
        return tiDecrementDetailsConvertDTOS;
    }

    public void setTiDecrementDetailsConvertDTOS(List<TIDecrementDetailsConvertDTO> tiDecrementDetailsConvertDTOS) {
        this.tiDecrementDetailsConvertDTOS = tiDecrementDetailsConvertDTOS;
    }

    public List<RefVoucherDTO> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucherDTO> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }
}
