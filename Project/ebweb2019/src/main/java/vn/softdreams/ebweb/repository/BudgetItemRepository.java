package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.BudgetItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.BudgetItemConvertDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the BudgetItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BudgetItemRepository extends JpaRepository<BudgetItem, UUID> {

    @Query(value = "select id, BudgetItemCode, BudgetItemName from BudgetItem where IsActive = 1", nativeQuery = true)
    List<BudgetItemConvertDTO> findAllByIsActiveTrue();

    @Query(value = "select * from BudgetItem b where b.CompanyID = ?1 AND isActive = 1 ORDER BY BudgetItemCode", nativeQuery = true)
    List<BudgetItem> findAllByIsActive(UUID companyID);

    @Query(value = "select * from BudgetItem b where b.CompanyID = ?1 ORDER BY BudgetItemCode", nativeQuery = true)
    List<BudgetItem> getBudgetItemsByCompanyID(UUID companyID);

    @Query(value = "select id from budgetitem where UPPER(budgetitemcode) = UPPER (?1) and CompanyID = ?2 and isActive = 1", nativeQuery = true)
    UUID findIdByBudgetItemCode(String budgetItemCode, UUID companyId);

    @Query(value = "select * from BudgetItem b where b.CompanyID = ?1 order by BudgetItemCode", nativeQuery = true)
    Page<BudgetItem> findAll(UUID companyID, Pageable pageable);

    @Query(value = "select count(*) from BudgetItem where companyID = ?1 and budgetItemCode = ?2 COLLATE Latin1_General_CS_AS", nativeQuery = true)
    Integer checkDuplicateBudgetItem(UUID org, String code);

    @Modifying
    @Query(value = "update BudgetItem set isParentNode = ?2 WHERE id = ?1", nativeQuery = true)
    Integer updateIsParentNode(UUID org, boolean isParentNode);

    @Query(value = "select count(*) from BudgetItem where id = ?1 and isParentNode = 1", nativeQuery = true)
    Integer checkIsParentNode(UUID org);

    @Query(value = "select count (*) from BudgetItem where parentID = ?1", nativeQuery = true)
    Integer getCountChildren(UUID org);

    @Query(value = "select * from BudgetItem b where b.CompanyID = ?1 order by BudgetItemCode", nativeQuery = true)
    List<BudgetItem> findAllBudgetItem(UUID companyID);

    @Modifying
    @Query(value = "update BudgetItem set isActive = ?2 WHERE id = ?1", nativeQuery = true)
    Integer updateIsActive(UUID org, boolean isActive);

    @Query(value = "select * from BudgetItem where parentID = ?1", nativeQuery = true)
    List<BudgetItem> findAllChildren(UUID org);

    @Modifying
    @Query(value = "update BudgetItem set Grade = ?2 WHERE id = ?1", nativeQuery = true)
    Integer updateGrade(UUID org, Integer grade);

    @Modifying
    void deleteByIdIn(List<UUID> listID);
}
