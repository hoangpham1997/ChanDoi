package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.Unit;
import vn.softdreams.ebweb.service.dto.UnitConvertRateDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UnitRepositoryCustom {
    /**
     * @param pageable
     * @param unitName
     * @param unitDescription
     * @param isActive
     * @return
     */
    Page<Unit> findAll(Pageable pageable, String unitName, String unitDescription, Boolean isActive);

    List<UnitConvertRateDTO> convertRateForMaterialGoodsComboboxCustom(UUID materialGoodsId, List<UUID> companyID);

    UnitConvertRateDTO getMainUnitName(UUID materialGoodsId);

    /***
     * @param materialGoodsId map <RepositoryId, map<MaterialGoodsId, UnitId>>
     * @return
     */
    List<UnitConvertRateDTO> getConvertUnitInfo(Map<String, Map<UUID, UUID>> materialGoodsId);

    List<UnitConvertRateDTO> findAllWithConvertRate(UUID companyID);
    Page<Unit> pageableAllUnit(Pageable sort, UUID org);

    List<UnitConvertRateDTO> convertRateForMaterialGoodsComboboxCustomList(List<UUID> materialGoodsId, UUID org);

    List<UnitConvertRateDTO> getUnitByITIIncrementID(UUID companyID, UUID incrementID);
}
