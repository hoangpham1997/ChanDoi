package vn.softdreams.ebweb.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.GenCodeService;
import vn.softdreams.ebweb.service.MCAuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.UtilsService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.MessageDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.dto.UserSystemOption;
import vn.softdreams.ebweb.service.dto.cashandbank.MCAuditDTO;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.ExcelConstant;
import vn.softdreams.ebweb.service.util.ExcelUtils;
import vn.softdreams.ebweb.service.util.PdfUtils;
import vn.softdreams.ebweb.web.rest.dto.*;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing MCAudit.
 */

@Service
@Transactional
public class MCAuditServiceImpl implements MCAuditService {

    private final Logger log = LoggerFactory.getLogger(MCAuditServiceImpl.class);
    private final String PHIEN_SoLamViec = "PHIEN_SoLamViec";
    private final String TienVND = "VND";
    private final MCAuditRepository mCAuditRepository;
    private final MCPaymentRepository mcPaymentRepository;
    private final MCReceiptRepository mcReceiptRepository;
    private final RefVoucherRepository refVoucherRepository;
    private final UserService userService;
    private final GeneralLedgerServiceImpl generalLedgerServiceImpl;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final UtilsService utilsService;
    @Autowired
    GenCodeService genCodeService;
    @Autowired
    UtilsRepository utilsRepository;
    @Autowired
    GeneralLedgerRepository generalLedgerRepository;

    public MCAuditServiceImpl(MCAuditRepository mCAuditRepository, MCPaymentRepository mcPaymentRepository,
                              MCReceiptRepository mcReceiptRepository, RefVoucherRepository refVoucherRepository,
                              UserService userService, GeneralLedgerServiceImpl generalLedgerServiceImpl,
                              OrganizationUnitRepository organizationUnitRepository,
                              UtilsService utilsService) {
        this.mCAuditRepository = mCAuditRepository;
        this.mcPaymentRepository = mcPaymentRepository;
        this.mcReceiptRepository = mcReceiptRepository;
        this.refVoucherRepository = refVoucherRepository;
        this.userService = userService;
        this.generalLedgerServiceImpl = generalLedgerServiceImpl;
        this.organizationUnitRepository = organizationUnitRepository;
        this.utilsService = utilsService;
    }

    /**
     * Save a mCAudit.
     *
     * @param mCAudit the entity to save
     * @return the persisted entity
     */
    @Override
    public MCAudit save(MCAudit mCAudit) {
        Boolean isAdd = true;
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            mCAudit.setCompanyID(currentUserLoginAndOrg.get().getOrg());
        }
        if (mCAudit.getId() == null) {
            mCAudit.setId(UUID.randomUUID());
            mCAudit.setTemplateID(UUID.randomUUID());
        } else {
            isAdd = false;
        }
        for (MCAuditDetails x : mCAudit.getMcAuditDetails()) {
            if (isAdd) x.setId(null);
            x.setmCAuditId(mCAudit.getId());
        }
        for (MCAuditDetailMember x : mCAudit.getMcAuditDetailMembers()) {
            if (isAdd) x.setId(null);
            x.setmCAuditId(mCAudit.getId());
        }
        MCAudit mCAuditSave = new MCAudit();
        mCAuditSave = mCAuditRepository.save(mCAudit);
        // utilsRepository.updateGencode(mCAuditSave.getNo(),mCAudit.getNo(), 18, mCAuditSave.getTypeLedger());
        List<RefVoucher> refVouchers = new ArrayList<>();
        for (RefVoucherDTO item : mCAudit.getViewVouchers() != null ? mCAudit.getViewVouchers() : new ArrayList<RefVoucherDTO>()) {
            RefVoucher refVoucher = new RefVoucher();
            refVoucher.setCompanyID(mCAuditSave.getCompanyID());
            refVoucher.setRefID1(mCAuditSave.getId());
            refVoucher.setRefID2(item.getRefID2());
            item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            refVouchers.add(refVoucher);
        }
        refVoucherRepository.deleteByRefID1(mCAuditSave.getId());
        refVoucherRepository.saveAll(refVouchers);
        mCAuditSave.setViewVouchers(mCAudit.getViewVouchers());
        if (!isAdd) {
            MCReceipt mcReceipt = mcReceiptRepository.findByAuditID(mCAudit.getId());
            MCPayment mcPayment = mcPaymentRepository.findByAuditID(mCAudit.getId());
            if (mcReceipt != null) {
                mcReceiptRepository.deleteById(mcReceipt.getId());
            }
            if (mcPayment != null) {
                mcPaymentRepository.deleteById(mcPayment.getId());
            }
        }
        int res = mCAudit.getTotalAuditAmount().compareTo(mCAudit.getTotalBalanceAmount());
        if (res == 1) {
            MCReceipt mcReceipt = GetReceiptFromAudit(mCAudit);
            mcReceiptRepository.save(mcReceipt);
            // utilsRepository.updateGencode(mcReceipt.getNoFBook(), mcReceipt.getNoMBook(),10, mCAudit.getTypeLedger() == null ? 2 : mCAudit.getTypeLedger());
        } else if (res == -1) {
            MCPayment mcPayment = GetPaymentFromAudit(mCAudit);
            mcPaymentRepository.save(mcPayment);
            // utilsRepository.updateGencode(mcPayment.getNoFBook(),mcPayment.getNoMBook(), 11, mCAudit.getTypeLedger() == null ? 2 : mCAudit.getTypeLedger());
        }
        return mCAuditSave;
    }

    private MCPayment GetPaymentFromAudit(MCAudit mcAudit) {
        MCPayment mcPayment = new MCPayment();
        mcPayment.setId(UUID.randomUUID());
        mcPayment.setTypeID(110);
        mcPayment.setDate(mcAudit.getDate());
        mcPayment.setPostedDate(mcAudit.getAuditDate().toLocalDate());
        GenCode genCode = genCodeService.findWithTypeID(11, mcAudit.getTypeLedger() == null ? 2 : mcAudit.getTypeLedger());
        String codeVoucher = String.format("%1$s%2$s%3$s", genCode.getPrefix(), StringUtils.leftPad(String.valueOf(genCode.getCurrentValue() + 1), genCode.getLength(), '0'), genCode.getSuffix() == null ? "" : genCode.getSuffix());
        if (mcAudit.getTypeLedger() == 0) {
            mcPayment.setNoFBook(codeVoucher);
        } else {
            mcPayment.setNoMBook(codeVoucher);
        }
        mcPayment.setReason("Xử lý chênh lệch do kiểm kê thiếu quỹ");
        mcPayment.setCurrencyID(mcAudit.getCurrencyID());
        mcPayment.setExchangeRate(mcAudit.getExchangeRate());
        mcPayment.setCompanyID(mcAudit.getCompanyID());
        mcPayment.setTypeLedger(mcAudit.getTypeLedger());
        mcPayment.setTotalAmount((mcAudit.getDifferAmount().abs()).multiply(mcAudit.getExchangeRate()));
        mcPayment.setTotalAmountOriginal(mcAudit.getDifferAmount().abs());
        mcPayment.setTotalVATAmount(BigDecimal.ZERO);
        mcPayment.setTotalVATAmountOriginal(BigDecimal.ZERO);
        mcPayment.setIsImportPurchase(false);
        mcPayment.setRecorded(true);
        mcPayment.setTemplateID(null);
        mcPayment.setmCAuditID(mcAudit.getId());
        Set<MCPaymentDetails> lstDetail = new HashSet<>();
        MCPaymentDetails detail = new MCPaymentDetails();
        detail.setmCPaymentID(mcPayment.getId());
        detail.setDescription("Xử lý chênh lệch do kiểm kê thiếu quỹ");
        detail.setDebitAccount("1381");
        if (mcPayment.getCurrencyID().equals(TienVND)) {
            detail.setCreditAccount("1111");
        } else {
            detail.setCreditAccount("1112");
        }
        detail.setAmount((mcAudit.getDifferAmount().abs()).multiply(mcAudit.getExchangeRate()));
        detail.setAmountOriginal(mcAudit.getDifferAmount().abs());
        lstDetail.add(detail);
        mcPayment.setMCPaymentDetails(lstDetail);
        return mcPayment;
    }

    private MCReceipt GetReceiptFromAudit(MCAudit mcAudit) {
        MCReceipt mcReceipt = new MCReceipt();
        mcReceipt.setId(UUID.randomUUID());
        mcReceipt.setTypeID(100);
        mcReceipt.setDate(mcAudit.getDate());
        mcReceipt.setPostedDate(mcAudit.getAuditDate().toLocalDate());
        GenCode genCode = genCodeService.findWithTypeID(10, mcAudit.getTypeLedger() == null ? 2 : mcAudit.getTypeLedger());
        String codeVoucher = String.format("%1$s%2$s%3$s", genCode.getPrefix(), StringUtils.leftPad(String.valueOf(genCode.getCurrentValue() + 1), genCode.getLength(), '0'), genCode.getSuffix() == null ? "" : genCode.getSuffix());
        if (mcAudit.getTypeLedger() == 0) {
            mcReceipt.setNoFBook(codeVoucher);
        } else {
            mcReceipt.setNoMBook(codeVoucher);
        }
        mcReceipt.setReason("Xử lý chênh lệch do kiểm kê thừa quỹ");
        mcReceipt.setCurrencyID(mcAudit.getCurrencyID());
        mcReceipt.setExchangeRate(mcAudit.getExchangeRate());
        mcReceipt.setCompanyID(mcAudit.getCompanyID());
        mcReceipt.setTypeLedger(mcAudit.getTypeLedger());
        mcReceipt.setTotalAmount((mcAudit.getDifferAmount().abs()).multiply(mcAudit.getExchangeRate()));
        mcReceipt.setTotalAmountOriginal(mcAudit.getDifferAmount());
        mcReceipt.setTotalVATAmount(BigDecimal.ZERO);
        mcReceipt.setTotalVATAmountOriginal(BigDecimal.ZERO);
        mcReceipt.setExported(false);
        mcReceipt.setRecorded(true);
        mcReceipt.setTemplateID(null);
        mcReceipt.setmCAuditID(mcAudit.getId());
        Set<MCReceiptDetails> lstDetail = new HashSet<>();
        MCReceiptDetails detail = new MCReceiptDetails();
        detail.setmCReceiptID(mcReceipt.getId());
        detail.setDescription("Xử lý chênh lệch do kiểm kê thừa quỹ");
        detail.setCreaditAccount("3381");
        if (mcReceipt.getCurrencyID().equals(TienVND)) {
            detail.setDebitAccount("1111");
        } else {
            detail.setDebitAccount("1112");
        }
        detail.setAmount((mcAudit.getDifferAmount().abs()).multiply(mcAudit.getExchangeRate()));
        detail.setAmountOriginal(mcAudit.getDifferAmount());
        lstDetail.add(detail);
        mcReceipt.setMCReceiptDetails(lstDetail);
        return mcReceipt;
    }

    /**
     * Get all the mCAudits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MCAudit> findAll(Pageable pageable) {
        log.debug("Request to get all MCAudits");
        return mCAuditRepository.findAll(pageable);
    }


    /**
     * Get one mCAudit by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MCAudit> findOne(UUID id) {
        Optional<MCAudit> mcAudit = mCAuditRepository.findById(id);
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            boolean isNoMBook = !collect.isEmpty() && collect.get(0).getCode().equalsIgnoreCase("1");
            List<RefVoucherDTO> dtos = refVoucherRepository.getRefViewVoucher(id, isNoMBook);
            mcAudit.get().setViewVouchers(dtos);
        }
        return mcAudit;
    }

    /**
     * Delete the mCAudit by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MCAudit : {}", id);
        MCReceipt mcReceipt = mcReceiptRepository.findByAuditID(id);
        MCPayment mcPayment = mcPaymentRepository.findByAuditID(id);
        MessageDTO messageDTO = new MessageDTO();
        if (mcReceipt != null) {
            if (Boolean.TRUE.equals(mcReceipt.isRecorded())) {
                generalLedgerServiceImpl.unrecord(mcReceipt.getId(), null);
            }
            mcReceiptRepository.deleteById(mcReceipt.getId());
        }
        if (mcPayment != null) {
            if (Boolean.TRUE.equals(mcPayment.isRecorded())) {
                generalLedgerServiceImpl.unrecord(mcPayment.getId(), null);
            }
            mcPaymentRepository.deleteById(mcPayment.getId());
        }
        mCAuditRepository.deleteById(id);
    }

    /**
     * Save a mCAudit.
     *
     * @param mCAudit the entity to save
     * @return the persisted entity
     */
    @Override
    public MCAuditSaveDTO saveDTO(MCAudit mCAudit) {
        log.debug("Request to save MCReceipt : {}", mCAudit);
        MCAudit mCAuditSave = new MCAudit();
        MCAuditSaveDTO mcAuditSaveDTO = new MCAuditSaveDTO();
        UserDTO userDTO = userService.getAccount();
        int res = mCAudit.getTotalAuditAmount().compareTo((mCAudit.getTotalBalanceAmount() != null ? mCAudit.getTotalBalanceAmount() : BigDecimal.ZERO));
        if (mCAudit.getId() == null && res != 0 && !utilsService.checkQuantityLimitedNoVoucher()) {
            throw new BadRequestAlertException("Số chứng từ hiện tại đã vượt quá số cho phép của Gói sử dụng", "", "noVoucherLimited");
        }
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (!utilsRepository.checkDuplicateNoVoucher(mCAudit.getNo(), mCAudit.getNo(), mCAudit.getTypeLedger(), mCAudit.getId())) {
            mcAuditSaveDTO.setMcAudit(mCAudit);
            mcAuditSaveDTO.setStatus(1);
            return mcAuditSaveDTO;
        }
        Boolean isAdd = true;
        if (currentUserLoginAndOrg.isPresent()) {
            mCAudit.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            if (mCAudit.getId() == null) {
                mCAudit.setId(UUID.randomUUID());
                mCAudit.setTemplateID(UUID.randomUUID());
            } else {
                isAdd = false;
            }
            for (MCAuditDetails x : mCAudit.getMcAuditDetails()) {
                if (isAdd) x.setId(null);
                x.setmCAuditId(mCAudit.getId());
            }
            for (MCAuditDetailMember x : mCAudit.getMcAuditDetailMembers()) {
                if (isAdd) x.setId(null);
                x.setmCAuditId(mCAudit.getId());
            }
            if (!isAdd) {
                MCReceipt mcReceipt = mcReceiptRepository.findByAuditID(mCAudit.getId());
                MCPayment mcPayment = mcPaymentRepository.findByAuditID(mCAudit.getId());
                if (mcReceipt != null) {
                    mcReceiptRepository.deleteById(mcReceipt.getId());
                }
                if (mcPayment != null) {
                    mcPaymentRepository.deleteById(mcPayment.getId());
                }
            }
            if (res == 1) {
                MCReceipt mcReceipt = GetReceiptFromAudit(mCAudit);
                MCReceipt mcReceiptSave = mcReceiptRepository.save(mcReceipt);
//                utilsRepository.updateGencode(mcReceipt.getNoFBook(), mcReceipt.getNoMBook(),10, mCAudit.getTypeLedger() == null ? 2 : mCAudit.getTypeLedger());
                if (mcReceiptSave != null && Boolean.TRUE.equals(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_GhiSo)).findAny().get().getData().equals("0"))) {
                    if (generalLedgerServiceImpl.record(mcReceiptSave, new MessageDTO(""))) {
                        mcReceiptSave.setRecorded(true);
                        mcReceiptRepository.save(mcReceiptSave);
                    }
                }
            } else if (res == -1) {
                MCPayment mcPayment = GetPaymentFromAudit(mCAudit);
                MCPayment mcPaymentSave = mcPaymentRepository.save(mcPayment);
//                utilsRepository.updateGencode(mcPayment.getNoFBook(),mcPayment.getNoMBook(), 11, mCAudit.getTypeLedger() == null ? 2 : mCAudit.getTypeLedger());
                if (mcPaymentSave != null && Boolean.TRUE.equals(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_GhiSo)).findAny().get().getData().equals("0"))) {
                    if (generalLedgerServiceImpl.record(mcPaymentSave, new MessageDTO(""))) {
                        mcPaymentSave.setRecorded(true);
                        mcPaymentRepository.save(mcPaymentSave);
                    }
                }
            }
            mCAuditSave = mCAuditRepository.save(mCAudit);
            utilsRepository.updateGencode(mCAuditSave.getNo(), mCAudit.getNo(), 18, 2);
            List<RefVoucher> refVouchers = new ArrayList<>();
            for (RefVoucherDTO item : mCAudit.getViewVouchers() != null ? mCAudit.getViewVouchers() : new ArrayList<RefVoucherDTO>()) {
                RefVoucher refVoucher = new RefVoucher();
                refVoucher.setCompanyID(mCAuditSave.getCompanyID());
                refVoucher.setRefID1(mCAuditSave.getId());
                refVoucher.setRefID2(item.getRefID2());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                refVouchers.add(refVoucher);
            }
            refVoucherRepository.deleteByRefID1(mCAuditSave.getId());
            refVoucherRepository.saveAll(refVouchers);
            mCAuditSave.setViewVouchers(mCAudit.getViewVouchers());
            mcAuditSaveDTO.setMcAudit(mCAuditSave);
            mcAuditSaveDTO.setStatus(0);
            return mcAuditSaveDTO;
        }
        throw new BadRequestAlertException("Không thể lưu kiểm kê quỹ", "", "");
    }

    @Override
    public Page<MCAuditDTO> findAllDTOByCompanyID(Pageable pageable) {
        log.debug("Request to get all MCReceiptsDTO by CompanyID");
        UserDTO userDTO = userService.getAccount();
        return mCAuditRepository.findAllDTOByCompanyID(pageable, userDTO.getOrganizationUnit().getId(), Integer.valueOf(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(PHIEN_SoLamViec)).findAny().get().getData()));
    }

    @Override
    public byte[] exportExcel(Pageable pageable, String currencyID, String fromDate, String toDate, String textSearch) {
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            Page<MCAuditDTO> saInvoice = mCAuditRepository.searchMCAudit(pageable, currencyID, fromDate,
                toDate, textSearch, currentUserLoginAndOrg.get().getOrg(), currentBook, true);
            return ExcelUtils.writeToFile(saInvoice.getContent(), ExcelConstant.MCAudit.NAME, ExcelConstant.MCAudit.HEADER, ExcelConstant.MCAudit.FIELD, userDTO);
        }
        return null;
    }

    @Override
    public byte[] exportPdf(Pageable pageable, String currencyID, String fromDate, String toDate, String textSearch) {
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            Page<MCAuditDTO> saInvoice = mCAuditRepository.searchMCAudit(pageable, currencyID, fromDate,
                toDate, textSearch, currentUserLoginAndOrg.get().getOrg(), currentBook, true);
            return PdfUtils.writeToFile(saInvoice.getContent(), ExcelConstant.MCAudit.HEADER, ExcelConstant.MCAudit.FIELD, userDTO);
        }
        return null;
    }

    @Override
    public Page<MCAuditDTO> searchMCAudit(Pageable pageable, String currencyID, String fromDate, String toDate, String keySearch) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            return mCAuditRepository.searchMCAudit(pageable, currencyID, fromDate,
                toDate, keySearch, currentUserLoginAndOrg.get().getOrg(), currentBook, false);
        }
        return null;
    }

    @Override
    public MCAudit findByRowNum(String currencyID, String fromDate, String toDate, String keySearch, Integer rowNum) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        String currentBook = "";
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
        }
        return mCAuditRepository.findByRowNum(currencyID, fromDate, toDate, keySearch, rowNum, currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    public MCAuditDetailDTO getMCAuditDetailsByID(UUID mCAuditID) {
        MCAuditDetailDTO mcAuditDetailDTO = new MCAuditDetailDTO();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            boolean isNoMBook = !collect.isEmpty() && collect.get(0).getCode().equalsIgnoreCase("1");
            List<MCAuditDetails> mcAuditDetails = mCAuditRepository.findDetailByMCAuditID(mCAuditID);
            if (mcAuditDetails.size() > 0) {
                mcAuditDetailDTO.setMcAuditDetails(mcAuditDetails);
            }
            List<MCAuditDetailMember> mcAuditDetailMembers = mCAuditRepository.findDetailMemberByMCAuditID(mCAuditID);
            if (mcAuditDetailMembers.size() > 0) {
                mcAuditDetailDTO.setMcAuditDetailMembers(mcAuditDetailMembers);
            }
            List<RefVoucherDTO> refVoucherDTOS = refVoucherRepository.getRefViewVoucher(mCAuditID, isNoMBook);
            if (refVoucherDTOS.size() > 0) {
                mcAuditDetailDTO.setRefVouchers(refVoucherDTOS);
            }
        }
        return mcAuditDetailDTO;
    }

    @Override
    public HandlingResultDTO multiDelete(List<MCAudit> mcAudits) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        handlingResultDTO.setCountTotalVouchers(mcAudits.size());
        List<MCAudit> listDelete = mcAudits.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();

        // get ListID chung tu theo Type ID
        List<UUID> uuidListMCReceipt = new ArrayList<>();
        List<UUID> uuidListMCPayment = new ArrayList<>();
        List<UUID> uuidListMCAudit = new ArrayList<>();
        for (int i = 0; i < listDelete.size(); i++) {
            if (listDelete.get(i).getDifferAmount().floatValue() > 0) {
                uuidListMCReceipt.add(listDelete.get(i).getId());
            } else if (listDelete.get(i).getDifferAmount().floatValue() < 0) {
                uuidListMCPayment.add(listDelete.get(i).getId());
            }
            uuidListMCAudit.add(listDelete.get(i).getId());
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(listDelete.size() - viewVoucherNoListFail.size());
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        // Xoa chung tu
        if (uuidListMCAudit.size() > 0) {
            mCAuditRepository.multiDeleteMCAudit(currentUserLoginAndOrg.get().getOrg(), uuidListMCAudit);
            mCAuditRepository.multiDeleteMCAuditChild("MCAuditDetail", uuidListMCAudit);
            mCAuditRepository.multiDeleteMCAuditChild("MCAuditDetailMember", uuidListMCAudit);
            refVoucherRepository.deleteByRefID1sOrRefID2s(uuidListMCAudit);
        }
        if (uuidListMCPayment.size() > 0) {
            mcPaymentRepository.multiDeleteMCPayment(currentUserLoginAndOrg.get().getOrg(), uuidListMCPayment);
            // refVoucherRepository.deleteByRefID1sOrRefID2s(uuidListMCAudit);
        }
        if (uuidListMCReceipt.size() > 0) {
            mcReceiptRepository.multiDeleteMCReceipt(currentUserLoginAndOrg.get().getOrg(), uuidListMCReceipt);
            // refVoucherRepository.deleteByRefID1sOrRefID2s(uuidListMCAudit);
        }
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        // Xoa chung tu nop tien tu ban hang
        return handlingResultDTO;
    }

    @Override
    public void edit(UUID id) {
        MCReceipt mcReceipt = mcReceiptRepository.findByAuditID(id);
        MCPayment mcPayment = mcPaymentRepository.findByAuditID(id);
        MessageDTO messageDTO = new MessageDTO();
        if (mcReceipt != null) {
            if (Boolean.TRUE.equals(mcReceipt.isRecorded())) {
                generalLedgerServiceImpl.unrecord(mcReceipt.getId(), null);
            }
            mcReceiptRepository.deleteById(mcReceipt.getId());
        }
        if (mcPayment != null) {
            if (Boolean.TRUE.equals(mcPayment.isRecorded())) {
                generalLedgerServiceImpl.unrecord(mcPayment.getId(), null);
            }
            mcPaymentRepository.deleteById(mcPayment.getId());
        }
    }
}
