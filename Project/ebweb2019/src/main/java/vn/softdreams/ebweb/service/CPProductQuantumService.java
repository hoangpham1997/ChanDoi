package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.CPProductQuantum;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.CPProductQuantumDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing CPProductQuantum.
 */
public interface CPProductQuantumService {

    /**
     * Save a cPProductQuantum.
     *
     * @param cPProductQuantum the entity to save
     * @return the persisted entity
     */
    CPProductQuantum save(CPProductQuantum cPProductQuantum);

    /**
     * Get all the cPProductQuantums.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CPProductQuantum> findAll(Pageable pageable);


    /**
     * Get the "id" cPProductQuantum.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CPProductQuantum> findOne(UUID id);

    /**
     * Delete the "id" cPProductQuantum.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Page<CPProductQuantumDTO> findAllActive(Pageable pageable);

    void saveAll(List<CPProductQuantum> cpProductQuantums);


}
