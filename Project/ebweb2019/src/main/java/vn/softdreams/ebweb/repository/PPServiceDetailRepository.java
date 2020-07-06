package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.PPInvoiceDetails;
import vn.softdreams.ebweb.domain.PPServiceDetail;

import java.util.List;
import java.util.UUID;

public interface PPServiceDetailRepository extends JpaRepository<PPServiceDetail, UUID>, PPServiceDetailRepositoryCustom {
    @Transactional
    void deleteByPpServiceID(UUID uuid);

    @Transactional
    void deleteByPpServiceIDIn(List<UUID> uuids);

    @Query(nativeQuery = true,
        value = "select ppd.InvoiceNo from PPService pp left join PPServiceDetail ppd on pp.id = ppd.PPServiceID" +
            "  where pp.ID = ?1 group  by ppd.InvoiceNo")
    List<String> findAllInvoiceNoByPPServiceId(UUID id);

    List<PPServiceDetail> findAllByPpServiceIDIn(List<UUID> ppInvoiceList);

}
