package vn.softdreams.ebweb.service.impl;

import net.sf.jasperreports.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.IAReport;
import vn.softdreams.ebweb.domain.ViewVoucherNo;
import vn.softdreams.ebweb.repository.IAReportRepository;
import vn.softdreams.ebweb.repository.RefVoucherRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.IAReportService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.util.ExcelConstant;
import vn.softdreams.ebweb.service.util.ExcelUtils;
import vn.softdreams.ebweb.service.util.PdfUtils;
import vn.softdreams.ebweb.service.util.ReportUtils;
import vn.softdreams.ebweb.web.rest.dto.IAReportExportDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.TypeConstant.DANG_KY_SU_DUNG_HOA_DON;
import static vn.softdreams.ebweb.service.util.TypeConstant.KHOI_TAO_MAU_HOA_DON;

/**
 * Service Implementation for managing IAReport.
 */
@Service
@Transactional
public class IAReportServiceImpl implements IAReportService {

    private final Logger log = LoggerFactory.getLogger(IAReportServiceImpl.class);

    private final IAReportRepository iAReportRepository;

    private final RefVoucherRepository refVoucherRepository;

    public IAReportServiceImpl(IAReportRepository iAReportRepository, RefVoucherRepository refVoucherRepository) {
        this.iAReportRepository = iAReportRepository;
        this.refVoucherRepository = refVoucherRepository;
    }

    /**
     * Save a iAReport.
     *
     * @param iAReport the entity to save
     * @return the persisted entity
     */
    @Override
    public IAReport save(IAReport iAReport) {
        log.debug("Request to save IAReport : {}", iAReport);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        currentUserLoginAndOrg.ifPresent(securityDTO -> {
            iAReport.setCompanyID(securityDTO.getOrg());

            // TH sửa, nếu Mẫu số hóa đơn và Ký hiệu hóa đơn không thay đổi
            // thì dừng và lưu luôn
            // Nếu không tiếp tục check trùng Mẫu số hóa đơn ở bên dưới.
            if (iAReport.getId() != null) {
                 Optional<IAReport> oldIAReport = iAReportRepository.findById(iAReport.getId());
                 if (oldIAReport.isPresent()) {
                     if (oldIAReport.get().getInvoiceTemplate().equals(iAReport.getInvoiceTemplate())
                         && oldIAReport.get().getInvoiceSeries().equals(iAReport.getInvoiceSeries())) {
                         return;
                     }
                 }
            }
            if (iAReport.getInvoiceTemplate() != null && iAReport.getInvoiceSeries() != null) {
                Integer count = iAReportRepository.checkDuplicate(securityDTO.getOrg(), iAReport.getInvoiceTemplate(), iAReport.getInvoiceSeries());
                if (count != null && count > 0) {
                    throw new BadRequestAlertException("", "iAReport", "duplicate");
                }
            }
        });
        return iAReportRepository.save(iAReport);
    }

    /**
     * Get all the iAReports.
     *
     * @param pageable the pagination information
     * @param isUnregistered
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IAReport> findAll(Pageable pageable, Boolean isUnregistered) {
        log.debug("Request to get all IAReports");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return currentUserLoginAndOrg.map(securityDTO -> {
            if (isUnregistered != null && isUnregistered) {
                return iAReportRepository.findAllUnRegistered(pageable, securityDTO.getOrg());
            }
            return iAReportRepository.findAllByCompanyIDOrderByReportName(pageable, securityDTO.getOrg());
        }).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IAReport> findIAReportsByStatus(Pageable pageable, Boolean isUnregistered, Integer status) {
        log.debug("Request to get all IAReports");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return currentUserLoginAndOrg.map(securityDTO -> {
            if (isUnregistered != null && isUnregistered) {
                return iAReportRepository.findAllUnRegistered(pageable, securityDTO.getOrg());
            }
            return iAReportRepository.findAllByCompanyIDOrderByReportNameIsStatus(pageable, securityDTO.getOrg(), status);
        }).orElse(null);
    }

    @Override
    public List<IAReport> findAllByIds(List<UUID> ids) {
        if (ids != null && ids.size() > 0) {
            return iAReportRepository.findAllByIDs(ids);
        }
        return new ArrayList<>();
    }

    @Override
    public Integer checkIsPublishedReport(List<UUID> ids) {
        Integer count = iAReportRepository.checkIsPublishedReport(ids);
        return count != null ? count : 0;
    }

    @Override
    public byte[] previewInvoiceTemplate(String templatePath) throws JRException {
        return ReportUtils.generateEmptyReportPDF(templatePath);
    }

    @Override
    public HandlingResultDTO multiDelete(List<IAReport> iaReports) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        handlingResultDTO.setCountTotalVouchers(iaReports.size());
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<UUID> listDelete = iaReports.stream().map(IAReport::getId).collect(Collectors.toList());
        List<UUID> uuidList_KTMHD = new ArrayList<>();
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();

        // check rang buoc khi xoa nhieu
        Integer countPublished = iAReportRepository.checkRegisteredMultiDelete(listDelete);
        if (countPublished != null && countPublished > 0) {
            throw new BadRequestAlertException("", "iAReport", "published");
        }

        Integer countRegistered = iAReportRepository.checkPublishedMultiDelete(listDelete);
        if (countRegistered != null && countRegistered > 0) {
            throw new BadRequestAlertException("", "iAReport", "registered");
        }

        handlingResultDTO.setCountSuccessVouchers(listDelete.size());
        iAReportRepository.multiDeleteReportInvoice(currentUserLoginAndOrg.get().getOrg(), uuidList_KTMHD);
        refVoucherRepository.deleteByRefID1sOrRefID2s(uuidList_KTMHD);
        return handlingResultDTO;
    }

    /**
     * Get one iAReport by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<IAReport> findOne(UUID id) {
        log.debug("Request to get IAReport : {}", id);
        return iAReportRepository.findById(id);
    }

    /**
     * Delete the iAReport by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete IAReport : {}", id);

        Integer countPublished = iAReportRepository.checkPublished(id);
        if (countPublished != null && countPublished > 0) {
            throw new BadRequestAlertException("", "iAReport", "published");
        }

        Integer countRegistered = iAReportRepository.checkRegistered(id);
        if (countRegistered != null && countRegistered > 0) {
            throw new BadRequestAlertException("", "iAReport", "registered");
        }
        iAReportRepository.deleteById(id);
    }

    @Override
    public byte[] exportPdf() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            List<IAReportExportDTO> iaReports = iAReportRepository.findAllByCompanyIDOrderByReportName(null, currentUserLoginAndOrg.get().getOrg())
                .stream()
                .map(IAReportExportDTO::new)
                .collect(Collectors.toList());
            return PdfUtils.writeToFile(iaReports, ExcelConstant.IAReport.HEADER, ExcelConstant.IAReport.FIELD);
        }
        return new byte[]{};
    }

    @Override
    public byte[] exportExcel() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            List<IAReportExportDTO> iaReports = iAReportRepository.findAllByCompanyIDOrderByReportName(null, currentUserLoginAndOrg.get().getOrg())
                .stream()
                .map(IAReportExportDTO::new)
                .collect(Collectors.toList());
            return ExcelUtils.writeToFile(iaReports, ExcelConstant.IAReport.NAME, ExcelConstant.IAReport.HEADER, ExcelConstant.IAReport.FIELD);
        }
        return new byte[]{};
    }
}
