package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.web.rest.dto.TIAuditDetailDTO;

import java.util.List;
import java.util.UUID;

public interface TIAuditDetailsRepositoryCustom {
    List<TIAuditDetailDTO> findTIAuditDetailDTO(UUID id);
}
