package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.FixedAssetDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the FixedAssetDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FixedAssetDetailsRepository extends JpaRepository<FixedAssetDetails, UUID> {

}
