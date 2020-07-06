package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.CPAcceptanceDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the CPAcceptanceDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CPAcceptanceDetailsRepository extends JpaRepository<CPAcceptanceDetails, UUID>, CPAcceptanceDetailsRepositoryCustom {

    @Query(value = "select * from CPAcceptanceDetail a where a.CPPeriodID = ?1", nativeQuery = true)
    List<CPAcceptanceDetails> findByCPPeriod(UUID id);
}
