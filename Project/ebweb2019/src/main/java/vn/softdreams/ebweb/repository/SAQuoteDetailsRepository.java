package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.SAQuoteDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the SAQuoteDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SAQuoteDetailsRepository extends JpaRepository<SAQuoteDetails, UUID> {

}
