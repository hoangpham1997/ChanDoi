package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.CPOPN;
import vn.softdreams.ebweb.service.dto.CPOPNSDTO;

import java.util.List;
import java.util.UUID;

public interface CPOPNRepositoryCustom {
    Page<CPOPNSDTO> findAllByIsActive(Pageable pageable, List<UUID> uuidList, UUID companyID, Boolean isNoMBook);

    List<CPOPN> getByCosetID(List<UUID> collect);

}
