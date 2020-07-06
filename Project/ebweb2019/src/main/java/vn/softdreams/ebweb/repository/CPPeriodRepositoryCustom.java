package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.CPPeriod;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.web.rest.dto.CPPeriodDTO;
import vn.softdreams.ebweb.web.rest.dto.CPUncompleteDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the CPPeriod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CPPeriodRepositoryCustom {

    Page<CPPeriodDTO> findAllByType(Pageable pageable, Integer type, UUID org, Integer typeLedger, Boolean isExport);

    Integer findPeriod(String fromDate, String toDate, List<UUID> costSetIDs, Integer type);

    List<CPPeriodDetailDTO> getAllCPPeriodDetailByCPPeriodID(UUID id);

    List<CPExpenseListDTO> getAllCPExpenseListByCPPeriodID(UUID id);

    List<CPAllocationExpenseDTO> getAllCPAllocationExpenseByCPPeriodID(UUID id);

    List<CPAllocationExpenseDetailDTO> getAllCPAllocationExpenseDetailByCPPeriodID(UUID id);

    List<CPUncompletesDTO> getAllCPUncompleteByCPPeriodID(UUID id);

    List<CPUncompleteDetailDTO> getAllCPUncompleteDetailByCPPeriodID(UUID id);

    List<CPResultDTO> getAllCPResultByCPPeriodID(UUID id);

    List<CPAllocationRateDTO> getAllCPAllocationRateByCPPeriodID(UUID id);

    List<CPAcceptanceDTO> getAllPAcceptanceByCPPeriodID(UUID id);

    List<CPAcceptanceDetailDTO> getAllCPAcceptanceDetailByCPPeriodID(UUID id);

    CPPeriodDTO getByID(UUID id);

    void deleteCPPeriod(List<UUID> uuids);

    List<CPAcceptanceDetailDTO> getAllCPAcceptanceDetailByCPPeriodIDSecond(UUID id);

}
