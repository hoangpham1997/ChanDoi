package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.service.dto.UnitConvertRateDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class SaBillCreatedDetailAndIDDTO {
    private List<SaBillCreatedDetailDTO> saBillCreatedDetailDTO;
    private List<UUID> saBillList;

    public SaBillCreatedDetailAndIDDTO() {
    }

    public SaBillCreatedDetailAndIDDTO(List<SaBillCreatedDetailDTO> saBillCreatedDetailDTO, List<UUID> saBillList) {
        this.saBillCreatedDetailDTO = saBillCreatedDetailDTO;
        this.saBillList = saBillList;
    }

    public List<SaBillCreatedDetailDTO> getSaBillCreatedDetailDTO() {
        return saBillCreatedDetailDTO;
    }

    public void setSaBillCreatedDetailDTO(List<SaBillCreatedDetailDTO> saBillCreatedDetailDTO) {
        this.saBillCreatedDetailDTO = saBillCreatedDetailDTO;
    }

    public List<UUID> getSaBillList() {
        return saBillList;
    }

    public void setSaBillList(List<UUID> saBillList) {
        this.saBillList = saBillList;
    }
}
