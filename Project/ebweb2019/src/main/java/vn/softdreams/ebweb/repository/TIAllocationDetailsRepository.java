package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.TIAllocationDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the TIAllocationDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TIAllocationDetailsRepository extends JpaRepository<TIAllocationDetails, UUID>, TIAllocationDetailsRepositoryCustom{

}
