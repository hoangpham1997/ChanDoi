package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.RefVoucherRepository;
import vn.softdreams.ebweb.repository.ToolsRepository;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.GeneralLedgerService;
import vn.softdreams.ebweb.service.TITransferService;
import vn.softdreams.ebweb.repository.TITransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.UtilsService;
import vn.softdreams.ebweb.service.dto.MessageDTO;
import vn.softdreams.ebweb.service.dto.TITransferAndTIAdjustmentConvertDTO;
import vn.softdreams.ebweb.service.dto.ToolsConvertDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.web.rest.dto.OrganizationUnitCustomDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.TITransferDetailConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.TITransferDetailsAllConvertDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.*;

/**
 * Service Implementation for managing TITransfer.
 */
@Service
@Transactional
public class TITransferServiceImpl implements TITransferService {

    private final Logger log = LoggerFactory.getLogger(TITransferServiceImpl.class);

    private final TITransferRepository tITransferRepository;

    private final GeneralLedgerService generalLedgerService;

    private final UserService userService;

    private final UtilsService utilsService;

    private final RefVoucherRepository refVoucherRepository;

    private final UtilsRepository utilsRepository;

    private final ToolsRepository toolsRepository;

    public TITransferServiceImpl(TITransferRepository tITransferRepository, GeneralLedgerService generalLedgerService, UserService userService, UtilsService utilsService, RefVoucherRepository refVoucherRepository, UtilsRepository utilsRepository, ToolsRepository toolsRepository) {
        this.tITransferRepository = tITransferRepository;
        this.generalLedgerService = generalLedgerService;
        this.userService = userService;
        this.utilsService = utilsService;
        this.refVoucherRepository = refVoucherRepository;
        this.utilsRepository = utilsRepository;
        this.toolsRepository = toolsRepository;
    }

    /**
     * Save a tITransfer.
     *
     * @param tITransfer the entity to save
     * @return the persisted entity
     */
    @Override
    public TITransfer save(TITransfer tITransfer) {
        log.debug("Request to save TITransfer : {}", tITransfer);
        tITransfer.setTypeID(TypeConstant.DIEU_CHUYEN_CCDC);
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        tITransfer.setCompanyID(currentUserLoginAndOrg.get().getOrg());
        if (tITransfer.getId() == null) {
            if (!utilsService.checkQuantityLimitedNoVoucher()) {
                throw new BadRequestAlertException("noBookLimit", "voucherExists", "");
            }
        }

        UserDTO user = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(user);
        if (tITransfer.getTypeLedger() == null) {
            tITransfer.setTypeLedger(currentBook);
        }
        if (!utilsRepository.checkDuplicateNoVoucher(tITransfer.getNoFBook(), tITransfer.getNoMBook(), tITransfer.getTypeLedger(), tITransfer.getId())) {
            throw  new BadRequestAlertException("RSIBadRequest", "voucherExists", "");
        }
        if (tITransfer.getRecorded() == null)
        {
            tITransfer.setRecorded(false);
        }
        List<RefVoucher> refVouchers = tITransfer.getViewVouchers();
        tITransfer = tITransferRepository.save(tITransfer);
        if (refVouchers != null && refVouchers.size() > 0) {
            for (RefVoucher item : refVouchers) {
                item.setRefID1(tITransfer.getId());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            }
            refVoucherRepository.deleteByRefID1(tITransfer.getId());
            refVouchers = refVoucherRepository.saveAll(refVouchers);
            tITransfer.setViewVouchers(refVouchers);
        }
        if (tITransfer.getRecorded() == null || !tITransfer.getRecorded()) {
            if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {
                MessageDTO msg = new MessageDTO("");
                if (!generalLedgerService.record(tITransfer, msg)) {
                    tITransfer.setRecorded(false);
                } else {
                    tITransfer.setRecorded(true);
                }
            }
        }
        return tITransferRepository.save(tITransfer);
    }

    /**
     * Get all the tITransfers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TITransfer> findAll(Pageable pageable) {
        log.debug("Request to get all TITransfers");
        return tITransferRepository.findAll(pageable);
    }


    /**
     * Get one tITransfer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TITransfer> findOne(UUID id) {
        log.debug("Request to get TITransfer : {}", id);
        Optional<TITransfer> tiTransfer =  tITransferRepository.findById(id);
        if (tiTransfer.isPresent()) {
            Map<UUID, List<OrganizationUnitCustomDTO>> map = new HashMap<UUID, List<OrganizationUnitCustomDTO>>();
//            List<TIIncrementDetails> tIincrementdetails =  tiIncrement.get().getTiIncrementDetails().stream().collect(Collectors.toList());
            for (TITransferDetails tiTransferDetails : tiTransfer.get().getTiTransferDetails()) {
                if (tiTransferDetails.getToolsID() != null) {
                    List<OrganizationUnitCustomDTO> organizationUnits = new ArrayList<>();
                    if (!map.containsKey(tiTransferDetails.getToolsID())) {
                        String date = tiTransfer.get().getDate() != null ? Common.convertDate(tiTransfer.get().getDate()) : null;
                        organizationUnits = toolsRepository.getOrganizationUnitByToolsIDTIDecrement(tiTransfer.get().getCompanyID(), tiTransfer.get().getTypeLedger(), tiTransferDetails.getToolsID(), date);
                        map.put(tiTransferDetails.getToolsID(), organizationUnits);
                        if (organizationUnits != null && organizationUnits.size() > 0) {
                            tiTransferDetails.setOrganizationUnitsFrom(organizationUnits);
                        }
                    } else {
                        tiTransferDetails.setOrganizationUnitsFrom(map.get(tiTransferDetails.getToolsID()));
                    }
                }
            }
        }
        return tITransferRepository.findById(id);
    }

    /**
     * Delete the tITransfer by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete TIDecrement : {}", id);
        TITransfer tiTransfer = tITransferRepository.findById(id).get();
        // nếu đang ghi sổ thì bỏ ghi sổ
        if (tiTransfer != null && tiTransfer.isRecorded()) {
            Record record = new Record();
            record.setId(id);
            record.setTypeID(TypeConstant.GHI_GIAM_CCDC);
            generalLedgerService.unRecord(record);
        }
        // xóa chứng từ tham chiếu
        tITransferRepository.deleteById(id);
        if (refVoucherRepository.countAllByRefID1OrRefID2(id) > 0) {
            refVoucherRepository.deleteByRefID1OrRefID2(id);
        }
    }

    @Override
    public List<ToolsConvertDTO> getToolsActiveByTITransfer() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(user);
        return tITransferRepository.getToolsActiveByTITransfer(currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    public Page<TITransferAndTIAdjustmentConvertDTO> getAllTITransferSearch(Pageable pageable, String fromDate, String toDate, String keySearch) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return tITransferRepository.getAllTITransferSearch(pageable, currentUserLoginAndOrg.get().getOrg(), fromDate, toDate, keySearch, currentBook);
    }

    @Override
    public TITransferDetailsAllConvertDTO findDetailsByID(UUID id) {
        List<TITransferDetailConvertDTO> tiTransferDetailsAllConvertDTOS = tITransferRepository.findDetailsByID(id);
        UserDTO user = userService.getAccount();
        boolean isNoMBook = Utils.PhienSoLamViec(user).equals(1);
        List<RefVoucherDTO> viewVouchers = refVoucherRepository.getRefViewVoucher(id, isNoMBook);
        TITransferDetailsAllConvertDTO tiTransferDetailsAllConvertDTO = new TITransferDetailsAllConvertDTO();
        tiTransferDetailsAllConvertDTO.setTiTransferDetailsConvertDTOS(tiTransferDetailsAllConvertDTOS);
        tiTransferDetailsAllConvertDTO.setViewVouchers(viewVouchers);
        return tiTransferDetailsAllConvertDTO;
    }
}
