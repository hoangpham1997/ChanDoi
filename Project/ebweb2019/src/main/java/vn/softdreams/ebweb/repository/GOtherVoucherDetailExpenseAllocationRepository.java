package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.GOtherVoucherDetailExpenseAllocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the GOtherVoucherDetailExpenseAllocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GOtherVoucherDetailExpenseAllocationRepository extends JpaRepository<GOtherVoucherDetailExpenseAllocation, UUID>, GOtherVoucherDetailExpenseAllocationRepositoryCustom  {
    @Modifying
    @Query(value = "delete from GOtherVoucherDetailExpenseAllocation where GOtherVoucherID = ?1", nativeQuery = true)
    void deleteByIdPB(UUID id);

//    @Query(value = "select * from GOtherVoucherDetailExpenseAllocation where GOtherVoucherID = ?1", nativeQuery = true)
//    List<GOtherVoucherDetailExpenseAllocation> findAllByGOtherVoucherID(UUID id);
}
