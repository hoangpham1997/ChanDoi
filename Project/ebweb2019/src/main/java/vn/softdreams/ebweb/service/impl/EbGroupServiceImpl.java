package vn.softdreams.ebweb.service.impl;

import com.itextpdf.text.log.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.EbGroup;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.repository.EbGroupRepository;
import vn.softdreams.ebweb.repository.OrganizationUnitRepository;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.EbGroupService;
import vn.softdreams.ebweb.service.dto.OrgGroupDTO;
import vn.softdreams.ebweb.web.rest.dto.EbGroupSaveDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@Transactional
public class EbGroupServiceImpl implements EbGroupService {
//    private final Logger log = LoggerFactory.getLogger(EbGroupServiceImpl.class);

    private final EbGroupRepository ebGroupRepository;

    private final OrganizationUnitRepository organizationUnitRepository;

    public EbGroupServiceImpl(EbGroupRepository ebGroupRepository,
                              OrganizationUnitRepository organizationUnitRepository
                              ) {
        this.ebGroupRepository = ebGroupRepository;
        this.organizationUnitRepository = organizationUnitRepository;
    }

    @Override
    public EbGroupSaveDTO save(EbGroup ebGroup) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        EbGroupSaveDTO ebGroupSaveDTO = new EbGroupSaveDTO();
        EbGroup ebGr = new EbGroup();
        if (currentUserLoginAndOrg.isPresent()) {
            if (ebGroup.getId() == null) {
                ebGroup.setCompanyID(currentUserLoginAndOrg.get().getOrg());
                ebGroup.setIsSystem(false);
                int count = ebGroupRepository.countByCodeIgnoreCase(ebGroup.getCode(), ebGroup.getCompanyID());
                if (count == 0) {
                    ebGr = ebGroupRepository.save(ebGroup);
                    ebGroupRepository.insertEbOrganizationUnitGroup(currentUserLoginAndOrg.get().getOrg(), ebGr.getId());
                    ebGroupSaveDTO.setEbGroup(ebGr);
                    ebGroupSaveDTO.setStatus(0);
                    return ebGroupSaveDTO;
                } else {
                    ebGroupSaveDTO.setEbGroup(ebGroup);
                    ebGroupSaveDTO.setStatus(count);
                    return ebGroupSaveDTO;
                }
            } else {
                ebGr = ebGroupRepository.save(ebGroup);
                ebGroupSaveDTO.setEbGroup(ebGr);
                ebGroupSaveDTO.setStatus(0);
                return ebGroupSaveDTO;
            }
        }
        throw new BadRequestAlertException("", "", "");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EbGroup> findAll(Pageable pageable) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        return ebGroupRepository.findAllByCompanyID(pageable, currentUserLoginAndOrg.get().getOrg());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EbGroup> findOne(UUID id) {
        return ebGroupRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        ebGroupRepository.deleteEbOrganizationUnitGroupByGroupId(id);
        ebGroupRepository.deleteById(id);
    }

    @Override
    public Optional<EbGroup> findOneById(UUID id) {
        return ebGroupRepository.findOneById(id);
    }

    @Override
    public EbGroup saveAuthorities(EbGroup ebGroup) {
        return ebGroupRepository.save(ebGroup);
    }

    @Override
    public List<EbGroup> findAllByOrgId(UUID orgID) {
        return ebGroupRepository.findAllByOrgId(orgID);
    }

    @Override
    public EbGroupSaveDTO saveOrgGroup(OrgGroupDTO orgGroupDTO) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        EbGroupSaveDTO ebGroupSaveDTO = new EbGroupSaveDTO();
        EbGroup ebGr = new EbGroup();
        if (currentUserLoginAndOrg.isPresent()) {
            EbGroup ebGroup = orgGroupDTO.getEbGroup();
            if (ebGroup.getId() == null) {
                ebGroup.setCompanyID(orgGroupDTO.getOrgId());
                ebGroup.setIsSystem(false);
                int count = ebGroupRepository.countByCodeIgnoreCase(ebGroup.getCode(), ebGroup.getCompanyID());
                if (count == 0) {
                    ebGr = ebGroupRepository.save(ebGroup);
                    ebGroupRepository.insertEbOrganizationUnitGroup(orgGroupDTO.getOrgId(), ebGr.getId());
                    ebGroupSaveDTO.setEbGroup(ebGr);
                    ebGroupSaveDTO.setStatus(0);
                    return ebGroupSaveDTO;
                } else {
                    ebGroupSaveDTO.setEbGroup(ebGroup);
                    ebGroupSaveDTO.setStatus(count);
                    return ebGroupSaveDTO;
                }
            } else {
                ebGr = ebGroupRepository.save(ebGroup);
                ebGroupSaveDTO.setEbGroup(ebGr);
                ebGroupSaveDTO.setStatus(0);
                return ebGroupSaveDTO;
            }
        }
        throw new BadRequestAlertException("", "", "");
    }

}
