package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MBDeposit;
import vn.softdreams.ebweb.domain.MBTellerPaper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MBTellerPaperDTO;
import vn.softdreams.ebweb.web.rest.dto.MBTellerPaperSaveDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MBTellerPaper.
 */
public interface MBTellerPaperService {

    /**
     * Save a mBTellerPaper.
     *
     * @param mBTellerPaper the entity to save
     * @return the persisted entity
     */
    MBTellerPaper save(MBTellerPaper mBTellerPaper);

    /**
     * Get all the mBTellerPapers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MBTellerPaper> findAll(Pageable pageable);


    /**
     * Get the "id" mBTellerPaper.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MBTellerPaper> findOne(UUID id);

    /**
     * Delete the "id" mBTellerPaper.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    /**
     * @author mran
     */
    Page<MBTellerPaper> findAll(Pageable pageable, SearchVoucher seachVoucher);

    /**
     * @return
     * @author mran
     */
    MBTellerPaper findOneByRowNum(SearchVoucher seachVoucher, Number rowNum);

    List<Number> getIndexRow(UUID id, SearchVoucher searchVoucher);

    /**
     * @return
     * @author mran
     */
    Page<MBTellerPaperDTO> findAllDTOByCompanyID(Pageable pageable);

    MBTellerPaperSaveDTO saveDTO(MBTellerPaper mBTellerPaper, Boolean isEdit);

    byte[] exportPdf(SearchVoucher searchVoucher1);

    byte[] exportExcel(SearchVoucher searchVoucher1);

    HandlingResultDTO multiDelete(List<MBTellerPaper> mbTellerPapers);

    HandlingResultDTO multiUnRecord(List<MBTellerPaper> mbTellerPapers);

    boolean unrecord(List<UUID> refID, List<UUID> repositoryLedgerID);
}
