package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.CPPeriodDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the CPPeriodDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CPPeriodDetailsRepository extends JpaRepository<CPPeriodDetails, UUID> {

}
