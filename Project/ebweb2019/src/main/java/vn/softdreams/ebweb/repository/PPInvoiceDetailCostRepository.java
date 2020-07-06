package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.PPInvoiceDetailCost;
import vn.softdreams.ebweb.web.rest.dto.PPInvoiceDetailCostDTO;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data  repository for the PPInvoiceDetailCost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PPInvoiceDetailCostRepository extends JpaRepository<PPInvoiceDetailCost, UUID>, PPInvoiceDetailCostRepositoryCustom {
    List<PPInvoiceDetailCost> findAllByRefID(UUID refId);

    void deleteByRefID(UUID id);

    boolean existsByPpServiceID(UUID uuid);

//    @Modifying
//    @Query(nativeQuery = true, value = "update ppinvoicedetailcost set ppserviceid = null where ppserviceid = ?1")
//    void removePPServiceByPpServiceID(UUID uuid);

//    @Modifying
//    @Query(nativeQuery = true, value = "update ppinvoicedetailcost set ppserviceid = null where ppserviceid IN ?1")
//    void removeListPPServiceByPpServiceID(List<UUID> uuid);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE ppinvoicedetailcost set ppserviceid = null where ppserviceid = ?1")
    void cleanPPServiceId(UUID id);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE ppinvoicedetailcost set ppserviceid = null where ppserviceid in ?1")
    void cleanPPServiceIdByListID(List<UUID> uuidList);

    @Modifying
    @Query(value = "DELETE FROM PPInvoiceDetailCost WHERE RefID IN ?1 ", nativeQuery = true)
    void deleteListRefID(List<UUID> uuidList);

    @Query(value = "SELECT COUNT(*) FROM PPInvoiceDetailCost WHERE PPServiceID IN ?1", nativeQuery = true)
    Integer existsInPpServiceID(List<UUID> uuid);

}
