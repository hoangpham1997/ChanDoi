package vn.softdreams.ebweb.service.impl;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.FAIncrement;
import vn.softdreams.ebweb.domain.ViewVoucherNo;
import vn.softdreams.ebweb.repository.FAIncrementRepository;
import vn.softdreams.ebweb.repository.FixedAssetRepository;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.*;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.*;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing FAIncrement.
 */
@Service
@Transactional
public class FAIncrementServiceImpl implements FAIncrementService {

    private final Logger log = LoggerFactory.getLogger(FAIncrementServiceImpl.class);

    private final FAIncrementRepository faIncrementRepository;

    private final FixedAssetRepository fixedAssetRepository;

    private final UserService userService;

    private final UtilsService utilsService;

    private final GenCodeService genCodeService;

    private final UtilsRepository utilsRepository;

    private final GeneralLedgerService generalLedgerService;

    public FAIncrementServiceImpl(FAIncrementRepository faIncrementRepository, FixedAssetRepository fixedAssetRepository, UserService userService, UtilsService utilsService, GenCodeService genCodeService, UtilsRepository utilsRepository, GeneralLedgerService generalLedgerService) {
        this.faIncrementRepository = faIncrementRepository;
        this.fixedAssetRepository = fixedAssetRepository;
        this.userService = userService;
        this.utilsService = utilsService;
        this.genCodeService = genCodeService;
        this.utilsRepository = utilsRepository;
        this.generalLedgerService = generalLedgerService;
    }

    /**
     * Save a faIncrement.
     *
     * @param faIncrement the entity to save
     * @return the persisted entity
     */
    @Override
    public FAIncrement save(FAIncrement faIncrement) {
        log.debug("Request to save TIIncrement : {}", faIncrement);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        faIncrement.setCompanyID(currentUserLoginAndOrg.get().getOrg());
        if (faIncrement.getId() == null) {
            if (!utilsService.checkQuantityLimitedNoVoucher()) {
                throw new BadRequestAlertException("noBookLimit", "voucherExists", "");
            }
        }
        UserDTO user = userService.getAccount();
        String currentBook = Utils.PhienSoLamViec(user).toString();
        faIncrement.setTypeLedger(Integer.valueOf(currentBook));
        faIncrement.setTypeID(TypeConstant.TSCD_GHI_TANG);
        if (Constants.TypeLedger.BOTH_BOOK.equals(faIncrement.getTypeLedger())) {
            if (currentBook.equals(Constants.TypeLedger.FINANCIAL_BOOK.toString())) {
                if (faIncrement.getId() == null) {
                    faIncrement.setNoMBook(genCodeService.getCodeVoucher(TypeGroupConstant.TSCD_GHI_TANG, Constants.TypeLedger.MANAGEMENT_BOOK));
                }
            } else {
                if (faIncrement.getId() == null) {
                    faIncrement.setNoFBook(genCodeService.getCodeVoucher(TypeGroupConstant.TSCD_GHI_TANG, Constants.TypeLedger.FINANCIAL_BOOK));
                }
            }
        } else if (Constants.TypeLedger.MANAGEMENT_BOOK.equals(faIncrement.getTypeLedger())) {
            if (faIncrement.getNoFBook() != null) {
                faIncrement.setNoFBook(null);
            }
        } else if (Constants.TypeLedger.FINANCIAL_BOOK.equals(faIncrement.getTypeLedger())) {
            if (faIncrement.getNoMBook() != null) {
                faIncrement.setNoMBook(null);
            }
        }
        if (!utilsRepository.checkDuplicateNoVoucher(faIncrement.getNoFBook(), faIncrement.getNoMBook(), faIncrement.getTypeLedger(), faIncrement.getId())) {
            throw new BadRequestAlertException("RSIBadRequest", "voucherExists", "");
        }
        faIncrement.setRecorded(false);
        faIncrement = faIncrementRepository.save(faIncrement);

        if (faIncrement.getRecorded() == null || !faIncrement.getRecorded()) {
            if (user.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {
                MessageDTO msg = new MessageDTO("");
                if (!generalLedgerService.record(faIncrement, msg)) {
                    faIncrement.setRecorded(false);
                } else {
                    faIncrement.setRecorded(true);
                }
            }
        }
        return faIncrementRepository.save(faIncrement);
    }

    /**
     * Get all the faIncrements.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FAIncrement> findAll(Pageable pageable) {
        log.debug("Request to get all FAIncrements");
        return faIncrementRepository.findAll(pageable);
    }


    /**
     * Get one faIncrement by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FAIncrement> findOne(UUID id) {
        log.debug("Request to get FAIncrement : {}", id);
        return faIncrementRepository.findById(id);
    }

    /**
     * Delete the faIncrement by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete FAIncrement : {}", id);
        faIncrementRepository.deleteById(id);
    }

    @Override
    public List<FixedAssetDTO> getAllFixedAssetByActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return fixedAssetRepository.getAllFixedAssetByActive(currentUserLoginAndOrg.get().getOrg());
    }

    @Override
    public Page<FAIncrementConvertDTO> getAllFAIncrementsSearch(Pageable pageable, String fromDate, String toDate, String keySearch) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return faIncrementRepository.getAllFAIncrementsSearch(pageable, currentUserLoginAndOrg.get().getOrg(), fromDate, toDate, keySearch, currentBook);
    }

    @Override
    public FAIncrementAllDetailsConvertDTO findDetailsByID(UUID id) {
        FAIncrementAllDetailsConvertDTO  faIncrementAllDetailsConvertDTOS = new FAIncrementAllDetailsConvertDTO();
        faIncrementAllDetailsConvertDTOS.setFaIncrementDetailsConvertDTOS(faIncrementRepository.findDetailsByID(id));
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        faIncrementAllDetailsConvertDTOS.setFaIncrementDetailRefVoucherConvertDTOS(faIncrementRepository.getDataRefVouchers(id, currentBook));
        return faIncrementAllDetailsConvertDTOS;
    }

    @Override
    public HandlingResultDTO multiDelete(List<FAIncrement> faIncrements) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
//        faIncrementRepository.findAllByIdIn(listID);
        handlingResultDTO.setCountTotalVouchers(faIncrements.size());
        List<FAIncrement> listDelete = faIncrements.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        LocalDate dateClosed = LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (!closeDateStr.equals("") && closeDateStr != null) {
            dateClosed = LocalDate.parse(closeDateStr);
        }
        for (FAIncrement pp: faIncrements) {
            if (pp.getRecorded() != null && pp.getRecorded()) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang ghi sổ!");
                viewVoucherNo.setPostedDate(pp.getDate());
                viewVoucherNo.setDate(pp.getDate());
                viewVoucherNo.setRefID(pp.getId());
                viewVoucherNo.setNoFBook(pp.getNoFBook());
                viewVoucherNo.setNoMBook(pp.getNoMBook());
                viewVoucherNo.setTypeName("Ghi tăng TSCD");
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(pp);
            }
        }

        List<UUID> uuidList = new ArrayList<>();
        for (int i = 0; i < listDelete.size(); i++) {
            uuidList.add(listDelete.get(i).getId());
        }
        if (uuidList.size() > 0) {
//            faIncrementRepository.updateUnrecord(uuidList);
            faIncrementRepository.deleteList(uuidList);
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(uuidList.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);

        return handlingResultDTO;
    }

    @Override
    public HandlingResultDTO multiUnRecord(List<FAIncrement> faIncrements) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
//        faIncrementRepository.findAllByIdIn(listID);
        handlingResultDTO.setCountTotalVouchers(faIncrements.size());
        List<FAIncrement> listDelete = faIncrements.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        LocalDate dateClosed = LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (!closeDateStr.equals("") && closeDateStr != null) {
            dateClosed = LocalDate.parse(closeDateStr);
        }
        for (FAIncrement pp: faIncrements) {
            if (pp.getRecorded() != null && !pp.getRecorded()) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang bỏ ghi sổ!");
                viewVoucherNo.setPostedDate(pp.getDate());
                viewVoucherNo.setDate(pp.getDate());
                viewVoucherNo.setRefID(pp.getId());
                viewVoucherNo.setNoFBook(pp.getNoFBook());
                viewVoucherNo.setNoMBook(pp.getNoMBook());
                viewVoucherNo.setTypeName("Ghi tăng TSCD");
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(pp);
            }
        }

        List<UUID> uuidList = new ArrayList<>();
        for (int i = 0; i < listDelete.size(); i++) {
            uuidList.add(listDelete.get(i).getId());
        }
        if (uuidList.size() > 0) {
            faIncrementRepository.updateUnRecord(uuidList);
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(uuidList.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);

        return handlingResultDTO;
    }

    @Override
    public Optional<FAIncrement> findByRowNum(Pageable pageable, String fromDate, String toDate, String keySearch, Integer rowNum) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return faIncrementRepository.findByRowNum(pageable, currentUserLoginAndOrg.get().getOrg(), fromDate, toDate, rowNum, keySearch, currentBook);
    }

    @Override
    public byte[] exportPdf(String fromDate, String toDate, String keySearch) {
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            UserDTO user = userService.getAccount();
            boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
            Page<FAIncrementConvertDTO> convertDTOS = currentUserLoginAndOrg.map(securityDTO -> faIncrementRepository.getAllFAIncrementsSearch(null, currentUserLoginAndOrg.get().getOrg(), fromDate, toDate, keySearch, currentBook)).orElse(null);
            return PdfUtils.writeToFile(convertDTOS.getContent(), ExcelConstant.FAIncrement.HEADER, ExcelConstant.FAIncrement.FIELD, user);
        }
        return null;
    }

    @Override
    public byte[] exportExcel(String fromDate, String toDate, String keySearch) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            UserDTO user = userService.getAccount();
            boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
            Page<FAIncrementConvertDTO> convertDTOS = currentUserLoginAndOrg.map(securityDTO -> faIncrementRepository.getAllFAIncrementsSearch(null, currentUserLoginAndOrg.get().getOrg(), fromDate, toDate, keySearch, currentBook)).orElse(null);
            return ExcelUtils.writeToFile(convertDTOS.getContent(), ExcelConstant.FAIncrement.NAME, ExcelConstant.FAIncrement.HEADER, ExcelConstant.FAIncrement.FIELD, user);
        }
        return null;
    }
}
