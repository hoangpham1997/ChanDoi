package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.AccountList;
import vn.softdreams.ebweb.domain.GOtherVoucher;
import vn.softdreams.ebweb.domain.PrepaidExpenseVoucher;
import vn.softdreams.ebweb.repository.PrepaidExpenseRepositoryCustom;
import vn.softdreams.ebweb.service.dto.PrepaidExpenseAllDTO;
import vn.softdreams.ebweb.service.dto.PrepaidExpenseCodeDTO;
import vn.softdreams.ebweb.service.dto.PrepaidExpenseConvertDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherSecondDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class PrepaidExpenseRepositoryImpl implements PrepaidExpenseRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<PrepaidExpenseCodeDTO> getPrepaidExpenseCode(List<UUID> listCompanyID, UUID companyID, String currentBook) {
        StringBuilder sqlBuilder = new StringBuilder();
        List<PrepaidExpenseCodeDTO> prepaidExpenseCodeDTOS = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append(" select * " +
            " from (select id, N'Phòng ban' species, OrganizationUnitCode code, OrganizationUnitName name, 0 as type " +
            "      from EbOrganizationUnit " +
            "      where ParentID = :companyID and UnitType = 4 " +
            "        and IsActive = 1 " +
            "      union " +
            "      select id, N'Đối tượng THCP' species, CostSetCode code, CostSetName name, 1 as type " +
            "      from CostSet " +
            "      where CompanyID in :listCompanyID " +
            "        and IsActive = 1 " +
            "      union " +
            "       select id, N'Hợp đồng' species, case :typeLedger when 0 then NoFBook " +
            "          when 1 then NoMBook end code, Name name, 2 as type " +
            "      from EMContract " +
            "      where CompanyID = :companyID and TypeLedger in (:typeLedger, 2) " +
            "        and IsActive = 1) as lcnlcn " +
            " order by lcnlcn.species asc, lcnlcn.code asc, lcnlcn.name asc; ");
        params.put("companyID", companyID);
        params.put("listCompanyID", listCompanyID);
        params.put("typeLedger", currentBook);
        Query query = entityManager.createNativeQuery(sqlBuilder.toString() , "PrepaidExpenseCodeDTO");
        Common.setParams(query, params);
        prepaidExpenseCodeDTOS = query.getResultList();
        return prepaidExpenseCodeDTOS;
    }

    @Override
    public List<AccountList> getCostAccounts(UUID org) {
        StringBuilder sqlBuilder = new StringBuilder();
        List<AccountList> accountLists = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append("select * from AccountList where AccountingType = 0 and IsActive = 1 and CompanyID = :companyID and IsParentNode = 0 order by AccountNumber ASC");
        params.put("companyID", org);
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), AccountList.class);
        Common.setParams(query, params);
        accountLists = query.getResultList();
        return accountLists;
    }

    @Override
    public Page<PrepaidExpenseAllDTO> getAll(Pageable pageable, Integer typeExpense, String fromDate, String toDate, String textSearch, UUID org, String currentBook) {
        StringBuilder sqlBuilder = new StringBuilder();
        List<PrepaidExpenseAllDTO> prepaidExpenses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append(" from PrepaidExpense pr where CompanyID = :companyID  and TypeLedger in (:typeLedger, 2) ");
        params.put("companyID", org);
        params.put("typeLedger", currentBook);
        if (typeExpense != null) {
            sqlBuilder.append(" and TypeExpense = :typeExpense ");
            params.put("typeExpense", typeExpense);
        }
        if (fromDate != null || toDate != null){
            Common.addDateSearchCustom(fromDate, toDate, params, sqlBuilder, "Date", "Date");
        }
        if(!Strings.isNullOrEmpty(textSearch)) {
            sqlBuilder.append(" and (PrepaidExpenseCode like :keySearch or PrepaidExpenseName like :keySearch ) ");
            params.put("keySearch", "%" + textSearch + "%");
        }
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) " + sqlBuilder.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            sqlBuilder.append(" order by pr.PrepaidExpenseCode, pr.date DESC ");
            Query query = entityManager.createNativeQuery("select pr.id ID, " +
                "       pr.companyid, " +
                "       pr.branchid, " +
                "       pr.typeledger, " +
                "       pr.typeexpense, " +
                "       pr.prepaidexpensecode, " +
                "       pr.prepaidexpensename, " +
                "       pr.date, " +
                "       pr.amount, " +
                "       COALESCE((select sum(AllocationAmount) " +
                "        from GotherVoucherDetailExpense gov " +
                "                 left join GOtherVoucher gv on gv.id = gov.GOtherVoucherID and TypeID = 709 " +
                "        where TypeID = 709 " +
                "          and CompanyID = :companyID " +
                "          and TypeLedger in (:typeLedger, 2) " +
                "          and pr.id = gov.PrepaidExpenseID), 0) + COALESCE(pr.AllocationAmount, 0)                           allocationAmount, " +
                "       pr.allocationtime, " +
                "       pr.allocatedperiod, " +
                "       pr.allocatedamount, " +
                "       pr.allocationaccount, " +
                "       pr.isactive, " +
                "       case when (COALESCE((select count(*) from GotherVoucherDetailExpense where PrepaidExpenseID = pr.id), 0)) <> 0 then CAST(1 AS BIT) else CAST(0 AS BIT) end isAllocation " + sqlBuilder.toString(), "PrepaidExpenseAllDTO");
            Common.setParamsWithPageable(query, params, pageable, total);
            prepaidExpenses = query.getResultList();
        }
         return new PageImpl<>(prepaidExpenses, pageable, total.longValue());
    }

    @Override
    public List<RefVoucherSecondDTO> findPrepaidExpenseByID(UUID id, UUID org, String currentBook) {
        StringBuilder sqlBuilder = new StringBuilder();
        List<PrepaidExpenseVoucher> prepaidExpenses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append(" SELECT vv.id                                                        id, " +
            "       vv.RefID                                                     refID1, " +
            "       vv.RefID                                                        refID2, " +
            "       vv.companyID, " +
            "       case :currentBook when 0 then vv.NoFBook else vv.NoMBook end no, " +
            "       convert(varchar, date, 103)                                  date, " +
            "       convert(varchar, PostedDate, 103)                            PostedDate, " +
            "       vv.Reason, " +
            "       vv.recorded, " +
            "       vv.TypeID                                                    TypeID, " +
            "       vv.typegroupid, " +
            "       vv.TotalAmount, " +
            "       vv.TotalAmountOriginal " +
            "from PrepaidExpenseVoucher prv " +
            "         left join ViewVoucherNo vv on prv.RefID = vv.RefID  and vv.TypeID = prv.typeID " +
            "where vv.companyID = :companyID " +
            "  and prv.PrepaidExpenseID = :id " +
            "  and vv.TypeLedger in (:currentBook, 2) " +
            "order by ISNULL(date, '1900-06-05T23:59:00') desc, ISNULL(posteddate, '1900-06-05T23:59:00') desc, noFBook desc ");
        params.put("companyID", org);
        params.put("id", id);
        params.put("currentBook", currentBook);
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "RefVoucherSecondDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }


    @Override
    public List<PrepaidExpenseConvertDTO> getPrepaidExpenseAllocation(Integer month, Integer year, UUID org, String currentBook) {
        StringBuilder sqlBuilder = new StringBuilder();
        List<PrepaidExpenseConvertDTO> prepaidExpensesDTOs = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append(" select pr.ID PrepaidExpenseID, " +
            "       pr.CompanyID, " +
            "       pr.BranchID, " +
            "       pr.TypeLedger, " +
            "       pr.TypeExpense, " +
            "       pr.PrepaidExpenseCode, " +
            "       pr.PrepaidExpenseName, " +
            "       pr.Date, " +
            "       pr.Amount, " +
            "       pr.AllocationTime, " +
            "       pr.AllocatedPeriod, " +
            "       pr.AllocatedAmount, " +
            "       pr.AllocationAccount, " +
            "       pr.IsActive, " +
            "       pr.Amount - (COALESCE((select sum(gov.AllocationAmount) " +
            "                     from GotherVoucherDetailExpense gov " +
            "                              left join GOtherVoucher gv on gv.id = gov.GOtherVoucherID and TypeID = 709 " +
            "                     where TypeID = 709 " +
            "                       and CompanyID = :companyID " +
            "                       and TypeLedger in (:currentBook, 2) " +
            "                       and pr.id = gov.PrepaidExpenseID), 0) + COALESCE(pr.AllocationAmount, 0)) " +
            "                                                                                                 RemainingAmount," +
            "   case (select Data from SystemOption where Code = 'TCKHAC_PBChiPhi' and CompanyID = :companyID) " +
            "           when 0 then pr.AllocatedAmount " +
            "           when 1 then (pr.AllocatedAmount / :numberDate) * (:numberDate - DAY(pr.Date) + 1) end AllocationAmount " +
            "  from PrepaidExpense pr " +
            "  where COALESCE(pr.allocationTime, 0) - COALESCE(pr.AllocatedPeriod, 0) - (select  count(*) " +
            "                    from GotherVoucherDetailExpense godtl " +
            "                             left join PrepaidExpense pr1 on pr1.ID = godtl.PrepaidExpenseID " +
            "                             left join GotherVoucher gov on gov.id = godtl.GOtherVoucherID " +
            "                    where gov.TypeLedger = :currentBook " +
            "                          and pr1.ID = pr.ID) > 0 " +
            "  and  pr.TypeLedger in (:currentBook, 2) " +
            "  and  pr.IsActive = 1 and  pr.CompanyID = :companyID " +
//            "  and  COALESCE(pr.AllocatedPeriod, 0) <= COALESCE(pr.allocationTime, 0) " +
            "  and (convert(nvarchar, pr.Date, 112) <= convert(nvarchar, :currentDate, 112)) order by pr.PrepaidExpenseCode, pr.date DESC ");
        params.put("companyID", org);
        params.put("currentBook", currentBook);
        int numberDate = Common.getNumberOfDayInMonth(month, year);
        params.put("numberDate", numberDate);
        String monthConvert = month < 10 ? "0" + month : month.toString();
        params.put("currentDate", year + monthConvert + numberDate);
        Query query = entityManager.createNativeQuery( sqlBuilder.toString(), "PrepaidExpenseConvertDTO");
        Common.setParams(query, params);
        prepaidExpensesDTOs = query.getResultList();
            if (prepaidExpensesDTOs != null && prepaidExpensesDTOs.size() > 0) {
                return prepaidExpensesDTOs;
            } else {
                return null;
        }
    }

    @Override
    public Long countAllByPrepaidExpenseCode(String prepaidExpenseCode, UUID org, UUID id, String currentBook) {
        StringBuilder sqlBuilder = new StringBuilder();
        List<PrepaidExpenseConvertDTO> prepaidExpensesDTOs = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append(" from PrepaidExpense where PrepaidExpenseCode = :prepaidExpenseCode and CompanyID = :companyID and TypeLedger in (:currentBook, 2)");
        params.put("prepaidExpenseCode", prepaidExpenseCode);
        params.put("companyID", org);
        if (id != null) {
            sqlBuilder.append(" and ID <> :id ");
            params.put("id", id);
        }
        params.put("currentBook", currentBook != null ? currentBook : 0);
        Query countQuery = entityManager.createNativeQuery("SELECT  count(*) " + sqlBuilder.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        return total.longValue();
    }

    @Override
    public Long countAllByMonthAndYear(Integer month, Integer year, UUID org, String currentBook) {
        StringBuilder sqlBuilder = new StringBuilder();
        List<PrepaidExpenseConvertDTO> prepaidExpensesDTOs = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append(" select  count(*) " +
            " from PrepaidExpense pr " +
            " where (pr.TypeExpense = 1 and pr.Amount <> 0 or (pr.TypeExpense = 0 and (pr.Amount - pr.allocationAmount) <> 0)) " +
            "  and pr.TypeLedger in (:currentBook, 2) " +
            "  and pr.IsActive = 1 " +
            "  and pr.CompanyID = :companyID " +
            "  and (convert(nvarchar, pr.Date, 112) <= convert(nvarchar, :currentDate, 112)) ");
        params.put("companyID", org);
        params.put("currentBook", currentBook);
        int numberDate = Common.getNumberOfDayInMonth(month, year);
        String monthConvert = month < 10 ? "0" + month : month.toString();
        params.put("currentDate", year + monthConvert + numberDate);
        Query countQuery = entityManager.createNativeQuery(sqlBuilder.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        return total.longValue();
    }

    @Override
    public GOtherVoucher getPrepaidExpenseAllocationDuplicate(Integer month, Integer year, UUID org, String currentBook) {
        StringBuilder sqlBuilder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append(" select top 1 * from GOtherVoucher where CompanyID = :companyID  and " +
            "   TypeLedger in (:currentBook, 2) and  MONTH(Date) = :month and YEAR(Date) = :year and TypeID = 709 ");
        params.put("companyID", org);
        params.put("currentBook", currentBook);
        params.put("month", month);
        params.put("year", year);
        Query countQuery = entityManager.createNativeQuery(sqlBuilder.toString(), GOtherVoucher.class);
        Common.setParams(countQuery, params);
        GOtherVoucher gOtherVoucher = (GOtherVoucher) countQuery.getSingleResult();
        return gOtherVoucher;
    }

    @Override
    public void updateAllocatedPeriod(boolean check, List<UUID> pre) {
//        check = true + số kì phân bổ và ngược lại
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("update PrepaidExpense set ");
        if (check) {
            sql.append(" AllocatedPeriod =  COALESCE(AllocatedPeriod, 0) + 1, allocationAmount = COALESCE(allocationAmount, 0) + COALESCE(allocatedAmount, 0) ");
        } else {
            sql.append(" AllocatedPeriod =  COALESCE(AllocatedPeriod, 0) - 1, allocationAmount = COALESCE(allocationAmount, 0) - COALESCE(allocatedAmount, 0)  ");
        }
        sql.append(" where id in :ids");
        params.put("ids", pre);
        Query countQuery = entityManager.createNativeQuery(sql.toString());
        Common.setParams(countQuery, params);
        countQuery.executeUpdate();
    }

    @Override
    public List<PrepaidExpenseCodeDTO> getPrepaidExpenseCodeCanActive(List<UUID> allCompanyByCompanyIdAndCode, UUID org, String currentBook) {
        StringBuilder sqlBuilder = new StringBuilder();
        List<PrepaidExpenseCodeDTO> prepaidExpenseCodeDTOS = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append(" select * " +
            " from (select id, N'Phòng ban' species, OrganizationUnitCode code, OrganizationUnitName name, 0 as type " +
            "      from EbOrganizationUnit " +
            "      where ParentID = :companyID and UnitType = 4 " +
            "      union " +
            "      select id, N'Đối tượng THCP' species, CostSetCode code, CostSetName name, 1 as type " +
            "      from CostSet " +
            "      where CompanyID in :listCompanyID " +
            "      union " +
            "       select id, N'Hợp đồng' species, case :typeLedger when 0 then NoFBook " +
            "          when 1 then NoMBook end code, Name name, 2 as type " +
            "      from EMContract " +
            "      where CompanyID = :companyID and TypeLedger in (:typeLedger, 2)) as lcnlcn " +
            " order by lcnlcn.species asc, lcnlcn.code asc, lcnlcn.name asc; ");
        params.put("companyID", org);
        params.put("listCompanyID", allCompanyByCompanyIdAndCode);
        params.put("typeLedger", currentBook);
        Query query = entityManager.createNativeQuery(sqlBuilder.toString() , "PrepaidExpenseCodeDTO");
        Common.setParams(query, params);
        prepaidExpenseCodeDTOS = query.getResultList();
        return prepaidExpenseCodeDTOS;
    }

}
