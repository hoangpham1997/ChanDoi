package vn.softdreams.ebweb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.repository.RefVoucherRepository;
import vn.softdreams.ebweb.repository.UtilsRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.SAQuoteService;
import vn.softdreams.ebweb.repository.SAQuoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UnitService;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.UtilsService;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.UnitConvertRateDTO;
import vn.softdreams.ebweb.service.dto.UserSystemOption;
import vn.softdreams.ebweb.service.dto.ViewSAQuoteDTO;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.ExcelConstant;
import vn.softdreams.ebweb.service.util.ExcelUtils;
import vn.softdreams.ebweb.service.util.PdfUtils;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.SAQuoteDTO;
import vn.softdreams.ebweb.web.rest.dto.SAQuoteSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.SAQuoteViewDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SAQuote.
 */
@Service
@Transactional
public class SAQuoteServiceImpl implements SAQuoteService {

    private final Logger log = LoggerFactory.getLogger(SAQuoteServiceImpl.class);

    private final SAQuoteRepository sAQuoteRepository;
    private final RefVoucherRepository refVoucherRepository;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final UserService userService;
    private final UtilsService utilsService;
    private final UnitService unitService;

    @Autowired
    UtilsRepository utilsRepository;

    public SAQuoteServiceImpl(SAQuoteRepository sAQuoteRepository,
                              RefVoucherRepository refVoucherRepository,
                              OrganizationUnitRepository organizationUnitRepository, UserService userService,
                              UtilsService utilsService, UnitService unitService
    ) {
        this.sAQuoteRepository = sAQuoteRepository;
        this.refVoucherRepository = refVoucherRepository;
        this.organizationUnitRepository = organizationUnitRepository;
        this.userService = userService;
        this.utilsService = utilsService;
        this.unitService = unitService;
    }

    /**
     * Save a sAQuoteSaveDTO.
     *
     * @param sAQuote the entity to save
     * @return the persisted entity
     */
    @Override
    public SAQuoteSaveDTO save(SAQuote sAQuote, Boolean isNew) {
        log.debug("Request to save SAQuote : {}", sAQuote);
        SAQuoteSaveDTO sAQuoteSaveDTO = new SAQuoteSaveDTO();
        SAQuote sAQ = new SAQuote();
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            sAQuote.setCompanyId(currentUserLoginAndOrg.get().getOrg());
            if (isNew) {
                if (!utilsService.checkQuantityLimitedNoVoucher()) {
                    throw new BadRequestAlertException("Số chứng từ hiện tại đã vượt quá số cho phép của Gói sử dụng", "", "noVoucherLimited");
                }
            }
            if (!utilsRepository.checkDuplicateNoVoucher(sAQuote.getNo(), sAQuote.getNo(), 2, sAQuote.getId())) {
                sAQuoteSaveDTO.setsAQuote(sAQuote);
                sAQuoteSaveDTO.setStatus(1);
                return sAQuoteSaveDTO;
            }
            sAQ = sAQuoteRepository.save(sAQuote);
//            Boolean resultUpdateGenCode = utilsRepository.updateGencode(sAQuote.getNo(), sAQuote.getNo(), 30, 2);
//            if (!resultUpdateGenCode) {
//                // update gencode that bai
//                sAQuoteSaveDTO.setsAQuote(sAQuote);
//                sAQuoteSaveDTO.setStatus(2);
//                return sAQuoteSaveDTO;
//            }
            List<RefVoucher> refVouchers = new ArrayList<>();
            for (RefVoucherDTO item : sAQuote.getViewVouchers() != null ? sAQuote.getViewVouchers() : new ArrayList<RefVoucherDTO>()) {
                RefVoucher refVoucher = new RefVoucher();
                refVoucher.setCompanyID(sAQ.getCompanyId());
                refVoucher.setRefID1(sAQ.getId());
                refVoucher.setRefID2(item.getRefID2());
                item.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                refVouchers.add(refVoucher);
            }
            refVoucherRepository.deleteByRefID1(sAQ.getId());
            refVoucherRepository.saveAll(refVouchers);
            sAQ.setViewVouchers(sAQuote.getViewVouchers());
            sAQuoteSaveDTO.setsAQuote(sAQ);
            sAQuoteSaveDTO.setStatus(0);
            return sAQuoteSaveDTO;
        }
        throw new BadRequestAlertException("Khong the bao gia", "", "");
    }

    /**
     * Get all the sAQuotes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SAQuote> findAll(Pageable pageable) {
        log.debug("Request to get all SAQuotes");
        return sAQuoteRepository.findAll(pageable);
    }

    @Override
    public Page<ViewSAQuoteDTO> findAllView(Pageable pageable) {

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SAQuoteDTO> findAllData(Pageable pageable) {
        log.debug("Request to get all SAQuotes");
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return sAQuoteRepository.findAllData(pageable, currentUserLoginAndOrg.get().getOrg());
    }

    /**
     * Get one sAQuote by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SAQuote> findOne(UUID id) {
        log.debug("Request to get SAQuote : {}", id);
        Optional<SAQuote> sAQuote = sAQuoteRepository.findOneById(id);
        List<UnitConvertRateDTO> units = new ArrayList<>();
        sAQuote.get().getsAQuoteDetails().stream().forEach(n -> {
            n.setUnits(unitService.convertRateForMaterialGoodsComboboxCustom(n.getMaterialGoods().getId()));
        });
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            boolean isNoMBook = !collect.isEmpty() && collect.get(0).getCode().equalsIgnoreCase("1");
            List<RefVoucherDTO> dtos = refVoucherRepository.getRefViewVoucher(id, isNoMBook);
            sAQuote.get().setViewVouchers(dtos);
        }
        return sAQuote;
    }

    @Override
    @Transactional(readOnly = true)
    public SAQuoteViewDTO findOneByID(UUID id) {
        log.debug("Request to get SaBill : {}", id);
        SAQuoteViewDTO dto = new SAQuoteViewDTO();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent()) {
            List<vn.softdreams.ebweb.domain.SystemOption> collect = userWithAuthoritiesAndSystemOption.get().getSystemOptions().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(Constants.SystemOption.PHIEN_SoLamViec)).collect(Collectors.toList());
            boolean isNoMBook = !collect.isEmpty() && collect.get(0).getCode().equalsIgnoreCase("1");
            List<RefVoucherDTO> dtos = refVoucherRepository.getRefViewVoucher(id, isNoMBook);
            dto.setViewVouchers(dtos);
        }
        Optional<SAQuote> byId = sAQuoteRepository.findOneById(id);
        if (byId.isPresent()) {
            dto.setsAQuote(byId.get());
            return dto;
        }
        return null;
    }

    /**
     * Delete the sAQuote by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete SAQuote : {}", id);
        sAQuoteRepository.deleteById(id);
        sAQuoteRepository.deleteRefSAInvoiceAndRSInwardOutward(id);
        refVoucherRepository.deleteByRefID1(id);
        refVoucherRepository.deleteByRefID2(id);
    }

    @Override
    public HandlingResultDTO deleteList(List<UUID> uuids) {
        log.debug("Request to delete SAQuote : {}", uuids);
        HandlingResultDTO handlingResultDTO = new HandlingResultDTO();
        List<String> lstFail = sAQuoteRepository.getIDRef(uuids);
        List<UUID> uuidsFail = new ArrayList<>();
        for (String id : lstFail) {
            uuidsFail.add(Utils.uuidConvertToGUID(UUID.fromString(id)));
        }
        List<UUID> uuidListDelete = uuids.stream().filter(n -> uuidsFail.stream().noneMatch(m -> m.compareTo(n) == 0)).collect(Collectors.toList());
        if (uuidListDelete.size() > 0){
            sAQuoteRepository.deleteByListID(uuidListDelete);
        }
        handlingResultDTO.setListIDFail(uuidsFail);
        handlingResultDTO.setCountFailVouchers(uuidsFail.size());
        handlingResultDTO.setCountTotalVouchers(uuids.size());
        handlingResultDTO.setCountSuccessVouchers(uuidListDelete.size());
        return handlingResultDTO;
    }

    /**
     * @author mran
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SAQuote> findAll(Pageable pageable, SearchVoucher searchVoucher) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return sAQuoteRepository.findAll(pageable, searchVoucher, currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }

    /**
     * @return
     * @author mran
     */
    @Override
    @Transactional(readOnly = true)
    public SAQuote findOneByRowNum(SearchVoucher searchVoucher, Number rowNum) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return sAQuoteRepository.findOneByRowNum(searchVoucher, rowNum, currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Number> getIndexRow(UUID id, SearchVoucher searchVoucher) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return sAQuoteRepository.getIndexRow(id, searchVoucher, currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public Page<ViewSAQuoteDTO> getViewSAQuoteDTOPopup(Pageable pageable, UUID accountingObjectID, String fromDate, String toDate, String currencyID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return sAQuoteRepository.findAllView(pageable, currentUserLoginAndOrg.get().getOrg(), accountingObjectID, fromDate, toDate, currencyID);
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public byte[] exportPdf(SearchVoucher searchVoucher1) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            List<SAQuote> sAQuotes = sAQuoteRepository.findAllForExport(searchVoucher1, currentUserLoginAndOrg.get().getOrg());
            return PdfUtils.writeToFile(sAQuotes, ExcelConstant.SAQuote.HEADER, ExcelConstant.SAQuote.FIELD);
        }
        return null;
    }

    @Override
    public byte[] exportExcel(SearchVoucher searchVoucher1) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
        if (userWithAuthoritiesAndSystemOption.isPresent() && currentUserLoginAndOrg.isPresent()) {
            List<SAQuote> sAQuotes = sAQuoteRepository.findAllForExport(searchVoucher1, currentUserLoginAndOrg.get().getOrg());
            return ExcelUtils.writeToFile(sAQuotes, ExcelConstant.SAQuote.NAME, ExcelConstant.SAQuote.HEADER, ExcelConstant.SAQuote.FIELD);
        }
        return null;
    }

    @Override
    public Boolean checkRelateVoucher(UUID id) {
        if (sAQuoteRepository.checkRelateVoucherSAInvoice(id) > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean deleteRefSAInvoiceAndRSInwardOutward(UUID id) {
        sAQuoteRepository.deleteRefSAInvoiceAndRSInwardOutward(id);
        return true;
    }
}
