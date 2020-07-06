package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MCReceiptDetailCustomer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MCReceiptDetailCustomer.
 */
public interface MCReceiptDetailCustomerService {

    /**
     * Save a mCReceiptDetailCustomer.
     *
     * @param mCReceiptDetailCustomer the entity to save
     * @return the persisted entity
     */
    MCReceiptDetailCustomer save(MCReceiptDetailCustomer mCReceiptDetailCustomer);

    /**
     * Get all the mCReceiptDetailCustomers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MCReceiptDetailCustomer> findAll(Pageable pageable);


    /**
     * Get the "id" mCReceiptDetailCustomer.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MCReceiptDetailCustomer> findOne(UUID id);

    /**
     * Delete the "id" mCReceiptDetailCustomer.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
