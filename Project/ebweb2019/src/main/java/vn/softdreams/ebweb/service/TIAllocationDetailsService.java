package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.TIAllocationDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing TIAllocationDetails.
 */
public interface TIAllocationDetailsService {

    /**
     * Save a tIAllocationDetails.
     *
     * @param tIAllocationDetails the entity to save
     * @return the persisted entity
     */
    TIAllocationDetails save(TIAllocationDetails tIAllocationDetails);

    /**
     * Get all the tIAllocationDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TIAllocationDetails> findAll(Pageable pageable);


    /**
     * Get the "id" tIAllocationDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TIAllocationDetails> findOne(UUID id);

    /**
     * Delete the "id" tIAllocationDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
