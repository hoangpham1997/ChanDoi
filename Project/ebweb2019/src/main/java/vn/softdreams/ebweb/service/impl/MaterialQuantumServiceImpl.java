package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.MaterialQuantumDetails;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.domain.RSInwardOutward;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.MaterialQuantumService;
import vn.softdreams.ebweb.domain.MaterialQuantum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.OrganizationUnitService;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.Constants.SystemOption.TCKHAC_SDDMDTTHCP;
import static vn.softdreams.ebweb.service.util.Constants.SystemOption.TCKHAC_SDDMVTHH;

/**
 * Service Implementation for managing MaterialQuantum.
 */
@Service
@Transactional
public class MaterialQuantumServiceImpl implements MaterialQuantumService {

    private final Logger log = LoggerFactory.getLogger(MaterialQuantumServiceImpl.class);

    private final MaterialQuantumRepository materialQuantumRepository;

    private final MaterialQuantumDetailsRepository materialQuantumDetailsRepository;

    private final UserService userService;

    private final OrganizationUnitRepository organizationUnitRepository;

    private final RSInwardOutwardRepository rsInwardOutwardRepository;
    private final OrganizationUnitService organizationUnitService;
    private final SystemOptionRepository systemOptionRepository;

    public MaterialQuantumServiceImpl(MaterialQuantumRepository materialQuantumRepository, MaterialQuantumDetailsRepository materialQuantumDetailsRepository,
                                      UserService userService, OrganizationUnitRepository organizationUnitRepository, RSInwardOutwardRepository rsInwardOutwardRepository,
                                      OrganizationUnitService organizationUnitService, SystemOptionRepository systemOptionRepository) {
        this.materialQuantumRepository = materialQuantumRepository;
        this.materialQuantumDetailsRepository = materialQuantumDetailsRepository;
        this.userService = userService;
        this.organizationUnitRepository = organizationUnitRepository;
        this.rsInwardOutwardRepository = rsInwardOutwardRepository;
        this.organizationUnitService = organizationUnitService;
        this.systemOptionRepository = systemOptionRepository;
    }

    /**
     * Save a materialQuantum.
     *
     * @param materialQuantum the entity to save
     * @return the persisted entity
     */
    @Override
    public MaterialQuantum save(MaterialQuantum materialQuantum) {
        log.debug("Request to save MaterialQuantum : {}", materialQuantum);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        MaterialQuantum mBD = new MaterialQuantum();
        int status = 1;
        if (currentUserLoginAndOrg.isPresent()) {
            materialQuantum.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            if (materialQuantum.getId() == null) {
                materialQuantum.setId(UUID.randomUUID());
                //gan id cha cho details con
                for (MaterialQuantumDetails details : materialQuantum.getMaterialQuantumDetails()) {
                    if (details.getId() != null) {
                        details.setId(null);
                    }
                    details.setMaterialQuantumID(materialQuantum.getId());
                }
            } else {
                if (Boolean.TRUE.equals(materialQuantum.getChange())) {
                    rsInwardOutwardRepository.updateAfterDeleteMaterialQuantum(currentUserLoginAndOrg.get().getOrg(), materialQuantum.getMaterialQuantumDetails().stream().collect(Collectors.toList()), materialQuantum.getId());
                }
            }
            List<MaterialQuantum> materialQuantums = materialQuantumRepository.getListMaterialQuantum(materialQuantum.getObjectID(), materialQuantum.getId());
            for (int i = 0; i < materialQuantums.size(); i++) {
                if (materialQuantum.getFromDate().compareTo(materialQuantums.get(i).getFromDate()) <= 0
                    && materialQuantum.getToDate().compareTo(materialQuantums.get(i).getToDate()) >= 0
                    || materialQuantum.getFromDate().compareTo(materialQuantums.get(i).getFromDate()) >= 0
                    && materialQuantum.getToDate().compareTo(materialQuantums.get(i).getToDate()) <= 0) {
                    status = 0;
                } else if (materialQuantum.getFromDate().compareTo(materialQuantums.get(i).getFromDate()) <= 0
                    && (materialQuantums.get(i).getFromDate().compareTo(materialQuantum.getToDate()) <= 0
                    && materialQuantum.getToDate().compareTo(materialQuantums.get(i).getToDate()) <= 0)) {
                    status = 0;
                } else if ((materialQuantums.get(i).getFromDate().compareTo(materialQuantum.getFromDate()) <= 0
                    && materialQuantum.getFromDate().compareTo(materialQuantums.get(i).getToDate()) <= 0
                    && materialQuantum.getToDate().compareTo(materialQuantums.get(i).getToDate()) >= 0)) {
                    status = 0;
                }
            }
        }
        if (status == 1) {
            mBD = materialQuantumRepository.save(materialQuantum);
            return mBD;
        } else {
            throw new BadRequestAlertException("Không thể lưu định mức nvl", "materialQuantum", "duplicate");
        }
    }

    /**
     * Get all the materialQuantums.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MaterialQuantum> findAll(Pageable pageable) {
        log.debug("Request to get all MaterialQuantums");
        return materialQuantumRepository.findAll(pageable);
    }


    /**
     * Get one materialQuantum by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialQuantum> findOne(UUID id) {
        log.debug("Request to get MaterialQuantum : {}", id);
        return materialQuantumRepository.findById(id);
    }

    /**
     * Delete the materialQuantum by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MaterialQuantum : {}", id);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<MaterialQuantumDetails> materialQuantumDetails = materialQuantumDetailsRepository.getAllMaterialQuantumDetails(id);
        rsInwardOutwardRepository.updateAfterDeleteMaterialQuantum(currentUserLoginAndOrg.get().getOrg(), materialQuantumDetails, id);
        materialQuantumRepository.deleteById(id);
    }

    public List<MaterialQuantumGeneralDTO> findAllByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return materialQuantumRepository.findAllByCompanyID(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMDTTHCP), systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMVTHH),currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<ObjectsMaterialQuantumDTO> findAllObjectActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
        return materialQuantumRepository.findAllObjectActive(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), Constants.SystemOption.TCKHAC_SDDMDTTHCP), systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMVTHH));
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public Page<MaterialQuantumDTO> findAllMaterialQuantumDTO(Pageable pageable, String fromDate, String toDate) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            return materialQuantumDetailsRepository.findAllMaterialQuantumDTO(pageable, fromDate, toDate, currentUserLoginAndOrg.get().getOrg());
        }
        return null;
    }
}
