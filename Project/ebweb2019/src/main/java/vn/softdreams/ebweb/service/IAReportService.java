package vn.softdreams.ebweb.service;

import net.sf.jasperreports.engine.JRException;
import vn.softdreams.ebweb.domain.IAInvoiceTemplate;
import vn.softdreams.ebweb.domain.IAReport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing IAReport.
 */
public interface IAReportService {

    /**
     * Save a iAReport.
     *
     * @param iAReport the entity to save
     * @return the persisted entity
     */
    IAReport save(IAReport iAReport);

    /**
     * Get all the iAReports.
     *
     * @param pageable the pagination information
     * @param isUnregistered
     * @return the list of entities
     */
    Page<IAReport> findAll(Pageable pageable, Boolean isUnregistered);


    /**
     * Get the "id" iAReport.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<IAReport> findOne(UUID id);

    /**
     * Delete the "id" iAReport.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    /**
     * xuất pdf khỏi tạo mẫu hóa đơn
     */
    byte[] exportPdf();

    /**
     * xuất excell khỏi tạo mẫu hóa đơn
     */
    byte[] exportExcel();

    List<IAReport> findAllByIds(List<UUID> ids);

    /**
     * @param ids danh sách mẫu hóa đơn cần check đã thông báo phát hành hay chưa
     * @return số lượng mẫu hóa đơn đã phát hành
     */
    Integer checkIsPublishedReport(List<UUID> ids);

    byte[] previewInvoiceTemplate(String templatePath) throws JRException;

    HandlingResultDTO multiDelete(List<IAReport> iaReports);

    Page<IAReport> findIAReportsByStatus(Pageable pageable, Boolean isUnregistered, Integer status);
}
