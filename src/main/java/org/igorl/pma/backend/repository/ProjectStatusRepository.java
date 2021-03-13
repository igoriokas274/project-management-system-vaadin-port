package org.igorl.pma.backend.repository;

import org.igorl.pma.backend.entity.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectStatusRepository extends JpaRepository<ProjectStatus, Long> {

    @Query("select p from ProjectStatus p where lower(p.projectStatusName) like lower(concat('%', :searchTerm, '%'))")
    List<ProjectStatus> search(@Param("searchTerm") String searchTerm);
}
