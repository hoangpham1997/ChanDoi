package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.PrepaidExpenseVoucher;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the PrepaidExpenseVoucher entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrepaidExpenseVoucherRepository extends JpaRepository<PrepaidExpenseVoucher, UUID> {
    void deleteAllByRefID(UUID ref);
}
