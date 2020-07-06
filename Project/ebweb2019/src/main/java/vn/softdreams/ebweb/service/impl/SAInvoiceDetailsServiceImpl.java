package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.SAInvoiceDetailsService;
import vn.softdreams.ebweb.domain.SAInvoiceDetails;
import vn.softdreams.ebweb.repository.SAInvoiceDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.dto.SAInvoiceDetailsOutWardDTO;
import vn.softdreams.ebweb.service.dto.UserSystemOption;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing SAInvoiceDetails.
 */
@Service
@Transactional
public class SAInvoiceDetailsServiceImpl implements SAInvoiceDetailsService {

    private final Logger log = LoggerFactory.getLogger(SAInvoiceDetailsServiceImpl.class);

    private final SAInvoiceDetailsRepository sAInvoiceDetailsRepository;

    private final OrganizationUnitRepository organizationUnitRepository;

    private final UserService userService;

    public SAInvoiceDetailsServiceImpl(SAInvoiceDetailsRepository sAInvoiceDetailsRepository, OrganizationUnitRepository organizationUnitRepository, UserService userService) {
        this.sAInvoiceDetailsRepository = sAInvoiceDetailsRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.userService = userService;
    }

    /**
     * Save a sAInvoiceDetails.
     *
     * @param sAInvoiceDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public SAInvoiceDetails save(SAInvoiceDetails sAInvoiceDetails) {
        log.debug("Request to save SAInvoiceDetails : {}", sAInvoiceDetails);        return sAInvoiceDetailsRepository.save(sAInvoiceDetails);
    }

    /**
     * Get all the sAInvoiceDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SAInvoiceDetails> findAll(Pageable pageable) {
        log.debug("Request to get all SAInvoiceDetails");
        return sAInvoiceDetailsRepository.findAll(pageable);
    }


    /**
     * Get one sAInvoiceDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SAInvoiceDetails> findOne(UUID id) {
        log.debug("Request to get SAInvoiceDetails : {}", id);
        return sAInvoiceDetailsRepository.findById(id);
    }

    /**
     * Delete the sAInvoiceDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete SAInvoiceDetails : {}", id);
        sAInvoiceDetailsRepository.deleteById(id);
    }

    @Override
    public List<SAInvoiceDetails> getSAInvoiceDetailsByID(UUID sAInvoiceID) {
        return sAInvoiceDetailsRepository.getSAInvoiceDetailsByID(sAInvoiceID);
    }

    @Override
    public List<SAInvoiceDetails> getSAInvoiceDetailsByMCReceiptID(UUID mCReceiptID) {
        return sAInvoiceDetailsRepository.getSAInvoiceDetailsByMCReceiptID(mCReceiptID);
    }

    @Override
    public List<SAInvoiceDetails> getSAInvoiceDetailsByMCDepositID(UUID mBDepositID) {
        return sAInvoiceDetailsRepository.getSAInvoiceDetailsByMBDepositID(mBDepositID);
    }

    @Override
    public List<SAInvoiceDetailsOutWardDTO> findAllDetailsById(List<UUID> id) {
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
        return sAInvoiceDetailsRepository.findBySaInvoiceIDOrderByOrderPriority(id, currentBook);
    }
}
