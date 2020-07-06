package vn.softdreams.ebweb.repository;

import io.undertow.util.QValueParser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.domain.MaterialGoods;
import vn.softdreams.ebweb.service.dto.MGForPPOrderDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


/**
 * Spring Data  repository for the MaterialGoods entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialGoodsRepository extends JpaRepository<MaterialGoods, UUID>, MaterialGoodsRepositoryCustom {

//    @Query(value = "select a.id id, a.CompanyID companyID, a.materialGoodsCode materialGoodsCode, a.materialGoodsName materialGoodsName, b.IWQuantity - b.OWQuantity as materialGoodsInStock,a.RepositoryID repositoryID " +
//        "from MaterialGoods a " +
//        "         left join RepositoryLedger b on a.ID = b.MaterialGoodsID where a.CompanyID = ?1", nativeQuery = true)
//    List<MGForPPOrderConvertDTO> getMaterialGoodsForCombobox(String companyID);
    @Query(value = "select a.id, a.materialGoodsCode, a.materialGoodsName, b.IWQuantity - b.OWQuantity as materialGoodsInStock, a.unitId " +
        "from MaterialGoods a " +
        "         left join RepositoryLedger b on a.ID = b.MaterialGoodsID where a.CompanyID = ?1", nativeQuery = true)
    List<MGForPPOrderDTO> getMaterialGoodsForCombobox(String companyId);

    @Query(value = "select IIF(sum(Quantity) is null, 0, sum(Quantity)) from PPInvoiceDetail where MaterialGoodsID = ?1 and PPOrderDetailID = ?2", nativeQuery = true)
    Long getMaterialGoodsPPInvoiceQuantity(UUID id, UUID ppOrderDetailID);

    /**
     * @Author hieugie
     *
     * Lấy hàng hóa theo com id
     *
     * @param companyID
     * @return
     */
    List<MaterialGoods> findByCompanyIDAndIsActiveTrue(UUID companyID);

    @Query(value = "select id, MaterialGoodsCode, MaterialGoodsName from MaterialGoods where MaterialGoodsType = 2", nativeQuery = true)
    List<MaterialGoodsConvertDTO> ConvertToCombobox();

    @Query(value = "select b.* from MaterialGoods b where b.CompanyID = ?1 AND isActive = 1 ORDER BY MaterialGoodsCode", nativeQuery = true)
    List<MaterialGoods> findByCompanyIDAndIsActiveTrue(String companyID);

    @Query(value = "select * from MaterialGoods where id = ?1 ORDER BY MaterialGoodsCode", nativeQuery = true)
    Optional<MaterialGoods> getByUUID(String id);

    @Query(value = "select b.* from MaterialGoods b where b.CompanyID in ?1 AND isActive = 1 and MaterialGoodsCode = ?2", nativeQuery = true)
    MaterialGoods findByCompanyIDAndMaterialGoodsCodeAndIsActiveTrue(List<UUID> org, String maHang);

    @Query(value = "select * from MaterialGoods b where b.CompanyID = ?1 AND isActive = 1 ORDER BY MaterialGoodsCode", nativeQuery = true)
    List<MaterialGoods> findAllByIsActive(UUID companyID);

    @Query(value = "select b.* from MaterialGoods b where b.CompanyID in ?1 AND b.isActive = 1 and b.repositoryID = ?2 ORDER BY MaterialGoodsCode", nativeQuery = true)
    List<MaterialGoods> getAllMaterialGoodsActiveCompanyIdByRepository(List<UUID> org, UUID repositoryId);

    @Query(value = "select b.* from MaterialGoods b where b.CompanyID in ?1  AND isActive = 1 ORDER BY MaterialGoodsCode", nativeQuery = true)
    List<MaterialGoods> findAllByCompanyID(List<UUID> org);

    @Query(value = "select b.* from MaterialGoods b where b.CompanyID in ?1 and MaterialGoodsCode in ?2 ORDER BY MaterialGoodsCode", nativeQuery = true)
    List<MaterialGoods> findAllByCompanyIDAndMaterialGoodCode(List<UUID> org, Set<String> materialGoodsCodes);

    @Query(value = "select b.* from MaterialGoods b where b.CompanyID in ?1 and MaterialGoodsCode in ?2 ORDER BY MaterialGoodsCode", nativeQuery = true)
    List<MaterialGoods> findAllByCompanyIDAndMaterialGoodCode(List<UUID> org, List<String> materialGoodsCodes);

    @Query(value = "select id from MaterialGoods where UPPER(materialGoodsCode) = UPPER (?1) and CompanyID = ?2 and isActive = 1 ORDER BY MaterialGoodsCode", nativeQuery = true)
    UUID findIdByMaterialGoodsCode(String materialGoodsCode, UUID companyId);

    @Query(value = "select TOP(1) * from MaterialGoods where CompanyID = ?1 and unitID = ?2", nativeQuery = true)
    MaterialGoods findByCompanyIDAndUnitID(UUID companyID, UUID unitID);

    @Query(value = "select top(1) * from MaterialGoods where CompanyID in ?1 and MaterialGoodsCode = ?2 and id <> ?3", nativeQuery = true)
    MaterialGoods findByCompanyIDAndMaterialGoodsCode(List<UUID> companyID, String MaterialGoodsCode, UUID id);

    @Query(value = "select TOP(1) * from MaterialGoods where CompanyID = ?1 and RepositoryID = ?2", nativeQuery = true)
    MaterialGoods findByRepositoryIDAndCompanyID(UUID companyID, UUID RepositoryID);

    @Query(value = "select TOP(1) * from MaterialGoods where CompanyID = ?1 and MaterialGoodsCategoryID = ?2", nativeQuery = true)
    MaterialGoods findByCompanyIDAndMaterialGoodsCategoryID(UUID companyID, UUID MaterialGoodsCategoryID);

    @Query(value = "select count(*) from MaterialGoods b where upper(materialGoodsCode) = upper(?1) AND b.CompanyID = ?2", nativeQuery = true)
    int countByMaterialGoodsCodeIgnoreCaseAndIsActiveTrue(String materialGoodsCode, UUID companyID);

    @Query(nativeQuery = true, value = "select materialGoodsCode from MaterialGoods where id = ?1")
    String findMaterialGoodsCodeById(UUID id);

    @Query(value = "select MaterialGoodsID from ViewCheckConstraint where MaterialGoodsID in ?1", nativeQuery = true)
    List<String> getIDRefEmployee(List<UUID> uuids);

    @Query(value = "select m.id from MaterialGoods m, CostSet c " +
        " where m.id = c.MaterialGoodsID and m.id in ?1" +
        " union " +
        " select m.id from MaterialGoods m, MaterialQuantum mq " +
        " where m.id = mq.ObjectID and m.id in ?1" +
        " union " +
        "select m.id from MaterialGoods m, MaterialQuantumDetail mqd " +
        "where m.id = mqd.MaterialGoodsID and m.id in ?1", nativeQuery = true)
    List<String> getIDContraint(List<UUID> uuids);

//    @Query(value = "select c.MaterialGoodsID id  from CostSet c " +
//        "where MaterialGoodsID = '8A93FBE0-23FA-A544-830D-ED8CAB850BB0' " +
//        "union " +
//        "select m.ObjectID  from MaterialQuantum m " +
//        "where ObjectID = '8A93FBE0-23FA-A544-830D-ED8CAB850BB0'", nativeQuery = true)
//    List<String> getIDRefEmployee(List<UUID> uuids);


    @Query(value = "select m.MaterialGoodsCode from ViewCheckConstraint v left join MaterialGoods m on v.MaterialGoodsID = m.id " +
            "where v.CompanyID in ?1  and v.MaterialGoodsID is not null " +
            "    group by m.MaterialGoodsCode", nativeQuery = true)
    List<String> getMaterialGoodsCodeHasReference(List<UUID> uuids);

    @Modifying
    @Query(value = "delete MaterialGoods where id in ?1 ;", nativeQuery = true)
    void deleteByListID(List<UUID> uuids);

    @Modifying
    @Query(value = "delete MaterialGoods where CompanyID in ?1 ;", nativeQuery = true)
    void deleteAllByCompanyIdIn(List<UUID> companyID);

//    @Query(value = "select mg.id ID, mg.MaterialGoodsCode name from MaterialGoods mg where mg.id in ?1 and mg.id not in (select r.id from RepositoryLedger r where r.id in ?1)", nativeQuery = true)
//    List<ObjectDTO> getIDAndNameByIDS(List<UUID> materialGoodsIDs);
}
