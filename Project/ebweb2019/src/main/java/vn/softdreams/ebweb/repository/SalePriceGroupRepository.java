package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.SalePriceGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the SalePriceGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalePriceGroupRepository extends JpaRepository<SalePriceGroup, UUID> {

}
