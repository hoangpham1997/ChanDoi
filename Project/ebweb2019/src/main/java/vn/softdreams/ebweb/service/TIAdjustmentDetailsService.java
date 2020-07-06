package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.TIAdjustmentDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing TIAdjustmentDetails.
 */
public interface TIAdjustmentDetailsService {

    /**
     * Save a tIAdjustmentDetails.
     *
     * @param tIAdjustmentDetails the entity to save
     * @return the persisted entity
     */
    TIAdjustmentDetails save(TIAdjustmentDetails tIAdjustmentDetails);

    /**
     * Get all the tIAdjustmentDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TIAdjustmentDetails> findAll(Pageable pageable);


    /**
     * Get the "id" tIAdjustmentDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TIAdjustmentDetails> findOne(UUID id);

    /**
     * Delete the "id" tIAdjustmentDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
