package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.domain.Repository;
import vn.softdreams.ebweb.domain.Unit;
import vn.softdreams.ebweb.repository.RepositoryRepositoryCustom;
import vn.softdreams.ebweb.service.dto.ObjectDTO;
import vn.softdreams.ebweb.service.dto.RepositoryDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.Utils.Utils.setParams;

public class RepositoryRepositoryImpl implements RepositoryRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Page<Repository> pageableAllRepositories(Pageable pageable, List<UUID> org) {
        StringBuilder sql = new StringBuilder();
        List<Repository> repositories = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM Repository WHERE 1 = 1 ");
        if (org != null) {
            sql.append("AND CompanyID in :org ");
            params.put("org", org);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + "order by RepositoryCode"
                , Repository.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            repositories = query.getResultList();
        }
        return new PageImpl<>(repositories, pageable, total.longValue());
    }

    @Override
    public Page<Repository> getAllByListCompany(Pageable pageable, List<UUID> listCompanyID) {
        StringBuilder sql = new StringBuilder();
        List<Repository> repositories = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM Repository WHERE 1 = 1 ");
        if (listCompanyID.size() > 0) {
            sql.append(" AND CompanyID in :org ");
            params.put("org", listCompanyID);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " order by RepositoryCode "
                , Repository.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            repositories = query.getResultList();
        }
        return new PageImpl<>(repositories, pageable, total.longValue());
    }

    @Override
    public List<Repository> pageableAllRepositoriesCBB(UUID org) {
        StringBuilder sql = new StringBuilder();
        List<Repository> repositories = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM Repository WHERE 1 = 1 ");
        if (org != null) {
            sql.append("AND CompanyID = :org  AND IsActive = 1 ");
            params.put("org", org);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + "order by RepositoryCode"
                , Repository.class);
            Common.setParams(query, params);
            repositories = query.getResultList();
        }
        return repositories;
    }

    @Override
    public List<Repository>getAllByListCompanyCBB(List<UUID> listCompanyID) {
        StringBuilder sql = new StringBuilder();
        List<Repository> repositories = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM Repository WHERE 1 = 1 ");
        if (listCompanyID.size() > 0) {
            sql.append(" AND CompanyID in :org AND IsActive = 1 ");
            params.put("org", listCompanyID);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " order by RepositoryCode "
                , Repository.class);
            Common.setParams(query, params);
            repositories = query.getResultList();
        }
        return repositories;
    }

    @Override
    public List<Repository> findAllRepositoryCustom(List<UUID> companyId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM Repository WHERE 1 = 1 ");
        sql.append("AND CompanyID in :org  AND IsActive = 1 ");
        params.put("org", companyId);
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Query query = entityManager.createNativeQuery("SELECT id, repositorycode, repositoryName " + sql.toString() + "order by RepositoryCode", "RepositoryDTO");
        Common.setParams(query, params);
        return ((List<RepositoryDTO>) query.getResultList()).stream().map(dto -> {
            Repository repository = new Repository();
            repository.setId(dto.getId());
            repository.setRepositoryCode(dto.getRepositoryCode());
            repository.setRepositoryName(dto.getRepositoryName());
            return repository;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ObjectDTO> getIDAndNameByIDS(List<UUID> materialGoodsIDs) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select mg.id ID, mg.RepositoryCode name from Repository mg where mg.id in :ids");
        params.put("ids", materialGoodsIDs);
        Query query = entityManager.createNativeQuery(sql.toString(), "ObjectDTO");
        setParams(query, params);
        return query.getResultList();
    }

}
