package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import vn.softdreams.ebweb.config.Constants;
import vn.softdreams.ebweb.domain.EbUserGroup;
import vn.softdreams.ebweb.domain.EbUserPackage;
import vn.softdreams.ebweb.domain.Unit;
import vn.softdreams.ebweb.domain.User;
import vn.softdreams.ebweb.repository.UserRepositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.EInvoice.SDS.KeyInvoiceNo;
import vn.softdreams.ebweb.service.dto.EbLogHistoryDTO;
import vn.softdreams.ebweb.service.dto.EbUserGroupDTO;
import vn.softdreams.ebweb.service.dto.EbUserOrganizationUnitDTO;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.UserSearchDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

public class UserRepositoryImpl implements UserRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    JdbcTemplate jdbcTemplate;

//    @Override
////    public void updateAfterUserEbGroup(List<EbUserGroupDTO> ebUserGroupDTOs) {
////        String sql = "INSERT INTO EbUserGroup(ID, GroupID, CompanyID, UserID, WorkingOnBook) Values(?, ?, ?, ?, ?)";
////        int[][] ints = jdbcTemplate.batchUpdate(sql, ebUserGroupDTOs, Constants.BATCH_SIZE, (ps, detail) -> {
////            ps.setString(1, Common.revertUUID(detail.getId()).toString());
////            ps.setString(2, Common.revertUUID(detail.getGroupID()).toString());
////            ps.setString(3, Common.revertUUID(detail.getCompanyID()).toString());
////            ps.setLong(4, detail.getUserID());
////            ps.setInt(5, detail.getWorkingOnBook());
////        });
////    }

    @Override
    public void insertUserEbGroup(List<EbUserGroupDTO> ebUserGroupDTOs) {
        String sql = "INSERT INTO EbUserGroup(GroupID, CompanyID, UserID, WorkingOnBook) Values(?, ?, ?, ?)";
        int[][] ints = jdbcTemplate.batchUpdate(sql, ebUserGroupDTOs, Constants.BATCH_SIZE, (ps, detail) -> {
//            ps.setString(1, Common.revertUUID(detail.getId()).toString());
            ps.setString(1, Common.revertUUID(detail.getGroupID()).toString());
            if (detail.getCompanyID() != null) {
                ps.setString(2, Common.revertUUID(detail.getCompanyID()).toString());
            } else {
                ps.setNull(2, Types.NVARCHAR);
            }
            ps.setLong(3, detail.getUserID());
            if (detail.getWorkingOnBook() != null) {
                ps.setInt(4, detail.getWorkingOnBook());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
        });
    }

    @Override
    public void insertEbUserOrganizationUnit(List<EbUserOrganizationUnitDTO> ebUserOrganizationUnitDTOs) {
        String sql = "INSERT INTO EbUserOrganizationUnit(UserID, OrgID, CurrentBook) Values(?, ?, ?)";
        int[][] ints = jdbcTemplate.batchUpdate(sql, ebUserOrganizationUnitDTOs, Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setLong(1, detail.getUserID());
            ps.setString(2, Common.revertUUID(detail.getOrgID()).toString());
            ps.setString(3, detail.getCurrentBook());
        });
    }

    @Override
    public void insertEbLogHistory(List<EbLogHistoryDTO> ebLogHistoryDTOS) {
        String sql = "INSERT INTO EbLogHistory(ID, UserID, BeforeChange, AfterChange, ChangedTime) Values(?, ?, ?, ?, ?)";
        int[][] ints = jdbcTemplate.batchUpdate(sql, ebLogHistoryDTOS, Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Common.revertUUID(detail.getId()).toString());
            ps.setLong(2, detail.getUserID());
            ps.setString(3, detail.getBeforeChange());
            ps.setString(4, detail.getAfterChange());
            ps.setDate(5, java.sql.Date.valueOf(java.time.LocalDate.now()));
        });
    }

    @Override
    public Page<UserDTO> findAllByParentIDOrderByLogin(Pageable pageable, User user) {
        StringBuilder sql = new StringBuilder();
        List<UserDTO> users = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
            sql.append(" from EbUser a " +
                " left join EbUserPackage b on b.UserId = a.id " +
                " left join EbOrganizationUnit c on c.ID = b.CompanyId " +
                " left join EbPackage d on d.ID =  b.PackageId " +
                " where a.parentID is null ");
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT a.id, a.login, a.full_name fullName, a.job, a.email, a.mobile_phone mobilePhone, b.status, b.PackageId, b.CompanyId, c.OrganizationUnitName, d.PackageName, d.isTotalPackage "
                + sql.toString() + " order by login ", "UserDTO");
            Common.setParamsWithPageable(query, params, pageable, total);
            users = query.getResultList();
        }
        return new PageImpl<>(users, pageable, total.longValue());
    }

    @Override
    public Page<UserDTO> getListUserSearch(Pageable pageable, UserSearchDTO userSearchDTO) {
        StringBuilder sql = new StringBuilder();
        List<UserDTO> users = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append(" from EbUser a " +
            " left join EbUserPackage b on b.UserId = a.id " +
            " left join EbOrganizationUnit c on c.ID = b.CompanyId " +
            " left join EbPackage d on d.ID =  b.PackageId " +
            " where a.parentID is null ");
        if (userSearchDTO.getLogin() != null) {
            sql.append(" And a.login like :login ");
            params.put("login", "%" + userSearchDTO.getLogin() + "%");
        }
        if (userSearchDTO.getFullName() != null) {
            sql.append(" And a.full_name like :fullName ");
            params.put("fullName", "%" + userSearchDTO.getFullName() + "%");
        }
        if (userSearchDTO.getMobilePhone() != null) {
            sql.append(" And a.mobile_phone like :phone ");
            params.put("phone", "%" + userSearchDTO.getMobilePhone() + "%");
        }
        if (userSearchDTO.getOrganizationUnit() != null && userSearchDTO.getOrganizationUnit().getOrganizationUnitName() != null) {
            sql.append(" And c.OrganizationUnitName like :orgName ");
            params.put("orgName", "%" + userSearchDTO.getOrganizationUnit().getOrganizationUnitName() + "%");
        }
        if (userSearchDTO.getStatus() != null) {
            sql.append(" And b.status like :status ");
            params.put("status", "%" + userSearchDTO.getStatus() + "%");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT a.id, a.login, a.full_name fullName, a.job, a.email, a.mobile_phone mobilePhone, b.status, b.PackageId, b.CompanyId, c.OrganizationUnitName, d.PackageName, d.isTotalPackage "
                + sql.toString() + " order by login ", "UserDTO");
            Common.setParamsWithPageable(query, params, pageable, total);
            users = query.getResultList();
        }
        return new PageImpl<>(users, pageable, total.longValue());
    }

    @Override
    public Page<User> findAllClientByParentIDOrderByLogin(Pageable pageable, User user) {
        StringBuilder sql = new StringBuilder();
        List<User> users = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM EbUser WHERE parentID = :UserID ");
        params.put("UserID", user.getId());
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(*) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " order by login ", User.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            users = query.getResultList();
        }
        return new PageImpl<>(users, pageable, total.longValue());
    }
}
