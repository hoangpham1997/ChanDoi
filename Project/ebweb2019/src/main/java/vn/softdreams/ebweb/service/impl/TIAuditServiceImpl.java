package vn.softdreams.ebweb.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
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
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.TIAuditConvertDTO;
import vn.softdreams.ebweb.service.dto.ToolsDetailsTiAuditConvertDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.TIAuditDetailAllDTO;
import vn.softdreams.ebweb.web.rest.dto.TIAuditDetailByIDDTO;
import vn.softdreams.ebweb.web.rest.dto.TIAuditMemberDetailByIDDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing TIAudit.
 */
@Service
@Transactional
public class TIAuditServiceImpl implements TIAuditService {

    private final Logger log = LoggerFactory.getLogger(TIAuditServiceImpl.class);

    private final TIAuditRepository tIAuditRepository;

    private final TIAuditMemberDetailsRepository tiAuditMemberDetailsRepository;

    private final UserService userService;

    private final GenCodeService genCodeService;

    private final UtilsRepository utilsRepository;

    private final TIIncrementService tiIncrementService;

    private final TIDecrementService tiDecrementService;

    private final ToolsRepository toolsRepository;

    private final RefVoucherRepository refVoucherRepository;

    public TIAuditServiceImpl(TIAuditRepository tIAuditRepository, TIAuditMemberDetailsRepository tiAuditMemberDetailsRepository, UserService userService, GenCodeService genCodeService, UtilsRepository utilsRepository, TIIncrementService tiIncrementService, TIDecrementService tiDecrementService, ToolsRepository toolsRepository, RefVoucherRepository refVoucherRepository) {
        this.tIAuditRepository = tIAuditRepository;
        this.tiAuditMemberDetailsRepository = tiAuditMemberDetailsRepository;
        this.userService = userService;
        this.genCodeService = genCodeService;
        this.utilsRepository = utilsRepository;

        this.tiIncrementService = tiIncrementService;
        this.tiDecrementService = tiDecrementService;
        this.toolsRepository = toolsRepository;
        this.refVoucherRepository = refVoucherRepository;
    }

    /**
     * Save a tIAudit.
     *
     * @param tIAudit the entity to save
     * @return the persisted entity
     */
    @Override
    public TIAudit save(TIAudit tIAudit) {
        log.debug("Request to save TIAudit : {}", tIAudit);
        tIAudit.typeID(TypeConstant.KIEM_KE_CCDC);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<TIAuditDetails> tiAuditDetails = new ArrayList<>(tIAudit.getTiAuditDetails());
//        ghi tăng
        TIIncrement tiIncrement = new TIIncrement();
//        GHi giảm
        tIAudit.setCompanyID(currentUserLoginAndOrg.get().getOrg());
        TIDecrement tiDecrement = new TIDecrement();
        tiDecrement.setDate(tIAudit.getDate());
        UserDTO user = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(user);
        tIAudit.setTypeLedger(currentBook);
        if (!utilsRepository.checkDuplicateNoVoucher(tIAudit.getNoFBook(), tIAudit.getNoMBook(), tIAudit.getTypeLedger(), tIAudit.getId())) {
            throw new BadRequestAlertException("noBookLimit", "noBookLimit", "noBookLimit");
        }
        List<TIDecrementDetails> tiDecrementDetails = new ArrayList<>();
        List<TIIncrementDetails> tiIncrementDetails = new ArrayList<>();
        for (TIAuditDetails item : tiAuditDetails) {
            if (item.getExecuteQuantity() == null) {
                item.setExecuteQuantity(BigDecimal.ZERO);
            }
            if (item.getQuantityInventory() == null) {
                item.setQuantityInventory(BigDecimal.ZERO);
            }
            if (item.getQuantityOnBook() == null) {
                item.setQuantityOnBook(BigDecimal.ZERO);
            }
            if (item.getDiffQuantity() == null) {
                item.setDiffQuantity(BigDecimal.ZERO);
            }
            if (item.getRecommendation() == null) {
                item.setRecommendation(2);
            }
            if (item.getRecommendation() == 1) { // ghi giảm
                tiDecrement.setTotalAmount(BigDecimal.ZERO);
                TIDecrementDetails tiDecrementDetail = new TIDecrementDetails();
                tiDecrementDetail.setQuantity(item.getQuantityOnBook() != null ? item.getQuantityOnBook() : BigDecimal.ZERO);
                tiDecrementDetail.setDecrementQuantity(item.getExecuteQuantity() != null ? item.getExecuteQuantity() : BigDecimal.ZERO);
                tiDecrementDetail.setToolsID(item.getToolsID());
                tiDecrementDetail.setDepartmentID(item.getDepartmentID());
                tiDecrementDetails.add(tiDecrementDetail);
            } else if (item.getRecommendation() == 0) { // ghi tăng
                TIIncrementDetails tiIncrementDetail = new TIIncrementDetails();
                tiIncrementDetail.setQuantity(item.getExecuteQuantity());
                tiIncrementDetail.setDepartmentID(item.getDepartmentID());
                tiIncrementDetail.setUnitPrice(item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO);
                if (item.getToolsID() != null) {
                    tiIncrementDetail.setToolsID(item.getToolsID());
                    Optional<Tools> tool =  toolsRepository.findById(item.getToolsID());
                    if (tool.isPresent()) {
                        tiIncrementDetail.setUnitPrice(tool.get().getUnitPrice() != null ? tool.get().getUnitPrice() : BigDecimal.ZERO);
                        tiIncrementDetail.setAmount(tiIncrementDetail.getUnitPrice().multiply(tiIncrementDetail.getQuantity()));
                    }
                }
                if (tiIncrementDetail.getUnitPrice() != null && tiIncrementDetail.getQuantity() != null) {
                    tiIncrementDetail.setAmount(tiIncrementDetail.getUnitPrice().multiply(tiIncrementDetail.getQuantity()));
                } else {
                    tiIncrementDetail.setAmount(BigDecimal.ZERO);
                }
                tiIncrement.setTotalAmount(BigDecimal.ZERO);
                tiIncrementDetails.add(tiIncrementDetail);
            }
        }
        tIAudit = tIAuditRepository.save(tIAudit);
        List<RefVoucher> refVouchers = tIAudit.getViewVouchers();
        if (refVouchers != null && refVouchers.size() > 0) {
            for (RefVoucher item : refVouchers) {
                item.setRefID1(tIAudit.getId());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            }
            refVoucherRepository.deleteByRefID1(tIAudit.getId());
            refVouchers = refVoucherRepository.saveAll(refVouchers);
            tIAudit.setViewVouchers(refVouchers);
        }
        if (tiDecrementDetails != null && tiDecrementDetails.size() > 0) {
            tiDecrement.setReason("Ghi giảm từ kiểm kê CCDC đến ngày " + tIAudit.getDate());
            for (TIDecrementDetails ti: tiDecrementDetails) {
                ti.setTiAuditID(tIAudit.getId());
            }
            tiDecrement.setTiDecrementDetails(new HashSet<>(tiDecrementDetails));
            GenCode genCode = genCodeService.findWithTypeID(53, currentBook);
            String codeVoucher = String.format("%1$s%2$s%3$s", genCode.getPrefix(), StringUtils.leftPad(String.valueOf(genCode.getCurrentValue() + 1), genCode.getLength(),
                '0'), genCode.getSuffix() == null ? "" : genCode.getSuffix());
            if (currentBook.equals(1)) {
                tiDecrement.setNoMBook(codeVoucher);
            } else {
                tiDecrement.setNoFBook(codeVoucher);
            }
            tiDecrement = tiDecrementService.save(tiDecrement);
            if (tiDecrement.getId() != null) {
                tIAudit.setTiDecrementID(tiDecrement.getId());
                tIAudit.setNoBookDecrement(codeVoucher);
            }
        }
        if (tiIncrementDetails != null && tiIncrementDetails.size() > 0) {
            for (TIIncrementDetails ti: tiIncrementDetails) {
                ti.setTiAuditID(tIAudit.getId());
            }
            tiIncrement.setDate(tIAudit.getDate());
            tiIncrement.setReason("Ghi tăng từ kiểm kê CCDC đến ngày " + tIAudit.getDate());
            tiIncrement.setTiIncrementDetails(new HashSet<>(tiIncrementDetails));
            GenCode genCode = genCodeService.findWithTypeID(52, currentBook);
            String codeVoucher = String.format("%1$s%2$s%3$s", genCode.getPrefix(), StringUtils.leftPad(String.valueOf(genCode.getCurrentValue() + 1), genCode.getLength(),
                '0'), genCode.getSuffix() == null ? "" : genCode.getSuffix());
            if (currentBook.equals(1)) {
                tiIncrement.setNoMBook(codeVoucher);
            } else {
                tiIncrement.setNoFBook(codeVoucher);
            }
            tiIncrement = tiIncrementService.save(tiIncrement);
            if (tiIncrement.getId() != null) {
                tIAudit.setTiIncrementID(tiIncrement.getId());
                tIAudit.setNoBookIncrement(codeVoucher);
            }

        }
        tIAudit = tIAuditRepository.save(tIAudit);
        return tIAudit;
    }

    /**
     * Get all the tIAudits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TIAudit> findAll(Pageable pageable) {
        log.debug("Request to get all TIAudits");
        return tIAuditRepository.findAll(pageable);
    }


    /**
     * Get one tIAudit by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TIAudit> findOne(UUID id) {
        log.debug("Request to get TIAudit : {}", id);
        return tIAuditRepository.findById(id);
    }

    /**
     * Delete the tIAudit by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete TIAudit : {}", id);
        Optional<TIAudit> tiAudit = tIAuditRepository.findById(id);
        UUID tiIncrementID =  tIAuditRepository.getTIIncrement(id);
        UUID tiDecrementID =  tIAuditRepository.getTIDecrement(id);
        if (tiIncrementID != null) {
            tiIncrementService.delete(Utils.uuidConvertToGUID(tiIncrementID));
        }
        if(tiDecrementID != null) {
            tiDecrementService.delete(Utils.uuidConvertToGUID(tiDecrementID));
        }
        tIAuditRepository.deleteById(id);
    }

    @Override
    public List<ToolsDetailsTiAuditConvertDTO> getTIAudit(String date) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        String checkPeriodic = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_PBCCDC)).findAny().get().getData();
//        List<TIAllocationDetailConvertDTO> tiAllocationDetailConvertDTOS = tIAllocationRepository.getTIAllocationDetails(currentUserLoginAndOrg.get().getOrg(), month, year, date, checkPeriodic);
//        if (!tiAllocationDetailConvertDTOS.isEmpty()) {
//            for (int i = 0; i < tiAllocationDetailConvertDTOS.size(); i++) {
//                List<ToolsDetailsConvertDTO> toolsDetailsConvertDTOS = toolsRepository.getToolsDetailsByID(tiAllocationDetailConvertDTOS.get(i).getToolsID());
//                tiAllocationDetailConvertDTOS.get(i).setToolsDetailsConvertDTOS(toolsDetailsConvertDTOS);
//            }
//        }
        UserDTO user = userService.getAccount();
        Integer currentBook = Utils.PhienSoLamViec(user);
        return tIAuditRepository.getTIAuditDetails(currentUserLoginAndOrg.get().getOrg(), date, checkPeriodic, currentBook);
    }

    @Override
    public List<ToolsDetailsTiAuditConvertDTO> getAllToolsByTiAuditID(UUID id) {
        return tIAuditRepository.getAllToolsByTiAuditID(id);
    }

    @Override
    public TIAuditDetailAllDTO findDetailsByID(UUID id) {
//        tIAuditRepository
        List<TIAuditDetailByIDDTO> tiAuditDetailByIDDTOS = tIAuditRepository.findDetailsByID(id);
        List<TIAuditMemberDetailByIDDTO> tiAuditMemberDetails = tiAuditMemberDetailsRepository.findAllByTiAuditID(id);
        UserDTO user = userService.getAccount();
        boolean isNoMBook = Utils.PhienSoLamViec(user).equals(1);
        List<RefVoucherDTO> refVouchers = refVoucherRepository.getRefViewVoucher(id, isNoMBook);

        TIAuditDetailAllDTO tiAuditDetailAllDTO = new TIAuditDetailAllDTO();
        tiAuditDetailAllDTO.setTiAuditDetailByIDDTOS(tiAuditDetailByIDDTOS);
        tiAuditDetailAllDTO.setTiAuditMemberDetailDTOS(tiAuditMemberDetails);
        tiAuditDetailAllDTO.setRefVouchers(refVouchers);
        return tiAuditDetailAllDTO;
    }

    @Override
    public Page<TIAuditConvertDTO> getAllTIAuditSearch(Pageable pageable, String fromDate, String toDate, String keySearch) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return tIAuditRepository.getAllTIAuditSearch(pageable, currentUserLoginAndOrg.get().getOrg(), fromDate, toDate, keySearch, currentBook);
    }

    @Override
    public Optional<TIAudit> findByRowNum(Pageable pageable, String fromDate, String toDate, String keySearch, Integer rowNum) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO user = userService.getAccount();
        boolean currentBook = Utils.PhienSoLamViec(user).equals(1);
        return tIAuditRepository.findByRowNum(pageable, currentUserLoginAndOrg.get().getOrg(), fromDate, toDate, rowNum, keySearch, currentBook);
    }

    @Override
    public HandlingResultDTO multiDelete(List<TIAudit> tiAudits) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        UserDTO userDTO = userService.getAccount();
//        tIIncrementRepository.findAllByIdIn(listID);
        handlingResultDTO.setCountTotalVouchers(tiAudits.size());
        List<TIAudit> listDelete = tiAudits.stream().collect(Collectors.toList());
        List<ViewVoucherNo> viewVoucherNoListFail = new ArrayList<ViewVoucherNo>();
        LocalDate dateClosed = LocalDate.now();
        String closeDateStr = userDTO.getSystemOption().stream().filter(x -> x.getCode().equals(Constants.SystemOption.DBDateClosed)).findFirst().get().getData();
        if (!closeDateStr.equals("") && closeDateStr != null) {
            dateClosed = LocalDate.parse(closeDateStr);
        }
//        for (TIAudit pp: tiAudits) {
//            if (pp.getrr() != null && pp.getRecorded()) {
//                ViewVoucherNo viewVoucherNo = new ViewVoucherNo();
//                viewVoucherNo.setReasonFail("Chứng từ đang ghi sổ!");
//                viewVoucherNo.setPostedDate(pp.getDate());
//                viewVoucherNo.setDate(pp.getDate());
//                viewVoucherNo.setRefID(pp.getId());
//                viewVoucherNo.setNoFBook(pp.getNoFBook());
//                viewVoucherNo.setNoMBook(pp.getNoMBook());
//                viewVoucherNo.setTypeName("Ghi tăng CCDC");
//                viewVoucherNoListFail.add(viewVoucherNo);
//                listDelete.remove(pp);
//            }
//        }

        List<UUID> uuidList = new ArrayList<>();
        for (int i = 0; i < listDelete.size(); i++) {
            uuidList.add(listDelete.get(i).getId());
        }
        int count = 0;
        if (uuidList.size() > 0) {
            count = refVoucherRepository.countAllByRefID1sOrRefID2s(uuidList);
            if (count > 0) {
                refVoucherRepository.deleteByRefID1sOrRefID2s(uuidList);
            }
//            tIIncrementRepository.updateUnrecord(uuidList);
            tIAuditRepository.deleteList(uuidList);
            // xóa nhiều dòng ghi tăng
            // xóa nhiều dòng ghi giảm
        }
        handlingResultDTO.setCountFailVouchers(viewVoucherNoListFail.size());
        handlingResultDTO.setCountSuccessVouchers(uuidList.size());
        handlingResultDTO.setListFail(viewVoucherNoListFail);

        return handlingResultDTO;
    }

    @Override
    public void deleteRelationShip(UUID id) {
        UUID tiIncrementID =  tIAuditRepository.getTIIncrement(id);
        UUID tiDecrementID =  tIAuditRepository.getTIDecrement(id);
        if (tiIncrementID != null) {
            tiIncrementService.delete(Utils.uuidConvertToGUID(tiIncrementID));
        }
        if(tiDecrementID != null) {
            tiDecrementService.delete(Utils.uuidConvertToGUID(tiDecrementID));
        }
    }

}
