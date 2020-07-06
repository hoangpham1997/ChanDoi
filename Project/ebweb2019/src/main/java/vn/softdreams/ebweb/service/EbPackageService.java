package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.EbPackage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.EbUserPackage;
import vn.softdreams.ebweb.web.rest.dto.CRMEbPackageDTO;
import vn.softdreams.ebweb.web.rest.dto.CRMUserRespDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing EbPackage.
 */
public interface EbPackageService {

    /**
     * Save a ebPackage.
     *
     * @param ebPackage the entity to save
     * @return the persisted entity
     */
    EbPackage save(EbPackage ebPackage);

    /**
     * Get all the ebPackages.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<EbPackage> findAll(Pageable pageable);

    List<EbPackage> findAll();

    /**
     * Get the "id" ebPackage.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<EbPackage> findOne(UUID id);

    /**
     * Delete the "id" ebPackage.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Integer findOneByOrgIdAndUserId(Integer comType);

    Integer findOneByUserId();

    CRMUserRespDTO createNewEbPackageCrm(CRMEbPackageDTO cRMEbPackageDTO);

    Boolean isExtension(String servicePackage, String companyTaxCode);

    EbUserPackage findOneByTaxCode(String companyTaxCode);

    CRMUserRespDTO updateEbPackageCrm(CRMEbPackageDTO cRMEbPackageDTO);
}
