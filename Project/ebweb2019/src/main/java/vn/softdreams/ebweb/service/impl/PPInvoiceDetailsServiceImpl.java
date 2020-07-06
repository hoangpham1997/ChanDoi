package vn.softdreams.ebweb.service.impl;

import vn.softdreams.ebweb.domain.ReceiveBill;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.PPInvoiceDetailsService;
import vn.softdreams.ebweb.domain.PPInvoiceDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.dto.PPInvoiceConvertDTO;
import vn.softdreams.ebweb.service.dto.ResultDTO;
import vn.softdreams.ebweb.service.dto.UserSystemOption;
import vn.softdreams.ebweb.web.rest.dto.LotNoDTO;

import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.util.Constants.SystemOption.TCKHAC_SDDMVTHH;

/**
 * Service Implementation for managing PPInvoiceDetails.
 */
@Service
@Transactional
public class PPInvoiceDetailsServiceImpl implements PPInvoiceDetailsService {

    private final Logger log = LoggerFactory.getLogger(PPInvoiceDetailsServiceImpl.class);

    private final PPInvoiceDetailsRepository pPInvoiceDetailsRepository;
    private final PPInvoiceRepository ppInvoiceRepository;
    private final PPServiceRepository ppServiceRepository;
    private final PPServiceDetailRepository ppServiceDetailRepository;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final UnitRepository unitRepository;
    private final UserService userService;
    private final PporderRepository pporderRepository;
    private final SystemOptionRepository systemOptionRepository;

    private final String PHIEN_SoLamViec = "PHIEN_SoLamViec";

    public PPInvoiceDetailsServiceImpl(PPInvoiceDetailsRepository pPInvoiceDetailsRepository, UnitRepository unitRepository, UserService userService,
                                       PPInvoiceRepository ppInvoiceRepository,
                                       PPServiceRepository ppServiceRepository,
                                       PPServiceDetailRepository ppServiceDetailRepository, OrganizationUnitRepository organizationUnitRepository, PporderRepository pporderRepository, SystemOptionRepository systemOptionRepository) {
        this.pPInvoiceDetailsRepository = pPInvoiceDetailsRepository;
        this.unitRepository = unitRepository;
        this.ppInvoiceRepository = ppInvoiceRepository;
        this.ppServiceRepository = ppServiceRepository;
        this.ppServiceDetailRepository = ppServiceDetailRepository;
        this.userService = userService;
        this.organizationUnitRepository = organizationUnitRepository;
        this.pporderRepository = pporderRepository;
        this.systemOptionRepository = systemOptionRepository;
    }

    /**
     * Save a pPInvoiceDetails.
     *
     * @param pPInvoiceDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public PPInvoiceDetails save(PPInvoiceDetails pPInvoiceDetails) {
        log.debug("Request to save PPInvoiceDetails : {}", pPInvoiceDetails);
        return pPInvoiceDetailsRepository.save(pPInvoiceDetails);
    }

    @Override
    public void saveReceiveBillPPInvoice(ReceiveBill receiveBill) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Integer vATRate;
        if (receiveBill.getvATRate() == 1) {
            vATRate = 5;
        } else if (receiveBill.getvATRate() == 2) {
            vATRate = 10;
        } else {
            vATRate = 0;
        }
        pPInvoiceDetailsRepository.saveReceiveBillPPInvoice(receiveBill, vATRate);
        List<UUID> uuidList = ppInvoiceRepository.findAllParentID(receiveBill.getListIDPPDetail());
        pPInvoiceDetailsRepository.updateTotalReceiveBill(uuidList, currentUserLoginAndOrg.get().getOrg());
    }

    @Override
    public void saveAllReceiveBill(ReceiveBill receiveBill) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Integer vATRate;
        if (receiveBill.getvATRate() != null) {
            if (receiveBill.getvATRate() == 1) {
                vATRate = 5;
            } else if (receiveBill.getvATRate() == 2) {
                vATRate = 10;
            } else {
                vATRate = 0;
            }
        } else {
            vATRate = 0;
        }
        pPInvoiceDetailsRepository.saveReceiveBillPPInvoice(receiveBill, vATRate);
        List<UUID> uuidList = ppInvoiceRepository.findAllParentID(receiveBill.getListIDPPDetail());
        pPInvoiceDetailsRepository.updateTotalReceiveBill(uuidList, currentUserLoginAndOrg.get().getOrg());
        ppServiceDetailRepository.saveReceiveBillPPService(receiveBill, vATRate);
    }

    /**
     * Get all the pPInvoiceDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PPInvoiceDetails> findAll(Pageable pageable) {
        log.debug("Request to get all PPInvoiceDetails");
        return pPInvoiceDetailsRepository.findAll(pageable);
    }


    /**
     * Get one pPInvoiceDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PPInvoiceDetails> findOne(UUID id) {
        log.debug("Request to get PPInvoiceDetails : {}", id);
        return pPInvoiceDetailsRepository.findById(id);
    }

    /**
     * Delete the pPInvoiceDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete PPInvoiceDetails : {}", id);
        pPInvoiceDetailsRepository.deleteById(id);
    }

    /**
     * Author Huy Xoăn
     *
     * @param materialGoodsID
     */
    @Override
    public List<LotNoDTO> getListLotNo(UUID materialGoodsID) {
        log.debug("Request to delete PPInvoiceDetails : {}", materialGoodsID);
        return pPInvoiceDetailsRepository.getListLotNo(materialGoodsID);
    }

    @Override
    public Page<PPInvoiceConvertDTO> getPPInvoiceDetailsGetLicense(Pageable pageable, UUID accountingObjectID, String formDate, String toDate, String currencyCode, List<UUID> listID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Page<PPInvoiceConvertDTO> ppInvoiceConvertPage = null;
        if (currentUserLoginAndOrg.isPresent()) {
            Optional<UserSystemOption> userWithAuthoritiesAndSystemOption = userService.getUserWithAuthoritiesAndSystemOption();
            if (userWithAuthoritiesAndSystemOption.isPresent()) {
                String currentBook = organizationUnitRepository.findCurrentBook(userWithAuthoritiesAndSystemOption.get().getUser().getId(), currentUserLoginAndOrg.get().getOrg());
                ppInvoiceConvertPage = pPInvoiceDetailsRepository.getPPInvoiceDetailsGetLicense(pageable, accountingObjectID, formDate, toDate, currencyCode, listID, currentUserLoginAndOrg.get().getOrg(), currentBook);
//        List<PPInvoiceConvertDTO> ppInvoiceConvertDTO = ppInvoiceConvertPage.getContent();
                if (ppInvoiceConvertPage.getContent().size() > 0) {
                    for (int i = 0; i < ppInvoiceConvertPage.getContent().size(); i++) {
                        if (ppInvoiceConvertPage.getContent().get(i).getMaterialGoodsID() != null) {
                            for (int j = 0; j < i; j++) {
                                if (ppInvoiceConvertPage.getContent().get(i).getMaterialGoodsID().equals(ppInvoiceConvertPage.getContent().get(j).getMaterialGoodsID())) {
                                    ppInvoiceConvertPage.getContent().get(i).setUnits(ppInvoiceConvertPage.getContent().get(j).getUnits());
                                    break;
                                }
                            }
                        }
                        if (ppInvoiceConvertPage.getContent().get(i).getUnits() == null) {
                            ppInvoiceConvertPage.getContent().get(i).setUnits(unitRepository.convertRateForMaterialGoodsComboboxCustom(ppInvoiceConvertPage.getContent().get(i).getMaterialGoodsID(), systemOptionRepository.getAllCompanyByCompanyIdAndCode(currentUserLoginAndOrg.get().getOrg(), TCKHAC_SDDMVTHH)));
                        }
                    }
                }
            }
            return ppInvoiceConvertPage;
        }
        return null;
    }

    @Override
    public ResultDTO getPPOrderNoById(UUID id) {
        ResultDTO resultDTO = new ResultDTO();
        // todo set trạng thái success của resultDto
        resultDTO.setResult(pporderRepository.findNoById(id));
        return resultDTO;
    }
}
