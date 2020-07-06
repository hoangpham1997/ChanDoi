package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.CPPeriodDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing CPPeriodDetails.
 */
public interface CPPeriodDetailsService {

    /**
     * Save a cPPeriodDetails.
     *
     * @param cPPeriodDetails the entity to save
     * @return the persisted entity
     */
    CPPeriodDetails save(CPPeriodDetails cPPeriodDetails);

    /**
     * Get all the cPPeriodDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CPPeriodDetails> findAll(Pageable pageable);


    /**
     * Get the "id" cPPeriodDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CPPeriodDetails> findOne(UUID id);

    /**
     * Delete the "id" cPPeriodDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
