package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.ViewVoucherNo;
import vn.softdreams.ebweb.service.dto.ViewVoucherNoDetailDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDateClosedBook;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ViewVoucherNoRespository extends JpaRepository<ViewVoucherNo, UUID>, ViewVoucherNoRespositoryCustom {
    @Query(value = "select * from ViewVoucherNoForCloseBook where " +
        "companyID = ?1 and postedDate < = ?3 and ( TypeLedger = 2 or TypeLedger = ?2 ) " +
        "and Recorded = 0", nativeQuery = true)
    Page<ViewVoucherNo> getAllVoucherNotRecorded(Pageable pageable, UUID companyID, Integer typeLedger, LocalDate postedDate);

    @Query(value = "select * from ViewVoucherNoForCloseBook where " +
        "companyID = ?1 and postedDate < = ?3 and ( TypeLedger = 2 or TypeLedger = ?2 ) " +
        "and Recorded = 0", nativeQuery = true)
    List<ViewVoucherNo> getAllVoucherNotRecorded(UUID companyID, Integer typeLedger, LocalDate postedDate);

    @Query(value = "select * from ViewVoucherNoForCloseBook where " +
        "companyID = ?1 and RefID in ?2", nativeQuery = true)
    List<ViewVoucherNo> getAllByListID(UUID companyID, List<UUID> uuids);

    @Query(value = "SELECT count(DISTINCT RefID) from  ViewVoucherNo where CompanyID = ?1 and NoFBook in ?2 and RefID not in ?3 ", nativeQuery = true)
    Number checkExistNoForResetFBook(UUID companyID, List<String> noFBooks, List<UUID> lstID);

    @Query(value = "SELECT count(DISTINCT RefID) from  ViewVoucherNo where CompanyID = ?1 and NoMBook in ?2 and RefID not in ?3 ", nativeQuery = true)
    Number checkExistNoForResetMBook(UUID companyID, List<String> noMBooks, List<UUID> lstID);

}
