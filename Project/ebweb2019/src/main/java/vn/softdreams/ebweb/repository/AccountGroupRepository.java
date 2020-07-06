package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.AccountGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the AccountGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountGroupRepository extends JpaRepository<AccountGroup, String> {

    @Query(value = "select * from AccountGroup ORDER BY ID", nativeQuery = true)
    List<AccountGroup> findAllByIsActive();
}
