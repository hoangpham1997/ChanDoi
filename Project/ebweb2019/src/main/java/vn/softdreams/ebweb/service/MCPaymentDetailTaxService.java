package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MCPaymentDetailTax;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MCPaymentDetailTax.
 */
public interface MCPaymentDetailTaxService {

    /**
     * Save a mCPaymentDetailTax.
     *
     * @param mCPaymentDetailTax the entity to save
     * @return the persisted entity
     */
    MCPaymentDetailTax save(MCPaymentDetailTax mCPaymentDetailTax);

    /**
     * Get all the mCPaymentDetailTaxes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MCPaymentDetailTax> findAll(Pageable pageable);


    /**
     * Get the "id" mCPaymentDetailTax.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MCPaymentDetailTax> findOne(UUID id);

    /**
     * Delete the "id" mCPaymentDetailTax.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
