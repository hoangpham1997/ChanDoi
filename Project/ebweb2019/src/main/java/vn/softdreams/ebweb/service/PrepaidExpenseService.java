package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherSecondDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing PrepaidExpense.
 */
public interface PrepaidExpenseService {

    /**
     * Save a prepaidExpense.
     *
     * @return the persisted entity
     */
    PrepaidExpense save(PrepaidExpense prepaidExpenseDTO);

    /**
     * Get all the prepaidExpenses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PrepaidExpense> findAll(Pageable pageable);


    /**
     * Get the "id" prepaidExpense.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PrepaidExpense> findOne(UUID id);

    /**
     * Delete the "id" prepaidExpense.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<PrepaidExpenseCodeDTO> getPrepaidExpenseCode();

    List<AccountList> getCostAccounts();

    Page<PrepaidExpenseAllDTO> getAll(Pageable pageable, Integer typeExpense, String fromDate, String toDate, String textSearch);

    List<RefVoucherSecondDTO> findPrepaidExpenseByID(UUID id);

    PrepaidExpenseObjectConvertDTO getPrepaidExpenseAllocation(Integer month, Integer year);

    Long getPrepaidExpenseAllocationCount(Integer month, Integer year);

    GOtherVoucher getPrepaidExpenseAllocationDuplicate(Integer month, Integer year);

    Long countByPrepaidExpenseID(UUID id);

    List<PrepaidExpenseAllocation> getPrepaidExpenseAllocationByID(UUID id);

    Page<PrepaidExpense> getPrepaidExpenseByCurrentBookToModal(Pageable pageable, String fromDate, String toDate, Integer typeExpense);

    List<PrepaidExpense> getPrepaidExpensesByCompanyID();

    HandlingResultDTO multiDelete(List<PrepaidExpense> prepaidExpense);

    void updateIsActive(UUID id);

    List<PrepaidExpenseCodeDTO> getPrepaidExpenseCodeCanActive();
}
