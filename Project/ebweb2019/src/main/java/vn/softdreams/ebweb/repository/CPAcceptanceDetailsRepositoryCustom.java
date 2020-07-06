package vn.softdreams.ebweb.repository;

import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.CPAcceptanceDetails;
import vn.softdreams.ebweb.domain.CPUncompleteDetails;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the CPUncompleteDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CPAcceptanceDetailsRepositoryCustom {

    List<CPAcceptanceDetails> getByCosetID(String costSetIDs, LocalDate fromDate, UUID org, UUID id);

    void deleteByGOtherVoucherID(List<UUID> uuids);

}
