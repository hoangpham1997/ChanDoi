package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.Report.TIAdjustmentDTO;
import vn.softdreams.ebweb.service.dto.TIAdjustmentDetailAllConvertDTO;
import vn.softdreams.ebweb.service.dto.TIAdjustmentDetailConvertDTO;
import vn.softdreams.ebweb.service.dto.TITransferAndTIAdjustmentConvertDTO;

import java.util.List;
import java.util.UUID;

public interface TIAdjustmentRepositoryCustom {

    List<TIAdjustmentDTO> getListTIAdjustmentDTO (UUID id);

    Page<TITransferAndTIAdjustmentConvertDTO> getAllTITransferSearch(Pageable pageable, UUID org, String fromDate, String toDate, String keySearch, boolean currentBook);

    List<TIAdjustmentDetailConvertDTO> findDetailsByID(UUID id);
}
