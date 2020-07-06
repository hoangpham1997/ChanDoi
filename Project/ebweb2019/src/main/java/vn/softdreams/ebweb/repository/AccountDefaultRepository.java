package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.AccountDefault;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the AccountDefault entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountDefaultRepository extends JpaRepository<AccountDefault, UUID>,AccountDefaultRepositoryCustom {

    @Override
    List<AccountDefault> findAll();

    @Query(value = "select * from AccountDefault b where b.CompanyID = ?2 AND TypeID = ?1", nativeQuery = true)
    List<AccountDefault> findByTypeID(Integer typeID, UUID companyID);
}
