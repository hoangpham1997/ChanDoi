package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.CostSetMaterialGood;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the CostSetMaterialGood entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CostSetMaterialGoodRepository extends JpaRepository<CostSetMaterialGood, UUID>, CostSetMaterialGoodRepositoryCustom {

}
