package vn.softdreams.ebweb.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.PPOrder;
import vn.softdreams.ebweb.domain.PPOrderDetail;
import vn.softdreams.ebweb.domain.RefVoucher;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.PporderService;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.dto.PPOrderDTO;
import vn.softdreams.ebweb.service.dto.ResultDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.dto.UserSystemOption;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.ExcelConstant;
import vn.softdreams.ebweb.service.util.ExcelUtils;
import vn.softdreams.ebweb.service.util.PdfUtils;
import vn.softdreams.ebweb.web.rest.dto.*;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.TypeConstant.DON_MUA_HANG;

/**
 * Service Implementation for managing PPOrder.
 */
@Service
@Transactional
public class PporderServiceImpl implements PporderService {

    private final Logger log = LoggerFactory.getLogger(PporderServiceImpl.class);

    private final PporderRepository pporderRepository;

    private final PPInvoiceRepository ppInvoiceRepository;

    private final RefVoucherRepository refVoucherRepository;

    private final UserService userService;

    @Autowired
    OrganizationUnitRepository organizationUnitRepository;

    @Autowired
    UtilsRepository utilsRepository;

    public PporderServiceImpl(PporderRepository pporderRepository, PPInvoiceRepository ppInvoiceRepository, RefVoucherRepository refVoucherRepository, UserService userService) {
        this.pporderRepository = pporderRepository;
        this.ppInvoiceRepository = ppInvoiceRepository;
        this.refVoucherRepository = refVoucherRepository;
        this.userService = userService;
    }

    /**
     * Save a pporder.
     *
     * @param pporder the entity to save
     * @return the persisted entity
     */
    @Override
    public PPOrder save(PPOrder pporder) {
        log.debug("Request to save PPOrder : {}", pporder);
        return pporderRepository.save(pporder);
    }

    @Override
    public PPOrderSaveDTO save(PPOrderSaveDTO ppOrderSaveDTO) {
        log.debug("Request to save PPOrderSaveDTO : {}", ppOrderSaveDTO);
        PPOrder ppOrder = ppOrderSaveDTO.getPpOrder();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            ppOrder.setTypeId(DON_MUA_HANG);
            ppOrder.setCompanyId(currentUserLoginAndOrg.get().getOrg());
            if (!utilsRepository.checkDuplicateNoVoucher(null, ppOrder.getNo(), 2, ppOrder.getId())) {
                throw new BadRequestAlertException("Trùng số đơn hàng", "pporder", "duplicateNo");
            }

//            int orderPriority = 0;
            for (PPOrderDetail item : ppOrder.getPpOrderDetails()) {
                item.setUnitPriceOriginal(item.getUnitPriceOriginal() != null ? item.getUnitPriceOriginal() : BigDecimal.ZERO);
                item.setUnitPrice(item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO);
                item.setMainUnitPrice(item.getMainUnitPrice() != null ? item.getMainUnitPrice() : BigDecimal.ZERO);
                item.setAmount(item.getAmount() != null ? item.getAmount() : BigDecimal.ZERO);
                item.setAmountOriginal(item.getAmountOriginal() != null ? item.getAmountOriginal() : BigDecimal.ZERO);
//                item.setOrderPriority(orderPriority);
//                orderPriority++;
            }

            if (ppOrder.getId() != null) {
                Optional<PPOrder> ppOrderOld = pporderRepository.findById(ppOrder.getId());
                PPOrder finalPpOrder = ppOrder;
                ppOrderOld.ifPresent(item -> {
                    List<PPOrderDetail> detailsOld = new ArrayList<>(item.getPpOrderDetails());
                    List<PPOrderDetail> details = new ArrayList<>(finalPpOrder.getPpOrderDetails());
                    Map<UUID, PPOrderDetail> hashMap = new HashMap<>();
                    details.forEach(detail -> hashMap.put(detail.getId(), detail));
                    detailsOld.forEach(detailOld -> {
                        List<UUID> deletedIDs = new ArrayList<>();
                        if (hashMap.get(detailOld.getId()) == null) {
                            deletedIDs.add(detailOld.getId());
                        }
                        pporderRepository.updateReferences(deletedIDs);
                    });
                });
            }
            ppOrder = pporderRepository.save(ppOrder);

            List<RefVoucher> refVouchers = ppOrderSaveDTO.getViewVouchers();
            for (RefVoucher item : refVouchers) {
                item.setRefID1(ppOrder.getId());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            }
            refVoucherRepository.deleteByRefID1(ppOrder.getId());
            refVouchers = refVoucherRepository.saveAll(refVouchers);
            ppOrderSaveDTO.setPpOrder(ppOrder);
            ppOrderSaveDTO.setViewVouchers(refVouchers);
            return ppOrderSaveDTO;
        }
        throw new BadRequestAlertException("Khong the tao don mua hang", "", "");
    }

    /**
     * Get all the pporders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PPOrder> findAll(Pageable pageable) {
        log.debug("Request to get all Pporders");
        return pporderRepository.findAll(pageable);
    }


    /**
     * Get one pporder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PPOrder> findOne(UUID id) {
        log.debug("Request to get PPOrder : {}", id);
        return pporderRepository.findById(id);
    }

    /**
     * Delete the pporder by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete PPOrder : {}", id);
        pporderRepository.deleteById(id);
    }

    @Override
    public Page<PPOrder> searchAll(Pageable pageable, String searchDTO) throws IOException {
        return pporderRepository.searchAll(pageable, parseJSON(searchDTO));
    }

    private PPOrderSearchDTO parseJSON(String searchDTO) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.readValue(searchDTO, PPOrderSearchDTO.class);
    }

    @Override
    public byte[] exportPdf(String searchDTO) throws IOException {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            Page<PPOrder> ppOrders = pporderRepository.searchAll(null, parseJSON(searchDTO));
            List<PPOrderExportDTO> exportDTO = ppOrders.getContent()
                .stream()
                .map(PPOrderExportDTO::new)
                .collect(Collectors.toList());
            UserDTO userDTO = userService.getAccount();
            return PdfUtils.writeToFile(exportDTO, ExcelConstant.PPOrder.HEADER, ExcelConstant.PPOrder.FIELD, userDTO);
        }
        return null;
    }

    @Override
    public byte[] exportExcel(String searchDTO) throws IOException {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            Page<PPOrder> ppOrders = pporderRepository.searchAll(null, parseJSON(searchDTO));
            List<PPOrderExportDTO> exportDTO = ppOrders.getContent()
                .stream()
                .map(PPOrderExportDTO::new)
                .collect(Collectors.toList());
            UserDTO userDTO = userService.getAccount();
            return ExcelUtils.writeToFile(exportDTO, ExcelConstant.PPOrder.NAME, ExcelConstant.PPOrder.HEADER, ExcelConstant.PPOrder.FIELD, userDTO);
        }
        return null;
    }

    @Override
    public List<RefVoucherDTO> refVouchersByPPOrderID(UUID id) {
        log.debug("Request to get refVouchersByPPOrderID : {}", id);
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            boolean isNoMBook = !collect.isEmpty() && collect.get(0).getCode().equalsIgnoreCase("1");
            return refVoucherRepository.getRefViewVoucher(id, isNoMBook);
        }
        return new ArrayList<>();
    }

    @Override
    public Page<PPOrderDTO> findAllPPOrderDTO(Pageable pageable, UUID accountingObject, String fromDate, String toDate, Integer type, List<UUID> itemsSelected, String currency) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return pporderRepository.findAllPPOrderDTO(pageable, accountingObject, fromDate, toDate, type, itemsSelected, currency, currentUserLoginAndOrg.get().getOrg());
    }

    @Override
    public Integer findByRowNumByID(String searchDTO, UUID id) throws IOException {
        return pporderRepository.findByRowNumByID(parseJSON(searchDTO), id);
    }

    @Override
    public ResultDTO validateDelete(UUID id) {
//        int count = pporderRepository.checkReferences(id);
//        if (count > 0) {
//            return new ResultDTO("duplicate");
//        }
        pporderRepository.deleteById(id);
        refVoucherRepository.deleteByRefID1(id);
        return new ResultDTO("success");
    }

    @Override
    public ResultDTO deletePPOrderAndReferences(UUID id) {
        try {
            pporderRepository.deleteById(id);
            pporderRepository.updateReferencesByPPOrderID(id);
            refVoucherRepository.deleteByRefID1(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultDTO("error");
        }
        return new ResultDTO("success");
    }

    @Override
    public ResultDTO checkReferencesCount(UUID id) {
        int count = pporderRepository.checkReferences(id);
        if (count > 0) {
            return new ResultDTO("duplicate");
        }
        return new ResultDTO("success");
    }

    @Override
    public ResultDTO deleteReferences(UUID id) {
        try {
            pporderRepository.updateReferencesByPPOrderID(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultDTO("error");
        }
        return new ResultDTO("success");
    }

    @Override
    public Number findTotalSum(Pageable pageable, String searchDTO) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return pporderRepository.findTotalSum(currentUserLoginAndOrg.get().getOrg());
    }

    @Override
    public HandlingResultDTO multiDelete(List<PPOrder> ppOrders) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        handlingResultDTO.setCountTotalVouchers(ppOrders.size());
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<PPOrder> listDelete = ppOrders.stream().collect(Collectors.toList());
        List<UUID> uuidList_DMH = new ArrayList<>();
        for (int i = 0; i < listDelete.size(); i++) {
            if (listDelete.get(i).getTypeId() == Constants.PPOrder.TYPE_DON_MUA_HANG) {
                uuidList_DMH.add(listDelete.get(i).getId());
            }
        }
        handlingResultDTO.setCountSuccessVouchers(listDelete.size());
        if (uuidList_DMH.size() > 0) {
            pporderRepository.multiDeletePPOrder(currentUserLoginAndOrg.get().getOrg(), uuidList_DMH);
            pporderRepository.multiDeleteChildPPOrder("PPOrderDetail", uuidList_DMH);
            pporderRepository.updateReferencesByPPOrderListID(uuidList_DMH);
        }
        return handlingResultDTO;
    }

    @Override
    public ResultDTO validateCheckDuplicat(UUID id) {
        int count = pporderRepository.checkReferences(id);
        if (count > 0) {
            return new ResultDTO("duplicate");
        }
        return new ResultDTO();
    }

    @Override
    public PPOrder findByRowNum(String searchDTO, Integer rowNum) throws IOException {
        return pporderRepository.findByRowNum(parseJSON(searchDTO), rowNum);
    }
}
