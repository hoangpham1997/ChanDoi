package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.PrepaidExpenseAllocation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing PrepaidExpenseAllocation.
 */
public interface PrepaidExpenseAllocationService {

    /**
     * Save a prepaidExpenseAllocation.
     *
     * @param prepaidExpenseAllocation the entity to save
     * @return the persisted entity
     */
    PrepaidExpenseAllocation save(PrepaidExpenseAllocation prepaidExpenseAllocation);

    /**
     * Get all the prepaidExpenseAllocations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PrepaidExpenseAllocation> findAll(Pageable pageable);


    /**
     * Get the "id" prepaidExpenseAllocation.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PrepaidExpenseAllocation> findOne(UUID id);

    /**
     * Delete the "id" prepaidExpenseAllocation.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
