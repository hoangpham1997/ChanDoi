package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.RepositoryLedgerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.CostSetMaterialGoodsDTO;
import vn.softdreams.ebweb.service.dto.RSInwardOutwardDetailsDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.web.rest.dto.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.domain.RepositoryLedger.deepCopy;

/**
 * Service Implementation for managing RepositoryLedger.
 */
@Service
@Transactional
public class RepositoryLedgerServiceImpl implements RepositoryLedgerService {

    private final Logger log = LoggerFactory.getLogger(RepositoryLedgerServiceImpl.class);

    private final RepositoryLedgerRepository repositoryLedgerRepository;
    private final CostSetRepository costSetRepository;
    private final RSInwardOutwardRepository rsInwardOutwardRepository;
    private final RSTransferRepository rsTransferRepository;
    private final SAInvoiceRepository saInvoiceRepository;
    private final SaReturnRepository saReturnRepository;
    private final PPInvoiceRepository ppInvoiceRepository;
    private final PPDiscountReturnRepository ppDiscountReturnRepository;
    private final PPDiscountReturnDetailsRepository ppDiscountReturnDetailsRepository;
    private final GeneralLedgerRepository generalLedgerRepository;
    private final UserService userService;

    public RepositoryLedgerServiceImpl(RepositoryLedgerRepository repositoryLedgerRepository, CostSetRepository costSetRepository, UserService userService,
                                       RSInwardOutwardRepository rsInwardOutwardRepository,
                                       RSTransferRepository rsTransferRepository, SAInvoiceRepository saInvoiceRepository,
                                       SaReturnRepository saReturnRepository,
                                       PPInvoiceRepository ppInvoiceRepository, PPDiscountReturnRepository ppDiscountReturnRepository, PPDiscountReturnDetailsRepository ppDiscountReturnDetailsRepository, GeneralLedgerRepository generalLedgerRepository) {
        this.repositoryLedgerRepository = repositoryLedgerRepository;
        this.costSetRepository = costSetRepository;
        this.rsInwardOutwardRepository = rsInwardOutwardRepository;
        this.rsTransferRepository = rsTransferRepository;
        this.saInvoiceRepository = saInvoiceRepository;
        this.saReturnRepository = saReturnRepository;
        this.ppInvoiceRepository = ppInvoiceRepository;
        this.ppDiscountReturnRepository = ppDiscountReturnRepository;
        this.ppDiscountReturnDetailsRepository = ppDiscountReturnDetailsRepository;
        this.generalLedgerRepository = generalLedgerRepository;
        this.userService = userService;
    }

    /**
     * Save a repositoryLedger.
     *
     * @param repositoryLedger the entity to save
     * @return the persisted entity
     */
    @Override
    public RepositoryLedger save(RepositoryLedger repositoryLedger) {
        log.debug("Request to save RepositoryLedger : {}", repositoryLedger);        return repositoryLedgerRepository.save(repositoryLedger);
    }

    /**
     * Get all the repositoryLedgers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RepositoryLedger> findAll(Pageable pageable) {
        log.debug("Request to get all RepositoryLedgers");
        return repositoryLedgerRepository.findAll(pageable);
    }


    /**
     * Get one repositoryLedger by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RepositoryLedger> findOne(UUID id) {
        log.debug("Request to get RepositoryLedger : {}", id);
        return repositoryLedgerRepository.findById(id);
    }

    /**
     * Delete the repositoryLedger by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete RepositoryLedger : {}", id);
        repositoryLedgerRepository.deleteById(id);
    }

    @Override
    public List<LotNoDTO> getListLotNoByMaterialGoodsID(UUID materialGoodsID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return repositoryLedgerRepository.getListLotNoByMaterialGoodsID(materialGoodsID, currentUserLoginAndOrg.get().getOrg());
    }

    @Override
    public ResultCalculateOWDTO calculateOWPrice(Integer calculationMethod, List<UUID> materialGoods, String fromDate, String toDate) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        Integer soLamViec = Utils.PhienSoLamViec(userDTO);
        Integer lamTronDonGia = 0;
        Integer lamTronDonGiaNT = 0;
        Integer lamTronTienVN = 0;
        Integer lamTronTienNT = 0;
        for (SystemOption sys: userDTO.getSystemOption()) {
            if (sys.getCode().equals(Constants.SystemOption.DDSo_DonGia)) {
                lamTronDonGia = Integer.valueOf(sys.getData());
            } else if (sys.getCode().equals(Constants.SystemOption.DDSo_DonGiaNT)) {
                lamTronDonGiaNT = Integer.valueOf(sys.getData());
            } else if (sys.getCode().equals(Constants.SystemOption.DDSo_TienVND)) {
                lamTronTienVN = Integer.valueOf(sys.getData());
            } else if (sys.getCode().equals(Constants.SystemOption.DDSo_NgoaiTe)) {
                lamTronTienNT = Integer.valueOf(sys.getData());
            }
        }
        ResultCalculateOWDTO resultCalculateOWDTO = new ResultCalculateOWDTO();
        resultCalculateOWDTO.setStatus(0);
        List<ViewVoucherNo> listErr = new ArrayList<>();
        List<RepositoryLedgerDTO> repositoryLedgers = repositoryLedgerRepository.getListRepositoryError(fromDate, toDate, currentUserLoginAndOrg.get().getOrg(), soLamViec);
        if (repositoryLedgers.size() > 0) {
            List<SAInvoiceViewDTO> saInvoiceDTOS = saInvoiceRepository.getAllSAInvoiceHasRSID(currentUserLoginAndOrg.get().getOrg(), soLamViec);
            for (RepositoryLedgerDTO item: repositoryLedgers) {
                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
                viewVoucherNo.setNoFBook(item.getNoFBook());
                viewVoucherNo.setNoMBook(item.getNoMBook());
                viewVoucherNo.setPostedDate(item.getPostedDate());
                viewVoucherNo.setDate(item.getDate());
                viewVoucherNo.setTypeID(item.getTypeID());
                viewVoucherNo.setRefID(item.getReferenceID());
                if (item.getTypeID().equals(TypeConstant.XUAT_KHO_TU_BAN_HANG) && saInvoiceDTOS != null) {
                    List<SAInvoiceViewDTO> saInvoice = saInvoiceDTOS.stream().filter(n -> n.getRsInwardOutwardID().equals(item.getReferenceID())).collect(Collectors.toList());
                    if (saInvoice.size() > 0) {
                        if (saInvoice.get(0).getTypeID().equals(TypeConstant.BAN_HANG_CHUA_THU_TIEN)) {
                            viewVoucherNo.setTypeID(saInvoice.get(0).getTypeID());
                            viewVoucherNo.setRefID(saInvoice.get(0).getId());
                        } else {
                            viewVoucherNo.setTypeID(item.getTypeID());
                            viewVoucherNo.setRefID(item.getReferenceID());
                        }
                    }
                }
                listErr.add(viewVoucherNo);
            }
            resultCalculateOWDTO.setVoucherResultDTOs(listErr);
            return resultCalculateOWDTO;
        }
        if (calculationMethod.equals(Constants.CalculationMethod.PP_TINH_GIA_BINH_QUAN_CUOI_KY)) {
            Boolean isByRepository = Integer.valueOf(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.VTHH_TinhgiaTungKho)).findAny().get().getData()).equals(1);
            List<CalculateOWPriceDTO> calculateOWPriceDTOs = new ArrayList<>();
            calculateOWPriceDTOs = repositoryLedgerRepository.calculateOWPrice(materialGoods, fromDate, toDate, isByRepository, currentUserLoginAndOrg.get().getOrg(), soLamViec, lamTronTienVN);
            if (calculateOWPriceDTOs.size() > 0) {
                Boolean result = repositoryLedgerRepository.updateOwPrice(calculateOWPriceDTOs, fromDate, toDate, lamTronDonGia, lamTronDonGiaNT, lamTronTienVN, lamTronTienNT);
                resultCalculateOWDTO.setStatus(result ? 1 : 0);
            } else {
                resultCalculateOWDTO.setStatus(1);
            }
        } else if (calculationMethod.equals(Constants.CalculationMethod.PP_TINH_GIA_BINH_QUAN_TUC_THOI)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate beginDate = LocalDate.parse(fromDate, formatter);
            String strStartDate =  beginDate.getYear() + "-01-01";
            LocalDate startDate = LocalDate.parse(strStartDate, formatter);
            String opnDate =  beginDate.plusYears(-1).getYear() + "-12-31";
            List<RepositoryLedger> listRepositoryLedgersIWDKAll = repositoryLedgerRepository.getListRepositoryIWDK(opnDate, currentUserLoginAndOrg.get().getOrg());//danh sách nhập kho đầu kỳ
            List<RepositoryLedger> listRepositoryLedgersOWAll = repositoryLedgerRepository.getListRepositoryOW(strStartDate, toDate, currentUserLoginAndOrg.get().getOrg()); // danh sách xuất kho trong kỳ
            List<RepositoryLedger> listRepositoryLedgersIWAll = repositoryLedgerRepository.getListRepositoryIW(strStartDate, toDate, currentUserLoginAndOrg.get().getOrg()); // danh sách nhập kho trong kỳ
            List<PPDiscountReturnDetails> discountReturnDetailsList = ppDiscountReturnDetailsRepository.getAllPPDiscountReturnDetailsByComID(strStartDate, currentUserLoginAndOrg.get().getOrg(), materialGoods); // danh sách nhập kho trong kỳ
            for (UUID materialGoodsID: materialGoods) {
                List<RepositoryLedger> listRepositoryLedgersIWDK = listRepositoryLedgersIWDKAll.stream().filter(x -> x.getMaterialGoodsID().equals(materialGoodsID)).collect(Collectors.toList());
                BigDecimal giaTriTonDK = BigDecimal.ZERO;
                BigDecimal soLuongTonDK = BigDecimal.ZERO;
                for (RepositoryLedger item: listRepositoryLedgersIWDK) {
                    soLuongTonDK = soLuongTonDK.add(item.getMainIWQuantity());
                    giaTriTonDK = giaTriTonDK.add(Utils.round(item.getIwAmount(), lamTronTienVN));
                }
                List<RepositoryLedger> listRepositoryLedgersOW = listRepositoryLedgersOWAll.stream().filter(x -> x.getMaterialGoodsID().equals(materialGoodsID)).collect(Collectors.toList()); // danh sách xuất kho
                List<RepositoryLedger> listRepositoryLedgersIWOriginal = listRepositoryLedgersIWAll.stream().filter(x -> x.getMaterialGoodsID().equals(materialGoodsID)).collect(Collectors.toList()); // danh sách nhập kho
                List<RepositoryLedger> listRepositoryLedgersIW = new ArrayList<>(); // danh sách nhập kho
                for (RepositoryLedger item: listRepositoryLedgersIWOriginal) {
                    RepositoryLedger clone = (RepositoryLedger) deepCopy(item);
                    List<PPDiscountReturnDetails> ppDiscountReturnDetails = discountReturnDetailsList.stream().filter(x -> x.getMaterialGoodsID().equals(materialGoodsID) && x.getId().equals(clone.getDetailID())).collect(Collectors.toList());
                    BigDecimal discountAmount = ppDiscountReturnDetails.stream().map(x -> x.getAmount()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
                    clone.setIwAmount(clone.getIwAmount().subtract(discountAmount));
                    listRepositoryLedgersIW.add(clone);
                }
                BigDecimal soLuongTon = soLuongTonDK;
                BigDecimal giaTriTon = giaTriTonDK;
                BigDecimal donGia = soLuongTonDK == BigDecimal.ZERO ? BigDecimal.ZERO : Utils.round(giaTriTonDK.divide(soLuongTonDK), lamTronDonGia);
                for (int i = 0; i < listRepositoryLedgersOW.size(); i++) {
                    int finalI = i;
                    List<RepositoryLedger> listRLIW = listRepositoryLedgersIW.stream().filter(x -> x.getMaterialGoodsID().equals(materialGoodsID)
                        && (x.getPostedDate().isAfter(finalI != 0 ? listRepositoryLedgersOW.get(finalI -1).getPostedDate() : startDate) || x.getPostedDate().equals(finalI != 0 ? listRepositoryLedgersOW.get(finalI -1).getPostedDate() : startDate) )
                        && (x.getPostedDate().isBefore(listRepositoryLedgersOW.get(finalI).getPostedDate()) || x.getPostedDate().equals(listRepositoryLedgersOW.get(finalI).getPostedDate()))).collect(Collectors.toList());
                    BigDecimal totalQuantityIW = BigDecimal.ZERO;
                    BigDecimal totalAmountIW = BigDecimal.ZERO;
                    for (RepositoryLedger item: listRLIW) {
                        if (!Boolean.TRUE.equals(item.getUsed())){
                            totalQuantityIW = totalQuantityIW.add(item.getMainIWQuantity());
                            totalAmountIW = totalAmountIW.add(Utils.round(item.getIwAmount(), lamTronTienVN));
                            item.setUsed(true);
                        }
                    }
                    BigDecimal soLuongXuatTruoc = (i != 0 ? listRepositoryLedgersOW.get(i - 1).getMainOWQuantity() : BigDecimal.ZERO);
                    soLuongTon = soLuongTon.add(totalQuantityIW).subtract(soLuongXuatTruoc);
                    BigDecimal giaTriXuatTruoc = (i != 0 ? Utils.round((listRepositoryLedgersOW.get(i - 1).getMainOWQuantity().multiply(listRepositoryLedgersOW.get(i - 1).getMainUnitPrice())), lamTronTienVN) : BigDecimal.ZERO);
                    giaTriTon = giaTriTon.add(totalAmountIW).subtract(giaTriXuatTruoc);
                    donGia = soLuongTon.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : Utils.round(giaTriTon.divide(soLuongTon, MathContext.DECIMAL64), lamTronDonGia);
                    listRepositoryLedgersOW.get(i).setMainUnitPrice(donGia);
                    listRepositoryLedgersOW.get(i).setOwAmount(Utils.round(donGia.multiply(listRepositoryLedgersOW.get(i).getMainOWQuantity()), lamTronTienVN));
                    if (listRepositoryLedgersOW.size() - 1 == i && listRepositoryLedgersOW.get(i).getMainOWQuantity().equals(soLuongTon)) {
                        listRepositoryLedgersOW.get(i).setOwAmount(giaTriTon);
                    }
                }
                // Update so kho
                Boolean result = repositoryLedgerRepository.updateOwPricePP2(listRepositoryLedgersOW, fromDate, toDate, lamTronDonGia, lamTronDonGiaNT, lamTronTienVN, lamTronTienNT);
                // Update cho nhung chung tu lien quan
                for (RepositoryLedger item: listRepositoryLedgersOW) {
                    if (item.getTypeID().equals(TypeConstant.XUAT_KHO) || item.getTypeID().equals(TypeConstant.XUAT_KHO_TU_DIEU_CHINH) || item.getTypeID().equals(TypeConstant.XUAT_KHO_TU_BAN_HANG)) {
                        Optional<RSInwardOutward> rsInwardOutward = rsInwardOutwardRepository.findById(item.getReferenceID());
                        if (rsInwardOutward.isPresent()) {
                            if (item.getTypeID().equals(TypeConstant.XUAT_KHO) || item.getTypeID().equals(TypeConstant.XUAT_KHO_TU_DIEU_CHINH)) {
                                BigDecimal totalAmountOriginal = BigDecimal.ZERO;
                                BigDecimal totalAmount = BigDecimal.ZERO;
                                for (RSInwardOutWardDetails rsInwardOutWardDetails: rsInwardOutward.get().getRsInwardOutwardDetails()) {
                                    if (rsInwardOutWardDetails.getMaterialGood().getId().equals(materialGoodsID)) {
                                        rsInwardOutWardDetails.setUnitPrice((rsInwardOutWardDetails.getFormula().equals(Constants.Formular.PHEP_NHAN) ? rsInwardOutWardDetails.getMainConvertRate().multiply(item.getMainUnitPrice()) : item.getMainUnitPrice().divide(rsInwardOutWardDetails.getMainConvertRate())));
                                        rsInwardOutWardDetails.setUnitPriceOriginal((rsInwardOutWardDetails.getFormula().equals(Constants.Formular.PHEP_NHAN) ? rsInwardOutWardDetails.getMainConvertRate().multiply(item.getMainUnitPrice()) : item.getMainUnitPrice().divide(rsInwardOutWardDetails.getMainConvertRate())));
                                        rsInwardOutWardDetails.setMainUnitPrice(item.getMainUnitPrice());
                                        rsInwardOutWardDetails.setAmountOriginal(Utils.round(item.getOwAmount(), lamTronTienVN));
                                        rsInwardOutWardDetails.setAmount(Utils.round(item.getOwAmount(), lamTronTienVN));

                                        // Update so cai
                                        List<GeneralLedger> generalLedgersRS = generalLedgerRepository.findByDetailID(rsInwardOutWardDetails.getId());
                                        if (generalLedgersRS.size() > 0) {
                                            for (GeneralLedger generalLedger: generalLedgersRS) {
                                                if (generalLedger.getAccount().equals(rsInwardOutWardDetails.getCreditAccount())) {
                                                    generalLedger.setCreditAmount(rsInwardOutWardDetails.getAmount());
                                                    generalLedger.setCreditAmountOriginal(rsInwardOutWardDetails.getAmountOriginal());
                                                } else if (generalLedger.getAccount().equals(rsInwardOutWardDetails.getDebitAccount())) {
                                                    generalLedger.setDebitAmount(rsInwardOutWardDetails.getAmount());
                                                    generalLedger.setDebitAmountOriginal(rsInwardOutWardDetails.getAmountOriginal());
                                                }
                                            }
                                            generalLedgerRepository.saveAll(generalLedgersRS);
                                        }
                                    }
                                    totalAmountOriginal = totalAmountOriginal.add(rsInwardOutWardDetails.getAmountOriginal());
                                    totalAmount = totalAmount.add(rsInwardOutWardDetails.getAmount());
                                }
                                rsInwardOutward.get().setTotalAmountOriginal(totalAmountOriginal);
                                rsInwardOutward.get().setTotalAmount(totalAmount);
                                rsInwardOutwardRepository.save(rsInwardOutward.get());
                            } else if (item.getTypeID().equals(TypeConstant.XUAT_KHO_TU_BAN_HANG)) {
                                Optional<SAInvoice> saInvoice = saInvoiceRepository.findByRsInwardOutwardID(rsInwardOutward.get().getId());
                                if (saInvoice.isPresent()) {
                                    BigDecimal totalAmountSA = BigDecimal.ZERO;
                                    for (SAInvoiceDetails saInvoiceDetails: saInvoice.get().getsAInvoiceDetails()) {
                                        if (saInvoiceDetails.getMaterialGoodsID().equals(materialGoodsID)) {
                                            saInvoiceDetails.setoWPrice((saInvoiceDetails.getFormula().equals(Constants.Formular.PHEP_NHAN) ? saInvoiceDetails.getMainConvertRate().multiply(item.getMainUnitPrice()) : item.getMainUnitPrice().divide(saInvoiceDetails.getMainConvertRate())));
                                            saInvoiceDetails.setoWAmount(Utils.round(item.getOwAmount(), lamTronTienVN));

                                            // Update so cai
                                            List<GeneralLedger> generalLedgersSA = generalLedgerRepository.findByDetailID(saInvoiceDetails.getId());
                                            if (generalLedgersSA.size() > 0) {
                                                for (GeneralLedger generalLedger: generalLedgersSA) {
                                                    if (generalLedger.getAccount().equals(Constants.Account.TK_GIA_VON)) {
                                                        generalLedger.setDebitAmount(saInvoiceDetails.getoWAmount());
                                                        generalLedger.setDebitAmountOriginal(Utils.round(saInvoiceDetails.getoWAmount().divide(saInvoice.get().getExchangeRate(), MathContext.DECIMAL32),
                                                            saInvoice.get().getCurrencyID().equals(Constants.CurrencyID.TIEN_VND) ? lamTronTienVN : lamTronTienNT));
                                                    } else if (generalLedger.getAccountCorresponding().equals(Constants.Account.TK_GIA_VON)) {
                                                        generalLedger.setCreditAmount(saInvoiceDetails.getoWAmount());
                                                        generalLedger.setCreditAmountOriginal(Utils.round(saInvoiceDetails.getoWAmount().divide(saInvoice.get().getExchangeRate(), MathContext.DECIMAL32),
                                                            saInvoice.get().getCurrencyID().equals(Constants.CurrencyID.TIEN_VND) ? lamTronTienVN : lamTronTienNT));
                                                    }
                                                }
                                                generalLedgerRepository.saveAll(generalLedgersSA);
                                            }
                                        }
                                        totalAmountSA = totalAmountSA.add(saInvoiceDetails.getoWAmount() != null ? saInvoiceDetails.getoWAmount() : BigDecimal.ZERO );
                                    }
                                    saInvoice.get().setTotalCapitalAmount(totalAmountSA);
                                    saInvoiceRepository.save(saInvoice.get());
                                    rsInwardOutward.get().setTotalAmount(totalAmountSA);
                                    rsInwardOutward.get().setTotalAmountOriginal(Utils.round(totalAmountSA.divide(rsInwardOutward.get().getExchangeRate(), MathContext.DECIMAL32),
                                        rsInwardOutward.get().getCurrencyID().equals(Constants.CurrencyID.TIEN_VND) ? lamTronTienVN : lamTronTienNT));
                                    rsInwardOutwardRepository.save(rsInwardOutward.get());

                                    Optional<SaReturn> saReturn = saReturnRepository.findBySAInvoiceID(saInvoice.get().getId());
                                    if (saReturn.isPresent() && Boolean.FALSE.equals(saReturn.get().getAutoOWAmountCal())) {
                                        BigDecimal totalAmountSAR = BigDecimal.ZERO;
                                        for (SaReturnDetails saReturnDetails: saReturn.get().getSaReturnDetails()) {
                                            if (saReturnDetails.getMaterialGoodsID().equals(materialGoodsID)) {
                                                saReturnDetails.setOwAmount((saReturnDetails.getFormula().equals(Constants.Formular.PHEP_NHAN) ? saReturnDetails.getMainConvertRate().multiply(item.getMainUnitPrice()) : item.getMainUnitPrice().divide(saReturnDetails.getMainConvertRate())));
                                                saReturnDetails.setOwAmount(Utils.round(saReturnDetails.getOwPrice().multiply(saReturnDetails.getQuantity()), lamTronTienVN));

                                                // Update so cai
                                                List<GeneralLedger> generalLedgersSAR = generalLedgerRepository.findByDetailID(saReturnDetails.getId());
                                                if (generalLedgersSAR.size() > 0) {
                                                    for (GeneralLedger generalLedger: generalLedgersSAR) {
                                                        if (generalLedger.getAccount().equals(Constants.Account.TK_GIA_VON)) {
                                                            generalLedger.setCreditAmount(saReturnDetails.getOwAmount());
                                                            generalLedger.setCreditAmountOriginal(Utils.round(saReturnDetails.getOwAmount().divide(saReturn.get().getExchangeRate(), MathContext.DECIMAL32),
                                                                saReturn.get().getCurrencyID().equals(Constants.CurrencyID.TIEN_VND) ? lamTronTienVN : lamTronTienNT));
                                                        } else if (generalLedger.getAccountCorresponding().equals(Constants.Account.TK_GIA_VON)) {
                                                            generalLedger.setDebitAmount(saReturnDetails.getOwAmount());
                                                            generalLedger.setDebitAmountOriginal(Utils.round(saReturnDetails.getOwAmount().divide(saReturn.get().getExchangeRate(), MathContext.DECIMAL32),
                                                                saReturn.get().getCurrencyID().equals(Constants.CurrencyID.TIEN_VND) ? lamTronTienVN : lamTronTienNT));
                                                        }
                                                    }
                                                    generalLedgerRepository.saveAll(generalLedgersSAR);
                                                }
                                            }
                                            totalAmountSAR = totalAmountSAR.add(saReturnDetails.getOwAmount() != null ? saReturnDetails.getOwAmount() : BigDecimal.ZERO);
                                        }
                                        saReturn.get().setTotalOWAmount(totalAmountSAR);
                                        saReturnRepository.save(saReturn.get());

                                        Optional<RSInwardOutward> rsInwardOutwardSAR = rsInwardOutwardRepository.findById(saReturn.get().getRsInwardOutwardID());
                                        if (rsInwardOutwardSAR.isPresent()) {
                                            rsInwardOutwardSAR.get().setTotalAmount(totalAmountSAR);
                                            rsInwardOutwardSAR.get().setTotalAmountOriginal(Utils.round(totalAmountSAR.divide(rsInwardOutwardSAR.get().getExchangeRate(), MathContext.DECIMAL32),
                                                rsInwardOutwardSAR.get().getCurrencyID().equals(Constants.CurrencyID.TIEN_VND) ? lamTronTienVN : lamTronTienNT));
                                            rsInwardOutwardRepository.save(rsInwardOutwardSAR.get());
                                        }
                                    }
                                }
                            }
                        }
                    } else if (item.getTypeID().equals(TypeConstant.CHUYEN_KHO) ||
                        item.getTypeID().equals(TypeConstant.CHUYEN_KHO_GUI_DAI_LY) ||
                        item.getTypeID().equals(TypeConstant.CHUYEN_KHO_CHUYEN_NOI_BO)) {
                        Optional<RSTransfer> rsTransfer = rsTransferRepository.findById(item.getReferenceID());
                        if (rsTransfer.isPresent()) {
                            BigDecimal totalAmountOriginal = BigDecimal.ZERO;
                            BigDecimal totalAmount = BigDecimal.ZERO;
                            for (RSTransferDetail rsTransferDetail: rsTransfer.get().getRsTransferDetails()) {
                                if (rsTransferDetail.getMaterialGood().getId().equals(materialGoodsID)) {
                                    rsTransferDetail.setoWPrice((rsTransferDetail.getFormula().equals(Constants.Formular.PHEP_NHAN) ? rsTransferDetail.getMainConvertRate().multiply(item.getMainUnitPrice()) : item.getMainUnitPrice().divide(rsTransferDetail.getMainConvertRate())));
                                    rsTransferDetail.setoWAmount(Utils.round(item.getOwAmount(), lamTronTienVN));
                                    // Update so cai
                                    List<GeneralLedger> generalLedgersRS = generalLedgerRepository.findByDetailID(rsTransferDetail.getId());
                                    if (generalLedgersRS.size() > 0) {
                                        for (GeneralLedger generalLedger: generalLedgersRS) {
                                            if (generalLedger.getAccount().equals(rsTransferDetail.getCreditAccount())) {
                                                generalLedger.setCreditAmount(rsTransferDetail.getoWAmount());
                                                generalLedger.setCreditAmountOriginal(rsTransferDetail.getoWAmount());
                                            } else if (generalLedger.getAccount().equals(rsTransferDetail.getDebitAccount())) {
                                                generalLedger.setDebitAmount(rsTransferDetail.getoWAmount());
                                                generalLedger.setDebitAmountOriginal(rsTransferDetail.getoWAmount());
                                            }
                                        }
                                        generalLedgerRepository.saveAll(generalLedgersRS);
                                    }
                                }
                                totalAmountOriginal = totalAmountOriginal.add(rsTransferDetail.getoWAmount());
                                totalAmount = totalAmount.add(rsTransferDetail.getoWAmount());
                            }
                            rsTransfer.get().setTotalAmountOriginal(totalAmountOriginal);
                            rsTransfer.get().setTotalAmount(totalAmount);
                            rsTransferRepository.save(rsTransfer.get());
                        }
                    }
                }
            }
            resultCalculateOWDTO.setStatus(1);
        } else if (calculationMethod.equals(Constants.CalculationMethod.PP_TINH_GIA_NHAP_TRUOC_XUAT_TRUOC)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate beginDate = LocalDate.parse(fromDate, formatter);
            String strStartDate =  beginDate.getYear() + "-01-01";
            LocalDate startDate = LocalDate.parse(strStartDate, formatter);
            String opnDate =  beginDate.plusYears(-1).getYear() + "-12-31";
            List<RepositoryLedger> listRepositoryLedgersIWDKAll = repositoryLedgerRepository.getListRepositoryIWDK(opnDate, currentUserLoginAndOrg.get().getOrg());//danh sách nhập kho đầu kỳ
            List<PPDiscountReturnDetails> discountReturnDetailsList = ppDiscountReturnDetailsRepository.getAllPPDiscountReturnDetailsByComID(strStartDate, currentUserLoginAndOrg.get().getOrg(), materialGoods); // danh sách nhập kho trong kỳ
            for (UUID materialGoodsID: materialGoods) {
                // DS chứng từ VTHH đầu kỳ
                List<RepositoryLedger> listRepositoryLedgersIWDK = listRepositoryLedgersIWDKAll.stream().filter(x -> x.getMaterialGoodsID().equals(materialGoodsID)).collect(Collectors.toList());
                BigDecimal giaTriTonDK = BigDecimal.ZERO;
                BigDecimal soLuongTonDK = BigDecimal.ZERO;
                BigDecimal donGiaDK = BigDecimal.ZERO;
                for (RepositoryLedger item: listRepositoryLedgersIWDK) {
                    soLuongTonDK = soLuongTonDK.add(item.getMainIWQuantity());
                    giaTriTonDK = giaTriTonDK.add(Utils.round((item.getMainIWQuantity().multiply(item.getMainUnitPrice())), lamTronTienVN));
                    donGiaDK = soLuongTonDK.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : Utils.round(giaTriTonDK.divide(soLuongTonDK, MathContext.DECIMAL64), lamTronDonGia);
                }
                // DS chứng từ VTHH trong kỳ
                List<RepositoryLedger> listRepositoryLedgersOW = repositoryLedgerRepository.getListRepositoryOWNTXT(materialGoodsID, toDate);//danh sách xuất kho
                List<RepositoryLedger> listsIW = repositoryLedgerRepository.getListRepositoryIWNTXT(materialGoodsID, toDate);//danh sách nhập kho
                List<RepositoryLedger> listRepositoryLedgersIW = new ArrayList<>();
                for (RepositoryLedger item: listsIW) {
                    RepositoryLedger clone = (RepositoryLedger) deepCopy(item);
                    List<PPDiscountReturnDetails> ppDiscountReturnDetails = discountReturnDetailsList.stream().filter(x -> x.getMaterialGoodsID().equals(materialGoodsID) && x.getId().equals(clone.getDetailID())).collect(Collectors.toList());
                    BigDecimal discountAmount = ppDiscountReturnDetails.stream().map(x -> x.getAmount()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
                    clone.setIwAmount(clone.getIwAmount().subtract(discountAmount));
                    listRepositoryLedgersIW.add(clone);
                }
                Integer dem = 0;
                BigDecimal soLuong = BigDecimal.ZERO;
                BigDecimal giaTri = BigDecimal.ZERO;
                if (listRepositoryLedgersIW.size() > 0) {
                    for (RepositoryLedger item: listRepositoryLedgersIW) {
                        soLuong = soLuong.add(item.getMainIWQuantity());
                        giaTri = giaTri.add(Utils.round((item.getMainIWQuantity().multiply(item.getMainUnitPrice())), lamTronTienVN));
                    }
                }
                for (int i = 0; i < listRepositoryLedgersOW.size(); i++) {
                    Boolean check = true;
                    try {
                        if (soLuongTonDK.floatValue() > 0) {
                            if (listRepositoryLedgersOW.get(i).getMainOWQuantity().floatValue() <= soLuongTonDK.floatValue()) {
                                soLuongTonDK = soLuongTonDK.subtract(listRepositoryLedgersOW.get(i).getMainOWQuantity());
                                listRepositoryLedgersOW.get(i).setMainUnitPrice(donGiaDK);
                                if (soLuongTonDK.compareTo(BigDecimal.ZERO) == 0) {
                                    listRepositoryLedgersOW.get(i).setOwAmount(giaTriTonDK);
                                    giaTriTonDK = BigDecimal.ZERO;
                                } else {
                                    listRepositoryLedgersOW.get(i).setOwAmount(listRepositoryLedgersOW.get(i).getMainOWQuantity().multiply(listRepositoryLedgersOW.get(i).getMainUnitPrice()));
                                    giaTriTonDK = giaTriTonDK.subtract(Utils.round((listRepositoryLedgersOW.get(i).getMainOWQuantity().multiply(listRepositoryLedgersOW.get(i).getMainUnitPrice())), lamTronTienVN));
                                }
                            } else {
                                while (soLuong.floatValue() < (listRepositoryLedgersOW.get(i).getMainOWQuantity().subtract(soLuongTonDK)).floatValue()) {
                                    dem++;
                                    soLuong = soLuong.add(listRepositoryLedgersIW.get(dem).getMainIWQuantity());
                                    giaTri = giaTri.add(Utils.round((listRepositoryLedgersIW.get(dem).getMainIWQuantity().multiply(listRepositoryLedgersIW.get(dem).getMainUnitPrice())), lamTronTienVN));
                                }
                                listRepositoryLedgersIW.get(dem).setMainIWQuantity(soLuong.subtract((listRepositoryLedgersOW.get(i).getMainOWQuantity().subtract(soLuongTonDK))));
                                listRepositoryLedgersIW.get(dem).setIwAmount(Utils.round((listRepositoryLedgersIW.get(dem).getMainIWQuantity().multiply(listRepositoryLedgersIW.get(dem).getMainUnitPrice())), lamTronTienVN));
                                listRepositoryLedgersOW.get(i).setOwAmount(giaTri.subtract(listRepositoryLedgersIW.get(dem).getIwAmount().add(giaTriTonDK)));
                                listRepositoryLedgersOW.get(i).setMainUnitPrice(Utils.round(listRepositoryLedgersOW.get(i).getOwAmount().divide(listRepositoryLedgersOW.get(i).getMainOWQuantity(), MathContext.DECIMAL64)));
                                soLuong = listRepositoryLedgersIW.get(dem).getMainIWQuantity();
                                giaTri = Utils.round((listRepositoryLedgersIW.get(dem).getMainIWQuantity().multiply(listRepositoryLedgersIW.get(dem).getMainUnitPrice())), lamTronTienVN);
                                soLuongTonDK = BigDecimal.ZERO;
                                giaTriTonDK = BigDecimal.ZERO;
                            }
                        } else {
                            while (soLuong.floatValue() < listRepositoryLedgersOW.get(i).getMainOWQuantity().floatValue()) {
                                dem++;
                                soLuong = soLuong.add(listRepositoryLedgersIW.get(dem).getMainIWQuantity());
                                giaTri = giaTri.add(Utils.round((listRepositoryLedgersIW.get(dem).getMainIWQuantity().multiply(listRepositoryLedgersIW.get(dem).getMainUnitPrice())), lamTronTienVN));
                                check = false;
                            }
                            if (check) {
                                listRepositoryLedgersOW.get(i).setMainUnitPrice(listRepositoryLedgersIW.get(dem).getMainUnitPrice());
                                listRepositoryLedgersIW.get(dem).setMainIWQuantity(soLuong.subtract(listRepositoryLedgersOW.get(i).getMainOWQuantity()));
                                if (listRepositoryLedgersIW.get(dem).getMainIWQuantity().floatValue() == 0) {
                                    listRepositoryLedgersOW.get(i).setOwAmount(giaTri);
                                    listRepositoryLedgersIW.get(dem).setIwAmount(giaTri.subtract(Utils.round((listRepositoryLedgersOW.get(i).getMainOWQuantity().multiply(listRepositoryLedgersOW.get(i).getMainUnitPrice())), lamTronTienVN)));
                                    if (listRepositoryLedgersIW.size() - 1 > dem) {
                                        dem++;
                                        soLuong = listRepositoryLedgersIW.get(dem).getMainIWQuantity();
                                        giaTri = Utils.round((listRepositoryLedgersIW.get(dem).getMainIWQuantity().multiply(listRepositoryLedgersIW.get(dem).getMainUnitPrice())), lamTronTienVN);
                                    } else {
                                        soLuong = BigDecimal.ZERO;
                                        giaTri = BigDecimal.ZERO;
                                    }
                                } else {
                                    listRepositoryLedgersOW.get(i).setOwAmount(Utils.round((listRepositoryLedgersOW.get(i).getMainUnitPrice().multiply(listRepositoryLedgersOW.get(i).getMainOWQuantity())), lamTronTienVN));
                                    listRepositoryLedgersIW.get(dem).setIwAmount(giaTri.subtract(listRepositoryLedgersOW.get(i).getOwAmount()));
                                    soLuong = listRepositoryLedgersIW.get(dem).getMainIWQuantity();
                                    giaTri = Utils.round((listRepositoryLedgersIW.get(dem).getMainIWQuantity().multiply(listRepositoryLedgersIW.get(dem).getMainUnitPrice())), lamTronTienVN);
                                }
                            } else {
                                listRepositoryLedgersIW.get(dem).setMainIWQuantity(soLuong.subtract(listRepositoryLedgersOW.get(i).getMainOWQuantity()));
                                listRepositoryLedgersIW.get(dem).setIwAmount(Utils.round((listRepositoryLedgersIW.get(dem).getMainIWQuantity().multiply(listRepositoryLedgersIW.get(dem).getMainUnitPrice())), lamTronTienVN));
                                listRepositoryLedgersOW.get(i).setOwAmount(giaTri.subtract(listRepositoryLedgersIW.get(dem).getIwAmount()));
                                listRepositoryLedgersOW.get(i).setMainUnitPrice(Utils.round(listRepositoryLedgersOW.get(i).getOwAmount().divide(listRepositoryLedgersOW.get(i).getMainOWQuantity(), MathContext.DECIMAL64), lamTronDonGia));
                                soLuong = listRepositoryLedgersIW.get(dem).getMainIWQuantity();
                                giaTri = Utils.round((listRepositoryLedgersIW.get(dem).getMainIWQuantity().multiply(listRepositoryLedgersIW.get(dem).getMainUnitPrice())), lamTronTienVN);
                            }
                        }
                    }
                    catch (Exception ex)
                    {

                    }
                }
                // Update so kho
                Boolean result = repositoryLedgerRepository.updateOwPricePP2(listRepositoryLedgersOW, fromDate, toDate, lamTronDonGia, lamTronDonGiaNT, lamTronTienVN, lamTronTienNT);
                // Update cho nhung chung tu lien quan
                for (RepositoryLedger item: listRepositoryLedgersOW) {
                    if (item.getTypeID().equals(TypeConstant.XUAT_KHO) || item.getTypeID().equals(TypeConstant.XUAT_KHO_TU_DIEU_CHINH) || item.getTypeID().equals(TypeConstant.XUAT_KHO_TU_BAN_HANG)) {
                        Optional<RSInwardOutward> rsInwardOutward = rsInwardOutwardRepository.findById(item.getReferenceID());
                        if (rsInwardOutward.isPresent()) {
                            if (item.getTypeID().equals(TypeConstant.XUAT_KHO) || item.getTypeID().equals(TypeConstant.XUAT_KHO_TU_DIEU_CHINH)) {
                                BigDecimal totalAmountOriginal = BigDecimal.ZERO;
                                BigDecimal totalAmount = BigDecimal.ZERO;
                                for (RSInwardOutWardDetails rsInwardOutWardDetails: rsInwardOutward.get().getRsInwardOutwardDetails()) {
                                    if (rsInwardOutWardDetails.getMaterialGood().getId().equals(materialGoodsID)) {
                                        rsInwardOutWardDetails.setUnitPrice((rsInwardOutWardDetails.getFormula().equals(Constants.Formular.PHEP_NHAN) ? rsInwardOutWardDetails.getMainConvertRate().multiply(item.getMainUnitPrice()) : item.getMainUnitPrice().divide(rsInwardOutWardDetails.getMainConvertRate())));
                                        rsInwardOutWardDetails.setMainUnitPrice(item.getMainUnitPrice());
                                        rsInwardOutWardDetails.setUnitPriceOriginal((rsInwardOutWardDetails.getFormula().equals(Constants.Formular.PHEP_NHAN) ? rsInwardOutWardDetails.getMainConvertRate().multiply(item.getMainUnitPrice()) : item.getMainUnitPrice().divide(rsInwardOutWardDetails.getMainConvertRate())));
                                        rsInwardOutWardDetails.setAmountOriginal(Utils.round(item.getOwAmount(), lamTronTienVN));
                                        rsInwardOutWardDetails.setAmount(Utils.round(item.getOwAmount(), lamTronTienVN));

                                        // Update so cai
                                        List<GeneralLedger> generalLedgersRS = generalLedgerRepository.findByDetailID(rsInwardOutWardDetails.getId());
                                        if (generalLedgersRS.size() > 0) {
                                            for (GeneralLedger generalLedger: generalLedgersRS) {
                                                if (generalLedger.getAccount().equals(rsInwardOutWardDetails.getCreditAccount())) {
                                                    generalLedger.setCreditAmount(rsInwardOutWardDetails.getAmount());
                                                    generalLedger.setCreditAmountOriginal(rsInwardOutWardDetails.getAmountOriginal());
                                                } else if (generalLedger.getAccount().equals(rsInwardOutWardDetails.getDebitAccount())) {
                                                    generalLedger.setDebitAmount(rsInwardOutWardDetails.getAmount());
                                                    generalLedger.setDebitAmountOriginal(rsInwardOutWardDetails.getAmountOriginal());
                                                }
                                            }
                                            generalLedgerRepository.saveAll(generalLedgersRS);
                                        }
                                    }
                                    totalAmountOriginal = totalAmountOriginal.add(rsInwardOutWardDetails.getAmountOriginal());
                                    totalAmount = totalAmount.add(rsInwardOutWardDetails.getAmount());
                                }
                                rsInwardOutward.get().setTotalAmountOriginal(totalAmountOriginal);
                                rsInwardOutward.get().setTotalAmount(totalAmount);
                                rsInwardOutwardRepository.save(rsInwardOutward.get());
                            } else if (item.getTypeID().equals(TypeConstant.XUAT_KHO_TU_BAN_HANG)) {
                                Optional<SAInvoice> saInvoice = saInvoiceRepository.findByRsInwardOutwardID(rsInwardOutward.get().getId());
                                if (saInvoice.isPresent()) {
                                    BigDecimal totalAmountSA = BigDecimal.ZERO;
                                    for (SAInvoiceDetails saInvoiceDetails: saInvoice.get().getsAInvoiceDetails()) {
                                        if (saInvoiceDetails.getMaterialGoodsID().equals(materialGoodsID)) {
                                            saInvoiceDetails.setoWPrice((saInvoiceDetails.getFormula().equals(Constants.Formular.PHEP_NHAN) ? saInvoiceDetails.getMainConvertRate().multiply(item.getMainUnitPrice()) : item.getMainUnitPrice().divide(saInvoiceDetails.getMainConvertRate())));
                                            saInvoiceDetails.setoWAmount(Utils.round(item.getOwAmount(), lamTronTienVN));

                                            // Update so cai
                                            List<GeneralLedger> generalLedgersSA = generalLedgerRepository.findByDetailID(saInvoiceDetails.getId());
                                            if (generalLedgersSA.size() > 0) {
                                                for (GeneralLedger generalLedger: generalLedgersSA) {
                                                    if (generalLedger.getAccount().equals(Constants.Account.TK_GIA_VON)) {
                                                        generalLedger.setDebitAmount(saInvoiceDetails.getoWAmount());
                                                        generalLedger.setDebitAmountOriginal(Utils.round(saInvoiceDetails.getoWAmount().divide(saInvoice.get().getExchangeRate(), MathContext.DECIMAL32),
                                                            saInvoice.get().getCurrencyID().equals(Constants.CurrencyID.TIEN_VND) ? lamTronTienVN : lamTronTienNT));
                                                    } else if (generalLedger.getAccountCorresponding().equals(Constants.Account.TK_GIA_VON)) {
                                                        generalLedger.setCreditAmount(saInvoiceDetails.getoWAmount());
                                                        generalLedger.setCreditAmountOriginal(Utils.round(saInvoiceDetails.getoWAmount().divide(saInvoice.get().getExchangeRate(), MathContext.DECIMAL32),
                                                            saInvoice.get().getCurrencyID().equals(Constants.CurrencyID.TIEN_VND) ? lamTronTienVN : lamTronTienNT));
                                                    }
                                                }
                                                generalLedgerRepository.saveAll(generalLedgersSA);
                                            }
                                        }
                                        totalAmountSA = totalAmountSA.add(saInvoiceDetails.getoWAmount() != null ? saInvoiceDetails.getoWAmount() : BigDecimal.ZERO);
                                    }
                                    saInvoice.get().setTotalCapitalAmount(totalAmountSA);
                                    saInvoiceRepository.save(saInvoice.get());
                                    rsInwardOutward.get().setTotalAmount(totalAmountSA);
                                    rsInwardOutward.get().setTotalAmountOriginal(Utils.round(totalAmountSA.divide(rsInwardOutward.get().getExchangeRate(), MathContext.DECIMAL32),
                                        rsInwardOutward.get().getCurrencyID().equals(Constants.CurrencyID.TIEN_VND) ? lamTronTienVN : lamTronTienNT));
                                    rsInwardOutwardRepository.save(rsInwardOutward.get());

                                    Optional<SaReturn> saReturn = saReturnRepository.findBySAInvoiceID(saInvoice.get().getId());
                                    if (saReturn.isPresent() && Boolean.FALSE.equals(saReturn.get().getAutoOWAmountCal())) {
                                        BigDecimal totalAmountSAR = BigDecimal.ZERO;
                                        for (SaReturnDetails saReturnDetails: saReturn.get().getSaReturnDetails()) {
                                            if (saReturnDetails.getMaterialGoodsID().equals(materialGoodsID)) {
                                                saReturnDetails.setOwAmount((saReturnDetails.getFormula().equals(Constants.Formular.PHEP_NHAN) ? saReturnDetails.getMainConvertRate().multiply(item.getMainUnitPrice()) : item.getMainConvertRate().divide(saReturnDetails.getMainUnitPrice())));
                                                saReturnDetails.setOwAmount(Utils.round(saReturnDetails.getOwPrice().multiply(saReturnDetails.getQuantity()), lamTronTienVN));

                                                // Update so cai
                                                List<GeneralLedger> generalLedgersSAR = generalLedgerRepository.findByDetailID(saReturnDetails.getId());
                                                if (generalLedgersSAR.size() > 0) {
                                                    for (GeneralLedger generalLedger: generalLedgersSAR) {
                                                        if (generalLedger.getAccount().equals(Constants.Account.TK_GIA_VON)) {
                                                            generalLedger.setCreditAmount(saReturnDetails.getOwAmount());
                                                            generalLedger.setCreditAmountOriginal(Utils.round(saReturnDetails.getOwAmount().divide(saReturn.get().getExchangeRate(), MathContext.DECIMAL32),
                                                                saReturn.get().getCurrencyID().equals(Constants.CurrencyID.TIEN_VND) ? lamTronTienVN : lamTronTienNT));
                                                        } else if (generalLedger.getAccountCorresponding().equals(Constants.Account.TK_GIA_VON)) {
                                                            generalLedger.setDebitAmount(saReturnDetails.getOwAmount());
                                                            generalLedger.setDebitAmountOriginal(Utils.round(saReturnDetails.getOwAmount().divide(saReturn.get().getExchangeRate(), MathContext.DECIMAL32),
                                                                saReturn.get().getCurrencyID().equals(Constants.CurrencyID.TIEN_VND) ? lamTronTienVN : lamTronTienNT));
                                                        }
                                                    }
                                                    generalLedgerRepository.saveAll(generalLedgersSAR);
                                                }
                                            }
                                            totalAmountSAR = totalAmountSAR.add(saReturnDetails.getOwAmount() != null ? saReturnDetails.getOwAmount() : BigDecimal.ZERO);
                                        }
                                        saReturn.get().setTotalOWAmount(totalAmountSAR);
                                        saReturnRepository.save(saReturn.get());

                                        Optional<RSInwardOutward> rsInwardOutwardSAR = rsInwardOutwardRepository.findById(saReturn.get().getRsInwardOutwardID());
                                        if (rsInwardOutwardSAR.isPresent()) {
                                            rsInwardOutwardSAR.get().setTotalAmount(totalAmountSAR);
                                            rsInwardOutwardSAR.get().setTotalAmountOriginal(Utils.round(totalAmountSAR.divide(rsInwardOutwardSAR.get().getExchangeRate(), MathContext.DECIMAL32),
                                                rsInwardOutwardSAR.get().getCurrencyID().equals(Constants.CurrencyID.TIEN_VND) ? lamTronTienVN : lamTronTienNT));
                                            rsInwardOutwardRepository.save(rsInwardOutwardSAR.get());
                                        }
                                    }
                                }
                            }
                        }
                    } else if (item.getTypeID().equals(TypeConstant.CHUYEN_KHO) ||
                        item.getTypeID().equals(TypeConstant.CHUYEN_KHO_GUI_DAI_LY) ||
                        item.getTypeID().equals(TypeConstant.CHUYEN_KHO_CHUYEN_NOI_BO)) {
                        Optional<RSTransfer> rsTransfer = rsTransferRepository.findById(item.getReferenceID());
                        if (rsTransfer.isPresent()) {
                            BigDecimal totalAmountOriginal = BigDecimal.ZERO;
                            BigDecimal totalAmount = BigDecimal.ZERO;
                            for (RSTransferDetail rsTransferDetail: rsTransfer.get().getRsTransferDetails()) {
                                if (rsTransferDetail.getMaterialGood().getId().equals(materialGoodsID)) {
                                    rsTransferDetail.setoWPrice((rsTransferDetail.getFormula().equals(Constants.Formular.PHEP_NHAN) ? rsTransferDetail.getMainConvertRate().multiply(item.getMainUnitPrice()) : item.getMainUnitPrice().divide(rsTransferDetail.getMainConvertRate())));
                                    rsTransferDetail.setoWAmount(Utils.round(item.getOwAmount(), lamTronTienVN));
                                    // Update so cai
                                    List<GeneralLedger> generalLedgersRS = generalLedgerRepository.findByDetailID(rsTransferDetail.getId());
                                    if (generalLedgersRS.size() > 0) {
                                        for (GeneralLedger generalLedger: generalLedgersRS) {
                                            if (generalLedger.getAccount().equals(rsTransferDetail.getCreditAccount())) {
                                                generalLedger.setCreditAmount(rsTransferDetail.getoWAmount());
                                                generalLedger.setCreditAmountOriginal(rsTransferDetail.getoWAmount());
                                            } else if (generalLedger.getAccount().equals(rsTransferDetail.getDebitAccount())) {
                                                generalLedger.setDebitAmount(rsTransferDetail.getoWAmount());
                                                generalLedger.setDebitAmountOriginal(rsTransferDetail.getoWAmount());
                                            }
                                        }
                                        generalLedgerRepository.saveAll(generalLedgersRS);
                                    }
                                }
                                totalAmountOriginal = totalAmountOriginal.add(rsTransferDetail.getoWAmount());
                                totalAmount = totalAmount.add(rsTransferDetail.getoWAmount());
                            }
                            rsTransfer.get().setTotalAmountOriginal(totalAmountOriginal);
                            rsTransfer.get().setTotalAmount(totalAmount);
                            rsTransferRepository.save(rsTransfer.get());
                        }
                    }
                }
            }
            resultCalculateOWDTO.setStatus(1);
        } else if (calculationMethod.equals(Constants.CalculationMethod.PP_TINH_GIA_DICH_DANH)) {

        } else if (calculationMethod.equals(Constants.CalculationMethod.PP_TINH_GIA_GIA_TRI)) {

        }
        return resultCalculateOWDTO;
    }

    @Override
    public Page<IWVoucherDTO> getIWVoucher(Pageable pageable, String fromDate, String toDate, UUID objectId) {
        return rsInwardOutwardRepository.getIWVoucher(pageable, fromDate, toDate, objectId);
    }

    @Override
    public Boolean updateIWPriceFromCost(List<CPResult> cpResults) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        Integer soLamViec = Utils.PhienSoLamViec(userDTO);
        Integer lamTronDonGia = 0;
        Integer lamTronDonGiaNT = 0;
        Integer lamTronTienVN = 0;
        Integer lamTronTienNT = 0;
        for (SystemOption sys : userDTO.getSystemOption()) {
            if (sys.getCode().equals(Constants.SystemOption.DDSo_DonGia)) {
                lamTronDonGia = Integer.valueOf(sys.getData());
            } else if (sys.getCode().equals(Constants.SystemOption.DDSo_DonGiaNT)) {
                lamTronDonGiaNT = Integer.valueOf(sys.getData());
            } else if (sys.getCode().equals(Constants.SystemOption.DDSo_TienVND)) {
                lamTronTienVN = Integer.valueOf(sys.getData());
            } else if (sys.getCode().equals(Constants.SystemOption.DDSo_NgoaiTe)) {
                lamTronTienNT = Integer.valueOf(sys.getData());
            }
        }
        List<UUID> uuids = cpResults.stream().map(x -> x.getMaterialGoodsID()).collect(Collectors.toList());
        List<RSInwardOutwardDetailsDTO> rsInwardOutWardDetails = rsInwardOutwardRepository.getListUpdateUnitPrice(uuids,
            soLamViec, cpResults.get(0).getFromDate(), cpResults.get(0).getToDate(), cpResults.stream().map(x -> x.getCostSetID()).collect(Collectors.toList()));
        for (RSInwardOutwardDetailsDTO detail: rsInwardOutWardDetails) {
            Optional<CPResult> cpResult = cpResults.stream().filter(x -> x.getMaterialGoodsID().equals(detail.getMaterialGoodsID())).findFirst();
            if (cpResult.isPresent()) {
                detail.setMainUnitPrice(Utils.round(cpResult.get().getUnitPrice(),lamTronDonGia));
                detail.setUnitPrice(Utils.round((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(detail.getMainUnitPrice()) : detail.getMainUnitPrice().divide(detail.getMainConvertRate(), MathContext.DECIMAL64)), lamTronDonGia));
                detail.setUnitPriceOriginal(Utils.round((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(detail.getMainUnitPrice()) : detail.getMainUnitPrice().divide(detail.getMainConvertRate(), MathContext.DECIMAL64)), lamTronDonGia));
                detail.setAmount(Utils.round((detail.getQuantity().multiply(detail.getUnitPrice())), lamTronTienVN));
                detail.setAmountOriginal(Utils.round((detail.getQuantity().multiply(detail.getUnitPriceOriginal())), lamTronTienVN));
            }
        }
        return repositoryLedgerRepository.updateIWPriceFromCost(rsInwardOutWardDetails, currentUserLoginAndOrg.get().getOrg(), soLamViec);
    }

    @Override
    public Boolean updateOWPriceFromCost(List<CPResult> cpResults) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        Integer soLamViec = Utils.PhienSoLamViec(userDTO);
        Integer lamTronDonGia = 0;
        Integer lamTronDonGiaNT = 0;
        Integer lamTronTienVN = 0;
        Integer lamTronTienNT = 0;
        for (SystemOption sys : userDTO.getSystemOption()) {
            if (sys.getCode().equals(Constants.SystemOption.DDSo_DonGia)) {
                lamTronDonGia = Integer.valueOf(sys.getData());
            } else if (sys.getCode().equals(Constants.SystemOption.DDSo_DonGiaNT)) {
                lamTronDonGiaNT = Integer.valueOf(sys.getData());
            } else if (sys.getCode().equals(Constants.SystemOption.DDSo_TienVND)) {
                lamTronTienVN = Integer.valueOf(sys.getData());
            } else if (sys.getCode().equals(Constants.SystemOption.DDSo_NgoaiTe)) {
                lamTronTienNT = Integer.valueOf(sys.getData());
            }
        }
        List<RepositoryLedger> repositoryLedgersRS = new ArrayList<>();
        List<RepositoryLedger> repositoryLedgersSA = new ArrayList<>();
        List<RepositoryLedger> repositoryLedgersAD = new ArrayList<>();
        List<UUID> uuids = cpResults.stream().map(x -> x.getMaterialGoodsID()).collect(Collectors.toList());
        List<RepositoryLedger> repositoryLedgers = repositoryLedgerRepository.getListUpdateUnitPrice(uuids,
            soLamViec, cpResults.get(0).getFromDate(), cpResults.get(0).getToDate());
        for (RepositoryLedger detail: repositoryLedgers) {
            Optional<CPResult> cpResult = cpResults.stream().filter(x -> x.getMaterialGoodsID().equals(detail.getMaterialGoodsID())).findFirst();
            if (cpResult.isPresent()) {
                detail.setMainUnitPrice(Utils.round(cpResult.get().getUnitPrice(), lamTronDonGia));
                detail.setUnitPrice(Utils.round((detail.getFormula().equals(vn.softdreams.ebweb.service.util.Constants.Formular.PHEP_NHAN) ? detail.getMainConvertRate().multiply(detail.getMainUnitPrice()) : detail.getMainUnitPrice().divide(detail.getMainConvertRate(), MathContext.DECIMAL64)), lamTronDonGia));
                detail.setOwAmount(Utils.round((detail.getOwQuantity().multiply(detail.getUnitPrice())), lamTronTienVN));
            }
            if (detail.getTypeID().equals(TypeConstant.XUAT_KHO_TU_BAN_HANG)) {
                repositoryLedgersSA.add(detail);
            } else if (detail.getTypeID().equals(TypeConstant.XUAT_KHO)) {
                repositoryLedgersRS.add(detail);
            } else if (detail.getTypeID().equals(TypeConstant.XUAT_KHO_TU_DIEU_CHINH)) {
                repositoryLedgersAD.add(detail);
            }
        }
        return repositoryLedgerRepository.updateOWPriceFromCost(repositoryLedgersRS, repositoryLedgersSA, repositoryLedgersAD, currentUserLoginAndOrg.get().getOrg(), soLamViec);
    }

    @Override
    public List<LotNoDTO> getListLotNo(UUID materialGoodsID) {
        log.debug("Request to delete RepositoryLedger : {}", materialGoodsID);
        return repositoryLedgerRepository.getListLotNo(materialGoodsID);
    }
}
