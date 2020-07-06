package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.StatisticsCode;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.StatisticsConvertDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing StatisticsCode.
 */
public interface StatisticsCodeService {

    /**
     * Save a statisticsCode.
     *
     * @param statisticsCode the entity to save
     * @return the persisted entity
     */
    StatisticsCode save(StatisticsCode statisticsCode);

    /**
     * Get all the statisticsCodes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
//    Page<StatisticsCode> findAll(Pageable pageable);

    /**
     * add by namnh
     *
     * @return the list of entities
     */
//    Page<StatisticsCode> findAll();


    /**
     * Get the "id" statisticsCode.F
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<StatisticsCode> findOne(UUID id);

    /**
     * Delete the "id" statisticsCode.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Page<StatisticsConvertDTO> getAllStatisticsCodesActive();

    List<StatisticsCode> findAllActive();

    List<StatisticsCode> findAllStatisticsCode();

    List<StatisticsCode> getAllStatisticsCodesByCompanyID();

    List<StatisticsCode> getAllStatisticsCodesByCompanyIDSimilarBranch(Boolean similarBranch, UUID companyID);

    List<StatisticsCode> findAllByCompanyID();

    List<StatisticsCode> findCbxStatisticsCode(UUID id);

//    Integer checkDuplicateStatisticsCode(StatisticsCode statisticsCode);
}
