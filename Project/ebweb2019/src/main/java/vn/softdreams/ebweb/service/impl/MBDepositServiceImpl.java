package vn.softdreams.ebweb.service.impl;

import com.google.common.base.Functions;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.dto.cashandbank.MBDepositExportDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MBDepositSaveDTO;
import vn.softdreams.ebweb.service.util.*;
import vn.softdreams.ebweb.web.rest.dto.MBDepositViewDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing MBDeposit.
 */
@Service
@Transactional
public class MBDepositServiceImpl implements MBDepositService {

    private final Logger log = LoggerFactory.getLogger(MBDepositServiceImpl.class);

    private final MBDepositRepository mBDepositRepository;
    private final MCReceiptRepository mcReceiptRepository;
    private final RSInwardOutwardRepository rsInwardOutwardRepository;

    private final MBDepositDetailsRepository mbDepositDetailsRepository;
    private final GeneralLedgerRepository generalLedgerRepository;
    private final GeneralLedgerService generalLedgerService;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final TypeRepository typeRepository;
    private final SAInvoiceRepository saInvoiceRepository;
    private final SAOrderRepository saOrderRepository;
    private final UtilsService utilsService;
    private final String TCKHAC_GhiSo = "TCKHAC_GhiSo";

//    private final MBDepositDetailTaxRepository mbDepositDetailTaxRepository;

    private final RefVoucherRepository refVoucherRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final GenCodeService genCodeService;
    private final String PHIEN_SoLamViec = "PHIEN_SoLamViec";
    private final RSInwardOutWardDetailsRepository rsInwardOutWardDetailsRepository;
    private final SaReturnDetailsRepository saReturnDetailsRepository;
    private final RepositoryLedgerRepository repositoryLedgerRepository;
    @Autowired
    UtilsRepository utilsRepository;

    public MBDepositServiceImpl(MBDepositRepository mBDepositRepository,
                                MBDepositDetailsRepository mbDepositDetailsRepository,
                                RefVoucherRepository refVoucherRepository,
                                UserService userService,
                                UserRepository userRepository,
                                GenCodeService genCodeService,
                                GeneralLedgerService generalLedgerService,
                                GeneralLedgerRepository generalLedgerRepository,
                                OrganizationUnitRepository organizationUnitRepository,
                                MCReceiptRepository mcReceiptRepository,
                                RSInwardOutwardRepository rsInwardOutwardRepository,
                                TypeRepository typeRepository, SAInvoiceRepository saInvoiceRepository,
                                SAOrderRepository saOrderRepository, IaPublishInvoiceRepository iaPublishInvoiceRepository, UtilsService utilsService,
                                RSInwardOutWardDetailsRepository rsInwardOutWardDetailsRepository, SaReturnDetailsRepository saReturnDetailsRepository,
                                RepositoryLedgerRepository repositoryLedgerRepository) {
        this.mBDepositRepository = mBDepositRepository;
        this.mbDepositDetailsRepository = mbDepositDetailsRepository;
//        this.mbDepositDetailTaxRepository = mbDepositDetailTaxRepository;
        this.refVoucherRepository = refVoucherRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.genCodeService = genCodeService;
        this.generalLedgerService = generalLedgerService;
        this.generalLedgerRepository = generalLedgerRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.mcReceiptRepository = mcReceiptRepository;
        this.rsInwardOutwardRepository = rsInwardOutwardRepository;
        this.typeRepository = typeRepository;
        this.saInvoiceRepository = saInvoiceRepository;
        this.saOrderRepository = saOrderRepository;
        this.utilsService = utilsService;
        this.rsInwardOutWardDetailsRepository = rsInwardOutWardDetailsRepository;
        this.saReturnDetailsRepository = saReturnDetailsRepository;
        this.repositoryLedgerRepository = repositoryLedgerRepository;
    }

    /**
     * Save a mBDeposit.
     *
     * @param mBDeposit the entity to save
     * @return the persisted entity
     */
    @Override
    public MBDeposit save(MBDeposit mBDeposit) {
        log.debug("Request to save MBDeposit : {}", mBDeposit);
        MBDeposit mBD = new MBDeposit();
        if (mBDeposit.getId() == null) {
            mBDeposit.setId(UUID.randomUUID());
            //gan id cha cho details con
            for (MBDepositDetails details : mBDeposit.getmBDepositDetails()) {
                if (details.getId() != null) {
                    details.setId(null);
                }
                details.setmBDepositID(mBDeposit.getId());
            }
//            for (MBDepositDetailTax detailTax : mBDeposit.getmBDepositDetailTax()) {
//                if (detailTax.getId() != null) {
//                    detailTax.setId(null);
//                }
//                detailTax.setmBDepositID(mBDeposit.getId());
//            }
        }
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            mBDeposit.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            if (!utilsService.checkQuantityLimitedNoVoucher()) {
                throw new BadRequestAlertException("Số chứng từ hiện tại đã vượt quá số cho phép của Gói sử dụng", "", "noVoucherLimited");
            }
            mBD = mBDepositRepository.save(mBDeposit);
//            utilsRepository.updateGencode(mBD.getNoFBook(), mBD.getNoMBook(), 16, mBD.getTypeLedger() == null ? 0 : mBD.getTypeLedger());
            List<RefVoucher> refVouchers = new ArrayList<>();
            for (RefVoucherDTO item : mBDeposit.getViewVouchers() != null ? mBDeposit.getViewVouchers() : new ArrayList<RefVoucherDTO>()) {
                RefVoucher refVoucher = new RefVoucher();
                refVoucher.setCompanyID(mBD.getCompanyID());
                refVoucher.setRefID1(mBD.getId());
                refVoucher.setRefID2(item.getRefID2());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                refVouchers.add(refVoucher);
            }
            refVoucherRepository.deleteByRefID1(mBD.getId());
            refVoucherRepository.deleteByRefID2(mBD.getId());
            refVoucherRepository.saveAll(refVouchers);
            mBD.setViewVouchers(mBDeposit.getViewVouchers());
            return mBD;
        }
        throw new BadRequestAlertException("Không thể lưu báo có ! ", "", "");
    }

    /**
     * Get all the mBDeposits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MBDeposit> findAll(Pageable pageable) {
        log.debug("Request to get all MBDeposits");
        return mBDepositRepository.findAll(pageable);
    }


    /**
     * Get one mBDeposit by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MBDeposit> findOne(UUID id) {
        log.debug("Request to get MBDeposit : {}", id);
        Optional<MBDeposit> mbDeposit = mBDepositRepository.findById(id);
        if (mbDeposit.get().getTypeID().equals(Constants.MBDeposit.TYPE_NOP_TIEN_TU_BAN_HANG)) {
            Object object = mBDepositRepository.findIDRef(id);
            mbDeposit.get().setsAInvoiceID(Utils.uuidConvertToGUID(UUID.fromString(((Object[]) object)[0].toString())));
            if (((Object[]) object)[1] != null) {
                mbDeposit.get().setrSInwardOutwardID(Utils.uuidConvertToGUID(UUID.fromString(((Object[]) object)[1].toString())));
            }
            mbDeposit.get().setTypeIDSAInvoice(Integer.valueOf(((Object[]) object)[2].toString()));
        } else if (mbDeposit.get().getTypeID().equals(Constants.MBDeposit.TYPE_BAO_CO)) {
            Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
            Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
            if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
                String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
                boolean isNoMBook = currentBook.equalsIgnoreCase("1");
                List<RefVoucherDTO> dtos = refVoucherRepository.getRefViewVoucher(id, isNoMBook);
                mbDeposit.get().setViewVouchers(dtos);
            }
        } else if (mbDeposit.get().getTypeID().equals(TypeConstant.NOP_TIEN_TU_KHACH_HANG)) {
            List<SAInvoiceForMCReceiptDTO> listSAInvoice =
                mcReceiptRepository.findVoucherByListSAInvoice(mbDeposit.get().getmBDepositDetailCustomers().stream().map(n -> n.getSaleInvoiceID()).collect(Collectors.toList()),
                    mbDeposit.get().getmBDepositDetailCustomers().stream().collect(Collectors.toList()).get(0).getVoucherTypeID());
            for (MBDepositDetailCustomer mbDepositDetailCustomer : mbDeposit.get().getmBDepositDetailCustomers()) {
                SAInvoiceForMCReceiptDTO saInvoiceDTO = listSAInvoice.stream().filter(n -> n.getId().equals(mbDepositDetailCustomer.getSaleInvoiceID())).findFirst().get();
                mbDepositDetailCustomer.setNoFBook(saInvoiceDTO.getNoFBook());
                mbDepositDetailCustomer.setNoMBook(saInvoiceDTO.getNoMBook());
                mbDepositDetailCustomer.setDate(saInvoiceDTO.getDate());
                mbDepositDetailCustomer.setDueDate(saInvoiceDTO.getDueDate());
                mbDepositDetailCustomer.setPaymentClauseCode(saInvoiceDTO.getPaymentClauseCode());
            }
        }
        return mbDeposit;
    }

    /**
     * Delete the mBDeposit by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MBDeposit : {}", id);
        Optional<MBDeposit> mbDeposit = mBDepositRepository.findById(id);
        mBDepositRepository.deleteById(id);
        refVoucherRepository.deleteByRefID1(id);
        refVoucherRepository.deleteByRefID2(id);
        // Optional<SaBill> oldSaBill = saBillRepository.findBySaInvoiceID(id);
    }

    @Override
    public Page<MBDepositViewDTO> findAll(Pageable pageable, SearchVoucher searchVoucher) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return mBDepositRepository.findAll(pageable, searchVoucher, currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    public MBDepositDTO getAllData(UUID mbDepositId) {
        List<MBDepositDetails> mbDepositDetails = mbDepositDetailsRepository.findByMBDepositID(mbDepositId);
//        List<MBDepositDetailTax> mbDepositDetailTaxes = mbDepositDetailTaxRepository.findByMBDepositID(mbDepositId);
        MBDepositDTO dto = new MBDepositDTO();
        dto.setMbDepositDetails(mbDepositDetails);
//        dto.setMbDepositDetailTaxes(mbDepositDetailTaxes);
        return dto;
    }

    /**
     * @param searchVoucher
     * @param rowNum
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public MBDeposit findOneByRowNum(SearchVoucher searchVoucher, Number rowNum) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        List<vn.softdreams.ebweb.domain.SystemOption> collect = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
            .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return mBDepositRepository.findByRowNum(searchVoucher, rowNum, currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    public void mutipleRecord(MutipleRecord mutipleRecord) {
        Optional<User> userOptional = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        mBDepositRepository.mutipleRecord(userOptional, currentUserLoginAndOrg, mutipleRecord);
    }

    @Override
    public MBDepositSaveDTO saveDTO(MBDeposit mBDeposit) {
        log.debug("Request to save MBDeposit : {}", mBDeposit);
        MBDeposit mBD = new MBDeposit();
        MBDepositSaveDTO mbDepositSaveDTO = new MBDepositSaveDTO();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            if (mBDeposit.getId() == null) {
                if (!utilsService.checkQuantityLimitedNoVoucher()) {
                    throw new BadRequestAlertException("Số chứng từ hiện tại đã vượt quá số cho phép của Gói sử dụng", "", "noVoucherLimited");
                }
            }
            UserDTO userDTO = userService.getAccount();
            if (mBDeposit.getTypeLedger() == 2) {
                if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(PHIEN_SoLamViec)).findAny().get().getData().equals("0")) {
                    if (StringUtils.isEmpty(mBDeposit.getNoMBook())) {
                        mBDeposit.setNoMBook(genCodeService.getCodeVoucher(16, 1));
                    }
                } else {
                    if (StringUtils.isEmpty(mBDeposit.getNoFBook())) {
                        mBDeposit.setNoFBook(genCodeService.getCodeVoucher(16, 0));
                    }
                }
            }
            if (!utilsRepository.checkDuplicateNoVoucher(mBDeposit.getNoFBook(), mBDeposit.getNoMBook(), mBDeposit.getTypeLedger(), mBDeposit.getId())) {
                mbDepositSaveDTO.setMbDeposit(mBDeposit);
                mbDepositSaveDTO.setStatus(1);
                return mbDepositSaveDTO;
            }
            mBDeposit.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            mBD = mBDepositRepository.save(mBDeposit);
//            utilsRepository.updateGencode(mBD.getNoFBook(), mBD.getNoMBook(), 16, mBD.getTypeLedger() == null ? 2 : mBD.getTypeLedger());
            List<RefVoucher> refVouchers = new ArrayList<>();
            for (RefVoucherDTO item : mBDeposit.getViewVouchers() != null ? mBDeposit.getViewVouchers() : new ArrayList<RefVoucherDTO>()) {
                RefVoucher refVoucher = new RefVoucher();
                refVoucher.setCompanyID(mBD.getCompanyID());
                refVoucher.setRefID1(mBD.getId());
                refVoucher.setRefID2(item.getRefID2());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                refVouchers.add(refVoucher);
            }
            refVoucherRepository.deleteByRefID1(mBD.getId());
            refVoucherRepository.deleteByRefID2(mBD.getId());
            refVoucherRepository.saveAll(refVouchers);
            mBD.setViewVouchers(mBDeposit.getViewVouchers());
            if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {
                if (!generalLedgerService.record(mBD, new MessageDTO(""))) {
                    mbDepositSaveDTO.setStatus(2);
                } else {
                    mBD.setRecorded(true);
                    mBDepositRepository.save(mBD);
                }
            }
            mbDepositSaveDTO.setMbDeposit(mBD);
            mbDepositSaveDTO.setStatus(0);
            return mbDepositSaveDTO;
        }
        throw new BadRequestAlertException("Không thể lưu phiếu thu", "", "");
    }

    @Override
    public byte[] exportPDF(SearchVoucher searchVoucher) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        Page<MBDepositExportDTO> mbDeposits = currentUserLoginAndOrg.map(securityDTO -> mBDepositRepository.getAllMBDeposits(null, searchVoucher,
            securityDTO.getOrg(), currentBook)).orElse(null);
        for (int i = 0; i < mbDeposits.getContent().size(); i++) {
            if (mbDeposits.getContent().get(i).getTypeID().equals(Constants.MBDeposit.TYPE_BAO_CO)) {
                mbDeposits.getContent().get(i).setTypeIDInWord("Nộp tiền vào tài khoản");
            } else if (mbDeposits.getContent().get(i).getTypeID().equals(Constants.MBDeposit.TYPE_NOP_TIEN_TU_BAN_HANG)) {
                mbDeposits.getContent().get(i).setTypeIDInWord("Nộp tiền từ khách hàng");
            } else if (mbDeposits.getContent().get(i).getTypeID().equals(Constants.MBDeposit.TYPE_NOP_TIEN_TU_KHACH_HANG)) {
                mbDeposits.getContent().get(i).setTypeIDInWord("Nộp tiền từ bán hàng");
            }
        }
        return PdfUtils.writeToFile(mbDeposits.getContent(), ExcelConstant.MBDeposit.HEADER, ExcelConstant.MBDeposit.FIELD);
    }

    @Override
    public byte[] exportExcel(SearchVoucher searchVoucher) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        Page<MBDepositExportDTO> mbDeposits = currentUserLoginAndOrg.map(securityDTO -> mBDepositRepository.getAllMBDeposits(null, searchVoucher,
            securityDTO.getOrg(), currentBook)).orElse(null);
        for (int i = 0; i < mbDeposits.getContent().size(); i++) {
            if (mbDeposits.getContent().get(i).getTypeID().equals(Constants.MBDeposit.TYPE_BAO_CO)) {
                mbDeposits.getContent().get(i).setTypeIDInWord("Nộp tiền vào tài khoản");
            } else if (mbDeposits.getContent().get(i).getTypeID().equals(Constants.MBDeposit.TYPE_NOP_TIEN_TU_BAN_HANG)) {
                mbDeposits.getContent().get(i).setTypeIDInWord("Nộp tiền từ khách hàng");
            } else if (mbDeposits.getContent().get(i).getTypeID().equals(Constants.MBDeposit.TYPE_NOP_TIEN_TU_KHACH_HANG)) {
                mbDeposits.getContent().get(i).setTypeIDInWord("Nộp tiền từ bán hàng");
            }
        }
        return ExcelUtils.writeToFile(mbDeposits.getContent(), ExcelConstant.MBDeposit.NAME, ExcelConstant.MBDeposit.HEADER, ExcelConstant.MBDeposit.FIELD);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Number> getIndexRow(UUID id, SearchVoucher searchVoucher) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return mBDepositRepository.getIndexRow(id, searchVoucher, currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    public HandlingResultDTO multiDelete(List<MBDeposit> mbDeposits) {
        UserDTO userDTO = userService.getAccount();
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        handlingResultDTO.setCountTotalVouchers(mbDeposits.size());
        List<MBDeposit> listDelete = mbDeposits.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        org.joda.time.LocalDate dateClosed = org.joda.time.LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (!closeDateStr.equals("") && closeDateStr != null) {
            dateClosed = org.joda.time.LocalDate.parse(closeDateStr);
        }
        List<UUID> uuidListDelete = new ArrayList<>();
        for (MBDeposit md : mbDeposits) {
            org.joda.time.LocalDate postedDate = org.joda.time.LocalDate.parse(md.getPostedDate().toString());
            if (Boolean.TRUE.equals(md.isRecorded()) && !closeDateStr.equals("") && closeDateStr != null && dateClosed != null && dateClosed.isAfter(postedDate)) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đã khóa sổ!");
                BeanUtils.copyProperties(md, viewVoucherNo);
                if (md.getTypeID() == TypeConstant.NOP_TIEN_TU_TAI_KHOAN) {
                    viewVoucherNo.setTypeName("Nộp tiền từ tài khoản");
                } else if (md.getTypeID() == TypeConstant.NOP_TIEN_TU_KHACH_HANG) {
                    viewVoucherNo.setTypeName("Nộp tiền từ khách hàng");
                } else if (md.getTypeID() == TypeConstant.NOP_TIEN_TU_BAN_HANG) {
                    viewVoucherNo.setTypeName("Nộp tiền từ bán hàng");
                }
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(md);
            } else if (Boolean.TRUE.equals(md.isRecorded())) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang ghi sổ !");
                BeanUtils.copyProperties(md, viewVoucherNo);
                if (md.getTypeID() == TypeConstant.NOP_TIEN_TU_TAI_KHOAN) {
                    viewVoucherNo.setTypeName("Nộp tiền từ tài khoản");
                } else if (md.getTypeID() == TypeConstant.NOP_TIEN_TU_KHACH_HANG) {
                    viewVoucherNo.setTypeName("Nộp tiền từ khách hàng");
                } else if (md.getTypeID() == TypeConstant.NOP_TIEN_TU_BAN_HANG) {
                    viewVoucherNo.setTypeName("Nộp tiền từ bán hàng");
                }
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(md);
            }
        }
        if (listDelete.size() == 0) {
            throw new BadRequestAlertException("Các chứng từ đang được ghi sổ, không thể xoá", "mBDeposit", "errorDeleteList");
        }
        // get ListID chung tu theo Type ID
        List<UUID> uuidList_NopTien = new ArrayList<>();
        List<UUID> uuidList_NopTienTuBanHang = new ArrayList<>();
        for (int i = 0; i < listDelete.size(); i++) {
            if (listDelete.get(i).getTypeID() == 162) {
                uuidList_NopTienTuBanHang.add(listDelete.get(i).getId());
            } else {
                uuidList_NopTien.add(listDelete.get(i).getId());
            }
        }
        // Gan' TypeName
        for (int i = 0; i < viewVoucherNoListFail.size(); i++) {
            viewVoucherNoListFail.get(i).setTypeName(typeRepository.findTypeNameByTypeID(viewVoucherNoListFail.get(i).getTypeID()));
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(uuidList_NopTien.size() + uuidList_NopTienTuBanHang.size());
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        // Xoa chung tu
        if (uuidList_NopTien.size() > 0) {
            mBDepositRepository.multiDeleteMBDeposit(currentUserLoginAndOrg.get().getOrg(), uuidList_NopTien);
            mBDepositRepository.multiDeleteMBDepositChild("MBDepositDetail", uuidList_NopTien);
            mBDepositRepository.multiDeleteMBDepositChild("MBDepositDetailCustomer", uuidList_NopTien);
//            mBDepositRepository.multiDeleteGeneralLedger(currentUserLoginAndOrg.get().getOrg(), multiDelete);
            refVoucherRepository.deleteByRefID1sOrRefID2s(uuidList_NopTien);
        }
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        // Xoa chung tu nop tien tu ban hang
        if (uuidList_NopTienTuBanHang.size() > 0) {
            List<UUID> oldSAInvoiceList = saInvoiceRepository.findListSAInvoiceID(uuidList_NopTienTuBanHang);
            if (oldSAInvoiceList.size() > 0) {
                List<UUID> oldRSInwardOutwardList = rsInwardOutwardRepository.findByListSaInvoiceID(oldSAInvoiceList);
                // Cap nhat lai Quantity ben SAOrder
                List<UUID> oldSAOrderDetailList = Utils.convertListStringToListUUIDReverse(saInvoiceRepository.findDetailsByListSAInvoice(oldSAInvoiceList));
                if (oldSAOrderDetailList.size() > 0) {
                    saOrderRepository.updateQuantitySAOrder(oldSAOrderDetailList);
                    // Add by Hautv
                    List<UUID> lstID = Utils.convertListStringToListUUIDReverse(saInvoiceRepository.getListIDSAorderByListDetail(oldSAInvoiceList));
                    String idStr = lstID.stream().map(Functions.toStringFunction()).collect(Collectors.joining(","));
                    saOrderRepository.updateStatusSAOder(idStr);
                }
                saInvoiceRepository.deleteListSAInvoiceID(oldSAInvoiceList, currentUserLoginAndOrg.get().getOrg());
                refVoucherRepository.deleteByRefID1sOrRefID2s(oldSAInvoiceList);
                if (oldRSInwardOutwardList.size() > 0) {
                    rsInwardOutwardRepository.deleteByListSAInvoiceID(oldSAInvoiceList, currentUserLoginAndOrg.get().getOrg());
                }
                mBDepositRepository.deleteByListID(uuidList_NopTienTuBanHang);
            }
        }
        return handlingResultDTO;
    }

    @Override
    public HandlingResultDTO multiUnrecord(List<MBDeposit> mbDeposits) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        handlingResultDTO.setCountTotalVouchers(mbDeposits.size());
        List<MBDeposit> listDelete = mbDeposits.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        org.joda.time.LocalDate dateClosed = org.joda.time.LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (!closeDateStr.equals("") && closeDateStr != null) {
            dateClosed = org.joda.time.LocalDate.parse(closeDateStr);
        }
        for (MBDeposit md : mbDeposits) {
            org.joda.time.LocalDate postedDate = org.joda.time.LocalDate.parse(md.getPostedDate().toString());
            if (Boolean.TRUE.equals(md.isRecorded()) && !closeDateStr.equals("") && closeDateStr != null && dateClosed != null && (dateClosed.isAfter(postedDate) || dateClosed.equals(postedDate))) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đã khóa sổ!");
                BeanUtils.copyProperties(md, viewVoucherNo);
                if (md.getTypeID() == TypeConstant.NOP_TIEN_TU_TAI_KHOAN) {
                    viewVoucherNo.setTypeName("Nộp tiền từ tài khoản");
                } else if (md.getTypeID() == TypeConstant.NOP_TIEN_TU_KHACH_HANG) {
                    viewVoucherNo.setTypeName("Nộp tiền từ khách hàng");
                } else if (md.getTypeID() == TypeConstant.NOP_TIEN_TU_BAN_HANG) {
                    viewVoucherNo.setTypeName("Nộp tiền từ bán hàng");
                }
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(md);
            } else if (Boolean.FALSE.equals(md.isRecorded())) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang bỏ ghi sổ !");
                BeanUtils.copyProperties(md, viewVoucherNo);
                if (md.getTypeID() == TypeConstant.NOP_TIEN_TU_TAI_KHOAN) {
                    viewVoucherNo.setTypeName("Nộp tiền từ tài khoản");
                } else if (md.getTypeID() == TypeConstant.NOP_TIEN_TU_KHACH_HANG) {
                    viewVoucherNo.setTypeName("Nộp tiền từ khách hàng");
                } else if (md.getTypeID() == TypeConstant.NOP_TIEN_TU_BAN_HANG) {
                    viewVoucherNo.setTypeName("Nộp tiền từ bán hàng");
                }
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(md);
            }
        }

        List<UUID> uuidList_MB_MC_Deposit = new ArrayList<>();
        List<UUID> uuidList_MSDeposit = new ArrayList<>();
        List<UUID> uuidListRS = new ArrayList<>();
        List<UUID> uuidList = new ArrayList<>();
        for (int i = 0; i < listDelete.size(); i++) {
            if (listDelete.get(i).getTypeID() == TypeConstant.NOP_TIEN_TU_BAN_HANG) {
                uuidList_MSDeposit.add(listDelete.get(i).getId());
            } else {
                uuidList_MB_MC_Deposit.add(listDelete.get(i).getId());
            }
            uuidList.add(listDelete.get(i).getId());
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(listDelete.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();

        if (uuidList.size() > 0) {
            mBDepositRepository.mutipleUnRecord(uuidList, currentUserLoginAndOrg.get().getOrg());
            if (uuidList_MB_MC_Deposit.size() > 0) {
                mBDepositRepository.updateUnrecord(uuidList_MB_MC_Deposit);
            }
            if (uuidList_MSDeposit.size() > 0) {
                List<UUID> oldSAInvoiceList = saInvoiceRepository.findListSAInvoiceID(uuidList_MSDeposit);
                uuidListRS = saInvoiceRepository.findListRSInwardOutWardIDNotNULL(uuidList_MSDeposit);
                mBDepositRepository.updateUnrecord(uuidList_MSDeposit);
                // Xoa chung tu nop tien tu ban hang
                if (oldSAInvoiceList.size() > 0) {
                    saInvoiceRepository.updateUnRecordListID(oldSAInvoiceList);
                    saInvoiceRepository.deleteUnRecordListID(oldSAInvoiceList, currentUserLoginAndOrg.get().getOrg());
                    rsInwardOutWardDetailsRepository.UpdateSAInvoiceIDNull(oldSAInvoiceList);
                    saReturnDetailsRepository.UpdateSAInvoiceIDNull(oldSAInvoiceList);
                    List<String> listRelateID = saInvoiceRepository.getRelateIDBySAInvoiceID(oldSAInvoiceList);
                    if (listRelateID.size() > 0) {
                        saInvoiceRepository.DeleteRelate(listRelateID);
                    }
                    if (uuidListRS.size() > 0) {
                        rsInwardOutwardRepository.updateUnrecord(uuidListRS);
                        repositoryLedgerRepository.deleteAllByReferenceID(uuidListRS);
                    }
                }
            }
        }
        return handlingResultDTO;
    }
}
