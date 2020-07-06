package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.CPAllocationQuantum;
import vn.softdreams.ebweb.service.dto.CPAllocationQuantumDTO;
import vn.softdreams.ebweb.service.dto.CPProductQuantumDTO;

import java.util.List;
import java.util.UUID;

public interface CPAllocationQuantumRepositoryCustom {
    /**
     * @param companyID
     * @return
     */
    Page<CPAllocationQuantumDTO> findAllByIsActive(Pageable pageable, List<UUID> uuidList, UUID companyID, Boolean isNoMBook);

    List<CPAllocationQuantum> findByCostSetID(List<UUID> ids);

}
