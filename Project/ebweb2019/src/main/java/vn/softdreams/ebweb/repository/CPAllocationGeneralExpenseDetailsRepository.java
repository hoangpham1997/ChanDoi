package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.CPAllocationGeneralExpenseDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the CPAllocationGeneralExpenseDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CPAllocationGeneralExpenseDetailsRepository extends JpaRepository<CPAllocationGeneralExpenseDetails, UUID> {
    @Query(value = "Select * from CPAllocationGeneralExpenseDetail a " +
        "where a.CPAllocationGeneralExpenseID in (Select b.ID from CPAllocationGeneralExpense b where b.CPPeriodID = ?1)", nativeQuery = true)
    List<CPAllocationGeneralExpenseDetails> findAllByCPPeriodID(UUID cPPeriodID);

}
