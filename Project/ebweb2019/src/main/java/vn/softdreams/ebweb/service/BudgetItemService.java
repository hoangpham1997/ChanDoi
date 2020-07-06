package vn.softdreams.ebweb.service;

import com.sun.org.apache.xpath.internal.objects.XString;
import vn.softdreams.ebweb.domain.BudgetItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.BudgetItemConvertDTO;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing BudgetItem.
 */
public interface BudgetItemService {

    /**
     * Save a budgetItem.
     *
     * @param budgetItem the entity to save
     * @return the persisted entity
     */
    BudgetItem save(BudgetItem budgetItem);

    /**
     * Get all the budgetItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BudgetItem> findAll(Pageable pageable);

    /**
     * Get the "id" budgetItem.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<BudgetItem> findOne(UUID id);

    /**
     * Delete the "id" budgetItem.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Page<BudgetItemConvertDTO> getAllBudgetItemsActive();

    List<BudgetItem> findAllActive();
    List<BudgetItem> getBudgetItemsByCompanyID();

    List<BudgetItem> findAllBudgetItem();

    HandlingResultDTO deleteMulti(List<BudgetItem> budgetItems);
}
