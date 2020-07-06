package vn.softdreams.ebweb.service.dto;

import java.util.List;

public class FAIncrementAllDetailsConvertDTO {
    private List<FAIncrementDetailsConvertDTO> faIncrementDetailsConvertDTOS;

    private List<FAIncrementDetailRefVoucherConvertDTO> faIncrementDetailRefVoucherConvertDTOS;

    public FAIncrementAllDetailsConvertDTO() {
    }

    public FAIncrementAllDetailsConvertDTO(List<FAIncrementDetailsConvertDTO> faIncrementDetailsConvertDTOS, List<FAIncrementDetailRefVoucherConvertDTO> faIncrementDetailRefVoucherConvertDTOS) {
        this.faIncrementDetailsConvertDTOS = faIncrementDetailsConvertDTOS;
        this.faIncrementDetailRefVoucherConvertDTOS = faIncrementDetailRefVoucherConvertDTOS;
    }

    public List<FAIncrementDetailsConvertDTO> getFaIncrementDetailsConvertDTOS() {
        return faIncrementDetailsConvertDTOS;
    }

    public void setFaIncrementDetailsConvertDTOS(List<FAIncrementDetailsConvertDTO> faIncrementDetailsConvertDTOS) {
        this.faIncrementDetailsConvertDTOS = faIncrementDetailsConvertDTOS;
    }

    public List<FAIncrementDetailRefVoucherConvertDTO> getFaIncrementDetailRefVoucherConvertDTOS() {
        return faIncrementDetailRefVoucherConvertDTOS;
    }

    public void setFaIncrementDetailRefVoucherConvertDTOS(List<FAIncrementDetailRefVoucherConvertDTO> faIncrementDetailRefVoucherConvertDTOS) {
        this.faIncrementDetailRefVoucherConvertDTOS = faIncrementDetailRefVoucherConvertDTOS;
    }
}
