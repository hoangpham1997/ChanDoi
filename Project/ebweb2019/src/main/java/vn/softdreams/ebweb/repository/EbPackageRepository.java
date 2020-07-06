package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.EbPackage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the EbPackage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EbPackageRepository extends JpaRepository<EbPackage, UUID>, EbPackageRepositoryCustom {

    @Query(value = "select * from EbPackage where ID = (select EbUserPackage.PackageId from EbUserPackage " +
        "    where EbUserPackage.UserId = ?1 and EbUserPackage.CompanyId = ?2 " +
        "    )", nativeQuery = true)
    EbPackage findOneByOrgIdAndUserId(Long id, UUID org);

    @Query(value = "select COUNT (1) from EbPackage where PackageCode = ?1", nativeQuery = true)
    Long countPackageCode(String code);

    @Query(value = "select * from EbPackage where PackageCode = ?1", nativeQuery = true)
    EbPackage findOneByPackageCode(String packageCode);

    @Query(value = "select * from EbPackage where ID = ?1", nativeQuery = true)
    EbPackage findOneByID(UUID id);

    @Query(value = "select a.*, b.IsTotalPackage from EbPackage a left join EbUserPackage b on a.ID = b.PackageId " +
        " where a.ID = (select EbUserPackage.PackageId from EbUserPackage where EbUserPackage.UserId = ?1 and EbUserPackage.CompanyId = ?2 )", nativeQuery = true)
    EbPackage findOneByOrgIdAndUserIdCustom(Long userId, UUID org);

    @Modifying
    @Query(value = "Insert into EbPackage(ID, PackageCode, Description, LimitedVoucher, ExpiredTime, Status) " +
        "values (?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    void insertEbPackageCrm(UUID iD, String packageCode, String description, int limitedVoucher, int expireTime, boolean status);

    @Query(value = "select COUNT(*) from EbPackage where ID = ?1 AND PackageName LIKE '%DEMO'", nativeQuery = true)
    Integer checkPackageDemo(UUID id);

    @Query(value = "select ebp.* from EbPackage ebp " +
        " left join  EbUserPackage EUP on ebp.id = EUP.PackageId " +
        " left join EbUser EU on EUP.UserId = EU.id " +
        " where EU.login = ?1 ", nativeQuery = true)
    EbPackage findEbPackageByUser(String userLogin);

    @Query(value = "SELECT a.ComType FROM EbUserPackage b LEFT JOIN EbPackage a ON a.ID = b.PackageId WHERE b.CompanyID = ?1", nativeQuery = true)
    Integer findComTypeByOrgID(UUID id);
}
