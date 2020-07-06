package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.repository.SystemOptionRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.MaterialGoodsConvertUnitService;
import vn.softdreams.ebweb.domain.MaterialGoodsConvertUnit;
import vn.softdreams.ebweb.repository.MaterialGoodsConvertUnitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.web.rest.dto.MaterialGoodsConvertUnitDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.Constants.SystemOption.TCKHAC_SDDMVTHH;

/**
 * Service Implementation for managing MaterialGoodsConvertUnit.
 */
@Service
@Transactional
public class MaterialGoodsConvertUnitServiceImpl implements MaterialGoodsConvertUnitService {

    private final Logger log = LoggerFactory.getLogger(MaterialGoodsConvertUnitServiceImpl.class);

    private final MaterialGoodsConvertUnitRepository materialGoodsConvertUnitRepository;
    private final SystemOptionRepository systemOptionRepository;
    private final OrganizationUnitRepository organizationUnitRepository;

    public MaterialGoodsConvertUnitServiceImpl(MaterialGoodsConvertUnitRepository materialGoodsConvertUnitRepository, SystemOptionRepository systemOptionRepository, OrganizationUnitRepository organizationUnitRepository) {
        this.materialGoodsConvertUnitRepository = materialGoodsConvertUnitRepository;
        this.systemOptionRepository = systemOptionRepository;
        this.organizationUnitRepository = organizationUnitRepository;
    }

    /**
     * Save a materialGoodsConvertUnit.
     *
     * @param materialGoodsConvertUnit the entity to save
     * @return the persisted entity
     */
    @Override
    public MaterialGoodsConvertUnit save(MaterialGoodsConvertUnit materialGoodsConvertUnit) {
        log.debug("Request to save MaterialGoodsConvertUnit : {}", materialGoodsConvertUnit);        return materialGoodsConvertUnitRepository.save(materialGoodsConvertUnit);
    }

    /**
     * Get all the materialGoodsConvertUnits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MaterialGoodsConvertUnit> findAll(Pageable pageable) {
        log.debug("Request to get all MaterialGoodsConvertUnits");
        return materialGoodsConvertUnitRepository.findAll(pageable);
    }


    /**
     * Get one materialGoodsConvertUnit by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialGoodsConvertUnit> findOne(UUID id) {
        log.debug("Request to get MaterialGoodsConvertUnit : {}", id);
        return materialGoodsConvertUnitRepository.findById(id);
    }

    /**
     * Delete the materialGoodsConvertUnit by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MaterialGoodsConvertUnit : {}", id);
        materialGoodsConvertUnitRepository.deleteById(id);
    }

    /**
     * lấy tỉ lệ chuyển đổ với phép tính từ mua hàng qua kho
     * @author congnd
     * @param materialGoodsId id hàng
     * @param unitId id đơn vị tính
     * @return
     */
    @Override
    public MaterialGoodsConvertUnit getMaterialGoodsConvertUnitPPInvoice(UUID materialGoodsId, UUID unitId) {
        return materialGoodsConvertUnitRepository.getMaterialGoodsConvertUnitPPInvoice(materialGoodsId, unitId);
    }

    @Override
    public List<MaterialGoodsConvertUnitDTO> getAll() {
        SecurityDTO securityDTO = SecurityUtils.getCurrentUserLoginAndOrg().get();
        List<UUID> companyIDs = systemOptionRepository.getAllCompanyByCompanyIdAndCode(securityDTO.getOrg(), TCKHAC_SDDMVTHH);
        return materialGoodsConvertUnitRepository.getAllDTO(companyIDs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaterialGoodsConvertUnit> findByMaterialGoodsID(UUID id) {
        return materialGoodsConvertUnitRepository.findByMaterialGoodsID(id);
    }

    @Override
    public List<Integer> getNumberOrder(UUID companyID, Boolean similarBranch) {
        List<UUID> comIds = new ArrayList<>();
        if (similarBranch != null && similarBranch) {
            comIds = organizationUnitRepository.getCompanyAndBranch(companyID).stream().map(x -> x.getId()).collect(Collectors.toList());
        } else {
            comIds.add(companyID);
        }
        return materialGoodsConvertUnitRepository.getNumberOrder(comIds);
    }
}
