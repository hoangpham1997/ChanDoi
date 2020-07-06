package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.CPOPN;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.UUID;


/**
 * Spring Data  repository for the CPOPN entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CPOPNRepository extends JpaRepository<CPOPN, UUID>, CPOPNRepositoryCustom {


}
