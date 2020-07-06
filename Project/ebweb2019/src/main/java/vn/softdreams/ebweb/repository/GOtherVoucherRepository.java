package vn.softdreams.ebweb.repository;

import org.springframework.data.repository.query.Param;
import vn.softdreams.ebweb.domain.GOtherVoucher;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.GOtherVoucherDetailExpense;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the GOtherVoucher entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GOtherVoucherRepository extends JpaRepository<GOtherVoucher, UUID>, GOtherVoucherRepositoryCustom {
    @Query(value = "select * from GOtherVoucher where id = ?1", nativeQuery = true)
    GOtherVoucher getOneGOtherVoucher(UUID ListID);

    @Query(value = "select top 1 gv.PostedDate " +
        " from GotherVoucherDetailExpense gvde " +
        "         left join GOtherVoucher gv on gv.ID = gvde.GOtherVoucherID " +
        " where gvde.PrepaidExpenseID in " +
        "      (select gvde1.PrepaidExpenseID from GotherVoucherDetailExpense gvde1 where gvde1.GOtherVoucherID = ?1) " +
        "      and gv.TypeLedger = ?2 " +
        " order by gv.PostedDate desc", nativeQuery = true)
    Instant getMaxMonth(UUID id, String currentBook);

    @Query(value = "select top 1 PostedDate from GOtherVoucher where TypeLedger = ?2 and CompanyID = ?1 and TypeID = 709 order by PostedDate desc", nativeQuery = true)
    Instant getMaxMonthByCompanyID(UUID companyID, String currentBook);

    @Modifying
    void deleteByIdIn(List<UUID> listID);

    @Modifying
    @Query(value = "UPDATE GOtherVoucher SET Recorded = 0 WHERE ID IN ?1 AND CompanyID = ?2 ", nativeQuery = true)
    void updateUnRecord(List<UUID> listID, UUID companyID);

    @Modifying
    @Query(value = "Delete GeneralLedger WHERE ReferenceID IN ?1 AND CompanyID = ?2", nativeQuery = true)
    void deleteRefIDInGL(List<UUID> listID, UUID companyID);

    @Query(value = "select top 1 g.PostedDate " +
        " from GOtherVoucher g " +
        " where g.TypeId = 702 " +
        "      and g.Recorded = 1 " +
        "      and g.CompanyID = ?1 " +
        "      and g.TypeLedger = ?2 " +
        " order by g.PostedDate desc", nativeQuery = true)
    Instant getMaxDate(UUID id, String currentBook);

    @Query(value = "select top 1 g.PostedDate " +
        " from GOtherVoucher g " +
        " where g.TypeId = 702 " +
        "      and g.Recorded = 1 " +
        "      and g.CompanyID = ?1 " +
        "      and g.TypeLedger = ?2 " +
        "      and g.id != ?3 " +
        " order by g.PostedDate desc", nativeQuery = true)
    Instant getMaxDateNotId(UUID org, String currentBook, UUID id);
}
