package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.web.rest.dto.TIAuditMemberDetailByIDDTO;
import vn.softdreams.ebweb.web.rest.dto.TIAuditMemberDetailDTO;

import java.util.List;
import java.util.UUID;

public interface TIAuditMemberDetailsRepositoryCustom {
    List<TIAuditMemberDetailDTO> findTIAuditMemberDetailDTO(UUID id);

    List<TIAuditMemberDetailByIDDTO> findAllByTiAuditID(UUID id);
}
