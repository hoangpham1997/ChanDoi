package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.TITransfer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the TITransfer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TITransferRepository extends JpaRepository<TITransfer, UUID> , TITransferRepositoryCustom {


    @Query(value = "select * from TITransfer where ID = '2FA9CDA9-EABD-49DA-A0B9-7219D756BCE3'", nativeQuery = true)
    Optional<TITransfer> findByTITransfer();
}
