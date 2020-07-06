package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.TITransferDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing TITransferDetails.
 */
public interface TITransferDetailsService {

    /**
     * Save a tITransferDetails.
     *
     * @param tITransferDetails the entity to save
     * @return the persisted entity
     */
    TITransferDetails save(TITransferDetails tITransferDetails);

    /**
     * Get all the tITransferDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TITransferDetails> findAll(Pageable pageable);


    /**
     * Get the "id" tITransferDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TITransferDetails> findOne(UUID id);

    /**
     * Delete the "id" tITransferDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
