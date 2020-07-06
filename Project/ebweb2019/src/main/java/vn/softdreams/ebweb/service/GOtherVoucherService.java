package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.GOtherVoucher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.GOtherVoucherDetails;
import vn.softdreams.ebweb.domain.MBDeposit;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.dto.GOtherVoucherSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing GOtherVoucher.
 */
public interface GOtherVoucherService {

    /**
     * Save a gOtherVoucher.
     *
     * @param gOtherVoucher the entity to save
     * @return the persisted entity
     */
    GOtherVoucher save(GOtherVoucher gOtherVoucher);

    /**
     * Get all the gOtherVouchers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GOtherVoucher> findAll(Pageable pageable);


    /**
     * Get the "id" gOtherVoucher.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GOtherVoucher> findOne(UUID id);

    /**
     * Delete the "id" gOtherVoucher.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Page<GOtherVoucherViewDTO> findAll(Pageable pageable, SearchVoucher searchVoucher);

    GOtherVoucher findOneByRowNum(SearchVoucher searchVoucher, Number rowNum);

    GOtherVoucherSaveDTO saveDTO(GOtherVoucher gOtherVoucher);

    byte[] exportPDF(SearchVoucher searchVoucher);

    byte[] exportExcel(SearchVoucher searchVoucher);

    List<Number> getIndexRow(UUID id, SearchVoucher searchVoucher);

    Page<GOtherVoucherKcDsDTO> searchGOtherVoucher(Pageable pageable, String fromDate, String toDate, Integer status, String keySearch);

    List<GOtherVoucherDetailKcDTO> getGOtherVoucherDetailByGOtherVoucherId(UUID id);

    GOtherVoucherGeneralDTO saveGOtherVoucherGeneralDTO(GOtherVoucherGeneralDTO gOtherVoucherGeneralDTO);

    GOtherVoucherKcDTO getGOtherVoucherById(UUID id);

    UpdateDataDTO saveGOtherVoucherKc(GOtherVoucherKcDTO gOtherVoucherKc);

    ResultDTO deleteById(UUID id);

    GOtherVoucherKcDTO findIdByRowNumKc(Pageable pageable, Integer status, String fromDate, String toDate, String searchValue, Integer rowNum);

    Page<GOtherVoucherDTO> searchAllPB(Pageable pageable, String fromDate, String toDate, String textSearch);

    GOtherVoucherGeneralDoubleClickDTO findDetailPB(UUID id);

    GOtherVoucherGeneralViewDTO findDetailViewPB(UUID id);

    void deletePB(UUID id);

    GOtherVoucher findOneByRowNumPB(String fromDate, String toDate, String textSearch, Integer rowNum);

    byte[] exportExcelPB(Pageable pageable, String fromDate, String toDate, String textSearch);

    byte[] exportPDFPB(Pageable pageable, String fromDate, String toDate, String textSearch);

    Integer findRowNumByID(String fromDate, String toDate, String textSearch, UUID id);
    byte[] exportPdfKc(Integer status, String fromDate, String toDate, String searchValue);

    byte[] exportExcelKc(Integer status, String fromDate, String toDate, String searchValue);

    List<GOtherVoucherDetailDataKcDTO> getDataKc(String postDate);

    List<GOtherVoucherDetailDataKcDTO> getDataKcDiff(String postDate);

    List<GOtherVoucherDetailDataKcDTO> getDataAccountSpecial(String postDate);

    Long getPrepaidExpenseAllocationCount(Integer month, Integer year);

    LocalDate getMaxMonth(UUID id);

    HandlingResultDTO multiDelete(List<GOtherVoucher> gOtherVouchers);

    HandlingResultDTO multiUnRecord(List<GOtherVoucher> gOtherVouchers);

    HandlingResultDTO multiDeletePB(List<GOtherVoucher> gOtherVouchers);
}
