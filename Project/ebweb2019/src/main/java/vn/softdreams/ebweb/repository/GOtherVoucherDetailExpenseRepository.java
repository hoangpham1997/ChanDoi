package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.GOtherVoucherDetailExpense;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the GOtherVoucherDetailExpense entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GOtherVoucherDetailExpenseRepository extends JpaRepository<GOtherVoucherDetailExpense, UUID>, GOtherVoucherDetailExpenseRepositoryCustom{
    @Modifying
    @Query(value = "delete from GotherVoucherDetailExpense where GOtherVoucherID = ?1", nativeQuery = true)
    void deleteByIdPB(UUID id);

    List<GOtherVoucherDetailExpense> findAllByGOtherVoucherID(UUID id);

    //    @Query(value = "SELECT * FROM GotherVoucherDetailExpense where GOtherVoucherID = ?1", nativeQuery = true)
//    List<GOtherVoucherDetailExpense> findAllByGOtherVoucherID(UUID id);
}
