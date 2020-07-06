package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MBCreditCard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.MBDeposit;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MBCreditCardSaveDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MBDepositSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.MBCreditCardViewDTO;

//import vn.softdreams.ebweb.service.dto.MBCreditCardDTO;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MBCreditCard.
 */
public interface MBCreditCardService {

    /**
     * Save a mBCreditCard.
     *
     * @param mBCreditCard the entity to save
     * @return the persisted entity
     */
    MBCreditCard save(MBCreditCard mBCreditCard);

    /**
     * Get all the mBCreditCards.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MBCreditCard> findAll(Pageable pageable);


    /**
     * Get the "id" mBCreditCard.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MBCreditCard> findOne(UUID id);

    /**
     * Delete the "id" mBCreditCard.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);


    Page<MBCreditCardViewDTO> findAll(Pageable pageable, SearchVoucher searchVoucher);

    MBCreditCard findOneByRowNum(SearchVoucher searchVoucher, Number rowNum);

    MBCreditCardSaveDTO saveDTO(MBCreditCard mbCreditCard);

    byte[] exportPDF(SearchVoucher searchVoucher);

    byte[] exportExcel(SearchVoucher searchVoucher);

    List<Number> getIndexRow(UUID id, SearchVoucher searchVoucher);

    HandlingResultDTO multiDelete(List<MBCreditCard> mbCreditCards);

    HandlingResultDTO multiUnRecord(List<MBCreditCard> mbCreditCards);

    boolean unrecord(List<UUID> refID, List<UUID> repositoryLedgerID);

}
