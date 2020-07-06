package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.CareerGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the CareerGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CareerGroupRepository extends JpaRepository<CareerGroup, UUID> {

    @Query(value = "SELECT * FROM CareerGroup ORDER BY CareerGroupCode", nativeQuery = true)
    List<CareerGroup> findAllCareerGroups();

}
