package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.AccountList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.CostSet;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.service.dto.AccountListDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the AccountList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountListRepository extends JpaRepository<AccountList, UUID>, AccountListRepositoryCustom {

    @Query(value = "select * from AccountList where CompanyID =?1 AND isActive = 1 order by AccountNumber asc", nativeQuery = true)
    List<AccountList> findAllByIsActive(UUID companyID);

    @Query(value = "select * from AccountList where CompanyID =?1 AND isActive = 1 AND accountingtype = 0 AND IsParentNode = 0 AND (accountNumber LIKE '511%' OR accountNumber LIKE '711%')order by AccountNumber asc", nativeQuery = true)
    List<AccountList> findAllByIsActive1(UUID companyID);

    @Query(value = "select * from AccountList where CompanyID =?1 AND isActive = 1 AND accountingtype = 0 AND IsParentNode = 0 order by AccountNumber asc", nativeQuery = true)
    List<AccountList> findAllByIsActive2(UUID companyID);

    @Query(value = "select * from AccountList where CompanyID =?1 AND isActive = 1 AND IsParentNode = 0 order by AccountNumber asc", nativeQuery = true)
    List<AccountList> findAllForSystemOptions(UUID companyID);

    AccountList findByAccountNumberAndCompanyIDAndIsActiveTrue(String accountNumber, UUID companyId);

    @Query(value = "select * from AccountList where CompanyID =?1 AND isActive = 1 AND AccountingType = 0 order by AccountNumber", nativeQuery = true)
    List<AccountList> getAccountForOrganizationUnit(UUID companyID);

    @Query(value = "select * from AccountList b where b.CompanyID = ?1 ORDER BY AccountNumber", nativeQuery = true)
    List<AccountList> findAllAccountLists(UUID companyID);

    @Query(value = "select * from AccountList b where b.CompanyID = ?1 AND AccountNumber LIKE '154%' ORDER BY AccountNumber", nativeQuery = true)
    List<AccountList> getAccountStartWith154(UUID companyID);

    @Query(value = "select * from AccountList where CompanyID = ?1 AND AccountingType = 0 and DetailType like '%0%' and IsActive = 1 order by AccountNumber ", nativeQuery = true)
    List<AccountList> getAccountDetailType(UUID companyID);

    @Query(value = "select * from AccountList where CompanyID = ?1 AND AccountingType = 0 and DetailType like '%1%' and IsActive = 1 order by AccountNumber ", nativeQuery = true)
    List<AccountList> getAccountDetailTypeActive(UUID companyID);

    @Query(value = "select * from AccountList b where b.CompanyID = ?1 AND AccountNumber LIKE '112%' AND AccountingType = 0 AND IsActive = 1 ORDER BY AccountNumber", nativeQuery = true)
    List<AccountList> getAccountStartWith112(UUID companyID);

    @Query(value = "select * from AccountList b where b.CompanyID = ?1 AND AccountNumber LIKE '112%' And AccountNumber <> '112' AND AccountingType = 0 AND IsActive = 1 ORDER BY AccountNumber", nativeQuery = true)
    List<AccountList> getAccountStartWith112Except112(UUID companyID);

    @Query(value = "select * from AccountList b where b.CompanyID = ?1 AND AccountNumber LIKE '111%' AND AccountNumber <> '111' ORDER BY AccountNumber", nativeQuery = true)
    List<AccountList> getAccountStartWith111(UUID companyID);

    @Query(value = "select * from AccountList b where ParentAccountID = ?1 AND CompanyID = ?2", nativeQuery = true)
    List<AccountList> findByParentAccountID(UUID parentAccountID, UUID companyID);

    @Query(value = "select * from AccountList b where ID = ?1 AND CompanyID = ?2", nativeQuery = true)
    AccountList findByChildAccountID(UUID parentAccountID, UUID companyID);

    @Query(value = "select count(*) from AccountList b where b.CompanyID = ?1 AND ParentAccountID = ?2", nativeQuery = true)
    int countChildByParentID(UUID companyID, UUID parentID);

    @Query(value = "select count(*) from AccountList b where b.CompanyID = ?1 AND ParentAccountID = ?2", nativeQuery = true)
    int countChildByID(UUID companyID, UUID ID);

    @Query(value = "select TOP 1 DefaultAccount   " +
        "                                    from AccountDefault   " +
        "                                    where TypeID = ?2   " +
        "                                      and ColumnName = ?3   " +
        "                                      and PPType = ?4   " +
        "                                      and CompanyID = ?1", nativeQuery = true)
    String getAccountTypeDefault(UUID org, Integer typeID, String column, Boolean ppType);

    @Query(value = "SELECT DISTINCT Grade FROM AccountList WHERE CompanyID = ?1", nativeQuery = true)
    List<Integer> getGradeAccount(UUID org);

    @Query(value = "DECLARE " +
        "    @companyIDParent uniqueidentifier; " +
        "DECLARE " +
        "    @unitType int; " +
        "set @companyIDParent = (select parentID " +
        "                        from EbOrganizationUnit\n" +
        "                        where id = ?1 ) " +
        "set @unitType = (select unitType " +
        "                 from EbOrganizationUnit " +
        "                 where id = ?1 ) " +
        "SELECT MAX(Grade) " +
        "FROM AccountList " +
        "WHERE CompanyID = (case " +
        "                       when @unitType = 0 then ?1  " +
        "                       else @companyIDParent " +
        "    end)", nativeQuery = true)
    Integer getGradeMaxAccount(UUID org);

    @Query(value = "[CheckConstraintDeleteAccount] ?1, ?2", nativeQuery = true)
    Integer checkConstrainDelete(String accountNumber, UUID companyID);

    @Query(value = "select * from AccountList where CompanyID in ?1 and AccountingType = 0 and  IsActive = 1 and  IsParentNode = 0 order by AccountNumber", nativeQuery = true)
    List<AccountList> getAccountTypeFromGOV(List<UUID> org);

    @Query(value = "select * from AccountList b where b.CompanyID = ?1 order by AccountNumber", nativeQuery = true)
    List<AccountList> getAllAccountListByCompanyID(UUID companyID);

    @Query(value = "select * from AccountList b where ParentID = ?1 AND CompanyID = ?2", nativeQuery = true)
    List<AccountList> findByParentID(UUID parentAccountID, UUID companyID);

    @Query(value = "select COUNT(*) from ViewVoucherNoDetailForCloseBook d join ViewVoucherNoForCloseBook p on d.RefParentID = p.RefID WHERE DebitAccount = ?1 OR CreditAccount = ?1", nativeQuery = true)
    Integer checkUse(String accountNumber);

    @Query(value = "SELECT Count(*) FROM AccountList WHERE AccountNumber = ?1 AND CompanyID = ?2", nativeQuery = true)
    Integer findByAccountNumberAndCompanyID(String accountNumber, UUID orgID);

    @Query(value = "SELECT * From AccountList WHERE (AccountNumber IN ('632','635','641','642','811','154') OR DetailType = '8') AND CompanyID = ?1", nativeQuery = true)
    List<AccountList> getAccountForTHCPTheoKMCP(UUID orgGetData);

    @Query(value = "SELECT * from AccountList WHERE ParentAccountID = ?1 AND CompanyID = ?2", nativeQuery = true)
    List<AccountList> findAccountListByParentID(UUID id, UUID orgID);

    @Query(value = "SELECT * From AccountList WHERE AccountNumber IN ?1 AND CompanyID = ?2", nativeQuery = true)
    List<AccountList> findAllByListAccountNumberAndOrg(List<String> accountList, UUID orgGetData);

    @Query(value = "select id from Func_getCompany(?1, ?2)", nativeQuery = true)
    List<UUID> getIDByCompanyIDAndSimilarBranch(UUID CompanyID, Boolean isDependent);
}
