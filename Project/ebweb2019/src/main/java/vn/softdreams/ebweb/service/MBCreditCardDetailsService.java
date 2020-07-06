package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MBCreditCardDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MBCreditCardDetails.
 */
public interface MBCreditCardDetailsService {

    /**
     * Save a mBCreditCardDetails.
     *
     * @param mBCreditCardDetails the entity to save
     * @return the persisted entity
     */
    MBCreditCardDetails save(MBCreditCardDetails mBCreditCardDetails);

    /**
     * Get all the mBCreditCardDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MBCreditCardDetails> findAll(Pageable pageable);


    /**
     * Get the "id" mBCreditCardDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MBCreditCardDetails> findOne(UUID id);

    /**
     * Delete the "id" mBCreditCardDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
