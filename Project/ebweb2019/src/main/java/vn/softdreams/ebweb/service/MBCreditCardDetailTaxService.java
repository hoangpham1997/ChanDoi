package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MBCreditCardDetailTax;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MBCreditCardDetailTax.
 */
public interface MBCreditCardDetailTaxService {

    /**
     * Save a mBCreditCardDetailTax.
     *
     * @param mBCreditCardDetailTax the entity to save
     * @return the persisted entity
     */
    MBCreditCardDetailTax save(MBCreditCardDetailTax mBCreditCardDetailTax);

    /**
     * Get all the mBCreditCardDetailTaxes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MBCreditCardDetailTax> findAll(Pageable pageable);


    /**
     * Get the "id" mBCreditCardDetailTax.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MBCreditCardDetailTax> findOne(UUID id);

    /**
     * Delete the "id" mBCreditCardDetailTax.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
