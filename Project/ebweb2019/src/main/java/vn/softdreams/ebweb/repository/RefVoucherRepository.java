package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.RefVoucher;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;


/**
 * Spring Data  repository for the ViewVoucher entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RefVoucherRepository extends JpaRepository<RefVoucher, Long>, RefVoucherRepositoryCustom {

    void deleteByRefID1(UUID id);

    @Query(value = "select count(*) from RefVoucher where RefID1 = ?1 or RefID2 = ?1", nativeQuery = true)
    long countAllByRefID1OrRefID2(UUID id);

    List<RefVoucher> findAllByRefID1(UUID refId);
    @Query(value = "select * from RefVoucher where RefID1 in ?1 and RefID2 = ?2", nativeQuery = true)
    List<RefVoucher> findAllByRefID1InAndRefID2(Set<UUID> refId1, UUID refId2);
    @Modifying
    @Query(value = "delete from RefVoucher where RefID1 = ?1 or RefID2 = ?1", nativeQuery = true)
    void deleteByRefID1OrRefID2(UUID id);

    void deleteByRefID2(UUID id);

    @Query(value = "select count(*) from RefVoucher where RefID1 in ?1 or RefID2 in ?1", nativeQuery = true)
    int countAllByRefID1sOrRefID2s(List<UUID> listID);

    @Modifying
    @Query(value = "delete from RefVoucher where RefID1 in ?1 or RefID2 in ?1", nativeQuery = true)
    void deleteByRefID1sOrRefID2s(List<UUID> listID);
}
