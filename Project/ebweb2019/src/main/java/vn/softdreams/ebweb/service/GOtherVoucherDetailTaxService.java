package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.GOtherVoucherDetailTax;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing GOtherVoucherDetailTax.
 */
public interface GOtherVoucherDetailTaxService {

    /**
     * Save a gOtherVoucherDetailTax.
     *
     * @param gOtherVoucherDetailTax the entity to save
     * @return the persisted entity
     */
    GOtherVoucherDetailTax save(GOtherVoucherDetailTax gOtherVoucherDetailTax);

    /**
     * Get all the gOtherVoucherDetailTaxes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GOtherVoucherDetailTax> findAll(Pageable pageable);


    /**
     * Get the "id" gOtherVoucherDetailTax.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GOtherVoucherDetailTax> findOne(UUID id);

    /**
     * Delete the "id" gOtherVoucherDetailTax.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
