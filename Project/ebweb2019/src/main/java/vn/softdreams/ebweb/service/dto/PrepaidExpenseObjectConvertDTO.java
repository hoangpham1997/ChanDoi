package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.PrepaidExpenseAllocation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class PrepaidExpenseObjectConvertDTO {
    private List<PrepaidExpenseConvertDTO> prepaidExpenseConvertDTOS;
    private List<PrepaidExpenseAllocationConvertDTO> prepaidExpenseAllocations;

    public PrepaidExpenseObjectConvertDTO() {
    }

    public PrepaidExpenseObjectConvertDTO(List<PrepaidExpenseConvertDTO> prepaidExpenseConvertDTOS, List<PrepaidExpenseAllocationConvertDTO> prepaidExpenseAllocations) {
        this.prepaidExpenseConvertDTOS = prepaidExpenseConvertDTOS;
        this.prepaidExpenseAllocations = prepaidExpenseAllocations;
    }

    public List<PrepaidExpenseConvertDTO> getPrepaidExpenseConvertDTOS() {
        return prepaidExpenseConvertDTOS;
    }

    public void setPrepaidExpenseConvertDTOS(List<PrepaidExpenseConvertDTO> prepaidExpenseConvertDTOS) {
        this.prepaidExpenseConvertDTOS = prepaidExpenseConvertDTOS;
    }

    public List<PrepaidExpenseAllocationConvertDTO> getPrepaidExpenseAllocations() {
        return prepaidExpenseAllocations;
    }

    public void setPrepaidExpenseAllocations(List<PrepaidExpenseAllocationConvertDTO> prepaidExpenseAllocations) {
        this.prepaidExpenseAllocations = prepaidExpenseAllocations;
    }
}
