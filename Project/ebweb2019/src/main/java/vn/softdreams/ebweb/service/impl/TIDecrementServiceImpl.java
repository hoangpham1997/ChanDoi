package vn.softdreams.ebweb.service.impl;

import org.joda.time.LocalDate;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.GeneralLedgerService;
import vn.softdreams.ebweb.service.TIDecrementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.UtilsService;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.web.rest.dto.OrganizationUnitCustomDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing TIDecrement.
 */
@Service
@Transactional
public class TIDecrementServiceImpl implements TIDecrementService {

    private final Logger log = LoggerFactory.getLogger(TIDecrementServiceImpl.class);

    private final TIDecrementRepository tIDecrementRepository;

    private final UtilsRepository utilsRepository;

    private final UtilsService utilsService;

    private final UserService userService;

    private final GeneralLedgerService generalLedgerService;

    private final RefVoucherRepository refVoucherRepository;

    private final ToolledgerRepository toolledgerRepository;

    private final TIIncrementRepository tIIncrementRepository;

    private final ToolsRepository toolsRepository;

    public TIDecrementServiceImpl(TIDecrementRepository tIDecrementRepository, UtilsRepository utilsRepository, UtilsService utilsService, GeneralLedgerService generalLedgerService, UserService userService, RefVoucherRepository refVoucherRepository, ToolledgerRepository toolledgerRepository, TIIncrementRepository tIIncrementRepository, ToolsRepository toolsRepository) {
        this.tIDecrementRepository = tIDecrementRepository;
        this.utilsRepository = utilsRepository;
        this.utilsService = utilsService;
        this.generalLedgerService = generalLedgerService;
        this.userService = userService;
        this.refVoucherRepository = refVoucherRepository;
        this.toolledgerRepository = toolledgerRepository;
        this.tIIncrementRepository = tIIncrementRepository;
        this.toolsRepository = toolsRepository;
    }

    /**
     * Save a tIDecrement.
     *
     * @param tIDecrement the entity to save
     * @return the persisted entity
     */
    @Override
    public TIDecrement save(TIDecrement tIDecrement) {
        log.debug("Request to save TIDecrement : {}", tIDecrement);
        tIDecrement.setRecorded(false);
        tIDecrement.setTypeID(TypeConstant.GHI_GIAM_CCDC);
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        tIDecrement.setCompanyID(currentUserLoginAndOrg.get().getOrg());
        if (tIDecrement.getId() == null) {
            if (!utilsService.checkQuantityLimitedNoVoucher()) {
                throw new BadRequestAlertException("noBookLimit", "voucherExists", "");
            }
        }

        UserDTO user = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(user);
        if (tIDecrement.getTypeLedger() == null) {
            tIDecrement.setTypeLedger(currentBook);
        }
        if (!utilsRepository.checkDuplicateNoVoucher(tIDecrement.getNoFBook(), tIDecrement.getNoMBook(), tIDecrement.getTypeLedger(), tIDecrement.getId())) {
            throw  new BadRequestAlertException("RSIBadRequest", "voucherExists", "");
        }
        List<RefVoucher> refVouchers = tIDecrement.getViewVouchers();
        tIDecrement = tIDecrementRepository.save(tIDecrement);
        if (refVouchers != null && refVouchers.size() > 0) {
            for (RefVoucher item : refVouchers) {
                item.setRefID1(tIDecrement.getId());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            }

            refVoucherRepository.deleteByRefID1(tIDecrement.getId());
            refVouchers = refVoucherRepository.saveAll(refVouchers);
            tIDecrement.setViewVouchers(refVouchers);
        }
        if (tIDecrement.getRecorded() == null || !tIDecrement.getRecorded()) {
            if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {
                MessageDTO msg = new MessageDTO("");
                if (!generalLedgerService.record(tIDecrement, msg)) {
                    tIDecrement.setRecorded(false);
                } else {
                    tIDecrement.setRecorded(true);
                }
            }
        }
        return tIDecrementRepository.save(tIDecrement);
    }

    /**
     * Get all the tIDecrements.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TIDecrement> findAll(Pageable pageable) {
        log.debug("Request to get all TIDecrements");
        return tIDecrementRepository.findAll(pageable);
    }


    /**
     * Get one tIDecrement by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TIDecrement> findOne(UUID id) {
        log.debug("Request to get TIDecrement : {}", id);
        Optional<TIDecrement> tiDecrement =  tIDecrementRepository.findById(id);
        if (tiDecrement.isPresent()) {
            Map<UUID, List<OrganizationUnitCustomDTO>> map = new HashMap<UUID, List<OrganizationUnitCustomDTO>>();
//            List<TIIncrementDetails> tIincrementdetails =  tiIncrement.get().getTiIncrementDetails().stream().collect(Collectors.toList());
            for (TIDecrementDetails tiDecrementDetails : tiDecrement.get().getTiDecrementDetails()) {
                if (tiDecrementDetails.getToolsID() != null) {
                    List<OrganizationUnitCustomDTO> organizationUnits = new ArrayList<>();
                    if (!map.containsKey(tiDecrementDetails.getToolsID())) {
                        String date = tiDecrement.get().getDate() != null ? Common.convertDate(tiDecrement.get().getDate()) : null;
                        organizationUnits = toolsRepository.getOrganizationUnitByToolsIDTIDecrement(tiDecrement.get().getCompanyID(), tiDecrement.get().getTypeLedger(), tiDecrementDetails.getToolsID(), date);
                        map.put(tiDecrementDetails.getToolsID(), organizationUnits);
                        if (organizationUnits != null && organizationUnits.size() > 0) {
                            tiDecrementDetails.setOrganizationUnits(organizationUnits);
                        }
                    } else {
                        tiDecrementDetails.setOrganizationUnits(map.get(tiDecrementDetails.getToolsID()));
                    }
                }
            }
        }
        return tIDecrementRepository.findById(id);
    }

    /**
     * Delete the tIDecrement by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete TIDecrement : {}", id);
        TIDecrement tiDecrement = tIDecrementRepository.findById(id).get();
        // nếu đang ghi sổ thì bỏ ghi sổ
        if (tiDecrement != null && tiDecrement.isRecorded()) {
            Record record = new Record();
            record.setId(id);
            record.setTypeID(TypeConstant.GHI_GIAM_CCDC);
            generalLedgerService.unRecord(record);
        }
        // xóa chứng từ tham chiếu
        tIDecrementRepository.deleteById(id);
        if (refVoucherRepository.countAllByRefID1OrRefID2(id) > 0) {
            refVoucherRepository.deleteByRefID1OrRefID2(id);
        }
    }

    @Override
    public Page<TIDecrementConvertDTO> getAllTIDecrementSearch(Pageable pageable, String fromDate, String toDate, String keySearch) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return tIDecrementRepository.getAllTIDecrementSearch(pageable, currentUserLoginAndOrg.get().getOrg(), fromDate, toDate, keySearch, currentBook);
    }

    @Override
    public TIDecrementAllDetailsConvertDTO findDetailsByID(UUID id) {
        UserDTO user = userService.getAccount();
        TIDecrementAllDetailsConvertDTO tiDecrementAllDetailsConvertDTO = new TIDecrementAllDetailsConvertDTO();
        tiDecrementAllDetailsConvertDTO.setTiDecrementDetailsConvertDTOS(tIDecrementRepository.findDetailsByID(id));
        Boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        tiDecrementAllDetailsConvertDTO.setViewVouchers(refVoucherRepository.getRefViewVoucher(id, currentBook));
//        tiIncrementAllDetailsConvertDTOS.setTiIncrementDetailRefVoucherConvertDTOS(tIDecrementRepository.getDataRefVouchers(id, currentBook));
        return tiDecrementAllDetailsConvertDTO;
    }

    @Override
    public HandlingResultDTO multiDelete(List<TIDecrement> tiDecrements) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
//        tIIncrementRepository.findAllByIdIn(listID);
        handlingResultDTO.setCountTotalVouchers(tiDecrements.size());
        List<TIDecrement> listDelete = tiDecrements.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        LocalDate dateClosed = LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (!closeDateStr.equals("") && closeDateStr != null) {
            dateClosed = LocalDate.parse(closeDateStr);
        }
        for (TIDecrement pp: tiDecrements) {
            if (pp.getRecorded() != null && pp.getRecorded()) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang ghi sổ!");
                viewVoucherNo.setPostedDate(pp.getDate());
                viewVoucherNo.setDate(pp.getDate());
                viewVoucherNo.setRefID(pp.getId());
                viewVoucherNo.setNoFBook(pp.getNoFBook());
                viewVoucherNo.setNoMBook(pp.getNoMBook());
                viewVoucherNo.setTypeName("Ghi giảm CCDC");
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(pp);
            }
        }

        List<UUID> uuidList = new ArrayList<>();
        for (int i = 0; i < listDelete.size(); i++) {
            uuidList.add(listDelete.get(i).getId());
        }
        int count = 0;
        if (uuidList.size() > 0) {
            count = refVoucherRepository.countAllByRefID1sOrRefID2s(uuidList);
            if (count > 0) {
                refVoucherRepository.deleteByRefID1sOrRefID2s(uuidList);
            }
//            tIIncrementRepository.updateUnrecord(uuidList);
            tIDecrementRepository.deleteList(uuidList);
            // xóa nhiều dòng ghi tăng
            // xóa nhiều dòng ghi giảm
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(uuidList.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);

        return handlingResultDTO;
    }

    @Override
    public HandlingResultDTO multiUnRecord(List<TIDecrement> tiDecrements) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
//        tIIncrementRepository.findAllByIdIn(listID);
        handlingResultDTO.setCountTotalVouchers(tiDecrements.size());
        List<TIDecrement> listDelete = tiDecrements.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        LocalDate dateClosed = LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (!closeDateStr.equals("") && closeDateStr != null) {
            dateClosed = LocalDate.parse(closeDateStr);
        }
        for (TIDecrement pp: tiDecrements) {
            if (pp.getRecorded() != null && !pp.getRecorded()) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang bỏ ghi sổ!");
                viewVoucherNo.setPostedDate(pp.getDate());
                viewVoucherNo.setDate(pp.getDate());
                viewVoucherNo.setRefID(pp.getId());
                viewVoucherNo.setNoFBook(pp.getNoFBook());
                viewVoucherNo.setNoMBook(pp.getNoMBook());
                viewVoucherNo.setTypeName("Ghi tăng CCDC");
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(pp);
            }
        }

        List<UUID> uuidList = new ArrayList<>();
        for (int i = 0; i < listDelete.size(); i++) {
            uuidList.add(listDelete.get(i).getId());
        }
        if (uuidList.size() > 0) {
            tIDecrementRepository.updateUnrecord(uuidList);
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(uuidList.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);

        return handlingResultDTO;
    }
}
