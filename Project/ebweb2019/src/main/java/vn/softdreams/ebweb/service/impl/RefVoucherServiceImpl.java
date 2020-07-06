package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.RefVoucher;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.RefVoucherService;
import vn.softdreams.ebweb.repository.RefVoucherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.dto.UserSystemOption;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherSecondDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing ViewVoucher.
 */
@Service
@Transactional
public class RefVoucherServiceImpl implements RefVoucherService {

    private final Logger log = LoggerFactory.getLogger(RefVoucherServiceImpl.class);

    private final RefVoucherRepository refVoucherRepository;

    private final UserService userService;

    private final OrganizationUnitRepository organizationUnitRepository;

    public RefVoucherServiceImpl(RefVoucherRepository refVoucherRepository,
                                 UserService userService, OrganizationUnitRepository organizationUnitRepository) {
        this.refVoucherRepository = refVoucherRepository;
        this.userService = userService;
        this.organizationUnitRepository = organizationUnitRepository;
    }

    /**
     * Save a viewVoucher.
     *
     * @param viewVoucher the entity to save
     * @return the persisted entity
     */
    @Override
    public RefVoucher save(RefVoucher viewVoucher) {
        log.debug("Request to save ViewVoucher : {}", viewVoucher);
        return refVoucherRepository.save(viewVoucher);
    }

    /**
     * Get all the viewVouchers.
     *
     * @param pageable the pagination information
     * @param typeSearch
     * @param status
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RefVoucherDTO> findAll(Pageable pageable, Integer typeGroup, String no, String invoiceNo, Boolean recorded,
                                       String fromDate, String toDate, Integer typeSearch, Integer status) {
        log.debug("Request to get all ViewVouchers");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (currentUserLoginAndOrg.isPresent() && userWithAuthoritiesAndSystemOption.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");
            return refVoucherRepository.getAllRefViewVoucher(pageable, typeGroup, no, invoiceNo, recorded, fromDate, toDate,
                isNoMBook, currentUserLoginAndOrg.get().getOrg(), typeSearch, status);
        }
        return null;
    }


    /**
     * Get one viewVoucher by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RefVoucher> findOne(Long id) {
        log.debug("Request to get ViewVoucher : {}", id);
        return refVoucherRepository.findById(id);
    }

    /**
     * Delete the viewVoucher by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ViewVoucher : {}", id);
        refVoucherRepository.deleteById(id);
    }

    @Override
    public List<RefVoucherDTO> getRefViewVoucher(UUID id, int currentBook) {
        return refVoucherRepository.getRefViewVoucher(id, currentBook == Constants.TypeLedger.FINANCIAL_BOOK );
    }

    @Override
    public List<RefVoucherDTO> getRefViewVoucherByPaymentVoucherID(Integer typeID, UUID id, int currentBook) {
        return refVoucherRepository.getRefViewVoucherByPaymentVoucherID(typeID ,id, currentBook == Constants.TypeLedger.FINANCIAL_BOOK );
    }

    @Override
    public List<RefVoucherDTO> getRefViewVoucherPPinvoice(UUID refId) {
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");
            return refVoucherRepository.getRefViewVoucher(refId, isNoMBook);
        }
        return new ArrayList<>();
    }

    @Override
    public Page<RefVoucherSecondDTO> getViewVoucherToModal(Pageable pageable, Integer typeGroup, String fromDate, String toDate) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (currentUserLoginAndOrg.isPresent() && userWithAuthoritiesAndSystemOption.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");
            return refVoucherRepository.getViewVoucherToModal(pageable, typeGroup, fromDate, toDate,
                isNoMBook, currentUserLoginAndOrg.get().getOrg());
        }
        return null;
    }
}
