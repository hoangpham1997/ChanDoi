package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.GenCode;
import vn.softdreams.ebweb.domain.SystemOption;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.PrivateToGeneralUse;
import vn.softdreams.ebweb.service.dto.SaveSystemOptionsDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing SystemOption.
 */
public interface SystemOptionService {

    /**
     * Save a systemOption.
     *
     * @param systemOption the entity to save
     * @return the persisted entity
     */
    SystemOption save(SystemOption systemOption);

    /**
     * Get all the systemOptions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SystemOption> findAll(Pageable pageable);


    /**
     * Get the "id" systemOption.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SystemOption> findOne(Long id);

    /**
     * Delete the "id" systemOption.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    Optional<SystemOption> findOneByUser(String code);

    void savePostedDate(String data, String dataDefault);

    List<PrivateToGeneralUse> saveSystemOptions(SaveSystemOptionsDTO saveSystemOptionsDTO);

    List<SystemOption> findAllSystemOptions();

    List<SystemOption> getSystemOptionsByCompanyID(UUID companyID);

}
