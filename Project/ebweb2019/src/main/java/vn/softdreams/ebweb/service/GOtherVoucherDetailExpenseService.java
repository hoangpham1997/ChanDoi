package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.GOtherVoucherDetailExpense;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing GOtherVoucherDetailExpense.
 */
public interface GOtherVoucherDetailExpenseService {

    /**
     * Save a gOtherVoucherDetailExpense.
     *
     * @param gOtherVoucherDetailExpense the entity to save
     * @return the persisted entity
     */
    GOtherVoucherDetailExpense save(GOtherVoucherDetailExpense gOtherVoucherDetailExpense);

    /**
     * Get all the gOtherVoucherDetailExpenses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GOtherVoucherDetailExpense> findAll(Pageable pageable);


    /**
     * Get the "id" gOtherVoucherDetailExpense.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GOtherVoucherDetailExpense> findOne(UUID id);

    /**
     * Delete the "id" gOtherVoucherDetailExpense.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
