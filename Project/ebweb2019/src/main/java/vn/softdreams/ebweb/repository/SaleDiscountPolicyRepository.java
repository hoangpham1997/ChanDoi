package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.SaleDiscountPolicy;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the SaleDiscountPolicy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaleDiscountPolicyRepository extends JpaRepository<SaleDiscountPolicy, UUID>, SaleDiscountPolicyRepositoryCustom {

    @Query(value = "select * from SaleDiscountPolicy b where MaterialGoodsID = ?1", nativeQuery = true)
    List<SaleDiscountPolicy> findByMaterialGoodsID(UUID id);
}
