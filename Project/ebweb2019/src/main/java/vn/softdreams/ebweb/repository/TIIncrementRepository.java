package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.TIIncrement;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the TIIncrement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TIIncrementRepository extends JpaRepository<TIIncrement, UUID>, TIIncrementRepositoryCustom {

    @Query(value = "select * from TIIncrement where id in ?1 order by Date desc", nativeQuery = true)
    List<TIIncrement> findAllByIdIn(List<UUID> uuids);
}
