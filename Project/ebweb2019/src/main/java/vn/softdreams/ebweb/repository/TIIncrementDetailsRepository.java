package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.TIIncrementDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the TIIncrementDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TIIncrementDetailsRepository extends JpaRepository<TIIncrementDetails, UUID> {

}
