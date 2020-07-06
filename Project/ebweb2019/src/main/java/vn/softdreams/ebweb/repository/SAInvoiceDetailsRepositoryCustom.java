package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.SAInvoiceDTO;
import vn.softdreams.ebweb.service.dto.SAInvoiceDetailsOutWardDTO;

import java.util.List;
import java.util.UUID;

public interface SAInvoiceDetailsRepositoryCustom {

    Page<SAInvoiceDTO> findAllSAInvoiceDTO(Pageable pageable, UUID accountingObject, String fromDate, String toDate, UUID org, String currentBook);

    List<SAInvoiceDetailsOutWardDTO> findBySaInvoiceIDOrderByOrderPriority(List<UUID> id, String currentBook);
}
