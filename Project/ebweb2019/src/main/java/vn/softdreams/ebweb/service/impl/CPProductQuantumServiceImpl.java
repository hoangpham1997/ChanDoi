package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.CPProductQuantumService;
import vn.softdreams.ebweb.domain.CPProductQuantum;
import vn.softdreams.ebweb.repository.CPProductQuantumRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.OrganizationUnitService;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.dto.CPProductQuantumDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CPProductQuantum.
 */
@Service
@Transactional
public class CPProductQuantumServiceImpl implements CPProductQuantumService {

    private final Logger log = LoggerFactory.getLogger(CPProductQuantumServiceImpl.class);

    private final CPProductQuantumRepository cPProductQuantumRepository;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final OrganizationUnitService organizationUnitService;
    private final UserService userService;

    public CPProductQuantumServiceImpl(CPProductQuantumRepository cPProductQuantumRepository,
                                       OrganizationUnitService organizationUnitService,
                                       OrganizationUnitRepository organizationUnitRepository, UserService userService) {
        this.cPProductQuantumRepository = cPProductQuantumRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.organizationUnitService = organizationUnitService;
        this.userService = userService;
    }

    /**
     * Save a cPProductQuantum.
     *
     * @param cPProductQuantum the entity to save
     * @return the persisted entity
     */
    @Override
    public CPProductQuantum save(CPProductQuantum cPProductQuantum) {
        log.debug("Request to save CPProductQuantum : {}", cPProductQuantum);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            if (cPProductQuantum.getCompanyID() == null) {
                cPProductQuantum.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            }
        }
        return cPProductQuantumRepository.save(cPProductQuantum);
    }

    @Override
    public void saveAll(List<CPProductQuantum> cpProductQuantums) {
        log.debug("Request to save CPProductQuantum : {}", cpProductQuantums);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            for (int i = 0; i < cpProductQuantums.size(); i++) {
                cpProductQuantums.get(i).setCompanyID(currentUserLoginAndOrg.get().getOrg());
            }
        }
        cPProductQuantumRepository.saveAll(cpProductQuantums);
    }

    /**
     * Get all the cPProductQuantums.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CPProductQuantum> findAll(Pageable pageable) {
        log.debug("Request to get all CPProductQuantums");
        return cPProductQuantumRepository.findAll(pageable);
    }


    /**
     * Get one cPProductQuantum by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CPProductQuantum> findOne(UUID id) {
        log.debug("Request to get CPProductQuantum : {}", id);
        return cPProductQuantumRepository.findById(id);
    }

    /**
     * Delete the cPProductQuantum by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete CPProductQuantum : {}", id);
        cPProductQuantumRepository.deleteById(id);
    }

    public Page<CPProductQuantumDTO> findAllActive(Pageable pageable) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<OrganizationUnit> organizationUnits = new ArrayList<>();
        List<UUID> uuidList = new ArrayList<>();
        UserDTO user = userService.getAccount();
        Boolean isDungChungVTHH = user.getSystemOption().stream().filter(a -> a.getCode().equals("TCKHAC_SDDMĐTTHCP") && a.getData() != null).findFirst().get().getData().equals("0");
        if (currentUserLoginAndOrg.isPresent()) {
            OrganizationUnit organizationUnit = organizationUnitRepository.findByID(currentUserLoginAndOrg.get().getOrg());
            if (Boolean.TRUE.equals(isDungChungVTHH)) {
                if (organizationUnit.getParentID() != null) {
                    OrganizationUnit organizationUnitParent = organizationUnitRepository.findByID(organizationUnit.getParentID());
                    organizationUnits.add(organizationUnitParent);
                    organizationUnitService.getListChildOrganizationUnitCustom(organizationUnits, organizationUnit.getParentID());
                } else {
                    organizationUnits.add(organizationUnit);
                    organizationUnitService.getListChildOrganizationUnitCustom(organizationUnits, organizationUnit.getId());
                }
                uuidList = organizationUnits.stream().map(OrganizationUnit::getId).collect(Collectors.toList());
            } else {
                uuidList.add(currentUserLoginAndOrg.get().getOrg());
            }
            return cPProductQuantumRepository.findAllByIsActive(pageable, uuidList);
        }
        throw new BadRequestAlertException("", "", "");
    }
}
