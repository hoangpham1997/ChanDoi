package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.ContractState;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the ContractState entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContractStateRepository extends JpaRepository<ContractState, Long> {

}
