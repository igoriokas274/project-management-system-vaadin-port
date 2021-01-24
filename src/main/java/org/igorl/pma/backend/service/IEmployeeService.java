package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Employee;

import java.util.List;

public interface IEmployeeService {

    List<Employee> findAll();
    void save(Employee theEmployee);
    void deleteById(long theId);
}
