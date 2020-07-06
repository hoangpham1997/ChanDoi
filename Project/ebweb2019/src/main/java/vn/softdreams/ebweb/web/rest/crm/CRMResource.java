package vn.softdreams.ebweb.web.rest.crm;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.ebweb.domain.EbUserPackage;
import vn.softdreams.ebweb.service.EbPackageService;
import vn.softdreams.ebweb.service.MailService;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.web.rest.dto.*;

import java.util.List;

import static vn.softdreams.ebweb.service.util.Constants.CRMResStatus.Fail_UnAuthenticated;

/**
 * REST controller for managing InvoiceType.
 */
@RestController
@RequestMapping("/api")
public class CRMResource {

    private final Logger log = LoggerFactory.getLogger(CRMResource.class);

    private static final String ENTITY_NAME = "crmUser";

    private final UserService userService;

    private final MailService mailService;

    private final EbPackageService ebPackageService;

    public CRMResource(UserService userService, MailService mailService, EbPackageService ebPackageService) {
        this.userService = userService;
        this.mailService = mailService;
        this.ebPackageService = ebPackageService;
    }

    /**
     * GET
     *
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceTypes in body
     */
    @PostMapping("/eb-c-r-m/new")
    @Timed
    public ResponseEntity<CRMUserRespDTO> getCrmUserResponse(@RequestBody CRMUserDTO crmUserDTO) {
        log.debug("REST request to get a response to CRM");
        Boolean isAuthenticated = userService.checkAuthenticationNewUser(crmUserDTO);
        CRMUserRespDTO result = new CRMUserRespDTO();
        if (!isAuthenticated) {
            result.setSystemCode(Fail_UnAuthenticated);
            result.setStatus(false);
        } else {
            result = userService.getCrmUserResponse(crmUserDTO);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/eb-c-r-m/new-eb-package")
    @Timed
    public ResponseEntity<CRMUserRespDTO> createNewEbPackage(@RequestBody CRMEbPackageDTO cRMEbPackageDTO) {
        log.debug("REST request to get a response to CRM");
        Boolean isAuthenticated = userService.checkAuthenticationEBPack(cRMEbPackageDTO);
        CRMUserRespDTO result = new CRMUserRespDTO();
        if (!isAuthenticated) {
            result.setSystemCode(Fail_UnAuthenticated);
            result.setStatus(false);
        } else {
            result = ebPackageService.createNewEbPackageCrm(cRMEbPackageDTO);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/eb-c-r-m/update-eb-package")
    @Timed
    public ResponseEntity<CRMUserRespDTO> updateEbPackage(@RequestBody CRMEbPackageDTO cRMEbPackageDTO) {
        log.debug("REST request to get a response to CRM");
        Boolean isAuthenticated = userService.checkAuthenticationEBPack(cRMEbPackageDTO);
        CRMUserRespDTO result = new CRMUserRespDTO();
        if (!isAuthenticated) {
            result.setSystemCode(Fail_UnAuthenticated);
            result.setStatus(false);
        } else {
            result = ebPackageService.updateEbPackageCrm(cRMEbPackageDTO);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/eb-c-r-m/renew-package")
    @Timed
    public ResponseEntity<CRMUserRespDTO> renewPackage(@RequestBody CRMRenewPackageDTO cRMRenewPackageDTO) {
        log.debug("REST request to get a response to CRM");
        String preData = cRMRenewPackageDTO.getEmail();
        Boolean isAuthenticated = userService.checkIsAuthentication(preData, cRMRenewPackageDTO.getHash());
        CRMUserRespDTO result = new CRMUserRespDTO();
        if (!isAuthenticated) {
            result.setSystemCode(Fail_UnAuthenticated);
            result.setStatus(false);
        } else {
            // lay goi hien tai theo email
            EbUserPackage ebUserPackage = userService.findOneUserPackageByLogin(cRMRenewPackageDTO.getEmail());
            Boolean isExtension = userService.isExtension(cRMRenewPackageDTO.getServicePackage(), cRMRenewPackageDTO.getEmail());
            // gia hạn gói
            if (isExtension) {
                result = userService.extensionPackage(ebUserPackage, cRMRenewPackageDTO.getRenewEndDate());
            } else {
                // nâng cấp gói
                result = userService.upgradePackage(ebUserPackage, cRMRenewPackageDTO.getServicePackage(), cRMRenewPackageDTO.getRenewEndDate());
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/eb-c-r-m/update-status-user")
    @Timed
    public ResponseEntity<CRMUserRespDTO> updateStatusUser(@RequestBody List<CRMUserStatusDTO> cRMUserStatusDTOs) {
        log.debug("REST request to get a response to CRM");
        String preData = cRMUserStatusDTOs.get(0).getStatus();
        Boolean isAuthenticated = userService.checkIsAuthentication(preData, cRMUserStatusDTOs.get(0).getHash());
        CRMUserRespDTO result = new CRMUserRespDTO();
        if (!isAuthenticated) {
            result.setSystemCode(Fail_UnAuthenticated);
            result.setStatus(false);
        } else {
            result = userService.updateStatusUser(cRMUserStatusDTOs);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/eb-c-r-m/reset-user")
    @Timed
    public ResponseEntity<CRMUserRespDTO> resetUser(@RequestBody CRMUserResetDTO crmUserResetDTO) {
        log.debug("REST request to get a response to CRM");
        String preData = crmUserResetDTO.getEmail();
        Boolean isAuthenticated = userService.checkIsAuthentication(preData, crmUserResetDTO.getHash());
        CRMUserRespDTO result = new CRMUserRespDTO();
        if (!isAuthenticated) {
            result.setSystemCode(Fail_UnAuthenticated);
            result.setStatus(false);
        } else {
            result = userService.resetUserFromCRM(crmUserResetDTO);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
