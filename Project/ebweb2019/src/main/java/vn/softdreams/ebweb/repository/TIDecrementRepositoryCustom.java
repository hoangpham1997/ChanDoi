package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.Report.TIDecrementDTO;
import vn.softdreams.ebweb.service.dto.TIDecrementConvertDTO;
import vn.softdreams.ebweb.service.dto.TIDecrementDetailsConvertDTO;
import vn.softdreams.ebweb.service.dto.TIIncrementDetailsConvertDTO;

import java.util.List;
import java.util.UUID;

public interface TIDecrementRepositoryCustom {

    List<TIDecrementDTO> getDetailsTIDecrementDTO (UUID id);

    Page<TIDecrementConvertDTO> getAllTIDecrementSearch(Pageable pageable, UUID org, String fromDate, String toDate, String keySearch, boolean currentBook);

    List<TIDecrementDetailsConvertDTO> findDetailsByID(UUID id);

    void deleteList(List<UUID> uuidList);

    void updateUnrecord(List<UUID> uuidList);
}
