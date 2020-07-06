package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import vn.softdreams.ebweb.domain.EbGroup;
import vn.softdreams.ebweb.domain.EbUserGroup;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Spring Data JPA repository for the EbAuthority entity.
 */
public interface EbUserGroupRepository extends JpaRepository<EbUserGroup, UUID> {
    @Query(value = "select a.* from EbUserGroup a where a.UserId = ?1 and a.GroupId = ?2", nativeQuery = true)
    List<EbUserGroup> findAllByUserIdAndGroupId(Long userId, UUID groupId);

    @Query(value = "select a.* from EbUserGroup a where a.UserId = ?1 and a.CompanyID = ?2", nativeQuery = true)
    List<EbUserGroup> findAllByUserIdAndCompanyID(Long userId, UUID companyID);

    @Query(value = "SELECT COUNT(*) FROM EbUserGroup WHERE CompanyID = ?1 ", nativeQuery = true)
    Integer countAllUser(UUID orgID);

    @Modifying
    @Query(value = "DELETE FROM EbUserGroup WHERE CompanyID = ?1 ", nativeQuery = true)
    void deleteByOrgID(UUID orgID);

    @Query(value = "select b.code from EbGroupAuth a join EbAuthority b on a.AuthorityId = b.id " +
        "where GroupId in (select GroupId from EbUserGroup where UserID = ?1 and CompanyID = ?2) " +
        "order by b.code", nativeQuery = true)
    List<String> getAuthoritiesByUserIdAndCompanyId(Long id, UUID org);
}
