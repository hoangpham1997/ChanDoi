package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.ExpenseItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing ExpenseItem.
 */
public interface ExpenseItemService {

    List<ExpenseItem> getAllByCompanyID();

    List<ExpenseItem> findAllByCompanyID();

    List<ExpenseItem> findAllByAndCompanyID();

    /**
     * Save a expenseItem.
     *
     * @param expenseItem the entity to save
     * @return the persisted entity
     */
    ExpenseItem save(ExpenseItem expenseItem);

    /**
     * Get all the expenseItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ExpenseItem> findAll(Pageable pageable);

    /**
     * add by namnh
     *
     * @return
     */
    Page<ExpenseItem> findAll();


    /**
     * Get the "id" expenseItem.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ExpenseItem> findOne(UUID id);

    /**
     * Delete the "id" expenseItem.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Page<ExpenseItem> getExpenseItemActive();

    List<ExpenseItem> findAllActive();

    List<ExpenseItem> getExpenseItemSimilarBranch(Boolean similarBranch, UUID companyID);

    List<ExpenseItem> getExpenseItemsByCompanyID();

    HandlingResultDTO deleteMulti(List<ExpenseItem> expenseItems);

    List<ExpenseItem> findAllActiveByCompanyID(UUID companyID);

    List<ExpenseItem> getExpenseItemsAndIsDependent(Boolean isDependent, UUID orgID);
}
