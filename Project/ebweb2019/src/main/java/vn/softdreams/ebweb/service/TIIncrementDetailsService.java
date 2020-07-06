package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.TIIncrementDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing TIIncrementDetails.
 */
public interface TIIncrementDetailsService {

    /**
     * Save a tIIncrementDetails.
     *
     * @param tIIncrementDetails the entity to save
     * @return the persisted entity
     */
    TIIncrementDetails save(TIIncrementDetails tIIncrementDetails);

    /**
     * Get all the tIIncrementDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TIIncrementDetails> findAll(Pageable pageable);


    /**
     * Get the "id" tIIncrementDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TIIncrementDetails> findOne(UUID id);

    /**
     * Delete the "id" tIIncrementDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
