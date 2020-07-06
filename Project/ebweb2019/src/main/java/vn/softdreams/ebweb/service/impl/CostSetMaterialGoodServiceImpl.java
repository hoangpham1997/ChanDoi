package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.repository.SystemOptionRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.CostSetMaterialGoodService;
import vn.softdreams.ebweb.domain.CostSetMaterialGood;
import vn.softdreams.ebweb.repository.CostSetMaterialGoodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.TheTinhGiaThanhDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing CostSetMaterialGood.
 */
@Service
@Transactional
public class CostSetMaterialGoodServiceImpl implements CostSetMaterialGoodService {

    private final Logger log = LoggerFactory.getLogger(CostSetMaterialGoodServiceImpl.class);

    private final CostSetMaterialGoodRepository costSetMaterialGoodRepository;
    private final SystemOptionRepository systemOptionRepository;
    private final UserService userService;
    private final OrganizationUnitRepository  organizationUnitRepository;

    public CostSetMaterialGoodServiceImpl(CostSetMaterialGoodRepository costSetMaterialGoodRepository, SystemOptionRepository systemOptionRepository, UserService userService, OrganizationUnitRepository organizationUnitRepository) {
        this.costSetMaterialGoodRepository = costSetMaterialGoodRepository;
        this.systemOptionRepository = systemOptionRepository;
        this.userService = userService;
        this.organizationUnitRepository = organizationUnitRepository;
    }

    /**
     * Save a costSetMaterialGood.
     *
     * @param costSetMaterialGood the entity to save
     * @return the persisted entity
     */
    @Override
    public CostSetMaterialGood save(CostSetMaterialGood costSetMaterialGood) {
        log.debug("Request to save CostSetMaterialGood : {}", costSetMaterialGood);
        return costSetMaterialGoodRepository.save(costSetMaterialGood);
    }

    /**
     * Get all the costSetMaterialGoods.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CostSetMaterialGood> findAll(Pageable pageable) {
        log.debug("Request to get all CostSetMaterialGoods");
        return costSetMaterialGoodRepository.findAll(pageable);
    }


    /**
     * Get one costSetMaterialGood by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CostSetMaterialGood> findOne(UUID id) {
        log.debug("Request to get CostSetMaterialGood : {}", id);
        return costSetMaterialGoodRepository.findById(id);
    }

    /**
     * Delete the costSetMaterialGood by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete CostSetMaterialGood : {}", id);
        costSetMaterialGoodRepository.deleteById(id);
    }

    @Override
    public List<TheTinhGiaThanhDTO> getAllByCompanyID(Integer typeMethod) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return costSetMaterialGoodRepository.getAllByCompanyID(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDTTHCP), systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMVTHH), typeMethod);
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<TheTinhGiaThanhDTO> getAllForReport(Integer typeMethod, Boolean isDependent, UUID orgID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<UUID> uuids = Utils.convertListStringToListUUIDReverse(organizationUnitRepository.findAllOrgStringAccType0(orgID));
        List<UUID> listUUIDVTHH = systemOptionRepository.getAllCompanyByCompanyIdAndCode(orgID, Constants.SystemOption.TCKHAC_SDDMVTHH);
        List<UUID> listUUIDDTTHCP = systemOptionRepository.getAllCompanyByCompanyIdAndCode(orgID, Constants.SystemOption.TCKHAC_SDDMDTTHCP);
        for (int i = 0; i < uuids.size(); i++) {
            Integer value = i;
            Optional<UUID> org = listUUIDVTHH.stream().filter(a -> a.equals(uuids.get(value))).findFirst();
            if (!org.isPresent()) {
                listUUIDVTHH.add(uuids.get(value));
            }
            Optional<UUID> org2 = listUUIDDTTHCP.stream().filter(a -> a.equals(uuids.get(value))).findFirst();
            if (!org2.isPresent()) {
                listUUIDDTTHCP.add(uuids.get(value));
            }
        }
        if (currentUserLoginAndOrg.isPresent()) {
            return costSetMaterialGoodRepository.getAllByCompanyID(listUUIDDTTHCP, listUUIDVTHH, typeMethod);
        }
        throw new BadRequestAlertException("", "", "");
    }
}
