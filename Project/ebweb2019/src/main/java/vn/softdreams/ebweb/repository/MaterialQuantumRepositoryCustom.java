package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MaterialQuantum;
import vn.softdreams.ebweb.service.dto.MaterialQuantumGeneralDTO;
import vn.softdreams.ebweb.service.dto.ObjectsMaterialQuantumDTO;

import java.util.List;
import java.util.UUID;

public interface MaterialQuantumRepositoryCustom {

    List<ObjectsMaterialQuantumDTO> findAllObjectActive(List<UUID> listOrgIDDTTHCP, List<UUID> listOrgIDVTHH);

    List<MaterialQuantumGeneralDTO> findAllByCompanyID(List<UUID> companyIDCostSet, List<UUID> companyIDMaterialGoods, UUID currentOrg);


}
