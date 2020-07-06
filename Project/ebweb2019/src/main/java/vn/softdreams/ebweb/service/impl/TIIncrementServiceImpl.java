package vn.softdreams.ebweb.service.impl;

import org.joda.time.LocalDate;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.RefVoucherRepository;
import vn.softdreams.ebweb.repository.ToolsRepository;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.*;
import vn.softdreams.ebweb.repository.TIIncrementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.service.util.TypeGroupConstant;
import vn.softdreams.ebweb.web.rest.dto.ItemMapDTO;
import vn.softdreams.ebweb.web.rest.dto.OrganizationUnitCustomDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing TIIncrement.
 */
@Service
@Transactional
public class TIIncrementServiceImpl implements TIIncrementService {

    private final Logger log = LoggerFactory.getLogger(TIIncrementServiceImpl.class);

    private final TIIncrementRepository tIIncrementRepository;

    private final ToolsRepository toolsRepository;

    private final UserService userService;

    private final UtilsService utilsService;

    private final GenCodeService genCodeService;

    private final UtilsRepository utilsRepository;

    private final RefVoucherRepository refVoucherRepository;

    private final GeneralLedgerService generalLedgerService;

    public TIIncrementServiceImpl(TIIncrementRepository tIIncrementRepository, ToolsRepository toolsRepository, UserService userService, UtilsService utilsService, GenCodeService genCodeService, UtilsRepository utilsRepository, RefVoucherRepository refVoucherRepository, GeneralLedgerService generalLedgerService) {
        this.tIIncrementRepository = tIIncrementRepository;
        this.toolsRepository = toolsRepository;
        this.userService = userService;
        this.utilsService = utilsService;
        this.genCodeService = genCodeService;
        this.utilsRepository = utilsRepository;
        this.refVoucherRepository = refVoucherRepository;
        this.generalLedgerService = generalLedgerService;
    }

    /**
     * Save a tIIncrement.
     *
     * @param tIIncrement the entity to save
     * @return the persisted entity
     */
    @Override
    public TIIncrement save(TIIncrement tIIncrement) {
        log.debug("Request to save TIIncrement : {}", tIIncrement);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        tIIncrement.setCompanyID(currentUserLoginAndOrg.get().getOrg());
        if (tIIncrement.getId() == null) {
            if (!utilsService.checkQuantityLimitedNoVoucher()) {
                throw new BadRequestAlertException("noBookLimit", "voucherExists", "");
            }
        }

        UserDTO user = userService.getAccount();
        String currentBook = Utils.PhienSoLamViec(user).toString();
        tIIncrement.setTypeLedger(Integer.valueOf(currentBook));
        tIIncrement.setTypeID(TypeConstant.GHI_TANG_CCDC);
        if (Constants.TypeLedger.BOTH_BOOK.equals(tIIncrement.getTypeLedger())) {
            if (currentBook.equals(Constants.TypeLedger.FINANCIAL_BOOK.toString())) {
                if (tIIncrement.getId() == null) {
                    tIIncrement.setNoMBook(genCodeService.getCodeVoucher(TypeGroupConstant.GHI_TANG_CCDC, Constants.TypeLedger.MANAGEMENT_BOOK));
                }
            } else {
                if (tIIncrement.getId() == null) {
                    tIIncrement.setNoFBook(genCodeService.getCodeVoucher(TypeGroupConstant.GHI_TANG_CCDC, Constants.TypeLedger.FINANCIAL_BOOK));
                }
            }
        } else if (Constants.TypeLedger.MANAGEMENT_BOOK.equals(tIIncrement.getTypeLedger())) {
            if (tIIncrement.getNoFBook() != null) {
                tIIncrement.setNoFBook(null);
            }
        } else if (Constants.TypeLedger.FINANCIAL_BOOK.equals(tIIncrement.getTypeLedger())) {
            if (tIIncrement.getNoMBook() != null) {
                tIIncrement.setNoMBook(null);
            }
        }
        if (!utilsRepository.checkDuplicateNoVoucher(tIIncrement.getNoFBook(), tIIncrement.getNoMBook(), tIIncrement.getTypeLedger(), tIIncrement.getId())) {
            throw new BadRequestAlertException("RSIBadRequest", "voucherExists", "");
        }
        tIIncrement.setRecorded(false);
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        Map<String, ItemMapDTO> mapGetData = new HashMap<String, ItemMapDTO>();
        for (TIIncrementDetails tiIncrementDetails : tIIncrement.getTiIncrementDetails()) {
            if (!mapGetData.containsKey(tiIncrementDetails.getToolsID() + "" + tiIncrementDetails.getDepartmentID())) {
                List<OrganizationUnitCustomDTO> organizationUnits = new ArrayList<>();
                String date = tIIncrement.getDate() != null ? Common.convertDate(tIIncrement.getDate()) : null;
                organizationUnits = toolsRepository.getOrganizationUnitByToolsIDTIIncrement(tIIncrement.getCompanyID(), tIIncrement.getTypeLedger(), tiIncrementDetails.getToolsID(), date);
                for (OrganizationUnitCustomDTO organizationUnitsDto : organizationUnits) {
                    ItemMapDTO itemMapDTO = new ItemMapDTO(organizationUnitsDto.getToolsCode() + " tại " +  organizationUnitsDto.getOrganizationUnitCode(), organizationUnitsDto.getQuantityRest());
                    mapGetData.put(tiIncrementDetails.getToolsID() + "" + organizationUnitsDto.getId(), itemMapDTO);
                }
            }
            if (!map.containsKey(tiIncrementDetails.getToolsID() + "" + tiIncrementDetails.getDepartmentID())) {
                map.put(tiIncrementDetails.getToolsID() + "" + tiIncrementDetails.getDepartmentID(), tiIncrementDetails.getQuantity());
            } else {
                map.put(tiIncrementDetails.getToolsID() + "" + tiIncrementDetails.getDepartmentID(),
                    map.get(tiIncrementDetails.getToolsID() + "" + tiIncrementDetails.getDepartmentID()).add(tiIncrementDetails.getQuantity()));
            }
        }
        StringBuilder error = new StringBuilder();
        for (Object key : map.keySet()) {
            System.out.println(key + " " + map.get(key));
            if (mapGetData.containsKey(key)) {
                if (map.get(key).compareTo(mapGetData.get(key).getQuantity()) > 0) {
                    error.append(mapGetData.get(key).getName() + ", ");
                }
            }
        }
        if (error != null && (error.toString().trim().length() > 0)) {
            throw new BadRequestAlertException("maxQuantity", error.toString().substring(0, error.toString().length() - 2), "");
        }
        tIIncrement = tIIncrementRepository.save(tIIncrement);
//        List<RefVoucher> refVouchers = TIIncrement.();
//        List<RefVoucher> refVouchersForRef = new ArrayList<>();
//        for (RefVoucher item : refVouchers) {
//            item.setRefID1(ppDiscountReturns.getId());
//            item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
//        }
//        if (refVouchersForRef.size() > 0) {
//            refVouchers.addAll(refVouchersForRef);
//        }
//        refVoucherRepository.deleteByRefID1(ppDiscountReturns.getId());
//        refVouchers = refVoucherRepository.saveAll(refVouchers);
//        discountAndBillAndRSIDTO1.setViewVouchers(refVouchers);

        if (tIIncrement.getRecorded() == null || !tIIncrement.getRecorded()) {
            if (user.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {
                MessageDTO msg = new MessageDTO("");
                if (!generalLedgerService.record(tIIncrement, msg)) {
                    tIIncrement.setRecorded(false);
                } else {
                    tIIncrement.setRecorded(true);
                }
            }
        }
        return tIIncrementRepository.save(tIIncrement);
    }

    /**
     * Get all the tIIncrements.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TIIncrement> findAll(Pageable pageable) {
        log.debug("Request to get all TIIncrements");
        return tIIncrementRepository.findAll(pageable);
    }


    /**
     * Get one tIIncrement by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TIIncrement> findOne(UUID id) {
        log.debug("Request to get TIIncrement : {}", id);
        Optional<TIIncrement> tiIncrement = tIIncrementRepository.findById(id);
        if (tiIncrement.isPresent()) {
            Map<UUID, List<OrganizationUnitCustomDTO>> map = new HashMap<UUID, List<OrganizationUnitCustomDTO>>();
//            List<TIIncrementDetails> tIincrementdetails =  tiIncrement.get().getTiIncrementDetails().stream().collect(Collectors.toList());
            for (TIIncrementDetails tiIncrementDetails : tiIncrement.get().getTiIncrementDetails()) {
                if (tiIncrementDetails.getToolsID() != null) {
                    List<OrganizationUnitCustomDTO> organizationUnits = new ArrayList<>();
                    if (!map.containsKey(tiIncrementDetails.getToolsID())) {
                        String date = tiIncrement.get().getDate() != null ? Common.convertDate(tiIncrement.get().getDate()) : null;
                        organizationUnits = toolsRepository.getOrganizationUnitByToolsIDTIDecrement(tiIncrement.get().getCompanyID(), tiIncrement.get().getTypeLedger(), tiIncrementDetails.getToolsID(), date);
                        map.put(tiIncrementDetails.getToolsID(), organizationUnits);
                        if (organizationUnits != null && organizationUnits.size() > 0) {
                            tiIncrementDetails.setOrganizationUnits(organizationUnits);
                        }
                    } else {
                        tiIncrementDetails.setOrganizationUnits(map.get(tiIncrementDetails.getToolsID()));
                    }
                }
            }
        }
        return tiIncrement;
    }

    /**
     * Delete the tIIncrement by id.
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
        TIIncrement tiIncrement = tIIncrementRepository.findById(id).get();
        if (tiIncrement != null && tiIncrement.isRecorded()) {
            Record record = new Record();
            record.setId(id);
            record.setTypeID(TypeConstant.GHI_TANG_CCDC);
            generalLedgerService.unRecord(record);
        }
        tIIncrementRepository.deleteById(id);
    }

    @Override
    public List<ToolsConvertDTO> getAllToolsByActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return toolsRepository.getAllToolsByActive(currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    public Page<TIIncrementConvertDTO> getAllTIIncrementsSearch(Pageable pageable, String fromDate, String toDate, String keySearch) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return tIIncrementRepository.getAllTIIncrementsSearch(pageable, currentUserLoginAndOrg.get().getOrg(), fromDate, toDate, keySearch, currentBook);
    }

    @Override
    public TIIncrementAllDetailsConvertDTO findDetailsByID(UUID id) {
        TIIncrementAllDetailsConvertDTO tiIncrementAllDetailsConvertDTOS = new TIIncrementAllDetailsConvertDTO();
        tiIncrementAllDetailsConvertDTOS.setTiIncrementDetailsConvertDTOS(tIIncrementRepository.findDetailsByID(id));
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        tiIncrementAllDetailsConvertDTOS.setTiIncrementDetailRefVoucherConvertDTOS(tIIncrementRepository.getDataRefVouchers(id, currentBook));
        return tiIncrementAllDetailsConvertDTOS;
    }

    @Override
    public HandlingResultDTO multiDelete(List<TIIncrement> tiIncrements) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
//        tIIncrementRepository.findAllByIdIn(listID);
        handlingResultDTO.setCountTotalVouchers(tiIncrements.size());
        List<TIIncrement> listDelete = tiIncrements.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        LocalDate dateClosed = LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (!closeDateStr.equals("") && closeDateStr != null) {
            dateClosed = LocalDate.parse(closeDateStr);
        }
        for (TIIncrement pp : tiIncrements) {
            if (pp.getRecorded() != null && pp.getRecorded()) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang ghi sổ!");
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
        int count = 0;
        if (uuidList.size() > 0) {
            count = refVoucherRepository.countAllByRefID1sOrRefID2s(uuidList);
            if (count > 0) {
                refVoucherRepository.deleteByRefID1sOrRefID2s(uuidList);
            }
//            tIIncrementRepository.updateUnrecord(uuidList);
            tIIncrementRepository.deleteList(uuidList);
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(uuidList.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);

        return handlingResultDTO;
    }

    @Override
    public HandlingResultDTO multiUnRecord(List<TIIncrement> tiIncrements) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
//        tIIncrementRepository.findAllByIdIn(listID);
        handlingResultDTO.setCountTotalVouchers(tiIncrements.size());
        List<TIIncrement> listDelete = tiIncrements.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        LocalDate dateClosed = LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (!closeDateStr.equals("") && closeDateStr != null) {
            dateClosed = LocalDate.parse(closeDateStr);
        }
        for (TIIncrement pp : tiIncrements) {
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
            tIIncrementRepository.updateUnrecord(uuidList);
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(uuidList.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);

        return handlingResultDTO;
    }

    @Override
    public Optional<TIIncrement> findByRowNum(Pageable pageable, String fromDate, String toDate, String keySearch, Integer rowNum) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return tIIncrementRepository.findByRowNum(pageable, currentUserLoginAndOrg.get().getOrg(), fromDate, toDate, rowNum, keySearch, currentBook);
    }

    @Override
    public List<ToolsConvertDTO> getToolsActiveByIncrements(String date) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(user);
        return toolsRepository.getToolsActiveByIncrements(currentUserLoginAndOrg.get().getOrg(), currentBook, date);
    }

    @Override
    public List<ToolsConvertDTO> getToolsActiveByTIDecrement(String date) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(user);
        return toolsRepository.getToolsActiveByTIDecrement(currentUserLoginAndOrg.get().getOrg(), currentBook, date);
    }

    @Override
    public List<OrganizationUnitCustomDTO> getOrganizationUnitByToolsIDTIDecrement(UUID id, String date) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(user);
        return toolsRepository.getOrganizationUnitByToolsIDTIDecrement(currentUserLoginAndOrg.get().getOrg(), currentBook, id, date);
    }

    @Override
    public List<OrganizationUnitCustomDTO> getOrganizationUnitByToolsIDTIIncrement(UUID id, String date) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(user);
        return toolsRepository.getOrganizationUnitByToolsIDTIIncrement(currentUserLoginAndOrg.get().getOrg(), currentBook, id, date);
    }

}
