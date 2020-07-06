package vn.softdreams.ebweb.service.impl;

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
import vn.softdreams.ebweb.service.dto.cashandbank.MCPaymentDTO;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.web.rest.dto.MCPaymentSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.MCPaymentViewDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.TypeConstant.*;

/**
 * Service Implementation for managing MCPayment.
 */
@Service
@Transactional
public class MCPaymentServiceImpl implements MCPaymentService {

    private final Logger log = LoggerFactory.getLogger(MCPaymentServiceImpl.class);
    private final MCPaymentRepository mCPaymentRepository;
    private final MCAuditRepository mcAuditRepository;
    private final UserService userService;
    private final GenCodeService genCodeService;
    private final String PHIEN_SoLamViec = "PHIEN_SoLamViec";
    private final RefVoucherRepository refVoucherRepository;
    private final GeneralLedgerService generalLedgerService;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final UtilsService utilsService;
    private final TypeRepository typeRepository;
    private final PPInvoiceRepository ppInvoiceRepository;
    private final PPDiscountReturnRepository ppDiscountReturnRepository;
    private final RSInwardOutwardRepository rsInwardOutwardRepository;
    private final SAInvoiceRepository saInvoiceRepository;
    private final PPServiceRepository ppServiceRepository;
    private final PPInvoiceDetailCostRepository pPInvoiceDetailCostRepository;
    private final GeneralLedgerRepository generalLedgerRepository;
    private final RepositoryLedgerRepository repositoryLedgerRepository;
    private final PPInvoiceDetailsRepository ppInvoiceDetailsRepository;
    private final PporderdetailRepository pporderdetailRepository;
    private final PporderRepository pporderRepository;
    private final PPServiceDetailRepository ppServiceDetailRepository;
    private final ViewVoucherNoRespository viewVoucherNoRespository;
    private final String TCKHAC_GhiSo = "TCKHAC_GhiSo";

    @Autowired
    UtilsRepository utilsRepository;

    public MCPaymentServiceImpl(MCPaymentRepository mCPaymentRepository,
                                UserService userService,
                                RefVoucherRepository refVoucherRepository,
                                GenCodeService genCodeService,
                                GeneralLedgerService generalLedgerService,
                                OrganizationUnitRepository organizationUnitRepository,
                                MCAuditRepository mcAuditRepository,
                                UtilsService utilsService,
                                TypeRepository typeRepository,
                                PPInvoiceRepository ppInvoiceRepository,
                                PPDiscountReturnRepository ppDiscountReturnRepository,
                                RSInwardOutwardRepository rsInwardOutwardRepository,
                                SAInvoiceRepository saInvoiceRepository,
                                PPServiceRepository ppServiceRepository,
                                PPInvoiceDetailCostRepository pPInvoiceDetailCostRepository,
                                GeneralLedgerRepository generalLedgerRepository,
                                RepositoryLedgerRepository repositoryLedgerRepository,
                                PPInvoiceDetailsRepository ppInvoiceDetailsRepository,
                                PporderdetailRepository pporderdetailRepository,
                                PporderRepository pporderRepository, PPServiceDetailRepository ppServiceDetailRepository,
                                ViewVoucherNoRespository viewVoucherNoRespository
    ) {
        this.mCPaymentRepository = mCPaymentRepository;
        this.userService = userService;
        this.refVoucherRepository = refVoucherRepository;
        this.genCodeService = genCodeService;
        this.generalLedgerService = generalLedgerService;
        this.organizationUnitRepository = organizationUnitRepository;
        this.mcAuditRepository = mcAuditRepository;
        this.utilsService = utilsService;
        this.typeRepository = typeRepository;
        this.ppInvoiceRepository = ppInvoiceRepository;
        this.ppDiscountReturnRepository = ppDiscountReturnRepository;
        this.rsInwardOutwardRepository = rsInwardOutwardRepository;
        this.saInvoiceRepository = saInvoiceRepository;
        this.ppServiceRepository = ppServiceRepository;
        this.pPInvoiceDetailCostRepository = pPInvoiceDetailCostRepository;
        this.generalLedgerRepository = generalLedgerRepository;
        this.repositoryLedgerRepository = repositoryLedgerRepository;
        this.ppInvoiceDetailsRepository = ppInvoiceDetailsRepository;
        this.pporderdetailRepository = pporderdetailRepository;
        this.pporderRepository = pporderRepository;
        this.ppServiceDetailRepository = ppServiceDetailRepository;
        this.viewVoucherNoRespository = viewVoucherNoRespository;
    }

    /**
     * Save a mCPayment.
     *
     * @param mCPayment the entity to save
     * @return the persisted entity
     */
    @Override
    public MCPayment save(MCPayment mCPayment) {
        log.debug("Request to save MCReceipt : {}", mCPayment);
        MCPayment mcP = new MCPayment();
        /*if (mCPayment.getId() == null) {
            mCPayment.setId(UUID.randomUUID());
        }
        for (MCPaymentDetails details : mCPayment.getMCPaymentDetails()) {
            details.setmCPaymentID(mCPayment.getId());
        }*/
        /*for (MCReceiptDetailTax details: mCReceipt.getMCReceiptDetailTaxes()){
            details.setmCReceiptID(mCReceipt.getId());
        }*/
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            mCPayment.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            mcP = mCPaymentRepository.save(mCPayment);
//            utilsRepository.updateGencode(mcP.getNoFBook(), mcP.getNoMBook(), 11, mcP.getTypeLedger());
            List<RefVoucher> refVouchers = new ArrayList<>();
            for (RefVoucherDTO item : mCPayment.getViewVouchers() != null ? mCPayment.getViewVouchers() : new ArrayList<RefVoucherDTO>()) {
                RefVoucher refVoucher = new RefVoucher();
                refVoucher.setCompanyID(mcP.getCompanyID());
                refVoucher.setRefID1(mcP.getId());
                refVoucher.setRefID2(item.getRefID2());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                refVouchers.add(refVoucher);
            }
            refVoucherRepository.deleteByRefID1(mcP.getId());
            refVoucherRepository.saveAll(refVouchers);
            mcP.setViewVouchers(mCPayment.getViewVouchers());
            return mcP;
        }
        throw new BadRequestAlertException("", "", "");
    }

    /**
     * Get all the mCPayments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MCPayment> findAll(Pageable pageable) {
        log.debug("Request to get all MCPayments");
        return mCPaymentRepository.findAll(pageable);
    }


    /**
     * Get one mCPayment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MCPayment> findOne(UUID id) {
        log.debug("Request to get MCPayment : {}", id);
        Optional<MCPayment> mcPayment = mCPaymentRepository.findById(id);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");
            List<RefVoucherDTO> dtos = refVoucherRepository.getRefViewVoucher(id, isNoMBook);
            mcPayment.get().setViewVouchers(dtos);
        }
        if (mcPayment.get().getTypeID().equals(Constants.PPInvoiceType.TYPE_ID_PHIEU_CHI_MUA_HANG)) {
            Object object = mCPaymentRepository.findIDRef(id, Constants.PPInvoiceType.TYPE_ID_PHIEU_CHI_MUA_HANG);
            String uuid = ((Object[]) object)[0].toString();
            Boolean storedInRepository = Boolean.valueOf(((Object[]) object)[1].toString());
            mcPayment.get().setPpInvocieID(Utils.uuidConvertToGUID(UUID.fromString(uuid)));
            mcPayment.get().setStoredInRepository(storedInRepository);

        } else if (mcPayment.get().getTypeID().equals(Constants.PPInvoiceType.TYPE_ID_PHIEU_CHI_MUA_HANG_DICH_VU)) {
            UUID uuid = UUID.fromString(mCPaymentRepository.findIDRef(id, Constants.PPInvoiceType.TYPE_ID_PHIEU_CHI_MUA_HANG_DICH_VU).toString());
            mcPayment.get().setPpServiceID(Utils.uuidConvertToGUID(uuid));
        } else if (mcPayment.get().getTypeID().equals(TypeConstant.PHIEU_CHI_TRA_TIEN_NHA_CUNG_CAP)) {
            List<PPInvoiceDTO> lstPPInvoice =
                mCPaymentRepository.findVoucherByListPPInvoiceID(mcPayment.get().getMCPaymentDetailVendors().stream().map(n -> n.getpPInvoiceID()).collect(Collectors.toList()),
                    mcPayment.get().getMCPaymentDetailVendors().stream().collect(Collectors.toList()).get(0).getVoucherTypeID());
            for (MCPaymentDetailVendor mcPaymentDetailVendor : mcPayment.get().getMCPaymentDetailVendors()) {
                PPInvoiceDTO ppInvoiceDTO = lstPPInvoice.stream().filter(n -> n.getId().equals(mcPaymentDetailVendor.getpPInvoiceID())).findFirst().orElse(null);
                if (ppInvoiceDTO != null) {
                    mcPaymentDetailVendor.setNoFBook(ppInvoiceDTO.getNoFBook());
                    mcPaymentDetailVendor.setNoMBook(ppInvoiceDTO.getNoMBook());
                    mcPaymentDetailVendor.setDate(ppInvoiceDTO.getDate());
                    mcPaymentDetailVendor.setDueDate(ppInvoiceDTO.getDueDate());
                }
            }
        }
        return mcPayment;
    }

    @Override
    public MCPayment findByAuditID(UUID mcAuditID) {
        return mCPaymentRepository.findByAuditID(mcAuditID);
    }

    /**
     * Delete the mCPayment by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MCPayment : {}", id);
        /*MCPayment mcPayment = mCPaymentRepository.findById(id).get();
        if (mcPayment.getmCAuditID() != null) {
            mcAuditRepository.deleteById(mcPayment.getmCAuditID());
        }*/
        mCPaymentRepository.deleteById(id);
        refVoucherRepository.deleteByRefID1(id);
        refVoucherRepository.deleteByRefID2(id);
    }

    @Override
    public List<MCPaymentDetails> findMCPaymentDetails(UUID ID) {
        log.debug("Request to get MCPaymentDetails : {}", ID);
        return mCPaymentRepository.findMCPaymentDetails(ID);
    }

    @Override
    public Page<MCPaymentDTO> findAllDTOByCompanyID(Pageable pageable) {
        log.debug("Request to get all MCReceiptsDTO by CompanyID");
        UserDTO userDTO = userService.getAccount();
        return mCPaymentRepository.findAllDTOByCompanyID(pageable, userDTO.getOrganizationUnit().getId(), Integer.valueOf(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(PHIEN_SoLamViec)).findAny().get().getData()));
    }

    @Override
    public MCPaymentViewDTO findOneDTO(UUID id) {
        MCPaymentViewDTO mcPaymentViewDTO = new MCPaymentViewDTO();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            boolean isNoMBook = !collect.isEmpty() && collect.get(0).getCode().equalsIgnoreCase("1");
            List<RefVoucherDTO> dtos = refVoucherRepository.getRefViewVoucher(id, isNoMBook);
            mcPaymentViewDTO.setViewVouchers(dtos);
        }
        Optional<MCPayment> byId = mCPaymentRepository.findById(id);
        if (byId.isPresent()) {
            mcPaymentViewDTO.setmCPayment(byId.get());
            return mcPaymentViewDTO;
        }
        return null;
    }

    @Override
    public MCPaymentSaveDTO saveDTO(MCPayment mCPayment) {
        log.debug("Request to save MCReceipt : {}", mCPayment);
        if (mCPayment.getId() == null && !utilsService.checkQuantityLimitedNoVoucher()) {
            throw new BadRequestAlertException("Số chứng từ hiện tại đã vượt quá số cho phép của Gói sử dụng", "", "noVoucherLimited");
        }
        MCPaymentSaveDTO mcPaymentSaveDTO = new MCPaymentSaveDTO();
        MCPayment mcP = new MCPayment();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        if (mCPayment.getTypeLedger() == 2) {
            if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(PHIEN_SoLamViec)).findAny().get().getData().equals("0")) {
                if (StringUtils.isEmpty(mCPayment.getNoMBook())) {
                    mCPayment.setNoMBook(genCodeService.getCodeVoucher(11, 1));
                }
            } else {
                if (StringUtils.isEmpty(mCPayment.getNoFBook())) {
                    mCPayment.setNoFBook(genCodeService.getCodeVoucher(11, 0));
                }
            }
        }
        if (!utilsRepository.checkDuplicateNoVoucher(mCPayment.getNoFBook(), mCPayment.getNoMBook(), mCPayment.getTypeLedger(), mCPayment.getId())) {
            mcPaymentSaveDTO.setmCPayment(mCPayment);
            mcPaymentSaveDTO.setStatus(1);
            return mcPaymentSaveDTO;
        }
        if (currentUserLoginAndOrg.isPresent()) {
            mCPayment.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            mcP = mCPaymentRepository.save(mCPayment);
//            utilsRepository.updateGencode(mcP.getNoFBook(), mcP.getNoMBook(), 11, mcP.getTypeLedger());
            List<RefVoucher> refVouchers = new ArrayList<>();
            for (RefVoucherDTO item : mCPayment.getViewVouchers() != null ? mCPayment.getViewVouchers() : new ArrayList<RefVoucherDTO>()) {
                RefVoucher refVoucher = new RefVoucher();
                refVoucher.setCompanyID(mcP.getCompanyID());
                refVoucher.setRefID1(mcP.getId());
                refVoucher.setRefID2(item.getRefID2());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                refVouchers.add(refVoucher);
            }
            refVoucherRepository.deleteByRefID1(mcP.getId());
            refVoucherRepository.saveAll(refVouchers);
            mcP.setViewVouchers(mCPayment.getViewVouchers());
            MessageDTO messageDTO = new MessageDTO();
            if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {
                if (!generalLedgerService.record(mcP, messageDTO)) {
                    mcPaymentSaveDTO.setStatus(2);
                    mcPaymentSaveDTO.setMsg(messageDTO.getMsgError());
                } else {
                    mcPaymentSaveDTO.setStatus(0);
                    mcP.setRecorded(true);
                    mCPaymentRepository.save(mcP);
                }
            } else {
                mcPaymentSaveDTO.setStatus(0);
                if (mcP.getRecorded() == null) {
                    mcP.setRecorded(false);
                    mCPaymentRepository.save(mcP);
                }
//                mcP.setRecorded(false);
            }
            mcPaymentSaveDTO.setmCPayment(mcP);
            return mcPaymentSaveDTO;
        }
        throw new BadRequestAlertException("Không thể lưu Phiếu chi", "", "");
    }

    @Override
    public HandlingResultDTO multiDelete(List<MCPayment> mcPayments) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        handlingResultDTO.setCountTotalVouchers(mcPayments.size());
        List<MCPayment> listDelete = new ArrayList<>(mcPayments);
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UUID companyID = currentUserLoginAndOrg.get().getOrg();
        for (int i = 0; i < mcPayments.size(); i++) {
            if (Boolean.TRUE.equals(mcPayments.get(i).getRecorded())) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang được ghi sổ !");
                BeanUtils.copyProperties(mcPayments.get(i), viewVoucherNo);
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(mcPayments.get(i));
            }
        }
        if (listDelete.size() == 0) {
            throw new BadRequestAlertException("Các chứng từ đang được ghi sổ, không thể xoá", "mBTellerPaper", "errorDeleteList");
        }
        // get ListID chung tu theo Type ID
        List<UUID> uuidList_MC = new ArrayList<>();
        List<UUID> uuidList_MC_MH = new ArrayList<>();
        List<UUID> uuidList_MC_MDV = new ArrayList<>();
        for (MCPayment mcPayment : listDelete) {
            if (mcPayment.getTypeID() == PHIEU_CHI || mcPayment.getTypeID() == PHIEU_CHI_TRA_TIEN_NHA_CUNG_CAP) {
                uuidList_MC.add(mcPayment.getId());
            } else if (mcPayment.getTypeID() == PHIEU_CHI_MUA_HANG) {
                uuidList_MC_MH.add(mcPayment.getId());
            } else if (mcPayment.getTypeID() == PHIEU_CHI_MUA_DICH_VU) {
                uuidList_MC_MDV.add(mcPayment.getId());
            }
        }
        // Gan' TypeName
        List<Type> types = typeRepository.findAllByIsActive();
        for (ViewVoucherNo viewVoucherNo : viewVoucherNoListFail) {
            viewVoucherNo.setTypeName(types.stream().filter(n -> n.getId().equals(viewVoucherNo.getTypeID())).findFirst().get().getTypeName());
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(listDelete.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        // Xoa chung tu phiếu chi - phiếu chi trả tiền ncc
        if (uuidList_MC.size() > 0) {
            mCPaymentRepository.multiDeleteByID(companyID, uuidList_MC);
        }
        // Xoa chung tu mua hang
        if (uuidList_MC_MH.size() > 0) {
            List<UUID> ppInvoiceList = ppInvoiceRepository.findByListIDMBCreditCard(uuidList_MC_MH).stream().map(PPInvoice::getId).collect(Collectors.toList());
            if (ppInvoiceList.size() > 0) {
                List<UUID> uuidListRSIO = rsInwardOutwardRepository.findByListPPInvoiceID(ppInvoiceList);
                rsInwardOutwardRepository.deleteByListID(uuidListRSIO);
                List<PPInvoiceDetails> ppInvoiceDetailsList = ppInvoiceDetailsRepository.findAllByppInvoiceIdIn(ppInvoiceList);
                // Get List PPOrderDetail after save PPService
                List<PPOrderDetail> ppOrderDetails = utilsService.calculatingQuantityReceiptPPOrder(
                    ppInvoiceDetailsList.stream().filter(x -> x.getPpOrderDetailId() != null)
                        .map(x -> new PPOrderDTO(x.getPpOrderDetailId(), BigDecimal.ZERO.subtract(x.getQuantity())))
                        .collect(Collectors.toList()),
                    new ArrayList<>(), false);
                // update PPOrderDetails
                pporderdetailRepository.saveAll(ppOrderDetails);
                pporderRepository.updateStatus(ppOrderDetails.stream()
                    .collect(Collectors.groupingBy(PPOrderDetail::getpPOrderID)).keySet()
                    .stream().map(Utils::uuidConvertToGUID).map(UUID::toString).collect(Collectors.joining(";")));
                refVoucherRepository.deleteByRefID1sOrRefID2s(uuidList_MC_MH);
                // xóa phân bổ chi phí
                pPInvoiceDetailCostRepository.deleteListRefID(ppInvoiceList);
                mCPaymentRepository.multiDeleteByID(companyID, uuidList_MC_MH);
                ppInvoiceRepository.deleteByListID(ppInvoiceList);
            }
        }
//         Xoa chung tu mua dich vu
        if (uuidList_MC_MDV.size() > 0) {
            List<UUID> listIDPPService = ppServiceRepository.findByListIDMBCreditCard(uuidList_MC_MDV).stream().map(PPService::getId).collect(Collectors.toList());
            if (listIDPPService.size() > 0) {
                List<PPServiceDetail> ppServiceDetails = ppServiceDetailRepository.findAllByPpServiceIDIn(listIDPPService);
                List<PPOrderDetail> ppOrderDetails = utilsService.calculatingQuantityReceiptPPOrder(
                    ppServiceDetails.stream().filter(x -> x.getPpOrderDetailID() != null).map(x -> new PPOrderDTO(x.getPpOrderDetailID(), BigDecimal.ZERO.subtract(x.getQuantity()))).collect(Collectors.toList()),
                    new ArrayList<>(), false);
                pPInvoiceDetailCostRepository.cleanPPServiceIdByListID(listIDPPService);
                // update PPOrderDetails
                refVoucherRepository.deleteByRefID1sOrRefID2s(listIDPPService);
                pporderdetailRepository.saveAll(ppOrderDetails);
                pporderRepository.updateStatus(ppOrderDetails.stream()
                    .collect(Collectors.groupingBy(PPOrderDetail::getpPOrderID)).keySet()
                    .stream().map(Utils::uuidConvertToGUID).map(UUID::toString).collect(Collectors.joining(";")));
                ppServiceRepository.deleteByListID(listIDPPService);
                mCPaymentRepository.multiDeleteByID(companyID, uuidList_MC_MDV);
            }
        }
        return handlingResultDTO;
    }

    @Override
    public HandlingResultDTO multiUnrecord(List<MCPayment> mcPayments) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        handlingResultDTO.setCountTotalVouchers(mcPayments.size());
        List<MCPayment> listDelete = new ArrayList<>(mcPayments);
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        org.joda.time.LocalDate dateClosed = org.joda.time.LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (closeDateStr != null && !closeDateStr.equals("")) {
            dateClosed = org.joda.time.LocalDate.parse(closeDateStr);
        }
        for (MCPayment mtp : mcPayments) {
            org.joda.time.LocalDate postedDate = org.joda.time.LocalDate.parse(mtp.getPostedDate().toString());
            if (closeDateStr != null && Boolean.TRUE.equals(mtp.isRecorded()) && !closeDateStr.equals("") && dateClosed != null && (dateClosed.isAfter(postedDate) || dateClosed.equals(postedDate)))
            {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đã khóa sổ!");
                viewVoucherNo.setPostedDate(mtp.getPostedDate());
                viewVoucherNo.setDate(mtp.getDate());
                viewVoucherNo.setTypeID(mtp.getTypeID());
                viewVoucherNo.setNoFBook(mtp.getNoFBook());
                viewVoucherNo.setNoMBook(mtp.getNoMBook());
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(mtp);
            } else if (Boolean.FALSE.equals(mtp.isRecorded())) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang bỏ ghi sổ !");
                BeanUtils.copyProperties(mtp, viewVoucherNo);
                viewVoucherNo.setTypeID(mtp.getTypeID());
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(mtp);
            }
        }
        // Gan' TypeName
        List<Type> types = typeRepository.findAllByIsActive();
        for (ViewVoucherNo viewVoucherNo : viewVoucherNoListFail) {
            viewVoucherNo.setTypeName(types.stream().filter(n -> n.getId().equals(viewVoucherNo.getTypeID())).findFirst().get().getTypeName());
        }
        List<UUID> uuidList_MC = new ArrayList<>();
        List<UUID> uuidList_MC_MUA_HANG = new ArrayList<>();
        List<UUID> uuidList_MC_MUA_DICH_VU = new ArrayList<>();
        List<UUID> uuidListRS = new ArrayList<>();
        List<UUID> uuidList = new ArrayList<>();
        for (MCPayment mcPayment : listDelete) {
            if (mcPayment.getTypeID() == PHIEU_CHI || mcPayment.getTypeID() == PHIEU_CHI_TRA_TIEN_NHA_CUNG_CAP) {
                uuidList_MC.add(mcPayment.getId());
            } else if (mcPayment.getTypeID() == PHIEU_CHI_MUA_HANG) {
                uuidList_MC_MUA_HANG.add(mcPayment.getId());
            } else if (mcPayment.getTypeID() == PHIEU_CHI_MUA_DICH_VU) {
                uuidList_MC_MUA_DICH_VU.add(mcPayment.getId());
            }
            uuidList.add(mcPayment.getId());
        }
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();

        if (uuidList.size() > 0) {
            boolean rs = false;
            mCPaymentRepository.multiUnRecord(uuidList, currentUserLoginAndOrg.get().getOrg());
            mCPaymentRepository.deleteGL(uuidList, currentUserLoginAndOrg.get().getOrg());
            if (uuidList_MC_MUA_HANG.size() > 0) {
                List<PPInvoice> ppInvoiceList = ppInvoiceRepository.findByListIDMBCreditCard(uuidList_MC_MUA_HANG);
                List<UUID> listIDPPInvoice = ppInvoiceList.stream().map(PPInvoice::getId).collect(Collectors.toList());
                ppInvoiceRepository.updateMultiUnRecord(listIDPPInvoice);
                ppDiscountReturnRepository.updateListPPInvoiceIDAndPPInvoiceDetailIDToNull(listIDPPInvoice);
                saInvoiceRepository.updateListPPInvoiceIDAndPPInvoiceDetailIDToNull(listIDPPInvoice);
                Optional<RSInwardOutward> rsInwardOutward = Optional.empty();
                ppInvoiceRepository.updateMultiUnRecord(listIDPPInvoice);
                List<UUID> listID_MHQK = ppInvoiceList.stream().filter(n -> Boolean.TRUE.equals(n.isStoredInRepository())).map(PPInvoice::getId).collect(Collectors.toList());
                List<UUID> listID_MHKQK = ppInvoiceList.stream().filter(n -> Boolean.FALSE.equals(n.isStoredInRepository())).map(PPInvoice::getId).collect(Collectors.toList());
                if (listID_MHQK.size() > 0) {
                    List<UUID> rsInwardOutWardIDList = rsInwardOutwardRepository.findByListPPInvoiceID(listID_MHQK);
                    rs = unrecord(listID_MHQK, rsInwardOutWardIDList);
                    if (rs) {
                        rsInwardOutwardRepository.updateUnrecord(rsInwardOutWardIDList);
                    }
                }
            }
            if (uuidList_MC_MUA_DICH_VU.size() > 0) {
                List<PPService> ppServiceList = ppServiceRepository.findByListIDMBCreditCard(uuidList_MC_MUA_DICH_VU);
                List<UUID> uuidPPService = ppServiceList.stream().map(PPService::getId).collect(Collectors.toList());
                viewVoucherNoRespository.deletePaymentVoucherInID(generalLedgerRepository.findAllPaymentVoucherByPPInvoiceId(uuidPPService));
                List<UUID> paymentVouchers = ppServiceRepository.findAllPaymentVoucherIDByID(uuidPPService);
                List<UUID> uuidsRecord = new ArrayList<>(uuidPPService);
                uuidsRecord.addAll(paymentVouchers);
                generalLedgerRepository.unrecordList(uuidsRecord);
                ppServiceRepository.updateMultiUnRecord(uuidPPService);
                mCPaymentRepository.updateRecordInId(paymentVouchers, false);
            }
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(listDelete.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        return handlingResultDTO;
    }

    @Override
    public boolean unrecord(List<UUID> refID, List<UUID> repositoryLedgerID) {
        if (repositoryLedgerID != null) {
            if (!repositoryLedgerRepository.unrecordList(repositoryLedgerID)) {
                return false;
            }
        }
        return generalLedgerRepository.unrecordList(refID);
    }
}
