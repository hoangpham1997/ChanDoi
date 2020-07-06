package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.FixedAsset;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.FixedAssetDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the FixedAsset entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FixedAssetRepository extends JpaRepository<FixedAsset, UUID>, FixedAssetRepositoryCustom {
}
