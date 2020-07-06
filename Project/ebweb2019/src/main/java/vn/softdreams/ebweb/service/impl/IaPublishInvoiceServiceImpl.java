package vn.softdreams.ebweb.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.IaPublishInvoice;
import vn.softdreams.ebweb.domain.IaPublishInvoiceDetails;
import vn.softdreams.ebweb.repository.IaPublishInvoiceRepository;
import vn.softdreams.ebweb.repository.SaBillRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.IaPublishInvoiceService;
import vn.softdreams.ebweb.service.util.ExcelConstant;
import vn.softdreams.ebweb.service.util.ExcelUtils;
import vn.softdreams.ebweb.service.util.PdfUtils;
import vn.softdreams.ebweb.web.rest.dto.IAPublishInvoiceExportDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing IaPublishInvoice.
 */
@Service
@Transactional
public class IaPublishInvoiceServiceImpl implements IaPublishInvoiceService {

    private final Logger log = LoggerFactory.getLogger(IaPublishInvoiceServiceImpl.class);

    private final IaPublishInvoiceRepository iaPublishInvoiceRepository;
    private final SaBillRepository saBillRepository;

    public IaPublishInvoiceServiceImpl(IaPublishInvoiceRepository iaPublishInvoiceRepository,
                                       SaBillRepository saBillRepository
    ) {
        this.iaPublishInvoiceRepository = iaPublishInvoiceRepository;
        this.saBillRepository = saBillRepository;
    }

    /**
     * Save a iaPublishInvoice.
     *
     * @param iaPublishInvoice the entity to save
     * @return the persisted entity
     */
    @Override
    public IaPublishInvoice save(IaPublishInvoice iaPublishInvoice) {
        log.debug("Request to save IaPublishInvoice : {}", iaPublishInvoice);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        currentUserLoginAndOrg.ifPresent(securityDTO -> iaPublishInvoice.setCompanyId(securityDTO.getOrg()));

        /**
         * B1: Tìm sự thay đổi của chi tiết thông báo
         * B2: Nếu có thay đổi thì check xem nó đã đc dùng ở sabill chưa
         * B3: Nếu đã dùng thì thông báo
         * B4: Nếu chưa check tiếp từ số và đến số có trùng không
         */
        if (iaPublishInvoice.getId() != null) {
            Set<IaPublishInvoiceDetails> newData = iaPublishInvoice.getIaPublishInvoiceDetails();
            Set<IaPublishInvoiceDetails> oldData = iaPublishInvoiceRepository
                .findById(iaPublishInvoice.getId())
                .map(IaPublishInvoice::getIaPublishInvoiceDetails)
                .orElseGet(null);
            if (oldData != null) {
                oldData.forEach(item -> {
                    Optional<IaPublishInvoiceDetails> newDetail = newData.stream()
                        .filter(data -> data.getId().equals(item.getId()))
                        .findFirst();
                    if (newDetail.isPresent()) {
                        IaPublishInvoiceDetails detail = newDetail.get();
                        if (!detail.equals(item)) {
                            checkLinkedInvoiceTemplate(
                                detail.getInvoiceTemplate(),
                                detail.getInvoiceSeries(),
                                iaPublishInvoice.getCompanyId());
                        }
                        if (!detail.getFromNo().equals(item.getFromNo()) || !detail.getToNo().equals(item.getToNo())) {
                            checkExistedNoRange(detail.getFromNo(), detail.getToNo(), detail.getIaReportID(), detail.getInvoiceTemplate(), detail.getId(), detail.getInvoiceSeries());
                        }
                    } else {
                        checkLinkedInvoiceTemplate(
                            item.getInvoiceTemplate(),
                            item.getInvoiceSeries(),
                            iaPublishInvoice.getCompanyId());
                    }
                });
            }
        } else {
            for (IaPublishInvoiceDetails detail: iaPublishInvoice.getIaPublishInvoiceDetails()) {
                checkExistedNoRange(detail.getFromNo(), detail.getToNo(), detail.getIaReportID(), detail.getInvoiceTemplate(), null, detail.getInvoiceSeries());
            }
        }
        return iaPublishInvoiceRepository.save(iaPublishInvoice);
    }

    private void checkExistedNoRange(String fromNo, String toNo, UUID iaReportID, String invoiceTemplate, UUID iaPublishInvoiceDetailID, String invoiceSeries) {
        Long count = iaPublishInvoiceRepository.checkExistedNoRange(fromNo, toNo, iaReportID, iaPublishInvoiceDetailID, invoiceSeries);
        if (count != null && count > 0) {
            throw new BadRequestAlertException(invoiceTemplate + " - " + invoiceSeries, "iaPublishInvoice", "noRangeExisted");
        }
    }

    private void checkLinkedInvoiceTemplate(String invoiceTemplate, String invoiceSeries, UUID companyID) {
        Long count = saBillRepository.countByInvoiceTemplateAndInvoiceSeriesAndCompanyID(invoiceTemplate, invoiceSeries, companyID);
        if (count != null && count > 0) {
            throw new BadRequestAlertException(invoiceTemplate + " - " + invoiceSeries, "iaPublishInvoice", "linkedInvoiceTemplate");
        }
    }

    /**
     * Get all the iaPublishInvoices.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<IaPublishInvoice> findAll() {
        log.debug("Request to get all IaPublishInvoices");
        return iaPublishInvoiceRepository.findAll();
    }


    /**
     * Get one iaPublishInvoice by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<IaPublishInvoice> findOne(UUID id) {
        log.debug("Request to get IaPublishInvoice : {}", id);
        return iaPublishInvoiceRepository.findById(id);
    }

    /**
     * Delete the iaPublishInvoice by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete IaPublishInvoice : {}", id);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        currentUserLoginAndOrg.ifPresent(securityDTO ->
            iaPublishInvoiceRepository
                .findById(id)
                .map(IaPublishInvoice::getIaPublishInvoiceDetails)
                .ifPresent(details -> details
                    .forEach(detail ->
                        checkLinkedInvoiceTemplate(
                            detail.getInvoiceTemplate(),
                            detail.getInvoiceSeries(),
                            securityDTO.getOrg()))));
        iaPublishInvoiceRepository.deleteById(id);
    }

    @Override
    public Page<IaPublishInvoice> findAll(Pageable pageable) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return currentUserLoginAndOrg.map(securityDTO -> iaPublishInvoiceRepository.findAllOrderByDate(pageable, securityDTO.getOrg())).orElse(null);
    }

    /**
     * xuất pdf thông báo phát hành hóa đơn
     */
    @Override
    public byte[] exportPdf() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return currentUserLoginAndOrg.map(securityDTO -> {
            List<IAPublishInvoiceExportDTO> iaPublishInvoices = iaPublishInvoiceRepository.findAllOrderByDate(null, securityDTO.getOrg())
                .stream()
                .map(IAPublishInvoiceExportDTO::new)
                .collect(Collectors.toList());
            return PdfUtils.writeToFile(iaPublishInvoices, ExcelConstant.IAPublishInvoice.HEADER, ExcelConstant.IAPublishInvoice.FIELD);
        }).orElse(null);
    }

    /**
     * xuất excell thông báo phát hành hóa đơn
     */
    @Override
    public byte[] exportExcel() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return currentUserLoginAndOrg.map(securityDTO -> {
            List<IAPublishInvoiceExportDTO> iaPublishInvoices = iaPublishInvoiceRepository.findAllOrderByDate(null, securityDTO.getOrg())
                .stream()
                .map(IAPublishInvoiceExportDTO::new)
                .collect(Collectors.toList());
            return ExcelUtils.writeToFile(iaPublishInvoices, ExcelConstant.IAPublishInvoice.NAME, ExcelConstant.IAPublishInvoice.HEADER, ExcelConstant.IAPublishInvoice.FIELD);
        }).orElse(null);
    }

    /**
     * @author dungvm
     * lấy đến số lớn nhất dựa vào IAReportID
     */
    @Override
    public Long findCurrentMaxFromNo(UUID id) {
        return iaPublishInvoiceRepository.findCurrentMaxFromNo(id);
    }
}
