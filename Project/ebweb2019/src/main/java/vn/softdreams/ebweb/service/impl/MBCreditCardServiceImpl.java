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
import vn.softdreams.ebweb.service.dto.cashandbank.MBCreditCardExportDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MBCreditCardSaveDTO;
import vn.softdreams.ebweb.service.util.*;
import vn.softdreams.ebweb.web.rest.dto.MBCreditCardViewDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;
//import vn.softdreams.ebweb.service.dto.MBCreditCardDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing MBCreditCard.
 */
@Service
@Transactional
public class MBCreditCardServiceImpl implements MBCreditCardService {

    private final Logger log = LoggerFactory.getLogger(MBCreditCardServiceImpl.class);

    private final MBCreditCardRepository mBCreditCardRepository;

    private final MBCreditCardDetailsRepository mbCreditCardDetailsRepository;

    private final MBCreditCardDetailTaxRepository mbCreditCardDetailTaxRepository;
    private final RefVoucherRepository refVoucherRepository;
    private final UserService userService;
    private final String PHIEN_SoLamViec = "PHIEN_SoLamViec";
    private final GenCodeService genCodeService;
    private final GeneralLedgerService generalLedgerService;
    private final Integer TYPE_MBCREDITCARD = 170;
    private final Integer CREDIT_CARD_PURCHASES = 171;
    private final Integer CREDIT_CARD_FIXED_ASSETS = 172;
    private final Integer CREDIT_CARD_PURCHASE_SERVICE = 173;
    private final Integer CREDIT_CARD_PAYMENT_PROVIDER = 174;
    private final Integer CREDIT_CARD_TOOLS = 175;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final MCPaymentRepository mCPaymentRepository;
    private final UtilsService utilsService;
    private final TypeRepository typeRepository;
    private final PPInvoiceRepository ppInvoiceRepository;
    private final PPInvoiceDetailsRepository ppInvoiceDetailsRepository;
    private final MBTellerPaperRepository mbTellerPaperRepository;
    private final PporderRepository pporderRepository;
    private final PporderdetailRepository pporderdetailRepository;
    private final PPInvoiceDetailCostRepository pPInvoiceDetailCostRepository;
    private final RSInwardOutwardRepository rsInwardOutwardRepository;
    private final PPServiceRepository ppServiceRepository;
    private final PPServiceDetailRepository ppServiceDetailRepository;
    private final PPDiscountReturnRepository ppDiscountReturnRepository;
    private final SAInvoiceRepository saInvoiceRepository;
    private final RepositoryLedgerRepository repositoryLedgerRepository;
    private final GeneralLedgerRepository generalLedgerRepository;
    private final MBCreditCardDetailVendorRepository mbCreditCardDetailVendorRepository;
    private final String TCKHAC_GhiSo = "TCKHAC_GhiSo";

    @Autowired
    UtilsRepository utilsRepository;

    public MBCreditCardServiceImpl(MBCreditCardRepository mBCreditCardRepository,
                                   MBCreditCardDetailsRepository mbCreditCardDetailsRepository,
                                   MBCreditCardDetailTaxRepository mbCreditCardDetailTaxRepository,
                                   RefVoucherRepository refVoucherRepository,
                                   UserService userService,
                                   GenCodeService genCodeService,
                                   GeneralLedgerService generalLedgerService,
                                   OrganizationUnitRepository organizationUnitRepository,
                                   UtilsService utilsService, TypeRepository typeRepository,
                                   PPInvoiceRepository ppInvoiceRepository, MBTellerPaperRepository mbTellerPaperRepository,
                                   MCPaymentRepository mcPaymentRepository, PporderRepository pporderRepository, PporderdetailRepository pporderdetailRepository,
                                   PPInvoiceDetailCostRepository ppInvoiceDetailCostRepository, RSInwardOutwardRepository rsInwardOutwardRepository,
                                   PPServiceRepository ppServiceRepository, PPInvoiceDetailsRepository ppInvoiceDetailsRepository,
                                   PPServiceDetailRepository ppServiceDetailRepository, PPDiscountReturnRepository ppDiscountReturnRepository,
                                   SAInvoiceRepository saInvoiceRepository, RepositoryLedgerRepository repositoryLedgerRepository,
                                   GeneralLedgerRepository generalLedgerRepository, MBCreditCardDetailVendorRepository mbCreditCardDetailVendorRepository) {
        this.mBCreditCardRepository = mBCreditCardRepository;
        this.mbCreditCardDetailsRepository = mbCreditCardDetailsRepository;
        this.mbCreditCardDetailTaxRepository = mbCreditCardDetailTaxRepository;
        this.refVoucherRepository = refVoucherRepository;
        this.userService = userService;
        this.genCodeService = genCodeService;
        this.generalLedgerService = generalLedgerService;
        this.organizationUnitRepository = organizationUnitRepository;
        this.mCPaymentRepository = mcPaymentRepository;
        this.utilsService = utilsService;
        this.typeRepository = typeRepository;
        this.ppInvoiceRepository = ppInvoiceRepository;
        this.mbTellerPaperRepository = mbTellerPaperRepository;
        this.pporderRepository = pporderRepository;
        this.pporderdetailRepository = pporderdetailRepository;
        this.pPInvoiceDetailCostRepository = ppInvoiceDetailCostRepository;
        this.rsInwardOutwardRepository = rsInwardOutwardRepository;
        this.ppServiceRepository = ppServiceRepository;
        this.ppInvoiceDetailsRepository = ppInvoiceDetailsRepository;
        this.ppServiceDetailRepository = ppServiceDetailRepository;
        this.ppDiscountReturnRepository = ppDiscountReturnRepository;
        this.saInvoiceRepository = saInvoiceRepository;
        this.repositoryLedgerRepository = repositoryLedgerRepository;
        this.generalLedgerRepository = generalLedgerRepository;
        this.mbCreditCardDetailVendorRepository = mbCreditCardDetailVendorRepository;
    }

    /**
     * Save a mBCreditCard.
     *
     * @param mBCreditCard the entity to save
     * @return the persisted entity
     */
    @Override
    public MBCreditCard save(MBCreditCard mBCreditCard) {
        log.debug("Request to save mBCreditCard : {}", mBCreditCard);
        MBCreditCard mBC = new MBCreditCard();
        if (mBCreditCard.getId() == null) {
            mBCreditCard.setId(UUID.randomUUID());
            //gan id cha cho details con
            for (MBCreditCardDetails details : mBCreditCard.getmBCreditCardDetails()) {
                if (details.getId() != null) {
                    details.setId(null);
                }
                details.setmBCreditCardID(mBCreditCard.getId());
            }
            for (MBCreditCardDetailTax detailTax : mBCreditCard.getMbCreditCardDetailTax()) {
                if (detailTax.getId() != null) {
                    detailTax.setId(null);
                }
                detailTax.setmBCreditCardID(mBCreditCard.getId());
            }
        }
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            mBCreditCard.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            if (!utilsService.checkQuantityLimitedNoVoucher()) {
                throw new BadRequestAlertException("Số chứng từ hiện tại đã vượt quá số cho phép của Gói sử dụng", "", "noVoucherLimited");
            }
            mBC = mBCreditCardRepository.save(mBCreditCard);
//            utilsRepository.updateGencode(mBC.getNoFBook(), mBC.getNoMBook(), 17, mBC.getTypeLedger());
            List<RefVoucher> refVouchers = new ArrayList<>();
            for (RefVoucherDTO item : mBCreditCard.getViewVouchers() != null ? mBCreditCard.getViewVouchers() : new ArrayList<RefVoucherDTO>()) {
                RefVoucher refVoucher = new RefVoucher();
                refVoucher.setCompanyID(mBC.getCompanyID());
                refVoucher.setRefID1(mBC.getId());
                refVoucher.setRefID2(item.getRefID2());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                refVouchers.add(refVoucher);
            }
            refVoucherRepository.deleteByRefID1(mBC.getId());
            refVoucherRepository.deleteByRefID2(mBC.getId());
            refVoucherRepository.saveAll(refVouchers);
            mBC.setViewVouchers(mBCreditCard.getViewVouchers());
            return mBC;
        }
        throw new BadRequestAlertException("Không thể lưu báo có ! ", "", "");
    }

    /**
     * Get all the mBCreditCards.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MBCreditCard> findAll(Pageable pageable) {
        log.debug("Request to get all MBCreditCards");
        return mBCreditCardRepository.findAll(pageable);
    }


    /**
     * Get one mBCreditCard by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MBCreditCard> findOne(UUID id) {
        log.debug("Request to get MBCreditCard : {}", id);
        Optional<MBCreditCard> mbCreditCard = mBCreditCardRepository.findById(id);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
            boolean isNoMBook = currentBook.equalsIgnoreCase("1");
            List<RefVoucherDTO> dtos = refVoucherRepository.getRefViewVoucher(id, isNoMBook);
            mbCreditCard.get().setViewVouchers(dtos);
        }
        if (mbCreditCard.get().getTypeID().equals(Constants.PPInvoiceType.TYPE_ID_THE_TIN_DUNG_MUA_HANG)) {
            Object object = mCPaymentRepository.findIDRef(id, Constants.PPInvoiceType.TYPE_ID_THE_TIN_DUNG_MUA_HANG);
            String uuid = ((Object[]) object)[0].toString();
            Boolean storedInRepository = Boolean.valueOf(((Object[]) object)[1].toString());
            mbCreditCard.get().setPpInvocieID(Utils.uuidConvertToGUID(UUID.fromString(uuid)));
            mbCreditCard.get().setStoredInRepository(storedInRepository);

        } else if (mbCreditCard.get().getTypeID().equals(Constants.PPInvoiceType.TYPE_ID_THE_TIN_DUNG_MUA_DICH_VU)) {
            UUID uuid = UUID.fromString(mCPaymentRepository.findIDRef(id, Constants.PPInvoiceType.TYPE_ID_THE_TIN_DUNG_MUA_DICH_VU).toString());
            mbCreditCard.get().setPpServiceID(Utils.uuidConvertToGUID(uuid));
        } else if (mbCreditCard.get().getTypeID().equals(TypeConstant.THE_TIN_DUNG_TRA_TIEN_NCC)) {
            List<PPInvoiceDTO> lstPPInvoice =
                mCPaymentRepository.findVoucherByListPPInvoiceID(mbCreditCard.get().getmBCreditCardDetailVendor().stream().map(n -> n.getpPInvoiceID()).collect(Collectors.toList()),
                    mbCreditCard.get().getmBCreditCardDetailVendor().stream().collect(Collectors.toList()).get(0).getVoucherTypeID());
            for (MBCreditCardDetailVendor mbCreditCardDetailVendor : mbCreditCard.get().getmBCreditCardDetailVendor()) {
                PPInvoiceDTO ppInvoiceDTO = lstPPInvoice.stream().filter(n -> n.getId().equals(mbCreditCardDetailVendor.getpPInvoiceID())).findFirst().orElse(null);
                mbCreditCardDetailVendor.setNoFBook(ppInvoiceDTO.getNoFBook());
                mbCreditCardDetailVendor.setNoMBook(ppInvoiceDTO.getNoMBook());
                mbCreditCardDetailVendor.setDate(ppInvoiceDTO.getDate());
                mbCreditCardDetailVendor.setDueDate(ppInvoiceDTO.getDueDate());
            }
        }
        return mbCreditCard;
    }

    /**
     * Delete the mBCreditCard by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MBCreditCard : {}", id);
        mBCreditCardRepository.deleteById(id);
        refVoucherRepository.deleteByRefID1(id);
        refVoucherRepository.deleteByRefID2(id);
    }

    @Override
    public Page<MBCreditCardViewDTO> findAll(Pageable pageable, SearchVoucher searchVoucher) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return mBCreditCardRepository.findAll(pageable, searchVoucher, currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    /**
     * @param searchVoucher
     * @param rowNum
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public MBCreditCard findOneByRowNum(SearchVoucher searchVoucher, Number rowNum) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return mBCreditCardRepository.findByRowNum(searchVoucher, rowNum, currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    public MBCreditCardSaveDTO saveDTO(MBCreditCard mbCreditCard) {
        log.debug("Request to save MBCreditCard : {}", mbCreditCard);
        MBCreditCard mBC = new MBCreditCard();
        MBCreditCardSaveDTO mbCreditCardSaveDTO = new MBCreditCardSaveDTO();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            if (mbCreditCard.getId() == null) {
                if (!utilsService.checkQuantityLimitedNoVoucher()) {
                    throw new BadRequestAlertException("Số chứng từ hiện tại đã vượt quá số cho phép của Gói sử dụng", "", "noVoucherLimited");
                }
            }
            UserDTO userDTO = userService.getAccount();
            if (mbCreditCard.getTypeLedger() == 2) {
                if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(PHIEN_SoLamViec)).findAny().get().getData().equals("0")) {
                    if (StringUtils.isEmpty(mbCreditCard.getNoMBook())) {
                        mbCreditCard.setNoMBook(genCodeService.getCodeVoucher(17, 1));
                    }
                } else {
                    if (StringUtils.isEmpty(mbCreditCard.getNoFBook())) {
                        mbCreditCard.setNoFBook(genCodeService.getCodeVoucher(17, 0));
                    }
                }
            }
            if (!utilsRepository.checkDuplicateNoVoucher(mbCreditCard.getNoFBook(), mbCreditCard.getNoMBook(), mbCreditCard.getTypeLedger(), mbCreditCard.getId())) {
                mbCreditCardSaveDTO.setMbCreditCard(mbCreditCard);
                mbCreditCardSaveDTO.setStatus(1);
                return mbCreditCardSaveDTO;
            }
            mbCreditCard.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            mBC = mBCreditCardRepository.save(mbCreditCard);
//            utilsRepository.updateGencode(mBC.getNoFBook(), mBC.getNoMBook(), 17, mBC.getTypeLedger() == null ? 2 : mBC.getTypeLedger());
            List<RefVoucher> refVouchers = new ArrayList<>();
            for (RefVoucherDTO item : mbCreditCard.getViewVouchers() != null ? mbCreditCard.getViewVouchers() : new ArrayList<RefVoucherDTO>()) {
                RefVoucher refVoucher = new RefVoucher();
                refVoucher.setCompanyID(mBC.getCompanyID());
                refVoucher.setRefID1(mBC.getId());
                refVoucher.setRefID2(item.getRefID2());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                refVouchers.add(refVoucher);
            }
            refVoucherRepository.deleteByRefID1(mBC.getId());
            refVoucherRepository.deleteByRefID2(mBC.getId());
            refVoucherRepository.saveAll(refVouchers);
            mBC.setViewVouchers(mbCreditCard.getViewVouchers());
            MessageDTO messageDTO = new MessageDTO();
            if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {
                if (!generalLedgerService.record(mBC, messageDTO)) {
                    mbCreditCardSaveDTO.setStatus(2);
                    mbCreditCardSaveDTO.setMsg(messageDTO.getMsgError());
                    mBC.setRecorded(false);
                    mBCreditCardRepository.save(mBC);
                } else {
                    mbCreditCardSaveDTO.setStatus(0);
                    mBC.setRecorded(true);
                    mBCreditCardRepository.save(mBC);
                }
            } else {
                mbCreditCardSaveDTO.setStatus(0);
                if (mBC.getRecorded() == null) {
                    mBC.setRecorded(false);
                    mBCreditCardRepository.save(mBC);
                }
//                mcP.setRecorded(false);
            }
            mbCreditCardSaveDTO.setMbCreditCard(mBC);
            return mbCreditCardSaveDTO;
        }
        throw new BadRequestAlertException("Không thể lưu phiếu thu", "", "");
    }

    @Override
    public byte[] exportPDF(SearchVoucher searchVoucher) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        Page<MBCreditCardExportDTO> mbCreditCardExportDTOS = currentUserLoginAndOrg.map(securityDTO -> mBCreditCardRepository.getAllMBCreditCards(null, searchVoucher,
            securityDTO.getOrg(), currentBook)).orElse(null);
        for (int i = 0; i < mbCreditCardExportDTOS.getContent().size(); i++) {
            if (mbCreditCardExportDTOS.getContent().get(i).getTypeID().equals(TYPE_MBCREDITCARD)) {
                mbCreditCardExportDTOS.getContent().get(i).setTypeIDInWord("Thẻ tín dụng");
            } else if (mbCreditCardExportDTOS.getContent().get(i).getTypeID().equals(CREDIT_CARD_PURCHASES)) {
                mbCreditCardExportDTOS.getContent().get(i).setTypeIDInWord("Thẻ tín dụng mua hàng");
            } else if (mbCreditCardExportDTOS.getContent().get(i).getTypeID().equals(CREDIT_CARD_FIXED_ASSETS)) {
                mbCreditCardExportDTOS.getContent().get(i).setTypeIDInWord("Thẻ tín dụng mua TSCĐ");
            } else if (mbCreditCardExportDTOS.getContent().get(i).getTypeID().equals(CREDIT_CARD_PURCHASE_SERVICE)) {
                mbCreditCardExportDTOS.getContent().get(i).setTypeIDInWord("Thẻ tín dụng mua dịch vụ");
            } else if (mbCreditCardExportDTOS.getContent().get(i).getTypeID().equals(CREDIT_CARD_PAYMENT_PROVIDER)) {
                mbCreditCardExportDTOS.getContent().get(i).setTypeIDInWord("Thẻ tín dụng trả tiền NCC");
            } else if (mbCreditCardExportDTOS.getContent().get(i).getTypeID().equals(CREDIT_CARD_TOOLS)) {
                mbCreditCardExportDTOS.getContent().get(i).setTypeIDInWord("Thẻ tín dụng mua CDCC");
            }
        }
        return PdfUtils.writeToFile(mbCreditCardExportDTOS.getContent(), ExcelConstant.MBCreditCard.HEADER, ExcelConstant.MBCreditCard.FIELD);
    }

    @Override
    public byte[] exportExcel(SearchVoucher searchVoucher) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        Page<MBCreditCardExportDTO> mbCreditCardExportDTOS = currentUserLoginAndOrg.map(securityDTO -> mBCreditCardRepository.getAllMBCreditCards(null, searchVoucher,
            securityDTO.getOrg(), currentBook)).orElse(null);
        for (int i = 0; i < mbCreditCardExportDTOS.getContent().size(); i++) {
            if (mbCreditCardExportDTOS.getContent().get(i).getTypeID().equals(TYPE_MBCREDITCARD)) {
                mbCreditCardExportDTOS.getContent().get(i).setTypeIDInWord("Thẻ tín dụng");
            } else if (mbCreditCardExportDTOS.getContent().get(i).getTypeID().equals(CREDIT_CARD_PURCHASES)) {
                mbCreditCardExportDTOS.getContent().get(i).setTypeIDInWord("Thẻ tín dụng mua hàng");
            } else if (mbCreditCardExportDTOS.getContent().get(i).getTypeID().equals(CREDIT_CARD_FIXED_ASSETS)) {
                mbCreditCardExportDTOS.getContent().get(i).setTypeIDInWord("Thẻ tín dụng mua TSCĐ");
            } else if (mbCreditCardExportDTOS.getContent().get(i).getTypeID().equals(CREDIT_CARD_PURCHASE_SERVICE)) {
                mbCreditCardExportDTOS.getContent().get(i).setTypeIDInWord("Thẻ tín dụng mua dịch vụ");
            } else if (mbCreditCardExportDTOS.getContent().get(i).getTypeID().equals(CREDIT_CARD_PAYMENT_PROVIDER)) {
                mbCreditCardExportDTOS.getContent().get(i).setTypeIDInWord("Thẻ tín dụng trả tiền NCC");
            } else if (mbCreditCardExportDTOS.getContent().get(i).getTypeID().equals(CREDIT_CARD_TOOLS)) {
                mbCreditCardExportDTOS.getContent().get(i).setTypeIDInWord("Thẻ tín dụng mua CDCC");
            }
        }
        return ExcelUtils.writeToFile(mbCreditCardExportDTOS.getContent(), ExcelConstant.MBCreditCard.NAME, ExcelConstant.MBCreditCard.HEADER, ExcelConstant.MBCreditCard.FIELD);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Number> getIndexRow(UUID id, SearchVoucher searchVoucher) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return mBCreditCardRepository.getIndexRow(id, searchVoucher, currentUserLoginAndOrg.get().getOrg(), currentBook);
    }

    @Override
    public HandlingResultDTO multiDelete(List<MBCreditCard> mbCreditCards) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        handlingResultDTO.setCountTotalVouchers(mbCreditCards.size());
        List<MBCreditCard> listDelete = mbCreditCards.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        for (int i = 0; i < mbCreditCards.size(); i++) {
            if (Boolean.TRUE.equals(mbCreditCards.get(i).getRecorded())) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đang được ghi sổ !");
                BeanUtils.copyProperties(mbCreditCards.get(i), viewVoucherNo);
                viewVoucherNoListFail.add(viewVoucherNo);
                listDelete.remove(mbCreditCards.get(i));
            }
        }
        if (listDelete.size() == 0) {
            throw new BadRequestAlertException("Các chứng từ đang được ghi sổ, không thể xoá", "mBCreditCard", "errorDeleteList");
        }
        // get ListID chung tu theo Type ID
        List<UUID> uuidList_TTD_TTDNCC = new ArrayList<>();
        List<UUID> uuidList_TTD_MH = new ArrayList<>();
        List<UUID> uuidList_TTD_MDV = new ArrayList<>();
        for (int i = 0; i < listDelete.size(); i++) {
            if (listDelete.get(i).getTypeID() == 170 || listDelete.get(i).getTypeID() == 174) {
                uuidList_TTD_TTDNCC.add(listDelete.get(i).getId());
            } else if (listDelete.get(i).getTypeID() == 171) {
                uuidList_TTD_MH.add(listDelete.get(i).getId());
            } else if (listDelete.get(i).getTypeID() == 173) {
                uuidList_TTD_MDV.add(listDelete.get(i).getId());
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
        if (uuidList_TTD_TTDNCC.size() > 0) {
            mBCreditCardRepository.multiDeleteMBCreditCard(currentUserLoginAndOrg.get().getOrg(), uuidList_TTD_TTDNCC);
            mBCreditCardRepository.multiDeleteChildMBCreditCard("MBCreditCardDetail", uuidList_TTD_TTDNCC);
            mBCreditCardRepository.multiDeleteChildMBCreditCard("MBCreditCardDetailTax", uuidList_TTD_TTDNCC);
            mBCreditCardRepository.multiDeleteChildMBCreditCard("MBCreditCardDetailVendor", uuidList_TTD_TTDNCC);
//            mBDepositRepository.multiDeleteGeneralLedger(currentUserLoginAndOrg.get().getOrg(), multiDelete);
            refVoucherRepository.deleteByRefID1sOrRefID2s(uuidList_TTD_TTDNCC);
        }
        // Xoa chung tu The tin dung mua hang
        if (uuidList_TTD_MH.size() > 0) {
            List<UUID> ppInvoiceList = ppInvoiceRepository.findByListIDMBCreditCard(uuidList_TTD_MH).stream().map(PPInvoice::getId).collect(Collectors.toList());
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
                refVoucherRepository.deleteByRefID1sOrRefID2s(uuidList_TTD_MH);
                // xóa phân bổ chi phí
                pPInvoiceDetailCostRepository.deleteListRefID(ppInvoiceList);
                mBCreditCardRepository.deleteByListID(uuidList_TTD_MH);
                ppInvoiceDetailsRepository.deleteByListID(ppInvoiceList);
                ppInvoiceRepository.deleteByListID(ppInvoiceList);
            }
        }
//         Xoa chung tu the tin dung mua dich vu
        if (uuidList_TTD_MDV.size() > 0) {
            List<UUID> listIDPPService = ppServiceRepository.findByListIDMBCreditCard(uuidList_TTD_MDV).stream().map(PPService::getId).collect(Collectors.toList());
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
                mBCreditCardRepository.deleteByListID(uuidList_TTD_MDV);
            }
        }
        return handlingResultDTO;
    }

    @Override
    public HandlingResultDTO multiUnRecord(List<MBCreditCard> mbCreditCards) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        handlingResultDTO.setCountTotalVouchers(mbCreditCards.size());
        List<MBCreditCard> listDelete = mbCreditCards.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        org.joda.time.LocalDate dateClosed = org.joda.time.LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (!closeDateStr.equals("") && closeDateStr != null) {
            dateClosed = org.joda.time.LocalDate.parse(closeDateStr);
        }
        for (MBCreditCard mc : mbCreditCards) {
            org.joda.time.LocalDate postedDate = org.joda.time.LocalDate.parse(mc.getPostedDate().toString());
            if (Boolean.TRUE.equals(mc.isRecorded()) && !closeDateStr.equals("") && closeDateStr != null && dateClosed != null &&(dateClosed.isAfter(postedDate) || dateClosed.equals(postedDate))) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setReasonFail("Chứng từ đã khóa sổ!");
                viewVoucherNo.setPostedDate(mc.getPostedDate());
                viewVoucherNo.setDate(mc.getDate());
                viewVoucherNo.setNoFBook(mc.getNoFBook());
                viewVoucherNo.setNoMBook(mc.getNoMBook());
                if (mc.getTypeID() == TypeConstant.THE_TIN_DUNG) {
                    viewVoucherNo.setTypeName("Thẻ tín dụng");
                } else if (mc.getTypeID() == TypeConstant.THE_TIN_DUNG_MUA_HANG) {
                    viewVoucherNo.setTypeName("Thẻ tín dụng mua hàng");
                } else if (mc.getTypeID() == TypeConstant.THE_TIN_DUNG_MUA_DICH_VU) {
                    viewVoucherNo.setTypeName("Thẻ tín dụng mua dịch vụ");
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
            if (v.getTypeID() == TypeConstant.NOP_TIEN_TU_TAI_KHOAN) {
                v.setTypeName("Thẻ tín dụng");
            } else if (v.getTypeID() == TypeConstant.NOP_TIEN_TU_KHACH_HANG) {
                v.setTypeName("Thẻ tín dụng mua hàng");
            } else if (v.getTypeID() == TypeConstant.NOP_TIEN_TU_BAN_HANG) {
                v.setTypeName("Thẻ tín dụng mua dịch vụ");
            }
        }

        List<UUID> uuidList_THE_TIN_DUNG = new ArrayList<>();
        List<UUID> uuidList_TTD_MUA_HANG = new ArrayList<>();
        List<UUID> uuidList_TTD_MUA_DICH_VU = new ArrayList<>();
        List<UUID> uuidListRS = new ArrayList<>();
        List<UUID> uuidList = new ArrayList<>();
        for (int i = 0; i < listDelete.size(); i++) {
            if (listDelete.get(i).getTypeID() == TypeConstant.THE_TIN_DUNG_MUA_HANG) {
                uuidList_TTD_MUA_HANG.add(listDelete.get(i).getId());
            } else if (listDelete.get(i).getTypeID() == TypeConstant.THE_TIN_DUNG_MUA_DICH_VU) {
                uuidList_TTD_MUA_DICH_VU.add(listDelete.get(i).getId());
            } else {
                uuidList_THE_TIN_DUNG.add(listDelete.get(i).getId());
            }
            uuidList.add(listDelete.get(i).getId());
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(listDelete.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();

        if (uuidList.size() > 0) {
            boolean rs = false;
            mBCreditCardRepository.multiUnRecord(uuidList, currentUserLoginAndOrg.get().getOrg());
            mBCreditCardRepository.deleteGL(uuidList, currentUserLoginAndOrg.get().getOrg());
            if (uuidList_TTD_MUA_HANG.size() > 0) {
                List<PPInvoice> ppInvoiceList = ppInvoiceRepository.findByListIDMBCreditCard(uuidList_TTD_MUA_HANG);
                List<UUID> listIDPPInvoice = ppInvoiceList.stream().map(PPInvoice::getId).collect(Collectors.toList());
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
            if (uuidList_TTD_MUA_DICH_VU.size() > 0) {
                List<PPService> ppServiceList = ppServiceRepository.findByListIDMBCreditCard(uuidList_TTD_MUA_DICH_VU);
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
                        mbCreditCardDetailVendorRepository.deleteByListMBCreditCardID(uuidList_TTD_MUA_DICH_VU);
                        mBCreditCardRepository.deleteByListID(uuidList_TTD_MUA_DICH_VU);
                    }
                }
            }
        }
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
