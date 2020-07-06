package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.CPAllocationGeneralExpense;
import vn.softdreams.ebweb.service.dto.Report.GiaThanhAllocationPoPupDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the CPAllocationGeneralExpense entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CPAllocationGeneralExpenseRepositoryCustom {

    List<GiaThanhAllocationPoPupDTO> getAllocationPeriod(UUID org, String fromDate, String toDate, List<UUID> costSetID, Integer soLamViec);

    List<GiaThanhAllocationPoPupDTO> getAllocationPeriodSum(UUID org, String fromDate, String toDate, List<UUID> costSetID, Integer phienSoLamViec);

}
