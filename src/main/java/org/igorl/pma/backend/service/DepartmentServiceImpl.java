package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Department;
import org.igorl.pma.backend.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements IDepartmentService{

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl (DepartmentRepository theDepartmentRepository) {
        departmentRepository = theDepartmentRepository;
    }

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public List<Department> findAll(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return departmentRepository.findAll();
        } else {
            return departmentRepository.search(stringFilter);
        }
    }

    public void delete(Department department) {
        departmentRepository.delete(department);
    }

    public List<Department> findAll(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return departmentRepository.findAll();
        } else {
            return departmentRepository.search(stringFilter);
        }
    }

    @Override
    public Department findById(long theId) {

        Optional<Department> result = departmentRepository.findById(theId);
        Department theDepartment;
        if (result.isPresent()) theDepartment = result.get();
        else {
            throw new RuntimeException("Did not find department id " + theId);
        }
        return theDepartment;
    }

    @Override
    @Transactional
    public void save(Department theDepartment) {
        departmentRepository.save(theDepartment);
    }

    @Override
    @Transactional
    public void deleteById(long theId) {
        departmentRepository.deleteById(theId);
    }
}
