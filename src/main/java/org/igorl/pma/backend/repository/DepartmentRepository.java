package org.igorl.pma.backend.repository;

import org.igorl.pma.backend.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("select d from Department d where lower(d.departmentName) like lower(concat('%', :searchTerm, '%'))")
    List<Department> search(@Param("searchTerm") String searchTerm);
}
