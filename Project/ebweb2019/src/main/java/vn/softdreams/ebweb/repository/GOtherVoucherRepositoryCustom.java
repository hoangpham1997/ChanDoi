package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.GOtherVoucher;
import vn.softdreams.ebweb.domain.GOtherVoucherDetails;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.dto.GOtherVoucherDetailKcDTO;
import vn.softdreams.ebweb.service.dto.GOtherVoucherKcDsDTO;
import vn.softdreams.ebweb.service.dto.GOtherVoucherKcDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.GOtherVoucherExportDTO;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherDetailDataKcDTO;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherViewDTO;

import java.util.List;
import java.util.UUID;

public interface GOtherVoucherRepositoryCustom {
    Page<GOtherVoucherViewDTO> findAll(Pageable sort, SearchVoucher searchVoucher, UUID org, Boolean isNoMBook);

    GOtherVoucher findByRowNum(SearchVoucher searchVoucher, Number rowNum, UUID org, Boolean isNoMBook);

    Page<GOtherVoucherExportDTO> getAllGOtherVouchers(Pageable pageable, SearchVoucher searchVoucher, UUID org, Boolean isNoMBook);

    List<Number> getIndexRow(UUID id, SearchVoucher searchVoucher, UUID companyID, Boolean isNoMBook);

    Page<GOtherVoucherKcDsDTO> searchGOtherVoucher(Pageable pageable, String fromDate, String toDate, Integer status, String keySearch, boolean isNoMBook, UUID companyID);

    List<GOtherVoucherDetailKcDTO> getGOtherVoucherDetailByGOtherVoucherId(UUID id);

    GOtherVoucherKcDTO getGOtherVoucherById(UUID id);

    GOtherVoucher findIdByRowNumKc(Pageable pageable, Integer status, String fromDate, String toDate, String searchValue, Integer rowNum, UUID org, boolean isNoMBook);

    Page<GOtherVoucherDTO> searchAllPB(Pageable pageable, String fromDate, String toDate, String textSearch, UUID org, Integer currentBook, boolean checkPDF);

    GOtherVoucher findOneByRowNumPB(String fromDate, String toDate, String textSearch, Integer rowNum, UUID org, Integer currentBook);

    List<GOtherVoucherDetailDataKcDTO> getDataKc(String postDate, UUID org, boolean isNoMBook);

    List<GOtherVoucherDetailDataKcDTO> getDataKcDiff(String postDate, UUID org, boolean isNoMBook);

    List<GOtherVoucherDetailDataKcDTO> getDataAccountSpecial(String postDate, UUID org, boolean isNoMBook);

    Integer findRowNumByID(String fromDate, String toDate, String textSearch, UUID id, UUID org, Integer currentBook);

    Long countAllByMonthAndYear(Integer month, Integer year, UUID org, String currentBook);

    List<UUID> getAllByGOtherVoucherID(UUID gOtherVoucherID, List<UUID> listID);

    void multiDeleteGOtherVoucher(UUID orgID, List<UUID> uuidList);

    void multiDeleteGOtherVoucherChild(String tableChildName, List<UUID> uuidList);

    void updateUnrecord(List<UUID> uuidList);

    void deleteByCPPeriodID(List<UUID> uuids);

}
