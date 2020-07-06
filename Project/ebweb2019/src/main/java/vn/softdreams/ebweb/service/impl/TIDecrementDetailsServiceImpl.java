package vn.softdreams.ebweb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.service.TIDecrementDetailsService;
import vn.softdreams.ebweb.domain.TIDecrementDetails;
import vn.softdreams.ebweb.repository.TIDecrementDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.service.dto.Report.TIDecrementDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

/**
 * Service Implementation for managing TIDecrementDetails.
 */
@Service
@Transactional
public class TIDecrementDetailsServiceImpl implements TIDecrementDetailsService {

    private final Logger log = LoggerFactory.getLogger(TIDecrementDetailsServiceImpl.class);

    private final TIDecrementDetailsRepository tIDecrementDetailsRepository;

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;


    public TIDecrementDetailsServiceImpl(TIDecrementDetailsRepository tIDecrementDetailsRepository) {
        this.tIDecrementDetailsRepository = tIDecrementDetailsRepository;
    }

    /**
     * Save a tIDecrementDetails.
     *
     * @param tIDecrementDetails the entity to save
     * @return the persisted entity
     */
    @Override
    public TIDecrementDetails save(TIDecrementDetails tIDecrementDetails) {
        log.debug("Request to save TIDecrementDetails : {}", tIDecrementDetails);        return tIDecrementDetailsRepository.save(tIDecrementDetails);
    }

    /**
     * Get all the tIDecrementDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TIDecrementDetails> findAll(Pageable pageable) {
        log.debug("Request to get all TIDecrementDetails");
        return tIDecrementDetailsRepository.findAll(pageable);
    }


    /**
     * Get one tIDecrementDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TIDecrementDetails> findOne(UUID id) {
        log.debug("Request to get TIDecrementDetails : {}", id);
        return tIDecrementDetailsRepository.findById(id);
    }

    /**
     * Delete the tIDecrementDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete TIDecrementDetails : {}", id);
        tIDecrementDetailsRepository.deleteById(id);
    }

    @Override
    public List<TIDecrementDTO> getDetailsTIDecrementDTO(UUID id) {
        StringBuilder sqlBuilder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append("select" +
            " a.ToolsCode ," +
            " a.ToolsCode ," +
            " a.DepartmentID ," +
            " b.Reason ," +
            " b.Date , " +
            " c.RemainingAmount , " +
            " c.NewRemainingAmount ," +
            " c.DiffRemainingAmount " +
            " from Tools a " +
            " inner join TIAdjustmentDetail c on a.ID = c.ToolsID " +
            " inner join  TIDecrement b on b.CompanyID = a.CompanyID " +
            " where b.ID = :id" +
            " order by c.OrderPriority");
        params.put("ID" , id);
        List<TIDecrementDTO>tiDecrementDTOList = new ArrayList<>();
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "TIDecrementDTOList");
        Common.setParams(query, params);
        tiDecrementDTOList = query.getResultList();
        return tiDecrementDTOList;
    }
}
