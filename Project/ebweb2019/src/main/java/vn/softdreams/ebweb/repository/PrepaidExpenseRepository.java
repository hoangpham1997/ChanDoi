package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.repository.query.Param;
import vn.softdreams.ebweb.domain.AccountList;
import vn.softdreams.ebweb.domain.PrepaidExpense;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.PrepaidExpenseVoucher;
import vn.softdreams.ebweb.service.dto.PrepaidExpenseCodeDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the PrepaidExpense entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrepaidExpenseRepository extends JpaRepository<PrepaidExpense, UUID>, PrepaidExpenseRepositoryCustom {
    @Query(value = "select count(*) " +
        "from GotherVoucherDetailExpense govdtl " +
        "         left join Gothervoucher gov on gov.ID = govdtl.GOtherVoucherID " +
        "where govdtl.PrepaidExpenseID = ?1 " +
        "  and gov.TypeLedger = ?2", nativeQuery = true)
    Long countByPrepaidExpenseID(UUID id, String currentBook);

    @Query(value = "select pr.id, " +
        "       pr.companyid, " +
        "       pr.branchid, " +
        "       pr.typeledger, " +
        "       pr.typeexpense, " +
        "       pr.prepaidexpensecode, " +
        "       pr.prepaidexpensename, " +
        "       pr.date, " +
        "       pr.amount, " +
        "       COALESCE((select sum(AllocationAmount) " +
        "        from GotherVoucherDetailExpense gov " +
        "                 left join GOtherVoucher gv on gv.id = gov.GOtherVoucherID and TypeID = 709 " +
        "        where TypeID = 709 " +
        "          and CompanyID = :companyID " +
        "          and TypeLedger in (:currentBook, '2') " +
        "          and pr.id = gov.PrepaidExpenseID" +
        "          and gov.PrepaidExpenseID = :id), 0) + COALESCE(pr.AllocationAmount, 0) as allocationAmount, " +
        "       pr.allocationtime, " +
        "       case pr.TypeExpense " +
        "           when 1 then " +
        "               COALESCE((select count(*) " +
        "                         from GotherVoucherDetailExpense godtl " +
        "                                  left join PrepaidExpense pr on pr.ID = godtl.PrepaidExpenseID " +
        "                                  left join GotherVoucher gov on gov.id = godtl.GOtherVoucherID " +
        "                         where gov.TypeLedger = :currentBook " +
        "                           and godtl.PrepaidExpenseID = :id), 0) " +

        "           when 0 then COALESCE((select count(*) " +
        "                                 from GotherVoucherDetailExpense godtl " +
        "                                          left join PrepaidExpense pr on pr.ID = godtl.PrepaidExpenseID " +
        "                                          left join GotherVoucher gov on gov.id = godtl.GOtherVoucherID " +
        "                                 where gov.TypeLedger = :currentBook " +
        "                                   and godtl.PrepaidExpenseID = :id), 0) + " +
        "                       COALESCE(AllocatedPeriod, 0) end as  allocatedPeriod, " +
        "       pr.allocatedamount, " +
        "       pr.allocationaccount, " +
        "       pr.isactive " +
        "from PrepaidExpense pr " +
        "where pr.id = :id", nativeQuery = true)
    Optional<PrepaidExpense> findOneById(@Param("id") UUID id, @Param("currentBook") String currentBook, @Param("companyID") UUID companyID);

    @Query(value = "select * from PrepaidExpense where CompanyID =?1 AND isActive = 1 order by PrepaidExpenseCode asc", nativeQuery = true)
    List<PrepaidExpense> getPrepaidExpensesByCompanyID(UUID companyID);

    @Modifying
    void deleteByIdIn(List<UUID> listID);

    @Modifying
    @Query(value = "update PrepaidExpense set IsActive = CASE WHEN COALESCE(IsActive, 0) = 1 THEN 0 ELSE 1 END where id = ?1", nativeQuery = true)
    void updateIsActive(UUID id);
}
