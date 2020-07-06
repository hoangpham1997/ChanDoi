package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.TIAdjustmentDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the TIAdjustmentDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TIAdjustmentDetailsRepository extends JpaRepository<TIAdjustmentDetails, UUID> {

}
