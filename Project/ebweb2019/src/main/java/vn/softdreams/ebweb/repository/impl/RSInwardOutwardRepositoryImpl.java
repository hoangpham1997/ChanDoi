package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.config.Constants;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.RSInwardOutwardDetailReportDTO;
import vn.softdreams.ebweb.service.dto.RSInwardOutwardDetailsDTO;
import vn.softdreams.ebweb.service.dto.ViewRSOutwardDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.web.rest.dto.IWVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.RSInwardOutwardSearchDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class RSInwardOutwardRepositoryImpl implements RSInwardOutwardRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private MaterialGoodsRepository materialGoodsRepository;

    @Autowired
    private RepositoryRepository repositoryRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private CostSetRepository costSetRepository;

    @Autowired
    private StatisticsCodeRepository statisticsCodeRepository;

    @Autowired
    private OrganizationUnitRepository organizationUnitRepository;

    @Autowired
    private ExpenseItemRepository expenseItemRepository;

    @Autowired
    private BudgetItemRepository budgetItemRepository;

    @Autowired
    private EMContractRepository contractRepository;

    @Autowired
    private PporderRepository pporderRepository;

    @Autowired
    private PporderdetailRepository pporderdetailRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

//    @Override
//    public void deleteById(String name, UUID id) {
//        Query query = entityManager.createNativeQuery("delete from RSInwardOutward where id in (select RSInwardOutwardId from " + name + " where id = :id )");
//        query.setParameter("id", id);
//        int i = query.executeUpdate();
//        System.out.println(i);
//    }

    @Override
    public Page<RSInwardOutwardSearchDTO> searchAll(Pageable pageable, UUID accountingObject, Integer status, String fromDate, String toDate, String searchValue, Integer noType, Integer currentBook) {
        StringBuilder sql = new StringBuilder();
        List<RSInwardOutwardSearchDTO> rsInwardOutwards = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        Number total = getQueryString(accountingObject, status, fromDate, toDate, searchValue, noType, currentBook, sql, params);
        if (total.longValue() > 0) {
            Query sumQuery = entityManager.createNativeQuery(new StringBuilder("Select sum(a.totalAmount) from (select ")
                .append(" (case ")
                .append(" when a.TypeID = 402 then pp.TotalInwardAmount ")
                .append(" when a.TypeID = 403 then sa.TotalOWAmount ")
                .append(" else a.TotalAmount end) totalAmount ")
                .append(sql).append(") a").toString());
            Common.setParams(sumQuery, params);
            BigDecimal sumTotalAmount = (BigDecimal) sumQuery.getSingleResult();

            StringBuilder select = new StringBuilder("select case a.TypeID when 400 then null when 402 then pp.ID when 403 then sa.ID end as refID,")
                .append(" a.id, " +
                    "   a.date, " +
                    "   a.postedDate, " +
                    "   a.noFBook, " +
                    "   a.noMBook, " +
                    "   a.accountingObjectName, " +
                    "   b.typeName, " +
                    "   a.reason, " +
                    "(case when a.TypeID = 402 then pp.TotalInwardAmount " +
                    "      when a.TypeID = 403 then sa.TotalOWAmount " +
                    " else a.TotalAmount end) totalAmount, " +
                    "   a.typeID, " +
                    "   a.recorded ")
                .append(sql)
                .append(" ORDER BY date DESC, NoFBook DESC, NoMBook DESC ");

            Query query = entityManager.createNativeQuery(select.toString(), "RSInwardOutwardSearchDTO");
            if (pageable != null) {
                Common.setParamsWithPageable(query, params, pageable, total);
            } else {
                Common.setParams(query, params);
            }
            // set tổng vào phần tử đầu tiên
            rsInwardOutwards = query.getResultList();
            if (rsInwardOutwards.size() > 0) {
                rsInwardOutwards.get(0).setSumTotalAmount(sumTotalAmount);
            }
        }
        if (pageable == null) {
            return new PageImpl<>(rsInwardOutwards);
        }
        return new PageImpl<>(rsInwardOutwards, pageable, total.longValue());
    }

    @Override
    public Page<RSInwardOutwardSearchDTO> searchAllOutWard(Pageable pageable, UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, String searchValue, Integer noType, Integer currentBook) {
        StringBuilder sql = new StringBuilder();
        List<RSInwardOutwardSearchDTO> rsInwardOutwards = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        Number total = getQueryStringOutWard(accountingObject, accountingObjectEmployee, status, fromDate, toDate, searchValue, noType, currentBook, sql, params);
        if (total.longValue() > 0) {
            Query sumQuery = entityManager.createNativeQuery(new StringBuilder(" Select sum(a.totalAmount) from (select a.totalAmount ")
                .append(sql).append(" ) a ").toString());
            Common.setParams(sumQuery, params);
            BigDecimal totalAmountSum = (BigDecimal) sumQuery.getSingleResult();

            StringBuilder select = new StringBuilder("select case a.TypeID when 410 then null when 411 then sa.ID ")
                .append(" when 412 then pp.ID end as refID, ")
                .append(" a.id, a.date, a.postedDate, a.noFBook, a.noMBook, a.accountingObjectName, b.typeName, a.reason, ")
                .append(" case a.TypeID when 410 then a.TotalAmount when 411 then sa.TotalVATAmount + sa.TotalAmount - sa.TotalDiscountAmount when 412 then pp.TotalVATAmount + pp.TotalAmount end as totalAmount, ")
                .append(" a.typeID, a.recorded, sa.isDeliveryVoucher, ")
                .append(" case a.TypeID when 411 then sa.InvoiceForm when 412 then pp.InvoiceForm end  as RefInvoiceForm, ")
                .append(" case a.TypeID when 411 then sa.InvoiceNo when 412 then pp.InvoiceNo end      as RefInvoiceNo, ")
                .append(" case a.TypeID when 411 then sa.IsBill when 412 then pp.IsBill end            as RefIsBill, ")
                .append(" case a.TypeID when 411 then sa.TypeID end as refTypeID ")
                .append(sql)
                .append(" ORDER BY date DESC, NoFBook DESC, NoMBook DESC ");

            Query query = entityManager.createNativeQuery(select.toString(), "RSOutwardSearchDTO");
            if (pageable != null) {
                Common.setParamsWithPageable(query, params, pageable, total);
            } else {
                Common.setParams(query, params);
            }
            rsInwardOutwards = query.getResultList();
            rsInwardOutwards.get(0).setSumTotalAmount(totalAmountSum);
        }
        if (pageable == null) {
            return new PageImpl<>(rsInwardOutwards);
        }
        return new PageImpl<>(rsInwardOutwards, pageable, total.longValue());
    }

    @Override
    public Integer findRowNumByID(UUID accountingObject, Integer status, String fromDate, String toDate, Integer noType, String searchValue, UUID id, int currentBook) {
        StringBuilder sql = new StringBuilder();
        Number rowNum = null;
        Map<String, Object> params = new HashMap<>();
        Number total = getQueryString(accountingObject, status, fromDate, toDate, searchValue, noType, currentBook, sql, params);
        if (total.longValue() > 0) {
            String newSql = "SELECT rowNum FROM (SELECT a.id, ROW_NUMBER() over (ORDER BY a.date DESC, iif(a.TypeLedger in (:book, 2), a.NoFBook, a.NoMBook) desc ) rownum " + sql.toString() + ") a where a.id = :id";
            params.put("id", id);
            params.put("book", currentBook);
            Query query = entityManager.createNativeQuery(newSql);
            Common.setParams(query, params);
            rowNum = (Number) query.getSingleResult();
        }
        return rowNum != null ? rowNum.intValue() : -1;
    }

    @Override
    public Integer findRowNumOutWardByID(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, UUID id, int currentBook) {
        StringBuilder sql = new StringBuilder();
        Number rowNum = null;
        Map<String, Object> params = new HashMap<>();
        Number total = getQueryStringOutWard(accountingObject, accountingObjectEmployee, status, fromDate, toDate, searchValue, noType, currentBook, sql, params);
        if (total.longValue() > 0) {
            String newSql = "SELECT rowNum FROM (SELECT a.id, ROW_NUMBER() over (ORDER BY a.date DESC, iif(a.TypeLedger in (:book, 2), a.NoFBook, a.NoMBook) desc ) rownum " + sql.toString() + ") a where a.id = :id";
            params.put("id", id);
            params.put("book", currentBook);
            Query query = entityManager.createNativeQuery(newSql);
            Common.setParams(query, params);
            rowNum = (Number) query.getSingleResult();
        }
        return rowNum != null ? rowNum.intValue() : -1;
    }

    @Override
    public RSInwardOutwardSearchDTO findReferenceTablesID(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, Integer rowNum, int currentBook) {
        StringBuilder sql = new StringBuilder();
        RSInwardOutwardSearchDTO rsInwardOutward = null;
        Map<String, Object> params = new HashMap<>();
        Number total = getQueryStringOutWard(accountingObject, accountingObjectEmployee, status, fromDate, toDate, searchValue, noType, currentBook, sql, params);
        if (total.longValue() > 0) {
            StringBuilder select = new StringBuilder("select case a.TypeID when 410 then a.ID when 411 then sa.ID ")
                .append(" when 412 then pp.ID end as refID, a.id, a.typeid from ")
                .append(" ( SELECT id, typeid FROM (SELECT a.*, ROW_NUMBER() over (ORDER BY a.date DESC, iif(a.TypeLedger in (:book, 2), a.NoFBook, a.NoMBook) desc ) rownum ")
                .append(sql)
                .append(") a where a.rownum = :rowNum) a ")
                .append(" left join SAInvoice sa on sa.RSInwardOutwardID = a.ID and a.TypeID = 411 ")
                .append(" left join PPDiscountReturn pp on pp.RSInwardOutwardID = a.ID and a.TypeID = 412 ");
            params.put("rowNum", rowNum);
            params.put("book", currentBook);
            Query query = entityManager.createNativeQuery(select.toString(), "findReferenceTableID");
            Common.setParams(query, params);
            rsInwardOutward = (RSInwardOutwardSearchDTO) query.getSingleResult();
        }
        return rsInwardOutward;
    }

    @Override
    public RSInwardOutward findByRowNumOutWard(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, Integer rowNum, int currentBook) {
        StringBuilder sql = new StringBuilder();
        RSInwardOutward rsInwardOutward = null;
        Map<String, Object> params = new HashMap<>();
        Number total = getQueryStringOutWard(accountingObject, accountingObjectEmployee, status, fromDate, toDate, searchValue, noType, currentBook, sql, params);
        if (total.longValue() > 0) {
            String newSql = "SELECT * FROM (SELECT a.*, ROW_NUMBER() over (ORDER BY date DESC, iif(TypeLedger in (0, 2), NoFBook, NoMBook) desc ) rownum " + sql.toString() + ") a where a.rownum = :rowNum";
            params.put("rowNum", rowNum);
            Query query = entityManager.createNativeQuery(newSql, RSInwardOutward.class);
            Common.setParams(query, params);
            rsInwardOutward = (RSInwardOutward) query.getSingleResult();
        }
        return rsInwardOutward;
    }

    @Override
    public RSInwardOutwardSearchDTO findReferenceTableID(UUID accountingObject, Integer status, String fromDate, String toDate, Integer noType, String searchValue, Integer rowNum, int currentBook) {
        StringBuilder sql = new StringBuilder();
        RSInwardOutwardSearchDTO rsInwardOutward = null;
        Map<String, Object> params = new HashMap<>();
        Number total = getQueryString(accountingObject, status, fromDate, toDate, searchValue, noType, currentBook, sql, params);
        if (total.longValue() > 0) {
            StringBuilder select = new StringBuilder("select case a.TypeID when 400 then null when 402 then pp.ID ")
                .append(" when 403 then sa.ID end as refID, a.id, a.typeid from ( ")
                .append(" SELECT id, typeid FROM (SELECT a.*, ROW_NUMBER() over (ORDER BY a.date DESC, iif(a.TypeLedger in (:book, 2), a.NoFBook, a.NoMBook) desc ) rownum ")
                .append(sql)
                .append(") a where a.rownum = :rowNum) a ")
                .append(" left join SAReturn sa on sa.RSInwardOutwardID = a.ID and a.TypeID = 403 ")
                .append(" left join PPInvoice pp on pp.RSInwardOutwardID = a.ID and a.TypeID = 402 ");
            params.put("rowNum", rowNum);
            params.put("book", currentBook);
            Query query = entityManager.createNativeQuery(select.toString(), "findReferenceTableID");
            Common.setParams(query, params);
            rsInwardOutward = (RSInwardOutwardSearchDTO) query.getSingleResult();
        }
        return rsInwardOutward;
    }

    @Override
    public void unRecordDetails(List<UUID> ids) {
        String sql = "UPDATE RSInwardOutwardDetail SET SaReturnID = null, SaReturnDetailsID = null, PPOrderID = null, PPOrderDetailsID = null WHERE ID = ?";
        jdbcTemplate.batchUpdate(
            sql,
            ids,
            Constants.BATCH_SIZE,
            (ps, id) -> ps.setString(1, Utils.uuidConvertToGUID(id).toString())
        );
    }

    @Override
    public void deleteByListID(List<UUID> ids) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("DELETE FROM RSInwardOutward WHERE ID IN :ids ");
        params.put("ids", ids);
        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, params);
        query.executeUpdate();
    }

    @Override
    public List<RSInwardOutwardDetailReportDTO> getRSInwardOutWardDetails(UUID id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select mg.MaterialGoodsCode, " +
            "       mg.MaterialGoodsName, " +
            "       ut.UnitName, " +
            "       rsdtl.Quantity, " +
            "       rsdtl.AmountOriginal, " +
            "       rsdtl.Amount, " +
            "       rsdtl.CreditAccount, " +
            "       rsdtl.DebitAccount, " +
            "       rs.RepositoryCode, " +
            "       rsdtl.UnitPriceOriginal, " +
            "       rs.RepositoryName, " +
            "       rsdtl.Description " +
            "from RSInwardOutwardDetail rsdtl " +
            "         left join Unit ut on ut.ID = rsdtl.UnitID " +
            "         left join Repository rs on rs.ID = rsdtl.RepositoryID " +
            "         left join MaterialGoods mg on mg.id = rsdtl.MaterialGoodsID " +
            "where rsdtl.RSInwardOutwardID = :id order by OrderPriority");
        params.put("id", id);
        Query query = entityManager.createNativeQuery(sql.toString(), "RSInwardOutwardDetailReportDTO");
        setParams(query, params);
        return query.getResultList();
    }

    public Page<IWVoucherDTO> getIWVoucher(Pageable pageable, String fromDate, String toDate, UUID objectId) {
        StringBuilder sql = new StringBuilder();
        List<IWVoucherDTO> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("from RepositoryLedger rl left join Repository r on r.ID = rl.RepositoryID " +
            "left join Unit u on u.ID = rl.UnitID " +
            "left join Unit mu on mu.ID = rl.MainUnitID where rl.typeID in (400, 401, 402, 403, 741) and rl.materialGoodsID = :objectID ");
        params.put("objectID", objectId);
        if (!StringUtils.isEmpty(fromDate)) {
            sql.append("and rl.Date >= :fromDate ");
            params.put("fromDate", fromDate);
        }
        if (!StringUtils.isEmpty(toDate)) {
            sql.append("and rl.Date <= :toDate ");
            params.put("toDate", toDate);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(*) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("select rl.referenceID as id," +
                "      rl.detailID," +
                "      rl.noMBook," +
                "      rl.noFBook," +
                "      rl.date," +
                "      rl.repositoryID," +
                "      r.repositoryCode," +
                "      rl.unitID," +
                "      u.unitName," +
                "      rl.unitPrice," +
                "      (rl.iwquantity - (select ISNULL(SUM(rl1.OWQuantity),0) from RepositoryLedger rl1 where rl1.ConfrontDetailID = rl.DetailID)) as quantity," +
                "      (rl.iwamount - (select ISNULL(SUM(rl1.OWAmount),0) from RepositoryLedger rl1 where rl1.ConfrontDetailID = rl.DetailID)) as amount," +
                "      rl.lotNo," +
                "      rl.expiryDate," +
                "      rl.mainUnitID," +
                "      mu.unitName as mainUnitName," +
                "      rl.mainIWQuantity as mainQuantity," +
                "      rl.mainUnitPrice," +
                "      rl.typeID " +
                sql.toString() + " order by Date DESC ,noFBook DESC, noMBook DESC", "IWVoucherDTO");
            setParamsWithPageable(query, params, pageable, total);
            lst = query.getResultList();
        }
        return new PageImpl<>(lst, pageable, total.longValue());
    }

    @Override
    public void updateAfterDeleteMaterialQuantum(UUID id, List<MaterialQuantumDetails> materialQuantumDetails, UUID rsInwardOutwardID) {
        String sql = "UPDATE RSInwardOutwardDetail SET MaterialQuantumID = NULL, MaterialQuantumDetailID = NULL WHERE MaterialQuantumID = ? OR MaterialQuantumDetailID = ?";
        jdbcTemplate.batchUpdate(sql, materialQuantumDetails, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Common.revertUUID(rsInwardOutwardID).toString());
            ps.setString(2, Common.revertUUID(detail.getId()).toString());
        });
    }

    @Override
    public RSInwardOutwardSearchDTO findByID(UUID id) {
        StringBuilder sql = new StringBuilder();
        RSInwardOutwardSearchDTO rsInwardOutward = null;
        if (id != null) {
            Map<String, Object> params = new HashMap<>();
            sql.append(" Select * from RSInwardOutward where id = :id");
            params.put("id", id);
            Query query = entityManager.createNativeQuery(sql.toString(), "RSOutwardSearchDTOFind");
            Common.setParams(query, params);
            List<RSInwardOutwardSearchDTO> rsInwardOutwards = query.getResultList();
            if (rsInwardOutwards.size() > 0) {
                rsInwardOutward = rsInwardOutwards.get(0);
            }
            rsInwardOutward = (RSInwardOutwardSearchDTO) query.getSingleResult();
        }
        return rsInwardOutward;
    }

    @Override
    public void multiDeleteRSInwardOutward(UUID orgID, List<UUID> uuidList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("DELETE FROM RSInwardOutward WHERE companyID = :orgID ");
        params.put("orgID", orgID);
        sql.append("AND ID IN :uuidList ");
        params.put("uuidList", uuidList);
        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, params);
        query.executeUpdate();
    }

    @Override
    public void multiDeleteChildRSInwardOutward(String tableName, List<UUID> uuidList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        String deleteSQL = "DELETE FROM " + tableName + " ";
        sql.append(" WHERE RSInwardOutwardID IN :uuidList ");
        params.put("uuidList", uuidList);
        Query query = entityManager.createNativeQuery(deleteSQL + sql.toString());
        setParams(query, params);
        query.executeUpdate();
    }

    @Override
    public void updateUnrecordRS(List<UUID> uuidList) {
        String sql = "Update RSInwardOutward SET Recorded = 0 WHERE id = ?;"+
            "Delete GeneralLedger WHERE referenceID = ?;";
        jdbcTemplate.batchUpdate(sql, uuidList, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail).toString());
        });
    }

    @Override
    public List<RSInwardOutwardDetailsDTO> getListUpdateUnitPrice(List<UUID> materialGoodsIDs, Integer soLamViec, LocalDate fromDate, LocalDate toDate, List<UUID> costSetIDs) {
        StringBuilder sql = new StringBuilder();
        List<RSInwardOutwardDetailsDTO> rsInwardOutWardDetails = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("select rsd.ID,  " +
            "       rsd.RSInwardOutwardID,  " +
            "       rsd.MaterialGoodsID,  " +
            "       rsd.UnitID,  " +
            "       rsd.MainUnitID,  " +
            "       rsd.DebitAccount,  " +
            "       rsd.CreditAccount,  " +
            "       rsd.Quantity,  " +
            "       rsd.UnitPrice,  " +
            "       rsd.UnitPriceOriginal,  " +
            "       rsd.MainQuantity ,  " +
            "       rsd.MainUnitPrice ,  " +
            "       rsd.MainConvertRate ,  " +
            "       rsd.Formula Formula,  " +
            "       rsd.Amount Amount,  " +
            "       rsd.AmountOriginal " +
            "from RSInwardOutwardDetail rsd " +
            "         left join RSInwardOutward rs on rs.ID = rsd.RSInwardOutwardID " +
            "where rsd.MaterialGoodsID in :mids " +
            "  and rsd.CostSetID in :cids " +
            "  and (rs.TypeLedger = :soLamViec or rs.TypeLedger = 2) " +
            "  and rs.Recorded = 1 " +
            "  and rs.TypeID = 400 " +
            "  and rs.PostedDate >= :fromDate " +
            "  and rs.PostedDate <= :toDate " +
            "  and rsd.DebitAccount like '155%' " +
            "  and rsd.CreditAccount like '154%'");
        params.put("mids", materialGoodsIDs);
        params.put("cids", costSetIDs);
        params.put("soLamViec", soLamViec);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        Query query = entityManager.createNativeQuery(sql.toString(), "RSInwardOutwardDetailsDTO");
        Common.setParams(query, params);
        rsInwardOutWardDetails = query.getResultList();
        return rsInwardOutWardDetails;
    }


    private Number getQueryString(UUID accountingObject, Integer status, String fromDate, String toDate, String searchValue, Integer noType, Integer currentBook, StringBuilder sql, Map<String, Object> params) {
        sql.append("FROM RSInwardOutward a left join Type b on a.typeID = b.ID ")
            .append(" left join SAReturn sa on sa.RSInwardOutwardID = a.ID and a.TypeID = 403 ")
            .append(" left join PPInvoice pp on pp.RSInwardOutwardID = a.ID and a.TypeID = 402 ")
            .append(" WHERE a.typeID / 10 = 40 ");
        if (!Strings.isNullOrEmpty(fromDate)) {
            sql.append("AND a.Date >= :fromDate ");
            params.put("fromDate", fromDate);
        }
        if (!Strings.isNullOrEmpty(toDate)) {
            sql.append("AND a.Date <= :toDate ");
            params.put("toDate", toDate);
        }
        if (status != null && status != 0) {
            sql.append("AND a.Recorded = :status ");
            params.put("status", status == 1);
        }
        if (noType != null && noType != 0) {
            sql.append("AND a.typeID = :typeID ");
            params.put("typeID", noType);
        }
        if (accountingObject != null) {
            sql.append("AND a.AccountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", accountingObject);
        }
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            sql.append("AND a.CompanyID = :CompanyID ");
            params.put("CompanyID", currentUserLoginAndOrg.get().getOrg());
        }
        if (currentBook != null) {
            sql.append("AND a.typeLedger in (:book, 2) ");
            params.put("book", currentBook);
        }

        if (!Strings.isNullOrEmpty(searchValue)) {
            if (currentBook != null) {
                if (currentBook == 0) {
                    sql.append("AND (a.NoFBook LIKE :searchValue ");
                } else {
                    sql.append("AND (a.NoMBook LIKE :searchValue ");
                }
            }
            sql.append("OR a.ContactName LIKE :searchValue ");
            sql.append("OR a.accountingObjectAddress LIKE :searchValue ");
            sql.append("OR a.Reason LIKE :searchValue ");
            sql.append("OR a.NumberAttach LIKE :searchValue ) ");
            params.put("searchValue", "%" + searchValue + "%");
        }

        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        return (Number) countQuerry.getSingleResult();
    }

    private Number getQueryStringOutWard(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, String searchValue, Integer noType, Integer currentBook, StringBuilder sql, Map<String, Object> params) {
        sql.append(" FROM RSInwardOutward a inner join Type b on a.typeID = b.ID ")
            .append(" left join SAInvoice sa on sa.RSInwardOutwardID = a.ID and a.TypeID = 411 ")
            .append(" left join PPDiscountReturn pp on pp.RSInwardOutwardID = a.ID and a.TypeID = 412 ")
            .append(" left join AccountingObject aobj on aobj.ID = a.EmployeeID ")
            .append(" WHERE a.typeID / 10 = 41 and a.id is not null ");
        if (!Strings.isNullOrEmpty(fromDate)) {
            sql.append("AND a.Date >= :fromDate ");
            params.put("fromDate", fromDate);
        }
        if (!Strings.isNullOrEmpty(toDate)) {
            sql.append("AND a.Date <= :toDate ");
            params.put("toDate", toDate);
        }
        if (status != null) {
            sql.append("AND a.Recorded = :status ");
            params.put("status", status == 1);
        }
        if (noType != null) {
            sql.append("AND a.typeID = :typeID ");
            params.put("typeID", noType);
        }
        if (accountingObject != null) {
            sql.append("AND a.AccountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", accountingObject);
        }
        if (accountingObjectEmployee != null) {
            sql.append("AND a.AccountingObjectEmployeeID = :AccountingObjectEmployeeID ");
            params.put("AccountingObjectEmployeeID", accountingObjectEmployee);
        }
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        if (currentUserLoginAndOrg.isPresent()) {
            sql.append("AND a.CompanyID = :CompanyID ");
            params.put("CompanyID", currentUserLoginAndOrg.get().getOrg());
        }
        if (currentBook != null) {
            sql.append("AND a.typeLedger in (:book, 2) ");
            params.put("book", currentBook);
        }
        if (!Strings.isNullOrEmpty(searchValue)) {
            if (currentBook != null) {
                if (currentBook == 0) {
                    sql.append("AND (a.NoFBook LIKE :searchValue ");
                } else {
                    sql.append("AND (a.NoMBook LIKE :searchValue ");
                }
            }
            sql.append("OR a.ContactName LIKE :searchValue ");
            sql.append("OR a.accountingObjectAddress LIKE :searchValue ");
            sql.append("OR aobj.AccountingObjectCode LIKE :searchValue ");
            sql.append("OR aobj.AccountingObjectName LIKE :searchValue ");
            sql.append("OR a.Reason LIKE :searchValue ");
            sql.append("OR a.NumberAttach LIKE :searchValue ) ");
            params.put("searchValue", "%" + searchValue + "%");
        }

        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        return (Number) countQuerry.getSingleResult();
    }

    @Override
    public List<RSInwardOutWardDetails> getDetailsById(UUID id, Integer typeID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        StringBuilder sql = new StringBuilder();
        if (typeID == TypeConstant.NHAP_KHO) {
            sql.append("select a.* from RSInwardOutwardDetail a left join RSInwardOutward b on a.RSInwardOutwardID = b.ID where a.RSInwardOutwardID = ?1 and b.companyID = ?2");
        } else {
            Map<Integer, List<String>> referenceTableMap = Common.getRSReferenceTableMap();
            sql.append("select * from ")
                .append(referenceTableMap.get(typeID).get(0))
                .append(" detail where ")
                .append(referenceTableMap.get(typeID).get(1))
                .append(" = (select id from ")
                .append(referenceTableMap.get(typeID).get(2))
                .append(" where RSInwardOutwardID = ?1 and CompanyID = ?2) ");
        }

        Query query = entityManager.createNativeQuery(sql.toString(), getReturnEntityFromTypeID(typeID));
        query.setParameter(1, id);
        currentUserLoginAndOrg.ifPresent(securityDTO -> query.setParameter(2, securityDTO.getOrg()));
        if (typeID == TypeConstant.NHAP_KHO_TU_MUA_HANG) {
            return getFromPPInvoice(query.getResultList());
        } else if (typeID == TypeConstant.NHAP_KHO_TU_BAN_HANG_TRA_LAI) {
            return getFromSaReturn(query.getResultList());
        } else if (typeID == TypeConstant.NHAP_KHO_TU_DIEU_CHINH) {
            // TODO: Nhập kho từ điều chỉnh
            return null;
        } else {
            return query.getResultList();
        }
    }

    // Click show detail xuất kho
    @Override
    public List<RSInwardOutWardDetails> getDetailsOutWardById(UUID id, Integer typeID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        StringBuilder sql = new StringBuilder();
        if (typeID == TypeConstant.XUAT_KHO) {
            sql.append("select a.* from RSInwardOutwardDetail a left join RSInwardOutward b on a.RSInwardOutwardID = b.ID where a.RSInwardOutwardID = ?1 and b.companyID = ?2 ");
        } else {
            Map<Integer, List<String>> referenceTableMap = Common.getRSOutWardReferenceTableMap();
            sql.append("select * from ")
                .append(referenceTableMap.get(typeID).get(0))
                .append(" detail where ")
                .append(referenceTableMap.get(typeID).get(1))
                .append(" = (select id from ")
                .append(referenceTableMap.get(typeID).get(2))
                .append(" where RSInwardOutwardID = ?1 and CompanyID = ?2) ");
        }

        Query query = entityManager.createNativeQuery(sql.toString(), getReturnEntityFromTypeID(typeID));
        query.setParameter(1, id);
        currentUserLoginAndOrg.ifPresent(securityDTO -> query.setParameter(2, securityDTO.getOrg()));
        if (typeID == TypeConstant.XUAT_KHO_TU_BAN_HANG) {
            return getFromSAInvoice(query.getResultList());
        } else if (typeID == TypeConstant.XUAT_KHO_TU_HANG_MUA_TRA_LAI) {
            return getFromPPDiscountReturn(query.getResultList());
        } else if (typeID == TypeConstant.XUAT_KHO_TU_DIEU_CHINH) {
            // TODO: Xuất kho từ điều chỉnh
            return null;
        } else {
            return query.getResultList();
        }
    }

    @Override
    public Page<ViewRSOutwardDTO> findAllView(Pageable pageable, UUID org, UUID accountingObjectID, String fromDate, String toDate, String soLamViec, String currencyID) {
        StringBuilder sql = new StringBuilder();
        List<ViewRSOutwardDTO> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("from RSInwardOutwardDetail a " +
            "         left join " +
            "     MaterialGoods ON MaterialGoods.ID = a.MaterialGoodsID " +
            "         right join RSInwardOutward on RSInwardOutward.ID = a.RSInwardOutwardID " +
            "where RSInwardOutward.TypeID = 410 AND RSInwardOutward.Recorded = 1 AND RSInwardOutward.CompanyID = :companyID ");
        params.put("companyID", org);
        sql.append("  and ( RSInwardOutward.TypeLedger = 2 or RSInwardOutward.TypeLedger = :typeLedger) ");
        params.put("typeLedger", soLamViec);
        if (currencyID != null) {
            sql.append("  and RSInwardOutward.CurrencyID = :currencyID ");
            params.put("currencyID", currencyID);
        }
        if (accountingObjectID != null) {
            sql.append("and AccountingObjectID = :accountingObjectID ");
            params.put("accountingObjectID", accountingObjectID);
        }
        if (!StringUtils.isEmpty(fromDate)) {
            sql.append("and Date >= :fromDate ");
            params.put("fromDate", fromDate);
        }
        if (!StringUtils.isEmpty(toDate)) {
            sql.append("and Date <= :toDate ");
            params.put("toDate", toDate);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("select a.RSInwardOutwardID as ID, " +
                "       a.ID as rSInwardOutwardDetailID, " +
                "       TypeID, " +
                "       RSInwardOutward.CompanyID, " +
                "       case when :typeLedger = 0 then RSInwardOutward.NoFBook else RSInwardOutward.NoMBook end as No, " +
                "       Date," +
                "       a.MaterialGoodsID," +
                "       MaterialGoodsCode," +
                "       a.Description," +
                "       Reason," +
                "       a.Quantity," +
                "       a.unitID," +
                "       a.mainQuantity," +
                "       a.mainUnitID," +
                "       a.mainUnitPrice," +
                "       a.mainConvertRate," +
                "       a.formula," +
                "       a.CreditAccount," +
                "       a.DebitAccount," +
                "       RSInwardOutward.accountingObjectID," +
                "       RSInwardOutward.accountingObjectName," +
                "       RSInwardOutward.accountingObjectAddress," +
                "       RSInwardOutward.contactName," +
                "       RSInwardOutward.employeeID " +
                sql.toString() + " order by Date DESC ,No DESC", "RSOutwardDTOPopup");
            setParamsWithPageable(query, params, pageable, total);
            lst = query.getResultList();
        }
        return new PageImpl<>(((List<ViewRSOutwardDTO>) lst), pageable, total.longValue());
    }

    private List<RSInwardOutWardDetails> getFromPPInvoice(List<PPInvoiceDetails> detailsResult) {
        return detailsResult.stream().map(item -> {
            RSInwardOutWardDetails rsInwardOutWardDetails = new RSInwardOutWardDetails();
            if (item.getMaterialGoodsId() != null) {
                materialGoodsRepository.findById(item.getMaterialGoodsId()).ifPresent(rsInwardOutWardDetails::setMaterialGood);
            }
            if (item.getRepositoryId() != null) {
                repositoryRepository.findById(item.getRepositoryId()).ifPresent(rsInwardOutWardDetails::setRepository);
            }
            if (item.getUnitId() != null) {
                unitRepository.findById(item.getUnitId()).ifPresent(rsInwardOutWardDetails::setUnit);
            }
            if (item.getMainUnitId() != null) {
                unitRepository.findById(item.getMainUnitId()).ifPresent(rsInwardOutWardDetails::setMainUnit);
            }
            if (item.getCostSetId() != null) {
                costSetRepository.findById(item.getCostSetId()).ifPresent(rsInwardOutWardDetails::setCostSet);
            }
            if (item.getStatisticCodeId() != null) {
                statisticsCodeRepository.findById(item.getStatisticCodeId()).ifPresent(rsInwardOutWardDetails::setStatisticsCode);
            }
            if (item.getDepartmentId() != null) {
                organizationUnitRepository.findById(item.getDepartmentId()).ifPresent(rsInwardOutWardDetails::setDepartment);
            }
            if (item.getExpenseItemId() != null) {
                expenseItemRepository.findById(item.getExpenseItemId()).ifPresent(rsInwardOutWardDetails::setExpenseItem);
            }
            if (item.getBudgetItemId() != null) {
                budgetItemRepository.findById(item.getBudgetItemId()).ifPresent(rsInwardOutWardDetails::setBudgetItem);
            }
            if (item.getContractId() != null) {
                contractRepository.findById(item.getContractId()).ifPresent(rsInwardOutWardDetails::setContract);
            }
            rsInwardOutWardDetails.setDescription(item.getDescription());
            rsInwardOutWardDetails.setDebitAccount(item.getDebitAccount());
            rsInwardOutWardDetails.setCreditAccount(item.getCreditAccount());
            rsInwardOutWardDetails.setQuantity(item.getQuantity());
            rsInwardOutWardDetails.setUnitPrice(item.getUnitPrice());
            rsInwardOutWardDetails.setUnitPriceOriginal(item.getUnitPriceOriginal());
            rsInwardOutWardDetails.setMainQuantity(item.getMainQuantity());
            rsInwardOutWardDetails.setMainUnitPrice(item.getMainUnitPrice());
            rsInwardOutWardDetails.setMainConvertRate(item.getMainConvertRate());
            rsInwardOutWardDetails.setFormula(item.getFormula());
            rsInwardOutWardDetails.setAmount(item.getAmount());
            rsInwardOutWardDetails.setAmountOriginal(item.getAmountOriginal());
            rsInwardOutWardDetails.setExpiryDate(item.getExpiryDate());
            rsInwardOutWardDetails.setLotNo(item.getLotNo());
            if (item.getPpOrderId() != null) {
                pporderRepository.findById(item.getPpOrderId()).ifPresent(rsInwardOutWardDetails::setPpOrder);
            }
            if (item.getPpOrderDetailId() != null) {
                pporderdetailRepository.findById(item.getPpOrderDetailId()).ifPresent(rsInwardOutWardDetails::setPpOrderDetail);
            }
            rsInwardOutWardDetails.setOrderPriority(item.getOrderPriority());
            return rsInwardOutWardDetails;
        }).collect(Collectors.toList());
    }


    private List<RSInwardOutWardDetails> getFromSaReturn(List<SaReturnDetails> detailsResult) {
        return detailsResult.stream().map(item -> {
            RSInwardOutWardDetails rsInwardOutWardDetails = new RSInwardOutWardDetails();
            if (item.getMaterialGoodsID() != null) {
                materialGoodsRepository.findById(item.getMaterialGoodsID()).ifPresent(rsInwardOutWardDetails::setMaterialGood);
            }
            if (item.getRepositoryID() != null) {
                repositoryRepository.findById(item.getRepositoryID()).ifPresent(rsInwardOutWardDetails::setRepository);
            }
            if (item.getUnitID() != null) {
                unitRepository.findById(item.getUnitID()).ifPresent(rsInwardOutWardDetails::setUnit);
            }
            if (item.getMainUnitID() != null) {
                unitRepository.findById(item.getMainUnitID()).ifPresent(rsInwardOutWardDetails::setMainUnit);
            }
            if (item.getCostSetID() != null) {
                costSetRepository.findById(item.getCostSetID()).ifPresent(rsInwardOutWardDetails::setCostSet);
            }
            if (item.getStatisticsCodeID() != null) {
                statisticsCodeRepository.findById(item.getStatisticsCodeID()).ifPresent(rsInwardOutWardDetails::setStatisticsCode);
            }
            if (item.getDepartmentID() != null) {
                organizationUnitRepository.findById(item.getDepartmentID()).ifPresent(rsInwardOutWardDetails::setDepartment);
            }
            if (item.getExpenseItemID() != null) {
                expenseItemRepository.findById(item.getExpenseItemID()).ifPresent(rsInwardOutWardDetails::setExpenseItem);
            }
            if (item.getBudgetItemID() != null) {
                budgetItemRepository.findById(item.getBudgetItemID()).ifPresent(rsInwardOutWardDetails::setBudgetItem);
            }
            if (item.getContractID() != null) {
                contractRepository.findById(item.getContractID()).ifPresent(rsInwardOutWardDetails::setContract);
            }
            rsInwardOutWardDetails.setDescription(item.getDescription());
            rsInwardOutWardDetails.setDebitAccount(item.getDebitAccount());
            rsInwardOutWardDetails.setCreditAccount(item.getCreditAccount());
            rsInwardOutWardDetails.setQuantity(item.getQuantity());
            rsInwardOutWardDetails.setUnitPrice(item.getUnitPrice());
            rsInwardOutWardDetails.setUnitPriceOriginal(item.getUnitPriceOriginal());
            rsInwardOutWardDetails.setMainQuantity(item.getMainQuantity());
            rsInwardOutWardDetails.setMainUnitPrice(item.getMainUnitPrice());
            rsInwardOutWardDetails.setMainConvertRate(item.getMainConvertRate());
            rsInwardOutWardDetails.setFormula(item.getFormula());
            rsInwardOutWardDetails.setAmount(item.getAmount());
            rsInwardOutWardDetails.setAmountOriginal(item.getAmountOriginal());
            rsInwardOutWardDetails.setExpiryDate(item.getExpiryDate());
            rsInwardOutWardDetails.setLotNo(item.getLotNo());
            rsInwardOutWardDetails.setOrderPriority(item.getOrderPriority());
            return rsInwardOutWardDetails;
        }).collect(Collectors.toList());
    }

    private List<RSInwardOutWardDetails> getFromSAInvoice(List<SAInvoiceDetails> detailsResult) {
        return detailsResult.stream().map(item -> {
            RSInwardOutWardDetails rsInwardOutWardDetails = new RSInwardOutWardDetails();
            if (item.getMaterialGoodsID() != null) {
                materialGoodsRepository.findById(item.getMaterialGoodsID()).ifPresent(rsInwardOutWardDetails::setMaterialGood);
            }
            if (item.getRepositoryID() != null) {
                repositoryRepository.findById(item.getRepositoryID()).ifPresent(rsInwardOutWardDetails::setRepository);
            }
            if (item.getUnitID() != null) {
                unitRepository.findById(item.getUnitID()).ifPresent(rsInwardOutWardDetails::setUnit);
            }
            if (item.getMainUnitID() != null) {
                unitRepository.findById(item.getMainUnitID()).ifPresent(rsInwardOutWardDetails::setMainUnit);
            }
            if (item.getCostSetID() != null) {
                costSetRepository.findById(item.getCostSetID()).ifPresent(rsInwardOutWardDetails::setCostSet);
            }
            if (item.getStatisticsCodeID() != null) {
                statisticsCodeRepository.findById(item.getStatisticsCodeID()).ifPresent(rsInwardOutWardDetails::setStatisticsCode);
            }
            if (item.getDepartmentID() != null) {
                organizationUnitRepository.findById(item.getDepartmentID()).ifPresent(rsInwardOutWardDetails::setDepartment);
            }
            if (item.getExpenseItemID() != null) {
                expenseItemRepository.findById(item.getExpenseItemID()).ifPresent(rsInwardOutWardDetails::setExpenseItem);
            }
            if (item.getBudgetItemID() != null) {
                budgetItemRepository.findById(item.getBudgetItemID()).ifPresent(rsInwardOutWardDetails::setBudgetItem);
            }
            if (item.getContractID() != null) {
                contractRepository.findById(item.getContractID()).ifPresent(rsInwardOutWardDetails::setContract);
            }
            if (item.getsABillID() != null) {
                contractRepository.findById(item.getsABillID()).ifPresent(rsInwardOutWardDetails::setContract);
            }
            if (item.getsABillDetailID() != null) {
                contractRepository.findById(item.getsABillDetailID()).ifPresent(rsInwardOutWardDetails::setContract);
            }
            rsInwardOutWardDetails.setDescription(item.getDescription());
            rsInwardOutWardDetails.setDebitAccount(item.getDebitAccount());
            rsInwardOutWardDetails.setCreditAccount(item.getCreditAccount());
            rsInwardOutWardDetails.setQuantity(item.getQuantity());
            rsInwardOutWardDetails.setUnitPrice(item.getUnitPrice());
            rsInwardOutWardDetails.setUnitPriceOriginal(item.getUnitPriceOriginal());
            rsInwardOutWardDetails.setMainQuantity(item.getMainQuantity());
            rsInwardOutWardDetails.setMainUnitPrice(item.getMainUnitPrice());
            rsInwardOutWardDetails.setMainConvertRate(item.getMainConvertRate());
            rsInwardOutWardDetails.setFormula(item.getFormula());
            rsInwardOutWardDetails.setAmount(item.getAmount());
            rsInwardOutWardDetails.setAmountOriginal(item.getAmountOriginal());
            rsInwardOutWardDetails.setExpiryDate(item.getExpiryDate());
            rsInwardOutWardDetails.setLotNo(item.getLotNo());
            rsInwardOutWardDetails.setOrderPriority(item.getOrderPriority());
            return rsInwardOutWardDetails;
        }).collect(Collectors.toList());
    }


    private List<RSInwardOutWardDetails> getFromPPDiscountReturn(List<PPDiscountReturnDetails> detailsResult) {
        return detailsResult.stream().map(item -> {
            RSInwardOutWardDetails rsInwardOutWardDetails = new RSInwardOutWardDetails();
            if (item.getMaterialGoodsID() != null) {
                materialGoodsRepository.findById(item.getMaterialGoodsID()).ifPresent(rsInwardOutWardDetails::setMaterialGood);
            }
            if (item.getRepositoryID() != null) {
                repositoryRepository.findById(item.getRepositoryID()).ifPresent(rsInwardOutWardDetails::setRepository);
            }
            if (item.getUnitID() != null) {
                unitRepository.findById(item.getUnitID()).ifPresent(rsInwardOutWardDetails::setUnit);
            }
            if (item.getMainUnitID() != null) {
                unitRepository.findById(item.getMainUnitID()).ifPresent(rsInwardOutWardDetails::setMainUnit);
            }
            if (item.getCostSetID() != null) {
                costSetRepository.findById(item.getCostSetID()).ifPresent(rsInwardOutWardDetails::setCostSet);
            }
            if (item.getStatisticsCodeID() != null) {
                statisticsCodeRepository.findById(item.getStatisticsCodeID()).ifPresent(rsInwardOutWardDetails::setStatisticsCode);
            }
            if (item.getDepartmentID() != null) {
                organizationUnitRepository.findById(item.getDepartmentID()).ifPresent(rsInwardOutWardDetails::setDepartment);
            }
            if (item.getExpenseItemID() != null) {
                expenseItemRepository.findById(item.getExpenseItemID()).ifPresent(rsInwardOutWardDetails::setExpenseItem);
            }
            if (item.getBudgetItemID() != null) {
                budgetItemRepository.findById(item.getBudgetItemID()).ifPresent(rsInwardOutWardDetails::setBudgetItem);
            }
            if (item.getContractID() != null) {
                contractRepository.findById(item.getContractID()).ifPresent(rsInwardOutWardDetails::setContract);
            }
            rsInwardOutWardDetails.setDescription(item.getDescription());
            rsInwardOutWardDetails.setDebitAccount(item.getDebitAccount());
            rsInwardOutWardDetails.setCreditAccount(item.getCreditAccount());
            rsInwardOutWardDetails.setQuantity(item.getQuantity());
            rsInwardOutWardDetails.setUnitPrice(item.getUnitPrice());
            rsInwardOutWardDetails.setUnitPriceOriginal(item.getUnitPriceOriginal());
            rsInwardOutWardDetails.setMainQuantity(item.getMainQuantity());
            rsInwardOutWardDetails.setMainUnitPrice(item.getMainUnitPrice());
            rsInwardOutWardDetails.setMainConvertRate(item.getMainConvertRate());
            rsInwardOutWardDetails.setFormula(item.getFormula());
            rsInwardOutWardDetails.setAmount(item.getAmount());
            rsInwardOutWardDetails.setAmountOriginal(item.getAmountOriginal());
            rsInwardOutWardDetails.setExpiryDate(item.getExpiryDate());
            rsInwardOutWardDetails.setLotNo(item.getLotNo());
            rsInwardOutWardDetails.setOrderPriority(item.getOrderPriority());
            return rsInwardOutWardDetails;
        }).collect(Collectors.toList());
    }

    private Class getReturnEntityFromTypeID(Integer typeID) {
        switch (typeID) {
            case TypeConstant.NHAP_KHO_TU_DIEU_CHINH:
                break;
            case TypeConstant.NHAP_KHO_TU_MUA_HANG:
                return PPInvoiceDetails.class;
            case TypeConstant.NHAP_KHO_TU_BAN_HANG_TRA_LAI:
                return SaReturnDetails.class;
            case TypeConstant.XUAT_KHO_TU_BAN_HANG:
                return SAInvoiceDetails.class;
            case TypeConstant.XUAT_KHO_TU_HANG_MUA_TRA_LAI:
                return PPDiscountReturnDetails.class;
            case TypeConstant.XUAT_KHO_TU_DIEU_CHINH:
                break;
        }
        return RSInwardOutWardDetails.class;
    }

    private Class getReferenceTableEntity(Integer typeID) {
        switch (typeID) {
            case TypeConstant.NHAP_KHO_TU_DIEU_CHINH:
                return null;
            case TypeConstant.NHAP_KHO_TU_MUA_HANG:
                return PPInvoice.class;
            case TypeConstant.NHAP_KHO_TU_BAN_HANG_TRA_LAI:
                return SaReturn.class;
        }
        return null;
    }

    public static void setParams(@NotNull Query query, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }
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
}
