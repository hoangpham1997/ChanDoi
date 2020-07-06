package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MBCreditCardDetailVendor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MBCreditCardDetailVendor.
 */
public interface MBCreditCardDetailVendorService {

    /**
     * Save a mBCreditCardDetailVendor.
     *
     * @param mBCreditCardDetailVendor the entity to save
     * @return the persisted entity
     */
    MBCreditCardDetailVendor save(MBCreditCardDetailVendor mBCreditCardDetailVendor);

    /**
     * Get all the mBCreditCardDetailVendors.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MBCreditCardDetailVendor> findAll(Pageable pageable);


    /**
     * Get the "id" mBCreditCardDetailVendor.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MBCreditCardDetailVendor> findOne(UUID id);

    /**
     * Delete the "id" mBCreditCardDetailVendor.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
