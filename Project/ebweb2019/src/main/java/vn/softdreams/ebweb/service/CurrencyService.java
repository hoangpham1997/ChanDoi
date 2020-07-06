package vn.softdreams.ebweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.Currency;
import vn.softdreams.ebweb.domain.SearchVoucherCurrency;
import vn.softdreams.ebweb.web.rest.dto.CurrencySaveDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing Currency.
 */
public interface CurrencyService {

    /**
     * Save a currency.
     *
     * @param currency the entity to save
     * @return the persisted entity
     */
    CurrencySaveDTO save(Currency currency);

    /**
     * Get all the currencies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Currency> findAll(Pageable pageable);

    /**
     * add by namnh
     * @return
     */
    Page<Currency> findAll();

    /**
     * Get the "id" currency.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Currency> findOne(UUID id);

    /**
     * Delete the "id" currency.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Page<Currency> findCurrencies();
    /**
     * Get all the currencies isActive.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Currency> findAllByIsActiveIsTrue(Pageable pageable);
    Page<Currency> pageableAllCurrency(Pageable pageable);

    Page<Currency> findAll1(Pageable pageable, SearchVoucherCurrency searchVoucherCurrency);

    /**
     * @Author hieugie
     *
     * Lấy ra tất cả ngoại tệ đang active
     *
     * @return
     */
    List<Currency> findAllActive();
    List<Currency> findAllByCompanyID();
    List<Currency> findAllByCompanyIDNull();

    Optional<Currency> findActiveDefault(UUID companyID);

    List<Currency> findCurrencyByCompanyID();
}
