package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.CPExpenseList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing CPExpenseList.
 */
public interface CPExpenseListService {

    /**
     * Save a cPExpenseList.
     *
     * @param cPExpenseList the entity to save
     * @return the persisted entity
     */
    CPExpenseList save(CPExpenseList cPExpenseList);

    /**
     * Get all the cPExpenseLists.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CPExpenseList> findAll(Pageable pageable);

    List<CPExpenseList> findAllByCPPeriodID(UUID cPPeriodID);


    /**
     * Get the "id" cPExpenseList.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CPExpenseList> findOne(UUID id);

    /**
     * Delete the "id" cPExpenseList.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
