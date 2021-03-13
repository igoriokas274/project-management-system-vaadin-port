package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Department;

import java.util.List;

public interface IDepartmentService {
    List<Department> findAll();
    List<Department> findAll(String stringFilter);
    void save(Department theDepartment);
    void deleteById(long theId);
}
