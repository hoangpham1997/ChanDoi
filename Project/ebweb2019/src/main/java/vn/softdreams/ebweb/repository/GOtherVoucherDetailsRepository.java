package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.GOtherVoucherDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.service.dto.PPDiscountReturnDetailsReportConvertDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the GOtherVoucherDetails entity.
 */
@SuppressWarnings("unused")
@Repository

public interface GOtherVoucherDetailsRepository extends JpaRepository<GOtherVoucherDetails, UUID>, GOtherVoucherDetailsRepositoryCustom {
    @Query(value = "select * from GOtherVoucherDetail where GOtherVoucherID = ?1 order by OrderPriority ", nativeQuery = true)
    List<GOtherVoucherDetails> findAllByGOtherVoucherID(UUID id);

    /**
     * @param id
     * @return
     * @author congnd
     */
    List<GOtherVoucherDetails> findAllBygOtherVoucherIDOrderByOrderPriorityAsc(UUID id);

}
