package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.RSInwardOutwardDetailReportDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.TypeConstant;
import vn.softdreams.ebweb.web.rest.dto.RSTransferDetailsDTO;
import vn.softdreams.ebweb.web.rest.dto.RSTransferSearchDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;

public class RSTransferRepositoryImpl implements RSTransferRepositoryCustom {
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
    private JdbcTemplate jdbcTemplate;

    @Override
    public Page<RSTransferSearchDTO> searchAllTransfer(Pageable pageable, UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, String searchValue, Integer noType, Integer currentBook) {
        StringBuilder sql = new StringBuilder();
        List<RSTransferSearchDTO> rsTransfers = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        Number total = getQueryStringTransfer(accountingObject, accountingObjectEmployee, status, fromDate, toDate, searchValue, noType, currentBook, sql, params);
        if (total.longValue() > 0) {
            Query sumQuery = entityManager.createNativeQuery(new StringBuilder(" Select sum(a.totalAmount) from (select a.totalAmount ")
                .append(sql).append(" ) a ").toString());
            Common.setParams(sumQuery, params);
            BigDecimal totalAmountSum = (BigDecimal) sumQuery.getSingleResult();

            StringBuilder select = new StringBuilder("select ")
                .append(" a.typeID, a.id, a.date, a.postedDate, a.noFBook, a.noMBook, a.accountingObjectName, b.typeName, a.reason, a.totalAmount, a.recorded, a.invoiceNo, a.employeeName ")
                .append(sql)
                .append(" ORDER BY date DESC, NoFBook DESC, NoMBook DESC ");

            Query query = entityManager.createNativeQuery(select.toString(), "RSTransferSearchDTO");
            if (pageable != null) {
                Common.setParamsWithPageable(query, params, pageable, total);
            } else {
                Common.setParams(query, params);
            }
            rsTransfers = query.getResultList();
            rsTransfers.get(0).setSumTotalAmount(totalAmountSum);
        }
        if (pageable == null) {
            return new PageImpl<>(rsTransfers);
        }
        return new PageImpl<>(rsTransfers, pageable, total.longValue());
    }

    @Override
    public Page<RSTransferSearchDTO> searchAll(Pageable pageable, UUID accountingObject, UUID repository, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, String searchValue, Integer noType, Integer currentBook) {
        StringBuilder sql = new StringBuilder();
        List<RSTransferSearchDTO> rsTransfers = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        Number total = getQueryString(accountingObject, repository, accountingObjectEmployee, status, fromDate, toDate, searchValue, noType, currentBook, sql, params);
        if (total.longValue() > 0) {
            Query sumQuery = entityManager.createNativeQuery(new StringBuilder(" Select sum(a.totalAmount) from (select a.totalAmount ")
                .append(sql).append(" ) a ").toString());
            Common.setParams(sumQuery, params);
            BigDecimal totalAmountSum = (BigDecimal) sumQuery.getSingleResult();

            StringBuilder select = new StringBuilder("select ")
                .append(" a.typeID, a.id, a.date, a.postedDate, a.noFBook, a.noMBook, a.accountingObjectName, b.typeName, a.reason, ")
                .append(" a.totalAmount, ")
                .append(" a.recorded, a.invoiceNo, a.employeeName ")
                .append(sql)
                .append(" group by a.typeID, a.id, a.date, a.postedDate, a.noFBook, a.noMBook, a.accountingObjectName, b.typeName, a.reason, a.totalAmount, a.recorded, a.invoiceNo, a.employeeName ")
                .append(" ORDER BY date DESC, NoFBook DESC, NoMBook DESC ");

            Query query = entityManager.createNativeQuery(select.toString(), "RSTransferSearchDTO");
            if (pageable != null) {
                Common.setParamsWithPageable(query, params, pageable, total);
            } else {
                Common.setParams(query, params);
            }
            rsTransfers = query.getResultList();
            rsTransfers.get(0).setSumTotalAmount(totalAmountSum);
        }
        if (pageable == null) {
            return new PageImpl<>(rsTransfers);
        }
        return new PageImpl<>(rsTransfers, pageable, total.longValue());
    }

    // Click show details chuyen kho
    @Override
    public List<RSTransferDetailsDTO> getDetailsTransferById(UUID id, Integer typeID) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        StringBuilder sql = new StringBuilder();
        if (typeID == TypeConstant.CHUYEN_KHO ||
            typeID == TypeConstant.CHUYEN_KHO_GUI_DAI_LY ||
            typeID == TypeConstant.CHUYEN_KHO_CHUYEN_NOI_BO) {
            sql.append("select a.id, a.RSTransferID, a.materialGoodsID, e.MaterialGoodsCode, a.Description, c.RepositoryCode fromRepositoryCode, d.RepositoryCode toRepositoryCode, ")
                .append(" a.DebitAccount, a.CreditAccount, f.UnitName, a.Quantity, a.UnitPriceOriginal, a.Amount, a.OWPrice, a.OWAmount, a.LotNo, a.ExpiryDate, g.ExpenseItemCode, ")
                .append(" h.CostSetCode,i.BudgetItemCode, k.OrganizationUnitCode, n.StatisticsCode, a.OrderPriority, a.FromRepositoryID ")
                .append(" from RSTransferDetail a ")
                .append(" left join RSTransfer b on a.RSTransferID = b.ID ")
                .append(" left join Repository c on c.ID = a.FromRepositoryID ")
                .append(" left join Repository d on d.ID = a.ToRepositoryID ")
                .append(" left join MaterialGoods e on e.ID = a.MaterialGoodsID ")
                .append(" left join Unit f on f.ID = a.UnitID ")
                .append(" left join ExpenseItem g on g.ID = a.ExpenseItemID ")
                .append(" left join CostSet h on h.ID = a.CostSetID ")
                .append(" left join BudgetItem i on i.ID = a.BudgetItemID ")
                .append(" left join EbOrganizationUnit k on k.ID = a.DepartmentID ")
                .append(" left join StatisticsCode n on n.ID = a.StatisticsCodeID ")
                .append(" where a.RSTransferID = ?1 and b.companyID = ?2 ");
        }

        Query query = entityManager.createNativeQuery(sql.toString(), "RSTransferDetailsDTO");
        query.setParameter(1, id);
        currentUserLoginAndOrg.ifPresent(securityDTO -> query.setParameter(2, securityDTO.getOrg()));
//        if (typeID == TypeConstant.XUAT_KHO_TU_BAN_HANG) {
//            return getFromSAInvoice(query.getResultList());
//        } else if (typeID == TypeConstant.XUAT_KHO_TU_HANG_MUA_TRA_LAI) {
//            return getFromPPDiscountReturn(query.getResultList());
//        } else if (typeID == TypeConstant.XUAT_KHO_TU_DIEU_CHINH) {
//            // TODO: Xuất kho từ điều chỉnh
//            return null;
//        } else {
        return query.getResultList();
//        }
    }

    @Override
    public List<RSInwardOutwardDetailReportDTO> getRSTransferDetails(UUID id) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select a.MaterialGoodsCode, " +
            "       a.MaterialGoodsName, " +
            "       u.UnitName, " +
            "       r.Quantity, " +
            "       r.AmountOriginal, " +
            "       r.Amount, " +
            "       r.OWPrice, " +
            "       r.OWAmount, " +
            "       r.CreditAccount, " +
            "       r.DebitAccount, " +
            "       re.RepositoryCode, " +
            "       r.UnitPriceOriginal, " +
            "       re.RepositoryName, " +
            "       r.Description " +
            "from RSTransferDetail r " +
            "         left join MaterialGoods a on a.ID = r.MaterialGoodsID " +
            "         left join Unit u on u.ID = r.UnitID " +
            "         left join Repository re on re.ID = r.FromRepositoryID " +
            "left join RSTransfer rs on rs.ID = r.RSTransferID " +
            "where rs.id = :id order by OrderPriority ");
        params.put("id", id);
        Query query = entityManager.createNativeQuery(sql.toString(), "RSInwardOutwardDetailReportDTO1");
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<RSInwardOutwardDetailReportDTO> getRSTransferDetail(UUID id) {
        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select mg.MaterialGoodsCode, " +
            "       mg.MaterialGoodsName, " +
            "       ut.UnitName, " +
            "       rsdtl.Quantity, " +
            "       rsdtl.AmountOriginal, " +
            "       rsdtl.Amount, " +
            "       rsdtl.OWPrice, " +
            "       rsdtl.OWAmount, " +
            "       rsdtl.CreditAccount, " +
            "       rsdtl.DebitAccount, " +
            "       rsdtl.UnitPriceOriginal, " +
            "       rs.RepositoryCode, " +
            "       rs.RepositoryName, " +
            "       rsdtl.Description " +
            "from RSTransferDetail rsdtl " +
            "         left join Unit ut on ut.ID = rsdtl.UnitID " +
            "   left join Repository rs on rs.ID = rsdtl.ToRepositoryID " +
            "         left join MaterialGoods mg on mg.id = rsdtl.MaterialGoodsID " +
            "where rsdtl.RSTransferID = :id order by OrderPriority");
        params.put("id", id);
        Query query = entityManager.createNativeQuery(sql.toString(), "RSInwardOutwardDetailReportDTO1");
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public RSTransferSearchDTO findReferenceTablesID(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, Integer rowNum, int currentBook) {
        StringBuilder sql = new StringBuilder();
        RSTransferSearchDTO rsTransferSearchDTO = null;
        Map<String, Object> params = new HashMap<>();
        Number total = getQueryStringTransfer(accountingObject, accountingObjectEmployee, status, fromDate, toDate, searchValue, noType, currentBook, sql, params);
        if (total.longValue() > 0) {
            StringBuilder select = new StringBuilder("select ")
                .append(" a.typeid, a.id from ")
                .append(" ( SELECT id, typeid FROM (SELECT a.*, ROW_NUMBER() over (ORDER BY a.date DESC, iif(a.TypeLedger in (:book, 2), a.NoFBook, a.NoMBook) desc ) rownum ")
                .append(sql)
                .append(") a where a.rownum = :rowNum) a ");
            params.put("rowNum", rowNum);
            params.put("book", currentBook);
            Query query = entityManager.createNativeQuery(select.toString(), "findReferenceTableTransferID");
            Common.setParams(query, params);
            rsTransferSearchDTO = (RSTransferSearchDTO) query.getSingleResult();
        }
        return rsTransferSearchDTO;
    }

    @Override
    public Integer findRowNumByID(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, UUID id, int currentBook) {
        StringBuilder sql = new StringBuilder();
        Number rowNum = null;
        Map<String, Object> params = new HashMap<>();
        Number total = getQueryStringTransfer(accountingObject, accountingObjectEmployee, status, fromDate, toDate, searchValue, noType, currentBook, sql, params);
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
    public void multiDeleteRSTransfer(UUID orgID, List<UUID> uuidList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("DELETE FROM RSTransfer WHERE companyID = :orgID ");
        params.put("orgID", orgID);
        sql.append("AND ID IN :uuidList ");
        params.put("uuidList", uuidList);
        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, params);
        query.executeUpdate();
    }

    @Override
    public void multiDeleteChildRSTransfer(String tableName, List<UUID> uuidList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        String deleteSQL = "DELETE FROM " + tableName + " ";
        sql.append(" WHERE RSTransferID IN :uuidList ");
        params.put("uuidList", uuidList);
        Query query = entityManager.createNativeQuery(deleteSQL + sql.toString());
        setParams(query, params);
        query.executeUpdate();
    }

    @Override
    public void updateUnrecordRS(List<UUID> uuidList) {
        String sql = "Update RSTransfer SET Recorded = 0 WHERE id = ?;"+
            "Delete GeneralLedger WHERE referenceID = ?;";
        jdbcTemplate.batchUpdate(sql, uuidList, vn.softdreams.ebweb.config.Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail).toString());
        });
    }

//    @Override
//    public RSInwardOutward findByRowNumOutWard(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, Integer rowNum, int currentBook) {
//        StringBuilder sql = new StringBuilder();
//        RSInwardOutward rsInwardOutward = null;
//        Map<String, Object> params = new HashMap<>();
//        Number total = getQueryStringTransfer(accountingObject, accountingObjectEmployee, status, fromDate, toDate, searchValue, noType, currentBook, sql, params);
//        if (total.longValue() > 0) {
//            String newSql = "SELECT * FROM (SELECT a.*, ROW_NUMBER() over (ORDER BY date DESC, iif(TypeLedger in (0, 2), NoFBook, NoMBook) desc ) rownum " + sql.toString() + ") a where a.rownum = :rowNum";
//            params.put("rowNum", rowNum);
//            Query query = entityManager.createNativeQuery(newSql, RSInwardOutward.class);
//            Common.setParams(query, params);
//            rsInwardOutward = (RSInwardOutward) query.getSingleResult();
//        }
//        return rsInwardOutward;
//    }

//    @Override
//    public RSInwardOutwardSearchDTO findReferenceTableID(UUID accountingObject, Integer status, String fromDate, String toDate, Integer noType, String searchValue, Integer rowNum, int currentBook) {
//        StringBuilder sql = new StringBuilder();
//        RSInwardOutwardSearchDTO rsInwardOutward = null;
//        Map<String, Object> params = new HashMap<>();
//        Number total = getQueryString(accountingObject, status, fromDate, toDate, searchValue, noType, currentBook, sql, params);
//        if (total.longValue() > 0) {
//            StringBuilder select = new StringBuilder("select case a.TypeID when 400 then null when 402 then pp.ID ")
//                .append(" when 403 then sa.ID end as refID, a.id, a.typeid from ( ")
//                .append(" SELECT id, typeid FROM (SELECT a.*, ROW_NUMBER() over (ORDER BY a.date DESC, iif(a.TypeLedger in (:book, 2), a.NoFBook, a.NoMBook) desc ) rownum ")
//                .append(sql)
//                .append(") a where a.rownum = :rowNum) a ")
//                .append(" left join SAReturn sa on sa.RSInwardOutwardID = a.ID and a.TypeID = 403 ")
//                .append(" left join PPInvoice pp on pp.RSInwardOutwardID = a.ID and a.TypeID = 402 ");
//            params.put("rowNum", rowNum);
//            params.put("book", currentBook);
//            Query query = entityManager.createNativeQuery(select.toString(), "findReferenceTableID");
//            Common.setParams(query, params);
//            rsInwardOutward = (RSInwardOutwardSearchDTO) query.getSingleResult();
//        }
//        return rsInwardOutward;
//    }

//    @Override
//    public void unRecordDetails(List<UUID> ids) {
//        String sql = "UPDATE RSInwardOutwardDetail SET SaReturnID = null, SaReturnDetailsID = null, PPOrderID = null, PPOrderDetailsID = null WHERE ID = ?";
//        jdbcTemplate.batchUpdate(
//            sql,
//            ids,
//            Constants.BATCH_SIZE,
//            (ps, id) -> ps.setString(1, Utils.uuidConvertToGUID(id).toString())
//        );
//    }

//    @Override
//    public void deleteByListID(List<UUID> ids) {
//        StringBuilder sql = new StringBuilder();
//        Map<String, Object> params = new HashMap<>();
//        sql.append("DELETE FROM RSInwardOutward WHERE ID IN :ids ");
//        params.put("ids", ids);
//        Query query = entityManager.createNativeQuery(sql.toString());
//        setParams(query, params);
//        query.executeUpdate();
//    }

//    @Override
//    public List<RSInwardOutwardDetailReportDTO> getRSInwardOutWardDetails(UUID id) {
//        StringBuilder sql = new StringBuilder();
//        Map<String, Object> params = new HashMap<>();
//        sql.append("select mg.MaterialGoodsCode, " +
//            "       mg.MaterialGoodsName, " +
//            "       ut.UnitName, " +
//            "       rsdtl.Quantity, " +
//            "       rsdtl.AmountOriginal, " +
//            "       rsdtl.Amount, " +
//            "       rsdtl.CreditAccount, " +
//            "       rsdtl.DebitAccount, " +
//            "       rs.RepositoryCode, " +
//            "       rsdtl.UnitPriceOriginal, " +
//            "       rs.RepositoryName, " +
//            "       rsdtl.Description " +
//            "from RSInwardOutwardDetail rsdtl " +
//            "         left join Unit ut on ut.ID = rsdtl.UnitID " +
//            "         left join Repository rs on rs.ID = rsdtl.RepositoryID " +
//            "         left join MaterialGoods mg on mg.id = rsdtl.MaterialGoodsID " +
//            "where rsdtl.RSInwardOutwardID = :id order by OrderPriority");
//        params.put("id", id);
//        Query query = entityManager.createNativeQuery(sql.toString(), "RSInwardOutwardDetailReportDTO");
//        setParams(query, params);
//        return query.getResultList();
//    }

//    public Page<IWVoucherDTO> getIWVoucher(Pageable pageable, String fromDate, String toDate, UUID objectId) {
//        StringBuilder sql = new StringBuilder();
//        List<IWVoucherDTO> lst = new ArrayList<>();
//        Map<String, Object> params = new HashMap<>();
//        sql.append("from RepositoryLedger rl left join Repository r on r.ID = rl.RepositoryID " +
//            "left join Unit u on u.ID = rl.UnitID " +
//            "left join Unit mu on mu.ID = rl.MainUnitID where rl.typeID in (400, 401, 402, 403, 741) and rl.materialGoodsID = :objectID ");
//        params.put("objectID", objectId);
//        if (!StringUtils.isEmpty(fromDate)) {
//            sql.append("and rl.Date >= :fromDate ");
//            params.put("fromDate", fromDate);
//        }
//        if (!StringUtils.isEmpty(toDate)) {
//            sql.append("and rl.Date <= :toDate ");
//            params.put("toDate", toDate);
//        }
//        Query countQuerry = entityManager.createNativeQuery("SELECT Count(*) " + sql.toString());
//        setParams(countQuerry, params);
//        Number total = (Number) countQuerry.getSingleResult();
//        if (total.longValue() > 0) {
//            Query query = entityManager.createNativeQuery("select rl.referenceID as id," +
//                "      rl.detailID," +
//                "      rl.noMBook," +
//                "      rl.noFBook," +
//                "      rl.date," +
//                "      rl.repositoryID," +
//                "      r.repositoryCode," +
//                "      rl.unitID," +
//                "      u.unitName," +
//                "      rl.unitPrice," +
//                "      (rl.iwquantity - (select ISNULL(SUM(rl1.OWQuantity),0) from RepositoryLedger rl1 where rl1.ConfrontDetailID = rl.DetailID)) as quantity," +
//                "      (rl.iwamount - (select ISNULL(SUM(rl1.OWAmount),0) from RepositoryLedger rl1 where rl1.ConfrontDetailID = rl.DetailID)) as amount," +
//                "      rl.lotNo," +
//                "      rl.expiryDate," +
//                "      rl.mainUnitID," +
//                "      mu.unitName as mainUnitName," +
//                "      rl.mainIWQuantity as mainQuantity," +
//                "      rl.mainUnitPrice," +
//                "      rl.typeID " +
//                sql.toString() + " order by Date DESC ,noFBook DESC, noMBook DESC", "IWVoucherDTO");
//            setParamsWithPageable(query, params, pageable, total);
//            lst = query.getResultList();
//        }
//        return new PageImpl<>(lst, pageable, total.longValue());
//    }

//    @Override
//    public void updateAfterDeleteMaterialQuantum(UUID id, List<MaterialQuantumDetails> materialQuantumDetails, UUID rsInwardOutwardID) {
//        String sql = "UPDATE RSInwardOutwardDetail SET MaterialQuantumID = NULL, MaterialQuantumDetailID = NULL WHERE MaterialQuantumID = ? OR MaterialQuantumDetailID = ?";
//        jdbcTemplate.batchUpdate(sql, materialQuantumDetails, Constants.BATCH_SIZE, (ps, detail) -> {
//            ps.setString(1, Common.revertUUID(rsInwardOutwardID).toString());
//            ps.setString(2, Common.revertUUID(detail.getId()).toString());
//        });
//    }
//
//    @Override
//    public RSInwardOutwardSearchDTO findByID(UUID id) {
//        StringBuilder sql = new StringBuilder();
//        RSInwardOutwardSearchDTO rsInwardOutward = null;
//        if (id != null) {
//            Map<String, Object> params = new HashMap<>();
//            sql.append(" Select * from RSInwardOutward where id = :id");
//            params.put("id", id);
//            Query query = entityManager.createNativeQuery(sql.toString(), "RSOutwardSearchDTOFind");
//            Common.setParams(query, params);
//            List<RSInwardOutwardSearchDTO> rsInwardOutwards = query.getResultList();
//            if (rsInwardOutwards.size() > 0) {
//                rsInwardOutward = rsInwardOutwards.get(0);
//            }
//            rsInwardOutward = (RSInwardOutwardSearchDTO) query.getSingleResult();
//        }
//        return rsInwardOutward;
//    }
//
//    @Override
//    public void multiDeleteRSInwardOutward(UUID orgID, List<UUID> uuidList) {
//        StringBuilder sql = new StringBuilder();
//        Map<String, Object> params = new HashMap<>();
//        sql.append("DELETE FROM RSInwardOutward WHERE companyID = :orgID ");
//        params.put("orgID", orgID);
//        sql.append("AND ID IN :uuidList ");
//        params.put("uuidList", uuidList);
//        Query query = entityManager.createNativeQuery(sql.toString());
//        setParams(query, params);
//        query.executeUpdate();
//    }
//
//    @Override
//    public void multiDeleteChildRSInwardOutward(String tableName, List<UUID> uuidList) {
//        StringBuilder sql = new StringBuilder();
//        Map<String, Object> params = new HashMap<>();
//        String deleteSQL = "DELETE FROM " + tableName + " ";
//        sql.append(" WHERE RSInwardOutwardID IN :uuidList ");
//        params.put("uuidList", uuidList);
//        Query query = entityManager.createNativeQuery(deleteSQL + sql.toString());
//        setParams(query, params);
//        query.executeUpdate();
//    }
//
//    @Override
//    public void updateUnrecordRS(List<UUID> uuidList) {
//        String sql = "Update RSInwardOutward SET Recorded = 0 WHERE id = ?;"+
//            "Delete GeneralLedger WHERE referenceID = ?;";
//        jdbcTemplate.batchUpdate(sql, uuidList, Constants.BATCH_SIZE, (ps, detail) -> {
//            ps.setString(1, Utils.uuidConvertToGUID(detail).toString());
//            ps.setString(2, Utils.uuidConvertToGUID(detail).toString());
//        });
//    }


//    private Number getQueryString(UUID accountingObject, Integer status, String fromDate, String toDate, String searchValue, Integer noType, Integer currentBook, StringBuilder sql, Map<String, Object> params) {
//        sql.append("FROM RSInwardOutward a left join Type b on a.typeID = b.ID ")
//            .append(" left join SAReturn sa on sa.RSInwardOutwardID = a.ID and a.TypeID = 403 ")
//            .append(" left join PPInvoice pp on pp.RSInwardOutwardID = a.ID and a.TypeID = 402 ")
//            .append(" WHERE a.typeID / 10 = 40 ");
//        if (!Strings.isNullOrEmpty(fromDate)) {
//            sql.append("AND a.Date >= :fromDate ");
//            params.put("fromDate", fromDate);
//        }
//        if (!Strings.isNullOrEmpty(toDate)) {
//            sql.append("AND a.Date <= :toDate ");
//            params.put("toDate", toDate);
//        }
//        if (status != null && status != 0) {
//            sql.append("AND a.Recorded = :status ");
//            params.put("status", status == 1);
//        }
//        if (noType != null && noType != 0) {
//            sql.append("AND a.typeID = :typeID ");
//            params.put("typeID", noType);
//        }
//        if (accountingObject != null) {
//            sql.append("AND a.AccountingObjectID = :accountingObjectID ");
//            params.put("accountingObjectID", accountingObject);
//        }
//        Optional<SecurityDTO> currentUserLoginAndOrg = SecurityUtils.getCurrentUserLoginAndOrg();
//        if (currentUserLoginAndOrg.isPresent()) {
//            sql.append("AND a.CompanyID = :CompanyID ");
//            params.put("CompanyID", currentUserLoginAndOrg.get().getOrg());
//        }
//        if (currentBook != null) {
//            sql.append("AND a.typeLedger in (:book, 2) ");
//            params.put("book", currentBook);
//        }
//
//        if (!Strings.isNullOrEmpty(searchValue)) {
//            if (currentBook != null) {
//                if (currentBook == 0) {
//                    sql.append("AND (a.NoFBook LIKE :searchValue ");
//                } else {
//                    sql.append("AND (a.NoMBook LIKE :searchValue ");
//                }
//            }
//            sql.append("OR a.ContactName LIKE :searchValue ");
//            sql.append("OR a.accountingObjectAddress LIKE :searchValue ");
//            sql.append("OR a.Reason LIKE :searchValue ");
//            sql.append("OR a.NumberAttach LIKE :searchValue ) ");
//            params.put("searchValue", "%" + searchValue + "%");
//        }
//
//        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
//        Common.setParams(countQuerry, params);
//        return (Number) countQuerry.getSingleResult();
//    }

    private Number getQueryStringTransfer(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, String searchValue, Integer noType, Integer currentBook, StringBuilder sql, Map<String, Object> params) {
        sql.append(" FROM RSTransfer a inner join Type b on a.typeID = b.ID ")
            .append(" left join AccountingObject aobj on aobj.ID = a.EmployeeID ")
            .append(" WHERE a.typeID / 10 = 42 and a.id is not null ");
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
            sql.append("OR aobj.AccountingObjectCode LIKE :searchValue ");
            sql.append("OR aobj.AccountingObjectName LIKE :searchValue ");
            sql.append("OR a.Reason LIKE :searchValue ) ");
            params.put("searchValue", "%" + searchValue + "%");
        }

        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        return (Number) countQuerry.getSingleResult();
    }

    private Number getQueryString(UUID accountingObject, UUID repository, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, String searchValue, Integer noType, Integer currentBook, StringBuilder sql, Map<String, Object> params) {
        sql.append(" FROM RSTransfer a inner join Type b on a.typeID = b.ID ")
            .append(" left join AccountingObject aobj on aobj.ID = a.EmployeeID ")
            .append(" left join RSTransferDetail c on c.RSTransferID = a.ID ")
            .append(" WHERE a.typeID / 10 = 42 and a.id is not null ");
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
        if (repository != null) {
            sql.append("AND (c.FromRepositoryID = :repositoryID OR c.ToRepositoryID = :repositoryID) ");
            params.put("repositoryID", repository);
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
            sql.append("OR a.MobilizationOrderNo LIKE :searchValue ");
            sql.append("OR a.MobilizationOrderOf LIKE :searchValue ");
            sql.append("OR a.MobilizationOrderFor LIKE :searchValue ");
            sql.append("OR a.InvoiceNo LIKE :searchValue ");
            sql.append("OR a.InvoiceSeries LIKE :searchValue ");
            sql.append("OR a.InvoiceTemplate LIKE :searchValue ");
            sql.append("OR a.Reason LIKE :searchValue ) ");
            params.put("searchValue", "%" + searchValue + "%");
        }

        StringBuilder select = new StringBuilder("select ")
            .append(" a.typeID, a.id, a.date, a.postedDate, a.noFBook, a.noMBook, a.accountingObjectName, b.typeName, a.reason, a.totalAmount, a.recorded, a.invoiceNo, a.employeeName ")
            .append(sql)
            .append(" group by a.typeID, a.id, a.date, a.postedDate, a.noFBook, a.noMBook, a.accountingObjectName, b.typeName, a.reason, a.totalAmount, a.recorded, a.invoiceNo, a.employeeName ) a ");

        Query countQuerry = entityManager.createNativeQuery("SELECT Count(*) FROM ( " + select.toString());
        Common.setParams(countQuerry, params);
        return (Number) countQuerry.getSingleResult();
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
