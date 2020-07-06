package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MCReceiptDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MCReceiptDetails.
 */
public interface MCReceiptDetailsService {

    /**
     * Save a mCReceiptDetails.
     *
     * @param mCReceiptDetails the entity to save
     * @return the persisted entity
     */
    MCReceiptDetails save(MCReceiptDetails mCReceiptDetails);

    /**
     * Get all the mCReceiptDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MCReceiptDetails> findAll(Pageable pageable);


    /**
     * Get the "id" mCReceiptDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MCReceiptDetails> findOne(UUID id);

    /**
     * Delete the "id" mCReceiptDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
