package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.TIAudit;
import vn.softdreams.ebweb.service.dto.TIAuditConvertDTO;
import vn.softdreams.ebweb.service.dto.ToolsDetailsTiAuditConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.TIAuditDetailByIDDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TIAuditRepositoryCustom {

    List<ToolsDetailsTiAuditConvertDTO> getTIAuditDetails(UUID org, String date, String checkPeriodic, Integer currentBook);

    List<ToolsDetailsTiAuditConvertDTO> getAllToolsByTiAuditID(UUID id);

    List<TIAuditDetailByIDDTO> findDetailsByID(UUID id);

    Page<TIAuditConvertDTO> getAllTIAuditSearch(Pageable pageable, UUID org, String fromDate, String toDate, String keySearch, boolean currentBook);

    Optional<TIAudit> findByRowNum(Pageable pageable, UUID org, String fromDate, String toDate, Integer rowNum, String keySearch, boolean currentBook);

    void deleteList(List<UUID> uuidList);

    void updateUnrecord(List<UUID> uuidList);
}
