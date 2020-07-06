package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.CPAcceptance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing CPAcceptance.
 */
public interface CPAcceptanceService {

    /**
     * Save a cPAcceptance.
     *
     * @param cPAcceptance the entity to save
     * @return the persisted entity
     */
    CPAcceptance save(CPAcceptance cPAcceptance);

    /**
     * Get all the cPAcceptances.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CPAcceptance> findAll(Pageable pageable);


    /**
     * Get the "id" cPAcceptance.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CPAcceptance> findOne(Long id);

    /**
     * Delete the "id" cPAcceptance.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
