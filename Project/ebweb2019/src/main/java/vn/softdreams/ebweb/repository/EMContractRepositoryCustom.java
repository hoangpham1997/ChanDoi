package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.Query;
import vn.softdreams.ebweb.domain.EMContract;

import java.util.List;
import java.util.UUID;

public interface EMContractRepositoryCustom {
    List<EMContract> findAllByIsActive(UUID companyID, Boolean isNoMBook);

    List<EMContract> findAllForReport(List<UUID> listOrgID, boolean currentBook);
}
