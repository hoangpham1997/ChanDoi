package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.PrepaidExpenseAllocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the PrepaidExpenseAllocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrepaidExpenseAllocationRepository extends JpaRepository<PrepaidExpenseAllocation, UUID>, PrepaidExpenseAllocationRepositoryCustom {
//    @Query(value = "select * from PrepaidExpenseAllocation where PrepaidExpenseID = ?1", nativeQuery = true)
//    List<PrepaidExpenseAllocation> findAllByExpenseListItemID(List<UUID> id);
    @Query(value = "select * from PrepaidExpenseAllocation where PrepaidExpenseID = ?1", nativeQuery = true)
    List<PrepaidExpenseAllocation> findByPrepaidExpenseID(UUID id);
}
