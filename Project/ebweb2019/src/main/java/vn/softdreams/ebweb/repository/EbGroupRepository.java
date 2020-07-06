package vn.softdreams.ebweb.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import vn.softdreams.ebweb.domain.EbAuthority;
import vn.softdreams.ebweb.domain.EbGroup;
import vn.softdreams.ebweb.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Spring Data JPA repository for the EbAuthority entity.
 */
public interface EbGroupRepository extends EbGroupRepositoryCustom, JpaRepository<EbGroup, UUID> {
    @EntityGraph(attributePaths = {"authorities", "users"})
//    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<EbGroup> findOneById(UUID id);

    @Query(value = "select count(1) from EbGroup where upper(Code) = upper(?1) and CompanyID = ?2", nativeQuery = true)
    int countByCodeIgnoreCase(String code, UUID comID);

    @Query(value = "select a.* from EbGroup a left join EbUserGroup b ON a.Id = b.GroupId where b.UserId = ?1", nativeQuery = true)
    Set<EbGroup> getEbGroupsByUserId(Long userId);

    @Query(value = "select * from EbGroup where companyID = ?1 order by code", nativeQuery = true)
    List<EbGroup> findAllByOrgId(UUID org);

    @Modifying
    @Query(value = "INSERT INTO EbOrganizationUnitGroup (orgId, groupId) values (?1, ?2) ", nativeQuery = true)
    void insertEbOrganizationUnitGroup(UUID org, UUID groupId);

    @Modifying
    @Query(value = "DELETE EbOrganizationUnitGroup where groupId = ?1", nativeQuery = true)
    void deleteEbOrganizationUnitGroupByGroupId(UUID groupId);

    @Query(value = "SELECT * FROM ebgroup where id in(select groupid from eborganizationunitgroup where orgId = ?1)", nativeQuery = true)
    List<EbGroup> findGroupsByOrgId(UUID org);
}
