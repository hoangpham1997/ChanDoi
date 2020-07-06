package vn.softdreams.ebweb.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.EbPackage;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.service.EbUserPackageService;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.RestfullAPI_CRM.RequestCRM;
import vn.softdreams.ebweb.service.Utils.RestfullAPI_CRM.RestApiCRMService;
import vn.softdreams.ebweb.service.dto.EbUserPackageDTO;
import vn.softdreams.ebweb.service.dto.crm.ResponeCRM;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.Constants;

import java.time.LocalDate;

import static vn.softdreams.ebweb.service.util.Constants.CRMResActiveStatus.Active_Done;
import static vn.softdreams.ebweb.service.util.DeployConstants.getCrmUrl;

@Service
@Transactional
public class EbUserPackageServiceImpl implements EbUserPackageService {
    private final Logger log = LoggerFactory.getLogger(EbPackageServiceImpl.class);
    private UserService userService;
    private final EbPackageRepository ebPackageRepository;
    private final EbUserPackageRepository ebUserPackageRepository;
    private final OrganizationUnitRepository organizationUnitRepository;

    public EbUserPackageServiceImpl(EbPackageRepository ebPackageRepository,
                                    UserService userService,
                                    OrganizationUnitRepository organizationUnitRepository,
                                    EbUserPackageRepository ebUserPackageRepository,
                                    EbUserOrganizationUnitRepository ebUserOrganizationUnitRepository,
                                    UserRepository userRepository, EbUserPackageRepository ebUserPackageRepository1) {
        this.ebPackageRepository = ebPackageRepository;
        this.userService = userService;
        this.ebUserPackageRepository = ebUserPackageRepository1;
        this.organizationUnitRepository = organizationUnitRepository;
    }
    @Override
    public Boolean activePackageEbUser(EbUserPackageDTO ebUserPackageDTO) {
        try {
            if(ebUserPackageDTO.getStatus() == vn.softdreams.ebweb.service.util.Constants.EbUserPackage.DANG_DUNG) {
                ebUserPackageRepository.activatedUser(ebUserPackageDTO.getUserID());
            } else {
                ebUserPackageRepository.InActivatedUser(ebUserPackageDTO.getUserID());
            }
            ebUserPackageRepository.updateEbUserPackageForActive(ebUserPackageDTO.getPackageID(),
                ebUserPackageDTO.getStatus(), ebUserPackageDTO.getIsTotalPackage(), ebUserPackageDTO.getId());
            // call api Crm active package
            OrganizationUnit org = organizationUnitRepository.findByID(ebUserPackageDTO.getCompanyID());
            RequestCRM request_sds = new RequestCRM();
            request_sds.setCompanyTaxCode(org.gettaxCode());
            request_sds.setStatus(true);
            String md5Encrypt = Common.getMd5(request_sds.getCompanyTaxCode() + vn.softdreams.ebweb.service.util.Constants.Salt.value);
            request_sds.setHash(md5Encrypt);
            String uri = getCrmUrl();
            ResponeCRM result = RestApiCRMService.postCRM(request_sds, uri);
            return result.status == Active_Done;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean activePackageEbUserNoSendCrm(EbUserPackageDTO ebUserPackageDTO) {
        try {
            if(ebUserPackageDTO.getStatus() == vn.softdreams.ebweb.service.util.Constants.EbUserPackage.DANG_DUNG) {
                ebUserPackageRepository.activatedUser(ebUserPackageDTO.getUserID());
            } else {
                ebUserPackageRepository.InActivatedUser(ebUserPackageDTO.getUserID());
            }
            ebUserPackageRepository.updateEbUserPackageForActive(ebUserPackageDTO.getPackageID(),
                ebUserPackageDTO.getStatus(), ebUserPackageDTO.getIsTotalPackage(), ebUserPackageDTO.getId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
