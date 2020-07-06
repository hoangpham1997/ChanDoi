package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.MBDepositDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MBDepositSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.MBDepositViewDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MBDeposit.
 */
public interface MBDepositService {

    /**
     * Save a mBDeposit.
     *
     * @param mBDeposit the entity to save
     * @return the persisted entity
     */
    MBDeposit save(MBDeposit mBDeposit);

    /**
     * Get all the mBDeposits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MBDeposit> findAll(Pageable pageable);


    /**
     * Get the "id" mBDeposit.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MBDeposit> findOne(UUID id);

    /**
     * Delete the "id" mBDeposit.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Page<MBDepositViewDTO> findAll(Pageable pageable, SearchVoucher searchVoucher);

    /**
     * @param mbDepositId
     * @return
     */
    MBDepositDTO getAllData(UUID mbDepositId);

    /**
     * @param searchVoucher
     * @param rowNum
     * @return
     */
    MBDeposit findOneByRowNum(SearchVoucher searchVoucher, Number rowNum);

    /**
     * @param mutipleRecord
     */
    void mutipleRecord(MutipleRecord mutipleRecord);

    MBDepositSaveDTO saveDTO(MBDeposit mBDeposit);

    byte[] exportPDF(SearchVoucher searchVoucher);

    byte[] exportExcel(SearchVoucher searchVoucher);

    List<Number> getIndexRow(UUID id, SearchVoucher searchVoucher);

    HandlingResultDTO multiDelete(List<MBDeposit> mbDeposits);

    HandlingResultDTO multiUnrecord(List<MBDeposit> mbDeposits);
}
