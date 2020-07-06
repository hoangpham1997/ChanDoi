package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.softdreams.ebweb.domain.*;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.EInvoice.SDS.Respone_SDS;
import vn.softdreams.ebweb.service.dto.EbUserGroupDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = {"authorities", "organizationUnits", "organizationUnits.groups"})
    Optional<User> findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = {"authorities", "ebGroups", "organizationUnits", "organizationUnits.groups"})
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = {"authorities", "organizationUnit", "organizationUnits.groups"})
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<User> findOneWithAuthoritiesByEmail(String email);

    Page<User> findAllByLoginNotOrderByLogin(Pageable pageable, String login);

    @EntityGraph(attributePaths = {"authorities", "ebGroups", "organizationUnits", "organizationUnits.groups"})
    Optional<User> findOneWithAuthoritiesByLoginAndOrganizationUnitsId(String login, UUID orgId);

    @EntityGraph(attributePaths = {"ebGroups", "organizationUnits", "organizationUnits.groups"})
    Optional<User> findOneByLoginAndOrganizationUnitsId(String login, UUID orgId);

    Long countByLoginAndOrganizationUnitsId(String login, UUID org);

    @Modifying
    @Query(value = "delete EbUserGroup where UserId = ?1", nativeQuery = true)
    void deleteByUserId(Long userId);

    @Modifying
    @Query(value = "delete EbUserOrganizationUnit where UserId = ?1", nativeQuery = true)
    void deleteEbOrgUnitByUserId(Long userId);

    @Query(value = "select COUNT(*) from EbUserOrganizationUnit a where a.UserId = ?1", nativeQuery = true)
    Long countEbOrgUnitsByUserId(Long userId);

    @Query(value = "select COUNT(*) from EbUserOrganizationUnit a where a.UserId = ?1 and a.OrgId = ?2", nativeQuery = true)
    Long countEbOrgUnitsByUserIdAndOrId(Long userId, UUID orgId);

    @Query(value = "select COUNT(*) from EbUserGroup a where a.UserId = ?1", nativeQuery = true)
    Long countEbUserGroupsByUserId(Long userId);

    @Query(value = "select COUNT(*) from EbUserGroup a where a.UserId = ?1 and a.companyId = ?2 and a.groupId = ?3", nativeQuery = true)
    Long countEbUserGroupsByUserIdAndOrgIdAndEbGroupId(Long userId, UUID orgId, UUID groupId);

    @Modifying
    @Query(value = "UPDATE EbUser SET job = ?1, full_name = ?2, password_hash = ?3, activated = ?4 " +
        " where id = ?5", nativeQuery = true)
    void updateEbUser(String job, String fullName, String encryptedPassword, boolean activated, Long id);

    @Modifying
    @Query(value = "UPDATE EbUserPackage SET PackageID = ?1, CompanyID = ?2, status = ?3 " +
        " where Userid = ?4", nativeQuery = true)
    void updateEbUserPackage(UUID packageID, UUID companyID, Integer status, Long userID);

    @Modifying
    @Query(value = "UPDATE EbUser SET orgID = ?1 " +
        " where id = ?2", nativeQuery = true)
    void updateEbUser(UUID orgID, Long id);

    @Modifying
    @Query(value = "insert into EbUserPackage(ID, UserID, PackageID, CompanyId, Status) values (NEWID(), ?1, ?2, ?3, ?4 )", nativeQuery = true)
    void insertEbUserPackage(Long userID, UUID packageID, UUID companyId, Integer status);

    @Query(value = "select COUNT(*) from EbUser a where a.login = ?1", nativeQuery = true)
    Long countEbUserByLogin(String login);

    @Query(value = "select COUNT(*) from EbUser a where parentID = ?1", nativeQuery = true)
    int countByUserId(Long UserId);

    @Query(value = "select * from EbUser " +
        "where parentID is null and id not in ( " +
        "    select UserId from EbUserPackage " +
        "    )", nativeQuery = true)
    List<User> getListUser();

    @Modifying
    @Query(value = "delete from EbUser where id = ?1 ", nativeQuery = true)
    void deleteById(Long id);

    @Modifying
    @Query(value = "delete from EbUser where parentID = ?1 ", nativeQuery = true)
    void deleteByParentID(Long id);

    @Query(value = "select * from EbUser where parentID is null or parentID = ?1", nativeQuery = true)
    List<User> findAllByParentID(Long id);

    @Query(value = "select TOP (1) a.login from (select * from EbUser where login like CONCAT(?1,'%[0-9]')) a " +
            "where substring(login, len(?1) + 1, 1) like '[0-9]%' order by len(a.login) desc, a.login DESC", nativeQuery = true)
    String getLoginUserByLogin(@Param("login") String login);

    @Query(value = "select * from EbUser where id = ?1", nativeQuery = true)
    User findOneById(Long id);

    @Query(value = "select * from EbUser where login = ?1", nativeQuery = true)
    User findOneByEmail(String email);

    @Query(value = "select * from EbUser where login = ?1", nativeQuery = true)
    User findOneByLoginReset(String login);

    @Modifying
    @Query(value = "insert into EbUserPackage(ID, UserID, PackageID, CompanyId, Status, ActivedDate, ExpriredDate) " +
        "values (NEWID(), ?1, ?2, ?3, ?4, ?5, ?6 )", nativeQuery = true)
    void insertEbUserPackageForActive(Long userID, UUID packageID, UUID orgID, Integer status, LocalDate activedDate, LocalDate expriredDate);

    @Modifying
    @Query(value = "UPDATE EbUserPackage SET PackageID = ?1, Status = ?2, ActivedDate = ?3, ExpriredDate = ?4 " +
        " where id = ?5", nativeQuery = true)
    void updateEbUserPackageForActive(UUID packageID, Integer status, LocalDate activedDate, LocalDate expriredDate, UUID id);

    @Modifying
    @Query(value = "UPDATE EbUserPackage SET PackageID = ?1, Status = ?2 " +
        " where id = ?3", nativeQuery = true)
    void updateEbUserPackageForInActive(UUID packageID, Integer status, UUID id);

    @Modifying
    @Query(value = "insert into EbUserAuthority " +
        "select ?1, code " +
        "from EbAuthority where ParentCode is not null", nativeQuery = true)
    void insertEbUserAuthorities(Long id);

    @Modifying
    @Query(value = "[Proc_DeleteUser_Package_Company] ?1", nativeQuery = true)
    void deleteUserPackageCompany(Long id);

    @Query(value = "SELECT AuthorityCode from EbUserAuthority where UserId = ?1", nativeQuery = true)
    List<String> getAllAuthoritiesByUserID(Long id);
}
