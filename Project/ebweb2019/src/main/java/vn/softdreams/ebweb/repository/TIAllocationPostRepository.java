package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.TIAllocationPost;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the TIAllocationPost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TIAllocationPostRepository extends JpaRepository<TIAllocationPost, UUID> {

    @Query(value ="select t.* from TIAllocation tia left join TIAllocationPost t on tia.ID = t.TIAllocationID where tia.ID = ?1 order by t.OrderPriority ", nativeQuery = true)
    List<TIAllocationPost> findByTIAllocationID(UUID id);
}
