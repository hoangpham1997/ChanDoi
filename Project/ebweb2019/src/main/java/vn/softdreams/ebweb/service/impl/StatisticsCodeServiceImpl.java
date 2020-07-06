package vn.softdreams.ebweb.service.impl;

import org.springframework.data.domain.PageImpl;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.StatisticsCodeService;
import vn.softdreams.ebweb.domain.StatisticsCode;
import vn.softdreams.ebweb.repository.StatisticsCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.dto.StatisticsConvertDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing StatisticsCode.
 */
@Service
@Transactional
public class StatisticsCodeServiceImpl implements StatisticsCodeService {

    private final Logger log = LoggerFactory.getLogger(StatisticsCodeServiceImpl.class);

    private final StatisticsCodeRepository statisticsCodeRepository;

    public StatisticsCodeServiceImpl(StatisticsCodeRepository statisticsCodeRepository) {
        this.statisticsCodeRepository = statisticsCodeRepository;
    }

    /**
     * Save a statisticsCode.
     *
     * @param statisticsCode the entity to save
     * @return the persisted entity
     */
    @Override
    public StatisticsCode save(StatisticsCode statisticsCode) {
        StatisticsCode savedStatisticsCode;
        log.debug("Request to save StatisticsCode : {}", statisticsCode);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            statisticsCode.setCompanyID(currentUserLoginAndOrg.get().getOrg());
            // check duplicate statistic-code
            Integer count = this.statisticsCodeRepository.checkDuplicateStatisticsCode(statisticsCode.getId() == null ? UUID.randomUUID() : statisticsCode.getId(), currentUserLoginAndOrg.get().getOrg(), statisticsCode.getStatisticsCode());
            if (count != null && count > 0) {
                throw new BadRequestAlertException("", "StatisticsCode", "statisticsCodeDuplicate");
            }
            if (statisticsCode.getId() == null) {
                List<String> listCode = statisticsCodeRepository.findByStatisticsCode(currentUserLoginAndOrg.get().getOrg());
                listCode.stream().forEach(n -> {
                    if (n.equalsIgnoreCase(statisticsCode.getStatisticsCode())) {
                        throw new BadRequestAlertException("", "StatisticsCode", "statisticsCodeDuplicate");
                    }
                });
            }
            // update statisticsCode's grade and parents
            if (statisticsCode.getParentID() != null) {
                Optional<StatisticsCode> parentStatisticsCode = this.statisticsCodeRepository.findById(statisticsCode.getParentID());
                statisticsCode.setGrade(parentStatisticsCode.get().getGrade() + 1);
                parentStatisticsCode.get().setIsParentNode(true);
                this.statisticsCodeRepository.save(parentStatisticsCode.get());
                if (statisticsCode.isIsActive()) {
                    List<StatisticsCode> parents = new ArrayList<>();
                    if (statisticsCode.isIsActive()) parents = this.updateAllParents(parents, statisticsCode);
                    this.statisticsCodeRepository.saveAll(parents);
                }
            } else {
                statisticsCode.setGrade(1);
//                if (statisticsCode.getId() == null) statisticsCode.setIsParentNode(null);
            }

            // update isActive and grade for statisticsCode's descendants
            if (statisticsCode.getId() != null) {
                Optional<StatisticsCode> currentStatisticsCode = this.statisticsCodeRepository.findById(statisticsCode.getId());
                if (currentStatisticsCode.get().isIsActive() != statisticsCode.isIsActive() || currentStatisticsCode.get().getGrade() != statisticsCode.getGrade()) {
                    List<StatisticsCode> descendants = new ArrayList<>();
                    descendants = this.updateAllChildren(descendants, statisticsCode);
                    this.statisticsCodeRepository.saveAll(descendants);
                }
            }

            // get old parent
            StatisticsCode oldParent = null;
            if (statisticsCode.getId() != null) {
                oldParent = this.statisticsCodeRepository.findParentByChildID(statisticsCode.getId());
            }

            // save current statistics code
            savedStatisticsCode = statisticsCodeRepository.save(statisticsCode);

            // update old parent if no child exists
            if (oldParent != null) {
                count = this.statisticsCodeRepository.countChildrenByID(oldParent.getId());
                if (count == null || count == 0) {
                    oldParent.setIsParentNode(false);
                    this.statisticsCodeRepository.save(oldParent);
                }
            }
            return savedStatisticsCode;
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<StatisticsCode> updateAllParents(List<StatisticsCode> parents, StatisticsCode statisticsCode) {
        if (statisticsCode.getParentID() != null) {
            Optional<StatisticsCode> parent = this.statisticsCodeRepository.findById(statisticsCode.getParentID());
            parent.get().setIsActive(true);
            parents.add(parent.get());
            updateAllParents(parents, parent.get());
        }
        return parents;
    }

    public List<StatisticsCode> updateAllChildren(List<StatisticsCode> descendants, StatisticsCode statisticsCode) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        List<StatisticsCode> children = this.statisticsCodeRepository.listChildByParentID(currentUserLoginAndOrg.get().getOrg(), statisticsCode.getId());
        if (children != null) {
            for (StatisticsCode child : children) {
                child.setIsActive(statisticsCode.isIsActive());
                child.setGrade(statisticsCode.getGrade() + 1);
                descendants.add(child);
                if (child.isIsParentNode()) updateAllChildren(descendants, child);
            }
        }
        return descendants;
    }

    /**
     * Get all the statisticsCodes.
     *
     * @param pageable th           e pagination information
     * @return the list of entities
     */
//    @Override
//    @Transactional(readOnly = true)
//    public Page<StatisticsCode> findAll(Pageable pageable) {
//        log.debug("Request to get all StatisticsCodes");
//        return statisticsCodeRepository.findAll(pageable);
//    }

//    @Override
//    public Page<StatisticsCode> findAll() {
//        return new PageImpl<StatisticsCode>(statisticsCodeRepository.findAll());
//    }


    /**
     * Get one statisticsCode by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StatisticsCode> findOne(UUID id) {
        log.debug("Request to get StatisticsCode : {}", id);
        return statisticsCodeRepository.findById(id);
    }

    /**
     * Delete the statisticsCode by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete StatisticsCode : {}", id);
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        Integer check = this.statisticsCodeRepository.checkExistedInAccountingObject(id);
        if (check != null && check > 0) {
            throw new BadRequestAlertException("", "", "");
        }
        if (currentUserLoginAndOrg.isPresent()) {
            Optional<StatisticsCode> statisticsCode = statisticsCodeRepository.findById(id);
            this.statisticsCodeRepository.deleteById(id);
            if (statisticsCode.get().getParentID() != null) {
                int count = statisticsCodeRepository.countSiblings(currentUserLoginAndOrg.get().getOrg(), statisticsCode.get().getParentID());
                //if statisticsCode no longer has children
                if (count == 0) {
                    Optional<StatisticsCode> statisticsCodeParent = statisticsCodeRepository.findById(statisticsCode.get().getParentID());
                    statisticsCodeParent.get().setIsParentNode(false);
                    statisticsCodeRepository.save(statisticsCodeParent.get());
                }
            }

        }
    }

    @Override
    public Page<StatisticsConvertDTO> getAllStatisticsCodesActive() {
        return new PageImpl<>(statisticsCodeRepository.findAllByIsActiveTrue());
    }

    public List<StatisticsCode> findAllByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return statisticsCodeRepository.findAllByCompanyID(currentUserLoginAndOrg.get().getOrg());
        }
        return null;
    }

    @Override
    public List<StatisticsCode> findCbxStatisticsCode(UUID id) {
        List<StatisticsCode> cbxStatisticsCode = findAllActive();
        Optional<StatisticsCode> statisticsCode = this.statisticsCodeRepository.findById(id);
        cbxStatisticsCode = filterStatisticsCode(cbxStatisticsCode, statisticsCode.get());
        return cbxStatisticsCode;
    }

    public List<StatisticsCode> filterStatisticsCode(List<StatisticsCode> cbxStatisticsCodes, StatisticsCode statisticsCode) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
//        cbxStatisticsCodes.stream().filter(s -> !s.getId().equals(statisticsCode.getId())).collect(Collectors.toList());
        cbxStatisticsCodes.remove(statisticsCode);
        List<StatisticsCode> childStatisticsCodes = this.statisticsCodeRepository.listChildByParentID(currentUserLoginAndOrg.get().getOrg(), statisticsCode.getId());
        if (childStatisticsCodes != null) {
            for (StatisticsCode s : childStatisticsCodes) {
                this.filterStatisticsCode(cbxStatisticsCodes, s);
            }
        }
        return cbxStatisticsCodes;
    }

    public List<StatisticsCode> findAllActive() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return statisticsCodeRepository.findAllByIsActive(currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    public List<StatisticsCode> findAllStatisticsCode() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return statisticsCodeRepository.findAllByCompanyID(currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<StatisticsCode> getAllStatisticsCodesByCompanyID() {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            return statisticsCodeRepository.getAllStatisticsCodesByCompanyID(currentUserLoginAndOrg.get().getOrg());
        }
        throw new BadRequestAlertException("", "", "");
    }

    public List<StatisticsCode> getAllStatisticsCodesByCompanyIDSimilarBranch(Boolean similarBranch, UUID companyID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            if (companyID == null) {
                companyID = currentUserLoginAndOrg.get().getOrg();
            }
            List<UUID> listCompanyID = statisticsCodeRepository.getIDByCompanyIDAndSimilarBranch(companyID, similarBranch);
            return statisticsCodeRepository.getAllStatisticsCodeByCompanyIDAndSimilarBranch(listCompanyID, similarBranch);
        }
        throw new BadRequestAlertException("", "", "");
    }


//    @Override
//    public Integer checkDuplicateStatisticsCode(StatisticsCode statisticsCode) {
//        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
//        return statisticsCodeRepository.checkDuplicateStatisticsCode(currentUserLoginAndOrg.get().getOrg(), statisticsCode.getStatisticsCode());
//    }
}
