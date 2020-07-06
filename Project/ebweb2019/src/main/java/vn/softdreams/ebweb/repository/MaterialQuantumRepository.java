package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MaterialQuantum;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the MaterialQuantum entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialQuantumRepository extends JpaRepository<MaterialQuantum, UUID>, MaterialQuantumRepositoryCustom {

    @Query(value = "select * from MaterialQuantum where objectID = ?1 AND ID <> ?2 ", nativeQuery = true)
    List<MaterialQuantum> getListMaterialQuantum(UUID objectID, UUID id);
}
