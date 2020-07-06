package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.OPAccount;
import vn.softdreams.ebweb.domain.OPMaterialGoods;
import vn.softdreams.ebweb.web.rest.dto.OPMaterialGoodsDTO;

import java.util.List;
import java.util.UUID;

public interface OPMaterialGoodsRepositoryCustom {
    List<OPMaterialGoodsDTO> findAllByAccountNumberAndCompanyIdAndTypeLedger(String accountNumber, UUID org, UUID repositoryId, Integer typeLedger);

    List<OPMaterialGoods> insertBulk(List<OPMaterialGoods> opMaterialGoods);
}
