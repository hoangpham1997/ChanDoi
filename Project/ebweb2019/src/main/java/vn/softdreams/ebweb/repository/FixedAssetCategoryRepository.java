package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.FixedAssetCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the FixedAssetCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FixedAssetCategoryRepository extends JpaRepository<FixedAssetCategory, UUID> {

}
