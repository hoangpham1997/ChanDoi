package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.EMContract;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.EMContractConvertDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the EMContract entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EMContractRepository extends JpaRepository<EMContract, UUID>,EMContractRepositoryCustom {

    /**
     * add by namnh
     *
     * @return
     */
    @Override
    List<EMContract> findAll();

    List<EMContract> findAllByIsActiveTrue();
    @Query(value = "select id from EMContract where UPPER(NoFBook) = UPPER (?1) and CompanyID = ?2 and isActive = 1", nativeQuery = true)
    UUID findIdByNoFBook(String expenseItemCode, UUID companyId);

    @Query(value = "select id from EMContract where UPPER(NoMBook) = UPPER (?1) and CompanyID = ?2 and isActive = 1", nativeQuery = true)
    UUID findIdByNoMBook(String expenseItemCode, UUID companyId);
}
