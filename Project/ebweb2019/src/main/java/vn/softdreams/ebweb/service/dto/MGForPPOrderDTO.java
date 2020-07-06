package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public interface MGForPPOrderDTO {
    UUID getId();
    UUID getCompanyID();
    String getMaterialGoodsCode();
    String getMaterialGoodsName();
    UUID getRepositoryID();
    UUID getUnitId();
    BigDecimal getMaterialGoodsInStock();
}
