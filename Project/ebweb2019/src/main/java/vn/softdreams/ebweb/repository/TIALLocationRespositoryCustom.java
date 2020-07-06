package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.TIAllocation;
import vn.softdreams.ebweb.service.dto.TIAllocationConvertDTO;
import vn.softdreams.ebweb.service.dto.TIAllocationDetailConvertDTO;

import javax.tools.Tool;
import java.util.List;
import java.util.UUID;

public interface TIALLocationRespositoryCustom {

    List<TIAllocationDetailConvertDTO> getTIAllocationDetails(UUID orgID, Integer month, Integer year, Integer date, String checkPeriodic);

    List<Tool> getTIAllocations(UUID org, Integer month, Integer year);

    Long countAllByMonthAndYear(Integer month, Integer year, UUID org, String currentBook);

    TIAllocation getTIAllocationDuplicate(Integer month, Integer year, UUID org, String currentBook);

    Page<TIAllocationConvertDTO> getAllTIAllocationSearch(Pageable pageable, UUID org, String fromDate, String toDate, String keySearch, boolean currentBook);
}
