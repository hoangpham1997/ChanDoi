package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.Type;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the Type entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeRepository extends JpaRepository<Type, Integer> {
    @Override
    List<Type> findAll();

    List<Type> findAllByTypeGroupID(Integer groupId);

    @Query(value = "select * from Type b ORDER BY ID", nativeQuery = true)
    List<Type> findAllByIsActive();

    @Query(value = "select TypeName from Type  WHERE ID = ?1 ", nativeQuery = true)
    String findTypeNameByTypeID(Integer type);
}
