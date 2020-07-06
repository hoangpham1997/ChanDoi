package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.TIIncrement;
import vn.softdreams.ebweb.service.dto.TIIncrementConvertDTO;
import vn.softdreams.ebweb.service.dto.TIIncrementDetailRefVoucherConvertDTO;
import vn.softdreams.ebweb.service.dto.TIIncrementDetailsConvertDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TIIncrementRepositoryCustom {

    Page<TIIncrementConvertDTO> getAllTIIncrementsSearch(Pageable pageable, UUID org, String fromDate, String toDate, String keySearch, boolean currentBook);

    List<TIIncrementDetailsConvertDTO> findDetailsByID(UUID id);

    void updateUnrecord(List<UUID> uuidList);

    void deleteList(List<UUID> uuidList);

    Optional<TIIncrement> findByRowNum(Pageable pageable, UUID org, String fromDate, String toDate, Integer rowNum, String keySearch, boolean currentBook);

    List<TIIncrementDetailRefVoucherConvertDTO> getDataRefVouchers(UUID id, boolean currentBook);
}
