package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.CostSet;

public class CostSetSaveDTO {
    private CostSet costSet;

    private int status;

    public CostSetSaveDTO() {
    }

    public CostSetSaveDTO(CostSet costSet, int status) {
        this.costSet = costSet;
        this.status = status;
    }

    public CostSet getCostSet() {
        return costSet;
    }

    public void setCostSet(CostSet costSet) {
        this.costSet = costSet;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
