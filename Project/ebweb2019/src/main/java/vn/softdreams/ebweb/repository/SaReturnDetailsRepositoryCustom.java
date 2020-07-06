package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.service.dto.SaReturnDetailsRSInwardDTO;

import java.util.List;
import java.util.UUID;

public interface SaReturnDetailsRepositoryCustom {
    List<SaReturnDetailsRSInwardDTO> findBySaReturnOrderLstIDByOrderPriority(List<UUID> id, String currentBook);
}
