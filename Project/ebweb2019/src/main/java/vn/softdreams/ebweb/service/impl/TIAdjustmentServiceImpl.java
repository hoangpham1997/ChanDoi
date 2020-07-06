package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.RefVoucherRepository;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.*;
import vn.softdreams.ebweb.repository.TIAdjustmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.TITransferDetailConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.TITransferDetailsAllConvertDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing TIAdjustment.
 */
@Service
@Transactional
public class TIAdjustmentServiceImpl implements TIAdjustmentService {

    private final Logger log = LoggerFactory.getLogger(TIAdjustmentServiceImpl.class);

    private final UserService userService;

    private final TIAdjustmentRepository tIAdjustmentRepository;

    private final UtilsService utilsService;

    private final UtilsRepository utilsRepository;

    private final RefVoucherRepository refVoucherRepository;

    private final GenCodeService genCodeService;

    private final GeneralLedgerService generalLedgerService;


    public TIAdjustmentServiceImpl(UserService userService, TIAdjustmentRepository tIAdjustmentRepository, UtilsService utilsService, UtilsRepository utilsRepository, RefVoucherRepository refVoucherRepository, GenCodeService genCodeService, GeneralLedgerService generalLedgerService) {
        this.userService = userService;
        this.tIAdjustmentRepository = tIAdjustmentRepository;
        this.utilsService = utilsService;
        this.utilsRepository = utilsRepository;
        this.refVoucherRepository = refVoucherRepository;
        this.genCodeService = genCodeService;
        this.generalLedgerService = generalLedgerService;
    }

    /**
     * Save a tIAdjustment.
     *
     * @param tIAdjustment the entity to save
     * @return the persisted entity
     */
    @Override
    public TIAdjustment save(TIAdjustment tIAdjustment) {
        log.debug("Request to save TITransfer : {}", tIAdjustment);
        tIAdjustment.setTypeID(TypeConstant.DIEU_CHINH_CCDC);
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        tIAdjustment.setCompanyID(currentUserLoginAndOrg.get().getOrg());
        if (tIAdjustment.getId() == null) {
            if (!utilsService.checkQuantityLimitedNoVoucher()) {
                throw new BadRequestAlertException("noBookLimit", "voucherExists", "");
            }
        }

        UserDTO user = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(user);
        if (tIAdjustment.getTypeLedger() == null) {
            tIAdjustment.setTypeLedger(currentBook);
        }
        if (!utilsRepository.checkDuplicateNoVoucher(tIAdjustment.getNoFBook(), tIAdjustment.getNoMBook(), tIAdjustment.getTypeLedger(), tIAdjustment.getId())) {
            throw  new BadRequestAlertException("RSIBadRequest", "voucherExists", "");
        }
        List<RefVoucher> refVouchers = tIAdjustment.getViewVouchers();
        if (tIAdjustment.getRecorded() == null)
        {
            tIAdjustment.setRecorded(false);
        }
        tIAdjustment = tIAdjustmentRepository.save(tIAdjustment);
        if (refVouchers != null && refVouchers.size() > 0) {
            for (RefVoucher item : refVouchers) {
                item.setRefID1(tIAdjustment.getId());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            }

            refVoucherRepository.deleteByRefID1(tIAdjustment.getId());
            refVouchers = refVoucherRepository.saveAll(refVouchers);
            tIAdjustment.setViewVouchers(refVouchers);
        }
        if (tIAdjustment.getRecorded() == null || !tIAdjustment.getRecorded()) {
            if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {
                MessageDTO msg = new MessageDTO("");
                if (!generalLedgerService.record(tIAdjustment, msg)) {
                    tIAdjustment.setRecorded(false);
                } else {
                    tIAdjustment.setRecorded(true);
                }
            }
        }
        return tIAdjustmentRepository.save(tIAdjustment);
    }

    /**
     * Get all the tIAdjustments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TIAdjustment> findAll(Pageable pageable) {
        log.debug("Request to get all TIAdjustments");
        return tIAdjustmentRepository.findAll(pageable);
    }


    /**
     * Get one tIAdjustment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TIAdjustment> findOne(UUID id) {
        log.debug("Request to get TIAdjustment : {}", id);
        return tIAdjustmentRepository.findById(id);
    }

    /**
     * Delete the tIAdjustment by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete TIIncrement : {}", id);
        Long count = refVoucherRepository.countAllByRefID1OrRefID2(id);
        if (count > 0) {
            refVoucherRepository.deleteByRefID1OrRefID2(id);
        }
        TIAdjustment tiIncrement = tIAdjustmentRepository.findById(id).get();
        if (tiIncrement != null && tiIncrement.isRecorded()) {
            Record record = new Record();
            record.setId(id);
            record.setTypeID(TypeConstant.DIEU_CHINH_CCDC);
            generalLedgerService.unRecord(record);
        }
        tIAdjustmentRepository.deleteById(id);
    }

    @Override
    public Page<TITransferAndTIAdjustmentConvertDTO> getAllTIAdjustmentsSearch(Pageable pageable, String fromDate, String toDate, String keySearch) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return tIAdjustmentRepository.getAllTITransferSearch(pageable, currentUserLoginAndOrg.get().getOrg(), fromDate, toDate, keySearch, currentBook);
    }

    @Override
    public TIAdjustmentDetailAllConvertDTO findDetailsByID(UUID id) {
        List<TIAdjustmentDetailConvertDTO> tiAdjustmentDetailConvertDTOS = tIAdjustmentRepository.findDetailsByID(id);
        UserDTO user = userService.getAccount();
        boolean isNoMBook = Utils.PhienSoLamViec(user).equals(1);
        List<RefVoucherDTO> viewVouchers = refVoucherRepository.getRefViewVoucher(id, isNoMBook);
        TIAdjustmentDetailAllConvertDTO tiAdjustmentDetailAllConvertDTO = new TIAdjustmentDetailAllConvertDTO();
        tiAdjustmentDetailAllConvertDTO.setTiAdjustmentDetailConvertDTOS(tiAdjustmentDetailConvertDTOS);
        tiAdjustmentDetailAllConvertDTO.setViewVouchers(viewVouchers);
        return tiAdjustmentDetailAllConvertDTO;
    }

}
