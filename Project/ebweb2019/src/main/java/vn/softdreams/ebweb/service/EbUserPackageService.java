package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.EbPackage;
import vn.softdreams.ebweb.service.dto.EbUserPackageDTO;

public interface EbUserPackageService {

    Boolean activePackageEbUser(EbUserPackageDTO ebUserPackageDTO);

    Boolean activePackageEbUserNoSendCrm(EbUserPackageDTO ebUserPackageDTO);
}
