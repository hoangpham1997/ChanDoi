package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.CPPeriod;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the CPPeriod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CPPeriodRepository extends JpaRepository<CPPeriod, UUID>, CPPeriodRepositoryCustom {

    @Query(value = "select * from CPPeriod  where CompanyID = ?1 ORDER BY Name", nativeQuery = true)
    List<CPPeriod> getAllCPPeriod(UUID org);

    @Query(value = "select * from CPPeriod  where CompanyID IN ?1 ORDER BY Name", nativeQuery = true)
    List<CPPeriod> findAllByOrgID(List<UUID> listOrgID);
}
