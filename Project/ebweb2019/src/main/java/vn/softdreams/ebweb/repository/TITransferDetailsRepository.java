package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.TITransferDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the TITransferDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TITransferDetailsRepository extends JpaRepository<TITransferDetails, UUID> {

}
