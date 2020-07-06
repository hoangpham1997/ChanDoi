package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.domain.SystemOption;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.repository.SystemOptionRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.CPAllocationQuantumService;
import vn.softdreams.ebweb.domain.CPAllocationQuantum;
import vn.softdreams.ebweb.repository.CPAllocationQuantumRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.OrganizationUnitService;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.CPAllocationQuantumDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.dto.UserSystemOption;
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
 * Service Implementation for managing CPAllocationQuantum.
 */
@Service
@Transactional
public class CPAllocationQuantumServiceImpl implements CPAllocationQuantumService {

    private final Logger log = LoggerFactory.getLogger(CPAllocationQuantumServiceImpl.class);

    private final CPAllocationQuantumRepository cPAllocationQuantumRepository;
    private final UserService userService;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final OrganizationUnitService organizationUnitService;
    private final SystemOptionRepository systemOptionRepository;

    public CPAllocationQuantumServiceImpl(CPAllocationQuantumRepository cPAllocationQuantumRepository,
                                          UserService userService,
                                          OrganizationUnitRepository organizationUnitRepository,
                                          OrganizationUnitService organizationUnitService,
                                          SystemOptionRepository systemOptionRepository) {
        this.cPAllocationQuantumRepository = cPAllocationQuantumRepository;
        this.userService = userService;
        this.organizationUnitRepository = organizationUnitRepository;
        this.organizationUnitService = organizationUnitService;
        this.systemOptionRepository = systemOptionRepository;
    }

    /**
     * Save a cPAllocationQuantum.
     *
     * @param cPAllocationQuantum the entity to save
     * @return the persisted entity
     */
    @Override
    public CPAllocationQuantum save(CPAllocationQuantum cPAllocationQuantum) {
        log.debug("Request to save CPAllocationQuantum : {}", cPAllocationQuantum);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            if (cPAllocationQuantum.getCompanyID() == null) {
                cPAllocationQuantum.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            }
        }
        return cPAllocationQuantumRepository.save(cPAllocationQuantum);
    }

    /**
     * Get all the cPAllocationQuantums.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CPAllocationQuantum> findAll(Pageable pageable) {
        log.debug("Request to get all CPAllocationQuantums");
        return cPAllocationQuantumRepository.findAll(pageable);
    }


    @Override
    public void saveAll(List<CPAllocationQuantum> cpAllocationQuantums) {
        log.debug("Request to save CPProductQuantum : {}", cpAllocationQuantums);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            for (int i = 0; i < cpAllocationQuantums.size(); i++) {
                cpAllocationQuantums.get(i).setCompanyID(currentUserLoginAndOrg.get().getOrg());
            }
        }
        cPAllocationQuantumRepository.saveAll(cpAllocationQuantums);
    }

    /**
     * Get one cPAllocationQuantum by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CPAllocationQuantum> findOne(UUID id) {
        log.debug("Request to get CPAllocationQuantum : {}", id);
        return cPAllocationQuantumRepository.findById(id);
    }

    /**
     * Delete the cPAllocationQuantum by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete CPAllocationQuantum : {}", id);
        cPAllocationQuantumRepository.deleteById(id);
    }

    public Page<CPAllocationQuantumDTO> findAllActive(Pageable pageable) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        List<UUID> uuidList = new ArrayList<>();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        if (currentUserLoginAndOrg.isPresent()) {

            return cPAllocationQuantumRepository.findAllByIsActive(pageable, systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMDTTHCP), currentUserLoginAndOrg.get().getOrg(), currentBook);
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<CPAllocationQuantum> findByCostSetID(List<UUID> ids) {
        return cPAllocationQuantumRepository.findByCostSetID(ids);
    }
}
