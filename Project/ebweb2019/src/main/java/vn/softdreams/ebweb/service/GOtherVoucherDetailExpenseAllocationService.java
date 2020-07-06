package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.GOtherVoucherDetailExpenseAllocation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing GOtherVoucherDetailExpenseAllocation.
 */
public interface GOtherVoucherDetailExpenseAllocationService {

    /**
     * Save a gOtherVoucherDetailExpenseAllocation.
     *
     * @param gOtherVoucherDetailExpenseAllocation the entity to save
     * @return the persisted entity
     */
    GOtherVoucherDetailExpenseAllocation save(GOtherVoucherDetailExpenseAllocation gOtherVoucherDetailExpenseAllocation);

    /**
     * Get all the gOtherVoucherDetailExpenseAllocations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GOtherVoucherDetailExpenseAllocation> findAll(Pageable pageable);


    /**
     * Get the "id" gOtherVoucherDetailExpenseAllocation.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GOtherVoucherDetailExpenseAllocation> findOne(UUID id);

    /**
     * Delete the "id" gOtherVoucherDetailExpenseAllocation.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
