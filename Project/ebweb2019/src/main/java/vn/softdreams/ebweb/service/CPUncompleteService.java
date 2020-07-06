package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.CPUncomplete;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing CPUncomplete.
 */
public interface CPUncompleteService {

    /**
     * Save a cPUncomplete.
     *
     * @param cPUncomplete the entity to save
     * @return the persisted entity
     */
    CPUncomplete save(CPUncomplete cPUncomplete);

    /**
     * Get all the cPUncompletes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CPUncomplete> findAll(Pageable pageable);


    /**
     * Get the "id" cPUncomplete.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CPUncomplete> findOne(Long id);

    /**
     * Delete the "id" cPUncomplete.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
