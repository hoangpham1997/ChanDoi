package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.*;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the OrganizationUnit entity.
 */
@SuppressWarnings("unused")
public interface PPDiscountReturnDetailsRepositoryCustom {
    List<PPDiscountReturnDetailDTO> getPPDiscountReturnDetailsByID(UUID ppDiscountReturnId, String data);

    Page<PPDiscountReturnOutWardDTO> findAllPPDisCountReturnDTO(Pageable pageable, UUID accountingObject, String fromDate, String toDate, UUID org, String currentBook);

    List<PPDiscountReturnDetailOutWardDTO> findByPpDiscountReturnIDOrderByOrderPriority(List<UUID> id, String currentBook);

    List<PPDiscountReturnDetailsReportConvertDTO> getAllPPDiscountReturnDetailsReportByID(UUID id, String currentBook);

    List<PPDiscountReturnDetailsReportConvertKTDTO> getAllPPDiscountReturnDetailsReportKTByID(UUID id);

    List<PPDiscountReturnDetailConvertDTO> getAllPPDiscountReturnDetailsByID(UUID ppDiscountReturnId, String data);
}
