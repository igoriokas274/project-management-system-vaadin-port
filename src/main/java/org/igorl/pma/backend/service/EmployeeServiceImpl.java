package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Employee;
import org.igorl.pma.backend.repository.CountryRepository;
import org.igorl.pma.backend.repository.DepartmentRepository;
import org.igorl.pma.backend.repository.EmployeeRepository;
import org.igorl.pma.backend.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

    private EmployeeRepository employeeRepository;
    private CountryRepository countryRepository;
    private DepartmentRepository departmentRepository;
    private ProjectRepository projectRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository theEmployeeRepository,
                               CountryRepository theCountryRepository,
                               DepartmentRepository theDepartmentRepository,
                               ProjectRepository theProjectRepository) {
        employeeRepository = theEmployeeRepository;
        countryRepository = theCountryRepository;
        departmentRepository = theDepartmentRepository;
        projectRepository = theProjectRepository;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public List<Employee> findAll(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return employeeRepository.findAll();
        } else {
            return employeeRepository.search(stringFilter);
        }
    }

    public void delete(Employee employee) {
        employeeRepository.delete(employee);
    }

    @Override
    @Transactional
    public void save(Employee theEmployee) {
        employeeRepository.save(theEmployee);
    }

    @Override
    @Transactional
    public void deleteById(long theId) {
        employeeRepository.deleteById(theId);
    }

}
