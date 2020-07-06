package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.CPUncompleteDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the CPUncompleteDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CPUncompleteDetailsRepository extends JpaRepository<CPUncompleteDetails, UUID>, CPUncompleteDetailsRepositoryCustom {

    @Query(value = "Select * from CPUncompleteDetail a where a.CPPeriodID = ?1", nativeQuery = true)
    List<CPUncompleteDetails> findAllByCPPeriodID(UUID cPPeriodID);
}
