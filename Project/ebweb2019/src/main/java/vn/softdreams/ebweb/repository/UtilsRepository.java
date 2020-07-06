package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.domain.Unit;
import vn.softdreams.ebweb.service.dto.VoucherRefCatalogDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UtilsRepository {
    Object findOneByRowNum(UUID id, Number typeID, Boolean isNext, SearchVoucher seachVoucher);

    List getIndexRow(UUID id, Number typeID, SearchVoucher searchVoucher);

    Page<Object> findAll(Pageable pageable, SearchVoucher searchVoucher, Number typeID);

    /**
     * @param noFBook
     * @param noMBook
     * @param typeGroupID
     * @param displayOnBook
     * @return
     * @Author Hautv
     */
    boolean updateGencode(String noFBook, String noMBook, int typeGroupID, int displayOnBook);

    /**
     * @param noFBook,      noMBook,  Số chứng từ
     * @param displayOnBook Sổ hoạt động 0 - sổ tài chính, 1 - sổ quản trị, 2 - cả 2 sổ
     * @param refID         ID chứng từ : NULL - thêm mới , có dữ liệu - Sửa
     * @return false : số chứng từ đã tồn tại
     * @Author Hautv
     */
    boolean checkDuplicateNoVoucher(String noFBook, String noMBook, int displayOnBook, UUID refID);

    byte[] exportPdf(SearchVoucher searchVoucher, Number typeID);

    byte[] exportExcel(SearchVoucher searchVoucher, Number typeID);

    Boolean checkCatalogInUsed(UUID companyID, Object uuid, String nameColumn);

    Boolean checkCostSetIDUsed(UUID companyID, List<UUID> uuids, String nameColumn);

    Boolean checkContraint(String nameTable, String nameColumn, UUID uuid);

    List<VoucherRefCatalogDTO> getVoucherRefCatalogDTOByID(UUID companyID, Integer currentBook, UUID uuid, String nameColumn);

    Page<VoucherRefCatalogDTO> getVoucherRefCatalogDTOByID(Pageable pageable, UUID companyID, Integer currentBook, UUID uuid, String nameColumn);

    List<VoucherRefCatalogDTO> getVoucherRefCatalogDTOByID(UUID companyID, Integer currentBook, UUID uuid, List<String> nameColumns);

    Page<VoucherRefCatalogDTO> getVoucherRefCatalogDTOByID(Pageable pageable, UUID companyID, Integer currentBook, UUID uuid, List<String> nameColumns);

    Long checkQuantityLimitedNoVoucher(UUID org);
}
