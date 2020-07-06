package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.Bank;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.SearchVoucherBank;
import vn.softdreams.ebweb.domain.Unit;
import vn.softdreams.ebweb.service.dto.BankConvertDTO;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.web.rest.dto.BankSaveDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing Bank.
 */
public interface BankService {

    /**
     * Save a bank.
     *
     * @param bank the entity to save
     * @return the persisted entity
     */
    BankSaveDTO save(Bank bank);

    /**
     * Get all the banks.
     * add by namnh
     *
     * @return the list of entities
     */
    Page<Bank> findAll();

    /**
     * Get all the banks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Bank> findAll(Pageable pageable);
    Page<Bank> pageableAllBank(Pageable pageable);

    /**
     * Get the "id" bank.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Bank> findOne(UUID id);

    /**
     * Delete the "id" bank.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    HandlingResultDTO delete(List<UUID> uuids);

    Page<Bank> findAll(Pageable pageable, String bankCode, String bankName, String bankNameRepresent, String address, String description, Boolean isActive);

    Page<Bank> findAllBank(Pageable pageable, SearchVoucherBank searchVoucherBank);

    List<Bank> findAllActive();
    List<Bank> findAllByCompanyID();

    List<Bank> findAllBankByCompanyID();



}
