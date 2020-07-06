package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.CPExpenseList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the CPExpenseList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CPExpenseListRepository extends JpaRepository<CPExpenseList, UUID> {

    @Query(value = "Select * from CPExpenseList a where a.CPPeriodID = ?1", nativeQuery = true)
    List<CPExpenseList> findAllByCPPeriodID(UUID cPPeriodID);
}
