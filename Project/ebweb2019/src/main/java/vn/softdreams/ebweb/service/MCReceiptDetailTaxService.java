package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MCReceiptDetailTax;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MCReceiptDetailTax.
 */
public interface MCReceiptDetailTaxService {

    /**
     * Save a mCReceiptDetailTax.
     *
     * @param mCReceiptDetailTax the entity to save
     * @return the persisted entity
     */
    MCReceiptDetailTax save(MCReceiptDetailTax mCReceiptDetailTax);

    /**
     * Get all the mCReceiptDetailTaxes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MCReceiptDetailTax> findAll(Pageable pageable);


    /**
     * Get the "id" mCReceiptDetailTax.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MCReceiptDetailTax> findOne(UUID id);

    /**
     * Delete the "id" mCReceiptDetailTax.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
