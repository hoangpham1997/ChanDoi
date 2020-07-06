package vn.softdreams.ebweb.repository;

import org.springframework.data.repository.query.Param;
import vn.softdreams.ebweb.domain.SAOrderDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.SaReturnDetails;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the SAOrderDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SAOrderDetailsRepository extends JpaRepository<SAOrderDetails, UUID> {

    @Query(value = "Select * from SaOrderDetail where sAOrderID in :ids", nativeQuery = true)
    List<SAOrderDetails> findBySaOrderIDOrderByOrderPriority(@Param("ids") List<UUID> ids);
}
