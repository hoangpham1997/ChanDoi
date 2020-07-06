package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MBDepositDetailCustomer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MBDepositDetailCustomer.
 */
public interface MBDepositDetailCustomerService {

    /**
     * Save a mBDepositDetailCustomer.
     *
     * @param mBDepositDetailCustomer the entity to save
     * @return the persisted entity
     */
    MBDepositDetailCustomer save(MBDepositDetailCustomer mBDepositDetailCustomer);

    /**
     * Get all the mBDepositDetailCustomers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MBDepositDetailCustomer> findAll(Pageable pageable);


    /**
     * Get the "id" mBDepositDetailCustomer.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MBDepositDetailCustomer> findOne(UUID id);

    /**
     * Delete the "id" mBDepositDetailCustomer.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
