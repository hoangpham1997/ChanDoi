package vn.softdreams.ebweb.service.impl;

import org.springframework.data.domain.PageImpl;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.EMContractService;
import vn.softdreams.ebweb.domain.EMContract;
import vn.softdreams.ebweb.repository.EMContractRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.EMContractConvertDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.dto.UserSystemOption;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing EMContract.
 */
@Service
@Transactional
public class EMContractServiceImpl implements EMContractService {

    private final Logger log = LoggerFactory.getLogger(EMContractServiceImpl.class);

    private final EMContractRepository eMContractRepository;
    private final UserService userService;
    private final OrganizationUnitRepository organizationUnitRepository;


    public EMContractServiceImpl(EMContractRepository eMContractRepository, UserService userService, OrganizationUnitRepository organizationUnitRepository) {
        this.eMContractRepository = eMContractRepository;
        this.userService = userService;
        this.organizationUnitRepository = organizationUnitRepository;
    }

    /**
     * Save a eMContract.
     *
     * @param eMContract the entity to save
     * @return the persisted entity
     */
    @Override
    public EMContract save(EMContract eMContract) {
        log.debug("Request to save EMContract : {}", eMContract);
        return eMContractRepository.save(eMContract);
    }

    /**
     * Get all the eMContracts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EMContract> findAll(Pageable pageable) {
        log.debug("Request to get all EMContracts");
        return eMContractRepository.findAll(pageable);
    }

    @Override
    public Page<EMContract> findAll() {
        return new PageImpl<EMContract>(eMContractRepository.findAll());
    }


    /**
     * Get one eMContract by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EMContract> findOne(UUID id) {
        log.debug("Request to get EMContract : {}", id);
        return eMContractRepository.findById(id);
    }

    /**
     * Delete the eMContract by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete EMContract : {}", id);
        eMContractRepository.deleteById(id);
    }

    @Override
    public Page<EMContract> getAllEMContractsActive() {
        return new PageImpl<>(eMContractRepository.findAllByIsActiveTrue());
    }

    public List<EMContract> findAllActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return eMContractRepository.findAllByIsActive(currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    public List<EMContract> getAllForReport(Boolean isDependent, UUID orgID) {
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<UUID> listOrgID = new ArrayList<>();
        if (currentUserLoginAndOrg.isPresent()) {
            if (Boolean.TRUE.equals(isDependent)) {
                List<UUID> uuids = organizationUnitRepository.findAllOrgAccType0(orgID);
                listOrgID.addAll(uuids);
                return eMContractRepository.findAllForReport(listOrgID, currentBook);
            } else {
                listOrgID.add(orgID);
                return eMContractRepository.findAllForReport(listOrgID, currentBook);
            }
        }
        throw new BadRequestAlertException("", "", "");
    }
}
