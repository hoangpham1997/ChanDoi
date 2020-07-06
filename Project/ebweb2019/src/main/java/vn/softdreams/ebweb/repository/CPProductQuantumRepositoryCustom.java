package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import vn.softdreams.ebweb.domain.CPProductQuantum;
import vn.softdreams.ebweb.service.dto.CPProductQuantumDTO;

import java.util.List;
import java.util.UUID;

public interface CPProductQuantumRepositoryCustom {
    /**
     * @param companyID
     * @return
     */
    Page<CPProductQuantumDTO> findAllByIsActive(Pageable pageable, List<UUID> companyID);

    List<CPProductQuantum> getByCosetID(List<UUID> collect);

}
