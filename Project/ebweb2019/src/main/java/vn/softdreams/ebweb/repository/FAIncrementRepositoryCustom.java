package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.FAIncrement;
import vn.softdreams.ebweb.domain.TIIncrement;
import vn.softdreams.ebweb.service.dto.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FAIncrementRepositoryCustom {

    Page<FAIncrementConvertDTO> getAllFAIncrementsSearch(Pageable pageable, UUID org, String fromDate, String toDate, String keySearch, boolean currentBook);

    List<FAIncrementDetailsConvertDTO> findDetailsByID(UUID id);

    void updateUnRecord(List<UUID> uuidList);

    void deleteList(List<UUID> uuidList);

    Optional<FAIncrement> findByRowNum(Pageable pageable, UUID org, String fromDate, String toDate, Integer rowNum, String keySearch, boolean currentBook);

    List<FAIncrementDetailRefVoucherConvertDTO> getDataRefVouchers(UUID id, boolean currentBook);
}
