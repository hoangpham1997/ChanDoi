package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.Toolledger;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the Toolledger entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ToolledgerRepository extends JpaRepository<Toolledger, UUID>, ToolledgerRepositoryCustom {

}
