package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.TIAllocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the TIAllocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TIAllocationRepository extends JpaRepository<TIAllocation, UUID>, TIALLocationRespositoryCustom {

    @Query(value = "select count(*) from ToolLedger where ToolsID in ?1 and TypeID in (550, 560,530) and Date >= ?2", nativeQuery = true)
    Long countToolsAllocation(List<UUID> uuidList, LocalDate date);

    @Query(value = "select top 1 gv.PostedDate " +
        "        from TIAllocationDetail gvde " +
        "                left join TIAllocation gv on gv.ID = gvde.TIAllocationID " +
        "        where gvde.TIAllocationID in " +
        "             (select gvde1.TIAllocationID from TIAllocationDetail gvde1 where gvde1.TIAllocationID = ?1) " +
        "             and gv.TypeLedger = ?2 " +
        "        order by gv.PostedDate desc", nativeQuery = true)
    Instant getMaxMonth(UUID id, String currentBook);
}
