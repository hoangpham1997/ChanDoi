package vn.softdreams.ebweb.service.impl;

import com.google.common.base.Strings;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.UtilsService;
import vn.softdreams.ebweb.service.ViewVoucherNoService;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.dto.EInvoice.SDS.Respone_SDS;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.web.rest.dto.*;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.Constants.PPInvoiceType.*;
import static vn.softdreams.ebweb.service.util.Constants.PPInvoiceType.TYPE_ID_STM_MUA_DICH_VU;
import static vn.softdreams.ebweb.service.util.Constants.SystemOption.TCKHAC_SDDMVTHH;
import static vn.softdreams.ebweb.service.util.TypeConstant.*;

/**
 * @Author Hautv
 * Service Implementation for managing InvoiceType.
 */
@Service
@Transactional
public class ViewVoucherNoServiceImpl implements ViewVoucherNoService {

    private final UserService userService;
    private final ViewVoucherNoRespository viewVoucherNoRespository;
    private final MCReceiptRepository mcReceiptRepository;
    private final MBDepositRepository mbDepositRepository;
    private final BankAccountDetailsRepository bankAccountDetailsRepository;
    private final AccountingObjectRepository accountingObjectRepository;
    private final RSInwardOutwardRepository rSInwardOutwardRepository;
    private final RepositoryRepository repositoryRepository;
    private final MaterialGoodsRepository materialGoodsRepository;
    private final GeneralLedgerRepository generalLedgerRepository;
    private final SystemOptionRepository systemOptionRepository;
    private final UtilsService utilsService;
    private final GenCodeRepository genCodeRepository;

    public ViewVoucherNoServiceImpl(
        UserService userService,
        ViewVoucherNoRespository viewVoucherNoRespository,
        MCReceiptRepository mcReceiptRepository,
        MBDepositRepository mbDepositRepository,
        BankAccountDetailsRepository bankAccountDetailsRepository,
        AccountingObjectRepository accountingObjectRepository,
        RSInwardOutwardRepository rSInwardOutwardRepository,
        RepositoryRepository repositoryRepository,
        MaterialGoodsRepository materialGoodsRepository,
        UtilsService utilsService,
        GeneralLedgerRepository generalLedgerRepository,
        GenCodeRepository genCodeRepository,
        SystemOptionRepository systemOptionRepository) {
        this.userService = userService;
        this.viewVoucherNoRespository = viewVoucherNoRespository;
        this.mcReceiptRepository = mcReceiptRepository;
        this.mbDepositRepository = mbDepositRepository;
        this.bankAccountDetailsRepository = bankAccountDetailsRepository;
        this.accountingObjectRepository = accountingObjectRepository;
        this.rSInwardOutwardRepository = rSInwardOutwardRepository;
        this.repositoryRepository = repositoryRepository;
        this.materialGoodsRepository = materialGoodsRepository;
        this.utilsService = utilsService;
        this.generalLedgerRepository = generalLedgerRepository;
        this.systemOptionRepository = systemOptionRepository;
        this.genCodeRepository = genCodeRepository;
    }

    @Override
    public Page<ViewVoucherNo> findAll(Pageable pageable) {
        return viewVoucherNoRespository.findAll(pageable);
    }

    @Override
    public Page<ViewVoucherNo> getAllVoucherNotRecorded(Pageable pageable, LocalDate postedDate) {
        UserDTO userDTO = userService.getAccount();
        Integer book = Utils.PhienSoLamViec(userDTO);
        Pageable pageable1 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("Date").descending().and(Sort.by("PostedDate")).descending().and(Sort.by(Constants.TypeLedger.FINANCIAL_BOOK.equals(book) ? "NoFBook" : "NoMBook")).descending());
        return viewVoucherNoRespository.getAllVoucherNotRecorded(pageable1, userDTO.getOrganizationUnit().getId(), book, postedDate);
    }

    @Override
    public List<ViewVoucherNo> getAllVoucherNotRecorded(LocalDate postedDate) {
        UserDTO userDTO = userService.getAccount();
        Integer book = Utils.PhienSoLamViec(userDTO);
        return viewVoucherNoRespository.getAllVoucherNotRecorded(userDTO.getOrganizationUnit().getId(), book, postedDate);
    }

    @Override
    public List<ViewVoucherNoDetailDTO> findAllViewVoucherNoDetailDTOByListParentID(List<UUID> uuids) {
        return viewVoucherNoRespository.findAllViewVoucherNoDetailDTOByListParentID(uuids);
    }

    @Override
    public Optional<ViewVoucherNo> findOne(UUID id) {
        return viewVoucherNoRespository.findById(id);
    }

    @Override
    public HandlingResultDTO closeBook(CloseBookDTO closeBookDTO) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
        Integer book = Utils.PhienSoLamViec(userDTO);
        List<GeneralLedger> ledgers1 = new ArrayList<>();
        List<GeneralLedger> ledgers2 = new ArrayList<>();
        List<RepositoryLedger> repositoryLedgers1 = new ArrayList<>();
        List<RepositoryLedger> repositoryLedgers2 = new ArrayList<>();
        List<ViewVoucherNo> lstFail = new ArrayList<>(); // ListError
        List<ViewVoucherNo> lstVoucherNoList = viewVoucherNoRespository.getAllVoucherNotRecorded(userDTO.getOrganizationUnit().getId(), book, closeBookDTO.getPostedDate());
        List<ViewVoucherNoDetailDTO> lstViewVoucherNoDetailDTOS = viewVoucherNoRespository.findAllViewVoucherNoDetailDTOByListParentID(lstVoucherNoList.stream().map(n -> n.getRefID()).collect(Collectors.toList()));
        for (ViewVoucherNo viewVoucherNo : lstVoucherNoList) {
            List<ViewVoucherNoDetailDTO> lstDT = lstViewVoucherNoDetailDTOS.stream().filter(n -> n.getRefParentID().equals(viewVoucherNo.getRefID())).collect(Collectors.toList());
            if (lstDT.size() > 0 && lstDT.get(0).getOrderPriority() != null) {
                lstDT.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));
            }
            viewVoucherNo.setViewVoucherNoDetailDTOS(lstDT);
        }
        List<ViewVoucherNo> listMain = lstVoucherNoList.stream().filter(n -> !closeBookDTO.getListDataChangeDiff().stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList()).contains(n.getRefID())).collect(Collectors.toList());
        List<ViewVoucherNo> listSub = lstVoucherNoList.stream().filter(n -> closeBookDTO.getListDataChangeDiff().stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList()).contains(n.getRefID())).collect(Collectors.toList());
//        List<ViewVoucherNo> listSubChangePostedDate = lstVoucherNoList.stream().filter(n -> closeBookDTO.getListDataChangeDiff().stream().map(c -> c.getRefID()).collect(Collectors.toList()).contains(n.getRefID())).collect(Collectors.toList());
        List<ViewVoucherNo> lstRecord1;
        List<UUID> lstID1;
        List<ViewVoucherNo> lstRecord2;
        List<ViewVoucherNo> listSubRecordMain;
        List<ViewVoucherNo> listSubRecord1;
        List<ViewVoucherNo> listSubRecord2;
        List<ViewVoucherNo> listSubDate;
        List<ViewVoucherNo> listSubDelete;
        switch (closeBookDTO.getChoseFuntion()) {
            case Constants.ClOSE_BOOK.RECORD:
                listSubDate = listSub.stream().filter(n -> closeBookDTO.getListDataChangeDiff().stream().filter(ViewVoucherNo::getChecked2).map(ViewVoucherNo::getRefID).collect(Collectors.toList()).contains(n.getRefID())).collect(Collectors.toList());
                listSubDelete = listSub.stream().filter(n -> closeBookDTO.getListDataChangeDiff().stream().filter(ViewVoucherNo::getChecked3).map(ViewVoucherNo::getRefID).collect(Collectors.toList()).contains(n.getRefID())).collect(Collectors.toList());
                lstRecord1 = listMain.stream().filter(n -> n.getRefTable().equals("PPInvoice") || n.getTypeID().equals(NHAP_KHO)
                    || n.getTypeID().equals(NHAP_KHO_TU_DIEU_CHINH) || n.getTypeID().equals(NHAP_KHO_TU_MUA_HANG) || n.getTypeID().equals(NHAP_KHO_TU_BAN_HANG_TRA_LAI)).collect(Collectors.toList());
                ledgers1 = handleRecord(lstRecord1, lstFail); // Ghi sổ cái
                repositoryLedgers1 = handleRepositoryLedger(lstRecord1, lstFail); // ghi sổ kho
                lstRecord1.removeAll(lstFail);
                if (ledgers1.size() > 0) {
                    viewVoucherNoRespository.saveGeneralLedger(ledgers1);
                    viewVoucherNoRespository.updateVoucherRefRecorded(lstRecord1);
                    viewVoucherNoRespository.updateTableRecord(lstRecord1);
                }
                if (repositoryLedgers1.size() > 0) {
                    viewVoucherNoRespository.saveRepositoryLedger(repositoryLedgers1);
                }

                lstID1 = lstRecord1.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList());
                lstRecord2 = listMain.stream().filter(n -> !lstID1.contains(n.getRefID())).collect(Collectors.toList());
                ledgers2 = handleRecord(lstRecord2, lstFail); // Ghi sổ cái
                repositoryLedgers2 = handleRepositoryLedger(lstRecord2, lstFail); // ghi sổ kho
                lstRecord2.removeAll(lstFail);
                if (ledgers2.size() > 0) {
                    viewVoucherNoRespository.saveGeneralLedger(ledgers2);
                    viewVoucherNoRespository.updateVoucherRefRecorded(lstRecord2);
                    viewVoucherNoRespository.updateTableRecord(lstRecord2);
                }
                if (repositoryLedgers2.size() > 0) {
                    viewVoucherNoRespository.saveRepositoryLedger(repositoryLedgers2);
                }
                if (listSubDate.size() > 0) {
                    handleChangePostedDate(listSubDate, closeBookDTO.getListDataChangeDiff().stream().filter(d -> d.getChecked2()).collect(Collectors.toList()), lstFail);
                }
                if (listSubDelete.size() > 0) {
                    checkDelete(listSubDelete, lstFail, userDTO);
                    handleDelete(listSubDelete, lstFail);
                }
                break;
            case Constants.ClOSE_BOOK.CHANG_POSTEDDATE:
                listSubRecordMain = listSub.stream().filter(n -> closeBookDTO.getListDataChangeDiff().stream().filter(ViewVoucherNo::getChecked1).map(ViewVoucherNo::getRefID).collect(Collectors.toList()).contains(n.getRefID())).collect(Collectors.toList());
                listSubDelete = listSub.stream().filter(n -> closeBookDTO.getListDataChangeDiff().stream().filter(ViewVoucherNo::getChecked3).map(ViewVoucherNo::getRefID).collect(Collectors.toList()).contains(n.getRefID())).collect(Collectors.toList());
                handleChangePostedDate(listMain, closeBookDTO.getListChangePostedDateDiff(), closeBookDTO.getPostedDateNew(), lstFail);
                if (listSubRecordMain.size() > 0) {
                    listSubRecord1 = listSubRecordMain.stream().filter(n -> n.getRefTable().equals("PPInvoice") || n.getTypeID().equals(NHAP_KHO)
                        || n.getTypeID().equals(NHAP_KHO_TU_DIEU_CHINH) || n.getTypeID().equals(NHAP_KHO_TU_MUA_HANG) || n.getTypeID().equals(NHAP_KHO_TU_BAN_HANG_TRA_LAI)).collect(Collectors.toList());
                    ledgers1 = handleRecord(listSubRecord1, lstFail); // Ghi sổ cái
                    repositoryLedgers1 = handleRepositoryLedger(listSubRecord1, lstFail); // ghi sổ kho
                    listSubRecord1.removeAll(lstFail);
                    if (ledgers1.size() > 0) {
                        viewVoucherNoRespository.saveGeneralLedger(ledgers1);
                        viewVoucherNoRespository.updateVoucherRefRecorded(listSubRecord1);
                        viewVoucherNoRespository.updateTableRecord(listSubRecord1);
                    }
                    if (repositoryLedgers1.size() > 0) {
                        viewVoucherNoRespository.saveRepositoryLedger(repositoryLedgers1);
                    }

                    lstID1 = listSubRecord1.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList());
                    listSubRecord2 = listSubRecordMain.stream().filter(n -> !lstID1.contains(n.getRefID())).collect(Collectors.toList());
                    ledgers2 = handleRecord(listSubRecord2, lstFail); // Ghi sổ cái
                    repositoryLedgers2 = handleRepositoryLedger(listSubRecord2, lstFail); // ghi sổ kho
                    listSubRecord2.removeAll(lstFail);
                    if (ledgers2.size() > 0) {
                        viewVoucherNoRespository.saveGeneralLedger(ledgers2);
                        viewVoucherNoRespository.updateVoucherRefRecorded(listSubRecord2);
                        viewVoucherNoRespository.updateTableRecord(listSubRecord2);
                    }
                    if (repositoryLedgers2.size() > 0) {
                        viewVoucherNoRespository.saveRepositoryLedger(repositoryLedgers2);
                    }
                }
                if (listSubDelete.size() > 0) {
                    checkDelete(listSubDelete, lstFail, userDTO);
                    handleDelete(listSubDelete, lstFail);
                }
                break;
            case Constants.ClOSE_BOOK.DELETE:
                listSubRecordMain = listSub.stream().filter(n -> closeBookDTO.getListDataChangeDiff().stream().filter(d -> d.getChecked1()).map(c -> c.getRefID()).collect(Collectors.toList()).contains(n.getRefID())).collect(Collectors.toList());
                listSubDate = listSub.stream().filter(n -> closeBookDTO.getListDataChangeDiff().stream().filter(d -> d.getChecked3()).map(c -> c.getRefID()).collect(Collectors.toList()).contains(n.getRefID())).collect(Collectors.toList());
                checkDelete(listMain, lstFail, userDTO);
                handleDelete(listMain, lstFail);
                if (listSubRecordMain.size() > 0) {
                    listSubRecord1 = listSubRecordMain.stream().filter(n -> n.getRefTable().equals("PPInvoice") || n.getTypeID().equals(NHAP_KHO)
                        || n.getTypeID().equals(NHAP_KHO_TU_DIEU_CHINH) || n.getTypeID().equals(NHAP_KHO_TU_MUA_HANG) || n.getTypeID().equals(NHAP_KHO_TU_BAN_HANG_TRA_LAI)).collect(Collectors.toList());
                    ledgers1 = handleRecord(listSubRecord1, lstFail); // Ghi sổ cái
                    repositoryLedgers1 = handleRepositoryLedger(listSubRecord1, lstFail); // ghi sổ kho
                    listSubRecord1.removeAll(lstFail);
                    if (ledgers1.size() > 0) {
                        viewVoucherNoRespository.saveGeneralLedger(ledgers1);
                        viewVoucherNoRespository.updateVoucherRefRecorded(listSubRecord1);
                        viewVoucherNoRespository.updateTableRecord(listSubRecord1);
                    }
                    if (repositoryLedgers1.size() > 0) {
                        viewVoucherNoRespository.saveRepositoryLedger(repositoryLedgers1);
                    }

                    lstID1 = listSubRecord1.stream().map(ViewVoucherNo::getRefID).collect(Collectors.toList());
                    listSubRecord2 = listSubRecordMain.stream().filter(n -> !lstID1.contains(n.getRefID())).collect(Collectors.toList());
                    ledgers2 = handleRecord(listSubRecord2, lstFail); // Ghi sổ cái
                    repositoryLedgers2 = handleRepositoryLedger(listSubRecord2, lstFail); // ghi sổ kho
                    listSubRecord2.removeAll(lstFail);
                    if (ledgers2.size() > 0) {
                        viewVoucherNoRespository.saveGeneralLedger(ledgers2);
                        viewVoucherNoRespository.updateVoucherRefRecorded(listSubRecord2);
                        viewVoucherNoRespository.updateTableRecord(listSubRecord2);
                    }
                    if (repositoryLedgers2.size() > 0) {
                        viewVoucherNoRespository.saveRepositoryLedger(repositoryLedgers2);
                    }
                }
                if (listSubDate.size() > 0) {
                    handleChangePostedDate(listSubDate, closeBookDTO.getListDataChangeDiff().stream().filter(d -> d.getChecked2()).collect(Collectors.toList()), lstFail);
                }
                break;
        }
        if (lstFail.size() == 0) {
            UpdateDateClosedBook updateDateClosedBook = new UpdateDateClosedBook();
            String dBDateClosed = Utils.DBDateClosed(userDTO);
            if (!StringUtils.isEmpty(dBDateClosed)) {
                LocalDate dBDateClosedOld = LocalDate.parse(dBDateClosed);
                updateDateClosedBook.setDateClosedBookOld(dBDateClosedOld);
            }
            updateDateClosedBook.setDateClosedBook(closeBookDTO.getPostedDate());
            if (closeBookDTO.getLstBranch() != null) {
                updateCloseBookDateForBranch(closeBookDTO.getLstBranch(), closeBookDTO.getPostedDateNew());
            }
            updateDateClosedBook(updateDateClosedBook);
        }
        lstFail = lstFail.stream().distinct().collect(Collectors.toList());
        handlingResultDTO.setListFail(lstFail);
        handlingResultDTO.setCountSuccessVouchers(lstVoucherNoList.size() - lstFail.size());
        handlingResultDTO.setCountFailVouchers(lstFail.size());
        handlingResultDTO.setCountTotalVouchers(lstVoucherNoList.size());
        return handlingResultDTO;
    }

    @Override
    public Boolean updateDateClosedBook(UpdateDateClosedBook updateDateClosedBook) {
        Optional<SecurityDTO> org = SecurityUtils.getCurrentUserLoginAndOrg();
        if (org.isPresent()) {
            return viewVoucherNoRespository.updateDateClosedBook(
                org.get().getOrg(),
                Constants.SystemOption.DBDateClosed,
                Constants.SystemOption.DBDateClosedOld,
                updateDateClosedBook.getDateClosedBook(),
                updateDateClosedBook.getDateClosedBookOld());
        } else {
            return false;
        }
    }

    @Override
    public String checkCloseBookForListBranch(List<OrgTreeDTO> orgTreeDTOS, LocalDate postedDate) {
        String result = "";
        UserDTO userDTO = userService.getAccount();
        Integer book = Utils.PhienSoLamViec(userDTO);
        for (OrgTreeDTO orgTreeDTO : orgTreeDTOS) {
            List<ViewVoucherNo> list = viewVoucherNoRespository.getAllVoucherNotRecorded(orgTreeDTO.getValue(), book, postedDate);
            if (list.size() > 0) {
                if (!StringUtils.isEmpty(result)) {
                    result += ",";
                }
                result += orgTreeDTO.getText();
            }
        }
        if (!StringUtils.isEmpty(result)) {
            result = "Khóa sổ không thành công! \n" + result + " đang tồn tại chứng từ chưa được ghi sổ" + "\nVui lòng xử lý các chứng từ này và thử lại!";
        }
        return result;
    }

    @Override
    public void updateCloseBookDateForBranch(List<OrgTreeDTO> orgTreeDTOS, LocalDate postedDate) {
        viewVoucherNoRespository.updateCloseBookDateForBranch(orgTreeDTOS, postedDate);
    }

    void handleChangePostedDate(List<ViewVoucherNo> listMain, List<ViewVoucherNo> listSubChangePostedDate, LocalDate postedDate, List<ViewVoucherNo> lstFail) {
        viewVoucherNoRespository.updateVoucherPostedDate(listMain, listSubChangePostedDate, postedDate);
    }

    void handleChangePostedDate(List<ViewVoucherNo> listSubDateRoot, List<ViewVoucherNo> listDateChangeAfter, List<ViewVoucherNo> lstFail) {
        viewVoucherNoRespository.handleChangePostedDate(listSubDateRoot, listDateChangeAfter);
    }

    void handleDelete(List<ViewVoucherNo> lstViewVoucherNos, List<ViewVoucherNo> lstFail) {
        viewVoucherNoRespository.deleteVoucher(lstViewVoucherNos);
    }

    void checkDelete(List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail, UserDTO userDTO) {
        List<ViewVoucherNo> fail = new ArrayList<>();
        for (ViewVoucherNo viewVoucherNo : viewVoucherNos) {
            switch (viewVoucherNo.getTypeID()) {
                case BAN_HANG_CHUA_THU_TIEN:
                case BAN_HANG_THU_TIEN_NGAY_TM:
                case BAN_HANG_THU_TIEN_NGAY_CK:
                case MUA_HANG_TRA_LAI:
                case HANG_GIAM_GIA:
                case HANG_BAN_TRA_LAI:
                    if (viewVoucherNo.getInvoiceForm() != null && viewVoucherNo.getInvoiceNo() != null && viewVoucherNo.getInvoiceForm().compareTo(Constants.InvoiceForm.HD_DIEN_TU) == 0 &&
                        userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_SDTichHopHDDT)).findAny().get().getData().equals("1")) {
                        viewVoucherNo.setReasonFail("Hóa đơn đã cấp số !");
                        fail.add(viewVoucherNo);
                    }
                    break;
            }
        }
        lstFail.addAll(fail);
        viewVoucherNos.removeAll(fail);
    }

    /**
     * @param lstRecord
     * @param lstFail
     * @return
     * @Author hautv
     */
    @Override
    public List<GeneralLedger> handleRecord(List<ViewVoucherNo> lstRecord, List<ViewVoucherNo> lstFail) {
        Map<Integer, List<ViewVoucherNo>> lstRecordGroupByTypeID =
            lstRecord.stream().collect(Collectors.groupingBy(w -> w.getTypeID()));
        List<GeneralLedger> lsGeneralLedgers = new ArrayList<>();
        UserDTO userDTO = userService.getAccount();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Boolean checkQuaSLTon = false;
        Boolean checkXuatQuaTonQuy = false;
        List<MGForPPOrderConvertDTO> mgForPPOrderConvertDTOS = new ArrayList<>();
        List<ViewGLPayExceedCashDTO> viewGLPayExceedCashAll = new ArrayList<>();
        for (SystemOption sys : userDTO.getSystemOption()) {
            if (sys.getCode().equals(Constants.SystemOption.HH_XUATQUASLTON)) {
                checkQuaSLTon = sys.getData().equals("0");
            }
            if (sys.getCode().equals(Constants.SystemOption.XUAT_QUA_TON_QUY)) {
                checkXuatQuaTonQuy = sys.getData().equals("0");
            }
        }
        if (checkXuatQuaTonQuy) {
            mgForPPOrderConvertDTOS = materialGoodsRepository.getMaterialGoodsForCombobox1(systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMVTHH), null);
        }
        if (checkXuatQuaTonQuy) {
            viewGLPayExceedCashAll = generalLedgerRepository.getViewGLPayExceedCash(Constants.TypeLedger.BOTH_BOOK, userDTO.getOrganizationUnit().getId(), LocalDate.now());
        }
        for (Map.Entry<Integer, List<ViewVoucherNo>> listEntry : lstRecordGroupByTypeID.entrySet()) {
            switch (listEntry.getKey()) {
                case TypeConstant
                    .PHIEU_THU:
                case TypeConstant
                    .PHIEU_THU_TIEN_KHACH_HANG:
                    lsGeneralLedgers.addAll(genGeneralLedgersMCReceipt(listEntry.getValue(), lstFail));
                    break;
                case TypeConstant
                    .PHIEU_CHI:
                case TypeConstant
                    .PHIEU_CHI_TRA_TIEN_NHA_CUNG_CAP:
                    lsGeneralLedgers.addAll(genGeneralLedgersMCPayment(listEntry.getValue(), lstFail));
                    break;
                case UY_NHIEM_CHI:
                case SEC_CHUYEN_KHOAN:
                case SEC_TIEN_MAT:
                case TYPE_ID_UNC_MUA_HANG:
                case TYPE_ID_SCK_MUA_HANG:
                case TYPE_ID_STM_MUA_HANG:
                case TYPE_ID_UNC_MUA_DICH_VU:
                case TYPE_ID_SCK_MUA_DICH_VU:
                case TYPE_ID_STM_MUA_DICH_VU:
                case BAO_NO_UNC_TRA_TIEN_NCC:
                case BAO_NO_SCK_TRA_TIEN_NCC:
                case BAO_NO_STM_TRA_TIEN_NCC:
                    lsGeneralLedgers.addAll(genGeneralLedgersMBTellerPaper(listEntry.getValue(), lstFail, viewGLPayExceedCashAll));
                    break;
                case NOP_TIEN_TU_TAI_KHOAN:
                case NOP_TIEN_TU_KHACH_HANG:
                    lsGeneralLedgers.addAll(genGeneralLedgersMBDeposit(listEntry.getValue(), lstFail));
                    break;
                case THE_TIN_DUNG:
                case THE_TIN_DUNG_TRA_TIEN_NCC:
                    lsGeneralLedgers.addAll(genGeneralLedgersMBCreditCard(listEntry.getValue(), lstFail, viewGLPayExceedCashAll));
                    break;
                case CHUNG_TU_NGHIEP_VU_KHAC:
                    lsGeneralLedgers.addAll(genGeneralLedgersGOtherVoucher(listEntry.getValue(), lstFail, viewGLPayExceedCashAll));
                    break;
                case BAN_HANG_CHUA_THU_TIEN:
                case BAN_HANG_THU_TIEN_NGAY_TM:
                case BAN_HANG_THU_TIEN_NGAY_CK:
                    lsGeneralLedgers.addAll(genGeneralLedgersSAInvoice(listEntry.getValue(), lstFail, mgForPPOrderConvertDTOS));
                    break;
                case HANG_BAN_TRA_LAI:
                case HANG_GIAM_GIA:
                    lsGeneralLedgers.addAll(genGeneralLedgersSAReturn(listEntry.getValue(), lstFail, mgForPPOrderConvertDTOS, viewGLPayExceedCashAll));
                    break;
                case NHAP_KHO:
                    lsGeneralLedgers.addAll(genGeneralLedgersInWard(listEntry.getValue(), lstFail));
                    break;
                case XUAT_KHO:
                    lsGeneralLedgers.addAll(genGeneralLedgersOutWard(listEntry.getValue(), lstFail));
                    break;
                case CHUYEN_KHO:
                case CHUYEN_KHO_GUI_DAI_LY:
                case CHUYEN_KHO_CHUYEN_NOI_BO:
                    lsGeneralLedgers.addAll(genGeneralLedgersRSTransfer(listEntry.getValue(), lstFail));
                    break;
                case Constants.PPServiceType.PPSERVICE_UNPAID:
                case Constants.PPServiceType.PPSERVICE_CASH:
                case Constants.PPServiceType.PPSERVICE_PAYMENT_ORDER:
                case Constants.PPServiceType.PPSERVICE_TRANSFER_SEC:
                case Constants.PPServiceType.PPSERVICE_CREDIT_CARD:
                case Constants.PPServiceType.PPSERVICE_CASH_SEC:
                    lsGeneralLedgers.addAll(genGeneralLedgersPPService(listEntry.getValue(), lstFail, mgForPPOrderConvertDTOS, viewGLPayExceedCashAll));
                    break;
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_CHUA_THANH_TOAN:
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_TIEN_MAT:
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_UY_NHIEM_CHI:
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_CK:
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_TIEN_MAT:
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_THE_TIN_DUNG:
                    lsGeneralLedgers.addAll(genGeneralLedgersPPInvoice(listEntry.getValue(), lstFail));
                    break;
                case PHAN_BO_CHI_PHI_TRA_TRUOC:
                    lsGeneralLedgers.addAll(genGeneralLedgersGOtherVoucher(listEntry.getValue(), lstFail, viewGLPayExceedCashAll));
                    break;
                case MUA_HANG_TRA_LAI:
                case MUA_HANG_GIAM_GIA:
                    lsGeneralLedgers.addAll(genGeneralLedgersPPdiscountretun(listEntry.getValue(), lstFail));
                    break;
                case KET_CHUYEN_LAI_LO:
                    lsGeneralLedgers.addAll(genGeneralLedgersGOtherVoucherKc(listEntry.getValue(), lstFail));
                    break;
                case KET_CHUYEN_CHI_PHI:
                    lsGeneralLedgers.addAll(genGeneralLedgersCPExpenseTranfer(listEntry.getValue(), lstFail));
                    break;
            }
        }
        return lsGeneralLedgers;
    }

    List<GeneralLedger> genGeneralLedgersGOtherVoucherKc(List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail) {
        List<GeneralLedger> listGenTemp = new ArrayList<>();
        for (ViewVoucherNo parent : viewVoucherNos) {
            List<ViewVoucherNoDetailDTO> listDetail = parent.getViewVoucherNoDetailDTOS();
            boolean checkContinue = false;
            for (int i = 0; i < listDetail.size(); i++) {
                if (listDetail.get(i).getAmount() == null || listDetail.get(i).getAmount().doubleValue() < 0) {
                    parent.setReasonFail(Constants.MSGRecord.KC_DU_LIEU_AM);
                    lstFail.add(parent);
                    checkContinue = true;
                    viewVoucherNos.remove(parent);
                    break;
                }
            }
            if (checkContinue) {
                continue;
            }

            listDetail.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));
            for (ViewVoucherNoDetailDTO detailDTO : listDetail) {
                GeneralLedger generalLedger = new GeneralLedger();
                try {
                    // cặp nợ có
                    BeanUtils.copyProperties(generalLedger, parent);
                    generalLedger.setNoFBook(parent.getNoFBook());
                    generalLedger.setNoMBook(parent.getNoMBook());

                    generalLedger.setTypeID(parent.getTypeID());
                    generalLedger.account(detailDTO.getDebitAccount());
                    generalLedger.accountCorresponding(detailDTO.getCreditAccount());
                    generalLedger.debitAmount(detailDTO.getAmount());
                    generalLedger.debitAmountOriginal(detailDTO.getAmountOriginal());
                    generalLedger.creditAmount(BigDecimal.ZERO);
                    generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                    generalLedger.description(detailDTO.getDescription());
                    generalLedger.setReferenceID(parent.getRefID());
                    generalLedger.setDetailID(detailDTO.getRefID());
                    generalLedger.refDateTime(parent.getDate());
                    generalLedger.setCompanyID(parent.getCompanyID());
                    generalLedger.setRefTable(parent.getRefTable());
                    generalLedger.setTypeLedger(parent.getTypeLedger());

                    // thành tiền lớn hơn 0 ms lưu
                    if (detailDTO.getAmount() != null && detailDTO.getAmount().doubleValue() > 0) {
                        listGenTemp.add(generalLedger);
                    }

                    GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                    BeanUtils.copyProperties(generalLedgerCorresponding, generalLedger);
                    generalLedgerCorresponding.account(detailDTO.getCreditAccount());
                    generalLedgerCorresponding.accountCorresponding(detailDTO.getDebitAccount());
                    generalLedgerCorresponding.creditAmount(detailDTO.getAmount());
                    generalLedgerCorresponding.creditAmountOriginal(detailDTO.getAmountOriginal());
                    generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                    generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);

                    if (detailDTO.getAmount() != null && detailDTO.getAmount().doubleValue() > 0) {
                        listGenTemp.add(generalLedgerCorresponding);
                    }

                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return listGenTemp;
    }

    /**
     * @param lstRecord
     * @param lstFail
     * @return
     * @Author hautv
     */
    @Override
    public List<RepositoryLedger> handleRepositoryLedger(List<ViewVoucherNo> lstRecord, List<ViewVoucherNo> lstFail) {
        Map<Integer, List<ViewVoucherNo>> lstRecordGroupByTypeID =
            lstRecord.stream().collect(Collectors.groupingBy(w -> w.getTypeID()));
        List<RepositoryLedger> repositoryLedgers = new ArrayList<>();
        UserDTO userDTO = userService.getAccount();
        Boolean checkQuaSLTon = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.HH_XUATQUASLTON)).findAny().get().getData().equals("0");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<MGForPPOrderConvertDTO> mgForPPOrderConvertDTOS = materialGoodsRepository.getMaterialGoodsForCombobox1(
            systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMVTHH), null);
        for (Map.Entry<Integer, List<ViewVoucherNo>> listEntry : lstRecordGroupByTypeID.entrySet()) {
            switch (listEntry.getKey()) {
                case TypeConstant
                    .PHIEU_THU:
                case TypeConstant
                    .PHIEU_THU_TIEN_KHACH_HANG:
                    break;
                case TypeConstant
                    .PHIEU_CHI:
                case TypeConstant
                    .PHIEU_CHI_TRA_TIEN_NHA_CUNG_CAP:
                    break;
                case UY_NHIEM_CHI:
                case SEC_CHUYEN_KHOAN:
                case SEC_TIEN_MAT:
                case TYPE_ID_UNC_MUA_HANG:
                case TYPE_ID_SCK_MUA_HANG:
                case TYPE_ID_STM_MUA_HANG:
                case TYPE_ID_UNC_MUA_DICH_VU:
                case TYPE_ID_SCK_MUA_DICH_VU:
                case TYPE_ID_STM_MUA_DICH_VU:
                case BAO_NO_UNC_TRA_TIEN_NCC:
                case BAO_NO_SCK_TRA_TIEN_NCC:
                case BAO_NO_STM_TRA_TIEN_NCC:
                    // repositoryLedgers.addAll(repositoryLedgersSAInvoice(listEntry.getValue(), lstFail));
                    break;
                case BAN_HANG_CHUA_THU_TIEN:
                case BAN_HANG_THU_TIEN_NGAY_TM:
                case BAN_HANG_THU_TIEN_NGAY_CK:
                    repositoryLedgers.addAll(repositoryLedgersSAInvoice(listEntry.getValue(), lstFail, mgForPPOrderConvertDTOS, checkQuaSLTon));
                    break;
                case HANG_BAN_TRA_LAI:
                case HANG_GIAM_GIA:
                    repositoryLedgers.addAll(repositoryLedgersSAReturn(listEntry.getValue(), lstFail, mgForPPOrderConvertDTOS, checkQuaSLTon));
                    break;
                case NHAP_KHO:
                    repositoryLedgers.addAll(repositoryLedgersInWard(listEntry.getValue(), lstFail));
                    break;
                case XUAT_KHO:
                    repositoryLedgers.addAll(repositoryLedgersOutWard(listEntry.getValue(), lstFail));
                    break;
                case CHUYEN_KHO:
                case CHUYEN_KHO_GUI_DAI_LY:
                case CHUYEN_KHO_CHUYEN_NOI_BO:
                    repositoryLedgers.addAll(repositoryLedgersRSTransfer(listEntry.getValue(), lstFail));
                    break;
                case MUA_HANG_TRA_LAI:
                    repositoryLedgers.addAll(repositoryLedgersPPDiscountReturn(listEntry.getValue(), lstFail, checkQuaSLTon, mgForPPOrderConvertDTOS));
                    break;
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_CHUA_THANH_TOAN:
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_TIEN_MAT:
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_UY_NHIEM_CHI:
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_CK:
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_TIEN_MAT:
                case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_THE_TIN_DUNG:
                    repositoryLedgers.addAll(repositoryLedgersPPInvoice(listEntry.getValue()
                        .stream()
                        .filter(ViewVoucherNo::getStoredInRepository)
                        .collect(Collectors.toList()), lstFail));
                    break;
            }
        }
        return repositoryLedgers;
    }

    @Override
    public List<Toolledger> handleToolRecord(List<ViewVoucherNo> lstRecord, List<ViewVoucherNo> lstFail) {
        Map<Integer, List<ViewVoucherNo>> lstRecordGroupByTypeID =
            lstRecord.stream().collect(Collectors.groupingBy(w -> w.getTypeID()));
        List<Toolledger> toolledgers = new ArrayList<>();
        UserDTO userDTO = userService.getAccount();
        Boolean checkQuaSLTon = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.HH_XUATQUASLTON)).findAny().get().getData().equals("0");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<MGForPPOrderConvertDTO> mgForPPOrderConvertDTOS = materialGoodsRepository.getMaterialGoodsForCombobox1(
            systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMVTHH), null);
        for (Map.Entry<Integer, List<ViewVoucherNo>> listEntry : lstRecordGroupByTypeID.entrySet()) {
            switch (listEntry.getKey()) {
                case GHI_TANG_CCDC:
                    toolledgers.addAll(toolLedgersTIIncrement(listEntry.getValue(), lstFail));
                    break;
                case GHI_GIAM_CCDC:
                    toolledgers.addAll(toolLedgersTIDecrement(listEntry.getValue(), lstFail));
                    break;

            }
        }
        return toolledgers;
    }

    private Collection<? extends Toolledger> toolLedgersTIDecrement(List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail) {
        List<Toolledger> toolledgers = new ArrayList<>();
        for (ViewVoucherNo parent : viewVoucherNos) {
            for (ViewVoucherNoDetailDTO detailDTO : parent.getViewVoucherNoDetailDTOS()) {
                // Dòng 1
//                Optional<Tools> tool = toolsRepository.getToolsById(detailDTO.getToolsID());
                Toolledger toolledger = new Toolledger();
//                if (tool.isPresent()) {
//                    toolledger.setIncrementAllocationTime(BigDecimal.valueOf(tool.get().getAllocationTimes()));
//                    toolledger.setAllocatedAmount(tool.get().getAllocatedAmount());
//                }
                toolledger.setBranchID(null);
                toolledger.setReferenceID(parent.getId());
                toolledger.setCompanyID(parent.getCompanyID());
                toolledger.setTypeID(parent.getTypeID());
                toolledger.date(parent.getDate());
                toolledger.noFBook(parent.getNoFBook());
                toolledger.noMBook(parent.getNoMBook());
                toolledger.typeLedger(parent.getTypeLedger());
//                toolledger.setToolsID(detailDTO.getToolsID());
//            toolledger.setDescription(detailDTO.get());
                toolledger.setReason(parent.getReason());
//            toolledger.setUnitPrice(detailDTO.get());
                toolledger.setIncrementAllocationTime(BigDecimal.ZERO);
                toolledger.setDecrementAllocationTime(BigDecimal.ZERO);
                toolledger.setIncrementQuantity(BigDecimal.ZERO);
//                toolledger.setDecrementQuantity(detailDTO.getDecrementQuantity());
                toolledger.setIncrementAmount(BigDecimal.ZERO);
                toolledger.setDecrementAmount(BigDecimal.ZERO);
                toolledger.setAllocationAmount(BigDecimal.ZERO);
                toolledger.setAllocatedAmount(BigDecimal.ZERO);
                toolledger.setOrderPriority(detailDTO.getOrderPriority());

//                Toolledger toolledger = new Toolledger();
//                toolledger.setBranchID(null);
//                toolledger.setReferenceID(parent.getId());
//                toolledger.setCompanyID(parent.getCompanyID());
//                toolledger.setTypeID(parent.getTypeID());
//                toolledger.date(parent.getDate());
//                toolledger.noFBook(parent.getNoFBook());
//                toolledger.noMBook(parent.getNoMBook());
//                toolledger.typeLedger(parent.getTypeLedger());
////                toolledger.setToolsID(detailDTO.getToolsID());
//                toolledger.setDescription(detailDTO.getDescription());
//                toolledger.setReason(parent.getReason());
//                toolledger.setUnitPrice(detailDTO.getUnitPrice());
//                toolledger.setDepartmentID(detailDTO.getDepartmentID());
//                toolledger.setIncrementAllocationTime(BigDecimal.ZERO);
//                toolledger.setDecrementAllocationTime(BigDecimal.ZERO);
//                toolledger.setIncrementQuantity(detailDTO.getQuantity());
//                toolledger.setDecrementQuantity(BigDecimal.ZERO);
//                toolledger.setIncrementAmount(parent.getTotalAmount());
//                toolledger.setDecrementAmount(BigDecimal.ZERO);
//                toolledger.setAllocationAmount(BigDecimal.ZERO);
//                toolledger.setAllocatedAmount(BigDecimal.ZERO);
                toolledgers.add(toolledger);
            }
        }
        return toolledgers;
    }

    @Override
    public List<FixedAssetLedger> handleFixedRecord(List<ViewVoucherNo> lstRecord, List<ViewVoucherNo> lstFail) {
        Map<Integer, List<ViewVoucherNo>> lstRecordGroupByTypeID =
            lstRecord.stream().collect(Collectors.groupingBy(w -> w.getTypeID()));
        List<FixedAssetLedger> fixedAssetLedgers = new ArrayList<>();
        for (Map.Entry<Integer, List<ViewVoucherNo>> listEntry : lstRecordGroupByTypeID.entrySet()) {
            switch (listEntry.getKey()) {
                case TSCD_GHI_TANG:
                    fixedAssetLedgers.addAll(fixedAssetLedgersFAIncrement(listEntry.getValue(), lstFail));
                    break;

            }
        }
        return fixedAssetLedgers;
    }

    private Collection<? extends FixedAssetLedger> fixedAssetLedgersFAIncrement(List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail) {
        List<FixedAssetLedger> fixedAssetLedgers = new ArrayList<>();
        for (ViewVoucherNo parent : viewVoucherNos) {
            for (ViewVoucherNoDetailDTO detailDTO : parent.getViewVoucherNoDetailDTOS()) {
                FixedAssetLedger fixedAssetLedger = new FixedAssetLedger();
                fixedAssetLedger.setBranchID(null);
                fixedAssetLedger.setReferenceID(parent.getId());
                fixedAssetLedger.setCompanyID(parent.getCompanyID());
                fixedAssetLedger.setTypeID(parent.getTypeID());
                fixedAssetLedger.setDescription(null);
                fixedAssetLedger.setReason(parent.getReason());
                fixedAssetLedger.setDepartmentID(detailDTO.getDepartmentID());
                fixedAssetLedger.setDate(parent.getDate());
                fixedAssetLedger.setNoFBook(parent.getNoFBook());
                fixedAssetLedger.setNoMBook(parent.getNoMBook());
//                FixedAssetDTO fixedAsset = fixedAssetRepository.getById(detailDTO.getFixedAssetID());
//                if (fixedAsset != null) {
//                    fixedAssetLedger.setFixedAssetID(fixedAsset.getID());
//                    fixedAssetLedger.setFixedAssetCode(fixedAsset.getFixedAssetCode());
//                    fixedAssetLedger.setFixedAssetName(fixedAsset.getFixedAssetName());
//                    fixedAssetLedger.setUsedTime(fixedAsset.getUsedTime());
//                    fixedAssetLedger.setDepreciationRate(fixedAsset.getDepreciationRate());
//                    fixedAssetLedger.setDepreciationAmount(fixedAsset.getPurchasePrice());
//                    fixedAssetLedger.setDepreciationAccount(fixedAsset.getDepreciationAccount());
//                    fixedAssetLedger.setOriginalPrice(fixedAsset.getOriginalPrice());
//                    fixedAssetLedger.setOriginalPriceAccount(fixedAsset.getOriginalPriceAccount());
//                    fixedAssetLedger.setUsedTimeRemain(fixedAsset.getUsedTime());
//                    fixedAssetLedger.setDifferUsedTime(null);
//                    fixedAssetLedger.setMonthDepreciationRate(fixedAsset.getMonthDepreciationRate());
//                    fixedAssetLedger.setMonthPeriodDepreciationAmount(fixedAsset.getMonthPeriodDepreciationAmount());
//                    fixedAssetLedger.setAcDepreciationAmount(BigDecimal.ZERO);
//                    fixedAssetLedger.setRemainingAmount(fixedAsset.getPurchasePrice());
//                    fixedAssetLedger.setDifferOrgPrice(null);
//                    fixedAssetLedger.setDifferAcDepreciationAmount(null);
//                    fixedAssetLedger.setDifferMonthlyDepreciationAmount(null);
//                    fixedAssetLedger.setDifferRemainingAmount(null);
//                    fixedAssetLedger.setDifferDepreciationAmount(null);
//                }

                fixedAssetLedgers.add(fixedAssetLedger);
            }
        }
        return fixedAssetLedgers;
    }

    @Override
    public Page<ViewVoucherDTO> findAllByTypeGroupID(Pageable pageable, Integer typeGroupID, String fromDate, String toDate) {
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        return viewVoucherNoRespository.findAllByTypeGroupID(pageable, typeGroupID, userDTO.getOrganizationUnit().getId(), phienSoLamViec, fromDate, toDate);
    }

    @Override
    public Page<ViewVoucherDTO> searchVoucher(Pageable pageable, Integer typeSearch, Integer typeGroupID, String no, String invoiceDate, Boolean recorded, String fromDate, String toDate) {
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);
        return viewVoucherNoRespository.searchVoucher(pageable, typeSearch, typeGroupID, no, invoiceDate, recorded, fromDate, toDate, phienSoLamViec, userDTO.getOrganizationUnit().getId());
    }

    @Override
    public Respone_SDS resetNo(RequestResetNoDTO requestResetNoDTO) {
        Respone_SDS respone_sds = new Respone_SDS();
        UserDTO userDTO = userService.getAccount();
        Integer phienSoLamViec = Utils.PhienSoLamViec(userDTO);

        List<ViewVoucherDTO> viewVoucherDTOS = viewVoucherNoRespository.findAllByTypeGroupID(requestResetNoDTO.getTypeGroupID(), userDTO.getOrganizationUnit().getId(), phienSoLamViec, requestResetNoDTO.getFromDate(), requestResetNoDTO.getToDate());
        Integer startNumber = Integer.valueOf(requestResetNoDTO.getCurrentValue());
        Integer lenght = requestResetNoDTO.getCurrentValue().length();
        String prefix = requestResetNoDTO.getPrefix() == null ? "" : requestResetNoDTO.getPrefix();
        String suffix = requestResetNoDTO.getSuffix() == null ? "" : requestResetNoDTO.getSuffix();
        List<String> lstNo = new ArrayList<>();
        List<UUID> lstID = new ArrayList<>();
        for (ViewVoucherDTO item : viewVoucherDTOS) {
            item.setNoNew(prefix + StringUtils.leftPad(String.valueOf(startNumber), lenght, '0') + suffix);
            lstNo.add(item.getNoNew());
            lstID.add(item.getId());
            startNumber++;
        }
        if (requestResetNoDTO.getVoucher().size() > 0) {
            lstNo.addAll(requestResetNoDTO.getVoucher().stream().map(ViewVoucherDTO::getNoNew).collect(Collectors.toList()));
            lstID.addAll(requestResetNoDTO.getVoucher().stream().map(ViewVoucherDTO::getId).collect(Collectors.toList()));
        }
        List<GenCode> genCodes = genCodeRepository.findByTypeGroupIDAndCompanyID(requestResetNoDTO.getTypeGroupID(), userDTO.getOrganizationUnit().getId());
        Boolean onlyOneBook = genCodes.size() == 1;
        // Check trùng
        if (onlyOneBook || phienSoLamViec.equals(Constants.TypeLedger.FINANCIAL_BOOK)) {
            if (viewVoucherNoRespository.checkExistNoForResetFBook(userDTO.getOrganizationUnit().getId(), lstNo, lstID).intValue() > 0) {
                respone_sds.setStatus(Constants.ResetNoVoucher.Duplicate);
                respone_sds.setMessage("Số chứng từ bị trùng");
                return respone_sds;
            }
        } else {
            if (viewVoucherNoRespository.checkExistNoForResetMBook(userDTO.getOrganizationUnit().getId(), lstNo, lstID).intValue() > 0) {
                respone_sds.setStatus(Constants.ResetNoVoucher.Duplicate);
                respone_sds.setMessage("Số chứng từ bị trùng");
                return respone_sds;
            }
        }
        viewVoucherNoRespository.resetNo(viewVoucherDTOS, phienSoLamViec, onlyOneBook);
        if (requestResetNoDTO.getVoucher().size() > 0) {
            viewVoucherNoRespository.resetNo(requestResetNoDTO.getVoucher(), phienSoLamViec, onlyOneBook);
        }

        respone_sds.setStatus(Constants.ResetNoVoucher.Success);
        return respone_sds;
    }

    private Collection<? extends Toolledger> toolLedgersTIIncrement(List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail) {
        List<Toolledger> toolledgers = new ArrayList<>();
        for (ViewVoucherNo parent : viewVoucherNos) {
            for (ViewVoucherNoDetailDTO detailDTO : parent.getViewVoucherNoDetailDTOS()) {
                // Dòng 1
                Toolledger toolledger = new Toolledger();
                toolledger.setBranchID(null);
                toolledger.setReferenceID(parent.getId());
                toolledger.setCompanyID(parent.getCompanyID());
                toolledger.setTypeID(parent.getTypeID());
                toolledger.date(parent.getDate());
                toolledger.noFBook(parent.getNoFBook());
                toolledger.noMBook(parent.getNoMBook());
                toolledger.typeLedger(parent.getTypeLedger());
//                toolledger.setToolsID(detailDTO.getToolsID());
                toolledger.setDescription(detailDTO.getDescription());
                toolledger.setReason(parent.getReason());
                toolledger.setUnitPrice(detailDTO.getUnitPrice());
                toolledger.setDepartmentID(detailDTO.getDepartmentID());
                toolledger.setIncrementAllocationTime(BigDecimal.ZERO);
                toolledger.setDecrementAllocationTime(BigDecimal.ZERO);
                toolledger.setIncrementQuantity(detailDTO.getQuantity());
                toolledger.setDecrementQuantity(BigDecimal.ZERO);
                toolledger.setIncrementAmount(parent.getTotalAmount());
                toolledger.setDecrementAmount(BigDecimal.ZERO);
                toolledger.setAllocationAmount(BigDecimal.ZERO);
                toolledger.setAllocatedAmount(BigDecimal.ZERO);
                toolledgers.add(toolledger);
            }
        }
        return toolledgers;
    }

    /*Phiếu thu*/
    List<GeneralLedger> genGeneralLedgersMCReceipt(List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail) {
        List<GeneralLedger> generalLedgers = new ArrayList<>();
        for (ViewVoucherNo parent : viewVoucherNos) {
            for (ViewVoucherNoDetailDTO detailDTO : parent.getViewVoucherNoDetailDTOS()) {
                // Dòng 1
                GeneralLedger generalLedger = new GeneralLedger();
                generalLedger.setCompanyID(parent.getCompanyID());
                generalLedger.setReferenceID(parent.getRefID());
                generalLedger.setDetailID(detailDTO.getRefID());
                generalLedger.setTypeID(parent.getTypeID());
                generalLedger.date(parent.getDate());
                generalLedger.postedDate(parent.getPostedDate());
                generalLedger.noFBook(parent.getNoFBook());
                generalLedger.noMBook(parent.getNoMBook());
                generalLedger.typeLedger(parent.getTypeLedger());
                generalLedger.accountingObjectID(detailDTO.getAccountingObjectID());
                generalLedger.accountingObjectCode(detailDTO.getAccountingObjectCode());
                generalLedger.accountingObjectName(detailDTO.getAccountingObjectName());
                generalLedger.accountingObjectAddress(detailDTO.getAccountingObjectAddress());
                generalLedger.account(detailDTO.getDebitAccount());
                generalLedger.accountCorresponding(detailDTO.getCreditAccount());
                generalLedger.bankAccountDetailID(detailDTO.getBankAccountDetailID());
                generalLedger.bankAccount(detailDTO.getBankAccount());
                generalLedger.bankName(detailDTO.getBankName());
                generalLedger.currencyID(parent.getCurrencyID());
                generalLedger.exchangeRate(parent.getExchangeRate());
                generalLedger.debitAmount(detailDTO.getAmount());
                generalLedger.debitAmountOriginal(detailDTO.getAmountOriginal());
                generalLedger.creditAmount(BigDecimal.ZERO);
                generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                generalLedger.reason(parent.getReason());
                generalLedger.description(detailDTO.getDescription());
                generalLedger.employeeID(parent.getEmployeeID());
                generalLedger.employeeCode(parent.getEmployeeCode());
                generalLedger.employeeName(parent.getEmployeeName());
                generalLedger.budgetItemID(detailDTO.getBudgetItemID());
                generalLedger.costSetID(detailDTO.getCostSetID());
                generalLedger.contractID(detailDTO.getContractID());
                generalLedger.statisticsCodeID(detailDTO.getStatisticsCodeID());
                generalLedger.contactName(parent.getContactName());
                generalLedger.detailID(detailDTO.getRefID());
                generalLedger.refDateTime(parent.getDate());
                generalLedger.departmentID(detailDTO.getDepartmentID());
                generalLedger.expenseItemID(detailDTO.getExpenseItemID());
                generalLedger.setRefTable(parent.getRefTable());
                generalLedgers.add(generalLedger);

                // Dòng 2
                GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                generalLedgerCorresponding.setCompanyID(parent.getCompanyID());
                generalLedgerCorresponding.setReferenceID(parent.getRefID());
                generalLedgerCorresponding.setDetailID(detailDTO.getRefID());
                generalLedgerCorresponding.setTypeID(parent.getTypeID());
                generalLedgerCorresponding.date(parent.getDate());
                generalLedgerCorresponding.postedDate(parent.getPostedDate());
                generalLedgerCorresponding.noFBook(parent.getNoFBook());
                generalLedgerCorresponding.noMBook(parent.getNoMBook());
                generalLedgerCorresponding.typeLedger(parent.getTypeLedger());
                generalLedgerCorresponding.accountingObjectID(detailDTO.getAccountingObjectID());
                generalLedgerCorresponding.accountingObjectCode(detailDTO.getAccountingObjectCode());
                generalLedgerCorresponding.accountingObjectName(detailDTO.getAccountingObjectName());
                generalLedgerCorresponding.accountingObjectAddress(detailDTO.getAccountingObjectAddress());
                generalLedgerCorresponding.account(detailDTO.getCreditAccount());
                generalLedgerCorresponding.accountCorresponding(detailDTO.getDebitAccount());
                generalLedgerCorresponding.bankAccountDetailID(detailDTO.getBankAccountDetailID());
                generalLedgerCorresponding.bankAccount(detailDTO.getBankAccount());
                generalLedgerCorresponding.bankName(detailDTO.getBankName());
                generalLedgerCorresponding.currencyID(parent.getCurrencyID());
                generalLedgerCorresponding.exchangeRate(parent.getExchangeRate());
                generalLedgerCorresponding.creditAmount(detailDTO.getAmount());
                generalLedgerCorresponding.creditAmountOriginal(detailDTO.getAmountOriginal());
                generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);
                generalLedgerCorresponding.reason(parent.getReason());
                generalLedgerCorresponding.description(detailDTO.getDescription());
                generalLedgerCorresponding.employeeID(parent.getEmployeeID());
                generalLedgerCorresponding.employeeCode(parent.getEmployeeCode());
                generalLedgerCorresponding.employeeName(parent.getEmployeeName());
                generalLedgerCorresponding.budgetItemID(detailDTO.getBudgetItemID());
                generalLedgerCorresponding.costSetID(detailDTO.getCostSetID());
                generalLedgerCorresponding.contractID(detailDTO.getContractID());
                generalLedgerCorresponding.statisticsCodeID(detailDTO.getStatisticsCodeID());
                generalLedgerCorresponding.contactName(parent.getContactName());
                generalLedgerCorresponding.detailID(detailDTO.getRefID());
                generalLedgerCorresponding.refDateTime(parent.getDate());
                generalLedgerCorresponding.departmentID(detailDTO.getDepartmentID());
                generalLedgerCorresponding.expenseItemID(detailDTO.getExpenseItemID());
                generalLedgers.add(generalLedgerCorresponding);
            }
        }
        return generalLedgers;
    }

    /*Hàng Mua Trả lại giảm giá*/
    List<GeneralLedger> genGeneralLedgersPPdiscountretun(List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail) {
        List<GeneralLedger> generalLedgers = new ArrayList<>();
        for (ViewVoucherNo parent : viewVoucherNos) {
            Optional<RSInwardOutward> rSInwardOutward = Optional.empty();
            if (parent.getDeliveryVoucher() && parent.getrSInwardOutwardID() != null) {
                rSInwardOutward = rSInwardOutwardRepository.findById(parent.getrSInwardOutwardID());
            }
            AccountingObject empl = null;
            if (parent.getEmployeeID() != null) {
                empl = accountingObjectRepository.findById(parent.getEmployeeID()).get();
            }
            for (ViewVoucherNoDetailDTO detailDTO : parent.getViewVoucherNoDetailDTOS()) {
                Optional<Repository> repository = Optional.empty();
                if (detailDTO.getRepositoryID() != null) {
                    repository = repositoryRepository.findById(detailDTO.getRepositoryID());
                }
                Optional<MaterialGoods> materialGoods = materialGoodsRepository.findById(detailDTO.getMaterialGoodsID());
                Optional<AccountingObject> accountingObject = Optional.empty();
                if (detailDTO.getAccountingObjectID() != null) {
                    accountingObject = accountingObjectRepository.findById(detailDTO.getAccountingObjectID());
                }
                GeneralLedger generalLedger = new GeneralLedger();
                generalLedger.setCompanyID(parent.getCompanyID());
                if (parent.getTypeID().equals(MUA_HANG_TRA_LAI)) {
                    generalLedger.setInvoiceDate(parent.getInvoiceDate());
                    generalLedger.setInvoiceSeries(parent.getInvoiceSeries());
                    generalLedger.setInvoiceNo(parent.getInvoiceNo());
                }
//            generalLedger.bankAccountDetailID(parent.geta());
                generalLedger.setBankAccount(parent.getBankAccount());
                generalLedger.setBankName(parent.getBankName());
                generalLedger.setDate(parent.getDate());
                generalLedger.setTypeLedger(parent.getTypeLedger());
                generalLedger.setReferenceID(parent.getRefID());
                generalLedger.setTypeID(parent.getTypeID());
                generalLedger.postedDate(parent.getPostedDate());
                generalLedger.noFBook(parent.getNoFBook());
                generalLedger.noMBook(parent.getNoMBook());
                generalLedger.account(detailDTO.getDebitAccount());
                generalLedger.accountCorresponding(detailDTO.getCreditAccount());
                generalLedger.currencyID(parent.getCurrencyID());
                generalLedger.exchangeRate(parent.getExchangeRate());
                generalLedger.debitAmount(detailDTO.getAmount() != null ? detailDTO.getAmount() : BigDecimal.ZERO);
                generalLedger.debitAmountOriginal(detailDTO.getAmountOriginal() != null ? detailDTO.getAmountOriginal() : BigDecimal.ZERO);
                generalLedger.creditAmount(BigDecimal.ZERO);
                generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                generalLedger.reason(parent.getReason());
                generalLedger.description(detailDTO.getDescription());
                generalLedger.accountingObjectID(detailDTO.getAccountingObjectID());
                if (accountingObject.isPresent()) {
                    generalLedger.accountingObjectName(accountingObject.get().getAccountingObjectName());
                    generalLedger.accountingObjectCode(accountingObject.get().getAccountingObjectCode());
                    generalLedger.contactName(accountingObject.get().getContactName());
                    generalLedger.accountingObjectAddress(accountingObject.get().getAccountingObjectAddress());
                }
                // repository
                generalLedger.setRepositoryID(detailDTO.getRepositoryID());
                if (repository.isPresent()) {
                    generalLedger.setRepositoryCode(repository.get().getRepositoryCode());
                    generalLedger.setRepositoryName(repository.get().getRepositoryName());
                }
                generalLedger.employeeID(parent.getEmployeeID());
                if (empl != null) {
                    generalLedger.employeeCode(empl.getAccountingObjectCode());
                    generalLedger.employeeName(empl.getAccountingObjectName());
                }
                generalLedger.setMaterialGoodsID(detailDTO.getMaterialGoodsID());
                if (materialGoods.isPresent()) {
                    generalLedger.setMaterialGoodsCode(materialGoods.get().getMaterialGoodsCode());
                    generalLedger.setMaterialGoodsName(materialGoods.get().getMaterialGoodsName());
                }
                generalLedger.budgetItemID(detailDTO.getBudgetItemID());
                generalLedger.costSetID(detailDTO.getCostSetID());
                generalLedger.contractID(detailDTO.getContractID());
                generalLedger.statisticsCodeID(detailDTO.getStatisticsCodeID());
                generalLedger.detailID(detailDTO.getId());
                generalLedger.refDateTime(parent.getDate());
                generalLedger.expenseItemID(detailDTO.getExpenseItemID());
                generalLedger.setQuantity(detailDTO.getQuantity());
                generalLedger.setUnitID(detailDTO.getUnitID());
                generalLedger.setUnitPriceOriginal(detailDTO.getUnitPriceOriginal());
                generalLedger.setUnitPrice(detailDTO.getUnitPrice());
                generalLedger.mainUnitID(detailDTO.getMainUnitID());
                generalLedger.mainQuantity(detailDTO.getMainQuantity());
                generalLedger.mainUnitPrice(detailDTO.getMainUnitPrice());
                generalLedger.mainConvertRate(detailDTO.getMainConvertRate());
                generalLedger.formula(detailDTO.getFormula());
                generalLedger.departmentID(detailDTO.getDepartmentID());
                generalLedger.orderPriority(detailDTO.getOrderPriority());
                generalLedgers.add(generalLedger);

                GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                generalLedgerCorresponding.setCompanyID(parent.getCompanyID());
                if (parent.getTypeID().equals(MUA_HANG_TRA_LAI)) {
                    generalLedgerCorresponding.setInvoiceDate(parent.getInvoiceDate());
                    generalLedgerCorresponding.setInvoiceSeries(parent.getInvoiceSeries());
                    generalLedgerCorresponding.setInvoiceNo(parent.getInvoiceNo());
                }
                generalLedgerCorresponding.setBankAccount(parent.getBankAccount());
                generalLedgerCorresponding.setBankName(parent.getBankName());
                generalLedgerCorresponding.setDate(parent.getDate());
                generalLedgerCorresponding.setTypeLedger(parent.getTypeLedger());
                generalLedgerCorresponding.setReferenceID(parent.getRefID());
                generalLedgerCorresponding.setTypeID(parent.getTypeID());
                generalLedgerCorresponding.postedDate(parent.getPostedDate());
                generalLedgerCorresponding.noFBook(parent.getNoFBook());
                generalLedgerCorresponding.noMBook(parent.getNoMBook());
                generalLedgerCorresponding.account(detailDTO.getCreditAccount());
                generalLedgerCorresponding.accountCorresponding(detailDTO.getDebitAccount());
//            generalLedgerCorresponding.bankAccountDetailID(detailDTO.getBankAccountDetails() == null ? null : detailDTO.getBankAccountDetails().getId());
                generalLedgerCorresponding.currencyID(parent.getCurrencyID());
                generalLedgerCorresponding.exchangeRate(parent.getExchangeRate());
                generalLedgerCorresponding.creditAmount(detailDTO.getAmount() != null ? detailDTO.getAmount() : BigDecimal.ZERO);
                generalLedgerCorresponding.creditAmountOriginal(detailDTO.getAmountOriginal() != null ? detailDTO.getAmountOriginal() : BigDecimal.ZERO);
                generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);
                generalLedgerCorresponding.reason(parent.getReason());
                generalLedgerCorresponding.description(detailDTO.getDescription());
                generalLedgerCorresponding.accountingObjectID(detailDTO.getAccountingObjectID());
                generalLedgerCorresponding.employeeID(parent.getEmployeeID());
                if (empl != null) {
                    generalLedgerCorresponding.employeeCode(empl.getAccountingObjectCode());
                    generalLedgerCorresponding.employeeName(empl.getAccountingObjectName());
                }
                generalLedgerCorresponding.setRepositoryID(detailDTO.getRepositoryID());
                if (repository.isPresent()) {
                    generalLedgerCorresponding.setRepositoryCode(repository.get().getRepositoryCode());
                    generalLedgerCorresponding.setRepositoryName(repository.get().getRepositoryName());
                }
                generalLedgerCorresponding.setMaterialGoodsID(detailDTO.getMaterialGoodsID());
                if (materialGoods.isPresent()) {
                    generalLedgerCorresponding.setMaterialGoodsCode(materialGoods.get().getMaterialGoodsCode());
                    generalLedgerCorresponding.setMaterialGoodsName(materialGoods.get().getMaterialGoodsName());
                }
                generalLedgerCorresponding.mainUnitID(detailDTO.getMainUnitID());
                generalLedgerCorresponding.mainQuantity(detailDTO.getMainQuantity());
                generalLedgerCorresponding.mainUnitPrice(detailDTO.getMainUnitPrice());
                generalLedgerCorresponding.mainConvertRate(detailDTO.getMainConvertRate());
                generalLedgerCorresponding.formula(detailDTO.getFormula());
                generalLedgerCorresponding.departmentID(detailDTO.getDepartmentID());
                generalLedgerCorresponding.orderPriority(detailDTO.getOrderPriority());
                generalLedgerCorresponding.setUnitPriceOriginal(detailDTO.getUnitPriceOriginal());
                generalLedgerCorresponding.setUnitPrice(detailDTO.getUnitPrice());
                generalLedgerCorresponding.setQuantity(detailDTO.getQuantity());
                generalLedgerCorresponding.setUnitID(detailDTO.getUnitID());
                generalLedgerCorresponding.budgetItemID(detailDTO.getBudgetItemID());
                generalLedgerCorresponding.costSetID(detailDTO.getCostSetID());
                generalLedgerCorresponding.contractID(detailDTO.getContractID());
                generalLedgerCorresponding.statisticsCodeID(detailDTO.getStatisticsCodeID());
//            generalLedgerCorresponding.contactName(parent.getPayers());
                generalLedgerCorresponding.detailID(detailDTO.getId());
                generalLedgerCorresponding.refDateTime(parent.getDate());
//            generalLedgerCorresponding.departmentID(detailDTO.getOrganizationUnit() == null ? null : detailDTO.getOrganizationUnit().getId());
                generalLedgerCorresponding.expenseItemID(detailDTO.getExpenseItemID());
                generalLedgerCorresponding.accountingObjectID(detailDTO.getAccountingObjectID());
                if (accountingObject.isPresent()) {
                    generalLedgerCorresponding.accountingObjectName(accountingObject.get().getAccountingObjectName());
                    generalLedgerCorresponding.accountingObjectCode(accountingObject.get().getAccountingObjectCode());
                    generalLedgerCorresponding.accountingObjectAddress(accountingObject.get().getAccountingObjectAddress());
                    generalLedgerCorresponding.contactName(accountingObject.get().getContactName());
                }
                generalLedgers.add(generalLedgerCorresponding);
                // lưu gl thuế gtgt
                if (detailDTO.getvATAccount() != null && detailDTO.getDeductionDebitAccount() != null && detailDTO.getvATAmountOriginal() != null && !BigDecimal.ZERO.equals(detailDTO.getvATAmountOriginal())) {
                    GeneralLedger generalLedgerCorrespondingVAT = new GeneralLedger();
                    generalLedgerCorrespondingVAT.setCompanyID(parent.getCompanyID());
                    if (parent.getTypeID().equals(MUA_HANG_TRA_LAI)) {
                        generalLedgerCorrespondingVAT.setInvoiceDate(parent.getInvoiceDate());
                        generalLedgerCorrespondingVAT.setInvoiceSeries(parent.getInvoiceSeries());
                        generalLedgerCorrespondingVAT.setInvoiceNo(parent.getInvoiceNo());
                    }
                    generalLedgerCorrespondingVAT.setBankAccount(parent.getBankAccount());
                    generalLedgerCorrespondingVAT.setBankName(parent.getBankName());
                    generalLedgerCorrespondingVAT.setDate(parent.getDate());
                    generalLedgerCorrespondingVAT.setTypeLedger(parent.getTypeLedger());
                    generalLedgerCorrespondingVAT.setReferenceID(parent.getRefID());
                    generalLedgerCorrespondingVAT.setTypeID(parent.getTypeID());
                    generalLedgerCorrespondingVAT.postedDate(parent.getPostedDate());
                    generalLedgerCorrespondingVAT.noFBook(parent.getNoFBook());
                    generalLedgerCorrespondingVAT.noMBook(parent.getNoMBook());
                    generalLedgerCorrespondingVAT.account(detailDTO.getDeductionDebitAccount());
                    generalLedgerCorrespondingVAT.accountCorresponding(detailDTO.getvATAccount());
//            generalLedgerCorrespondingVAT.bankAccountDetailID(detailDTO.getBankAccountDetails() == null ? null : detailDTO.getBankAccountDetails().getId());
                    generalLedgerCorrespondingVAT.currencyID(parent.getCurrencyID());
                    generalLedgerCorrespondingVAT.exchangeRate(parent.getExchangeRate());
                    generalLedgerCorrespondingVAT.creditAmount(BigDecimal.ZERO);
                    generalLedgerCorrespondingVAT.creditAmountOriginal(BigDecimal.ZERO);
                    generalLedgerCorrespondingVAT.debitAmount(detailDTO.getvATAmount() != null ? detailDTO.getvATAmount() : BigDecimal.ZERO);
                    generalLedgerCorrespondingVAT.debitAmountOriginal(detailDTO.getvATAmountOriginal() != null ? detailDTO.getvATAmountOriginal() : BigDecimal.ZERO);
                    generalLedgerCorrespondingVAT.reason(parent.getReason());
                    generalLedgerCorrespondingVAT.description(detailDTO.getVatDescription());
                    generalLedgerCorrespondingVAT.accountingObjectID(detailDTO.getAccountingObjectID());
                    generalLedgerCorrespondingVAT.employeeID(parent.getEmployeeID());
                    if (empl != null) {
                        generalLedgerCorrespondingVAT.employeeCode(empl.getAccountingObjectCode());
                        generalLedgerCorrespondingVAT.employeeName(empl.getAccountingObjectName());
                    }
                    generalLedgerCorrespondingVAT.setRepositoryID(detailDTO.getRepositoryID());
                    if (repository.isPresent()) {
                        generalLedgerCorrespondingVAT.setRepositoryCode(repository.get().getRepositoryCode());
                        generalLedgerCorrespondingVAT.setRepositoryName(repository.get().getRepositoryName());
                    }
                    generalLedgerCorrespondingVAT.setMaterialGoodsID(detailDTO.getMaterialGoodsID());
                    if (materialGoods.isPresent()) {
                        generalLedgerCorrespondingVAT.setMaterialGoodsCode(materialGoods.get().getMaterialGoodsCode());
                        generalLedgerCorrespondingVAT.setMaterialGoodsName(materialGoods.get().getMaterialGoodsName());
                    }
                    generalLedgerCorrespondingVAT.mainUnitID(detailDTO.getMainUnitID());
                    generalLedgerCorrespondingVAT.mainQuantity(detailDTO.getMainQuantity());
                    generalLedgerCorrespondingVAT.mainUnitPrice(detailDTO.getMainUnitPrice());
                    generalLedgerCorrespondingVAT.mainConvertRate(detailDTO.getMainConvertRate());
                    generalLedgerCorrespondingVAT.formula(detailDTO.getFormula());
                    generalLedgerCorrespondingVAT.departmentID(detailDTO.getDepartmentID());
                    generalLedgerCorrespondingVAT.orderPriority(detailDTO.getOrderPriority());
                    generalLedgerCorrespondingVAT.setUnitPriceOriginal(detailDTO.getUnitPriceOriginal());
                    generalLedgerCorrespondingVAT.setUnitPrice(detailDTO.getUnitPrice());
                    generalLedgerCorrespondingVAT.setQuantity(detailDTO.getQuantity());
                    generalLedgerCorrespondingVAT.setUnitID(detailDTO.getUnitID());
                    generalLedgerCorrespondingVAT.budgetItemID(detailDTO.getBudgetItemID());
                    generalLedgerCorrespondingVAT.costSetID(detailDTO.getCostSetID());
                    generalLedgerCorrespondingVAT.contractID(detailDTO.getContractID());
                    generalLedgerCorrespondingVAT.statisticsCodeID(detailDTO.getStatisticsCodeID());
//            generalLedgerCorrespondingVAT.contactName(parent.getPayers());
                    generalLedgerCorrespondingVAT.detailID(detailDTO.getId());
                    generalLedgerCorrespondingVAT.refDateTime(parent.getDate());
//            generalLedgerCorrespondingVAT.departmentID(detailDTO.getOrganizationUnit() == null ? null : detailDTO.getOrganizationUnit().getId());
                    generalLedgerCorrespondingVAT.expenseItemID(detailDTO.getExpenseItemID());
                    generalLedgerCorrespondingVAT.accountingObjectID(detailDTO.getAccountingObjectID());
                    if (accountingObject.isPresent()) {
                        generalLedgerCorrespondingVAT.accountingObjectName(accountingObject.get().getAccountingObjectName());
                        generalLedgerCorrespondingVAT.accountingObjectCode(accountingObject.get().getAccountingObjectCode());
                        generalLedgerCorrespondingVAT.accountingObjectAddress(accountingObject.get().getAccountingObjectAddress());
                        generalLedgerCorrespondingVAT.contactName(accountingObject.get().getContactName());
                    }
                    generalLedgers.add(generalLedgerCorrespondingVAT);
                    GeneralLedger generalLedgerVAT = new GeneralLedger();
                    generalLedgerVAT.setCompanyID(parent.getCompanyID());
                    if (parent.getTypeID().equals(MUA_HANG_TRA_LAI)) {
                        generalLedgerVAT.setInvoiceDate(parent.getInvoiceDate());
                        generalLedgerVAT.setInvoiceSeries(parent.getInvoiceSeries());
                        generalLedgerVAT.setInvoiceNo(parent.getInvoiceNo());
                    }
                    generalLedgerVAT.setBankAccount(parent.getBankAccount());
                    generalLedgerVAT.setBankName(parent.getBankName());
                    generalLedgerVAT.setDate(parent.getDate());
                    generalLedgerVAT.setTypeLedger(parent.getTypeLedger());
                    generalLedgerVAT.setReferenceID(parent.getRefID());
                    generalLedgerVAT.setTypeID(parent.getTypeID());
                    generalLedgerVAT.postedDate(parent.getPostedDate());
                    generalLedgerVAT.noFBook(parent.getNoFBook());
                    generalLedgerVAT.noMBook(parent.getNoMBook());
                    generalLedgerVAT.account(detailDTO.getvATAccount());
                    generalLedgerVAT.accountCorresponding(detailDTO.getDeductionDebitAccount());
//            generalLedgerVAT.bankAccountDetailID(detailDTO.getBankAccountDetails() == null ? null : detailDTO.getBankAccountDetails().getId());
                    generalLedgerVAT.currencyID(parent.getCurrencyID());
                    generalLedgerVAT.exchangeRate(parent.getExchangeRate());
                    generalLedgerVAT.creditAmount(detailDTO.getvATAmount() != null ? detailDTO.getvATAmount() : BigDecimal.ZERO);
                    generalLedgerVAT.creditAmountOriginal(detailDTO.getvATAmountOriginal() != null ? detailDTO.getvATAmountOriginal() : BigDecimal.ZERO);
                    generalLedgerVAT.debitAmount(BigDecimal.ZERO);
                    generalLedgerVAT.debitAmountOriginal(BigDecimal.ZERO);
                    generalLedgerVAT.reason(parent.getReason());
                    generalLedgerVAT.description(detailDTO.getVatDescription());
                    generalLedgerVAT.accountingObjectID(detailDTO.getAccountingObjectID());
                    generalLedgerVAT.employeeID(parent.getEmployeeID());
                    if (empl != null) {
                        generalLedgerVAT.employeeCode(empl.getAccountingObjectCode());
                        generalLedgerVAT.employeeName(empl.getAccountingObjectName());
                    }
                    generalLedgerVAT.setRepositoryID(detailDTO.getRepositoryID());
                    if (repository.isPresent()) {
                        generalLedgerVAT.setRepositoryCode(repository.get().getRepositoryCode());
                        generalLedgerVAT.setRepositoryName(repository.get().getRepositoryName());
                    }
                    generalLedgerVAT.setMaterialGoodsID(detailDTO.getMaterialGoodsID());
                    if (materialGoods.isPresent()) {
                        generalLedgerVAT.setMaterialGoodsCode(materialGoods.get().getMaterialGoodsCode());
                        generalLedgerVAT.setMaterialGoodsName(materialGoods.get().getMaterialGoodsName());
                    }
                    generalLedgerVAT.mainUnitID(detailDTO.getMainUnitID());
                    generalLedgerVAT.mainQuantity(detailDTO.getMainQuantity());
                    generalLedgerVAT.mainUnitPrice(detailDTO.getMainUnitPrice());
                    generalLedgerVAT.mainConvertRate(detailDTO.getMainConvertRate());
                    generalLedgerVAT.formula(detailDTO.getFormula());
                    generalLedgerVAT.departmentID(detailDTO.getDepartmentID());
                    generalLedgerVAT.orderPriority(detailDTO.getOrderPriority());
                    generalLedgerVAT.setUnitPriceOriginal(detailDTO.getUnitPriceOriginal());
                    generalLedgerVAT.setUnitPrice(detailDTO.getUnitPrice());
                    generalLedgerVAT.setQuantity(detailDTO.getQuantity());
                    generalLedgerVAT.setUnitID(detailDTO.getUnitID());
                    generalLedgerVAT.budgetItemID(detailDTO.getBudgetItemID());
                    generalLedgerVAT.costSetID(detailDTO.getCostSetID());
                    generalLedgerVAT.contractID(detailDTO.getContractID());
                    generalLedgerVAT.statisticsCodeID(detailDTO.getStatisticsCodeID());
//            generalLedgerVAT.contactName(parent.getPayers());
                    generalLedgerVAT.detailID(detailDTO.getId());
                    generalLedgerVAT.refDateTime(parent.getDate());
//            generalLedgerVAT.departmentID(detailDTO.getOrganizationUnit() == null ? null : detailDTO.getOrganizationUnit().getId());
                    generalLedgerVAT.expenseItemID(detailDTO.getExpenseItemID());
                    generalLedgerVAT.accountingObjectID(detailDTO.getAccountingObjectID());
                    if (accountingObject.isPresent()) {
                        generalLedgerVAT.accountingObjectName(accountingObject.get().getAccountingObjectName());
                        generalLedgerVAT.accountingObjectCode(accountingObject.get().getAccountingObjectCode());
                        generalLedgerVAT.accountingObjectAddress(accountingObject.get().getAccountingObjectAddress());
                        generalLedgerVAT.contactName(accountingObject.get().getContactName());
                    }
                    generalLedgers.add(generalLedgerVAT);
                }
            }
        }
        return generalLedgers;
    }

    /*Phiếu chi*/
    List<GeneralLedger> genGeneralLedgersMCPayment(List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail) {
        List<GeneralLedger> generalLedgers = new ArrayList<>();
        for (ViewVoucherNo parent : viewVoucherNos) {
            for (ViewVoucherNoDetailDTO detailDTO : parent.getViewVoucherNoDetailDTOS()) {
                GeneralLedger generalLedger = new GeneralLedger();
                generalLedger.setCompanyID(parent.getCompanyID());
                generalLedger.setReferenceID(parent.getRefID());
                generalLedger.setDetailID(detailDTO.getRefID());
                generalLedger.setTypeID(parent.getTypeID());
                generalLedger.date(parent.getDate());
                generalLedger.postedDate(parent.getPostedDate());
                generalLedger.noFBook(parent.getNoFBook());
                generalLedger.noMBook(parent.getNoMBook());
                generalLedger.typeLedger(parent.getTypeLedger());
                generalLedger.accountingObjectID(detailDTO.getAccountingObjectID());
                generalLedger.accountingObjectCode(detailDTO.getAccountingObjectCode());
                generalLedger.accountingObjectName(detailDTO.getAccountingObjectName());
                generalLedger.accountingObjectAddress(detailDTO.getAccountingObjectAddress());
                generalLedger.account(detailDTO.getDebitAccount());
                generalLedger.accountCorresponding(detailDTO.getCreditAccount());
                generalLedger.bankAccountDetailID(detailDTO.getBankAccountDetailID());
                generalLedger.bankAccount(detailDTO.getBankAccount());
                generalLedger.bankName(detailDTO.getBankName());
                generalLedger.currencyID(parent.getCurrencyID());
                generalLedger.exchangeRate(parent.getExchangeRate());
                generalLedger.debitAmount(detailDTO.getAmount());
                generalLedger.debitAmountOriginal(detailDTO.getAmountOriginal());
                generalLedger.creditAmount(BigDecimal.ZERO);
                generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                generalLedger.reason(parent.getReason());
                generalLedger.description(detailDTO.getDescription());
                generalLedger.employeeID(parent.getEmployeeID());
                generalLedger.employeeCode(parent.getEmployeeCode());
                generalLedger.employeeName(parent.getEmployeeName());
                generalLedger.budgetItemID(detailDTO.getBudgetItemID());
                generalLedger.costSetID(detailDTO.getCostSetID());
                generalLedger.contractID(detailDTO.getContractID());
                generalLedger.statisticsCodeID(detailDTO.getStatisticsCodeID());
                generalLedger.contactName(parent.getContactName());
                generalLedger.detailID(detailDTO.getRefID());
                generalLedger.refDateTime(parent.getDate());
                generalLedger.departmentID(detailDTO.getDepartmentID());
                generalLedger.expenseItemID(detailDTO.getExpenseItemID());
                generalLedgers.add(generalLedger);

                GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                generalLedgerCorresponding.setCompanyID(parent.getCompanyID());
                generalLedgerCorresponding.setReferenceID(parent.getRefID());
                generalLedgerCorresponding.setDetailID(detailDTO.getRefID());
                generalLedgerCorresponding.setTypeID(parent.getTypeID());
                generalLedgerCorresponding.date(parent.getDate());
                generalLedgerCorresponding.postedDate(parent.getPostedDate());
                generalLedgerCorresponding.noFBook(parent.getNoFBook());
                generalLedgerCorresponding.noMBook(parent.getNoMBook());
                generalLedgerCorresponding.typeLedger(parent.getTypeLedger());
                generalLedgerCorresponding.accountingObjectID(detailDTO.getAccountingObjectID());
                generalLedgerCorresponding.accountingObjectCode(detailDTO.getAccountingObjectCode());
                generalLedgerCorresponding.accountingObjectName(detailDTO.getAccountingObjectName());
                generalLedgerCorresponding.accountingObjectAddress(detailDTO.getAccountingObjectAddress());
                generalLedgerCorresponding.account(detailDTO.getCreditAccount());
                generalLedgerCorresponding.accountCorresponding(detailDTO.getDebitAccount());
                generalLedgerCorresponding.bankAccountDetailID(detailDTO.getBankAccountDetailID());
                generalLedgerCorresponding.bankAccount(detailDTO.getBankAccount());
                generalLedgerCorresponding.bankName(detailDTO.getBankName());
                generalLedgerCorresponding.currencyID(parent.getCurrencyID());
                generalLedgerCorresponding.exchangeRate(parent.getExchangeRate());
                generalLedgerCorresponding.creditAmount(detailDTO.getAmount());
                generalLedgerCorresponding.creditAmountOriginal(detailDTO.getAmountOriginal());
                generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);
                generalLedgerCorresponding.reason(parent.getReason());
                generalLedgerCorresponding.description(detailDTO.getDescription());
                generalLedgerCorresponding.employeeID(parent.getEmployeeID());
                generalLedgerCorresponding.employeeCode(parent.getEmployeeCode());
                generalLedgerCorresponding.employeeName(parent.getEmployeeName());
                generalLedgerCorresponding.budgetItemID(detailDTO.getBudgetItemID());
                generalLedgerCorresponding.costSetID(detailDTO.getCostSetID());
                generalLedgerCorresponding.contractID(detailDTO.getContractID());
                generalLedgerCorresponding.statisticsCodeID(detailDTO.getStatisticsCodeID());
                generalLedgerCorresponding.contactName(parent.getContactName());
                generalLedgerCorresponding.detailID(detailDTO.getRefID());
                generalLedgerCorresponding.refDateTime(parent.getDate());
                generalLedgerCorresponding.departmentID(detailDTO.getDepartmentID());
                generalLedgerCorresponding.expenseItemID(detailDTO.getExpenseItemID());
                generalLedgers.add(generalLedgerCorresponding);
            }
        }
        return generalLedgers;
    }

    /*Báo nợ*/
    List<GeneralLedger> genGeneralLedgersMBTellerPaper(List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail
        , List<ViewGLPayExceedCashDTO> viewGLPayExceedCashAll) {
        List<GeneralLedger> generalLedgers = new ArrayList<>();
        List<ViewVoucherNo> listCheck = new ArrayList<>();
        listCheck.addAll(viewVoucherNos);
        for (ViewVoucherNo parent : listCheck) {
            boolean checkContinue = true;
            if (viewGLPayExceedCashAll.size() > 0) {
                List<CheckQuantityExistsConvertDTO> details = new ArrayList<>();
                for (ViewVoucherNoDetailDTO sad : parent.getViewVoucherNoDetailDTOS()) {
                    for (ViewGLPayExceedCashDTO item : viewGLPayExceedCashAll) {
                        if ((parent.getTypeLedger().compareTo(Constants.TypeLedger.BOTH_BOOK) == 0 || parent.getTypeLedger().compareTo(item.getTypeLedger()) == 0) &&
                            sad.getCreditAccount().equals(item.getAccount()) && item.getDebitAmount().subtract(item.getCreditAmount()).subtract(sad.getAmount()).floatValue() < 0) {
                            parent.setReasonFail("Xuất quá tồn quỹ!");
                            lstFail.remove(parent);
                            lstFail.add(parent);
                            viewVoucherNos.remove(parent);
                            checkContinue = false;
                        } else {
                            item.setDebitAmount(item.getDebitAmount().subtract(sad.getAmount()));
                        }
                    }
                }
            }
            if (checkContinue) {
                for (ViewVoucherNoDetailDTO detailDTO : parent.getViewVoucherNoDetailDTOS()) {
                    GeneralLedger generalLedger = new GeneralLedger();
                    generalLedger.setCompanyID(parent.getCompanyID());
                    generalLedger.setReferenceID(parent.getRefID());
                    generalLedger.setDetailID(detailDTO.getRefID());
                    generalLedger.setTypeID(parent.getTypeID());
                    generalLedger.date(parent.getDate());
                    generalLedger.postedDate(parent.getPostedDate());
                    generalLedger.noFBook(parent.getNoFBook());
                    generalLedger.noMBook(parent.getNoMBook());
                    generalLedger.typeLedger(parent.getTypeLedger());
                    generalLedger.accountingObjectID(detailDTO.getAccountingObjectID());
                    generalLedger.accountingObjectCode(detailDTO.getAccountingObjectCode());
                    generalLedger.accountingObjectName(detailDTO.getAccountingObjectName());
                    generalLedger.accountingObjectAddress(detailDTO.getAccountingObjectAddress());
                    generalLedger.account(detailDTO.getDebitAccount());
                    generalLedger.accountCorresponding(detailDTO.getCreditAccount());
                    generalLedger.bankAccountDetailID(parent.getBankAccountDetailID());
                    generalLedger.bankAccount(parent.getBankAccount());
                    generalLedger.bankName(parent.getBankName());
                    generalLedger.currencyID(parent.getCurrencyID());
                    generalLedger.exchangeRate(parent.getExchangeRate());
                    generalLedger.debitAmount(detailDTO.getAmount());
                    generalLedger.debitAmountOriginal(detailDTO.getAmountOriginal());
                    generalLedger.creditAmount(BigDecimal.ZERO);
                    generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                    generalLedger.reason(parent.getReason());
                    generalLedger.description(detailDTO.getDescription());
                    generalLedger.employeeID(parent.getEmployeeID());
                    generalLedger.employeeCode(parent.getEmployeeCode());
                    generalLedger.employeeName(parent.getEmployeeName());
                    generalLedger.budgetItemID(detailDTO.getBudgetItemID());
                    generalLedger.costSetID(detailDTO.getCostSetID());
                    generalLedger.contractID(detailDTO.getContractID());
                    generalLedger.statisticsCodeID(detailDTO.getStatisticsCodeID());
                    generalLedger.contactName(parent.getContactName());
//                generalLedger.detailID(detailDTO.getRefID());
                    generalLedger.refDateTime(parent.getDate());
                    generalLedger.departmentID(detailDTO.getDepartmentID());
                    generalLedger.expenseItemID(detailDTO.getExpenseItemID());
                    generalLedgers.add(generalLedger);

                    GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                    generalLedgerCorresponding.setCompanyID(parent.getCompanyID());
                    generalLedgerCorresponding.setReferenceID(parent.getRefID());
                    generalLedgerCorresponding.setDetailID(detailDTO.getRefID());
                    generalLedgerCorresponding.setTypeID(parent.getTypeID());
                    generalLedgerCorresponding.date(parent.getDate());
                    generalLedgerCorresponding.postedDate(parent.getPostedDate());
                    generalLedgerCorresponding.noFBook(parent.getNoFBook());
                    generalLedgerCorresponding.noMBook(parent.getNoMBook());
                    generalLedgerCorresponding.typeLedger(parent.getTypeLedger());
                    generalLedgerCorresponding.accountingObjectID(detailDTO.getAccountingObjectID());
                    generalLedgerCorresponding.accountingObjectCode(detailDTO.getAccountingObjectCode());
                    generalLedgerCorresponding.accountingObjectName(detailDTO.getAccountingObjectName());
                    generalLedgerCorresponding.accountingObjectAddress(detailDTO.getAccountingObjectAddress());
                    generalLedgerCorresponding.account(detailDTO.getCreditAccount());
                    generalLedgerCorresponding.accountCorresponding(detailDTO.getDebitAccount());
                    generalLedgerCorresponding.bankAccountDetailID(parent.getBankAccountDetailID());
                    generalLedgerCorresponding.bankAccount(parent.getBankAccount());
                    generalLedgerCorresponding.bankName(parent.getBankName());
                    generalLedgerCorresponding.currencyID(parent.getCurrencyID());
                    generalLedgerCorresponding.exchangeRate(parent.getExchangeRate());
                    generalLedgerCorresponding.creditAmount(detailDTO.getAmount());
                    generalLedgerCorresponding.creditAmountOriginal(detailDTO.getAmountOriginal());
                    generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                    generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);
                    generalLedgerCorresponding.reason(parent.getReason());
                    generalLedgerCorresponding.description(detailDTO.getDescription());
                    generalLedgerCorresponding.employeeID(parent.getEmployeeID());
                    generalLedgerCorresponding.employeeCode(parent.getEmployeeCode());
                    generalLedgerCorresponding.employeeName(parent.getEmployeeName());
                    generalLedgerCorresponding.budgetItemID(detailDTO.getBudgetItemID());
                    generalLedgerCorresponding.costSetID(detailDTO.getCostSetID());
                    generalLedgerCorresponding.contractID(detailDTO.getContractID());
                    generalLedgerCorresponding.statisticsCodeID(detailDTO.getStatisticsCodeID());
                    generalLedgerCorresponding.contactName(parent.getContactName());
//                generalLedgerCorresponding.detailID(detailDTO.getRefID());
                    generalLedgerCorresponding.refDateTime(parent.getDate());
                    generalLedgerCorresponding.departmentID(detailDTO.getDepartmentID());
                    generalLedgerCorresponding.expenseItemID(detailDTO.getExpenseItemID());
                    generalLedgers.add(generalLedgerCorresponding);
                }
            }
        }
        return generalLedgers;
    }

    /*Báo có*/
    List<GeneralLedger> genGeneralLedgersMBDeposit(List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail) {
        List<GeneralLedger> generalLedgers = new ArrayList<>();
        for (ViewVoucherNo parent : viewVoucherNos) {
            for (ViewVoucherNoDetailDTO detailDTO : parent.getViewVoucherNoDetailDTOS()) {
                GeneralLedger generalLedger = new GeneralLedger();
                generalLedger.branchID(null);
                generalLedger.setReferenceID(parent.getRefID());
                generalLedger.setDetailID(detailDTO.getRefID());
                generalLedger.setCompanyID(parent.getCompanyID());
                generalLedger.setTypeID(parent.getTypeID());
                generalLedger.postedDate(parent.getPostedDate());
                generalLedger.date(parent.getDate());
                generalLedger.noFBook(parent.getNoFBook());
                generalLedger.noMBook(parent.getNoMBook());
                generalLedger.typeLedger(parent.getTypeLedger());
                generalLedger.account(detailDTO.getDebitAccount());
                generalLedger.accountCorresponding(detailDTO.getCreditAccount());
                generalLedger.currencyID(parent.getCurrencyID());
                generalLedger.exchangeRate(parent.getExchangeRate());
                generalLedger.contractID(detailDTO.getContractID());
                generalLedger.bankAccountDetailID(parent.getBankAccountDetailID());
                generalLedger.bankAccount(parent.getBankAccount());
                generalLedger.bankName(parent.getBankName());
                generalLedger.accountingObjectCode(detailDTO.getAccountingObjectCode());
                generalLedger.accountingObjectName(detailDTO.getAccountingObjectName());
                generalLedger.accountingObjectAddress(detailDTO.getAccountingObjectAddress());
                generalLedger.accountingObjectID(detailDTO.getAccountingObjectID());
                generalLedger.employeeCode(parent.getEmployeeCode());
                generalLedger.employeeName(parent.getEmployeeName());
                generalLedger.debitAmountOriginal(detailDTO.getAmountOriginal());
                generalLedger.debitAmount(detailDTO.getAmount());
                generalLedger.creditAmount(BigDecimal.ZERO);
                generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                generalLedger.reason(parent.getReason());
                generalLedger.description(detailDTO.getDescription());
                generalLedger.employeeID(parent.getEmployeeID());
                generalLedger.budgetItemID(detailDTO.getBudgetItemID());
                generalLedger.costSetID(detailDTO.getCostSetID());
                generalLedger.statisticsCodeID(detailDTO.getStatisticsCodeID());
                generalLedger.refDateTime(parent.getPostedDate());
                generalLedger.departmentID(detailDTO.getDepartmentID());
                generalLedger.expenseItemID(detailDTO.getExpenseItemID());
                generalLedgers.add(generalLedger);

                GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                generalLedgerCorresponding.branchID(null);
                generalLedgerCorresponding.setReferenceID(parent.getRefID());
                generalLedgerCorresponding.setDetailID(detailDTO.getRefID());
                generalLedgerCorresponding.setCompanyID(parent.getCompanyID());
                generalLedgerCorresponding.setTypeID(parent.getTypeID());
                generalLedgerCorresponding.postedDate(parent.getPostedDate());
                generalLedgerCorresponding.date(parent.getDate());
                generalLedgerCorresponding.noFBook(parent.getNoFBook());
                generalLedgerCorresponding.noMBook(parent.getNoMBook());
                generalLedgerCorresponding.typeLedger(parent.getTypeLedger());
                generalLedgerCorresponding.account(detailDTO.getCreditAccount());
                generalLedgerCorresponding.accountCorresponding(detailDTO.getDebitAccount());
                generalLedgerCorresponding.currencyID(parent.getCurrencyID());
                generalLedgerCorresponding.contractID(detailDTO.getContractID());
                generalLedgerCorresponding.bankAccountDetailID(parent.getBankAccountDetailID());
                generalLedgerCorresponding.bankAccount(parent.getBankAccount());
                generalLedgerCorresponding.bankName(parent.getBankName());
                generalLedgerCorresponding.accountingObjectCode(detailDTO.getAccountingObjectCode());
                generalLedgerCorresponding.accountingObjectName(detailDTO.getAccountingObjectName());
                generalLedgerCorresponding.accountingObjectAddress(detailDTO.getAccountingObjectAddress());
                generalLedgerCorresponding.accountingObjectID(detailDTO.getAccountingObjectID());
                generalLedgerCorresponding.employeeCode(parent.getEmployeeCode());
                generalLedgerCorresponding.employeeName(parent.getEmployeeName());
                generalLedgerCorresponding.exchangeRate(parent.getExchangeRate());
                generalLedgerCorresponding.creditAmount(detailDTO.getAmount());
                generalLedgerCorresponding.creditAmountOriginal(detailDTO.getAmountOriginal());
                generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);
                generalLedgerCorresponding.reason(parent.getReason());
                generalLedgerCorresponding.description(detailDTO.getDescription());
                generalLedgerCorresponding.employeeID(parent.getEmployeeID());
                generalLedgerCorresponding.budgetItemID(detailDTO.getBudgetItemID());
                generalLedgerCorresponding.costSetID(detailDTO.getCostSetID());
                generalLedgerCorresponding.statisticsCodeID(detailDTO.getStatisticsCodeID());
                generalLedgerCorresponding.refDateTime(parent.getPostedDate());
                generalLedgerCorresponding.departmentID(detailDTO.getDepartmentID());
                generalLedgerCorresponding.expenseItemID(detailDTO.getExpenseItemID());
                generalLedgers.add(generalLedgerCorresponding);
            }
        }
        return generalLedgers;
    }

    /*Thẻ tín dụng*/
    List<GeneralLedger> genGeneralLedgersMBCreditCard(List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail
        , List<ViewGLPayExceedCashDTO> viewGLPayExceedCashAll) {
        List<GeneralLedger> generalLedgers = new ArrayList<>();
        List<ViewVoucherNo> listCheck = new ArrayList<>();
        listCheck.addAll(viewVoucherNos);
        for (ViewVoucherNo parent : listCheck) {
            boolean checkContinue = true;
            if (viewGLPayExceedCashAll.size() > 0) {
                List<CheckQuantityExistsConvertDTO> details = new ArrayList<>();
                for (ViewVoucherNoDetailDTO sad : parent.getViewVoucherNoDetailDTOS()) {
                    for (ViewGLPayExceedCashDTO item : viewGLPayExceedCashAll) {
                        if ((parent.getTypeLedger().compareTo(Constants.TypeLedger.BOTH_BOOK) == 0 || parent.getTypeLedger().compareTo(item.getTypeLedger()) == 0) &&
                            sad.getCreditAccount().equals(item.getAccount()) && item.getDebitAmount().subtract(item.getCreditAmount()).subtract(sad.getAmount()).floatValue() < 0) {
                            parent.setReasonFail("Xuất quá tồn quỹ!");
                            lstFail.remove(parent);
                            lstFail.add(parent);
                            viewVoucherNos.remove(parent);
                            checkContinue = false;
                        } else {
                            item.setDebitAmount(item.getDebitAmount().subtract(sad.getAmount()));
                        }
                    }
                }
            }
            if (checkContinue) {
                for (ViewVoucherNoDetailDTO detailDTO : parent.getViewVoucherNoDetailDTOS()) {
                    GeneralLedger generalLedger = new GeneralLedger();
                    generalLedger.branchID(null);
                    generalLedger.setReferenceID(parent.getRefID());
                    generalLedger.setDetailID(detailDTO.getRefID());
                    generalLedger.setCompanyID(parent.getCompanyID());
                    generalLedger.setTypeID(parent.getTypeID());
                    generalLedger.postedDate(parent.getPostedDate());
                    generalLedger.noFBook(parent.getNoFBook());
                    generalLedger.noMBook(parent.getNoMBook());
                    generalLedger.date(parent.getDate());
                    generalLedger.typeLedger(parent.getTypeLedger());
                    generalLedger.account(detailDTO.getDebitAccount());
                    generalLedger.accountCorresponding(detailDTO.getCreditAccount());
                    generalLedger.currencyID(parent.getCurrencyID());
                    generalLedger.bankAccountDetailID(parent.getBankAccountDetailID());
                    generalLedger.bankAccount(parent.getBankAccount());
                    generalLedger.bankName(parent.getBankName());
                    generalLedger.contractID(detailDTO.getContractID());
                    generalLedger.accountingObjectCode(detailDTO.getAccountingObjectCode());
                    generalLedger.accountingObjectName(detailDTO.getAccountingObjectName());
                    generalLedger.accountingObjectAddress(detailDTO.getAccountingObjectAddress());
                    generalLedger.employeeCode(parent.getEmployeeCode());
                    generalLedger.employeeName(parent.getEmployeeName());
                    generalLedger.exchangeRate(parent.getExchangeRate());
                    generalLedger.debitAmount(detailDTO.getAmount());
                    generalLedger.debitAmountOriginal(detailDTO.getAmountOriginal());
                    generalLedger.creditAmount(BigDecimal.ZERO);
                    generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                    generalLedger.reason(parent.getReason());
                    generalLedger.description(detailDTO.getDescription());
                    generalLedger.accountingObjectID(detailDTO.getAccountingObjectID());
                    generalLedger.employeeID(parent.getEmployeeID());
                    generalLedger.budgetItemID(detailDTO.getBudgetItemID());
                    generalLedger.costSetID(detailDTO.getCostSetID());
                    generalLedger.statisticsCodeID(detailDTO.getStatisticsCodeID());
                    generalLedger.refDateTime(parent.getPostedDate());
                    generalLedger.departmentID(detailDTO.getDepartmentID());
                    generalLedger.expenseItemID(detailDTO.getExpenseItemID());
                    generalLedgers.add(generalLedger);

                    GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                    generalLedgerCorresponding.branchID(null);
                    generalLedgerCorresponding.date(parent.getDate());
                    generalLedgerCorresponding.typeLedger(parent.getTypeLedger());
                    generalLedgerCorresponding.setReferenceID(parent.getRefID());
                    generalLedgerCorresponding.setDetailID(detailDTO.getRefID());
                    generalLedgerCorresponding.setCompanyID(parent.getCompanyID());
                    generalLedgerCorresponding.setTypeID(parent.getTypeID());
                    generalLedgerCorresponding.postedDate(parent.getPostedDate());
                    generalLedgerCorresponding.noFBook(parent.getNoFBook());
                    generalLedgerCorresponding.noMBook(parent.getNoMBook());
                    generalLedgerCorresponding.account(detailDTO.getCreditAccount());
                    generalLedgerCorresponding.accountCorresponding(detailDTO.getDebitAccount());
                    generalLedgerCorresponding.currencyID(parent.getCurrencyID());
                    generalLedgerCorresponding.contractID(detailDTO.getContractID());
                    generalLedgerCorresponding.bankAccountDetailID(parent.getBankAccountDetailID());
                    generalLedgerCorresponding.bankAccount(parent.getBankAccount());
                    generalLedgerCorresponding.bankName(parent.getBankName());
                    generalLedgerCorresponding.bankAccountDetailID(parent.getBankAccountDetailID());
                    generalLedgerCorresponding.bankAccount(parent.getBankAccount());
                    generalLedgerCorresponding.bankName(parent.getBankName());
                    generalLedgerCorresponding.contractID(detailDTO.getContractID());
                    generalLedgerCorresponding.accountingObjectCode(detailDTO.getAccountingObjectCode());
                    generalLedgerCorresponding.accountingObjectName(detailDTO.getAccountingObjectName());
                    generalLedgerCorresponding.accountingObjectAddress(detailDTO.getAccountingObjectAddress());
                    generalLedgerCorresponding.employeeCode(parent.getEmployeeCode());
                    generalLedgerCorresponding.employeeName(parent.getEmployeeName());
                    generalLedgerCorresponding.exchangeRate(parent.getExchangeRate());
                    generalLedgerCorresponding.creditAmount(detailDTO.getAmount());
                    generalLedgerCorresponding.creditAmountOriginal(detailDTO.getAmountOriginal());
                    generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                    generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);
                    generalLedgerCorresponding.reason(parent.getReason());
                    generalLedgerCorresponding.description(detailDTO.getDescription());
                    generalLedgerCorresponding.accountingObjectID(detailDTO.getAccountingObjectID());
                    generalLedgerCorresponding.employeeID(parent.getEmployeeID());
                    generalLedgerCorresponding.budgetItemID(detailDTO.getBudgetItemID());
                    generalLedgerCorresponding.costSetID(detailDTO.getCostSetID());
                    generalLedgerCorresponding.statisticsCodeID(detailDTO.getStatisticsCodeID());
                    generalLedgerCorresponding.refDateTime(parent.getPostedDate());
                    generalLedgerCorresponding.departmentID(detailDTO.getDepartmentID());
                    generalLedgerCorresponding.expenseItemID(detailDTO.getExpenseItemID());
                    generalLedgers.add(generalLedgerCorresponding);
                }
            }
        }
        return generalLedgers;
    }


    /*Thẻ tín dụng*/
    List<GeneralLedger> genGeneralLedgersGOtherVoucher(List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail
        , List<ViewGLPayExceedCashDTO> viewGLPayExceedCashAll) {
        List<GeneralLedger> generalLedgers = new ArrayList<>();
        List<ViewVoucherNo> listCheck = new ArrayList<>();
        listCheck.addAll(viewVoucherNos);
        for (ViewVoucherNo parent : listCheck) {
            boolean checkContinue = true;
            if (viewGLPayExceedCashAll.size() > 0) {
                List<CheckQuantityExistsConvertDTO> details = new ArrayList<>();
                for (ViewVoucherNoDetailDTO sad : parent.getViewVoucherNoDetailDTOS()) {
                    for (ViewGLPayExceedCashDTO item : viewGLPayExceedCashAll) {
                        if ((parent.getTypeLedger().compareTo(Constants.TypeLedger.BOTH_BOOK) == 0 || parent.getTypeLedger().compareTo(item.getTypeLedger()) == 0) &&
                            sad.getCreditAccount().equals(item.getAccount()) && item.getDebitAmount().subtract(item.getCreditAmount()).subtract(sad.getAmount()).floatValue() < 0) {
                            parent.setReasonFail("Xuất quá tồn quỹ!");
                            lstFail.remove(parent);
                            lstFail.add(parent);
                            viewVoucherNos.remove(parent);
                            checkContinue = false;
                        } else {
                            item.setDebitAmount(item.getDebitAmount().subtract(sad.getAmount()));
                        }
                    }
                }
            }
            if (checkContinue) {
                for (ViewVoucherNoDetailDTO detailDTO : parent.getViewVoucherNoDetailDTOS()) {
                    GeneralLedger generalLedger = new GeneralLedger();
                    generalLedger.branchID(null);
                    generalLedger.setReferenceID(parent.getRefID());
                    generalLedger.setDetailID(detailDTO.getRefID());
                    generalLedger.setCompanyID(parent.getCompanyID());
                    generalLedger.setTypeID(parent.getTypeID());
                    generalLedger.postedDate(parent.getPostedDate());
                    generalLedger.date(parent.getDate());
                    generalLedger.typeLedger(parent.getTypeLedger());
                    generalLedger.noFBook(parent.getNoFBook());
                    generalLedger.noMBook(parent.getNoMBook());
                    generalLedger.account(detailDTO.getDebitAccount());
                    generalLedger.accountCorresponding(detailDTO.getCreditAccount());
                    generalLedger.currencyID(parent.getCurrencyID());
                    generalLedger.currencyID(parent.getCurrencyID());
                    generalLedger.exchangeRate(parent.getExchangeRate());
                    generalLedger.contractID(detailDTO.getContractID());
                    generalLedger.bankAccountDetailID(detailDTO.getBankAccountDetailID());
                    generalLedger.bankAccount(parent.getBankAccount());
                    generalLedger.bankName(parent.getBankName());
                    generalLedger.accountingObjectCode(detailDTO.getDebitAccountingObjectCode());
                    generalLedger.accountingObjectName(detailDTO.getDebitAccountingObjectName());
                    generalLedger.accountingObjectAddress(detailDTO.getDebitAccountingObjectAddress());
                    generalLedger.accountingObjectID(detailDTO.getDebitAccountingObjectID());
                    generalLedger.employeeCode(detailDTO.getEmployeeCode());
                    generalLedger.employeeName(detailDTO.getEmployeeName());
                    generalLedger.debitAmountOriginal(detailDTO.getAmountOriginal());
                    generalLedger.debitAmount(detailDTO.getAmount());
                    generalLedger.creditAmount(BigDecimal.ZERO);
                    generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                    generalLedger.reason(parent.getReason());
                    generalLedger.description(detailDTO.getDescription());
                    generalLedger.budgetItemID(detailDTO.getBudgetItemID());
                    generalLedger.costSetID(detailDTO.getCostSetID());
                    generalLedger.statisticsCodeID(detailDTO.getStatisticsCodeID());
                    generalLedger.employeeID(detailDTO.getEmployeeID());
                    generalLedger.refDateTime(parent.getPostedDate());
                    generalLedger.departmentID(detailDTO.getDepartmentID());
                    generalLedger.expenseItemID(detailDTO.getExpenseItemID());
                    generalLedgers.add(generalLedger);

                    GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                    generalLedgerCorresponding.branchID(null);
                    generalLedgerCorresponding.setReferenceID(parent.getRefID());
                    generalLedgerCorresponding.setDetailID(detailDTO.getRefID());
                    generalLedgerCorresponding.setCompanyID(parent.getCompanyID());
                    generalLedgerCorresponding.setTypeID(parent.getTypeID());
                    generalLedgerCorresponding.postedDate(parent.getPostedDate());
                    generalLedgerCorresponding.date(parent.getDate());
                    generalLedgerCorresponding.typeLedger(parent.getTypeLedger());
                    generalLedgerCorresponding.noFBook(parent.getNoFBook());
                    generalLedgerCorresponding.noMBook(parent.getNoMBook());
                    generalLedgerCorresponding.account(detailDTO.getCreditAccount());
                    generalLedgerCorresponding.accountCorresponding(detailDTO.getDebitAccount());
                    generalLedgerCorresponding.currencyID(parent.getCurrencyID());
                    generalLedgerCorresponding.exchangeRate(parent.getExchangeRate());
                    generalLedgerCorresponding.contractID(detailDTO.getContractID());
                    generalLedgerCorresponding.bankAccountDetailID(detailDTO.getBankAccountDetailID());
                    generalLedgerCorresponding.bankAccount(parent.getBankAccount());
                    generalLedgerCorresponding.bankName(parent.getBankName());
                    generalLedgerCorresponding.accountingObjectCode(detailDTO.getCreditAccountingObjectCode());
                    generalLedgerCorresponding.accountingObjectName(detailDTO.getCreditAccountingObjectName());
                    generalLedgerCorresponding.accountingObjectAddress(detailDTO.getCreditAccountingObjectAddress());
                    generalLedgerCorresponding.accountingObjectID(detailDTO.getCreditAccountingObjectID());
                    generalLedgerCorresponding.employeeCode(detailDTO.getEmployeeCode());
                    generalLedgerCorresponding.employeeName(detailDTO.getEmployeeName());
                    generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                    generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);
                    generalLedgerCorresponding.creditAmount(detailDTO.getAmountOriginal());
                    generalLedgerCorresponding.creditAmountOriginal(detailDTO.getAmount());
                    generalLedgerCorresponding.reason(parent.getReason());
                    generalLedgerCorresponding.description(detailDTO.getDescription());
                    generalLedgerCorresponding.budgetItemID(detailDTO.getBudgetItemID());
                    generalLedgerCorresponding.costSetID(detailDTO.getCostSetID());
                    generalLedgerCorresponding.statisticsCodeID(detailDTO.getStatisticsCodeID());
                    generalLedgerCorresponding.employeeID(detailDTO.getEmployeeID());
                    generalLedgerCorresponding.refDateTime(parent.getPostedDate());
                    generalLedgerCorresponding.departmentID(detailDTO.getDepartmentID());
                    generalLedgerCorresponding.expenseItemID(detailDTO.getExpenseItemID());
                    generalLedgers.add(generalLedgerCorresponding);
                }
            }
        }
        return generalLedgers;
    }

    /*Bán hàng*/
    List<GeneralLedger> genGeneralLedgersSAInvoice
    (List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail, List<MGForPPOrderConvertDTO> mgForPPOrderConvertDTOS) {
        List<GeneralLedger> generalLedgers = new ArrayList<>();
        List<ViewVoucherNo> listCheck = new ArrayList<>();
        listCheck.addAll(viewVoucherNos);
        for (ViewVoucherNo parent : listCheck) {
            boolean checkContinue = true;
            if (Boolean.TRUE.equals(parent.getDeliveryVoucher()) && mgForPPOrderConvertDTOS.size() > 0) {
                List<CheckQuantityExistsConvertDTO> details = new ArrayList<>();
                for (ViewVoucherNoDetailDTO item : parent.getViewVoucherNoDetailDTOS()) {
                    details.add(new CheckQuantityExistsConvertDTO(item.getMaterialGoodsID(), item.getMainQuantity()));
                }
                CheckQuantityExistsDTO checkQuantityExistsDTO = utilsService.checkQuantityBalance(details, mgForPPOrderConvertDTOS);
                if (checkQuantityExistsDTO.getQuantityExists() != null && checkQuantityExistsDTO.getQuantityExists().length() > 0) {
                    parent.setReasonFail("VTHH " + checkQuantityExistsDTO.getQuantityExists() + " xuất quá số tồn!");
                    lstFail.add(parent);
                    viewVoucherNos.remove(parent);
                    checkContinue = false;
                }
            }
            if (checkContinue) {
                Optional<RSInwardOutward> rSInwardOutward = Optional.empty();
                Optional<MCReceipt> mcReceiptOptional = Optional.empty();
                Optional<MBDeposit> mbDepositOptional = Optional.empty();
                if (parent.getTypeID().equals(BAN_HANG_THU_TIEN_NGAY_TM)) {
                    mcReceiptOptional = mcReceiptRepository.findById(parent.getmCReceiptID());
                } else if (parent.getTypeID().equals(BAN_HANG_THU_TIEN_NGAY_CK)) {
                    mbDepositOptional = mbDepositRepository.findById(parent.getmBDepositID());
                }
                for (ViewVoucherNoDetailDTO detailDTO : parent.getViewVoucherNoDetailDTOS()) {
                    try {
                        Optional<BankAccountDetails> accBankDetailObj = (mbDepositOptional.isPresent() && mbDepositOptional.get().getBankAccountDetailID() != null) ? bankAccountDetailsRepository.findOneById(mbDepositOptional.get().getBankAccountDetailID()) : Optional.empty();
                        if (Boolean.FALSE.equals(detailDTO.isIsPromotion()) || detailDTO.isIsPromotion() == null) {
                            GeneralLedger generalLedger = new GeneralLedger();
                            BeanUtils.copyProperties(generalLedger, parent);
                            generalLedger.setTypeID(parent.getTypeID());
                            generalLedger.account(detailDTO.getDebitAccount());
                            generalLedger.accountCorresponding(detailDTO.getCreditAccount());
                            generalLedger.debitAmount(detailDTO.getAmount());
                            generalLedger.debitAmountOriginal(detailDTO.getAmountOriginal());
                            generalLedger.creditAmount(BigDecimal.ZERO);
                            generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                            generalLedger.description(detailDTO.getDescription());
                            generalLedger.accountingObjectID(detailDTO.getAccountingObjectID());
                            generalLedger.accountingObjectName(detailDTO.getAccountingObjectName());
                            generalLedger.accountingObjectCode(detailDTO.getAccountingObjectCode());
                            generalLedger.setReferenceID(parent.getRefID());
                            generalLedger.employeeID(parent.getEmployeeID());
                            generalLedger.setEmployeeCode(parent.getEmployeeCode());
                            generalLedger.setEmployeeName(parent.getEmployeeName());
                            generalLedger.detailID(detailDTO.getRefID());

                            generalLedger.materialGoodsID(detailDTO.getMaterialGoodsID());
                            generalLedger.materialGoodsCode(detailDTO.getMaterialGoodsCode());
                            generalLedger.materialGoodsName(detailDTO.getMaterialGoodsName());
                            generalLedger.repositoryID(detailDTO.getRepositoryID());
                            generalLedger.repositoryCode(detailDTO.getRepositoryCode());
                            generalLedger.repositoryName(detailDTO.getRepositoryName());

                            generalLedger.setUnitID(detailDTO.getUnitID());
                            generalLedger.setQuantity(detailDTO.getQuantity());
                            generalLedger.setUnitPrice(detailDTO.getUnitPrice());
                            generalLedger.setUnitPriceOriginal(detailDTO.getUnitPriceOriginal());
                            generalLedger.setMainUnitID(detailDTO.getMainUnitID());
                            generalLedger.setMainQuantity(detailDTO.getMainQuantity());
                            generalLedger.setMainUnitPrice(detailDTO.getMainUnitPrice());
                            generalLedger.setMainConvertRate(detailDTO.getMainConvertRate());
                            generalLedger.setFormula(detailDTO.getFormula());

                            generalLedger.refDateTime(parent.getDate());
                            generalLedger.budgetItemID(detailDTO.getBudgetItemID());
                            generalLedger.costSetID(detailDTO.getCostSetID());
                            generalLedger.contractID(detailDTO.getContractID());
                            generalLedger.statisticsCodeID(detailDTO.getStatisticsCodeID());
                            generalLedger.expenseItemID(detailDTO.getExpenseItemID());
                            if (mcReceiptOptional.isPresent()) {
                                generalLedger.setNoFBook(mcReceiptOptional.get().getNoFBook());
                                generalLedger.setNoMBook(mcReceiptOptional.get().getNoMBook());
                            } else if (mbDepositOptional.isPresent()) {
                                generalLedger.setNoFBook(mbDepositOptional.get().getNoFBook());
                                generalLedger.setNoMBook(mbDepositOptional.get().getNoMBook());
                                generalLedger.setBankAccountDetailID(mbDepositOptional.get().getBankAccountDetailID());
                                generalLedger.setBankAccount(accBankDetailObj.map(BankAccountDetails::getBankAccount).orElse(null));
                                generalLedger.setBankName(accBankDetailObj.map(BankAccountDetails::getBankName).orElse(null));
                            }
                            generalLedgers.add(generalLedger);

                            GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                            BeanUtils.copyProperties(generalLedgerCorresponding, generalLedger);
                            generalLedgerCorresponding.setTypeID(parent.getTypeID());
                            generalLedgerCorresponding.account(detailDTO.getCreditAccount());
                            generalLedgerCorresponding.accountCorresponding(detailDTO.getDebitAccount());
                            generalLedgerCorresponding.creditAmount(detailDTO.getAmount());
                            generalLedgerCorresponding.creditAmountOriginal(detailDTO.getAmountOriginal());
                            generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                            generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);
                            generalLedgers.add(generalLedgerCorresponding);
                            if (detailDTO.getDiscountAmountOriginal() != null && detailDTO.getDiscountAmountOriginal().doubleValue() != 0 && detailDTO.getDiscountAccount() != null) {
                                GeneralLedger generalLedger3 = new GeneralLedger();
                                BeanUtils.copyProperties(generalLedger3, parent);
                                generalLedger3.setTypeID(parent.getTypeID());
                                generalLedger3.account(detailDTO.getDiscountAccount());
                                generalLedger3.accountCorresponding(detailDTO.getDebitAccount());
                                generalLedger3.debitAmount(detailDTO.getDiscountAmount());
                                generalLedger3.debitAmountOriginal(detailDTO.getDiscountAmountOriginal());
                                generalLedger3.creditAmount(BigDecimal.ZERO);
                                generalLedger3.creditAmountOriginal(BigDecimal.ZERO);
                                generalLedger3.description(detailDTO.getDescription());
                                generalLedger3.accountingObjectID(detailDTO.getAccountingObjectID());
                                generalLedger3.accountingObjectName(detailDTO.getAccountingObjectName());
                                generalLedger3.accountingObjectCode(detailDTO.getAccountingObjectCode());
                                generalLedger3.setReferenceID(parent.getRefID());
                                generalLedger3.employeeID(parent.getEmployeeID());
                                generalLedger3.setEmployeeCode(parent.getEmployeeCode());
                                generalLedger3.setEmployeeName(parent.getEmployeeName());
                                generalLedger3.detailID(detailDTO.getRefID());

                                generalLedger3.materialGoodsID(detailDTO.getMaterialGoodsID());
                                generalLedger3.materialGoodsCode(detailDTO.getMaterialGoodsCode());
                                generalLedger3.materialGoodsName(detailDTO.getMaterialGoodsName());
                                generalLedger3.repositoryID(detailDTO.getRepositoryID());
                                generalLedger3.repositoryCode(detailDTO.getRepositoryCode());
                                generalLedger3.repositoryName(detailDTO.getRepositoryName());

                                generalLedger3.setUnitID(detailDTO.getUnitID());
                                generalLedger3.setQuantity(detailDTO.getQuantity());
                                generalLedger3.setUnitPrice(detailDTO.getUnitPrice());
                                generalLedger3.setUnitPriceOriginal(detailDTO.getUnitPriceOriginal());
                                generalLedger3.setMainUnitID(detailDTO.getMainUnitID());
                                generalLedger3.setMainQuantity(detailDTO.getMainQuantity());
                                generalLedger3.setMainUnitPrice(detailDTO.getMainUnitPrice());
                                generalLedger3.setMainConvertRate(detailDTO.getMainConvertRate());
                                generalLedger3.setFormula(detailDTO.getFormula());

                                generalLedger3.refDateTime(parent.getDate());
                                generalLedger3.budgetItemID(detailDTO.getBudgetItemID());
                                generalLedger3.costSetID(detailDTO.getCostSetID());
                                generalLedger3.contractID(detailDTO.getContractID());
                                generalLedger3.statisticsCodeID(detailDTO.getStatisticsCodeID());
                                generalLedger3.expenseItemID(detailDTO.getExpenseItemID());
                                if (mcReceiptOptional.isPresent()) {
                                    generalLedger3.setNoFBook(mcReceiptOptional.get().getNoFBook());
                                    generalLedger3.setNoMBook(mcReceiptOptional.get().getNoMBook());
                                } else if (mbDepositOptional.isPresent()) {
                                    generalLedger3.setNoFBook(mbDepositOptional.get().getNoFBook());
                                    generalLedger3.setNoMBook(mbDepositOptional.get().getNoMBook());
                                    generalLedger3.setBankAccountDetailID(mbDepositOptional.get().getBankAccountDetailID());
                                    generalLedger3.setBankAccount(accBankDetailObj.map(BankAccountDetails::getBankAccount).orElse(null));
                                    generalLedger3.setBankName(accBankDetailObj.map(BankAccountDetails::getBankName).orElse(null));
                                }
                                generalLedgers.add(generalLedger3);

                                GeneralLedger generalLedgerCorresponding3 = new GeneralLedger();
                                BeanUtils.copyProperties(generalLedgerCorresponding3, generalLedger3);
                                generalLedgerCorresponding3.setTypeID(parent.getTypeID());
                                generalLedgerCorresponding3.account(detailDTO.getDebitAccount());
                                generalLedgerCorresponding3.accountCorresponding(detailDTO.getDiscountAccount());
                                generalLedgerCorresponding3.creditAmount(detailDTO.getDiscountAmount());
                                generalLedgerCorresponding3.creditAmountOriginal(detailDTO.getDiscountAmountOriginal());
                                generalLedgerCorresponding3.debitAmount(BigDecimal.ZERO);
                                generalLedgerCorresponding3.debitAmountOriginal(BigDecimal.ZERO);
                                generalLedgers.add(generalLedgerCorresponding3);
                            }
                        }
                        if (Boolean.TRUE.equals(parent.getExported())) {
                            if (detailDTO.getExportTaxAmount() != null && detailDTO.getExportTaxAmount().doubleValue() != 0) {
                                GeneralLedger generalLedger1 = new GeneralLedger();
                                BeanUtils.copyProperties(generalLedger1, parent);
                                generalLedger1.setTypeID(parent.getTypeID());
                                generalLedger1.account(detailDTO.getExportTaxAccountCorresponding());
                                generalLedger1.accountCorresponding(detailDTO.getExportTaxAmountAccount());
                                generalLedger1.debitAmount(detailDTO.getExportTaxAmount());
                                generalLedger1.debitAmountOriginal(detailDTO.getExportTaxAmount());
                                generalLedger1.creditAmount(BigDecimal.ZERO);
                                generalLedger1.creditAmountOriginal(BigDecimal.ZERO);
                                generalLedger1.description((DESCRIPTION_VAT.EXPORT_TAX + detailDTO.getMaterialGoodsCode()));
                                generalLedger1.accountingObjectID(detailDTO.getAccountingObjectID());
                                generalLedger1.accountingObjectName(detailDTO.getAccountingObjectName());
                                generalLedger1.accountingObjectCode(detailDTO.getAccountingObjectCode());
                                generalLedger1.setReferenceID(parent.getRefID());
                                generalLedger1.employeeID(parent.getEmployeeID());
                                generalLedger1.setEmployeeCode(parent.getEmployeeCode());
                                generalLedger1.setEmployeeName(parent.getEmployeeName());
                                generalLedger1.detailID(detailDTO.getRefID());

                                generalLedger1.materialGoodsID(detailDTO.getMaterialGoodsID());
                                generalLedger1.materialGoodsCode(detailDTO.getMaterialGoodsCode());
                                generalLedger1.materialGoodsName(detailDTO.getMaterialGoodsName());
                                generalLedger1.repositoryID(detailDTO.getRepositoryID());
                                generalLedger1.repositoryCode(detailDTO.getRepositoryCode());
                                generalLedger1.repositoryName(detailDTO.getRepositoryName());

                                generalLedger1.setUnitID(detailDTO.getUnitID());
                                generalLedger1.setQuantity(detailDTO.getQuantity());
                                generalLedger1.setUnitPrice(detailDTO.getUnitPrice());
                                generalLedger1.setUnitPriceOriginal(detailDTO.getUnitPriceOriginal());
                                generalLedger1.setMainUnitID(detailDTO.getMainUnitID());
                                generalLedger1.setMainQuantity(detailDTO.getMainQuantity());
                                generalLedger1.setMainUnitPrice(detailDTO.getMainUnitPrice());
                                generalLedger1.setMainConvertRate(detailDTO.getMainConvertRate());
                                generalLedger1.setFormula(detailDTO.getFormula());

                                generalLedger1.refDateTime(parent.getDate());
                                generalLedger1.budgetItemID(detailDTO.getBudgetItemID());
                                generalLedger1.costSetID(detailDTO.getCostSetID());
                                generalLedger1.contractID(detailDTO.getContractID());
                                generalLedger1.statisticsCodeID(detailDTO.getStatisticsCodeID());
                                generalLedger1.expenseItemID(detailDTO.getExpenseItemID());
                                if (mcReceiptOptional.isPresent()) {
                                    generalLedger1.setNoFBook(mcReceiptOptional.get().getNoFBook());
                                    generalLedger1.setNoMBook(mcReceiptOptional.get().getNoMBook());
                                } else if (mbDepositOptional.isPresent()) {
                                    generalLedger1.setNoFBook(mbDepositOptional.get().getNoFBook());
                                    generalLedger1.setNoMBook(mbDepositOptional.get().getNoMBook());
                                    generalLedger1.setBankAccountDetailID(mbDepositOptional.get().getBankAccountDetailID());
                                    generalLedger1.setBankAccount(accBankDetailObj.map(BankAccountDetails::getBankAccount).orElse(null));
                                    generalLedger1.setBankName(accBankDetailObj.map(BankAccountDetails::getBankName).orElse(null));
                                }
                                generalLedgers.add(generalLedger1);

                                GeneralLedger generalLedgerCorresponding1 = new GeneralLedger();
                                BeanUtils.copyProperties(generalLedgerCorresponding1, generalLedger1);
                                generalLedgerCorresponding1.setTypeID(parent.getTypeID());
                                generalLedgerCorresponding1.account(detailDTO.getExportTaxAmountAccount());
                                generalLedgerCorresponding1.accountCorresponding(detailDTO.getExportTaxAccountCorresponding());
                                generalLedgerCorresponding1.creditAmount(detailDTO.getExportTaxAmount());
                                generalLedgerCorresponding1.creditAmountOriginal(detailDTO.getExportTaxAmount());
                                generalLedgerCorresponding1.debitAmount(BigDecimal.ZERO);
                                generalLedgerCorresponding1.debitAmountOriginal(BigDecimal.ZERO);
                                generalLedgers.add(generalLedgerCorresponding1);
                            }
                        } else {
                            if (detailDTO.getvATAmountOriginal() != null && detailDTO.getvATAmountOriginal().doubleValue() != 0) {
                                GeneralLedger generalLedger2 = new GeneralLedger();
                                BeanUtils.copyProperties(generalLedger2, parent);
                                generalLedger2.setTypeID(parent.getTypeID());
                                generalLedger2.account(detailDTO.getDeductionDebitAccount());
                                generalLedger2.accountCorresponding(detailDTO.getvATAccount());
                                generalLedger2.debitAmount(detailDTO.getvATAmount());
                                generalLedger2.debitAmountOriginal(detailDTO.getvATAmountOriginal());
                                generalLedger2.creditAmount(BigDecimal.ZERO);
                                generalLedger2.creditAmountOriginal(BigDecimal.ZERO);
                                generalLedger2.description(detailDTO.getVatDescription());
                                generalLedger2.accountingObjectID(detailDTO.getAccountingObjectID());
                                generalLedger2.accountingObjectCode(detailDTO.getAccountingObjectCode());
                                generalLedger2.accountingObjectName(detailDTO.getAccountingObjectName());
                                generalLedger2.setReferenceID(parent.getRefID());
                                generalLedger2.employeeID(parent.getEmployeeID());
                                generalLedger2.setEmployeeCode(parent.getEmployeeCode());
                                generalLedger2.setEmployeeName(parent.getEmployeeName());
                                generalLedger2.detailID(detailDTO.getRefID());

                                generalLedger2.materialGoodsID(detailDTO.getMaterialGoodsID());
                                generalLedger2.materialGoodsCode(detailDTO.getMaterialGoodsCode());
                                generalLedger2.materialGoodsName(detailDTO.getMaterialGoodsName());
                                generalLedger2.repositoryID(detailDTO.getRepositoryID());
                                generalLedger2.repositoryCode(detailDTO.getRepositoryCode());
                                generalLedger2.repositoryName(detailDTO.getRepositoryName());

                                generalLedger2.setUnitID(detailDTO.getUnitID());
                                generalLedger2.setQuantity(detailDTO.getQuantity());
                                generalLedger2.setUnitPrice(detailDTO.getUnitPrice());
                                generalLedger2.setUnitPriceOriginal(detailDTO.getUnitPriceOriginal());
                                generalLedger2.setMainUnitID(detailDTO.getMainUnitID());
                                generalLedger2.setMainQuantity(detailDTO.getMainQuantity());
                                generalLedger2.setMainConvertRate(detailDTO.getMainConvertRate());
                                generalLedger2.setMainUnitPrice(detailDTO.getMainUnitPrice());
                                generalLedger2.setFormula(detailDTO.getFormula());

                                generalLedger2.refDateTime(parent.getDate());
                                generalLedger2.budgetItemID(detailDTO.getBudgetItemID());
                                generalLedger2.costSetID(detailDTO.getCostSetID());
                                generalLedger2.contractID(detailDTO.getContractID());
                                generalLedger2.statisticsCodeID(detailDTO.getStatisticsCodeID());
                                generalLedger2.expenseItemID(detailDTO.getExpenseItemID());
                                if (mcReceiptOptional.isPresent()) {
                                    generalLedger2.setNoFBook(mcReceiptOptional.get().getNoFBook());
                                    generalLedger2.setNoMBook(mcReceiptOptional.get().getNoMBook());
                                } else if (mbDepositOptional.isPresent()) {
                                    generalLedger2.setNoFBook(mbDepositOptional.get().getNoFBook());
                                    generalLedger2.setNoMBook(mbDepositOptional.get().getNoMBook());
                                    generalLedger2.setBankAccountDetailID(mbDepositOptional.get().getBankAccountDetailID());
                                    generalLedger2.setBankAccount(accBankDetailObj.map(BankAccountDetails::getBankAccount).orElse(null));
                                    generalLedger2.setBankName(accBankDetailObj.map(BankAccountDetails::getBankName).orElse(null));
                                }
                                generalLedgers.add(generalLedger2);

                                GeneralLedger generalLedgerCorresponding2 = new GeneralLedger();
                                BeanUtils.copyProperties(generalLedgerCorresponding2, generalLedger2);
                                generalLedgerCorresponding2.setTypeID(parent.getTypeID());
                                generalLedgerCorresponding2.account(detailDTO.getvATAccount());
                                generalLedgerCorresponding2.accountCorresponding(detailDTO.getDeductionDebitAccount());
                                generalLedgerCorresponding2.creditAmount(detailDTO.getvATAmount());
                                generalLedgerCorresponding2.creditAmountOriginal(detailDTO.getvATAmountOriginal());
                                generalLedgerCorresponding2.debitAmount(BigDecimal.ZERO);
                                generalLedgerCorresponding2.debitAmountOriginal(BigDecimal.ZERO);
                                generalLedgers.add(generalLedgerCorresponding2);
                            }
                        }
                        if (Boolean.TRUE.equals(parent.getDeliveryVoucher())) {
                            rSInwardOutward = rSInwardOutwardRepository.findById(parent.getrSInwardOutwardID());
                            if (rSInwardOutward.isPresent()) {
                                GeneralLedger generalLedger4 = new GeneralLedger();
                                BeanUtils.copyProperties(generalLedger4, parent);
                                generalLedger4.setTypeID(411);
                                if (rSInwardOutward.get().getNoFBook() != null) {
                                    generalLedger4.setNoFBook(rSInwardOutward.get().getNoFBook());
                                }
                                if (rSInwardOutward.get().getNoMBook() != null) {
                                    generalLedger4.setNoMBook(rSInwardOutward.get().getNoMBook());
                                }
                                generalLedger4.account(detailDTO.getCostAccount());
                                generalLedger4.accountCorresponding(detailDTO.getRepositoryAccount());
                                generalLedger4.debitAmount(detailDTO.getoWAmount());
                                generalLedger4.debitAmountOriginal(detailDTO.getoWAmount());
                                generalLedger4.creditAmount(BigDecimal.ZERO);
                                generalLedger4.creditAmountOriginal(BigDecimal.ZERO);
                                generalLedger4.description(detailDTO.getDescription());
                                generalLedger4.accountingObjectID(detailDTO.getAccountingObjectID());
                                generalLedger4.accountingObjectName(detailDTO.getAccountingObjectName());
                                generalLedger4.accountingObjectCode(detailDTO.getAccountingObjectCode());
                                generalLedger4.setReferenceID(parent.getRefID());
                                generalLedger4.employeeID(parent.getEmployeeID());
                                generalLedger4.setEmployeeCode(parent.getEmployeeCode());
                                generalLedger4.setEmployeeName(parent.getEmployeeName());
                                generalLedger4.detailID(detailDTO.getRefID());

                                generalLedger4.materialGoodsID(detailDTO.getMaterialGoodsID());
                                generalLedger4.materialGoodsCode(detailDTO.getMaterialGoodsCode());
                                generalLedger4.materialGoodsName(detailDTO.getMaterialGoodsName());
                                generalLedger4.repositoryID(detailDTO.getRepositoryID());
                                generalLedger4.repositoryCode(detailDTO.getRepositoryCode());
                                generalLedger4.repositoryName(detailDTO.getRepositoryName());

                                generalLedger4.setUnitID(detailDTO.getUnitID());
                                generalLedger4.setQuantity(detailDTO.getQuantity());
                                generalLedger4.setUnitPrice(detailDTO.getUnitPrice());
                                generalLedger4.setUnitPriceOriginal(detailDTO.getUnitPriceOriginal());
                                generalLedger4.setMainUnitID(detailDTO.getMainUnitID());
                                generalLedger4.setMainQuantity(detailDTO.getMainQuantity());
                                generalLedger4.setMainUnitPrice(detailDTO.getMainUnitPrice());
                                generalLedger4.setMainConvertRate(detailDTO.getMainConvertRate());
                                generalLedger4.setFormula(detailDTO.getFormula());

                                generalLedger4.refDateTime(parent.getDate());
                                generalLedger4.budgetItemID(detailDTO.getBudgetItemID());
                                generalLedger4.costSetID(detailDTO.getCostSetID());
                                generalLedger4.contractID(detailDTO.getContractID());
                                generalLedger4.statisticsCodeID(detailDTO.getStatisticsCodeID());
                                generalLedger4.expenseItemID(detailDTO.getExpenseItemID());
                                generalLedgers.add(generalLedger4);

                                GeneralLedger generalLedgerCorresponding4 = new GeneralLedger();
                                BeanUtils.copyProperties(generalLedgerCorresponding4, generalLedger4);
                                generalLedgerCorresponding4.setTypeID(411);
                                generalLedgerCorresponding4.account(detailDTO.getRepositoryAccount());
                                generalLedgerCorresponding4.accountCorresponding(detailDTO.getCostAccount());
                                generalLedgerCorresponding4.creditAmount(detailDTO.getoWAmount());
                                generalLedgerCorresponding4.creditAmountOriginal(detailDTO.getoWAmount());
                                generalLedgerCorresponding4.debitAmount(BigDecimal.ZERO);
                                generalLedgerCorresponding4.debitAmountOriginal(BigDecimal.ZERO);
                                generalLedgers.add(generalLedgerCorresponding4);
                            }
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return generalLedgers;
    }

    /*Bán hàng*/
    List<RepositoryLedger> repositoryLedgersSAInvoice
    (List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail, List<MGForPPOrderConvertDTO> mgForPPOrderConvertDTOS, Boolean checkQuaSLTon) {
        List<RepositoryLedger> repositoryLedgers = new ArrayList<>();
        List<ViewVoucherNo> listCheck = new ArrayList<>();
        listCheck.addAll(viewVoucherNos);
        for (ViewVoucherNo parent : listCheck) {
            boolean checkContinue = true;
            if (Boolean.TRUE.equals(parent.getDeliveryVoucher()) && checkQuaSLTon) {
                List<CheckQuantityExistsConvertDTO> details = new ArrayList<>();
                for (ViewVoucherNoDetailDTO item : parent.getViewVoucherNoDetailDTOS()) {
                    details.add(new CheckQuantityExistsConvertDTO(item.getMaterialGoodsID(), item.getMainQuantity()));
                }
                CheckQuantityExistsDTO checkQuantityExistsDTO = utilsService.checkQuantityBalance(details, mgForPPOrderConvertDTOS);
                if (checkQuantityExistsDTO.getQuantityExists() != null && checkQuantityExistsDTO.getQuantityExists().length() > 0) {
                    parent.setReasonFail("VTHH " + checkQuantityExistsDTO.getQuantityExists() + " xuất quá số tồn!");
                    lstFail.add(parent);
                    viewVoucherNos.remove(parent);
                    checkContinue = false;
                }
            }
            if (checkContinue) {
                RSInwardOutwardSearchDTO rSInwardOutward = new RSInwardOutwardSearchDTO();
                for (ViewVoucherNoDetailDTO detailDTO : parent.getViewVoucherNoDetailDTOS()) {
                    if (Boolean.TRUE.equals(parent.getDeliveryVoucher())) {
                        rSInwardOutward = rSInwardOutwardRepository.findByID(parent.getrSInwardOutwardID());
                        if (rSInwardOutward != null) {
                            RepositoryLedger repositoryLedgerItem = new RepositoryLedger();
                            repositoryLedgerItem.setReferenceID(rSInwardOutward.getId());
                            repositoryLedgerItem.setCompanyID(parent.getCompanyID());
                            if (rSInwardOutward.getNoFBook() != null) {
                                repositoryLedgerItem.setNoFBook(rSInwardOutward.getNoFBook());
                            }
                            if (rSInwardOutward.getNoMBook() != null) {
                                repositoryLedgerItem.setNoMBook(rSInwardOutward.getNoMBook());
                            }
                            repositoryLedgerItem.setTypeID(rSInwardOutward.getTypeID());
                            repositoryLedgerItem.setOwQuantity(detailDTO.getQuantity());
                            repositoryLedgerItem.setUnitPrice(detailDTO.getoWPrice());
                            repositoryLedgerItem.setOwAmount(detailDTO.getoWAmount());
                            repositoryLedgerItem.setAccount(detailDTO.getRepositoryAccount());
                            repositoryLedgerItem.setAccountCorresponding(detailDTO.getCostAccount());
                            repositoryLedgerItem.setDetailID(detailDTO.getRefID());
                            repositoryLedgerItem.setFormula(detailDTO.getFormula());
                            repositoryLedgerItem.setMaterialGoodsID(detailDTO.getMaterialGoodsID());
                            repositoryLedgerItem.setRepositoryID(detailDTO.getRepositoryID());
                            repositoryLedgerItem.setLotNo(detailDTO.getLotNo());
                            repositoryLedgerItem.setDescription(detailDTO.getDescription());
                            repositoryLedgerItem.setReason(rSInwardOutward.getReason());
                            repositoryLedgerItem.setDate(rSInwardOutward.getDate());
                            repositoryLedgerItem.setPostedDate(rSInwardOutward.getPostedDate());
                            repositoryLedgerItem.setExpiryDate(detailDTO.getExpiryDate());
                            repositoryLedgerItem.setTypeLedger(rSInwardOutward.getTypeLedger());
                            repositoryLedgerItem.setUnitID(detailDTO.getUnitID());
                            repositoryLedgerItem.setMainUnitID(detailDTO.getMainUnitID());
                            repositoryLedgerItem.setMainUnitPrice(detailDTO.getMainUnitPrice());
                            repositoryLedgerItem.setMainOWQuantity(detailDTO.getMainQuantity());
                            repositoryLedgerItem.setMainConvertRate(detailDTO.getMainConvertRate());
                            repositoryLedgerItem.setBudgetItemID(detailDTO.getBudgetItemID());
                            repositoryLedgerItem.setCostSetID(detailDTO.getCostSetID());
                            repositoryLedgerItem.setStatisticsCodeID(detailDTO.getStatisticsCodeID());
                            repositoryLedgerItem.setExpenseItemID(detailDTO.getExpenseItemID());
                            repositoryLedgerItem.setConfrontID(detailDTO.getConfrontID());
                            repositoryLedgerItem.setConfrontDetailID(detailDTO.getConfrontDetailID());
                            repositoryLedgerItem.setRepositoryCode(detailDTO.getRepositoryCode());
                            repositoryLedgerItem.setRepositoryName(detailDTO.getRepositoryName());
                            repositoryLedgerItem.setMaterialGoodsCode(detailDTO.getMaterialGoodsCode());
                            repositoryLedgerItem.setMaterialGoodsName(detailDTO.getMaterialGoodsName());
                            repositoryLedgers.add(repositoryLedgerItem);
                        }
                    }
                }
            }
        }
        return repositoryLedgers;
    }

    /*Bán hàng*/
    List<GeneralLedger> genGeneralLedgersSAReturn
    (List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail, List<MGForPPOrderConvertDTO> mgForPPOrderConvertDTOS, List<ViewGLPayExceedCashDTO> viewGLPayExceedCashAll) {
        List<GeneralLedger> generalLedgers = new ArrayList<>();
        List<ViewVoucherNo> listCheck = new ArrayList<>();
        listCheck.addAll(viewVoucherNos);
        for (ViewVoucherNo parent : listCheck) {
            boolean checkContinue = true;
            if (Boolean.TRUE.equals(parent.getDeliveryVoucher()) && mgForPPOrderConvertDTOS.size() > 0) {
                List<CheckQuantityExistsConvertDTO> details = new ArrayList<>();
                for (ViewVoucherNoDetailDTO item : parent.getViewVoucherNoDetailDTOS()) {
                    details.add(new CheckQuantityExistsConvertDTO(item.getMaterialGoodsID(), item.getMainQuantity()));
                }
                CheckQuantityExistsDTO checkQuantityExistsDTO = utilsService.checkQuantityBalance(details, mgForPPOrderConvertDTOS);
                if (checkQuantityExistsDTO.getQuantityExists() != null && checkQuantityExistsDTO.getQuantityExists().length() > 0) {
                    parent.setReasonFail("VTHH " + checkQuantityExistsDTO.getQuantityExists() + " xuất quá số tồn!");
                    lstFail.add(parent);
                    viewVoucherNos.remove(parent);
                    checkContinue = false;
                }
            }
            if (viewGLPayExceedCashAll.size() > 0) {
                List<CheckQuantityExistsConvertDTO> details = new ArrayList<>();
                for (ViewVoucherNoDetailDTO sad : parent.getViewVoucherNoDetailDTOS()) {
                    for (ViewGLPayExceedCashDTO item : viewGLPayExceedCashAll) {
                        if ((parent.getTypeLedger().compareTo(Constants.TypeLedger.BOTH_BOOK) == 0 || parent.getTypeLedger().compareTo(item.getTypeLedger()) == 0) &&
                            sad.getCreditAccount().equals(item.getAccount()) && item.getDebitAmount().subtract(item.getCreditAmount()).subtract(sad.getAmount()).floatValue() < 0) {
                            parent.setReasonFail("Xuất quá tồn quỹ!");
                            lstFail.remove(parent);
                            lstFail.add(parent);
                            viewVoucherNos.remove(parent);
                            checkContinue = false;
                        } else {
                            item.setDebitAmount(item.getDebitAmount().subtract(sad.getAmount()));
                        }
                    }
                }
            }
            if (checkContinue) {
                Optional<RSInwardOutward> rSInwardOutward = Optional.empty();
                if (Boolean.TRUE.equals(parent.getDeliveryVoucher())) {
                    rSInwardOutward = rSInwardOutwardRepository.findById(parent.getrSInwardOutwardID());
                }
                for (ViewVoucherNoDetailDTO detailDTO : parent.getViewVoucherNoDetailDTOS()) {
                    try {
                        GeneralLedger generalLedger = new GeneralLedger();
                        BeanUtils.copyProperties(generalLedger, parent);
                        generalLedger.setTypeID(parent.getTypeID());
                        generalLedger.account(detailDTO.getDebitAccount());
                        generalLedger.accountCorresponding(detailDTO.getCreditAccount());
                        generalLedger.debitAmount(detailDTO.getAmount().subtract(detailDTO.getExportTaxAmount() == null ? BigDecimal.ZERO : detailDTO.getExportTaxAmount()));
                        generalLedger.debitAmountOriginal(detailDTO.getAmountOriginal().subtract(detailDTO.getExportTaxAmount() == null ? BigDecimal.ZERO : detailDTO.getExportTaxAmount().divide(parent.getExchangeRate())));
                        generalLedger.creditAmount(BigDecimal.ZERO);
                        generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                        generalLedger.description(detailDTO.getDescription());
                        generalLedger.accountingObjectID(detailDTO.getAccountingObjectID());
                        generalLedger.accountingObjectName(detailDTO.getAccountingObjectName());
                        generalLedger.accountingObjectCode(detailDTO.getAccountingObjectCode());
                        generalLedger.setReferenceID(parent.getRefID());
                        generalLedger.employeeID(parent.getEmployeeID());
                        generalLedger.setEmployeeCode(parent.getEmployeeCode());
                        generalLedger.setEmployeeName(parent.getEmployeeName());
                        generalLedger.detailID(detailDTO.getRefID());

                        generalLedger.materialGoodsID(detailDTO.getMaterialGoodsID());
                        generalLedger.materialGoodsCode(detailDTO.getMaterialGoodsCode());
                        generalLedger.materialGoodsName(detailDTO.getMaterialGoodsName());
                        generalLedger.repositoryID(detailDTO.getRepositoryID());
                        generalLedger.repositoryCode(detailDTO.getRepositoryCode());
                        generalLedger.repositoryName(detailDTO.getRepositoryName());

                        generalLedger.setUnitID(detailDTO.getUnitID());
                        generalLedger.setQuantity(detailDTO.getQuantity());
                        generalLedger.setUnitPrice(detailDTO.getUnitPrice());
                        generalLedger.setUnitPriceOriginal(detailDTO.getUnitPriceOriginal());
                        generalLedger.setMainUnitID(detailDTO.getMainUnitID());
                        generalLedger.setMainQuantity(detailDTO.getMainQuantity());
                        generalLedger.setMainUnitPrice(detailDTO.getMainUnitPrice());
                        generalLedger.setMainConvertRate(detailDTO.getMainConvertRate());
                        generalLedger.setFormula(detailDTO.getFormula());

                        generalLedger.refDateTime(parent.getDate());
                        generalLedger.budgetItemID(detailDTO.getBudgetItemID());
                        generalLedger.costSetID(detailDTO.getCostSetID());
                        generalLedger.contractID(detailDTO.getContractID());
                        generalLedger.statisticsCodeID(detailDTO.getStatisticsCodeID());
                        generalLedger.expenseItemID(detailDTO.getExpenseItemID());
                        generalLedgers.add(generalLedger);

                        GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                        BeanUtils.copyProperties(generalLedgerCorresponding, generalLedger);
                        generalLedgerCorresponding.setTypeID(parent.getTypeID());
                        generalLedgerCorresponding.account(detailDTO.getCreditAccount());
                        generalLedgerCorresponding.accountCorresponding(detailDTO.getDebitAccount());
                        generalLedgerCorresponding.creditAmount(detailDTO.getAmount().subtract(detailDTO.getExportTaxAmount() == null ? BigDecimal.ZERO : detailDTO.getExportTaxAmount()));
                        generalLedgerCorresponding.creditAmountOriginal(detailDTO.getAmountOriginal().subtract(detailDTO.getExportTaxAmount() == null ? BigDecimal.ZERO : detailDTO.getExportTaxAmount().divide(parent.getExchangeRate())));
                        generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                        generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);
                        generalLedgers.add(generalLedgerCorresponding);
                        if (detailDTO.getDiscountAmountOriginal() != null && detailDTO.getDiscountAmountOriginal().doubleValue() != 0 && detailDTO.getDiscountAccount() != null) {
                            GeneralLedger generalLedger3 = new GeneralLedger();
                            BeanUtils.copyProperties(generalLedger3, parent);
                            generalLedger3.setTypeID(parent.getTypeID());
                            generalLedger3.account(detailDTO.getCreditAccount());
                            generalLedger3.accountCorresponding(detailDTO.getDiscountAccount());
                            generalLedger3.debitAmount(detailDTO.getDiscountAmount());
                            generalLedger3.debitAmountOriginal(detailDTO.getDiscountAmountOriginal());
                            generalLedger3.creditAmount(BigDecimal.ZERO);
                            generalLedger3.creditAmountOriginal(BigDecimal.ZERO);
                            generalLedger3.description(detailDTO.getDescription());
                            generalLedger3.accountingObjectID(detailDTO.getAccountingObjectID());
                            generalLedger3.accountingObjectName(detailDTO.getAccountingObjectName());
                            generalLedger3.accountingObjectCode(detailDTO.getAccountingObjectCode());
                            generalLedger3.setReferenceID(parent.getRefID());
                            generalLedger3.employeeID(parent.getEmployeeID());
                            generalLedger3.setEmployeeCode(parent.getEmployeeCode());
                            generalLedger3.setEmployeeName(parent.getEmployeeName());
                            generalLedger3.detailID(detailDTO.getRefID());

                            generalLedger3.materialGoodsID(detailDTO.getMaterialGoodsID());
                            generalLedger3.materialGoodsCode(detailDTO.getMaterialGoodsCode());
                            generalLedger3.materialGoodsName(detailDTO.getMaterialGoodsName());
                            generalLedger3.repositoryID(detailDTO.getRepositoryID());
                            generalLedger3.repositoryCode(detailDTO.getRepositoryCode());
                            generalLedger3.repositoryName(detailDTO.getRepositoryName());

                            generalLedger3.setUnitID(detailDTO.getUnitID());
                            generalLedger3.setQuantity(detailDTO.getQuantity());
                            generalLedger3.setUnitPrice(detailDTO.getUnitPrice());
                            generalLedger3.setUnitPriceOriginal(detailDTO.getUnitPriceOriginal());
                            generalLedger3.setMainUnitID(detailDTO.getMainUnitID());
                            generalLedger3.setMainQuantity(detailDTO.getMainQuantity());
                            generalLedger3.setMainUnitPrice(detailDTO.getMainUnitPrice());
                            generalLedger3.setMainConvertRate(detailDTO.getMainConvertRate());
                            generalLedger3.setFormula(detailDTO.getFormula());

                            generalLedger3.refDateTime(parent.getDate());
                            generalLedger3.budgetItemID(detailDTO.getBudgetItemID());
                            generalLedger3.costSetID(detailDTO.getCostSetID());
                            generalLedger3.contractID(detailDTO.getContractID());
                            generalLedger3.statisticsCodeID(detailDTO.getStatisticsCodeID());
                            generalLedger3.expenseItemID(detailDTO.getExpenseItemID());
                            generalLedgers.add(generalLedger3);

                            GeneralLedger generalLedgerCorresponding3 = new GeneralLedger();
                            BeanUtils.copyProperties(generalLedgerCorresponding3, generalLedger3);
                            generalLedgerCorresponding3.setTypeID(parent.getTypeID());
                            generalLedgerCorresponding3.account(detailDTO.getDiscountAccount());
                            generalLedgerCorresponding3.accountCorresponding(detailDTO.getCreditAccount());
                            generalLedgerCorresponding3.creditAmount(detailDTO.getDiscountAmount());
                            generalLedgerCorresponding3.creditAmountOriginal(detailDTO.getDiscountAmountOriginal());
                            generalLedgerCorresponding3.debitAmount(BigDecimal.ZERO);
                            generalLedgerCorresponding3.debitAmountOriginal(BigDecimal.ZERO);
                            generalLedgers.add(generalLedgerCorresponding3);
                        }
                        if (detailDTO.getvATAmountOriginal() != null && detailDTO.getvATAmountOriginal().doubleValue() != 0) {
                            GeneralLedger generalLedger2 = new GeneralLedger();
                            BeanUtils.copyProperties(generalLedger2, parent);
                            generalLedger2.setTypeID(parent.getTypeID());
                            generalLedger2.account(detailDTO.getvATAccount());
                            generalLedger2.accountCorresponding(detailDTO.getDeductionDebitAccount());
                            generalLedger2.debitAmount(detailDTO.getvATAmount());
                            generalLedger2.debitAmountOriginal(detailDTO.getvATAmountOriginal());
                            generalLedger2.creditAmount(BigDecimal.ZERO);
                            generalLedger2.creditAmountOriginal(BigDecimal.ZERO);
                            generalLedger2.description(detailDTO.getVatDescription());
                            generalLedger2.accountingObjectID(detailDTO.getAccountingObjectID());
                            generalLedger2.accountingObjectName(detailDTO.getAccountingObjectName());
                            generalLedger2.accountingObjectCode(detailDTO.getAccountingObjectCode());
                            generalLedger2.setReferenceID(parent.getRefID());
                            generalLedger2.employeeID(parent.getEmployeeID());
                            generalLedger2.setEmployeeCode(parent.getEmployeeCode());
                            generalLedger2.setEmployeeName(parent.getEmployeeName());
                            ;
                            generalLedger2.detailID(detailDTO.getRefID());

                            generalLedger2.setUnitID(detailDTO.getUnitID());
                            generalLedger2.setQuantity(detailDTO.getQuantity());
                            generalLedger2.setUnitPrice(detailDTO.getUnitPrice());
                            generalLedger2.setUnitPriceOriginal(detailDTO.getUnitPriceOriginal());
                            generalLedger2.setMainUnitID(detailDTO.getMainUnitID());
                            generalLedger2.setMainQuantity(detailDTO.getMainQuantity());
                            generalLedger2.setMainUnitPrice(detailDTO.getMainUnitPrice());
                            generalLedger2.setMainConvertRate(detailDTO.getMainConvertRate());
                            generalLedger2.setFormula(detailDTO.getFormula());

                            generalLedger2.materialGoodsID(detailDTO.getMaterialGoodsID());
                            generalLedger2.materialGoodsCode(detailDTO.getMaterialGoodsCode());
                            generalLedger2.materialGoodsName(detailDTO.getMaterialGoodsName());
                            generalLedger2.repositoryID(detailDTO.getRepositoryID());
                            generalLedger2.repositoryCode(detailDTO.getRepositoryCode());
                            generalLedger2.repositoryName(detailDTO.getRepositoryName());

                            generalLedger2.refDateTime(parent.getDate());
                            generalLedger2.budgetItemID(detailDTO.getBudgetItemID());
                            generalLedger2.costSetID(detailDTO.getCostSetID());
                            generalLedger2.contractID(detailDTO.getContractID());
                            generalLedger2.statisticsCodeID(detailDTO.getStatisticsCodeID());
                            generalLedger2.expenseItemID(detailDTO.getExpenseItemID());
                            generalLedgers.add(generalLedger2);

                            GeneralLedger generalLedgerCorresponding2 = new GeneralLedger();
                            BeanUtils.copyProperties(generalLedgerCorresponding2, generalLedger2);
                            generalLedgerCorresponding2.setTypeID(parent.getTypeID());
                            generalLedgerCorresponding2.account(detailDTO.getDeductionDebitAccount());
                            generalLedgerCorresponding2.accountCorresponding(detailDTO.getvATAccount());
                            generalLedgerCorresponding2.creditAmount(detailDTO.getvATAmount());
                            generalLedgerCorresponding2.creditAmountOriginal(detailDTO.getvATAmountOriginal());
                            generalLedgerCorresponding2.debitAmount(BigDecimal.ZERO);
                            generalLedgerCorresponding2.debitAmountOriginal(BigDecimal.ZERO);
                            generalLedgers.add(generalLedgerCorresponding2);
                        }
                        if (Boolean.TRUE.equals(parent.getDeliveryVoucher())) {
                            if (rSInwardOutward.isPresent()) {
                                GeneralLedger generalLedger4 = new GeneralLedger();
                                BeanUtils.copyProperties(generalLedger4, parent);
                                generalLedger4.setTypeID(TypeConstant.RS_INWARD_OUTWARD);
                                if (rSInwardOutward.get().getNoFBook() != null) {
                                    generalLedger4.setNoFBook(rSInwardOutward.get().getNoFBook());
                                }
                                if (rSInwardOutward.get().getNoMBook() != null) {
                                    generalLedger4.setNoMBook(rSInwardOutward.get().getNoMBook());
                                }
                                generalLedger4.account(detailDTO.getCostAccount());
                                generalLedger4.accountCorresponding(detailDTO.getRepositoryAccount());
                                generalLedger4.debitAmount(detailDTO.getoWAmount());
                                generalLedger4.debitAmountOriginal(detailDTO.getoWAmount());
                                generalLedger4.creditAmount(BigDecimal.ZERO);
                                generalLedger4.creditAmountOriginal(BigDecimal.ZERO);
                                generalLedger4.description(detailDTO.getDescription());
                                generalLedger4.accountingObjectID(detailDTO.getAccountingObjectID());
                                generalLedger4.accountingObjectName(detailDTO.getAccountingObjectName());
                                generalLedger4.accountingObjectCode(detailDTO.getAccountingObjectCode());
                                generalLedger4.setReferenceID(parent.getRefID());
                                generalLedger4.employeeID(parent.getEmployeeID());
                                generalLedger4.setEmployeeCode(parent.getEmployeeCode());
                                generalLedger4.setEmployeeName(parent.getEmployeeName());
                                generalLedger4.setReferenceID(parent.getRefID());
                                generalLedger4.employeeID(parent.getEmployeeID());
                                generalLedger4.detailID(detailDTO.getRefID());

                                generalLedger4.materialGoodsID(detailDTO.getMaterialGoodsID());
                                generalLedger4.materialGoodsCode(detailDTO.getMaterialGoodsCode());
                                generalLedger4.materialGoodsName(detailDTO.getMaterialGoodsName());
                                generalLedger4.repositoryID(detailDTO.getRepositoryID());
                                generalLedger4.repositoryCode(detailDTO.getRepositoryCode());
                                generalLedger4.repositoryName(detailDTO.getRepositoryName());

                                generalLedger4.setUnitID(detailDTO.getUnitID());
                                generalLedger4.setQuantity(detailDTO.getQuantity());
                                generalLedger4.setUnitPrice(detailDTO.getUnitPrice());
                                generalLedger4.setUnitPriceOriginal(detailDTO.getUnitPriceOriginal());
                                generalLedger4.setMainUnitID(detailDTO.getMainUnitID());
                                generalLedger4.setMainQuantity(detailDTO.getMainQuantity());
                                generalLedger4.setMainUnitPrice(detailDTO.getMainUnitPrice());
                                generalLedger4.setMainConvertRate(detailDTO.getMainConvertRate());
                                generalLedger4.setFormula(detailDTO.getFormula());

                                generalLedger4.refDateTime(parent.getDate());
                                generalLedger4.budgetItemID(detailDTO.getBudgetItemID());
                                generalLedger4.costSetID(detailDTO.getCostSetID());
                                generalLedger4.contractID(detailDTO.getContractID());
                                generalLedger4.statisticsCodeID(detailDTO.getStatisticsCodeID());
                                generalLedger4.expenseItemID(detailDTO.getExpenseItemID());
                                generalLedgers.add(generalLedger4);

                                GeneralLedger generalLedgerCorresponding4 = new GeneralLedger();
                                BeanUtils.copyProperties(generalLedgerCorresponding4, generalLedger4);
                                generalLedgerCorresponding4.setTypeID(TypeConstant.RS_INWARD_OUTWARD);
                                generalLedgerCorresponding4.account(detailDTO.getRepositoryAccount());
                                generalLedgerCorresponding4.accountCorresponding(detailDTO.getCostAccount());
                                generalLedgerCorresponding4.creditAmount(detailDTO.getoWAmount());
                                generalLedgerCorresponding4.creditAmountOriginal(detailDTO.getoWAmount());
                                generalLedgerCorresponding4.debitAmount(BigDecimal.ZERO);
                                generalLedgerCorresponding4.debitAmountOriginal(BigDecimal.ZERO);
                                generalLedgers.add(generalLedgerCorresponding4);
                            }
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return generalLedgers;
    }

    /*Bán hàng*/
    List<RepositoryLedger> repositoryLedgersSAReturn
    (List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail, List<MGForPPOrderConvertDTO> mgForPPOrderConvertDTOS, Boolean checkQuaSLTon) {
        List<RepositoryLedger> repositoryLedgers = new ArrayList<>();
        List<ViewVoucherNo> listCheck = new ArrayList<>();
        listCheck.addAll(viewVoucherNos);
        for (ViewVoucherNo parent : listCheck) {
            boolean checkContinue = true;
            if (Boolean.TRUE.equals(parent.getDeliveryVoucher()) && checkQuaSLTon) {
                List<CheckQuantityExistsConvertDTO> details = new ArrayList<>();
                for (ViewVoucherNoDetailDTO item : parent.getViewVoucherNoDetailDTOS()) {
                    details.add(new CheckQuantityExistsConvertDTO(item.getMaterialGoodsID(), item.getMainQuantity()));
                }
                CheckQuantityExistsDTO checkQuantityExistsDTO = utilsService.checkQuantityBalance(details, mgForPPOrderConvertDTOS);
                if (checkQuantityExistsDTO.getQuantityExists() != null && checkQuantityExistsDTO.getQuantityExists().length() > 0) {
                    parent.setReasonFail("VTHH " + checkQuantityExistsDTO.getQuantityExists() + " xuất quá số tồn!");
                    lstFail.add(parent);
                    viewVoucherNos.remove(parent);
                    checkContinue = false;
                }
            }
            if (checkContinue) {
                RSInwardOutwardSearchDTO rSInwardOutward = new RSInwardOutwardSearchDTO();
                if (Boolean.TRUE.equals(parent.getDeliveryVoucher())) {
                    rSInwardOutward = rSInwardOutwardRepository.findByID(parent.getrSInwardOutwardID());
                }
                for (ViewVoucherNoDetailDTO detailDTO : parent.getViewVoucherNoDetailDTOS()) {
                    if (Boolean.TRUE.equals(parent.getDeliveryVoucher()) && detailDTO.getRepositoryID() != null) {
                        if (rSInwardOutward != null) {
                            RepositoryLedger repositoryLedgerItem = new RepositoryLedger();
                            repositoryLedgerItem.setReferenceID(rSInwardOutward.getId());
                            repositoryLedgerItem.setCompanyID(parent.getCompanyID());
                            if (rSInwardOutward.getNoFBook() != null) {
                                repositoryLedgerItem.setNoFBook(rSInwardOutward.getNoFBook());
                            }
                            if (rSInwardOutward.getNoMBook() != null) {
                                repositoryLedgerItem.setNoMBook(rSInwardOutward.getNoMBook());
                            }
                            repositoryLedgerItem.setTypeID(rSInwardOutward.getTypeID());
                            repositoryLedgerItem.setIwQuantity(detailDTO.getQuantity());
                            repositoryLedgerItem.setUnitPrice(detailDTO.getoWPrice());
                            repositoryLedgerItem.setIwAmount(detailDTO.getoWAmount());
                            repositoryLedgerItem.setAccount(detailDTO.getRepositoryAccount());
                            repositoryLedgerItem.setAccountCorresponding(detailDTO.getCostAccount());
                            repositoryLedgerItem.setDetailID(detailDTO.getRefID());
                            repositoryLedgerItem.setFormula(detailDTO.getFormula());
                            repositoryLedgerItem.setMaterialGoodsID(detailDTO.getMaterialGoodsID());
                            repositoryLedgerItem.setRepositoryID(detailDTO.getRepositoryID());
                            repositoryLedgerItem.setLotNo(detailDTO.getLotNo());
                            repositoryLedgerItem.setDescription(detailDTO.getDescription());
                            repositoryLedgerItem.setReason(rSInwardOutward.getReason());
                            repositoryLedgerItem.setDate(rSInwardOutward.getDate());
                            repositoryLedgerItem.setPostedDate(rSInwardOutward.getPostedDate());
                            repositoryLedgerItem.setExpiryDate(detailDTO.getExpiryDate());
                            repositoryLedgerItem.setTypeLedger(rSInwardOutward.getTypeLedger());
                            repositoryLedgerItem.setUnitID(detailDTO.getUnitID());
                            repositoryLedgerItem.setMainUnitID(detailDTO.getMainUnitID());
                            repositoryLedgerItem.setMainUnitPrice(detailDTO.getMainUnitPrice());
                            repositoryLedgerItem.setMainOWQuantity(detailDTO.getMainQuantity());
                            repositoryLedgerItem.setMainConvertRate(detailDTO.getMainConvertRate());
                            repositoryLedgerItem.setBudgetItemID(detailDTO.getBudgetItemID());
                            repositoryLedgerItem.setCostSetID(detailDTO.getCostSetID());
                            repositoryLedgerItem.setStatisticsCodeID(detailDTO.getStatisticsCodeID());
                            repositoryLedgerItem.setExpenseItemID(detailDTO.getExpenseItemID());
                            repositoryLedgerItem.setRepositoryCode(detailDTO.getRepositoryCode());
                            repositoryLedgerItem.setRepositoryName(detailDTO.getRepositoryName());
                            repositoryLedgerItem.setMaterialGoodsCode(detailDTO.getMaterialGoodsCode());
                            repositoryLedgerItem.setMaterialGoodsName(detailDTO.getMaterialGoodsName());
                            repositoryLedgers.add(repositoryLedgerItem);
                        }
                    }
                }
            }
        }
        return repositoryLedgers;
    }

    /*Nhập kho by huypq*/
    List<GeneralLedger> genGeneralLedgersInWard
    (List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail) {
        List<GeneralLedger> generalLedgers = new ArrayList<>();
        for (ViewVoucherNo parent : viewVoucherNos) {
            for (ViewVoucherNoDetailDTO detailDTO : parent.getViewVoucherNoDetailDTOS()) {
                GeneralLedger generalLedger = new GeneralLedger();
                try {
                    BeanUtils.copyProperties(generalLedger, parent);
                    generalLedger.setReferenceID(parent.getRefID());
                    generalLedger.setDetailID(detailDTO.getRefID());
                    generalLedger.setTypeID(parent.getTypeID());
                    generalLedger.account(detailDTO.getDebitAccount());
                    generalLedger.accountCorresponding(detailDTO.getCreditAccount());
                    generalLedger.debitAmount(detailDTO.getAmount());
                    generalLedger.debitAmountOriginal(detailDTO.getAmountOriginal());
                    generalLedger.creditAmount(BigDecimal.ZERO);
                    generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                    generalLedger.description(detailDTO.getDescription());
                    generalLedger.accountingObjectID(parent.getAccountingObjectID());
                    generalLedger.accountingObjectName(parent.getAccountingObjectName());
                    generalLedger.accountingObjectCode(parent.getAccountingObjectCode());
                    generalLedger.employeeID(parent.getEmployeeID());
                    generalLedger.setEmployeeCode(parent.getEmployeeCode());
                    generalLedger.setEmployeeName(parent.getEmployeeName());
                    generalLedger.materialGoodsID(detailDTO.getMaterialGoodsID());
                    generalLedger.materialGoodsCode(detailDTO.getMaterialGoodsCode());
                    generalLedger.materialGoodsName(detailDTO.getMaterialGoodsName());
                    generalLedger.repositoryID(detailDTO.getRepositoryID());
                    generalLedger.repositoryCode(detailDTO.getRepositoryCode());
                    generalLedger.repositoryName(detailDTO.getRepositoryName());
                    generalLedger.unitID(detailDTO.getUnitID());
                    generalLedger.quantity(detailDTO.getQuantity());
                    generalLedger.unitPrice(detailDTO.getUnitPrice());
                    generalLedger.unitPriceOriginal(detailDTO.getUnitPriceOriginal());
                    generalLedger.mainUnitPrice(detailDTO.getMainUnitPrice());
                    generalLedger.mainQuantity(detailDTO.getMainQuantity());
                    generalLedger.mainConvertRate(detailDTO.getMainConvertRate());
                    generalLedger.formula(detailDTO.getFormula());
                    generalLedger.departmentID(detailDTO.getDepartmentID());
                    generalLedger.mainUnitID(detailDTO.getMainUnitID());
                    generalLedger.orderPriority(detailDTO.getOrderPriority());
                    generalLedger.refDateTime(parent.getDate());
                    generalLedger.setNoFBook(parent.getNoFBook());
                    generalLedger.setNoMBook(parent.getNoMBook());
                    generalLedger.budgetItemID(detailDTO.getBudgetItemID());
                    generalLedger.costSetID(detailDTO.getCostSetID());
                    generalLedger.contractID(detailDTO.getContractID());
                    generalLedger.statisticsCodeID(detailDTO.getStatisticsCodeID());
                    generalLedger.expenseItemID(detailDTO.getExpenseItemID());
                    generalLedgers.add(generalLedger);

                    GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                    BeanUtils.copyProperties(generalLedgerCorresponding, generalLedger);
                    generalLedgerCorresponding.referenceID(parent.getRefID());
                    generalLedgerCorresponding.detailID(detailDTO.getRefID());
                    generalLedgerCorresponding.setTypeID(parent.getTypeID());
                    generalLedgerCorresponding.account(detailDTO.getCreditAccount());
                    generalLedgerCorresponding.accountCorresponding(detailDTO.getDebitAccount());
                    generalLedgerCorresponding.creditAmount(detailDTO.getAmount());
                    generalLedgerCorresponding.creditAmountOriginal(detailDTO.getAmountOriginal());
                    generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                    generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);

                    generalLedgers.add(generalLedgerCorresponding);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return generalLedgers;
    }

    /*Nhập kho by huypq*/
    List<RepositoryLedger> repositoryLedgersInWard
    (List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail) {
        List<RepositoryLedger> repositoryLedgers = new ArrayList<>();
        for (ViewVoucherNo parent : viewVoucherNos) {
            for (ViewVoucherNoDetailDTO detailDTO : parent.getViewVoucherNoDetailDTOS()) {
                RepositoryLedger repositoryLedgerItem = new RepositoryLedger();
                try {
                    BeanUtils.copyProperties(repositoryLedgerItem, parent);
                    repositoryLedgerItem.setTypeID(parent.getTypeID());
                    repositoryLedgerItem.setDetailID(detailDTO.getRefID());
                    repositoryLedgerItem.setReferenceID(parent.getRefID());
                    repositoryLedgerItem.setCompanyID(parent.getCompanyID());
                    repositoryLedgerItem.setNoFBook(parent.getNoFBook());
                    repositoryLedgerItem.setNoMBook(parent.getNoMBook());
                    repositoryLedgerItem.setIwQuantity(detailDTO.getQuantity());
                    repositoryLedgerItem.setIwAmount(detailDTO.getAmount());
                    repositoryLedgerItem.setMainIWQuantity(detailDTO.getMainQuantity());
                    repositoryLedgerItem.setUnitPrice(detailDTO.getUnitPrice());
                    repositoryLedgerItem.setAccount(detailDTO.getDebitAccount());
                    repositoryLedgerItem.setAccountCorresponding(detailDTO.getCreditAccount());
                    repositoryLedgerItem.setFormula(detailDTO.getFormula());
                    repositoryLedgerItem.setMaterialGoodsID(detailDTO.getMaterialGoodsID());
                    repositoryLedgerItem.setRepositoryID(detailDTO.getRepositoryID());
                    repositoryLedgerItem.setLotNo(detailDTO.getLotNo());
                    repositoryLedgerItem.setDescription(detailDTO.getDescription());
                    repositoryLedgerItem.setReason(parent.getReason());
                    repositoryLedgerItem.setDate(parent.getDate());
                    repositoryLedgerItem.setPostedDate(parent.getPostedDate());
                    repositoryLedgerItem.setExpiryDate(detailDTO.getExpiryDate());
                    repositoryLedgerItem.setTypeLedger(parent.getTypeLedger());
                    repositoryLedgerItem.setUnitID(detailDTO.getUnitID());
                    repositoryLedgerItem.setMainUnitID(detailDTO.getMainUnitID());
                    repositoryLedgerItem.setMainUnitPrice(detailDTO.getMainUnitPrice());
                    repositoryLedgerItem.setMainConvertRate(detailDTO.getMainConvertRate());
                    repositoryLedgerItem.setBudgetItemID(detailDTO.getBudgetItemID());
                    repositoryLedgerItem.setCostSetID(detailDTO.getCostSetID());
                    repositoryLedgerItem.setStatisticsCodeID(detailDTO.getStatisticsCodeID());
                    repositoryLedgerItem.setExpenseItemID(detailDTO.getExpenseItemID());
                    repositoryLedgerItem.setRepositoryCode(detailDTO.getRepositoryCode());
                    repositoryLedgerItem.setRepositoryName(detailDTO.getRepositoryName());
                    repositoryLedgerItem.setMaterialGoodsCode(detailDTO.getMaterialGoodsCode());
                    repositoryLedgerItem.setMaterialGoodsName(detailDTO.getMaterialGoodsName());
                    repositoryLedgers.add(repositoryLedgerItem);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return repositoryLedgers;
    }

    /*Xuất kho by huypq*/
    List<GeneralLedger> genGeneralLedgersOutWard
    (List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail) {
        List<GeneralLedger> generalLedgers = new ArrayList<>();
        for (ViewVoucherNo parent : viewVoucherNos) {
            for (ViewVoucherNoDetailDTO detailDTO : parent.getViewVoucherNoDetailDTOS()) {
                GeneralLedger generalLedger = new GeneralLedger();
                try {
                    BeanUtils.copyProperties(generalLedger, parent);
                    generalLedger.setReferenceID(parent.getRefID());
                    generalLedger.setDetailID(detailDTO.getRefID());
                    generalLedger.setTypeID(parent.getTypeID());
                    generalLedger.account(detailDTO.getDebitAccount());
                    generalLedger.accountCorresponding(detailDTO.getCreditAccount());
                    generalLedger.debitAmount(detailDTO.getAmount());
                    generalLedger.debitAmountOriginal(detailDTO.getAmountOriginal());
                    generalLedger.creditAmount(BigDecimal.ZERO);
                    generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                    generalLedger.description(detailDTO.getDescription());
                    generalLedger.setEmployeeCode(parent.getAccountingObjectCode());
                    generalLedger.setEmployeeName(parent.getAccountingObjectName());
                    generalLedger.employeeID(parent.getEmployeeID());
                    generalLedger.setEmployeeCode(parent.getEmployeeCode());
                    generalLedger.setEmployeeName(parent.getEmployeeName());
                    generalLedger.materialGoodsID(detailDTO.getMaterialGoodsID());
                    generalLedger.materialGoodsCode(detailDTO.getMaterialGoodsCode());
                    generalLedger.materialGoodsName(detailDTO.getMaterialGoodsName());
                    generalLedger.repositoryID(detailDTO.getRepositoryID());
                    generalLedger.repositoryCode(detailDTO.getRepositoryCode());
                    generalLedger.repositoryName(detailDTO.getRepositoryName());
                    generalLedger.unitID(detailDTO.getUnitID());
                    generalLedger.quantity(detailDTO.getQuantity());
                    generalLedger.unitPrice(detailDTO.getUnitPrice());
                    generalLedger.mainUnitPrice(detailDTO.getMainUnitPrice());
                    generalLedger.unitPriceOriginal(detailDTO.getUnitPriceOriginal());
                    generalLedger.mainQuantity(detailDTO.getMainQuantity());
                    generalLedger.mainConvertRate(detailDTO.getMainConvertRate());
                    generalLedger.formula(detailDTO.getFormula());
                    generalLedger.departmentID(detailDTO.getDepartmentID());
                    generalLedger.mainUnitID(detailDTO.getMainUnitID());
                    generalLedger.orderPriority(detailDTO.getOrderPriority());
                    generalLedger.refDateTime(parent.getDate());
                    generalLedger.setNoFBook(parent.getNoFBook());
                    generalLedger.setNoMBook(parent.getNoMBook());
                    generalLedger.budgetItemID(detailDTO.getBudgetItemID());
                    generalLedger.costSetID(detailDTO.getCostSetID());
                    generalLedger.contractID(detailDTO.getContractID());
                    generalLedger.statisticsCodeID(detailDTO.getStatisticsCodeID());
                    generalLedger.expenseItemID(detailDTO.getExpenseItemID());

                    generalLedgers.add(generalLedger);

                    GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                    BeanUtils.copyProperties(generalLedgerCorresponding, generalLedger);
                    generalLedgerCorresponding.referenceID(parent.getRefID());
                    generalLedgerCorresponding.detailID(detailDTO.getRefID());
                    generalLedgerCorresponding.setTypeID(parent.getTypeID());
                    generalLedgerCorresponding.account(detailDTO.getCreditAccount());
                    generalLedgerCorresponding.accountCorresponding(detailDTO.getDebitAccount());
                    generalLedgerCorresponding.creditAmount(detailDTO.getAmount());
                    generalLedgerCorresponding.creditAmountOriginal(detailDTO.getAmountOriginal());
                    generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                    generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);

                    generalLedgers.add(generalLedgerCorresponding);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return generalLedgers;
    }

    /*Xuất kho by huypq*/
    List<RepositoryLedger> repositoryLedgersOutWard
    (List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail) {
        List<RepositoryLedger> repositoryLedgers = new ArrayList<>();
        for (ViewVoucherNo parent : viewVoucherNos) {
            for (ViewVoucherNoDetailDTO detailDTO : parent.getViewVoucherNoDetailDTOS()) {
                RepositoryLedger repositoryLedgerItem = new RepositoryLedger();
                try {
                    BeanUtils.copyProperties(repositoryLedgerItem, parent);
                    repositoryLedgerItem.setTypeID(parent.getTypeID());
                    repositoryLedgerItem.setReferenceID(parent.getRefID());
                    repositoryLedgerItem.setDetailID(detailDTO.getRefID());
                    repositoryLedgerItem.setCompanyID(parent.getCompanyID());
                    repositoryLedgerItem.setNoFBook(parent.getNoFBook());
                    repositoryLedgerItem.setNoMBook(parent.getNoMBook());
                    repositoryLedgerItem.setOwQuantity(detailDTO.getQuantity());
                    repositoryLedgerItem.setOwAmount(detailDTO.getAmount());
                    repositoryLedgerItem.setMainOWQuantity(detailDTO.getMainQuantity());
                    repositoryLedgerItem.setUnitPrice(detailDTO.getUnitPrice());
                    repositoryLedgerItem.setAccount(detailDTO.getCreditAccount());
                    repositoryLedgerItem.setAccountCorresponding(detailDTO.getDebitAccount());
                    repositoryLedgerItem.setFormula(detailDTO.getFormula());
                    repositoryLedgerItem.setMaterialGoodsID(detailDTO.getMaterialGoodsID());
                    repositoryLedgerItem.setRepositoryID(detailDTO.getRepositoryID());
                    repositoryLedgerItem.setLotNo(detailDTO.getLotNo());
                    repositoryLedgerItem.setReason(parent.getReason());
                    repositoryLedgerItem.setDate(parent.getDate());
                    repositoryLedgerItem.setPostedDate(parent.getPostedDate());
                    repositoryLedgerItem.setExpiryDate(detailDTO.getExpiryDate());
                    repositoryLedgerItem.setTypeLedger(parent.getTypeLedger());
                    repositoryLedgerItem.setUnitID(detailDTO.getUnitID());
                    repositoryLedgerItem.setMainUnitID(detailDTO.getMainUnitID());
                    repositoryLedgerItem.setMainUnitPrice(detailDTO.getMainUnitPrice());
                    repositoryLedgerItem.setMainConvertRate(detailDTO.getMainConvertRate());
                    repositoryLedgerItem.setBudgetItemID(detailDTO.getBudgetItemID());
                    repositoryLedgerItem.setCostSetID(detailDTO.getCostSetID());
                    repositoryLedgerItem.setStatisticsCodeID(detailDTO.getStatisticsCodeID());
                    repositoryLedgerItem.setExpenseItemID(detailDTO.getExpenseItemID());
                    repositoryLedgerItem.setConfrontID(detailDTO.getConfrontID());
                    repositoryLedgerItem.setConfrontDetailID(detailDTO.getConfrontDetailID());
                    if (detailDTO.getUnitID() == null) {
                        repositoryLedgerItem.setMainUnitPrice(repositoryLedgerItem.getUnitPrice());
                        repositoryLedgerItem.setMainOWQuantity(repositoryLedgerItem.getOwQuantity());
                        repositoryLedgerItem.setMainConvertRate(new BigDecimal(1));
                        repositoryLedgerItem.setFormula("*");
                    }
                    Optional<Repository> repository = repositoryRepository.findById(detailDTO.getRepositoryID());
                    // repository
                    if (repository.isPresent()) {
                        repositoryLedgerItem.setRepositoryCode(repository.get().getRepositoryCode());
                        repositoryLedgerItem.setRepositoryName(repository.get().getRepositoryName());
                    }
                    // materialGoods
                    Optional<MaterialGoods> materialGoods = materialGoodsRepository.findById(detailDTO.getMaterialGoodsID());
                    if (materialGoods.isPresent()) {
                        repositoryLedgerItem.setMaterialGoodsCode(materialGoods.get().getMaterialGoodsCode());
                        repositoryLedgerItem.setMaterialGoodsName(materialGoods.get().getMaterialGoodsName());
                    }
                    repositoryLedgers.add(repositoryLedgerItem);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return repositoryLedgers;
    }

    /*Chuyển kho by huypq*/
    List<GeneralLedger> genGeneralLedgersRSTransfer
    (List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail) {
        List<GeneralLedger> generalLedgers = new ArrayList<>();
        for (ViewVoucherNo parent : viewVoucherNos) {
            for (ViewVoucherNoDetailDTO detailDTO : parent.getViewVoucherNoDetailDTOS()) {
                GeneralLedger generalLedger = new GeneralLedger();
                try {
                    BeanUtils.copyProperties(generalLedger, parent);
                    generalLedger.setReferenceID(parent.getRefID());
                    generalLedger.setDetailID(detailDTO.getRefID());
                    generalLedger.setTypeID(parent.getTypeID());
                    generalLedger.account(detailDTO.getDebitAccount());
                    generalLedger.accountCorresponding(detailDTO.getCreditAccount());
                    generalLedger.debitAmount(detailDTO.getoWAmount());
                    generalLedger.debitAmountOriginal(detailDTO.getoWAmount());
                    generalLedger.creditAmount(BigDecimal.ZERO);
                    generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                    generalLedger.description(detailDTO.getDescription());

                    Optional<AccountingObject> accountingObject = accountingObjectRepository.findOneById(parent.getAccountingObjectID());
                    accountingObject.ifPresent(e -> {
                        generalLedger.accountingObjectCode(e.getAccountingObjectCode());
                        generalLedger.accountingObjectName(e.getAccountingObjectName());
                    });
                    generalLedger.employeeID(parent.getEmployeeID());
                    Optional<AccountingObject> employee = accountingObjectRepository.findOneById(parent.getEmployeeID());
                    employee.ifPresent(e -> {
                        generalLedger.employeeCode(e.getAccountingObjectCode());
                        generalLedger.employeeName(e.getAccountingObjectName());
                    });
                    Optional<Repository> repository = repositoryRepository.findById(detailDTO.getToRepositoryID());
                    // repository
                    if (repository.isPresent()) {
                        generalLedger.setRepositoryCode(repository.get().getRepositoryCode());
                        generalLedger.setRepositoryName(repository.get().getRepositoryName());
                    }
                    generalLedger.setRepositoryID(detailDTO.getToRepositoryID());
                    generalLedger.materialGoodsID(detailDTO.getMaterialGoodsID());
                    generalLedger.materialGoodsCode(detailDTO.getMaterialGoodsCode());
                    generalLedger.materialGoodsName(detailDTO.getMaterialGoodsName());
                    generalLedger.unitID(detailDTO.getUnitID());
                    generalLedger.quantity(detailDTO.getQuantity());
                    generalLedger.unitPrice(detailDTO.getoWPrice());
                    generalLedger.mainUnitPrice(detailDTO.getoWPrice());
                    generalLedger.unitPriceOriginal(detailDTO.getoWPrice());
                    generalLedger.mainQuantity(detailDTO.getMainQuantity());
                    generalLedger.mainConvertRate(detailDTO.getMainConvertRate());
                    generalLedger.formula(detailDTO.getFormula());
                    generalLedger.departmentID(detailDTO.getDepartmentID());
                    generalLedger.mainUnitID(detailDTO.getMainUnitID());
                    generalLedger.orderPriority(detailDTO.getOrderPriority());
                    generalLedger.refDateTime(parent.getDate());
                    generalLedger.invoiceDate(parent.getInvoiceDate());
                    generalLedger.setNoFBook(parent.getNoFBook());
                    generalLedger.setNoMBook(parent.getNoMBook());
                    generalLedger.budgetItemID(detailDTO.getBudgetItemID());
                    generalLedger.costSetID(detailDTO.getCostSetID());
                    generalLedger.statisticsCodeID(detailDTO.getStatisticsCodeID());
                    generalLedger.expenseItemID(detailDTO.getExpenseItemID());
                    UserDTO userDTO = userService.getAccount();
                    Optional<SystemOption> first = userDTO.getSystemOption().stream().filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.TCKHAC_SDSOQUANTRI)).findFirst();
                    if (first.isPresent() && first.get().getData().equals(Constants.SystemOption.Zero)) {
                        generalLedger.setNoMBook(null);
                        generalLedger.setTypeLedger(Constants.TypeLedger.FINANCIAL_BOOK);
                    }

                    generalLedgers.add(generalLedger);

                    GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                    BeanUtils.copyProperties(generalLedgerCorresponding, generalLedger);
                    generalLedgerCorresponding.referenceID(parent.getRefID());
                    generalLedgerCorresponding.detailID(detailDTO.getRefID());
                    generalLedgerCorresponding.setTypeID(parent.getTypeID());
                    generalLedgerCorresponding.account(detailDTO.getCreditAccount());
                    generalLedgerCorresponding.accountCorresponding(detailDTO.getDebitAccount());
                    generalLedgerCorresponding.creditAmount(detailDTO.getoWAmount());
                    generalLedgerCorresponding.creditAmountOriginal(detailDTO.getoWAmount());
                    generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                    generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);
                    Optional<Repository> repository3 = repositoryRepository.findById(detailDTO.getFromRepositoryID());
                    // repository
                    if (repository3.isPresent()) {
                        generalLedgerCorresponding.setRepositoryCode(repository3.get().getRepositoryCode());
                        generalLedgerCorresponding.setRepositoryName(repository3.get().getRepositoryName());
                    }
                    generalLedgerCorresponding.setRepositoryID(detailDTO.getFromRepositoryID());

                    generalLedgers.add(generalLedgerCorresponding);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return generalLedgers;
    }

    /*Chuyển kho by huypq*/
    List<RepositoryLedger> repositoryLedgersRSTransfer
    (List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail) {
        List<RepositoryLedger> repositoryLedgers = new ArrayList<>();
        for (ViewVoucherNo parent : viewVoucherNos) {
            for (ViewVoucherNoDetailDTO detailDTO : parent.getViewVoucherNoDetailDTOS()) {
                RepositoryLedger repositoryLedgerItem = new RepositoryLedger();
                try {
                    BeanUtils.copyProperties(repositoryLedgerItem, parent);
                    repositoryLedgerItem.setTypeID(parent.getTypeID());
                    repositoryLedgerItem.setReferenceID(parent.getRefID());
                    repositoryLedgerItem.setDetailID(detailDTO.getRefID());
                    repositoryLedgerItem.setCompanyID(parent.getCompanyID());
                    repositoryLedgerItem.setNoFBook(parent.getNoFBook());
                    repositoryLedgerItem.setNoMBook(parent.getNoMBook());
                    repositoryLedgerItem.setOwQuantity(detailDTO.getQuantity());
                    repositoryLedgerItem.setOwAmount(detailDTO.getoWAmount());
                    repositoryLedgerItem.setMainOWQuantity(detailDTO.getMainQuantity());
                    repositoryLedgerItem.setUnitPrice(detailDTO.getUnitPrice());
                    repositoryLedgerItem.setAccount(detailDTO.getCreditAccount());
                    repositoryLedgerItem.setAccountCorresponding(detailDTO.getDebitAccount());
                    repositoryLedgerItem.setFormula(detailDTO.getFormula());
                    repositoryLedgerItem.setMaterialGoodsID(detailDTO.getMaterialGoodsID());
                    repositoryLedgerItem.setLotNo(detailDTO.getLotNo());
                    repositoryLedgerItem.setReason(parent.getReason());
                    repositoryLedgerItem.setDate(parent.getDate());
                    repositoryLedgerItem.setPostedDate(parent.getPostedDate());
                    repositoryLedgerItem.setExpiryDate(detailDTO.getExpiryDate());
                    repositoryLedgerItem.setTypeLedger(parent.getTypeLedger());
                    repositoryLedgerItem.setUnitID(detailDTO.getUnitID());
                    repositoryLedgerItem.setMainUnitID(detailDTO.getMainUnitID());
                    repositoryLedgerItem.setMainUnitPrice(detailDTO.getMainUnitPrice());
                    repositoryLedgerItem.setMainConvertRate(detailDTO.getMainConvertRate());
                    repositoryLedgerItem.setBudgetItemID(detailDTO.getBudgetItemID());
                    repositoryLedgerItem.setCostSetID(detailDTO.getCostSetID());
                    repositoryLedgerItem.setStatisticsCodeID(detailDTO.getStatisticsCodeID());
                    repositoryLedgerItem.setExpenseItemID(detailDTO.getExpenseItemID());
                    if (detailDTO.getUnitID() == null) {
                        repositoryLedgerItem.setMainUnitPrice(repositoryLedgerItem.getUnitPrice());
                        repositoryLedgerItem.setMainOWQuantity(repositoryLedgerItem.getOwQuantity());
                        repositoryLedgerItem.setMainConvertRate(new BigDecimal(1));
                        repositoryLedgerItem.setFormula("*");
                    }
                    Optional<Repository> repository1 = repositoryRepository.findById(detailDTO.getFromRepositoryID());
                    // repository
                    if (repository1.isPresent()) {
                        repositoryLedgerItem.setRepositoryCode(repository1.get().getRepositoryCode());
                        repositoryLedgerItem.setRepositoryName(repository1.get().getRepositoryName());
                    }
                    repositoryLedgerItem.setRepositoryID(detailDTO.getFromRepositoryID());
                    // materialGoods
                    Optional<MaterialGoods> materialGoods = materialGoodsRepository.findById(detailDTO.getMaterialGoodsID());
                    if (materialGoods.isPresent()) {
                        repositoryLedgerItem.setMaterialGoodsCode(materialGoods.get().getMaterialGoodsCode());
                        repositoryLedgerItem.setMaterialGoodsName(materialGoods.get().getMaterialGoodsName());
                    }
                    /*Add fromRepository */
                    repositoryLedgers.add(repositoryLedgerItem);

                    RepositoryLedger repositoryLedgerItem1 = new RepositoryLedger();
                    BeanUtils.copyProperties(repositoryLedgerItem1, repositoryLedgerItem);

                    Optional<Repository> repository2 = repositoryRepository.findById(detailDTO.getToRepositoryID());
                    // repository
                    if (repository2.isPresent()) {
                        repositoryLedgerItem1.setRepositoryCode(repository2.get().getRepositoryCode());
                        repositoryLedgerItem1.setRepositoryName(repository2.get().getRepositoryName());
                    }
                    repositoryLedgerItem1.setOwQuantity(null);
                    repositoryLedgerItem1.setOwAmount(null);
                    repositoryLedgerItem1.setIwQuantity(detailDTO.getQuantity());
                    repositoryLedgerItem1.setIwAmount(detailDTO.getoWAmount());
                    repositoryLedgerItem1.setAccount(detailDTO.getDebitAccount());
                    repositoryLedgerItem1.setAccountCorresponding(detailDTO.getCreditAccount());
                    repositoryLedgerItem1.setRepositoryID(detailDTO.getToRepositoryID());
                    repositoryLedgerItem1.setMainIWQuantity(detailDTO.getMainQuantity());
                    repositoryLedgerItem1.setMainOWQuantity(null);

                    /*Add fromRepository */
                    repositoryLedgers.add(repositoryLedgerItem1);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return repositoryLedgers;
    }


    List<RepositoryLedger> repositoryLedgersPPDiscountReturn
        (List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail, Boolean checkQuaSLTon, List<MGForPPOrderConvertDTO> mgForPPOrderConvertDTOS) {
        List<RepositoryLedger> repositoryLedgers = new ArrayList<>();
        for (ViewVoucherNo parent : viewVoucherNos) {
            boolean checkContinue = true;
            if (Boolean.TRUE.equals(parent.getDeliveryVoucher()) && checkQuaSLTon) {
                List<CheckQuantityExistsConvertDTO> details = new ArrayList<>();
                for (ViewVoucherNoDetailDTO item : parent.getViewVoucherNoDetailDTOS()) {
                    details.add(new CheckQuantityExistsConvertDTO(item.getMaterialGoodsID(), item.getQuantity()));
                }
                CheckQuantityExistsDTO checkQuantityExistsDTO = utilsService.checkQuantityBalance(details, mgForPPOrderConvertDTOS);
                if (checkQuantityExistsDTO.getQuantityExists() != null && checkQuantityExistsDTO.getQuantityExists().length() > 0) {
                    parent.setReasonFail("VTHH " + checkQuantityExistsDTO.getQuantityExists() + " xuất quá số tồn!");
                    lstFail.add(parent);
                    viewVoucherNos.remove(parent);
                    checkContinue = false;
                    if (viewVoucherNos.size() <= 0) {
                        return repositoryLedgers;
                    }
                }
            }
            if (checkContinue) {
                RSInwardOutwardSearchDTO rSInwardOutward = new RSInwardOutwardSearchDTO();
                if (Boolean.TRUE.equals(parent.getDeliveryVoucher())) {
                    rSInwardOutward = rSInwardOutwardRepository.findByID(parent.getrSInwardOutwardID());
                }
                for (ViewVoucherNoDetailDTO detailDTO : parent.getViewVoucherNoDetailDTOS()) {
                    if (Boolean.TRUE.equals(parent.getDeliveryVoucher())) {
                        Optional<Repository> repository = Optional.empty();
                        if (detailDTO.getRepositoryID() != null) {
                            repository = repositoryRepository.findById(detailDTO.getRepositoryID());
                        }
                        Optional<MaterialGoods> materialGoods = materialGoodsRepository.findById(detailDTO.getMaterialGoodsID());
                        if (rSInwardOutward != null) {
                            RepositoryLedger repositoryLedgerItem = new RepositoryLedger();
                            repositoryLedgerItem.setReferenceID(rSInwardOutward.getId());
                            repositoryLedgerItem.setCompanyID(parent.getCompanyID());
                            if (rSInwardOutward.getNoFBook() != null) {
                                repositoryLedgerItem.setNoFBook(rSInwardOutward.getNoFBook());
                            }
                            if (rSInwardOutward.getNoMBook() != null) {
                                repositoryLedgerItem.setNoMBook(rSInwardOutward.getNoMBook());
                            }
                            repositoryLedgerItem.setOwQuantity(detailDTO.getQuantity());
                            repositoryLedgerItem.setUnitPrice(detailDTO.getUnitPrice());
                            repositoryLedgerItem.setOwAmount(detailDTO.getAmount());
                            repositoryLedgerItem.setAccount(detailDTO.getCreditAccount());
                            repositoryLedgerItem.setAccountCorresponding(detailDTO.getDebitAccount());
                            repositoryLedgerItem.setDetailID(detailDTO.getId());
                            repositoryLedgerItem.setFormula(detailDTO.getFormula());
                            repositoryLedgerItem.setMaterialGoodsID(detailDTO.getMaterialGoodsID());
                            repositoryLedgerItem.setRepositoryID(detailDTO.getRepositoryID());
                            repositoryLedgerItem.setLotNo(detailDTO.getLotNo());
                            repositoryLedgerItem.setDescription(detailDTO.getDescription());
                            repositoryLedgerItem.setReason(parent.getReason());
                            repositoryLedgerItem.setDate(parent.getDate());
                            repositoryLedgerItem.setPostedDate(parent.getPostedDate());
                            repositoryLedgerItem.setExpiryDate(detailDTO.getExpiryDate());
                            repositoryLedgerItem.setTypeLedger(parent.getTypeLedger());
                            repositoryLedgerItem.setUnitID(detailDTO.getUnitID());
                            repositoryLedgerItem.setMainUnitID(detailDTO.getMainUnitID());
                            repositoryLedgerItem.setMainUnitPrice(detailDTO.getMainUnitPrice());
                            repositoryLedgerItem.setMainOWQuantity(detailDTO.getMainQuantity());
                            repositoryLedgerItem.setMainConvertRate(detailDTO.getMainConvertRate());
                            repositoryLedgerItem.setBudgetItemID(detailDTO.getBudgetItemID());
                            repositoryLedgerItem.setCostSetID(detailDTO.getCostSetID());
                            repositoryLedgerItem.setStatisticsCodeID(detailDTO.getStatisticsCodeID());
                            repositoryLedgerItem.setExpenseItemID(detailDTO.getExpenseItemID());
                            // repository
                            if (repository.isPresent()) {
                                repositoryLedgerItem.setRepositoryCode(repository.get().getRepositoryCode());
                                repositoryLedgerItem.setRepositoryName(repository.get().getRepositoryName());
                            }
                            // materialGoods
                            repositoryLedgerItem.setMaterialGoodsID(detailDTO.getMaterialGoodsID());
                            if (materialGoods.isPresent()) {
                                repositoryLedgerItem.setMaterialGoodsCode(materialGoods.get().getMaterialGoodsCode());
                                repositoryLedgerItem.setMaterialGoodsName(materialGoods.get().getMaterialGoodsName());
                            }
                            Optional<RSInwardOutward> rsInwardOutward = Optional.empty();
                            rsInwardOutward = rSInwardOutwardRepository.findById(rSInwardOutward.getId());
                            if (rsInwardOutward.isPresent()) {
                                rsInwardOutward.get().setRecorded(true);
                                rSInwardOutwardRepository.save(rsInwardOutward.get());
                            }
                            repositoryLedgers.add(repositoryLedgerItem);
                        }
                    }
                }
            }
        }
        return repositoryLedgers;
    }

    List<RepositoryLedger> repositoryLedgersPPInvoice
        (List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail) {
        List<RepositoryLedger> repositoryLedgers = new ArrayList<>();
        UserDTO userDTO = userService.getAccount();
        for (ViewVoucherNo parent : viewVoucherNos) {
            Optional<RSInwardOutward> rsInwardOutward = Optional.empty();
            boolean isContinue = false;
            List<ViewVoucherNoDetailDTO> listDetail = parent.getViewVoucherNoDetailDTOS();
            if (!parent.getTypeID().equals(Constants.PPInvoiceType.TYPE_ID_MUA_HANG_CHUA_THANH_TOAN)) {
                SystemOption systemOptionXQTQ = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.XUAT_QUA_TON_QUY)).findFirst().get();
                if (systemOptionXQTQ.getData().equals("0")) {
                    List<ViewGLPayExceedCashDTO> viewGLPayExceedCashAll = generalLedgerRepository.getViewGLPayExceedCash(parent.getTypeLedger(), userDTO.getOrganizationUnit().getId(), parent.getPostedDate());
                    if (viewGLPayExceedCashAll.size() > 0) {
                        for (ViewVoucherNoDetailDTO sad : listDetail) {
                            for (ViewGLPayExceedCashDTO item : viewGLPayExceedCashAll) {
                                if (parent.getBankAccountDetailID() != null) {
                                    if ((parent.getTypeLedger().compareTo(Constants.TypeLedger.BOTH_BOOK) == 0 || parent.getTypeLedger().compareTo(item.getTypeLedger()) == 0) &&
                                        parent.getBankAccountDetailID().equals(item.getBankAccountDetailID()) &&
                                        sad.getCreditAccount().equals(item.getAccount()) && item.getDebitAmount().subtract(item.getCreditAmount()).subtract(sad.getAmount()).floatValue() < 0) {
                                        if (parent.getTypeLedger().compareTo(Constants.TypeLedger.FINANCIAL_BOOK) == 0) {
                                            parent.setReasonFail(Constants.MSGRecord.XUAT_QUA_TON_QUY_TC);
                                        } else if (parent.getTypeLedger().compareTo(Constants.TypeLedger.MANAGEMENT_BOOK) == 0) {
                                            parent.setReasonFail(Constants.MSGRecord.XUAT_QUA_TON_QUY_QT);
                                        } else {
                                            parent.setReasonFail(Constants.MSGRecord.XUAT_QUA_TON_QUY);
                                        }
                                        isContinue = true;
                                    }
                                } else {
                                    if ((parent.getTypeLedger().compareTo(Constants.TypeLedger.BOTH_BOOK) == 0 || parent.getTypeLedger().compareTo(item.getTypeLedger()) == 0) &&
                                        sad.getCreditAccount().equals(item.getAccount()) && item.getDebitAmount().subtract(item.getCreditAmount()).subtract(sad.getAmount()).floatValue() < 0) {
                                        if (parent.getTypeLedger().compareTo(Constants.TypeLedger.FINANCIAL_BOOK) == 0) {
                                            parent.setReasonFail(Constants.MSGRecord.XUAT_QUA_TON_QUY_TC);
                                        } else if (parent.getTypeLedger().compareTo(Constants.TypeLedger.MANAGEMENT_BOOK) == 0) {
                                            parent.setReasonFail(Constants.MSGRecord.XUAT_QUA_TON_QUY_QT);
                                        } else {
                                            parent.setReasonFail(Constants.MSGRecord.XUAT_QUA_TON_QUY);
                                        }
                                        isContinue = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (isContinue) {
                continue;
            }
            listDetail.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));
            for (ViewVoucherNoDetailDTO detailDTO : listDetail) {
                if ((detailDTO.getMaterialGoodsType() == null || (detailDTO.getMaterialGoodsType() != null && !detailDTO.getMaterialGoodsType().equals(Constants.MaterialGoodsType.SERVICE))) && detailDTO.getRepositoryID() != null) {
                    rsInwardOutward = rSInwardOutwardRepository.findById(parent.getrSInwardOutwardID());
                    if (rsInwardOutward.isPresent()) {
                        RepositoryLedger repositoryLedgerItem = new RepositoryLedger();
                        repositoryLedgerItem.setTypeID(rsInwardOutward.get().getTypeID());
                        repositoryLedgerItem.setReferenceID(rsInwardOutward.get().getId());
                        repositoryLedgerItem.setCompanyID(rsInwardOutward.get().getCompanyID());
                        repositoryLedgerItem.setNoFBook(rsInwardOutward.get().getNoFBook());
                        repositoryLedgerItem.setNoMBook(rsInwardOutward.get().getNoMBook());
                        repositoryLedgerItem.setIwAmount(detailDTO.getInwardAmount());
                        repositoryLedgerItem.setMainIWQuantity(detailDTO.getMainQuantity());
                        repositoryLedgerItem.setIwQuantity(detailDTO.getQuantity());

                        if (repositoryLedgerItem.getIwAmount() != null && repositoryLedgerItem.getIwQuantity() != null && repositoryLedgerItem.getIwQuantity().doubleValue() != 0) {
                            repositoryLedgerItem.setUnitPrice(repositoryLedgerItem.getIwAmount().divide(repositoryLedgerItem.getIwQuantity(), MathContext.DECIMAL32));
                        } else {
                            repositoryLedgerItem.setUnitPrice(BigDecimal.ZERO);
                        }
                        repositoryLedgerItem.setAccount(detailDTO.getDebitAccount());
                        repositoryLedgerItem.setAccountCorresponding(detailDTO.getCreditAccount());
                        repositoryLedgerItem.setDetailID(detailDTO.getRefID());
                        repositoryLedgerItem.setFormula(detailDTO.getFormula());
                        repositoryLedgerItem.setMaterialGoodsID(detailDTO.getMaterialGoodsID());
                        repositoryLedgerItem.setRepositoryID(detailDTO.getRepositoryID());
                        repositoryLedgerItem.setLotNo(detailDTO.getLotNo());
                        repositoryLedgerItem.setDescription(detailDTO.getDescription());
                        repositoryLedgerItem.setReason(rsInwardOutward.get().getReason());
                        repositoryLedgerItem.setDate(rsInwardOutward.get().getDate());
                        repositoryLedgerItem.setPostedDate(rsInwardOutward.get().getPostedDate());
                        repositoryLedgerItem.setExpiryDate(detailDTO.getExpiryDate());
                        repositoryLedgerItem.setTypeLedger(rsInwardOutward.get().getTypeLedger());
                        repositoryLedgerItem.setUnitID(detailDTO.getUnitID());
                        repositoryLedgerItem.setMainUnitID(detailDTO.getMainUnitID());
                        repositoryLedgerItem.setMainUnitPrice(detailDTO.getMainUnitPrice());
                        repositoryLedgerItem.setMainConvertRate(detailDTO.getMainConvertRate());
                        repositoryLedgerItem.setBudgetItemID(detailDTO.getBudgetItemID());
                        repositoryLedgerItem.setCostSetID(detailDTO.getCostSetID());
                        repositoryLedgerItem.setStatisticsCodeID(detailDTO.getStatisticsCodeID());
                        repositoryLedgerItem.setExpenseItemID(detailDTO.getExpenseItemID());
                        repositoryLedgerItem.setConfrontID(detailDTO.getConfrontID());
                        repositoryLedgerItem.setConfrontDetailID(detailDTO.getConfrontDetailID());
                        if (detailDTO.getUnitID() == null) {
                            repositoryLedgerItem.setMainUnitPrice(repositoryLedgerItem.getUnitPrice());
                            repositoryLedgerItem.setMainOWQuantity(repositoryLedgerItem.getOwQuantity());
                            repositoryLedgerItem.setMainConvertRate(new BigDecimal(1));
                            repositoryLedgerItem.setFormula("*");
                        }
                        // repository
                        if (detailDTO.getRepositoryID() != null) {
                            Optional<Repository> repository = repositoryRepository.findById(detailDTO.getRepositoryID());
                            if (repository.isPresent()) {
                                repositoryLedgerItem.setRepositoryCode(repository.get().getRepositoryCode());
                                repositoryLedgerItem.setRepositoryName(repository.get().getRepositoryName());
                            }
                        }
                        // materialGoods
                        if (detailDTO.getRepositoryID() != null) {
                            Optional<MaterialGoods> materialGoods = materialGoodsRepository.findById(detailDTO.getMaterialGoodsID());
                            if (materialGoods.isPresent()) {
                                repositoryLedgerItem.setMaterialGoodsCode(materialGoods.get().getMaterialGoodsCode());
                                repositoryLedgerItem.setMaterialGoodsName(materialGoods.get().getMaterialGoodsName());
                            }
                        }
                        repositoryLedgers.add(repositoryLedgerItem);
                    }

                }
            }
        }
        return repositoryLedgers;
    }

    List<GeneralLedger> genGeneralLedgersPPService(List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail
        , List<MGForPPOrderConvertDTO> mgForPPOrderConvertDTOS, List<ViewGLPayExceedCashDTO> viewGLPayExceedCashAll) {
        List<GeneralLedger> listGenTemp = new ArrayList<>();
        for (ViewVoucherNo parent : viewVoucherNos) {
            boolean checkContinue = true;
            if (Boolean.TRUE.equals(parent.getDeliveryVoucher()) && mgForPPOrderConvertDTOS.size() > 0) {
                List<CheckQuantityExistsConvertDTO> details = new ArrayList<>();
                for (ViewVoucherNoDetailDTO item : parent.getViewVoucherNoDetailDTOS()) {
                    details.add(new CheckQuantityExistsConvertDTO(item.getMaterialGoodsID(), item.getQuantity()));
                }
                CheckQuantityExistsDTO checkQuantityExistsDTO = utilsService.checkQuantityBalance(details, mgForPPOrderConvertDTOS);
                if (checkQuantityExistsDTO.getQuantityExists() != null && checkQuantityExistsDTO.getQuantityExists().length() > 0) {
                    parent.setReasonFail("VTHH " + checkQuantityExistsDTO.getQuantityExists() + " xuất quá số tồn!");
                    lstFail.add(parent);
//                    viewVoucherNos.remove(parent);
                    checkContinue = false;
                }
            }
            if (viewGLPayExceedCashAll.size() > 0) {
                List<CheckQuantityExistsConvertDTO> details = new ArrayList<>();
                for (ViewVoucherNoDetailDTO sad : parent.getViewVoucherNoDetailDTOS()) {
                    for (ViewGLPayExceedCashDTO item : viewGLPayExceedCashAll) {
                        if ((parent.getTypeLedger().compareTo(Constants.TypeLedger.BOTH_BOOK) == 0 || parent.getTypeLedger().compareTo(item.getTypeLedger()) == 0) &&
                            sad.getCreditAccount().equals(item.getAccount()) && item.getDebitAmount().subtract(item.getCreditAmount()).subtract(sad.getAmount()).floatValue() < 0) {
                            parent.setReasonFail("Xuất quá tồn quỹ!");
                            lstFail.add(parent);
//                            viewVoucherNos.remove(parent);
                            checkContinue = false;
                        }
                    }
                }
            }
            if (checkContinue) {
                List<ViewVoucherNoDetailDTO> listDetail = parent.getViewVoucherNoDetailDTOS();
                listDetail.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));
                for (ViewVoucherNoDetailDTO detailDTO : listDetail) {
                    GeneralLedger generalLedger = new GeneralLedger();
                    try {
                        // cặp nợ có
                        BeanUtils.copyProperties(generalLedger, parent);
                        generalLedger.setReferenceID(parent.getRefID());
                        generalLedger.setTypeID(parent.getTypeID());
                        generalLedger.setTypeLedger(parent.getTypeLedger());
                        generalLedger.setDetailID(detailDTO.getRefID());
                        generalLedger.setInvoiceNo(detailDTO.getInvoiceNo());
                        generalLedger.setDate(parent.getDate());
                        generalLedger.setPostedDate(parent.getPostedDate());
                        generalLedger.setBankAccountDetailID(parent.getBankAccountDetailID());
                        generalLedger.setBankAccount(parent.getBankAccount());
                        generalLedger.bankName(parent.getBankName());
                        generalLedger.setExchangeRate(parent.getExchangeRate());
                        generalLedger.reason(parent.getReason());
                        generalLedger.description(parent.getDescription());
                        generalLedger.setAccountingObjectID(parent.getAccountingObjectID());
                        generalLedger.setAccountingObjectCode(parent.getAccountingObjectCode());
                        generalLedger.setAccountingObjectName(parent.getAccountingObjectName());
                        generalLedger.setAccountingObjectAddress(parent.getAccountingObjectAddress());
                        generalLedger.setContactName(parent.getContactName());
                        generalLedger.setEmployeeID(parent.getEmployeeID());
                        generalLedger.setEmployeeCode(parent.getEmployeeCode());
                        generalLedger.setEmployeeName(parent.getEmployeeName());
                        generalLedger.setRefDateTime(parent.getRefDateTime());
                        generalLedger.setRefTable(parent.getRefTable());

                        generalLedger.setNoFBook(parent.getNoFBook());
                        generalLedger.setNoMBook(parent.getNoMBook());
                        generalLedger.account(detailDTO.getDebitAccount());
                        generalLedger.accountCorresponding(detailDTO.getCreditAccount());
                        generalLedger.debitAmount(detailDTO.getAmount().subtract(detailDTO.getDiscountAmount()));
                        generalLedger.debitAmountOriginal(detailDTO.getAmountOriginal().subtract(detailDTO.getDiscountAmountOriginal()));
                        generalLedger.creditAmount(BigDecimal.ZERO);
                        generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                        generalLedger.description(detailDTO.getDescription());

                        generalLedger.accountingObjectID(detailDTO.getAccountingObjectID());
                        generalLedger.accountingObjectName(detailDTO.getAccountingObjectName());
                        generalLedger.accountingObjectCode(detailDTO.getAccountingObjectCode());
                        generalLedger.accountingObjectAddress(detailDTO.getAccountingObjectAddress());
                        generalLedger.contactName(parent.getContactName());
                        generalLedger.employeeID(parent.getEmployeeID());
                        generalLedger.refDateTime(parent.getDate());
                        generalLedger.budgetItemID(detailDTO.getBudgetItemID());
                        generalLedger.costSetID(detailDTO.getCostSetID());
                        generalLedger.contractID(detailDTO.getContractID());
                        generalLedger.statisticsCodeID(detailDTO.getStatisticsCodeID());
                        generalLedger.expenseItemID(detailDTO.getExpenseItemID());
                        generalLedger.departmentID(detailDTO.getDepartmentID());
                        generalLedger.setCompanyID(parent.getCompanyID());

                        generalLedger.setMaterialGoodsID(detailDTO.getMaterialGoodsID());
                        generalLedger.setMaterialGoodsCode(detailDTO.getMaterialGoodsCode());
                        generalLedger.setMaterialGoodsName(detailDTO.getMaterialGoodsName());


                        generalLedger.setUnitID(detailDTO.getUnitID());
                        generalLedger.setCurrencyID(parent.getCurrencyID());
                        generalLedger.quantity(detailDTO.getQuantity());
                        generalLedger.setUnitPrice(detailDTO.getUnitPrice());
                        generalLedger.setUnitPriceOriginal(detailDTO.getUnitPriceOriginal());
                        generalLedger.setOrderPriority(detailDTO.getOrderPriority());

                        // thành tiền lớn hơn 0 ms lưu
                        if (detailDTO.getAmount() != null && detailDTO.getAmount().compareTo(BigDecimal.ZERO) >= 0) {
                            listGenTemp.add(generalLedger);
                        }

                        GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                        BeanUtils.copyProperties(generalLedgerCorresponding, generalLedger);
                        generalLedgerCorresponding.account(detailDTO.getCreditAccount());
                        generalLedgerCorresponding.accountCorresponding(detailDTO.getDebitAccount());
                        generalLedgerCorresponding.creditAmount(detailDTO.getAmount().subtract(detailDTO.getDiscountAmount()));
                        generalLedgerCorresponding.creditAmountOriginal(detailDTO.getAmountOriginal().subtract(detailDTO.getDiscountAmountOriginal()));
                        generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                        generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);

                        if (detailDTO.getAmount() != null && detailDTO.getAmount().compareTo(BigDecimal.ZERO) >= 0) {
                            listGenTemp.add(generalLedgerCorresponding);
                        }

                        if (!Strings.isNullOrEmpty(detailDTO.getvATAccount()) && !Strings.isNullOrEmpty(detailDTO.getDeductionDebitAccount())) {
                            GeneralLedger generalLedgerVAT = new GeneralLedger();
                            BeanUtils.copyProperties(generalLedgerVAT, generalLedger);
                            generalLedgerVAT.account(detailDTO.getvATAccount());
                            generalLedgerVAT.accountCorresponding(detailDTO.getDeductionDebitAccount());
                            generalLedgerVAT.creditAmount(BigDecimal.ZERO);
                            generalLedgerVAT.creditAmountOriginal(BigDecimal.ZERO);
                            generalLedgerVAT.debitAmount(detailDTO.getvATAmount());
                            generalLedgerVAT.debitAmountOriginal(detailDTO.getvATAmountOriginal());
                            generalLedgerVAT.setReason(detailDTO.getVatDescription());
                            listGenTemp.add(generalLedgerVAT);

                            GeneralLedger generalLedgerVATCorresponding = new GeneralLedger();
                            BeanUtils.copyProperties(generalLedgerVATCorresponding, generalLedgerVAT);
                            generalLedgerVATCorresponding.account(detailDTO.getDeductionDebitAccount());
                            generalLedgerVATCorresponding.accountCorresponding(detailDTO.getvATAccount());
                            generalLedgerVATCorresponding.creditAmount(detailDTO.getvATAmount());
                            generalLedgerVATCorresponding.creditAmountOriginal(detailDTO.getvATAmountOriginal());
                            generalLedgerVATCorresponding.debitAmount(BigDecimal.ZERO);
                            generalLedgerVATCorresponding.debitAmountOriginal(BigDecimal.ZERO);

                            listGenTemp.add(generalLedgerVATCorresponding);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return listGenTemp;

    }


    /*Thẻ tín dụng*/
    List<GeneralLedger> genGeneralLedgersCPExpenseTranfer(List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail) {
        List<GeneralLedger> generalLedgers = new ArrayList<>();
        for (ViewVoucherNo parent : viewVoucherNos) {
            for (ViewVoucherNoDetailDTO detailDTO : parent.getViewVoucherNoDetailDTOS()) {
                GeneralLedger generalLedger = new GeneralLedger();
                generalLedger.branchID(null);
                generalLedger.setReferenceID(parent.getRefID());
                generalLedger.setDetailID(detailDTO.getRefID());
                generalLedger.setCompanyID(parent.getCompanyID());
                generalLedger.setTypeID(parent.getTypeID());
//                generalLedger.postedDate(parent.getPostedDate());
//                generalLedger.date(parent.getDate());
                generalLedger.typeLedger(parent.getTypeLedger());
                generalLedger.noFBook(parent.getNoFBook());
                generalLedger.noMBook(parent.getNoMBook());
                generalLedger.account(detailDTO.getDebitAccount());
                generalLedger.accountCorresponding(detailDTO.getCreditAccount());
//                generalLedger.currencyID(parent.getCurrencyID());
//                generalLedger.currencyID(parent.getCurrencyID());
//                generalLedger.exchangeRate(parent.getExchangeRate());
//                generalLedger.contractID(detailDTO.getContractID());
//                generalLedger.bankAccountDetailID(detailDTO.getBankAccountDetailID());
//                generalLedger.bankAccount(parent.getBankAccount());
//                generalLedger.bankName(parent.getBankName());
//                generalLedger.accountingObjectCode(detailDTO.getDebitAccountingObjectCode());
//                generalLedger.accountingObjectName(detailDTO.getDebitAccountingObjectName());
//                generalLedger.accountingObjectAddress(detailDTO.getDebitAccountingObjectAddress());
//                generalLedger.accountingObjectID(detailDTO.getDebitAccountingObjectID());
//                generalLedger.employeeCode(detailDTO.getEmployeeCode());
//                generalLedger.employeeName(detailDTO.getEmployeeName());
                generalLedger.debitAmountOriginal(detailDTO.getAmountOriginal());
                generalLedger.debitAmount(detailDTO.getAmount());
                generalLedger.creditAmount(BigDecimal.ZERO);
                generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                generalLedger.reason(parent.getReason());
                generalLedger.description(detailDTO.getDescription());
//                generalLedger.budgetItemID(detailDTO.getBudgetItemID());
                generalLedger.costSetID(detailDTO.getCostSetID());
                generalLedger.statisticsCodeID(detailDTO.getStatisticsCodeID());
//                generalLedger.employeeID(detailDTO.getEmployeeID());
                generalLedger.refDateTime(parent.getPostedDate());
//                generalLedger.departmentID(detailDTO.getDepartmentID());
                generalLedger.expenseItemID(detailDTO.getExpenseItemID());
                generalLedgers.add(generalLedger);

                GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                generalLedgerCorresponding.branchID(null);
                generalLedgerCorresponding.setReferenceID(parent.getRefID());
                generalLedgerCorresponding.setDetailID(detailDTO.getRefID());
                generalLedgerCorresponding.setCompanyID(parent.getCompanyID());
                generalLedgerCorresponding.setTypeID(parent.getTypeID());
//                generalLedgerCorresponding.postedDate(parent.getPostedDate());
//                generalLedgerCorresponding.date(parent.getDate());
                generalLedgerCorresponding.typeLedger(parent.getTypeLedger());
                generalLedgerCorresponding.noFBook(parent.getNoFBook());
                generalLedgerCorresponding.noMBook(parent.getNoMBook());
                generalLedgerCorresponding.account(detailDTO.getCreditAccount());
                generalLedgerCorresponding.accountCorresponding(detailDTO.getDebitAccount());
//                generalLedgerCorresponding.currencyID(parent.getCurrencyID());
//                generalLedgerCorresponding.exchangeRate(parent.getExchangeRate());
//                generalLedgerCorresponding.contractID(detailDTO.getContractID());
//                generalLedgerCorresponding.bankAccountDetailID(detailDTO.getBankAccountDetailID());
//                generalLedgerCorresponding.bankAccount(parent.getBankAccount());
//                generalLedgerCorresponding.bankName(parent.getBankName());
//                generalLedgerCorresponding.accountingObjectCode(detailDTO.getCreditAccountingObjectCode());
//                generalLedgerCorresponding.accountingObjectName(detailDTO.getCreditAccountingObjectName());
//                generalLedgerCorresponding.accountingObjectAddress(detailDTO.getCreditAccountingObjectAddress());
//                generalLedgerCorresponding.accountingObjectID(detailDTO.getCreditAccountingObjectID());
//                generalLedgerCorresponding.employeeCode(detailDTO.getEmployeeCode());
//                generalLedgerCorresponding.employeeName(detailDTO.getEmployeeName());
                generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);
                generalLedgerCorresponding.creditAmount(detailDTO.getAmountOriginal());
                generalLedgerCorresponding.creditAmountOriginal(detailDTO.getAmount());
                generalLedgerCorresponding.reason(parent.getReason());
                generalLedgerCorresponding.description(detailDTO.getDescription());
//                generalLedgerCorresponding.budgetItemID(detailDTO.getBudgetItemID());
                generalLedgerCorresponding.costSetID(detailDTO.getCostSetID());
                generalLedgerCorresponding.statisticsCodeID(detailDTO.getStatisticsCodeID());
//                generalLedgerCorresponding.employeeID(detailDTO.getEmployeeID());
                generalLedgerCorresponding.refDateTime(parent.getPostedDate());
//                generalLedgerCorresponding.departmentID(detailDTO.getDepartmentID());
                generalLedgerCorresponding.expenseItemID(detailDTO.getExpenseItemID());
                generalLedgers.add(generalLedgerCorresponding);
            }
        }
        return generalLedgers;
    }

    List<GeneralLedger> genGeneralLedgersPPInvoice(List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> lstFail) {
        List<GeneralLedger> listGenTemp = new ArrayList<>();
        UserDTO userDTO = userService.getAccount();
        for (ViewVoucherNo parent : viewVoucherNos) {
            boolean isContinue = false;
            List<ViewVoucherNoDetailDTO> listDetail = parent.getViewVoucherNoDetailDTOS();
            if (!parent.getTypeID().equals(Constants.PPInvoiceType.TYPE_ID_MUA_HANG_CHUA_THANH_TOAN)) {
                SystemOption systemOptionXQTQ = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.XUAT_QUA_TON_QUY)).findFirst().get();
                if (systemOptionXQTQ.getData().equals("0")) {
                    List<ViewGLPayExceedCashDTO> viewGLPayExceedCashAll = generalLedgerRepository.getViewGLPayExceedCash(parent.getTypeLedger(), userDTO.getOrganizationUnit().getId(), parent.getPostedDate());
                    if (viewGLPayExceedCashAll.size() > 0) {
                        for (ViewVoucherNoDetailDTO sad : listDetail) {
                            for (ViewGLPayExceedCashDTO item : viewGLPayExceedCashAll) {
                                if (parent.getBankAccountDetailID() != null) {
                                    if ((parent.getTypeLedger().compareTo(Constants.TypeLedger.BOTH_BOOK) == 0 || parent.getTypeLedger().compareTo(item.getTypeLedger()) == 0) &&
                                        parent.getBankAccountDetailID().equals(item.getBankAccountDetailID()) &&
                                        sad.getCreditAccount().equals(item.getAccount()) && item.getDebitAmount().subtract(item.getCreditAmount()).subtract(sad.getAmount()).floatValue() < 0) {
                                        if (parent.getTypeLedger().compareTo(Constants.TypeLedger.FINANCIAL_BOOK) == 0) {
                                            parent.setReasonFail(Constants.MSGRecord.XUAT_QUA_TON_QUY_TC);
                                        } else if (parent.getTypeLedger().compareTo(Constants.TypeLedger.MANAGEMENT_BOOK) == 0) {
                                            parent.setReasonFail(Constants.MSGRecord.XUAT_QUA_TON_QUY_QT);
                                        } else {
                                            parent.setReasonFail(Constants.MSGRecord.XUAT_QUA_TON_QUY);
                                        }
                                        isContinue = true;
                                    }
                                } else {
                                    if ((parent.getTypeLedger().compareTo(Constants.TypeLedger.BOTH_BOOK) == 0 || parent.getTypeLedger().compareTo(item.getTypeLedger()) == 0) &&
                                        sad.getCreditAccount().equals(item.getAccount()) && item.getDebitAmount().subtract(item.getCreditAmount()).subtract(sad.getAmount()).floatValue() < 0) {
                                        if (parent.getTypeLedger().compareTo(Constants.TypeLedger.FINANCIAL_BOOK) == 0) {
                                            parent.setReasonFail(Constants.MSGRecord.XUAT_QUA_TON_QUY_TC);
                                        } else if (parent.getTypeLedger().compareTo(Constants.TypeLedger.MANAGEMENT_BOOK) == 0) {
                                            parent.setReasonFail(Constants.MSGRecord.XUAT_QUA_TON_QUY_QT);
                                        } else {
                                            parent.setReasonFail(Constants.MSGRecord.XUAT_QUA_TON_QUY);
                                        }
                                        isContinue = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (isContinue) {
                lstFail.add(parent);
                continue;
            }
            listDetail.sort(Comparator.comparingInt(ViewVoucherNoDetailDTO::getOrderPriority));

            for (ViewVoucherNoDetailDTO detailDTO : listDetail) {
                GeneralLedger generalLedger = new GeneralLedger();
                try {
                    // cặp nợ có
                    BeanUtils.copyProperties(generalLedger, parent);
                    generalLedger.setReferenceID(parent.getRefID());
                    generalLedger.setTypeID(parent.getTypeID());
                    generalLedger.setTypeLedger(parent.getTypeLedger());
                    generalLedger.setDetailID(detailDTO.getRefID());
                    generalLedger.setInvoiceNo(detailDTO.getInvoiceNo());
                    generalLedger.setDate(parent.getDate());
                    generalLedger.setPostedDate(parent.getPostedDate());
                    generalLedger.setBankAccountDetailID(parent.getBankAccountDetailID());
                    generalLedger.setBankAccount(parent.getBankAccount());
                    generalLedger.bankName(parent.getBankName());
                    generalLedger.setExchangeRate(parent.getExchangeRate());
                    generalLedger.reason(parent.getReason());
                    generalLedger.description(parent.getDescription());
                    generalLedger.setAccountingObjectID(parent.getAccountingObjectID());
                    generalLedger.setAccountingObjectCode(parent.getAccountingObjectCode());
                    generalLedger.setAccountingObjectName(parent.getAccountingObjectName());
                    generalLedger.setAccountingObjectAddress(parent.getAccountingObjectAddress());
                    generalLedger.setContactName(parent.getContactName());
                    generalLedger.setEmployeeID(parent.getEmployeeID());
                    generalLedger.setEmployeeCode(parent.getEmployeeCode());
                    generalLedger.setEmployeeName(parent.getEmployeeName());
                    generalLedger.setRefDateTime(parent.getRefDateTime());
                    generalLedger.setRefTable(parent.getRefTable());

                    generalLedger.setNoFBook(parent.getNoFBook());
                    generalLedger.setNoMBook(parent.getNoMBook());
                    generalLedger.account(detailDTO.getDebitAccount());
                    generalLedger.accountCorresponding(detailDTO.getCreditAccount());

                    BigDecimal debitAmount = null;
                    if (detailDTO.getDiscountAmount() != null) {
                        debitAmount = detailDTO.getAmount().subtract(detailDTO.getDiscountAmount());
                    } else {
                        debitAmount = detailDTO.getAmount();
                    }
                    BigDecimal debitAmountOriginal = null;
                    if (detailDTO.getDiscountAmountOriginal() != null) {
                        debitAmountOriginal = detailDTO.getAmountOriginal().subtract(detailDTO.getDiscountAmountOriginal());
                    } else {
                        debitAmountOriginal = detailDTO.getAmountOriginal();
                    }

                    generalLedger.debitAmount(debitAmount);
                    generalLedger.debitAmountOriginal(debitAmountOriginal);
                    generalLedger.creditAmount(BigDecimal.ZERO);
                    generalLedger.creditAmountOriginal(BigDecimal.ZERO);
                    generalLedger.description(detailDTO.getDescription());

                    generalLedger.accountingObjectID(detailDTO.getAccountingObjectID());
                    generalLedger.accountingObjectName(detailDTO.getAccountingObjectName());
                    generalLedger.accountingObjectCode(detailDTO.getAccountingObjectCode());
                    generalLedger.accountingObjectAddress(detailDTO.getAccountingObjectAddress());
                    generalLedger.contactName(parent.getContactName());
                    generalLedger.employeeID(parent.getEmployeeID());
                    generalLedger.refDateTime(parent.getDate());
                    generalLedger.budgetItemID(detailDTO.getBudgetItemID());
                    generalLedger.costSetID(detailDTO.getCostSetID());
                    generalLedger.contractID(detailDTO.getContractID());
                    generalLedger.statisticsCodeID(detailDTO.getStatisticsCodeID());
                    generalLedger.expenseItemID(detailDTO.getExpenseItemID());
                    generalLedger.departmentID(detailDTO.getDepartmentID());
                    generalLedger.setCompanyID(parent.getCompanyID());

                    generalLedger.setMaterialGoodsID(detailDTO.getMaterialGoodsID());
                    generalLedger.setMaterialGoodsCode(detailDTO.getMaterialGoodsCode());
                    generalLedger.setMaterialGoodsName(detailDTO.getMaterialGoodsName());


                    generalLedger.setUnitID(detailDTO.getUnitID());
                    generalLedger.setCurrencyID(parent.getCurrencyID());
                    generalLedger.quantity(detailDTO.getQuantity());
                    generalLedger.setUnitPrice(detailDTO.getUnitPrice());
                    generalLedger.setUnitPriceOriginal(detailDTO.getUnitPriceOriginal());
                    generalLedger.setOrderPriority(detailDTO.getOrderPriority());

                    generalLedger.setRepositoryID(detailDTO.getRepositoryID());
                    generalLedger.setRepositoryCode(detailDTO.getRepositoryCode());
                    generalLedger.setRepositoryName(detailDTO.getRepositoryName());

                    generalLedger.setMainUnitID(detailDTO.getMainUnitID());
                    generalLedger.mainQuantity(detailDTO.getMainQuantity());
                    generalLedger.mainUnitPrice(detailDTO.getMainUnitPrice());
                    generalLedger.mainConvertRate(detailDTO.getMainConvertRate());
                    generalLedger.setFormula(detailDTO.getFormula());

                    generalLedger.statisticsCodeID(detailDTO.getStatisticsCodeID());

                    // thành tiền lớn hơn 0 ms lưu
                    if (detailDTO.getAmount() != null && detailDTO.getAmount().doubleValue() != 0) {
                        listGenTemp.add(generalLedger);
                    }

                    GeneralLedger generalLedgerCorresponding = new GeneralLedger();
                    BeanUtils.copyProperties(generalLedgerCorresponding, generalLedger);
                    generalLedgerCorresponding.account(detailDTO.getCreditAccount());
                    generalLedgerCorresponding.accountCorresponding(detailDTO.getDebitAccount());
                    generalLedgerCorresponding.creditAmount(detailDTO.getAmount().subtract(detailDTO.getDiscountAmount()));
                    generalLedgerCorresponding.creditAmountOriginal(detailDTO.getAmountOriginal().subtract(detailDTO.getDiscountAmountOriginal()));
                    generalLedgerCorresponding.debitAmount(BigDecimal.ZERO);
                    generalLedgerCorresponding.debitAmountOriginal(BigDecimal.ZERO);

                    if (detailDTO.getAmount() != null && detailDTO.getAmount().doubleValue() != 0) {
                        listGenTemp.add(generalLedgerCorresponding);
                    }

                    if (parent.getImportPurchase()) {
                        // nợ - thuế nhập khẩu
                        if (detailDTO.getImportTaxAmount() != null && detailDTO.getImportTaxAmount().doubleValue() != 0) {
                            GeneralLedger genTempVATCorrespondingImportPurchase = new GeneralLedger();
                            BeanUtils.copyProperties(genTempVATCorrespondingImportPurchase, generalLedger);
                            genTempVATCorrespondingImportPurchase.account(detailDTO.getDebitAccount());
                            genTempVATCorrespondingImportPurchase.accountCorresponding(detailDTO.getImportTaxAccount());
                            genTempVATCorrespondingImportPurchase.debitAmount(detailDTO.getImportTaxAmount());
                            genTempVATCorrespondingImportPurchase.debitAmountOriginal(detailDTO.getImportTaxAmountOriginal());
                            genTempVATCorrespondingImportPurchase.creditAmount(BigDecimal.ZERO);
                            genTempVATCorrespondingImportPurchase.creditAmountOriginal(BigDecimal.ZERO);

                            listGenTemp.add(genTempVATCorrespondingImportPurchase);

                            GeneralLedger genTempVATImportPurchase = new GeneralLedger();
                            BeanUtils.copyProperties(genTempVATImportPurchase, generalLedger);
                            genTempVATImportPurchase.account(detailDTO.getImportTaxAccount());
                            genTempVATImportPurchase.accountCorresponding(detailDTO.getDebitAccount());
                            genTempVATImportPurchase.debitAmount(BigDecimal.ZERO);
                            genTempVATImportPurchase.debitAmountOriginal(BigDecimal.ZERO);
                            genTempVATImportPurchase.creditAmount(detailDTO.getImportTaxAmount());
                            genTempVATImportPurchase.creditAmountOriginal(detailDTO.getImportTaxAmountOriginal());

                            listGenTemp.add(genTempVATImportPurchase);
                        }

                    }

                    // cặp nợ thuế tiêu thụ đặc biệt
                    if (detailDTO.getSpecialConsumeTaxAmount() != null && detailDTO.getSpecialConsumeTaxAmount().doubleValue() != 0) {
                        GeneralLedger genTempTTDBCorresponding = new GeneralLedger();
                        BeanUtils.copyProperties(genTempTTDBCorresponding, generalLedger);
                        genTempTTDBCorresponding.account(detailDTO.getDebitAccount());
                        genTempTTDBCorresponding.accountCorresponding(detailDTO.getSpecialConsumeTaxAccount());
                        genTempTTDBCorresponding.debitAmount(detailDTO.getSpecialConsumeTaxAmount());
                        genTempTTDBCorresponding.debitAmountOriginal(detailDTO.getSpecialConsumeTaxAmountOriginal());
                        genTempTTDBCorresponding.creditAmount(BigDecimal.ZERO);
                        genTempTTDBCorresponding.creditAmountOriginal(BigDecimal.ZERO);

                        listGenTemp.add(genTempTTDBCorresponding);

                        GeneralLedger genTempTTDB = new GeneralLedger();
                        BeanUtils.copyProperties(genTempTTDB, generalLedger);
                        genTempTTDB.account(detailDTO.getSpecialConsumeTaxAccount());
                        genTempTTDB.accountCorresponding(detailDTO.getDebitAccount());
                        genTempTTDB.debitAmount(BigDecimal.ZERO);
                        genTempTTDB.debitAmountOriginal(BigDecimal.ZERO);
                        genTempTTDB.creditAmount(detailDTO.getSpecialConsumeTaxAmount());
                        genTempTTDB.creditAmountOriginal(detailDTO.getSpecialConsumeTaxAmountOriginal());

                        listGenTemp.add(genTempTTDB);
                    }

                    // nợ - thuế giá trị gia tăng
                    if (detailDTO.getvATAmount() != null && detailDTO.getvATAmount().doubleValue() != 0) {
                        GeneralLedger genTempVAT = new GeneralLedger();
                        BeanUtils.copyProperties(genTempVAT, generalLedger);
                        if (parent.getImportPurchase()) {
                            genTempVAT.account(detailDTO.getDeductionDebitAccount());
                            genTempVAT.accountCorresponding(detailDTO.getvATAccount());
                            genTempVAT.debitAmount(detailDTO.getvATAmount());
                            genTempVAT.debitAmountOriginal(detailDTO.getvATAmountOriginal());
                            genTempVAT.creditAmount(BigDecimal.ZERO);
                            genTempVAT.creditAmountOriginal(BigDecimal.ZERO);
                        } else {
                            genTempVAT.account(detailDTO.getvATAccount());
                            genTempVAT.accountCorresponding(detailDTO.getDeductionDebitAccount());
                            genTempVAT.debitAmount(detailDTO.getvATAmount());
                            genTempVAT.debitAmountOriginal(detailDTO.getvATAmountOriginal());
                            genTempVAT.creditAmount(BigDecimal.ZERO);
                            genTempVAT.creditAmountOriginal(BigDecimal.ZERO);
                        }
                        genTempVAT.description(detailDTO.getVatDescription());
                        listGenTemp.add(genTempVAT);

                        GeneralLedger genTempVATCorresponding = new GeneralLedger();
                        BeanUtils.copyProperties(genTempVATCorresponding, generalLedger);

                        if (parent.getImportPurchase()) {
                            genTempVATCorresponding.account(detailDTO.getvATAccount());
                            genTempVATCorresponding.accountCorresponding(detailDTO.getDeductionDebitAccount());
                            genTempVATCorresponding.debitAmount(BigDecimal.ZERO);
                            genTempVATCorresponding.debitAmountOriginal(BigDecimal.ZERO);
                            genTempVATCorresponding.creditAmount(detailDTO.getvATAmount());
                            genTempVATCorresponding.creditAmountOriginal(detailDTO.getvATAmountOriginal());
                        } else {
                            genTempVATCorresponding.account(detailDTO.getDeductionDebitAccount());
                            genTempVATCorresponding.accountCorresponding(detailDTO.getvATAccount());
                            genTempVATCorresponding.debitAmount(BigDecimal.ZERO);
                            genTempVATCorresponding.debitAmountOriginal(BigDecimal.ZERO);
                            genTempVATCorresponding.creditAmount(detailDTO.getvATAmount());
                            genTempVATCorresponding.creditAmountOriginal(detailDTO.getvATAmountOriginal());
                        }
                        genTempVATCorresponding.description(detailDTO.getVatDescription());

                        listGenTemp.add(genTempVATCorresponding);
                    }

                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return listGenTemp;
    }
}
