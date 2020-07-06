package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.TIAdjustment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the TIAdjustment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TIAdjustmentRepository extends JpaRepository<TIAdjustment, UUID> , TIAdjustmentRepositoryCustom {

    @Query(value = "select * from TIAdjustment where ID = '8C65955F-5A58-46E2-9EF2-96D73589BA30'", nativeQuery = true)
    Optional<TIAdjustment> findByTIAdjustment ();

}
