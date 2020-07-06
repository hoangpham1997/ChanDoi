package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.TIDecrementDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the TIDecrementDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TIDecrementDetailsRepository extends JpaRepository<TIDecrementDetails, UUID> {

}
