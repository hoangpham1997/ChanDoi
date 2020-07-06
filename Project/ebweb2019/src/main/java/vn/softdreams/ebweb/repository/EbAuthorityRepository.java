package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.softdreams.ebweb.domain.EbAuthority;
import vn.softdreams.ebweb.domain.EbGroup;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for the EbAuthority entity.
 */
public interface EbAuthorityRepository extends JpaRepository<EbAuthority, UUID> {
    @EntityGraph(attributePaths = {"authorities", "users"})
//    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<EbAuthority> findOneById(UUID id);

    @Query(value = "select count(1) from EbGroup where upper(Code) = upper(?1) and CompanyID = ?2", nativeQuery = true)
    int countByCodeIgnoreCase(String code, UUID comID);

    @Query(value = "select * from EbAuthority", nativeQuery = true)
    List<EbAuthority> findAllEbAuthority();

    @Query(value = "select * from EbAuthority order by code", nativeQuery = true)
    List<EbAuthority> findAllEbAuthorityOrderByCode();
}
