package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.MaterialQuantumDetails;
import vn.softdreams.ebweb.service.dto.MaterialQuantumDTO;
import vn.softdreams.ebweb.service.dto.MaterialQuantumDetailsDTO;

import java.util.List;
import java.util.UUID;

public interface MaterialQuantumDetailsRepositoryCustom {

    Page<MaterialQuantumDTO> findAllMaterialQuantumDTO(Pageable pageable, String fromDate, String toDate, UUID org);

    List<MaterialQuantumDetailsDTO> findByMaterialQuantumIDOrderByOrderPriority(List<UUID> id);
}
