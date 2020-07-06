package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.PPOrderDetail;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the PPOrderDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PporderdetailRepository extends JpaRepository<PPOrderDetail, UUID> {
    @Modifying
    @Query(nativeQuery = true, value = "update  PPOrderDetail set QuantityReceipt = IIF(QuantityReceipt is null, ?2, QuantityReceipt + ?2) where id = ?1")
    void updateQuantityReceipt(UUID id, BigDecimal quantityReceipt);
    @Modifying
    @Query(nativeQuery = true, value = "update PPOrderDetail  set QuantityReceipt = (\n" +
        "select (coalesce((select sum(Quantity)\n" +
        "from PPInvoiceDetail\n" +
        "where PPInvoiceDetail.PPOrderDetailID = aa.oderID), 0) - aa.quantityMinus) from  (\n" +
        "select ppo.id                           oderID,\n" +
        "       sum(COALESCE(ppdtl.Quantity, 0)) quantityMinus\n" +
        "from PPDiscountReturnDetail ppdtl\n" +
        "         join PPInvoiceDetail ppi on ppi.ID = ppdtl.PPInvoiceDetailID\n" +
        "         join PPOrderDetail ppo on ppo.ID = ppi.PPOrderDetailID\n" +
        "where ppo.id is not null\n" +
        "group by ppo.id ) aa\n" +
        "where aa.oderID = id) where id in ?1")
    void updateQuantityReceiptByPPOderOrPPDiscount(List<UUID> id);


}
