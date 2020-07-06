package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.ExpenseItem;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the ExpenseItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpenseItemRepository extends JpaRepository<ExpenseItem, UUID>, ExpenseItemRepositoryCustom {
    /**
     * add by namnh
     *
     * @return
     */
    @Override
    List<ExpenseItem> findAll();

    List<ExpenseItem> findByIsActiveTrue();

    @Query(value = "select * from ExpenseItem b where b.CompanyID = ?1 AND isActive = 1  ORDER BY ExpenseItemCode", nativeQuery = true)
    List<ExpenseItem> findAllByIsActive(UUID companyID);

    @Query(value = "select * from ExpenseItem b where b.CompanyID in ?1 AND isActive = 1  ORDER BY ExpenseItemCode", nativeQuery = true)
    List<ExpenseItem> findAllExpenseItemByIsActive(List<UUID> companyID);

    @Query(value = "select * from ExpenseItem b where b.CompanyID = ?1  ORDER BY ExpenseItemCode", nativeQuery = true)
    List<ExpenseItem> getExpenseItemsByCompanyID(UUID companyID);

    @Query(value = "select id from ExpenseItem where UPPER(expenseItemCode) = UPPER (?1) and CompanyID = ?2 and isActive = 1", nativeQuery = true)
    UUID findIdByExoanseItemCode(String expenseItemCode, UUID companyId);

    @Query(value = "select * from ExpenseItem b where b.CompanyID = ?1  and isActive = 1", nativeQuery = true)
    List<ExpenseItem> getAllByCompanyID(UUID companyID);

    @Query(value = "select * from ExpenseItem b where b.CompanyID = ?1 ORDER BY ExpenseItemCode ", nativeQuery = true)
    List<ExpenseItem> findAllByAndCompanyID(UUID companyID);

    @Query(value = "select count(*) from ExpenseItem where ID <> ?1 and CompanyID = ?2 and ExpenseItemCode = ?3", nativeQuery = true)
    Integer checkDuplicateExpenseItemCode(UUID ID, UUID companyID, String ExpenseItemCode);

    @Query(value = "select * from ExpenseItem where CompanyID = ?1 and ParentID = ?2", nativeQuery = true)
    List<ExpenseItem> listChildByParentID(UUID CompanyID, UUID ParentID);

    @Query(value = "select count(*) from  ExpenseItem where CompanyID = ?1 and  ParentID = ?2 ", nativeQuery = true)
    Integer countSiblings(UUID ID, UUID ParenID);

    @Modifying
    @Query(value = "update ExpenseItem set IsParentNode = ?2 where ID = ?1", nativeQuery = true)
    Integer setParenNode(UUID ID, boolean isParentNode);

    @Query(value = "select count(*) from ExpenseItem where id = ?1 and isParentNode = 1", nativeQuery = true)
    Integer checkIsParentNode(UUID org);

    @Modifying
    @Query(value = "update ExpenseItem  set isActive = ?2  where ID = ?1", nativeQuery = true)
    Integer updateIsActive(UUID ID, boolean isActive);

    @Query(value = "select * from ExpenseItem where parentID = ?1", nativeQuery = true)
    List<ExpenseItem> findAllChildren(UUID org);

    @Query(value = "select count(*) from ViewCheckConstraint where CompanyID = ?1 and ExpenseItemID = ?2", nativeQuery = true)
    Integer checkLicense(UUID CompanyID, UUID ID);

    @Query(value = "select count(*) from ExpenseItem where parentID = ?1", nativeQuery = true)
    Integer checkChildren(UUID org);

    @Modifying
    @Query(value = "update ExpenseItem set isParentNode = ?2 WHERE id = ?1", nativeQuery = true)
    void updateIsParentNode(UUID org, boolean isParentNode);

    @Modifying
    void deleteByIdIn(List<UUID> listID);

    @Query(value = "select * from ExpenseItem where IsActive = 1 AND ID in (select DISTINCT ParentID from ExpenseItem where CompanyID = ?1 and ParentID is not null)", nativeQuery = true)
    List<ExpenseItem> listParentHasChild(UUID companyID);

    @Query(value = "SELECT * FROM ExpenseItem WHERE IsActive = 1 AND CompanyID IN ?1 Order by ExpenseItemCode", nativeQuery = true)
    List<ExpenseItem> getExpenseItemsAndIsDependent(List<UUID> org);

    @Query(value = "select id from Func_getCompany(?1, ?2)", nativeQuery = true)
    List<UUID> getIDByCompanyIDAndSimilarBranch(UUID CompanyID, Boolean isDependent);

    @Query(value = "SELECT * From ExpenseItem WHERE ID IN ?1 AND CompanyID = ?2", nativeQuery = true)
    List<ExpenseItem> findAllByListIDAndOrg(List<UUID> listExpenseItems, UUID orgGetData);

    @Query(value = "SELECT * from ExpenseItem WHERE ParentID = ?1 AND CompanyID = ?2", nativeQuery = true)
    List<ExpenseItem> findExpenseItemsByParentID(UUID id, UUID orgID);
}
