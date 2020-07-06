package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MCReceiptDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the MCReceiptDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MCReceiptDetailsRepository extends JpaRepository<MCReceiptDetails, UUID> {

}
