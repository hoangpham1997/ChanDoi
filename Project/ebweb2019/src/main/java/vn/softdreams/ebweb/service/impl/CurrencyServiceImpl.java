package vn.softdreams.ebweb.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.Currency;
import vn.softdreams.ebweb.domain.SearchVoucherCurrency;
import vn.softdreams.ebweb.repository.CurrencyRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.CurrencyService;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.UtilsService;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.web.rest.dto.CurrencySaveDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing Currency.
 */
@Service
@Transactional
public class CurrencyServiceImpl implements CurrencyService {

    private final Logger log = LoggerFactory.getLogger(CurrencyServiceImpl.class);
    private final UtilsService utilsService;
    private final UserService userService;

    private final CurrencyRepository currencyRepository;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository, UtilsService utilsService, UserService userService) {
        this.currencyRepository = currencyRepository;
        this.utilsService = utilsService;
        this.userService = userService;
    }

    /**
     * Save a currency.
     *
     * @param currency the entity to save
     * @return the persisted entity
     */
    @Override
    public CurrencySaveDTO save(Currency currency) {
        log.debug("Request to save Currency : {}", currency);
        Currency curr = new Currency();
        CurrencySaveDTO currencySaveDTO = new CurrencySaveDTO();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();

        if (currentUserLoginAndOrg.isPresent()) {
            currency.setCompanyID(currentUserLoginAndOrg.get().getOrgGetData());
        }
        if (currency.getId() == null) {
            int count = currencyRepository.countByCurrencyCodeIgnoreCaseAndIsActiveTrue(currency.getCurrencyCode(),currentUserLoginAndOrg.get().getOrgGetData());
            if (count > 0) {
                currencySaveDTO.setCurrency(currency);
                currencySaveDTO.setStatus(count);
                return currencySaveDTO;
            } else {
                curr = currencyRepository.save(currency);
                currencySaveDTO.setCurrency(curr);
                currencySaveDTO.setStatus(count);
                return currencySaveDTO;
            }
        } else {
            curr = currencyRepository.save(currency);
            currencySaveDTO.setCurrency(curr);
            currencySaveDTO.setStatus(0);
            return currencySaveDTO;
        }
    }

    /**
     * Get all the currencies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Currency> findAll(Pageable pageable) {
        log.debug("Request to get all Currencies");
        return currencyRepository.findAll(pageable);
    }

    /**
     * add by namnh
     *
     * @return
     */
    @Override
    public Page<Currency> findAll() {
        return new PageImpl<Currency>(currencyRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Currency> pageableAllCurrency(Pageable pageable) {
        log.debug("Request to get all currencies");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return currencyRepository.pageableAllCurrency(pageable, currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }


    /**
     * Get one currency by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Currency> findOne(UUID id) {
        log.debug("Request to get Currency : {}", id);
        return currencyRepository.findById(id);
    }

    @Override
    public Page<Currency> findAll1(Pageable pageable, SearchVoucherCurrency searchVoucherCurrency) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        if (currentUserLoginAndOrg.isPresent()) {
            return currencyRepository.findAll1
                (pageable, searchVoucherCurrency, currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    /**
     * Delete the currency by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Currency : {}", id);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<Currency> currency = currencyRepository.findById(id);
        if (currency.isPresent()) {
            Boolean checkUsedAcc = utilsService.checkCatalogInUsed(currentUserLoginAndOrg.get().getOrgGetData(), currency.get().getCurrencyCode(), "CurrencyID");
            if (checkUsedAcc) {
                throw new BadRequestAlertException("Không thể xóa dữ liệu vì đã phát sinh chứng từ liên quan ", "", "");
            } else {
                currencyRepository.deleteById(id);
            }
        }
    }

    /**
     * Get all the currencies isActive.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Currency> findAllByIsActiveIsTrue(Pageable pageable) {
        log.debug("Request to get all Currencies");
        return currencyRepository.findAllByIsActiveIsTrue(pageable);
    }

    @Override
    public Page<Currency> findCurrencies() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return new PageImpl<Currency>(currencyRepository.findByIsActiveTrue(currentUserLoginAndOrg.get().getOrgGetData()));
    }

    public List<Currency> findAllActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return currencyRepository.findByIsActiveTrue(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<Currency> findAllByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return currencyRepository.findAllByCompanyID(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<Currency> findAllByCompanyIDNull() {
        return currencyRepository.findAllByCompanyIDNull();
    }

    @Override
    public Optional<Currency> findActiveDefault(UUID companyID) {
        return currencyRepository.findActiveDefault(companyID.toString());
    }

    @Override
    public List<Currency> findCurrencyByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return currencyRepository.findCurrencyByCompanyID(currentUserLoginAndOrg.get().getOrgGetData());
        }
        throw new BadRequestAlertException("", "", "");
    }
}
