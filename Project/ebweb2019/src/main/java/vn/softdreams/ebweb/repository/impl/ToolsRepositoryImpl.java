package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.domain.Tools;
import vn.softdreams.ebweb.repository.ToolsRepositoryCustom;
import vn.softdreams.ebweb.service.dto.ToolsConvertDTO;
import vn.softdreams.ebweb.service.dto.ToolsDetailsConvertDTO;
import vn.softdreams.ebweb.service.dto.ToolsViewDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.OrganizationUnitCustomDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class ToolsRepositoryImpl implements ToolsRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;


    @Override
    public List<ToolsConvertDTO> getAllToolsByActive(UUID org, boolean currentBook) {
        StringBuilder sql = new StringBuilder();
        sql.append("select id, declaretype, posteddate, toolcode toolsCode, toolname toolsName, unitid, quantity, " +
            " unitprice, allocationAwaitAccount from Tools where CompanyID = :companyID and TypeLedger in (:currentBook, 2) order by ToolCode");
        Map<String, Object> params = new HashMap<>();
        params.put("companyID", org);
        params.put("currentBook", currentBook);
        Query query = entityManager.createNativeQuery(sql.toString(), "ToolsConvertDTO");
        Common.setParams(query, params);
        List<ToolsConvertDTO> toolsConvertDTOS = query.getResultList();
        return toolsConvertDTOS;
    }

    @Override
    public List<ToolsConvertDTO> getToolsActiveByIncrements(UUID org, Integer currentBook, String date) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, " +
            "       t.declaretype, " +
            "       t.posteddate, " +
            "       t.toolcode                                                                              toolsCode, " +
            "       t.toolname                                                                              toolsName, " +
            "       t.unitid, " +
            "       t.quantity, " +
            "       (COALESCE(t.Quantity, 0) - COALESCE((select sum(COALESCE(tl2.IncrementQuantity, 0)) " +
            "                                            from ToolLedger tl2 " +
            "                                            where tl2.ToolsID = t.id and tl2.TypeID = 520" +
            " and convert(nvarchar, tl2.PostedDate, 112) <= convert(nvarchar, :postDate, 112)), 0)) as quantityRest, " +
            "       t.unitprice, " +
            "       t.allocationAwaitAccount " +
            "from Tools t " +
            "where t.CompanyID = :companyID " +
            "  and t.TypeLedger in (:currentBook, 2) " +
            "  and t.IsActive = 1 " +
            "  and t.DeclareType = 0 " +
            "  and ((select count(*) from ToolLedger tl where tl.ToolsID = t.id) <= 0 or " +
            "       ((select count(*) from ToolLedger tl where tl.ToolsID = t.id) > 0 and " +
            "        (COALESCE(t.Quantity, 0) > COALESCE((select sum(COALESCE(tl2.IncrementQuantity, 0)) " +
            "                                             from ToolLedger tl2 " +
            "                                             where tl2.ToolsID = t.id and convert(nvarchar, tl2.PostedDate, 112)" +
            " <= convert(nvarchar, :postDate, 112) and tl2.TypeID = 520), 0)))) " +
            "order by t.ToolCode");
        Map<String, Object> params = new HashMap<>();
        params.put("companyID", org);
        params.put("currentBook", currentBook);
        params.put("postDate", date);
        Query query = entityManager.createNativeQuery(sql.toString(), "ToolsConvertDTO2");
        Common.setParams(query, params);
        List<ToolsConvertDTO> toolsConvertDTOS = query.getResultList();
        return toolsConvertDTOS;
    }

    @Override
    public List<ToolsDetailsConvertDTO> getToolsDetailsByID(UUID toolsID) {
        List<ToolsDetailsConvertDTO> toolsDetailsConvertDTOS = new ArrayList<>();
        String sql = "select * from ToolsDetail where ToolsID = :id order by OrderPriority";
        Map<String, Object> params = new HashMap<>();
        params.put("id", toolsID);
        Query query = entityManager.createNativeQuery(sql, "ToolsDetailsConvertDTO");
        Common.setParams(query, params);
        toolsDetailsConvertDTOS = query.getResultList();
        return toolsDetailsConvertDTOS;
    }

    @Override
    public List<ToolsConvertDTO> getToolsActiveByTIDecrement(UUID org, Integer currentBook, String date) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ts.id, " +
            "       ts.declaretype, " +
            "       ts.posteddate, " +
            "       ts.toolcode        toolsCode, " +
            "       ts.toolname        toolsName, " +
            "       ts.unitid, " +
            "       ts.quantity, " +
            "       ts.unitprice, " +
            "       ts.allocationAwaitAccount, " +
            "       case ts.DeclareType " +
            "           when 0 then COALESCE( " +
            "                   (select sum(COALESCE(tl01.IncrementQuantity, 0)) " +
            "                    from ToolLedger tl01 " +
            "                    where convert(nvarchar, tl01.PostedDate, 112) <= " +
            "                          convert(nvarchar, :postDate, 112) " +
            "                      and tl01.ToolsID = ts.ID and tl01.TypeLedger in (:currentBook, 2)), 0) " +
            "           else COALESCE(ts.Quantity, 0) - COALESCE( " +
            "                   (select sum(COALESCE(tl01.IncrementQuantity, 0)) " +
            "                    from ToolLedger tl01 " +
            "                    where convert(nvarchar, tl01.PostedDate, 112) <= " +
            "                          convert(nvarchar, :postDate, 112) " +
            "                      and tl01.ToolsID = ts.ID and tl01.TypeLedger in (:currentBook, 2)), " +
            "                   0) end quantityRest " +
            "from Tools ts " +
            "where ts.IsActive = 1 " +
            "  and ts.CompanyID = :companyID " +
            "  and ts.TypeLedger in (:currentBook, 2) " +
            "  and (COALESCE((select count(*) from ToolLedger tl where tl.TypeID = 530 and tl.ToolsID = ts.ID), 0) <= 0 or " +
            "       (COALESCE((select count(*) from ToolLedger tl where tl.TypeID = 530 and tl.ToolsID = ts.ID), 0) > 0 and " +
            "        (ts.DeclareType = 1 and COALESCE(ts.Quantity, 0) - COALESCE( " +
            "                (select sum(COALESCE(tl01.IncrementQuantity, 0)) from ToolLedger tl01 where tl01.ToolsID = ts.ID), 0) > " +
            "                                0) or ((ts.DeclareType = 0 or ts.DeclareType is not null) and COALESCE( " +
            "                                                                                                      (select sum(COALESCE(tl01.IncrementQuantity, 0)) " +
            "                                                                                                       from ToolLedger tl01 " +
            "                                                                                                       where convert(nvarchar, tl01.PostedDate, 112) <= " +
            "                                                                                                             convert(nvarchar, :postDate, 112) " +
            "                                                                                                         and tl01.ToolsID = ts.ID), " +
            "                                                                                                      0) > 0))) " +
            "  AND (ts.DeclareType = 1 or ((ts.DeclareType = 0 or ts.DeclareType is not null) and (COALESCE((select count(*) " +
            "                                                                                                from ToolLedger tl1 " +
            "                                                                                                where tl1.TypeID = 520 " +
            "                                                                                                  and convert(nvarchar, tl1.PostedDate, 112) <= " +
            "                                                                                                      convert(nvarchar, :postDate, 112) " +
            "                                                                                                  and tl1.ToolsID = ts.ID), " +
            "                                                                                               0) > 0)))");
        Map<String, Object> params = new HashMap<>();
        params.put("companyID", org);
        params.put("currentBook", currentBook);
        params.put("postDate", date);
        Query query = entityManager.createNativeQuery(sql.toString(), "ToolsConvertDTO");
        Common.setParams(query, params);
        List<ToolsConvertDTO> toolsConvertDTOS = query.getResultList();
        return toolsConvertDTOS;
    }

    @Override
    public List<OrganizationUnitCustomDTO> getOrganizationUnitByToolsIDTIDecrement(UUID org, Integer currentBook, UUID id, String date) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from (select a.organizationunitcode, " +
            "       a.organizationunitname, " +
            "       a.id, " +
            "       a.unittype, " +
            "       a.acctype, " +
            "       a.iddetal, " +
            "       case a.DeclareType " +
            "           when 1 then " +
            "               (a.soluongdaghitang + a.Quantity - a.soluongdieuchuyentu) " +
            "           else (a.soluongdaghitang - " +
            "                 a.soluongdieuchuyentu + " +
            "                 a.soluongdieuchuyenden) end quantityRest " +
            "from (select eb.OrganizationUnitCode, " +
            "             eb.OrganizationUnitName, " +
            "             tl.DepartmentID                          id, " +
            "             eb.UnitType, " +
            "             eb.AccType, " +
            "             t.DeclareType, " +
            "             (select sum(tld.quantity) from ToolsDetail tld where tld.ToolsID = :id and tld.ObjectID = tl.DepartmentID)  Quantity, " +
            "        ts.id iddetal, " +
            "             COALESCE((select sum(COALESCE(tl2.IncrementQuantity, 0)) " +
            "                       from ToolLedger tl2 " +
            "                       where tl2.TypeLedger in (:currentBook, 2) " +
            "                         and tl.DepartmentID = tl2.DepartmentID " +
            "                         and tl.ToolsID = tl2.ToolsID " +
            "                         and convert(nvarchar, tl2.PostedDate, 112) <= convert(nvarchar, :postDate, 112) " +
            "                         and tl2.TypeID = 520 " +
            "                       group by tl2.DepartmentID), 0) soluongdaghitang, " +
            "             COALESCE((select sum(COALESCE(tl2.DecrementQuantity, 0)) " +
            "                       from ToolLedger tl2 " +
            "                       where tl2.TypeLedger in (:currentBook, 2) " +
            "                         and tl.DepartmentID = tl2.DepartmentID " +
            "                         and tl.ToolsID = tl2.ToolsID " +
            "                         and convert(nvarchar, tl2.PostedDate, 112) <= " +
            "                             convert(nvarchar, :postDate, 112) " +
            "                         and tl2.TypeID in (530, 540, 570) " +
            "                       group by tl2.DepartmentID), 0) soluongdieuchuyentu, " +
            "             COALESCE((select sum(COALESCE(tl2.IncrementQuantity, 0)) " +
            "                       from ToolLedger tl2 " +
            "                       where tl2.TypeLedger in (:currentBook, 2) " +
            "                         and tl.DepartmentID = tl2.DepartmentID " +
            "                         and tl.ToolsID = tl2.ToolsID " +
            "                         and convert(nvarchar, tl2.PostedDate, 112) <= convert(nvarchar, :postDate, 112) " +
            "                         and tl2.TypeID in (530, 540, 570) " +
            "                       group by tl2.DepartmentID), 0) soluongdieuchuyenden " +
            " " +
            "      from ToolLedger tl " +
            "               left join EbOrganizationUnit eb on eb.id = tl.DepartmentID " +
            "               left join ToolsDetail ts on ts.id = tl.ReferenceID " +
            "               left join Tools t on t.ID = tl.ToolsID " +
            "      where tl.ToolsID = :id " +
            "        and TypeID in (520, 530, 540, 570) " +
            "      group by eb.OrganizationUnitCode, eb.OrganizationUnitName, tl.DepartmentID, tl.ToolsID, eb.UnitType, eb.AccType, " +
            "               t.declaretype,ts.id, ts.Quantity) a) b where b.quantityRest > 0 ");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("currentBook", currentBook);
        params.put("postDate", date);
        Query query = entityManager.createNativeQuery(sql.toString(), "OrganizationUnitCustomTIDTO2");
        Common.setParams(query, params);
        List<OrganizationUnitCustomDTO> organizationUnitCustomDTOS = query.getResultList();
        return organizationUnitCustomDTOS;
    }
//
//    @Override
//    public List<OrganizationUnitCustomDTO> getOrganizationUnitByToolsIDTIDecrement(UUID org, Integer currentBook, UUID id, String date) {
//        StringBuilder sql = new StringBuilder();
//        sql.append("select *  " +
//            "from (select eb.OrganizationUnitCode,  " +
//            "             eb.OrganizationUnitName,  " +
//            "             tl.DepartmentID id,  " +
//            "             eb.UnitType,  " +
//            "             eb.AccType,  " +
//            "             COALESCE((select sum(COALESCE(tl2.IncrementQuantity, 0))  " +
//            "                       from ToolLedger tl2  " +
//            "                       where tl2.TypeLedger in (:currentBook, 2)  " +
//            "                         and tl.DepartmentID = tl2.DepartmentID  " +
//            "                         and tl.ToolsID = tl2.ToolsID  " +
//            "                         and convert(nvarchar, tl2.PostedDate, 112) <=  " +
//            "                             convert(nvarchar, :postDate, 112)  " +
//            "  " +
//            "                         and tl2.TypeID = 520  " +
//            "                       group by tl2.DepartmentID), 0) - COALESCE((select sum(COALESCE(tl2.DecrementQuantity, 0))  " +
//            "                                                                  from ToolLedger tl2  " +
//            "                                                                  where tl2.TypeLedger in (:currentBook, 2)  " +
//            "                                                                    and tl.DepartmentID = tl2.DepartmentID  " +
//            "                                                                    and tl.ToolsID = tl2.ToolsID  " +
//            "                                                                    and convert(nvarchar, tl2.PostedDate, 112) <=  " +
//            "                                                                        convert(nvarchar, :postDate, 112)  " +
//            "                                                                    and tl2.TypeID in (530, 540, 570)  " +
//            "                                                                  group by tl2.DepartmentID), 0) +  " +
//            "             COALESCE((select sum(COALESCE(tl2.IncrementQuantity, 0))  " +
//            "                       from ToolLedger tl2  " +
//            "                       where tl2.TypeLedger in (:currentBook, 2)  " +
//            "                         and tl.DepartmentID = tl2.DepartmentID  " +
//            "                         and tl.ToolsID = tl2.ToolsID  " +
//            "                         and convert(nvarchar, tl2.PostedDate, 112) <=  " +
//            "                             convert(nvarchar, :postDate, 112)  " +
//            "                         and tl2.TypeID in (530, 540, 570)  " +
//            "                       group by tl2.DepartmentID), 0) quantityRest  " +
//            "      from ToolLedger tl  " +
//            "               left join EbOrganizationUnit eb on eb.id = tl.DepartmentID  " +
//            "               left join ToolsDetail ts on ts.id = tl.ReferenceID  " +
//            "      where tl.ToolsID = :id  " +
//            "        and TypeID in (520, 530, 540, 570)  " +
//            "      group by eb.OrganizationUnitCode, eb.OrganizationUnitName, tl.DepartmentID, tl.ToolsID, eb.UnitType, eb.AccType) a  " +
//            "where a.quantityRest > 0");
//        Map<String, Object> params = new HashMap<>();
//        params.put("id", id);
//        params.put("currentBook", currentBook);
//        params.put("postDate", date);
//        Query query = entityManager.createNativeQuery(sql.toString(), "OrganizationUnitCustomTIDTO2");
//        Common.setParams(query, params);
//        List<OrganizationUnitCustomDTO> organizationUnitCustomDTOS = query.getResultList();
//        return organizationUnitCustomDTOS;
//    }

    @Override
    public List<OrganizationUnitCustomDTO> getOrganizationUnitByToolsIDTIIncrement(UUID org, Integer currentBook, UUID id, String date) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * " +
            "from (select ts.ObjectID                                                           id, " +
            "       eb.OrganizationUnitCode, " +
            "       eb.OrganizationUnitName, " +
            "       eb.UnitType, " +
            "       eb.AccType, " +
            "       sum(COALESCE(ts.Quantity, 0))                                         quantity, " +
            "       sum(COALESCE(ts.Quantity, 0)) - COALESCE((select sum(COALESCE(tl2.IncrementQuantity, 0)) " +
            "                                                 from ToolLedger tl2 " +
            "                                                 where tl2.ToolsID = ts.ToolsID " +
            "                                                   and tl2.DepartmentID = ts.ObjectID " +
            "                                                   and convert(nvarchar, tl2.PostedDate, 112) <= " +
            "                                                       convert(nvarchar, :postDate, 112) " +
            "                                                   and tl2.TypeLedger in (:currentBook, 2) " +
            "                                                   and tl2.TypeID = 520), 0) quantityRest, " +
            "       t.ToolCode                                                            toolsCode, " +
            "       t.ToolName                                                            toolsName, " +
            "       t.id toolsID " +
            "from ToolsDetail ts " +
            "         left join EbOrganizationUnit eb on ts.ObjectID = eb.id " +
            "         left join Tools t on t.ID = ts.ToolsID " +
            "where eb.OrganizationUnitCode is not null " +
            "  and ts.ToolsID = :id " +
            "  and ((COALESCE(ts.Quantity, 0)) - COALESCE((select sum(COALESCE(tl2.IncrementQuantity, 0)) " +
            "                                              from ToolLedger tl2 " +
            "                                              where tl2.ToolsID = ts.ToolsID " +
            "                                                and tl2.DepartmentID = ts.ObjectID " +
            "                                                and convert(nvarchar, tl2.PostedDate, 112) <= " +
            "                                                    convert(nvarchar, :postDate, 112) " +
            "                                                and tl2.TypeLedger in (:currentBook, 2) " +
            "                                                and tl2.TypeID = 520), 0)) > 0 " +
            "group by ts.ToolsID, ts.ObjectID, eb.OrganizationUnitCode, eb.OrganizationUnitName, eb.UnitType, eb.AccType, t.ToolCode, " +
            "         t.ToolName, t.id " +
            ") q where q.quantityRest > 0 order by q.OrganizationUnitCode");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("currentBook", currentBook);
        params.put("postDate", date);
        Query query = entityManager.createNativeQuery(sql.toString(), "OrganizationUnitCustomTIDTO");
        Common.setParams(query, params);
        List<OrganizationUnitCustomDTO> organizationUnitCustomDTOS = query.getResultList();
        return organizationUnitCustomDTOS;
    }

    @Override
    public List<Tools> findAllToolsByCompanyID(UUID orgID, boolean isDependent, int phienSoLamViec) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select distinct a.ToolsID as ID, a.ToolCode, a.ToolName from ToolLedger a left join Tools b on a.ToolsID = b.ID " +
            "where a.TypeID in (500,520) and a.TypeLedger = :typeLedger and a.CompanyID = :CompanyID ");
        if (isDependent) {
            sql.append(" OR a.CompanyID in (select ID from EbOrganizationUnit where (ParentID = :CompanyID and UnitType = 1 and IsActive = 1 and AccType = 0))");
        }
        params.put("CompanyID", orgID);
        params.put("typeLedger", phienSoLamViec);
        sql.append(" order by a.toolCode ");
        Query query = entityManager.createNativeQuery(sql.toString(), "ToolsViewDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }
}
