package vn.softdreams.ebweb.service.impl;

import com.google.common.base.Functions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.*;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.dto.cashandbank.MCReceiptDTO;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.web.rest.dto.MCReceiptSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing MCReceipt.
 */
@Service
@Transactional
public class MCReceiptServiceImpl implements MCReceiptService {

    private final Logger log = LoggerFactory.getLogger(MCReceiptServiceImpl.class);

    private final MCReceiptRepository mCReceiptRepository;
    private final MCReceiptDetailsRepository mcReceiptDetailsRepository;
    private final RefVoucherRepository refVoucherRepository;
    private final MCAuditRepository mcAuditRepository;
    private final UserService userService;
    private final GenCodeService genCodeService;
    private final UtilsService utilsService;
    private final GeneralLedgerService generalLedgerService;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final SAInvoiceRepository saInvoiceRepository;
    private final RSInwardOutWardDetailsRepository rsInwardOutWardDetailsRepository;
    private final SaReturnDetailsRepository saReturnDetailsRepository;
    private final RSInwardOutwardRepository rsInwardOutwardRepository;
    private final RepositoryLedgerRepository repositoryLedgerRepository;
    private final ViewVoucherNoRespository viewVoucherNoRespository;
    private final TypeRepository typeRepository;
    private final SAOrderRepository saOrderRepository;
    private final String PHIEN_SoLamViec = "PHIEN_SoLamViec";
    private final String TCKHAC_GhiSo = "TCKHAC_GhiSo";
    private final Integer TYPE_MC_RECEIPT_FROM_SAINVOICE = 102;
    @Autowired
    UtilsRepository utilsRepository;

    public MCReceiptServiceImpl(MCReceiptRepository mCReceiptRepository,
                                MCReceiptDetailsRepository mcReceiptDetailsRepository,
                                RefVoucherRepository refVoucherRepository,
                                UserService userService,
                                GenCodeService genCodeService,
                                GeneralLedgerService generalLedgerService,
                                OrganizationUnitRepository organizationUnitRepository,
                                MCAuditRepository mcAuditRepository,
                                UtilsService utilsService,
                                SAInvoiceRepository saInvoiceRepository,
                                RSInwardOutWardDetailsRepository rsInwardOutWardDetailsRepository,
                                SaReturnDetailsRepository saReturnDetailsRepository,
                                RSInwardOutwardRepository rsInwardOutwardRepository,
                                RepositoryLedgerRepository repositoryLedgerRepository,
                                SAOrderRepository saOrderRepository,
                                TypeRepository typeRepository,
                                ViewVoucherNoRespository viewVoucherNoRespository
    ) {
        this.mCReceiptRepository = mCReceiptRepository;
        this.mcReceiptDetailsRepository = mcReceiptDetailsRepository;
        this.refVoucherRepository = refVoucherRepository;
        this.userService = userService;
        this.genCodeService = genCodeService;
        this.generalLedgerService = generalLedgerService;
        this.organizationUnitRepository = organizationUnitRepository;
        this.mcAuditRepository = mcAuditRepository;
        this.utilsService = utilsService;
        this.saInvoiceRepository = saInvoiceRepository;
        this.rsInwardOutWardDetailsRepository = rsInwardOutWardDetailsRepository;
        this.saReturnDetailsRepository = saReturnDetailsRepository;
        this.rsInwardOutwardRepository = rsInwardOutwardRepository;
        this.repositoryLedgerRepository = repositoryLedgerRepository;
        this.saOrderRepository = saOrderRepository;
        this.typeRepository = typeRepository;
        this.viewVoucherNoRespository = viewVoucherNoRespository;
    }

    /**
     * Save a mCReceipt.
     *
     * @param mCReceipt the entity to save
     * @return the persisted entity
     */
    @Override
    public MCReceipt save(MCReceipt mCReceipt) {
        log.debug("Request to save MCReceipt : {}", mCReceipt);
        MCReceipt mcR = new MCReceipt();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            mCReceipt.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            mcR = mCReceiptRepository.save(mCReceipt);
//            utilsRepository.updateGencode(mcR.getNoFBook(), mcR.getNoMBook(), 10, mcR.getTypeLedger() == null ? 2 : mcR.getTypeLedger());
            List<RefVoucher> refVouchers = new ArrayList<>();
            for (RefVoucherDTO item : mCReceipt.getViewVouchers() != null ? mCReceipt.getViewVouchers() : new ArrayList<RefVoucherDTO>()) {
                RefVoucher refVoucher = new RefVoucher();
                refVoucher.setCompanyID(mcR.getCompanyID());
                refVoucher.setRefID1(mcR.getId());
                refVoucher.setRefID2(item.getRefID2());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                refVouchers.add(refVoucher);
            }
            refVoucherRepository.deleteByRefID1(mcR.getId());
            refVoucherRepository.saveAll(refVouchers);
            mcR.setViewVouchers(mCReceipt.getViewVouchers());
            return mcR;
        }
        throw new BadRequestAlertException("Không thể lưu phiếu thu", "", "");
    }

    /**
     * Get all the mCReceipts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MCReceipt> findAll(Pageable pageable) {
        log.debug("Request to get all MCReceipts");
        return mCReceiptRepository.findAll(pageable);
    }


    /**
     * Get one mCReceipt by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MCReceipt> findOne(UUID id) {
        log.debug("Request to get MCReceipt : {}", id);
        Optional<MCReceipt> mCReceipt = mCReceiptRepository.findById(id);
        if (mCReceipt.get().getTypeID().equals(TYPE_MC_RECEIPT_FROM_SAINVOICE)) {
            Object object = mCReceiptRepository.findRefSAInvoice(id);
            mCReceipt.get().setsAInvoiceID(Utils.uuidConvertToGUID(UUID.fromString(((Object[]) object)[0].toString())));
            if (((Object[]) object)[1] != null) {
                mCReceipt.get().setrSInwardOutwardID(Utils.uuidConvertToGUID(UUID.fromString(((Object[]) object)[1].toString())));
            }
            if (((Object[]) object)[3] != null) {
                mCReceipt.get().setsAIsBill(Boolean.valueOf(((Object[]) object)[3].toString()));
            }
            if (((Object[]) object)[4] != null) {
                mCReceipt.get().setsAInvoiceForm(Integer.valueOf(((Object[]) object)[4].toString()));
            }
            if (((Object[]) object)[5] != null) {
                mCReceipt.get().setsAInvoiceNo(((Object[]) object)[5].toString());
            }
            mCReceipt.get().setTypeIDSAInvoice(Integer.valueOf(((Object[]) object)[2].toString()));
        } else if (mCReceipt.get().getTypeID().equals(Constants.MCReceipt.TYPE_PHIEU_THU)) {
            Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
            Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
            if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
                String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
                boolean isNoMBook = currentBook.equalsIgnoreCase("1");
                List<RefVoucherDTO> dtos = refVoucherRepository.getRefViewVoucher(id, isNoMBook);
                mCReceipt.get().setViewVouchers(dtos);
            }
        } else if (mCReceipt.get().getTypeID().equals(TypeConstant.PHIEU_THU_TIEN_KHACH_HANG)) {
            List<SAInvoiceForMCReceiptDTO> listSAInvoice =
                mCReceiptRepository.findVoucherByListSAInvoice(mCReceipt.get().getMCReceiptDetailCustomers().stream().map(n -> n.getSaleInvoiceID()).collect(Collectors.toList()),
                    mCReceipt.get().getMCReceiptDetailCustomers().stream().collect(Collectors.toList()).get(0).getVoucherTypeID());
            for (MCReceiptDetailCustomer mcReceiptDetailCustomer : mCReceipt.get().getMCReceiptDetailCustomers()) {
                SAInvoiceForMCReceiptDTO saInvoiceDTO = listSAInvoice.stream().filter(n -> n.getId().equals(mcReceiptDetailCustomer.getSaleInvoiceID())).findFirst().get();
                if (saInvoiceDTO != null) {
                    mcReceiptDetailCustomer.setNoFBook(saInvoiceDTO.getNoFBook());
                    mcReceiptDetailCustomer.setNoMBook(saInvoiceDTO.getNoMBook());
                    mcReceiptDetailCustomer.setDate(saInvoiceDTO.getDate());
                    mcReceiptDetailCustomer.setDuDate(saInvoiceDTO.getDueDate());
                    mcReceiptDetailCustomer.setPaymentClauseCode(saInvoiceDTO.getPaymentClauseCode());
                }
            }
        }
        return mCReceipt;
    }

    @Override
    public MCReceipt findByAuditID(UUID mcAuditID) {
        return mCReceiptRepository.findByAuditID(mcAuditID);
    }

    /**
     * Delete the mCReceipt by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MCReceipt : {}", id);
        /*MCReceipt mcReceipt = mCReceiptRepository.findById(id).get();
        if (mcReceipt.getmCAuditID() != null) {
            mcAuditRepository.deleteById(mcReceipt.getmCAuditID());
        }*/
        mCReceiptRepository.deleteById(id);
        refVoucherRepository.deleteByRefID1(id);
        refVoucherRepository.deleteByRefID2(id);
    }

    @Override
    public Page<MCReceiptDTO> findAllDTOByCompanyID(Pageable pageable) {
        log.debug("Request to get all MCReceiptsDTO by CompanyID");
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        return mCReceiptRepository.findAllDTOByCompanyID(pageable, userDTO.getOrganizationUnit().getId(), phienSoLamViec);
    }

    /**
     * Save a mCReceipt.
     *
     * @param mCReceipt the entity to save
     * @return the persisted entity
     */
    @Override
    public MCReceiptSaveDTO saveDTO(MCReceipt mCReceipt) {
        log.debug("Request to save MCReceipt : {}", mCReceipt);
        if (mCReceipt.getId() == null && !utilsService.checkQuantityLimitedNoVoucher()) {
            throw new BadRequestAlertException("Số chứng từ hiện tại đã vượt quá số cho phép của Gói sử dụng", "", "noVoucherLimited");
        }
        MCReceipt mcR = new MCReceipt();
        MCReceiptSaveDTO mcReceiptSaveDTO = new MCReceiptSaveDTO();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        if (mCReceipt.getTypeLedger() == 2) {
            if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(PHIEN_SoLamViec)).findAny().get().getData().equals("0")) {
                if (StringUtils.isEmpty(mCReceipt.getNoMBook())) {
                    mCReceipt.setNoMBook(genCodeService.getCodeVoucher(10, 1));
                }
            } else {
                if (StringUtils.isEmpty(mCReceipt.getNoFBook())) {
                    mCReceipt.setNoFBook(genCodeService.getCodeVoucher(10, 0));
                }
            }
        }
        if (!utilsRepository.checkDuplicateNoVoucher(mCReceipt.getNoFBook(), mCReceipt.getNoMBook(), mCReceipt.getTypeLedger(), mCReceipt.getId())) {
            mcReceiptSaveDTO.setmCReceipt(mCReceipt);
            mcReceiptSaveDTO.setStatus(1);
            return mcReceiptSaveDTO;
        }
        if (currentUserLoginAndOrg.isPresent()) {
            mCReceipt.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            mcR = mCReceiptRepository.save(mCReceipt);
//            utilsRepository.updateGencode(mcR.getNoFBook(), mcR.getNoMBook(), 10, mcR.getTypeLedger() == null ? 2 : mcR.getTypeLedger());
            List<RefVoucher> refVouchers = new ArrayList<>();
            for (RefVoucherDTO item : mCReceipt.getViewVouchers() != null ? mCReceipt.getViewVouchers() : new ArrayList<RefVoucherDTO>()) {
                RefVoucher refVoucher = new RefVoucher();
                refVoucher.setCompanyID(mcR.getCompanyID());
                refVoucher.setRefID1(mcR.getId());
                refVoucher.setRefID2(item.getRefID2());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                refVouchers.add(refVoucher);
            }
            refVoucherRepository.deleteByRefID1(mcR.getId());
            refVoucherRepository.saveAll(refVouchers);
            mcR.setViewVouchers(mCReceipt.getViewVouchers());
            MessageDTO messageDTO = new MessageDTO();
            if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {
                if (!generalLedgerService.record(mcR, messageDTO)) {
                    mcReceiptSaveDTO.setStatus(2);
                    mcReceiptSaveDTO.setMsg(messageDTO.getMsgError());
                } else {
                    mcReceiptSaveDTO.setStatus(0);
                    mcR.setRecorded(true);
                    mCReceiptRepository.save(mcR);
                }
            } else {
                mcReceiptSaveDTO.setStatus(0);
                if (mcR.getRecorded() == null) {
                    mcR.setRecorded(false);
                    mCReceiptRepository.save(mcR);
                }
//                mcP.setRecorded(false);
            }
            mcReceiptSaveDTO.setmCReceipt(mcR);
            return mcReceiptSaveDTO;
        }
        throw new BadRequestAlertException("Không thể lưu phiếu thu", "", "");
    }

    @Override
    public HandlingResultDTO multiUnrecord(List<MCReceipt> mcReceipts) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        handlingResultDTO.setCountTotalVouchers(mcReceipts.size());
        List<MCReceipt> listDelete = new ArrayList<>(mcReceipts);
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        org.joda.time.LocalDate dateClosed = org.joda.time.LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (closeDateStr != null && !closeDateStr.equals("")) {
            dateClosed = org.joda.time.LocalDate.parse(closeDateStr);
        }
        for (MCReceipt md : mcReceipts) {
            org.joda.time.LocalDate postedDate = org.joda.time.LocalDate.parse(md.getPostedDate().toString());
            if (closeDateStr != null && Boolean.TRUE.equals(md.isRecorded()) && !closeDateStr.equals("") && dateClosed != null && (dateClosed.isAfter(postedDate) || dateClosed.equals(postedDate))) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đã khóa sổ!");
                viewVoucherNo.setPostedDate(md.getPostedDate());
                viewVoucherNo.setDate(md.getDate());
                viewVoucherNo.setTypeID(md.getTypeID());
                viewVoucherNo.setNoFBook(md.getNoFBook());
                viewVoucherNo.setNoMBook(md.getNoMBook());
                if (md.getTypeID() == TypeConstant.PHIEU_THU_TIEN_KHACH_HANG) {
                    viewVoucherNo.setTypeName("Phiếu thu tiền khách hàng");
                } else if (md.getTypeID() == TypeConstant.PHIEU_THU_TU_BAN_HANG) {
                    viewVoucherNo.setTypeName("Phiếu thu từ bán hàng");
                } else {
                    viewVoucherNo.setTypeName("Phiếu thu");
                }
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(md);
            } else if (Boolean.FALSE.equals(md.isRecorded())) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang bỏ ghi sổ !");
                BeanUtils.copyProperties(md, viewVoucherNo);
                viewVoucherNo.setTypeID(md.getTypeID());
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(md);
            }
        }

        for (ViewVoucherNo v : viewVoucherNoListFail) {
            if (v.getTypeID() == TypeConstant.PHIEU_THU_TIEN_KHACH_HANG) {
                v.setTypeName("Phiếu thu tiền khách hàng");
            } else if (v.getTypeID() == TypeConstant.PHIEU_THU_TU_BAN_HANG) {
                v.setTypeName("Phiếu thu từ bán hàng");
            } else {
                v.setTypeName("Phiếu thu");
            }
        }

        List<UUID> uuidList_MB_MC_Receipt = new ArrayList<>();
        List<UUID> uuidList_MC_SA = new ArrayList<>();
        List<UUID> uuidListRS = new ArrayList<>();
        List<UUID> uuidList = new ArrayList<>();
        for (int i = 0; i < listDelete.size(); i++) {
            if (listDelete.get(i).getTypeID() == TypeConstant.PHIEU_THU_TU_BAN_HANG) {
                uuidList_MC_SA.add(listDelete.get(i).getId());
            } else {
                uuidList_MB_MC_Receipt.add(listDelete.get(i).getId());
            }
            uuidList.add(listDelete.get(i).getId());
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(listDelete.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();

        if (uuidList.size() > 0) {
            mCReceiptRepository.mutipleUnRecord(uuidList, currentUserLoginAndOrg.get().getOrg());
            if (uuidList_MB_MC_Receipt.size() > 0) {
                mCReceiptRepository.updateUnrecord(uuidList_MB_MC_Receipt);
            }
            if (uuidList_MC_SA.size() > 0) {
                List<UUID> oldSAInvoiceList = convertToUUID(saInvoiceRepository.findAllIDByMCReceiptID(uuidList_MC_SA));
                uuidListRS = saInvoiceRepository.findListRSInwardOutWardIDNotNULL(uuidList_MC_SA);
                mCReceiptRepository.updateUnrecord(uuidList_MC_SA);
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

    /**
     * @param uuids
     * @return
     * @Author Hautv
     */
    List<UUID> convertToUUID(List<String> uuids) {
        List<UUID> result = new ArrayList<>();
        for (String id : uuids) {
            result.add(Utils.uuidConvertToGUID(UUID.fromString(id)));
        }
        return result;
    }

    @Override
    public HandlingResultDTO multiDelete(List<MCReceipt> mcReceipts) {
        UserDTO userDTO = userService.getAccount();
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        handlingResultDTO.setCountTotalVouchers(mcReceipts.size());
        List<MCReceipt> listDelete = new ArrayList<>(mcReceipts);
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        List<UUID> uuidListDelete = new ArrayList<>();
        for (int i = 0; i < mcReceipts.size(); i++) {
            if (Boolean.TRUE.equals(mcReceipts.get(i).getRecorded())) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang được ghi sổ !");
                BeanUtils.copyProperties(mcReceipts.get(i), viewVoucherNo);
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(mcReceipts.get(i));
            }
        }
        if (listDelete.size() == 0) {
            throw new BadRequestAlertException("Các chứng từ đang được ghi sổ, không thể xoá", "mBDeposit", "errorDeleteList");
        }
        // get ListID chung tu theo Type ID
        List<UUID> uuidList_PT = new ArrayList<>();
        List<UUID> uuidList_PTTuBanHang = new ArrayList<>();
        for (int i = 0; i < listDelete.size(); i++) {
            if (listDelete.get(i).getTypeID() == TypeConstant.PHIEU_THU_TU_BAN_HANG) {
                uuidList_PTTuBanHang.add(listDelete.get(i).getId());
            } else {
                uuidList_PT.add(listDelete.get(i).getId());
            }
        }
        // Xoa chung tu
        if (uuidList_PT.size() > 0) {
            mCReceiptRepository.multiDeleteByID(userDTO.getOrganizationUnit().getId(), uuidList_PT);
        }
        // Xoa chung tu nop tien tu ban hang
        if (uuidList_PTTuBanHang.size() > 0) {
            List<UUID> oldSAInvoiceList = convertToUUID(saInvoiceRepository.findAllIDByMCReceiptID(uuidList_PTTuBanHang));
            List<ViewVoucherNo> viewVoucherNos = viewVoucherNoRespository.getAllByListID(userDTO.getOrganizationUnit().getId(), oldSAInvoiceList);
            for (ViewVoucherNo sa : viewVoucherNos) {
                if (sa.getInvoiceForm() != null && sa.getInvoiceNo() != null && sa.getInvoiceForm().compareTo(Constants.InvoiceForm.HD_DIEN_TU) == 0 &&
                    userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_SDTichHopHDDT)).findAny().get().getData().equals("1")) {
                    ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                    viewVoucherNo.setReasonFail("Chứng từ này đã phát sinh hóa đơn được cấp số !");
                    MCReceipt mcReceipt = mcReceipts.stream().filter(n -> n.getId().equals(sa.getmCReceiptID())).findFirst().get();
                    BeanUtils.copyProperties(mcReceipt, viewVoucherNo);
                    viewVoucherNoListFail.add(viewVoucherNo);
                    listDelete.remove(mcReceipt);
                    uuidList_PTTuBanHang.remove(mcReceipt.getId());
                    oldSAInvoiceList.remove(sa.getRefID());
                }
            }
            if (oldSAInvoiceList.size() > 0) {
                List<UUID> oldRSInwardOutwardList = rsInwardOutwardRepository.findByListSaInvoiceID(oldSAInvoiceList);
                // Cap nhat lai Quantity ben SAOrder
                List<UUID> oldSAOrderDetailList = Utils.convertListStringToListUUIDReverse(saInvoiceRepository.findDetailsByListSAInvoice(oldSAInvoiceList));
                if (oldSAOrderDetailList.size() > 0) {
                    saOrderRepository.updateQuantitySAOrder(oldSAOrderDetailList);
                    List<UUID> lstID = Utils.convertListStringToListUUIDReverse(saInvoiceRepository.getListIDSAorderByListDetail(oldSAInvoiceList));
                    String idStr = lstID.stream().map(Functions.toStringFunction()).collect(Collectors.joining(","));
                    saOrderRepository.updateStatusSAOder(idStr);
                }
                saInvoiceRepository.deleteListSAInvoiceID(oldSAInvoiceList, userDTO.getOrganizationUnit().getId());
                refVoucherRepository.deleteByRefID1sOrRefID2s(oldSAInvoiceList);
                if (oldRSInwardOutwardList.size() > 0) {
                    rsInwardOutwardRepository.deleteByListSAInvoiceID(oldSAInvoiceList, userDTO.getOrganizationUnit().getId());
                }
                mCReceiptRepository.deleteByListID(uuidList_PTTuBanHang);
            }
        }
        // Gan' TypeName
        List<Type> types = typeRepository.findAllByIsActive();
        for (ViewVoucherNo viewVoucherNo : viewVoucherNoListFail) {
            viewVoucherNo.setTypeName(types.stream().filter(n -> n.getId().equals(viewVoucherNo.getTypeID())).findFirst().get().getTypeName());
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(uuidList_PT.size() + uuidList_PTTuBanHang.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        return handlingResultDTO;
    }

}
