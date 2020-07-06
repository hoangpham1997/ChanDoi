package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.EbPackage;

public interface EbPackageRepositoryCustom {
    Page<EbPackage> findAllEbPackage(Pageable pageable);
}
