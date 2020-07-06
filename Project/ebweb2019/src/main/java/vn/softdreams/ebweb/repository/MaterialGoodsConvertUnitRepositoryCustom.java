package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MaterialGoodsConvertUnit;
import vn.softdreams.ebweb.web.rest.dto.MaterialGoodsConvertUnitDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MaterialGoodsConvertUnitRepositoryCustom {
    MaterialGoodsConvertUnit getMaterialGoodsConvertUnitPPInvoice(UUID materialGoodsId, UUID unitId);
    List<MaterialGoodsConvertUnitDTO> getAllDTO(List<UUID> companyIDs);
}
