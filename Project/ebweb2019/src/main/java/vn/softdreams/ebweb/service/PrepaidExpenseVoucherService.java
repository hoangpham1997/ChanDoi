package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.PrepaidExpenseVoucher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing PrepaidExpenseVoucher.
 */
public interface PrepaidExpenseVoucherService {

    /**
     * Save a prepaidExpenseVoucher.
     *
     * @param prepaidExpenseVoucher the entity to save
     * @return the persisted entity
     */
    PrepaidExpenseVoucher save(PrepaidExpenseVoucher prepaidExpenseVoucher);

    /**
     * Get all the prepaidExpenseVouchers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PrepaidExpenseVoucher> findAll(Pageable pageable);


    /**
     * Get the "id" prepaidExpenseVoucher.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PrepaidExpenseVoucher> findOne(UUID id);

    /**
     * Delete the "id" prepaidExpenseVoucher.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
