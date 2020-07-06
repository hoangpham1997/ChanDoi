package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.web.rest.dto.TIAllocationDetailDTO;

import java.util.List;
import java.util.UUID;

public interface TIAllocationDetailsRepositoryCustom {
    List<TIAllocationDetailDTO> findTIAllocationDetails(UUID id);
}
