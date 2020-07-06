package vn.softdreams.ebweb.repository;

import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.TheTinhGiaThanhDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the CostSetMaterialGood entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CostSetMaterialGoodRepositoryCustom {

    List<TheTinhGiaThanhDTO> getAllByCompanyID(List<UUID> listOrgCS, List<UUID> listOrgMG, Integer typeMethod);
}
