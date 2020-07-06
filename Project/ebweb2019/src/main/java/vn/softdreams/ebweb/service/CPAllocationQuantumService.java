package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.CPAllocationQuantum;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.CPAllocationQuantumDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing CPAllocationQuantum.
 */
public interface CPAllocationQuantumService {

    /**
     * Save a cPAllocationQuantum.
     *
     * @param cPAllocationQuantum the entity to save
     * @return the persisted entity
     */
    CPAllocationQuantum save(CPAllocationQuantum cPAllocationQuantum);

    /**
     * Get all the cPAllocationQuantums.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CPAllocationQuantum> findAll(Pageable pageable);

    void saveAll(List<CPAllocationQuantum> cpAllocationQuantums);

    /**
     * Get the "id" cPAllocationQuantum.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CPAllocationQuantum> findOne(UUID id);

    /**
     * Delete the "id" cPAllocationQuantum.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Page<CPAllocationQuantumDTO> findAllActive(Pageable pageable);

    List<CPAllocationQuantum> findByCostSetID(List<UUID> ids);

}
