package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.SystemOption;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.repository.RefVoucherRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.SaReturnDetailsService;
import vn.softdreams.ebweb.domain.SaReturnDetails;
import vn.softdreams.ebweb.repository.SaReturnDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.dto.SaReturnDetailsRSInwardDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.dto.UserSystemOption;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.SaBillDetailDTO;
import vn.softdreams.ebweb.web.rest.dto.SaReturnDetailsDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SaReturnDetails.
 */
@Service
@Transactional
public class SaReturnDetailsServiceImpl implements SaReturnDetailsService {

    private final Logger log = LoggerFactory.getLogger(SaReturnDetailsServiceImpl.class);

    private final SaReturnDetailsRepository saReturnDetailsRepository;
    private final RefVoucherRepository refVoucherRepository;
    private final UserService userService;
    private final OrganizationUnitRepository organizationUnitRepository;

    public SaReturnDetailsServiceImpl(SaReturnDetailsRepository saReturnDetailsRepository,
                                      RefVoucherRepository refVoucherRepository, UserService userService,
                                      OrganizationUnitRepository organizationUnitRepository) {
        this.saReturnDetailsRepository = saReturnDetailsRepository;
        this.refVoucherRepository = refVoucherRepository;
        this.userService = userService;
        this.organizationUnitRepository = organizationUnitRepository;
    }

    /**
     * Get one saReturnDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SaReturnDetailsDTO findOne(UUID id) {
        log.debug("Request to get SaReturnDetails : {}", id);
        SaReturnDetailsDTO dto = new SaReturnDetailsDTO();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());

            boolean isNoMBook = currentBook.equalsIgnoreCase("1");
            List<RefVoucherDTO> dtos = refVoucherRepository.getRefViewVoucher(id, isNoMBook);
            dto.setRefVoucherDTOS(dtos);
            dto.setSaReturnDetailsViewDTOs(saReturnDetailsRepository.findViewFullByID(id, currentBook));
            return dto;
        }
        return null;
    }

    @Override
    public List<SaReturnDetailsRSInwardDTO> findAllDetailsById(List<UUID> id) {
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
        return saReturnDetailsRepository.findBySaReturnOrderLstIDByOrderPriority(id, currentBook);
    }
}
