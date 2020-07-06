package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.TIAllocationAllocated;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the TIAllocationAllocated entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TIAllocationAllocatedRepository extends JpaRepository<TIAllocationAllocated, UUID> {

}
