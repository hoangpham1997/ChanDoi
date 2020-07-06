package vn.softdreams.ebweb.service.impl;

import com.google.common.base.Functions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.repository.RefVoucherRepository;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.*;
import vn.softdreams.ebweb.repository.CPExpenseTranferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.dto.cashandbank.GOtherVoucherExportDTO;
import vn.softdreams.ebweb.service.util.*;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CPExpenseTranfer.
 */
@Service
@Transactional
public class CPExpenseTranferServiceImpl implements CPExpenseTranferService {

    private final Logger log = LoggerFactory.getLogger(CPExpenseTranferServiceImpl.class);
    private final CPExpenseTranferRepository cPExpenseTranferRepository;
    private final UserService userService;
    private final RefVoucherRepository refVoucherRepository;
    private final String PHIEN_SoLamViec = "PHIEN_SoLamViec";
    private final String TCKHAC_GhiSo = "TCKHAC_GhiSo";
    private final GenCodeService genCodeService;
    private final GeneralLedgerService generalLedgerService;
    private final UtilsService utilsService;
    private final OrganizationUnitRepository organizationUnitRepository;

    @Autowired
    UtilsRepository utilsRepository;

    public CPExpenseTranferServiceImpl(CPExpenseTranferRepository cPExpenseTranferRepository, UserService userService, RefVoucherRepository refVoucherRepository, GenCodeService genCodeService, GeneralLedgerService generalLedgerService, UtilsService utilsService, OrganizationUnitRepository organizationUnitRepository) {
        this.cPExpenseTranferRepository = cPExpenseTranferRepository;
        this.userService = userService;
        this.refVoucherRepository = refVoucherRepository;
        this.genCodeService = genCodeService;
        this.generalLedgerService = generalLedgerService;
        this.utilsService = utilsService;
        this.organizationUnitRepository = organizationUnitRepository;
    }

    /**
     * Save a cPExpenseTranfer.
     *
     * @param cPExpenseTranfer the entity to save
     * @return the persisted entity
     */
    @Override
    public CPExpenseTranfer save(CPExpenseTranfer cPExpenseTranfer) {
        log.debug("Request to save CPExpenseTranfer : {}", cPExpenseTranfer);
        return cPExpenseTranferRepository.save(cPExpenseTranfer);
    }

    /**
     * Get all the cPExpenseTranfers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CPExpenseTranfer> findAll(Pageable pageable) {
        log.debug("Request to get all CPExpenseTranfers");
        return cPExpenseTranferRepository.findAll(pageable);
    }


    /**
     * Get one cPExpenseTranfer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CPExpenseTranfer> findOne(UUID id) {
        log.debug("Request to get MBDeposit : {}", id);
        Optional<CPExpenseTranfer> cpExpenseTranfer = cPExpenseTranferRepository.findById(id);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");
            List<RefVoucherDTO> dtos = refVoucherRepository.getRefViewVoucher(id, isNoMBook);
            cpExpenseTranfer.get().setViewVouchers(dtos);
        }
//        Object object = mBDepositRepository.findIDRef(id);
        return cpExpenseTranfer;
    }

    /**
     * Delete the cPExpenseTranfer by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete CPExpenseTranfer : {}", id);
        cPExpenseTranferRepository.deleteById(id);
    }

    @Override
    public List<KetChuyenChiPhiDTO> getCPExpenseTransferByCPPeriodID(UUID cPPeriodID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return cPExpenseTranferRepository.getCPExpenseTransferByCPPeriodID(currentUserLoginAndOrg.get().getOrg(), cPPeriodID);
        }
        throw new BadRequestAlertException("", "", "");
    }


    @Override
    public Page<CPExpenseTranfer> findAll(Pageable pageable, SearchVoucher searchVoucher) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return cPExpenseTranferRepository.findAll(pageable, searchVoucher, currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    public CPExpenseTranferSaveDTO saveDTO(CPExpenseTranfer cpExpenseTranfer) {
        log.debug("Request to save CPExpenseTranfer : {}", cpExpenseTranfer);
        CPExpenseTranfer cPET = new CPExpenseTranfer();
        CPExpenseTranferSaveDTO cpExpenseTranferSaveDTO = new CPExpenseTranferSaveDTO();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            if (cpExpenseTranfer.getId() == null) {
                if (!utilsService.checkQuantityLimitedNoVoucher()) {
                    throw new BadRequestAlertException("Số chứng từ hiện tại đã vượt quá số cho phép của Gói sử dụng", "", "noVoucherLimited");
                }
            }
            UserDTO userDTO = userService.getAccount();
            if (cpExpenseTranfer.getTypeLedger() == Constants.TypeLedger.BOTH_BOOK) {
                if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(PHIEN_SoLamViec)).findAny().get().getData().equals("0")) {
                    if (StringUtils.isEmpty(cpExpenseTranfer.getNoMBook())) {
                        cpExpenseTranfer.setNoMBook(genCodeService.getCodeVoucher(TypeGroupConstant.PHAN_BO_CHI_PHI_TRA_TRUOC, Constants.TypeLedger.MANAGEMENT_BOOK));
                    }
                } else {
                    if (StringUtils.isEmpty(cpExpenseTranfer.getNoFBook())) {
                        cpExpenseTranfer.setNoFBook(genCodeService.getCodeVoucher(TypeGroupConstant.PHAN_BO_CHI_PHI_TRA_TRUOC, Constants.TypeLedger.FINANCIAL_BOOK));
                    }
                }
            }
            if (!utilsRepository.checkDuplicateNoVoucher(cpExpenseTranfer.getNoFBook(), cpExpenseTranfer.getNoMBook(), cpExpenseTranfer.getTypeLedger(), cpExpenseTranfer.getId())) {
                cpExpenseTranferSaveDTO.setCpExpenseTranfer(cpExpenseTranfer);
                cpExpenseTranferSaveDTO.setStatus(1);
                return cpExpenseTranferSaveDTO;
            }
            cpExpenseTranfer.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            cPET = cPExpenseTranferRepository.save(cpExpenseTranfer);
//            utilsRepository.updateGencode(cPET.getNoFBook(), cPET.getNoMBook(), 70, cPET.getTypeLedger() == null ? 2 : cPET.getTypeLedger());
            List<RefVoucher> refVouchers = new ArrayList<>();
            for (RefVoucherDTO item : cpExpenseTranfer.getViewVouchers() != null ? cpExpenseTranfer.getViewVouchers() : new ArrayList<RefVoucherDTO>()) {
                RefVoucher refVoucher = new RefVoucher();
                refVoucher.setCompanyID(cPET.getCompanyID());
                refVoucher.setRefID1(cPET.getId());
                refVoucher.setRefID2(item.getRefID2());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                refVouchers.add(refVoucher);
            }
            refVoucherRepository.deleteByRefID1(cPET.getId());
            refVoucherRepository.deleteByRefID2(cPET.getId());
            refVoucherRepository.saveAll(refVouchers);
            cPET.setViewVouchers(cpExpenseTranfer.getViewVouchers());
            MessageDTO messageDTO = new MessageDTO();
            if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {
                if (!generalLedgerService.record(cPET, messageDTO)) {
                    cpExpenseTranferSaveDTO.setStatus(2);
                    cpExpenseTranferSaveDTO.setMsg(messageDTO.getMsgError());
                    cPET.setRecorded(false);
                    cPExpenseTranferRepository.save(cPET);
                } else {
                    cpExpenseTranferSaveDTO.setStatus(0);
                    cPET.setRecorded(true);
                    cPExpenseTranferRepository.save(cPET);
                }
            } else {
                cpExpenseTranferSaveDTO.setStatus(0);
                if (cPET.getRecorded() == null) {
                    cPET.setRecorded(false);
                    cPExpenseTranferRepository.save(cPET);
                }
//                mcP.setRecorded(false);
            }
            cpExpenseTranferSaveDTO.setCpExpenseTranfer(cPET);
            return cpExpenseTranferSaveDTO;
        }
        throw new BadRequestAlertException("Không thể lưu phiếu thu", "", "");
    }

    @Override
    public void checkExistCPPeriodID(UUID cPPeriodID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            Integer count = cPExpenseTranferRepository.checkExist(currentUserLoginAndOrg.get().getOrg(), cPPeriodID);
            if (count > 0) {
                throw new BadRequestAlertException("Đã tồn tại kỳ tính giá thành", "", "existCPPeriod");
            }
        }
    }

    @Override
    public byte[] exportPDF(SearchVoucher searchVoucher) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        Page<CPExpenseTranferExportDTO> cpExpenseTranferExportDTOS = currentUserLoginAndOrg.map(securityDTO -> cPExpenseTranferRepository.getAllCPExpenseTranfers(null, searchVoucher,
            securityDTO.getOrg(), currentBook)).orElse(null);
        return PdfUtils.writeToFile(cpExpenseTranferExportDTOS.getContent(), ExcelConstant.CPExpenseTranfer.HEADER, ExcelConstant.CPExpenseTranfer.FIELD);
    }

    @Override
    public byte[] exportExcel(SearchVoucher searchVoucher) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        Page<CPExpenseTranferExportDTO> cpExpenseTranferExportDTOS = currentUserLoginAndOrg.map(securityDTO -> cPExpenseTranferRepository.getAllCPExpenseTranfers(null, searchVoucher,
            securityDTO.getOrg(), currentBook)).orElse(null);
        return PdfUtils.writeToFile(cpExpenseTranferExportDTOS.getContent(), ExcelConstant.CPExpenseTranfer.HEADER, ExcelConstant.CPExpenseTranfer.FIELD);
    }

    @Override
    public HandlingResultDTO multiUnRecord(List<CPExpenseTranfer> cpExpenseTranfers) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        handlingResultDTO.setCountTotalVouchers(cpExpenseTranfers.size());
        List<CPExpenseTranfer> listDelete = cpExpenseTranfers.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        org.joda.time.LocalDate dateClosed = org.joda.time.LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (!closeDateStr.equals("") && closeDateStr != null) {
            dateClosed = org.joda.time.LocalDate.parse(closeDateStr);
        }
        for (CPExpenseTranfer mc : cpExpenseTranfers) {
            org.joda.time.LocalDate postedDate = org.joda.time.LocalDate.parse(mc.getPostedDate().toString());
            if (Boolean.TRUE.equals(mc.isRecorded()) && !closeDateStr.equals("") && closeDateStr != null && dateClosed != null && (dateClosed.isAfter(postedDate) || dateClosed.equals(postedDate))) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đã khóa sổ!");
                viewVoucherNo.setPostedDate(mc.getPostedDate());
                viewVoucherNo.setDate(mc.getDate());
                viewVoucherNo.setNoFBook(mc.getNoFBook());
                viewVoucherNo.setNoMBook(mc.getNoMBook());
                if (mc.getTypeID() == TypeConstant.KET_CHUYEN_CHI_PHI) {
                    viewVoucherNo.setTypeName("Kết chuyển chi phí");
                }
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(mc);
            } else if (Boolean.FALSE.equals(mc.isRecorded())) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang bỏ ghi sổ !");
                BeanUtils.copyProperties(mc, viewVoucherNo);
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(mc);
            }
        }

        for (ViewVoucherNo v : viewVoucherNoListFail) {
            if (v.getTypeID() == TypeConstant.KET_CHUYEN_CHI_PHI) {
                v.setTypeName("Kết chuyển chi phí");
            }
        }
        List<UUID> uuidList_KCCP = listDelete.stream().map(a -> a.getId()).collect(Collectors.toList());

        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(listDelete.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();

        if (uuidList_KCCP.size() > 0) {
            cPExpenseTranferRepository.multiUnRecord(uuidList_KCCP, currentUserLoginAndOrg.get().getOrg());
            cPExpenseTranferRepository.deleteGL(uuidList_KCCP, currentUserLoginAndOrg.get().getOrg());
        }
        return handlingResultDTO;
    }

    @Override
    public HandlingResultDTO multiDelete(List<CPExpenseTranfer> cpExpenseTranfers) {

        UserDTO userDTO = userService.getAccount();
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        handlingResultDTO.setCountTotalVouchers(cpExpenseTranfers.size());
        List<CPExpenseTranfer> listDelete = cpExpenseTranfers.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        org.joda.time.LocalDate dateClosed = org.joda.time.LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (!closeDateStr.equals("") && closeDateStr != null) {
            dateClosed = org.joda.time.LocalDate.parse(closeDateStr);
        }
        List<UUID> uuidListDelete = new ArrayList<>();
        for (CPExpenseTranfer md : cpExpenseTranfers) {
            org.joda.time.LocalDate postedDate = org.joda.time.LocalDate.parse(md.getPostedDate().toString());
            if (Boolean.TRUE.equals(md.isRecorded()) && !closeDateStr.equals("") && closeDateStr != null && dateClosed != null && dateClosed.isAfter(postedDate)) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đã khóa sổ!");
                BeanUtils.copyProperties(md, viewVoucherNo);
                if (md.getTypeID() == TypeConstant.KET_CHUYEN_CHI_PHI) {
                    viewVoucherNo.setTypeName("Kết chuyển chi phí");
                }
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(md);
            } else if (Boolean.TRUE.equals(md.isRecorded())) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang ghi sổ !");
                BeanUtils.copyProperties(md, viewVoucherNo);
                if (md.getTypeID() == TypeConstant.KET_CHUYEN_CHI_PHI) {
                    viewVoucherNo.setTypeName("Kết chuyển chi phí");
                }
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(md);
            }
        }
        if (listDelete.size() == 0) {
            throw new BadRequestAlertException("Các chứng từ đang được ghi sổ, không thể xoá", "mBDeposit", "errorDeleteList");
        }
        // get ListID chung tu theo Type ID
        List<UUID> uuidList_KCCP = listDelete.stream().map(a -> a.getId()).collect(Collectors.toList());

        // Gan' TypeName
        for (int i = 0; i < viewVoucherNoListFail.size(); i++) {
            viewVoucherNoListFail.get(i).setTypeName("Kết chuyển cih phí");
        }

        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(uuidList_KCCP.size());
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        // Xoa chung tu
        if (uuidList_KCCP.size() > 0) {
            cPExpenseTranferRepository.multiDeleteCPExpenseTranfer(currentUserLoginAndOrg.get().getOrg(), uuidList_KCCP);
            cPExpenseTranferRepository.multiDeleteCPExpenseTranferDetails(currentUserLoginAndOrg.get().getOrg(), uuidList_KCCP);
            refVoucherRepository.deleteByRefID1sOrRefID2s(uuidList_KCCP);
        }
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        // Xoa chung tu nop tien tu ban hang
        return handlingResultDTO;
    }
}
