package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.Bank;
import vn.softdreams.ebweb.domain.FixedAsset;
import vn.softdreams.ebweb.repository.FixedAssetRepositoryCustom;
import vn.softdreams.ebweb.service.dto.FixedAssetDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;

public class FixedAssetRepositoryImpl implements FixedAssetRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;
    @Override
    public Page<FixedAsset> findAll(Pageable pageable, UUID branchID, UUID fixedAssetCategoryID, UUID organizationUnitID, String fixedAssetCode, String fixedAssetName, String description, Integer productionYear, String madeIn, String serialNumber, UUID accountingObjectID, String accountingObjectName, String warranty, String guaranteeCodition, Boolean isSecondHand, Integer currentState, String deliveryRecordNo, LocalDateTime deliveryRecordDate, LocalDateTime purchasedDate, LocalDateTime incrementDate, LocalDateTime depreciationDate, LocalDateTime usedDate, String depreciationAccount, String originalPriceAccount, String expenditureAccount, BigDecimal purchasePrice, BigDecimal originalPrice, Integer depreciationMethod, BigDecimal usedTime, Boolean displayMonthYear, BigDecimal periodDepreciationAmount, BigDecimal depreciationRate, BigDecimal monthDepreciationRate, BigDecimal monthPeriodDepreciationAmount, Boolean isActive) {
        StringBuilder sql = new StringBuilder();
        List<FixedAsset> fixedAssets = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM FIXEDASSET WHERE 1 = 1 ");
        if (branchID!=null) {
            sql.append("AND branchID = :branchID ");
            params.put("branchID",branchID);
        }
        if (fixedAssetCategoryID!=null) {
            sql.append("AND fixedAssetCategoryID = :fixedAssetCategoryID ");
            params.put("fixedAssetCategoryID", fixedAssetCategoryID);
        }
        if (organizationUnitID!=null) {
            sql.append("AND organizationUnitID = :organizationUnitID ");
            params.put("organizationUnitID", organizationUnitID);
        }
        if (!Strings.isNullOrEmpty(fixedAssetCode)) {
            sql.append("AND fixedAssetCode LIKE :fixedAssetCode ");
            params.put("fixedAssetCode","%" + fixedAssetCode + "%");
        }
        if (!Strings.isNullOrEmpty(fixedAssetName)) {
            sql.append("AND fixedAssetName = :fixedAssetName ");
            params.put("fixedAssetName", fixedAssetName);
        }
        if (!Strings.isNullOrEmpty(description)) {
            sql.append("AND description = :description ");
            params.put("description", description);
        }
        if (productionYear!=null) {
            sql.append("AND productionYear = :productionYear ");
            params.put("productionYear", productionYear);
        }
        if (!Strings.isNullOrEmpty(madeIn)) {
            sql.append("AND madeIn = :madeIn ");
            params.put("madeIn", madeIn);
        }
        if (!Strings.isNullOrEmpty(serialNumber)) {
            sql.append("AND serialNumber = :serialNumber ");
            params.put("serialNumber", serialNumber);
        }
        if (accountingObjectID!=null) {
            sql.append("AND accountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", accountingObjectID);
        }
        if (!Strings.isNullOrEmpty(accountingObjectName)) {
            sql.append("AND accountingObjectName = :accountingObjectName ");
            params.put("accountingObjectName", accountingObjectName);
        }
        if (!Strings.isNullOrEmpty(warranty)) {
            sql.append("AND warranty = :warranty ");
            params.put("warranty", warranty);
        }
        if (!Strings.isNullOrEmpty(guaranteeCodition)) {
            sql.append("AND guaranteeCodition = :guaranteeCodition ");
            params.put("guaranteeCodition", guaranteeCodition);
        }
        if (isSecondHand!=null) {
            sql.append("AND isSecondHand = :isSecondHand ");
            params.put("isSecondHand", isSecondHand);
        }
        if (currentState!=null) {
            sql.append("AND currentState = :currentState ");
            params.put("currentState", currentState);
        }
        if (!Strings.isNullOrEmpty(deliveryRecordNo)) {
            sql.append("AND currentState = :currentState ");
            params.put("currentState", currentState);
        }
        if (deliveryRecordDate!=null) {
            sql.append("AND deliveryRecordDate = :deliveryRecordDate ");
            params.put("deliveryRecordDate", deliveryRecordDate);
        }
        if (purchasedDate!=null) {
            sql.append("AND purchasedDate = :purchasedDate ");
            params.put("purchasedDate", purchasedDate);
        }
        if (incrementDate!=null) {
            sql.append("AND incrementDate = :incrementDate ");
            params.put("incrementDate", incrementDate);
        }
        if (depreciationDate!=null) {
            sql.append("AND depreciationDate = :depreciationDate ");
            params.put("depreciationDate", depreciationDate);
        }
        if (usedDate!=null) {
            sql.append("AND usedDate = :usedDate ");
            params.put("usedDate", usedDate);
        }
        if (!Strings.isNullOrEmpty(depreciationAccount)) {
            sql.append("AND depreciationAccount = :depreciationAccount ");
            params.put("depreciationAccount", depreciationAccount);
        }
        if (!Strings.isNullOrEmpty(originalPriceAccount)) {
            sql.append("AND originalPriceAccount = :originalPriceAccount ");
            params.put("originalPriceAccount", originalPriceAccount);
        }
        if (!Strings.isNullOrEmpty(expenditureAccount)) {
            sql.append("AND expenditureAccount = :expenditureAccount ");
            params.put("expenditureAccount", expenditureAccount);
        }
        if (purchasePrice!=null) {
            sql.append("AND purchasePrice = :purchasePrice ");
            params.put("purchasePrice", purchasePrice);
        }
        if (originalPrice!=null) {
            sql.append("AND originalPrice = :originalPrice ");
            params.put("originalPrice", originalPrice);
        }
        if (depreciationMethod!=null) {
            sql.append("AND depreciationMethod = :depreciationMethod ");
            params.put("depreciationMethod", depreciationMethod);
        }
        if (usedTime!=null) {
            sql.append("AND usedTime = :usedTime ");
            params.put("usedTime", usedTime);
        }
        if (displayMonthYear!=null) {
            sql.append("AND displayMonthYear = :displayMonthYear ");
            params.put("displayMonthYear", displayMonthYear);
        }
        if (periodDepreciationAmount!=null) {
            sql.append("AND periodDepreciationAmount = :periodDepreciationAmount ");
            params.put("periodDepreciationAmount", periodDepreciationAmount);
        }
        if (depreciationRate!=null) {
            sql.append("AND depreciationRate = :depreciationRate ");
            params.put("depreciationRate", depreciationRate);
        }
        if (monthDepreciationRate!=null) {
            sql.append("AND monthDepreciationRate = :monthDepreciationRate ");
            params.put("monthDepreciationRate", monthDepreciationRate);
        }
        if (monthPeriodDepreciationAmount!=null) {
            sql.append("AND monthPeriodDepreciationAmount = :monthPeriodDepreciationAmount ");
            params.put("monthPeriodDepreciationAmount", monthPeriodDepreciationAmount);
        }
        if (isActive != null) {
            sql.append("AND isActive = :isActive ");
            params.put("isActive", isActive);
        }
        Query countQuery = entityManager.createNativeQuery("Select Count(1)" + sql.toString());
        setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("Select *" + sql.toString(), FixedAsset.class);
            setParamsWithPageable(query, params, pageable, total);
            fixedAssets = query.getResultList();
        }
        return new PageImpl<>(fixedAssets, pageable, total.longValue());
    }

    @Override
    public List<FixedAssetDTO> getAllFixedAssetByActive(UUID org) {
        String sql = "select id, FixedAssetCode, FixedAssetName, departmentID, originalPrice from FixedAsset where CompanyID = :companyID and IsActive = 1 order by FixedAssetCode";
        Map<String, Object> params = new HashMap<>();
        params.put("companyID", org);
        Query query = entityManager.createNativeQuery(sql, "FixedAssetActiveDTO");
        Common.setParams(query, params);
        List<FixedAssetDTO> fixedAssetDTOS = query.getResultList();
        return fixedAssetDTOS;
    }

    @Override
    public FixedAssetDTO getById(UUID fixedAssetID) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append("ID, ");
        sql.append("fixedAssetCode, ");
        sql.append("fixedAssetName, ");
        sql.append("departmentID, ");
        sql.append("originalPrice, ");
        sql.append("usedTime, ");
        sql.append("depreciationRate, ");
        sql.append("purchasePrice, ");
        sql.append("depreciationAccount, ");
        sql.append("originalPriceAccount, ");
        sql.append("monthDepreciationRate, ");
        sql.append("monthPeriodDepreciationAmount ");
        sql.append(" from FixedAsset where id = :id ");

        Map<String, Object> params = new HashMap<>();
        params.put("id", fixedAssetID);
        Query query = entityManager.createNativeQuery(sql.toString(), "FixedAssetLedgerDTO");
        Common.setParams(query, params);
        List<FixedAssetDTO> fixedAssetDTOS = query.getResultList();
        if (fixedAssetDTOS.size() > 0) {
            return fixedAssetDTOS.get(0);
        }
        return null;
    }

    public static void setParamsWithPageable(@NotNull Query query, Map<String, Object> params, @NotNull Pageable pageable, @NotNull Number total) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
    }

    public static void setParams(@NotNull Query query, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }
    }
}
