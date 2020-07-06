package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.GenCode;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the GenCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GenCodeRepository extends JpaRepository<GenCode, UUID>, GenCodeRepositoryCustom {
    List<GenCode> findByTypeGroupIDAndCompanyID(Integer typeGroupID, UUID companyID);
}
