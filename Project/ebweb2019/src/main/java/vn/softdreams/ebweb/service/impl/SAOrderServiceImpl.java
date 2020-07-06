package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.RefVoucher;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.repository.RefVoucherRepository;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.SAOrderService;
import vn.softdreams.ebweb.domain.SAOrder;
import vn.softdreams.ebweb.repository.SAOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.UtilsService;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.SAOrderSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.SAOrderViewDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SAOrder.
 */
@Service
@Transactional
public class SAOrderServiceImpl implements SAOrderService {

    private final Logger log = LoggerFactory.getLogger(SAOrderServiceImpl.class);

    private final SAOrderRepository sAOrderRepository;
    private final UtilsRepository utilsRepository;
    private final RefVoucherRepository refVoucherRepository;
    private final UserService userService;
    private final UtilsService utilsService;

    private final OrganizationUnitRepository organizationUnitRepository;

    public SAOrderServiceImpl(SAOrderRepository sAOrderRepository,
                              UserService userService,
                              UtilsRepository utilsRepository,
                              RefVoucherRepository refVoucherRepository,
                              OrganizationUnitRepository organizationUnitRepository,
                              UtilsService utilsService) {
        this.sAOrderRepository = sAOrderRepository;
        this.userService = userService;
        this.utilsRepository = utilsRepository;
        this.refVoucherRepository = refVoucherRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.utilsService = utilsService;
    }

    /**
     * Save a sAOrder.
     *
     * @param sAOrder the entity to save
     * @return the persisted entity
     */
    @Override
    public SAOrder save(SAOrder sAOrder) {
        log.debug("Request to save SAOrder : {}", sAOrder);
        return sAOrderRepository.save(sAOrder);
    }

    /**
     * Get all the sAOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SAOrder> findAll(Pageable pageable) {
        log.debug("Request to get all SAOrders");
        return sAOrderRepository.findAll(pageable);
    }


    /**
     * Get one sAOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SAOrder> findOne(UUID id) {
        log.debug("Request to get SAOrder : {}", id);
        Optional<SAOrder> saOrder = sAOrderRepository.findById(id);
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            boolean isNoMBook = !collect.isEmpty() && collect.get(0).getCode().equalsIgnoreCase("1");
            List<RefVoucherDTO> dtos = refVoucherRepository.getRefViewVoucher(id, isNoMBook);
            saOrder.get().setViewVouchers(dtos);
        }
        return saOrder;
    }

    /**
     * Delete the sAOrder by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete SAOrder : {}", id);
        sAOrderRepository.deleteById(id);
        refVoucherRepository.deleteByRefID1(id);
        refVoucherRepository.deleteByRefID2(id);
        sAOrderRepository.deleteRefSAInvoiceAndRSInwardOutward(id);
    }

    @Override
    public HandlingResultDTO delete(List<UUID> uuids) {
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        List<String> lstFail = sAOrderRepository.getIDRef(uuids);
        List<UUID> uuidsFail = new ArrayList<>();
        for (String id : lstFail) {
            uuidsFail.add(Utils.uuidConvertToGUID(UUID.fromString(id)));
        }
        List<UUID> uuidListDelete = uuids.stream().filter(n -> uuidsFail.stream().noneMatch(m -> m.compareTo(n) == 0)).collect(Collectors.toList());
        if (uuidListDelete.size() > 0){
            sAOrderRepository.deleteByListID(uuidListDelete);
        }
        handlingResultDTO.setListIDFail(uuidsFail);
        handlingResultDTO.setCountFailVouchers(uuidsFail.size());
        handlingResultDTO.setCountTotalVouchers(uuids.size());
        handlingResultDTO.setCountSuccessVouchers(uuidListDelete.size());
        return handlingResultDTO;
    }

    @Override
    public Page<SAOrderDTO> findAllDTOByCompanyID(Pageable pageable) {
        log.debug("Request to get all MCReceiptsDTO by CompanyID");
        UserDTO userDTO = userService.getAccount();
        return sAOrderRepository.findAllDTOByCompanyID(pageable, userDTO.getOrganizationUnit().getId());
    }

    @Override
    public SAOrderViewDTO findOneDTO(UUID id) {
        return null;
    }

    @Override
    public SAOrderSaveDTO saveDTO(SAOrder sAOrder) {
        log.debug("Request to save SAOrder : {}", sAOrder);
        if (sAOrder.getId() == null && !utilsService.checkQuantityLimitedNoVoucher()) {
            throw new BadRequestAlertException("Số chứng từ hiện tại đã vượt quá số cho phép của Gói sử dụng", "", "noVoucherLimited");
        }
        SAOrderSaveDTO saOrderSaveDTO = new SAOrderSaveDTO();
        SAOrder sa = new SAOrder();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        UserDTO userDTO = userService.getAccount();
        if (!utilsRepository.checkDuplicateNoVoucher(sAOrder.getNo(), sAOrder.getNo(), 2, sAOrder.getId())) {
            saOrderSaveDTO.setsAOrder(sAOrder);
            saOrderSaveDTO.setStatus(1);
            return saOrderSaveDTO;
        }
        if (currentUserLoginAndOrg.isPresent()) {
            sAOrder.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            sa = sAOrderRepository.save(sAOrder);
            utilsRepository.updateGencode(sAOrder.getNo(), sAOrder.getNo(), 31, 2);
            List<RefVoucher> refVouchers = new ArrayList<>();
            for (RefVoucherDTO item : sAOrder.getViewVouchers() != null ? sAOrder.getViewVouchers() : new ArrayList<RefVoucherDTO>()) {
                RefVoucher refVoucher = new RefVoucher();
                refVoucher.setCompanyID(sa.getCompanyID());
                refVoucher.setRefID1(sa.getId());
                refVoucher.setRefID2(item.getRefID2());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                refVouchers.add(refVoucher);
            }
            refVoucherRepository.deleteByRefID1(sa.getId());
            refVoucherRepository.saveAll(refVouchers);
            sa.setViewVouchers(sAOrder.getViewVouchers());
            /*if (userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(TCKHAC_GhiSo)).findAny().get().getData().equals("0")) {
                String msg = "";
                if (!generalLedgerService.record(mcP, msg)) {
                    mcPaymentSaveDTO.setStatus(2);
                } else {
                    mcP.setRecorded(true);
                    mCPaymentRepository.save(mcP);
                }
            }*/
            saOrderSaveDTO.setsAOrder(sa);
            saOrderSaveDTO.setStatus(0);
            return saOrderSaveDTO;
        }
        throw new BadRequestAlertException("Không thể lưu Đơn đặt hàng", "", "");
    }

    @Override
    public Page<SAOrderDTO> getViewSAOrderDTOPopup(Pageable pageable, UUID accountingObjectID, String fromDate, String toDate, String currencyID, UUID objectId) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return sAOrderRepository.findAllView(pageable, currentUserLoginAndOrg.get().getOrg(), accountingObjectID, fromDate, toDate, currencyID, objectId);
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public Boolean checkRelateVoucher(UUID id) {
        if (sAOrderRepository.checkRelateVoucherSAInvoice(id) > 0) {
            return true;
        } else if (sAOrderRepository.checkRelateVoucherRSInwardOutward(id) > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean deleteRefSAInvoiceAndRSInwardOutward(UUID id) {
        sAOrderRepository.deleteRefSAInvoiceAndRSInwardOutward(id);
        return true;
    }
}
