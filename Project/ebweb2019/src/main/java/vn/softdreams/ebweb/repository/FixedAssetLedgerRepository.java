package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.FixedAssetLedger;
import vn.softdreams.ebweb.domain.Toolledger;

import java.util.UUID;


/**
 * Spring Data  repository for the Toolledger entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FixedAssetLedgerRepository extends JpaRepository<FixedAssetLedger, UUID>, FixedAssetLedgerRepositoryCustom {

}
