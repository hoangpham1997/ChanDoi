package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.CPAcceptance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the CPAcceptance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CPAcceptanceRepository extends JpaRepository<CPAcceptance, Long> {

    @Query(value = "select Count(*) from CPAcceptance a where a.CPPeriodID = ?1", nativeQuery = true)
    Integer checkExist(UUID id);

    @Query(value = "select * from CPAcceptance a where a.CPPeriodID = ?1", nativeQuery = true)
    List<CPAcceptance> findByCPPeriod(UUID id);
}
