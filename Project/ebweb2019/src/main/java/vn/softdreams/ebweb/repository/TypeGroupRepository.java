package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.TypeGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the TypeGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeGroupRepository extends JpaRepository<TypeGroup, Long> {

    List<TypeGroup> findByIdNotIn(List<Long> asList);
}
