package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import vn.softdreams.ebweb.domain.OPMaterialGoods;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public interface OPMaterialGoodsRepository extends JpaRepository<OPMaterialGoods, UUID>, OPMaterialGoodsRepositoryCustom {
    @Query(nativeQuery = true, value = " DELETE FROM opmaterialgoods WHERE ID IN ?1 ")
    @Modifying
    void deleteByIds(List<UUID> uuids);

    @Modifying
    void deleteAllByTypeIdAndCompanyIdAndTypeLedger(Integer typeId, UUID companyId, Integer typeLedger);

    Boolean existsByCompanyIdAndTypeLedger(UUID companyId, Integer typeLedger);

    @Query(nativeQuery = true, value = "select id from opmaterialgoods where companyID = ?1 and typeLedger = ?2")
    List<UUID> findAllUUIDForCompanyIDAndTypeLedger(UUID companyId, Integer typeLedger);
}
