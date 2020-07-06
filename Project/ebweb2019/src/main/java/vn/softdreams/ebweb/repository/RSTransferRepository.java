package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.RSTransfer;
import vn.softdreams.ebweb.web.rest.dto.RSInwardOutwardSearchDTO;

import java.util.UUID;


/**
 * Spring Data  repository for the RSTranfer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RSTransferRepository extends JpaRepository<RSTransfer, UUID>, RSTransferRepositoryCustom {

}
