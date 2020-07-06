package vn.softdreams.ebweb.service.impl;

import org.springframework.data.domain.PageImpl;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.PPDiscountReturnDetailsService;
import vn.softdreams.ebweb.domain.PPDiscountReturnDetails;
import vn.softdreams.ebweb.repository.PPDiscountReturnDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.dto.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing PPDiscountReturnDetails.
 */
@Service
@Transactional
public class PPDiscountReturnDetailsServiceImpl implements PPDiscountReturnDetailsService {

    private final Logger log = LoggerFactory.getLogger(PPDiscountReturnDetailsServiceImpl.class);

    private final PPDiscountReturnDetailsRepository pPDiscountReturnDetailsRepository;

    private final OrganizationUnitRepository organizationUnitRepository;

    private final UserService userService;

    private final String PHIEN_SoLamViec = "PHIEN_SoLamViec";

    public PPDiscountReturnDetailsServiceImpl(PPDiscountReturnDetailsRepository pPDiscountReturnDetailsRepository, OrganizationUnitRepository organizationUnitRepository, UserService userService) {
        this.pPDiscountReturnDetailsRepository = pPDiscountReturnDetailsRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.userService = userService;
    }

    /**
     * Save a pPDiscountReturnDetails.
     *
     * @param pPDiscountReturnDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public PPDiscountReturnDetails save(PPDiscountReturnDetails pPDiscountReturnDetails) {
        log.debug("Request to save PPDiscountReturnDetails : {}", pPDiscountReturnDetails);        return pPDiscountReturnDetailsRepository.save(pPDiscountReturnDetails);
    }

    /**
     * Get all the pPDiscountReturnDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PPDiscountReturnDetails> findAll(Pageable pageable) {
        log.debug("Request to get all PPDiscountReturnDetails");
        return pPDiscountReturnDetailsRepository.findAll(pageable);
    }


    /**
     * Get one pPDiscountReturnDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PPDiscountReturnDetails> findOne(Long id) {
        log.debug("Request to get PPDiscountReturnDetails : {}", id);
        return pPDiscountReturnDetailsRepository.findById(id);
    }

    /**
     * Delete the pPDiscountReturnDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PPDiscountReturnDetails : {}", id);
        pPDiscountReturnDetailsRepository.deleteById(id);
    }

    @Override
    public Page<PPDiscountReturnDetailConvertDTO> getAllPPDiscountReturnDetailsByID(UUID ppDiscountReturnId) {
        UserDTO userDTO = userService.getAccount();
        return new PageImpl<>(pPDiscountReturnDetailsRepository.getAllPPDiscountReturnDetailsByID(ppDiscountReturnId, userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(PHIEN_SoLamViec)).findAny().get().getData()));
    }
    @Override
    public List<PPDiscountReturnDetailDTO> getPPDiscountReturnDetailsByID(UUID ppDiscountReturnId) {
        UserDTO userDTO = userService.getAccount();
        return pPDiscountReturnDetailsRepository.getPPDiscountReturnDetailsByID(ppDiscountReturnId, userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(PHIEN_SoLamViec)).findAny().get().getData());
    }

    @Override
    public List<PPDiscountReturnDetailOutWardDTO> findAllDetailsById(List<UUID> id) {
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
        return pPDiscountReturnDetailsRepository.findByPpDiscountReturnIDOrderByOrderPriority(id, currentBook);
    }
}
