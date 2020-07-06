package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TIIncrementAllDetailsConvertDTO {
    private List<TIIncrementDetailsConvertDTO> tiIncrementDetailsConvertDTOS;

    private List<TIIncrementDetailRefVoucherConvertDTO> tiIncrementDetailRefVoucherConvertDTOS;

    public TIIncrementAllDetailsConvertDTO() {
    }

    public TIIncrementAllDetailsConvertDTO(List<TIIncrementDetailsConvertDTO> tiIncrementDetailsConvertDTOS, List<TIIncrementDetailRefVoucherConvertDTO> tiIncrementDetailRefVoucherConvertDTOS) {
        this.tiIncrementDetailsConvertDTOS = tiIncrementDetailsConvertDTOS;
        this.tiIncrementDetailRefVoucherConvertDTOS = tiIncrementDetailRefVoucherConvertDTOS;
    }

    public List<TIIncrementDetailsConvertDTO> getTiIncrementDetailsConvertDTOS() {
        return tiIncrementDetailsConvertDTOS;
    }

    public void setTiIncrementDetailsConvertDTOS(List<TIIncrementDetailsConvertDTO> tiIncrementDetailsConvertDTOS) {
        this.tiIncrementDetailsConvertDTOS = tiIncrementDetailsConvertDTOS;
    }

    public List<TIIncrementDetailRefVoucherConvertDTO> getTiIncrementDetailRefVoucherConvertDTOS() {
        return tiIncrementDetailRefVoucherConvertDTOS;
    }

    public void setTiIncrementDetailRefVoucherConvertDTOS(List<TIIncrementDetailRefVoucherConvertDTO> tiIncrementDetailRefVoucherConvertDTOS) {
        this.tiIncrementDetailRefVoucherConvertDTOS = tiIncrementDetailRefVoucherConvertDTOS;
    }
}
