package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.MaterialQuantumDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.MaterialQuantumDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the MaterialQuantumDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialQuantumDetailsRepository extends JpaRepository<MaterialQuantumDetails, UUID>, MaterialQuantumDetailsRepositoryCustom {

    @Query(value = "Select * from MaterialQuantumDetail where MaterialQuantumID = ?1", nativeQuery = true)
    List<MaterialQuantumDetails> getAllMaterialQuantumDetails(UUID id);
}
