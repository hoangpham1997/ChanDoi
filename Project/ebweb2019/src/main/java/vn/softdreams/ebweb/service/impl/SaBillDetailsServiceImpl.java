package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.SaBill;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.repository.RefVoucherRepository;
import vn.softdreams.ebweb.repository.SaBillRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.SaBillDetailsService;
import vn.softdreams.ebweb.domain.SaBillDetails;
import vn.softdreams.ebweb.repository.SaBillDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.dto.UserSystemOption;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.SaBillDetailDTO;
import vn.softdreams.ebweb.web.rest.dto.SaBillSaveDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SaBillDetails.
 */
@Service
@Transactional
public class SaBillDetailsServiceImpl implements SaBillDetailsService {

    private final Logger log = LoggerFactory.getLogger(SaBillDetailsServiceImpl.class);

    private final SaBillDetailsRepository saBillDetailsRepository;
    private final SaBillRepository saBillRepository;
    private final RefVoucherRepository refVoucherRepository;
    private final UserService userService;
    private final OrganizationUnitRepository organizationUnitRepository;

    public SaBillDetailsServiceImpl(SaBillDetailsRepository saBillDetailsRepository,
                                    SaBillRepository saBillRepository,
                                    RefVoucherRepository refVoucherRepository,
                                    UserService userService, OrganizationUnitRepository organizationUnitRepository) {
        this.saBillDetailsRepository = saBillDetailsRepository;
        this.saBillRepository = saBillRepository;
        this.refVoucherRepository = refVoucherRepository;
        this.userService = userService;
        this.organizationUnitRepository = organizationUnitRepository;
    }

    /**
     * Save a saBillDetails.
     *
     * @param saBillDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public SaBillDetails save(SaBillDetails saBillDetails) {
        log.debug("Request to save SaBillDetails : {}", saBillDetails);
        return saBillDetailsRepository.save(saBillDetails);
    }

    /**
     * Get all the saBillDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SaBillDetails> findAll(Pageable pageable) {
        log.debug("Request to get all SaBillDetails");
        return saBillDetailsRepository.findAll(pageable);
    }


    /**
     * Get one saBillDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SaBillDetailDTO findAll(UUID id) {
        log.debug("Request to get SaBillDetails : {}", id);
        SaBillDetailDTO dto = new SaBillDetailDTO();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");
            List<RefVoucherDTO> dtos = refVoucherRepository.getRefViewVoucher(id, isNoMBook);
            dto.setRefVoucherDTOS(dtos);
        }
        dto.setSaBillDetails(saBillDetailsRepository.findBySaBillIdOrderByOrderPriority(id));
        return dto;
    }

    /**
     * Delete the saBillDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete SaBillDetails : {}", id);
        saBillDetailsRepository.deleteById(id);
        refVoucherRepository.deleteByRefID2(id);
    }
}
