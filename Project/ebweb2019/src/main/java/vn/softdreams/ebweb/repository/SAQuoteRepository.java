package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.SAQuote;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.SAQuoteDetails;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the SAQuote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SAQuoteRepository extends JpaRepository<SAQuote, UUID>, SAQuoteRepositoryCustom {
    @Query(value = "select * from SAQuote a where a.ID = ?1", nativeQuery = true)
    Optional<SAQuote> findOneById(UUID id);

    @Modifying
    @Query(value = "DELETE SAQuote where ID = ?1", nativeQuery = true)
    void deleteOneById(UUID id);

    @Query(value = "select count(*) from  SAInvoiceDetail where SAQuoteID = ?1", nativeQuery = true)
    int checkRelateVoucherSAInvoice(UUID id);

    @Query(value = "select distinct a.SAQuoteID from (select SAQuoteID from SAInvoiceDetail where SAQuoteID in ?1) a ", nativeQuery = true)
    List<String> getIDRef(List<UUID> uuids);

    @Modifying
    @Query(value = "delete SAQuote where id in ?1 ;" +
        "delete RefVoucher where RefID1 in ?1 or RefID2 in ?1 ;" +
        "UPDATE SAInvoiceDetail set SAQuoteDetailID = null, SAQuoteID = null where SAQuoteID in ?1 ;", nativeQuery = true)
    void deleteByListID(List<UUID> uuids);
}
