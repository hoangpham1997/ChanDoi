package vn.softdreams.ebweb.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.IARegisterInvoice;
import vn.softdreams.ebweb.domain.Type;
import vn.softdreams.ebweb.repository.IARegisterInvoiceRepository;
import vn.softdreams.ebweb.repository.RefVoucherRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.IARegisterInvoiceService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.util.*;
import vn.softdreams.ebweb.web.rest.dto.IARegisterInvoiceExportDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.TypeConstant.DANG_KY_SU_DUNG_HOA_DON;

/**
 * Service Implementation for managing IARegisterInvoice.
 */
@Service
@Transactional
public class IARegisterInvoiceServiceImpl implements IARegisterInvoiceService {

    private final Logger log = LoggerFactory.getLogger(IARegisterInvoiceServiceImpl.class);

    private final IARegisterInvoiceRepository iARegisterInvoiceRepository;

    private final RefVoucherRepository refVoucherRepository;

    public IARegisterInvoiceServiceImpl(IARegisterInvoiceRepository iARegisterInvoiceRepository, RefVoucherRepository refVoucherRepository) {
        this.iARegisterInvoiceRepository = iARegisterInvoiceRepository;
        this.refVoucherRepository = refVoucherRepository;
    }

    /**
     * Save a iARegisterInvoice.
     *
     * @param iARegisterInvoice the entity to save
     * @return the persisted entity
     */
    @Override
    public IARegisterInvoice save(IARegisterInvoice iARegisterInvoice) {
        log.debug("Request to save IARegisterInvoice : {}", iARegisterInvoice);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        currentUserLoginAndOrg.ifPresent(securityDTO -> {
            iARegisterInvoice.setCompanyID(securityDTO.getOrg());
            iARegisterInvoice.setTypeId(DANG_KY_SU_DUNG_HOA_DON);

            // TH sửa, nếu số đăng ký không thay đổi
            // thì dừng và lưu luôn
            // Nếu không tiếp tục check trùng số đăng ký ở bên dưới.
            if (iARegisterInvoice.getId() != null) {
                Optional<IARegisterInvoice> oldIARegisterInvoice = iARegisterInvoiceRepository.findById(iARegisterInvoice.getId());
                if (oldIARegisterInvoice.isPresent()) {
                    if (oldIARegisterInvoice.get().getNo() != null && oldIARegisterInvoice.get().getNo().equals(iARegisterInvoice.getNo())) {
                        return;
                    }
                }
            }
            Integer count = iARegisterInvoiceRepository.checkDuplicateNo(securityDTO.getOrg(), iARegisterInvoice.getNo());
            if (count != null && count > 0) {
                throw new BadRequestAlertException("", "iARegisterInvoice", "checkDuplicateNo");
            }
        });
        return iARegisterInvoiceRepository.save(iARegisterInvoice);
    }

    /**
     * Get all the iARegisterInvoices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IARegisterInvoice> findAll(Pageable pageable) {
        log.debug("Request to get all IARegisterInvoices");
        Page<IARegisterInvoice> iaRegisterInvoice = null;
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            iaRegisterInvoice = iARegisterInvoiceRepository.findAllByCompanyIDOrderByDateDesc(pageable, currentUserLoginAndOrg.get().getOrg());
            if (iaRegisterInvoice.getTotalPages() > 0) {
                iaRegisterInvoice.getContent().forEach(IARegisterInvoice::resetAttachFileContent);
            }
        }
        return iaRegisterInvoice;
    }


    /**
     * Get one iARegisterInvoice by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<IARegisterInvoice> findOne(UUID id) {
        log.debug("Request to get IARegisterInvoice : {}", id);
        return iARegisterInvoiceRepository.findById(id);
    }

    /**
     * Delete the iARegisterInvoice by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete IARegisterInvoice : {}", id);
        iARegisterInvoiceRepository.deleteById(id);
    }

    /**
     * xuất pdf đăng ký sử dụng hóa đơn
     */
    @Override
    public byte[] exportPdf() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            List<IARegisterInvoiceExportDTO> iaRegisterInvoice =
                iARegisterInvoiceRepository.findAllByCompanyIDOrderByDateDesc(null, currentUserLoginAndOrg.get().getOrg())
                .stream()
                .map(IARegisterInvoiceExportDTO::new)
                .collect(Collectors.toList());
            return PdfUtils.writeToFile(iaRegisterInvoice, ExcelConstant.IARegisterInvoice.HEADER, ExcelConstant.IARegisterInvoice.FIELD);
        }
        return new byte[]{};
    }

    /**
     * xuất excell đăng ký sử dụng hóa đơn
     */
    @Override
    public byte[] exportExcel() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            List<IARegisterInvoiceExportDTO> iaRegisterInvoice =
                iARegisterInvoiceRepository.findAllByCompanyIDOrderByDateDesc(null, currentUserLoginAndOrg.get().getOrg())
                    .stream()
                    .map(IARegisterInvoiceExportDTO::new)
                    .collect(Collectors.toList());
            return ExcelUtils.writeToFile(iaRegisterInvoice, ExcelConstant.IARegisterInvoice.NAME, ExcelConstant.IARegisterInvoice.HEADER, ExcelConstant.IARegisterInvoice.FIELD);
        }
        return new byte[]{};
    }

    @Override
    public ResponseEntity<byte[]> downloadAttachFile(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        byte[] export = new byte[]{};
        headers.setContentDispositionFormData("error", "error");
        Optional<IARegisterInvoice> iaRegisterInvoiceOptional = iARegisterInvoiceRepository.findById(id);
        if (iaRegisterInvoiceOptional.isPresent()) {
            headers.add("fileName", iaRegisterInvoiceOptional.get().getAttachFileName());
            export = iaRegisterInvoiceOptional.get().getAttachFileContent();
        }
        return new ResponseEntity<>(export, headers, HttpStatus.OK);
    }

    @Override
    public HandlingResultDTO multiDelete(List<IARegisterInvoice> iaRegisterInvoices) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        handlingResultDTO.setCountTotalVouchers(iaRegisterInvoices.size());
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<IARegisterInvoice> listDelete = iaRegisterInvoices.stream().collect(Collectors.toList());
        List<UUID> uuidList_DKSDHD = new ArrayList<>();
        for (int i = 0; i < listDelete.size(); i++) {
            if (listDelete.get(i).getTypeId() == DANG_KY_SU_DUNG_HOA_DON) {
                uuidList_DKSDHD.add(listDelete.get(i).getId());
            }
        }
        handlingResultDTO.setCountSuccessVouchers(listDelete.size());
        if (uuidList_DKSDHD.size() > 0) {
            iARegisterInvoiceRepository.multiDeleteRegisInvoice(currentUserLoginAndOrg.get().getOrg(), uuidList_DKSDHD);
            iARegisterInvoiceRepository.multiDeleteChildRegisInvoice("IARegisterInvoiceDetail", uuidList_DKSDHD);
            refVoucherRepository.deleteByRefID1sOrRefID2s(uuidList_DKSDHD);
        }
        return handlingResultDTO;
    }
}
