package vn.softdreams.ebweb.service.dto.cashandbank;

import java.util.UUID;

public interface UnitConvertRateDTO {
    UUID getId();

    String getUnitName();

    String getFormula();

    Double getConvertRate();
}
