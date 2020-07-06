package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.repository.OrganizationUnitRepositoryCustom;
import vn.softdreams.ebweb.service.dto.cashandbank.MCReceiptDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.OrganizationUnitCustomDTO;
import vn.softdreams.ebweb.web.rest.dto.OrganizationUnitSearchDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.Instant;
import java.util.*;

public class OrganizationUnitRepositoryImpl implements OrganizationUnitRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<String> GetListOrderFixCodeParentID(UUID id) {
        Query query = entityManager.createNativeQuery("SELECT OrderFixCode FROM OrganizationUnit", OrganizationUnit.class);
        List<String> lstFixCOde = query.getResultList();
        return lstFixCOde;
    }

    @Override
    public Page<OrganizationUnit> findAllBigOrg(Pageable pageable) {
        StringBuilder sql = new StringBuilder();
        List<OrganizationUnit> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append(" FROM EbOrganizationUnit WHERE UnitType = 0 and Grade = 1 ");
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(*) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " ORDER BY OrganizationUnitCode ", OrganizationUnit.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            lst = query.getResultList();
        }
        return new PageImpl<>(((List<OrganizationUnit>) lst), pageable, total.longValue());
    }

    @Override
    public Page<OrganizationUnit> findAllBigOrgSearch(Pageable pageable, OrganizationUnitSearchDTO organizationUnitSearchDTO) {
        StringBuilder sql = new StringBuilder();
        List<OrganizationUnit> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append(" FROM EbOrganizationUnit WHERE UnitType = 0 and Grade = 1 ");
        if (organizationUnitSearchDTO.getOrganizationUnitCode() != null) {
            sql.append(" And organizationUnitCode like :organizationUnitCode ");
            params.put("organizationUnitCode", "%" + organizationUnitSearchDTO.getOrganizationUnitCode() + "%");
        }
        if (organizationUnitSearchDTO.getOrganizationUnitName() != null) {
            sql.append(" And organizationUnitName like :organizationUnitName ");
            params.put("organizationUnitName", "%" + organizationUnitSearchDTO.getOrganizationUnitName() + "%");
        }
        if (organizationUnitSearchDTO.getTaxCode() != null) {
            sql.append(" And taxCode like :taxCode ");
            params.put("taxCode", "%" + organizationUnitSearchDTO.getTaxCode() + "%");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(*) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " ORDER BY OrganizationUnitCode ", OrganizationUnit.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            lst = query.getResultList();
        }
        return new PageImpl<>(((List<OrganizationUnit>) lst), pageable, total.longValue());
    }


    @Override
    public OrganizationUnitCustomDTO findByIDCustom(UUID id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT ID as id, OrganizationUnitCode as organizationUnitCode, OrganizationUnitName as organizationUnitName, UnitType as unitType, AccType as accType, ParentID as parentID FROM EbOrganizationUnit WHERE ID = :id");
        params.put("id", id);
        Query query = entityManager.createNativeQuery(sql.toString(), "OrganizationUnitCustomDTO");
        Common.setParams(query, params);
        OrganizationUnitCustomDTO organizationUnitCustomDTO = (OrganizationUnitCustomDTO) query.getSingleResult();
        return organizationUnitCustomDTO;
    }

    @Override
    public List<OrganizationUnitCustomDTO> findByParentIDCustomLogin(UUID parentID) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT  ID as id, OrganizationUnitCode as organizationUnitCode, OrganizationUnitName as organizationUnitName, UnitType as unitType, AccType as accType, ParentID as parentID FROM EbOrganizationUnit where parentID = :parentID AND ID <> :parentID  AND UnitType < 2 ORDER BY UnitType, ParentID, OrganizationUnitName");
        params.put("parentID", parentID);
        Query query = entityManager.createNativeQuery(sql.toString(), "OrganizationUnitCustomDTO");
        Common.setParams(query, params);
        List<OrganizationUnitCustomDTO> organizationUnitCustomDTO = query.getResultList();
        return organizationUnitCustomDTO;
    }

    @Override
    public List<OrganizationUnitCustomDTO> findAllByListCompanyID(Long id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT ID as id, OrganizationUnitCode as organizationUnitCode, OrganizationUnitName as organizationUnitName, UnitType as unitType, AccType as accType from EbOrganizationUnit a WHERE ID IN (SELECT OrgId FROM EbUserOrganizationUnit WHERE UserId = :id) ORDER BY OrganizationUnitCode");
        params.put("id", id);
        Query query = entityManager.createNativeQuery(sql.toString(), "OrganizationUnitCustomDTO");
        Common.setParams(query, params);
        List<OrganizationUnitCustomDTO> organizationUnitCustomDTO = query.getResultList();
        return organizationUnitCustomDTO;
    }
}
