package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class MGForPPOrderQuantityExistsConvertDTO {
    private List<MGForPPOrderConvertDTO> mgForPPOrderConvertDTOS;
    private Long count;

    public MGForPPOrderQuantityExistsConvertDTO(List<MGForPPOrderConvertDTO> mgForPPOrderConvertDTOS, Long count) {
        this.mgForPPOrderConvertDTOS = mgForPPOrderConvertDTOS;
        this.count = count;
    }

    public List<MGForPPOrderConvertDTO> getMgForPPOrderConvertDTOS() {
        return mgForPPOrderConvertDTOS;
    }

    public void setMgForPPOrderConvertDTOS(List<MGForPPOrderConvertDTO> mgForPPOrderConvertDTOS) {
        this.mgForPPOrderConvertDTOS = mgForPPOrderConvertDTOS;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
