package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.domain.MaterialQuantumDetails;
import vn.softdreams.ebweb.domain.PPOrder;
import vn.softdreams.ebweb.domain.PPOrderDetail;
import vn.softdreams.ebweb.repository.PporderRepositoryCustom;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.PPOrderDTO;
import vn.softdreams.ebweb.service.dto.SAOrderDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.dto.PPOrderSearchDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.Utils.Utils.setParams;

public class PporderRepositoryImpl implements PporderRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    private final JdbcTemplate jdbcTemplate;

    public PporderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Page<PPOrder> searchAll(Pageable pageable, PPOrderSearchDTO searchDTO) {
        StringBuilder sql = new StringBuilder();
        List<PPOrder> ppOrders = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        Number total = getQueryString(searchDTO, sql, params);
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " ORDER BY date DESC , No DESC ", PPOrder.class);
            if (pageable != null) {
                Common.setParamsWithPageable(query, params, pageable, total);
            } else {
                Common.setParams(query, params);
            }
            ppOrders = query.getResultList();
        }
        if (pageable == null) {
            return new PageImpl<>(ppOrders);
        }
        return new PageImpl<>(ppOrders, pageable, total.longValue());
    }

    @Override
    public Page<PPOrderDTO> findAllPPOrderDTO(Pageable pageable, UUID accountingObject, String fromDate, String toDate,
                                              Integer typeId, List<UUID> itemsSelected, String currency, UUID companyID) {
        List<PPOrderDTO> ppOrderDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select ppod.id as id, ppo.id as ppOrderId, CONVERT(varchar, ")
            .append("ppo.Date, 103) as orderDate, ")
            .append("ppo.No as orderNumber, ")
            .append("mg.MaterialGoodsCode as productCode, ")
            .append("ppod.Description as productName, ")
            .append("ppod.Quantity -  IIF(ppod.QuantityReceipt is null, 0, ppod.QuantityReceipt) as quantityReceipt, ")
            .append("row_number() over (ORDER BY date DESC, No DESC, ppod.orderPriority) as priority, ")
            .append("ppo.accountingObjectID, ")
            .append("ppo.reason, ")
            .append("a.TypeGroupID ");
        StringBuilder sqlFrom = new StringBuilder();
            sqlFrom.append("from PPOrder ppo ")
            .append("left join PPOrderDetail ppod on ppo.ID = ppod.PPOrderID ")
            .append("left join Type a on a.ID = ppo.TypeID ")
            .append("left join MaterialGoods mg on ppod.MaterialGoodsID = mg.ID ")
            .append("where ppo.CompanyID = :companyID and (ppod.Quantity - IIF(ppod.QuantityReceipt is null, 0, ppod.QuantityReceipt) != 0 and (ppod.QuantityReceipt <= ppod.Quantity or ppod.QuantityReceipt is null)");
        Map<String, Object> params = new HashMap<>();
        params.put("companyID", companyID);
        if (accountingObject != null) {
            sqlFrom.append("and ppo.AccountingObjectID = :accountingObject ");
            params.put("accountingObject", accountingObject);
        }
        if (!Strings.isNullOrEmpty(fromDate)) {
            sqlFrom.append("and CONVERT(varchar, ppo.Date, 112) >= :fromDate ");
            params.put("fromDate", fromDate);
        }
        if (!Strings.isNullOrEmpty(toDate)) {
            sqlFrom.append("and CONVERT(varchar, ppo.Date, 112) <= :toDate ");
            params.put("toDate", toDate);
        }
        if (typeId != null) {
            sqlFrom.append("and mg.MaterialGoodsType = :typeId ");
            params.put("typeId", typeId);
        }

        sqlFrom.append(") ");
        if (itemsSelected != null && itemsSelected.size() > 0) {
            sqlFrom.append("or ppod.id in :itemsSelected ");
            params.put("itemsSelected", itemsSelected);
        }
        if (!Strings.isNullOrEmpty(currency)) {
            sqlFrom.append("and ppo.CurrencyID = :currency ");
            params.put("currency", currency);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sqlFrom.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            sqlFrom.append(" ORDER BY date DESC , No DESC, ppod.orderPriority ");
            Query query = entityManager.createNativeQuery(sql.toString() + sqlFrom.toString(), "PPOrderDTO");
            Common.setParamsWithPageable(query, params, pageable, total);
            ppOrderDTOS = query.getResultList();
        }
        return new PageImpl<>(ppOrderDTOS, pageable, total.longValue());
    }

    @Override
    public Integer findByRowNumByID(PPOrderSearchDTO searchDTO, UUID id) {
        StringBuilder sql = new StringBuilder();
        Number rowNum = null;
        Map<String, Object> params = new HashMap<>();
        Number total = getQueryString(searchDTO, sql, params);
        if (total.longValue() > 0) {
            String newSql = "SELECT rowNum FROM (SELECT id, ROW_NUMBER() over (ORDER BY date DESC , No DESC ) rownum " + sql.toString() + ") a where a.id = :id";
            params.put("id", id);
            Query query = entityManager.createNativeQuery(newSql);
            Common.setParams(query, params);
            rowNum = (Number) query.getSingleResult();
        }
        return rowNum != null ? rowNum.intValue() : -1;
    }

    @Override
    public int checkReferences(UUID id) {
        if (id == null) return 0;

        List<String> referenceTables = Constants.PPOrderDetailReferenceTable.referenceTables;

        StringBuilder sql = new StringBuilder();

        for (int i = 0; i < referenceTables.size(); i++) {
            sql.append(" select count(*) as total from ")
                .append(referenceTables.get(i))
                .append(" where PPOrderID = ?1 ")
                .append(i == referenceTables.size() - 1 ? "" : " union ");
        }

        Query query = entityManager.createNativeQuery("select sum(total) sum from (" + sql.toString() + ") a");
        query.setParameter(1, id);
        Number sum = (Number) query.getSingleResult();
        return sum != null ? sum.intValue() : -1;
    }

    @Override
    public void updateReferences(List<UUID> deletedIDs) {
        if (deletedIDs == null || deletedIDs.size() == 0) return;

        StringBuilder sql = new StringBuilder();

        sql.append("begin ")
            .append(" update PPServiceDetail set PPOrderID = null, PPOrderDetailID = null where PPOrderDetailID in ?1 ")
            .append(" update PPInvoiceDetail set PPOrderID = null, PPOrderDetailID = null where PPOrderDetailID in ?1 ")
            .append(" update RSInwardOutwardDetail set PPOrderID = null, PPOrderDetailsID  = null where PPOrderDetailsID in ?1 ")
            .append("end");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter(1, deletedIDs);
        query.executeUpdate();
    }

    @Override
    public void updateReferencesByPPOrderID(UUID id) {
        if (id == null) return;

        StringBuilder sql = new StringBuilder();

        sql.append("begin ")
            .append(" update PPServiceDetail set PPOrderID = null, PPOrderDetailID = null where PPOrderID = ?1 ")
            .append(" update PPInvoiceDetail set PPOrderID = null, PPOrderDetailID = null where PPOrderID = ?1 ")
            .append(" update PPOrderDetail set QuantityReceipt = null where PPOrderID = ?1 ")
            .append(" update RSInwardOutwardDetail set PPOrderID = null, PPOrderDetailsID  = null where PPOrderID = ?1 ")
            .append("end");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter(1, id);
        query.executeUpdate();
    }

    @Override
    public List<PPOrderDTO> findPPOrderDetailsDTO(UUID id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select " +
                "       a.DiscountAmount, " +
                "       a.Description, " +
                "       b.MaterialGoodsCode, " +
                "       a.Quantity, " +
                "       c.UnitName, " +
                "       a.UnitPrice, " +
                "       a.UnitPriceOriginal, " +
                "       a.Amount, " +
                "       a.AmountOriginal, " +
                "       a.DiscountAmount, " +
                "       a.DiscountAmountOriginal, " +
                "       a.DiscountRate, " +
                "       a.VATRate, " +
                "       a.VATAmount, " +
                "       a.VATAmountOriginal," +
                "       a.orderPriority " +
                "        from PPOrderDetail a " +
                "            left join MaterialGoods b on a.MaterialGoodsID = b.ID " +
                "            left join Unit c on c.id = a.UnitID " +
                "where PPOrderID = :PPOrderID");
        params.put("PPOrderID", id);
        Query query = entityManager.createNativeQuery(sql.toString(), "PPOrderDTOReport");
        setParams(query, params);
        return query.getResultList();
    }

    @Override
    public PPOrder findByRowNum(PPOrderSearchDTO searchDTO, Integer rowNum) {
        StringBuilder sql = new StringBuilder();
        PPOrder ppOrder = null;
        Map<String, Object> params = new HashMap<>();
        Number total = getQueryString(searchDTO, sql, params);
        if (total.longValue() > 0) {
            String newSql = "SELECT * FROM (SELECT *, ROW_NUMBER() over (ORDER BY date DESC , No DESC ) rownum " + sql.toString() + ") a where a.rownum = :rowNum";
            params.put("rowNum", rowNum);
            Query query = entityManager.createNativeQuery(newSql, PPOrder.class);
            Common.setParams(query, params);
            ppOrder = (PPOrder) query.getSingleResult();
        }
        return ppOrder;
    }

    private Number getQueryString(PPOrderSearchDTO searchDTO, StringBuilder sql, Map<String, Object> params) {
        getQueryCriteria(searchDTO, sql, params);
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        return (Number) countQuerry.getSingleResult();
    }

    private void getQueryCriteria(PPOrderSearchDTO searchDTO, StringBuilder sql, Map<String, Object> params) {
        sql.append("FROM PPOrder WHERE 1 = 1 ");

        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            sql.append("AND CompanyID = :CompanyID ");
            params.put("CompanyID", currentUserLoginAndOrg.get().getOrg());
        }

        if (Objects.nonNull(searchDTO)) {
            if (!Strings.isNullOrEmpty(searchDTO.getCurrency())) {
                sql.append("AND CurrencyID = :currencyID ");
                params.put("currencyID", searchDTO.getCurrency());
            }
            if (!Strings.isNullOrEmpty(searchDTO.getFromDate())) {
                sql.append("AND Date >= :fromDate ");
                params.put("fromDate", searchDTO.getFromDate());
            }
            if (!Strings.isNullOrEmpty(searchDTO.getToDate())) {
                sql.append("AND Date <= :toDate ");
                params.put("toDate", searchDTO.getToDate());
            }
            if (searchDTO.getEmployee() != null) {
                sql.append("AND EmployeeID = :employeeID ");
                params.put("employeeID", searchDTO.getEmployee());
            }
            if (searchDTO.getStatus() != null && searchDTO.getStatus() != -1) {
                sql.append("AND Status = :status ");
                params.put("status", searchDTO.getStatus());
            }
            if (searchDTO.getAccountingObject() != null) {
                sql.append("AND AccountingObjectID = :accountingObjectID ");
                params.put("accountingObjectID", searchDTO.getAccountingObject());
            }

            if (!Strings.isNullOrEmpty(searchDTO.getSearchValue())) {
                sql.append("AND (No LIKE :searchValue ");
                sql.append("OR ContactName LIKE :searchValue ");
                sql.append("OR accountingObjectAddress LIKE :searchValue ");
                sql.append("OR ShippingPlace LIKE :searchValue ");
                sql.append("OR Reason LIKE :searchValue ");
                sql.append("OR CompanyTaxCode LIKE :searchValue) ");
                params.put("searchValue", "%" + searchDTO.getSearchValue() + "%");
            }
        }
    }

    @Override
    public void multiDeletePPOrder(UUID orgID, List<UUID> uuidList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("DELETE FROM PPOrder WHERE companyID = :orgID ");
        params.put("orgID", orgID);
        sql.append("AND ID IN :uuidList ");
        params.put("uuidList", uuidList);
        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, params);
        query.executeUpdate();
    }

    @Override
    public void multiDeleteChildPPOrder(String tableName, List<UUID> uuidList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        String deleteSQL = "DELETE FROM " + tableName + " ";
        sql.append(" WHERE PPOrderID IN :uuidList ");
        params.put("uuidList", uuidList);
        Query query = entityManager.createNativeQuery(deleteSQL + sql.toString());
        setParams(query, params);
        query.executeUpdate();
    }

    @Override
    public void updateReferencesByPPOrderListID(List<UUID> uuidList) {
        if (uuidList == null) return;
        String sql =
            "update PPServiceDetail set PPOrderID = null, PPOrderDetailID = null where PPOrderID = ?; " +
            "update PPInvoiceDetail set PPOrderID = null, PPOrderDetailID = null where PPOrderID = ?; " +
            "update PPOrderDetail set QuantityReceipt = null where PPOrderID = ?; " +
            "update RSInwardOutwardDetail set PPOrderID = null, PPOrderDetailsID  = null where PPOrderID = ?; ";

        jdbcTemplate.batchUpdate(sql, uuidList, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Common.revertUUID(detail).toString());
            ps.setString(2, Common.revertUUID(detail).toString());
            ps.setString(3, Common.revertUUID(detail).toString());
            ps.setString(4, Common.revertUUID(detail).toString());
        });
    }
    @Override
    public void updateStatus(String uuids) {
        Query query = entityManager.createNativeQuery("execute PROC_UPDATE_PP_ORDER :uuids");
        query.setParameter("uuids", ";" + uuids + ";");
        query.executeUpdate();
    }
}
