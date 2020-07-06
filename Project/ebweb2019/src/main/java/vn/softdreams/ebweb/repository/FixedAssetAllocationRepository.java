package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.FixedAssetAllocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the FixedAssetAllocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FixedAssetAllocationRepository extends JpaRepository<FixedAssetAllocation, UUID> {

}
