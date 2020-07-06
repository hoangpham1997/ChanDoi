package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.FixedAssetAccessories;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the FixedAssetAccessories entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FixedAssetAccessoriesRepository extends JpaRepository<FixedAssetAccessories, UUID> {

}
