package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MaterialGoodsResourceTaxGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the MaterialGoodsResourceTaxGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialGoodsResourceTaxGroupRepository extends JpaRepository<MaterialGoodsResourceTaxGroup, UUID> {

}
