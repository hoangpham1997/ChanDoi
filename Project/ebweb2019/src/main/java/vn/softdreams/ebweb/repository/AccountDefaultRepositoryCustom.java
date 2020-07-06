package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.Query;
import vn.softdreams.ebweb.domain.AccountDefault;
import vn.softdreams.ebweb.service.dto.AccountDefaultDTO;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data  repository for the AccountingObjectGroup entity.
 */
@SuppressWarnings("unused")

public interface AccountDefaultRepositoryCustom {
    List<AccountDefaultDTO> findAllForAccountDefaultCategory(UUID companyID);
}
