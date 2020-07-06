package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.CPUncomplete;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CPUncomplete entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CPUncompleteRepository extends JpaRepository<CPUncomplete, Long> {

}
