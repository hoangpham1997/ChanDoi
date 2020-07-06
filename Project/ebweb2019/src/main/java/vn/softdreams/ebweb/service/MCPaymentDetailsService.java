package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MCPaymentDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MCPaymentDetails.
 */
public interface MCPaymentDetailsService {

    /**
     * Save a mCPaymentDetails.
     *
     * @param mCPaymentDetails the entity to save
     * @return the persisted entity
     */
    MCPaymentDetails save(MCPaymentDetails mCPaymentDetails);

    /**
     * Get all the mCPaymentDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MCPaymentDetails> findAll(Pageable pageable);


    /**
     * Get the "id" mCPaymentDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MCPaymentDetails> findOne(UUID id);

    /**
     * Delete the "id" mCPaymentDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
