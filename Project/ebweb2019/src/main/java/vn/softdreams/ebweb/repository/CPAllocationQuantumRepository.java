package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.CPAllocationQuantum;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the CPAllocationQuantum entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CPAllocationQuantumRepository extends JpaRepository<CPAllocationQuantum, UUID>,CPAllocationQuantumRepositoryCustom {

}
