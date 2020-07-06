package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.Unit;
import vn.softdreams.ebweb.repository.UnitRepositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.UnitConvertRateDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class UnitRepositoryImpl implements UnitRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public Page<Unit> findAll(Pageable pageable, String unitName, String unitDescription, Boolean isActive) {
        StringBuilder sql = new StringBuilder();
        List<Unit> units = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM UNIT WHERE 1 = 1 ");
        if (!Strings.isNullOrEmpty(unitName)) {
            sql.append("AND UNITNAME LIKE :UNITNAME ");
            params.put("UNITNAME", "%"+unitName+"%");
        }
        if (!Strings.isNullOrEmpty(unitDescription)) {
            sql.append("AND unitDescription = :unitDescription ");
            params.put("unitDescription", unitDescription);
        }
        if (isActive != null) {
            sql.append("AND isActive = :isActive ");
            params.put("isActive", isActive);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString(), Unit.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            units = query.getResultList();
        }
        return new PageImpl<>(units, pageable, total.longValue());
    }

    @Override
    public List<UnitConvertRateDTO> convertRateForMaterialGoodsComboboxCustom(UUID materialGoodsId, List<UUID> companyID) {
        StringBuilder sql = new StringBuilder();
        List<UnitConvertRateDTO> units;
        Map<String, Object> params = new HashMap<>();
        sql.append("select b.id, b.UnitName, 1 as convertRate, '*' as Formula, a.id MaterialGoodsID " +
            "from MaterialGoods a left join Unit b on a.UnitID = b.ID where B.CompanyID in :companyID ");
        if (materialGoodsId != null) {
            sql.append("AND a.ID = :materialGoodsID and b.ID is not null ");
            params.put("materialGoodsID", materialGoodsId);
        }
        sql.append("union all " +
            "select b.id, b.UnitName, a.ConvertRate, a.Formula, a.materialGoodsID MaterialGoodsID " +
            "from MaterialGoodsConvertUnit a left join Unit b on a.UnitID = b.ID where b.CompanyID in :companyID ");
        params.put("companyID", companyID);
        if (materialGoodsId != null) {
            sql.append(" and a.MaterialGoodsID = :materialGoodsID and b.ID is not null");
            params.put("materialGoodsID", materialGoodsId);
        }
        Query query = entityManager.createNativeQuery( sql.toString(), "UnitConvertRateDTO");
        Common.setParams(query, params);
        units = query.getResultList();
        return units;
    }

    @Override
    public UnitConvertRateDTO getMainUnitName(UUID materialGoodsId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select u.id, u.UnitName, 1 as convertRate, '*' as Formula from MaterialGoods mg left join Unit u on mg.UnitID = u.ID where mg.ID = ?1 ");
//        params.put("id", materialGoodsId);
        Query query = entityManager.createNativeQuery( sql.toString(), "MainUnitNameDTO");
//        Common.setParams(query, params);
        query.setParameter(1, materialGoodsId);
        UnitConvertRateDTO unit = (UnitConvertRateDTO) query.getSingleResult();
        return unit;
    }

    @Override
    public List<UnitConvertRateDTO> getConvertUnitInfo(Map<String, Map<UUID, UUID>> materialGoodsId) {
        StringBuilder sql = new StringBuilder();
        int i = 0;
        int paramCount = 0;
        Map<String, Object> params = new HashMap<>();
        for (Map.Entry<String, Map<UUID, UUID>> e : materialGoodsId.entrySet()) {
            String repositoryId = e.getKey();
            Map<UUID, UUID> convertUnit = e.getValue();
            if (!convertUnit.values().isEmpty()) {
                if (i > 0) {
                    sql.append("union all ");
                }
                sql.append("select '")
                    .append(repositoryId)
                    .append("' as repositoryId, ")
                    .append("UnitID as id, ")
                    .append("mgc.MaterialGoodsID as materialGoodsID, mgc.ConvertRate, mgc.Formula")
                    .append(" from MaterialGoodsConvertUnit mgc where (");
                int j = 0 ;
                for (Map.Entry<UUID, UUID> entry : convertUnit.entrySet()) {
                    UUID materialGoodsID = entry.getKey();
                    UUID id = entry.getValue();
                    if (j > 0) {
                        sql.append(" or ");
                    }
                    sql.append("(UnitID = :")
                        .append(paramCount)
                        .append(" and mgc.MaterialGoodsID = :")
                        .append(paramCount + 1)
                        .append(")");
                    params.put(String.valueOf(paramCount), id);
                    params.put(String.valueOf(paramCount + 1), materialGoodsID);
                    paramCount +=2;
                    j++;
                }
                sql.append(")");
                i ++;
            }
        }
        Query query = entityManager.createNativeQuery( sql.toString(), "OPUnitConvertRateDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<UnitConvertRateDTO> findAllWithConvertRate(UUID companyID) {
        StringBuilder sql = new StringBuilder();
        List<UnitConvertRateDTO> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("select a.ID, a.UnitName , b.ConvertRate, b.Formula " +
            "from Unit a " +
            "         left join MaterialGoodsConvertUnit b " +
            "                   on a.ID = b.UnitID " +
            "where a.IsActive = 1 and CompanyID = :CompanyID ");

        params.put("CompanyID", companyID);
        Query query = entityManager.createNativeQuery(sql.toString(), "UnitConvertRateDTOForFindAll");
        Utils.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public Page<Unit> pageableAllUnit(Pageable pageable, UUID org) {
        StringBuilder sql = new StringBuilder();
        List<Unit> units = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM Unit WHERE 1 = 1 ");
        if (org != null) {
            sql.append("AND CompanyID = :org ");
            params.put("org", org);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " order by UnitName "
                , Unit.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            units = query.getResultList();

        }
        return new PageImpl<>(units, pageable, total.longValue());
    }

    @Override
    public List<UnitConvertRateDTO> convertRateForMaterialGoodsComboboxCustomList(List<UUID> materialGoodsId, UUID companyID) {
        StringBuilder sql = new StringBuilder();
        List<UnitConvertRateDTO> units;
        Map<String, Object> params = new HashMap<>();
        sql.append("select b.id, b.UnitName, 1 as convertRate, '*' as Formula, a.id MaterialGoodsID " +
            "from MaterialGoods a left join Unit b on a.UnitID = b.ID where B.CompanyID = :companyID ");
        if (materialGoodsId != null) {
            sql.append("AND a.ID in :materialGoodsID and b.ID is not null ");
            params.put("materialGoodsID", materialGoodsId);
        }
        sql.append("union all " +
            "select b.id, b.UnitName, a.ConvertRate, a.Formula, a.materialGoodsID MaterialGoodsID " +
            "from MaterialGoodsConvertUnit a left join Unit b on a.UnitID = b.ID where b.CompanyID = :companyID ");
        params.put("companyID", companyID);
        if (materialGoodsId != null) {
            sql.append(" and a.MaterialGoodsID in :materialGoodsID and b.ID is not null");
            params.put("materialGoodsID", materialGoodsId);
        }
        Query query = entityManager.createNativeQuery( sql.toString(), "UnitConvertRateDTO");
        Common.setParams(query, params);
        units = query.getResultList();
        return units;
    }

    @Override
    public List<UnitConvertRateDTO> getUnitByITIIncrementID(UUID companyID, UUID tiIncrementID) {
        StringBuilder sql = new StringBuilder();
        List<UnitConvertRateDTO> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("select a.ID, a.UnitName , b.ConvertRate, b.Formula " +
            "from Unit a " +
            "         left join MaterialGoodsConvertUnit b " +
            "                   on a.ID = b.UnitID " +
            "where a.IsActive = 1 and CompanyID = :CompanyID ");

        params.put("CompanyID", companyID);
        Query query = entityManager.createNativeQuery(sql.toString(), "UnitConvertRateDTOForFindAll");
        Utils.setParams(query, params);
        return query.getResultList();
    }
}
