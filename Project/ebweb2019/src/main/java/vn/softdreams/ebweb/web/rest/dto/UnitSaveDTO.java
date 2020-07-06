package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.MBTellerPaper;
import vn.softdreams.ebweb.domain.Unit;

public class UnitSaveDTO {
    private Unit unit;

    private int status;

    public UnitSaveDTO() {
    }

    public UnitSaveDTO(Unit unit, int status) {
        this.unit = unit;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
