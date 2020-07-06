package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.Query;
import vn.softdreams.ebweb.domain.EbAuthority;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for the EbAuthority entity.
 */
public interface AuthorityRepository extends JpaRepository<EbAuthority, UUID> {
    @Query(value = "select * from EbAuthority a where a.name = ?1", nativeQuery = true)
    Optional<EbAuthority> findOneByName(String name);
}
