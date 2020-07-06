package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.SaBillDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the SaBillDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaBillDetailsRepository extends JpaRepository<SaBillDetails, UUID> {

    List<SaBillDetails> findBySaBillIdOrderByOrderPriority(UUID id);

    @Modifying
    @Query(value = "delete from SABillDetail where SABillID in ?1", nativeQuery = true)
    void deleteByListIds(List<UUID> saBillIds);
}
