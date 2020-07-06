package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.TIIncrementDetailRefVoucher;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the TIIncrementDetailRefVoucher entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TIIncrementDetailRefVoucherRepository extends JpaRepository<TIIncrementDetailRefVoucher, UUID> {

}
