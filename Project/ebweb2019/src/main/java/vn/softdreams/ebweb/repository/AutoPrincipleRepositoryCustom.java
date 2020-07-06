package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.AutoPrinciple;

import java.util.UUID;

public interface AutoPrincipleRepositoryCustom {
    Page<AutoPrinciple> pageableAllAutoPrinciple(Pageable sort, UUID org);
}
