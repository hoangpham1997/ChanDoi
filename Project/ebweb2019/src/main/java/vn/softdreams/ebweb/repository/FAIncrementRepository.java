package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.FAIncrement;
import vn.softdreams.ebweb.domain.TIIncrement;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the TIIncrement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FAIncrementRepository extends JpaRepository<FAIncrement, UUID>, FAIncrementRepositoryCustom {

    @Query(value = "select * from FAIncrement where id in ?1 order by Date desc", nativeQuery = true)
    List<FAIncrement> findAllByIdIn(List<UUID> uuids);
}
