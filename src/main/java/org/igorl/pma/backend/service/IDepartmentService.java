package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Department;

import java.util.List;

public interface IDepartmentService {
    List<Department> findAll();
    Department findById(long theId);
    void save(Department theDepartment);
    void deleteById(long theId);
}
