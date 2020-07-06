package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.AutoPrinciple;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the AutoPrinciple entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AutoPrincipleRepository extends JpaRepository<AutoPrinciple, UUID>, AutoPrincipleRepositoryCustom {
    /**
     * add by namnh
     *
     * @return
     */
    @Override
    List<AutoPrinciple> findAll();

    @Query(value = "select * from AutoPrinciple b where b.CompanyID = ?1  and TypeID = 0 ORDER BY b.typeId, b.autoPrincipleName ", nativeQuery = true)
    List<AutoPrinciple> findAllByCompanyIDAndTypeID(UUID companyID);

    @Query(value = "select * from AutoPrinciple b where b.CompanyID = ?1 AND isActive = 1", nativeQuery = true)
    List<AutoPrinciple> findAllByIsActive(UUID companyID);

    @Query(value = "select * from AutoPrinciple b where b.CompanyID = ?1 ORDER BY b.typeId, b.autoPrincipleName ", nativeQuery = true)
    List<AutoPrinciple> findAllByCompanyID(UUID companyID);

    @Query(value = "select * from AutoPrinciple b where b.CompanyID = ?1 AND isActive = 1 AND (b.TypeID = ?2 or b.TypeID = 0) " +
        "order by case when b.TypeID = 0 then 0 else 1 end, b.AutoPrincipleName", nativeQuery = true)
    List<AutoPrinciple> getByTypeAndCompanyID(UUID org, Integer type);

}
