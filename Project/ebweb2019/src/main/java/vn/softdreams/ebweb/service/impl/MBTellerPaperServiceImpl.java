package vn.softdreams.ebweb.service.impl;

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
import vn.softdreams.ebweb.service.dto.cashandbank.MBTellerPaperDTO;
import vn.softdreams.ebweb.service.util.*;
import vn.softdreams.ebweb.web.rest.dto.MBTellerPaperSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.Constants.PPInvoiceType.*;
import static vn.softdreams.ebweb.service.util.Constants.PPInvoiceType.TYPE_ID_STM_MUA_DICH_VU;
import static vn.softdreams.ebweb.service.util.TypeConstant.*;

/**
 * Service Implementation for managing MBTellerPaper.
 */
@Service
@Transactional
public class MBTellerPaperServiceImpl implements MBTellerPaperService {

    private final Logger log = LoggerFactory.getLogger(MBTellerPaperServiceImpl.class);

    private final MBTellerPaperRepository mBTellerPaperRepository;
    private final RefVoucherRepository refVoucherRepository;
    private final UserService userService;
    private final UtilsService utilsService;
    private final String PHIEN_SoLamViec = "PHIEN_SoLamViec";
    private final String TCKHAC_GhiSo = "TCKHAC_GhiSo";
    @Autowired
    UtilsRepository utilsRepository;

    @Autowired
    private GenCodeService genCodeService;

    @Autowired
    private GeneralLedgerService generalLedgerService;
    private final TypeRepository typeRepository;
    private final PPInvoiceRepository ppInvoiceRepository;
    private final PPInvoiceDetailsRepository ppInvoiceDetailsRepository;
    private final PporderdetailRepository pporderdetailRepository;
    private final PporderRepository pporderRepository;
    private final PPInvoiceDetailCostRepository pPInvoiceDetailCostRepository;
    private final RSInwardOutwardRepository rsInwardOutwardRepository;
    private final PPServiceRepository ppServiceRepository;
    private final PPServiceDetailRepository ppServiceDetailRepository;
    private final PPDiscountReturnRepository ppDiscountReturnRepository;
    private final SAInvoiceRepository saInvoiceRepository;
    private final RepositoryLedgerRepository repositoryLedgerRepository;
    private final GeneralLedgerRepository generalLedgerRepository;
    private final MBTellerPaperDetailVendorRepository mbTellerPaperDetailVendorRepository;

    public MBTellerPaperServiceImpl(MBTellerPaperRepository mBTellerPaperRepository, RefVoucherRepository refVoucherRepository,
                                    UserService userService, UtilsService utilsService, TypeRepository typeRepository, PPInvoiceRepository ppInvoiceRepository,
                                    PPInvoiceDetailsRepository ppInvoiceDetailsRepository, PporderdetailRepository pporderdetailRepository,
                                    PporderRepository pporderRepository, PPInvoiceDetailCostRepository ppInvoiceDetailCostRepository, RSInwardOutwardRepository rsInwardOutwardRepository,
                                    PPServiceRepository ppServiceRepository, PPServiceDetailRepository ppServiceDetailRepository,
                                    PPDiscountReturnRepository ppDiscountReturnRepository, SAInvoiceRepository saInvoiceRepository,
                                    RepositoryLedgerRepository repositoryLedgerRepository, GeneralLedgerRepository generalLedgerRepository,
                                    MBTellerPaperDetailVendorRepository mbTellerPaperDetailVendorRepository) {
        this.mBTellerPaperRepository = mBTellerPaperRepository;
        this.refVoucherRepository = refVoucherRepository;
        this.userService = userService;
        this.utilsService = utilsService;
        this.typeRepository = typeRepository;
        this.ppInvoiceDetailsRepository = ppInvoiceDetailsRepository;
        this.ppInvoiceRepository = ppInvoiceRepository;
        this.pporderdetailRepository = pporderdetailRepository;
        this.pporderRepository = pporderRepository;
        this.rsInwardOutwardRepository = rsInwardOutwardRepository;
        this.ppServiceRepository = ppServiceRepository;
        this.ppServiceDetailRepository = ppServiceDetailRepository;
        this.pPInvoiceDetailCostRepository = ppInvoiceDetailCostRepository;
        this.ppDiscountReturnRepository = ppDiscountReturnRepository;
        this.saInvoiceRepository = saInvoiceRepository;
        this.repositoryLedgerRepository = repositoryLedgerRepository;
        this.generalLedgerRepository = generalLedgerRepository;
        this.mbTellerPaperDetailVendorRepository = mbTellerPaperDetailVendorRepository;
    }

    /**
     * Save a mBTellerPaper.
     *
     * @param mBTellerPaper the entity to save
     * @return the persisted entity
     */
    @Override
    public MBTellerPaper save(MBTellerPaper mBTellerPaper) {
        log.debug("Request to save MBTellerPaper : {}", mBTellerPaper);
        MBTellerPaper mTP = new MBTellerPaper();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            mBTellerPaper.setCompanyId(currentUserLoginAndOrg.get().getOrg());
            if (!utilsService.checkQuantityLimitedNoVoucher()) {
                throw new BadRequestAlertException("Số chứng từ hiện tại đã vượt quá số cho phép của Gói sử dụng", "", "noVoucherLimited");
            }
            mTP = mBTellerPaperRepository.save(mBTellerPaper);
            List<RefVoucher> refVouchers = new ArrayList<>();
            for (RefVoucherDTO item : mBTellerPaper.getViewVouchers() != null ? mBTellerPaper.getViewVouchers() : new ArrayList<RefVoucherDTO>()) {
                RefVoucher refVoucher = new RefVoucher();
                refVoucher.setCompanyID(mTP.getCompanyId());
                refVoucher.setRefID1(mTP.getId());
                refVoucher.setRefID2(item.getRefID2());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                refVouchers.add(refVoucher);
            }
            refVoucherRepository.deleteByRefID1(mTP.getId());
            refVoucherRepository.saveAll(refVouchers);
            mTP.setViewVouchers(mBTellerPaper.getViewVouchers());
            return mTP;
        }
        throw new BadRequestAlertException("Không thể lưu báo nợ", "", "");
    }

    /**
     * Save a mBTellerPaper.
     *
     * @param mBTellerPaper the entity to save
     * @return the persisted entity
     */
    @Override
    public MBTellerPaperSaveDTO saveDTO(MBTellerPaper mBTellerPaper, Boolean isEdit) {
        log.debug("Request to save MBTellerPaper : {}", mBTellerPaper);
        MBTellerPaper mTP = new MBTellerPaper();
        MBTellerPaperSaveDTO mBTellerPaperSaveDTO = new MBTellerPaperSaveDTO();
        // neu chua co id thi tao moi
        if (mBTellerPaper.getId() == null) {
            if (!utilsService.checkQuantityLimitedNoVoucher()) {
                throw new BadRequestAlertException("Số chứng từ hiện tại đã vượt quá số cho phép của Gói sử dụng", "", "noVoucherLimited");
            }
            mBTellerPaper.setId(UUID.randomUUID());
            // gan id cha cho details con
            for (MBTellerPaperDetails details : mBTellerPaper.getmBTellerPaperDetails()) {
                if (details.getId() != null) {
                    details.setId(null);
                }
                details.setmBTellerPaperId(mBTellerPaper.getId());
            }
            for (MBTellerPaperDetailTax detailTax : mBTellerPaper.getmBTellerPaperDetailTaxs()) {
                if (detailTax.getId() != null) {
                    detailTax.setId(null);
                }
                detailTax.setmBTellerPaperId(mBTellerPaper.getId());
            }
            for (MBTellerPaperDetailVendor detailVendor : mBTellerPaper.getmBTellerPaperDetailVendor()) {
                if (detailVendor.getId() != null) {
                    detailVendor.setId(null);
                }
                detailVendor.setmBTellerPaperID(mBTellerPaper.getId());
            }
        }

        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        if (currentUserLoginAndOrg.isPresent()) {
            mBTellerPaper.setCompanyId(currentUserLoginAndOrg.get().getOrg());
            int typeGroupID = 0;
            switch (mBTellerPaper.getTypeId()) {
                case TYPE_BAO_NO_UNC:
                case TYPE_ID_UNC_MUA_HANG:
                case TYPE_ID_UNC_MUA_DICH_VU:
                case BAO_NO_UNC_TRA_TIEN_NCC:
                    typeGroupID = 12;
                    break;
                case TYPE_BAO_NO_SCK:
                case TYPE_ID_SCK_MUA_HANG:
                case TYPE_ID_SCK_MUA_DICH_VU:
                case BAO_NO_SCK_TRA_TIEN_NCC:
                    typeGroupID = 13;
                    break;
                case TYPE_BAO_NO_STM:
                case TYPE_ID_STM_MUA_HANG:
                case TYPE_ID_STM_MUA_DICH_VU:
                case BAO_NO_STM_TRA_TIEN_NCC:
                    typeGroupID = 14;
                    break;
            }
            if (mBTellerPaper.getTypeLedger() == 2) {
                if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(PHIEN_SoLamViec)).findAny().get().getData().equals("0")) {
                    if (StringUtils.isEmpty(mBTellerPaper.getNoMBook())) {
                        mBTellerPaper.setNoMBook(genCodeService.getCodeVoucher(typeGroupID, 1));
                    }
                } else {
                    if (StringUtils.isEmpty(mBTellerPaper.getNoFBook())) {
                        mBTellerPaper.setNoFBook(genCodeService.getCodeVoucher(typeGroupID, 0));
                    }
                }
            }
            if (!utilsRepository.checkDuplicateNoVoucher(mBTellerPaper.getNoFBook(), mBTellerPaper.getNoMBook(), mBTellerPaper.getTypeLedger(), mBTellerPaper.getId())) {
                mBTellerPaperSaveDTO.setmBTellerPaper(mBTellerPaper);
                mBTellerPaperSaveDTO.setStatus(1);
                return mBTellerPaperSaveDTO;
            }
            mTP = mBTellerPaperRepository.save(mBTellerPaper);
//            Boolean resultUpdateGenCode = utilsRepository.updateGencode(mTP.getNoFBook(), mTP.getNoMBook(), typeGroupID, mTP.getTypeLedger());
//            if (!resultUpdateGenCode) {
//                // update gencode that bai
//                mBTellerPaperSaveDTO.setmBTellerPaper(mBTellerPaper);
//                mBTellerPaperSaveDTO.setStatus(3);
//                return mBTellerPaperSaveDTO;
//            }
            List<RefVoucher> refVouchers = new ArrayList<>();
            for (RefVoucherDTO item : mBTellerPaper.getViewVouchers() != null ? mBTellerPaper.getViewVouchers() : new ArrayList<RefVoucherDTO>()) {
                RefVoucher refVoucher = new RefVoucher();
                refVoucher.setCompanyID(mTP.getCompanyId());
                refVoucher.setRefID1(mTP.getId());
                refVoucher.setRefID2(item.getRefID2());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                refVouchers.add(refVoucher);
            }
            refVoucherRepository.deleteByRefID1(mTP.getId());
            refVoucherRepository.saveAll(refVouchers);
            mTP.setViewVouchers(mBTellerPaper.getViewVouchers());
            MessageDTO messageDTO = new MessageDTO();
            if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {
                if (!generalLedgerService.record(mTP, messageDTO)) {
                    mBTellerPaperSaveDTO.setStatus(2);
                    mBTellerPaperSaveDTO.setMsg(messageDTO.getMsgError());
                } else {
                    mBTellerPaperSaveDTO.setStatus(0);
                    mTP.setRecorded(true);
                    mBTellerPaperRepository.save(mTP);
                }
            } else {
                mBTellerPaperSaveDTO.setStatus(0);
                if (mTP.getRecorded() == null) {
                    mTP.setRecorded(false);
                    mBTellerPaperRepository.save(mTP);
                }
//                mcP.setRecorded(false);
            }
            mBTellerPaperSaveDTO.setmBTellerPaper(mTP);
            return mBTellerPaperSaveDTO;
        }
        throw new BadRequestAlertException("Không thể lưu báo nợ", "", "");
    }

    @Override
    public byte[] exportPdf(SearchVoucher searchVoucher1) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        UserDTO userDTO = userService.getAccount();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            List<MBTellerPaperDTO> mBTellerPapers = mBTellerPaperRepository.findAllForExport(searchVoucher1, currentUserLoginAndOrg.get().getOrg());
            return PdfUtils.writeToFile(mBTellerPapers, ExcelConstant.MBTellerPaper.HEADER, ExcelConstant.MBTellerPaper.FIELD, userDTO);
        }
        return null;
    }

    @Override
    public byte[] exportExcel(SearchVoucher searchVoucher1) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            List<MBTellerPaperDTO> mBTellerPapers = mBTellerPaperRepository.findAllForExport(searchVoucher1, currentUserLoginAndOrg.get().getOrg());
            return ExcelUtils.writeToFile(mBTellerPapers, ExcelConstant.MBTellerPaper.NAME, ExcelConstant.MBTellerPaper.HEADER, ExcelConstant.MBTellerPaper.FIELD);
        }
        return null;
    }

    @Override
    public HandlingResultDTO multiDelete(List<MBTellerPaper> mbTellerPapers) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        handlingResultDTO.setCountTotalVouchers(mbTellerPapers.size());
        List<MBTellerPaper> listDelete = mbTellerPapers.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        for (int i = 0; i < mbTellerPapers.size(); i++) {
            if (Boolean.TRUE.equals(mbTellerPapers.get(i).getRecorded())) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang được ghi sổ !");
                BeanUtils.copyProperties(mbTellerPapers.get(i), viewVoucherNo);
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(mbTellerPapers.get(i));
            }
        }
        if (listDelete.size() == 0) {
            throw new BadRequestAlertException("Các chứng từ đang được ghi sổ, không thể xoá", "mBTellerPaper", "errorDeleteList");
        }
        // get ListID chung tu theo Type ID
        List<UUID> uuidList_UNC_UNCNCC = new ArrayList<>();
        List<UUID> uuidList_UNC_MH = new ArrayList<>();
        List<UUID> uuidList_UNC_MDV = new ArrayList<>();
        for (int i = 0; i < listDelete.size(); i++) {
            if (listDelete.get(i).getTypeId() == 120 || listDelete.get(i).getTypeId() == 130 || listDelete.get(i).getTypeId() == 140 ||
                listDelete.get(i).getTypeId() == 126 || listDelete.get(i).getTypeId() == 134 || listDelete.get(i).getTypeId() == 144) {
                uuidList_UNC_UNCNCC.add(listDelete.get(i).getId());
            } else if (listDelete.get(i).getTypeId() == 125 || listDelete.get(i).getTypeId() == 131 || listDelete.get(i).getTypeId() == 141) {
                uuidList_UNC_MH.add(listDelete.get(i).getId());
            } else if (listDelete.get(i).getTypeId() == 124 || listDelete.get(i).getTypeId() == 133 || listDelete.get(i).getTypeId() == 143) {
                uuidList_UNC_MDV.add(listDelete.get(i).getId());
            }
        }
        // Gan' TypeName
        for (int i = 0; i < viewVoucherNoListFail.size(); i++) {
            viewVoucherNoListFail.get(i).setTypeName(typeRepository.findTypeNameByTypeID(viewVoucherNoListFail.get(i).getTypeID()));
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(listDelete.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        // Xoa chung tu The tin dung + The tin dung NCC
        if (uuidList_UNC_UNCNCC.size() > 0) {
            mBTellerPaperRepository.multiDeleteMBTellerPaper(currentUserLoginAndOrg.get().getOrg(), uuidList_UNC_UNCNCC);
            mBTellerPaperRepository.multiDeleteChildMBTellerPaper("MBTellerPaperDetail", uuidList_UNC_UNCNCC);
            mBTellerPaperRepository.multiDeleteChildMBTellerPaper("MBTellerPaperDetailTax", uuidList_UNC_UNCNCC);
            mBTellerPaperRepository.multiDeleteChildMBTellerPaper("MBTellerPaperDetailVendor", uuidList_UNC_UNCNCC);
//            mBDepositRepository.multiDeleteGeneralLedger(currentUserLoginAndOrg.get().getOrg(), multiDelete);
            refVoucherRepository.deleteByRefID1sOrRefID2s(uuidList_UNC_UNCNCC);
        }
        // Xoa chung tu The tin dung mua hang
        if (uuidList_UNC_MH.size() > 0) {
            List<UUID> ppInvoiceList = ppInvoiceRepository.findByListIDMBCreditCard(uuidList_UNC_MH).stream().map(PPInvoice::getId).collect(Collectors.toList());
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
                refVoucherRepository.deleteByRefID1sOrRefID2s(uuidList_UNC_MH);
                // xóa phân bổ chi phí
                pPInvoiceDetailCostRepository.deleteListRefID(ppInvoiceList);
                mBTellerPaperRepository.deleteByListID(uuidList_UNC_MH);
                ppInvoiceRepository.deleteByListID(ppInvoiceList);
            }
        }
//         Xoa chung tu the tin dung mua dich vu
        if (uuidList_UNC_MDV.size() > 0) {
            List<UUID> listIDPPService = ppServiceRepository.findByListIDMBCreditCard(uuidList_UNC_MDV).stream().map(PPService::getId).collect(Collectors.toList());
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
                mBTellerPaperRepository.deleteByListID(uuidList_UNC_MDV);
            }
        }
        return handlingResultDTO;
    }

    @Override
    public HandlingResultDTO multiUnRecord(List<MBTellerPaper> mbTellerPapers) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        handlingResultDTO.setCountTotalVouchers(mbTellerPapers.size());
        List<MBTellerPaper> listDelete = mbTellerPapers.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        org.joda.time.LocalDate dateClosed = org.joda.time.LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (!closeDateStr.equals("") && closeDateStr != null) {
            dateClosed = org.joda.time.LocalDate.parse(closeDateStr);
        }
        for (MBTellerPaper mtp : mbTellerPapers) {
            org.joda.time.LocalDate postedDate = org.joda.time.LocalDate.parse(mtp.getPostedDate().toString());
            if (Boolean.TRUE.equals(mtp.isRecorded()) && !closeDateStr.equals("") && closeDateStr != null && dateClosed != null && (dateClosed.isAfter(postedDate) || dateClosed.equals(postedDate))) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đã khóa sổ!");
                viewVoucherNo.setPostedDate(mtp.getPostedDate());
                viewVoucherNo.setDate(mtp.getDate());
                viewVoucherNo.setNoFBook(mtp.getNoFBook());
                viewVoucherNo.setNoMBook(mtp.getNoMBook());
                if (mtp.getTypeId() == TYPE_BAO_NO_UNC) {
                    viewVoucherNo.setTypeName("Uỷ nhiệm chi");
                } else if (mtp.getTypeId() == TYPE_BAO_NO_SCK) {
                    viewVoucherNo.setTypeName("Séc chuyển khoản");
                } else if (mtp.getTypeId() == TYPE_BAO_NO_STM) {
                    viewVoucherNo.setTypeName("Séc tiền mặt");
                } else if (mtp.getTypeId() == BAO_NO_SCK_TRA_TIEN_NCC) {
                    viewVoucherNo.setTypeName("Séc chuyển khoản nhà cung cấp");
                } else if (mtp.getTypeId() == BAO_NO_STM_TRA_TIEN_NCC) {
                    viewVoucherNo.setTypeName("Séc tiền mặt nhà cung cấp");
                } else if (mtp.getTypeId() == BAO_NO_UNC_TRA_TIEN_NCC) {
                    viewVoucherNo.setTypeName("Uỷ nhiệm chi nhà cung cấp");
                } else if (mtp.getTypeId() == TYPE_ID_MUA_HANG_UY_NHIEM_CHI) {
                    viewVoucherNo.setTypeName("Mua hàng uỷ nhiệm chi");
                } else if (mtp.getTypeId() == TYPE_ID_MUA_HANG_SEC_CK) {
                    viewVoucherNo.setTypeName("Séc chuyển khoản mua hàng");
                } else if (mtp.getTypeId() == TYPE_ID_MUA_HANG_SEC_TIEN_MAT) {
                    viewVoucherNo.setTypeName("Séc tiền mặt mua hàng");
                } else if (mtp.getTypeId() == TYPE_ID_UNC_MUA_DICH_VU) {
                    viewVoucherNo.setTypeName("Uỷ nhiệm chi mua dịch vụ");
                } else if (mtp.getTypeId() == TYPE_ID_SCK_MUA_DICH_VU) {
                    viewVoucherNo.setTypeName("Séc chuyển khoản mua dịch vụ");
                } else if (mtp.getTypeId() == TYPE_ID_STM_MUA_DICH_VU) {
                    viewVoucherNo.setTypeName("Séc tiền mặt mua dịch vụ");
                }
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(mtp);
            } else if (Boolean.FALSE.equals(mtp.isRecorded())) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang bỏ ghi sổ !");
                BeanUtils.copyProperties(mtp, viewVoucherNo);
                viewVoucherNo.setTypeID(mtp.getTypeId());
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(mtp);
            }
        }
        if (viewVoucherNoListFail.size() > 0) {
            for (ViewVoucherNo viewVoucherNo : viewVoucherNoListFail) {
                if (viewVoucherNo.getTypeID() == TYPE_BAO_NO_UNC) {
                    viewVoucherNo.setTypeName("Uỷ nhiệm chi");
                } else if (viewVoucherNo.getTypeID() == TYPE_BAO_NO_SCK) {
                    viewVoucherNo.setTypeName("Séc chuyển khoản");
                } else if (viewVoucherNo.getTypeID() == TYPE_BAO_NO_STM) {
                    viewVoucherNo.setTypeName("Séc tiền mặt");
                } else if (viewVoucherNo.getTypeID() == BAO_NO_SCK_TRA_TIEN_NCC) {
                    viewVoucherNo.setTypeName("Séc chuyển khoản nhà cung cấp");
                } else if (viewVoucherNo.getTypeID() == BAO_NO_STM_TRA_TIEN_NCC) {
                    viewVoucherNo.setTypeName("Séc tiền mặt nhà cung cấp");
                } else if (viewVoucherNo.getTypeID() == BAO_NO_UNC_TRA_TIEN_NCC) {
                    viewVoucherNo.setTypeName("Uỷ nhiệm chi nhà cung cấp");
                } else if (viewVoucherNo.getTypeID() == TYPE_ID_MUA_HANG_UY_NHIEM_CHI) {
                    viewVoucherNo.setTypeName("Mua hàng uỷ nhiệm chi");
                } else if (viewVoucherNo.getTypeID() == TYPE_ID_MUA_HANG_SEC_CK) {
                    viewVoucherNo.setTypeName("Séc chuyển khoản mua hàng");
                } else if (viewVoucherNo.getTypeID() == TYPE_ID_MUA_HANG_SEC_TIEN_MAT) {
                    viewVoucherNo.setTypeName("Séc tiền mặt mua hàng");
                } else if (viewVoucherNo.getTypeID() == TYPE_ID_UNC_MUA_DICH_VU) {
                    viewVoucherNo.setTypeName("Uỷ nhiệm chi mua dịch vụ");
                } else if (viewVoucherNo.getTypeID() == TYPE_ID_SCK_MUA_DICH_VU) {
                    viewVoucherNo.setTypeName("Séc chuyển khoản mua dịch vụ");
                } else if (viewVoucherNo.getTypeID() == TYPE_ID_STM_MUA_DICH_VU) {
                    viewVoucherNo.setTypeName("Séc tiền mặt mua dịch vụ");
                }
            }
        }
        List<UUID> uuidList_UNC = new ArrayList<>();
        List<UUID> uuidList_UNC_MUA_HANG = new ArrayList<>();
        List<UUID> uuidList_UNC_MUA_DICH_VU = new ArrayList<>();
        List<UUID> uuidListRS = new ArrayList<>();
        List<UUID> uuidList = new ArrayList<>();
        for (int i = 0; i < listDelete.size(); i++) {
            if (listDelete.get(i).getTypeId() == TYPE_BAO_NO_UNC || listDelete.get(i).getTypeId() == TYPE_BAO_NO_SCK ||
                listDelete.get(i).getTypeId() == TYPE_BAO_NO_STM || listDelete.get(i).getTypeId() == BAO_NO_SCK_TRA_TIEN_NCC ||
                listDelete.get(i).getTypeId() == BAO_NO_STM_TRA_TIEN_NCC || listDelete.get(i).getTypeId() == BAO_NO_UNC_TRA_TIEN_NCC
            ) {
                uuidList_UNC.add(listDelete.get(i).getId());
            } else if (listDelete.get(i).getTypeId() == TYPE_ID_UNC_MUA_HANG || listDelete.get(i).getTypeId() == TYPE_ID_STM_MUA_HANG
                || listDelete.get(i).getTypeId() == TYPE_ID_SCK_MUA_HANG) {
                uuidList_UNC_MUA_HANG.add(listDelete.get(i).getId());
            } else if (listDelete.get(i).getTypeId() == TYPE_ID_UNC_MUA_DICH_VU || listDelete.get(i).getTypeId() == TYPE_ID_SCK_MUA_DICH_VU || listDelete.get(i).getTypeId() == TYPE_ID_STM_MUA_DICH_VU) {
                uuidList_UNC_MUA_DICH_VU.add(listDelete.get(i).getId());
            }
            uuidList.add(listDelete.get(i).getId());
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(listDelete.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();

        if (uuidList.size() > 0) {
            boolean rs = false;
            mBTellerPaperRepository.multiUnRecord(uuidList, currentUserLoginAndOrg.get().getOrg());
            mBTellerPaperRepository.deleteGL(uuidList, currentUserLoginAndOrg.get().getOrg());
            if (uuidList_UNC_MUA_HANG.size() > 0) {
                List<PPInvoice> ppInvoiceList = ppInvoiceRepository.findByListIDMBCreditCard(uuidList_UNC_MUA_HANG);
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
            if (uuidList_UNC_MUA_DICH_VU.size() > 0) {
                List<PPService> ppServiceList = ppServiceRepository.findByListIDMBCreditCard(uuidList_UNC_MUA_DICH_VU);
                List<UUID> listIDPPService = ppServiceList.stream().map(PPService::getId).collect(Collectors.toList());
                ppServiceRepository.updateMultiUnRecord(listIDPPService);
                rs = unrecord(listIDPPService, null);
                if (rs) {
//                    Integer count = pPInvoiceDetailCostRepository.existsInPpServiceID(listIDPPService);
//                    if (count != null && count > 0) {
//                        pPInvoiceDetailCostRepository.removeListPPServiceByPpServiceID(listIDPPService);
//                    }
                    if (ppServiceRepository.checkListHasPaid(listIDPPService)) {
                        generalLedgerRepository.unrecordList(listIDPPService);
                        mbTellerPaperDetailVendorRepository.deleteByListMBTellerPaperID(uuidList_UNC_MUA_DICH_VU);
                        mBTellerPaperRepository.deleteByListID(uuidList_UNC_MUA_DICH_VU);
                    }
                }
            }
        }
        return handlingResultDTO;
    }

    /**
     * Get all the mBTellerPapers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MBTellerPaper> findAll(Pageable pageable) {
        log.debug("Request to get all MBTellerPapers");
        return mBTellerPaperRepository.findAll(pageable);
    }


    /**
     * Get one mBTellerPaper by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MBTellerPaper> findOne(UUID id) {
        log.debug("Request to get MBTellerPaper : {}", id);
//        return mBTellerPaperRepository.findById(id);
        Optional<MBTellerPaper> mBTellerPaper = mBTellerPaperRepository.findOneById(id);
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            boolean isNoMBook = !collect.isEmpty() && collect.get(0).getCode().equalsIgnoreCase("1");
            List<RefVoucherDTO> dtos = refVoucherRepository.getRefViewVoucher(id, isNoMBook);
            mBTellerPaper.get().setViewVouchers(dtos);
        }
        if (
            mBTellerPaper.get().getTypeId().equals(Constants.PPInvoiceType.TYPE_ID_UNC_MUA_HANG) ||
                mBTellerPaper.get().getTypeId().equals(Constants.PPInvoiceType.TYPE_ID_SCK_MUA_HANG) ||
                mBTellerPaper.get().getTypeId().equals(Constants.PPInvoiceType.TYPE_ID_STM_MUA_HANG)
        ) {
            Object object = mBTellerPaperRepository.findIDRef(id, mBTellerPaper.get().getTypeId());
            String uuid = ((Object[]) object)[0].toString();
            Boolean storedInRepository = Boolean.valueOf(((Object[]) object)[1].toString());
            mBTellerPaper.get().setPpInvocieID(Utils.uuidConvertToGUID(UUID.fromString(uuid)));
            mBTellerPaper.get().setStoredInRepository(storedInRepository);

        } else if (
            mBTellerPaper.get().getTypeId().equals(Constants.PPInvoiceType.TYPE_ID_UNC_MUA_DICH_VU) ||
                mBTellerPaper.get().getTypeId().equals(Constants.PPInvoiceType.TYPE_ID_SCK_MUA_DICH_VU) ||
                mBTellerPaper.get().getTypeId().equals(Constants.PPInvoiceType.TYPE_ID_STM_MUA_DICH_VU)
        ) {
            UUID uuid = UUID.fromString(mBTellerPaperRepository.findIDRef(id, mBTellerPaper.get().getTypeId()).toString());
            mBTellerPaper.get().setPpServiceID(Utils.uuidConvertToGUID(uuid));
        } else if (
            mBTellerPaper.get().getTypeId().equals(TypeConstant.BAO_NO_UNC_TRA_TIEN_NCC) ||
                mBTellerPaper.get().getTypeId().equals(TypeConstant.BAO_NO_SCK_TRA_TIEN_NCC) ||
                mBTellerPaper.get().getTypeId().equals(TypeConstant.BAO_NO_STM_TRA_TIEN_NCC)
        ) {
            List<PPInvoiceDTO> lstPPInvoice = mBTellerPaperRepository.findVoucherByListPPInvoiceID
                (mBTellerPaper.get().getmBTellerPaperDetailVendor().stream().map(n -> n.getpPInvoiceID()).collect(Collectors.toList()), mBTellerPaper.get().getmBTellerPaperDetailVendor().stream().collect(Collectors.toList()).get(0).getVoucherTypeID());
            for (MBTellerPaperDetailVendor mBTellerPaperDetailVendor : mBTellerPaper.get().getmBTellerPaperDetailVendor()) {
                PPInvoiceDTO ppInvoiceDTO = lstPPInvoice.stream().filter(n -> n.getId().equals(mBTellerPaperDetailVendor.getpPInvoiceID())).findFirst().orElse(null);
                if (ppInvoiceDTO != null) {
                    mBTellerPaperDetailVendor.setNoFBook(ppInvoiceDTO.getNoFBook());
                    mBTellerPaperDetailVendor.setNoMBook(ppInvoiceDTO.getNoMBook());
                    mBTellerPaperDetailVendor.setDate(ppInvoiceDTO.getDate());
                    mBTellerPaperDetailVendor.setDueDate(ppInvoiceDTO.getDueDate());
                }
            }
        }
        return mBTellerPaper;
    }

    /**
     * Delete the mBTellerPaper by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MBTellerPaper : {}", id);
        mBTellerPaperRepository.deleteById(id);
    }

    /**
     * @author mran
     */
    @Override
    public Page<MBTellerPaper> findAll(Pageable pageable, SearchVoucher searchVoucher) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        if (currentUserLoginAndOrg.isPresent()) {
            return mBTellerPaperRepository.findAll
                (pageable, searchVoucher, currentUserLoginAndOrg.get().getOrg(),
                    Integer.valueOf(userDTO.getSystemOption()
                        .stream().filter(n -> n.getCode().equals(PHIEN_SoLamViec))
                        .findAny().get().getData()));
        }
        throw new BadRequestAlertException("", "", "");
    }

    /**
     * @return
     * @author mran
     */
    @Override
    @Transactional(readOnly = true)
    public MBTellerPaper findOneByRowNum(SearchVoucher searchVoucher, Number rowNum) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return mBTellerPaperRepository.findOneByRowNum(searchVoucher, rowNum, currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Number> getIndexRow(UUID id, SearchVoucher searchVoucher) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return mBTellerPaperRepository.getIndexRow(id, searchVoucher, currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }

    /**
     * @return
     * @author mran
     */
    @Override
    public Page<MBTellerPaperDTO> findAllDTOByCompanyID(Pageable pageable) {
        log.debug("Request to get all MBTellerPaperDTO by CompanyID");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        if (currentUserLoginAndOrg.isPresent()) {
            return mBTellerPaperRepository.findAllDTOByCompanyID
                (pageable, currentUserLoginAndOrg.get().getOrg(),
                    Integer.valueOf(userDTO.getSystemOption()
                        .stream().filter(n -> n.getCode().equals(PHIEN_SoLamViec))
                        .findAny().get().getData()));
        }
        throw new BadRequestAlertException("", "", "");
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
