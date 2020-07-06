package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.MaterialGoods;
import vn.softdreams.ebweb.domain.MaterialGoodsConvertUnit;
import vn.softdreams.ebweb.repository.MaterialGoodsConvertUnitRepository;
import vn.softdreams.ebweb.repository.MaterialGoodsRepository;
import vn.softdreams.ebweb.repository.SystemOptionRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.UnitService;
import vn.softdreams.ebweb.domain.Unit;
import vn.softdreams.ebweb.repository.UnitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.UtilsService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.UnitConvertRateDTO;
import vn.softdreams.ebweb.web.rest.dto.UnitSaveDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import static vn.softdreams.ebweb.service.util.Constants.SystemOption.TCKHAC_SDDMVTHH;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.Constants.SystemOption.TCKHAC_SDDMVTHH;

/**
 * Service Implementation for managing Unit.
 */
@Service
@Transactional
public class UnitServiceImpl implements UnitService {

    private final Logger log = LoggerFactory.getLogger(UnitServiceImpl.class);

    private final UnitRepository unitRepository;
    private final MaterialGoodsRepository materialGoodsRepository;
    private final MaterialGoodsConvertUnitRepository materialGoodsConvertUnitRepository;
    private final SystemOptionRepository systemOptionRepository;
    private final UtilsService utilsService;

    public UnitServiceImpl(UnitRepository unitRepository,
                           MaterialGoodsRepository materialGoodsRepository,
                           MaterialGoodsConvertUnitRepository materialGoodsConvertUnitRepository, SystemOptionRepository systemOptionRepository,
                           UtilsService utilsService) {
        this.unitRepository = unitRepository;
        this.materialGoodsRepository = materialGoodsRepository;
        this.materialGoodsConvertUnitRepository = materialGoodsConvertUnitRepository;
        this.systemOptionRepository = systemOptionRepository;
        this.utilsService = utilsService;
    }

    /**
     * Save a unit.
     *
     * @param unit the entity to save
     * @return the persisted entity
     */
    @Override
    public UnitSaveDTO save(Unit unit) {
        log.debug("Request to save Unit : {}", unit);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Unit un = new Unit();
        UnitSaveDTO unitSaveDTO = new UnitSaveDTO();
        if (currentUserLoginAndOrg.isPresent()) {
            if (unit.getCompanyID() == null) {
                unit.setCompanyID(currentUserLoginAndOrg.get().getOrgGetData());
            }
            if (unit.getId() == null) {
                unit.setIsActive(true);
                int count = unitRepository.countByCompanyIdAndIsActiveTrueAndUnitName(unit.getUnitName(), currentUserLoginAndOrg.get().getOrgGetData());
                if (count > 0) {
                    unitSaveDTO.setUnit(unit);
                    unitSaveDTO.setStatus(count);
                    return unitSaveDTO;
                } else {
                    un = unitRepository.save(unit);
                    unitSaveDTO.setUnit(un);
                    unitSaveDTO.setStatus(count);
                    return unitSaveDTO;
                }
            } else {
                un = unitRepository.save(unit);
                unitSaveDTO.setUnit(un);
                unitSaveDTO.setStatus(0);
                return unitSaveDTO;
            }
        }
        throw new BadRequestAlertException("", "", "");
    }

    /**
     * Get all the units.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Unit> findAll() {
        log.debug("Request to get all Units");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return currentUserLoginAndOrg.map(securityDTO -> unitRepository.findByCompanyIDAndIsActiveTrue(currentUserLoginAndOrg.get().getOrgGetData())).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Unit> pageableAllUnit(Pageable pageable) {
        log.debug("Request to get all AccountingObjects");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return unitRepository.pageableAllUnit(pageable, currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }


    /**
     * Get one unit by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Unit> findOne(UUID id) {
        log.debug("Request to get Unit : {}", id);
        return unitRepository.findById(id);
    }

    /**
     * Delete the unit by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Unit : {}", id);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        MaterialGoods materialGoods = materialGoodsRepository.findByCompanyIDAndUnitID(currentUserLoginAndOrg.get().getOrgGetData(), id);
        MaterialGoodsConvertUnit materialGoodsConvertUnit = materialGoodsConvertUnitRepository.findByMaterialGoodsIDAndUnitID(currentUserLoginAndOrg.get().getOrgGetData(), id);
        if (materialGoods != null || materialGoodsConvertUnit != null) {
            throw new BadRequestAlertException("Không thể xóa dữ liệu vì đã phát sinh chứng từ liên quan ", "", "");
        } else {
            unitRepository.deleteById(id);
        }
    }

    public HandlingResultDTO deleteUnit(List<UUID> uuids) {
        log.debug("Request to delete Unit : {}", uuids);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        List<String> lstFail = unitRepository.getIDRef(currentUserLoginAndOrg.get().getOrgGetData(), uuids);
        List<UUID> uuidsFail = new ArrayList<>();
        for (String id : lstFail) {
            uuidsFail.add(Utils.uuidConvertToGUID(UUID.fromString(id)));
        }
        List<UUID> uuidsFailDistinct = uuidsFail.stream().distinct().collect(Collectors.toList());
        List<UUID> uuidListDelete = uuids.stream().filter(n -> uuidsFailDistinct.stream().noneMatch(m -> m.compareTo(n) == 0)).collect(Collectors.toList());
        if (uuidListDelete.size() > 0){
            unitRepository.deleteByListID(uuidListDelete);
        }
        handlingResultDTO.setListIDFail(uuidsFailDistinct);
        handlingResultDTO.setCountFailVouchers(uuids.size() - uuidListDelete.size());
        handlingResultDTO.setCountTotalVouchers(uuids.size());
        handlingResultDTO.setCountSuccessVouchers(uuidListDelete.size());
        return handlingResultDTO;
    }

    @Override
    public List<UnitConvertRateDTO> getUnitByITIIncrementID(UUID tiIncrementID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return unitRepository.getUnitByITIIncrementID(currentUserLoginAndOrg.get().getOrgGetData(), tiIncrementID);
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public Page<Unit> findAll(Pageable pageable, String unitName, String unitDescription, Boolean isActive) {
        return unitRepository.findAll(pageable, unitName, unitDescription, isActive);
    }

    @Override
    public UnitConvertRateDTO getMainUnitName(UUID materialGoodsId) {
        return unitRepository.getMainUnitName(materialGoodsId);
    }

    @Override
    public List<UnitConvertRateDTO> convertRateForMaterialGoodsCombobox(UUID materialGoodsId) {
        return unitRepository.convertRateForMaterialGoodsCombobox(materialGoodsId);
    }

    public List<BigDecimal> unitPriceOriginalForMaterialGoods(UUID materialGoodsId, UUID unitId, String currencyCode) {
        return unitRepository.unitPriceOriginalForMaterialGoods(materialGoodsId, unitId, currencyCode);
    }

    @Override
    public Optional<Unit> findOneByID(UUID id) {
        return unitRepository.findOneByID(id.toString());
    }

    @Override
    public Optional<Unit> getUUIDUnit(UUID id) {
        return unitRepository.getUUIDUnit(id.toString());
    }

    @Override
    public List<UnitConvertRateDTO> convertRateForMaterialGoodsComboboxCustom(UUID materialGoodsId) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return unitRepository.convertRateForMaterialGoodsComboboxCustom(materialGoodsId,
                systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrgGetData(), TCKHAC_SDDMVTHH));
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<UnitConvertRateDTO> convertRateForMaterialGoods(UUID materialGoodsId) {
        return unitRepository.convertRateForMaterialGoods(materialGoodsId.toString());
    }

    public List<Unit> findAllActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return unitRepository.findAllByIsActive(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }


    public List<Unit> findAllByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return unitRepository.findAllByCompanyID(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<UnitConvertRateDTO> findAllWithConvertRate() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return unitRepository.findAllWithConvertRate(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<UnitConvertRateDTO> convertRateForMaterialGoodsComboboxCustomList(List<UUID> materialGoodsId) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return unitRepository.convertRateForMaterialGoodsComboboxCustomList(materialGoodsId, currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

}
