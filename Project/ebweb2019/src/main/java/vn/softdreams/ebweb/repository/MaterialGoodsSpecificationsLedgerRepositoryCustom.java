package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.MaterialGoodsSpecificationsLedgerDTO;
import vn.softdreams.ebweb.service.dto.MaterialQuantumDTO;
import vn.softdreams.ebweb.service.dto.MaterialQuantumDetailsDTO;

import java.util.List;
import java.util.UUID;

public interface MaterialGoodsSpecificationsLedgerRepositoryCustom {

    List<MaterialGoodsSpecificationsLedgerDTO> findByMaterialGoodsID(UUID org, UUID id, UUID repositoryID);
}
