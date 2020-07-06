package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.Unit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.UnitConvertRateDTO;
import vn.softdreams.ebweb.web.rest.dto.UnitSaveDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing Unit.
 */
public interface UnitService {

    /**
     * Save a unit.
     *
     * @param unit the entity to save
     * @return the persisted entity
     */
    UnitSaveDTO save(Unit unit);

    /**
     * Get all the units.
     *
     * @return the list of entities
     */
    List<Unit> findAll();
    Page<Unit> pageableAllUnit(Pageable pageable);


    /**
     * Get the "id" unit.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Unit> findOne(UUID id);

    /**
     * Delete the "id" unit.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    /**
     * @Author hieugie
     *
     * @param pageable
     * @param unitName
     * @param unitDescription
     * @param isActive
     * @return
     */
    Page<Unit> findAll(Pageable pageable, String unitName, String unitDescription, Boolean isActive);

    List<UnitConvertRateDTO> convertRateForMaterialGoods(UUID materialGoodsId);

    UnitConvertRateDTO getMainUnitName(UUID materialGoodsId);

    List<UnitConvertRateDTO> convertRateForMaterialGoodsCombobox(UUID materialGoodsId);

    List<BigDecimal> unitPriceOriginalForMaterialGoods(UUID materialGoodsId, UUID unitId, String currencyCode);

    Optional<Unit> findOneByID(UUID id);

    Optional<Unit> getUUIDUnit(UUID id);

    List<UnitConvertRateDTO> convertRateForMaterialGoodsComboboxCustom(UUID materialGoodsId);

    List<Unit> findAllActive();

    List<Unit> findAllByCompanyID();

    List<UnitConvertRateDTO> findAllWithConvertRate();

    List<UnitConvertRateDTO> convertRateForMaterialGoodsComboboxCustomList(List<UUID> materialGoodsId);

    HandlingResultDTO deleteUnit(List<UUID> uuids);

    List<UnitConvertRateDTO> getUnitByITIIncrementID(UUID tiIncrementID);
}
