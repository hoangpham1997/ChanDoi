package vn.softdreams.ebweb.web.rest.dto;

import java.util.List;

public class TITransferDetailsAllConvertDTO {
    List<TITransferDetailConvertDTO> tiTransferDetailsConvertDTOS;
    List<RefVoucherDTO> viewVouchers;

    public TITransferDetailsAllConvertDTO() {
    }

    public TITransferDetailsAllConvertDTO(List<TITransferDetailConvertDTO> tiTransferDetailsConvertDTOS, List<RefVoucherDTO> viewVouchers) {
        this.tiTransferDetailsConvertDTOS = tiTransferDetailsConvertDTOS;
        this.viewVouchers = viewVouchers;
    }

    public List<TITransferDetailConvertDTO> getTiTransferDetailsConvertDTOS() {
        return tiTransferDetailsConvertDTOS;
    }

    public void setTiTransferDetailsConvertDTOS(List<TITransferDetailConvertDTO> tiTransferDetailsConvertDTOS) {
        this.tiTransferDetailsConvertDTOS = tiTransferDetailsConvertDTOS;
    }

    public List<RefVoucherDTO> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucherDTO> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }
}
