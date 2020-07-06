package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherDetailDTO;

import java.util.List;
import java.util.UUID;

public interface GOtherVoucherDetailsRepositoryCustom {

    List<GOtherVoucherDetailDTO> getGOtherVoucherViewID(UUID id, Integer currentBook);
}
