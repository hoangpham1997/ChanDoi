package vn.softdreams.ebweb.service;

import org.springframework.http.ResponseEntity;
import vn.softdreams.ebweb.domain.CPExpenseTranfer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.dto.CPExpenseTranferSaveDTO;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.KetChuyenChiPhiDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing CPExpenseTranfer.
 */
public interface CPExpenseTranferService {

    /**
     * Save a cPExpenseTranfer.
     *
     * @param cPExpenseTranfer the entity to save
     * @return the persisted entity
     */
    CPExpenseTranfer save(CPExpenseTranfer cPExpenseTranfer);

    /**
     * Get all the cPExpenseTranfers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CPExpenseTranfer> findAll(Pageable pageable);


    Page<CPExpenseTranfer> findAll(Pageable pageable, SearchVoucher searchVoucher);


    /**
     * Get the "id" cPExpenseTranfer.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CPExpenseTranfer> findOne(UUID id);

    /**
     * Delete the "id" cPExpenseTranfer.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<KetChuyenChiPhiDTO> getCPExpenseTransferByCPPeriodID(UUID cPPeriodID);

    CPExpenseTranferSaveDTO saveDTO(CPExpenseTranfer cPExpenseTranfer);

    void checkExistCPPeriodID(UUID cPPeriodID);

    byte[] exportPDF(SearchVoucher searchVoucher);

    byte[] exportExcel(SearchVoucher searchVoucher);

    HandlingResultDTO multiUnRecord(List<CPExpenseTranfer> cpExpenseTranfers);

    HandlingResultDTO multiDelete(List<CPExpenseTranfer> cpExpenseTranfers);
}
