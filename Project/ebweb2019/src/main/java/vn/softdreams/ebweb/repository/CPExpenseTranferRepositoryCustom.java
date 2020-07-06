package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.CPExpenseTranfer;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.dto.CPExpenseTranferExportDTO;
import vn.softdreams.ebweb.service.dto.KetChuyenChiPhiDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the CPExpenseTranfer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CPExpenseTranferRepositoryCustom {

    List<KetChuyenChiPhiDTO> getCPExpenseTransferByCPPeriodID(UUID org, UUID cPPeriodID);

    Page<CPExpenseTranfer> findAll(Pageable sort, SearchVoucher searchVoucher, UUID org, Boolean isNoMBook);

    Page<CPExpenseTranferExportDTO> getAllCPExpenseTranfers(Pageable pageable, SearchVoucher searchVoucher, UUID org, Boolean isNoMBook);

    void multiDeleteCPExpenseTranfer(UUID org, List<UUID> uuidList_kccp);

    void multiDeleteCPExpenseTranferDetails(UUID org, List<UUID> uuidList_kccp);

    void deleteByCPPeriodID(List<UUID> uuids);

}
