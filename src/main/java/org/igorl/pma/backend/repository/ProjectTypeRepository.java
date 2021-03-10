package org.igorl.pma.backend.repository;

import org.igorl.pma.backend.entity.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectTypeRepository extends JpaRepository<ProjectType, Long> {

    @Query("select p from ProjectType p where lower(p.projectTypeName) like lower(concat('%', :searchTerm, '%'))")
    List<ProjectType> search(@Param("searchTerm") String searchTerm);
}
