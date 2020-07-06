package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.Report.TITransferDTO;
import vn.softdreams.ebweb.service.dto.TITransferAndTIAdjustmentConvertDTO;
import vn.softdreams.ebweb.service.dto.ToolsConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.TITransferDetailConvertDTO;

import java.util.List;
import java.util.UUID;

public interface TITransferRepositoryCustom {

    List<TITransferDTO> getDetailsTITransferDTO (UUID id);

    List<ToolsConvertDTO> getToolsActiveByTITransfer(UUID org, Integer currentBook);

    Page<TITransferAndTIAdjustmentConvertDTO> getAllTITransferSearch(Pageable pageable, UUID org, String fromDate, String toDate, String keySearch, boolean currentBook);

    List<TITransferDetailConvertDTO> findDetailsByID(UUID id);
}
