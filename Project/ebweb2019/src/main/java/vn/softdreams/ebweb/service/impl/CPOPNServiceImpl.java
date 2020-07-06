package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.repository.SystemOptionRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.CPOPNService;
import vn.softdreams.ebweb.domain.CPOPN;
import vn.softdreams.ebweb.repository.CPOPNRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.OrganizationUnitService;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.CPOPNSDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.Constants.SystemOption.TCKHAC_SDDMDTTHCP;

/**
 * Service Implementation for managing CPOPN.
 */
@Service
@Transactional
public class CPOPNServiceImpl implements CPOPNService {

    private final Logger log = LoggerFactory.getLogger(CPOPNServiceImpl.class);

    private final CPOPNRepository cPOPNRepository;
    private final UserService userService;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final OrganizationUnitService organizationUnitService;
    private final SystemOptionRepository systemOptionRepository;

    public CPOPNServiceImpl(CPOPNRepository cPOPNRepository, UserService userService, OrganizationUnitRepository organizationUnitRepository, OrganizationUnitService organizationUnitService, SystemOptionRepository systemOptionRepository) {
        this.cPOPNRepository = cPOPNRepository;
        this.userService = userService;
        this.organizationUnitRepository = organizationUnitRepository;
        this.organizationUnitService = organizationUnitService;
        this.systemOptionRepository = systemOptionRepository;
    }

    /**
     * Save a cPOPN.
     *
     * @param cPOPN the entity to save
     * @return the persisted entity
     */
    @Override
    public CPOPN save(CPOPN cPOPN) {
        log.debug("Request to save CPOPN : {}", cPOPN);        return cPOPNRepository.save(cPOPN);
    }

    /**
     * Get all the cPOPNS.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CPOPN> findAll(Pageable pageable) {
        log.debug("Request to get all CPOPNS");
        return cPOPNRepository.findAll(pageable);
    }


    /**
     * Get one cPOPN by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CPOPN> findOne(UUID id) {
        log.debug("Request to get CPOPN : {}", id);
        return cPOPNRepository.findById(id);
    }

    /**
     * Delete the cPOPN by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete CPOPN : {}", id);
        cPOPNRepository.deleteById(id);
    }

    public Page<CPOPNSDTO> findAllActive(Pageable pageable) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        if (currentUserLoginAndOrg.isPresent()) {
            return cPOPNRepository.findAllByIsActive(pageable,  systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMDTTHCP), currentUserLoginAndOrg.get().getOrg(), currentBook);
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public void saveAll(List<CPOPN> cpopns) {
        log.debug("Request to save CPProductQuantum : {}", cpopns);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            for (int i = 0; i < cpopns.size(); i++) {
                if (cpopns.get(i).getCompanyID() == null) {
                    cpopns.get(i).setCompanyID(currentUserLoginAndOrg.get().getOrg());
                }
            }
        }
        cPOPNRepository.saveAll(cpopns);
    }
}
