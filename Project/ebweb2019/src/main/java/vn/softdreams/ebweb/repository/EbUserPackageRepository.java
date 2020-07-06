package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.EbUserPackage;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the AccountDefault entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EbUserPackageRepository extends JpaRepository<EbUserPackage, UUID> {
    EbUserPackage findOneByUserID(Long userID);

    EbUserPackage findOneById(UUID id);

    @Query(value = "select * from EbUserPackage " +
        " where CompanyId = ?1 ", nativeQuery = true)
    EbUserPackage findOneByCompanyID(UUID companyID);

    @Query(value = "select * from EbUserPackage " +
        " where PackageId = ?1 ", nativeQuery = true)
    EbUserPackage findOneByPackageID(UUID packageID);

    @Modifying
    @Query(value = "Insert into EbUserPackage (Id , UserID, PackageId, CompanyId, status) values (?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
    void insertEbUserPackage(UUID id, Long userId, UUID packageId, UUID orgId, Integer status);

    @Modifying
    @Query(value = "Insert into EbUserPackage (Id , UserID, PackageId, CompanyId, status, IsTotalPackage) values (?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    void insertEbUserPackageCrm(UUID id, Long userId, UUID packageId, UUID orgId, Integer status, Boolean isTotalPackage);

    @Modifying
    @Query(value = "update EbUserPackage set PackageId = ?2, status = ?3, isTotalPackage = ?4 where UserId= ?1", nativeQuery = true)
    void updateEbUserPackage(Long userID, UUID packageID, Integer status, Boolean IsTotalPackage);

    @Modifying
    @Query(value = "update EbUser set activated = 1 where id= ?1", nativeQuery = true)
    void activatedUser(Long userId);

    @Modifying
    @Query(value = "update EbUser set activated = 0 where id= ?1", nativeQuery = true)
    void InActivatedUser(Long userId);

    void deleteEbUserPackageByUserID(Long userID);

    void deleteEbUserPackageByCompanyID(UUID orgId);

    void deleteEbUserPackageByPackageID(UUID packageID);

    @Query(value = "select * from EbUserPackage where CompanyId = (select EbOrganizationUnit.ID from EbOrganizationUnit where TaxCode = ?1)", nativeQuery = true)
    EbUserPackage findOneByTaxCode(String companyTaxCode);

    @Modifying
    @Query(value = "update EbUserPackage set packageID = ?1 where userID= ?2", nativeQuery = true)
    void updateEbUserPackageUpgrade(UUID packageID, Long userID);

    @Query(value = "select * from EbUserPackage " +
        " where UserId = ?1 and CompanyId = ?2 ", nativeQuery = true)
    EbUserPackage findOneByUserIDAndCompanyID(Long id, UUID orgID);

    @Modifying
    @Query(value = "update EbUserPackage set packageID = ?1, status = ?2, isTotalPackage = ?3 " +
        " where id = ?4", nativeQuery = true)
    void updateEbUserPackageForActive(UUID packageID, Integer status, Boolean isTotalPackage, UUID id);

    @Query(value = "SELECT * FROM EbUserPackage WHERE UserId = ?1 " ,nativeQuery = true)
    EbUserPackage findComIDByUserID(Long UserID);

}
