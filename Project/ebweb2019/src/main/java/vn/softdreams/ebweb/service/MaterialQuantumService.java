package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MaterialQuantum;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.MaterialQuantumDTO;
import vn.softdreams.ebweb.service.dto.MaterialQuantumGeneralDTO;
import vn.softdreams.ebweb.service.dto.ObjectsMaterialQuantumDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MaterialQuantum.
 */
public interface MaterialQuantumService {

    /**
     * Save a materialQuantum.
     *
     * @param materialQuantum the entity to save
     * @return the persisted entity
     */
    MaterialQuantum save(MaterialQuantum materialQuantum);

    /**
     * Get all the materialQuantums.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MaterialQuantum> findAll(Pageable pageable);


    /**
     * Get the "id" materialQuantum.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MaterialQuantum> findOne(UUID id);

    /**
     * Delete the "id" materialQuantum.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<MaterialQuantumGeneralDTO> findAllByCompanyID();

    List<ObjectsMaterialQuantumDTO> findAllObjectActive();

    Page<MaterialQuantumDTO> findAllMaterialQuantumDTO(Pageable pageable, String fromDate, String toDate);
}
