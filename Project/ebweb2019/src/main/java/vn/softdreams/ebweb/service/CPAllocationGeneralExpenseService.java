package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.CPAllocationGeneralExpense;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing CPAllocationGeneralExpense.
 */
public interface CPAllocationGeneralExpenseService {

    /**
     * Save a cPAllocationGeneralExpense.
     *
     * @param cPAllocationGeneralExpense the entity to save
     * @return the persisted entity
     */
    CPAllocationGeneralExpense save(CPAllocationGeneralExpense cPAllocationGeneralExpense);

    /**
     * Get all the cPAllocationGeneralExpenses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CPAllocationGeneralExpense> findAll(Pageable pageable);


    /**
     * Get the "id" cPAllocationGeneralExpense.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CPAllocationGeneralExpense> findOne(UUID id);

    /**
     * Delete the "id" cPAllocationGeneralExpense.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
