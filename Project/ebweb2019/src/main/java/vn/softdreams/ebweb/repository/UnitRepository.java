package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.Unit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.UnitConvertRateDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the Unit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnitRepository extends JpaRepository<Unit, UUID>, UnitRepositoryCustom {

    List<Unit> findByCompanyIDAndIsActiveTrue(UUID org);

    /**
     * @Author hieugie
     *
     * Lấy unit theo hàng hóa
     *
     * @param materialGoodsID
     * @return
     */
    @Query(value = "select * from Unit where id in (select UnitID from MaterialGoods where id = ?1)", nativeQuery = true)
    List<Unit> getAllByMaterialGoodsID(UUID materialGoodsID);

//    /**
//     * @Author hieugie
//     *
//     * Lấy unit theo hhàng hóa quy đổi
//     *
//     * @param materialGoodsID
//     * @return
//     */
//    @Query(value = "select * from Unit where id in (select c.UnitID from MaterialGoodsConvertUnit c where c.materialGoodsID = ?1) ", nativeQuery = true)
//    List<Unit> getAllByMaterialGoodsConvertUnit(UUID materialGoodsID);

    @Query(value = "select b.id, b.UnitName, 1 as convertRate, '*' as Formula from MaterialGoods a left join Unit b on a.UnitID = b.ID where a.ID = ?1 " +
        "union all " +
        "select b.id, b.UnitName, a.ConvertRate, a.Formula from MaterialGoodsConvertUnit a left join Unit b on a.UnitID = b.ID where a.MaterialGoodsID = ?1", nativeQuery = true)
    List<UnitConvertRateDTO> convertRateForMaterialGoods(String materialGoodsId);

    @Query(value = "select b.id, b.UnitName, 1 as convertRate, '*' as Formula from MaterialGoods a left join Unit b on a.UnitID = b.ID where a.ID = ?1 " +
        "union all " +
        "select b.id, b.UnitName, a.ConvertRate, a.Formula from MaterialGoodsConvertUnit a left join Unit b on a.UnitID = b.ID where a.MaterialGoodsID = ?1", nativeQuery = true)
    List<UnitConvertRateDTO> convertRateForMaterialGoodsCombobox(UUID materialGoodsId);

//    @Query(value = "select u.id, u.UnitName, 1 as convertRate from MaterialGoods mg left join Unit u on mg.UnitID = u.ID where mg.ID = ?1 ", nativeQuery = true)
//    Optional<UnitConvertRateDTO> getMainUnitName(UUID materialGoodsId);

    @Query(value = "select UnitPrice " +
        "from MaterialGoodsPurchasePrice " +
        "where MaterialGoodsID = ?1 " +
        "and UnitID = ?2 " +
        "and CurrencyID = ?3 order by UnitPrice ", nativeQuery = true)
    List<BigDecimal> unitPriceOriginalForMaterialGoods(UUID materialGoodId, UUID unitId, String currencyId);

    @Query(value = "select * from Unit where id = ?1", nativeQuery = true)
    Optional<Unit> findOneByID(String id);

    @Query(value = "select * from Unit where id = ?1", nativeQuery = true)
    Optional<Unit> getUUIDUnit(String toString);

    @Query(value = "select top 1 * from Unit where UPPER(unitname) = ?1 and (id in (select UnitID from MaterialGoods where id = ?2) or id in (select c.UnitID from MaterialGoodsConvertUnit c where c.materialGoodsID = ?2)) and isActive = 1", nativeQuery = true)
    Unit findByCompanyIdAndIsActiveTrueAndUnitName(String unitName, UUID ord);

    @Query(value = "select * from Unit b where b.CompanyID = ?1 AND isActive = 1", nativeQuery = true)
    List<Unit> findAllByIsActive(UUID companyID);

    @Query(value = "select * from Unit b where b.CompanyID in ?1 AND isActive = 1", nativeQuery = true)
    List<Unit> findAllByIsActive(List<UUID> companyID);

    @Query(value = "select * from Unit b where b.CompanyID = ?1 AND isActive = 1 Order by b.unitName ", nativeQuery = true)
    List<Unit> findAllByCompanyID(UUID companyID);

    @Query(value = "select count(*) from Unit where UPPER(unitname) = UPPER (?1) and CompanyID = ?2 and isActive = 1", nativeQuery = true)
    int countByCompanyIdAndIsActiveTrueAndUnitName(String unitName, UUID org);

    @Query(value = "select id from Unit where UPPER(unitname) = UPPER (?1) and CompanyID = ?2 and isActive = 1", nativeQuery = true)
    UUID findIdByUnitName(String unitName, UUID companyId);

    @Query(value = "select UnitID from MaterialGoods where CompanyID = ?1 and UnitID in ?2" +
        " union all " +
        " select UnitID from MaterialGoodsConvertUnit where UnitID in ?2 ", nativeQuery = true)
    List<String> getIDRef(UUID orgID, List<UUID> uuids);

    @Modifying
    @Query(value = "delete Unit where id in ?1 ;", nativeQuery = true)
    void deleteByListID(List<UUID> uuids);
}
