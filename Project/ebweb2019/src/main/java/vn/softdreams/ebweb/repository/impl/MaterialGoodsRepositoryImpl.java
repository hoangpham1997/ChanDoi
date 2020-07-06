package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.AccountingObject;
import vn.softdreams.ebweb.domain.MaterialGoods;
import vn.softdreams.ebweb.domain.SearchVoucherMaterialGoods;
import vn.softdreams.ebweb.repository.MaterialGoodsRepositoryCustom;
import vn.softdreams.ebweb.service.dto.MGForPPOrderConvertDTO;
import vn.softdreams.ebweb.service.dto.MGForPPOrderConvertQuantityDTO;
import vn.softdreams.ebweb.service.dto.MaterialGoodsDTO;
import vn.softdreams.ebweb.service.dto.ObjectDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

import static vn.softdreams.ebweb.service.Utils.Utils.setParams;
import static vn.softdreams.ebweb.service.Utils.Utils.setParamsWithPageable;

public class MaterialGoodsRepositoryImpl implements MaterialGoodsRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public Page<MaterialGoods> findAll1(Pageable pageable, SearchVoucherMaterialGoods searchVoucherMaterialGoods, UUID companyID) {
        StringBuilder sql = new StringBuilder();
        List<MaterialGoods> lstMG = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM MaterialGoods mg " +
            "         left join Unit U on mg.UnitID = U.id " +
            "WHERE mg.companyID = :companyID ");
        params.put("companyID", companyID);
        if (searchVoucherMaterialGoods.getMaterialGoodsType() != null) {
            sql.append("AND mg.materialGoodsType = :materialGoodsType ");
            params.put("materialGoodsType", searchVoucherMaterialGoods.getMaterialGoodsType());
        }
        if (searchVoucherMaterialGoods.getUnitID() != null) {
            sql.append("AND u.id = :unitID ");
            params.put("unitID", searchVoucherMaterialGoods.getUnitID());
        }
        if (searchVoucherMaterialGoods.getMaterialGoodsCategoryID() != null) {
            sql.append("AND mg.materialGoodsCategoryID = :materialGoodsCategoryID ");
            params.put("materialGoodsCategoryID", searchVoucherMaterialGoods.getMaterialGoodsCategoryID());
        }
        if (!Strings.isNullOrEmpty(searchVoucherMaterialGoods.getKeySearch())) {
            sql.append("AND (mg.MaterialGoodsName LIKE :searchValue ");
            sql.append("or mg.MaterialGoodsCode LIKE :searchValue ");
            sql.append("OR u.unitName LIKE :searchValue ");
            sql.append("OR mg.MinimumStock LIKE :searchValue )");
            params.put("searchValue", "%" + searchVoucherMaterialGoods.getKeySearch() + "%");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(*) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT  mg.*, u.ID as Unit1 ,u.UnitName " + sql.toString(), MaterialGoods.class);
            setParamsWithPageable(query, params, pageable, total);
            lstMG = query.getResultList();
        }
        return new PageImpl<>(lstMG, pageable, total.longValue());
    }

    @Override
    public List<MaterialGoodsDTO> findAllMaterialGoodsCustom(List<UUID> companyId) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        sql.append("from materialgoods mg ");
        sql.append("left join Unit u on u.ID = mg.UnitID ");
//        sql.append("left join MaterialGoodsPurchasePrice mgp on mg.ID = mgp.MaterialGoodsID ");

        sql.append("where mg.CompanyID in :companyId and mg.isActive = 1 ");
        StringBuilder selectSql = new StringBuilder();

        selectSql.append("mg.id as id, ");
        selectSql.append("mg.materialGoodsCode as materialGoodsCode, ");
        selectSql.append("mg.materialGoodsName as materialGoodsName, ");
        selectSql.append("u.id as unitID, ");
        selectSql.append("u.UnitName as unitName, ");
        selectSql.append("1 as UnitPrice, ");
        selectSql.append("mg.materialGoodsType, ");
        selectSql.append("mg.isFollow ");
        Query query = entityManager.createNativeQuery("SELECT " + selectSql + sql.toString(), "MaterialGoodsDTO");
        params.put("companyId", companyId);
        setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<MaterialGoodsDTO> findAllForDTO(List<UUID> companyID) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("a.id as id, a.materialGoodsCode as materialGoodsCode, a.materialGoodsName as materialGoodsName, ");
        sql.append("a.unitID as  unitID, a.repositoryID as  repositoryID, ");
        sql.append(" a.materialGoodsType as  materialGoodsType, a.materialGoodsCategoryID as  materialGoodsCategoryID ");
        sql.append("from materialgoods a where a.companyID in :companyId and isActive = 1 order by materialGoodsCode, materialGoodsName");
        params.put("companyId", companyID);
        Query query = entityManager.createNativeQuery("SELECT " + sql.toString(), "MaterialGoodsDTOReport");
        setParams(query, params);
        return query.getResultList();
    }

    @Override
    public void rollBack() {
        entityManager.clear();
    }

    @Override
    public List<MGForPPOrderConvertQuantityDTO> getQuantityExistsTest(List<UUID> materialGoodsIDs, List<UUID> repositoryIDs, String postedDate) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select aa.materialGoodsCode, " +
            "       aa.MaterialGoodsID           id, " +
            "       aa.RepositoryID, " +
            "       aa.RepositoryCode, " +
            "       COALESCE(aa.MinimumStock, 0) MinimumStock, " +
            "       aa.MaterialGoodsType, " +
            "       sum(aa.materialGoodsInStock) materialGoodsInStock " +
            "from ( " +
            "         select rp.materialGoodsCode, " +
            "                rp.MaterialGoodsID, " +
            "                rp.RepositoryID, " +
            "                rp1.RepositoryCode, " +
            "                mg.MinimumStock, " +
            "                mg.MaterialGoodsType, " +
            "                case " +
            "                    when rp.MainUnitID is not null then sum(COALESCE(rp.MainIWQuantity, 0)) - " +
            "                                                        sum(COALESCE(rp.MainOWQuantity, 0)) " +
            "                    else sum(COALESCE(rp.IWQuantity, 0)) - sum(COALESCE(rp.OWQuantity, 0)) end as materialGoodsInStock " +
            "         from RepositoryLedger rp " +
            "                  left join MaterialGoods mg on mg.ID = rp.MaterialGoodsID " +
            "                  left join Repository rp1 on rp1.ID = rp.RepositoryID " +
            "         where rp.MaterialGoodsID in :MaterialGoodsID " +
            "           and rp.RepositoryID in :repositoryID ");
        if (!Strings.isNullOrEmpty(postedDate)) {
            sql.append("           and rp.PostedDate <= :postedDate ");
            params.put("postedDate", postedDate);
        }
        sql.append(" group by rp.materialGoodsCode, rp.MaterialGoodsID, rp.RepositoryID, rp1.RepositoryCode, mg.MinimumStock, " +
            "                  mg.MaterialGoodsType, " +
            "                  rp.MainUnitID) aa " +
            "group by aa.materialGoodsCode, aa.MaterialGoodsID, aa.RepositoryID, aa.RepositoryCode, aa.MinimumStock, " +
            "         aa.MaterialGoodsType " +
            "order by aa.MaterialGoodsCode");
        params.put("MaterialGoodsID", materialGoodsIDs);
        params.put("repositoryID", repositoryIDs);
        Query query = entityManager.createNativeQuery(sql.toString(), "MGForPPOrderConvertQuantityDTO");
        setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<ObjectDTO> getIDAndNameByIDS(List<UUID> materialGoodsIDs) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select mg.id as ID, mg.MaterialGoodsCode as name from MaterialGoods mg where mg.id in :ids");
        params.put("ids", materialGoodsIDs);
        Query query = entityManager.createNativeQuery(sql.toString(), "ObjectDTO");
        setParams(query, params);
        List<ObjectDTO> objectDTOS = query.getResultList();
        return objectDTOS;
    }
    public Page<MaterialGoods> pageableAllMaterialGood(Pageable pageable, List<UUID> org) {
        StringBuilder sql = new StringBuilder();
        List<MaterialGoods> materialGoods = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM MaterialGoods WHERE 1 = 1 ");
        if (org != null) {
            sql.append("AND CompanyID in :org ");
            params.put("org", org);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + "order by MaterialGoodsCode"
                , MaterialGoods.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            materialGoods = query.getResultList();

        }
        return new PageImpl<>(materialGoods, pageable, total.longValue());
    }

    @Override
    public List<MaterialGoodsDTO> findAllForDTOSimilarBranch(List<UUID> allCompanyByCompanyIdAndCode, Boolean similarBranch, Boolean checkShared) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select a.id as id, a.materialGoodsCode as materialGoodsCode, a.materialGoodsName as materialGoodsName, ");
        sql.append("a.unitID as  unitID, a.repositoryID as  repositoryID, ");
        sql.append(" a.materialGoodsType as  materialGoodsType, a.materialGoodsCategoryID as  materialGoodsCategoryID ");
        sql.append("from materialgoods a where isActive = 1 ");

        if (checkShared != null && checkShared) {
            if (similarBranch != null && similarBranch) {
                sql.append("  and CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID in :org) OR (ParentID in :org AND AccType = 0 AND UnitType = 1)) ");

            } else {
                sql.append(" and CompanyID in :org ");
            }
        } else {
            sql.append(" and CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID in :org) OR (ParentID in :org AND UnitType = 1)) ");
        }
        params.put("org", allCompanyByCompanyIdAndCode);
        sql.append(" order by materialGoodsCode, materialGoodsName ");
        Query query = entityManager.createNativeQuery(sql.toString(), "MaterialGoodsDTOReport");
        setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<MaterialGoodsDTO> findAllForPPService() {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        sql.append("from materialgoods mg ");
        sql.append("left join Unit u on u.ID = mg.UnitID ");
        sql.append("left join MaterialGoodsPurchasePrice mgp on mg.ID = mgp.MaterialGoodsID ");

        StringBuilder selectSql = new StringBuilder();

        selectSql.append("mg.id as id, ");
        selectSql.append("mg.materialGoodsCode as materialGoodsCode, ");
        selectSql.append("mg.materialGoodsName as materialGoodsName, ");
        selectSql.append("u.id as unitID, ");
        selectSql.append("u.UnitName as unitName, ");
        selectSql.append("mgp.UnitPrice, ");
        selectSql.append("mg.materialGoodsType ");
        Query query = entityManager.createNativeQuery("SELECT " + selectSql + sql.toString(), "MaterialGoodsDTO");
        setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<MaterialGoodsDTO> findAllForPPInvoice() {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        sql.append("from materialgoods a ");
        sql.append(" left join RepositoryLedger b on a.ID = b.MaterialGoodsID ");
        StringBuilder selectSql = new StringBuilder();

        selectSql.append("a.id as id, ");
        selectSql.append("a.materialGoodsCode as materialGoodsCode, ");
        selectSql.append("a.materialGoodsName as materialGoodsName, ");
        selectSql.append("a.repositoryID as  repositoryID, ");
        selectSql.append("b.IWQuantity - b.OWQuantity as materialGoodsInStock ");
        Query query = entityManager.createNativeQuery("SELECT " + selectSql + sql.toString(), "MaterialGoodsPPInvoiceDTO");
        setParams(query, params);
        return query.getResultList();
    }

    public Page<MGForPPOrderConvertDTO> getMaterialGoodsForCombobox(UUID companyID) {
        Map<String, Object> params = new HashMap<>();
        List<MGForPPOrderConvertDTO> arr = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" a.id                           id, " +
            "       a.CompanyID                    companyID, " +
            "       a.materialGoodsCode            materialGoodsCode, " +
            "       a.materialGoodsName            materialGoodsName, " +
            "       b.IWQuantity - b.OWQuantity as materialGoodsInStock, " +
            "       a.RepositoryID                 repositoryID, " +
            "       a.UnitID                 unitID " +
            " from MaterialGoods a " +
            "         left join RepositoryLedger b on a.ID = b.MaterialGoodsID " +
            "where a.CompanyID = :companyId");
        params.put("companyId", companyID);
        Query query = entityManager.createNativeQuery("SELECT "+ sql.toString(), "MGForPPOrderConvertDTO");
        setParams(query, params);
        arr = query.getResultList();
        return new PageImpl<>(arr);
    }

    public List<MGForPPOrderConvertDTO> getMaterialGoodsForCombobox1(List<UUID> companyID, List<Integer> materialsGoodsType ) {
        Map<String, Object> params = new HashMap<>();
        List<MGForPPOrderConvertDTO> arr = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.id                                id, " +
            "       a.CompanyID                         companyID, " +
            "       a.materialGoodsCode                 materialGoodsCode, " +
            "       a.materialGoodsName                 materialGoodsName, " +
            "       a.MaterialGoodsType                 materialGoodsType," +
            "       a.MinimumStock                      minimumStock," +
            "       case " +
            "           when b.MainUnitID is not null then sum(COALESCE(MainIWQuantity, 0)) - sum(COALESCE(MainOWQuantity, 0)) " +
            "           else sum(COALESCE(IWQuantity, 0)) - sum(COALESCE(OWQuantity, 0)) end as materialGoodsInStock, " +
            "       a.RepositoryID                      repositoryID, " +
            "       a.UnitID                            unitID, " +
            "       a.fixedSalePrice                            fixedSalePrice, " +
            "       a.salePrice1                            salePrice1, " +
            "       a.salePrice2                            salePrice2, " +
            "       a.salePrice3                            salePrice3, " +
            "       a.ExpenseAccount, " +
            "       a.VATTaxRate as vatTaxRate, " +
            "       a.ReponsitoryAccount                    reponsitoryAccount, " +
            "       a.RevenueAccount                        revenueAccount, " +
            "       a.PurchaseDiscountRate                  PurchaseDiscountRate, " +
            "       a.ImportTaxRate                         importTaxRate, " +
            "       a.saleDiscountRate                        saleDiscountRate, " +
            "       a.exportTaxRate                        exportTaxRate, " +
            "       a.careerGroupID                        careerGroupID, " +
            "       a.isFollow                        isFollow " +
            "from MaterialGoods a " +
            "         left join RepositoryLedger b on a.ID = b.MaterialGoodsID " +
            "where a.CompanyID in :companyId and a.IsActive = 1 ");
        params.put("companyId", companyID);
        if (materialsGoodsType != null) {
            sql.append("and a.MaterialGoodsType in :materialsGoodsType ");
            params.put("materialsGoodsType", materialsGoodsType);
        }
        sql.append(" group by a.id, a.CompanyID, " +
            "         a.materialGoodsCode, " +
            "         a.materialGoodsName, " +
            "         b.MainUnitID, " +
            "         a.RepositoryID, " +
            "         a.MaterialGoodsType , " +
            "         a.UnitID, " +
            "         a.fixedSalePrice, " +
            "         a.salePrice1, " +
            "         a.salePrice2, " +
            "         a.salePrice3, " +
            "         a.ExpenseAccount, " +
            "         a.MinimumStock, " +
            "         a.VATTaxRate, " +
            "         a.ReponsitoryAccount, " +
            "         a.RevenueAccount, " +
            "         a.PurchaseDiscountRate, " +
            "         a.importTaxRate, " +
            "         a.saleDiscountRate, " +
            "         a.exportTaxRate, " +
            "         a.careerGroupID, " +
            "         a.isFollow ");
        Query query = entityManager.createNativeQuery("select av.id, " +
            "       av.CompanyID                 companyID, " +
            "       av.materialGoodsCode         materialGoodsCode, " +
            "       av.materialGoodsName         materialGoodsName, " +
            "       av.MaterialGoodsType         materialGoodsType, " +
            "       av.MinimumStock              minimumStock, " +
            "       sum(av.materialGoodsInStock) materialGoodsInStock, " +
            "       av.RepositoryID              repositoryID, " +
            "       av.UnitID                    unitID, " +
            "       av.fixedSalePrice            fixedSalePrice, " +
            "       av.salePrice1                salePrice1, " +
            "       av.salePrice2                salePrice2, " +
            "       av.salePrice3                salePrice3, " +
            "       av.ExpenseAccount, " +
            "       av.VATTaxRate as             vatTaxRate," +
            "       av.reponsitoryAccount        reponsitoryAccount," +
            "       av.RevenueAccount            RevenueAccount, " +
            "       av.PurchaseDiscountRate      PurchaseDiscountRate, " +
            "       av.ImportTaxRate             ImportTaxRate, " +
            "       av.saleDiscountRate                        saleDiscountRate, " +
            "       av.exportTaxRate                        exportTaxRate, " +
            "       av.careerGroupID                        careerGroupID, " +
            "       av.isFollow                        isFollow " +
            " from (" +
            sql.toString() + " ) av group by av.id, " +
            "         av.CompanyID, " +
            "         av.materialGoodsCode, " +
            "         av.materialGoodsName, " +
            "         av.MaterialGoodsType, " +
            "         av.MinimumStock, " +
            "         av.RepositoryID, " +
            "         av.UnitID, " +
            "         av.fixedSalePrice, " +
            "         av.salePrice1, " +
            "         av.salePrice2, " +
            "         av.salePrice3, " +
            "         av.ExpenseAccount, " +
            "         av.VATTaxRate, " +
            "         av.ReponsitoryAccount, " +
            "         av.RevenueAccount, " +
            "         av.PurchaseDiscountRate, " +
            "         av.ImportTaxRate, " +
            "         av.saleDiscountRate, " +
            "         av.exportTaxRate, " +
            "         av.careerGroupID, " +
            "         av.isFollow " +
            "order by av.materialGoodsCode ", "MGForPPOrderConvertDTO");
        setParams(query, params);
        arr = query.getResultList();
        return arr;
    }

    @Override
    public Page<MaterialGoods> getAllByCompanyID(Pageable pageable, UUID orgID) {
        StringBuilder sql = new StringBuilder();
        List<MaterialGoods> materialGoods = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM MaterialGoods WHERE 1 = 1 ");
        if (orgID != null) {
            sql.append(" and CompanyID = :orgID ");
            params.put("orgID", orgID);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " ORDER BY MaterialGoodsCode", MaterialGoods.class);
            setParamsWithPageable(query, params, pageable, total);
            materialGoods = query.getResultList();
        }
        return new PageImpl<>(materialGoods, pageable, total.longValue());
    }

    @Override
    public Page<MaterialGoods> getAllByListCompany(Pageable pageable, List<UUID> listCompanyID) {
        StringBuilder sql = new StringBuilder();
        List<MaterialGoods> materialGoods = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM materialGoods WHERE 1 = 1 ");
        if (listCompanyID.size() > 0) {
            sql.append(" AND CompanyID in :org ");
            params.put("org", listCompanyID);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " order by MaterialGoodsCode"
                , MaterialGoods.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            materialGoods = query.getResultList();
        }
        return new PageImpl<>(materialGoods, pageable, total.longValue());
    }

    @Override
    public Page<MaterialGoods> pageableAllMaterialGoods(Pageable pageable , UUID org) {
        StringBuilder sql = new StringBuilder();
        List<MaterialGoods> materialGoods = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM materialGoods WHERE 1 = 1 ");
        if (org != null) {
            sql.append("AND CompanyID = :org ");
            params.put("org", org);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " order by MaterialGoodsCode"
                , MaterialGoods.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            materialGoods = query.getResultList();
        }
        return new PageImpl<>(materialGoods, pageable, total.longValue());
    }

    @Override
    public Page<MaterialGoods> findVoucherByMaterialGoodsID(Pageable pageable, UUID id) {
        StringBuilder sql = new StringBuilder();
        List<MaterialGoods> materialGoods = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM MaterialGoods WHERE 1 = 1 ");
        if (id != null) {
            sql.append("AND id = :id ");
            params.put("id", id);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString(), MaterialGoods.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            materialGoods = query.getResultList();
        }
        return new PageImpl<>(materialGoods, pageable, total.longValue());
    }

    @Override
    public List<MaterialGoods> insertBulk(List<MaterialGoods> materialGoodss) {
        final List<MaterialGoods> savedEntities = new ArrayList<>(materialGoodss.size());
        int i = 0;
        for (MaterialGoods materialGoods : materialGoodss) {
            if (save(entityManager, materialGoods) == 1) {
                savedEntities.add(materialGoods);
            }
            i++;
            if (i > vn.softdreams.ebweb.config.Constants.BATCH_SIZE) {
                entityManager.flush();
                entityManager.clear();
                i = 0;
            }
        }
        if (i > 0) {
            entityManager.flush();
            entityManager.clear();
        }

        return savedEntities;
    }

    private int save(EntityManager em, MaterialGoods hisPsTbbDtl) {
        if (hisPsTbbDtl.getId() != null) {
            em.merge(hisPsTbbDtl);
        } else {
            em.persist(hisPsTbbDtl);
        }
        return 1;
    }
}
