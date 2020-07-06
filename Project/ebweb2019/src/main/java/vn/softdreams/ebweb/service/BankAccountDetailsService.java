package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.BankAccountDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.BankAccountDetailConvertDTO;
import vn.softdreams.ebweb.service.dto.BankAccountDetailDTO;
import vn.softdreams.ebweb.service.dto.ComboboxBankAccountDetailDTO;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.web.rest.dto.BankAccountDetailsSaveDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing BankAccountDetails.
 */
public interface BankAccountDetailsService {

    /**
     * Save a bankAccountDetails.
     *
     * @param bankAccountDetails the entity to save
     * @return the persisted entity
     */
    BankAccountDetailsSaveDTO save(BankAccountDetails bankAccountDetails);

    /**
     * Get all the bankAccountDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BankAccountDetails> findAll(Pageable pageable);
    Page<BankAccountDetails> pageableAllBankAccountDetails(Pageable pageable);

    /**
     * add by namnh
     *
     * @return
     */
    Page<BankAccountDetails> findAll();


    /**
     * Get the "id" bankAccountDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<BankAccountDetails> findOne(UUID id);
    Optional<BankAccountDetails> findID(UUID id);
    /**
     * Delete the "id" bankAccountDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<ComboboxBankAccountDetailDTO> findAllActive();

    List<ComboboxBankAccountDetailDTO> findAllForAccType();

    List<BankAccountDetails> findAllActiveCustom();
    List<BankAccountDetails> getAllBankAccountDetailsByOrgID(UUID orgID);
    List<BankAccountDetails> allBankAccountDetails();

    HandlingResultDTO deleteBank(List<UUID> uuids);

    List<BankAccountDetails> getAllBankAccountDetailsByCompanyID();

    List<BankAccountDetails> getAllBankAccountDetailsActiveCompanyIDNotCreditCard();
}
