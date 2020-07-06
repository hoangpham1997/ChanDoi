package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MCPaymentDetailVendor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MCPaymentDetailVendor.
 */
public interface MCPaymentDetailVendorService {

    /**
     * Save a mCPaymentDetailVendor.
     *
     * @param mCPaymentDetailVendor the entity to save
     * @return the persisted entity
     */
    MCPaymentDetailVendor save(MCPaymentDetailVendor mCPaymentDetailVendor);

    /**
     * Get all the mCPaymentDetailVendors.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MCPaymentDetailVendor> findAll(Pageable pageable);


    /**
     * Get the "id" mCPaymentDetailVendor.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MCPaymentDetailVendor> findOne(UUID id);

    /**
     * Delete the "id" mCPaymentDetailVendor.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
