package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.PPOrder;
import vn.softdreams.ebweb.web.rest.dto.PPOrderSearchDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the PPOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PporderRepository extends JpaRepository<PPOrder, UUID>, PporderRepositoryCustom {
    @Query(nativeQuery = true,
            value = "select ppo.no from PPService pp left join PPServiceDetail ppd on pp.id = ppd.PPServiceID" +
            " left join PPOrder ppo on ppd.PPOrderID = ppo.ID where pp.ID = ?1")
    List<String> findAllNoByPPServiceId(UUID ppServiceID);

    @Query(nativeQuery = true, value = "select pp.no from PPOrder pp where pp.ID = ?1")
    String findNoById(UUID id);

    @Query(nativeQuery = true, value = "SELECT SUM(a.TotalAmount) " +
        " FROM (SELECT (CASE " +
        " WHEN a.totalAmount != a.totalAmountOriginal THEN a.totalAmount - a.totalDiscountAmount + a.totalVATAmount " +
        " ELSE a.totalAmountOriginal - a.totalDiscountAmountOriginal + a.totalVATAmountOriginal END) totalAmount " +
        " FROM PPOrder a WHERE CompanyID = ?1) a")
    Number findTotalSum(UUID org);
}
