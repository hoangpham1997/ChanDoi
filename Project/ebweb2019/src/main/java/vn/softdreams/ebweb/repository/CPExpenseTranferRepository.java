package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.CPExpenseTranfer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.dto.KetChuyenChiPhiDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the CPExpenseTranfer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CPExpenseTranferRepository extends JpaRepository<CPExpenseTranfer, UUID>, CPExpenseTranferRepositoryCustom {

    @Query(value = "select Count(*) from CPExpenseTranfer a LEFT JOIN CPPeriod b ON a.CPPeriodID = b.ID where b.CompanyID = ?1 AND a.CPPeriodID = ?2 AND b.ID = ?2", nativeQuery = true)
    Integer checkExist(UUID org, UUID cPPeriodID);

    @Modifying
    @Query(value = "UPDATE CPExpenseTranfer SET Recorded = 0 WHERE ID IN ?1 AND CompanyID = ?2", nativeQuery = true)
    void multiUnRecord(List<UUID> uuidList, UUID companyID);

    @Modifying
    @Query(value = "DELETE FROM GeneralLedger WHERE ReferenceID IN ?1 AND CompanyID = ?2", nativeQuery = true)
    void deleteGL(List<UUID> uuidList, UUID companyID);

}
