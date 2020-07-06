package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.CPResult;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the CPResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CPResultRepository extends JpaRepository<CPResult, UUID> {

}
