package org.igorl.pma.backend.service;

import org.igorl.pma.backend.entity.Department;
import org.igorl.pma.backend.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public List<Department> findAll(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return departmentRepository.findAll();
        } else {
            return departmentRepository.search(stringFilter);
        }
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
