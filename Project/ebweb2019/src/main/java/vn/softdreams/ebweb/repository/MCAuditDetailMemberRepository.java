package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MCAuditDetailMember;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the MCAuditDetailMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MCAuditDetailMemberRepository extends JpaRepository<MCAuditDetailMember, UUID> {

}
