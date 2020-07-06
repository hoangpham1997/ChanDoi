package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.EbGroup;

import java.util.UUID;

public interface EbGroupRepositoryCustom {
    Page<EbGroup> findAllByCompanyID(Pageable pageable, UUID companyID);
}
